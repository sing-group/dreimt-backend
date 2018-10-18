package org.sing_group.dreimt.rest.entity.signature;

import java.io.Serializable;

public class DrugData implements Serializable {
  private static final long serialVersionUID = 1L;

  private String commonName;
  private String sourceName;
  private String sourceDb;

  DrugData() {}

  public DrugData(String commonName, String sourceName, String sourceDb) {
    super();
    this.commonName = commonName;
    this.sourceName = sourceName;
    this.sourceDb = sourceDb;
  }

  public String getCommonName() {
    return commonName;
  }

  public String getSourceName() {
    return sourceName;
  }

  public String getSourceDb() {
    return sourceDb;
  }
}
