package org.sing_group.dreimt.domain.entities.signature;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "signature_gene")
@IdClass(SignatureGeneId.class)
public class SignatureGene implements Serializable {
  private static final long serialVersionUID = 1L;

  public static enum Type {
    UP, DOWN
  };

  @Id
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "signature", referencedColumnName = "signatureName", nullable = false)
  private Signature signature;

  @Id
  private String gene;

  @Enumerated(EnumType.STRING)
  private Type type;

  SignatureGene() {}

  public Signature getSignature() {
    return signature;
  }

  public String getGene() {
    return gene;
  }

  public Type getType() {
    return type;
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
    SignatureGene other = (SignatureGene) obj;
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
