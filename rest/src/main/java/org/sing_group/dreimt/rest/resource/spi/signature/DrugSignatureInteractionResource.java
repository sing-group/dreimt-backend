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
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapQueryParameters;
import org.sing_group.dreimt.rest.entity.query.jaccard.JaccardQueryParameters;

@Local
public interface DrugSignatureInteractionResource {

  Response list(
    Integer page, Integer pageSize, DrugSignatureInteractionField orderField, SortDirection sortDirection,
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr,
    String freeText
  );
  
  Response listAsCsv(
    Integer page, Integer pageSize, DrugSignatureInteractionField orderField, SortDirection sortDirection,
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr,
    String freeText
  );

  Response jaccardQuery(JaccardQueryParameters jaccardQueryParameters);

  Response cmapQuery(CmapQueryParameters cmapQueryParameters);

  Response listSignatureNameValues(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listCellTypeAndSubtype1Values(
    String signatureName, String cellType1, String cellSubType1,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listCellTypeAndSubtype1Values(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listCellType1Values(
    String signatureName, String cellType1, String cellSubType1, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listCellSubType1Values(
    String signatureName, String cellType1, String cellSubType1, String cellType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listCellType2Values(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listCellSubType2Values(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listExperimentalDesignValues(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listOrganismValues(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listDiseaseValues(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listSignatureSourceDbValues(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listInteractionTypeValues(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listSignaturePubMedIdValues(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listDrugSourceNameValues(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listDrugSourceDbValues(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listDrugCommonNameValues(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );

  Response listDrugMoaValues(
    String signatureName, String cellType1, String cellSubType1, String cellType2, String cellSubType2,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer signaturePubMedId, String drugSourceName, String drugSourceDb, String drugCommonName, String drugMoa,
    Double minDrugDss, DrugSignatureInteractionType interactionType, Double minTau, Double maxUpFdr, Double maxDownFdr
  );
}
