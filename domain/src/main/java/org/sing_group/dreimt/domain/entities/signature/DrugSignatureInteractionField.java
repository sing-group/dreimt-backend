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
  NONE,

  CELL_TYPE_A,
  CELL_SUBTYPE_A,
  CELL_TYPE_B,
  CELL_SUBTYPE_B,
  SIGNATURE_NAME,
  EXPERIMENTAL_DESIGN,
  ORGANISM,
  DISEASE,
  SIGNATURE_SOURCE_DB,
  SIGNATURE_TYPE,

  DRUG_SOURCE_NAME,
  DRUG_SOURCE_DB,
  DRUG_COMMON_NAME,
  DRUG_MOA,
  DRUG_DSS,
  DRUG_STATUS,

  INTERACTION_TYPE,
  TAU,
  UP_FDR,
  DOWN_FDR;
}
