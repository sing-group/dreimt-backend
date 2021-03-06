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

/*-
 * #%L
 * DREIMT - Service
 * %%
 * Copyright (C) 2018 - 2020 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
 *      Kevin Troulé, Gonzálo Gómez-López, Fátima Al-Shahrour
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

package org.sing_group.dreimt.rest;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;

import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import io.swagger.jaxrs.Reader;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.ReaderListener;
import io.swagger.models.Contact;
import io.swagger.models.Swagger;
import io.swagger.models.auth.BasicAuthDefinition;

@SwaggerDefinition(tags = {
  @Tag(
    name = "query", description = "Operations to perform the DREIMT analyses (i.e. drug prioritization and signatures comparison queries)."
  ),
  @Tag(
    name = "results", description = "Access to the DREIMT analysis results."
  ),
  @Tag(name = "database", description = "Operations to obtain information about the DREIMT database."),
  @Tag(name = "database-associations", description = "Access to the drug-signature associations in the DREIMT database."),
  @Tag(name = "signature", description = "Access to the DREIMT signatures information."),
  @Tag(name = "examples", description = "Access to the drug prioritization and signatures comparison precalculated examples."),
  @Tag(name = "article", description = "Access to DREIMT article information."),
  @Tag(name = "work", description = "Access to DREIMT works."),
  @Tag(name = "statistical", description = "Operations to perform statistical tests.")
})
@Startup
@Singleton
public class SwaggerConfiguration implements ReaderListener {
  @Resource(name = "java:global/dreimt/swagger/version")
  private String version;
  @Resource(name = "java:global/dreimt/swagger/schemes")
  private String schemes;
  @Resource(name = "java:global/dreimt/swagger/host")
  private String host;
  @Resource(name = "java:global/dreimt/swagger/basePath")
  private String basePath;

  @PostConstruct
  public void init() {
    final BeanConfig config = new BeanConfig();

    config.setBasePath(this.basePath);
    config.setSchemes(this.schemes.split(","));
    config.setHost(this.host);

    config.setTitle("DREIMT");
    config.setDescription("Drug Repositioning for Immuno Transcriptomics");
    config.setVersion(this.version);
    config.setLicense("GPLv3");
    config.setLicenseUrl("https://www.gnu.org/licenses/gpl-3.0.en.html");
    config.setContact("SING Group");

    config.setResourcePackage(this.getClass().getPackage().getName());
    config.setScan(true);

    final Contact contact = config.getSwagger().getInfo().getContact();
    contact.setUrl("https://www.sing-group.org");
  }

  @Override
  public void beforeScan(Reader reader, Swagger swagger) {
    swagger.addSecurityDefinition("basicAuth", new BasicAuthDefinition());
  }

  @Override
  public void afterScan(Reader reader, Swagger swagger) {}

}
