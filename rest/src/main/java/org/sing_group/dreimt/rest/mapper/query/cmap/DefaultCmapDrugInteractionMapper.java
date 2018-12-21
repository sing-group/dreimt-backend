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
package org.sing_group.dreimt.rest.mapper.query.cmap;

import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugInteraction;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapDrugInteractionData;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapDrugInteractionResultData;
import org.sing_group.dreimt.rest.mapper.spi.query.cmap.CmapDrugInteractionMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugMapper;

@Default
public class DefaultCmapDrugInteractionMapper implements CmapDrugInteractionMapper {

  @Inject
  private DrugMapper drugMapper;

  @Override
  public CmapDrugInteractionResultData toCmapDrugInteractionResultData(List<CmapDrugInteraction> cmapDrugInteractions) {
    return new CmapDrugInteractionResultData(
      cmapDrugInteractions.stream().map(c -> 
        new CmapDrugInteractionData(
          drugMapper.toDrugData(c.getDrug()),
          c.getTes(),
          c.getPvalue(),
          c.getFdr()
        )
      ).collect(toList())
    );
  }

  @Override
  public String toCmapDrugInteractionCsvData(List<CmapDrugInteraction> cmapDrugInteractions) {
    StringBuilder sb = new StringBuilder();
    sb.append("drugSourceDb,drugSourceName,drugCommonName,tes,pvalue,fdr\n");
    cmapDrugInteractions.stream().forEach(c -> {
      Drug drug = c.getDrug();
      sb
        .append(drug.getSourceDb())
        .append(",")
        .append(drug.getSourceName())
        .append(",")
        .append(drug.getCommonName())
        .append(",")
        .append(c.getTes())
        .append(",")
        .append(c.getPvalue())
        .append(",")
        .append(c.getFdr())
        .append("\n");
    });

    return sb.toString();
  }
}
