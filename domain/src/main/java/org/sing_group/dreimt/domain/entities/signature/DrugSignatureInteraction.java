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

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(
  name = "drug_signature_interaction",
  indexes = {
    @Index(columnList = "tau"),
    @Index(columnList = "upFdr"),
    @Index(columnList = "downFdr")
  }
)
public class DrugSignatureInteraction implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "drugId", referencedColumnName = "id")
  private Drug drug;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "signature", referencedColumnName = "signatureName", nullable = false)
  private Signature signature;
  
  @Enumerated(EnumType.STRING)
  private DrugSignatureInteractionType interactionType;

  private double tau;
  
  @Column(nullable = true)
  private Double upFdr;

  @Column(nullable = true)
  private Double downFdr;

  public DrugSignatureInteraction() {}

  public DrugSignatureInteraction(
    Drug drug, Signature signature, DrugSignatureInteractionType interactionType,
    double tau, Double upFdr, Double downFdr
  ) {
    super();
    this.drug = drug;
    this.signature = signature;
    this.interactionType = interactionType;
    this.tau = tau;
    this.upFdr = upFdr;
    this.downFdr = downFdr;
  }

  public Drug getDrug() {
    return drug;
  }

  public Signature getSignature() {
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
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
    DrugSignatureInteraction other = (DrugSignatureInteraction) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
