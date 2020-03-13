package org.sing_group.dreimt.domain.entities.database;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dreimt_information")
public class DreimtInformation implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private int id;

  private double tauThreshold;

  DreimtInformation() {}

  public DreimtInformation(double tauThreshold) {
    this.tauThreshold = tauThreshold;
  }

  public double getTauThreshold() {
    return tauThreshold;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + id;
    long temp;
    temp = Double.doubleToLongBits(tauThreshold);
    result = prime * result + (int) (temp ^ (temp >>> 32));
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
    DreimtInformation other = (DreimtInformation) obj;
    if (id != other.id)
      return false;
    if (Double.doubleToLongBits(tauThreshold) != Double.doubleToLongBits(other.tauThreshold))
      return false;
    return true;
  }
}
