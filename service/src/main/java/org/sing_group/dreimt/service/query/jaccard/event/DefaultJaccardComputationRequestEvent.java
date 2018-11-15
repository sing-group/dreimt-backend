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
package org.sing_group.dreimt.service.query.jaccard.event;

import java.io.Serializable;

import org.sing_group.dreimt.domain.entities.query.SignatureListingOptions;
import org.sing_group.dreimt.service.spi.query.jaccard.event.JaccardComputationRequestEvent;

public class DefaultJaccardComputationRequestEvent implements JaccardComputationRequestEvent, Serializable {
  private static final long serialVersionUID = 1L;

  private String workId;
  private SignatureListingOptions signatureListingOptions;

  public DefaultJaccardComputationRequestEvent(String workId, SignatureListingOptions signatureListingOptions) {
    this.workId = workId;
    this.signatureListingOptions = signatureListingOptions;
  }

  @Override
  public String getWorkId() {
    return workId;
  }

  @Override
  public SignatureListingOptions getSignatureListingOptions() {
    return this.signatureListingOptions;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((signatureListingOptions == null) ? 0 : signatureListingOptions.hashCode());
    result = prime * result + ((workId == null) ? 0 : workId.hashCode());
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
    DefaultJaccardComputationRequestEvent other = (DefaultJaccardComputationRequestEvent) obj;
    if (signatureListingOptions == null) {
      if (other.signatureListingOptions != null)
        return false;
    } else if (!signatureListingOptions.equals(other.signatureListingOptions))
      return false;
    if (workId == null) {
      if (other.workId != null)
        return false;
    } else if (!workId.equals(other.workId))
      return false;
    return true;
  }
}
