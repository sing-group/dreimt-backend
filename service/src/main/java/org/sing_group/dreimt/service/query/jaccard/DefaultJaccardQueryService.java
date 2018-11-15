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

import static org.sing_group.dreimt.service.util.Sets.intersection;

import java.util.Optional;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.JaccardGeneSetSignatureResultDao;
import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.JaccardResultDao;
import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.JaccardUpDownSignatureResultDao;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardResult;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardUpDownSignatureResult;
import org.sing_group.dreimt.service.query.jaccard.event.DefaultJaccardComputationRequestEvent;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryOptions;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryService;

@Stateless
@PermitAll
public class DefaultJaccardQueryService implements JaccardQueryService {
  private static final int MINIMUM_GENESET_SIZE = 1;
  private static final int MAXIMUM_GENESET_SIZE = 10;

  @Inject
  private Event<DefaultJaccardComputationRequestEvent> jaccardComputationEvents;

  @Inject
  private JaccardResultDao jaccardResultDao;

  @Inject
  private JaccardGeneSetSignatureResultDao jaccardGeneSetDao;

  @Inject
  private JaccardUpDownSignatureResultDao jaccardUpDownSignatureDao;

  @Override
  public boolean isValidGeneSet(Set<String> genes) {
    return genes.size() >= getMinimumGeneSetSize() && genes.size() <= getMaximumGeneSetSize();
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
  public JaccardResult jaccardQuery(JaccardQueryOptions options) {
    this.validateGeneListsSizes(options);

    JaccardResult result =
      !options.getDownGenes().isEmpty() ? this.jaccardUpDownQuery(options) : this.jaccardGeneSetQuery(options);

    this.jaccardComputationEvents
      .fire(new DefaultJaccardComputationRequestEvent(result.getId(), options.getSignatureListingOptions()));

    result.setScheduled();

    return result;
  }

  private JaccardResult jaccardUpDownQuery(JaccardQueryOptions options) {
    final JaccardUpDownSignatureResult result =
      this.jaccardUpDownSignatureDao.create(
        "Jaccard UpDown Signature query",
        "Jaccard UpDown Signature query",
        options.getResultUriBuilder(),
        options.getUpGenes(),
        options.getDownGenes()
      );

    return result;
  }

  private JaccardResult jaccardGeneSetQuery(JaccardQueryOptions options) {
    final JaccardGeneSetSignatureResult result =
      this.jaccardGeneSetDao.create(
        "Jaccard GeneSet query",
        "Jaccard GeneSet query",
        options.getResultUriBuilder(),
        options.getUpGenes()
      );

    return result;
  }

  private void validateGeneListsSizes(JaccardQueryOptions options) {
    if (options.getUpGenes().isEmpty()) {
      throw new IllegalArgumentException("Up (or geneset) genes list is always required.");
    } else if (!this.isValidGeneSet(options.getUpGenes())) {
      throw new IllegalArgumentException(
        "Invalid up (or geneset) genes list size. It must have at least " + this.getMinimumGeneSetSize()
          + " and at most " + this.getMaximumGeneSetSize() + " genes."
        );
    }

    if (!options.getDownGenes().isEmpty() && !this.isValidGeneSet(options.getDownGenes())) {
      throw new IllegalArgumentException(
        "Invalid down genes list size. It must have at least " + this.getMinimumGeneSetSize()
          + " and at most " + this.getMaximumGeneSetSize() + " genes."
        );
    }
    
    if (!options.getDownGenes().isEmpty() && intersection(options.getUpGenes(), options.getDownGenes()).size() > 0) {
      throw new IllegalArgumentException("Up and down gene lists cannot have genes in common");
    }
  }

  @Override
  public Optional<JaccardResult> getResult(String resultId) {
    return this.jaccardResultDao.get(resultId);
  }
}
