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
  private Set<String> cellTypeB;
  private String sourceDb;
  private ExperimentalDesign experimentalDesign;
  private String organism;
  private String disease;
  private int articlePubMedId;
  private String articleTitle;
  private SignatureType signatureType;

  private URI signatureGenesUri;
  private URI articleMetadataUri;

  SignatureData() {}

  public SignatureData(
    String signatureName, Set<String> cellTypeA, Set<String> cellTypeB, String sourceDb,
    ExperimentalDesign experimentalDesign, String organism, String disease,
    int articlePubMedId, String articleTitle, SignatureType signatureType,
    URI signatureGenesUri, URI articleMetadataUri
  ) {
    super();
    this.signatureName = signatureName;
    this.cellTypeA = cellTypeA;
    this.cellTypeB = cellTypeB;
    this.sourceDb = sourceDb;
    this.experimentalDesign = experimentalDesign;
    this.organism = organism;
    this.disease = disease;
    this.articlePubMedId = articlePubMedId;
    this.articleTitle = articleTitle;
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

  public Set<String> getCellTypeB() {
    return cellTypeB;
  }

  public String getArticleTitle() {
    return articleTitle;
  }

  public int getArticlePubMedId() {
    return articlePubMedId;
  }

  public String getSourceDb() {
    return sourceDb;
  }

  public ExperimentalDesign getExperimentalDesign() {
    return experimentalDesign;
  }

  public String getOrganism() {
    return organism;
  }

  public String getDisease() {
    return disease;
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
