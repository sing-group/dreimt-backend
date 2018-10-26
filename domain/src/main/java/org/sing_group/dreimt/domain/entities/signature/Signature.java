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

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="signatureType")
@Table(name = "signature")
public abstract class Signature implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private String signatureName;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "signature_cell_type_a", 
    joinColumns = @JoinColumn(name = "signatureName"), 
    uniqueConstraints = @UniqueConstraint(columnNames = {"signatureName", "cellType"})
  )
  @Column(name = "cellType", nullable = false)
  private Set<String> cellTypeA;
  
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "signature_cell_type_b", 
    joinColumns = @JoinColumn(name = "signatureName"), 
    uniqueConstraints = @UniqueConstraint(columnNames = {"signatureName", "cellType"})
  )
  @Column(name = "cellType", nullable = false)
  private Set<String> cellTypeB;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_pubmedId", referencedColumnName = "pubmedId", nullable = true)
  private ArticleMetadata articleMetadata;

  private String sourceDb;

  @Enumerated(EnumType.STRING)
  private ExperimentalDesign experimentalDesign;

  private String organism;
  private String disease;

  Signature() {}
  
  public abstract SignatureType getSignatureType();

  public String getSignatureName() {
    return signatureName;
  }

  public Set<String> getCellTypeA() {
    return cellTypeA;
  }

  public Set<String> getCellTypeB() {
    return cellTypeB;
  }

  public ArticleMetadata getArticleMetadata() {
    return articleMetadata;
  }

  public String getSourceDb() {
    return sourceDb;
  }

  public ExperimentalDesign getExperimentalDesign() {
    return experimentalDesign;
  }

  public String getOrganism() {
    return organism;
  }

  public String getDisease() {
    return disease;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((signatureName == null) ? 0 : signatureName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Signature other = (Signature) obj;
    if (signatureName == null) {
      if (other.signatureName != null)
        return false;
    } else if (!signatureName.equals(other.signatureName))
      return false;
    return true;
  }
}
