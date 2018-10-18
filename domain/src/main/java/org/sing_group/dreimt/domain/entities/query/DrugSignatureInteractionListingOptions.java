package org.sing_group.dreimt.domain.entities.query;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.Optional;

import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;

public class DrugSignatureInteractionListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private final ListingOptions listingOptions;
  private final String cellTypeA;
  private final String cellTypeB;
  private final ExperimentalDesign experimentalDesign;
  private final String organism;
  private final String drugSourceName;
  private final String drugCommonName;

  public DrugSignatureInteractionListingOptions(
    ListingOptions listingOptions,
    String cellTypeA, String cellTypeB,
    ExperimentalDesign experimentalDesign, String organism,
    String drugSourceName, String drugCommonName
  ) {
    this.listingOptions = listingOptions;
    this.cellTypeA = cellTypeA;
    this.cellTypeB = cellTypeB;
    this.experimentalDesign = experimentalDesign;
    this.organism = organism;
    this.drugSourceName = drugSourceName;
    this.drugCommonName = drugCommonName;
  }

  public boolean hasAnyQueryModification() {
    return getListingOptions().hasResultLimits()
      || this.cellTypeA != null
      || this.cellTypeB != null
      || this.organism != null
      || this.experimentalDesign != null
      || this.drugSourceName != null
      || this.drugCommonName != null;
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
    result = prime * result + ((organism == null) ? 0 : organism.hashCode());
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
    if (organism == null) {
      if (other.organism != null)
        return false;
    } else if (!organism.equals(other.organism))
      return false;
    return true;
  }
}
