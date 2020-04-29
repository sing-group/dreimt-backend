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
import static org.sing_group.dreimt.domain.dao.ListingOptions.noModification;

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

import org.sing_group.dreimt.domain.dao.SortDirection;
import org.sing_group.dreimt.domain.dao.execution.cmap.CmapDrugUpDownSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugUpDownSignatureInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapResult;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapUpDownSignatureDrugInteractionField;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapUpDownSignatureResult;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapQueryUpDownSignatureMetadataData;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapUpDownSignatureDrugInteractionData;
import org.sing_group.dreimt.rest.entity.signature.UpDownSignatureGeneData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.query.ListingOptionsMapper;
import org.sing_group.dreimt.rest.mapper.spi.query.cmap.CmapQueryUpDownSignatureResultsMapper;
import org.sing_group.dreimt.rest.resource.spi.results.CmapUpDownSignatureQueryResultsResource;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapDrugUpDownSignatureInteractionService;
import org.sing_group.dreimt.service.spi.query.cmap.CmapQueryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("results/cmap/signature/")
@Produces({APPLICATION_JSON, "text/csv"})
@Stateless
@CrossDomain(allowedHeaders = "X-Count")
@Api("results/cmap/signature/")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultCmapUpDownSignatureQueryResultsResource implements CmapUpDownSignatureQueryResultsResource {

  @Inject
  private CmapDrugUpDownSignatureInteractionService cmapDrugSignatureInteractionService;
  
  @Inject
  private CmapQueryService cmapQueryService;
  
  @Inject
  private CmapQueryUpDownSignatureResultsMapper mapper;
  
  @Inject
  private ListingOptionsMapper listingOptionsMapper;
  
  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Returns the metadata associated with the specified Cmap result.",
    response = CmapQueryUpDownSignatureMetadataData.class,
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
    response = UpDownSignatureGeneData.class,
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

  private Response cmapQueryResult(String resultId, Function<CmapUpDownSignatureResult, Object> resultMapper) {
    CmapUpDownSignatureResult cmapResult = findCmapUpDownSignatureResult(resultId);

    return Response
      .ok(resultMapper.apply(cmapResult), APPLICATION_JSON)
      .build();
  }

  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/interactions")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Returns the drug interaction results associated with the specified Cmap result in JSON format.",
    response = CmapUpDownSignatureDrugInteractionData.class,
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
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") CmapUpDownSignatureDrugInteractionField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection
  ) {
    return this.cmapSignatureQueryResult(
      resultId, 
      minTau, maxUpFdr, maxDownFdr, drugSourceName, drugSourceDb, 
      drugCommonName, drugMoa, minDrugDss, page, pageSize, orderField, sortDirection,
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
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugMoa") String drugCommonName,
    @QueryParam("drugCommonName") String drugMoa,
    @QueryParam("minDrugDss") Double minDrugDss,
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") CmapUpDownSignatureDrugInteractionField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection
  ) {
    return this.cmapSignatureQueryResult(
      resultId, 
      minTau, maxUpFdr, maxDownFdr, drugSourceName, drugSourceDb, 
      drugCommonName, drugMoa, minDrugDss, page, pageSize, orderField, sortDirection,
      (cmapResult, interactions) -> mapper.toCmapDrugInteractionCsvData(interactions), 
      "text/csv", false
    );
  }

  private Response cmapSignatureQueryResult(
    String resultId,
    Double minTau, 
    Double maxUpFdr, 
    Double maxDownFdr,
    String drugSourceName, 
    String drugSourceDb, 
    String drugCommonName,
    String drugMoa,
    Double minDrugDss,
    Integer page, 
    Integer pageSize,
    CmapUpDownSignatureDrugInteractionField orderField, 
    SortDirection sortDirection,
    BiFunction<CmapResult, List<CmapDrugUpDownSignatureInteraction>, Object> cmapDrugInteractionMapper,
    String responseContentType,
    boolean includeCountHeader
  ) {
    final ListingOptionsData listingOptions = ListingOptionsData.from(page, pageSize, orderField.name(), sortDirection);

    final CmapDrugUpDownSignatureInteractionListingOptions cmapListingOptions =
      new CmapDrugUpDownSignatureInteractionListingOptions(
        listingOptionsMapper.toListingOptions(listingOptions),
        drugSourceName, drugSourceDb, drugCommonName, drugMoa,
        minDrugDss, minTau, maxUpFdr, maxDownFdr
      );

    CmapUpDownSignatureResult cmapResult = findCmapUpDownSignatureResult(resultId);

    List<CmapDrugUpDownSignatureInteraction> cmapDrugInteractions =
      this.cmapDrugSignatureInteractionService.list(cmapResult, cmapListingOptions).collect(toList());

    ResponseBuilder responseBuilder =
      Response
        .ok(cmapDrugInteractionMapper.apply(cmapResult, cmapDrugInteractions), responseContentType);

    if (includeCountHeader) {
      final long count = this.cmapDrugSignatureInteractionService.count(cmapResult, cmapListingOptions);
      responseBuilder = responseBuilder.header("X-Count", count);
    }

    return responseBuilder.build();
  }
  
  protected CmapUpDownSignatureResult findCmapUpDownSignatureResult(String resultId) {
    CmapResult cmapResult =
      cmapQueryService.getResult(resultId)
        .orElseThrow(() -> new IllegalArgumentException("Unknown cmap result: " + resultId));
    
    if (!(cmapResult instanceof CmapUpDownSignatureResult)) {
      throw new IllegalArgumentException(
        "The specified cmap result (" + resultId + ") corresponds to a GeneSet query. Use the appropiate method."
      );
    }
    
    return (CmapUpDownSignatureResult) cmapResult;
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
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("minDrugDss") Double minDrugDss
  ) {
    final CmapDrugUpDownSignatureInteractionListingOptions cmapListingOptions =
      new CmapDrugUpDownSignatureInteractionListingOptions(
        noModification(), drugSourceName, drugSourceDb, drugCommonName, drugMoa, minDrugDss, minTau, maxUpFdr, maxDownFdr
      );

    CmapUpDownSignatureResult cmapResult = findCmapUpDownSignatureResult(resultId);
    
    final String[] data =
      cmapDrugSignatureInteractionService.listDrugSourceNameValues(cmapResult, cmapListingOptions)
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
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("minDrugDss") Double minDrugDss
  ) {
    final CmapDrugUpDownSignatureInteractionListingOptions cmapListingOptions =
      new CmapDrugUpDownSignatureInteractionListingOptions(
        noModification(), drugSourceName, drugSourceDb, drugCommonName, drugMoa, minDrugDss, minTau, maxUpFdr, maxDownFdr
      );

    CmapUpDownSignatureResult cmapResult = findCmapUpDownSignatureResult(resultId);

    final String[] data =
      cmapDrugSignatureInteractionService.listDrugSourceDbValues(cmapResult, cmapListingOptions)
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
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("minDrugDss") Double minDrugDss
  ) {
    final CmapDrugUpDownSignatureInteractionListingOptions cmapListingOptions =
      new CmapDrugUpDownSignatureInteractionListingOptions(
        noModification(), drugSourceName, drugSourceDb, drugCommonName, drugMoa, minDrugDss, minTau, maxUpFdr, maxDownFdr
      );

    CmapUpDownSignatureResult cmapResult = findCmapUpDownSignatureResult(resultId);

    final String[] data =
      cmapDrugSignatureInteractionService.listDrugCommonNameValues(cmapResult, cmapListingOptions)
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
    @QueryParam("maxUpFdr") Double maxUpFdr,
    @QueryParam("maxDownFdr") Double maxDownFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugMoa") String drugMoa,
    @QueryParam("minDrugDss") Double minDrugDss
  ) {
    final CmapDrugUpDownSignatureInteractionListingOptions cmapListingOptions =
      new CmapDrugUpDownSignatureInteractionListingOptions(
        noModification(), drugSourceName, drugSourceDb, drugCommonName, drugMoa, minDrugDss, minTau, maxUpFdr, maxDownFdr
      );

    CmapUpDownSignatureResult cmapResult = findCmapUpDownSignatureResult(resultId);

    final String[] data =
      cmapDrugSignatureInteractionService.listDrugMoaValues(cmapResult, cmapListingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }
}
