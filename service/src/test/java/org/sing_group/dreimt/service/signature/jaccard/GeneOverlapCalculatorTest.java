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
package org.sing_group.dreimt.service.signature.jaccard;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.sing_group.dreimt.service.signature.geneoverlap.GeneOverlapCalculator.geneOverlap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.sing_group.dreimt.service.signature.geneoverlap.GeneOverlapResult;

@RunWith(Parameterized.class)
public class GeneOverlapCalculatorTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return asList(new Object[][] {
      {
        set("a", "b", "c", "d"), set("a", "b", "c", "d"), 10, new GeneOverlapResult(1d, 0.00476190476190475900)
      },
      {
        set("a", "b", "c", "d"), set("a", "b", "c", "d"), 20, new GeneOverlapResult(1d, 0.00020639834881320998)
      },
      {
        set("a", "b", "c", "d"), set("a", "b", "c", "e"), 20, new GeneOverlapResult(0.6d, 0.01341589267285860800)
      },
      {
        set("a", "b", "c", "d"), set("a", "b", "e", "f"), 20, new GeneOverlapResult(0.3333333333333333d, 0.16202270381837058000)
      },
      {
        set("a", "b", "c", "d"), set("e", "f", "g", "h"), 20, new GeneOverlapResult(0d, 1.00000000000000490000d)
      },
    });
  }

  private Set<String> a;
  private Set<String> b;
  private int universeSize;
  private GeneOverlapResult expectedResult;

  public GeneOverlapCalculatorTest(
    Set<String> a, Set<String> b, int universeSize, GeneOverlapResult expectedResult
  ) {
    this.a = a;
    this.b = b;
    this.universeSize = universeSize;
    this.expectedResult = expectedResult;
  }

  @Test
  public void test() {
    assertEquals(this.expectedResult, geneOverlap(a, b, universeSize));
  }

  private static Set<String> set(String... values) {
    return new HashSet<>(asList(values));
  }
}
