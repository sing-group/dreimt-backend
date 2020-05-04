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
package org.sing_group.dreimt.rest.resource.results;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.dao.SortDirection;
import org.sing_group.dreimt.domain.dao.execution.cmap.CmapDrugGeneSetSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugGeneSetSignatureInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapGeneSetSignatureDrugInteractionField;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapResult;
import org.sing_group.dreimt.domain.entities.signature.DrugStatus;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapGeneSetSignatureDrugInteractionData;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapQueryGeneSetSignatureMetadataData;
import org.sing_group.dreimt.rest.entity.signature.GeneSetSignatureGeneData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.query.ListingOptionsMapper;
import org.sing_group.dreimt.rest.mapper.spi.query.cmap.CmapQueryGeneSetSignatureResultsMapper;
import org.sing_group.dreimt.rest.resource.spi.results.CmapGeneSetSignatureQueryResultsResource;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapDrugGeneSetSignatureInteractionService;
import org.sing_group.dreimt.service.spi.query.cmap.CmapQueryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("results/cmap/geneset/")
@Produces({APPLICATION_JSON, "text/csv"})
@Stateless
@CrossDomain(allowedHeaders = "X-Count")
@Api("results/cmap/geneset/")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultCmapGeneSetSignatureQueryResultsResource implements CmapGeneSetSignatureQueryResultsResource {

  @Inject
  private CmapDrugGeneSetSignatureInteractionService cmapDrugGeneSetInteractionService;
  
  @Inject
  private CmapQueryService cmapQueryService;
  
  @Inject
  private CmapQueryGeneSetSignatureResultsMapper mapper;
  
  @Inject
  private ListingOptionsMapper listingOptionsMapper;
  
  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Returns the metadata associated with the specified Cmap result.",
    response = CmapQueryGeneSetSignatureMetadataData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown cmap result: {id}")
  )
  @Override
  public Response cmapQueryMetadata(@PathParam("id") String resultId) {
    return this.cmapQueryResult(
      resultId,
      mapper::toCmapQueryMetadataData
    );
  }
  
  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/genes")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Returns the query genes associated with the specified Cmap result.",
    response = GeneSetSignatureGeneData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown cmap result: {id}")
  )
  @Override
  public Response cmapQueryGenes(
    @PathParam("id") String resultId,
    @DefaultValue("false") @QueryParam("onlyUniverseGenes") boolean onlyUniverseGenes
  ) {
    return this.cmapQueryResult(
      resultId,
      (cmapResult) -> mapper.toGeneData(cmapResult, onlyUniverseGenes)
    );
  }

  private Response cmapQueryResult(String resultId, Function<CmapGeneSetSignatureResult, Object> resultMapper) {
    CmapGeneSetSignatureResult cmapResult = findCmapGeneSetSignatureResult(resultId);

    return Response
      .ok(resultMapper.apply(cmapResult), APPLICATION_JSON)
      .build();
  }

  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/interactions")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Returns the drug interaction results associated with the specified Cmap result in JSON format.",
    response = CmapGeneSetSignatureDrugInteractionData.class,
    responseContainer = "list",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown cmap result: {id}")
  )
  @Override
  public Response queryInteractions(
    @PathParam("id") String resultId,
    @QueryParam("minTau") Double minTau, 
    @QueryParam("maxFdr") Double maxFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") CmapGeneSetSignatureDrugInteractionField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection
  ) {
    return this.cmapSignatureQueryResult(
      resultId, 
      minTau, maxFdr, drugSourceName, drugSourceDb, drugCommonName, drugMoa, 
      drugStatus, minDrugDss, page, pageSize, orderField, sortDirection,
      (cmapResult, interactions) -> mapper.toCmapDrugInteractionData(interactions),
      APPLICATION_JSON, true
    );
  }

  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/interactions")
  @Produces("text/csv")
  @ApiOperation(
    value = "Returns the drug interaction results associated with the specified Cmap result in CSV format.",
    response = String.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown cmap result: {id}")
  )
  @Override
  public Response queryInteractionsAsCsv(
    @PathParam("id") String resultId,
    @QueryParam("minTau") Double minTau, 
    @QueryParam("maxFdr") Double maxFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") CmapGeneSetSignatureDrugInteractionField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection
  ) {
    return this.cmapSignatureQueryResult(
      resultId, 
      minTau, maxFdr, drugSourceName, drugSourceDb, drugCommonName, drugMoa, 
      drugStatus, minDrugDss, page, pageSize, orderField, sortDirection,
      (cmapResult, interactions) -> mapper.toCmapDrugInteractionCsvData(interactions), 
      "text/csv", false
    );
  }

  private Response cmapSignatureQueryResult(
    String resultId,
    Double minTau, 
    Double maxFdr,
    String drugSourceName, 
    String drugSourceDb, 
    String drugCommonName,
    String drugMoa,
    DrugStatus drugStatus,
    Double minDrugDss,
    Integer page, 
    Integer pageSize,
    CmapGeneSetSignatureDrugInteractionField orderField, 
    SortDirection sortDirection,
    BiFunction<CmapResult, List<CmapDrugGeneSetSignatureInteraction>, Object> cmapDrugInteractionMapper,
    String responseContentType,
    boolean includeCountHeader
  ) {
    final ListingOptionsData listingOptions = ListingOptionsData.from(page, pageSize, orderField.name(), sortDirection);

    final CmapDrugGeneSetSignatureInteractionListingOptions cmapListingOptions =
      new CmapDrugGeneSetSignatureInteractionListingOptions(
        listingOptionsMapper.toListingOptions(listingOptions),
        drugSourceName, drugSourceDb, drugCommonName, drugMoa,
        drugStatus, minDrugDss, minTau, maxFdr
      );

    CmapGeneSetSignatureResult cmapResult = findCmapGeneSetSignatureResult(resultId);

    List<CmapDrugGeneSetSignatureInteraction> cmapDrugInteractions =
      this.cmapDrugGeneSetInteractionService.list(cmapResult, cmapListingOptions).collect(toList());

    ResponseBuilder responseBuilder =
      Response
        .ok(cmapDrugInteractionMapper.apply(cmapResult, cmapDrugInteractions), responseContentType);

    if (includeCountHeader) {
      final long count = this.cmapDrugGeneSetInteractionService.count(cmapResult, cmapListingOptions);
      responseBuilder = responseBuilder.header("X-Count", count);
    }

    return responseBuilder.build();
  }
  
  protected CmapGeneSetSignatureResult findCmapGeneSetSignatureResult(String resultId) {
    CmapResult cmapResult =
      cmapQueryService.getResult(resultId)
        .orElseThrow(() -> new IllegalArgumentException("Unknown cmap result: " + resultId));
    
    if (!(cmapResult instanceof CmapGeneSetSignatureResult)) {
      throw new IllegalArgumentException(
        "The specified cmap result (" + resultId + ") corresponds to a UpDown Signature query. Use the appropiate method."
      );
    }
    
    return (CmapGeneSetSignatureResult) cmapResult;
  }
  
  @GET
  @Path("params/{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/drug-source-name/values")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Lists the possible drug source name values for the specified Cmap result.",
    response = String.class,
    code = 200
  )
  @Override
  public Response listDrugSourceNameValues(
    @PathParam("id") String resultId,
    @QueryParam("minTau") Double minTau, 
    @QueryParam("maxFdr") Double maxFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss
  ) {
    final CmapDrugGeneSetSignatureInteractionListingOptions cmapListingOptions =
      new CmapDrugGeneSetSignatureInteractionListingOptions(
        ListingOptions.noModification(),
        drugSourceName, drugSourceDb, drugCommonName, drugMoa,
        drugStatus, minDrugDss, minTau, maxFdr
      );

    CmapGeneSetSignatureResult cmapResult = findCmapGeneSetSignatureResult(resultId);
    
    final String[] data =
      cmapDrugGeneSetInteractionService.listDrugSourceNameValues(cmapResult, cmapListingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @GET
  @Path("params/{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/drug-source-db/values")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Lists the possible drug source database values for the specified Cmap result.",
    response = String.class,
    code = 200
  )
  @Override
  public Response listDrugSourceDbValues(
    @PathParam("id") String resultId,
    @QueryParam("minTau") Double minTau, 
    @QueryParam("maxFdr") Double maxFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss
  ) {
    final CmapDrugGeneSetSignatureInteractionListingOptions cmapListingOptions =
      new CmapDrugGeneSetSignatureInteractionListingOptions(
        ListingOptions.noModification(),
        drugSourceName, drugSourceDb, drugCommonName, drugMoa,
        drugStatus, minDrugDss, minTau, maxFdr
      );

    CmapGeneSetSignatureResult cmapResult = findCmapGeneSetSignatureResult(resultId);

    final String[] data =
      cmapDrugGeneSetInteractionService.listDrugSourceDbValues(cmapResult, cmapListingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @GET
  @Path("params/{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/drug-common-name/values")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Lists the possible drug common name values for the specified Cmap result.",
    response = String.class,
    code = 200
  )
  @Override
  public Response listDrugCommonNameValues(
    @PathParam("id") String resultId,
    @QueryParam("minTau") Double minTau, 
    @QueryParam("maxFdr") Double maxFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss
  ) {
    final CmapDrugGeneSetSignatureInteractionListingOptions cmapListingOptions =
      new CmapDrugGeneSetSignatureInteractionListingOptions(
        ListingOptions.noModification(),
        drugSourceName, drugSourceDb, drugCommonName, drugMoa,
        drugStatus, minDrugDss, minTau, maxFdr
      );

    CmapGeneSetSignatureResult cmapResult = findCmapGeneSetSignatureResult(resultId);

    final String[] data =
      cmapDrugGeneSetInteractionService.listDrugCommonNameValues(cmapResult, cmapListingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }
  
  @GET
  @Path("params/{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/drug-moa/values")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Lists the possible drug MOA values for the specified Cmap result.",
    response = String.class,
    code = 200
  )
  @Override
  public Response listDrugMoaValues(
    @PathParam("id") String resultId,
    @QueryParam("minTau") Double minTau, 
    @QueryParam("maxFdr") Double maxFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss
    ) {
    final CmapDrugGeneSetSignatureInteractionListingOptions cmapListingOptions =
      new CmapDrugGeneSetSignatureInteractionListingOptions(
        ListingOptions.noModification(),
        drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxFdr
      );

    CmapGeneSetSignatureResult cmapResult = findCmapGeneSetSignatureResult(resultId);

    final String[] data =
      cmapDrugGeneSetInteractionService.listDrugMoaValues(cmapResult, cmapListingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }
  
  @GET
  @Path("params/{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/drug-status/values")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Lists the possible drug status values for the specified Cmap result.",
    response = DrugStatus.class,
    code = 200
  )
  @Override
  public Response listDrugStatusValues(
    @PathParam("id") String resultId,
    @QueryParam("minTau") Double minTau, 
    @QueryParam("maxFdr") Double maxFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("drugStatus") DrugStatus drugStatus,
    @QueryParam("minDrugDss") Double minDrugDss
  ) {
    final CmapDrugGeneSetSignatureInteractionListingOptions cmapListingOptions =
      new CmapDrugGeneSetSignatureInteractionListingOptions(
        ListingOptions.noModification(),
        drugSourceName, drugSourceDb, drugCommonName, drugMoa, drugStatus,
        minDrugDss, minTau, maxFdr
      );

    CmapGeneSetSignatureResult cmapResult = findCmapGeneSetSignatureResult(resultId);

    final DrugStatus[] data =
      cmapDrugGeneSetInteractionService.listDrugStatusValues(cmapResult, cmapListingOptions)
        .toArray(DrugStatus[]::new);

    return Response.ok(data).build();
  }
}
