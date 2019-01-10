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

import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapResult;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapUpDownSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapDrugInteractionData;
import org.sing_group.dreimt.rest.entity.query.cmap.CmapQueryMetadataData;
import org.sing_group.dreimt.rest.entity.signature.UpDownSignatureGeneData;
import org.sing_group.dreimt.rest.mapper.spi.query.cmap.CmapQueryResultsMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugMapper;

@Default
public class DefaultQueryResultsMapper implements CmapQueryResultsMapper {

  @Inject
  private DrugMapper drugMapper;

  @Override
  public CmapQueryMetadataData toCmapQueryMetadataData(CmapResult cmapResult) {
    return new CmapQueryMetadataData(
      cmapResult.getName(),
      cmapResult.getNumPerm(),
      cmapResult.getMaxPvalue(),
      getUpGenesCount(cmapResult, false),
      getDownGenesCount(cmapResult, false),
      getUpGenesCount(cmapResult, true),
      getDownGenesCount(cmapResult, true)
    );
  }

  private Integer getUpGenesCount(CmapResult cmapResult, boolean universeGenes) {
    if (cmapResult instanceof CmapGeneSetSignatureResult) {
      return ((CmapGeneSetSignatureResult) cmapResult).getGenes(universeGenes).size();
    } else if (cmapResult instanceof CmapUpDownSignatureResult) {
      return ((CmapUpDownSignatureResult) cmapResult).getUpGenes(universeGenes).size();
    } else {
      throw new IllegalArgumentException("Unknown CmapResult type");
    }
  }

  private Integer getDownGenesCount(CmapResult cmapResult, boolean universeGenes) {
    if (cmapResult instanceof CmapGeneSetSignatureResult) {
      return null;
    } else if (cmapResult instanceof CmapUpDownSignatureResult) {
      return ((CmapUpDownSignatureResult) cmapResult).getDownGenes(universeGenes).size();
    } else {
      throw new IllegalArgumentException("Unknown CmapResult type");
    }
  }

  @Override
  public UpDownSignatureGeneData toGeneData(CmapResult cmapResult, boolean onlyUniverseGenes) {
    if (cmapResult instanceof CmapGeneSetSignatureResult) {
      return new UpDownSignatureGeneData(
        ((CmapGeneSetSignatureResult) cmapResult).getGenes(onlyUniverseGenes), null
      );
    } else if (cmapResult instanceof CmapUpDownSignatureResult) {
      return new UpDownSignatureGeneData(
        ((CmapUpDownSignatureResult) cmapResult).getUpGenes(onlyUniverseGenes),
        ((CmapUpDownSignatureResult) cmapResult).getDownGenes(onlyUniverseGenes)
      );
    } else {
      throw new IllegalArgumentException("Unknown CmapResult type");
    }
  }

  @Override
  public CmapDrugInteractionData[] toCmapDrugInteractionData(List<CmapDrugInteraction> cmapDrugInteractions) {
    return cmapDrugInteractions.stream().map(
      c -> new CmapDrugInteractionData(
        drugMapper.toDrugData(c.getDrug()),
        c.getTes(),
        c.getPvalue(),
        c.getFdr()
      )
    ).toArray(CmapDrugInteractionData[]::new);
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
