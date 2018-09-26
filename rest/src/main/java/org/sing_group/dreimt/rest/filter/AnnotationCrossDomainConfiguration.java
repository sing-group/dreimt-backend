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
package org.sing_group.dreimt.rest.filter;

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

import java.util.stream.Stream;

public class AnnotationCrossDomainConfiguration implements CrossDomainConfiguration {
  private final CrossDomain annotation;
  
  public AnnotationCrossDomainConfiguration(CrossDomain annotation) {
    this.annotation = requireNonNull(annotation, "annotation can't be null");
  }

  @Override
  public String getAllowedOrigin() {
    return this.annotation.allowedOrigin();
  }

  @Override
  public int getMaxAge() {
    return this.annotation.maxAge();
  }

  @Override
  public boolean areCredentialsAllowed() {
    return this.annotation.allowCredentials();
  }

  @Override
  public Stream<String> getAllowedMethods() {
    return stream(this.annotation.allowedMethods());
  }

  @Override
  public Stream<String> getAllowedHeaders() {
    return stream(this.annotation.allowedHeaders());
  }

  @Override
  public boolean areRequestHeadersAllowed() {
    return this.annotation.allowRequestHeaders();
  }

}
