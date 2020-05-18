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

import java.util.Optional;

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

import org.sing_group.dreimt.domain.dao.signature.SignatureListingOptions;
import org.sing_group.dreimt.domain.entities.signature.CellTypeAndSubtype;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;
import org.sing_group.dreimt.rest.entity.signature.SignatureData;
import org.sing_group.dreimt.rest.entity.signature.UpDownSignatureGeneData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.signature.SignatureMapper;
import org.sing_group.dreimt.rest.resource.spi.signature.SignatureResource;
import org.sing_group.dreimt.service.spi.signature.SignatureService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("signature")
@Produces(APPLICATION_JSON)
@Stateless
@CrossDomain
@Api("signature")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultSignatureResource implements SignatureResource {

  @Inject
  private SignatureService service;

  @Inject
  private SignatureMapper signatureMapper;
  
  @Context
  private UriInfo uriInfo;

  @PostConstruct
  public void postConstruct() {
    final UriBuilder uriBuilder = this.uriInfo.getBaseUriBuilder();
    this.signatureMapper.setUriBuilder(uriBuilder);
  }

  @GET
  @Path("{signatureName}")
  @ApiOperation(
    value = "Returns the information associated with the query signature", 
    response = SignatureData.class, 
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown signature name: {signatureName}")
  )
  @Override
  public Response get(@PathParam("signatureName") String signatureName) {
    Optional<Signature> signature = this.service.get(signatureName);
    if (signature.isPresent()) {
      final SignatureData data = this.signatureMapper.toSignatureData(signature.get());
      return Response.ok(data).build();
    } else {
      throw new IllegalArgumentException("Unknown signature name: " + signatureName);
    }
  }

  @Path("list")
  @GET
  @ApiOperation(
    value = "Lists the signatures",
    response = SignatureData.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response list(
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
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signatureType, signaturePubMedId, null, null
      );

    final SignatureData[] data =
      service.list(signatureListingOptions)
        .map(this.signatureMapper::toSignatureData)
        .toArray(SignatureData[]::new);

    return Response.ok(data).build();
  }
  
  @Path("list/count")
  @GET
  @ApiOperation(
    value = "Counts the signatures",
    response = Integer.class,
    code = 200
  )
  @Override
  public Response count(
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
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signatureType, signaturePubMedId, null, null
      );

    return Response.ok(service.count(signatureListingOptions)).build();
  }

  @GET
  @Path("{signatureName}/genes")
  @ApiOperation(
    value = "Returns the genes associated with the query signature", 
    response = UpDownSignatureGeneData.class, 
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown signature name: {signatureName}")
  )
  @Override
  public Response getGenes(
    @PathParam("signatureName") String signatureName,
    @DefaultValue("false") @QueryParam("onlyUniverseGenes") boolean onlyUniverseGenes
  ) {
    Optional<Signature> signature = this.service.get(signatureName);
    if (signature.isPresent()) {
      final Object data = this.signatureMapper.toSignatureGeneData(signature.get(), onlyUniverseGenes);

      return Response.ok(data).build();
    } else {
      throw new IllegalArgumentException("Unknown signature name: " + signatureName);
    }
  }

  @Path("params/signature-name/values")
  @GET
  @ApiOperation(
    value = "Lists the possible signature name values in signatures", 
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
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signatureType, signaturePubMedId, null, null
      );

    final String[] data =
      service.listSignatureNameValues(signatureListingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @Path("params/cell-type-and-subtype-1/values")
  @GET
  @ApiOperation(
    value = "Lists the possible cell type and subtype 1 values in signatures",
    response = CellTypeAndSubtype.class,
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
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId
    ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, null, null, null,
        experimentalDesign, organism, disease, signatureSourceDb, signatureType, signaturePubMedId, null, null
      );

    final CellTypeAndSubtype[] data =
      service.listCellTypeAndSubtype1Values(signatureListingOptions)
        .toArray(CellTypeAndSubtype[]::new);

    return Response.ok(data).build();
  }

  @Path("params/cell-type-and-subtype-2/values")
  @GET
  @ApiOperation(
    value = "Lists the possible cell type and subtype 2 values in signatures",
    response = CellTypeAndSubtype.class,
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
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signatureType, signaturePubMedId, null, null
      );

    final CellTypeAndSubtype[] data =
      service.listCellTypeAndSubtype2Values(signatureListingOptions)
        .toArray(CellTypeAndSubtype[]::new);

    return Response.ok(data).build();
  }

  @Path("params/experimental-design/values")
  @GET
  @ApiOperation(
    value = "Lists the possible experimental design values in signatures", 
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
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signatureType, signaturePubMedId, null, null
      );

    final ExperimentalDesign[] data =
      service.listExperimentalDesignValues(signatureListingOptions)
        .toArray(ExperimentalDesign[]::new);

    return Response.ok(data).build();
  }
  
  @Path("params/organism/values")
  @GET
  @ApiOperation(
    value = "Lists the possible organism values in signatures", 
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
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signatureType, signaturePubMedId, null, null
      );

    final String[] data =
      service.listOrganismValues(signatureListingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @Path("params/disease/values")
  @GET
  @ApiOperation(
    value = "Lists the possible disease values in signatures", 
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
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signatureType, signaturePubMedId, null, null
      );

    final String[] data =
      service.listDiseaseValues(signatureListingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }
  
  @Path("params/signature-source-db/values")
  @GET
  @ApiOperation(
    value = "Lists the possible signature source DB values in signatures", 
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
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signatureType, signaturePubMedId, null, null
      );

    final String[] data =
      service.listSourceDbValues(signatureListingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }

  @Path("params/signature-type/values")
  @GET
  @ApiOperation(
    value = "Lists the possible signature type values in signatures", 
    response = String.class,
    responseContainer = "list", 
    code = 200
  )
  @Override
  public Response listSignatureTypeValues(
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
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signatureType, signaturePubMedId, null, null
      );

    final SignatureType[] data =
      service.listSignatureTypeValues(signatureListingOptions)
        .toArray(SignatureType[]::new);

    return Response.ok(data).build();
  }

  @Path("params/signature-pubmed-id/values")
  @GET
  @ApiOperation(
    value = "Lists the possible signature article PubMed ID values in signatures", 
    response = String.class,
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
    @QueryParam("signatureType") SignatureType signatureType,
    @QueryParam("signaturePubMedId") Integer signaturePubMedId
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        signatureName, cellType1, cellSubType1, cellTypeOrSubType1, cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, organism, disease, signatureSourceDb, signatureType, signaturePubMedId, null, null
      );

    final Integer[] data =
      service.listSignaturePubMedIdValues(signatureListingOptions)
        .toArray(Integer[]::new);

    return Response.ok(data).build();
  }
}
