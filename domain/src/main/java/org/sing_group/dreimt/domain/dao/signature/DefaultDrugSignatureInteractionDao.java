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

import static java.util.stream.Collectors.toList;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
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
import org.sing_group.dreimt.domain.dao.spi.signature.DrugSignatureInteractionDao;
import org.sing_group.dreimt.domain.entities.signature.ArticleMetadata;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionField;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;

@Default
@Transactional(MANDATORY)
public class DefaultDrugSignatureInteractionDao implements DrugSignatureInteractionDao {

  @PersistenceContext
  private EntityManager em;

  private DaoHelper<Integer, DrugSignatureInteraction> dh;

  DefaultDrugSignatureInteractionDao() {}

  public DefaultDrugSignatureInteractionDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DaoHelper.of(Integer.class, DrugSignatureInteraction.class, this.em);
  }

  @Override
  public Stream<DrugSignatureInteraction> list(DrugSignatureInteractionListingOptions listingOptions) {
    if (!listingOptions.hasAnyQueryModification()) {
      return this.dh.list().stream();
    } else {
      CriteriaQuery<DrugSignatureInteraction> query = dh.createCBQuery();
      final Root<DrugSignatureInteraction> root = query.from(dh.getEntityType());

      query =
        query.select(root)
          .where(createPredicates(listingOptions, root))
          .orderBy(createOrders(listingOptions, root));

      if (
        listingOptions.getListingOptions().hasOrder() && listingOptions.getListingOptions().getSortFields().map(
          s -> DrugSignatureInteractionField.valueOf(s.getSortField())
        ).filter(s -> s.isMultivaluated()).findAny().isPresent()
      ) {
        query = query.groupBy(root);
      }

      ListingOptions generalListingOptions = listingOptions.getListingOptions();

      TypedQuery<DrugSignatureInteraction> typedQuery = em.createQuery(query);
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

  @Override
  public long count(DrugSignatureInteractionListingOptions listingOptions) {
    if (!listingOptions.hasAnyQueryModification()) {
      return this.dh.count();
    } else {
      final CriteriaBuilder cb = dh.cb();

      CriteriaQuery<Long> query = cb.createQuery(Long.class);
      final Root<DrugSignatureInteraction> root = query.from(dh.getEntityType());
      final Predicate[] predicates = createPredicates(listingOptions, root);

      query = query.select(cb.count(root)).where(predicates);

      return this.em.createQuery(query).getSingleResult();
    }
  }

  @Override
  public Stream<String> listCellTypeAValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.listDoubleJoinColumnValues(String.class, "signature", "cellTypeA", listingOptions);
  }

  @Override
  public Stream<String> listCellSubTypeAValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.listDoubleJoinColumnValues(String.class, "signature", "cellSubTypeA", listingOptions);
  }

  @Override
  public Stream<String> listCellTypeBValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.listDoubleJoinColumnValues(String.class, "signature", "cellTypeB", listingOptions);
  }

  @Override
  public Stream<String> listCellSubTypeBValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.listDoubleJoinColumnValues(String.class, "signature", "cellSubTypeB", listingOptions);
  }
  
  @Override
  public Stream<String> listDiseaseValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.listDoubleJoinColumnValues(String.class, "signature", "disease", listingOptions);
  }

  private <T> Stream<T> listDoubleJoinColumnValues(
    Class<T> valueClass, String firstJoinAttribute, String secondJoinAttribute,
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    final CriteriaBuilder cb = dh.cb();

    CriteriaQuery<T> query = cb.createQuery(valueClass);
    final Root<DrugSignatureInteraction> root = query.from(dh.getEntityType());
    final Join<DrugSignatureInteraction, ?> join = root.join(firstJoinAttribute);
    final Join<?, ?> join2 = join.join(secondJoinAttribute);

    query = query.multiselect(join2.as(String.class)).distinct(true);

    if (listingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(listingOptions, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  @Override
  public Stream<ExperimentalDesign> listExperimentalDesignValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.listSingleJoinColumnValues(ExperimentalDesign.class, "signature", "experimentalDesign", listingOptions);
  }

  @Override
  public Stream<String> listSignatureNameValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.listSingleJoinColumnValues(String.class, "signature", "signatureName", listingOptions);
  }

  @Override
  public Stream<String> listOrganismValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.listSingleJoinColumnValues(String.class, "signature", "organism", listingOptions);
  }

  @Override
  public Stream<String> listSignatureSourceDbValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.listSingleJoinColumnValues(String.class, "signature", "sourceDb", listingOptions);
  }

  @Override
  public Stream<DrugSignatureInteractionType> listInteractionTypeValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.listColumnValues(DrugSignatureInteractionType.class, "interactionType", listingOptions);
  }

  private <T> Stream<T> listColumnValues(
    Class<T> targetClass, String columnName, DrugSignatureInteractionListingOptions listingOptions
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<T> query = cb.createQuery(targetClass);
    final Root<DrugSignatureInteraction> root = query.from(dh.getEntityType());

    query = query.select(root.get(columnName)).distinct(true);

    if (listingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(listingOptions, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  @Override
  public Stream<String> listDrugSourceNameValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.listSingleJoinColumnValues(String.class, "drug", "sourceName", listingOptions);
  }

  @Override
  public Stream<String> listDrugSourceDbValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.listSingleJoinColumnValues(String.class, "drug", "sourceDb", listingOptions);
  }

  @Override
  public Stream<String> listDrugCommonNameValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.listSingleJoinColumnValues(String.class, "drug", "commonName", listingOptions);
  }

  private <T> Stream<T> listSingleJoinColumnValues(
    Class<T> valueClass, String joinAttribute, String columnName,
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    final CriteriaBuilder cb = dh.cb();

    CriteriaQuery<T> query = cb.createQuery(valueClass);
    final Root<DrugSignatureInteraction> root = query.from(dh.getEntityType());
    final Join<DrugSignatureInteraction, ?> join = root.join(joinAttribute);

    query = query.select(join.get(columnName)).distinct(true);

    if (listingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(listingOptions, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  @Override
  public Stream<Integer> listSignaturePubMedIdValues(DrugSignatureInteractionListingOptions listingOptions) {
    final CriteriaBuilder cb = dh.cb();

    CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
    final Root<DrugSignatureInteraction> root = query.from(dh.getEntityType());
    final Join<DrugSignatureInteraction, Signature> join = root.join("signature");
    final Join<Signature, ArticleMetadata> join2 = join.join("articleMetadata", JoinType.LEFT);

    query = query.select(join2.get("pubmedId")).distinct(true);

    if (listingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(listingOptions, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  private Predicate[] createPredicates(
    final DrugSignatureInteractionListingOptions listingOptions,
    final Root<DrugSignatureInteraction> root
  ) {
    final CriteriaBuilder cb = this.dh.cb();

    final List<Predicate> andPredicates = new ArrayList<>();

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

    if (listingOptions.getInteractionType().isPresent()) {
      final Path<DrugSignatureInteractionType> interactionType = root.get("interactionType");

      andPredicates.add(cb.equal(interactionType, listingOptions.getInteractionType().get()));
    }

    final SignatureListingOptions signatureListingOptions = listingOptions.getSignatureListingOptions();
    final Join<DrugSignatureInteraction, Signature> joinSignature = root.join("signature", JoinType.LEFT);

    final BiConsumer<String, Optional<String>> joinLikeSignatureBuilder = (attributeName, queryValue) -> {
      if (queryValue.isPresent()) {
        final Path<String> field = joinSignature.join(attributeName);
        
        andPredicates.add(cb.like(cb.upper(field), "%" + queryValue.get().toUpperCase() + "%"));
      }
    };

    final BiConsumer<String, Optional<String>> likeSignatureBuilder = (attributeName, queryValue) -> {
      if (queryValue.isPresent()) {
        final Path<String> field = joinSignature.get(attributeName);
        
        andPredicates.add(cb.like(cb.upper(field), "%" + queryValue.get().toUpperCase() + "%"));
      }
    };

    joinLikeSignatureBuilder.accept("cellTypeA", signatureListingOptions.getCellTypeA());
    joinLikeSignatureBuilder.accept("cellSubTypeA", signatureListingOptions.getCellSubTypeA());
    joinLikeSignatureBuilder.accept("cellTypeB", signatureListingOptions.getCellTypeB());
    joinLikeSignatureBuilder.accept("cellSubTypeB", signatureListingOptions.getCellSubTypeB());
    joinLikeSignatureBuilder.accept("disease", signatureListingOptions.getDisease());
    likeSignatureBuilder.accept("signatureName", signatureListingOptions.getSignatureName());
    likeSignatureBuilder.accept("organism", signatureListingOptions.getOrganism());
    likeSignatureBuilder.accept("sourceDb", signatureListingOptions.getSourceDb());
    likeSignatureBuilder.accept("signatureType", signatureListingOptions.getSignatureType().map(SignatureType::toString));

    signatureListingOptions.getExperimentalDesign().ifPresent(experimentalDesign -> {
      final Path<ExperimentalDesign> experimentalDesignPath = joinSignature.get("experimentalDesign");

      andPredicates.add(cb.equal(experimentalDesignPath, experimentalDesign));
    });

    signatureListingOptions.getSignaturePubMedId().ifPresent(signaturePubMedId -> {
      Join<Signature, ArticleMetadata> joinArticleMetadata = joinSignature.join("articleMetadata", JoinType.LEFT);
      final Path<Integer> signaturePubMedIdPath = joinArticleMetadata.get("pubmedId");

      andPredicates.add(cb.equal(signaturePubMedIdPath, signaturePubMedId));
    });

    final Join<DrugSignatureInteraction, Drug> joinDrug = root.join("drug", JoinType.LEFT);

    final BiConsumer<String, Optional<String>> joinLikeDrugBuilder = (attributeName, queryValue) -> {
      if (queryValue.isPresent()) {
        final Path<String> field = joinDrug.get(attributeName);
        
        andPredicates.add(cb.like(cb.upper(field), "%" + queryValue.get().toUpperCase() + "%"));
      }
    };
    
    joinLikeDrugBuilder.accept("sourceName", listingOptions.getDrugSourceName());
    joinLikeDrugBuilder.accept("sourceDb", listingOptions.getDrugSourceDb());
    joinLikeDrugBuilder.accept("commonName", listingOptions.getDrugCommonName());
    
    return andPredicates.toArray(new Predicate[andPredicates.size()]);
  }

  private Order[] createOrders(
    DrugSignatureInteractionListingOptions listingOptions, Root<DrugSignatureInteraction> root
  ) {
    List<Order> orders = new LinkedList<>();
    ListingOptions generalListingOptions = listingOptions.getListingOptions();

    if (generalListingOptions.hasOrder()) {
      final CriteriaBuilder cb = dh.cb();

      for (SortField sortField : generalListingOptions.getSortFields().collect(toList())) {
        DrugSignatureInteractionField field = DrugSignatureInteractionField.valueOf(sortField.getSortField());

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
        
        if (order != null) {
          switch (field) {
            case CELL_TYPE_A:
              orders.add(order.apply(cb.min(root.join("signature").join("cellTypeA", JoinType.LEFT))));
              break;
            case CELL_SUBTYPE_A:
              orders.add(order.apply(cb.min(root.join("signature").join("cellSubTypeA", JoinType.LEFT))));
              break;
            case CELL_TYPE_B:
              orders.add(order.apply(cb.min(root.join("signature").join("cellTypeB", JoinType.LEFT))));
              break;
            case CELL_SUBTYPE_B:
              orders.add(order.apply(cb.min(root.join("signature").join("cellSubTypeB", JoinType.LEFT))));
              break;
            case DISEASE:
              orders.add(order.apply(cb.min(root.join("signature").join("disease", JoinType.LEFT))));
              break;
            case SIGNATURE_NAME:
              orders.add(order.apply(root.join("signature").get("signatureName")));
              break;
            case EXPERIMENTAL_DESIGN:
              orders.add(order.apply(root.join("signature").get("experimentalDesign")));
              break;
            case ORGANISM:
              orders.add(order.apply(root.join("signature").get("organism")));
              break;
            case SIGNATURE_SOURCE_DB:
              orders.add(order.apply(root.join("signature").get("sourceDb")));
              break;
            case SIGNATURE_TYPE:
              orders.add(order.apply(root.join("signature").get("signatureType")));
              break;

            case DRUG_SOURCE_NAME:
              orders.add(order.apply(root.join("drug").get("sourceName")));
              break;
            case DRUG_SOURCE_DB:
              orders.add(order.apply(root.join("drug").get("sourceDb")));
              break;
            case DRUG_COMMON_NAME:
              orders.add(order.apply(root.join("drug").get("commonName")));
              break;

            case INTERACTION_TYPE:
              orders.add(order.apply(root.get("interactionType")));
              break;
            case TAU:
              orders.add(order.apply(root.get("tau")));
              break;
            case UP_FDR:
              orders.add(order.apply(root.get("upFdr")));
              break;
            case DOWN_FDR:
              orders.add(order.apply(root.get("downFdr")));
              break;
            case NONE:
              break;
            default:
              break;
          }
        }
      }
    }

    return orders.toArray(new Order[orders.size()]);
  }
}
