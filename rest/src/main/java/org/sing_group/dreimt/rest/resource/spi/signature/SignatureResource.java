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

  Response getGenes(String signatureName, boolean onlyUniverseGenes);

  Response listCellTypeAValues(
    String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType
  );

  Response listCellTypeBValues(
    String cellTypeA, ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType
  );

  Response listExperimentalDesignValues(
    String cellTypeA, String cellTypeB, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType
  );

  Response listOrganismValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String disease, String signatureSourceDb,
    SignatureType signatureType
  );

  Response listDiseaseValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism,
    String signatureSourceDb, SignatureType signatureType
  );

  Response listSignatureSourceDbValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease,
    SignatureType signatureType
  );

  Response listSignatureTypeValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease,
    String signatureSourceDb
  );
}
