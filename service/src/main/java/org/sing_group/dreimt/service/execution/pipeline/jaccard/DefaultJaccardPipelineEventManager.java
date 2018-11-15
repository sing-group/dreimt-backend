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

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.entities.execution.ExecutionStatus;
import org.sing_group.dreimt.domain.entities.execution.StepExecutionStatus;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineEvent;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineEventManager;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineStep;

@Stateless
@PermitAll
public class DefaultJaccardPipelineEventManager
  implements JaccardPipelineEventManager {
  @Inject
  private Event<JaccardPipelineEvent> events;

  @Override
  public void fireEvent(JaccardPipelineContext context, ExecutionStatus status, double progress, String description) {
    this.events.fire(new DefaultJaccardPipelineEvent(context, description, progress, status));
  }

  @Override
  public void fireRunningStepEvent(
    JaccardPipelineStep step, JaccardPipelineContext context, String stepId, StepExecutionStatus stepStatus,
    double progress, String description
  ) {
    this.events.fire(new DefaultJaccardPipelineEvent(context, description, progress, stepId, stepStatus));
  }
}
