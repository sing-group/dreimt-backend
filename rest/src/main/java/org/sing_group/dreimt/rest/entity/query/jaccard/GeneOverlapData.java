/*-
 * #%L
 * DREIMT - REST
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
package org.sing_group.dreimt.rest.entity.query.jaccard;

import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardComparisonType;
import org.sing_group.dreimt.rest.entity.signature.SignatureDataSummary;

public class GeneOverlapData {

  private JaccardComparisonType sourceComparisonType;
  private SignatureDataSummary targetSignatureData;
  private JaccardComparisonType targetComparisonType;
  private Double jaccard;
  private Double pValue;
  private Double fdr;

  public GeneOverlapData(
    JaccardComparisonType sourceComparisonType, SignatureDataSummary targetSignatureData,
    JaccardComparisonType targetComparisonType, Double jaccard, Double pValue, Double fdr
  ) {
    this.sourceComparisonType = sourceComparisonType;
    this.targetSignatureData = targetSignatureData;
    this.targetComparisonType = targetComparisonType;
    this.jaccard = jaccard;
    this.pValue = pValue;
    this.fdr = fdr;
  }

  public JaccardComparisonType getSourceComparisonType() {
    return sourceComparisonType;
  }

  public SignatureDataSummary getTargetSignatureData() {
    return targetSignatureData;
  }

  public JaccardComparisonType getTargetComparisonType() {
    return targetComparisonType;
  }

  public Double getJaccard() {
    return jaccard;
  }

  public Double getPvalue() {
    return pValue;
  }

  public Double getFdr() {
    return fdr;
  }
}
