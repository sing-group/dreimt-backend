/*-
 * #%L
 * DREIMT - Domain
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
 * 			Kevin Troulé, Gonzálo Gómez-López, Fátima Al-Shahrour
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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
