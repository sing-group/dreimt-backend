package org.sing_group.dreimt.domain.dao.spi.signature;

import java.util.Optional;

import org.sing_group.dreimt.domain.entities.signature.ArticleMetadata;

public interface ArticleMetadataDao {
  public Optional<ArticleMetadata> get(int pubMedId);
}
