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
package org.sing_group.dreimt.rest.resource.spi.results;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

import org.sing_group.dreimt.domain.dao.SortDirection;
import org.sing_group.dreimt.domain.entities.execution.jaccard.GeneOverlapField;

@Local
public interface JaccardQueryResultsResource {

  Response jaccardQueryMetadata(String resultId);

  Response jaccardQueryGenes(String resultId, boolean onlyUniverseGenes);

  Response jaccardQueryGeneOverlaps(
    String resultId, Double maxJaccard, Double maxPvalue, Double maxFdr, Integer page, Integer pageSize,
    GeneOverlapField orderField, SortDirection sortDirection
  );

  Response jaccardQueryGeneOverlapsAsCsv(
    String resultId, Double maxJaccard, Double maxPvalue, Double maxFdr, Integer page, Integer pageSize,
    GeneOverlapField orderField, SortDirection sortDirection
  );
}
