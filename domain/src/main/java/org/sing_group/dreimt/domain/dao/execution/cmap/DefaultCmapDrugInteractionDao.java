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

import static java.util.stream.Collectors.toList;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.DaoHelper;
import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.dao.ListingOptions.SortField;
import org.sing_group.dreimt.domain.dao.spi.execution.cmap.CmapDrugInteractionDao;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugInteractionField;
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
    final CriteriaBuilder cb = dh.cb();

    CriteriaQuery<CmapDrugInteraction> query = dh.createCBQuery();
    final Root<CmapDrugInteraction> root = query.from(dh.getEntityType());
    final Predicate[] predicates = createPredicates(listingOptions, root, cmapResult);

    query = query.select(root).where(predicates);

    ListingOptions generalListingOptions = listingOptions.getListingOptions();

    if (generalListingOptions.hasOrder()) {
      List<Order> orders = new LinkedList<>();

      for (SortField sortField : generalListingOptions.getSortFields().collect(toList())) {
        CmapDrugInteractionField field = CmapDrugInteractionField.valueOf(sortField.getSortField());
        final Function<Expression<?>, Order> order;
        switch (sortField.getSortDirection()) {
          case ASCENDING:
            order = cb::asc;
            break;
          case DESCENDING:
            order = cb::desc;
            break;
          default:
            order = null;
        }
        switch (field) {
          case DRUG_SOURCE_NAME:
            orders.add(order.apply(root.join("drug").get("sourceName")));
            break;
          case DRUG_SOURCE_DB:
            orders.add(order.apply(root.join("drug").get("sourceDb")));
            break;
          case DRUG_COMMON_NAME:
            orders.add(order.apply(root.join("drug").get("commonName")));
            break;

          case FDR:
            orders.add(order.apply(root.get("fdr")));
            break;
          case P_VALUE:
            orders.add(order.apply(root.get("pValue")));
            break;
          case TES:
            orders.add(order.apply(root.get("tes")));
            break;

          case NONE:
            break;
        }
      }

      query = query.orderBy(orders);
    }

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

  private Predicate[] createPredicates(
    CmapDrugInteractionListingOptions listingOptions, Root<CmapDrugInteraction> root, CmapResult cmapResult
  ) {
    final CriteriaBuilder cb = this.dh.cb();
    final List<Predicate> andPredicates = new ArrayList<>();

    andPredicates.add(cb.equal(root.get("cmapResult"), cmapResult));

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

    if (listingOptions.getDrugSourceDb().isPresent()) {
      final Path<String> sourceDb = joinDrug.get("sourceDb");

      andPredicates.add(cb.like(sourceDb, "%" + listingOptions.getDrugSourceDb().get() + "%"));
    }

    if (listingOptions.getDrugCommonName().isPresent()) {
      final Path<String> commonName = joinDrug.get("commonName");

      andPredicates.add(cb.like(commonName, "%" + listingOptions.getDrugCommonName().get() + "%"));
    }

    return andPredicates.toArray(new Predicate[andPredicates.size()]);
  }

  @Override
  public long count(CmapResult cmapResult, CmapDrugInteractionListingOptions listingOptions) {
    final CriteriaBuilder cb = dh.cb();

    CriteriaQuery<Long> query = cb.createQuery(Long.class);
    final Root<CmapDrugInteraction> root = query.from(dh.getEntityType());

    query = query.select(cb.count(root)).where(createPredicates(listingOptions, root, cmapResult));

    return this.em.createQuery(query).getSingleResult();
  }

  @Override
  public Stream<String> listDrugSourceNameValues(
    CmapResult cmapResult, CmapDrugInteractionListingOptions listingOptions
  ) {
    return listDrugColumnValues(cmapResult, listingOptions, "sourceName");
  }

  @Override
  public Stream<String> listDrugSourceDbValues(
    CmapResult cmapResult, CmapDrugInteractionListingOptions listingOptions
  ) {
    return listDrugColumnValues(cmapResult, listingOptions, "sourceDb");
  }

  @Override
  public Stream<String> listDrugCommonNameValues(
    CmapResult cmapResult, CmapDrugInteractionListingOptions listingOptions
  ) {
    return listDrugColumnValues(cmapResult, listingOptions, "commonName");
  }

  public Stream<String> listDrugColumnValues(
    CmapResult cmapResult, CmapDrugInteractionListingOptions listingOptions, String columnName
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<String> query = cb.createQuery(String.class);
    final Root<CmapDrugInteraction> root = query.from(dh.getEntityType());
    final Join<CmapDrugInteraction, Drug> join = root.join("drug", JoinType.LEFT);

    query = query.select(join.get(columnName)).distinct(true);

    query = query.where(createPredicates(listingOptions, root, cmapResult));

    return this.em.createQuery(query).getResultList().stream();
  }
}
