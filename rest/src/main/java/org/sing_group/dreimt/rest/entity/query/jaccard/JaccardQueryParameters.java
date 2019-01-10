package org.sing_group.dreimt.rest.entity.query.jaccard;

import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;

public class JaccardQueryParameters {

  public static final boolean DEFAULT_ONLY_UNIVERSE_GENES = false;

  private String queryTitle;
  private boolean onlyUniverseGenes = DEFAULT_ONLY_UNIVERSE_GENES;
  private String cellTypeA;
  private String cellSubTypeA;
  private String cellTypeB;
  private String cellSubTypeB;
  private ExperimentalDesign experimentalDesign;
  private String organism;
  private String disease;
  private String signatureSourceDb;
  private String[] upGenes;
  private String[] downGenes;

  JaccardQueryParameters() {}

  public JaccardQueryParameters(
    String queryTitle, boolean onlyUniverseGenes, String cellTypeA, String cellSubTypeA, String cellTypeB,
    String cellSubTypeB, ExperimentalDesign experimentalDesign, String organism, String disease,
    String signatureSourceDb, String[] upGenes, String[] downGenes
  ) {
    this.queryTitle = queryTitle;
    this.onlyUniverseGenes = onlyUniverseGenes;
    this.cellTypeA = cellTypeA;
    this.cellSubTypeA = cellSubTypeA;
    this.cellTypeB = cellTypeB;
    this.cellSubTypeB = cellSubTypeB;
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

  public String getCellTypeA() {
    return cellTypeA;
  }

  public String getCellSubTypeA() {
    return cellSubTypeA;
  }

  public String getCellTypeB() {
    return cellTypeB;
  }

  public String getCellSubTypeB() {
    return cellSubTypeB;
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
