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

import static org.sing_group.dreimt.service.spi.query.GeneListsValidationService.MAXIMUM_GENESET_SIZE;
import static org.sing_group.dreimt.service.spi.query.GeneListsValidationService.MINIMUM_GENESET_SIZE;

import java.util.Optional;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.JaccardGeneSetSignatureResultDao;
import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.JaccardResultDao;
import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.JaccardUpDownSignatureResultDao;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardResult;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardUpDownSignatureResult;
import org.sing_group.dreimt.service.query.jaccard.event.DefaultJaccardComputationRequestEvent;
import org.sing_group.dreimt.service.spi.query.GeneListsValidationService;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryOptions;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryService;

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
      !options.getDownGenes().isEmpty() ? this.jaccardUpDownQuery(options) : this.jaccardGeneSetQuery(options);

    DefaultJaccardComputationRequestEvent event =
      new DefaultJaccardComputationRequestEvent(result.getId(), options.getSignatureListingOptions(), options.isOnlyUniverseGenes());

    this.jaccardComputationEvents.fire(event);

    result.setScheduled();

    return result;
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
        options.getSignatureListingOptions().getCellTypeA().orElse(null),
        options.getSignatureListingOptions().getCellSubTypeA().orElse(null),
        options.getSignatureListingOptions().getCellTypeB().orElse(null),
        options.getSignatureListingOptions().getCellSubTypeB().orElse(null),
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
    final JaccardGeneSetSignatureResult result =
      this.jaccardGeneSetDao.create(
        options.getTitle().orElse("Untitled Jaccard GeneSet query"),
        "Jaccard GeneSet query",
        options.getResultUriBuilder(),
        options.isOnlyUniverseGenes(),
        options.getSignatureListingOptions().getCellTypeA().orElse(null),
        options.getSignatureListingOptions().getCellSubTypeA().orElse(null),
        options.getSignatureListingOptions().getCellTypeB().orElse(null),
        options.getSignatureListingOptions().getCellSubTypeB().orElse(null),
        options.getSignatureListingOptions().getExperimentalDesign().orElse(null),
        options.getSignatureListingOptions().getOrganism().orElse(null),
        options.getSignatureListingOptions().getDisease().orElse(null),
        options.getSignatureListingOptions().getSourceDb().orElse(null),
        options.getUpGenes()
      );

    return result;
  }

  @Override
  public Optional<JaccardResult> getResult(String resultId) {
    return this.jaccardResultDao.get(resultId);
  }
}
