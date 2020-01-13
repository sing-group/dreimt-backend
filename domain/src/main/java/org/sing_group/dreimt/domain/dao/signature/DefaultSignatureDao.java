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

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Stream.concat;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.DaoHelper;
import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.dao.spi.signature.SignatureDao;
import org.sing_group.dreimt.domain.entities.signature.ArticleMetadata;
import org.sing_group.dreimt.domain.entities.signature.CellTypeAndSubtype;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.Gene;
import org.sing_group.dreimt.domain.entities.signature.GeneSetSignature;
import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;
import org.sing_group.dreimt.domain.entities.signature.UpDownSignature;
import org.sing_group.dreimt.domain.entities.signature.UpDownSignatureGene;

@Default
@Transactional(MANDATORY)
public class DefaultSignatureDao implements SignatureDao {

  public static final Comparator<? super CellTypeAndSubtype> CELL_TYPE_AND_SUBTYPE_COMPARATOR =
    new Comparator<CellTypeAndSubtype>() {

      @Override
      public int compare(CellTypeAndSubtype o1, CellTypeAndSubtype o2) {
        if (o2.getType() == null) {
          if (o1.getType() == null) {
            return 0;
          } else {
            return 1;
          }
        } else {
          if (o1.getType() == null) {
            return -1;
          } else {
            int compare = o1.getType().compareTo(o2.getType());
            if (compare == 0) {
              if (o2.getSubType() == null) {
                if (o1.getSubType() == null) {
                  return 0;
                } else {
                  return 1;
                }
              } else {
                if (o1.getSubType() == null) {
                  return -1;
                } else {
                  return o1.getSubType().compareTo(o2.getSubType());
                }
              }
            } else {
              return compare;
            }
          }
        }
      }
    };

  @PersistenceContext
  private EntityManager em;

  private DaoHelper<Integer, Signature> dh;

  DefaultSignatureDao() {}

  public DefaultSignatureDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DaoHelper.of(Integer.class, Signature.class, this.em);
  }

  @Override
  public Optional<Signature> get(String signatureName) {
    try {
      return of(this.dh.getBy("signatureName", signatureName));
    } catch (NoResultException e) {
      return empty();
    }
  }

  @Override
  public Stream<Signature> list(SignatureListingOptions listingOptions) {
    if (!listingOptions.hasAnyQueryModification()) {
      return this.dh.list().stream();
    } else {
      final Stream<Signature> geneSetSignatures =
        this.listForSignature(
          GeneSetSignature.class, listingOptions, (root, geneSet) -> {
            final Join<GeneSetSignature, Gene> join = root.join("signatureGenes");

            return join.get("gene").in(geneSet);
          }
        );
      final Stream<Signature> upDownSignatures =
        this.listForSignature(
          UpDownSignature.class, listingOptions, (root, geneSet) -> {
            final Join<UpDownSignature, UpDownSignatureGene> joinUpDownSignatureGene = root.join("signatureGenes");
            final Join<UpDownSignatureGene, Gene> joinGene = joinUpDownSignatureGene.join("gene");

            return joinGene.get("gene").in(geneSet);
          }
        );

      return concat(geneSetSignatures, upDownSignatures);
    }
  }

  private <T extends Signature> Stream<Signature> listForSignature(
    Class<T> signatureClass, SignatureListingOptions listingOptions,
    BiFunction<Root<T>, Set<String>, Predicate> geneFilterBuilder
  ) {
    if (!listingOptions.hasAnyQueryModification()) {
      return this.dh.list().stream();
    } else {
      CriteriaQuery<T> query = dh.cb().createQuery(signatureClass);
      final Root<T> root = query.from(signatureClass);

      query = query.select(root);

      Predicate[] predicates = createPredicates(listingOptions, geneFilterBuilder, root);

      query = query.where(predicates);

      ListingOptions generalListingOptions = listingOptions.getListingOptions();

      TypedQuery<T> typedQuery = em.createQuery(query);
      if (generalListingOptions.hasResultLimits()) {
        final int start = generalListingOptions.getStart().getAsInt();
        final int end = generalListingOptions.getEnd().getAsInt();

        typedQuery =
          typedQuery
            .setFirstResult(start)
            .setMaxResults(end - start + 1);
      }

      return typedQuery.getResultList().stream()
        .map(signature -> (Signature) signature);
    }
  }

  private <T extends Signature> Predicate[] createPredicates(
    SignatureListingOptions listingOptions, BiFunction<Root<T>, Set<String>, Predicate> geneFilterBuilder,
    final Root<T> root
  ) {
    final CriteriaBuilder cb = dh.cb();

    final List<Predicate> andPredicates = new ArrayList<>();

    if (listingOptions.getSignatureName().isPresent()) {
      final Path<String> signatureName = root.get("signatureName");

      andPredicates.add(cb.like(signatureName, "%" + listingOptions.getSignatureName().get() + "%"));
    }

    if (listingOptions.getCellType1().isPresent()) {
      Join<Signature, String> joinSignatureCellTypeA = root.join("cellTypeA", JoinType.LEFT);
      Join<Signature, String> joinSignatureCellTypeB = root.join("cellTypeB", JoinType.LEFT);

      if (listingOptions.getCellType2().isPresent()) {
        andPredicates.add(
          cb.or(
            cb.and(
              cb.like(joinSignatureCellTypeA, "%" + listingOptions.getCellType1().get() + "%"),
              cb.like(joinSignatureCellTypeB, "%" + listingOptions.getCellType2().get() + "%")
            ),
            cb.and(
              cb.like(joinSignatureCellTypeA, "%" + listingOptions.getCellType2().get() + "%"),
              cb.like(joinSignatureCellTypeB, "%" + listingOptions.getCellType1().get() + "%")
            )
          )
        );
      } else {
        andPredicates.add(
          cb.or(
            cb.like(joinSignatureCellTypeA, "%" + listingOptions.getCellType1().get() + "%"),
            cb.like(joinSignatureCellTypeB, "%" + listingOptions.getCellType1().get() + "%")
          )
        );
      }
    }

    if (listingOptions.getCellSubType1().isPresent()) {
      Join<Signature, String> joinSignatureCellSubTypeA = root.join("cellSubTypeA", JoinType.LEFT);
      Join<Signature, String> joinSignatureCellSubTypeB = root.join("cellSubTypeB", JoinType.LEFT);

      if (listingOptions.getCellSubType2().isPresent()) {
        andPredicates.add(
          cb.or(
            cb.and(
              cb.like(joinSignatureCellSubTypeA, "%" + listingOptions.getCellSubType1().get() + "%"),
              cb.like(joinSignatureCellSubTypeB, "%" + listingOptions.getCellSubType2().get() + "%")
            ),
            cb.and(
              cb.like(joinSignatureCellSubTypeA, "%" + listingOptions.getCellSubType2().get() + "%"),
              cb.like(joinSignatureCellSubTypeB, "%" + listingOptions.getCellSubType1().get() + "%")
            )
          )
        );
      } else {
        andPredicates.add(
          cb.or(
            cb.like(joinSignatureCellSubTypeA, "%" + listingOptions.getCellSubType1().get() + "%"),
            cb.like(joinSignatureCellSubTypeB, "%" + listingOptions.getCellSubType1().get() + "%")
          )
        );
      }
    }

    if (listingOptions.getExperimentalDesign().isPresent()) {
      final Path<String> experimentalDesign = root.get("experimentalDesign");

      andPredicates.add(cb.equal(experimentalDesign, listingOptions.getExperimentalDesign().get()));
    }

    if (listingOptions.getOrganism().isPresent()) {
      final Path<String> organism = root.get("organism");

      andPredicates.add(cb.like(organism, "%" + listingOptions.getOrganism().get() + "%"));
    }

    if (listingOptions.getDisease().isPresent()) {
      Join<Signature, String> joinSignatureDisease = root.join("disease", JoinType.LEFT);

      andPredicates.add(cb.like(joinSignatureDisease, "%" + listingOptions.getDisease().get() + "%"));
    }

    if (listingOptions.getSourceDb().isPresent()) {
      final Path<String> sourceDb = root.get("sourceDb");

      andPredicates.add(cb.like(sourceDb, "%" + listingOptions.getSourceDb().get() + "%"));
    }

    if (listingOptions.getSignatureType().isPresent()) {
      final Path<String> signatureType = root.get("signatureType");

      andPredicates.add(cb.equal(signatureType, listingOptions.getSignatureType().get()));
    }

    if (listingOptions.getSignaturePubMedId().isPresent()) {
      Join<Signature, ArticleMetadata> joinArticleMetadata = root.join("articleMetadata", JoinType.LEFT);
      final Path<Integer> signaturePubMedId = joinArticleMetadata.get("pubmedId");

      andPredicates.add(cb.equal(signaturePubMedId, listingOptions.getSignaturePubMedId().get()));
    }

    if (geneFilterBuilder != null) {
      Set<String> genes = listingOptions.getMandatoryGenes().orElse(emptySet());
      if (!genes.isEmpty()) {
        andPredicates.add(geneFilterBuilder.apply(root, genes));
      }
    }

    return andPredicates.toArray(new Predicate[andPredicates.size()]);
  }

  @Override
  public Stream<CellTypeAndSubtype> listCellTypeAndSubtype1Values(SignatureListingOptions signatureListingOptions) {
    return listMultipleColumnCollectionValues(
      signatureListingOptions, "cellTypeA", "cellSubTypeA", "cellTypeB", "cellSubTypeB"
    )
      .map(tuple -> {
        return asList(tupleToCellTypeAndSubtype(tuple, 0, 1), tupleToCellTypeAndSubtype(tuple, 2, 3));
      })
      .flatMap(List::stream)
      .distinct()
      .filter(value -> DefaultSignatureDao.cellTypeAndSubTypeMatchesFilters(value, signatureListingOptions))
      .sorted(CELL_TYPE_AND_SUBTYPE_COMPARATOR);
  }

  private static boolean cellTypeAndSubTypeMatchesFilters(
    CellTypeAndSubtype cellTypeAndSubType, SignatureListingOptions signatureListingOptions
  ) {
    if (signatureListingOptions.getCellType1().isPresent()) {
      if (cellTypeAndSubType.getType() == null) {
        return false;
      } else {
        if (
          !cellTypeAndSubType.getType().toLowerCase()
            .contains(signatureListingOptions.getCellType1().get().toLowerCase())
        ) {
          return false;
        }
      }
    }

    if (signatureListingOptions.getCellSubType1().isPresent()) {
      if (cellTypeAndSubType.getSubType() == null) {
        return false;
      } else {
        if (
          !cellTypeAndSubType.getSubType().toLowerCase()
            .contains(signatureListingOptions.getCellSubType1().get().toLowerCase())
        ) {
          return false;
        }
      }
    }

    return true;
  }

  private static CellTypeAndSubtype tupleToCellTypeAndSubtype(Tuple tuple, int typeIndex, int subTypeIndex) {
    String type = null;
    if (tuple.get(typeIndex) != null) {
      type = tuple.get(typeIndex).toString();
    }

    String subType = null;
    if (tuple.get(subTypeIndex) != null) {
      subType = tuple.get(subTypeIndex).toString();
    }

    return new CellTypeAndSubtype(type, subType);
  }

  private Stream<Tuple> listMultipleColumnCollectionValues(
    SignatureListingOptions signatureListingOptions, String... columns
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<Tuple> query = cb.createTupleQuery();
    final Root<Signature> root = query.from(dh.getEntityType());

    List<Selection<?>> joins = new LinkedList<>();

    for (String column : columns) {
      joins.add(root.join(column).as(String.class));
    }

    query = query.multiselect(joins).distinct(true);

    if (signatureListingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(signatureListingOptions, null, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  @Override
  public Stream<CellTypeAndSubtype> listCellTypeAndSubtype2Values(SignatureListingOptions signatureListingOptions) {
    if (!signatureListingOptions.getCellType1().isPresent()) {
      throw new IllegalArgumentException("cellType1 must be defined in orter to list cellTypeAndSubType2 values");
    }

    return listMultipleColumnCollectionValues(
      signatureListingOptions, "cellTypeA", "cellSubTypeA", "cellTypeB", "cellSubTypeB"
    )
      .map(tuple -> getTuplePair(signatureListingOptions, tupleToCellTypeAndSubtype(tuple, 0, 1), tupleToCellTypeAndSubtype(tuple, 2, 3)))
      .flatMap(List::stream)
      .filter(DefaultSignatureDao::notEmpty)
      .distinct()
      .sorted(CELL_TYPE_AND_SUBTYPE_COMPARATOR);
  }

  private static List<CellTypeAndSubtype> getTuplePair(
    SignatureListingOptions signatureListingOptions, CellTypeAndSubtype a, CellTypeAndSubtype b
  ) {
    List<CellTypeAndSubtype> toret = new LinkedList<>();
    if (DefaultSignatureDao.cellTypeAndSubTypeMatchesFilters(a, signatureListingOptions)) {
      toret.add(b);
    }

    if (DefaultSignatureDao.cellTypeAndSubTypeMatchesFilters(b, signatureListingOptions)) {
      toret.add(a);
    }

    if (!toret.isEmpty()) {
      return toret;
    }

    throw new IllegalArgumentException(
      "Error processing match against cellType1 = " +
        signatureListingOptions.getCellType1().orElse("<undefined>") + " and cellSubType1 = " +
        signatureListingOptions.getCellSubType1().orElse("<undefined>")
    );
  }

  @Override
  public Stream<String> listCellType1Values(SignatureListingOptions signatureListingOptions) {
    return listTwoColumnCollectionValues(signatureListingOptions, "cellTypeA", "cellTypeB")
      .map(tuple -> DefaultSignatureDao.tupleToList(tuple, signatureListingOptions.getCellType1()))
      .flatMap(List::stream)
      .distinct();
  }

  @Override
  public Stream<String> listCellSubType1Values(SignatureListingOptions signatureListingOptions) {
    return listTwoColumnCollectionValues(signatureListingOptions, "cellSubTypeA", "cellSubTypeB")
      .map(tuple -> DefaultSignatureDao.tupleToList(tuple, signatureListingOptions.getCellSubType1()))
      .flatMap(List::stream)
      .distinct();
  }

  private static List<String> tupleToList(Tuple tuple, Optional<String> filter) {
    List<String> toret = new LinkedList<String>();
    if (tuple.get(0) != null && tuple.get(0).toString().contains(filter.orElse(""))) {
      toret.add(tuple.get(0).toString());
    }
    if (tuple.get(1) != null && tuple.get(1).toString().contains(filter.orElse(""))) {
      toret.add(tuple.get(1).toString());
    }
    return toret;
  }

  @Override
  public Stream<String> listCellType2Values(SignatureListingOptions signatureListingOptions) {
    if (!signatureListingOptions.getCellType1().isPresent()) {
      throw new IllegalArgumentException("cellType1 must be defined in orter to list cellType2 values");
    }

    return listTwoColumnCollectionValues(signatureListingOptions, "cellTypeA", "cellTypeB")
      .map(tuple -> getTuplePairs(tuple, signatureListingOptions.getCellType1().get()))
      .distinct()
      .filter(DefaultSignatureDao::notEmpty);
  }

  @Override
  public Stream<String> listCellSubType2Values(SignatureListingOptions signatureListingOptions) {
    if (!signatureListingOptions.getCellSubType1().isPresent()) {
      throw new IllegalArgumentException("cellSubType1 must be defined in orter to list cellSubType2 values");
    }

    return listTwoColumnCollectionValues(signatureListingOptions, "cellSubTypeA", "cellSubTypeB")
      .map(tuple -> getTuplePairs(tuple, signatureListingOptions.getCellSubType1().get()))
      .distinct()
      .filter(DefaultSignatureDao::notEmpty);
  }

  private static boolean notEmpty(String string) {
    return !string.isEmpty();
  }

  private static boolean notEmpty(CellTypeAndSubtype cellTypeAndSubType) {
    return cellTypeAndSubType.getType() != null;
  }

  private static String getTuplePairs(Tuple tuple, String match) {
    if (tuple.get(0) != null && tuple.get(0).toString().toUpperCase().contains(match.toUpperCase())) {
      return tuple.get(1) == null ? "" : tuple.get(1).toString();
    } else if (tuple.get(1) != null && tuple.get(1).toString().toUpperCase().contains(match.toUpperCase())) {
      return tuple.get(0) == null ? "" : tuple.get(0).toString();
    } else {
      throw new IllegalArgumentException("Error processing match: " + match);
    }
  }

  private Stream<Tuple> listTwoColumnCollectionValues(
    SignatureListingOptions signatureListingOptions, String columnName1, String columnName2
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<Tuple> query = cb.createTupleQuery();
    final Root<Signature> root = query.from(dh.getEntityType());
    final Join<Signature, ?> join1 = root.join(columnName1);
    final Join<Signature, ?> join2 = root.join(columnName2);

    query = query.multiselect(join1.as(String.class), join2.as(String.class)).distinct(true);

    if (signatureListingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(signatureListingOptions, null, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  @Override
  public Stream<String> listDiseaseValues(SignatureListingOptions signatureListingOptions) {
    return listElementCollectionValues(signatureListingOptions, "disease");
  }

  private Stream<String> listElementCollectionValues(
    SignatureListingOptions signatureListingOptions, String columnName
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<String> query = cb.createQuery(String.class);
    final Root<Signature> root = query.from(dh.getEntityType());
    final Join<Signature, ?> join = root.join(columnName);

    query = query.multiselect(join.as(String.class)).distinct(true);

    if (signatureListingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(signatureListingOptions, null, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  @Override
  public Stream<String> listSignatureNameValues(SignatureListingOptions signatureListingOptions) {
    return listColumnValues("signatureName", String.class, signatureListingOptions);
  }

  @Override
  public Stream<ExperimentalDesign> listExperimentalDesignValues(SignatureListingOptions signatureListingOptions) {
    return listColumnValues("experimentalDesign", ExperimentalDesign.class, signatureListingOptions);
  }

  @Override
  public Stream<String> listOrganismValues(SignatureListingOptions signatureListingOptions) {
    return listColumnValues("organism", String.class, signatureListingOptions);
  }

  @Override
  public Stream<String> listSourceDbValues(SignatureListingOptions signatureListingOptions) {
    return listColumnValues("sourceDb", String.class, signatureListingOptions);
  }

  @Override
  public Stream<SignatureType> listSignatureTypeValues(SignatureListingOptions signatureListingOptions) {
    return listColumnValues("signatureType", String.class, signatureListingOptions).map(SignatureType::valueOf);
  }

  private <T> Stream<T> listColumnValues(
    String columnName, Class<T> targetClass, SignatureListingOptions signatureListingOptions
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<T> query = cb.createQuery(targetClass);
    final Root<Signature> root = query.from(dh.getEntityType());

    query = query.select(root.get(columnName)).distinct(true);

    if (signatureListingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(signatureListingOptions, null, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  @Override
  public Stream<Integer> listSignaturePubMedIdValues(SignatureListingOptions listingOptions) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
    final Root<Signature> root = query.from(dh.getEntityType());
    final Join<Signature, ArticleMetadata> join = root.join("articleMetadata", JoinType.LEFT);

    query = query.select(join.get("pubmedId")).distinct(true);

    if (listingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(listingOptions, null, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }
}
