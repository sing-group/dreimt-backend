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
package org.sing_group.dreimt.service.execution.pipeline.cmap.steps;

import static javax.transaction.Transactional.TxType.NOT_SUPPORTED;
import static org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipeline.SINGLE_CMAP_STEP_ID;

import java.io.IOException;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineConfiguration;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContextBuilder;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContextBuilderFactory;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.SingleCmapPipelineStep;
import org.sing_group.dreimt.service.spi.query.cmap.CmapResultData;
import org.sing_group.dreimt.service.spi.query.cmap.CmapService;

@Transactional(NOT_SUPPORTED)
public class CmapStep implements SingleCmapPipelineStep {

  @Inject
  private CmapPipelineContextBuilderFactory cmapPipelineContextBuilderFactory;

  @Inject
  private CmapService cmapService;

  @Override
  public String getStepId() {
    return SINGLE_CMAP_STEP_ID;
  }

  @Override
  public String getName() {
    return "Cmap script execution";
  }

  @Override
  public int getOrder() {
    return 1;
  }

  @Override
  public boolean isComplete(CmapPipelineContext context) {
    return context.getCmapResultsData().isPresent();
  }

  @Override
  public CmapPipelineContext execute(CmapPipelineContext context) {
    try {
      CmapPipelineConfiguration configuration = context.getConfiguration();
      Stream<CmapResultData> cmapResultDataStream;
      if (configuration.getDownGenes().isEmpty()) {
        cmapResultDataStream =
          this.cmapService.cmap(
            context.getConfiguration().getCmapServiceConfiguration(),
            configuration.getUpGenes()
          );
      } else {
        cmapResultDataStream =
          this.cmapService.cmap(
            context.getConfiguration().getCmapServiceConfiguration(),
            configuration.getUpGenes(),
            configuration.getDownGenes()
          );
      }

      final CmapPipelineContextBuilder builder = this.cmapPipelineContextBuilderFactory.createBuilderFor(context);
      
      return builder.addCmapResultDataStream(cmapResultDataStream).build();
    } catch (IOException e) {
      throw new RuntimeException("Error running Cmap.", e);
    }
  }
}
