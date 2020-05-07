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
package org.sing_group.dreimt.rest.mapper.query.jaccard;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.dreimt.domain.entities.execution.jaccard.GeneOverlap;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardResult;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardUpDownSignatureResult;
import org.sing_group.dreimt.rest.entity.query.jaccard.GeneOverlapData;
import org.sing_group.dreimt.rest.entity.query.jaccard.JaccardQueryMetadataData;
import org.sing_group.dreimt.rest.entity.signature.UpDownSignatureGeneData;
import org.sing_group.dreimt.rest.mapper.spi.query.jaccard.JaccardQueryResultsMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.SignatureMapper;

@Default
public class DefaultJaccardQueryResultsMapper implements JaccardQueryResultsMapper {

  @Inject
  private SignatureMapper signatureMapper;

  private UriBuilder uriBuilder;

  @Override
  public void setUriBuilder(UriBuilder uriBuilder) {
    this.uriBuilder = requireNonNull(uriBuilder);
    this.signatureMapper.setUriBuilder(this.uriBuilder);
  }

  @Override
  public JaccardQueryMetadataData toJaccardQueryMetadataData(JaccardResult jaccardResult) {
    return new JaccardQueryMetadataData(
      jaccardResult.getName(),
      jaccardResult.isOnlyUniverseGenes(),
      jaccardResult.getCellType1(),
      jaccardResult.getCellSubType1(),
      jaccardResult.getCellType2(),
      jaccardResult.getCellSubType2(),
      jaccardResult.getExperimentalDesign(),
      jaccardResult.getOrganism(),
      jaccardResult.getDisease(),
      jaccardResult.getSignatureSourceDb(),
      getUpGenesCount(jaccardResult, false),
      getDownGenesCount(jaccardResult, false),
      getUpGenesCount(jaccardResult, true),
      getDownGenesCount(jaccardResult, true)
    );
  }

  private Integer getUpGenesCount(JaccardResult jaccardResult, boolean universeGenes) {
    if (jaccardResult instanceof JaccardGeneSetSignatureResult) {
      return ((JaccardGeneSetSignatureResult) jaccardResult).getGenes(universeGenes).size();
    } else if (jaccardResult instanceof JaccardUpDownSignatureResult) {
      return ((JaccardUpDownSignatureResult) jaccardResult).getUpGenes(universeGenes).size();
    } else {
      throw new IllegalArgumentException("Unknown JaccardResult type");
    }
  }

  private Integer getDownGenesCount(JaccardResult jaccardResult, boolean universeGenes) {
    if (jaccardResult instanceof JaccardGeneSetSignatureResult) {
      return null;
    } else if (jaccardResult instanceof JaccardUpDownSignatureResult) {
      return ((JaccardUpDownSignatureResult) jaccardResult).getDownGenes(universeGenes).size();
    } else {
      throw new IllegalArgumentException("Unknown JaccardResult type");
    }
  }

  @Override
  public UpDownSignatureGeneData toGeneData(JaccardResult jaccardResult, boolean onlyUniverseGenes) {
    if (jaccardResult instanceof JaccardGeneSetSignatureResult) {
      return new UpDownSignatureGeneData(
        ((JaccardGeneSetSignatureResult) jaccardResult).getGenes(onlyUniverseGenes), null
      );
    } else if (jaccardResult instanceof JaccardUpDownSignatureResult) {
      return new UpDownSignatureGeneData(
        ((JaccardUpDownSignatureResult) jaccardResult).getUpGenes(onlyUniverseGenes),
        ((JaccardUpDownSignatureResult) jaccardResult).getDownGenes(onlyUniverseGenes)
      );
    } else {
      throw new IllegalArgumentException("Unknown JaccardResult type");
    }
  }

  @Override
  public GeneOverlapData[] toGeneOverlapData(List<GeneOverlap> geneOverlaps) {
    return geneOverlaps.stream().map(
      g -> new GeneOverlapData(
        g.getSourceComparisonType(),
        signatureMapper.toSignatureDataSummary(g.getTargetSignature()),
        g.getTargetComparisonType(),
        g.getJaccard(),
        g.getPvalue(),
        g.getFdr()
      )
    ).toArray(GeneOverlapData[]::new);
  }

  @Override
  public String toGeneOverlapCsvData(List<GeneOverlap> geneOverlaps) {
    StringBuilder sb = new StringBuilder();
    sb.append("source_comparison_type,target_comparison_type,target_signature,pvalue,fdr,jaccard\n");
    geneOverlaps.forEach(o -> {
      sb
        .append(o.getSourceComparisonType())
        .append(",")
        .append(o.getTargetComparisonType())
        .append(",")
        .append(o.getTargetSignature().getSignatureName())
        .append(",")
        .append(o.getPvalue())
        .append(",")
        .append(o.getFdr())
        .append(",")
        .append(o.getJaccard())
        .append("\n");
    });

    return sb.toString();
  }
}
