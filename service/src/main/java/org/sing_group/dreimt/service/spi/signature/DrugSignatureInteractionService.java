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
package org.sing_group.dreimt.service.spi.signature;

import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.dao.signature.DrugSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;

@Local
public interface DrugSignatureInteractionService {
  Stream<DrugSignatureInteraction> list(DrugSignatureInteractionListingOptions listingOptions);

  long count(DrugSignatureInteractionListingOptions listingOptions);
  
  Stream<DrugSignatureInteraction> list(ListingOptions listingOptions, String freeText);
  
  long count(String freeText);

  Stream<String> listSignatureNameValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listCellTypeAValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listCellSubTypeAValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listCellTypeBValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listCellSubTypeBValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<ExperimentalDesign> listExperimentalDesignValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listOrganismValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listDiseaseValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listSignatureSourceDbValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<DrugSignatureInteractionType> listInteractionTypeValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<Integer> listSignaturePubMedIdValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listDrugSourceNameValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listDrugSourceDbValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listDrugCommonNameValues(DrugSignatureInteractionListingOptions listingOptions);
}
