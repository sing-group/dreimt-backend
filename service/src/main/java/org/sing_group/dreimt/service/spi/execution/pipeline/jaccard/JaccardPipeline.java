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
package org.sing_group.dreimt.service.spi.execution.pipeline.jaccard;

import org.sing_group.dreimt.service.spi.execution.pipeline.Pipeline;

public interface JaccardPipeline
extends Pipeline<
  JaccardPipelineConfiguration,
  JaccardPipelineContext,
  JaccardPipelineStep,
  JaccardPipeline,
  JaccardPipelineEvent,
  JaccardPipelineEventManager
> {
  public static final String SINGLE_RETRIEVE_SIGNATURES_STEP_ID = "SINGLE RETRIEVE SIGNATURES";
  public static final String SINGLE_JACCARD_COMPUTATION_STEP_ID = "SINGLE JACCARD COMPUTATION";
  public static final String MULTIPLE_JACCARD_COMPUTATION_STEP_ID = "MULTIPLE JACCARD COMPUTATION";
  public static final String SINGLE_FDR_CORRECTION_STEP_ID = "SINGLE FDR CORRECTION";
}
