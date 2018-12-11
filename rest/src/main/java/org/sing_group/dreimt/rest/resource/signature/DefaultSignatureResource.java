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

  @Path("params/cell-type-a/values")
  @GET
  @ApiOperation(
    value = "Lists the possible cell type a values in signatures", 
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
    @QueryParam("signatureType") SignatureType signatureType
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final String[] data =
      service.listCellTypeAValues(signatureListingOptions)
        .toArray(String[]::new);

    return Response.ok(data).build();
  }
  
  @Path("params/cell-type-b/values")
  @GET
  @ApiOperation(
    value = "Lists the possible cell type b values in signatures", 
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
    @QueryParam("signatureType") SignatureType signatureType
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final String[] data =
      service.listCellTypeBValues(signatureListingOptions)
        .toArray(String[]::new);

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
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
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
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
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
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
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
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
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
    @QueryParam("cellTypeA") String cellTypeA,
    @QueryParam("cellTypeB") String cellTypeB,
    @QueryParam("experimentalDesign") ExperimentalDesign experimentalDesign,
    @QueryParam("organism") String organism,
    @QueryParam("disease") String disease,
    @QueryParam("signatureSourceDb") String signatureSourceDb,
    @QueryParam("signatureType") SignatureType signatureType
  ) {
    final SignatureListingOptions signatureListingOptions =
      new SignatureListingOptions(
        cellTypeA, cellTypeB, experimentalDesign, organism, disease, signatureSourceDb, signatureType
      );

    final SignatureType[] data =
      service.listSignatureTypeValues(signatureListingOptions)
        .toArray(SignatureType[]::new);

    return Response.ok(data).build();
  }
}
