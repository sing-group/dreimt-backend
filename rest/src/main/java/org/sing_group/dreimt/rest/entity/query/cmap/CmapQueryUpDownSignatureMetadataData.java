/*-
 * #%L
 * DREIMT - REST
 * %%
 * Copyright (C) 2018 - 2019 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
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
package org.sing_group.dreimt.rest.entity.query.cmap;

public class CmapQueryUpDownSignatureMetadataData {

  private String queryTitle;
  private int numPerm;
  private Integer upGenesCount;
  private Integer downGenesCount;
  private Integer upUniverseGenesCount;
  private Integer downUniverseGenesCount;
  private String caseType;
  private String referenceType;

  public CmapQueryUpDownSignatureMetadataData(
    String queryTitle, int numPerm, Integer upGenesCount, Integer downGenesCount,
    Integer upUniverseGenesCount, Integer downUniverseGenesCount,
    String caseType, String referenceType
  ) {
    this.queryTitle = queryTitle;
    this.numPerm = numPerm;
    this.upGenesCount = upGenesCount;
    this.downGenesCount = downGenesCount;
    this.upUniverseGenesCount = upUniverseGenesCount;
    this.downUniverseGenesCount = downUniverseGenesCount;
    this.caseType = caseType;
    this.referenceType = referenceType;
  }

  public String getQueryTitle() {
    return queryTitle;
  }

  public int getNumPerm() {
    return numPerm;
  }

  public Integer getUpGenesCount() {
    return upGenesCount;
  }

  public Integer getDownGenesCount() {
    return downGenesCount;
  }

  public Integer getUpUniverseGenesCount() {
    return upUniverseGenesCount;
  }

  public Integer getDownUniverseGenesCount() {
    return downUniverseGenesCount;
  }

  public String getCaseType() {
    return caseType;
  }

  public String getReferenceType() {
    return referenceType;
  }
}
