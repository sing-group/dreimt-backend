/*-
 * #%L
 * DREIMT - REST
 * %%
 * Copyright (C) 2018 - 2020 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
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
package org.sing_group.dreimt.rest.resource.statistics;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.resource.spi.statistics.StatisticalTestsResource;
import org.sing_group.dreimt.service.spi.statistics.HypergeometricTestData;
import org.sing_group.dreimt.service.spi.statistics.StatisticalTestsService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("statistical")
@Produces({
  APPLICATION_JSON
})
@Stateless
@CrossDomain(allowedHeaders = "X-Count")
@Api("statistical")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultStatisticalTestsResource implements StatisticalTestsResource {

  @Inject
  private StatisticalTestsService service;

  @POST
  @Produces(APPLICATION_JSON)
  @Path("tests/hypergeometric")
  @Consumes(APPLICATION_JSON)
  @ApiOperation(
    value = "Returns the p-value of the corresponding hypergeometric test.",
    response = HypergeometricTestData.class,
    responseContainer = "list",
    code = 200
  )
  @Override
  public Response hypergeometricTest(HypergeometricTestDataContainer data) {
    return Response
      .ok(this.service.hypergeometricTest(data.getData()))
      .build();
  }
}
