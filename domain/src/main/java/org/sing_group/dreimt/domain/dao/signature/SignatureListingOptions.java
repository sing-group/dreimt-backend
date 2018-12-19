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

import static java.util.Collections.emptySet;
import static java.util.Optional.ofNullable;
import static org.sing_group.dreimt.domain.dao.ListingOptions.noModification;

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;

public class SignatureListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private final ListingOptions listingOptions;
  private final String signatureName;
  private final String cellTypeA;
  private final String cellSubTypeA;
  private final String cellTypeB;
  private final String cellSubTypeB;
  private final ExperimentalDesign experimentalDesign;
  private final String organism;
  private final String disease;
  private final SignatureType signatureType;
  private final String sourceDb;
  private final Integer signaturePubMedId;
  private final Set<String> mandatoryGenes;

  public SignatureListingOptions(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String sourceDb,
    SignatureType signatureType, Integer signaturePubMedId
  ) {
    this(
      noModification(), signatureName, cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB, experimentalDesign, organism,
      disease, sourceDb, signatureType, signaturePubMedId, emptySet()
    );
  }

  public SignatureListingOptions(
    ListingOptions listingOptions, String signatureName,  String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String sourceDb, SignatureType signatureType, Integer signaturePubMedId
  ) {
    this(
      listingOptions, signatureName, cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB, experimentalDesign, organism, disease, sourceDb,
      signatureType, signaturePubMedId, emptySet()
    );
  }

  public SignatureListingOptions(
    ListingOptions listingOptions, String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB,
    String cellSubTypeB, ExperimentalDesign experimentalDesign, String organism, String disease, String sourceDb,
    SignatureType signatureType, Integer signaturePubMedId, Set<String> mandatoryGenes
  ) {
    this.listingOptions = listingOptions;
    this.signatureName = signatureName;
    this.cellTypeA = cellTypeA;
    this.cellSubTypeA = cellSubTypeA;
    this.cellTypeB = cellTypeB;
    this.cellSubTypeB = cellSubTypeB;
    this.experimentalDesign = experimentalDesign;
    this.organism = organism;
    this.disease = disease;
    this.signatureType = signatureType;
    this.sourceDb = sourceDb;
    this.signaturePubMedId = signaturePubMedId;
    this.mandatoryGenes = mandatoryGenes;
  }

  public boolean hasAnyQueryModification() {
    return this.listingOptions.hasAnyQueryModification()
      || this.signatureName != null
      || this.cellTypeA != null
      || this.cellSubTypeA != null
      || this.cellTypeB != null
      || this.cellSubTypeB != null
      || this.organism != null
      || this.disease != null
      || this.experimentalDesign != null
      || this.signatureType != null
      || this.sourceDb != null
      || this.signaturePubMedId != null
      || this.mandatoryGenes != null;
  }

  public ListingOptions getListingOptions() {
    return listingOptions;
  }

  public Optional<String> getSignatureName() {
    return ofNullable(signatureName);
  }

  public Optional<String> getCellTypeA() {
    return ofNullable(cellTypeA);
  }

  public Optional<String> getCellSubTypeA() {
    return ofNullable(cellSubTypeA);
  }
  
  public Optional<String> getCellTypeB() {
    return ofNullable(cellTypeB);
  }
  
  public Optional<String> getCellSubTypeB() {
    return ofNullable(cellSubTypeB);
  }

  public Optional<ExperimentalDesign> getExperimentalDesign() {
    return ofNullable(experimentalDesign);
  }

  public Optional<String> getOrganism() {
    return ofNullable(organism);
  }

  public Optional<String> getDisease() {
    return ofNullable(disease);
  }

  public Optional<SignatureType> getSignatureType() {
    return ofNullable(signatureType);
  }

  public Optional<String> getSourceDb() {
    return ofNullable(sourceDb);
  }

  public Optional<Set<String>> getMandatoryGenes() {
    return ofNullable(mandatoryGenes);
  }
  
  public Optional<Integer> getSignaturePubMedId() {
    return ofNullable(signaturePubMedId);
  }

  public SignatureListingOptions withMandatoryGenes(Set<String> mandatoryGenes) {
    return new SignatureListingOptions(
      listingOptions, signatureName, cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB, experimentalDesign, organism,
      disease, sourceDb, signatureType, signaturePubMedId, mandatoryGenes
    );
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cellSubTypeA == null) ? 0 : cellSubTypeA.hashCode());
    result = prime * result + ((cellSubTypeB == null) ? 0 : cellSubTypeB.hashCode());
    result = prime * result + ((cellTypeA == null) ? 0 : cellTypeA.hashCode());
    result = prime * result + ((cellTypeB == null) ? 0 : cellTypeB.hashCode());
    result = prime * result + ((disease == null) ? 0 : disease.hashCode());
    result = prime * result + ((experimentalDesign == null) ? 0 : experimentalDesign.hashCode());
    result = prime * result + ((listingOptions == null) ? 0 : listingOptions.hashCode());
    result = prime * result + ((mandatoryGenes == null) ? 0 : mandatoryGenes.hashCode());
    result = prime * result + ((organism == null) ? 0 : organism.hashCode());
    result = prime * result + ((signatureName == null) ? 0 : signatureName.hashCode());
    result = prime * result + ((signaturePubMedId == null) ? 0 : signaturePubMedId.hashCode());
    result = prime * result + ((signatureType == null) ? 0 : signatureType.hashCode());
    result = prime * result + ((sourceDb == null) ? 0 : sourceDb.hashCode());
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
    SignatureListingOptions other = (SignatureListingOptions) obj;
    if (cellSubTypeA == null) {
      if (other.cellSubTypeA != null)
        return false;
    } else if (!cellSubTypeA.equals(other.cellSubTypeA))
      return false;
    if (cellSubTypeB == null) {
      if (other.cellSubTypeB != null)
        return false;
    } else if (!cellSubTypeB.equals(other.cellSubTypeB))
      return false;
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
    if (disease == null) {
      if (other.disease != null)
        return false;
    } else if (!disease.equals(other.disease))
      return false;
    if (experimentalDesign != other.experimentalDesign)
      return false;
    if (listingOptions == null) {
      if (other.listingOptions != null)
        return false;
    } else if (!listingOptions.equals(other.listingOptions))
      return false;
    if (mandatoryGenes == null) {
      if (other.mandatoryGenes != null)
        return false;
    } else if (!mandatoryGenes.equals(other.mandatoryGenes))
      return false;
    if (organism == null) {
      if (other.organism != null)
        return false;
    } else if (!organism.equals(other.organism))
      return false;
    if (signatureName == null) {
      if (other.signatureName != null)
        return false;
    } else if (!signatureName.equals(other.signatureName))
      return false;
    if (signaturePubMedId == null) {
      if (other.signaturePubMedId != null)
        return false;
    } else if (!signaturePubMedId.equals(other.signaturePubMedId))
      return false;
    if (signatureType != other.signatureType)
      return false;
    if (sourceDb == null) {
      if (other.sourceDb != null)
        return false;
    } else if (!sourceDb.equals(other.sourceDb))
      return false;
    return true;
  }
}
