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
package org.sing_group.dreimt.domain.entities.execution.cmap;

import static org.sing_group.dreimt.domain.entities.signature.DrugInteractionEffect.computeEffect;
import static org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType.SIGNATURE_DOWN;
import static org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType.SIGNATURE_UP;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.domain.entities.signature.DrugInteractionEffect;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;
import org.sing_group.dreimt.domain.entities.signature.GeneSetType;

@Entity
@Table(
  name = "cmap_result_geneset_drug_interactions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {
      "cmapResultId", "drugId"
    })
  }
)
public class CmapDrugGeneSetSignatureInteraction implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "drugId", referencedColumnName = "id", nullable = false)
  private Drug drug;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(
    name = "cmapResultId", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(
      name = "FK_cmap_result_cmap_result_geneset_drug_interactions"
    )
  )
  private CmapResult cmapResult;

  private double tau;
  private double fdr;

  CmapDrugGeneSetSignatureInteraction() {}

  public CmapDrugGeneSetSignatureInteraction(
    CmapResult cmapResult,
    Drug drug,
    double tau,
    double fdr
  ) {
    this.cmapResult = cmapResult;
    this.drug = drug;
    this.tau = tau;
    this.fdr = fdr;
  }

  public Drug getDrug() {
    return drug;
  }

  public double getTau() {
    return tau;
  }

  public double getFdr() {
    return fdr;
  }

  public DrugInteractionEffect getEffect() {
    DrugSignatureInteractionType interactionType =
      ((CmapGeneSetSignatureResult) this.cmapResult).getGeneSetType().equals(GeneSetType.UP) ? SIGNATURE_UP : SIGNATURE_DOWN;

    return computeEffect(tau, interactionType);
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
    CmapDrugGeneSetSignatureInteraction other = (CmapDrugGeneSetSignatureInteraction) obj;
    if (id != other.id)
      return false;
    return true;
  }

  @Override
  public String toString() {
    return new StringBuilder()
      .append("CmapDrugGenesetInteraction ID = ")
      .append(this.id)
      .append(" [")
      .append("Cmap Result ID = ")
      .append(this.cmapResult.getId())
      .append("]")
      .toString();
  }
}
