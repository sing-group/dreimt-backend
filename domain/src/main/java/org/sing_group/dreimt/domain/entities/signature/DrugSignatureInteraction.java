package org.sing_group.dreimt.domain.entities.signature;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "drug_signature_interaction")
public class DrugSignatureInteraction implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
    @JoinColumn(
      name = "drug_sourceName", referencedColumnName = "sourceName"
    ),
    @JoinColumn(
      name = "drug_sourceDb", referencedColumnName = "sourceDb"
    )
  })
  private Drug drug;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "signature", referencedColumnName = "signatureName", nullable = false)
  private Signature signature;

  private double tes;
  private double pValue;
  private double fdr;

  public DrugSignatureInteraction() {}

  public Drug getDrug() {
    return drug;
  }

  public Signature getSignature() {
    return signature;
  }

  public double getTes() {
    return tes;
  }

  public double getpValue() {
    return pValue;
  }

  public double getFdr() {
    return fdr;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
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
    DrugSignatureInteraction other = (DrugSignatureInteraction) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
