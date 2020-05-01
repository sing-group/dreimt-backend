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

import java.io.Serializable;
import java.util.Optional;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "signatureType")
@Table(name = "signature")
public abstract class Signature implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private String signatureName;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "signature_cell_type_a",
    joinColumns = @JoinColumn(name = "signatureName"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"signatureName", "cellType"})
  )
  @Column(name = "cellType", nullable = false)
  private Set<String> cellTypeA;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "signature_cell_subtype_a",
    joinColumns = @JoinColumn(name = "signatureName"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"signatureName", "cellSubType"})
  )
  @Column(name = "cellSubType", nullable = false)
  private Set<String> cellSubTypeA;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "signature_cell_type_b",
    joinColumns = @JoinColumn(name = "signatureName"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"signatureName", "cellType"})
  )
  @Column(name = "cellType", nullable = false)
  private Set<String> cellTypeB;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "signature_cell_subtype_b",
    joinColumns = @JoinColumn(name = "signatureName"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"signatureName", "cellSubType"})
  )
  @Column(name = "cellSubType", nullable = false)
  private Set<String> cellSubTypeB;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "article_pubmedId", referencedColumnName = "pubmedId", nullable = true)
  private ArticleMetadata articleMetadata;

  private String sourceDb;
  private String sourceDbUrl;

  @Enumerated(EnumType.STRING)
  private ExperimentalDesign experimentalDesign;

  private String organism;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "signature_disease",
    joinColumns = @JoinColumn(name = "signatureName"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"signatureName", "disease"})
  )
  private Set<String> disease;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "signature_treatment_a",
    joinColumns = @JoinColumn(name = "signatureName"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"signatureName", "treatmentA"})
  )
  private Set<String> treatmentA;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "signature_treatment_b",
    joinColumns = @JoinColumn(name = "signatureName"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"signatureName", "treatmentB"})
  )
  private Set<String> treatmentB;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "signature_disease_a",
    joinColumns = @JoinColumn(name = "signatureName"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"signatureName", "diseaseA"})
  )
  private Set<String> diseaseA;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(
    name = "signature_disease_b",
    joinColumns = @JoinColumn(name = "signatureName"),
    uniqueConstraints = @UniqueConstraint(columnNames = {"signatureName", "diseaseB"})
  )
  private Set<String> diseaseB;

  private String localisationA;
  private String localisationB;
  private String stateA;
  private String stateB;

  @Column(name = "signatureType", insertable = false, updatable = false)
  private String signatureType;

  Signature() {}

  public Signature(
    String signatureName, Set<String> cellTypeA, Set<String> cellSubTypeA, Set<String> cellTypeB,
    Set<String> cellSubTypeB, String sourceDb, String sourceDbUrl, ExperimentalDesign experimentalDesign,
    String organism, Set<String> disease, Set<String> treatmentA, Set<String> treatmentB, Set<String> diseaseA,
    Set<String> diseaseB, String localisationA, String localisationB, String stateA, String stateB
  ) {
    this(
      signatureName, cellTypeA, cellSubTypeA, cellTypeB, cellSubTypeB, null, sourceDb, sourceDbUrl, experimentalDesign,
      organism, disease, treatmentA, treatmentB, diseaseA, diseaseB, localisationA, localisationB, stateA, stateB
    );
  }

  public Signature(
    String signatureName, Set<String> cellTypeA, Set<String> cellSubTypeA, Set<String> cellTypeB,
    Set<String> cellSubTypeB, ArticleMetadata articleMetadata, String sourceDb, String sourceDbUrl,
    ExperimentalDesign experimentalDesign, String organism, Set<String> disease, Set<String> treatmentA,
    Set<String> treatmentB, Set<String> diseaseA, Set<String> diseaseB, String localisationA, String localisationB,
    String stateA, String stateB
  ) {
    this.signatureName = signatureName;
    this.cellTypeA = cellTypeA;
    this.cellSubTypeA = cellSubTypeA;
    this.cellTypeB = cellTypeB;
    this.cellSubTypeB = cellSubTypeB;
    this.articleMetadata = articleMetadata;
    this.sourceDb = sourceDb;
    this.sourceDbUrl = sourceDbUrl;
    this.experimentalDesign = experimentalDesign;
    this.organism = organism;
    this.disease = disease;
    this.treatmentA = treatmentA;
    this.treatmentB = treatmentB;
    this.diseaseA = diseaseA;
    this.diseaseB = diseaseB;
    this.localisationA = localisationA;
    this.localisationB = localisationB;
    this.stateA = stateA;
    this.stateB = stateB;
  }

  public Optional<ArticleMetadata> getArticleMetadata() {
    return ofNullable(articleMetadata);
  }

  public abstract SignatureType getSignatureType();

  public String getSignatureName() {
    return signatureName;
  }

  public Set<String> getCellTypeA() {
    return cellTypeA;
  }

  public Set<String> getCellSubTypeA() {
    return cellSubTypeA;
  }

  public Set<String> getCellTypeB() {
    return cellTypeB;
  }

  public Set<String> getCellSubTypeB() {
    return cellSubTypeB;
  }

  public String getSourceDb() {
    return sourceDb;
  }
  
  public String getSourceDbUrl() {
    return sourceDbUrl;
  }

  public ExperimentalDesign getExperimentalDesign() {
    return experimentalDesign;
  }

  public String getOrganism() {
    return organism;
  }

  public Set<String> getDisease() {
    return disease;
  }

  public Set<String> getDiseaseA() {
    return diseaseA;
  }

  public Set<String> getDiseaseB() {
    return diseaseB;
  }

  public Set<String> getTreatmentA() {
    return treatmentA;
  }

  public Set<String> getTreatmentB() {
    return treatmentB;
  }

  public String getLocalisationA() {
    return localisationA;
  }

  public String getLocalisationB() {
    return localisationB;
  }

  public String getStateA() {
    return stateA;
  }

  public String getStateB() {
    return stateB;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((signatureName == null) ? 0 : signatureName.hashCode());
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
    Signature other = (Signature) obj;
    if (signatureName == null) {
      if (other.signatureName != null)
        return false;
    } else if (!signatureName.equals(other.signatureName))
      return false;
    return true;
  }
}
