package org.sing_group.dreimt.rest.entity.database;

import java.io.Serializable;

public class DreimtInformationData implements Serializable {
  private static final long serialVersionUID = 1L;

  private double tauThreshold;

  DreimtInformationData() {}

  public DreimtInformationData(double tauThreshold) {
    this.tauThreshold = tauThreshold;
  }
  
  public double getTauThreshold() {
    return tauThreshold;
  }
}
