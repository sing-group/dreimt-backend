package org.sing_group.dreimt.rest.entity.query;

import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;

public class DrugSignatureInteractionListingOptionsData {
  private ListingOptionsData listingOptions;
  private String cellTypeA;
  private String cellTypeB;
  private ExperimentalDesign experimentalDesign;
  private String organism;
  private String drugSourceName;
  private String drugCommonName;

  DrugSignatureInteractionListingOptionsData() {}

  public DrugSignatureInteractionListingOptionsData(
    ListingOptionsData listingOptions,
    String cellTypeA, String cellTypeB,
    ExperimentalDesign experimentalDesign, String organism,
    String drugName, String drugCommonName
  ) {
    super();
    this.listingOptions = listingOptions;
    this.cellTypeA = cellTypeA;
    this.cellTypeB = cellTypeB;
    this.experimentalDesign = experimentalDesign;
    this.organism = organism;
    this.drugSourceName = drugName;
    this.drugCommonName = drugCommonName;
  }

  public ListingOptionsData getListingOptions() {
    return listingOptions;
  }

  public String getCellTypeA() {
    return cellTypeA;
  }

  public String getCellTypeB() {
    return cellTypeB;
  }

  public ExperimentalDesign getExperimentalDesign() {
    return experimentalDesign;
  }

  public String getOrganism() {
    return organism;
  }

  public String getDrugSourceName() {
    return drugSourceName;
  }

  public String getDrugCommonName() {
    return drugCommonName;
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
    DrugSignatureInteractionListingOptionsData other = (DrugSignatureInteractionListingOptionsData) obj;
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
