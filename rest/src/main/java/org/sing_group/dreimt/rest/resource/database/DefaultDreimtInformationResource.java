package org.sing_group.dreimt.rest.resource.database;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.sing_group.dreimt.domain.entities.database.DreimtInformation;
import org.sing_group.dreimt.rest.entity.database.DreimtInformationData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.database.DreimtInformationMapper;
import org.sing_group.dreimt.rest.resource.spi.database.DreimtInformationResource;
import org.sing_group.dreimt.service.spi.database.DreimtInformationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("database")
@Produces(APPLICATION_JSON)
@Stateless
@CrossDomain
@Api("database")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultDreimtInformationResource implements DreimtInformationResource {

  @Inject
  private DreimtInformationService service;
  
  @Inject
  private DreimtInformationMapper dreimtInformationMapper;
  
  @GET
  @Path("information")
  @ApiOperation(
    value = "Returns the general information associated with the Dreimt database", 
    response = DreimtInformationData.class, 
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Dreimt database information not available")
  )
  @Override
  public Response getDreimtInformation() {
    Optional<DreimtInformation> dreimtInformation = this.service.get();
    if (dreimtInformation.isPresent()) {
      return Response.ok(this.dreimtInformationMapper.mapToDreimtInformationData(dreimtInformation.get())).build();
    } else {
      throw new IllegalArgumentException("Dreimt database information not available.");
    }
  }
}
