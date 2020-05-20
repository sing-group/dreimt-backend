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

public class SignatureDataSummary implements Serializable {
  private static final long serialVersionUID = 1L;

  private String signatureName;
  private Set<String> cellTypeA;
  private Set<String> cellSubTypeA;
  private Set<String> cellTypeB;
  private Set<String> cellSubTypeB;
  private String sourceDb;
  private String sourceDbUrl;
  private String articleTitle;
  private String articleAuthors;
  private Integer articlePubMedId;

  private URI signatureGenesUri;
  private URI articleMetadataUri;

  SignatureDataSummary() {}

  public SignatureDataSummary(
    String signatureName, Set<String> cellTypeA, Set<String> cellSubTypeA, Set<String> cellTypeB,
    Set<String> cellSubTypeB, String sourceDb, String sourceDbUrl, Integer articlePubMedId, String articleTitle,
    String articleAuthors, URI signatureGenesUri, URI articleMetadataUri
  ) {
    super();
    this.signatureName = signatureName;
    this.cellTypeA = cellTypeA;
    this.cellSubTypeA = cellSubTypeA;
    this.cellTypeB = cellTypeB;
    this.cellSubTypeB = cellSubTypeB;
    this.sourceDb = sourceDb;
    this.sourceDbUrl = sourceDbUrl;
    this.articleTitle = articleTitle;
    this.articleAuthors = articleAuthors;
    this.articlePubMedId = articlePubMedId;

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

  public String getSourceDb() {
    return sourceDb;
  }

  public String getSourceDbUrl() {
    return sourceDbUrl;
  }

  public Integer getArticlePubMedId() {
    return articlePubMedId;
  }

  public String getArticleTitle() {
    return articleTitle;
  }

  public String getArticleAuthors() {
    return articleAuthors;
  }

  public URI getSignatureGenesUri() {
    return signatureGenesUri;
  }

  public URI getArticleMetadataUri() {
    return articleMetadataUri;
  }
}
