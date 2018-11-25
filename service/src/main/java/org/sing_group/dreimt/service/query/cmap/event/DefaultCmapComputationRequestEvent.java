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
package org.sing_group.dreimt.service.query.cmap.event;

import static java.util.Collections.emptySet;

import java.io.Serializable;
import java.util.Set;

import org.sing_group.dreimt.service.spi.query.cmap.CmapServiceConfiguration;
import org.sing_group.dreimt.service.spi.query.cmap.event.CmapComputationRequestEvent;

public class DefaultCmapComputationRequestEvent implements CmapComputationRequestEvent, Serializable {
  private static final long serialVersionUID = 1L;

  private String workId;
  private CmapServiceConfiguration cmapServiceConfiguration;
  private Set<String> upGenes;
  private Set<String> downGenes;

  public DefaultCmapComputationRequestEvent(
    String workId, CmapServiceConfiguration cmapServiceConfiguration, Set<String> upGenes
  ) {
    this(workId, cmapServiceConfiguration, upGenes, emptySet());
  }

  public DefaultCmapComputationRequestEvent(
    String workId, CmapServiceConfiguration cmapServiceConfiguration, Set<String> upGenes, Set<String> downGenes
  ) {
    this.workId = workId;
    this.cmapServiceConfiguration = cmapServiceConfiguration;
    this.upGenes = upGenes;
    this.downGenes = downGenes;
  }

  @Override
  public String getWorkId() {
    return workId;
  }

  @Override
  public CmapServiceConfiguration getCmapServiceConfiguration() {
    return this.cmapServiceConfiguration;
  }
  
  @Override
  public Set<String> getUpGenes() {
    return this.upGenes;
  }
  
  @Override
  public Set<String> getDownGenes() {
    return this.downGenes;
  }
  

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
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
    DefaultCmapComputationRequestEvent other = (DefaultCmapComputationRequestEvent) obj;
    if (workId == null) {
      if (other.workId != null)
        return false;
    } else if (!workId.equals(other.workId))
      return false;
    return true;
  }
}
