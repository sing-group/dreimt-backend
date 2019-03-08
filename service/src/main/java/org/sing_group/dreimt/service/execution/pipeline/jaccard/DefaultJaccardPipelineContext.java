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
package org.sing_group.dreimt.service.execution.pipeline.jaccard;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapData;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipeline;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineConfiguration;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineEventManager;

public class DefaultJaccardPipelineContext implements JaccardPipelineContext, Serializable {
  private static final long serialVersionUID = 1L;

  private transient final JaccardPipeline pipeline;
  private final JaccardPipelineConfiguration configuration;
  private transient final JaccardPipelineEventManager eventManager;
  private final Set<String> targetSignatureIds;
  private List<GeneOverlapData> geneOverlaps;
  private Map<GeneOverlapData, Double> correctedPvaluesMap;

  DefaultJaccardPipelineContext(
    JaccardPipeline pipeline,
    JaccardPipelineConfiguration configuration,
    JaccardPipelineEventManager eventManager,
    Set<String> targetSignatureIds,
    List<GeneOverlapData> geneOverlaps,
    Map<GeneOverlapData, Double> correctedPvaluesMap
  ) {
    this.pipeline = pipeline;
    this.configuration = configuration;
    this.eventManager = eventManager;
    this.targetSignatureIds = targetSignatureIds;
    this.geneOverlaps = geneOverlaps;
    this.correctedPvaluesMap = correctedPvaluesMap;
  }

  @Override
  public JaccardPipelineConfiguration getConfiguration() {
    return this.configuration;
  }

  @Override
  public JaccardPipelineEventManager getEventManager() {
    return this.eventManager;
  }

  @Override
  public JaccardPipeline getPipeline() {
    return this.pipeline;
  }

  @Override
  public Optional<Stream<String>> getTargetSignatureIds() {
    return ofNullable(this.targetSignatureIds).map(Set::stream);
  }

  @Override
  public Optional<Stream<GeneOverlapData>> getGeneOverlapResultsData() {
    return ofNullable(this.geneOverlaps).map(List::stream);
  }

  @Override
  public Optional<Map<GeneOverlapData, Double>> getCorrectedPvaluesMap() {
    return ofNullable(this.correctedPvaluesMap);
  }
}
