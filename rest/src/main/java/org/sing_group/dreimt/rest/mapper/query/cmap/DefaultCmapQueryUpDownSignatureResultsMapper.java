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

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.joining;
import static org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType.SIGNATURE;

import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugUpDownSignatureInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapUpDownSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.rest.entity.query.cmap.DrugPrioritizationQueryUpDownSignatureMetadataData;
import org.sing_group.dreimt.rest.entity.query.cmap.DrugPrioritizationUpDownSignatureDrugInteractionData;
import org.sing_group.dreimt.rest.entity.signature.UpDownSignatureGeneData;
import org.sing_group.dreimt.rest.mapper.signature.EmptyPredictionSummaryGenerator;
import org.sing_group.dreimt.rest.mapper.signature.PredictionSummaryGenerator;
import org.sing_group.dreimt.rest.mapper.spi.query.cmap.CmapQueryUpDownSignatureResultsMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugMapper;

@Default
public class DefaultCmapQueryUpDownSignatureResultsMapper implements CmapQueryUpDownSignatureResultsMapper {

  @Inject
  private DrugMapper drugMapper;

  @Override
  public DrugPrioritizationQueryUpDownSignatureMetadataData toDrugPrioritizationQueryMetadataData(CmapUpDownSignatureResult cmapResult) {
    return new DrugPrioritizationQueryUpDownSignatureMetadataData(
      cmapResult.getName(),
      cmapResult.getNumPerm(),
      getUpGenesCount(cmapResult, false),
      getDownGenesCount(cmapResult, false),
      getUpGenesCount(cmapResult, true),
      getDownGenesCount(cmapResult, true),
      cmapResult.getCaseType(),
      cmapResult.getReferenceType()
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
  public DrugPrioritizationUpDownSignatureDrugInteractionData[] toDrugPrioritizationDrugInteractionData(
    CmapUpDownSignatureResult result,
    List<CmapDrugUpDownSignatureInteraction> cmapDrugInteractions, boolean includeSummary
  ) {
    PredictionSummaryGenerator summaryGenerator =
      includeSummary ? new PredictionSummaryGenerator() : new EmptyPredictionSummaryGenerator();

    return cmapDrugInteractions.stream()
      .map(
        c -> new DrugPrioritizationUpDownSignatureDrugInteractionData(
          drugMapper.toDrugData(c.getDrug()),
          c.getTau(),
          c.getUpFdr(),
          c.getDownFdr(),
          summary(result, c, summaryGenerator)
        )
      ).toArray(DrugPrioritizationUpDownSignatureDrugInteractionData[]::new);
  }

  @Override
  public String toCmapDrugInteractionCsvData(
    CmapUpDownSignatureResult result,
    List<CmapDrugUpDownSignatureInteraction> cmapDrugInteractions
  ) {
    PredictionSummaryGenerator summaryGenerator = new PredictionSummaryGenerator();
    StringBuilder sb = new StringBuilder();
    sb.append(
      "drug_name,summary,up_dr,down_fdr,tau,drug_specificity_score,drug_source_db,drug_source_name,drug_status,drug_moa,\n"
    );
    cmapDrugInteractions.stream()
      .forEach(c -> {
        Drug drug = c.getDrug();

        sb
          .append("\"")
          .append(drug.getCommonName())
          .append("\"")
          .append(",")
          .append("\"")
          .append(summary(result, c, summaryGenerator))
          .append("\"")
          .append(",")
          .append(c.getUpFdr())
          .append(",")
          .append(c.getDownFdr())
          .append(",")
          .append(c.getTau())
          .append(",")
          .append(drug.getDss() == null ? "" : drug.getDss())
          .append(",")
          .append("\"")
          .append(drug.getSourceDb())
          .append("\"")
          .append(",")
          .append("\"")
          .append(drug.getSourceName())
          .append("\"")
          .append(",")
          .append("\"")
          .append(drug.getStatus())
          .append("\"")
          .append(",")
          .append("\"")
          .append(drug.getMoa().stream().collect(joining(", ")))
          .append("\"")
          .append("\n");
      });
    return sb.toString();
  }

  private static String summary(
    CmapUpDownSignatureResult result,
    CmapDrugUpDownSignatureInteraction interaction,
    PredictionSummaryGenerator predictionSummaryGenerator
  ) {
    return predictionSummaryGenerator.interpretation(
      "query-signature", interaction.getTau(), SIGNATURE, interaction.getDrug().getCommonName(),
      "", result.getCaseType(), emptySet(), emptySet(),
      "", result.getReferenceType(), emptySet(), emptySet()
    );
  }
}
