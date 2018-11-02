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
package org.sing_group.dreimt.domain.entities.query;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.Optional;

import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;

public class DrugSignatureInteractionListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private final ListingOptions listingOptions;
  private final String cellTypeA;
  private final String cellTypeB;
  private final ExperimentalDesign experimentalDesign;
  private final String organism;
  private final String drugSourceName;
  private final String drugCommonName;
  private final SignatureType signatureType;
  private final Double maxPvalue;
  private final Double minTes;
  private final Double maxTes;
  private final Double maxFdr;

  public DrugSignatureInteractionListingOptions(
    ListingOptions listingOptions,
    String cellTypeA, String cellTypeB,
    ExperimentalDesign experimentalDesign, String organism,
    String drugSourceName, String drugCommonName,
    SignatureType signatureType,
    Double pValue, Double minTes, Double maxTes, Double maxFdr
  ) {
    this.listingOptions = listingOptions;
    this.cellTypeA = cellTypeA;
    this.cellTypeB = cellTypeB;
    this.experimentalDesign = experimentalDesign;
    this.organism = organism;
    this.drugSourceName = drugSourceName;
    this.drugCommonName = drugCommonName;
    this.signatureType = signatureType;
    this.maxPvalue = pValue;
    this.minTes = minTes;
    this.maxTes = maxTes;
    this.maxFdr = maxFdr;
  }

  public boolean hasAnyQueryModification() {
    return getListingOptions().hasResultLimits()
      || this.cellTypeA != null
      || this.cellTypeB != null
      || this.organism != null
      || this.experimentalDesign != null
      || this.drugSourceName != null
      || this.drugCommonName != null
      || this.signatureType != null
      || this.maxPvalue != null
      || this.minTes != null
      || this.maxTes != null
      || this.maxFdr != null;
  }

  public ListingOptions getListingOptions() {
    return listingOptions;
  }

  public Optional<String> getCellTypeA() {
    return ofNullable(cellTypeA);
  }

  public Optional<String> getCellTypeB() {
    return ofNullable(cellTypeB);
  }

  public Optional<ExperimentalDesign> getExperimentalDesign() {
    return ofNullable(experimentalDesign);
  }

  public Optional<String> getOrganism() {
    return ofNullable(organism);
  }

  public Optional<String> getDrugSourceName() {
    return ofNullable(drugSourceName);
  }

  public Optional<String> getDrugCommonName() {
    return ofNullable(drugCommonName);
  }

  public Optional<SignatureType> getSignatureType() {
    return ofNullable(signatureType);
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
    result = prime * result + ((cellTypeA == null) ? 0 : cellTypeA.hashCode());
    result = prime * result + ((cellTypeB == null) ? 0 : cellTypeB.hashCode());
    result = prime * result + ((drugCommonName == null) ? 0 : drugCommonName.hashCode());
    result = prime * result + ((drugSourceName == null) ? 0 : drugSourceName.hashCode());
    result = prime * result + ((experimentalDesign == null) ? 0 : experimentalDesign.hashCode());
    result = prime * result + ((listingOptions == null) ? 0 : listingOptions.hashCode());
    result = prime * result + ((maxFdr == null) ? 0 : maxFdr.hashCode());
    result = prime * result + ((maxPvalue == null) ? 0 : maxPvalue.hashCode());
    result = prime * result + ((maxTes == null) ? 0 : maxTes.hashCode());
    result = prime * result + ((minTes == null) ? 0 : minTes.hashCode());
    result = prime * result + ((organism == null) ? 0 : organism.hashCode());
    result = prime * result + ((signatureType == null) ? 0 : signatureType.hashCode());
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
    if (cellTypeA == null) {
      if (other.cellTypeA != null)
        return false;
    } else if (!cellTypeA.equals(other.cellTypeA))
      return false;
    if (cellTypeB == null) {
      if (other.cellTypeB != null)
        return false;
    } else if (!cellTypeB.equals(other.cellTypeB))
      return false;
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
    if (experimentalDesign != other.experimentalDesign)
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
    if (organism == null) {
      if (other.organism != null)
        return false;
    } else if (!organism.equals(other.organism))
      return false;
    if (signatureType != other.signatureType)
      return false;
    return true;
  }
}
