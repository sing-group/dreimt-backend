/*-
 * #%L
 * DREIMT - Service
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
 * 			Kevin Troulé, Gonzálo Gómez-López, Fátima Al-Shahrour
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
package org.sing_group.dreimt.service.query.jaccard;

import static org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardResult.getInputGenes;
import static org.sing_group.dreimt.domain.entities.signature.GeneSetType.DOWN;
import static org.sing_group.dreimt.domain.entities.signature.GeneSetType.UP;
import static org.sing_group.dreimt.service.spi.query.GeneListsValidationService.MAXIMUM_GENESET_SIZE;
import static org.sing_group.dreimt.service.spi.query.GeneListsValidationService.MINIMUM_GENESET_SIZE;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.dao.signature.SignatureListingOptions;
import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.JaccardGeneSetSignatureResultDao;
import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.JaccardResultDao;
import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.JaccardUpDownSignatureResultDao;
import org.sing_group.dreimt.domain.dao.spi.signature.SignatureDao;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardComparisonType;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardResult;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardUpDownSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.GeneSetSignature;
import org.sing_group.dreimt.domain.entities.signature.GeneSetType;
import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;
import org.sing_group.dreimt.domain.entities.signature.UpDownSignature;
import org.sing_group.dreimt.service.query.DefaultValueDistribution;
import org.sing_group.dreimt.service.query.DefaultValuesDistribution;
import org.sing_group.dreimt.service.query.jaccard.event.DefaultJaccardComputationRequestEvent;
import org.sing_group.dreimt.service.spi.query.GeneListsValidationService;
import org.sing_group.dreimt.service.spi.query.ValuesDistribution;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryOptions;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryService;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardServiceConfiguration;

@Stateless
@PermitAll
public class DefaultJaccardQueryService implements JaccardQueryService {

  @Inject
  private GeneListsValidationService geneListsValidationService;

  @Inject
  private Event<DefaultJaccardComputationRequestEvent> jaccardComputationEvents;

  @Inject
  private JaccardResultDao jaccardResultDao;

  @Inject
  private JaccardGeneSetSignatureResultDao jaccardGeneSetDao;

  @Inject
  private JaccardUpDownSignatureResultDao jaccardUpDownSignatureDao;

  @Inject
  private SignatureDao signatureDao;

  @Override
  public boolean isValidGeneSet(Set<String> genes, boolean onlyUniverseGenes) {
    return this.geneListsValidationService
      .isValidGeneSet(genes, onlyUniverseGenes, getMinimumGeneSetSize(), getMaximumGeneSetSize());
  }

  @Override
  public int getMinimumGeneSetSize() {
    return MINIMUM_GENESET_SIZE;
  }

  @Override
  public int getMaximumGeneSetSize() {
    return MAXIMUM_GENESET_SIZE;
  }

  @Override
  public JaccardResult jaccardQuery(JaccardQueryOptions options) {
    this.validateGeneListsSizes(options);

    JaccardResult result =
      (options.getDownGenes().isEmpty() || options.getUpGenes().isEmpty()) ? this.jaccardGeneSetQuery(options) : this.jaccardUpDownQuery(options);

    DefaultJaccardComputationRequestEvent event =
      new DefaultJaccardComputationRequestEvent(
        result.getId(), options.getSignatureListingOptions(), createJaccardServiceConfiguration(result, options)
      );

    this.jaccardComputationEvents.fire(event);

    result.setScheduled();

    return result;
  }

  private JaccardServiceConfiguration createJaccardServiceConfiguration(
    JaccardResult result, JaccardQueryOptions options
  ) {
    if (result instanceof JaccardGeneSetSignatureResult) {
      return new DefaultJaccardServiceConfiguration(
        options.isOnlyUniverseGenes(), options.getUpGenes().isEmpty() ? options.getDownGenes() : options.getUpGenes()
      );
    } else if (result instanceof JaccardUpDownSignatureResult) {
      return new DefaultJaccardServiceConfiguration(
        options.isOnlyUniverseGenes(), options.getUpGenes(), options.getDownGenes()
      );
    } else {
      throw new RuntimeException("Unknown JaccardResul type");
    }
  }

  private void validateGeneListsSizes(JaccardQueryOptions options) {
    this.geneListsValidationService.validateGeneListsSizes(
      options.getUpGenes(),
      options.getDownGenes(),
      options.isOnlyUniverseGenes(),
      getMinimumGeneSetSize(),
      getMaximumGeneSetSize()
    );
  }

  private JaccardResult jaccardUpDownQuery(JaccardQueryOptions options) {
    final JaccardUpDownSignatureResult result =
      this.jaccardUpDownSignatureDao.create(
        options.getTitle().orElse("Untitled Jaccard UpDown Signature query"),
        "Jaccard UpDown Signature query",
        options.getResultUriBuilder(),
        options.isOnlyUniverseGenes(),
        options.getSignatureListingOptions().getCellTypeAndSubType1Filter().getType().orElse(null),
        options.getSignatureListingOptions().getCellTypeAndSubType1Filter().getSubType().orElse(null),
        options.getSignatureListingOptions().getCellTypeAndSubType1Filter().getTypeOrSubType().orElse(null),
        options.getSignatureListingOptions().getCellTypeAndSubType2Filter().getType().orElse(null),
        options.getSignatureListingOptions().getCellTypeAndSubType2Filter().getSubType().orElse(null),
        options.getSignatureListingOptions().getCellTypeAndSubType2Filter().getTypeOrSubType().orElse(null),
        options.getSignatureListingOptions().getExperimentalDesign().orElse(null),
        options.getSignatureListingOptions().getOrganism().orElse(null),
        options.getSignatureListingOptions().getDisease().orElse(null),
        options.getSignatureListingOptions().getSourceDb().orElse(null),
        options.getUpGenes(),
        options.getDownGenes()
      );

    return result;
  }

  private JaccardResult jaccardGeneSetQuery(JaccardQueryOptions options) {
    final GeneSetType type = options.getUpGenes().isEmpty() ? DOWN : UP;

    final JaccardGeneSetSignatureResult result =
      this.jaccardGeneSetDao.create(
        options.getTitle().orElse("Untitled Jaccard GeneSet query"),
        "Jaccard GeneSet query",
        options.getResultUriBuilder(),
        options.isOnlyUniverseGenes(),
        options.getSignatureListingOptions().getCellTypeAndSubType1Filter().getType().orElse(null),
        options.getSignatureListingOptions().getCellTypeAndSubType1Filter().getSubType().orElse(null),
        options.getSignatureListingOptions().getCellTypeAndSubType1Filter().getTypeOrSubType().orElse(null),
        options.getSignatureListingOptions().getCellTypeAndSubType2Filter().getType().orElse(null),
        options.getSignatureListingOptions().getCellTypeAndSubType2Filter().getSubType().orElse(null),
        options.getSignatureListingOptions().getCellTypeAndSubType2Filter().getTypeOrSubType().orElse(null),
        options.getSignatureListingOptions().getExperimentalDesign().orElse(null),
        options.getSignatureListingOptions().getOrganism().orElse(null),
        options.getSignatureListingOptions().getDisease().orElse(null),
        options.getSignatureListingOptions().getSourceDb().orElse(null),
        options.getUpGenes().isEmpty() ? options.getDownGenes() : options.getUpGenes(),
        type
      );

    return result;
  }

  @Override
  public Optional<JaccardResult> getResult(String resultId) {
    return this.jaccardResultDao.get(resultId);
  }

  @Override
  public Set<String> jaccardIntersectionQueryGenes(
    String resultId, String signatureName,
    JaccardComparisonType sourceComparisonType, JaccardComparisonType targetComparisonType
  ) {
    JaccardResult jaccardResult =
      this.getResult(resultId).orElseThrow(() -> new IllegalArgumentException("Unknown jaccard result: " + resultId));

    boolean onlyUniverseGenes = jaccardResult.isOnlyUniverseGenes();

    Signature signature =
      this.signatureDao.get(signatureName).orElseThrow(() -> new IllegalArgumentException("Unknown signature: " + signatureName));

    Set<String> sourceComparisonGenes = null;
    Set<String> targetComparisonGenes = null;

    if (jaccardResult instanceof JaccardGeneSetSignatureResult) {
      if (sourceComparisonType.equals(JaccardComparisonType.GENESET)) {
        sourceComparisonGenes = ((JaccardGeneSetSignatureResult) jaccardResult).getGenes(onlyUniverseGenes);
      } else {
        throw new IllegalArgumentException(
          "The sourceComparisonType for a JaccardGeneSetSignatureResult must be " + JaccardComparisonType.GENESET
        );
      }
    } else if (jaccardResult instanceof JaccardUpDownSignatureResult) {
      if (sourceComparisonType.equals(JaccardComparisonType.GENESET)) {
        throw new IllegalArgumentException(
          "The sourceComparisonType for an JaccardUpDownSignatureResult must be " + JaccardComparisonType.SIGNATURE_UP
            + " or " + JaccardComparisonType.SIGNATURE_DOWN
        );
      } else if (sourceComparisonType.equals(JaccardComparisonType.SIGNATURE_UP)) {
        sourceComparisonGenes = ((JaccardUpDownSignatureResult) jaccardResult).getUpGenes(onlyUniverseGenes);
      } else {
        sourceComparisonGenes = ((JaccardUpDownSignatureResult) jaccardResult).getDownGenes(onlyUniverseGenes);
      }
    } else {
      throw new IllegalArgumentException("Unknown JaccardResult type");
    }

    if (signature.getSignatureType().equals(SignatureType.GENESET)) {
      if (targetComparisonType.equals(JaccardComparisonType.GENESET)) {
        targetComparisonGenes = ((GeneSetSignature) signature).getSignatureGenes(onlyUniverseGenes);
      } else {
        throw new IllegalArgumentException(
          "The targetComparisonType for a GeneSetSignature must be " + JaccardComparisonType.GENESET
        );
      }
    } else {
      if (targetComparisonType.equals(JaccardComparisonType.GENESET)) {
        throw new IllegalArgumentException(
          "The targetComparisonType for an UpDownSignature must be " + JaccardComparisonType.SIGNATURE_UP + " or "
            + JaccardComparisonType.SIGNATURE_DOWN
        );
      } else if (targetComparisonType.equals(JaccardComparisonType.SIGNATURE_UP)) {
        targetComparisonGenes = ((UpDownSignature) signature).getUpGenes(onlyUniverseGenes);
      } else {
        targetComparisonGenes = ((UpDownSignature) signature).getDownGenes(onlyUniverseGenes);
      }
    }

    sourceComparisonGenes.retainAll(targetComparisonGenes);

    return sourceComparisonGenes;
  }

  @Override
  public ValuesDistribution cellTypeAndSubTypeDistribution(String resultId) {
    JaccardResult jaccardResult =
      this.getResult(resultId).orElseThrow(() -> new IllegalArgumentException("Unknown jaccard result: " + resultId));

    Set<String> inputGenes = getInputGenes(jaccardResult);

    SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        null, jaccardResult.getCellType1(), jaccardResult.getCellSubType1(),
        jaccardResult.getCellTypeOrSubType1(), jaccardResult.getCellType2(),
        jaccardResult.getCellSubType2(), jaccardResult.getCellTypeOrSubType2(),
        jaccardResult.getExperimentalDesign(), jaccardResult.getOrganism(),
        jaccardResult.getDisease(), jaccardResult.getSignatureSourceDb(),
        null, null, null, null
      );

    Stream<Signature> signatures =
      this.signatureDao.list(signatureListingOptions.withMandatoryGenes(inputGenes));

    Map<String, Integer> cellType = new HashMap<String, Integer>();
    Map<String, Integer> cellSubType = new HashMap<String, Integer>();
    signatures.forEach(s -> {
      Set<String> cellTypes = new HashSet<>(s.getCellTypeA());
      cellTypes.addAll(s.getCellTypeB());

      Set<String> cellSubTypes = new HashSet<>(s.getCellSubTypeA());
      cellSubTypes.addAll(s.getCellSubTypeB());

      cellTypes.forEach(cT -> {
        cellType.put(cT, cellType.getOrDefault(cT, 0) + 1);
      });
      cellSubTypes.forEach(cST -> {
        cellSubType.put(cST, cellSubType.getOrDefault(cST, 0) + 1);
      });
    });

    ValuesDistribution distributions = new DefaultValuesDistribution();
    distributions.addDistribution(new DefaultValueDistribution("cellType", cellType));
    distributions.addDistribution(new DefaultValueDistribution("cellSubType", cellSubType));

    return distributions;
  }
}
