/*-
 * #%L
 * DREIMT - Domain
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
package org.sing_group.dreimt.domain.entities.execution.jaccard;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.sing_group.dreimt.domain.entities.execution.WorkEntity;
import org.sing_group.dreimt.domain.entities.signature.Gene;
import org.sing_group.dreimt.domain.entities.signature.Signature;

@Entity
@Table(name = "jaccard_result")
public abstract class JaccardResult extends WorkEntity {
  private static final long serialVersionUID = 1L;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "jaccardResult", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GeneOverlap> geneOverlapResults;

  @Transient
  private final ReentrantReadWriteLock geneOverlapResultsLock;

  protected JaccardResult() {
    this.geneOverlapResultsLock = new ReentrantReadWriteLock();
  }

  protected JaccardResult(String name, String description, Function<String, String> resultReferenceBuilder) {
    super(name, description, resultReferenceBuilder);
    this.geneOverlapResultsLock = new ReentrantReadWriteLock();
  }

  public Stream<GeneOverlap> getGeneOverlapResults() {
    this.geneOverlapResultsLock.readLock().lock();
    try {
      return geneOverlapResults.stream();
    } finally {
      this.geneOverlapResultsLock.readLock().unlock();
    }
  }

  public GeneOverlap addGeneOverlapResult(
    JaccardComparisonType sourceComparisonType, Signature targetSignature,
    JaccardComparisonType targetComparisonType,
    Double jaccard, Double pValue
  ) {
    this.geneOverlapResultsLock.writeLock().lock();

    try {
      final GeneOverlap geneOverlap =
        new GeneOverlap(
          this, sourceComparisonType,
          targetSignature, targetComparisonType,
          jaccard, pValue
        );
      this.geneOverlapResults.add(geneOverlap);

      return geneOverlap;
    } finally {
      this.geneOverlapResultsLock.writeLock().unlock();
    }
  }

  protected static Set<String> getGenes(Set<Gene> genes, boolean onlyUniverseGenes) {
    return genes.stream()
      .filter((g -> !onlyUniverseGenes || g.isUniverseGene()))
      .map(Gene::getGene)
      .collect(toSet());
  }
}
