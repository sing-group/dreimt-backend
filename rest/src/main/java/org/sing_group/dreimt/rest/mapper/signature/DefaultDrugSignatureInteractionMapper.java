/*-
 * #%L
 * DREIMT - REST
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
package org.sing_group.dreimt.rest.mapper.signature;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import javax.ws.rs.core.UriBuilder;

import org.sing_group.dreimt.domain.entities.query.DrugSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;
import org.sing_group.dreimt.rest.entity.query.DrugSignatureInteractionListingOptionsData;
import org.sing_group.dreimt.rest.entity.signature.DrugSignatureInteractionData;
import org.sing_group.dreimt.rest.mapper.spi.query.ListingOptionsMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugSignatureInteractionMapper;
import org.sing_group.dreimt.rest.mapper.spi.signature.SignatureMapper;

public class DefaultDrugSignatureInteractionMapper implements DrugSignatureInteractionMapper {

  @Inject
  private DrugMapper drugMapper;

  @Inject
  private SignatureMapper signatureMapper;

  @Inject
  private ListingOptionsMapper listingOptionsMapper;

  @Override
  public void setUriBuilder(UriBuilder uriBuilder) {
    this.signatureMapper.setUriBuilder(requireNonNull(uriBuilder));
  }

  @Override
  public DrugSignatureInteractionData toDrugSignatureInteractionData(DrugSignatureInteraction interaction) {
    return new DrugSignatureInteractionData(
      this.drugMapper.toDrugData(interaction.getDrug()),
      this.signatureMapper.toSignatureData(interaction.getSignature()),
      interaction.getTes(), interaction.getpValue(), interaction.getFdr()
    );
  }

  @Override
  public DrugSignatureInteractionListingOptions toDrugSignatureInteractionListingOptions(
    DrugSignatureInteractionListingOptionsData listingOptionsData
  ) {
    return new DrugSignatureInteractionListingOptions(
      listingOptionsMapper.toListingOptions(listingOptionsData.getListingOptions()),
      listingOptionsData.getCellTypeA(), listingOptionsData.getCellTypeB(),
      listingOptionsData.getExperimentalDesign(), listingOptionsData.getOrganism(),
      listingOptionsData.getDrugSourceName(), listingOptionsData.getDrugCommonName()
    );
  }
}
