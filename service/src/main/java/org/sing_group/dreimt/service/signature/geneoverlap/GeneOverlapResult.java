package org.sing_group.dreimt.service.signature.geneoverlap;

public class GeneOverlapResult {
  private double jaccard;
  private double pValue;

  public GeneOverlapResult(double jaccard, double pValue) {
    super();
    this.jaccard = jaccard;
    this.pValue = pValue;
  }

  public double getJaccard() {
    return jaccard;
  }

  public double getpValue() {
    return pValue;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    long temp;
    temp = Double.doubleToLongBits(jaccard);
    result = prime * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(pValue);
    result = prime * result + (int) (temp ^ (temp >>> 32));
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
    GeneOverlapResult other = (GeneOverlapResult) obj;
    if (Double.doubleToLongBits(jaccard) != Double.doubleToLongBits(other.jaccard))
      return false;
    if (Double.doubleToLongBits(pValue) != Double.doubleToLongBits(other.pValue))
      return false;
    return true;
  }
}