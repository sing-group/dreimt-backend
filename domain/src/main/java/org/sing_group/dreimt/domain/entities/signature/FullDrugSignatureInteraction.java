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

import static java.util.Optional.ofNullable;
import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Optional;

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
  name = "full_drug_signature_interaction",
  indexes = {
    @Index(columnList = "tau"),
    @Index(columnList = "upFdr"),
    @Index(columnList = "downFdr"),

    @Index(columnList = "drugCommonName"),
    @Index(columnList = "drugSourceName"),
    @Index(columnList = "drugMoa"),
    @Index(columnList = "drugDss"),

    @Index(columnList = "signatureName"),
    @Index(columnList = "signatureType"),
    @Index(columnList = "signatureExperimentalDesign"),
    @Index(columnList = "signatureOrganism"),
    @Index(columnList = "signatureSourceDb"),
    @Index(columnList = "signatureCellTypeA"),
    @Index(columnList = "signatureCellSubTypeA"),
    @Index(columnList = "signatureCellTypeB"),
    @Index(columnList = "signatureCellSubTypeB")
  }
)
public class FullDrugSignatureInteraction implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private int id;

  private String drugCommonName;
  private String drugSourceName;
  private String drugSourceDb;

  @Enumerated(EnumType.ORDINAL)
  private DrugStatus drugStatus;
  private String drugMoa;

  @Column(length = 450)
  private String drugTargetGenes;

  @Column(nullable = true)
  private Double drugDss;

  private String signatureName;

  @Enumerated(EnumType.STRING)
  private SignatureType signatureType;

  @Enumerated(EnumType.STRING)
  private ExperimentalDesign signatureExperimentalDesign;

  private String signatureOrganism;
  private String signatureSourceDb;
  private String signatureSourceDbUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "signatureArticlePubmedId", referencedColumnName = "pubmedId", nullable = true)
  private ArticleMetadata signatureArticlePubmedId;

  private String signatureDisease;
  private String signatureCellTypeA;
  private String signatureCellSubTypeA;
  private String signatureCellTypeB;
  private String signatureCellSubTypeB;

  private String signatureTreatmentA;
  private String signatureTreatmentB;
  private String signatureDiseaseA;
  private String signatureDiseaseB;
  private String signatureLocalisationA;
  private String signatureLocalisationB;
  private String signatureStateA;
  private String signatureStateB;

  @Enumerated(EnumType.STRING)
  private DrugSignatureInteractionType interactionType;

  private double tau;

  @Column(nullable = true)
  private Double upFdr;

  @Column(nullable = true)
  private Double downFdr;

  @Enumerated(EnumType.ORDINAL)
  private DrugInteractionEffect cellTypeAEffect;

  @Enumerated(EnumType.ORDINAL)
  private DrugInteractionEffect cellTypeBEffect;

  public FullDrugSignatureInteraction() {}

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

  public DrugInteractionEffect getCellTypeAEffect() {
    return cellTypeAEffect;
  }
  
  public DrugInteractionEffect getCellTypeBEffect() {
    return cellTypeBEffect;
  }

  public String getDrugCommonName() {
    return drugCommonName;
  }

  public String getDrugSourceName() {
    return drugSourceName;
  }

  public String getDrugSourceDb() {
    return drugSourceDb;
  }

  public DrugStatus getDrugStatus() {
    return drugStatus;
  }

  public String getDrugMoa() {
    return drugMoa;
  }

  public String getDrugTargetGenes() {
    return drugTargetGenes;
  }

  public Double getDrugDss() {
    return drugDss;
  }

  public String getSignatureName() {
    return signatureName;
  }

  public SignatureType getSignatureType() {
    return signatureType;
  }

  public ExperimentalDesign getSignatureExperimentalDesign() {
    return signatureExperimentalDesign;
  }

  public String getSignatureOrganism() {
    return signatureOrganism;
  }

  public String getSignatureSourceDb() {
    return signatureSourceDb;
  }

  public String getSignatureSourceDbUrl() {
    return signatureSourceDbUrl;
  }

  public Optional<ArticleMetadata> getArticleMetadata() {
    return ofNullable(signatureArticlePubmedId);
  }

  public String getSignatureDisease() {
    return signatureDisease;
  }

  public String getSignatureCellTypeA() {
    return signatureCellTypeA;
  }

  public String getSignatureCellSubTypeA() {
    return signatureCellSubTypeA;
  }

  public String getSignatureCellTypeB() {
    return signatureCellTypeB;
  }

  public String getSignatureCellSubTypeB() {
    return signatureCellSubTypeB;
  }

  public String getSignatureDiseaseA() {
    return signatureDiseaseA;
  }

  public String getSignatureDiseaseB() {
    return signatureDiseaseB;
  }

  public String getSignatureTreatmentA() {
    return signatureTreatmentA;
  }

  public String getSignatureTreatmentB() {
    return signatureTreatmentB;
  }

  public String getSignatureLocalisationA() {
    return signatureLocalisationA;
  }

  public String getSignatureLocalisationB() {
    return signatureLocalisationB;
  }

  public String getSignatureStateA() {
    return signatureStateA;
  }

  public String getSignatureStateB() {
    return signatureStateB;
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
    FullDrugSignatureInteraction other = (FullDrugSignatureInteraction) obj;
    if (id != other.id)
      return false;
    return true;
  }
}
