package org.sing_group.dreimt.rest.mapper.signature;

import java.util.HashSet;
import java.util.Set;

import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.domain.entities.signature.SignatureGene.Type;
import org.sing_group.dreimt.rest.entity.signature.SignatureGeneData;
import org.sing_group.dreimt.rest.mapper.spi.signature.SignatureGeneMapper;

public class DefaultSignatureGeneMapper implements SignatureGeneMapper {

  @Override
  public SignatureGeneData toSignatureGeneData(Signature signature) {
    Set<String> up = new HashSet<>();
    Set<String> down = new HashSet<>();
    signature.getSignatureGenes().stream().forEach(g -> {
      if (g.getType().equals(Type.UP)) {
        up.add(g.getGene());
      } else {
        down.add(g.getGene());
      }
    });
    return new SignatureGeneData(up, down);
  }
}
