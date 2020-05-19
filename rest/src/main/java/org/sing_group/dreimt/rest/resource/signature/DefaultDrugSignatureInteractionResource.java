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

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.sing_group.dreimt.service.util.Sets.intersection;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.dreimt.domain.dao.ListingOptions.SortField;
import org.sing_group.dreimt.domain.dao.SortDirection;
import org.sing_group.dreimt.domain.dao.signature.DrugSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.dao.signature.SignatureListingOptions;
import org.sing_group.dreimt.domain.entities.execution.WorkEntity;
import org.sing_group.dreimt.domain.entities.signature.CellTypeAndSubtype;
import org.sing_group.dreimt.domain.entities.signature.DrugInteractionEffect;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionField;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;
import org.sing_group.dreimt.domain.entities.signature.DrugStatus;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.rest.entity.execution.WorkData;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapQueryParameters;
import org.sing_group.dreimt.rest.entity.query.jaccard.JaccardQueryParameters;
import org.sing_group.dreimt.rest.entity.signature.DrugSignatureInteractionData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.execution.ExecutionMapper;
import org.sing_group.dreimt.rest.mapper.spi.query.ListingOptionsMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugSignatureInteractionMapper;
import org.sing_group.dreimt.rest.resource.route.BaseRestPathBuilder;
import org.sing_group.dreimt.rest.resource.spi.signature.DrugSignatureInteractionResource;
import org.sing_group.dreimt.service.query.cmap.DefaultCmapQueryOptions;
import org.sing_group.dreimt.service.query.jaccard.DefaultJaccardQueryOptions;
import org.sing_group.dreimt.service.spi.query.cmap.CmapQueryOptions;
import org.sing_group.dreimt.service.spi.query.cmap.CmapQueryService;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryOptions;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryService;
import org.sing_group.dreimt.service.spi.signature.DrugSignatureInteractionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("interactions")
@Produces({APPLICATION_JSON, "text/csv"})
@Stateless
@CrossDomain(allowedHeaders = "X-Count")
@Api("interactions")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultDrugSignatureInteractionResource implements DrugSignatureInteractionResource {

  @Inject
  private JaccardQueryService jaccardQueryService;

  @Inject
  private CmapQueryService cmapQueryService;

  @Inject
  private DrugSignatureInteractionService service;

  @Inject
  private DrugSignatureInteractionMapper drugSignatureMapper;

  @Inject
  private ListingOptionsMapper listingOptionsMapper;

  @Inject
  private ExecutionMapper executionMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void postConstruct() {
    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();

    this.drugSignatureMapper.setUriBuilder(uriBuilder);
    this.executionMapper.setUriBuilder(uriBuilder);
  }

  @GET
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Lists the drug-signature interactions",
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
    @QueryParam("freeText") String freeText,
    @QueryParam("includeSummary") @DefaultValue("false") boolean includeSummary
  ) {
    if (
      freeText != null && (signatureName != null || cellType1 != null || cellSubType1 != null || cellType2 != null
        || cellSubType2 != null || cellTypeOrSubType1 != null || cellTypeOrSubType2 != null 
        || experimentalDesign != null || organism != null || disease != null
        || cellType1Effect != null || cellType1Treatment != null || cellType1Disease !=  null
        || signatureSourceDb != null || signaturePubMedId != null || drugSourceName != null || drugSourceDb != null
        || drugCommonName != null || drugMoa != null || drugStatus != null || minDrugDss != null
        || interactionType != null || minTau != null || maxUpFdr != null
        || maxDownFdr != null)
    ) {
      throw new BadRequestException("freeText parameter is not compatible with other filters");
    }

    if(freeText == null) {
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
    } else {
      final DrugSignatureInteractionData[] data =
        this.drugSignatureMapper.toDrugSignatureInteractionData(
          service.list(listingOptionsMapper.toListingOptions(getListingOptions(page, pageSize, orderField, sortDirection)), freeText),
          includeSummary
        );

      final long count = service.count(freeText);

      return Response.ok(data, APPLICATION_JSON)
        .header("X-Count", count)
        .build();
    }
  }
  
  @GET
  @Produces("text/csv")
  @ApiOperation(
    value = "Lists the drug-signature interactions in CSV format",
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
    @QueryParam("cellType1Disease") String cellType1Disease,
    @QueryParam("freeText") String freeText
  ) {
    if (
      freeText != null && (signatureName != null || cellType1 != null || cellSubType1 != null || cellType2 != null
      || cellSubType2 != null || cellTypeOrSubType1 != null || cellTypeOrSubType2 != null 
      || experimentalDesign != null || organism != null || disease != null
      || cellType1Effect != null || cellType1Treatment != null || cellType1Disease !=  null
      || signatureSourceDb != null || signaturePubMedId != null || drugSourceName != null || drugSourceDb != null
      || drugCommonName != null || drugMoa != null || drugStatus != null || minDrugDss != null
      || interactionType != null || minTau != null || maxUpFdr != null
      || maxDownFdr != null)
    ) {
      throw new BadRequestException("freeText parameter is not compatible with other filters");
    }
    
    if (freeText == null) {
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
    } else {
      final String data =
        this.drugSignatureMapper.toDrugSignatureInteractionCsvData(
          service.list(listingOptionsMapper.toListingOptions(getListingOptions(page, pageSize, orderField, sortDirection)), freeText)
        );

      return Response.ok(data, "text/csv").build();
    }
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
    value = "Lists the possible signature name values in drug-signature interactions",
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
    value = "Lists the possible cell type and subtype 1 values in drug-signature interactions",
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
    value = "Lists the possible cell type and subtype 2 values in drug-signature interactions",
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
    value = "Lists the possible experimental design values in drug-signature interactions",
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
    value = "Lists the possible organism values in drug-signature interactions",
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
    value = "Lists the possible disease values in drug-signature interactions",
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
    value = "Lists the possible signature source DB values in drug-signature interactions",
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
    value = "Lists the possible interaction type values in drug-signature interactions",
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
    value = "Lists the possible signature article PubMed ID values in drug-signature interactions",
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

  @Path("params/drug-source-name/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible drug source name values in drug-signature interactions",
    response = String.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response listDrugSourceNameValues(
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
      service.listDrugSourceNameValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @Path("params/drug-source-db/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible drug source DB values in drug-signature interactions",
    response = String.class,
    responseContainer = "list",
    code = 200
    )
  @Override
  public Response listDrugSourceDbValues(
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
      service.listDrugSourceDbValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @Path("params/drug-common-name/values")
  @Produces(APPLICATION_JSON)
  @GET
  @ApiOperation(
    value = "Lists the possible drug common name values in drug-signature interactions",
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
    value = "Lists the possible drug MOA values in drug-signature interactions",
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
    value = "Lists the possible drug status values in drug-signature interactions",
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
    value = "Lists the possible treatment values of the selected cell type 1 in drug-signature interactions",
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
    value = "Lists the possible disease values of the selected cell type 1 in drug-signature interactions",
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

  @POST
  @Produces(APPLICATION_JSON)
  @Path("query/jaccard")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "Calculates the Jaccard indexes between the introduced gene lists and all signatures in the database. "
      + "The calculus are done asynchronously, thus this method returns a work-data instance with information about "
      + "the asynchronous task doing the calculations.",
    response = WorkData.class,
    code = 200
  )
  @Override
  public Response jaccardQuery(
    JaccardQueryParameters jaccardQueryParameters
  ) {
    Set<String> upGenes =
      parseAndValidateJaccardQueryUpGenes(
        jaccardQueryParameters.getUpGenes(), jaccardQueryParameters.isOnlyUniverseGenes()
      );
    Set<String> downGenes =
      parseAndValidateJaccardQueryDownGenes(
        jaccardQueryParameters.getDownGenes(), jaccardQueryParameters.isOnlyUniverseGenes()
      );

    validateQueryGenes(upGenes, downGenes);

    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);

    final Function<String, String> resultUriBuilder =
      id -> pathBuilder.jaccardResult(id).build().toString();

    SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        null, jaccardQueryParameters.getCellType1(), jaccardQueryParameters.getCellSubType1(),
        jaccardQueryParameters.getCellTypeOrSubType1(), jaccardQueryParameters.getCellType2(),
        jaccardQueryParameters.getCellSubType2(), jaccardQueryParameters.getCellTypeOrSubType2(),
        jaccardQueryParameters.getExperimentalDesign(), jaccardQueryParameters.getOrganism(),
        jaccardQueryParameters.getDisease(), jaccardQueryParameters.getSignatureSourceDb(),
        null, null, null, null
      );

    JaccardQueryOptions options =
      new DefaultJaccardQueryOptions(
        jaccardQueryParameters.getQueryTitle(), upGenes, downGenes, jaccardQueryParameters.isOnlyUniverseGenes(),
        resultUriBuilder, signatureListingOptions
      );

    final WorkEntity work = this.jaccardQueryService.jaccardQuery(options);

    return Response.ok(this.executionMapper.toWorkData(work)).build();
  }

  private Set<String> parseAndValidateJaccardQueryUpGenes(String[] upGenes, boolean onlyUniverseGenes) {
    return parseAndValidateUpGenes(
      upGenes,
      onlyUniverseGenes,
      this.jaccardQueryService::isValidGeneSet,
      this.jaccardQueryService::getMaximumGeneSetSize,
      this.jaccardQueryService::getMinimumGeneSetSize
    );
  }

  private Set<String> parseAndValidateJaccardQueryDownGenes(String[] downGenes, boolean onlyUniverseGenes) {
    return parseAndValidateDownGenes(
      downGenes,
      onlyUniverseGenes,
      this.jaccardQueryService::isValidGeneSet,
      this.jaccardQueryService::getMaximumGeneSetSize,
      this.jaccardQueryService::getMinimumGeneSetSize
    );
  }

  private static Set<String> parseAndValidateUpGenes(
    String[] upGenes,
    boolean onlyUniverseGenes,
    BiFunction<Set<String>, Boolean, Boolean> isValidGeneSet,
    Supplier<Integer> maximumGeneSetSizeSupplier,
    Supplier<Integer> minimumGeneSetSizeSupplier
  ) {
    Set<String> upGenesSet =
      upGenes == null ? emptySet() : new HashSet<String>(asList(upGenes));

    if (!upGenesSet.isEmpty() && !isValidGeneSet.apply(upGenesSet, onlyUniverseGenes)) {
      throw new IllegalArgumentException(
        "Invalid up (or geneset) genes list size. It must have at least " + minimumGeneSetSizeSupplier.get()
          + " and at most " + maximumGeneSetSizeSupplier.get() + " genes." + onlyUniverseGenesWarning(onlyUniverseGenes)
      );
    }

    return upGenesSet;
  }

  private static Set<String> parseAndValidateDownGenes(
    String[] downGenes,
    boolean onlyUniverseGenes,
    BiFunction<Set<String>, Boolean, Boolean> isValidGeneSet,
    Supplier<Integer> maximumGeneSetSizeSupplier,
    Supplier<Integer> minimumGeneSetSizeSupplier
  ) {
    Set<String> downGenesSet =
      downGenes == null ? emptySet() : new HashSet<String>(asList(downGenes));

    if (!downGenesSet.isEmpty() && !isValidGeneSet.apply(downGenesSet, onlyUniverseGenes)) {
      throw new IllegalArgumentException(
        "Invalid down genes list size. It must have at least " + minimumGeneSetSizeSupplier.get()
          + " and at most " + maximumGeneSetSizeSupplier.get() + " genes." + onlyUniverseGenesWarning(onlyUniverseGenes)
      );
    }

    return downGenesSet;
  }

  private static String onlyUniverseGenesWarning(boolean onlyUniverseGenes) {
    return onlyUniverseGenes ? " Note that genes must be in the DREIMT genes." : "";
  }

  @POST
  @Produces(APPLICATION_JSON)
  @Path("query/cmap")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "Makes the Cmap drug predictions for the gene lists introduced. "
      + "The calculus are done asynchronously, thus this method returns a work-data instance with information about "
      + "the asynchronous task doing the calculations.",
    response = WorkData.class,
    code = 200
  )
  @Override
  public Response cmapQuery(CmapQueryParameters cmapQueryParameters) {
    this.cmapQueryService.validateNumPerm(cmapQueryParameters.getNumPerm());
    Set<String> upGenes = parseAndValidateCmapQueryUpGenes(cmapQueryParameters.getUpGenes());
    Set<String> downGenes = parseAndValidateCmapQueryDownGenes(cmapQueryParameters.getDownGenes());

    validateQueryGenes(upGenes, downGenes);

    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);

    final Function<String, String> resultUriBuilder =
      (downGenes.isEmpty() || upGenes.isEmpty()) ? id -> pathBuilder.cmapGeneSetSignatureResult(id).build().toString() : id -> pathBuilder.cmapUpDownSignatureResult(id).build().toString();

    CmapQueryOptions options =
      new DefaultCmapQueryOptions(
        cmapQueryParameters.getQueryTitle(), upGenes, downGenes, resultUriBuilder, cmapQueryParameters.getNumPerm(),
        cmapQueryParameters.getCaseType(), cmapQueryParameters.getReferenceType()
      );

    final WorkEntity work = this.cmapQueryService.cmapQuery(options);

    return Response.ok(this.executionMapper.toWorkData(work)).build();
  }

  private static void validateQueryGenes(Set<String> upGenes, Set<String> downGenes) {
    if (upGenes.isEmpty() && downGenes.isEmpty()) {
      throw new IllegalArgumentException("Both genes lists can't be empty. At least one gene list must be provided.");
    }

    if (intersection(upGenes, downGenes).size() > 0) {
      throw new IllegalArgumentException("Up and down gene lists cannot have genes in common");
    }
  }

  private Set<String> parseAndValidateCmapQueryUpGenes(String[] upGenes) {
    return parseAndValidateUpGenes(
      upGenes,
      true,
      (s, o) -> this.cmapQueryService.isValidGeneSet(s),
      this.cmapQueryService::getMaximumGeneSetSize,
      this.cmapQueryService::getMinimumGeneSetSize
    );
  }

  private Set<String> parseAndValidateCmapQueryDownGenes(String[] downGenes) {
    return parseAndValidateDownGenes(
      downGenes,
      true,
      (s, o) -> this.cmapQueryService.isValidGeneSet(s),
      this.cmapQueryService::getMaximumGeneSetSize,
      this.cmapQueryService::getMinimumGeneSetSize
    );
  }
}
