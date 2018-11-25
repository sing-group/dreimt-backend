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
package org.sing_group.dreimt.service.spi.query;

import java.util.Set;

import javax.ejb.Local;

@Local
public interface GeneListsValidationService {

  public static final int MINIMUM_GENESET_SIZE = 15;
  public static final int MAXIMUM_GENESET_SIZE = 200;

  void validateGeneListsSizes(
    Set<String> upGenes,
    Set<String> downGenes,
    int minimumGeneSetSize,
    int maximumGeneSetSize
  );

  boolean isValidGeneSet(
    Set<String> genes,
    int minimumGeneSetSize,
    int maximumGeneSetSize
  );
}
