/*-
 * #%L
 * DREIMT - Service
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
package org.sing_group.dreimt.service.execution.pipeline.jaccard;

import java.util.stream.Stream;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.GeneOverlapDao;
import org.sing_group.dreimt.domain.entities.execution.jaccard.GeneOverlap;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardResult;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapService;

@Stateless
@PermitAll
public class DefaultGeneOverlapService implements GeneOverlapService {
  
  @Inject
  private GeneOverlapDao geneOverlapDao;

  @Override
  public Stream<GeneOverlap> list(JaccardResult jaccardResult, ListingOptions listingOptions) {
    return geneOverlapDao.list(jaccardResult, listingOptions);
  }
}
