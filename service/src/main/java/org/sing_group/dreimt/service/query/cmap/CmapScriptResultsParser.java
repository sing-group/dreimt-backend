/*-
 * #%L
 * DREIMT - Service
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
package org.sing_group.dreimt.service.query.cmap;

import static java.nio.file.Files.lines;
import static java.util.Optional.ofNullable;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.sing_group.dreimt.service.spi.query.cmap.CmapResultData;

public class CmapScriptResultsParser {

  public static Stream<CmapResultData> parseGeneSetResults(File resultsFile) throws IOException {
    return lines(resultsFile.toPath())
      .skip(1)
      .map(CmapScriptResultsParser::mapGeneSetResultsLine);
  }
    
  private static CmapResultData mapGeneSetResultsLine(String line) {
    String[] fields = line.split("\t");
    if (fields.length == 17) {
      int drugId = Integer.valueOf(fields[0].replaceAll("\"", "").replaceAll("sig_", ""));
      double upFdr = Double.valueOf(fields[2]);
      double tau = Double.valueOf(fields[5]);
      String drugCommonName = fields[7].replaceAll("\"", "");
      String drugSource = fields[8].replaceAll("\"", "");
      String drugSourceName = fields[9].replaceAll("\"", "");

      return new DefaultCmapResultData(
        drugId, tau, upFdr, drugCommonName, drugSource, drugSourceName
      );
    } else {
      throw new RuntimeException(
        "Error reading the Cmap results file: one line does not have the expected number of fields (15)"
      );
    }
  }
    
  public static Stream<CmapResultData> parseSignatureResults(File resultsFile) throws IOException {
    return lines(resultsFile.toPath())
      .skip(1)
      .map(CmapScriptResultsParser::mapSignatureResultsLine);
  }

  private static CmapResultData mapSignatureResultsLine(String line) {
    String[] fields = line.split("\t");
    if (fields.length == 20) {
      int drugId = Integer.valueOf(fields[0].replaceAll("\"", "").replaceAll("sig_", ""));
      double upFdr = Double.valueOf(fields[2]);
      double downFdr = Double.valueOf(fields[5]);
      double tau = Double.valueOf(fields[8]);
      String drugCommonName = fields[10].replaceAll("\"", "");
      String drugSource = fields[11].replaceAll("\"", "");
      String drugSourceName = fields[12].replaceAll("\"", "");

      return new DefaultCmapResultData(
        drugId, tau, upFdr, downFdr, drugCommonName, drugSource, drugSourceName
      );
    } else {
      throw new RuntimeException(
        "Error reading the Cmap results file: one line does not have the expected number of fields (18)"
      );
    }
  }

  private static class DefaultCmapResultData implements CmapResultData {
    private int drugId;
    private double tau;
    private double upFdr;
    private Double downFdr;
    private String drugCommonName;
    private String drugSource;
    private String drugSourceName;

    public DefaultCmapResultData(
      int drugId, double tau, double upFdr, 
      String drugCommonName, String drugSource, String drugSourceName
    ) {
      this(drugId, tau, upFdr, null, drugCommonName, drugSource, drugSourceName);
    }

    public DefaultCmapResultData(
      int drugId, double tau, double upFdr, Double downFdr,
      String drugCommonName, String drugSource, String drugSourceName
    ) {
      super();
      this.drugId = drugId;
      this.tau = tau;
      this.upFdr = upFdr;
      this.downFdr = downFdr;
      this.drugCommonName = drugCommonName;
      this.drugSource = drugSource;
      this.drugSourceName = drugSourceName;
    }

    @Override
    public int getDrugId() {
      return this.drugId;
    }

    @Override
    public double getTau() {
      return tau;
    }

    @Override
    public double getUpFdr() {
      return upFdr;
    }

    @Override
    public Optional<Double> getDownFdr() {
      return ofNullable(downFdr);
    }

    @Override
    public String getDrugCommonName() {
      return drugCommonName;
    }

    @Override
    public String getDrugSourceDb() {
      return drugSource;
    }

    @Override
    public String getDrugSourceName() {
      return drugSourceName;
    }
  }
}
