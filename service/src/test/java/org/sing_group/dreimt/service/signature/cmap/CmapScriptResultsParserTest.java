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
package org.sing_group.dreimt.service.signature.cmap;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.sing_group.dreimt.service.cmap.CmapScriptResultsParser;
import org.sing_group.dreimt.service.spi.query.cmap.CmapResultData;

public class CmapScriptResultsParserTest {

  private File testResultsFile = new File("src/test/resources/cmap/sample_output.tsv");

  @Test
  public void testParseResults() throws IOException {
    List<CmapResultData> results = CmapScriptResultsParser.parse(testResultsFile).collect(toList());
    Assert.assertEquals(19, results.size());
  }
}