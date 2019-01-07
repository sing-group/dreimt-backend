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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.Gene;

@Entity
@DiscriminatorValue("JACCARD_GENESET")
@Table(name = "jaccard_result_geneset")
public class JaccardGeneSetSignatureResult extends JaccardResult implements Serializable {
  private static final long serialVersionUID = 1L;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
    name = "jaccard_result_geneset_genes", 
    joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"), 
    inverseJoinColumns = @JoinColumn(name = "gene", referencedColumnName = "gene"), 
    uniqueConstraints = @UniqueConstraint(columnNames = {"id", "gene"})
  )
  private Set<Gene> genes;

  protected JaccardGeneSetSignatureResult() {
    super();
    this.genes = new HashSet<>();
  }

  public JaccardGeneSetSignatureResult(
    String name, String description, Function<String, String> resultReferenceBuilder, boolean onlyUniverseGenes,
    String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB, ExperimentalDesign experimentalDesign,
    String organism, String disease, String signatureSourceDb, Set<Gene> genes
  ) {
    super(
      name, description, resultReferenceBuilder, onlyUniverseGenes, cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB,
      experimentalDesign, organism, disease, signatureSourceDb
    );

    this.genes = genes;
  }

  public Set<String> getGenes() {
    return getGenes(false);
  }

  public Set<String> getGenes(boolean onlyUniverseGenes) {
    return getGenes(genes, onlyUniverseGenes);
  }
}
