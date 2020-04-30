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
package org.sing_group.dreimt.rest.entity.query.cmap;

import org.sing_group.dreimt.rest.entity.signature.DrugData;

public class CmapGeneSetSignatureDrugInteractionData {

  private DrugData drugData;
  private Double tau;
  private Double fdr;
  private String drugEffect;

  public CmapGeneSetSignatureDrugInteractionData(
    DrugData drugData, Double tau, Double fdr, String drugEffect
  ) {
    this.drugData = drugData;
    this.tau = tau;
    this.fdr = fdr;
    this.drugEffect = drugEffect;
  }

  public DrugData getDrug() {
    return drugData;
  }

  public Double getTau() {
    return tau;
  }

  public Double getFdr() {
    return fdr;
  }

  public String getDrugEffect() {
    return drugEffect;
  }
}
