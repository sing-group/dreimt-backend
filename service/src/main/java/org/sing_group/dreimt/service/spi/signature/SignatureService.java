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

import java.util.Optional;
import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.dreimt.domain.dao.signature.SignatureListingOptions;
import org.sing_group.dreimt.domain.entities.signature.CellTypeAndSubtype;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;

@Local
public interface SignatureService {
  Optional<Signature> get(String signatureName);

  Stream<Signature> list(SignatureListingOptions listingOptions);

  long count(SignatureListingOptions listingOptions);

  Stream<String> listSignatureNameValues(SignatureListingOptions listingOptions);

  Stream<CellTypeAndSubtype> listCellTypeAndSubtype1Values(SignatureListingOptions signatureListingOptions);

  Stream<CellTypeAndSubtype> listCellTypeAndSubtype2Values(SignatureListingOptions signatureListingOptions);

  Stream<ExperimentalDesign> listExperimentalDesignValues(SignatureListingOptions signatureListingOptions);

  Stream<String> listOrganismValues(SignatureListingOptions signatureListingOptions);

  Stream<String> listDiseaseValues(SignatureListingOptions signatureListingOptions);

  Stream<String> listSourceDbValues(SignatureListingOptions signatureListingOptions);

  Stream<SignatureType> listSignatureTypeValues(SignatureListingOptions signatureListingOptions);

  Stream<Integer> listSignaturePubMedIdValues(SignatureListingOptions listingOptions);
}
