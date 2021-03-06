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
import static org.sing_group.dreimt.domain.dao.signature.DefaultSignatureDao.CELL_TYPE_AND_SUBTYPE_COMPARATOR;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.sing_group.dreimt.domain.dao.spi.signature.DrugDao;
import org.sing_group.dreimt.domain.dao.spi.signature.DrugSignatureInteractionDao;
import org.sing_group.dreimt.domain.dao.spi.signature.SignatureDao;
import org.sing_group.dreimt.domain.entities.signature.ArticleMetadata;
import org.sing_group.dreimt.domain.entities.signature.CellTypeAndSubtype;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.domain.entities.signature.DrugInteractionEffect;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionField;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;
import org.sing_group.dreimt.domain.entities.signature.DrugStatus;
import org.sing_group.dreimt.domain.entities.signature.DrugTargetGene;
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
  private SignatureDao signatureDao;

  @Inject
  private DrugDao drugDao;

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
            reconstructDrugTargetGenes(reconstructSet(fdsi.getDrugTargetGenes())),
            fdsi.getDrugDss(), fdsi.getDrugPubChemId(), fdsi.getDrugDbProfilesCount()
          );

        Signature signature =
          buildSignature(
            fdsi.getSignatureType(),
            fdsi.getSignatureName(),
            fdsi.getSignatureCellTypeA(),
            fdsi.getSignatureCellTypeAOntologyId(),
            fdsi.getSignatureCellSubTypeA(),
            fdsi.getSignatureCellSubTypeAOntologyId(),
            fdsi.getSignatureCellTypeB(),
            fdsi.getSignatureCellTypeBOntologyId(),
            fdsi.getSignatureCellSubTypeB(),
            fdsi.getSignatureCellSubTypeBOntologyId(),
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
          drug, signature, fdsi.getInteractionType(), fdsi.getTau(), fdsi.getUpFdr(), fdsi.getDownFdr(),
          fdsi.getCellTypeAEffect(), fdsi.getCellTypeBEffect()
        );
      });
  }

  private static Signature buildSignature(
    SignatureType signatureType, String signatureName, String cellTypeA, String cellTypeAOntologyId,
    String cellSubTypeA, String cellSubTypeAOntologyId, String cellTypeB, String cellTypeBOntologyId,
    String cellSubTypeB, String cellSubTypeBOntologyId, Optional<ArticleMetadata> articleMetadata, String sourceDb,
    String sourceDbUrl, ExperimentalDesign experimentalDesign, String organism, Set<String> disease,
    Set<String> treatmentA, Set<String> treatmentB, Set<String> diseaseA, Set<String> diseaseB, String localisationA,
    String localisationB, String stateA, String stateB
  ) {
    if (signatureType.equals(SignatureType.GENESET)) {
      if (articleMetadata.isPresent()) {
        return new GeneSetSignature(
          signatureName, cellTypeA, cellTypeAOntologyId, cellSubTypeA, cellSubTypeAOntologyId, cellTypeB,
          cellTypeBOntologyId, cellSubTypeB, cellSubTypeBOntologyId, articleMetadata.get(), sourceDb, sourceDbUrl,
          experimentalDesign, organism, disease, treatmentA, treatmentB, diseaseA, diseaseB, localisationA,
          localisationB, stateA, stateB
        );
      } else {
        return new GeneSetSignature(
          signatureName, cellTypeA, cellTypeAOntologyId, cellSubTypeA, cellSubTypeAOntologyId, cellTypeB,
          cellTypeBOntologyId, cellSubTypeB, cellSubTypeBOntologyId, sourceDb, sourceDbUrl,
          experimentalDesign, organism, disease, treatmentA, treatmentB, diseaseA, diseaseB, localisationA,
          localisationB, stateA, stateB
        );
      }
    } else {
      if (articleMetadata.isPresent()) {
        return new UpDownSignature(
          signatureName, cellTypeA, cellTypeAOntologyId, cellSubTypeA, cellSubTypeAOntologyId, cellTypeB,
          cellTypeBOntologyId, cellSubTypeB, cellSubTypeBOntologyId, articleMetadata.get(), sourceDb, sourceDbUrl,
          experimentalDesign, organism, disease, treatmentA, treatmentB, diseaseA, diseaseB, localisationA,
          localisationB, stateA, stateB
        );
      } else {
        return new UpDownSignature(
          signatureName, cellTypeA, cellTypeAOntologyId, cellSubTypeA, cellSubTypeAOntologyId, cellTypeB,
          cellTypeBOntologyId, cellSubTypeB, cellSubTypeBOntologyId, sourceDb, sourceDbUrl, experimentalDesign,
          organism, disease, treatmentA, treatmentB, diseaseA, diseaseB, localisationA, localisationB, stateA, stateB
        );
      }
    }
  }

  private static Set<String> reconstructSet(String field) {
    return field == null ? emptySet() : Stream.of(field.split("##")).map(String::trim).collect(toSet());
  }

  private static Set<DrugTargetGene> reconstructDrugTargetGenes(Set<String> targetGenes) {
    return targetGenes.isEmpty() ? emptySet() : targetGenes.stream().map(tG -> {
      String[] split = tG.split("\\|");
      return new DrugTargetGene(split[0], split[1]);
    }).collect(toSet());
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
  public Stream<CellTypeAndSubtype> listCellTypeAndSubtype1Values(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getDrugListingOptions().hasAnyQueryModification()
    ) {
      return this.signatureDao.listCellTypeAndSubtype1Values(listingOptions.getSignatureListingOptions());
    }
    
    return listMultipleColumnCollectionValues(
      listingOptions, "signatureCellTypeA", "signatureCellSubTypeA", "signatureCellTypeB", "signatureCellSubTypeB"
    ).map(tuple -> {
      return Arrays.asList(tupleToCellTypeAndSubtype(tuple, 0, 1), tupleToCellTypeAndSubtype(tuple, 2, 3));
    })
      .flatMap(List::stream)
      .distinct()
      .filter(
        value -> listingOptions.getSignatureListingOptions().getCellTypeAndSubType1Filter().matchesFilter(value)
      )
      .sorted(CELL_TYPE_AND_SUBTYPE_COMPARATOR);
  }

  @Override
  public Stream<String> listCellType1DiseaseValues(DrugSignatureInteractionListingOptions listingOptions) {
    SignatureListingOptions signatureListingOptions = listingOptions.getSignatureListingOptions();

    if (!signatureListingOptions.getCellTypeAndSubType1Filter().isApplicable()) {
      throw new IllegalArgumentException("A cellType1 filter must be defined in orter to list cellTypeAndSubType2 values");
    }

    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getDrugListingOptions().hasAnyQueryModification()
    ) {
      return this.signatureDao.listCellType1DiseaseValues(signatureListingOptions);
    }

    return listMultipleColumnCollectionValues(
      listingOptions,
      "signatureCellTypeA", "signatureCellSubTypeA", "signatureDiseaseA",
      "signatureCellTypeB", "signatureCellSubTypeB", "signatureDiseaseB"
    )
      .map(tuple -> {
        CellTypeAndSubtype typeA = tupleToCellTypeAndSubtype(tuple, 0, 1);
        CellTypeAndSubtype typeB = tupleToCellTypeAndSubtype(tuple, 3, 4);

        String additionalSetFieldFilter = listingOptions.getSignatureListingOptions().getCellType1Disease().orElse("");
        List<CustomCellTypeAndSubtype> list = toCustomCellTypeAndSubtypeList(tuple.get(2), additionalSetFieldFilter, typeA);
        list.addAll(toCustomCellTypeAndSubtypeList(tuple.get(5), additionalSetFieldFilter, typeB));

        return list;
      })
      .flatMap(List::stream)
      .distinct()
      .filter(
        value -> listingOptions.getSignatureListingOptions().getCellTypeAndSubType1Filter().matchesFilter(value)
      )
      .map(CustomCellTypeAndSubtype::getAdditionalInfo)
      .distinct();
  }

  @Override
  public Stream<String> listCellType1TreatmentValues(DrugSignatureInteractionListingOptions listingOptions) {
    SignatureListingOptions signatureListingOptions = listingOptions.getSignatureListingOptions();

    if (!signatureListingOptions.getCellTypeAndSubType1Filter().isApplicable()) {
      throw new IllegalArgumentException("A cellType1 filter must be defined in orter to list cellTypeAndSubType2 values");
    }

    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getDrugListingOptions().hasAnyQueryModification()
    ) {
      return this.signatureDao.listCellType1TreatmentValues(signatureListingOptions);
    }

    return listMultipleColumnCollectionValues(
      listingOptions,
      "signatureCellTypeA", "signatureCellSubTypeA", "signatureTreatmentA",
      "signatureCellTypeB", "signatureCellSubTypeB", "signatureTreatmentB"
    )
      .map(tuple -> {
        CellTypeAndSubtype typeA = tupleToCellTypeAndSubtype(tuple, 0, 1);
        CellTypeAndSubtype typeB = tupleToCellTypeAndSubtype(tuple, 3, 4);

        String additionalSetFieldFilter = listingOptions.getSignatureListingOptions().getCellType1Treatment().orElse("");
        List<CustomCellTypeAndSubtype> list = toCustomCellTypeAndSubtypeList(tuple.get(2), additionalSetFieldFilter, typeA);
        list.addAll(toCustomCellTypeAndSubtypeList(tuple.get(5), additionalSetFieldFilter, typeB));

        return list;
      })
      .flatMap(List::stream)
      .distinct()
      .filter(
        value -> listingOptions.getSignatureListingOptions().getCellTypeAndSubType1Filter().matchesFilter(value)
      )
      .map(CustomCellTypeAndSubtype::getAdditionalInfo)
      .distinct();
  }

  private static List<CustomCellTypeAndSubtype> toCustomCellTypeAndSubtypeList(
    Object additionalSetFieldValue, String additionalSetFieldFilter, CellTypeAndSubtype cellTypeAndSubtype
  ) {
    List<CustomCellTypeAndSubtype> toret = new LinkedList<>();

    if (additionalSetFieldValue != null) {
      Set<String> fieldValues = reconstructSet(additionalSetFieldValue.toString());
      for (String value : fieldValues) {
        if (value.contains(additionalSetFieldFilter)) {
          toret.add(new CustomCellTypeAndSubtype(cellTypeAndSubtype, value));
        }
      }
    }

    return toret;
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

    query = query.multiselect(joins);

    if (listingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(listingOptions, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  private static CellTypeAndSubtype tupleToCellTypeAndSubtype(Tuple tuple, int typeIndex, int subTypeIndex) {
    String type = tuple.get(typeIndex).toString();
    String subType = tuple.get(subTypeIndex).toString();

    return new CellTypeAndSubtype(type, subType);
  }

  @Override
  public Stream<CellTypeAndSubtype> listCellTypeAndSubtype2Values(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    SignatureListingOptions signatureListingOptions = listingOptions.getSignatureListingOptions();

    if (!signatureListingOptions.getCellTypeAndSubType1Filter().isApplicable()) {
      throw new IllegalArgumentException("A cellType1 filter must be defined in orter to list cellTypeAndSubType2 values");
    }
    
    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getDrugListingOptions().hasAnyQueryModification()
    ) {
      return this.signatureDao.listCellTypeAndSubtype2Values(signatureListingOptions);
    }

    return listMultipleColumnCollectionValues(
      listingOptions, "signatureCellTypeA", "signatureCellSubTypeA", "signatureCellTypeB", "signatureCellSubTypeB"
    )
      .map(tuple -> getTuplePair(signatureListingOptions, tupleToCellTypeAndSubtype(tuple, 0, 1), tupleToCellTypeAndSubtype(tuple, 2, 3)))
      .flatMap(List::stream)
      .filter(DefaultDrugSignatureInteractionDao::notEmpty)
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
  public Stream<String> listDiseaseValues(DrugSignatureInteractionListingOptions listingOptions) {
    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getDrugListingOptions().hasAnyQueryModification()
    ) {
      return this.signatureDao.listDiseaseValues(listingOptions.getSignatureListingOptions());
    }

    Stream<String> diseaseValues = this.listSetColumnValues("signatureDisease", listingOptions);
    if (listingOptions.getSignatureListingOptions().getDisease().isPresent()) {
      final String diseaseFilter = listingOptions.getSignatureListingOptions().getDisease().get().toLowerCase();
      return diseaseValues
        .filter(d -> d.toLowerCase().contains(diseaseFilter));
    } else {
      return diseaseValues;
    }
  }

  private Stream<String> listSetColumnValues(
    String columnName, DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.listColumnValues(String.class, columnName, listingOptions)
      .flatMap(v -> reconstructSet(v).stream())
      .distinct();
  }

  @Override
  public Stream<ExperimentalDesign> listExperimentalDesignValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getDrugListingOptions().hasAnyQueryModification()
    ) {
      return this.signatureDao.listExperimentalDesignValues(listingOptions.getSignatureListingOptions());
    }

    return this.listColumnValues(ExperimentalDesign.class, "signatureExperimentalDesign", listingOptions);
  }

  @Override
  public Stream<String> listSignatureNameValues(DrugSignatureInteractionListingOptions listingOptions) {
    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getDrugListingOptions().hasAnyQueryModification()
    ) {
      return this.signatureDao.listSignatureNameValues(listingOptions.getSignatureListingOptions());
    }

    return this.listColumnValues(String.class, "signatureName", listingOptions);
  }

  @Override
  public Stream<String> listOrganismValues(DrugSignatureInteractionListingOptions listingOptions) {
    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getDrugListingOptions().hasAnyQueryModification()
    ) {
      return this.signatureDao.listOrganismValues(listingOptions.getSignatureListingOptions());
    }

    return this.listColumnValues(String.class, "signatureOrganism", listingOptions);
  }

  @Override
  public Stream<String> listSignatureSourceDbValues(DrugSignatureInteractionListingOptions listingOptions) {
    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getDrugListingOptions().hasAnyQueryModification()
    ) {
      return this.signatureDao.listSourceDbValues(listingOptions.getSignatureListingOptions());
    }

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
  public Stream<String> listDrugCommonNameValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getSignatureListingOptions().hasAnyQueryModification()
    ) {
      return this.drugDao.listDrugCommonNameValues(listingOptions.getDrugListingOptions());
    }

    return this.listColumnValues(String.class, "drugCommonName", listingOptions);
  }

  @Override
  public Stream<String> listDrugMoaValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getSignatureListingOptions().hasAnyQueryModification()
    ) {
      return this.drugDao.listDrugMoaValues(listingOptions.getDrugListingOptions());
    }

    return this.listSetColumnValues("drugMoa", listingOptions);
  }
  
  @Override
  public Stream<DrugStatus> listDrugStatusValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getSignatureListingOptions().hasAnyQueryModification()
    ) {
      return this.drugDao.listDrugStatusValues(listingOptions.getDrugListingOptions());
    }

    return this.listColumnValues(DrugStatus.class, "drugStatus", listingOptions);
  }

  @Override
  public Stream<Integer> listSignaturePubMedIdValues(DrugSignatureInteractionListingOptions listingOptions) {
    if (
      !listingOptions.hasAnyDrugSignatureInteractionQueryModification()
        && !listingOptions.getDrugListingOptions().hasAnyQueryModification()
    ) {
      return this.signatureDao.listSignaturePubMedIdValues(listingOptions.getSignatureListingOptions());
    }
    
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
    final SignatureListingOptions signatureListingOptions = listingOptions.getSignatureListingOptions();
    final DrugListingOptions drugListingOptions = listingOptions.getDrugListingOptions();
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

    if (drugListingOptions.getMinDss().isPresent()) {
      final Path<Double> drugDss = root.get("drugDss");

      andPredicates.add(cb.greaterThanOrEqualTo(drugDss, drugListingOptions.getMinDss().get()));
    }

    if (listingOptions.getInteractionType().isPresent()) {
      final Path<DrugSignatureInteractionType> interactionType = root.get("interactionType");

      andPredicates.add(cb.equal(interactionType, listingOptions.getInteractionType().get()));
    }

    final TriConsumer<String, Boolean, Optional<String>> fieldLikeQueryBuilder =
      (attributeName, useEquals, queryValue) -> {
        if (queryValue.isPresent()) {
          final Path<String> field = root.get(attributeName);

          if (useEquals) {
            andPredicates.add(cb.equal(cb.upper(field), queryValue.get().toUpperCase()));
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

    if(signatureListingOptions.getCellTypeAndSubType1Filter().isApplicable()) {
      Path<String> cellTypeA = root.get("signatureCellTypeA");
      Path<String> cellTypeB = root.get("signatureCellTypeB");
      Path<String> cellSubTypeA = root.get("signatureCellSubTypeA");
      Path<String> cellSubTypeB = root.get("signatureCellSubTypeB");
      Path<String> signatureDiseaseA = root.get("signatureDiseaseA");
      Path<String> signatureDiseaseB = root.get("signatureDiseaseB");
      Path<String> signatureTreatmentA = root.get("signatureTreatmentA");
      Path<String> signatureTreatmentB = root.get("signatureTreatmentB");
      Path<DrugInteractionEffect> cellTypeAEffect = root.get("cellTypeAEffect");
      Path<DrugInteractionEffect> cellTypeBEffect = root.get("cellTypeBEffect");

      andPredicates.add(
        signatureListingOptions.getCellTypeAndSubType1Filter().getPredicate(
          cb, signatureListingOptions.getCellTypeAndSubType2Filter(),
          cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB,
          signatureDiseaseA, signatureDiseaseB, signatureListingOptions.getCellType1Disease().orElse(null),
          signatureTreatmentA, signatureTreatmentB, signatureListingOptions.getCellType1Treatment().orElse(null),
          cellTypeAEffect, cellTypeBEffect, listingOptions.getCellType1Effect().orElse(null)
        )
      );
    }

    signatureListingOptions.getExperimentalDesign().ifPresent(experimentalDesign -> {
      final Path<ExperimentalDesign> experimentalDesignPath = root.get("signatureExperimentalDesign");

      andPredicates.add(cb.equal(experimentalDesignPath, experimentalDesign));
    });

    signatureListingOptions.getSignaturePubMedId().ifPresent(signaturePubMedId -> {
      final Path<Integer> pubMedIdPath = root.get("signatureArticlePubmedId");

      andPredicates.add(cb.equal(pubMedIdPath, signaturePubMedId));
    });

    fieldLikeQueryBuilder.accept("drugCommonName", true, drugListingOptions.getCommonName());
    fieldLikeQueryBuilder.accept("drugMoa", false, drugListingOptions.getMoa());

    drugListingOptions.getStatus().ifPresent(drugStatus -> {
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
              orders.add(cb.desc(root.get("drugDss")));
              orders.add(cb.asc(root.get("drugStatus")));
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
