/*-
 * #%L
 * DREIMT - Domain
 * %%
 * Copyright (C) 2018 - 2020 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
 * 			Kevin Troulé, Gonzálo Gómez-López, Fátima Al-Shahrour
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
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

  public DrugInteractionEffect opposite() {
    if(this.equals(BOOST)) {
      return INHIBIT;
    } else {
      return BOOST;
    }
  }
}
