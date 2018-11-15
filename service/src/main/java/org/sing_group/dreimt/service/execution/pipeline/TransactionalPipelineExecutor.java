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

import static javax.ejb.TransactionAttributeType.NEVER;
import static javax.ejb.TransactionManagementType.BEAN;

import java.util.OptionalInt;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.entities.execution.ExecutionStatus;
import org.sing_group.dreimt.domain.entities.execution.StepExecutionStatus;
import org.sing_group.dreimt.service.spi.execution.pipeline.MultiplePipelineStep;
import org.sing_group.dreimt.service.spi.execution.pipeline.Pipeline;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineConfiguration;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineEvent;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineEventManager;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineExecutor;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineStep;
import org.sing_group.dreimt.service.spi.execution.pipeline.SinglePipelineStep;
import org.slf4j.Logger;

@Stateless
@PermitAll
@TransactionManagement(BEAN)
@TransactionAttribute(NEVER)
public class TransactionalPipelineExecutor implements PipelineExecutor {
  public static final String INITIAL_STAGE_ID = "Initial Stage";
  
  @Inject
  private Logger logger;
  
  @Override
  public <
    C extends PipelineConfiguration,
    PC extends PipelineContext<C, PC, PS, P, PE, PEM>,
    PS extends PipelineStep<C, PC, PS, P, PE, PEM>,
    P extends Pipeline<C, PC, PS, P, PE, PEM>,
    PE extends PipelineEvent<C, PC, PS, P, PE, PEM>,
    PEM extends PipelineEventManager<C, PC, PS, P, PE, PEM>
  > void execute(
    P pipeline, PC context
  ) {
    final PipelineEventManager<C, PC, PS, P, PE, PEM> eventManager = context.getEventManager();
    
    final String pipelineName = formatStepName(pipeline.getName());
    try {
      final double stepProgressSize = 1d / (double) pipeline.countTotalSteps();

      context = this.execute(pipeline.getSteps(), context, 0d, stepProgressSize);
      
      eventManager.fireEvent(context, ExecutionStatus.COMPLETED, 1d, "Completed " + pipelineName + " analysis");
    } catch (RuntimeException re) {
      logger.error("Error in pipeline " + pipelineName, re);
      eventManager.fireEvent(context, ExecutionStatus.FAILED, Double.NaN, re.getMessage());
    }
  }
  
  private <
    C extends PipelineConfiguration,
    PC extends PipelineContext<C, PC, PS, P, PE, PEM>,
    PS extends PipelineStep<C, PC, PS, P, PE, PEM>,
    P extends Pipeline<C, PC, PS, P, PE, PEM>,
    PE extends PipelineEvent<C, PC, PS, P, PE, PEM>,
    PEM extends PipelineEventManager<C, PC, PS, P, PE, PEM>
  > PC execute(Stream<PS> steps, PC context, double currentProgress, double stepProgressSize) {
    final PipelineEventManager<C, PC, PS, P, PE, PEM> eventManager = context.getEventManager();
    
    final ProgressAndContext<C, PC, PS, P, PE, PEM> pac = new ProgressAndContext<>(context, currentProgress);
    
    steps.forEach(step -> {
      final String name = formatStepName(step.getName());

      eventManager.fireRunningStepEvent(step, pac.getContext(), step.getStepId(), StepExecutionStatus.STARTED, pac.getProgress(), "Running " + name);
      
      final PC newContext;
      if (step instanceof SinglePipelineStep) {
        newContext = ((SinglePipelineStep<C, PC, PS, P, PE, PEM>) step).execute(pac.getContext());
      } else if (step instanceof MultiplePipelineStep) {
        final MultiplePipelineStep<C, PC, PS, P, PE, PEM> multistep = (MultiplePipelineStep<C, PC, PS, P, PE, PEM>) step;
        final OptionalInt countSteps = multistep.countSteps(pac.getContext());
        
        final double substepProgressSize = countSteps.isPresent() ? stepProgressSize / (double) countSteps.getAsInt() : 0d;
        
        final Stream<PS> substeps = multistep.getSteps(pac.getContext());
        
        newContext = this.execute(substeps, pac.getContext(), pac.getProgress(), substepProgressSize);
      } else {
        throw new IllegalArgumentException("Unknown pipeline step type: " + step.getClass());
      }
      
      pac.increaseProgress(stepProgressSize);
      

      pac.setContext(newContext);
      
      eventManager.fireRunningStepEvent(step, pac.getContext(), step.getStepId(), StepExecutionStatus.FINISHED, pac.getProgress(), "Completed " + name);
    });
    
    return pac.getContext();
  }
  
  private final static class ProgressAndContext<
    C extends PipelineConfiguration,
    PC extends PipelineContext<C, PC, PS, P, PE, PEM>,
    PS extends PipelineStep<C, PC, PS, P, PE, PEM>,
    P extends Pipeline<C, PC, PS, P, PE, PEM>,
    PE extends PipelineEvent<C, PC, PS, P, PE, PEM>,
    PEM extends PipelineEventManager<C, PC, PS, P, PE, PEM>
  > {
    private PC context;
    private double progress;
    
    public ProgressAndContext(PC context, double progress) {
      this.context = context;
      this.progress = progress;
    }

    public void increaseProgress(double stepProgressSize) {
      this.progress += stepProgressSize;
    }

    public double getProgress() {
      return progress;
    }

    public PC getContext() {
      return context;
    }

    public void setContext(PC context) {
      this.context = context;
    }
  }

  @Override
  public <
    C extends PipelineConfiguration,
    PC extends PipelineContext<C, PC, PS, P, PE, PEM>,
    PS extends PipelineStep<C, PC, PS, P, PE, PEM>,
    P extends Pipeline<C, PC, PS, P, PE, PEM>,
    PE extends PipelineEvent<C, PC, PS, P, PE, PEM>,
    PEM extends PipelineEventManager<C, PC, PS, P, PE, PEM>
  > void execute(
    P pipeline, C configuration
  ) {
    PipelineExecutor.super.execute(pipeline, configuration);
  }

  private String formatStepName(String name) {
    return Character.toLowerCase(name.charAt(0)) + name.substring(1);
  }
}
