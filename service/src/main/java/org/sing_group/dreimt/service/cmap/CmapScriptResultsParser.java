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
package org.sing_group.dreimt.service.cmap;

import static java.nio.file.Files.lines;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import org.sing_group.dreimt.service.spi.query.cmap.CmapResultData;

public class CmapScriptResultsParser {

  public static Stream<CmapResultData> parse(File resultsFile) throws IOException {
    return lines(resultsFile.toPath())
      .skip(1)
      .map(CmapScriptResultsParser::map);
  }

  private static CmapResultData map(String line) {
    String[] fields = line.split("\t");
    if (fields.length == 12) {
      int drugId = Integer.valueOf(fields[0].replaceAll("\"", "").replaceAll("sig_", ""));
      double tes = Double.valueOf(fields[2]);
      double pValue = Double.valueOf(fields[3]);
      double fdr = Double.valueOf(fields[4]);
      String drugCommonName = fields[6];
      String drugSource = fields[7];
      String drugSourceName = fields[8];

      return new DefaultCmapResultData(
        drugId, tes, pValue, fdr, drugCommonName, drugSource, drugSourceName
      );
    } else {
      throw new RuntimeException(
        "Error, the following line does not have the expected number of "
          + "fields (12): " + line
      );
    }
  }

  private static class DefaultCmapResultData implements CmapResultData {
    private int drugId;
    private double tes;
    private double pValue;
    private double fdr;
    private String drugCommonName;
    private String drugSource;
    private String drugSourceName;

    public DefaultCmapResultData(
      int drugId, double tes, double pValue, double fdr,
      String drugCommonName, String drugSource, String drugSourceName
    ) {
      super();
      this.drugId = drugId;
      this.tes = tes;
      this.pValue = pValue;
      this.fdr = fdr;
      this.drugCommonName = drugCommonName;
      this.drugSource = drugSource;
      this.drugSourceName = drugSourceName;
    }

    @Override
    public int getDrugId() {
      return this.drugId;
    }

    @Override
    public double getTes() {
      return tes;
    }

    @Override
    public double getpValue() {
      return pValue;
    }

    @Override
    public double getFdr() {
      return fdr;
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
