package org.sing_group.dreimt.rest.mapper.query;

import org.sing_group.dreimt.domain.dao.ListingOptions;
import org.sing_group.dreimt.rest.entity.query.ListingOptionsData;
import org.sing_group.dreimt.rest.mapper.spi.query.ListingOptionsMapper;

public class DefaultListingOptionsMapper implements ListingOptionsMapper {

  @Override
  public ListingOptions toListingOptions(ListingOptionsData listingOptionsData) {
    final Integer start, end;

    if (listingOptionsData.hasPagination()) {
      start = listingOptionsData.getPage() * listingOptionsData.getPageSize();
      end = start + listingOptionsData.getPageSize() - 1;
    } else {
      start = end = null;
    }

    return new ListingOptions(start, end);
  }
}
