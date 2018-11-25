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
package org.sing_group.dreimt.service.execution.pipeline.cmap;

import static java.util.stream.Collectors.toSet;

import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipeline;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineConfiguration;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContextBuilder;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineEventManager;
import org.sing_group.dreimt.service.spi.query.cmap.CmapResultData;

public class DefaultCmapPipelineContextBuilder implements CmapPipelineContextBuilder {

  private CmapPipeline pipeline;
  private CmapPipelineConfiguration configuration;
  private CmapPipelineEventManager eventManager;
  private Set<CmapResultData> cmapResultDataStream;

  public DefaultCmapPipelineContextBuilder(CmapPipelineContext context) {
    this(context.getPipeline(), context.getConfiguration(), context.getEventManager(), context.getCmapResultsData().map(br -> br.collect(toSet())).orElse(null));
  }

  public DefaultCmapPipelineContextBuilder(
    CmapPipeline pipeline,
    CmapPipelineConfiguration configuration,
    CmapPipelineEventManager eventManager
    ) {
    this(pipeline, configuration, eventManager, null);
  }
  
  public DefaultCmapPipelineContextBuilder(
    CmapPipeline pipeline,
    CmapPipelineConfiguration configuration,
    CmapPipelineEventManager eventManager,
    Set<CmapResultData> cmapResultDataStream
  ) {
    this.pipeline = pipeline;
    this.configuration = configuration;
    this.eventManager = eventManager;
    this.cmapResultDataStream = cmapResultDataStream;
  }
  
  @Override
  public CmapPipelineContextBuilder addCmapResultDataStream(Stream<CmapResultData> cmapResultDataStream) {
    this.cmapResultDataStream = cmapResultDataStream.collect(toSet());
    
    return this;
  }

  @Override
  public CmapPipelineContext build() {
    return new DefaultCmapPipelineContext(
      pipeline,
      configuration,
      eventManager,
      cmapResultDataStream
    );
  }
}
