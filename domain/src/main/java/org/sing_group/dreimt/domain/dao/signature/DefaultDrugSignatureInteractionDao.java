/*-
 * #%L
 * DREIMT - Domain
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
 *      Kevin Troulé, Gonzálo Gómez-López, Fátima Al-Shahrour
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

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static javax.transaction.Transactional.TxType.MANDATORY;
import static org.sing_group.dreimt.domain.dao.ListingOptions.noModification;
import static org.sing_group.dreimt.domain.dao.signature.DefaultSignatureDao.CELL_TYPE_AND_SUBTYPE_COMPARATOR;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Tuple;
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
import javax.persistence.criteria.Selection;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.DaoHelper;
import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.dao.ListingOptions.SortField;
import org.sing_group.dreimt.domain.dao.spi.signature.DrugSignatureInteractionDao;
import org.sing_group.dreimt.domain.dao.spi.signature.GeneDao;
import org.sing_group.dreimt.domain.entities.signature.ArticleMetadata;
import org.sing_group.dreimt.domain.entities.signature.CellTypeAndSubtype;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionField;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;
import org.sing_group.dreimt.domain.entities.signature.DrugStatus;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.FullDrugSignatureInteraction;
import org.sing_group.dreimt.domain.entities.signature.GeneSetSignature;
import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;
import org.sing_group.dreimt.domain.entities.signature.UpDownSignature;

@Default
@Transactional(MANDATORY)
public class DefaultDrugSignatureInteractionDao implements DrugSignatureInteractionDao {

  @PersistenceContext
  private EntityManager em;

  private DaoHelper<Integer, FullDrugSignatureInteraction> dh;

  @Inject
  private GeneDao geneDao;

  DefaultDrugSignatureInteractionDao() {}

  public DefaultDrugSignatureInteractionDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DaoHelper.of(Integer.class, FullDrugSignatureInteraction.class, this.em);
  }

  private Stream<DrugSignatureInteraction> reconstruct(Stream<FullDrugSignatureInteraction> stream) {
    return stream
      .map((fdsi) -> {
        Drug drug =
          new Drug(
            fdsi.getDrugCommonName(), fdsi.getDrugSourceName(), fdsi.getDrugSourceDb(), fdsi.getDrugStatus(),
            reconstructSet(fdsi.getDrugMoa()),
            this.geneDao.getGenes(reconstructSet(fdsi.getDrugTargetGenes()), false),
            fdsi.getDrugDss()
          );

        Signature signature =
          buildSignature(
            fdsi.getSignatureType(),
            fdsi.getSignatureName(),
            reconstructSet(fdsi.getSignatureCellTypeA()),
            reconstructSet(fdsi.getSignatureCellSubTypeA()),
            reconstructSet(fdsi.getSignatureCellTypeB()),
            reconstructSet(fdsi.getSignatureCellSubTypeB()),
            fdsi.getArticleMetadata(),
            fdsi.getSignatureSourceDb(),
            fdsi.getSignatureSourceDbUrl(),
            fdsi.getSignatureExperimentalDesign(),
            fdsi.getSignatureOrganism(),
            reconstructSet(fdsi.getSignatureDisease()),
            reconstructSet(fdsi.getSignatureTreatmentA()),
            reconstructSet(fdsi.getSignatureTreatmentB()),
            reconstructSet(fdsi.getSignatureDiseaseA()),
            reconstructSet(fdsi.getSignatureDiseaseB()),
            fdsi.getSignatureLocalisationA(),
            fdsi.getSignatureLocalisationB(),
            fdsi.getSignatureStateA(),
            fdsi.getSignatureStateB()
          );

        return new DrugSignatureInteraction(
          drug, signature, fdsi.getInteractionType(), fdsi.getTau(), fdsi.getUpFdr(), fdsi.getDownFdr()
        );
      });
  }

  private static Signature buildSignature(
    SignatureType signatureType, String signatureName, Set<String> cellTypeA, Set<String> cellSubTypeA,
    Set<String> cellTypeB, Set<String> cellSubTypeB, Optional<ArticleMetadata> articleMetadata, String sourceDb,
    String sourceDbUrl, ExperimentalDesign experimentalDesign, String organism, Set<String> disease,
    Set<String> treatmentA, Set<String> treatmentB, Set<String> diseaseA, Set<String> diseaseB, String localisationA,
    String localisationB, String stateA, String stateB
  ) {
    if (signatureType.equals(SignatureType.GENESET)) {
      if (articleMetadata.isPresent()) {
        return new GeneSetSignature(
          signatureName, cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB, articleMetadata.get(), sourceDb, sourceDbUrl,
          experimentalDesign, organism, disease, treatmentA, treatmentB, diseaseA, diseaseB, localisationA,
          localisationB, stateA, stateB
        );
      } else {
        return new GeneSetSignature(
          signatureName, cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB, sourceDb, sourceDbUrl,
          experimentalDesign, organism, disease, treatmentA, treatmentB, diseaseA, diseaseB, localisationA,
          localisationB, stateA, stateB
        );
      }
    } else {
      if (articleMetadata.isPresent()) {
        return new UpDownSignature(
          signatureName, cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB, articleMetadata.get(), sourceDb, sourceDbUrl,
          experimentalDesign, organism, disease, treatmentA, treatmentB, diseaseA, diseaseB, localisationA,
          localisationB, stateA, stateB
        );
      } else {
        return new UpDownSignature(
          signatureName, cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB, sourceDb, sourceDbUrl, experimentalDesign,
          organism, disease, treatmentA, treatmentB, diseaseA, diseaseB, localisationA, localisationB, stateA, stateB
        );
      }
    }
  }

  private static Set<String> reconstructSet(String field) {
    return field == null ? emptySet() : Stream.of(field.split("##")).map(String::trim).collect(toSet());
  }

  @Override
  public Stream<DrugSignatureInteraction> list(DrugSignatureInteractionListingOptions listingOptions) {
    if (!listingOptions.hasAnyQueryModification()) {
      return reconstruct(this.dh.list().stream());
    } else {
      CriteriaQuery<FullDrugSignatureInteraction> query = dh.createCBQuery();
      final Root<FullDrugSignatureInteraction> root = query.from(dh.getEntityType());

      query =
        query.select(root)
          .where(createPredicates(listingOptions, root))
          .orderBy(createOrders(listingOptions, root));

      ListingOptions generalListingOptions = listingOptions.getListingOptions();

      TypedQuery<FullDrugSignatureInteraction> typedQuery = em.createQuery(query);
      if (generalListingOptions.hasResultLimits()) {
        final int start = generalListingOptions.getStart().getAsInt();
        final int end = generalListingOptions.getEnd().getAsInt();

        typedQuery =
          typedQuery
            .setFirstResult(start)
            .setMaxResults(end - start + 1);
      }

      return reconstruct(typedQuery.getResultList().stream());
    }
  }

  @Override
  public long count(DrugSignatureInteractionListingOptions listingOptions) {
    if (!listingOptions.hasAnyQueryModification()) {
      return this.dh.count();
    } else {
      final CriteriaBuilder cb = dh.cb();

      CriteriaQuery<Long> query = cb.createQuery(Long.class);
      final Root<FullDrugSignatureInteraction> root = query.from(dh.getEntityType());
      final Predicate[] predicates = createPredicates(listingOptions, root);

      query = query.select(cb.count(root)).where(predicates);

      return this.em.createQuery(query).getSingleResult();
    }
  }

  @Override
  public Stream<DrugSignatureInteraction> list(ListingOptions listingOptions, String freeText) {
    if (!listingOptions.hasAnyQueryModification()) {
      return reconstruct(this.dh.list().stream());
    } else {
      final CriteriaBuilder cb = this.dh.cb();
      CriteriaQuery<FullDrugSignatureInteraction> query = dh.createCBQuery();
      final Root<FullDrugSignatureInteraction> root = query.from(dh.getEntityType());

      DrugSignatureInteractionListingOptions drugSignatureListingOptions =
        createDrugSignatureListingOptionsFromFreeText(listingOptions, freeText);

      query =
        query.select(root)
          .where(cb.or(createPredicates(drugSignatureListingOptions, root)))
          .orderBy(createOrders(drugSignatureListingOptions, root));

      TypedQuery<FullDrugSignatureInteraction> typedQuery = em.createQuery(query);
      if (listingOptions.hasResultLimits()) {
        final int start = listingOptions.getStart().getAsInt();
        final int end = listingOptions.getEnd().getAsInt();

        typedQuery =
          typedQuery
            .setFirstResult(start)
            .setMaxResults(end - start + 1);
      }

      return reconstruct(typedQuery.getResultList().stream());
    }
  }

  private DrugSignatureInteractionListingOptions createDrugSignatureListingOptionsFromFreeText(
    ListingOptions listingOptions, String freeText
  ) {
    SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        freeText, // signatureName
        freeText, // cellType1
        freeText, // cellSubType1
        freeText, // cellType2
        freeText, // cellSubType2
        null, // experimentalDesign
        freeText, // organism
        freeText, // disease
        null, // sourceDb
        null // signaturePubMedId
      );
    DrugSignatureInteractionListingOptions drugSignatureListingOptions =
      new DrugSignatureInteractionListingOptions(
        listingOptions, signatureListingOptions,
        null, // interactionType
        freeText, // drugSourceName
        freeText, // drugSourceDb
        freeText, // drugCommonName
        freeText, // drugMoa
        null, // drugStatus
        null, //
        null, // minTau
        null, // maxUpFdr
        null // maxDownFdr
      );

    return drugSignatureListingOptions;
  }

  @Override
  public long count(String freeText) {
    if (freeText.isEmpty()) {
      return this.dh.count();
    } else {
      final CriteriaBuilder cb = dh.cb();

      CriteriaQuery<Long> query = cb.createQuery(Long.class);
      final Root<FullDrugSignatureInteraction> root = query.from(dh.getEntityType());

      DrugSignatureInteractionListingOptions drugSignatureListingOptions =
        createDrugSignatureListingOptionsFromFreeText(noModification(), freeText);

      final Predicate[] predicates = createPredicates(drugSignatureListingOptions, root);

      query = query.select(cb.count(root)).where(cb.or(predicates));

      return this.em.createQuery(query).getSingleResult();
    }
  }

  @Override
  public Stream<CellTypeAndSubtype> listCellTypeAndSubtype1Values(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return listMultipleColumnCollectionValues(
      listingOptions, "signatureCellTypeA", "signatureCellSubTypeA", "signatureCellTypeB", "signatureCellSubTypeB"
    )
      .map(tuple -> {
        List<CellTypeAndSubtype> list = tupleToCellTypeAndSubtype(tuple, 0, 1);
        list.addAll(tupleToCellTypeAndSubtype(tuple, 2, 3));
        return list;
      })
      .flatMap(List::stream)
      .distinct()
      .filter(
        value -> DefaultDrugSignatureInteractionDao
          .cellTypeAndSubTypeMatchesFilters(value, listingOptions.getSignatureListingOptions())
      )
      .sorted(CELL_TYPE_AND_SUBTYPE_COMPARATOR);
  }

  private Stream<Tuple> listMultipleColumnCollectionValues(
    DrugSignatureInteractionListingOptions listingOptions, String... columns
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<Tuple> query = cb.createTupleQuery();
    final Root<FullDrugSignatureInteraction> root = query.from(dh.getEntityType());

    List<Selection<?>> joins = new LinkedList<>();

    for (String column : columns) {
      joins.add(root.get(column).as(String.class));
    }

    query = query.multiselect(joins).distinct(true);

    if (listingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(listingOptions, root));
    }

    return this.em.createQuery(query).getResultList().stream();
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

  private static List<CellTypeAndSubtype> tupleToCellTypeAndSubtype(Tuple tuple, int typeIndex, int subTypeIndex) {
    Set<String> types = null;
    if (tuple.get(typeIndex) != null) {
      types = reconstructSet(tuple.get(typeIndex).toString());
    }

    Set<String> subTypes = emptySet();
    if (tuple.get(subTypeIndex) != null) {
      subTypes = reconstructSet(tuple.get(subTypeIndex).toString());
    }

    List<CellTypeAndSubtype> toret = new LinkedList<>();
    for (String type : types) {
      if (subTypes.isEmpty()) {
        toret.add(new CellTypeAndSubtype(type, null));
      } else {
        for (String subType : subTypes) {
          toret.add(new CellTypeAndSubtype(type, subType));
        }
      }
    }

    return toret;
  }

  @Override
  public Stream<CellTypeAndSubtype> listCellTypeAndSubtype2Values(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    SignatureListingOptions signatureListingOptions = listingOptions.getSignatureListingOptions();

    if (!signatureListingOptions.getCellType1().isPresent()) {
      throw new IllegalArgumentException("cellType1 must be defined in orter to list cellTypeAndSubType2 values");
    }

    return listMultipleColumnCollectionValues(
      listingOptions, "signatureCellTypeA", "signatureCellSubTypeA", "signatureCellTypeB", "signatureCellSubTypeB"
    )
      .map(tuple -> {
        List<CellTypeAndSubtype> a = tupleToCellTypeAndSubtype(tuple, 0, 1);
        List<CellTypeAndSubtype> b = tupleToCellTypeAndSubtype(tuple, 2, 3);

        List<CellTypeAndSubtype> toret = new LinkedList<>();
        for (CellTypeAndSubtype ctsa : a) {
          for (CellTypeAndSubtype ctsb : b) {
            toret.addAll(getTuplePair(signatureListingOptions, ctsa, ctsb));
          }
        }
        if (toret.isEmpty()) {
          throw new IllegalArgumentException(
            "Error processing match against cellType1 = " +
              signatureListingOptions.getCellType1().orElse("<undefined>") + " and cellSubType1 = " +
              signatureListingOptions.getCellSubType1().orElse("<undefined>")
          );
        }

        return toret;
      })
      .flatMap(List::stream)
      .filter(DefaultDrugSignatureInteractionDao::notEmpty)
      .distinct()
      .sorted(CELL_TYPE_AND_SUBTYPE_COMPARATOR);
  }

  private static List<CellTypeAndSubtype> getTuplePair(
    SignatureListingOptions signatureListingOptions, CellTypeAndSubtype a, CellTypeAndSubtype b
  ) {
    List<CellTypeAndSubtype> toret = new LinkedList<>();
    if (DefaultDrugSignatureInteractionDao.cellTypeAndSubTypeMatchesFilters(a, signatureListingOptions)) {
      toret.add(b);
    }

    if (DefaultDrugSignatureInteractionDao.cellTypeAndSubTypeMatchesFilters(b, signatureListingOptions)) {
      toret.add(a);
    }

    return toret;
  }

  private static boolean notEmpty(CellTypeAndSubtype cellTypeAndSubType) {
    return cellTypeAndSubType.getType() != null;
  }

  @Override
  public Stream<String> listCellType1Values(DrugSignatureInteractionListingOptions listingOptions) {
    return reconstructTupleSets(
      this.listStringTuples(listingOptions, "signatureCellTypeA", "signatureCellTypeB")
    );
  }

  @Override
  public Stream<String> listCellSubType1Values(DrugSignatureInteractionListingOptions listingOptions) {
    return reconstructTupleSets(
      this.listStringTuples(listingOptions, "signatureCellSubTypeA", "signatureCellSubTypeB")
    );
  }

  public Stream<String> reconstructTupleSets(Stream<Tuple> stream) {
    return stream.map(tuple -> {
      Set<String> set = new HashSet<>();
      if (tuple.get(0) != null) {
        set.addAll(reconstructSet(tuple.get(0).toString()));
      }
      if (tuple.get(1) != null) {
        set.addAll(reconstructSet(tuple.get(1).toString()));
      }
      return set;
    })
      .flatMap(Set::stream)
      .distinct();
  }

  @Override
  public Stream<String> listCellType2Values(DrugSignatureInteractionListingOptions listingOptions) {
    if (!listingOptions.getSignatureListingOptions().getCellType1().isPresent()) {
      throw new IllegalArgumentException("cellType1 must be defined in orter to list cellType2 values");
    }

    return this.listStringTuples(listingOptions, "signatureCellTypeA", "signatureCellTypeB")
      .map(tuple -> getPairSets(tuple, listingOptions.getSignatureListingOptions().getCellType1().get()))
      .flatMap(Set::stream)
      .distinct();
  }

  @Override
  public Stream<String> listCellSubType2Values(DrugSignatureInteractionListingOptions listingOptions) {
    if (!listingOptions.getSignatureListingOptions().getCellSubType1().isPresent()) {
      throw new IllegalArgumentException("cellSubType1 must be defined in orter to list cellSubType2 values");
    }

    return this.listStringTuples(listingOptions, "signatureCellSubTypeA", "signatureCellSubTypeB")
      .map(tuple -> getPairSets(tuple, listingOptions.getSignatureListingOptions().getCellSubType1().get()))
      .flatMap(Set::stream)
      .distinct();
  }

  private Stream<Tuple> listStringTuples(
    DrugSignatureInteractionListingOptions listingOptions,
    String columnName1, String columnName2
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<Tuple> query = cb.createTupleQuery();
    final Root<FullDrugSignatureInteraction> root = query.from(dh.getEntityType());

    query =
      query
        .multiselect(root.get(columnName1).as(String.class), root.get(columnName2).as(String.class))
        .distinct(true);

    if (listingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(listingOptions, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  private static Set<String> getPairSets(Tuple setFieldsTuple, String match) {
    if (anySetValueContains(setFieldsTuple.get(0), match)) {
      return reconstructSetFromTuple(setFieldsTuple.get(1));
    } else if (anySetValueContains(setFieldsTuple.get(1), match)) {
      return reconstructSetFromTuple(setFieldsTuple.get(0));
    } else {
      throw new IllegalArgumentException("Error processing match: " + match);
    }
  }

  private static Set<String> reconstructSetFromTuple(Object tuple) {
    if (tuple == null) {
      return emptySet();
    } else {
      return reconstructSet(tuple.toString());
    }
  }

  private static boolean anySetValueContains(Object field, String match) {
    if (field == null) {
      return false;
    } else {
      return reconstructSet(field.toString()).stream()
        .anyMatch(value -> value.toLowerCase().contains(match.toLowerCase()));
    }
  }

  @Override
  public Stream<String> listDiseaseValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.listSetColumnValues("signatureDisease", listingOptions);
  }

  private Stream<String> listSetColumnValues(
    String columnName, DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.listColumnValues(String.class, columnName, listingOptions)
      .flatMap(v -> reconstructSet(v).stream()).distinct();
  }

  @Override
  public Stream<ExperimentalDesign> listExperimentalDesignValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.listColumnValues(ExperimentalDesign.class, "signatureExperimentalDesign", listingOptions);
  }

  @Override
  public Stream<String> listSignatureNameValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.listColumnValues(String.class, "signatureName", listingOptions);
  }

  @Override
  public Stream<String> listOrganismValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.listColumnValues(String.class, "signatureOrganism", listingOptions);
  }

  @Override
  public Stream<String> listSignatureSourceDbValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.listColumnValues(String.class, "signatureSourceDb", listingOptions);
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
    final Root<FullDrugSignatureInteraction> root = query.from(dh.getEntityType());

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
    return this.listColumnValues(String.class, "drugSourceName", listingOptions);
  }

  @Override
  public Stream<String> listDrugSourceDbValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.listColumnValues(String.class, "drugSourceDb", listingOptions);
  }

  @Override
  public Stream<String> listDrugCommonNameValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.listColumnValues(String.class, "drugCommonName", listingOptions);
  }

  @Override
  public Stream<String> listDrugMoaValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.listSetColumnValues("drugMoa", listingOptions);
  }
  
  @Override
  public Stream<DrugStatus> listDrugStatusValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.listColumnValues(DrugStatus.class, "drugStatus", listingOptions);
  }
  

  @Override
  public Stream<Integer> listSignaturePubMedIdValues(DrugSignatureInteractionListingOptions listingOptions) {
    final CriteriaBuilder cb = dh.cb();

    CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
    final Root<FullDrugSignatureInteraction> root = query.from(dh.getEntityType());
    final Join<FullDrugSignatureInteraction, ArticleMetadata> join =
      root.join("signatureArticlePubmedId", JoinType.LEFT);

    query = query.select(join.get("pubmedId")).distinct(true);

    if (listingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(listingOptions, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  private static interface TriConsumer<T, U, R> {
    void accept(T t, U u, R r);
  }

  private Predicate[] createPredicates(
    final DrugSignatureInteractionListingOptions listingOptions,
    final Root<FullDrugSignatureInteraction> root
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

    if (listingOptions.getMinDrugDss().isPresent()) {
      final Path<Double> drugDss = root.get("drugDss");

      andPredicates.add(cb.greaterThanOrEqualTo(drugDss, listingOptions.getMinDrugDss().get()));
    }

    if (listingOptions.getInteractionType().isPresent()) {
      final Path<DrugSignatureInteractionType> interactionType = root.get("interactionType");

      andPredicates.add(cb.equal(interactionType, listingOptions.getInteractionType().get()));
    }

    final SignatureListingOptions signatureListingOptions = listingOptions.getSignatureListingOptions();

    final TriConsumer<String, Boolean, Optional<String>> fieldLikeQueryBuilder =
      (attributeName, onlyRightLike, queryValue) -> {
        if (queryValue.isPresent()) {
          final Path<String> field = root.get(attributeName);

          if (onlyRightLike) {
            andPredicates.add(cb.like(cb.upper(field), queryValue.get().toUpperCase() + "%"));
          } else {
            andPredicates.add(cb.like(cb.upper(field), "%" + queryValue.get().toUpperCase() + "%"));
          }
        }
      };

    fieldLikeQueryBuilder.accept("signatureName", true, signatureListingOptions.getSignatureName());
    fieldLikeQueryBuilder
      .accept("signatureType", true, signatureListingOptions.getSignatureType().map(SignatureType::toString));
    fieldLikeQueryBuilder.accept("signatureOrganism", true, signatureListingOptions.getOrganism());
    fieldLikeQueryBuilder.accept("signatureSourceDb", true, signatureListingOptions.getSourceDb());
    fieldLikeQueryBuilder.accept("signatureDisease", false, signatureListingOptions.getDisease());

    if (signatureListingOptions.getCellType1().isPresent()) {
      Path<String> cellTypeA = root.get("signatureCellTypeA");
      Path<String> cellTypeB = root.get("signatureCellTypeB");

      if (signatureListingOptions.getCellType2().isPresent()) {
        andPredicates.add(
          cb.or(
            cb.and(
              cb.like(cellTypeA, "%" + signatureListingOptions.getCellType1().get() + "%"),
              cb.like(cellTypeB, "%" + signatureListingOptions.getCellType2().get() + "%")
            ),
            cb.and(
              cb.like(cellTypeA, "%" + signatureListingOptions.getCellType2().get() + "%"),
              cb.like(cellTypeB, "%" + signatureListingOptions.getCellType1().get() + "%")
            )
          )
        );
      } else {
        andPredicates.add(
          cb.or(
            cb.like(cellTypeA, "%" + signatureListingOptions.getCellType1().get() + "%"),
            cb.like(cellTypeB, "%" + signatureListingOptions.getCellType1().get() + "%")
          )
        );
      }
    }

    if (signatureListingOptions.getCellSubType1().isPresent()) {
      Path<String> cellSubTypeA = root.get("signatureCellSubTypeA");
      Path<String> cellSubTypeB = root.get("signatureCellSubTypeB");

      if (signatureListingOptions.getCellSubType2().isPresent()) {
        andPredicates.add(
          cb.or(
            cb.and(
              cb.like(cellSubTypeA, "%" + signatureListingOptions.getCellSubType1().get() + "%"),
              cb.like(cellSubTypeB, "%" + signatureListingOptions.getCellSubType2().get() + "%")
            ),
            cb.and(
              cb.like(cellSubTypeA, "%" + signatureListingOptions.getCellSubType2().get() + "%"),
              cb.like(cellSubTypeB, "%" + signatureListingOptions.getCellSubType1().get() + "%")
            )
          )
        );
      } else {
        andPredicates.add(
          cb.or(
            cb.like(cellSubTypeA, "%" + signatureListingOptions.getCellSubType1().get() + "%"),
            cb.like(cellSubTypeB, "%" + signatureListingOptions.getCellSubType1().get() + "%")
          )
        );
      }
    }

    signatureListingOptions.getExperimentalDesign().ifPresent(experimentalDesign -> {
      final Path<ExperimentalDesign> experimentalDesignPath = root.get("signatureExperimentalDesign");

      andPredicates.add(cb.equal(experimentalDesignPath, experimentalDesign));
    });

    signatureListingOptions.getSignaturePubMedId().ifPresent(signaturePubMedId -> {
      final Path<Integer> pubMedIdPath = root.get("signatureArticlePubmedId");

      andPredicates.add(cb.equal(pubMedIdPath, signaturePubMedId));
    });

    fieldLikeQueryBuilder.accept("drugSourceName", true, listingOptions.getDrugSourceName());
    fieldLikeQueryBuilder.accept("drugSourceDb", true, listingOptions.getDrugSourceDb());
    fieldLikeQueryBuilder.accept("drugCommonName", false, listingOptions.getDrugCommonName());
    fieldLikeQueryBuilder.accept("drugMoa", false, listingOptions.getDrugMoa());

    listingOptions.getDrugStatus().ifPresent(drugStatus -> {
      final Path<DrugStatus> drugStatusPath = root.get("drugStatus");

      andPredicates.add(cb.equal(drugStatusPath, drugStatus));
    });

    return andPredicates.toArray(new Predicate[andPredicates.size()]);
  }

  private Order[] createOrders(
    DrugSignatureInteractionListingOptions listingOptions, Root<FullDrugSignatureInteraction> root
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
              orders.add(order.apply(root.get("signatureCellTypeA")));
              break;
            case CELL_SUBTYPE_A:
              orders.add(order.apply(root.get("signatureCellSubTypeA")));
              break;
            case CELL_TYPE_B:
              orders.add(order.apply(root.get("signatureCellTypeB")));
              break;
            case CELL_SUBTYPE_B:
              orders.add(order.apply(root.get("signatureCellSubTypeB")));
              break;
            case DISEASE:
              orders.add(order.apply(root.get("signatureDisease")));
              break;
            case SIGNATURE_NAME:
              orders.add(order.apply(root.get("signatureName")));
              break;
            case EXPERIMENTAL_DESIGN:
              orders.add(order.apply(root.get("signatureExperimentalDesign")));
              break;
            case ORGANISM:
              orders.add(order.apply(root.get("signatureOrganism")));
              break;
            case SIGNATURE_SOURCE_DB:
              orders.add(order.apply(root.get("signatureSourceDb")));
              break;
            case SIGNATURE_TYPE:
              orders.add(order.apply(root.get("signatureType")));
              break;

            case DRUG_SOURCE_NAME:
              orders.add(order.apply(root.get("drugSourceName")));
              break;
            case DRUG_SOURCE_DB:
              orders.add(order.apply(root.get("drugSourceDb")));
              break;
            case DRUG_COMMON_NAME:
              orders.add(order.apply(root.get("drugCommonName")));
              break;
            case DRUG_MOA:
              orders.add(order.apply(root.get("drugMoa")));
              break;
            case DRUG_DSS:
              orders.add(order.apply(root.get("drugDss")));
              break;
            case DRUG_STATUS:
              orders.add(order.apply(root.get("drugStatus")));
              break;

            case INTERACTION_TYPE:
              orders.add(order.apply(root.get("interactionType")));
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
