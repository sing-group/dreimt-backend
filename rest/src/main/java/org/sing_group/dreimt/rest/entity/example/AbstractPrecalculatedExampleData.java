/*-
 * #%L
 * DREIMT - REST
 * %%
 * Copyright (C) 2018 - 2019 Daniel Glez-Peña, Miguel Reboiro-Jato, Hugo López-Fernández,
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
package org.sing_group.dreimt.rest.entity.example;

import org.sing_group.dreimt.rest.entity.execution.WorkData;

public abstract class AbstractPrecalculatedExampleData {
  private String title;
  private WorkData workData;
  private String description;
  private String reference;
  private String url;

  public AbstractPrecalculatedExampleData(
    String title, String description, String reference, String url, WorkData workData
  ) {
    this.title = title;
    this.description = description;
    this.reference = reference;
    this.url = url;
    this.workData = workData;
  }

  public String getTitle() {
    return title;
  }

  public WorkData getWorkData() {
    return workData;
  }

  public String getDescription() {
    return description;
  }

  public String getReference() {
    return reference;
  }

  public String getUrl() {
    return url;
  }
}
