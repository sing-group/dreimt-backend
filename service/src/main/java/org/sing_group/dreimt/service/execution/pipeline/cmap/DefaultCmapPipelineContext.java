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

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipeline;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineConfiguration;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineEventManager;
import org.sing_group.dreimt.service.spi.query.cmap.CmapResultData;

public class DefaultCmapPipelineContext implements CmapPipelineContext, Serializable {
  private static final long serialVersionUID = 1L;

  private transient final CmapPipeline pipeline;
  private final CmapPipelineConfiguration configuration;
  private transient final CmapPipelineEventManager eventManager;
  private final Set<CmapResultData> cmapResultData;

  DefaultCmapPipelineContext(
    CmapPipeline pipeline,
    CmapPipelineConfiguration configuration,
    CmapPipelineEventManager eventManager,
    Set<CmapResultData> cmapResultData
  ) {
    this.pipeline = pipeline;
    this.configuration = configuration;
    this.eventManager = eventManager;
    this.cmapResultData = cmapResultData;
  }

  @Override
  public CmapPipelineConfiguration getConfiguration() {
    return this.configuration;
  }

  @Override
  public CmapPipelineEventManager getEventManager() {
    return this.eventManager;
  }

  @Override
  public CmapPipeline getPipeline() {
    return this.pipeline;
  }

  @Override
  public Optional<Stream<CmapResultData>> getCmapResultsData() {
    return Optional.ofNullable(this.cmapResultData).map(Set::stream);
  }
}
