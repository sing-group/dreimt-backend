/*-
 * #%L
 * DREIMT - REST
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
package org.sing_group.dreimt.rest.mapper.query.cmap;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.dao.execution.cmap.CmapDrugInteractionListingOptions;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapDrugInteractionListingOptionsData;
import org.sing_group.dreimt.rest.mapper.spi.query.ListingOptionsMapper;
import org.sing_group.dreimt.rest.mapper.spi.query.cmap.CmapDrugInteractionListingOptionsMapper;

@Default
public class DefaultCmapDrugInteractionListingOptionsMapper implements CmapDrugInteractionListingOptionsMapper {

  @Inject
  private ListingOptionsMapper listingOptionsMapper;

  @Override
  public CmapDrugInteractionListingOptions toCmapDrugInteractionListingOptions(
    CmapDrugInteractionListingOptionsData listingOptionsData
  ) {
    return new CmapDrugInteractionListingOptions(
      listingOptionsMapper.toListingOptions(listingOptionsData.getListingOptions()),
      listingOptionsData.getDrugSourceName(),
      listingOptionsData.getDrugCommonName(),
      listingOptionsData.getMaxPvalue(),
      listingOptionsData.getMinTes(),
      listingOptionsData.getMaxTes(),
      listingOptionsData.getMaxFdr()
    );
  }
}
