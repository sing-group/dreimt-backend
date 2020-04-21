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
package org.sing_group.dreimt.service.spi.execution.pipeline.cmap;

import java.util.stream.Stream;

import javax.ejb.Local;

import org.sing_group.dreimt.domain.dao.execution.cmap.CmapDrugGeneSetSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugGeneSetSignatureInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapGeneSetSignatureResult;

@Local
public interface CmapDrugGeneSetSignatureInteractionService {
  Stream<CmapDrugGeneSetSignatureInteraction> list(
    CmapGeneSetSignatureResult cmapResult, CmapDrugGeneSetSignatureInteractionListingOptions listingOptions
  );

  long count(CmapGeneSetSignatureResult cmapResult, CmapDrugGeneSetSignatureInteractionListingOptions listingOptions);

  Stream<String> listDrugSourceNameValues(
    CmapGeneSetSignatureResult cmapResult, CmapDrugGeneSetSignatureInteractionListingOptions listingOptions
  );

  Stream<String> listDrugSourceDbValues(
    CmapGeneSetSignatureResult cmapResult, CmapDrugGeneSetSignatureInteractionListingOptions listingOptions
  );

  Stream<String> listDrugCommonNameValues(
    CmapGeneSetSignatureResult cmapResult, CmapDrugGeneSetSignatureInteractionListingOptions listingOptions
  );

  Stream<String> listDrugMoaValues(
    CmapGeneSetSignatureResult cmapResult, CmapDrugGeneSetSignatureInteractionListingOptions listingOptions
  );
}
