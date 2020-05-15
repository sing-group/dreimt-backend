/*-
 * #%L
 * DREIMT - REST
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
package org.sing_group.dreimt.rest.resource.spi.signature;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;

@Local
public interface SignatureResource {
  Response get(String signatureName);

  Response list(
    String signatureName, String cellType1, String cellSubType1, String cellTypeOrSubType1, String cellType2,
    String cellSubType2, String cellTypeOrSubType2, ExperimentalDesign experimentalDesign, String organism,
    String disease, String signatureSourceDb, SignatureType signatureType, Integer signaturePubMedId
  );

  Response getGenes(String signatureName, boolean onlyUniverseGenes);

  Response listSignatureNameValues(
    String signatureName, String cellType1, String cellSubType1, String cellTypeOrSubType1, String cellType2,
    String cellSubType2, String cellTypeOrSubType2, ExperimentalDesign experimentalDesign, String organism,
    String disease, String signatureSourceDb, SignatureType signatureType, Integer signaturePubMedId
  );

  Response listCellTypeAndSubtype1Values(
    String signatureName, String cellType1, String cellSubType1, String cellTypeOrSubType1,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId
  );

  Response listCellTypeAndSubtype2Values(
    String signatureName, String cellType1, String cellSubType1, String cellTypeOrSubType1, String cellType2,
    String cellSubType2, String cellTypeOrSubType2, ExperimentalDesign experimentalDesign, String organism,
    String disease, String signatureSourceDb, SignatureType signatureType, Integer signaturePubMedId
  );

  Response listExperimentalDesignValues(
    String signatureName, String cellType1, String cellSubType1, String cellTypeOrSubType1, String cellType2,
    String cellSubType2, String cellTypeOrSubType2, ExperimentalDesign experimentalDesign, String organism,
    String disease, String signatureSourceDb, SignatureType signatureType, Integer signaturePubMedId
  );

  Response listOrganismValues(
    String signatureName, String cellType1, String cellSubType1, String cellTypeOrSubType1, String cellType2,
    String cellSubType2, String cellTypeOrSubType2, ExperimentalDesign experimentalDesign, String organism,
    String disease, String signatureSourceDb, SignatureType signatureType, Integer signaturePubMedId
  );

  Response listDiseaseValues(
    String signatureName, String cellType1, String cellSubType1, String cellTypeOrSubType1, String cellType2,
    String cellSubType2, String cellTypeOrSubType2, ExperimentalDesign experimentalDesign, String organism,
    String disease, String signatureSourceDb, SignatureType signatureType, Integer signaturePubMedId
  );

  Response listSignatureSourceDbValues(
    String signatureName, String cellType1, String cellSubType1, String cellTypeOrSubType1, String cellType2,
    String cellSubType2, String cellTypeOrSubType2, ExperimentalDesign experimentalDesign, String organism,
    String disease, String signatureSourceDb, SignatureType signatureType, Integer signaturePubMedId
  );

  Response listSignatureTypeValues(
    String signatureName, String cellType1, String cellSubType1, String cellTypeOrSubType1, String cellType2,
    String cellSubType2, String cellTypeOrSubType2, ExperimentalDesign experimentalDesign, String organism,
    String disease, String signatureSourceDb, SignatureType signatureType, Integer signaturePubMedId
  );

  Response listSignaturePubMedIdValues(
    String signatureName, String cellType1, String cellSubType1, String cellTypeOrSubType1, String cellType2,
    String cellSubType2, String cellTypeOrSubType2, ExperimentalDesign experimentalDesign, String organism,
    String disease, String signatureSourceDb, SignatureType signatureType, Integer signaturePubMedId
  );
}
