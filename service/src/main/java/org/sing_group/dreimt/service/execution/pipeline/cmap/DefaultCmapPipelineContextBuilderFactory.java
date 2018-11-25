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

import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipeline;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineConfiguration;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContextBuilder;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContextBuilderFactory;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineEventManager;

public class DefaultCmapPipelineContextBuilderFactory implements CmapPipelineContextBuilderFactory {

  @Override
  public CmapPipelineContextBuilder createBuilderFor(
    CmapPipeline pipeline, CmapPipelineConfiguration configuration, CmapPipelineEventManager eventManager
  ) {
    return new DefaultCmapPipelineContextBuilder(pipeline, configuration, eventManager);
  }

  @Override
  public CmapPipelineContextBuilder createBuilderFor(CmapPipelineContext context) {
    return new DefaultCmapPipelineContextBuilder(context);
  }
}
