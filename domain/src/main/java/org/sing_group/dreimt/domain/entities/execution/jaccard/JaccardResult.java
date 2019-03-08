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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.sing_group.dreimt.domain.entities.execution.WorkEntity;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.Gene;
import org.sing_group.dreimt.domain.entities.signature.Signature;

@Entity
@Table(name = "jaccard_result")
public abstract class JaccardResult extends WorkEntity {
  private static final long serialVersionUID = 1L;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "jaccardResult", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<GeneOverlap> geneOverlapResults;

  private boolean onlyUniverseGenes;

  private String cellTypeA;
  private String cellSubTypeA;
  private String cellTypeB;
  private String cellSubTypeB;
  
  @Enumerated(EnumType.STRING)
  private ExperimentalDesign experimentalDesign;

  private String organism;
  private String disease;
  private String signatureSourceDb;

  @Transient
  private final ReentrantReadWriteLock geneOverlapResultsLock;

  protected JaccardResult() {
    this.geneOverlapResultsLock = new ReentrantReadWriteLock();
  }

  protected JaccardResult(
    String name, String description, Function<String, String> resultReferenceBuilder, boolean onlyUniverseGenes,
    String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB, ExperimentalDesign experimentalDesign,
    String organism, String disease, String signatureSourceDb
  ) {
    super(name, description, resultReferenceBuilder);

    this.onlyUniverseGenes = onlyUniverseGenes;
    this.cellTypeA = cellTypeA;
    this.cellSubTypeA = cellSubTypeA;
    this.cellTypeB = cellTypeB;
    this.cellSubTypeB = cellSubTypeB;
    this.experimentalDesign = experimentalDesign;
    this.organism = organism;
    this.disease = disease;
    this.signatureSourceDb = signatureSourceDb;
    this.geneOverlapResultsLock = new ReentrantReadWriteLock();
  }

  public boolean isOnlyUniverseGenes() {
    return onlyUniverseGenes;
  }
  
  public String getCellTypeA() {
    return cellTypeA;
  }

  public void setCellTypeA(String cellTypeA) {
    this.cellTypeA = cellTypeA;
  }

  public String getCellSubTypeA() {
    return cellSubTypeA;
  }

  public void setCellSubTypeA(String cellSubTypeA) {
    this.cellSubTypeA = cellSubTypeA;
  }

  public String getCellTypeB() {
    return cellTypeB;
  }

  public void setCellTypeB(String cellTypeB) {
    this.cellTypeB = cellTypeB;
  }

  public String getCellSubTypeB() {
    return cellSubTypeB;
  }

  public void setCellSubTypeB(String cellSubTypeB) {
    this.cellSubTypeB = cellSubTypeB;
  }

  public ExperimentalDesign getExperimentalDesign() {
    return experimentalDesign;
  }

  public void setExperimentalDesign(ExperimentalDesign experimentalDesign) {
    this.experimentalDesign = experimentalDesign;
  }

  public String getOrganism() {
    return organism;
  }

  public void setOrganism(String organism) {
    this.organism = organism;
  }

  public String getDisease() {
    return disease;
  }

  public void setDisease(String disease) {
    this.disease = disease;
  }

  public String getSignatureSourceDb() {
    return signatureSourceDb;
  }

  public void setSignatureSourceDb(String signatureSourceDb) {
    this.signatureSourceDb = signatureSourceDb;
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
    Double jaccard, Double pValue, Double fdr
  ) {
    this.geneOverlapResultsLock.writeLock().lock();

    try {
      final GeneOverlap geneOverlap =
        new GeneOverlap(
          this, sourceComparisonType,
          targetSignature, targetComparisonType,
          jaccard, pValue, fdr
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
