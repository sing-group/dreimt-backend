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

import static java.util.Collections.emptySet;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Stream.concat;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.ArrayList;
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
import org.sing_group.dreimt.domain.dao.spi.signature.SignatureDao;
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

    if (listingOptions.getCellTypeA().isPresent()) {
      Join<Signature, String> joinSignatureCellTypeA = root.join("cellTypeA", JoinType.LEFT);

      andPredicates.add(cb.like(joinSignatureCellTypeA, "%" + listingOptions.getCellTypeA().get() + "%"));
    }

    if (listingOptions.getCellTypeB().isPresent()) {
      Join<Signature, String> joinSignatureCellTypeB = root.join("cellTypeB", JoinType.LEFT);

      andPredicates.add(cb.like(joinSignatureCellTypeB, "%" + listingOptions.getCellTypeB().get() + "%"));
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
      final Path<String> disease = root.get("disease");
      
      andPredicates.add(cb.like(disease, "%" + listingOptions.getDisease().get() + "%"));
    }
    
    if (listingOptions.getSourceDb().isPresent()) {
      final Path<String> sourceDb = root.get("sourceDb");
      
      andPredicates.add(cb.like(sourceDb, "%" + listingOptions.getSourceDb().get() + "%"));
    }

    if (listingOptions.getSignatureType().isPresent()) {
      final Path<String> signatureType = root.get("signatureType");

      andPredicates.add(cb.equal(signatureType, listingOptions.getSignatureType().get()));
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
  public Stream<String> listCellTypeAValues(SignatureListingOptions signatureListingOptions) {
    return listCellTypeValues(signatureListingOptions, "cellTypeA");
  }

  @Override
  public Stream<String> listCellTypeBValues(SignatureListingOptions signatureListingOptions) {
    return listCellTypeValues(signatureListingOptions, "cellTypeB");
  }

  private Stream<String> listCellTypeValues(SignatureListingOptions signatureListingOptions, String cellTypeColumn) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<String> query = cb.createQuery(String.class);
    final Root<Signature> root = query.from(dh.getEntityType());
    final Join<Signature, ?> join = root.join(cellTypeColumn);

    query = query.multiselect(join.as(String.class)).distinct(true);

    if (signatureListingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(signatureListingOptions, null, root));
    }

    return this.em.createQuery(query).getResultList().stream();
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
  public Stream<String> listDiseaseValues(SignatureListingOptions signatureListingOptions) {
    return listColumnValues("disease", String.class, signatureListingOptions);
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
}
