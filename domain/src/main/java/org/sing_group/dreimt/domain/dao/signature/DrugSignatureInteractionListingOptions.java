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
import static org.sing_group.dreimt.domain.dao.ListingOptions.noModification;

import java.io.Serializable;
import java.util.Optional;

import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;
import org.sing_group.dreimt.domain.entities.signature.DrugStatus;

public class DrugSignatureInteractionListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private final ListingOptions listingOptions;
  private final SignatureListingOptions signatureListingOptions;
  private final DrugSignatureInteractionType interactionType;
  private final String drugSourceName;
  private final String drugSourceDb;
  private final String drugCommonName;
  private final String drugMoa;
  private final DrugStatus drugStatus;
  private final Double minDrugDss;
  private final Double minTau;
  private final Double maxUpFdr;
  private final Double maxDownFdr;

  public DrugSignatureInteractionListingOptions(
    SignatureListingOptions signatureListingOptions, DrugSignatureInteractionType interactionType,
    String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa, DrugStatus drugStatus, Double minDrugDss, 
    Double minTau, Double maxUpFdr, Double maxDownFdr
  ) {
    this(
      noModification(), signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa,
      drugStatus, minDrugDss, minTau, maxUpFdr, maxDownFdr
    );
  }

  public DrugSignatureInteractionListingOptions(
    ListingOptions listingOptions, SignatureListingOptions signatureListingOptions,
    DrugSignatureInteractionType interactionType, String drugSourceName, String drugSourceDb, String drugCommonName,
    String drugMoa, DrugStatus drugStatus, Double minDrugDss, Double minTau, Double maxUpFdr, Double maxDownFdr
  ) {
    this.listingOptions = listingOptions;
    this.signatureListingOptions = signatureListingOptions;
    this.interactionType = interactionType;
    this.drugSourceName = drugSourceName;
    this.drugSourceDb = drugSourceDb;
    this.drugCommonName = drugCommonName;
    this.drugMoa = drugMoa;
    this.drugStatus = drugStatus;
    this.minDrugDss = minDrugDss;
    this.minTau = minTau;
    this.maxUpFdr = maxUpFdr;
    this.maxDownFdr = maxDownFdr;
  }

  public boolean hasAnyQueryModification() {
    return this.listingOptions.hasAnyQueryModification()
      || this.signatureListingOptions.hasAnyQueryModification()
      || this.interactionType != null
      || this.drugSourceName != null
      || this.drugSourceDb != null
      || this.drugCommonName != null
      || this.drugMoa != null
      || this.drugStatus != null
      || this.minDrugDss != null
      || this.minTau != null
      || this.maxUpFdr != null
      || this.maxDownFdr != null;
  }

  public ListingOptions getListingOptions() {
    return listingOptions;
  }

  public SignatureListingOptions getSignatureListingOptions() {
    return signatureListingOptions;
  }
  
  public Optional<DrugSignatureInteractionType> getInteractionType() {
    return ofNullable(interactionType);
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

  public Optional<String> getDrugMoa() {
    return ofNullable(drugMoa);
  }

  public Optional<DrugStatus> getDrugStatus() {
    return ofNullable(drugStatus);
  }

  public Optional<Double> getMinDrugDss() {
    return ofNullable(minDrugDss);
  }

  public Optional<Double> getMinTau() {
    return ofNullable(minTau);
  }

  public Optional<Double> getMaxUpFdr() {
    return ofNullable(maxUpFdr);
  }

  public Optional<Double> getMaxDownFdr() {
    return ofNullable(maxDownFdr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((drugCommonName == null) ? 0 : drugCommonName.hashCode());
    result = prime * result + ((drugSourceDb == null) ? 0 : drugSourceDb.hashCode());
    result = prime * result + ((drugSourceName == null) ? 0 : drugSourceName.hashCode());
    result = prime * result + ((drugMoa == null) ? 0 : drugMoa.hashCode());
    result = prime * result + ((drugStatus == null) ? 0 : drugStatus.hashCode());
    result = prime * result + ((minDrugDss == null) ? 0 : minDrugDss.hashCode());
    result = prime * result + ((interactionType == null) ? 0 : interactionType.hashCode());
    result = prime * result + ((listingOptions == null) ? 0 : listingOptions.hashCode());
    result = prime * result + ((maxDownFdr == null) ? 0 : maxDownFdr.hashCode());
    result = prime * result + ((maxUpFdr == null) ? 0 : maxUpFdr.hashCode());
    result = prime * result + ((minTau == null) ? 0 : minTau.hashCode());
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
    if (drugMoa == null) {
      if (other.drugMoa != null)
        return false;
    } else if (!drugMoa.equals(other.drugMoa))
      return false;
    if (drugStatus == null) {
      if (other.drugStatus != null)
        return false;
    } else if (!drugStatus.equals(other.drugStatus))
      return false;
    if (minDrugDss == null) {
      if (other.minDrugDss != null)
        return false;
    } else if (!minDrugDss.equals(other.minDrugDss))
      return false;
    if (interactionType != other.interactionType)
      return false;
    if (listingOptions == null) {
      if (other.listingOptions != null)
        return false;
    } else if (!listingOptions.equals(other.listingOptions))
      return false;
    if (maxDownFdr == null) {
      if (other.maxDownFdr != null)
        return false;
    } else if (!maxDownFdr.equals(other.maxDownFdr))
      return false;
    if (maxUpFdr == null) {
      if (other.maxUpFdr != null)
        return false;
    } else if (!maxUpFdr.equals(other.maxUpFdr))
      return false;
    if (minTau == null) {
      if (other.minTau != null)
        return false;
    } else if (!minTau.equals(other.minTau))
      return false;
    if (signatureListingOptions == null) {
      if (other.signatureListingOptions != null)
        return false;
    } else if (!signatureListingOptions.equals(other.signatureListingOptions))
      return false;
    return true;
  }
}
