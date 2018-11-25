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
package org.sing_group.dreimt.service.execution.pipeline.cmap;

import java.util.Set;

import org.sing_group.dreimt.service.spi.execution.pipeline.cmap.CmapPipelineConfiguration;
import org.sing_group.dreimt.service.spi.query.cmap.CmapServiceConfiguration;

public class DefaultCmapPipelineConfiguration implements CmapPipelineConfiguration {

  private String workId;
  private String resultId;
  private CmapServiceConfiguration cmapServiceConfiguration;
  private Set<String> upGenes;
  private Set<String> downGenes;

  public DefaultCmapPipelineConfiguration(
    String workId, String resultId,
    CmapServiceConfiguration cmapServiceConfiguration,
    Set<String> upGenes,
    Set<String> downGenes
  ) {
    this.workId = workId;
    this.resultId = resultId;
    this.cmapServiceConfiguration = cmapServiceConfiguration;
    this.upGenes = upGenes;
    this.downGenes = downGenes;
  }

  @Override
  public String getWorkId() {
    return this.workId;
  }

  @Override
  public String getResultId() {
    return this.resultId;
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
}
