package org.sing_group.dreimt.service.spi.database;

import javax.ejb.Local;

@Local
public interface DreimtStatisticsService {
  long drugCount();

  long signaturesCount();
}
