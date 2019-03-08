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
package org.sing_group.dreimt.service.execution.pipeline.jaccard;

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;
import static org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipeline.SINGLE_FDR_CORRECTION_STEP_ID;

import java.util.Map;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.spi.signature.SignatureDao;
import org.sing_group.dreimt.domain.entities.execution.StepExecutionStatus;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardResult;
import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapData;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineEvent;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelinePersistenceManager;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryService;

@Stateless
@PermitAll
@Transactional(REQUIRES_NEW)
public class DefaultJaccardPipelinePersistenceManager implements JaccardPipelinePersistenceManager {

  @Inject
  private JaccardQueryService jaccardService;

  @Inject
  private SignatureDao signatureDao;

  @Override
  public void manageEvent(@Observes JaccardPipelineEvent event) {
    final JaccardPipelineContext context = event.getContext();
    final String resultId = context.getConfiguration().getResultId();

    final JaccardResult result =
      this.jaccardService.getResult(resultId)
        .orElseThrow(() -> new RuntimeException("Result ID not found: " + resultId));

    final Optional<String> step = event.getRunningStepId();

    if (step.isPresent() && event.getRunningStepStatus().get().equals(StepExecutionStatus.FINISHED)) {
      switch (step.get()) {
        case SINGLE_FDR_CORRECTION_STEP_ID:
          System.err.println("FDR STEP FINISHED");
          long targetSignatureIdsCount =
            context.getTargetSignatureIds()
              .orElseThrow(() -> new RuntimeException("Target signature IDs must be set before this step."))
              .count();

          if (targetSignatureIdsCount != 0) {
            Map<GeneOverlapData, Double> correctedPvaluesMap =
              context.getCorrectedPvaluesMap()
                .orElseThrow(() -> new RuntimeException("Corrected p-values map is not available"));

            context.getGeneOverlapResultsData()
              .orElseThrow(() -> new RuntimeException("Gene Overlaps must be calculated before this step"))
              .forEach(geneOverlapEntity -> {

                Signature targetSignature =
                  this.signatureDao.get(geneOverlapEntity.getTargetSignatureName())
                    .orElseThrow(
                      () -> new RuntimeException("Signature ID not found: " + geneOverlapEntity.getTargetSignatureName())
                    );

                Double fdr = correctedPvaluesMap.get(geneOverlapEntity);

                if (fdr != null) {
                  result.addGeneOverlapResult(
                    geneOverlapEntity.getSourceComparisonType(),
                    targetSignature,
                    geneOverlapEntity.getTargetComparisonType(),
                    geneOverlapEntity.getJaccard(),
                    geneOverlapEntity.getPvalue(),
                    fdr
                  );
                } else {
                  throw new RuntimeException("FDR is null for signature ID " + targetSignature.getSignatureName());
                }
              });
          }
          break;
        default:
          break;
      }
    }
  }
}
