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
package org.sing_group.dreimt.domain.dao.execution.jaccard;

import static java.util.Arrays.asList;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.DaoHelper;
import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.dao.ListingOptions.SortField;
import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.GeneOverlapDao;
import org.sing_group.dreimt.domain.entities.execution.jaccard.GeneOverlap;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardResult;

@Default
@Transactional(MANDATORY)
public class DefaultGeneOverlapDao implements GeneOverlapDao {

  @PersistenceContext
  protected EntityManager em;
  protected DaoHelper<Integer, GeneOverlap> dh;

  public DefaultGeneOverlapDao() {
    super();
  }

  public DefaultGeneOverlapDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DaoHelper.of(Integer.class, GeneOverlap.class, this.em);
  }

  @Override
  public Stream<GeneOverlap> list(JaccardResult jaccardResult, ListingOptions listingOptions) {
    return this.dh.listBy("jaccardResult", asList(jaccardResult), getListingOptions(listingOptions)).stream();
  }

  private static ListingOptions getListingOptions(ListingOptions listingOptions) {
    return listingOptions.getSortFields().count() > 0 ? listingOptions
      : new ListingOptions(
        listingOptions.getStart().isPresent() ? listingOptions.getStart().getAsInt() : null,
        listingOptions.getEnd().isPresent() ? listingOptions.getEnd().getAsInt() : null,
        SortField.ascending("id")
      );
  }
}
