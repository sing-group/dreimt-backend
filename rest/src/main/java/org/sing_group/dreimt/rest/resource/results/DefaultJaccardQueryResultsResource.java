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
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.dreimt.domain.dao.ListingOptions.SortField;
import org.sing_group.dreimt.domain.dao.SortDirection;
import org.sing_group.dreimt.domain.entities.execution.jaccard.GeneOverlap;
import org.sing_group.dreimt.domain.entities.execution.jaccard.GeneOverlapField;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardResult;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;
import org.sing_group.dreimt.rest.entity.query.jaccard.GeneOverlapResultData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.query.ListingOptionsMapper;
import org.sing_group.dreimt.rest.mapper.spi.query.jaccard.GeneOverlapMapper;
import org.sing_group.dreimt.rest.resource.spi.results.JaccardQueryResultsResource;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapService;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardQueryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("results/jaccard/")
@Produces({APPLICATION_JSON, "text/csv"})
@Stateless
@CrossDomain
@Api("results/jaccard/")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultJaccardQueryResultsResource implements JaccardQueryResultsResource {

  @Inject
  private GeneOverlapService geneOverlapService;
  
  @Inject
  private JaccardQueryService jaccardQueryService;
  
  @Inject
  private GeneOverlapMapper jaccardResultMapper;
  
  @Inject
  private ListingOptionsMapper listingOptionsMapper;

  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void postConstruct() {
    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();

    this.jaccardResultMapper.setUriBuilder(uriBuilder);
  }
  
  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Returns the gene overlap results associated with the specified id in JSON format.",
    response = GeneOverlapResultData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown jaccard result: {id}")
  )
  @Override
  public Response jaccardQueryResult(
    @PathParam("id") String resultId,
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") GeneOverlapField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection
  ) {
    return this.jaccardQueryResult(
      resultId, page, pageSize, orderField, sortDirection,
      jaccardResultMapper::toGeneOverlapResultData,
      APPLICATION_JSON
    );
  }

  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @Produces("text/csv")
  @ApiOperation(
    value = "Returns the gene overlap results associated with the specified id in CSV format.",
    response = String.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown jaccard result: {id}")
  )
  @Override
  public Response jaccardQueryResultAsCsv(
    @PathParam("id") String resultId,
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") GeneOverlapField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection
  ) {
    return this.jaccardQueryResult(
      resultId, page, pageSize, orderField, sortDirection,
      jaccardResultMapper::toGeneOverlapCsvData,
      "text/csv"
    );
  }

  private Response jaccardQueryResult(
    String resultId,
    Integer page, Integer pageSize,
    GeneOverlapField orderField, SortDirection sortDirection,
    Function<List<GeneOverlap>, Object> geneOverlapMapper,
    String responseContentType
  ) {
    final ListingOptionsData options;

    if (orderField == null || sortDirection == null || sortDirection == SortDirection.NONE) {
      options = new ListingOptionsData(page, pageSize);
    } else {
      SortField sortField = new SortField(orderField.getFieldName(), sortDirection);
      options = new ListingOptionsData(page, pageSize, sortField);
    }

    JaccardResult jaccardResult =
      jaccardQueryService.getResult(resultId)
        .orElseThrow(() -> new IllegalArgumentException("Unknown jaccard result: " + resultId));

    List<GeneOverlap> geneOverlaps =
      this.geneOverlapService
        .list(jaccardResult, listingOptionsMapper.toListingOptions(options))
        .collect(toList());

    return Response
      .ok(geneOverlapMapper.apply(geneOverlaps), responseContentType)
      .build();
  }
}
