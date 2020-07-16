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
import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.DaoHelper;
import org.sing_group.dreimt.domain.dao.spi.signature.DrugDao;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.domain.entities.signature.DrugStatus;

@Default
@Transactional(MANDATORY)
public class DefaultDrugDao implements DrugDao {

  @PersistenceContext
  protected EntityManager em;
  protected DaoHelper<Integer, Drug> dh;

  public DefaultDrugDao() {
    super();
  }

  public DefaultDrugDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DaoHelper.of(Integer.class, Drug.class, this.em);
  }

  @Override
  public Optional<Drug> get(int id) {
    return this.dh.get(id);
  }

  @Override
  public long count() {
    return this.dh.count();
  }

  @Override
  public Stream<String> listDrugCommonNameValues(DrugListingOptions listingOptions) {
    return this.listColumnValues(String.class, "commonName", listingOptions);
  }
  
  @Override
  public Stream<DrugStatus> listDrugStatusValues(DrugListingOptions listingOptions) {
    return this.listColumnValues(DrugStatus.class, "status", listingOptions);
  }

  private <T> Stream<T> listColumnValues(
    Class<T> targetClass, String columnName, DrugListingOptions listingOptions
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<T> query = cb.createQuery(targetClass);
    final Root<Drug> root = query.from(dh.getEntityType());

    query = query.select(root.get(columnName)).distinct(true);

    if (listingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(listingOptions, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  @Override
  public Stream<String> listDrugMoaValues(DrugListingOptions listingOptions) {
    Stream<String> diseaseValues = listElementCollectionValues(listingOptions, "moa");
    if (listingOptions.getMoa().isPresent()) {
      return diseaseValues
        .filter(d -> d.equals(listingOptions.getMoa().get()));
    } else {
      return diseaseValues;
    }
  }

  private Stream<String> listElementCollectionValues(
    DrugListingOptions listingOptions, String columnName
  ) {
    final CriteriaBuilder cb = dh.cb();
    CriteriaQuery<String> query = cb.createQuery(String.class);
    final Root<Drug> root = query.from(dh.getEntityType());
    final Join<Drug, ?> join = root.join(columnName);

    query = query.multiselect(join.as(String.class)).distinct(true);

    if (listingOptions.hasAnyQueryModification()) {
      query = query.where(createPredicates(listingOptions, root));
    }

    return this.em.createQuery(query).getResultList().stream();
  }

  private Predicate[] createPredicates(
    final DrugListingOptions listingOptions,
    final Root<Drug> root
  ) {
    final CriteriaBuilder cb = this.dh.cb();
    final List<Predicate> andPredicates = new ArrayList<>();

    if (listingOptions.getMinDss().isPresent()) {
      final Path<Double> drugDss = root.get("dss");

      andPredicates.add(cb.greaterThanOrEqualTo(drugDss, listingOptions.getMinDss().get()));
    }

    if (listingOptions.getCommonName().isPresent()) {
      final Path<String> commonName = root.get("commonName");

      andPredicates.add(cb.equal(cb.upper(commonName), listingOptions.getCommonName().get().toUpperCase()));
    }

    if (listingOptions.getMoa().isPresent()) {
      Join<Drug, String> joinDrugMoa = root.join("moa", JoinType.LEFT);

      andPredicates.add(cb.like(joinDrugMoa, "%" + listingOptions.getMoa().get() + "%"));
    }

    listingOptions.getStatus().ifPresent(drugStatus -> {
      final Path<DrugStatus> drugStatusPath = root.get("status");

      andPredicates.add(cb.equal(drugStatusPath, drugStatus));
    });

    return andPredicates.toArray(new Predicate[andPredicates.size()]);
  }
}
