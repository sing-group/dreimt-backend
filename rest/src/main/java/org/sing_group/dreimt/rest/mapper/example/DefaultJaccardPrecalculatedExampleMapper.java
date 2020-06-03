/*-
 * #%L
 * DREIMT - REST
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
package org.sing_group.dreimt.rest.mapper.example;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.entities.example.JaccardPrecalculatedExample;
import org.sing_group.dreimt.rest.entity.example.SignaturesComparisonExampleData;
import org.sing_group.dreimt.rest.mapper.spi.example.JaccardPrecalculatedExampleMapper;
import org.sing_group.dreimt.rest.mapper.spi.execution.ExecutionMapper;

@Default
public class DefaultJaccardPrecalculatedExampleMapper implements JaccardPrecalculatedExampleMapper {

  @Inject
  private ExecutionMapper executionMapper;

  @Override
  public SignaturesComparisonExampleData toSignaturesComparisonPrecalculatedExampleData(JaccardPrecalculatedExample example) {
    return new SignaturesComparisonExampleData(
      example.getWork().getName(),
      example.getWork().getDescription().orElse(null),
      example.getReference(),
      example.getUrl(),
      executionMapper.toWorkData(example.getWork())
    );
  }
}
