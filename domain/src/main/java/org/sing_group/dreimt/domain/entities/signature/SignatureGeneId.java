package org.sing_group.dreimt.domain.entities.signature;

import java.io.Serializable;

public class SignatureGeneId implements Serializable {
  private static final long serialVersionUID = 1L;

  private String signature;
  private String gene;

  public SignatureGeneId() {}

  public SignatureGeneId(String signature, String gene) {
    this.signature = signature;
    this.gene = gene;
  }

  public String getSignature() {
    return signature;
  }

  public String getGene() {
    return gene;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((gene == null) ? 0 : gene.hashCode());
    result = prime * result + ((signature == null) ? 0 : signature.hashCode());
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
    SignatureGeneId other = (SignatureGeneId) obj;
    if (gene == null) {
      if (other.gene != null)
        return false;
    } else if (!gene.equals(other.gene))
      return false;
    if (signature == null) {
      if (other.signature != null)
        return false;
    } else if (!signature.equals(other.signature))
      return false;
    return true;
  }
}