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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.sing_group.dreimt.domain.entities.signature.ArticleMetadata;
import org.sing_group.dreimt.rest.entity.signature.ArticleMetadataData;
import org.sing_group.dreimt.rest.filter.CrossDomain;
import org.sing_group.dreimt.rest.mapper.spi.signature.ArticleMetadataMapper;
import org.sing_group.dreimt.rest.resource.spi.signature.ArticleMetadataResource;
import org.sing_group.dreimt.service.spi.signature.ArticleMetadataService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("article")
@Produces(APPLICATION_JSON)
@Stateless
@CrossDomain
@Api("article")
@ApiResponses({
  @ApiResponse(code = 200, message = "successful operation")
})
public class DefaultArticleMetadataResource implements ArticleMetadataResource {
  
  @Inject
  private ArticleMetadataService service;

  @Inject
  private ArticleMetadataMapper articleMetadataMapper;

  @GET
  @Path("{pubMedId}")
  @ApiOperation(
    value = "Returns the article metadata associated with the specified PubMed ID.",
    response = ArticleMetadataData.class,
    code = 200
  )
  @ApiResponses(
    @ApiResponse(code = 400, message = "Unknown PubMed ID: {pubMedId}")
  )
  @Override
  public Response get(@PathParam("pubMedId") int pubMedId) {
    Optional<ArticleMetadata> articleMetadata = this.service.get(pubMedId);
    if (articleMetadata.isPresent()) {
      return Response
        .ok(this.articleMetadataMapper.toArticleMetadataData(articleMetadata.get()))
        .build();
    } else {
      throw new IllegalArgumentException("Unknown PubMed ID: " + pubMedId);
    }
  }
}
