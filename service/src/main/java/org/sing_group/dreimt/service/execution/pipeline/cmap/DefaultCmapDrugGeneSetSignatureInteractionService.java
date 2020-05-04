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

import org.sing_group.dreimt.domain.dao.execution.cmap.CmapDrugGeneSetSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.dao.spi.execution.cmap.CmapDrugGeneSetSignatureInteractionDao;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugGeneSetSignatureInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.DrugStatus;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapDrugGeneSetSignatureInteractionService;

@Stateless
@PermitAll
public class DefaultCmapDrugGeneSetSignatureInteractionService implements CmapDrugGeneSetSignatureInteractionService {

  @Inject
  private CmapDrugGeneSetSignatureInteractionDao cmapDrugInteractionDao;

  @Override
  public Stream<CmapDrugGeneSetSignatureInteraction> list(
    CmapGeneSetSignatureResult CmapGeneSetSignatureResult,
    CmapDrugGeneSetSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.list(CmapGeneSetSignatureResult, listingOptions);
  }

  @Override
  public long count(
    CmapGeneSetSignatureResult CmapGeneSetSignatureResult,
    CmapDrugGeneSetSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.count(CmapGeneSetSignatureResult, listingOptions);
  }

  @Override
  public Stream<String> listDrugSourceNameValues(
    CmapGeneSetSignatureResult CmapGeneSetSignatureResult,
    CmapDrugGeneSetSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.listDrugSourceNameValues(CmapGeneSetSignatureResult, listingOptions);
  }

  @Override
  public Stream<String> listDrugSourceDbValues(
    CmapGeneSetSignatureResult CmapGeneSetSignatureResult,
    CmapDrugGeneSetSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.listDrugSourceDbValues(CmapGeneSetSignatureResult, listingOptions);
  }

  @Override
  public Stream<String> listDrugCommonNameValues(
    CmapGeneSetSignatureResult CmapGeneSetSignatureResult,
    CmapDrugGeneSetSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.listDrugCommonNameValues(CmapGeneSetSignatureResult, listingOptions);
  }

  @Override
  public Stream<String> listDrugMoaValues(
    CmapGeneSetSignatureResult CmapGeneSetSignatureResult,
    CmapDrugGeneSetSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.listDrugMoaValues(CmapGeneSetSignatureResult, listingOptions);
  }

  @Override
  public Stream<DrugStatus> listDrugStatusValues(
    CmapGeneSetSignatureResult CmapGeneSetSignatureResult,
    CmapDrugGeneSetSignatureInteractionListingOptions listingOptions
  ) {
    return cmapDrugInteractionDao.listDrugStatusValues(CmapGeneSetSignatureResult, listingOptions);
  }
}
