package org.sing_group.dreimt.rest.resource.spi.database;

import javax.ejb.Local;
import javax.ws.rs.core.Response;

@Local
public interface DreimtInformationResource {
  Response getDreimtInformation();
}
