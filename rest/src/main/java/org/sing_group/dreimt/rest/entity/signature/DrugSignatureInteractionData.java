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

import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;

public class DrugSignatureInteractionData implements Serializable {
  private static final long serialVersionUID = 1L;

  private DrugData drug;
  private SignatureData signature;

  private DrugSignatureInteractionType interactionType;

  private double tau;
  private Double upFdr;
  private Double downFdr;

  private String predictionSummary;

  DrugSignatureInteractionData() {}

  public DrugSignatureInteractionData(
    DrugData drug, SignatureData signature, DrugSignatureInteractionType interactionType,
    double tau, Double upFdr, Double downFdr,
    String predictionSummary
  ) {
    super();
    this.drug = drug;
    this.signature = signature;
    this.interactionType = interactionType;
    this.tau = tau;
    this.upFdr = upFdr;
    this.downFdr = downFdr;
    this.predictionSummary = predictionSummary;
  }

  public DrugData getDrug() {
    return drug;
  }

  public SignatureData getSignature() {
    return signature;
  }

  public DrugSignatureInteractionType getInteractionType() {
    return interactionType;
  }

  public double getTau() {
    return tau;
  }

  public Double getUpFdr() {
    return upFdr;
  }

  public Double getDownFdr() {
    return downFdr;
  }

  public String getPredictionSummary() {
    return predictionSummary;
  }
}
