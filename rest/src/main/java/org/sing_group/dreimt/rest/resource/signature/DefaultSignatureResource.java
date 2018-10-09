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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.sing_group.dreimt.rest.entity.signature.SignatureData;
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
  private SignatureMapper mapper;

  @GET
  @ApiOperation(
    value = "List all the signatures in the database",
    response = SignatureData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response list() {
    final SignatureData[] data = this.service.list()
      .map(this.mapper::toSignatureData)
    .toArray(SignatureData[]::new);
    
    return Response.ok(data).build();
  }

}
