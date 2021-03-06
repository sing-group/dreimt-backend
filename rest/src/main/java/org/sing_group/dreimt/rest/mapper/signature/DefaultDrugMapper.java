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
package org.sing_group.dreimt.rest.mapper.signature;

import javax.enterprise.inject.Default;

import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.rest.entity.signature.DrugData;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugMapper;

@Default
public class DefaultDrugMapper implements DrugMapper {

  @Override
  public DrugData toDrugData(Drug drug) {
    return new DrugData(
      drug.getCommonName(), drug.getSourceName(), drug.getSourceDb(), drug.getStatus(), drug.getMoa(),
      drug.getTargetGenes(), drug.getDss(), drug.getPubChemId(), drug.getDbProfilesCount()
    );
  }
}
