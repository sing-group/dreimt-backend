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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.sing_group.dreimt.domain.entities.signature.Signature;

@Entity
@Table(
  name = "jaccard_result_gene_overlap",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {
      "jaccardResultId", "targetSignature", 
      "targetComparisonType", "sourceComparisonType"
    })
  }
)
public class GeneOverlap implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
    name = "jaccardResultId", 
    referencedColumnName = "id", 
    nullable = false, 
    foreignKey = @ForeignKey(name = "FK_jaccard_result_jaccard_result_gene_overlap")
  )
  private JaccardResult jaccardResult;

  @Column(nullable = true)
  private Double jaccard;

  @Column(nullable = true)
  private Double pValue;

  @Column(nullable = true)
  private Double fdr;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "targetSignature", referencedColumnName = "signatureName", nullable = false)
  private Signature targetSignature;

  @Enumerated(EnumType.STRING)
  private JaccardComparisonType targetComparisonType;

  @Enumerated(EnumType.STRING)
  private JaccardComparisonType sourceComparisonType;

  GeneOverlap() {}

  public GeneOverlap(
    JaccardResult jaccardResult,
    JaccardComparisonType sourceComparisonType,
    Signature targetSignature,
    JaccardComparisonType targetComparisonType,
    Double jaccard, Double pValue
  ) {
    this.jaccardResult = jaccardResult;
    this.sourceComparisonType = sourceComparisonType;
    this.targetSignature = targetSignature;
    this.targetComparisonType = targetComparisonType;
    this.jaccard = db(jaccard);
    this.pValue = db(pValue);
  }

  private static Double db(Double value) {
    return value.isNaN() ? null : value;
  }

  public Double getJaccard() {
    return jaccard;
  }

  public Double getpValue() {
    return pValue;
  }

  public Double getFdr() {
    return fdr;
  }

  public void setFdr(Double fdr) {
    this.fdr = db(fdr);
  }

  public Signature getTargetSignature() {
    return targetSignature;
  }

  public JaccardComparisonType getTargetComparisonType() {
    return targetComparisonType;
  }

  public JaccardComparisonType getSourceComparisonType() {
    return sourceComparisonType;
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
    GeneOverlap other = (GeneOverlap) obj;
    if (id != other.id)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append("GeneOverlap ID = ")
        .append(this.id)
        .append(" [")
        .append("Jaccard Result ID = ")
        .append(this.jaccardResult.getId())
        .append("; Source comp. = ")
        .append(this.sourceComparisonType)
        .append("; Target signature = ")
        .append(this.targetSignature.getSignatureName())
        .append("; Target comp. = ")
        .append(this.targetComparisonType)
        .append("]")
      .toString();
  }
}
