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

import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugGeneSetSignatureInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapGeneSetSignatureDrugInteractionData;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapQueryGeneSetSignatureMetadataData;
import org.sing_group.dreimt.rest.entity.signature.GeneSetSignatureGeneData;
import org.sing_group.dreimt.rest.mapper.spi.query.cmap.CmapQueryGeneSetSignatureResultsMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugMapper;

@Default
public class DefaultQueryGeneSetSignatureResultsMapper implements CmapQueryGeneSetSignatureResultsMapper {

  @Inject
  private DrugMapper drugMapper;

  @Override
  public CmapQueryGeneSetSignatureMetadataData toCmapQueryMetadataData(CmapGeneSetSignatureResult cmapResult) {
    return new CmapQueryGeneSetSignatureMetadataData(
      cmapResult.getName(),
      cmapResult.getNumPerm(),
      getGenesCount(cmapResult, false),
      getGenesCount(cmapResult, true)
    );
  }

  private Integer getGenesCount(CmapGeneSetSignatureResult cmapResult, boolean universeGenes) {
    return cmapResult.getGenes(universeGenes).size();
  }

  @Override
  public GeneSetSignatureGeneData toGeneData(CmapGeneSetSignatureResult cmapResult, boolean onlyUniverseGenes) {
    return new GeneSetSignatureGeneData(
      cmapResult.getGenes(onlyUniverseGenes)
    );
  }

  @Override
  public CmapGeneSetSignatureDrugInteractionData[] toCmapDrugInteractionData(
    List<CmapDrugGeneSetSignatureInteraction> cmapDrugInteractions
  ) {
    return cmapDrugInteractions.stream()
      .map(
        c -> new CmapGeneSetSignatureDrugInteractionData(
          drugMapper.toDrugData(c.getDrug()),
          c.getTau(),
          c.getFdr()
        )
      ).toArray(CmapGeneSetSignatureDrugInteractionData[]::new);
  }

  @Override
  public String toCmapDrugInteractionCsvData(List<CmapDrugGeneSetSignatureInteraction> cmapDrugInteractions) {
    StringBuilder sb = new StringBuilder();
    sb.append("drugSourceDb,drugSourceName,drugCommonName,tau,fdr\n");
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
          .append(c.getFdr())
          .append("\n");
      });

    return sb.toString();
  }
}
