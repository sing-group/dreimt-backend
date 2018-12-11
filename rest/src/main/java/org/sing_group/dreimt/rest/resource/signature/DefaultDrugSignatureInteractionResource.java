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
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionField;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;
import org.sing_group.dreimt.rest.entity.execution.WorkData;
import org.sing_group.dreimt.rest.entity.query.GenesQueryInfo;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;
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
@Produces(APPLICATION_JSON)
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
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        listingOptionsMapper.toListingOptions(getListingOptions(page, pageSize, orderField, sortDirection)),
        signatureListingOptions,
        drugSourceName, drugSourceDb, drugCommonName,
        maxPvalue, minTes, maxTes, maxFdr
      );

    final DrugSignatureInteractionData[] data =
      service.list(listingOptions)
        .map(this.drugSignatureMapper::toDrugSignatureInteractionData)
        .toArray(DrugSignatureInteractionData[]::new);

    final long count = service.count(listingOptions);

    return Response.ok(data)
      .header("X-Count", count)
    .build();
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

  @Path("params/cell-type-a/values")
  @GET
  @ApiOperation(
    value = "Lists the possible cell type a values in drug-signature interactions", 
    response = String.class,
    responseContainer = "list", 
    code = 200
  )
  @Override
  public Response listCellTypeAValues(
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions,
        drugSourceName, drugSourceDb, drugCommonName,
        maxPvalue, minTes, maxTes, maxFdr
      );

    final String[] data =
      service.listCellTypeAValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }
 
  @Path("params/cell-type-b/values")
  @GET
  @ApiOperation(
    value = "Lists the possible cell type b values in drug-signature interactions", 
    response = String.class,
    responseContainer = "list", 
    code = 200
    )
  @Override
  public Response listCellTypeBValues(
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions,
        drugSourceName, drugSourceDb, drugCommonName,
        maxPvalue, minTes, maxTes, maxFdr
      );

    final String[] data =
      service.listCellTypeBValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @Path("params/experimental-design/values")
  @GET
  @ApiOperation(
    value = "Lists the possible experimental design values in drug-signature interactions", 
    response = ExperimentalDesign.class,
    responseContainer = "list", 
    code = 200
  )
  @Override
  public Response listExperimentalDesignValues(
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions,
        drugSourceName, drugSourceDb, drugCommonName,
        maxPvalue, minTes, maxTes, maxFdr
      );

    final ExperimentalDesign[] data =
      service.listExperimentalDesignValues(listingOptions)
        .toArray(ExperimentalDesign[]::new);

    return Response.ok(data).build();
  }

  @Path("params/organism/values")
  @GET
  @ApiOperation(
    value = "Lists the possible organism values in drug-signature interactions", 
    response = String.class,
    responseContainer = "list", 
    code = 200
  )
  @Override
  public Response listOrganismValues(
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions,
        drugSourceName, drugSourceDb, drugCommonName,
        maxPvalue, minTes, maxTes, maxFdr
      );

    final String[] data =
      service.listOrganismValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @Path("params/disease/values")
  @GET
  @ApiOperation(
    value = "Lists the possible disease values in drug-signature interactions", 
    response = String.class,
    responseContainer = "list", 
    code = 200
  )
  @Override
  public Response listDiseaseValues(
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions,
        drugSourceName, drugSourceDb, drugCommonName,
        maxPvalue, minTes, maxTes, maxFdr
      );

    final String[] data =
      service.listDiseaseValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @Path("params/signature-source-db/values")
  @GET
  @ApiOperation(
    value = "Lists the possible signature source DB values in drug-signature interactions", 
    response = String.class,
    responseContainer = "list", 
    code = 200
  )
  @Override
  public Response listSignatureSourceDbValues(
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions,
        drugSourceName, drugSourceDb, drugCommonName,
        maxPvalue, minTes, maxTes, maxFdr
      );

    final String[] data =
      service.listSignatureSourceDbValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }
  
  @Path("params/signature-type/values")
  @GET
  @ApiOperation(
    value = "Lists the possible signature type values in drug-signature interactions", 
    response = String.class,
    responseContainer = "list", 
    code = 200
  )
  @Override
  public Response listSignatureTypeValues(
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions,
        drugSourceName, drugSourceDb, drugCommonName,
        maxPvalue, minTes, maxTes, maxFdr
      );

    final SignatureType[] data =
      service.listSignatureTypeValues(listingOptions)
        .toArray(SignatureType[]::new);

    return Response.ok(data).build();
  }

  @Path("params/drug-source-name/values")
  @GET
  @ApiOperation(
    value = "Lists the possible drug source name values in drug-signature interactions", 
    response = String.class,
    responseContainer = "list", 
    code = 200
  )
  @Override
  public Response listDrugSourceNameValues(
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions,
        drugSourceName, drugSourceDb, drugCommonName,
        maxPvalue, minTes, maxTes, maxFdr
      );

    final String[] data = service.listDrugSourceNameValues(listingOptions)
      .toArray(String[]::new);
    
    return Response.ok(data).build();
  }
  
  
  @Path("params/drug-source-db/values")
  @GET
  @ApiOperation(
    value = "Lists the possible drug source DB values in drug-signature interactions", 
    response = String.class,
    responseContainer = "list", 
    code = 200
    )
  @Override
  public Response listDrugSourceDbValues(
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions,
        drugSourceName, drugSourceDb, drugCommonName,
        maxPvalue, minTes, maxTes, maxFdr
      );

    final String[] data =
      service.listDrugSourceDbValues(listingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }
  
  @Path("params/drug-common-name/values")
  @GET
  @ApiOperation(
    value = "Lists the possible drug common name values in drug-signature interactions", 
    response = String.class,
    responseContainer = "list", 
    code = 200
  )
  @Override
  public Response listDrugCommonNameValues(
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("drugSourceName") String drugSourceName,
    @QueryParam("drugCommonName") String drugCommonName,
    @QueryParam("drugSourceDb") String drugSourceDb,
    @QueryParam("maxPvalue") Double maxPvalue,
    @QueryParam("minTes") Double minTes, 
    @QueryParam("maxTes") Double maxTes,
    @QueryParam("maxFdr") Double maxFdr
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final DrugSignatureInteractionListingOptions listingOptions =
      new DrugSignatureInteractionListingOptions(
        signatureListingOptions,
        drugSourceName, drugSourceDb, drugCommonName,
        maxPvalue, minTes, maxTes, maxFdr
      );

    final String[] data =
      service.listDrugCommonNameValues(listingOptions)
        .toArray(String[]::new);

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
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("onlyUniverseGenes") @DefaultValue("false") boolean onlyUniverseGenes
  ) {
    Set<String> upGenes = parseAndValidateJaccardQueryUpGenes(post.getUpGenes(), onlyUniverseGenes);
    Set<String> downGenes = parseAndValidateJaccardQueryDownGenes(post.getDownGenes(), onlyUniverseGenes);

    if (!downGenes.isEmpty() && intersection(upGenes, downGenes).size() > 0) {
      throw new IllegalArgumentException("Up and down gene lists cannot have genes in common");
    }

    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder(uriBuilder);

    final Function<String, String> resultUriBuilder =
      id -> pathBuilder.jaccardResult(id).build().toString();

    SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    JaccardQueryOptions options =
      new DefaultJaccardQueryOptions(upGenes, downGenes, onlyUniverseGenes, resultUriBuilder, signatureListingOptions);

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

    if (upGenesSet.isEmpty()) {
      throw new IllegalArgumentException("Up (or geneset) genes list is always required.");
    } else if (!isValidGeneSet.apply(upGenesSet, onlyUniverseGenes)) {
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
    return onlyUniverseGenes ? " Note that genes must be in the genes universe." : "";
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
