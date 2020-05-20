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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.sing_group.dreimt.service.spi.query.ValueDistribution;
import org.sing_group.dreimt.service.spi.query.ValuesDistribution;

public class DefaultValuesDistribution implements ValuesDistribution {

  private Map<String, ValueDistribution> map = new HashMap<>();

  public DefaultValuesDistribution() {}

  @Override
  public void addDistribution(ValueDistribution distribution) {
    this.map.put(distribution.getValueName(), distribution);
  }

  @Override
  public Optional<ValueDistribution> getDistribution(String valueName) {
    return Optional.ofNullable(this.map.get(valueName));
  }

  @Override
  public Set<String> getValueNames() {
    return this.map.keySet();
  }
}
