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

import javax.enterprise.inject.Default;

import org.sing_group.dreimt.domain.entities.signature.GeneSetSignature;
import org.sing_group.dreimt.domain.entities.signature.Signature;
import org.sing_group.dreimt.domain.entities.signature.SignatureType;
import org.sing_group.dreimt.domain.entities.signature.UpDownSignature;
import org.sing_group.dreimt.rest.entity.signature.SignatureData;
import org.sing_group.dreimt.rest.entity.signature.SignatureDataSummary;
import org.sing_group.dreimt.rest.entity.signature.UpDownSignatureGeneData;
import org.sing_group.dreimt.rest.mapper.spi.signature.SignatureMapper;
import org.sing_group.dreimt.rest.resource.route.BaseRestPathBuilder;

@Default
public class DefaultSignatureMapper implements SignatureMapper {

  @Override
  public SignatureData toSignatureData(Signature signature) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder();

    return new SignatureData(
      signature.getSignatureName(),
      signature.getCellTypeA(),
      signature.getCellSubTypeA(),
      signature.getCellTypeB(),
      signature.getCellSubTypeB(),
      signature.getSourceDb(),
      signature.getSourceDbUrl(),
      signature.getExperimentalDesign(),
      signature.getOrganism(),
      signature.getDisease(),
      signature.getDiseaseA(),
      signature.getDiseaseB(),
      signature.getTreatmentA(),
      signature.getTreatmentB(),
      signature.getStateA(),
      signature.getStateB(),
      signature.getArticleMetadata().isPresent() ? signature.getArticleMetadata().get().getPubmedId() : null,
      signature.getArticleMetadata().isPresent() ? signature.getArticleMetadata().get().getTitle() : null,
      signature.getArticleMetadata().isPresent() ? signature.getArticleMetadata().get().getAuthors() : null,
      signature.getSignatureType(),
      pathBuilder.signatureGenes(signature).build(),
      signature.getArticleMetadata().isPresent() ? pathBuilder.articleMetadata(signature.getArticleMetadata().get()).build() : null
    );
  }

  @Override
  public SignatureDataSummary toSignatureDataSummary(Signature signature) {
    final BaseRestPathBuilder pathBuilder = new BaseRestPathBuilder();

    return new SignatureDataSummary(
      signature.getSignatureName(),
      signature.getCellTypeA(),
      signature.getCellSubTypeA(),
      signature.getCellTypeB(),
      signature.getCellSubTypeB(),
      signature.getSourceDb(),
      signature.getSourceDbUrl(),
      signature.getArticleMetadata().isPresent() ? signature.getArticleMetadata().get().getPubmedId() : null,
      signature.getArticleMetadata().isPresent() ? signature.getArticleMetadata().get().getTitle() : null,
      signature.getArticleMetadata().isPresent() ? signature.getArticleMetadata().get().getAuthors() : null,
      pathBuilder.signatureGenes(signature).build(),
      signature.getArticleMetadata().isPresent() ? pathBuilder.articleMetadata(signature.getArticleMetadata().get()).build() : null
    );
  }

  @Override
  public Object toSignatureGeneData(Signature signature, boolean onlyUniverseGenes) {
    if (signature.getSignatureType().equals(SignatureType.UPDOWN)) {
      return new UpDownSignatureGeneData(
        ((UpDownSignature) signature).getUpGenes(onlyUniverseGenes),
        ((UpDownSignature) signature).getDownGenes(onlyUniverseGenes)
      );
    } else {
      return ((GeneSetSignature) signature).getSignatureGenes(onlyUniverseGenes);
    }
  }
}
