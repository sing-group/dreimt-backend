package org.sing_group.dreimt.domain.dao.execution.jaccard;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.Optional;

import org.sing_group.dreimt.domain.dao.ListingOptions;

public class GeneOverlapListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private final ListingOptions listingOptions;
  private final Double maxJaccard;
  private final Double maxPvalue;
  private final Double maxFdr;

  public GeneOverlapListingOptions(
    ListingOptions listingOptions,
    Double maxJaccard, Double pValue, Double maxFdr
  ) {
    this.listingOptions = listingOptions;
    this.maxJaccard = maxJaccard;
    this.maxPvalue = pValue;
    this.maxFdr = maxFdr;
  }

  public boolean hasAnyQueryModification() {
    return this.listingOptions.hasAnyQueryModification()
      || this.maxJaccard != null
      || this.maxPvalue != null
      || this.maxFdr != null;
  }

  public ListingOptions getListingOptions() {
    return listingOptions;
  }

  public Optional<Double> getMaxJaccard() {
    return ofNullable(maxJaccard);
  }

  public Optional<Double> getMaxPvalue() {
    return ofNullable(maxPvalue);
  }

  public Optional<Double> getMaxFdr() {
    return ofNullable(maxFdr);
  }
}
