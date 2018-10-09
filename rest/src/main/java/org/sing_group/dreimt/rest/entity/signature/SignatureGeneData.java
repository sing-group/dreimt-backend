package org.sing_group.dreimt.rest.entity.signature;

import java.io.Serializable;

import org.sing_group.dreimt.domain.entities.signature.SignatureGene.Type;

public class SignatureGeneData implements Serializable {
  private static final long serialVersionUID = 1L;

  private String gene;
  private Type type;

  SignatureGeneData() {}

  public SignatureGeneData(String gene, Type type) {
    super();
    this.gene = gene;
    this.type = type;
  }

  public String getGene() {
    return gene;
  }

  public Type getType() {
    return type;
  }
}
