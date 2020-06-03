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

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;
import java.util.Map;
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
import org.sing_group.dreimt.domain.dao.execution.jaccard.GeneOverlapListingOptions;
import org.sing_group.dreimt.domain.entities.execution.jaccard.GeneOverlap;
import org.sing_group.dreimt.domain.entities.execution.jaccard.GeneOverlapField;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardComparisonType;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardResult;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;
import org.sing_group.dreimt.rest.entity.query.jaccard.GeneOverlapData;
import org.sing_group.dreimt.rest.entity.query.jaccard.SignaturesComparisonQueryMetadataData;
import org.sing_group.dreimt.rest.entity.signature.UpDownSignatureGeneData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.query.ListingOptionsMapper;
import org.sing_group.dreimt.rest.mapper.spi.query.ValuesDistributionMapper;
import org.sing_group.dreimt.rest.mapper.spi.query.jaccard.JaccardQueryResultsMapper;
import org.sing_group.dreimt.rest.resource.spi.results.JaccardQueryResultsResource;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapService;
import org.sing_group.dreimt.service.spi.query.ValuesDistribution;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("results/signatures-comparison/")
@Produces({APPLICATION_JSON, "text/csv"})
@Stateless
@CrossDomain(allowedHeaders = "X-Count")
@Api("results")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultJaccardQueryResultsResource implements JaccardQueryResultsResource {

  @Inject
  private GeneOverlapService geneOverlapService;
  
  @Inject
  private JaccardQueryService jaccardQueryService;
  
  @Inject
  private JaccardQueryResultsMapper mapper;
  
  @Inject
  private ListingOptionsMapper listingOptionsMapper;
  
  @Inject
  private ValuesDistributionMapper valuesDistributionMapper;

  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Returns the metadata associated with the specified signatures comparison result.",
    response = SignaturesComparisonQueryMetadataData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown signatures comparison result: {id}")
  )
  @Override
  public Response jaccardQueryMetadata(@PathParam("id") String resultId) {
    return this.jaccardQueryResult(
      resultId,
      this.mapper::toSignaturesComparisonQueryMetadataData
    );
  }

  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/genes")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Returns the query genes associated with the specified signatures comparison result.",
    response = UpDownSignatureGeneData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown signatures comparison result: {id}")
  )
  @Override
  public Response jaccardQueryGenes(
    @PathParam("id") String resultId,
    @DefaultValue("false") @QueryParam("onlyUniverseGenes") boolean onlyUniverseGenes
  ) {
    return this.jaccardQueryResult(
      resultId,
      jaccardResult -> this.mapper.toGeneData(jaccardResult, onlyUniverseGenes)
    );
  }

  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/genes/intersection/{signatureName}")
  @Produces("text/plain")
  @ApiOperation(
    value = "Returns the intersection between the query genes and the specified signature associated with the specified signatures comparison result.",
    response = String.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown signatures comparison result: {id}")
  )
  @Override
  public Response jaccardIntersectionQueryGenes(
    @PathParam("id") String resultId,
    @PathParam("signatureName") String signatureName,
    @QueryParam("sourceComparisonType") JaccardComparisonType sourceComparisonType,
    @QueryParam("targetComparisonType") JaccardComparisonType targetComparisonType 
  ) {
    if (sourceComparisonType == null || targetComparisonType == null) {
      throw new IllegalArgumentException("sourceComparisonType and targetComparisonType are mandatory");
    }

    String result =
      this.jaccardQueryService.jaccardIntersectionQueryGenes(
        resultId, signatureName,
        sourceComparisonType, targetComparisonType
      ).stream().collect(joining("\n"));

    return Response
      .ok(result, APPLICATION_JSON)
      .build();
  }

  private Response jaccardQueryResult(String resultId, Function<JaccardResult, Object> resultMapper) {
    JaccardResult jaccardResult = getResult(resultId);

    return Response
      .ok(resultMapper.apply(jaccardResult), APPLICATION_JSON)
      .build();
  }

  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/distribution/cell-type-and-subtype")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Returns the distribution of the cell type and the cell subtype values in the signatures associated with the specified signatures comparison result.",
    response = Map.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown signatures comparison result: {id}")
  )
  @Override
  public Response cellTypeAndSubTypeDistribution(
    @PathParam("id") String resultId
  ) {
    ValuesDistribution cellTypeDistribution = this.jaccardQueryService.cellTypeAndSubTypeDistribution(resultId);

    return Response
      .ok(valuesDistributionMapper.toMap(cellTypeDistribution), APPLICATION_JSON)
      .build();
  }

  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/overlaps")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Returns the gene overlap results associated with the specified signatures comparison result in JSON format.",
    response = GeneOverlapData.class,
    responseContainer = "list",
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown signatures comparison result: {id}")
  )
  @Override
  public Response jaccardQueryGeneOverlaps(
    @PathParam("id") String resultId,
    @QueryParam("minJaccard") Double minJaccard,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("maxFdr") Double maxFdr,
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") GeneOverlapField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection
  ) {
    return this.jaccardQueryResult(
      resultId, minJaccard, maxPvalue, maxFdr, page, pageSize, orderField, sortDirection,
      (jaccardResult, overlaps) -> mapper.toGeneOverlapData(overlaps),
      APPLICATION_JSON, true
    );
  }

  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}/overlaps")
  @Produces("text/csv")
  @ApiOperation(
    value = "Returns the gene overlap results associated with the specified signatures comparison result in CSV format.",
    response = String.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown signatures comparison result: {id}")
  )
  @Override
  public Response jaccardQueryGeneOverlapsAsCsv(
    @PathParam("id") String resultId,
    @QueryParam("minJaccard") Double minJaccard,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("maxFdr") Double maxFdr,
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") GeneOverlapField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection
  ) {
    return this.jaccardQueryResult(
      resultId, minJaccard, maxPvalue, maxFdr, page, pageSize, orderField, sortDirection,
      (jaccardResult, overlaps) -> mapper.toGeneOverlapCsvData(overlaps),
      "text/csv", false
    );
  }

  private Response jaccardQueryResult(
    String resultId,
    Double minJaccard, 
    Double maxPvalue, 
    Double maxFdr, 
    Integer page,
    Integer pageSize,
    GeneOverlapField orderField,
    SortDirection sortDirection,
    BiFunction<JaccardResult, List<GeneOverlap>, Object> geneOverlapMapper,
    String responseContentType,
    boolean includeCountHeader
  ) {
    final ListingOptionsData options = ListingOptionsData.from(page, pageSize, orderField.name(), sortDirection);
    
    final GeneOverlapListingOptions geneOverlapListingOptions =
      new GeneOverlapListingOptions(listingOptionsMapper.toListingOptions(options), minJaccard, maxPvalue, maxFdr);

    JaccardResult jaccardResult = getResult(resultId);

    List<GeneOverlap> geneOverlaps =
      this.geneOverlapService
        .list(jaccardResult, geneOverlapListingOptions)
        .collect(toList());
    
    ResponseBuilder responseBuilder = Response
      .ok(geneOverlapMapper.apply(jaccardResult, geneOverlaps), responseContentType);
    
    if (includeCountHeader) {
      final long count = this.geneOverlapService.count(jaccardResult, geneOverlapListingOptions);
      responseBuilder = responseBuilder .header("X-Count", count);
    }

    return responseBuilder.build();
  }
  
  private JaccardResult getResult(String resultId) {
    return jaccardQueryService.getResult(resultId)
      .orElseThrow(() -> new IllegalArgumentException("Unknown signatures comparison result: " + resultId));
  }
}
