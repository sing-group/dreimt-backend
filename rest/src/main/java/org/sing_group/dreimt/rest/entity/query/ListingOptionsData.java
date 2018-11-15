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
package org.sing_group.dreimt.rest.entity.query;

import static java.util.Arrays.asList;

import java.util.List;

import org.sing_group.dreimt.domain.dao.ListingOptions.SortField;

public class ListingOptionsData {
  private Integer page;
  private Integer pageSize;
  private final List<SortField> sortFields;

  ListingOptionsData() {
    this(null, null);
  }

  public ListingOptionsData(
    Integer page, Integer pageSize, SortField... sortFields
  ) {
    if (page == null ^ pageSize == null) {
      throw new IllegalArgumentException("page and pageSize must be used together");
    }

    this.page = page;
    this.pageSize = pageSize;
    this.sortFields = asList(sortFields);
  }

  public Integer getPage() {
    return page;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public boolean hasPagination() {
    return this.page != null && this.pageSize != null;
  }

  public List<SortField> getSortFields() {
    return sortFields;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((page == null) ? 0 : page.hashCode());
    result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
    result = prime * result + ((sortFields == null) ? 0 : sortFields.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ListingOptionsData other = (ListingOptionsData) obj;
    if (page == null) {
      if (other.page != null)
        return false;
    } else if (!page.equals(other.page))
      return false;
    if (pageSize == null) {
      if (other.pageSize != null)
        return false;
    } else if (!pageSize.equals(other.pageSize))
      return false;
    if (sortFields == null) {
      if (other.sortFields != null)
        return false;
    } else if (!sortFields.equals(other.sortFields))
      return false;
    return true;
  }
}
