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
package org.sing_group.dreimt.service.signature.geneoverlap;

import java.util.HashSet;
import java.util.Set;

import es.uvigo.ei.sing.math.statistical.tests.FisherExactTest;

public class GeneOverlapCalculator {

  /**
   * This function does the same than the {@code testGeneOverlap} function fron the {@code GeneOverlap} Bioconductor
   * package (http://bioconductor.org/packages/release/bioc/html/GeneOverlap.html): calculates the Jaccard index of the
   * two sets and performs a one-tailed Fisher's exact test based on the information supplied (the two gene lists and
   * the universe size, which specifies the number of testable genes).
   * 
   * @param a a set of gene identifiers
   * @param b a set of gene identifiers
   * @param universeSize the number of testable genes
   * @return a {@code GeneOverlapResult}
   */
  public static GeneOverlapResult geneOverlap(Set<String> a, Set<String> b, int universeSize) {
    Set<String> union = new HashSet<>(a);
    union.addAll(b);

    Set<String> intersection = new HashSet<>(a);
    intersection.retainAll(b);

    double jaccard = (double) intersection.size() / (double) union.size();

    int notAB = universeSize - union.size();
    int onlyA = diff(a, b).size();
    int onlyB = diff(b, a).size();
    int bothAB = intersection.size();

    double pValue = new FisherExactTest().getRightTailedP(notAB, onlyA, onlyB, bothAB);

    return new GeneOverlapResult(jaccard, pValue);
  }

  private static Set<String> diff(Set<String> a, Set<String> b) {
    Set<String> setA = new HashSet<>(a);
    setA.removeAll(b);

    return setA;
  }
}
