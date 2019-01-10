package org.sing_group.dreimt.rest.entity.query.cmap;

public class CmapQueryMetadataData {

  private String queryTitle;
  private int numPerm;
  private double maxPvalue;
  private Integer upGenesCount;
  private Integer downGenesCount;
  private Integer upUniverseGenesCount;
  private Integer downUniverseGenesCount;

  public CmapQueryMetadataData(
    String queryTitle, int numPerm, double maxPvalue, Integer upGenesCount, Integer downGenesCount,
    Integer upUniverseGenesCount, Integer downUniverseGenesCount
  ) {
    this.queryTitle = queryTitle;
    this.numPerm = numPerm;
    this.maxPvalue = maxPvalue;
    this.upGenesCount = upGenesCount;
    this.downGenesCount = downGenesCount;
    this.upUniverseGenesCount = upUniverseGenesCount;
    this.downUniverseGenesCount = downUniverseGenesCount;
  }

  public String getQueryTitle() {
    return queryTitle;
  }

  public int getNumPerm() {
    return numPerm;
  }

  public double getMaxPvalue() {
    return maxPvalue;
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
