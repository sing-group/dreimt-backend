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
package org.sing_group.dreimt.service.spi.statistics;

import org.sing_group.dreimt.service.statistics.OddsRatio;

public class HypergeometricTestData {
  private String name;
  private int populationSize;
  private int populationSuccess;
  private int sampleSize;
  private int sampleSuccess;
  private OddsRatio oddsRatio;

  public HypergeometricTestData() {}

  public HypergeometricTestData(
    String name, int populationSize, int populationSuccess, int sampleSize, int sampleSuccess
  ) {
    this.name = name;
    this.populationSize = populationSize;
    this.populationSuccess = populationSuccess;
    this.sampleSize = sampleSize;
    this.sampleSuccess = sampleSuccess;
  }

  public String getName() {
    return this.name;
  }

  public int getPopulationSize() {
    return populationSize;
  }

  public int getPopulationSuccess() {
    return populationSuccess;
  }

  public int getSampleSize() {
    return sampleSize;
  }

  public int getSampleSuccess() {
    return sampleSuccess;
  }

  public double getOddsRatio() {
    if (this.oddsRatio == null) {
      this.oddsRatio =
        new OddsRatio(
          this.sampleSuccess, this.sampleSize - this.sampleSuccess, this.populationSuccess - this.sampleSuccess,
          this.populationSize - this.sampleSize - this.populationSuccess + this.sampleSuccess
        );
    }
    return this.oddsRatio.get();
  }
}
