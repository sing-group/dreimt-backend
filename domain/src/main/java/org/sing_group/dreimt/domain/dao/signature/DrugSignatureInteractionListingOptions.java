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
import org.sing_group.dreimt.domain.entities.signature.DrugInteractionEffect;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;

public class DrugSignatureInteractionListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private final ListingOptions listingOptions;
  private final SignatureListingOptions signatureListingOptions;
  private final DrugListingOptions drugListingOptions;
  private final DrugSignatureInteractionType interactionType;
  private final Double minTau;
  private final Double maxUpFdr;
  private final Double maxDownFdr;
  private final DrugInteractionEffect cellType1Effect;

  public DrugSignatureInteractionListingOptions(
    SignatureListingOptions signatureListingOptions, DrugListingOptions drugListingOptions, DrugSignatureInteractionType interactionType,
    Double minTau, Double maxUpFdr,
    Double maxDownFdr, DrugInteractionEffect cellType1Effect
  ) {
    this(
      noModification(), signatureListingOptions, drugListingOptions, interactionType, minTau, maxUpFdr, maxDownFdr, cellType1Effect
    );
  }

  public DrugSignatureInteractionListingOptions(
    ListingOptions listingOptions, SignatureListingOptions signatureListingOptions, DrugListingOptions drugListingOptions,
    DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr, DrugInteractionEffect cellType1Effect
  ) {
    validateCellTypeAEffect(cellType1Effect, signatureListingOptions);

    this.listingOptions = listingOptions;
    this.signatureListingOptions = signatureListingOptions;
    this.interactionType = interactionType;
    this.drugListingOptions = drugListingOptions;
    this.minTau = minTau;
    this.maxUpFdr = maxUpFdr;
    this.maxDownFdr = maxDownFdr;
    this.cellType1Effect = cellType1Effect;
  }

  private static void validateCellTypeAEffect(
    DrugInteractionEffect cellType1Effect, SignatureListingOptions signatureListingOptions
  ) {
    if (cellType1Effect != null && !signatureListingOptions.getCellTypeAndSubType1Filter().isApplicable()) {
      throw new IllegalArgumentException("A cellType1 filter is required when cellType1Effect is present");
    }
  }

  public boolean hasAnyQueryModification() {
    return this.listingOptions.hasAnyQueryModification()
      || this.signatureListingOptions.hasAnyQueryModification()
      || this.drugListingOptions.hasAnyQueryModification()
      || this.hasAnyDrugSignatureInteractionQueryModification();
  }

  public boolean hasAnyDrugSignatureInteractionQueryModification() {
    return this.interactionType != null
      || this.minTau != null
      || this.maxUpFdr != null
      || this.maxDownFdr != null
      || this.cellType1Effect != null;
  }

  public ListingOptions getListingOptions() {
    return listingOptions;
  }

  public SignatureListingOptions getSignatureListingOptions() {
    return signatureListingOptions;
  }
  
  public DrugListingOptions getDrugListingOptions() {
    return drugListingOptions;
  }

  public Optional<DrugSignatureInteractionType> getInteractionType() {
    return ofNullable(interactionType);
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

  public Optional<DrugInteractionEffect> getCellType1Effect() {
    return ofNullable(cellType1Effect);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((interactionType == null) ? 0 : interactionType.hashCode());
    result = prime * result + ((listingOptions == null) ? 0 : listingOptions.hashCode());
    result = prime * result + ((maxDownFdr == null) ? 0 : maxDownFdr.hashCode());
    result = prime * result + ((maxUpFdr == null) ? 0 : maxUpFdr.hashCode());
    result = prime * result + ((minTau == null) ? 0 : minTau.hashCode());
    result = prime * result + ((cellType1Effect == null) ? 0 : cellType1Effect.hashCode());
    result = prime * result + ((signatureListingOptions == null) ? 0 : signatureListingOptions.hashCode());
    result = prime * result + ((drugListingOptions == null) ? 0 : drugListingOptions.hashCode());
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
    if (cellType1Effect == null) {
      if (other.cellType1Effect != null)
        return false;
    } else if (!cellType1Effect.equals(other.cellType1Effect))
      return false;
    if (signatureListingOptions == null) {
      if (other.signatureListingOptions != null)
        return false;
    } else if (!signatureListingOptions.equals(other.signatureListingOptions))
      return false;
    if (drugListingOptions == null) {
      if (other.drugListingOptions != null)
        return false;
    } else if (!drugListingOptions.equals(other.drugListingOptions))
      return false;
    return true;
  }
}
