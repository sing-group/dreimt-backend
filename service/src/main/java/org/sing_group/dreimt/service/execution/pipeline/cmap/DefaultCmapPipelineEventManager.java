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

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.entities.execution.ExecutionStatus;
import org.sing_group.dreimt.domain.entities.execution.StepExecutionStatus;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineEvent;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineEventManager;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineStep;

@Stateless
@PermitAll
public class DefaultCmapPipelineEventManager
  implements CmapPipelineEventManager {
  @Inject
  private Event<CmapPipelineEvent> events;

  @Override
  public void fireEvent(CmapPipelineContext context, ExecutionStatus status, double progress, String description) {
    this.events.fire(new DefaultCmapPipelineEvent(context, description, progress, status));
  }

  @Override
  public void fireRunningStepEvent(
    CmapPipelineStep step, CmapPipelineContext context, String stepId, StepExecutionStatus stepStatus,
    double progress, String description
  ) {
    this.events.fire(new DefaultCmapPipelineEvent(context, description, progress, stepId, stepStatus));
  }
}
