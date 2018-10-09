package org.sing_group.dreimt.rest.mapper.signature;

import org.sing_group.dreimt.domain.entities.signature.ArticleMetadata;
import org.sing_group.dreimt.rest.entity.signature.ArticleMetadataData;
import org.sing_group.dreimt.rest.mapper.spi.signature.ArticleMetadataMapper;

public class DefaultArticleMetadataMapper implements ArticleMetadataMapper {

  @Override
  public ArticleMetadataData toArticleMetadataData(ArticleMetadata articleMetadata) {
    return new ArticleMetadataData(
      articleMetadata.getPubmedId(), articleMetadata.getTitle(), articleMetadata.getAuthors(),
      articleMetadata.getArticleAbstract()
    );
  }
}
