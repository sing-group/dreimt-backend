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
package org.sing_group.dreimt.rest.entity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class LocalDateTimeToIsoStringAdapter extends XmlAdapter<String, LocalDateTime> {

  @Override
  public LocalDateTime unmarshal(String isoDateTime) throws Exception {
    return LocalDateTime.parse(isoDateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  @Override
  public String marshal(LocalDateTime date) throws Exception {
    return toUtc(date).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
  }

  public static ZonedDateTime toZone(final LocalDateTime time, final ZoneId fromZone, final ZoneId toZone) {
    final ZonedDateTime zonedtime = time.atZone(fromZone);
    return zonedtime.withZoneSameInstant(toZone);
  }

  public static ZonedDateTime toUtc(final LocalDateTime time, final ZoneId fromZone) {
    return toZone(time, fromZone, ZoneOffset.UTC);
  }

  public static ZonedDateTime toUtc(final LocalDateTime time) {
    return toUtc(time, ZoneId.systemDefault());
  }
}
