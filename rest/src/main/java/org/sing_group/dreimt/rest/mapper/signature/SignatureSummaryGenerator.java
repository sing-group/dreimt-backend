/*-
 * #%L
 * DREIMT - REST
 * %%
 * Copyright (C) 2018 - 2020 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteractionType;

public class SignatureSummaryGenerator {
  private static final String[] COLLAPSE_TREATMENTS = new String[] {"Overexpression", "Knock-out model"};
  private final Map<String, List<String>> MAP_TREATMENT_A = new HashMap<>();
  private final Map<String, List<String>> MAP_TREATMENT_B = new HashMap<>();
  
  public String interpretation(DrugSignatureInteraction interaction) {
    String effect = "boosts";
    if (interaction.getTau() < 0) {
      effect = "inhibits";
    }

    if (interaction.getInteractionType().equals(DrugSignatureInteractionType.SIGNATURE_DOWN)) {
      return interpretation(
        interaction.getSignature().getSignatureName(), effect, interaction.getDrug().getCommonName(),
        interaction.getSignature().getStateB(), interaction.getSignature().getCellSubTypeB(),
        interaction.getSignature().getTreatmentB(), interaction.getSignature().getDiseaseB(),
        interaction.getSignature().getStateA(), interaction.getSignature().getCellSubTypeA(),
        interaction.getSignature().getTreatmentA(), interaction.getSignature().getDiseaseA()
      );
    } else {
      return interpretation(
        interaction.getSignature().getSignatureName(), effect, interaction.getDrug().getCommonName(),
        interaction.getSignature().getStateA(), interaction.getSignature().getCellSubTypeA(),
        interaction.getSignature().getTreatmentA(), interaction.getSignature().getDiseaseA(),
        interaction.getSignature().getStateB(), interaction.getSignature().getCellSubTypeB(),
        interaction.getSignature().getTreatmentB(), interaction.getSignature().getDiseaseB()
      );
    }
  }
    
  private String interpretation(
    String signatureName, String effect, String drugCommonName, String stateA, Set<String> cellSubTypeA,
    Set<String> treatmentA, Set<String> diseaseA, String stateB, Set<String> cellSubTypeB, Set<String> treatmentB,
    Set<String> diseaseB
  ) {
    String treatmentAStr = treatmentA.isEmpty() ? "" : concat(collapseTreatments(treatmentA, signatureName, MAP_TREATMENT_A));
    String treatmentBStr = treatmentB.isEmpty() ? "" : concat(collapseTreatments(treatmentB, signatureName, MAP_TREATMENT_B));
    String diseaseAStr = diseaseA.isEmpty() ? "" : concat(new LinkedList<String>(diseaseA));
    String diseaseBStr = diseaseB.isEmpty() ? "" : concat(new LinkedList<String>(diseaseB));

    StringBuilder interpretation = new StringBuilder();
    interpretation
      .append(drugCommonName)
      .append(" ")
      .append(effect)
      .append(" ")
      .append(stateA)
      .append(stateA.isEmpty() ? "" : " ")
      .append(concat(new LinkedList<>(cellSubTypeA)));

    if (!treatmentAStr.isEmpty()) {
      interpretation
        .append(" stimulated with ")
        .append(treatmentAStr);
    }
    if (!diseaseAStr.isEmpty()) {
      interpretation
        .append(" in ")
        .append(diseaseAStr);
    }
      
    interpretation
      .append(" compared to ")
      .append(stateB)
      .append(stateB.isEmpty() ? "" : " ")
      .append(concat(new LinkedList<>(cellSubTypeB)));

    if (!treatmentBStr.isEmpty()) {
      interpretation
        .append(" stimulated with ")
        .append(treatmentBStr);
    }
    if (!diseaseBStr.isEmpty()) {
      interpretation
        .append(" in ")
        .append(diseaseBStr);
    }
    
    return interpretation.toString();
  }

  private static List<String> collapseTreatments(
    Set<String> treatments, String signatureName, Map<String, List<String>> signatureTreatment
  ) {
    if(!signatureTreatment.containsKey(signatureName)) {
       Map<String, List<String>> collapseMap = new HashMap<>();
       List<String> result = new LinkedList<>();
       
       for(String treatment: treatments) {
         boolean collapse = false;
         
         for(String keyword : COLLAPSE_TREATMENTS) {
          if(treatment.startsWith(keyword)) {
            String treatmentValue = treatment.replace(keyword, "").replace("[", "").replace("]", "").trim();
            collapseMap.putIfAbsent(keyword, new LinkedList<>());
            collapseMap.get(keyword).add(treatmentValue);
            collapse = true;
            break;
          }
         }

         if(!collapse) {
           result.add(treatment);
         }
       }
       
       for(String treatment : collapseMap.keySet()) {
         StringBuilder sb = new StringBuilder();
         sb
           .append(treatment)
           .append(" (")
           .append(collapseMap.get(treatment).stream().collect(Collectors.joining(", ")))
           .append(")");
         result.add(sb.toString());
       }
       
       signatureTreatment.put(signatureName, result);
    }
    return signatureTreatment.get(signatureName);
  }
  
  private static String concat(List<String> data) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < data.size(); i++) {
      if (i > 0 && i == (data.size() - 1)) {
        result.append(" and ");
      } else if (i > 0) {
        result.append(", ");
      }
      result.append(data.get(i));
    }

    return result.toString();
  }
}
