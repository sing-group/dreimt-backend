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
package org.sing_group.dreimt.service.execution.pipeline.jaccard.steps;

import static java.util.Objects.requireNonNull;
import static javax.transaction.Transactional.TxType.REQUIRED;
import static org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardComparisonType.GENESET;
import static org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardComparisonType.SIGNATURE_DOWN;
import static org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardComparisonType.SIGNATURE_UP;
import static org.sing_group.dreimt.service.signature.geneoverlap.GeneOverlapCalculator.geneOverlap;
import static org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipeline.SINGLE_JACCARD_COMPUTATION_STEP_ID;

import java.util.LinkedList;
import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.JaccardResultDao;
import org.sing_group.dreimt.domain.dao.spi.signature.SignatureDao;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardResult;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardUpDownSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.GeneSetSignature;
import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;
import org.sing_group.dreimt.domain.entities.signature.UpDownSignature;
import org.sing_group.dreimt.service.execution.Constants;
import org.sing_group.dreimt.service.execution.pipeline.jaccard.DefaultGeneOverlapData;
import org.sing_group.dreimt.service.signature.geneoverlap.GeneOverlapResult;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapData;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContextBuilder;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContextBuilderFactory;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.SingleJaccardPipelineStep;

@Default
public class SingleJaccardComputationStep implements SingleJaccardPipelineStep {

  private Constants constants;
  private JaccardResultDao jaccardResultDao;
  private SignatureDao signatureDao;
  private JaccardPipelineContextBuilderFactory jaccardPipelineContextBuilderFactory;

  private String signatureName;

  @Inject
  public void setConstants(Constants constants) {
    this.constants = requireNonNull(constants);
  }

  @Inject
  public void setJaccardResultDao(JaccardResultDao jaccardResultDao) {
    this.jaccardResultDao = requireNonNull(jaccardResultDao);
  }

  @Inject
  public void setSignatureDao(SignatureDao signatureDao) {
    this.signatureDao = requireNonNull(signatureDao);
  }

  @Inject
  public void setJaccardPipelineContextBuilderFactory(
    JaccardPipelineContextBuilderFactory jaccardPipelineContextBuilderFactory
  ) {
    this.jaccardPipelineContextBuilderFactory = requireNonNull(jaccardPipelineContextBuilderFactory);
  }

  public void setSignatureName(String signatureName) {
    this.signatureName = requireNonNull(signatureName);
  }

  @Override
  public String getStepId() {
    return SINGLE_JACCARD_COMPUTATION_STEP_ID;
  }

  @Override
  public String getName() {
    return "Computing Jaccard against signature: " + this.signatureName;
  }

  @Override
  public int getOrder() {
    return -1;
  }

  @Override
  public boolean isComplete(JaccardPipelineContext context) {
    return context.getOverlapsForSignature(this.signatureName).isPresent();
  }

  @Transactional(REQUIRED)
  @Override
  public JaccardPipelineContext execute(JaccardPipelineContext context) {
    Signature targetSignature =
      this.signatureDao.get(this.signatureName).orElseThrow(
        () -> new RuntimeException("Signature not found: " + this.signatureName)
      );

    JaccardResult jaccardResult =
      jaccardResultDao.get(context.getConfiguration().getResultId())
        .orElseThrow(
          () -> new RuntimeException("Jaccard result not found: " + this.signatureName)
        );

    final JaccardPipelineContextBuilder builder = this.jaccardPipelineContextBuilderFactory.createBuilderFor(context);

    List<GeneOverlapData> geneOverlaps =
      computeGeneOverlaps(jaccardResult, targetSignature, constants.getUniverseGenesCount());
    builder.addGeneOverlaps(this.signatureName, geneOverlaps);

    return builder.build();
  }

  private static final List<GeneOverlapData> computeGeneOverlaps(
    JaccardResult jaccardResult, Signature targetSignature, int universeSize
  ) {
    List<GeneOverlapData> toret = new LinkedList<>();

    if (jaccardResult instanceof JaccardUpDownSignatureResult) {
      JaccardUpDownSignatureResult result = (JaccardUpDownSignatureResult) jaccardResult;
      if (targetSignature.getSignatureType().equals(SignatureType.UPDOWN)) {
        UpDownSignature upDownTargetSignature = (UpDownSignature) targetSignature;

        GeneOverlapResult upVersusUp =
          geneOverlap(result.getUpGenes(true), upDownTargetSignature.getUpGenes(true), universeSize);
        toret.add(
          new DefaultGeneOverlapData(
            SIGNATURE_UP,
            upDownTargetSignature.getSignatureName(), SIGNATURE_UP,
            upVersusUp.getJaccard(), upVersusUp.getPvalue()
          )
        );

        GeneOverlapResult upVersusDown =
          geneOverlap(result.getUpGenes(true), upDownTargetSignature.getDownGenes(true), universeSize);
        toret.add(
          new DefaultGeneOverlapData(
            SIGNATURE_UP,
            upDownTargetSignature.getSignatureName(), SIGNATURE_DOWN,
            upVersusDown.getJaccard(), upVersusDown.getPvalue()
          )
        );

        GeneOverlapResult downVersusUp =
          geneOverlap(result.getDownGenes(true), upDownTargetSignature.getUpGenes(true), universeSize);
        toret.add(
          new DefaultGeneOverlapData(
            SIGNATURE_DOWN,
            upDownTargetSignature.getSignatureName(), SIGNATURE_UP,
            downVersusUp.getJaccard(), downVersusUp.getPvalue()
          )
        );

        GeneOverlapResult downVersusDown =
          geneOverlap(result.getDownGenes(true), upDownTargetSignature.getDownGenes(true), universeSize);
        toret.add(
          new DefaultGeneOverlapData(
            SIGNATURE_DOWN,
            upDownTargetSignature.getSignatureName(), SIGNATURE_DOWN,
            downVersusDown.getJaccard(), downVersusDown.getPvalue()
          )
        );
      } else {
        GeneSetSignature geneSetSignature = (GeneSetSignature) targetSignature;

        GeneOverlapResult upVersusSignature =
          geneOverlap(result.getUpGenes(true), geneSetSignature.getSignatureGenes(true), universeSize);
        toret.add(
          new DefaultGeneOverlapData(
            SIGNATURE_UP,
            targetSignature.getSignatureName(), GENESET,
            upVersusSignature.getJaccard(), upVersusSignature.getPvalue()
          )
        );

        GeneOverlapResult downVersusSignature =
          geneOverlap(result.getDownGenes(true), geneSetSignature.getSignatureGenes(true), universeSize);
        toret.add(
          new DefaultGeneOverlapData(
            SIGNATURE_DOWN,
            targetSignature.getSignatureName(), GENESET,
            downVersusSignature.getJaccard(), downVersusSignature.getPvalue()
          )
        );
      }
    } else if (jaccardResult instanceof JaccardGeneSetSignatureResult) {
      JaccardGeneSetSignatureResult result = (JaccardGeneSetSignatureResult) jaccardResult;
      if (targetSignature.getSignatureType().equals(SignatureType.UPDOWN)) {
        UpDownSignature upDownTargetSignature = (UpDownSignature) targetSignature;

        GeneOverlapResult signatureVersusUp =
          geneOverlap(result.getGenes(true), upDownTargetSignature.getUpGenes(true), universeSize);
        toret.add(
          new DefaultGeneOverlapData(
            GENESET,
            targetSignature.getSignatureName(), SIGNATURE_UP,
            signatureVersusUp.getJaccard(), signatureVersusUp.getPvalue()
          )
        );

        GeneOverlapResult signatureVersusDown =
          geneOverlap(result.getGenes(true), upDownTargetSignature.getDownGenes(true), universeSize);
        toret.add(
          new DefaultGeneOverlapData(
            GENESET,
            targetSignature.getSignatureName(), SIGNATURE_DOWN,
            signatureVersusDown.getJaccard(), signatureVersusDown.getPvalue()
          )
        );
      } else {
        GeneSetSignature geneSetSignature = (GeneSetSignature) targetSignature;

        GeneOverlapResult signatureVersusSignature =
          geneOverlap(result.getGenes(true), geneSetSignature.getSignatureGenes(true), universeSize);
        toret.add(
          new DefaultGeneOverlapData(
            GENESET,
            targetSignature.getSignatureName(), GENESET,
            signatureVersusSignature.getJaccard(), signatureVersusSignature.getPvalue()
          )
        );
      }
    }

    return toret;
  }
}
