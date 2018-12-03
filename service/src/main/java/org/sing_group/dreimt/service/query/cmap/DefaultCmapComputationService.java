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
package org.sing_group.dreimt.service.query.cmap;

import static javax.ejb.TransactionAttributeType.NEVER;
import static javax.ejb.TransactionManagementType.BEAN;

import javax.annotation.security.PermitAll;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;

import org.sing_group.dreimt.service.execution.pipeline.cmap.DefaultCmapPipelineConfiguration;
import org.sing_group.dreimt.service.query.cmap.event.DefaultCmapComputationRequestEvent;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineExecutor;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipeline;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineConfiguration;
import org.sing_group.dreimt.service.spi.query.cmap.CmapComputationService;

@Stateless
@PermitAll
@TransactionManagement(BEAN)
@TransactionAttribute(NEVER)
public class DefaultCmapComputationService implements CmapComputationService {
  @Inject
  private PipelineExecutor executor;

  @Inject
  private CmapPipeline pipeline;

  @Asynchronous
  @Override
  public void computeCmap(
    @Observes(during = TransactionPhase.AFTER_SUCCESS) DefaultCmapComputationRequestEvent event
  ) {
    System.out.println("LAUNCHING PIPELINE");
    
    final CmapPipelineConfiguration configuration =
      new DefaultCmapPipelineConfiguration(
        event.getWorkId(), event.getWorkId(), 
        event.getCmapServiceConfiguration(),
        event.getUpGenes(),
        event.getDownGenes()
      );

    this.executor.execute(pipeline, configuration);
  }
}
