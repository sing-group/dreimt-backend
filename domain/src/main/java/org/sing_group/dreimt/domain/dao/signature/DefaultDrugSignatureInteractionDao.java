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
import org.sing_group.dreimt.domain.dao.spi.signature.DrugSignatureInteractionDao;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
import org.sing_group.dreimt.domain.entities.signature.Signature;

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

      query = query.select(root)
        .where(createPredicates(listingOptions, root));

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

  private Predicate[] createPredicates(
    final DrugSignatureInteractionListingOptions listingOptions,
    final Root<DrugSignatureInteraction> root
  ) {
    final CriteriaBuilder cb = this.dh.cb();
    
    final List<Predicate> andPredicates = new ArrayList<>();

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

    SignatureListingOptions signatureListingOptions = listingOptions.getSignatureListingOptions();
    
    Join<DrugSignatureInteraction, Signature> joinSignature = root.join("signature", JoinType.LEFT);
    if (signatureListingOptions.getCellTypeA().isPresent()) {
      Join<Signature, String> joinSignatureCellTypeA = joinSignature.join("cellTypeA", JoinType.LEFT);

      andPredicates.add(cb.like(joinSignatureCellTypeA, "%" + signatureListingOptions.getCellTypeA().get() + "%"));
    }

    if (signatureListingOptions.getCellTypeB().isPresent()) {
      Join<Signature, String> joinSignatureCellTypeB = joinSignature.join("cellTypeB", JoinType.LEFT);

      andPredicates.add(cb.like(joinSignatureCellTypeB, "%" +signatureListingOptions.getCellTypeB().get() + "%"));
    }

    if (signatureListingOptions.getExperimentalDesign().isPresent()) {
      final Path<String> experimentalDesign = joinSignature.get("experimentalDesign");

      andPredicates.add(cb.equal(experimentalDesign, signatureListingOptions.getExperimentalDesign().get()));
    }

    if (signatureListingOptions.getOrganism().isPresent()) {
      final Path<String> organism = joinSignature.get("organism");

      andPredicates.add(cb.like(organism, "%" + signatureListingOptions.getOrganism().get() + "%"));
    }
    
    if (signatureListingOptions.getSignatureType().isPresent()) {
      final Path<String> signatureType = joinSignature.get("signatureType");

      andPredicates.add(cb.like(signatureType, "%" + signatureListingOptions.getSignatureType().get() + "%"));
    }

    Join<DrugSignatureInteraction, Drug> joinDrug = root.join("drug", JoinType.LEFT);
    if (listingOptions.getDrugSourceName().isPresent()) {
      final Path<String> sourceName = joinDrug.get("sourceName");

      andPredicates.add(cb.like(sourceName, "%" + listingOptions.getDrugSourceName().get() + "%"));
    }

    if (listingOptions.getDrugCommonName().isPresent()) {
      final Path<String> commonName = joinDrug.get("commonName");

      andPredicates.add(cb.like(commonName, "%" + listingOptions.getDrugCommonName().get() + "%"));
    }
    return andPredicates.toArray(new Predicate[andPredicates.size()]);
  }
}
