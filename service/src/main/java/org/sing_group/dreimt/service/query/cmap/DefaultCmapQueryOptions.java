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
package org.sing_group.dreimt.service.query.cmap;

import static java.util.Optional.ofNullable;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.sing_group.dreimt.service.spi.query.cmap.CmapQueryOptions;

public class DefaultCmapQueryOptions implements CmapQueryOptions {

  private String title;
  private Set<String> upGenes;
  private Set<String> downGenes;
  private Function<String, String> resultUriBuilder;
  private Integer numPerm;
  private String caseType;
  private String referenceType;

  public DefaultCmapQueryOptions(
    String title, Set<String> upGenes, Set<String> downGenes, Function<String, String> resultUriBuilder,
    Integer numPerm, String caseType, String referenceType
  ) {
    this.title = title;
    this.upGenes = upGenes;
    this.downGenes = downGenes;
    this.resultUriBuilder = resultUriBuilder;
    this.numPerm = numPerm;
    this.caseType = caseType;
    this.referenceType = referenceType;
  }

  @Override
  public Optional<String> getTitle() {
    return ofNullable(this.title);
  }

  @Override
  public Set<String> getUpGenes() {
    return upGenes;
  }

  @Override
  public Set<String> getDownGenes() {
    return downGenes;
  }

  @Override
  public Function<String, String> getResultUriBuilder() {
    return resultUriBuilder;
  }

  @Override
  public Integer getNumPerm() {
    return numPerm;
  }

  @Override
  public String getCaseType() {
    return this.caseType;
  }

  @Override
  public Optional<String> getReferenceType() {
    return ofNullable(this.referenceType);
  }
}
