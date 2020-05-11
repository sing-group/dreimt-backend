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
  private final String cellType1;
  private final String cellSubType1;
  private final String cellType2;
  private final String cellSubType2;
  private final ExperimentalDesign experimentalDesign;
  private final String organism;
  private final String disease;
  private final SignatureType signatureType;
  private final String sourceDb;
  private final Integer signaturePubMedId;
  private final Set<String> mandatoryGenes;
  private final String cellType1Treatment;
  private final String cellType1Disease;

  public SignatureListingOptions() {
    this(null, null, null, null, null, null, null, null, null, null, null, null, null);
  }

  public SignatureListingOptions(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String sourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String cellType1Treatment, String cellType1Disease
  ) {
    this(
      noModification(), signatureName, cellType1, cellSubType1, cellType2, cellSubType2, experimentalDesign, organism,
      disease, sourceDb, signatureType, signaturePubMedId, emptySet(), cellType1Treatment, cellType1Disease
    );
  }

  public SignatureListingOptions(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String sourceDb, Integer signaturePubMedId,
    String cellType1Treatment, String cellType1Disease
  ) {
    this(
      noModification(), signatureName, cellType1, cellSubType1, cellType2, cellSubType2, experimentalDesign, organism,
      disease, sourceDb, null, signaturePubMedId, emptySet(), cellType1Treatment, cellType1Disease
    );
  }

  public SignatureListingOptions(
    ListingOptions listingOptions, String signatureName, String cellType1, String cellSubType1, String cellType2,
    String cellSubType2, ExperimentalDesign experimentalDesign, String organism, String disease, String sourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String cellType1Treatment, String cellType1Disease
  ) {
    this(
      listingOptions, signatureName, cellType1, cellSubType1, cellType2, cellSubType2, experimentalDesign, organism,
      disease, sourceDb, signatureType, signaturePubMedId, emptySet(), cellType1Treatment, cellType1Disease
    );
  }

  public SignatureListingOptions(
    ListingOptions listingOptions, String signatureName, String cellType1, String cellSubType1, String cellType2,
    String cellSubType2, ExperimentalDesign experimentalDesign, String organism, String disease, String sourceDb,
    SignatureType signatureType, Integer signaturePubMedId, Set<String> mandatoryGenes, String cellType1Treatment,
    String cellType1Disease
  ) {
    validateCellTypes(cellType1, cellSubType1, cellType2, cellSubType2);
    validCellType1Modifiers(cellType1, cellType1Treatment, cellType1Disease);

    this.listingOptions = listingOptions;
    this.signatureName = signatureName;
    this.cellType1 = cellType1;
    this.cellSubType1 = cellSubType1;
    this.cellType2 = cellType2;
    this.cellSubType2 = cellSubType2;
    this.experimentalDesign = experimentalDesign;
    this.organism = organism;
    this.disease = disease;
    this.signatureType = signatureType;
    this.sourceDb = sourceDb;
    this.signaturePubMedId = signaturePubMedId;
    this.mandatoryGenes = mandatoryGenes;
    this.cellType1Treatment = cellType1Treatment;
    this.cellType1Disease = cellType1Disease;
  }

  private static void validateCellTypes(String cellType1, String cellSubType1, String cellType2, String cellSubType2) {
    if (cellType1 == null && cellType2 != null) {
      throw new IllegalArgumentException("cellType1 is required when cellType2 is present");
    }
    if (cellSubType1 == null && cellSubType2 != null) {
      throw new IllegalArgumentException("cellSubType1 is required when cellSubType2 is present");
    }
  }

  private static void validCellType1Modifiers(String cellType1, String cellType1Treatment, String cellType1Disease) {
    if (cellType1 == null && cellType1Disease != null) {
      throw new IllegalArgumentException("cellType1 is required when cellType1Disease is present");
    }

    if (cellType1 == null && cellType1Treatment != null) {
      throw new IllegalArgumentException("cellType1 is required when cellType1Treatment is present");
    }
  }

  public boolean hasAnyQueryModification() {
    return this.listingOptions.hasAnyQueryModification()
      || this.signatureName != null
      || this.cellType1 != null
      || this.cellSubType1 != null
      || this.cellType2 != null
      || this.cellSubType2 != null
      || this.organism != null
      || this.disease != null
      || this.experimentalDesign != null
      || this.signatureType != null
      || this.sourceDb != null
      || this.signaturePubMedId != null
      || this.mandatoryGenes != null
      || this.cellType1Disease != null
      || this.cellType1Treatment != null;
  }

  public ListingOptions getListingOptions() {
    return listingOptions;
  }

  public Optional<String> getSignatureName() {
    return ofNullable(signatureName);
  }

  public Optional<String> getCellType1() {
    return ofNullable(cellType1);
  }

  public Optional<String> getCellSubType1() {
    return ofNullable(cellSubType1);
  }
  
  public Optional<String> getCellType2() {
    return ofNullable(cellType2);
  }
  
  public Optional<String> getCellSubType2() {
    return ofNullable(cellSubType2);
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
  
  public Optional<String> getCellType1Disease() {
    return ofNullable(cellType1Disease);
  }
  
  public Optional<String> getCellType1Treatment() {
    return ofNullable(cellType1Treatment);
  }

  public SignatureListingOptions withMandatoryGenes(Set<String> mandatoryGenes) {
    return new SignatureListingOptions(
      listingOptions, signatureName, cellType1, cellSubType1, cellType2, cellSubType2, experimentalDesign, organism,
      disease, sourceDb, signatureType, signaturePubMedId, mandatoryGenes, cellType1Treatment, cellType1Disease
    );
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((cellSubType1 == null) ? 0 : cellSubType1.hashCode());
    result = prime * result + ((cellSubType2 == null) ? 0 : cellSubType2.hashCode());
    result = prime * result + ((cellType1 == null) ? 0 : cellType1.hashCode());
    result = prime * result + ((cellType2 == null) ? 0 : cellType2.hashCode());
    result = prime * result + ((cellType1Treatment == null) ? 0 : cellType1.hashCode());
    result = prime * result + ((cellType1Disease == null) ? 0 : cellType2.hashCode());
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
    if (cellSubType1 == null) {
      if (other.cellSubType1 != null)
        return false;
    } else if (!cellSubType1.equals(other.cellSubType1))
      return false;
    if (cellSubType2 == null) {
      if (other.cellSubType2 != null)
        return false;
    } else if (!cellSubType2.equals(other.cellSubType2))
      return false;
    if (cellType1 == null) {
      if (other.cellType1 != null)
        return false;
    } else if (!cellType1.equals(other.cellType1))
      return false;
    if (cellType2 == null) {
      if (other.cellType2 != null)
        return false;
    } else if (!cellType2.equals(other.cellType2))
      return false;
    if (cellType1Treatment == null) {
      if (other.cellType1Treatment != null)
        return false;
    } else if (!cellType1Treatment.equals(other.cellType1Treatment))
      return false;
    if (cellType1Disease == null) {
      if (other.cellType1Disease != null)
        return false;
    } else if (!cellType1Disease.equals(other.cellType1Disease))
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
