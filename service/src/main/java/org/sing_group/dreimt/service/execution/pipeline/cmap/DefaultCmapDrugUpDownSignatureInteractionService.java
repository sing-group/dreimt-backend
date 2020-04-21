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

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.dao.execution.cmap.CmapDrugUpDownSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.dao.spi.execution.cmap.CmapDrugUpDownSignatureInteractionDao;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugUpDownSignatureInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapUpDownSignatureResult;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapDrugUpDownSignatureInteractionService;

@Stateless
@PermitAll
public class DefaultCmapDrugUpDownSignatureInteractionService implements CmapDrugUpDownSignatureInteractionService {

  @Inject
  private CmapDrugUpDownSignatureInteractionDao cmapDrugInteractionDao;

  @Override
  public Stream<CmapDrugUpDownSignatureInteraction> list(
    CmapUpDownSignatureResult CmapUpDownSignatureResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.list(CmapUpDownSignatureResult, listingOptions);
  }

  @Override
  public long count(
    CmapUpDownSignatureResult CmapUpDownSignatureResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.count(CmapUpDownSignatureResult, listingOptions);
  }

  @Override
  public Stream<String> listDrugSourceNameValues(
    CmapUpDownSignatureResult CmapUpDownSignatureResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.listDrugSourceNameValues(CmapUpDownSignatureResult, listingOptions);
  }

  @Override
  public Stream<String> listDrugSourceDbValues(
    CmapUpDownSignatureResult CmapUpDownSignatureResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.listDrugSourceDbValues(CmapUpDownSignatureResult, listingOptions);
  }

  @Override
  public Stream<String> listDrugCommonNameValues(
    CmapUpDownSignatureResult CmapUpDownSignatureResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.listDrugCommonNameValues(CmapUpDownSignatureResult, listingOptions);
  }

  @Override
  public Stream<String> listDrugMoaValues(
    CmapUpDownSignatureResult CmapUpDownSignatureResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.listDrugMoaValues(CmapUpDownSignatureResult, listingOptions);
  }
}
