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

import static java.util.Arrays.asList;
import static org.sing_group.fluent.checker.Checks.requireNonNegative;

import java.io.Serializable;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Stream;

public class ListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private final Integer start;
  private final Integer end;
  private final List<SortField> sortFields;

  public static ListingOptions noModification() {
    return new ListingOptions(null, null);
  }

  public static ListingOptions between(int start, int end) {
    return new ListingOptions(start, end);
  }

  public ListingOptions(Integer start, Integer end, SortField... sortFields) {
    this(start, end, asList(sortFields));
  }

  public ListingOptions(Integer start, Integer end, List<SortField> sortFields) {
    if (start == null ^ end == null) {
      throw new IllegalArgumentException("start and end must be used together");
    } else if (start != null) {
      requireNonNegative(start, "start can't be negative");
      requireNonNegative(end, "end can't be negative");

      if (start > end)
        throw new IllegalArgumentException("start should be lower or equal to end");
    }

    this.start = start;
    this.end = end;

    this.sortFields = sortFields;
  }

  public OptionalInt getStart() {
    return start == null ? OptionalInt.empty() : OptionalInt.of(start);
  }

  public OptionalInt getEnd() {
    return end == null ? OptionalInt.empty() : OptionalInt.of(end);
  }
  
  public boolean hasResultLimits() {
    return this.start != null;
  }
  
  public OptionalInt getMaxResults() {
    if (this.hasResultLimits()) {
      return OptionalInt.of(this.end - this.start + 1);
    } else {
      return OptionalInt.empty();
    }
  }
  
  public boolean hasOrder() {
    return !this.sortFields.isEmpty();
  }

  public Stream<SortField> getSortFields() {
    return sortFields.stream();
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((end == null) ? 0 : end.hashCode());
    result = prime * result + ((sortFields == null) ? 0 : sortFields.hashCode());
    result = prime * result + ((start == null) ? 0 : start.hashCode());
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
    ListingOptions other = (ListingOptions) obj;
    if (end == null) {
      if (other.end != null)
        return false;
    } else if (!end.equals(other.end))
      return false;
    if (sortFields == null) {
      if (other.sortFields != null)
        return false;
    } else if (!sortFields.equals(other.sortFields))
      return false;
    if (start == null) {
      if (other.start != null)
        return false;
    } else if (!start.equals(other.start))
      return false;
    return true;
  }

  public static class SortField implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final String sortField;
    private final SortDirection sortDirection;
    
    public static SortField ascending(String sortField) {
      return new SortField(sortField, SortDirection.ASCENDING);
    }
    
    public static SortField descending(String sortField) {
      return new SortField(sortField, SortDirection.DESCENDING);
    }
    
    public SortField(String sortField, SortDirection sortDirection) {
      super();
      this.sortField = sortField;
      this.sortDirection = sortDirection;
    }

    public String getSortField() {
      return sortField;
    }

    public SortDirection getSortDirection() {
      return sortDirection;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((sortDirection == null) ? 0 : sortDirection.hashCode());
      result = prime * result + ((sortField == null) ? 0 : sortField.hashCode());
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
      SortField other = (SortField) obj;
      if (sortDirection != other.sortDirection)
        return false;
      if (sortField == null) {
        if (other.sortField != null)
          return false;
      } else if (!sortField.equals(other.sortField))
        return false;
      return true;
    }
  }
}
