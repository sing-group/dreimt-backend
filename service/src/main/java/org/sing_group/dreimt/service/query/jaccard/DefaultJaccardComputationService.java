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
package org.sing_group.dreimt.service.query.jaccard;

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

import org.sing_group.dreimt.service.execution.pipeline.jaccard.DefaultJaccardPipelineConfiguration;
import org.sing_group.dreimt.service.query.jaccard.event.DefaultJaccardComputationRequestEvent;
import org.sing_group.dreimt.service.spi.execution.pipeline.PipelineExecutor;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipeline;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineConfiguration;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardComputationService;

@Stateless
@PermitAll
@TransactionManagement(BEAN)
@TransactionAttribute(NEVER)
public class DefaultJaccardComputationService implements JaccardComputationService {
  @Inject
  private PipelineExecutor executor;

  @Inject
  private JaccardPipeline pipeline;

  @Asynchronous
  @Override
  public void computeJaccard(
    @Observes(during = TransactionPhase.AFTER_SUCCESS) DefaultJaccardComputationRequestEvent event
  ) {
    final JaccardPipelineConfiguration configuration =
      new DefaultJaccardPipelineConfiguration(
        event.getWorkId(), event.getWorkId(), event.getSignatureListingOptions()
      );

    this.executor.execute(pipeline, configuration);
  }
}
