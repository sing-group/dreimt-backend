package org.sing_group.dreimt.service.database;

import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;

import org.sing_group.dreimt.domain.dao.spi.database.DreimtInformationDao;
import org.sing_group.dreimt.domain.entities.database.DreimtInformation;
import org.sing_group.dreimt.service.spi.database.DreimtInformationService;

@Stateless
@PermitAll
public class DefaultDreimtInformationService implements DreimtInformationService {

  @Inject
  DreimtInformationDao dao;

  @Override
  public Optional<DreimtInformation> get() {
    return this.dao.get();
  }
}
