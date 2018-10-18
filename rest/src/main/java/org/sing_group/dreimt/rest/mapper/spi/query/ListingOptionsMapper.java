package org.sing_group.dreimt.rest.mapper.spi.query;

import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;

public interface ListingOptionsMapper {
  public ListingOptions toListingOptions(ListingOptionsData listingOptionsData);
}
