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

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.sing_group.dreimt.domain.entities.query.DrugSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;
import org.sing_group.dreimt.rest.entity.query.DrugSignatureInteractionListingOptionsData;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;
import org.sing_group.dreimt.rest.entity.signature.DrugSignatureInteractionData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugSignatureInteractionMapper;
import org.sing_group.dreimt.rest.resource.spi.signature.DrugSignatureInteractionResource;
import org.sing_group.dreimt.service.spi.signature.DrugSignatureInteractionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("interactions")
@Produces(APPLICATION_JSON)
@Stateless
@CrossDomain
@Api("interactions")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultDrugSignatureInteractionResource implements DrugSignatureInteractionResource {

  @Inject
  private DrugSignatureInteractionService service;

  @Inject
  private DrugSignatureInteractionMapper drugSignatureMapper;
  
  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void postConstruct() {
    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    this.drugSignatureMapper.setUriBuilder(uriBuilder);
  }

  @GET
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
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr
  ) {
    final DrugSignatureInteractionListingOptionsData listingOptionsData =
      new DrugSignatureInteractionListingOptionsData(
        new ListingOptionsData(page, pageSize),
        cellTypeA, cellTypeB, experimentalDesign, organism, drugSourceName, drugCommonName, signatureType, maxPvalue, minTes, maxTes, maxFdr
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      drugSignatureMapper.toDrugSignatureInteractionListingOptions(listingOptionsData);

    final DrugSignatureInteractionData[] data =
      service.list(listingOptions)
        .map(this.drugSignatureMapper::toDrugSignatureInteractionData)
        .toArray(DrugSignatureInteractionData[]::new);

    return Response.ok(data).build();
  }
}
