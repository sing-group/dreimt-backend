/*-
 * #%L
 * DREIMT - Domain
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
package org.sing_group.dreimt.domain.dao;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public class ListingOptionsQueryBuilder {
  private final ListingOptions options;

  public ListingOptionsQueryBuilder(ListingOptions options) {
    this.options = requireNonNull(options, "options can't be null");
  }
  
  public <T> CriteriaQuery<T> addOrder(CriteriaBuilder cb, CriteriaQuery<T> query, Root<T> root) {
    if (options.hasOrder()) {
      final List<Order> order = this.options.getSortFields()
        .map(sortField -> {
          switch (sortField.getSortDirection()) {
            case ASCENDING:
              return cb.asc(root.get(sortField.getSortField()));
            case DESCENDING:
              return cb.desc(root.get(sortField.getSortField()));
            default:
              throw new IllegalStateException("Invalid sort direction: " + sortField.getSortDirection());
          }
        })
      .collect(toList());
      
      return query.orderBy(order);
    } else {
      return query;
    }
  }
  
  public <T> TypedQuery<T> addLimits(TypedQuery<T> query) {
    if (this.options.hasResultLimits()) {
      return query
        .setFirstResult(options.getStart().getAsInt())
        .setMaxResults(options.getMaxResults().getAsInt());
    } else {
      return query;
    }
  }
}
