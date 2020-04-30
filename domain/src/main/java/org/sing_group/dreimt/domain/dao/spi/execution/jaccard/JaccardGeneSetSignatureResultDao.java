/*-
 * #%L
 * DREIMT - Domain
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
package org.sing_group.dreimt.domain.dao.spi.execution.jaccard;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardGeneSetSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;
import org.sing_group.dreimt.domain.entities.signature.GeneSetType;

public interface JaccardGeneSetSignatureResultDao {
  Optional<JaccardGeneSetSignatureResult> get(String id);

  JaccardGeneSetSignatureResult create(
    String name,
    String description,
    Function<String, String> resultReferenceBuilder,
    boolean onlyUniverseGenes,
    String cellType1,
    String cellSubType1,
    String cellType2,
    String cellSubType2,
    ExperimentalDesign experimentalDesign,
    String organism,
    String disease,
    String signatureSourceDb,
    Set<String> genes,
    GeneSetType geneSetType
  );
}
