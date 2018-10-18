package org.sing_group.dreimt.rest.mapper.spi.signature;

import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.rest.entity.signature.SignatureGeneData;

public interface SignatureGeneMapper {
  public SignatureGeneData toSignatureGeneData(Signature signature);
}
