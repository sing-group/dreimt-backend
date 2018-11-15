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
package org.sing_group.dreimt.service.execution.pipeline;

import static java.util.Objects.requireNonNull;
import static org.sing_group.fluent.checker.Checks.requireNonEmpty;

import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.sing_group.dreimt.service.spi.execution.pipeline.Pipeline;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineConfiguration;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineEvent;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineEventManager;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineStep;
import org.sing_group.fluent.compare.Compare;

public abstract class AbstractPipeline<
  C extends PipelineConfiguration,
  PC extends PipelineContext<C, PC, PS, P, PE, PEM>,
  PS extends PipelineStep<C, PC, PS, P, PE, PEM>,
  P extends Pipeline<C, PC, PS, P, PE, PEM>,
  PE extends PipelineEvent<C, PC, PS, P, PE, PEM>,
  PEM extends PipelineEventManager<C, PC, PS, P, PE, PEM>
>
implements Pipeline<C, PC, PS, P, PE, PEM> {
  private final Comparator<PipelineStep<?, ?, ?, ?, ?, ?>> STEP_COMPARATOR =
    Compare.<PipelineStep<?, ?, ?, ?, ?, ?>>createComparator()
      .by(PipelineStep::getOrder)
    .andGetComparator();
  
  protected PEM eventManager;
  protected SortedSet<PS> steps;

  public AbstractPipeline() {
    super();
  }

  public void setEventManager(PEM eventManager) {
    this.eventManager = requireNonNull(eventManager);
  }

  public void setSteps(Collection<PS> steps) {
    requireNonEmpty(steps);
    
    this.steps = new TreeSet<>(STEP_COMPARATOR);
    this.steps.addAll(steps);
  }

  @Override
  public Stream<PS> getSteps() {
    return this.steps.stream();
  }
}
