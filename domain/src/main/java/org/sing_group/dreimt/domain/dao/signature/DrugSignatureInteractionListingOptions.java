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
package org.sing_group.dreimt.domain.dao.signature;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.Optional;

import org.sing_group.dreimt.domain.dao.ListingOptions;

public class DrugSignatureInteractionListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private final ListingOptions listingOptions;
  private final SignatureListingOptions signatureListingOptions;
  private final String drugSourceName;
  private final String drugCommonName;
  private final Double maxPvalue;
  private final Double minTes;
  private final Double maxTes;
  private final Double maxFdr;

  public DrugSignatureInteractionListingOptions(
    ListingOptions listingOptions,
    SignatureListingOptions signatureListingOptions,
    String drugSourceName, String drugCommonName,
    Double pValue, Double minTes, Double maxTes, Double maxFdr
  ) {
    this.listingOptions = listingOptions;
    this.signatureListingOptions = signatureListingOptions;
    this.drugSourceName = drugSourceName;
    this.drugCommonName = drugCommonName;
    this.maxPvalue = pValue;
    this.minTes = minTes;
    this.maxTes = maxTes;
    this.maxFdr = maxFdr;
  }

  public boolean hasAnyQueryModification() {
    return this.listingOptions.hasResultLimits()
      || this.signatureListingOptions.hasAnyQueryModification()
      || this.drugSourceName != null
      || this.drugCommonName != null
      || this.maxPvalue != null
      || this.minTes != null
      || this.maxTes != null
      || this.maxFdr != null;
  }

  public ListingOptions getListingOptions() {
    return listingOptions;
  }

  public SignatureListingOptions getSignatureListingOptions() {
    return signatureListingOptions;
  }

  public Optional<String> getDrugSourceName() {
    return ofNullable(drugSourceName);
  }

  public Optional<String> getDrugCommonName() {
    return ofNullable(drugCommonName);
  }

  public Optional<Double> getMaxPvalue() {
    return ofNullable(maxPvalue);
  }

  public Optional<Double> getMinTes() {
    return ofNullable(minTes);
  }

  public Optional<Double> getMaxTes() {
    return ofNullable(maxTes);
  }

  public Optional<Double> getMaxFdr() {
    return ofNullable(maxFdr);
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
    result = prime * result + ((signatureListingOptions == null) ? 0 : signatureListingOptions.hashCode());
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
    DrugSignatureInteractionListingOptions other = (DrugSignatureInteractionListingOptions) obj;
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
    if (signatureListingOptions == null) {
      if (other.signatureListingOptions != null)
        return false;
    } else if (!signatureListingOptions.equals(other.signatureListingOptions))
      return false;
    return true;
  }
}
