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

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardComparisonType.GENESET;
import static org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardComparisonType.SIGNATURE_DOWN;
import static org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardComparisonType.SIGNATURE_UP;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapData;

public class JaccardScriptResultsParserTest {

  private File testFile = new File("src/test/resources/jaccard/sample_output.tsv");

  @Test
  public void testParseGeneSetResults() throws IOException {
    List<GeneOverlapData> results =
      JaccardScriptResultsParser.parseResults(testFile).collect(toList());
    assertEquals(7, results.size());

    assertEquals("KAECH_NAIVE_VS_DAY8_EFF_CD8_TCELL", results.get(0).getTargetSignatureName());
    assertEquals(SIGNATURE_UP, results.get(0).getSourceComparisonType());
    assertEquals(SIGNATURE_UP, results.get(0).getTargetComparisonType());
    assertEquals(0.00315457413249211d, results.get(0).getJaccard(), 0d);
    assertEquals(0.871222426799424, results.get(0).getPvalue(), 0d);

    assertEquals("KAECH_NAIVE_VS_DAY8_EFF_CD8_TCELL", results.get(1).getTargetSignatureName());
    assertEquals(SIGNATURE_UP, results.get(1).getSourceComparisonType());
    assertEquals(SIGNATURE_DOWN, results.get(1).getTargetComparisonType());

    assertEquals("Macrophage_core", results.get(2).getTargetSignatureName());
    assertEquals(SIGNATURE_UP, results.get(2).getSourceComparisonType());
    assertEquals(GENESET, results.get(3).getTargetComparisonType());

    assertEquals("Macrophage_core", results.get(3).getTargetSignatureName());
    assertEquals(SIGNATURE_DOWN, results.get(3).getSourceComparisonType());
    assertEquals(GENESET, results.get(3).getTargetComparisonType());

    assertEquals("KAECH_NAIVE_VS_DAY8_EFF_CD8_TCELL", results.get(4).getTargetSignatureName());
    assertEquals(GENESET, results.get(4).getSourceComparisonType());
    assertEquals(SIGNATURE_UP, results.get(4).getTargetComparisonType());

    assertEquals("KAECH_NAIVE_VS_DAY8_EFF_CD8_TCELL", results.get(5).getTargetSignatureName());
    assertEquals(GENESET, results.get(5).getSourceComparisonType());
    assertEquals(SIGNATURE_DOWN, results.get(5).getTargetComparisonType());

    assertEquals("Macrophage_core", results.get(6).getTargetSignatureName());
    assertEquals(GENESET, results.get(6).getSourceComparisonType());
    assertEquals(GENESET, results.get(6).getTargetComparisonType());
  }
}
