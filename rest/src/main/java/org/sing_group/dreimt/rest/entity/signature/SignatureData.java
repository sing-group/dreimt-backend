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
import java.net.URI;
import java.util.Set;

import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;

public class SignatureData implements Serializable {
  private static final long serialVersionUID = 1L;

  private String signatureName;
  private Set<String> cellTypeA;
  private Set<String> cellSubTypeA;
  private Set<String> cellTypeB;
  private Set<String> cellSubTypeB;
  private String sourceDb;
  private String sourceDbUrl;
  private ExperimentalDesign experimentalDesign;
  private String organism;
  private Set<String> disease;
  private Set<String> diseaseA;
  private Set<String> diseaseB;
  private Set<String> treatmentA;
  private Set<String> treatmentB;
  private String stateA;
  private String stateB;
  private Integer articlePubMedId;
  private String articleTitle;
  private String articleAuthors;
  private SignatureType signatureType;

  private URI signatureGenesUri;
  private URI articleMetadataUri;

  SignatureData() {}

  public SignatureData(
    String signatureName, Set<String> cellTypeA, Set<String> cellSubTypeA, Set<String> cellTypeB,
    Set<String> cellSubTypeB, String sourceDb, String sourceDbUrl, ExperimentalDesign experimentalDesign, String organism,
    Set<String> disease, Set<String> diseaseA, Set<String> diseaseB, Set<String> treatmentA, Set<String> treatmentB,
    String stateA, String stateB, Integer articlePubMedId, String articleTitle, String articleAuthors,
    SignatureType signatureType, URI signatureGenesUri, URI articleMetadataUri
  ) {
    super();
    this.signatureName = signatureName;
    this.cellTypeA = cellTypeA;
    this.cellSubTypeA = cellSubTypeA;
    this.cellTypeB = cellTypeB;
    this.cellSubTypeB = cellSubTypeB;
    this.sourceDb = sourceDb;
    this.sourceDbUrl = sourceDbUrl;
    this.experimentalDesign = experimentalDesign;
    this.organism = organism;
    this.disease = disease;
    this.diseaseA = diseaseA;
    this.diseaseB = diseaseB;
    this.treatmentA = treatmentA;
    this.treatmentB = treatmentB;
    this.stateA = stateA;
    this.stateB = stateB;
    this.articlePubMedId = articlePubMedId;
    this.articleTitle = articleTitle;
    this.articleAuthors = articleAuthors;
    this.signatureType = signatureType;

    this.signatureGenesUri = signatureGenesUri;
    this.articleMetadataUri = articleMetadataUri;
  }

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

  public String getArticleTitle() {
    return articleTitle;
  }
  
  public String getArticleAuthors() {
    return articleAuthors;
  }

  public Integer getArticlePubMedId() {
    return articlePubMedId;
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

  public String getStateA() {
    return stateA;
  }

  public String getStateB() {
    return stateB;
  }

  public URI getSignatureGenesUri() {
    return signatureGenesUri;
  }

  public URI getArticleMetadataUri() {
    return articleMetadataUri;
  }

  public SignatureType getSignatureType() {
    return signatureType;
  }
}
