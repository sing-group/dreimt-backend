package org.sing_group.dreimt.rest.entity.query.jaccard;

import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;

public class JaccardQueryMetadataData {

  private boolean onlyUniverseGenes;
  private String cellTypeA;
  private String cellSubTypeA;
  private String cellTypeB;
  private String cellSubTypeB;
  private ExperimentalDesign experimentalDesign;
  private String organism;
  private String disease;
  private String signatureSourceDb;
  private Integer upGenesCount;
  private Integer downGenesCount;
  private Integer upUniverseGenesCount;
  private Integer downUniverseGenesCount;

  public JaccardQueryMetadataData(
    boolean onlyUniverseGenes, String cellTypeA, String cellSubTypeA, String cellTypeB, String cellSubTypeB,
    ExperimentalDesign experimentalDesign, String organism, String disease, String signatureSourceDb,
    Integer upGenesCount, Integer downGenesCount, Integer upUniverseGenesCount, Integer downUniverseGenesCount
  ) {
    this.onlyUniverseGenes = onlyUniverseGenes;
    this.cellTypeA = cellTypeA;
    this.cellSubTypeA = cellSubTypeA;
    this.cellTypeB = cellTypeB;
    this.cellSubTypeB = cellSubTypeB;
    this.experimentalDesign = experimentalDesign;
    this.organism = organism;
    this.disease = disease;
    this.signatureSourceDb = signatureSourceDb;
    this.upGenesCount = upGenesCount;
    this.downGenesCount = downGenesCount;
    this.upUniverseGenesCount = upUniverseGenesCount;
    this.downUniverseGenesCount = downUniverseGenesCount;
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

  public Integer getUpGenesCount() {
    return upGenesCount;
  }

  public Integer getDownGenesCount() {
    return downGenesCount;
  }

  public Integer getUpUniverseGenesCount() {
    return upUniverseGenesCount;
  }

  public Integer getDownUniverseGenesCount() {
    return downUniverseGenesCount;
  }
}
