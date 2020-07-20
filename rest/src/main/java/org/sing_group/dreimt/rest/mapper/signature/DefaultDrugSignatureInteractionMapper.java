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

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
import org.sing_group.dreimt.domain.entities.signature.DrugTargetGene;
import org.sing_group.dreimt.rest.entity.signature.DrugSignatureInteractionData;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugSignatureInteractionMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.SignatureMapper;

@Default
public class DefaultDrugSignatureInteractionMapper implements DrugSignatureInteractionMapper {

  @Inject
  private DrugMapper drugMapper;

  @Inject
  private SignatureMapper signatureMapper;

  @Override
  public DrugSignatureInteractionData[] toDrugSignatureInteractionData(
    Stream<DrugSignatureInteraction> interactions, boolean includeSummary
  ) {
    PredictionSummaryGenerator predictionSummaryGenerator =
      includeSummary ? new PredictionSummaryGenerator() : new EmptyPredictionSummaryGenerator();

    return interactions
      .map(i -> this.toDrugSignatureInteractionData(i, predictionSummaryGenerator))
      .toArray(DrugSignatureInteractionData[]::new);
  }

  private DrugSignatureInteractionData toDrugSignatureInteractionData(
    DrugSignatureInteraction interaction, PredictionSummaryGenerator predictionSummaryGenerator
  ) {
    return new DrugSignatureInteractionData(
      this.drugMapper.toDrugData(interaction.getDrug()),
      this.signatureMapper.toSignatureData(interaction.getSignature()),
      interaction.getInteractionType(),
      interaction.getTau(),
      interaction.getUpFdr(),
      interaction.getDownFdr(),
      predictionSummaryGenerator.interpretation(interaction)
    );
  }

  public String toDrugSignatureInteractionCsvData(Stream<DrugSignatureInteraction> interactions) {
    PredictionSummaryGenerator signatureSummaryGenerator = new PredictionSummaryGenerator();
    StringBuilder sb = new StringBuilder();
    sb.append(
      "drug_common_name,drug_pubchem_id,summary,"
        + "case_cell_type,case_cell_type_ontology_id,case_cell_subtype,"
        + "case_cell_subtype_ontology_id,reference_cell_type,"
        + "reference_cell_type_ontology_id,reference_cell_subtype,"
        + "reference_cell_subtype_ontology_id,"
        + "signature_name,up_fdr,down_fdr,tau,"
        + "drug_specificity_score,drug_status,drug_moa,"
        + "drug_target_gene_names,drug_target_gene_ids,"
        + "prediction_type,organism,experimental_design\n"
    );
    

    interactions.forEach(i -> {
      String cellTypeAOntologyId = i.getSignature().getCellTypeAOntologyId();
      String cellSubTypeAOntologyId = i.getSignature().getCellSubTypeAOntologyId();
      String cellTypeBOntologyId = i.getSignature().getCellTypeBOntologyId();
      String cellSubTypeBOntologyId = i.getSignature().getCellSubTypeBOntologyId();
      
      List<String> drugTargetGeneNames =
        i.getDrug().getTargetGenes().stream().map(DrugTargetGene::getGeneName).collect(toList());
      List<String> drugTargetGeneIds =
        i.getDrug().getTargetGenes().stream().map(DrugTargetGene::getGeneId).collect(toList());
  
      sb
        .append("\"").append(i.getDrug().getCommonName()).append("\"").append(",")
        .append("\"").append(i.getDrug().getPubChemId()).append("\"").append(",")

        .append("\"").append(signatureSummaryGenerator.interpretation(i)).append("\"").append(",")

        .append("\"").append(i.getSignature().getCellTypeA()).append("\"").append(",")
        .append("\"").append(cellTypeAOntologyId == null ? "" : cellTypeAOntologyId).append("\"").append(",")
        
        .append("\"").append(i.getSignature().getCellSubTypeA()).append("\"").append(",")
        .append("\"").append(cellSubTypeAOntologyId == null ? "" : cellSubTypeAOntologyId).append("\"").append(",")
        
        .append("\"").append(i.getSignature().getCellTypeB()).append("\"").append(",")
        .append("\"").append(cellTypeBOntologyId == null ? "" : cellTypeBOntologyId).append("\"").append(",")
        
        .append("\"").append(i.getSignature().getCellSubTypeB()).append("\"").append(",")
        .append("\"").append(cellSubTypeBOntologyId == null ? "" : cellSubTypeBOntologyId).append("\"").append(",")

        .append("\"").append(i.getSignature().getSignatureName()).append("\"").append(",")

        .append(i.getUpFdr() == null ? "" : i.getUpFdr()).append(",")
        .append(i.getDownFdr() == null ? "" : i.getDownFdr()).append(",")
        .append(i.getTau()).append(",")

        .append(i.getDrug().getDss() == null ? "" : i.getDrug().getDss()).append(",")
        .append("\"").append(i.getDrug().getStatus()).append("\"").append(",")
        .append("\"").append(collectionString(i.getDrug().getMoa())).append("\"").append(",")
        
        .append("\"").append(collectionString(drugTargetGeneNames)).append("\"").append(",")
        .append("\"").append(collectionString(drugTargetGeneIds)).append("\"").append(",")

        .append("\"").append(i.getInteractionType().toString()).append("\"").append(",")
        .append("\"").append(i.getSignature().getOrganism()).append("\"").append(",")
        .append("\"").append(i.getSignature().getExperimentalDesign()).append("\"")
        .append("\n");
    });

    return sb.toString();
  }

  private static String collectionString(Collection<String> elements) {
    return elements.stream().collect(joining(", "));
  }
}
