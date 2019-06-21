/*-
 * #%L
 * DREIMT - REST
 * %%
 * Copyright (C) 2018 - 2019 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
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

import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.sing_group.dreimt.domain.entities.signature.DatabaseVersion;
import org.sing_group.dreimt.rest.entity.signature.SignatureData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.resource.spi.signature.DatabaseVersionResource;
import org.sing_group.dreimt.service.spi.signature.DatabaseVersionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("versions")
@Produces("text/plain")
@Stateless
@CrossDomain
@Api("versions")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultDatabaseVersionResource implements DatabaseVersionResource {

  @Inject
  private DatabaseVersionService service;
  
  @GET
  @Path("database/current")
  @Produces("text/plain")
  @ApiOperation(
    value = "Returns the name of the current database version", 
    response = SignatureData.class, 
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "The current version is not defined.")
  )
  @Override
  public Response getCurrentVersion() {
    Optional<DatabaseVersion> currentDatabaseVersion = this.service.getCurrentDatabaseVersion();
    if (currentDatabaseVersion.isPresent()) {
      return Response.ok(currentDatabaseVersion.get().getVersion()).build();
    } else {
      throw new IllegalArgumentException("The current version is not defined.");
    }
  }
}
