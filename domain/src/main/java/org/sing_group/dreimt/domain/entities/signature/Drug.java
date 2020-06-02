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
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "drug")
public class Drug implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private int id;
  
  private String commonName;
  private String sourceName;
  private String sourceDb;

  @Enumerated(EnumType.ORDINAL) 
  private DrugStatus status;
  
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "drug_moa", 
    joinColumns = @JoinColumn(name = "id"), 
    uniqueConstraints = @UniqueConstraint(columnNames = {"id", "moa"})
  )
  private Set<String> moa;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "drug_target_genes",
    joinColumns = @JoinColumn(name = "id"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"id", "targetGene"})
  )
  private Set<String> targetGene;

  @Column(nullable = true)
  private Double dss;

  Drug() {}

  public Drug(
    String commonName, String sourceName, String sourceDb, DrugStatus status, Set<String> moa, Set<String> genes, Double dss
  ) {
    this.commonName = commonName;
    this.sourceName = sourceName;
    this.sourceDb = sourceDb;
    this.status = status;
    this.moa = moa;
    this.targetGene = genes;
    this.dss = dss;
  }

  public String getCommonName() {
    return commonName;
  }

  public String getSourceName() {
    return sourceName;
  }

  public String getSourceDb() {
    return sourceDb;
  }

  public DrugStatus getStatus() {
    return status;
  }

  public Set<String> getMoa() {
    return moa;
  }

  public Set<String> getTargetGenes() {
    return targetGene;
  }

  public Double getDss() {
    return dss;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
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
    Drug other = (Drug) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
