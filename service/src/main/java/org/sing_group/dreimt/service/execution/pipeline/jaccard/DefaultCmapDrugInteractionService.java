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

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.dao.execution.cmap.CmapDrugInteractionListingOptions;
import org.sing_group.dreimt.domain.dao.spi.execution.cmap.CmapDrugInteractionDao;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapResult;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapDrugInteractionService;

@Stateless
@PermitAll
public class DefaultCmapDrugInteractionService implements CmapDrugInteractionService {

  @Inject
  private CmapDrugInteractionDao cmapDrugInteractionDao;

  @Override
  public Stream<CmapDrugInteraction> list(CmapResult cmapResult, CmapDrugInteractionListingOptions listingOptions) {
    return cmapDrugInteractionDao.list(cmapResult, listingOptions);
  }

  @Override
  public long count(CmapResult cmapResult, CmapDrugInteractionListingOptions listingOptions) {
    return cmapDrugInteractionDao.count(cmapResult, listingOptions);
  }

  @Override
  public Stream<String> listDrugSourceNameValues(
    CmapResult cmapResult, CmapDrugInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.listDrugSourceNameValues(cmapResult, listingOptions);
  }

  @Override
  public Stream<String> listDrugSourceDbValues(
    CmapResult cmapResult, CmapDrugInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.listDrugSourceDbValues(cmapResult, listingOptions);
  }

  @Override
  public Stream<String> listDrugCommonNameValues(
    CmapResult cmapResult, CmapDrugInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.listDrugCommonNameValues(cmapResult, listingOptions);
  }
}
