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

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

import java.util.Set;
import java.util.stream.Stream;

import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
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
  public void setUriBuilder(UriBuilder uriBuilder) {
    this.signatureMapper.setUriBuilder(requireNonNull(uriBuilder));
  }

  @Override
  public DrugSignatureInteractionData[] toDrugSignatureInteractionData(Stream<DrugSignatureInteraction> interactions) {
    return interactions.map(this::toDrugSignatureInteractionData).toArray(DrugSignatureInteractionData[]::new);
  }

  private DrugSignatureInteractionData toDrugSignatureInteractionData(DrugSignatureInteraction interaction) {
    return new DrugSignatureInteractionData(
      this.drugMapper.toDrugData(interaction.getDrug()),
      this.signatureMapper.toSignatureData(interaction.getSignature()),
      interaction.getInteractionType(), interaction.getTau(),
      interaction.getUpFdr(), interaction.getDownFdr()
    );
  }

  public String toDrugSignatureInteractionCsvData(Stream<DrugSignatureInteraction> interactions) {
    SignatureSummaryGenerator signatureSummaryGenerator = new SignatureSummaryGenerator();
    StringBuilder sb = new StringBuilder();
    sb.append(
      "drug_common_name,summary,"
        + "case_cell_type,case_cell_subtype,reference_cell_type,reference_cell_subtype,"
        + "signature_name,up_fdr,down_fdr,tau,"
        + "drug_specificity_score,drug_status,drug_moa,"
        + "prediction_type,organism,experimental_design\n"
    );

    interactions.forEach(i -> {
      sb
        .append("\"").append(i.getDrug().getCommonName()).append("\"").append(",")
        .append("\"").append(signatureSummaryGenerator.interpretation(i)).append("\"").append(",")

        .append("\"").append(setString(i.getSignature().getCellTypeA())).append("\"").append(",")
        .append("\"").append(setString(i.getSignature().getCellSubTypeA())).append("\"").append(",")
        .append("\"").append(setString(i.getSignature().getCellTypeB())).append("\"").append(",")
        .append("\"").append(setString(i.getSignature().getCellSubTypeB())).append("\"").append(",")

        .append("\"").append(i.getSignature().getSignatureName()).append("\"").append(",")

        .append(i.getUpFdr() == null ? "" : i.getUpFdr()).append(",")
        .append(i.getDownFdr() == null ? "" : i.getDownFdr()).append(",")
        .append(i.getTau()).append(",")

        .append(i.getDrug().getDss() == null ? "" : i.getDrug().getDss()).append(",")
        .append("\"").append(i.getDrug().getStatus()).append("\"").append(",")
        .append("\"").append(setString(i.getDrug().getMoa())).append("\"").append(",")

        .append("\"").append(i.getInteractionType().toString()).append("\"").append(",")
        .append("\"").append(i.getSignature().getOrganism()).append("\"").append(",")
        .append("\"").append(i.getSignature().getExperimentalDesign()).append("\"")
        .append("\n");
    });

    return sb.toString();
  }

  private static String setString(Set<String> elements) {
    return elements.stream().collect(joining(", "));
  }
}
