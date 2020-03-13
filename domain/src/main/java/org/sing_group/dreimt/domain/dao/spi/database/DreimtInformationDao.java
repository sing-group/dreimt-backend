package org.sing_group.dreimt.domain.dao.spi.database;

import java.util.Optional;

import org.sing_group.dreimt.domain.entities.database.DreimtInformation;

public interface DreimtInformationDao {
  Optional<DreimtInformation> get();
}
