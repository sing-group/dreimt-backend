package org.sing_group.dreimt.domain.entities.signature;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class DrugId implements Serializable {
  private static final long serialVersionUID = 1L;

  private String sourceName;
  private String sourceDb;

  public DrugId() {}

  public DrugId(String sourceName, String sourceDb) {
    this.sourceName = sourceName;
    this.sourceDb = sourceDb;
  }

  public String getSourceName() {
    return sourceName;
  }

  public String getSourceDb() {
    return sourceDb;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((sourceDb == null) ? 0 : sourceDb.hashCode());
    result = prime * result + ((sourceName == null) ? 0 : sourceName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    DrugId other = (DrugId) obj;
    if (sourceDb == null) {
      if (other.sourceDb != null)
        return false;
    } else if (!sourceDb.equals(other.sourceDb))
      return false;
    if (sourceName == null) {
      if (other.sourceName != null)
        return false;
    } else if (!sourceName.equals(other.sourceName))
      return false;
    return true;
  }
}