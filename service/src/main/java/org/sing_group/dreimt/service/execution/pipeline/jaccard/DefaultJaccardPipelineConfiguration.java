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

import org.sing_group.dreimt.domain.dao.signature.SignatureListingOptions;
import org.sing_group.dreimt.service.spi.execution.pipeline.jaccard.JaccardPipelineConfiguration;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardServiceConfiguration;

public class DefaultJaccardPipelineConfiguration implements JaccardPipelineConfiguration {

  private String workId;
  private String resultId;
  private SignatureListingOptions signatureListingOptions;
  private JaccardServiceConfiguration jaccardServiceConfiguration;

  public DefaultJaccardPipelineConfiguration(
    String workId, String resultId, SignatureListingOptions signatureListingOptions,
    JaccardServiceConfiguration jaccardServiceConfiguration
  ) {
    this.workId = workId;
    this.resultId = resultId;
    this.signatureListingOptions = signatureListingOptions;
    this.jaccardServiceConfiguration = jaccardServiceConfiguration;
  }

  @Override
  public String getWorkId() {
    return this.workId;
  }

  @Override
  public String getResultId() {
    return this.resultId;
  }

  @Override
  public SignatureListingOptions getSignatureListingOptions() {
    return this.signatureListingOptions;
  }

  @Override
  public JaccardServiceConfiguration getJaccardServiceConfiguration() {
    return jaccardServiceConfiguration;
  }
}
