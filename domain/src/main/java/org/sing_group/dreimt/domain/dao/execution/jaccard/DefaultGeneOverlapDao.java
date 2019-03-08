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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.DaoHelper;
import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.dao.ListingOptions.SortField;
import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.GeneOverlapDao;
import org.sing_group.dreimt.domain.entities.execution.jaccard.GeneOverlap;
import org.sing_group.dreimt.domain.entities.execution.jaccard.GeneOverlapField;
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
  public Stream<GeneOverlap> list(JaccardResult jaccardResult, GeneOverlapListingOptions listingOptions) {
    final CriteriaBuilder cb = dh.cb();

    CriteriaQuery<GeneOverlap> query = dh.createCBQuery();
    final Root<GeneOverlap> root = query.from(dh.getEntityType());
    final Predicate[] predicates = createPredicates(listingOptions, root, jaccardResult);

    query = query.select(root).where(predicates);

    ListingOptions generalListingOptions = getListingOptions(listingOptions.getListingOptions());

    if (generalListingOptions.hasOrder()) {
      List<Order> orders = new LinkedList<>();

      for (SortField sortField : generalListingOptions.getSortFields().collect(toList())) {
        GeneOverlapField field = GeneOverlapField.valueOf(sortField.getSortField());
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
          case TARGET_SIGNATURE:
            orders.add(order.apply(root.get("targetSignature")));
            break;
          case SOURCE_COMPARISON_TYPE:
            orders.add(order.apply(root.get("sourceComparisonType")));
            break;
          case TARGET_COMPARISON_TYPE:
            orders.add(order.apply(root.get("targetComparisonType")));
            break;

          case FDR:
            orders.add(order.apply(root.get("fdr")));
            break;
          case P_VALUE:
            orders.add(order.apply(root.get("pValue")));
            break;
          case JACCARD:
            orders.add(order.apply(root.get("jaccard")));
            break;

          case ID:
            orders.add(order.apply(root.get("id")));
            break;

          case NONE:
            break;
        }
      }

      query = query.orderBy(orders);
    }

    TypedQuery<GeneOverlap> typedQuery = em.createQuery(query);
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

  private static ListingOptions getListingOptions(ListingOptions listingOptions) {
    return listingOptions.getSortFields().count() > 0 ? listingOptions
      : new ListingOptions(
        listingOptions.getStart().isPresent() ? listingOptions.getStart().getAsInt() : null,
        listingOptions.getEnd().isPresent() ? listingOptions.getEnd().getAsInt() : null,
        SortField.ascending(GeneOverlapField.ID.name())
      );
  }

  @Override
  public long count(JaccardResult jaccardResult, GeneOverlapListingOptions listingOptions) {
    final CriteriaBuilder cb = dh.cb();

    CriteriaQuery<Long> query = cb.createQuery(Long.class);
    final Root<GeneOverlap> root = query.from(dh.getEntityType());

    query = query.select(cb.count(root)).where(createPredicates(listingOptions, root, jaccardResult));

    return this.em.createQuery(query).getSingleResult();
  }

  private Predicate[] createPredicates(
    GeneOverlapListingOptions listingOptions, Root<GeneOverlap> root, JaccardResult jaccardResult
  ) {
    final CriteriaBuilder cb = this.dh.cb();
    final List<Predicate> andPredicates = new ArrayList<>();

    andPredicates.add(cb.equal(root.get("jaccardResult"), jaccardResult));

    if (listingOptions.getMaxPvalue().isPresent()) {
      final Path<Double> pValue = root.get("pValue");

      andPredicates.add(cb.lessThanOrEqualTo(pValue, listingOptions.getMaxPvalue().get()));
    }

    if (listingOptions.getMaxFdr().isPresent()) {
      final Path<Double> fdr = root.get("fdr");

      andPredicates.add(cb.lessThanOrEqualTo(fdr, listingOptions.getMaxFdr().get()));
    }

    if (listingOptions.getMinJaccard().isPresent()) {
      final Path<Double> tes = root.get("jaccard");

      andPredicates.add(cb.greaterThanOrEqualTo(tes, listingOptions.getMinJaccard().get()));
    }

    return andPredicates.toArray(new Predicate[andPredicates.size()]);
  }
}
