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
package org.sing_group.dreimt.domain.entities.interation;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "drug_cell_interaction")
public class DrugCellInteraction implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private int id;

  private String signatureName;
  private String studyId;
  private String source;
  private String article;
  
  @Column(length = 1024)
  private String articleAbstract;
  private String authors;
  private int pubMedId;
  private String signatureInfo;
  private String organism;
  private String cellTypeA;
  private String cellTypeB;
  private String experimentalDesign;
  private String tissueType;
  private String disease;
  private String drugName;
  private int drugSystematicName;
  private double nes;
  private double pValue;
  private double fdr;

  DrugCellInteraction() {}

  public int getId() {
    return id;
  }

  public String getSignatureName() {
    return signatureName;
  }

  public String getStudyId() {
    return studyId;
  }

  public String getSource() {
    return source;
  }

  public String getArticle() {
    return article;
  }

  public String getArticleAbstract() {
    return articleAbstract;
  }

  public String getAuthors() {
    return authors;
  }

  public int getPubMedId() {
    return pubMedId;
  }

  public String getSignatureInfo() {
    return signatureInfo;
  }

  public String getOrganism() {
    return organism;
  }

  public String getCellTypeA() {
    return cellTypeA;
  }

  public String getCellTypeB() {
    return cellTypeB;
  }

  public String getExperimentalDesign() {
    return experimentalDesign;
  }

  public String getTissueType() {
    return tissueType;
  }

  public String getDisease() {
    return disease;
  }

  public String getDrugName() {
    return drugName;
  }

  public int getDrugSystematicName() {
    return drugSystematicName;
  }

  public double getNes() {
    return nes;
  }

  public double getPValue() {
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
    DrugCellInteraction other = (DrugCellInteraction) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
