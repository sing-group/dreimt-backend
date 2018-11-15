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
import static java.util.stream.Collectors.toList;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.dreimt.domain.entities.execution.jaccard.GeneOverlap;
import org.sing_group.dreimt.rest.entity.query.jaccard.GeneOverlapData;
import org.sing_group.dreimt.rest.entity.query.jaccard.GeneOverlapResultData;
import org.sing_group.dreimt.rest.mapper.spi.query.jaccard.GeneOverlapMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.SignatureMapper;

public class DefaultGeneOverlapMapper implements GeneOverlapMapper {

  @Inject
  private SignatureMapper signatureMapper;

  private UriBuilder uriBuilder;

  @Override
  public void setUriBuilder(UriBuilder uriBuilder) {
    this.uriBuilder = requireNonNull(uriBuilder);
    this.signatureMapper.setUriBuilder(this.uriBuilder);
  }

  @Override
  public GeneOverlapResultData toGeneOverlapResultData(List<GeneOverlap> geneOverlaps) {
    List<GeneOverlapData> overlapData = geneOverlaps.stream().map(g -> {
      return new GeneOverlapData(
        g.getSourceComparisonType(),
        signatureMapper.toSignatureDataSummary(g.getTargetSignature()),
        g.getTargetComparisonType(),
        g.getJaccard(),
        g.getpValue(),
        g.getFdr()
      );
    }).collect(toList());

    return new GeneOverlapResultData(overlapData);
  }

  @Override
  public String toGeneOverlapCsvData(List<GeneOverlap> geneOverlaps) {
    GeneOverlapResultData resultData = toGeneOverlapResultData(geneOverlaps);
    StringBuilder sb = new StringBuilder();
    sb.append("source,target,target_signature,jaccard,pvalue,fdr\n");
    resultData.getOverlapData().forEach(o -> {

      sb
        .append(o.getSourceComparisonType())
        .append(",")
        .append(o.getTargetComparisonType())
        .append(",")
        .append(o.getTargetSignatureData().getSignatureName())
        .append(",")
        .append(o.getJaccard())
        .append(",")
        .append(o.getGetpValue())
        .append(",")
        .append(o.getFdr())
        .append("\n");
    });

    return sb.toString();
  }
}