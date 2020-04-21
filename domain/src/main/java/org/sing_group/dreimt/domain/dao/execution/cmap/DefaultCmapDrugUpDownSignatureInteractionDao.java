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
import org.sing_group.dreimt.domain.dao.spi.execution.cmap.CmapDrugUpDownSignatureInteractionDao;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugUpDownSignatureInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapUpDownSignatureDrugInteractionField;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapUpDownSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.Drug;

@Default
@Transactional(MANDATORY)
public class DefaultCmapDrugUpDownSignatureInteractionDao implements CmapDrugUpDownSignatureInteractionDao {

  @PersistenceContext
  protected EntityManager em;
  protected DaoHelper<Integer, CmapDrugUpDownSignatureInteraction> dh;

  public DefaultCmapDrugUpDownSignatureInteractionDao() {
    super();
  }

  public DefaultCmapDrugUpDownSignatureInteractionDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DaoHelper.of(Integer.class, CmapDrugUpDownSignatureInteraction.class, this.em);
  }

  @Override
  public Stream<CmapDrugUpDownSignatureInteraction> list(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  ) {
    final CriteriaBuilder cb = dh.cb();

    CriteriaQuery<CmapDrugUpDownSignatureInteraction> query = dh.createCBQuery();
    final Root<CmapDrugUpDownSignatureInteraction> root = query.from(dh.getEntityType());
    final Predicate[] predicates = createPredicates(listingOptions, root, cmapResult);

    query = query.select(root).where(predicates);

    ListingOptions generalListingOptions = listingOptions.getListingOptions();

    if (generalListingOptions.hasOrder()) {
      List<Order> orders = new LinkedList<>();

      for (SortField sortField : generalListingOptions.getSortFields().collect(toList())) {
        CmapUpDownSignatureDrugInteractionField field =
          CmapUpDownSignatureDrugInteractionField.valueOf(sortField.getSortField());

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

          case UP_FDR:
            orders.add(order.apply(root.get("upFdr")));
            break;
          case DOWN_FDR:
            orders.add(order.apply(root.get("downFdr")));
            break;
          case TAU:
            orders.add(order.apply(root.get("tau")));
            Expression<Double> greatestPred =
              cb.function(
                "least", Double.class,
                cb.coalesce(root.get("upFdr"), 1), cb.coalesce(root.get("downFdr"), 1)
              );
            orders.add(cb.asc(greatestPred));
            break;

          case NONE:
            break;
        }
      }

      query = query.orderBy(orders);
    }

    TypedQuery<CmapDrugUpDownSignatureInteraction> typedQuery = em.createQuery(query);
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
    CmapDrugUpDownSignatureInteractionListingOptions listingOptions, Root<CmapDrugUpDownSignatureInteraction> root,
    CmapUpDownSignatureResult cmapResult
  ) {
    final CriteriaBuilder cb = this.dh.cb();
    final List<Predicate> andPredicates = new ArrayList<>();

    andPredicates.add(cb.equal(root.get("cmapResult"), cmapResult));

    if (listingOptions.getMinTau().isPresent()) {
      final Path<Double> tau = root.get("tau");

      andPredicates.add(cb.greaterThanOrEqualTo(cb.abs(tau), listingOptions.getMinTau().get()));
    }

    if (listingOptions.getMaxUpFdr().isPresent()) {
      final Path<Double> upFdr = root.get("upFdr");

      andPredicates.add(cb.lessThanOrEqualTo(upFdr, listingOptions.getMaxUpFdr().get()));
    }

    if (listingOptions.getMaxDownFdr().isPresent()) {
      final Path<Double> downFdr = root.get("downFdr");

      andPredicates.add(cb.lessThanOrEqualTo(downFdr, listingOptions.getMaxDownFdr().get()));
    }

    Join<CmapDrugUpDownSignatureInteraction, Drug> joinDrug = root.join("drug", JoinType.LEFT);
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

    if (listingOptions.getDrugMoa().isPresent()) {
      Join<Drug, String> joinDrugMoa = joinDrug.join("moa", JoinType.LEFT);

      andPredicates.add(cb.like(joinDrugMoa, "%" + listingOptions.getDrugMoa().get() + "%"));
    }

    return andPredicates.toArray(new Predicate[andPredicates.size()]);
  }

  @Override
  public long count(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  ) {
    final CriteriaBuilder cb = dh.cb();

    CriteriaQuery<Long> query = cb.createQuery(Long.class);
    final Root<CmapDrugUpDownSignatureInteraction> root = query.from(dh.getEntityType());

    query = query.select(cb.count(root)).where(createPredicates(listingOptions, root, cmapResult));

    return this.em.createQuery(query).getSingleResult();
  }

  @Override
  public Stream<String> listDrugSourceNameValues(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  ) {
    return listDrugColumnValues(cmapResult, listingOptions, "sourceName");
  }

  @Override
  public Stream<String> listDrugSourceDbValues(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  ) {
    return listDrugColumnValues(cmapResult, listingOptions, "sourceDb");
  }

  @Override
  public Stream<String> listDrugCommonNameValues(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  ) {
    return listDrugColumnValues(cmapResult, listingOptions, "commonName");
  }
  
  @Override
  public Stream<String> listDrugMoaValues(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  ) {
    return listElementCollectionValues(cmapResult, listingOptions, "moa");
  }

  private Stream<String> listElementCollectionValues(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions,
    String columnName
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<String> query = cb.createQuery(String.class);
    final Root<CmapDrugUpDownSignatureInteraction> root = query.from(dh.getEntityType());
    final Join<CmapDrugUpDownSignatureInteraction, Drug> drugJoin = root.join("drug", JoinType.LEFT);
    final Join<Drug, ?> join = drugJoin.join(columnName);

    query = query.multiselect(join.as(String.class)).distinct(true);

    if (listingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(listingOptions, root, cmapResult));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  public Stream<String> listDrugColumnValues(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions,
    String columnName
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<String> query = cb.createQuery(String.class);
    final Root<CmapDrugUpDownSignatureInteraction> root = query.from(dh.getEntityType());
    final Join<CmapDrugUpDownSignatureInteraction, Drug> join = root.join("drug", JoinType.LEFT);

    query = query.select(join.get(columnName)).distinct(true);

    query = query.where(createPredicates(listingOptions, root, cmapResult));

    return this.em.createQuery(query).getResultList().stream();
  }
}
