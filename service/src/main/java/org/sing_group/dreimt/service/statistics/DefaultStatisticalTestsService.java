/*-
 * #%L
 * DREIMT - Service
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
package org.sing_group.dreimt.service.statistics;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;

import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.sing_group.dreimt.service.spi.statistics.HypergeometricTestData;
import org.sing_group.dreimt.service.spi.statistics.HypergeometricTestDataResult;
import org.sing_group.dreimt.service.spi.statistics.StatisticalTestsService;

import es.uvigo.ei.sing.math.statistical.StatisticsTestsUtils;
import es.uvigo.ei.sing.math.statistical.corrections.FDRCorrection;

@Stateless
@PermitAll
public class DefaultStatisticalTestsService implements StatisticalTestsService {

  @Override
  public double hypergeometricTest(int populationSize, int numberOfSuccesses, int sampleSize, int successes) {
    return new HypergeometricDistribution(populationSize, numberOfSuccesses, sampleSize)
      .upperCumulativeProbability(successes);
  }

  @Override
  public List<HypergeometricTestDataResult> hypergeometricTest(List<HypergeometricTestData> data) {
    Map<HypergeometricTestData, Double> pValuesMap = data.stream().collect(Collectors.toMap(d -> d, d -> {
      return this.hypergeometricTest(
        d.getPopulationSize(), d.getPopulationSuccess(), d.getSampleSize(), d.getSampleSuccess()
      );
    }));

    try {
      Map<HypergeometricTestData, Double> qValuesMap = StatisticsTestsUtils.correct(new FDRCorrection(), pValuesMap);
      return qValuesMap.keySet().stream().map(
        d -> new HypergeometricTestDataResult(
          d.getName(), d.getPopulationSize(), d.getPopulationSuccess(),
          d.getSampleSize(), d.getSampleSuccess(), pValuesMap.get(d), qValuesMap.get(d)
        )
      )
        .collect(toList());
    } catch (InterruptedException e) {
      throw new RuntimeException("An error ocurred while calculating FDR correction");
    }
  }
}
