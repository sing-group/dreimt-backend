package org.sing_group.dreimt.rest.mapper.signature;

import javax.inject.Inject;

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
