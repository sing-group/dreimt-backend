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

import org.sing_group.dreimt.domain.dao.execution.cmap.CmapDrugUpDownSignatureInteractionListingOptions;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapDrugUpDownSignatureInteraction;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapUpDownSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.DrugStatus;

@Local
public interface CmapDrugUpDownSignatureInteractionService {
  Stream<CmapDrugUpDownSignatureInteraction> list(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  );

  long count(CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions);

  Stream<String> listDrugSourceNameValues(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  );

  Stream<String> listDrugSourceDbValues(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  );

  Stream<String> listDrugCommonNameValues(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  );

  Stream<String> listDrugMoaValues(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  );
  
  Stream<DrugStatus> listDrugStatusValues(
    CmapUpDownSignatureResult cmapResult, CmapDrugUpDownSignatureInteractionListingOptions listingOptions
  );
}
