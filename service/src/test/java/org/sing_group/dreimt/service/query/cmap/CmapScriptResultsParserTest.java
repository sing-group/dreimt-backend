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

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sing_group.dreimt.service.query.cmap.CmapScriptResultsParser;
import org.sing_group.dreimt.service.spi.query.cmap.CmapResultData;

public class CmapScriptResultsParserTest {

  private File testGeneSetResultsFile = new File("src/test/resources/cmap/sample_output_geneset.tsv");
  private File testSignatureResultsFile = new File("src/test/resources/cmap/sample_output_signature.tsv");

  @Test
  public void testParseGeneSetResults() throws IOException {
    List<CmapResultData> results =
      CmapScriptResultsParser.parseGeneSetResults(testGeneSetResultsFile).collect(toList());
    assertEquals(20, results.size());

    CmapResultData firstResult = results.get(0);
    assertEquals(firstResult.getDrugId(), 157);
    assertEquals(firstResult.getTau(), -99.813223757938d, 0.0d);
    assertEquals(firstResult.getUpFdr(), 0.00956937799043062, 0.0d);
    assertFalse(firstResult.getDownFdr().isPresent());
    assertEquals(firstResult.getDrugCommonName(), "estradiol-benzoate");
    assertEquals(firstResult.getDrugSourceDb(), "LINCS");
    assertEquals(firstResult.getDrugSourceName(), "BRD-A36066264");
  }

  @Test
  public void testParseSignatureResults() throws IOException {
    List<CmapResultData> results =
      CmapScriptResultsParser.parseSignatureResults(testSignatureResultsFile).collect(toList());
    assertEquals(20, results.size());

    CmapResultData firstResult = results.get(0);
    assertEquals(firstResult.getDrugId(), 157);
    assertEquals(firstResult.getTau(), -99.813223757938d, 0.0d);
    assertEquals(firstResult.getUpFdr(), 0.00956937799043062, 0.0d);
    assertEquals(firstResult.getDownFdr().get().doubleValue(), 0.00278551532033426d, 0.0d);
    assertEquals(firstResult.getDrugCommonName(), "estradiol-benzoate");
    assertEquals(firstResult.getDrugSourceDb(), "LINCS");
    assertEquals(firstResult.getDrugSourceName(), "BRD-A36066264");
  }
}
