/*-
 * #%L
 * DREIMT - Service
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
package org.sing_group.dreimt.service.execution.pipeline.jaccard;

import org.sing_group.dreimt.domain.entities.execution.jaccard.GeneOverlap;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardComparisonType;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapData;

public class DefaultGeneOverlapData implements GeneOverlapData {

  private JaccardComparisonType sourceComparisonType;
  private String targetSignatureName;
  private JaccardComparisonType targetComparisonType;
  private Double jaccard;
  private Double pValue;
  private Double fdr;

  public DefaultGeneOverlapData(
    JaccardComparisonType sourceComparisonType, String targetSignatureName,
    JaccardComparisonType targetComparisonType, Double jaccard, Double pValue
  ) {
    this(sourceComparisonType, targetSignatureName, targetComparisonType, jaccard, pValue, null);
  }

  public DefaultGeneOverlapData(
    JaccardComparisonType sourceComparisonType, String targetSignatureName,
    JaccardComparisonType targetComparisonType, Double jaccard, Double pValue, Double fdr
  ) {
    this.sourceComparisonType = sourceComparisonType;
    this.targetSignatureName = targetSignatureName;
    this.targetComparisonType = targetComparisonType;
    this.jaccard = jaccard;
    this.pValue = pValue;
    this.fdr = fdr;
  }

  public DefaultGeneOverlapData(GeneOverlap geneOverlapEntity) {
    this(
      geneOverlapEntity.getSourceComparisonType(),
      geneOverlapEntity.getTargetSignature().getSignatureName(),
      geneOverlapEntity.getTargetComparisonType(),
      geneOverlapEntity.getJaccard(),
      geneOverlapEntity.getpValue()
    );
  }

  @Override
  public Double getJaccard() {
    return this.jaccard;
  }

  @Override
  public Double getPvalue() {
    return this.pValue;
  }

  @Override
  public Double getFdr() {
    return this.fdr;
  }

  @Override
  public String getTargetSignatureName() {
    return this.targetSignatureName;
  }

  @Override
  public JaccardComparisonType getTargetComparisonType() {
    return this.targetComparisonType;
  }

  @Override
  public JaccardComparisonType getSourceComparisonType() {
    return this.sourceComparisonType;
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((sourceComparisonType == null) ? 0 : sourceComparisonType.hashCode());
    result = prime * result + ((targetComparisonType == null) ? 0 : targetComparisonType.hashCode());
    result = prime * result + ((targetSignatureName == null) ? 0 : targetSignatureName.hashCode());
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
    DefaultGeneOverlapData other = (DefaultGeneOverlapData) obj;
    if (sourceComparisonType != other.sourceComparisonType)
      return false;
    if (targetComparisonType != other.targetComparisonType)
      return false;
    if (targetSignatureName == null) {
      if (other.targetSignatureName != null)
        return false;
    } else if (!targetSignatureName.equals(other.targetSignatureName))
      return false;
    return true;
  }
}
