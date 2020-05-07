/*-
 * #%L
 * DREIMT - REST
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
package org.sing_group.dreimt.rest.mapper.spi.signature;

import java.util.stream.Stream;

import javax.ws.rs.core.UriBuilder;

import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
import org.sing_group.dreimt.rest.entity.signature.DrugSignatureInteractionData;

public interface DrugSignatureInteractionMapper {

  void setUriBuilder(UriBuilder uriBuilder);

  DrugSignatureInteractionData[] toDrugSignatureInteractionData(Stream<DrugSignatureInteraction> interactions, boolean includeSummary);

  String toDrugSignatureInteractionCsvData(Stream<DrugSignatureInteraction> interactions);
}
