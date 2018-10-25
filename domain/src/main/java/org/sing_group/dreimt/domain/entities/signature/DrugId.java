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

import javax.persistence.Embeddable;

@Embeddable
public class DrugId implements Serializable {
  private static final long serialVersionUID = 1L;

  private String sourceName;
  private String sourceDb;

  public DrugId() {}

  public DrugId(String sourceName, String sourceDb) {
    this.sourceName = sourceName;
    this.sourceDb = sourceDb;
  }

  public String getSourceName() {
    return sourceName;
  }

  public String getSourceDb() {
    return sourceDb;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((sourceDb == null) ? 0 : sourceDb.hashCode());
    result = prime * result + ((sourceName == null) ? 0 : sourceName.hashCode());
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
    DrugId other = (DrugId) obj;
    if (sourceDb == null) {
      if (other.sourceDb != null)
        return false;
    } else if (!sourceDb.equals(other.sourceDb))
      return false;
    if (sourceName == null) {
      if (other.sourceName != null)
        return false;
    } else if (!sourceName.equals(other.sourceName))
      return false;
    return true;
  }
}
