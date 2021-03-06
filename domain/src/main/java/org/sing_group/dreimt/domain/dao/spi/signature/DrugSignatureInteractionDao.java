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
package org.sing_group.dreimt.domain.dao.spi.signature;

import java.util.stream.Stream;

import org.sing_group.dreimt.domain.dao.signature.DrugSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.entities.signature.CellTypeAndSubtype;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;
import org.sing_group.dreimt.domain.entities.signature.DrugStatus;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;

public interface DrugSignatureInteractionDao {
  Stream<DrugSignatureInteraction> list(DrugSignatureInteractionListingOptions listingOptions);

  long count(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listSignatureNameValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<CellTypeAndSubtype> listCellTypeAndSubtype1Values(
    DrugSignatureInteractionListingOptions signatureListingOptions
  );

  Stream<CellTypeAndSubtype> listCellTypeAndSubtype2Values(
    DrugSignatureInteractionListingOptions signatureListingOptions
  );

  Stream<ExperimentalDesign> listExperimentalDesignValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listOrganismValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listDiseaseValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listSignatureSourceDbValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<Integer> listSignaturePubMedIdValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<DrugSignatureInteractionType> listInteractionTypeValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listDrugCommonNameValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listDrugMoaValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<DrugStatus> listDrugStatusValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listCellType1TreatmentValues(DrugSignatureInteractionListingOptions listingOptions);

  Stream<String> listCellType1DiseaseValues(DrugSignatureInteractionListingOptions listingOptions);
}
