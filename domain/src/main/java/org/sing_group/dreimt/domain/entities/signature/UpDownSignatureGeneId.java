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

public class UpDownSignatureGeneId implements Serializable {
  private static final long serialVersionUID = 1L;

  private String signature;
  private String gene;

  public UpDownSignatureGeneId() {}

  public UpDownSignatureGeneId(String signature, String gene) {
    this.signature = signature;
    this.gene = gene;
  }

  public String getSignature() {
    return signature;
  }

  public String getGene() {
    return gene;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((gene == null) ? 0 : gene.hashCode());
    result = prime * result + ((signature == null) ? 0 : signature.hashCode());
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
    UpDownSignatureGeneId other = (UpDownSignatureGeneId) obj;
    if (gene == null) {
      if (other.gene != null)
        return false;
    } else if (!gene.equals(other.gene))
      return false;
    if (signature == null) {
      if (other.signature != null)
        return false;
    } else if (!signature.equals(other.signature))
      return false;
    return true;
  }
}
