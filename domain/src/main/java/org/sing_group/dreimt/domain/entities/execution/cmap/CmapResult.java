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
package org.sing_group.dreimt.domain.entities.execution.cmap;

import static java.util.stream.Collectors.toSet;

import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.sing_group.dreimt.domain.entities.execution.WorkEntity;
import org.sing_group.dreimt.domain.entities.signature.Gene;

@Entity
@Table(name = "cmap_result")
public abstract class CmapResult extends WorkEntity {
  private static final long serialVersionUID = 1L;

  private int numPerm;
  private String caseType;
  private String referenceType;

  @Transient
  protected final ReentrantReadWriteLock cmapResultsLock;

  protected CmapResult() {
    this.cmapResultsLock = new ReentrantReadWriteLock();
  }

  protected CmapResult(
    String name, String description,
    Function<String, String> resultReferenceBuilder, int numPerm,
    String caseType, String referenceType
  ) {
    super(name, description, resultReferenceBuilder);

    this.numPerm = numPerm;
    this.caseType = caseType;
    this.referenceType = referenceType;
    this.cmapResultsLock = new ReentrantReadWriteLock();
  }

  public int getNumPerm() {
    return numPerm;
  }

  public String getCaseType() {
    return caseType;
  }

  public String getReferenceType() {
    return referenceType;
  }

  protected static Set<String> getGenes(Set<Gene> genes, boolean onlyUniverseGenes) {
    return genes.stream()
      .filter((g -> !onlyUniverseGenes || g.isUniverseGene()))
      .map(Gene::getGene)
      .collect(toSet());
  }
}
