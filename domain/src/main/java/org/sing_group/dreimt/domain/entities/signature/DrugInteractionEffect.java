package org.sing_group.dreimt.domain.entities.signature;

import static org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType.SIGNATURE_DOWN;

public enum DrugInteractionEffect {
  BOOST("boosting"), INHIBIT("inhibition");

  private String effect;

  DrugInteractionEffect(String effect) {
    this.effect = effect;
  }

  @Override
  public String toString() {
    return this.effect;
  }

  public String getEffect() {
    return effect;
  }

  public static final DrugInteractionEffect computeEffect(Double tau, DrugSignatureInteractionType interactionType) {
    if (interactionType.equals(SIGNATURE_DOWN)) {
      return tau > 0 ? INHIBIT : BOOST;
    } else {
      return tau > 0 ? BOOST : INHIBIT;
    }
  }
}
