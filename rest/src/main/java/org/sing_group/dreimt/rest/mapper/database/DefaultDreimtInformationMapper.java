package org.sing_group.dreimt.rest.mapper.database;

import org.sing_group.dreimt.domain.entities.database.DreimtInformation;
import org.sing_group.dreimt.rest.entity.database.DreimtInformationData;
import org.sing_group.dreimt.rest.mapper.spi.database.DreimtInformationMapper;

public class DefaultDreimtInformationMapper implements DreimtInformationMapper {

  @Override
  public DreimtInformationData mapToDreimtInformationData(
    DreimtInformation dreimtInformation
  ) {
    return new DreimtInformationData(dreimtInformation.getTauThreshold());
  }
}
