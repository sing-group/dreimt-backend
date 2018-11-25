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
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipeline;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineConfiguration;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContextBuilderFactory;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineEvent;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineEventManager;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineStep;

@Stateless
@PermitAll
public class DefaultCmapPipeline
  extends AbstractPipeline<
    CmapPipelineConfiguration,
    CmapPipelineContext,
    CmapPipelineStep,
    CmapPipeline,
    CmapPipelineEvent,
    CmapPipelineEventManager
  >
  implements CmapPipeline {
  private CmapPipelineContextBuilderFactory contextBuilderFactory;

  DefaultCmapPipeline() {}

  public DefaultCmapPipeline(
    CmapPipelineEventManager eventManager,
    Collection<CmapPipelineStep> step
  ) {
    this.setEventManager(eventManager);
    this.setSteps(step);
  }

  @Inject
  @Override
  public void setEventManager(CmapPipelineEventManager eventManager) {
    super.setEventManager(eventManager);
  }

  @Inject
  public void setSteps(Instance<CmapPipelineStep> steps) {
    requireNonNull(steps);

    super.setSteps(
      StreamSupport.stream(steps.spliterator(), false)
        .filter(step -> step.getOrder() >= 0)
        .collect(toSet())
    );
  }

  @Inject
  public void setContextBuilderFactory(CmapPipelineContextBuilderFactory contextBuilderFactory) {
    this.contextBuilderFactory = requireNonNull(contextBuilderFactory);
  }

  @Override
  public String getName() {
    return "Cmap operation";
  }

  @Override
  public CmapPipelineContext createContext(CmapPipelineConfiguration configuration) {
    return this.contextBuilderFactory.createBuilderFor(
      this, configuration, this.eventManager
    ).build();
  }

  // Methods explicitly override to force @PermitAll on them
  @Override
  public Stream<CmapPipelineStep> getSteps() {
    return super.getSteps();
  }

  @Override
  public int countTotalSteps() {
    return super.countTotalSteps();
  }

  @Override
  public Stream<CmapPipelineStep> getExecutedSteps(CmapPipelineContext context) {
    return super.getExecutedSteps(context);
  }

  @Override
  public Stream<CmapPipelineStep> getUnexecutedSteps(CmapPipelineContext context) {
    return super.getUnexecutedSteps(context);
  }
}
