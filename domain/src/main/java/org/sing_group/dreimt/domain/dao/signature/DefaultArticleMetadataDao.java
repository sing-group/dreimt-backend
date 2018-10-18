package org.sing_group.dreimt.domain.dao.signature;

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.sing_group.dreimt.domain.dao.DAOHelper;
import org.sing_group.dreimt.domain.dao.spi.signature.ArticleMetadataDao;
import org.sing_group.dreimt.domain.entities.signature.ArticleMetadata;

public class DefaultArticleMetadataDao implements ArticleMetadataDao {

  @PersistenceContext
  private EntityManager em;

  private DAOHelper<Integer, ArticleMetadata> dh;

  DefaultArticleMetadataDao() {}

  public DefaultArticleMetadataDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, ArticleMetadata.class, this.em);
  }

  @Override
  public Optional<ArticleMetadata> get(int pubMedId) {
    try {
      return of(this.dh.getBy("pubmedId", pubMedId));
    } catch (NoResultException e) {
      return empty();
    }
  }
}
