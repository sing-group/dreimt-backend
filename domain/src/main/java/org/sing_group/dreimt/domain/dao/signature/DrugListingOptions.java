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

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.Optional;

import org.sing_group.dreimt.domain.entities.signature.DrugStatus;

public class DrugListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private final String commonName;
  private final String moa;
  private final DrugStatus status;
  private final Double minDss;

  public DrugListingOptions(String commonName, String moa, DrugStatus status, Double minDss) {
    this.commonName = commonName;
    this.moa = moa;
    this.status = status;
    this.minDss = minDss;
  }

  public boolean hasAnyQueryModification() {
    return this.commonName != null
      || this.moa != null
      || this.status != null
      || this.minDss != null;
  }

  public Optional<String> getCommonName() {
    return ofNullable(commonName);
  }

  public Optional<String> getMoa() {
    return ofNullable(moa);
  }

  public Optional<DrugStatus> getStatus() {
    return ofNullable(status);
  }

  public Optional<Double> getMinDss() {
    return ofNullable(minDss);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((commonName == null) ? 0 : commonName.hashCode());
    result = prime * result + ((moa == null) ? 0 : moa.hashCode());
    result = prime * result + ((status == null) ? 0 : status.hashCode());
    result = prime * result + ((minDss == null) ? 0 : minDss.hashCode());
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
    DrugListingOptions other = (DrugListingOptions) obj;
    if (commonName == null) {
      if (other.commonName != null)
        return false;
    } else if (!commonName.equals(other.commonName))
      return false;
    if (moa == null) {
      if (other.moa != null)
        return false;
    } else if (!moa.equals(other.moa))
      return false;
    if (status != other.status)
      return false;
    if (minDss == null) {
      if (other.minDss != null)
        return false;
    } else if (!minDss.equals(other.minDss))
      return false;
    return true;
  }
}
