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
package org.sing_group.dreimt.domain.dao.signature;

import org.sing_group.dreimt.domain.entities.signature.CellTypeAndSubtype;

public class CustomCellTypeAndSubtype extends CellTypeAndSubtype {

  private String additionalInfo;

  public CustomCellTypeAndSubtype(CellTypeAndSubtype type, String additionalInfo) {
    super(type.getType(), type.getSubType());
    this.additionalInfo = additionalInfo;
  }

  public String getAdditionalInfo() {
    return additionalInfo;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + super.hashCode();
    result = prime * result + ((additionalInfo == null) ? 0 : additionalInfo.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    CustomCellTypeAndSubtype other = (CustomCellTypeAndSubtype) obj;
    if (!super.equals(other))
      return false;
    if (additionalInfo == null) {
      if (other.additionalInfo != null)
        return false;
    } else if (!additionalInfo.equals(other.additionalInfo))
      return false;
    return true;
  }
}
