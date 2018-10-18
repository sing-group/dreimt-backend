package org.sing_group.dreimt.rest.entity.signature;

import java.io.Serializable;
import java.util.Set;

public class SignatureGeneData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private Set<String> up;
  private Set<String> down;

  SignatureGeneData() {}

  public SignatureGeneData(Set<String> up, Set<String> down) {
    this.up = up;
    this.down = down;
  }
  
  public Set<String> getUp() {
    return up;
  }
  
  public Set<String> getDown() {
    return down;
  }
}
