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
import java.util.function.Function;
import java.util.function.Supplier;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
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

import org.sing_group.dreimt.domain.dao.signature.DrugSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.dao.signature.SignatureListingOptions;
import org.sing_group.dreimt.domain.entities.execution.WorkEntity;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;
import org.sing_group.dreimt.rest.entity.execution.WorkData;
import org.sing_group.dreimt.rest.entity.query.DrugSignatureInteractionListingOptionsData;
import org.sing_group.dreimt.rest.entity.query.GenesQueryInfo;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;
import org.sing_group.dreimt.rest.entity.signature.DrugSignatureInteractionData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.execution.ExecutionMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugSignatureInteractionMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugSignatureListingOptionsMapper;
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
@Produces(APPLICATION_JSON)
@Stateless
@CrossDomain
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
  private DrugSignatureListingOptionsMapper drugSignatureListingOptionsMapper;
  
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
        cellTypeA, cellTypeB, experimentalDesign, organism, signatureType,
        drugSourceName, drugCommonName,
        maxPvalue, minTes, maxTes, maxFdr
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      drugSignatureListingOptionsMapper.toDrugSignatureInteractionListingOptions(listingOptionsData);

    final DrugSignatureInteractionData[] data =
      service.list(listingOptions)
        .map(this.drugSignatureMapper::toDrugSignatureInteractionData)
        .toArray(DrugSignatureInteractionData[]::new);

    return Response.ok(data).build();
  }
  
  @POST
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
    GenesQueryInfo post,
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("signatureType") SignatureType signatureType
  ) {
    Set<String> upGenes = parseAndValidateJaccardQueryUpGenes(post.getUpGenes());
    Set<String> downGenes = parseAndValidateJaccardQueryDownGenes(post.getDownGenes());

    if (!downGenes.isEmpty() && intersection(upGenes, downGenes).size() > 0) {
      throw new IllegalArgumentException("Up and down gene lists cannot have genes in common");
    }

    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);

    final Function<String, String> resultUriBuilder =
      id -> pathBuilder.jaccardResult(id).build().toString();

    SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, signatureType
      );

    JaccardQueryOptions options =
      new DefaultJaccardQueryOptions(upGenes, downGenes, resultUriBuilder, signatureListingOptions);

    final WorkEntity work = this.jaccardQueryService.jaccardQuery(options);

    return Response.ok(this.executionMapper.toWorkData(work)).build();
  }

  private Set<String> parseAndValidateJaccardQueryUpGenes(String[] upGenes) {
    return parseAndValidateUpGenes(
      upGenes,
      this.jaccardQueryService::isValidGeneSet,
      this.jaccardQueryService::getMaximumGeneSetSize,
      this.jaccardQueryService::getMinimumGeneSetSize
    );
  }

  private Set<String> parseAndValidateJaccardQueryDownGenes(String[] downGenes) {
    return parseAndValidateDownGenes(
      downGenes,
      this.jaccardQueryService::isValidGeneSet,
      this.jaccardQueryService::getMaximumGeneSetSize,
      this.jaccardQueryService::getMinimumGeneSetSize
    );
  }

  private static Set<String> parseAndValidateUpGenes(
    String[] upGenes,
    Function<Set<String>, Boolean> isValidGeneSet,
    Supplier<Integer> maximumGeneSetSizeSupplier,
    Supplier<Integer> minimumGeneSetSizeSupplier
  ) {
    Set<String> upGenesSet =
      upGenes == null ? emptySet() : new HashSet<String>(asList(upGenes));

    if (upGenesSet.isEmpty()) {
      throw new IllegalArgumentException("Up (or geneset) genes list is always required.");
    } else if (!isValidGeneSet.apply(upGenesSet)) {
      throw new IllegalArgumentException(
        "Invalid up (or geneset) genes list size. It must have at least " + minimumGeneSetSizeSupplier.get()
          + " and at most " + maximumGeneSetSizeSupplier.get() + " genes."
      );
    }

    return upGenesSet;
  }

  private static Set<String> parseAndValidateDownGenes(
    String[] downGenes,
    Function<Set<String>, Boolean> isValidGeneSet,
    Supplier<Integer> maximumGeneSetSizeSupplier,
    Supplier<Integer> minimumGeneSetSizeSupplier
  ) {
    Set<String> downGenesSet =
      downGenes == null ? emptySet() : new HashSet<String>(asList(downGenes));

    if (!downGenesSet.isEmpty() && !isValidGeneSet.apply(downGenesSet)) {
      throw new IllegalArgumentException(
        "Invalid down genes list size. It must have at least " + minimumGeneSetSizeSupplier.get()
          + " and at most " + maximumGeneSetSizeSupplier.get() + " genes."
      );
    }

    return downGenesSet;
  }

  @POST
  @Path("query/cmap")
  @Consumes(MediaType.APPLICATION_JSON)
  @ApiOperation(
    value = "Calculates the Jaccard indexes between the introduced gene lists and all signatures in the database. "
      + "The calculus are done asynchronously, thus this method returns a work-data instance with information about "
      + "the asynchronous task doing the calculations.",
    response = WorkData.class,
    code = 200
  )
  @Override
  public Response cmapQuery(
    GenesQueryInfo post,
    @QueryParam("numPerm") @DefaultValue("1000") Integer numPerm,
    @QueryParam("maxPvalue") @DefaultValue("1") Double maxPvalue
  ) {
    this.cmapQueryService.validateNumPerm(numPerm);
    Set<String> upGenes = parseAndValidateCmapQueryUpGenes(post.getUpGenes());
    Set<String> downGenes = parseAndValidateCmapQueryDownGenes(post.getDownGenes());

    if (!downGenes.isEmpty() && intersection(upGenes, downGenes).size() > 0) {
      throw new IllegalArgumentException("Up and down gene lists cannot have genes in common");
    }

    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);

    final Function<String, String> resultUriBuilder =
      id -> pathBuilder.cmapResult(id).build().toString();

    CmapQueryOptions options =
      new DefaultCmapQueryOptions(upGenes, downGenes, resultUriBuilder, numPerm, maxPvalue);

    final WorkEntity work = this.cmapQueryService.cmapQuery(options);

    return Response.ok(this.executionMapper.toWorkData(work)).build();
  }

  private Set<String> parseAndValidateCmapQueryUpGenes(String[] upGenes) {
    return parseAndValidateUpGenes(
      upGenes,
      this.cmapQueryService::isValidGeneSet,
      this.cmapQueryService::getMaximumGeneSetSize,
      this.cmapQueryService::getMinimumGeneSetSize
    );
  }

  private Set<String> parseAndValidateCmapQueryDownGenes(String[] downGenes) {
    return parseAndValidateDownGenes(
      downGenes,
      this.cmapQueryService::isValidGeneSet,
      this.cmapQueryService::getMaximumGeneSetSize,
      this.cmapQueryService::getMinimumGeneSetSize
    );
  }
}
