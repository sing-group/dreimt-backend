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
package org.sing_group.dreimt.domain.entities.signature;

import static java.util.stream.Collectors.toSet;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

@Entity
@DiscriminatorValue("GENESET")
public class GeneSetSignature extends Signature {
  private static final long serialVersionUID = 1L;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(
    name = "signature_geneset_genes", 
    joinColumns = @JoinColumn(name = "signature", referencedColumnName = "signatureName"),
    inverseJoinColumns = @JoinColumn(name = "gene", referencedColumnName = "gene"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"signature", "gene"})
  )
  private Set<Gene> signatureGenes;

  GeneSetSignature() {}

  public Set<String> getSignatureGenes() {
    return getSignatureGenes(false);
  }
  
  public Set<String> getSignatureGenes(boolean onlyUniverseGenes) {
    return signatureGenes.stream()
      .filter(g -> !onlyUniverseGenes || g.isUniverseGene())
      .map(Gene::getGene)
      .collect(toSet());
  }

  public SignatureType getSignatureType() {
    return SignatureType.GENESET;
  }
}
