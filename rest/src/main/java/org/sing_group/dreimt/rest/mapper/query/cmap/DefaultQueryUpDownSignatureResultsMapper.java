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

import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugUpDownSignatureInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapUpDownSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapQueryUpDownSignatureMetadataData;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapUpDownSignatureDrugInteractionData;
import org.sing_group.dreimt.rest.entity.signature.UpDownSignatureGeneData;
import org.sing_group.dreimt.rest.mapper.spi.query.cmap.CmapQueryUpDownSignatureResultsMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugMapper;

@Default
public class DefaultQueryUpDownSignatureResultsMapper implements CmapQueryUpDownSignatureResultsMapper {

  @Inject
  private DrugMapper drugMapper;
  
  @Override
  public CmapQueryUpDownSignatureMetadataData toCmapQueryMetadataData(CmapUpDownSignatureResult cmapResult) {
    return new CmapQueryUpDownSignatureMetadataData(
      cmapResult.getName(),
      cmapResult.getNumPerm(),
      getUpGenesCount(cmapResult, false),
      getDownGenesCount(cmapResult, false),
      getUpGenesCount(cmapResult, true),
      getDownGenesCount(cmapResult, true)
    );
  }

  private Integer getUpGenesCount(CmapUpDownSignatureResult cmapResult, boolean universeGenes) {
    return cmapResult.getUpGenes(universeGenes).size();
  }

  private Integer getDownGenesCount(CmapUpDownSignatureResult cmapResult, boolean universeGenes) {
    return cmapResult.getDownGenes(universeGenes).size();
  }

  @Override
  public UpDownSignatureGeneData toGeneData(CmapUpDownSignatureResult cmapResult, boolean onlyUniverseGenes) {
    return new UpDownSignatureGeneData(
      cmapResult.getUpGenes(onlyUniverseGenes),
      cmapResult.getDownGenes(onlyUniverseGenes)
    );
  }

  @Override
  public CmapUpDownSignatureDrugInteractionData[] toCmapDrugInteractionData(
    List<CmapDrugUpDownSignatureInteraction> cmapDrugInteractions
  ) {
    return cmapDrugInteractions.stream()
      .map(
        c -> new CmapUpDownSignatureDrugInteractionData(
          drugMapper.toDrugData(c.getDrug()),
          c.getTau(),
          c.getUpFdr(),
          c.getDownFdr()
        )
      ).toArray(CmapUpDownSignatureDrugInteractionData[]::new);
  }

  @Override
  public String toCmapDrugInteractionCsvData(List<CmapDrugUpDownSignatureInteraction> cmapDrugInteractions) {
    StringBuilder sb = new StringBuilder();
    sb.append("drugSourceDb,drugSourceName,drugCommonName,tau,upFdr,downFdr\n");
    cmapDrugInteractions.stream()
      .forEach(c -> {
        Drug drug = c.getDrug();
        sb
          .append("\"")
          .append(drug.getSourceDb())
          .append("\"")
          .append(",")
          .append("\"")
          .append(drug.getSourceName())
          .append("\"")
          .append(",")
          .append("\"")
          .append(drug.getCommonName())
          .append("\"")
          .append(",")
          .append(c.getTau())
          .append(",")
          .append(c.getUpFdr())
          .append(",")
          .append(c.getDownFdr())
          .append("\n");
      });

    return sb.toString();
  }
}
