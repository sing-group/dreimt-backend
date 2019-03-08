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
package org.sing_group.dreimt.rest.entity.query.cmap;

public class CmapQueryParameters {
  public static final int DEFAULT_NUM_PERM = 1000;

  private String queryTitle;
  private Integer numPerm = DEFAULT_NUM_PERM;
  private String[] upGenes;
  private String[] downGenes;

  CmapQueryParameters() {}

  public CmapQueryParameters(
    String queryTitle, Integer numPerm, String[] upGenes, String[] downGenes
  ) {
    this.queryTitle = queryTitle;
    this.numPerm = numPerm;
    this.upGenes = upGenes;
    this.downGenes = downGenes;
  }

  public String getQueryTitle() {
    return queryTitle;
  }

  public Integer getNumPerm() {
    return numPerm;
  }

  public String[] getUpGenes() {
    return upGenes;
  }

  public String[] getDownGenes() {
    return downGenes;
  }
}
