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

import static java.util.stream.Collectors.toSet;
import static org.sing_group.dreimt.service.util.Sets.intersection;

import java.util.Set;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.dao.spi.signature.GeneDao;
import org.sing_group.dreimt.domain.entities.signature.Gene;
import org.sing_group.dreimt.service.spi.query.GeneListsValidationService;

@Default
public class DefaultGeneListsValidationService implements GeneListsValidationService {

  @Inject
  private GeneDao geneDao;

  @Override
  public void validateGeneListsSizes(
    Set<String> upGenes,
    Set<String> downGenes,
    boolean onlyUniverseGenes,
    int minimumGeneSetSize,
    int maximumGeneSetSize
  ) {
    Set<String> processedUpGenes = processGenes(upGenes, onlyUniverseGenes);
    Set<String> processedDownGenes = processGenes(downGenes, onlyUniverseGenes);

    if (upGenes.isEmpty() && downGenes.isEmpty()) {
      throw new IllegalArgumentException("Both genes lists can't be empty. At least one gene list must be provided.");
    }

    if (!processedUpGenes.isEmpty() && !this.isSetSizeInRange(processedUpGenes, minimumGeneSetSize, maximumGeneSetSize)) {
      throw new IllegalArgumentException(
        "Invalid up (or geneset) genes list size. It must have at least " + minimumGeneSetSize
          + " and at most " + maximumGeneSetSize + " genes." + onlyUniverseGenesWarning(onlyUniverseGenes)
      );
    }

    if (!processedDownGenes.isEmpty() && !this.isSetSizeInRange(processedDownGenes, minimumGeneSetSize, maximumGeneSetSize)) {
      throw new IllegalArgumentException(
        "Invalid down genes list size. It must have at least " + minimumGeneSetSize
          + " and at most " + maximumGeneSetSize + " genes." + onlyUniverseGenesWarning(onlyUniverseGenes)
      );
    }

    if (intersection(processedUpGenes, processedDownGenes).size() > 0) {
      throw new IllegalArgumentException("Up and down gene lists cannot have genes in common");
    }
  }

  @Override
  public boolean isValidGeneSet(
    Set<String> genes,
    boolean onlyUniverseGenes,
    int minimumGeneSetSize,
    int maximumGeneSetSize
  ) {
    return isSetSizeInRange(processGenes(genes, onlyUniverseGenes), minimumGeneSetSize, maximumGeneSetSize);
  }

  private boolean isSetSizeInRange(Set<String> processedGenes, int minimumGeneSetSize, int maximumGeneSetSize) {
    return processedGenes.size() >= minimumGeneSetSize && processedGenes.size() <= maximumGeneSetSize;
  }

  private Set<String> processGenes(Set<String> genes, boolean onlyUniverseGenes) {
    if (onlyUniverseGenes) {
      return geneDao.getGenes(genes, false).stream().filter(g -> g.isUniverseGene()).map(Gene::getGene).collect(toSet());
    } else {
      return genes;
    }
  }

  private String onlyUniverseGenesWarning(boolean onlyUniverseGenes) {
    return onlyUniverseGenes ? " Note that genes must be in the DREIMT genes." : "";
  }
}
