/*-
 * #%L
 * DREIMT - Service
 * %%
 * Copyright (C) 2018 - 2019 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
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
package org.sing_group.dreimt.service.query.jaccard;

import static java.nio.file.Files.lines;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardComparisonType;
import org.sing_group.dreimt.service.execution.pipeline.jaccard.DefaultGeneOverlapData;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapData;

public class JaccardScriptResultsParser {

  public static Stream<GeneOverlapData> parseResults(File resultsFile) throws IOException {
    return lines(resultsFile.toPath())
      .skip(1)
      .map(JaccardScriptResultsParser::mapResultsLine);
  }

  private static GeneOverlapData mapResultsLine(String line) {
    String[] fields = line.split("\t");
    if (fields.length == 5) {
      String targetSignatureName = fields[0].replaceAll("\"", "");
      double jaccard = Double.valueOf(fields[1].replaceAll("\"", ""));
      double pValue = Double.valueOf(fields[2].replaceAll("\"", ""));
      String sourceComparisonTypeString = fields[4].replaceAll("\"", "");

      JaccardComparisonType sourceComparisonType = null;
      if (sourceComparisonTypeString.equalsIgnoreCase("Geneset")) {
        sourceComparisonType = JaccardComparisonType.GENESET;
      } else if (sourceComparisonTypeString.equalsIgnoreCase("Up signature")) {
        sourceComparisonType = JaccardComparisonType.SIGNATURE_UP;
      } else if (sourceComparisonTypeString.equalsIgnoreCase("Down signature")) {
        sourceComparisonType = JaccardComparisonType.SIGNATURE_DOWN;
      }

      JaccardComparisonType targetComparisonType = null;
      if (targetSignatureName.endsWith("_UP")) {
        targetComparisonType = JaccardComparisonType.SIGNATURE_UP;
        targetSignatureName = targetSignatureName.substring(0, targetSignatureName.length() - 3);
      } else if (targetSignatureName.endsWith("_DN")) {
        targetComparisonType = JaccardComparisonType.SIGNATURE_DOWN;
        targetSignatureName = targetSignatureName.substring(0, targetSignatureName.length() - 3);
      } else if (targetSignatureName.endsWith("_sig")) {
        targetComparisonType = JaccardComparisonType.GENESET;
        targetSignatureName = targetSignatureName.substring(0, targetSignatureName.length() - 4);
      }

      return new DefaultGeneOverlapData(
        sourceComparisonType, targetSignatureName, targetComparisonType, jaccard, pValue
      );
    } else {
      throw new RuntimeException(
        "Error, the following line does not have the expected number of fields (5): " + line
      );
    }
  }
}
