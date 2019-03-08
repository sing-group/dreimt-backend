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
package org.sing_group.dreimt.service.query.jaccard.event;

import java.io.Serializable;

import org.sing_group.dreimt.domain.dao.signature.SignatureListingOptions;
import org.sing_group.dreimt.service.spi.query.jaccard.JaccardServiceConfiguration;
import org.sing_group.dreimt.service.spi.query.jaccard.event.JaccardComputationRequestEvent;

public class DefaultJaccardComputationRequestEvent implements JaccardComputationRequestEvent, Serializable {
  private static final long serialVersionUID = 1L;

  private String workId;
  private SignatureListingOptions signatureListingOptions;
  private JaccardServiceConfiguration jaccardServiceConfiguration;

  public DefaultJaccardComputationRequestEvent(
    String workId, SignatureListingOptions signatureListingOptions,
    JaccardServiceConfiguration jaccardServiceConfiguration
  ) {
    this.workId = workId;
    this.signatureListingOptions = signatureListingOptions;
    this.jaccardServiceConfiguration = jaccardServiceConfiguration;
  }

  @Override
  public String getWorkId() {
    return workId;
  }

  @Override
  public SignatureListingOptions getSignatureListingOptions() {
    return this.signatureListingOptions;
  }

  @Override
  public JaccardServiceConfiguration getJaccardServiceConfiguration() {
    return this.jaccardServiceConfiguration;
  }
}
