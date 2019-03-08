/*-
 * #%L
 * DREIMT - Domain
 * %%
 * Copyright (C) 2018 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
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

public enum DrugSignatureInteractionField {
  NONE(false),

  CELL_TYPE_A(true),
  CELL_SUBTYPE_A(true),
  CELL_TYPE_B(true),
  CELL_SUBTYPE_B(true),
  SIGNATURE_NAME(false),
  EXPERIMENTAL_DESIGN(false),
  ORGANISM(false),
  DISEASE(true),
  SIGNATURE_SOURCE_DB(false),
  SIGNATURE_TYPE(false),

  DRUG_SOURCE_NAME(false),
  DRUG_SOURCE_DB(false),
  DRUG_COMMON_NAME(false),

  INTERACTION_TYPE(false),
  TAU(false),
  UP_FDR(false),
  DOWN_FDR(false);

  private boolean multivaluated;

  DrugSignatureInteractionField(boolean multivaluated) {
    this.multivaluated = multivaluated;
  }

  public boolean isMultivaluated() {
    return multivaluated;
  }
}
