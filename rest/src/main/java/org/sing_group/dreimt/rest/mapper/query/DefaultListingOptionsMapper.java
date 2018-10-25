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
package org.sing_group.dreimt.rest.mapper.query;

import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;
import org.sing_group.dreimt.rest.mapper.spi.query.ListingOptionsMapper;

public class DefaultListingOptionsMapper implements ListingOptionsMapper {

  @Override
  public ListingOptions toListingOptions(ListingOptionsData listingOptionsData) {
    final Integer start, end;

    if (listingOptionsData.hasPagination()) {
      start = listingOptionsData.getPage() * listingOptionsData.getPageSize();
      end = start + listingOptionsData.getPageSize() - 1;
    } else {
      start = end = null;
    }

    return new ListingOptions(start, end);
  }
}
