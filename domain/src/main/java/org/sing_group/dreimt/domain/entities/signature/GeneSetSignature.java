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

import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.UniqueConstraint;

@Entity
@DiscriminatorValue("GENESET")
public class GeneSetSignature extends Signature {
  private static final long serialVersionUID = 1L;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "signature_geneset_genes", 
    joinColumns = @JoinColumn(name = "signature", referencedColumnName = "signatureName"), 
    uniqueConstraints = @UniqueConstraint(columnNames = {"signature", "gene"})
  )
  @Column(name = "gene", nullable = false)
  private Set<String> signatureGenes;

  GeneSetSignature() {}

  public Set<String> getSignatureGenes() {
    return signatureGenes;
  }
  
  public SignatureType getSignatureType() {
    return SignatureType.GENESET;
  }
}
