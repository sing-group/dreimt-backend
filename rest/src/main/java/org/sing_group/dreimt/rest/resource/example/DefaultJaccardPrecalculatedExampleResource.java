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
package org.sing_group.dreimt.rest.resource.example;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.sing_group.dreimt.rest.entity.example.JaccardPrecalculatedExampleData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.example.JaccardPrecalculatedExampleMapper;
import org.sing_group.dreimt.rest.resource.spi.example.JaccardPrecalculatedExampleResource;
import org.sing_group.dreimt.service.spi.example.JaccardPrecalculatedExampleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("examples/jaccard/")
@Produces({
  APPLICATION_JSON
})
@Stateless
@CrossDomain(allowedHeaders = "X-Count")
@Api("results/jaccard/")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultJaccardPrecalculatedExampleResource implements JaccardPrecalculatedExampleResource {

  @Inject
  private JaccardPrecalculatedExampleService service;
  
  @Inject
  private JaccardPrecalculatedExampleMapper mapper;
  
  @Context
  private UriInfo uriInfo;
  
  @PostConstruct
  public void postConstruct() {
    this.mapper.setUriBuilder(this.uriInfo.getBaseUriBuilder());
  }

  @GET
  @ApiOperation(
    value = "Lists the jaccard precalculated examples",
    response = JaccardPrecalculatedExampleData.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response list() {
    final JaccardPrecalculatedExampleData[] data =
      service.list()
        .map(this.mapper::toJaccardPrecalculatedExampleData)
        .toArray(JaccardPrecalculatedExampleData[]::new);
    
    return Response.ok(data).build();
  }
}
