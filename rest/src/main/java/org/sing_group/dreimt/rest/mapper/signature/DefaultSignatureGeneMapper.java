package org.sing_group.dreimt.rest.mapper.signature;

import org.sing_group.dreimt.domain.entities.signature.SignatureGene;
import org.sing_group.dreimt.rest.entity.signature.SignatureGeneData;
import org.sing_group.dreimt.rest.mapper.spi.signature.SignatureGeneMapper;

public class DefaultSignatureGeneMapper implements SignatureGeneMapper {

  @Override
  public SignatureGeneData toSignatureGeneData(SignatureGene signatureGene) {
    return new SignatureGeneData(signatureGene.getGene(), signatureGene.getType());
  }
}
