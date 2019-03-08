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

import static javax.transaction.Transactional.TxType.NOT_SUPPORTED;
import static org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipeline.SINGLE_JACCARD_COMPUTATION_STEP_ID;

import java.io.IOException;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapData;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContextBuilder;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContextBuilderFactory;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.SingleJaccardPipelineStep;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardService;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardServiceConfiguration;

@Transactional(NOT_SUPPORTED)
@Default
public class JaccardComputationStep implements SingleJaccardPipelineStep {

  @Inject
  private JaccardPipelineContextBuilderFactory jaccardPipelineContextBuilderFactory;

  @Inject
  private JaccardService jaccardService;

  @Override
  public String getStepId() {
    return SINGLE_JACCARD_COMPUTATION_STEP_ID;
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
    return context.getGeneOverlapResultsData().isPresent();
  }

  @Override
  public JaccardPipelineContext execute(JaccardPipelineContext context) {
    try {
      JaccardServiceConfiguration configuration = context.getConfiguration().getJaccardServiceConfiguration();
      Stream<GeneOverlapData> jaccardResultDataStream =
        this.jaccardService.jaccard(configuration)
          .filter(
            geneOverlapData -> containsSignatureName(
              context.getTargetSignatureIds().get(), geneOverlapData.getTargetSignatureName()
            )
          );

      final JaccardPipelineContextBuilder builder = this.jaccardPipelineContextBuilderFactory.createBuilderFor(context);

      return builder.addJaccardResultDataStream(jaccardResultDataStream).build();
    } catch (IOException e) {
      throw new RuntimeException("Error running Jaccard.", e);
    }
  }

  private boolean containsSignatureName(Stream<String> signatures, String signatureName) {
    return signatures.filter(s -> s.equals(signatureName)).findAny().isPresent();
  }
}
