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

import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.GeneOverlapData;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipeline;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineConfiguration;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContext;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineContextBuilder;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineEventManager;

public class DefaultJaccardPipelineContextBuilder implements JaccardPipelineContextBuilder {

  private JaccardPipeline pipeline;
  private JaccardPipelineConfiguration configuration;
  private JaccardPipelineEventManager eventManager;
  private Set<String> targetSignatureIds;
  private Map<String, List<GeneOverlapData>> targetSignatureOverlaps;
  private Map<GeneOverlapData, Double> correctedPvaluesMap;

  public DefaultJaccardPipelineContextBuilder(JaccardPipelineContext context) {
    this(
      context.getPipeline(), context.getConfiguration(), context.getEventManager(),
      context.getTargetSignatureIds().map(signatures -> signatures.collect(toSet())).orElse(null),
      context.getTargetSignatureOverlaps().map(HashMap::new).orElse(null),
      context.getCorrectedPvaluesMap().map(HashMap::new).orElse(null)
    );
  }

  public DefaultJaccardPipelineContextBuilder(
    JaccardPipeline pipeline, JaccardPipelineConfiguration configuration, JaccardPipelineEventManager eventManager
  ) {
    this(pipeline, configuration, eventManager, null, null, null);
  }

  public DefaultJaccardPipelineContextBuilder(
    JaccardPipeline pipeline,
    JaccardPipelineConfiguration configuration,
    JaccardPipelineEventManager eventManager,
    Set<String> targetSignatureIds,
    Map<String, List<GeneOverlapData>> targetSignatureOverlaps,
    Map<GeneOverlapData, Double> correctedPvaluesMap
  ) {
    this.pipeline = pipeline;
    this.configuration = configuration;
    this.eventManager = eventManager;
    this.targetSignatureIds = targetSignatureIds;
    this.targetSignatureOverlaps = targetSignatureOverlaps;
    this.correctedPvaluesMap = correctedPvaluesMap;
  }

  @Override
  public JaccardPipelineContextBuilder addTargetSignatureIds(Set<String> targetSignatureIds) {
    this.targetSignatureIds = targetSignatureIds;
    return this;
  }

  @Override
  public JaccardPipelineContextBuilder addGeneOverlaps(String signatureName, List<GeneOverlapData> geneOverlaps) {
    if (this.targetSignatureOverlaps == null) {
      this.targetSignatureOverlaps = new HashMap<>();
    }
    this.targetSignatureOverlaps.put(signatureName, geneOverlaps);

    return this;
  }
  
  @Override
  public JaccardPipelineContextBuilder addCorrectedPvalues(Map<GeneOverlapData, Double> correctedPvaluesMap) {
    this.correctedPvaluesMap = correctedPvaluesMap;
    return this;
  }

  @Override
  public JaccardPipelineContext build() {
    return new DefaultJaccardPipelineContext(
      pipeline, 
      configuration, 
      eventManager, 
      targetSignatureIds, 
      targetSignatureOverlaps,
      correctedPvaluesMap
    );
  }
}
