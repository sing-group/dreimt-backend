package org.sing_group.dreimt.rest.entity.signature;

import java.io.Serializable;

public class ArticleMetadataData implements Serializable {
  private static final long serialVersionUID = 1L;

  private int pubmedId;
  private String title;
  private String authors;
  private String articleAbstract;

  ArticleMetadataData() {}

  public ArticleMetadataData(int pubmedId, String title, String authors, String articleAbstract) {
    super();
    this.pubmedId = pubmedId;
    this.title = title;
    this.authors = authors;
    this.articleAbstract = articleAbstract;
  }

  public int getPubmedId() {
    return pubmedId;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthors() {
    return authors;
  }

  public String getArticleAbstract() {
    return articleAbstract;
  }
}
