/*-
 * #%L
 * DREIMT - Domain
 * %%
 * Copyright (C) 2018 - 2020 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
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

import javax.persistence.Embeddable;

@Embeddable
public class DrugTargetGene implements Serializable {
  private static final long serialVersionUID = 1L;

  public String geneName;
  public String geneId;

  public DrugTargetGene() {}

  public DrugTargetGene(String geneName, String geneId) {
    this.geneName = geneName;
    this.geneId = geneId;
  }

  public String getGeneId() {
    return geneId;
  }

  public String getGeneName() {
    return geneName;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((geneId == null) ? 0 : geneId.hashCode());
    result = prime * result + ((geneName == null) ? 0 : geneName.hashCode());
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
    DrugTargetGene other = (DrugTargetGene) obj;
    if (geneId == null) {
      if (other.geneId != null)
        return false;
    } else if (!geneId.equals(other.geneId))
      return false;
    if (geneName == null) {
      if (other.geneName != null)
        return false;
    } else if (!geneName.equals(other.geneName))
      return false;
    return true;
  }
}
