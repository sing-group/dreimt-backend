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
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.DAOHelper;
import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.dao.spi.signature.DrugSignatureInteractionDao;
import org.sing_group.dreimt.domain.entities.query.DrugSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
import org.sing_group.dreimt.domain.entities.signature.Signature;

@Default
@Transactional(MANDATORY)
public class DefaultDrugSignatureInteractionDao implements DrugSignatureInteractionDao {

  @PersistenceContext
  private EntityManager em;

  private DAOHelper<Integer, DrugSignatureInteraction> dh;

  DefaultDrugSignatureInteractionDao() {}

  public DefaultDrugSignatureInteractionDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, DrugSignatureInteraction.class, this.em);
  }

  @Override
  public Stream<DrugSignatureInteraction> list(DrugSignatureInteractionListingOptions listingOptions) {
    if (!listingOptions.hasAnyQueryModification()) {
      return this.dh.list().stream();
    } else {
      final CriteriaBuilder cb = dh.cb();

      CriteriaQuery<DrugSignatureInteraction> query = dh.createCBQuery();
      final Root<DrugSignatureInteraction> root = query.from(dh.getEntityType());

      query = query.select(root);

      Join<DrugSignatureInteraction, Signature> joinSignature = root.join("signature", JoinType.LEFT);
      if (listingOptions.getCellTypeA().isPresent()) {
        Join<Signature, String> joinSignatureCellTypeA = joinSignature.join("cellTypeA", JoinType.LEFT);

        query = query.where(cb.like(joinSignatureCellTypeA, "%" + listingOptions.getCellTypeA().get() + "%"));
      }

      if (listingOptions.getCellTypeB().isPresent()) {
        Join<Signature, String> joinSignatureCellTypeB = joinSignature.join("cellTypeB", JoinType.LEFT);

        query = query.where(cb.like(joinSignatureCellTypeB, "%" + listingOptions.getCellTypeB().get() + "%"));
      }

      if (listingOptions.getExperimentalDesign().isPresent()) {
        final Expression<String> experimentalDesign = joinSignature.get("experimentalDesign").as(String.class);

        query = query.where(cb.like(experimentalDesign, "%" + listingOptions.getExperimentalDesign().get() + "%"));
      }

      if (listingOptions.getOrganism().isPresent()) {
        final Expression<String> organism = joinSignature.get("organism").as(String.class);

        query = query.where(cb.like(organism, "%" + listingOptions.getOrganism().get() + "%"));
      }

      Join<DrugSignatureInteraction, Drug> joinDrug = root.join("drug", JoinType.LEFT);
      if (listingOptions.getDrugSourceName().isPresent()) {
        final Expression<String> sourceName = joinDrug.get("id").get("sourceName").as(String.class);

        query = query.where(cb.like(sourceName, "%" + listingOptions.getDrugSourceName().get() + "%"));
      }

      if (listingOptions.getDrugCommonName().isPresent()) {
        final Expression<String> commonName = joinDrug.get("commonName").as(String.class);

        query = query.where(cb.like(commonName, "%" + listingOptions.getDrugCommonName().get() + "%"));
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
}
