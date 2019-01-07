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
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response jaccardQuery(
    GenesQueryInfo post, boolean onlyUniverseGenes, String cellTypeA, String cellSubTypeA, String cellTypeB,
    String cellSubTypeB, ExperimentalDesign experimentalDesign, String organism, String disease,
    String signatureSourceDb
  );

  Response cmapQuery(GenesQueryInfo post, Integer numPerm, Double maxPvalue);

  Response listSignatureNameValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response listCellTypeAValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );
  
  Response listCellSubTypeAValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response listCellTypeBValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );
  
  Response listCellSubTypeBValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response listExperimentalDesignValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response listOrganismValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response listDiseaseValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response listSignatureSourceDbValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response listSignatureTypeValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response listSignaturePubMedIdValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response listDrugSourceNameValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response listDrugSourceDbValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );

  Response listDrugCommonNameValues(
    String signatureName, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    SignatureType signatureType, Integer signaturePubMedId, String drugSourceName, String drugSourceDb,
    String drugCommonName, Double maxPvalue, Double minTes, Double maxTes, Double maxFdr
  );
}
