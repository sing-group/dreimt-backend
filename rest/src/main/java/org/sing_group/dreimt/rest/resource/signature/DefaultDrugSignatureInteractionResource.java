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
package org.sing_group.dreimt.rest.resource.signature;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.sing_group.dreimt.domain.dao.ListingOptions.SortField;
import org.sing_group.dreimt.domain.dao.SortDirection;
import org.sing_group.dreimt.domain.dao.signature.DrugSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.dao.signature.SignatureListingOptions;
import org.sing_group.dreimt.domain.entities.signature.CellTypeAndSubtype;
import org.sing_group.dreimt.domain.entities.signature.DrugInteractionEffect;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionField;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;
import org.sing_group.dreimt.domain.entities.signature.DrugStatus;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;
import org.sing_group.dreimt.rest.entity.signature.DrugSignatureInteractionData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.query.ListingOptionsMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugSignatureInteractionMapper;
import org.sing_group.dreimt.rest.resource.spi.signature.DrugSignatureInteractionResource;
import org.sing_group.dreimt.service.spi.signature.DrugSignatureInteractionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("database/associations")
@Produces({APPLICATION_JSON, "text/csv"})
@Stateless
@CrossDomain(allowedHeaders = "X-Count")
@Api("database-associations")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultDrugSignatureInteractionResource implements DrugSignatureInteractionResource {

  @Inject
  private DrugSignatureInteractionService service;

  @Inject
  private DrugSignatureInteractionMapper drugSignatureMapper;

  @Inject
  private ListingOptionsMapper listingOptionsMapper;

  @GET
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Lists the drug-signature associations.",
    response = DrugSignatureInteractionData.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response list(
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") DrugSignatureInteractionField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection,
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease,
    @QueryParam("includeSummary") @DefaultValue("false") boolean includeSummary
  ) {
      final SignatureListingOptions signatureListingOptions =
        new SignatureListingOptions(
          signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
          experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
          cellType1Disease
        );

      final DrugSignatureInteractionListingOptions listingOptions =
        new DrugSignatureInteractionListingOptions(
          listingOptionsMapper.toListingOptions(getListingOptions(page, pageSize, orderField, sortDirection)),
          signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName,
          drugMoa, drugStatus, minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
        );

      final DrugSignatureInteractionData[] data =
        this.drugSignatureMapper.toDrugSignatureInteractionData(service.list(listingOptions), includeSummary);

      final long count = service.count(listingOptions);

      return Response.ok(data, APPLICATION_JSON)
        .header("X-Count", count)
        .build();
  }
  
  @GET
  @Produces("text/csv")
  @ApiOperation(
    value = "Lists the drug-signature associations in CSV format",
    response = String.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listAsCsv(
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") DrugSignatureInteractionField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection,
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
      final SignatureListingOptions signatureListingOptions =
        new SignatureListingOptions(
          signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
          experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
          cellType1Disease
        );

      final DrugSignatureInteractionListingOptions listingOptions =
        new DrugSignatureInteractionListingOptions(
          listingOptionsMapper.toListingOptions(getListingOptions(page, pageSize, orderField, sortDirection)),
          signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName,
          drugMoa, drugStatus, minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
        );

      final String data = this.drugSignatureMapper.toDrugSignatureInteractionCsvData(service.list(listingOptions));

      return Response.ok(data, "text/csv").build();
  }

  private ListingOptionsData getListingOptions(
    Integer page, Integer pageSize, DrugSignatureInteractionField orderField, SortDirection sortDirection
  ) {
    final ListingOptionsData listingOptions;

    if (orderField == null || sortDirection == null || sortDirection == SortDirection.NONE) {
      listingOptions = new ListingOptionsData(page, pageSize);
    } else {
      SortField sortField = new SortField(orderField.name(), sortDirection);
      listingOptions = new ListingOptionsData(page, pageSize, sortField);
    }
    return listingOptions;
  }

  @Path("params/signature-name/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible signature name values in drug-signature associations.",
    response = String.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listSignatureNameValues(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final String[] data =
      service.listSignatureNameValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @Path("params/cell-type-and-subtype-1/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible cell type and subtype 1 values in drug-signature associations.",
    response = String.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listCellTypeAndSubtype1Values(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, null, null, null,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final CellTypeAndSubtype[] data =
      service.listCellTypeAndSubtype1Values(listingOptions)
        .toArray(CellTypeAndSubtype[]::new);

    return Response.ok(data).build();
  }

  @Path("params/cell-type-and-subtype-2/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible cell type and subtype 2 values in drug-signature associations.",
    response = String.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listCellTypeAndSubtype2Values(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final CellTypeAndSubtype[] data =
      service.listCellTypeAndSubtype2Values(listingOptions)
        .toArray(CellTypeAndSubtype[]::new);

    return Response.ok(data).build();
  }

  @Path("params/experimental-design/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible experimental design values in drug-signature associations.",
    response = ExperimentalDesign.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listExperimentalDesignValues(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final ExperimentalDesign[] data =
      service.listExperimentalDesignValues(listingOptions)
        .toArray(ExperimentalDesign[]::new);

    return Response.ok(data).build();
  }

  @Path("params/organism/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible organism values in drug-signature associations.",
    response = String.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listOrganismValues(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final String[] data =
      service.listOrganismValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @Path("params/disease/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible disease values in drug-signature associations.",
    response = String.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listDiseaseValues(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final String[] data =
      service.listDiseaseValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @Path("params/signature-source-db/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible signature source DB values in drug-signature associations.",
    response = String.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listSignatureSourceDbValues(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final String[] data =
      service.listSignatureSourceDbValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @Path("params/interaction-type/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible interaction type values in drug-signature associations.",
    response = String.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listInteractionTypeValues(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final DrugSignatureInteractionType[] data =
      service.listInteractionTypeValues(listingOptions)
        .toArray(DrugSignatureInteractionType[]::new);

    return Response.ok(data).build();
  }

  @Path("params/signature-pubmed-id/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible signature article PubMed ID values in drug-signature associations.",
    response = Integer.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listSignaturePubMedIdValues(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final Integer[] data =
      service.listSignaturePubMedIdValues(listingOptions)
        .toArray(Integer[]::new);

    return Response.ok(data).build();
  }

  @Path("params/drug-common-name/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible drug common name values in drug-signature associations.",
    response = String.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listDrugCommonNameValues(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final String[] data =
      service.listDrugCommonNameValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }
  
  @Path("params/drug-moa/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible drug MOA values in drug-signature associations.",
    response = String.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listDrugMoaValues(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final String[] data =
      service.listDrugMoaValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }
  
  @Path("params/drug-status/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible drug status values in drug-signature associations.",
    response = DrugStatus.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listDrugStatusValues(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final DrugStatus[] data =
      service.listDrugStatusValues(listingOptions)
        .toArray(DrugStatus[]::new);

    return Response.ok(data).build();
  }
  
  @Path("params/cell-type-1-treatment/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible treatment values of the selected cell type 1 in drug-signature associations.",
    response = String.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listCellType1TreatmentValues(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final String[] data =
      service.listCellType1TreatmentValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }
  
  @Path("params/cell-type-1-disease/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible disease values of the selected cell type 1 in drug-signature associations.",
    response = String.class,
    responseContainer = "list",
    code = 200
    )
  @Override
  public Response listCellType1DiseaseValues(
    @QueryParam("signatureName") String signatureName,
    @QueryParam("cellType1") String cellType1,
    @QueryParam("cellSubType1") String cellSubType1,
    @QueryParam("cellTypeOrSubType1") String cellTypeOrSubType1,
    @QueryParam("cellType2") String cellType2,
    @QueryParam("cellSubType2") String cellSubType2,
    @QueryParam("cellTypeOrSubType2") String cellTypeOrSubType2,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("interactionType") DrugSignatureInteractionType interactionType,
    @QueryParam("minTau") Double minTau,
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("cellType1Effect") DrugInteractionEffect cellType1Effect,
    @QueryParam("cellType1Treatment") String cellType1Treatment,
    @QueryParam("cellType1Disease") String cellType1Disease
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signaturePubMedId, cellType1Treatment,
        cellType1Disease
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions, interactionType, drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxUpFdr, maxDownFdr, cellType1Effect
      );

    final String[] data =
      service.listCellType1DiseaseValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }
}
