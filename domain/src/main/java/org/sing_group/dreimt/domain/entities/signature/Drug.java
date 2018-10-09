package org.sing_group.dreimt.domain.entities.signature;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "drug")
public class Drug implements Serializable {
  private static final long serialVersionUID = 1L;

  private String commonName;

  @EmbeddedId
  private DrugId id;

  Drug() {}

  public String getCommonName() {
    return commonName;
  }

  public String getSourceName() {
    return id.getSourceName();
  }

  public String getSourceDb() {
    return id.getSourceDb();
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    Drug other = (Drug) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
