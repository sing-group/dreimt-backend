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

import static javax.transaction.Transactional.TxType.REQUIRES_NEW;
import static org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipeline.SINGLE_CMAP_STEP_ID;

import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.spi.execution.cmap.CmapResultDao;
import org.sing_group.dreimt.domain.dao.spi.signature.DrugDao;
import org.sing_group.dreimt.domain.entities.execution.StepExecutionStatus;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapResult;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapUpDownSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineEvent;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelinePersistenceManager;

@Stateless
@PermitAll
@Transactional(REQUIRES_NEW)
public class DefaultCmapPipelinePersistenceManager implements CmapPipelinePersistenceManager {

  @Inject
  private DrugDao drugDao;

  @Inject
  private CmapResultDao cmapResultDao;
  
  @Override
  public void manageEvent(@Observes CmapPipelineEvent event) {
    final CmapPipelineContext context = event.getContext();
    final String resultId = context.getConfiguration().getResultId();

    final CmapResult result =
      this.cmapResultDao.get(resultId)
        .orElseThrow(() -> new RuntimeException("Result ID not found: " + resultId));

    final Optional<String> step = event.getRunningStepId();

    if (step.isPresent() && event.getRunningStepStatus().get().equals(StepExecutionStatus.FINISHED)) {
      switch (step.get()) {
        case SINGLE_CMAP_STEP_ID:
          context.getCmapResultsData().get().forEach(r -> {
            Optional<Drug> drug = this.drugDao.get(r.getDrugId());
            if (!drug.isPresent()) {
              throw new IllegalArgumentException(
                "Cannot found drug with source name = " + r.getDrugSourceName() + " and source DB = "
                  + r.getDrugSourceDb() + " in database."
              );
            }

            if (r.getDownFdr().isPresent()) {
              if (result instanceof CmapUpDownSignatureResult) {
                ((CmapUpDownSignatureResult) result)
                  .addCmapDrugInteraction(drug.get(), r.getTau(), r.getUpFdr(), r.getDownFdr().get());
              } else {
                throw new IllegalArgumentException("GeneSet results can't have a DOWN FDR value");
              }
            } else {
              if (result instanceof CmapGeneSetSignatureResult) {
                ((CmapGeneSetSignatureResult) result).addCmapDrugInteraction(drug.get(), r.getTau(), r.getUpFdr());
              } else {
                throw new IllegalArgumentException("Up/Down Signature results must have a DOWN FDR value");
              }
            }
          });
          break;
      }
    }
  }
}
