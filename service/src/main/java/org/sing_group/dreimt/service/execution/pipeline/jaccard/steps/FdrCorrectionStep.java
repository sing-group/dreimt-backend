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
import static org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipeline.SINGLE_FDR_CORRECTION_STEP_ID;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapData;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContextBuilder;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContextBuilderFactory;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.SingleJaccardPipelineStep;

import es.uvigo.ei.sing.math.statistical.StatisticsTestsUtils;
import es.uvigo.ei.sing.math.statistical.corrections.FDRCorrection;

@Default
public class FdrCorrectionStep implements SingleJaccardPipelineStep {

  private JaccardPipelineContextBuilderFactory jaccardPipelineContextBuilderFactory;

  @Inject
  public void setJaccardPipelineContextBuilderFactory(
    JaccardPipelineContextBuilderFactory jaccardPipelineContextBuilderFactory
  ) {
    this.jaccardPipelineContextBuilderFactory = requireNonNull(jaccardPipelineContextBuilderFactory);
  }

  @Override
  public String getStepId() {
    return SINGLE_FDR_CORRECTION_STEP_ID;
  }

  @Override
  public String getName() {
    return "P-values FDR correction";
  }

  @Override
  public int getOrder() {
    return 3;
  }

  @Override
  public boolean isComplete(JaccardPipelineContext context) {
    return context.getTargetSignatureIds().isPresent()
      && (context.getTargetSignatureIds().get().count() == 0 || context.getCorrectedPvaluesMap().isPresent());
  }

  @Transactional(REQUIRED)
  @Override
  public JaccardPipelineContext execute(JaccardPipelineContext context) {
    long targetSignatureIdsCount =
      context.getTargetSignatureIds()
        .orElseThrow(() -> new RuntimeException("Target signature IDs must be set before this step."))
        .count();

    if (targetSignatureIdsCount == 0) {
      return this.jaccardPipelineContextBuilderFactory.createBuilderFor(context).build();
    }

    Map<GeneOverlapData, Double> pValuesMap =
      context.getGeneOverlapResultsData()
        .orElseThrow(() -> new RuntimeException("Gene overlaps must be calculated before this step."))
        .collect(Collectors.toMap(Function.identity(), go -> go.getPvalue()));

    try {
      Map<GeneOverlapData, Double> correctedPvaluesMap = StatisticsTestsUtils.correct(new FDRCorrection(), pValuesMap);
      final JaccardPipelineContextBuilder builder = this.jaccardPipelineContextBuilderFactory.createBuilderFor(context);
      builder.addCorrectedPvalues(correctedPvaluesMap);

      return builder.build();
    } catch (InterruptedException e) {
      throw new RuntimeException("An error ocurred while calculating FDR correction");
    }
  }
}
