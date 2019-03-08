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
package org.sing_group.dreimt.domain.dao.execution.cmap;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.Optional;

import org.sing_group.dreimt.domain.dao.ListingOptions;

public class CmapDrugGeneSetSignatureInteractionListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private final ListingOptions listingOptions;
  private final String drugSourceName;
  private final String drugSourceDb;
  private final String drugCommonName;
  private final Double minTau;
  private final Double maxFdr;

  public CmapDrugGeneSetSignatureInteractionListingOptions(
    ListingOptions listingOptions,
    String drugSourceName, String drugSourceDb, String drugCommonName,
    Double minTau, Double maxFdr
  ) {
    this.listingOptions = listingOptions;
    this.drugSourceName = drugSourceName;
    this.drugSourceDb = drugSourceDb;
    this.drugCommonName = drugCommonName;
    this.minTau = minTau;
    this.maxFdr = maxFdr;
  }

  public boolean hasAnyQueryModification() {
    return this.listingOptions.hasAnyQueryModification()
      || this.drugSourceName != null
      || this.drugSourceDb != null
      || this.drugCommonName != null
      || this.minTau != null
      || this.maxFdr != null;
  }

  public ListingOptions getListingOptions() {
    return listingOptions;
  }

  public Optional<String> getDrugSourceName() {
    return ofNullable(drugSourceName);
  }

  public Optional<String> getDrugSourceDb() {
    return ofNullable(drugSourceDb);
  }

  public Optional<String> getDrugCommonName() {
    return ofNullable(drugCommonName);
  }

  public Optional<Double> getMinTau() {
    return ofNullable(minTau);
  }

  public Optional<Double> getMaxFdr() {
    return ofNullable(maxFdr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((drugCommonName == null) ? 0 : drugCommonName.hashCode());
    result = prime * result + ((drugSourceDb == null) ? 0 : drugSourceDb.hashCode());
    result = prime * result + ((drugSourceName == null) ? 0 : drugSourceName.hashCode());
    result = prime * result + ((listingOptions == null) ? 0 : listingOptions.hashCode());
    result = prime * result + ((maxFdr == null) ? 0 : maxFdr.hashCode());
    result = prime * result + ((minTau == null) ? 0 : minTau.hashCode());
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
    CmapDrugGeneSetSignatureInteractionListingOptions other = (CmapDrugGeneSetSignatureInteractionListingOptions) obj;
    if (drugCommonName == null) {
      if (other.drugCommonName != null)
        return false;
    } else if (!drugCommonName.equals(other.drugCommonName))
      return false;
    if (drugSourceDb == null) {
      if (other.drugSourceDb != null)
        return false;
    } else if (!drugSourceDb.equals(other.drugSourceDb))
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
    if (minTau == null) {
      if (other.minTau != null)
        return false;
    } else if (!minTau.equals(other.minTau))
      return false;
    return true;
  }
}
