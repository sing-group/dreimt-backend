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
package org.sing_group.dreimt.rest.entity.signature;

import java.io.Serializable;
import java.util.Set;

import org.sing_group.dreimt.domain.entities.signature.DrugStatus;

public class DrugData implements Serializable {
  private static final long serialVersionUID = 1L;

  private String commonName;
  private String sourceName;
  private String sourceDb;
  private DrugStatus status;
  private Set<String> moa;
  private Set<String> targetGenes;

  DrugData() {}

  public DrugData(String commonName, String sourceName, String sourceDb, DrugStatus status, Set<String> moa, Set<String> targetGenes) {
    super();
    this.commonName = commonName;
    this.sourceName = sourceName;
    this.sourceDb = sourceDb;
    this.status = status;
    this.moa = moa;
    this.targetGenes = targetGenes;
  }

  public String getCommonName() {
    return commonName;
  }

  public String getSourceName() {
    return sourceName;
  }

  public String getSourceDb() {
    return sourceDb;
  }

  public DrugStatus getStatus() {
    return status;
  }

  public Set<String> getMoa() {
    return moa;
  }

  public Set<String> getTargetGenes() {
    return targetGenes;
  }
}
