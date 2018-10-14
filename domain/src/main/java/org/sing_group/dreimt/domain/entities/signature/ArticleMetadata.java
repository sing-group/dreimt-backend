package org.sing_group.dreimt.domain.entities.signature;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "article_metadata")
public class ArticleMetadata {

  @Id
  private int pubmedId;

  private String title;

  @Column(nullable = false, length = 2000)
  private String authors;

  @Column(nullable = true, length = 10000)
  private String articleAbstract;

  ArticleMetadata() {}

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + pubmedId;
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
    ArticleMetadata other = (ArticleMetadata) obj;
    if (pubmedId != other.pubmedId)
      return false;
    return true;
  }
}
