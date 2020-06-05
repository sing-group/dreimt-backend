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

import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugGeneSetSignatureInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;
import org.sing_group.dreimt.rest.entity.query.cmap.DrugPrioritizationGeneSetSignatureDrugInteractionData;
import org.sing_group.dreimt.rest.entity.query.cmap.DrugPrioritizationQueryGeneSetSignatureMetadataData;
import org.sing_group.dreimt.rest.entity.signature.GeneSetSignatureGeneData;
import org.sing_group.dreimt.rest.mapper.signature.EmptyPredictionSummaryGenerator;
import org.sing_group.dreimt.rest.mapper.signature.PredictionSummaryGenerator;
import org.sing_group.dreimt.rest.mapper.spi.query.cmap.CmapQueryGeneSetSignatureResultsMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugMapper;

@Default
public class DefaultCmapQueryGeneSetSignatureResultsMapper implements CmapQueryGeneSetSignatureResultsMapper {

  @Inject
  private DrugMapper drugMapper;

  @Override
  public DrugPrioritizationQueryGeneSetSignatureMetadataData toCmapQueryMetadataData(CmapGeneSetSignatureResult cmapResult) {
    return new DrugPrioritizationQueryGeneSetSignatureMetadataData(
      cmapResult.getName(),
      cmapResult.getNumPerm(),
      getGenesCount(cmapResult, false),
      getGenesCount(cmapResult, true),
      cmapResult.getGeneSetType(),
      cmapResult.getCaseType(),
      cmapResult.getReferenceType()
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
  public DrugPrioritizationGeneSetSignatureDrugInteractionData[] toDrugPrioritizationDrugInteractionData(
    CmapGeneSetSignatureResult result,
    List<CmapDrugGeneSetSignatureInteraction> cmapDrugInteractions,
    boolean includeSummary
  ) {
    PredictionSummaryGenerator summaryGenerator =
      includeSummary ? new PredictionSummaryGenerator() : new EmptyPredictionSummaryGenerator();

    return cmapDrugInteractions.stream()
      .map(
        c -> new DrugPrioritizationGeneSetSignatureDrugInteractionData(
          drugMapper.toDrugData(c.getDrug()),
          c.getTau(),
          c.getFdr(),
          summary(result, c, summaryGenerator)
        )
      ).toArray(DrugPrioritizationGeneSetSignatureDrugInteractionData[]::new);
  }

  @Override
  public String toDrugPrioritizationDrugInteractionCsvData(
    CmapGeneSetSignatureResult result,
    List<CmapDrugGeneSetSignatureInteraction> cmapDrugInteractions
  ) {
    PredictionSummaryGenerator summaryGenerator = new PredictionSummaryGenerator();

    StringBuilder sb = new StringBuilder();
    sb.append(
      "drug_name,summary,fdr,tau,drug_specificity_score,drug_source_db,drug_source_name,drug_status,drug_moa,\n"
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
          .append(c.getFdr())
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

  public static String summary(
    CmapGeneSetSignatureResult result,
    CmapDrugGeneSetSignatureInteraction interaction,
    PredictionSummaryGenerator predictionSummaryGenerator
  ) {
    final DrugSignatureInteractionType interactionType = result.getGeneSetType().getInteractionType();

    return predictionSummaryGenerator.interpretation(
      "query-signature", interaction.getTau(), interactionType, interaction.getDrug().getCommonName(),
      "", result.getCaseType(), emptySet(), emptySet(),
      "", result.getReferenceType(), emptySet(), emptySet()
    );
  }
}
