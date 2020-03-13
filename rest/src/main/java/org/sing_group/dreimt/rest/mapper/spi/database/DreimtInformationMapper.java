package org.sing_group.dreimt.rest.mapper.spi.database;

import org.sing_group.dreimt.domain.entities.database.DreimtInformation;
import org.sing_group.dreimt.rest.entity.database.DreimtInformationData;

public interface DreimtInformationMapper {
  DreimtInformationData mapToDreimtInformationData(DreimtInformation dreimtInformation);
}
