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

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.sing_group.dreimt.service.execution.pipeline.AbstractPipeline;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipeline;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineConfiguration;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContextBuilderFactory;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineEvent;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineEventManager;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineStep;

@Stateless
@PermitAll
public class DefaultJaccardPipeline
  extends AbstractPipeline<
    JaccardPipelineConfiguration,
    JaccardPipelineContext,
    JaccardPipelineStep,
    JaccardPipeline,
    JaccardPipelineEvent,
    JaccardPipelineEventManager
  >
  implements JaccardPipeline {
  private JaccardPipelineContextBuilderFactory contextBuilderFactory;

  DefaultJaccardPipeline() {}

  public DefaultJaccardPipeline(
    JaccardPipelineEventManager eventManager,
    Collection<JaccardPipelineStep> step
  ) {
    this.setEventManager(eventManager);
    this.setSteps(step);
  }

  @Inject
  @Override
  public void setEventManager(JaccardPipelineEventManager eventManager) {
    super.setEventManager(eventManager);
  }

  @Inject
  public void setSteps(Instance<JaccardPipelineStep> steps) {
    requireNonNull(steps);

    super.setSteps(
      StreamSupport.stream(steps.spliterator(), false)
        .filter(step -> step.getOrder() >= 0)
        .collect(toSet())
    );
  }

  @Inject
  public void setContextBuilderFactory(JaccardPipelineContextBuilderFactory contextBuilderFactory) {
    this.contextBuilderFactory = requireNonNull(contextBuilderFactory);
  }

  @Override
  public String getName() {
    return "Jaccard operation";
  }

  @Override
  public JaccardPipelineContext createContext(JaccardPipelineConfiguration configuration) {
    return this.contextBuilderFactory.createBuilderFor(
      this, configuration, this.eventManager
    ).build();
  }

  // Methods explicitly override to force @PermitAll on them
  @Override
  public Stream<JaccardPipelineStep> getSteps() {
    return super.getSteps();
  }

  @Override
  public int countTotalSteps() {
    return super.countTotalSteps();
  }

  @Override
  public Stream<JaccardPipelineStep> getExecutedSteps(JaccardPipelineContext context) {
    return super.getExecutedSteps(context);
  }

  @Override
  public Stream<JaccardPipelineStep> getUnexecutedSteps(JaccardPipelineContext context) {
    return super.getUnexecutedSteps(context);
  }
}
