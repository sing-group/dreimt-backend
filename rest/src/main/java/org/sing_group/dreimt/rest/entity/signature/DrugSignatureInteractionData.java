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

public class DrugSignatureInteractionData implements Serializable {
  private static final long serialVersionUID = 1L;

  private DrugData drug;
  private SignatureData signature;

  private double tes;
  private double pValue;
  private double fdr;

  DrugSignatureInteractionData() {}

  public DrugSignatureInteractionData(
    DrugData drug, SignatureData signature, double tes, double pValue, double fdr
  ) {
    super();
    this.drug = drug;
    this.signature = signature;
    this.tes = tes;
    this.pValue = pValue;
    this.fdr = fdr;
  }

  public DrugData getDrug() {
    return drug;
  }

  public SignatureData getSignature() {
    return signature;
  }

  public double getTes() {
    return tes;
  }

  public double getpValue() {
    return pValue;
  }

  public double getFdr() {
    return fdr;
  }
}
