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
package org.sing_group.dreimt.service.query;

import java.util.Map;

import org.sing_group.dreimt.service.spi.query.ValueDistribution;

public class DefaultValueDistribution implements ValueDistribution {
  private String valueName;
  private Map<String, Integer> distribution;
  private Integer total;

  public DefaultValueDistribution(String valueName, Map<String, Integer> distribution) {
    this.valueName = valueName;
    this.distribution = distribution;
  }

  @Override
  public String getValueName() {
    return this.valueName;
  }

  @Override
  public Integer getTotal() {
    if (this.total == null) {
      this.total = this.distribution.values().stream().mapToInt(Integer::intValue).sum();
    }
    return this.total;
  }

  @Override
  public Map<String, Integer> getDistribution() {
    return this.distribution;
  }
}
