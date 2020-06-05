/*-
 * #%L
 * DREIMT - Domain
 * %%
 * Copyright (C) 2018 - 2020 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
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

import static java.util.Optional.empty;
import static java.util.Optional.of;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.sing_group.dreimt.domain.entities.signature.CellTypeAndSubtype;
import org.sing_group.dreimt.domain.entities.signature.DrugInteractionEffect;

public class CellTypeAndSubTypeFilter {
  private final boolean isOr;
  private final String type;
  private final String subType;
  private boolean applicable;

  public CellTypeAndSubTypeFilter(String type, String subType) {
    this.applicable = (type != null || subType != null);
    this.type = type == null ? "" : type;
    this.subType = subType == null ? "" : subType;
    this.isOr = false;
  }

  public CellTypeAndSubTypeFilter(String typeOrSubType) {
    this.applicable = true;
    this.type = typeOrSubType;
    this.subType = typeOrSubType;
    this.isOr = true;
  }

  public boolean isApplicable() {
    return applicable;
  }

  public boolean isOr() {
    return isOr;
  }

  public Predicate getPredicate(
    CriteriaBuilder cb, CellTypeAndSubTypeFilter cellTypeAndSubType2Filter,
    Path<String> cellTypeA, Path<String> cellSubTypeA,
    Path<String> cellTypeB, Path<String> cellSubTypeB
    ) { 
    return getPredicate(
      cb, cellTypeAndSubType2Filter, cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB,
      null, null, null, null, null, null, null, null, null
    );
  }
  
  public Predicate getPredicate(
    CriteriaBuilder cb, CellTypeAndSubTypeFilter cellTypeAndSubType2Filter,
    Path<String> cellTypeA, Path<String> cellSubTypeA,
    Path<String> cellTypeB, Path<String> cellSubTypeB,
    Path<String> signatureDiseaseA, Path<String> signatureDiseaseB, String cellType1DiseaseFilter,
    Path<String> signatureTreatmentA, Path<String> signatureTreatmentB, String cellType1TreatmentFilter,
    Path<DrugInteractionEffect> cellTypeAEffect,
    Path<DrugInteractionEffect> cellTypeBEffect,
    DrugInteractionEffect drugEffectFilter
  ) {
    List<Predicate> cellTypeAPredicates = new LinkedList<>();
    List<Predicate> cellTypeBPredicates = new LinkedList<>();

    if(drugEffectFilter != null) {
      cellTypeAPredicates.add(cb.equal(cellTypeAEffect, drugEffectFilter));
      cellTypeBPredicates.add(cb.equal(cellTypeBEffect, drugEffectFilter));
    }

    if(cellType1TreatmentFilter != null) {
      cellTypeAPredicates.add(cb.like(signatureTreatmentA, "%" + cellType1TreatmentFilter + "%"));
      cellTypeBPredicates.add(cb.like(signatureTreatmentB, "%" + cellType1TreatmentFilter + "%"));
    }

    if (cellType1DiseaseFilter != null) {
      cellTypeAPredicates.add(cb.like(signatureDiseaseA, "%" + cellType1DiseaseFilter + "%"));
      cellTypeBPredicates.add(cb.like(signatureDiseaseB, "%" + cellType1DiseaseFilter + "%"));
    }

    String type1 = this.type;
    String subType1 = this.subType;

    if (!cellTypeAndSubType2Filter.isApplicable()) {
      if (isOr) {
        cellTypeAPredicates.add(cb.or(cb.equal(cellTypeA, type1), cb.equal(cellSubTypeA, type1)));
        cellTypeBPredicates.add(cb.or(cb.equal(cellTypeB, type1), cb.equal(cellSubTypeB, type1)));
      } else {
        cellTypeAPredicates.addAll(typeAndSubTypePredicates(cb, cellTypeA, cellSubTypeA, type1, subType1));
        cellTypeBPredicates.addAll(typeAndSubTypePredicates(cb, cellTypeB, cellSubTypeB, type1, subType1));
      }

      return cb.or(
        andPredicate(cb, cellTypeAPredicates),
        andPredicate(cb, cellTypeBPredicates)
      );
    }

    String type2 = cellTypeAndSubType2Filter.type;
    String subType2 = cellTypeAndSubType2Filter.subType;

    if (isOr) {
      if (cellTypeAndSubType2Filter.isOr) {
        cellTypeAPredicates.add(cb.or(cb.equal(cellTypeA, type1), cb.equal(cellSubTypeA, type1)));
        cellTypeBPredicates.add(cb.or(cb.equal(cellTypeB, type1), cb.equal(cellSubTypeB, type1)));
        
        return cb.or(
          cb.and(
            andPredicate(cb, cellTypeAPredicates),
            cb.or(cb.equal(cellTypeB, type2), cb.equal(cellSubTypeB, type2))
          ),
          cb.and(
            cb.or(cb.equal(cellTypeA, type2), cb.equal(cellSubTypeA, type2)),
            andPredicate(cb, cellTypeBPredicates)
          )
        );
      } else {
        cellTypeAPredicates.add(cb.or(cb.equal(cellTypeA, type1), cb.equal(cellSubTypeA, type1)));
        cellTypeBPredicates.add(cb.or(cb.equal(cellTypeB, type1), cb.equal(cellSubTypeB, type1)));
        
        return cb.or(
          cb.and(
              andPredicate(
                cb, typeAndSubTypePredicates(cb, cellTypeB, cellSubTypeB, type2, subType2)
              ),
              andPredicate(cb, cellTypeAPredicates)
            ),
          cb.and(
            andPredicate(
              cb, typeAndSubTypePredicates(cb, cellTypeA, cellSubTypeA, type2, subType2)
            ),
            andPredicate(cb, cellTypeBPredicates)
          )
        );
      }
    } else {
      if (cellTypeAndSubType2Filter.isOr) {
        cellTypeAPredicates.addAll(
          typeAndSubTypePredicates(cb, cellTypeA, cellSubTypeA, type1, subType1)
        );
        cellTypeBPredicates.addAll(
          typeAndSubTypePredicates(cb, cellTypeB, cellSubTypeB, type1, subType1)
        );
        
        return cb.or(
          cb.and(
            andPredicate(cb, cellTypeBPredicates),
            cb.or(cb.equal(cellTypeA, type2), cb.equal(cellSubTypeA, type2))
          ),
          cb.and(
              andPredicate(cb, cellTypeAPredicates),
              cb.or(cb.equal(cellTypeB, type2), cb.equal(cellSubTypeB, type2))
          )
        );
      } else {
        cellTypeAPredicates.addAll(
          typeAndSubTypePredicates(cb, cellTypeA, cellSubTypeA, type1, subType1)
        );
        cellTypeBPredicates.addAll(
          typeAndSubTypePredicates(cb, cellTypeB, cellSubTypeB, type1, subType1)
        );

        return cb.or(
          cb.and(
            andPredicate(cb, cellTypeAPredicates),
            andPredicate(cb, 
              typeAndSubTypePredicates(cb, cellTypeB, cellSubTypeB, type2, subType2)
            )
          ),
          cb.and(
            andPredicate(cb,
              typeAndSubTypePredicates(cb, cellTypeA, cellSubTypeA, type2, subType2)
            ),
            andPredicate(cb, cellTypeBPredicates)
          )
        );
      }
    }
  }

  private static Predicate andPredicate(
    CriteriaBuilder cb,
    List<Predicate> predicatesList
  ) {
    return cb.and(predicatesList.toArray(new Predicate[predicatesList.size()]));
  }
    
  private static List<Predicate> typeAndSubTypePredicates(
    CriteriaBuilder cb,
    Path<String> typePath, Path<String> subTypePath,
    String typeFilter, String subTypeFilter
  ) {
    List<Predicate> predicates = new LinkedList<>();
    if (!typeFilter.isEmpty()) {
      predicates.add(cb.equal(typePath, typeFilter));
    }
    if (!subTypeFilter.isEmpty()) {
      predicates.add(cb.equal(subTypePath, subTypeFilter));
    }
    return predicates;
  }

  public boolean matchesFilter(CellTypeAndSubtype cellTypeAndSubType) {
    if (isOr) {
      if (
        cellTypeAndSubType.getType() != null && !cellTypeAndSubType.getType().toLowerCase()
          .contains(this.type.toLowerCase())
      ) {
        if (
          cellTypeAndSubType.getSubType() != null && !cellTypeAndSubType.getSubType().toLowerCase()
            .contains(this.type.toLowerCase())
        ) {
          return false;
        }
      }
    } else {
      if (
        cellTypeAndSubType.getType() != null
          && !cellTypeAndSubType.getType().toLowerCase().contains(this.type.toLowerCase())
      ) {
        return false;
      }

      if (
        cellTypeAndSubType.getSubType() != null
          && !cellTypeAndSubType.getSubType().toLowerCase().contains(this.subType.toLowerCase())
      ) {
        return false;
      }
    }

    return true;
  }

  public Optional<String> getType() {
    return of(type);
  }

  public Optional<String> getSubType() {
    return of(subType);
  }

  public Optional<String> getTypeOrSubType() {
    return isOr ? of(this.type) : empty();
  }

  @Override
  public String toString() {
    if (isOr) {
      return "cellTypeOrSubType1 = " + this.type;
    } else {
      return "cellType1 = " + (this.type.isEmpty() ? "<undefined>" : this.type)
        + " cellSubType1 = " + (this.subType.isEmpty() ? "<undefined>" : this.subType);
    }
  }
}
