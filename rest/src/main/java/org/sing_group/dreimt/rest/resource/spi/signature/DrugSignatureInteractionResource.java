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

import org.sing_group.dreimt.domain.dao.SortDirection;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionField;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;
import org.sing_group.dreimt.rest.entity.query.GenesQueryInfo;

@Local
public interface DrugSignatureInteractionResource {

  Response list(
    Integer page, Integer pageSize, DrugSignatureInteractionField orderField, SortDirection sortDirection,
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign,
    String organism, String disease, String signatureSourceDb, SignatureType signatureType, String drugSourceName,
    String drugSourceDb, String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response jaccardQuery(
    GenesQueryInfo post, String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism,
    String disease, String signatureSourceDb, SignatureType signatureType, boolean onlyUniverseGenes
  );

  Response cmapQuery(GenesQueryInfo post, Integer numPerm, Double maxPvalue);

  Response listCellTypeAValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, String drugSourceName, String drugSourceDb, String drugCommonName, Double maxPvalue,
    Double minTes, Double maxTes, Double maxFdr
  );

  Response listCellTypeBValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, String drugSourceName, String drugSourceDb, String drugCommonName, Double maxPvalue,
    Double minTes, Double maxTes, Double maxFdr
  );

  Response listExperimentalDesignValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, String drugSourceName, String drugSourceDb, String drugCommonName, Double maxPvalue,
    Double minTes, Double maxTes, Double maxFdr
  );

  Response listOrganismValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, String drugSourceName, String drugSourceDb, String drugCommonName, Double maxPvalue,
    Double minTes, Double maxTes, Double maxFdr
  );

  Response listDiseaseValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, String drugSourceName, String drugSourceDb, String drugCommonName, Double maxPvalue,
    Double minTes, Double maxTes, Double maxFdr
  );

  Response listSignatureSourceDbValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, String drugSourceName, String drugSourceDb, String drugCommonName, Double maxPvalue,
    Double minTes, Double maxTes, Double maxFdr
  );

  Response listSignatureTypeValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, String drugSourceName, String drugSourceDb, String drugCommonName, Double maxPvalue,
    Double minTes, Double maxTes, Double maxFdr
  );

  Response listDrugSourceNameValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, String drugSourceName, String drugSourceDb, String drugCommonName, Double maxPvalue,
    Double minTes, Double maxTes, Double maxFdr
  );

  Response listDrugSourceDbValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, String drugSourceName, String drugSourceDb, String drugCommonName, Double maxPvalue,
    Double minTes, Double maxTes, Double maxFdr
  );

  Response listDrugCommonNameValues(
    String cellTypeA, String cellTypeB, ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, String drugSourceName, String drugSourceDb, String drugCommonName, Double maxPvalue,
    Double minTes, Double maxTes, Double maxFdr
  );
}
