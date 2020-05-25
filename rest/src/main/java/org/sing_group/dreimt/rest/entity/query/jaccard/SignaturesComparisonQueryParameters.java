/*-
 * #%L
 * DREIMT - REST
 * %%
 * Copyright (C) 2018 - 2019 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
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
package org.sing_group.dreimt.rest.entity.query.jaccard;

import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;

public class SignaturesComparisonQueryParameters {

  public static final boolean DEFAULT_ONLY_UNIVERSE_GENES = false;

  private String queryTitle;
  private boolean onlyUniverseGenes = DEFAULT_ONLY_UNIVERSE_GENES;
  private String cellType1;
  private String cellSubType1;
  private String cellTypeOrSubType1;
  private String cellType2;
  private String cellSubType2;
  private String cellTypeOrSubType2;
  private ExperimentalDesign experimentalDesign;
  private String organism;
  private String disease;
  private String signatureSourceDb;
  private String[] upGenes;
  private String[] downGenes;

  SignaturesComparisonQueryParameters() {}

  public SignaturesComparisonQueryParameters(
    String queryTitle, boolean onlyUniverseGenes, String cellType1, String cellSubType1, String cellTypeOrSubType1,
    String cellType2, String cellTypeOrSubType2, String cellSubType2, ExperimentalDesign experimentalDesign,
    String organism, String disease, String signatureSourceDb, String[] upGenes, String[] downGenes
  ) {
    this.queryTitle = queryTitle;
    this.onlyUniverseGenes = onlyUniverseGenes;
    this.cellType1 = cellType1;
    this.cellSubType1 = cellSubType1;
    this.cellTypeOrSubType1 = cellTypeOrSubType1;
    this.cellType2 = cellType2;
    this.cellSubType2 = cellSubType2;
    this.cellTypeOrSubType2 = cellTypeOrSubType2;
    this.experimentalDesign = experimentalDesign;
    this.organism = organism;
    this.disease = disease;
    this.signatureSourceDb = signatureSourceDb;
    this.upGenes = upGenes;
    this.downGenes = downGenes;
  }

  public String getQueryTitle() {
    return queryTitle;
  }

  public boolean isOnlyUniverseGenes() {
    return onlyUniverseGenes;
  }

  public String getCellType1() {
    return cellType1;
  }

  public String getCellSubType1() {
    return cellSubType1;
  }

  public String getCellTypeOrSubType1() {
    return cellTypeOrSubType1;
  }

  public String getCellType2() {
    return cellType2;
  }

  public String getCellSubType2() {
    return cellSubType2;
  }

  public String getCellTypeOrSubType2() {
    return cellTypeOrSubType2;
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

  public String getSignatureSourceDb() {
    return signatureSourceDb;
  }

  public String[] getUpGenes() {
    return upGenes;
  }

  public String[] getDownGenes() {
    return downGenes;
  }
}
