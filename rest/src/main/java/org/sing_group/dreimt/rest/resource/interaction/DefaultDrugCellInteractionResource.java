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
package org.sing_group.dreimt.rest.resource.interaction;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.sing_group.dreimt.rest.entity.interaction.DrugCellInteractionData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.interaction.DrugCellInteractionMapper;
import org.sing_group.dreimt.rest.resource.spi.interaction.DrugCellInteractionResource;
import org.sing_group.dreimt.service.spi.interaction.DrugCellInteractionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("interaction")
@Produces(APPLICATION_JSON)
@Stateless
@CrossDomain
@Api("interaction")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultDrugCellInteractionResource implements DrugCellInteractionResource {

  @Inject
  private DrugCellInteractionService service;
  
  @Inject
  private DrugCellInteractionMapper mapper;

  @GET
  @ApiOperation(
    value = "List all the drug-cell interactions in the database",
    response = DrugCellInteractionData.class,
    responseContainer = "List",
    code = 200
  )
  @Override
  public Response list() {
    final DrugCellInteractionData[] data = this.service.list()
      .map(this.mapper::toDrugCellInteractionData)
    .toArray(DrugCellInteractionData[]::new);
    
    return Response.ok(data).build();
  }

}
