package org.sing_group.dreimt.service.spi.signature;

import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.dreimt.domain.entities.query.DrugSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.entities.signature.DrugSignatureInteraction;

@Local
public interface DrugSignatureInteractionService {
  public Stream<DrugSignatureInteraction> list(DrugSignatureInteractionListingOptions listingOptions);
}
