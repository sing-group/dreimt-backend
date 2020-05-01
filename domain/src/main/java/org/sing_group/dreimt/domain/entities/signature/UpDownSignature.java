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
import javax.persistence.OneToMany;

import org.sing_group.dreimt.domain.entities.signature.UpDownSignatureGene.Type;

@Entity
@DiscriminatorValue("UPDOWN")
public class UpDownSignature extends Signature {
  private static final long serialVersionUID = 1L;

  @OneToMany(mappedBy = "signature", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<UpDownSignatureGene> signatureGenes;

  UpDownSignature() {}
  
  public UpDownSignature(
    String signatureName, Set<String> cellTypeA, Set<String> cellSubTypeA, Set<String> cellTypeB,
    Set<String> cellSubTypeB, String sourceDb, String sourceDbUrl, ExperimentalDesign experimentalDesign,
    String organism, Set<String> disease, Set<String> treatmentA, Set<String> treatmentB, Set<String> diseaseA,
    Set<String> diseaseB, String localisationA, String localisationB, String stateA, String stateB
  ) {
    super(
      signatureName, cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB, sourceDb, sourceDbUrl, experimentalDesign,
      organism, disease, treatmentA, treatmentB, diseaseA, diseaseB, localisationA, localisationB, stateA, stateB
    );
  }

  public UpDownSignature(
    String signatureName, Set<String> cellTypeA, Set<String> cellSubTypeA, Set<String> cellTypeB,
    Set<String> cellSubTypeB, ArticleMetadata articleMetadata, String sourceDb, String sourceDbUrl,
    ExperimentalDesign experimentalDesign, String organism, Set<String> disease, Set<String> treatmentA,
    Set<String> treatmentB, Set<String> diseaseA, Set<String> diseaseB, String localisationA, String localisationB,
    String stateA, String stateB
  ) {
    super(
      signatureName, cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB, articleMetadata, sourceDb, sourceDbUrl,
      experimentalDesign, organism, disease, treatmentA, treatmentB, diseaseA, diseaseB, localisationA, localisationB,
      stateA, stateB
    );
  }

  public Set<String> getUpGenes() {
    return getUpGenes(false);
  }

  public Set<String> getUpGenes(boolean onlyUniverseGenes) {
    return getGenes(Type.UP, onlyUniverseGenes);
  }

  public Set<String> getDownGenes() {
    return getDownGenes(false);
  }

  public Set<String> getDownGenes(boolean onlyUniverseGenes) {
    return getGenes(Type.DOWN, onlyUniverseGenes);
  }

  private Set<String> getGenes(Type geneType, boolean onlyUniverseGenes) {
    return signatureGenes.stream()
      .filter(g -> g.getType().equals(geneType) && (!onlyUniverseGenes || g.isUniverseGene()))
      .map(UpDownSignatureGene::getGene)
      .collect(toSet());
  }

  public SignatureType getSignatureType() {
    return SignatureType.UPDOWN;
  }
}
