/*-
 * #%L
 * DREIMT - Service
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
package org.sing_group.dreimt.service.signature;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.dao.signature.DrugSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.dao.spi.signature.DrugSignatureInteractionDao;
import org.sing_group.dreimt.domain.entities.signature.CellTypeAndSubtype;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.service.spi.signature.DrugSignatureInteractionService;

@Stateless
@PermitAll
public class DefaultDrugSignatureInteractionService implements DrugSignatureInteractionService {

  @Inject
  private DrugSignatureInteractionDao dao;

  @Override
  public Stream<DrugSignatureInteraction> list(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.list(listingOptions);
  }

  @Override
  public long count(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.count(listingOptions);
  }
  
  @Override
  public Stream<DrugSignatureInteraction> list(ListingOptions listingOptions, String freeText) {
    return this.dao.list(listingOptions, freeText);
  }
  
  @Override
  public long count(String freeText) {
    return this.dao.count(freeText);
  }

  @Override
  public Stream<String> listSignatureNameValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listSignatureNameValues(listingOptions);
  }

  @Override
  public Stream<CellTypeAndSubtype> listCellTypeAndSubtype1Values(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listCellTypeAndSubtype1Values(listingOptions);
  }

  @Override
  public Stream<CellTypeAndSubtype> listCellTypeAndSubtype2Values(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listCellTypeAndSubtype2Values(listingOptions);
  }

  @Override
  public Stream<String> listCellType1Values(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listCellType1Values(listingOptions);
  }

  @Override
  public Stream<String> listCellSubType1Values(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listCellSubType1Values(listingOptions);
  }

  @Override
  public Stream<String> listCellType2Values(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listCellType2Values(listingOptions);
  }

  @Override
  public Stream<String> listCellSubType2Values(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listCellSubType2Values(listingOptions);
  }

  @Override
  public Stream<ExperimentalDesign> listExperimentalDesignValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.dao.listExperimentalDesignValues(listingOptions);
  }

  @Override
  public Stream<String> listOrganismValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listOrganismValues(listingOptions);
  }

  @Override
  public Stream<String> listDiseaseValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listDiseaseValues(listingOptions);
  }

  @Override
  public Stream<String> listSignatureSourceDbValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listSignatureSourceDbValues(listingOptions);
  }

  @Override
  public Stream<DrugSignatureInteractionType> listInteractionTypeValues(
    DrugSignatureInteractionListingOptions listingOptions
  ) {
    return this.dao.listInteractionTypeValues(listingOptions);
  }

  @Override
  public Stream<Integer> listSignaturePubMedIdValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listSignaturePubMedIdValues(listingOptions);
  }

  @Override
  public Stream<String> listDrugSourceNameValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listDrugSourceNameValues(listingOptions);
  }

  @Override
  public Stream<String> listDrugSourceDbValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listDrugSourceDbValues(listingOptions);
  }

  @Override
  public Stream<String> listDrugCommonNameValues(DrugSignatureInteractionListingOptions listingOptions) {
    return this.dao.listDrugCommonNameValues(listingOptions);
  }

}
