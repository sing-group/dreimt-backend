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
package org.sing_group.dreimt.service.query;

import static org.sing_group.dreimt.service.util.Sets.intersection;

import java.util.Set;

import org.sing_group.dreimt.service.spi.query.GeneListsValidationService;

public class DefaultGeneListsValidationService implements GeneListsValidationService {
  
  @Override
  public void validateGeneListsSizes(
    Set<String> upGenes, 
    Set<String> downGenes, 
    int minimumGeneSetSize, 
    int maximumGeneSetSize
  ) {
    if (upGenes.isEmpty()) {
      throw new IllegalArgumentException("Up (or geneset) genes list is always required.");
    } else if (!this.isValidGeneSet(upGenes, minimumGeneSetSize, maximumGeneSetSize)) {
      throw new IllegalArgumentException(
        "Invalid up (or geneset) genes list size. It must have at least " + minimumGeneSetSize
          + " and at most " + maximumGeneSetSize + " genes."
        );
    }

    if (!downGenes.isEmpty() && !this.isValidGeneSet(downGenes, minimumGeneSetSize, maximumGeneSetSize)) {
      throw new IllegalArgumentException(
        "Invalid down genes list size. It must have at least " + minimumGeneSetSize
          + " and at most " + maximumGeneSetSize + " genes."
        );
    }
    
    if (!downGenes.isEmpty() && intersection(upGenes, downGenes).size() > 0) {
      throw new IllegalArgumentException("Up and down gene lists cannot have genes in common");
    }
  }

  @Override
  public boolean isValidGeneSet(Set<String> genes, int minimumGeneSetSize, int maximumGeneSetSize) {
    return genes.size() >= minimumGeneSetSize && genes.size() <= maximumGeneSetSize;
  }
}
