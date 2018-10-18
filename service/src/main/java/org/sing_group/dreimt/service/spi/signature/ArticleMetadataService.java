package org.sing_group.dreimt.service.spi.signature;

import java.util.Optional;

import javax.ejb.Local;

import org.sing_group.dreimt.domain.entities.signature.ArticleMetadata;

@Local
public interface ArticleMetadataService {
  public Optional<ArticleMetadata> get(int pubMedId);
}
