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
package org.sing_group.dreimt.service.execution.pipeline.jaccard.steps;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.REQUIRED;
import static org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipeline.SINGLE_RETRIEVE_SIGNATURES_STEP_ID;

import java.util.Set;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.spi.signature.SignatureDao;
import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContextBuilder;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContextBuilderFactory;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.SingleJaccardPipelineStep;

@Default
public class RetrieveSignaturesStep implements SingleJaccardPipelineStep {

  private SignatureDao signatureDao;
  private JaccardPipelineContextBuilderFactory jaccardPipelineContextBuilderFactory;

  @Inject
  public void setSignatureDao(SignatureDao signatureDao) {
    this.signatureDao = requireNonNull(signatureDao);
  }

  @Inject
  public void setJaccardPipelineContextBuilderFactory(
    JaccardPipelineContextBuilderFactory jaccardPipelineContextBuilderFactory
  ) {
    this.jaccardPipelineContextBuilderFactory = requireNonNull(jaccardPipelineContextBuilderFactory);
  }

  @Override
  public String getStepId() {
    return SINGLE_RETRIEVE_SIGNATURES_STEP_ID;
  }

  @Override
  public String getName() {
    return "Retrieve signatures from database";
  }

  @Override
  public int getOrder() {
    return 1;
  }

  @Override
  public boolean isComplete(JaccardPipelineContext context) {
    return context.getTargetSignatureIds().isPresent();
  }

  @Transactional(REQUIRED)
  @Override
  public JaccardPipelineContext execute(JaccardPipelineContext context) {
    Stream<Signature> signatures = this.signatureDao.list(context.getConfiguration().getSignatureListingOptions());

    Set<String> targetSignatureIds = signatures.map(Signature::getSignatureName).collect(toSet());

    final JaccardPipelineContextBuilder builder = this.jaccardPipelineContextBuilderFactory.createBuilderFor(context);

    return builder.addTargetSignatureIds(targetSignatureIds).build();
  }
}
