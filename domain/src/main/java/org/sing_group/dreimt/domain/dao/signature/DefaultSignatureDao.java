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

      query = query.where(predicates).distinct(true);

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

  @Override
  public long count(SignatureListingOptions listingOptions) {
    return listColumnValues("signatureName", String.class, listingOptions).distinct().count();
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

    if (listingOptions.getCellTypeAndSubType1Filter().isApplicable()) {
      Path<String> cellTypeA = root.get("cellTypeA");
      Path<String> cellTypeB = root.get("cellTypeB");
      Path<String> cellSubTypeA = root.get("cellSubTypeA");
      Path<String> cellSubTypeB = root.get("cellSubTypeB");

      andPredicates.add(
        listingOptions.getCellTypeAndSubType1Filter().getPredicate(
          cb, listingOptions.getCellTypeAndSubType2Filter(),
          cellTypeA, cellSubTypeA,
          cellTypeB, cellSubTypeB
        )
      );
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
      .filter(value -> signatureListingOptions.getCellTypeAndSubType1Filter().matchesFilter(value))
      .sorted(CELL_TYPE_AND_SUBTYPE_COMPARATOR);
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
      joins.add(root.get(column).as(String.class));
    }

    query = query.multiselect(joins);

    if (signatureListingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(signatureListingOptions, null, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  @Override
  public Stream<CellTypeAndSubtype> listCellTypeAndSubtype2Values(SignatureListingOptions signatureListingOptions) {
    if (!signatureListingOptions.getCellTypeAndSubType1Filter().isApplicable()) {
      throw new IllegalArgumentException(
        "A cellType1 filter must be defined in orter to list cellTypeAndSubType2 values"
      );
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
    if (signatureListingOptions.getCellTypeAndSubType1Filter().matchesFilter(a)) {
      toret.add(b);
    }

    if (signatureListingOptions.getCellTypeAndSubType1Filter().matchesFilter(b)) {
      toret.add(a);
    }

    if (!toret.isEmpty()) {
      return toret;
    }

    throw new IllegalArgumentException(
      "Error processing match against cellType1 filter: "
        + signatureListingOptions.getCellTypeAndSubType1Filter().toString()
    );
  }

  private static boolean notEmpty(CellTypeAndSubtype cellTypeAndSubType) {
    return cellTypeAndSubType.getType() != null;
  }

  @Override
  public Stream<String> listCellType1DiseaseValues(SignatureListingOptions signatureListingOptions) {
    return listMultipleColumnWithJoinCollectionValues(
      signatureListingOptions,
      new String[] {
        "cellTypeA", "cellSubTypeA", "cellTypeB", "cellSubTypeB"
      },
      new String[] {
        "diseaseA", "diseaseB"
      }
    )
      .map(tuple -> {
        CellTypeAndSubtype typeA = tupleToCellTypeAndSubtype(tuple, 0, 1);
        CellTypeAndSubtype typeB = tupleToCellTypeAndSubtype(tuple, 2, 3);

        String additionalSetFieldFilter = signatureListingOptions.getCellType1Disease().orElse("");
        List<CustomCellTypeAndSubtype> list = new LinkedList<>();
        CustomCellTypeAndSubtype customTypeA =
          toCustomCellTypeAndSubtypeList((String) tuple.get(4), additionalSetFieldFilter, typeA);
        if (customTypeA != null) {
          list.add(customTypeA);
        }
        CustomCellTypeAndSubtype customTypeB =
          toCustomCellTypeAndSubtypeList((String) tuple.get(5), additionalSetFieldFilter, typeB);
        if (customTypeB != null) {
          list.add(customTypeB);
        }

        return list;
      })
      .flatMap(List::stream)
      .distinct()
      .filter(
        value -> signatureListingOptions.getCellTypeAndSubType1Filter().matchesFilter(value)
      )
      .map(CustomCellTypeAndSubtype::getAdditionalInfo)
      .distinct();
  }
  
  @Override
  public Stream<String> listCellType1TreatmentValues(SignatureListingOptions signatureListingOptions) {
    return listMultipleColumnWithJoinCollectionValues(
      signatureListingOptions,
      new String[] {
        "cellTypeA", "cellSubTypeA", "cellTypeB", "cellSubTypeB"
      },
      new String[] {
        "treatmentA", "treatmentB"
      }
    )
      .map(tuple -> {
        CellTypeAndSubtype typeA = tupleToCellTypeAndSubtype(tuple, 0, 1);
        CellTypeAndSubtype typeB = tupleToCellTypeAndSubtype(tuple, 2, 3);

        String additionalSetFieldFilter = signatureListingOptions.getCellType1Disease().orElse("");
        List<CustomCellTypeAndSubtype> list = new LinkedList<>();
        CustomCellTypeAndSubtype customTypeA =
          toCustomCellTypeAndSubtypeList((String) tuple.get(4), additionalSetFieldFilter, typeA);
        if (customTypeA != null) {
          list.add(customTypeA);
        }
        CustomCellTypeAndSubtype customTypeB =
          toCustomCellTypeAndSubtypeList((String) tuple.get(5), additionalSetFieldFilter, typeB);
        if (customTypeB != null) {
          list.add(customTypeB);
        }

        return list;
      })
      .flatMap(List::stream)
      .distinct()
      .filter(
        value -> signatureListingOptions.getCellTypeAndSubType1Filter().matchesFilter(value)
      )
      .map(CustomCellTypeAndSubtype::getAdditionalInfo)
      .distinct();
  }

  private Stream<Tuple> listMultipleColumnWithJoinCollectionValues(
    SignatureListingOptions signatureListingOptions, String[] columns, String[] joinColumns
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<Tuple> query = cb.createTupleQuery();
    final Root<Signature> root = query.from(dh.getEntityType());

    List<Selection<?>> joins = new LinkedList<>();

    for (String column : columns) {
      joins.add(root.get(column).as(String.class));
    }

    for (String column : joinColumns) {
      joins.add(root.join(column).as(String.class));
    }

    query = query.multiselect(joins);

    if (signatureListingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(signatureListingOptions, null, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  private static CustomCellTypeAndSubtype toCustomCellTypeAndSubtypeList(
    String additionalSetFieldValue, String additionalSetFieldFilter, CellTypeAndSubtype cellTypeAndSubtype
  ) {

    if (additionalSetFieldValue != null) {
      if (additionalSetFieldValue.contains(additionalSetFieldFilter)) {
        return new CustomCellTypeAndSubtype(cellTypeAndSubtype, additionalSetFieldValue);
      }
    }

    return null;
  }

  @Override
  public Stream<String> listDiseaseValues(SignatureListingOptions signatureListingOptions) {
    Stream<String> diseaseValues = listElementCollectionValues(signatureListingOptions, "disease");
    if (signatureListingOptions.getDisease().isPresent()) {
      return diseaseValues
        .filter(d -> d.equals(signatureListingOptions.getDisease().get()));
    } else {
      return diseaseValues;
    }
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

  private <T> Stream<T> listColumn(
    String columnName, Class<T> targetClass, SignatureListingOptions signatureListingOptions
    ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<T> query = cb.createQuery(targetClass);
    final Root<Signature> root = query.from(dh.getEntityType());
    
    query = query.select(root.get(columnName));

    if (signatureListingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(signatureListingOptions, null, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  private <T> Stream<T> listColumnValues(
    String columnName, Class<T> targetClass, SignatureListingOptions signatureListingOptions
  ) {
    return this.listColumn(columnName, targetClass, signatureListingOptions).distinct();
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
