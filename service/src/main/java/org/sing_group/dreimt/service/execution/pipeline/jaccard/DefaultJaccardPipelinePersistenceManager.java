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

import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.REQUIRES_NEW;
import static org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipeline.SINGLE_FDR_CORRECTION_STEP_ID;
import static org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipeline.SINGLE_JACCARD_COMPUTATION_STEP_ID;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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
        case SINGLE_JACCARD_COMPUTATION_STEP_ID:

          Set<String> completedTargetSignatures =
            result.getGeneOverlapResults()
              .map(e -> e.getTargetSignature().getSignatureName())
              .collect(toSet());

          context.getTargetSignatureOverlaps().orElseGet(HashMap::new)
            .entrySet().stream()
            .filter(e -> !completedTargetSignatures.contains(e.getKey()))
            .forEach(e -> {
              e.getValue().forEach(geneOverlapData -> {
                
                Signature targetSignature =
                  signatureDao.get(geneOverlapData.getTargetSignatureName())
                    .orElseThrow(
                      () -> new RuntimeException("Signature ID not found: " + geneOverlapData.getTargetSignatureName())
                    );

                result.addGeneOverlapResult(
                  geneOverlapData.getSourceComparisonType(),
                  targetSignature,
                  geneOverlapData.getTargetComparisonType(),
                  geneOverlapData.getJaccard(),
                  geneOverlapData.getPvalue()
                );
              });
            });

          break;
        case SINGLE_FDR_CORRECTION_STEP_ID:
          long targetSignatureIdsCount =
            context.getTargetSignatureIds()
              .orElseThrow(() -> new RuntimeException("Target signature IDs must be set before this step."))
              .count();

          if (targetSignatureIdsCount != 0) {
            Map<GeneOverlapData, Double> correctedPvaluesMap =
              context.getCorrectedPvaluesMap()
                .orElseThrow(() -> new RuntimeException("Corrected p-values map is not available"));

            result.getGeneOverlapResults().forEach(geneOverlapEntity -> {
              GeneOverlapData geneOverlapData = new DefaultGeneOverlapData(geneOverlapEntity);
              Double fdr = correctedPvaluesMap.get(geneOverlapData);
              if (fdr != null) {
                geneOverlapEntity.setFdr(fdr);
              } else {
                throw new RuntimeException("Gene Overlap not found in corrected p-values map " + geneOverlapEntity);
              }
            });
          }
          break;
        default:
      }
    }
  }
}
