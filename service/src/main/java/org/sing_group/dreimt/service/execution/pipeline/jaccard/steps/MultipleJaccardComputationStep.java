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

import static java.util.OptionalInt.of;
import static javax.transaction.Transactional.TxType.SUPPORTS;
import static org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipeline.MULTIPLE_JACCARD_COMPUTATION_STEP_ID;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapData;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineStep;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.MultipleJaccardPipelineStep;

@Stateless
@PermitAll
public class MultipleJaccardComputationStep implements MultipleJaccardPipelineStep {
  private Instance<SingleJaccardComputationStep> stepInstances;

  MultipleJaccardComputationStep() {}

  public MultipleJaccardComputationStep(
    Instance<SingleJaccardComputationStep> stepInstances
  ) {
    this.setStepInstances(stepInstances);
  }

  @Inject
  public void setStepInstances(Instance<SingleJaccardComputationStep> stepInstances) {
    this.stepInstances = stepInstances;
  }

  @Override
  public String getStepId() {
    return MULTIPLE_JACCARD_COMPUTATION_STEP_ID;
  }

  @Override
  public String getName() {
    return "Computing Jaccard indexes";
  }

  @Override
  public int getOrder() {
    return 2;
  }

  @Override
  public boolean isComplete(JaccardPipelineContext context) {
    Optional<Map<String, List<GeneOverlapData>>> targetSignatureOverlaps = context.getTargetSignatureOverlaps();
    if (targetSignatureOverlaps.isPresent()) {
      return targetSignatureOverlaps.get().keySet().size() == countSteps(context).getAsInt();
    } else {
      return false;
    }
  }

  @Transactional(SUPPORTS)
  @Override
  public Stream<JaccardPipelineStep> getSteps(JaccardPipelineContext context) {
    return context.getTargetSignatureIds()
      .orElseThrow(() -> new RuntimeException("Target signature IDs must be set before this step."))
      .map(signatureId -> {
        final SingleJaccardComputationStep step = stepInstances.get();
        step.setSignatureName(signatureId);

        return step;
      });
  }

  @Override
  public OptionalInt countSteps(JaccardPipelineContext context) {
    return of(
      (int) context.getTargetSignatureIds()
        .orElseThrow(() -> new RuntimeException("Target signature IDs must be set before this step."))
        .count()
    );
  }
}
