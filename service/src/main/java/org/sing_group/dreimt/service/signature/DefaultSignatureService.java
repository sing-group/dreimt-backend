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

import java.util.Optional;
import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.dao.signature.SignatureListingOptions;
import org.sing_group.dreimt.domain.dao.spi.signature.SignatureDao;
import org.sing_group.dreimt.domain.entities.signature.CellTypeAndSubtype;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;
import org.sing_group.dreimt.service.spi.signature.SignatureService;

@Stateless
@PermitAll
public class DefaultSignatureService implements SignatureService {

  @Inject
  private SignatureDao dao;

  @Override
  public Optional<Signature> get(String signatureName) {
    return this.dao.get(signatureName);
  }

  @Override
  public Stream<Signature> list(SignatureListingOptions listingOptions) {
    return this.dao.list(listingOptions);
  }

  @Override
  public Stream<String> listSignatureNameValues(SignatureListingOptions signatureListingOptions) {
    return this.dao.listSignatureNameValues(signatureListingOptions);
  }

  @Override
  public Stream<CellTypeAndSubtype> listCellTypeAndSubtype1Values(SignatureListingOptions signatureListingOptions) {
    return this.dao.listCellTypeAndSubtype1Values(signatureListingOptions);
  }

  @Override
  public Stream<CellTypeAndSubtype> listCellTypeAndSubtype2Values(SignatureListingOptions signatureListingOptions) {
    return this.dao.listCellTypeAndSubtype2Values(signatureListingOptions);
  }

  @Override
  public Stream<String> listCellType1Values(SignatureListingOptions signatureListingOptions) {
    return this.dao.listCellType1Values(signatureListingOptions);
  }

  @Override
  public Stream<String> listCellSubType1Values(SignatureListingOptions signatureListingOptions) {
    return this.dao.listCellSubType1Values(signatureListingOptions);
  }

  @Override
  public Stream<String> listCellType2Values(SignatureListingOptions signatureListingOptions) {
    return this.dao.listCellType2Values(signatureListingOptions);
  }

  @Override
  public Stream<String> listCellSubType2Values(SignatureListingOptions signatureListingOptions) {
    return this.dao.listCellSubType2Values(signatureListingOptions);
  }

  @Override
  public Stream<ExperimentalDesign> listExperimentalDesignValues(SignatureListingOptions signatureListingOptions) {
    return this.dao.listExperimentalDesignValues(signatureListingOptions);
  }

  @Override
  public Stream<String> listOrganismValues(SignatureListingOptions signatureListingOptions) {
    return this.dao.listOrganismValues(signatureListingOptions);
  }

  @Override
  public Stream<String> listDiseaseValues(SignatureListingOptions signatureListingOptions) {
    return this.dao.listDiseaseValues(signatureListingOptions);
  }

  @Override
  public Stream<String> listSourceDbValues(SignatureListingOptions signatureListingOptions) {
    return this.dao.listSourceDbValues(signatureListingOptions);
  }

  @Override
  public Stream<SignatureType> listSignatureTypeValues(SignatureListingOptions signatureListingOptions) {
    return this.dao.listSignatureTypeValues(signatureListingOptions);
  }

  @Override
  public Stream<Integer> listSignaturePubMedIdValues(SignatureListingOptions signatureListingOptions) {
    return this.dao.listSignaturePubMedIdValues(signatureListingOptions);
  }

}
