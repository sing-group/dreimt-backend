/*-
 * #%L
 * DREIMT - Domain
 * %%
 * Copyright (C) 2018 - 2019 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
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
package org.sing_group.dreimt.domain.dao.execution.jaccard;

import static java.util.Optional.ofNullable;

import java.io.Serializable;
import java.util.Optional;

import org.sing_group.dreimt.domain.dao.ListingOptions;

public class GeneOverlapListingOptions implements Serializable {
  private static final long serialVersionUID = 1L;

  private final ListingOptions listingOptions;
  private final Double minJaccard;
  private final Double maxPvalue;
  private final Double maxFdr;

  public GeneOverlapListingOptions(
    ListingOptions listingOptions,
    Double minJaccard, Double pValue, Double maxFdr
  ) {
    this.listingOptions = listingOptions;
    this.minJaccard = minJaccard;
    this.maxPvalue = pValue;
    this.maxFdr = maxFdr;
  }

  public boolean hasAnyQueryModification() {
    return this.listingOptions.hasAnyQueryModification()
      || this.minJaccard != null
      || this.maxPvalue != null
      || this.maxFdr != null;
  }

  public ListingOptions getListingOptions() {
    return listingOptions;
  }

  public Optional<Double> getMinJaccard() {
    return ofNullable(minJaccard);
  }

  public Optional<Double> getMaxPvalue() {
    return ofNullable(maxPvalue);
  }

  public Optional<Double> getMaxFdr() {
    return ofNullable(maxFdr);
  }
}
