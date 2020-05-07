package org.sing_group.dreimt.rest.entity.database;

public class DreimtStatistics {
  private final long drugCount;
  private final long signaturesCount;

  public DreimtStatistics(long drugCount, long signaturesCount) {
    this.drugCount = drugCount;
    this.signaturesCount = signaturesCount;
  }

  public long getDrugCount() {
    return drugCount;
  }

  public long getSignaturesCount() {
    return signaturesCount;
  }
}
