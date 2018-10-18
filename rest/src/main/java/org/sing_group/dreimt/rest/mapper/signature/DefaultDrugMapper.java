package org.sing_group.dreimt.rest.mapper.signature;

import org.sing_group.dreimt.domain.entities.signature.Drug;
import org.sing_group.dreimt.rest.entity.signature.DrugData;
import org.sing_group.dreimt.rest.mapper.spi.signature.DrugMapper;

public class DefaultDrugMapper implements DrugMapper {

  @Override
  public DrugData toDrugData(Drug drug) {
    return new DrugData(drug.getCommonName(), drug.getSourceName(), drug.getSourceDb());
  }
}
