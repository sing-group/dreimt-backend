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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.sing_group.dreimt.domain.dao.ListingOptions.SortField;
import org.sing_group.dreimt.domain.dao.SortDirection;
import org.sing_group.dreimt.domain.dao.execution.cmap.CmapDrugInteractionListingOptions;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugInteractionField;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapResult;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapDrugInteractionResultData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.query.ListingOptionsMapper;
import org.sing_group.dreimt.rest.mapper.spi.query.cmap.CmapDrugInteractionMapper;
import org.sing_group.dreimt.rest.resource.spi.results.CmapQueryResultsResource;
import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapDrugInteractionService;
import org.sing_group.dreimt.service.spi.query.cmap.CmapQueryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("results/cmap/")
@Produces({APPLICATION_JSON, "text/csv"})
@Stateless
@CrossDomain
@Api("results/cmap/")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultCmapQueryResultsResource implements CmapQueryResultsResource {

  @Inject
  private CmapDrugInteractionService cmapDrugInteractionService;
  
  @Inject
  private CmapQueryService cmapQueryService;
  
  @Inject
  private CmapDrugInteractionMapper cmapDrugInteractionMapper;
  
  @Inject
  private ListingOptionsMapper listingOptionsMapper;

  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @Produces(APPLICATION_JSON)
  @ApiOperation(
    value = "Returns the drug interaction results associated with the specified id in JSON format.",
    response = CmapDrugInteractionResultData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown cmap result: {id}")
  )
  @Override
  public Response cmapQueryResult(
    @PathParam("id") String resultId,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") @DefaultValue("0.05") Double maxFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") CmapDrugInteractionField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection
  ) {
    return this.cmapQueryResult(
      resultId, maxPvalue, minTes, maxTes, maxFdr, drugSourceName, drugSourceDb, drugCommonName, page, pageSize, orderField,
      sortDirection, cmapDrugInteractionMapper::toCmapDrugInteractionResultData, APPLICATION_JSON
    );
  }

  @GET
  @Path("{id: [0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}}")
  @Produces("text/csv")
  @ApiOperation(
    value = "Returns the drug interaction results associated with the specified id in CSV format.",
    response = String.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown cmap result: {id}")
  )
  @Override
  public Response cmapQueryResultAsCsv(
    @PathParam("id") String resultId,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("page") Integer page,
    @QueryParam("pageSize") Integer pageSize,
    @QueryParam("orderField") @DefaultValue("NONE") CmapDrugInteractionField orderField,
    @QueryParam("sortDirection") @DefaultValue("NONE") SortDirection sortDirection
  ) {
    return this.cmapQueryResult(
      resultId, maxPvalue, minTes, maxTes, maxFdr, drugSourceName, drugSourceDb, drugCommonName, page, pageSize, orderField,
      sortDirection, cmapDrugInteractionMapper::toCmapDrugInteractionCsvData, "text/csv"
    );
  }

  private Response cmapQueryResult(
    String resultId,
    Double maxPvalue, Double minTes, Double maxTes, Double maxFdr,
    String drugSourceName, String drugSourceDb, String drugCommonName,
    Integer page, Integer pageSize,
    CmapDrugInteractionField orderField, SortDirection sortDirection,
    Function<List<CmapDrugInteraction>, Object> cmapDrugInteractionMapper,
    String responseContentType
  ) {
    final ListingOptionsData listingOptions = getListingOptions(page, pageSize, orderField, sortDirection);

    final CmapDrugInteractionListingOptions cmapListingOptions =
      new CmapDrugInteractionListingOptions(
        listingOptionsMapper.toListingOptions(listingOptions), 
        drugSourceName, drugSourceDb, drugCommonName, 
        maxPvalue, minTes, maxTes, maxFdr
     );

    CmapResult cmapResult =
      cmapQueryService.getResult(resultId)
        .orElseThrow(() -> new IllegalArgumentException("Unknown cmap result: " + resultId));

    List<CmapDrugInteraction> cmapDrugInteractions =
      this.cmapDrugInteractionService.list(cmapResult, cmapListingOptions).collect(toList());

    return Response
      .ok(cmapDrugInteractionMapper.apply(cmapDrugInteractions), responseContentType)
      .build();
  }

  private ListingOptionsData getListingOptions(
    Integer page, Integer pageSize, CmapDrugInteractionField orderField, SortDirection sortDirection
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
}
