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
package org.sing_group.dreimt.domain.dao.execution.cmap;

import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.DaoHelper;
import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.dao.spi.execution.cmap.CmapDrugInteractionDao;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapResult;
import org.sing_group.dreimt.domain.entities.signature.Drug;

@Default
@Transactional(MANDATORY)
public class DefaultCmapDrugInteractionDao implements CmapDrugInteractionDao {

  @PersistenceContext
  protected EntityManager em;
  protected DaoHelper<Integer, CmapDrugInteraction> dh;

  public DefaultCmapDrugInteractionDao() {
    super();
  }

  public DefaultCmapDrugInteractionDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DaoHelper.of(Integer.class, CmapDrugInteraction.class, this.em);
  }

  @Override
  public Stream<CmapDrugInteraction> list(CmapResult cmapResult, CmapDrugInteractionListingOptions listingOptions) {
    if (!listingOptions.hasAnyQueryModification()) {
      return this.dh.list().stream();
    } else {
      final CriteriaBuilder cb = dh.cb();

      CriteriaQuery<CmapDrugInteraction> query = dh.createCBQuery();
      final Root<CmapDrugInteraction> root = query.from(dh.getEntityType());

      query = query.select(root);

      final List<Predicate> andPredicates = new ArrayList<>();

      final Path<CmapResult> cmapResultPath = root.get("cmapResult");
      andPredicates.add(cb.equal(cmapResultPath, cmapResult));

      if (listingOptions.getMaxPvalue().isPresent()) {
        final Path<Double> pValue = root.get("pValue");

        andPredicates.add(cb.lessThanOrEqualTo(pValue, listingOptions.getMaxPvalue().get()));
      }

      if (listingOptions.getMaxFdr().isPresent()) {
        final Path<Double> fdr = root.get("fdr");

        andPredicates.add(cb.lessThanOrEqualTo(fdr, listingOptions.getMaxFdr().get()));
      }

      if (listingOptions.getMinTes().isPresent()) {
        final Path<Double> tes = root.get("tes");

        andPredicates.add(cb.greaterThanOrEqualTo(tes, listingOptions.getMinTes().get()));
      }

      if (listingOptions.getMaxTes().isPresent()) {
        final Path<Double> tes = root.get("tes");

        andPredicates.add(cb.lessThanOrEqualTo(tes, listingOptions.getMaxTes().get()));
      }

      Join<CmapDrugInteraction, Drug> joinDrug = root.join("drug", JoinType.LEFT);
      if (listingOptions.getDrugSourceName().isPresent()) {
        final Path<String> sourceName = joinDrug.get("sourceName");

        andPredicates.add(cb.like(sourceName, "%" + listingOptions.getDrugSourceName().get() + "%"));
      }

      if (listingOptions.getDrugCommonName().isPresent()) {
        final Path<String> commonName = joinDrug.get("commonName");

        andPredicates.add(cb.like(commonName, "%" + listingOptions.getDrugCommonName().get() + "%"));
      }

      query = query.where(andPredicates.toArray(new Predicate[andPredicates.size()]));

      ListingOptions generalListingOptions = listingOptions.getListingOptions();

      TypedQuery<CmapDrugInteraction> typedQuery = em.createQuery(query);
      if (generalListingOptions.hasResultLimits()) {
        final int start = generalListingOptions.getStart().getAsInt();
        final int end = generalListingOptions.getEnd().getAsInt();

        typedQuery =
          typedQuery
            .setFirstResult(start)
            .setMaxResults(end - start + 1);
      }

      return typedQuery.getResultList().stream();
    }
  }
}
