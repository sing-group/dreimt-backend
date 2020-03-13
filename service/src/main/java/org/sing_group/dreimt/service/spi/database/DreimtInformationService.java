package org.sing_group.dreimt.service.spi.database;

import java.util.Optional;

import javax.ejb.Local;

import org.sing_group.dreimt.domain.entities.database.DreimtInformation;

@Local
public interface DreimtInformationService {
  Optional<DreimtInformation> get();
}
