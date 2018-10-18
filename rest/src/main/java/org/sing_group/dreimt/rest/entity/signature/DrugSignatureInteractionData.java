package org.sing_group.dreimt.rest.entity.signature;

import java.io.Serializable;

public class DrugSignatureInteractionData implements Serializable {
  private static final long serialVersionUID = 1L;

  private DrugData drug;
  private SignatureData signature;

  private double tes;
  private double pValue;
  private double fdr;

  DrugSignatureInteractionData() {}

  public DrugSignatureInteractionData(
    DrugData drug, SignatureData signature, double tes, double pValue, double fdr
  ) {
    super();
    this.drug = drug;
    this.signature = signature;
    this.tes = tes;
    this.pValue = pValue;
    this.fdr = fdr;
  }

  public DrugData getDrug() {
    return drug;
  }

  public SignatureData getSignature() {
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
}
