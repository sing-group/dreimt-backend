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
package org.sing_group.dreimt.rest.entity.query.cmap;

import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;

public class CmapDrugInteractionListingOptionsData {

  private ListingOptionsData listingOptions;
  private String drugSourceName;
  private String drugCommonName;
  private Double maxPvalue;
  private Double minTes;
  private Double maxTes;
  private Double maxFdr;

  CmapDrugInteractionListingOptionsData() {}

  public CmapDrugInteractionListingOptionsData(
    ListingOptionsData listingOptions, String drugSourceName, String drugCommonName, Double maxPvalue, Double minTes,
    Double maxTes, Double maxFdr
  ) {
    this.listingOptions = listingOptions;
    this.drugSourceName = drugSourceName;
    this.drugCommonName = drugCommonName;
    this.maxPvalue = maxPvalue;
    this.maxFdr = maxFdr;

    if (minTes != null && maxTes != null && minTes > maxTes) {
      throw new IllegalArgumentException("minTes must be less than or equal to maxTes");
    }
    this.minTes = minTes;
    this.maxTes = maxTes;
  }

  public ListingOptionsData getListingOptions() {
    return listingOptions;
  }

  public String getDrugSourceName() {
    return drugSourceName;
  }

  public String getDrugCommonName() {
    return drugCommonName;
  }

  public Double getMaxPvalue() {
    return maxPvalue;
  }

  public Double getMinTes() {
    return minTes;
  }

  public Double getMaxTes() {
    return maxTes;
  }

  public Double getMaxFdr() {
    return maxFdr;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((drugCommonName == null) ? 0 : drugCommonName.hashCode());
    result = prime * result + ((drugSourceName == null) ? 0 : drugSourceName.hashCode());
    result = prime * result + ((listingOptions == null) ? 0 : listingOptions.hashCode());
    result = prime * result + ((maxFdr == null) ? 0 : maxFdr.hashCode());
    result = prime * result + ((maxPvalue == null) ? 0 : maxPvalue.hashCode());
    result = prime * result + ((maxTes == null) ? 0 : maxTes.hashCode());
    result = prime * result + ((minTes == null) ? 0 : minTes.hashCode());
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
    CmapDrugInteractionListingOptionsData other = (CmapDrugInteractionListingOptionsData) obj;
    if (drugCommonName == null) {
      if (other.drugCommonName != null)
        return false;
    } else if (!drugCommonName.equals(other.drugCommonName))
      return false;
    if (drugSourceName == null) {
      if (other.drugSourceName != null)
        return false;
    } else if (!drugSourceName.equals(other.drugSourceName))
      return false;
    if (listingOptions == null) {
      if (other.listingOptions != null)
        return false;
    } else if (!listingOptions.equals(other.listingOptions))
      return false;
    if (maxFdr == null) {
      if (other.maxFdr != null)
        return false;
    } else if (!maxFdr.equals(other.maxFdr))
      return false;
    if (maxPvalue == null) {
      if (other.maxPvalue != null)
        return false;
    } else if (!maxPvalue.equals(other.maxPvalue))
      return false;
    if (maxTes == null) {
      if (other.maxTes != null)
        return false;
    } else if (!maxTes.equals(other.maxTes))
      return false;
    if (minTes == null) {
      if (other.minTes != null)
        return false;
    } else if (!minTes.equals(other.minTes))
      return false;
    return true;
  }
}
