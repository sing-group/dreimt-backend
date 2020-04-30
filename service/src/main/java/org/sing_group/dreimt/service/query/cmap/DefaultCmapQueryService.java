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

import static org.sing_group.dreimt.domain.entities.signature.GeneSetType.DOWN;
import static org.sing_group.dreimt.domain.entities.signature.GeneSetType.UP;
import static org.sing_group.dreimt.service.spi.query.GeneListsValidationService.MAXIMUM_GENESET_SIZE;
import static org.sing_group.dreimt.service.spi.query.GeneListsValidationService.MINIMUM_GENESET_SIZE;

import java.util.Optional;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.dao.spi.execution.cmap.CmapGeneSetSignatureResultDao;
import org.sing_group.dreimt.domain.dao.spi.execution.cmap.CmapResultDao;
import org.sing_group.dreimt.domain.dao.spi.execution.cmap.CmapUpDownSignatureResultDao;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapResult;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapUpDownSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.GeneSetType;
import org.sing_group.dreimt.service.query.cmap.event.DefaultCmapComputationRequestEvent;
import org.sing_group.dreimt.service.spi.query.GeneListsValidationService;
import org.sing_group.dreimt.service.spi.query.cmap.CmapQueryOptions;
import org.sing_group.dreimt.service.spi.query.cmap.CmapQueryService;

@Stateless
@PermitAll
public class DefaultCmapQueryService implements CmapQueryService {
  private static final int MINIMUM_NPERM = 100;
  private static final int MAXIMUM_NPERM = 1000;
  
  @Inject
  private GeneListsValidationService geneListsValidationService;

  @Inject
  private Event<DefaultCmapComputationRequestEvent> cmapComputationEvents;

  @Inject
  private CmapResultDao cmapResultDao;

  @Inject
  private CmapGeneSetSignatureResultDao cmapGeneSetDao;

  @Inject
  private CmapUpDownSignatureResultDao cmapUpDownSignatureDao;

  @Override
  public boolean isValidGeneSet(Set<String> genes) {
    return this.geneListsValidationService
      .isValidGeneSet(genes, true, getMinimumGeneSetSize(), getMaximumGeneSetSize());
  }

  @Override
  public int getMinimumGeneSetSize() {
    return MINIMUM_GENESET_SIZE;
  }

  @Override
  public int getMaximumGeneSetSize() {
    return MAXIMUM_GENESET_SIZE;
  }

  @Override
  public boolean validateNumPerm(Integer numPerm) {
    return numPerm >= MINIMUM_NPERM && numPerm <= MAXIMUM_NPERM;
  }

  @Override
  public CmapResult cmapQuery(CmapQueryOptions options) {
    this.validateGeneListsSizes(options);

    CmapResult result =
      (options.getDownGenes().isEmpty() || options.getUpGenes().isEmpty()) ? this.cmapGeneSetQuery(options) : this.cmapUpDownQuery(options);

    DefaultCmapServiceConfiguration configuration = new DefaultCmapServiceConfiguration(options.getNumPerm());

    DefaultCmapComputationRequestEvent event = constructComputationRequestEvent(result, configuration);

    this.cmapComputationEvents.fire(event);

    result.setScheduled();

    return result;
  }

  private void validateGeneListsSizes(CmapQueryOptions options) {
    this.geneListsValidationService.validateGeneListsSizes(
      options.getUpGenes(),
      options.getDownGenes(),
      true,
      getMinimumGeneSetSize(),
      getMaximumGeneSetSize()
    );
  }

  private DefaultCmapComputationRequestEvent constructComputationRequestEvent(
    CmapResult result, DefaultCmapServiceConfiguration configuration
  ) {
    DefaultCmapComputationRequestEvent event = null;
    if (result instanceof CmapGeneSetSignatureResult) {
      event =
        new DefaultCmapComputationRequestEvent(
          result.getId(), configuration, ((CmapGeneSetSignatureResult) result).getGenes(true)
        );
    } else if (result instanceof CmapUpDownSignatureResult) {
      event =
        new DefaultCmapComputationRequestEvent(
          result.getId(), configuration,
          ((CmapUpDownSignatureResult) result).getUpGenes(true),
          ((CmapUpDownSignatureResult) result).getDownGenes(true)
        );
    }
    return event;
  }

  private CmapResult cmapUpDownQuery(CmapQueryOptions options) {
    final CmapUpDownSignatureResult result =
      this.cmapUpDownSignatureDao.create(
        options.getTitle().orElse("Untitled Cmap UpDown Signature query"),
        "Cmap UpDown Signature query",
        options.getResultUriBuilder(),
        options.getUpGenes(),
        options.getDownGenes(),
        options.getNumPerm()
      );

    return result;
  }

  private CmapResult cmapGeneSetQuery(CmapQueryOptions options) {
    final GeneSetType type = options.getUpGenes().isEmpty() ? DOWN : UP;

    final CmapGeneSetSignatureResult result =
      this.cmapGeneSetDao.create(
        options.getTitle().orElse("Untitled Cmap GeneSet query"),
        "Cmap GeneSet query",
        options.getResultUriBuilder(),
        options.getUpGenes().isEmpty() ? options.getDownGenes() : options.getUpGenes(),
        options.getNumPerm(),
        type
      );

    return result;
  }

  @Override
  public Optional<CmapResult> getResult(String resultId) {
    return this.cmapResultDao.get(resultId);
  }
}
