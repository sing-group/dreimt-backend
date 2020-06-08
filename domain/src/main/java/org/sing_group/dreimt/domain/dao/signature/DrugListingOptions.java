package org.sing_group.dreimt.domain.dao.signature;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.Optional;

import org.sing_group.dreimt.domain.entities.signature.DrugStatus;

public class DrugListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String drugCommonName;
  private final String drugMoa;
  private final DrugStatus drugStatus;
  private final Double minDrugDss;

  public DrugListingOptions(String drugCommonName, String drugMoa, DrugStatus drugStatus, Double minDrugDss) {
    this.drugCommonName = drugCommonName;
    this.drugMoa = drugMoa;
    this.drugStatus = drugStatus;
    this.minDrugDss = minDrugDss;
  }

  public boolean hasAnyQueryModification() {
    return this.drugCommonName != null
      || this.drugMoa != null
      || this.drugStatus != null
      || this.minDrugDss != null;
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((drugCommonName == null) ? 0 : drugCommonName.hashCode());
    result = prime * result + ((drugMoa == null) ? 0 : drugMoa.hashCode());
    result = prime * result + ((drugStatus == null) ? 0 : drugStatus.hashCode());
    result = prime * result + ((minDrugDss == null) ? 0 : minDrugDss.hashCode());
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
    DrugListingOptions other = (DrugListingOptions) obj;
    if (drugCommonName == null) {
      if (other.drugCommonName != null)
        return false;
    } else if (!drugCommonName.equals(other.drugCommonName))
      return false;
    if (drugMoa == null) {
      if (other.drugMoa != null)
        return false;
    } else if (!drugMoa.equals(other.drugMoa))
      return false;
    if (drugStatus != other.drugStatus)
      return false;
    if (minDrugDss == null) {
      if (other.minDrugDss != null)
        return false;
    } else if (!minDrugDss.equals(other.minDrugDss))
      return false;
    return true;
  }
}
