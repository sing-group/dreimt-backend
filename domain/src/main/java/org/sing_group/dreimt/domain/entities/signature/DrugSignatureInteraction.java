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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "drug_signature_interaction")
public class DrugSignatureInteraction implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
    @JoinColumn(
      name = "drug_sourceName", referencedColumnName = "sourceName"
    ),
    @JoinColumn(
      name = "drug_sourceDb", referencedColumnName = "sourceDb"
    )
  })
  private Drug drug;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "signature", referencedColumnName = "signatureName", nullable = false)
  private Signature signature;

  private double tes;
  private double pValue;
  private double fdr;

  public DrugSignatureInteraction() {}

  public Drug getDrug() {
    return drug;
  }

  public Signature getSignature() {
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
