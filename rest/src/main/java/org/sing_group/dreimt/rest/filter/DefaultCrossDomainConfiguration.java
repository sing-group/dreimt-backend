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

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

public class DefaultCrossDomainConfiguration implements CrossDomainConfiguration {
  private String allowedOrigin;
  private int maxAge;
  private boolean credentialsAllowed;
  private Set<String> allowedMethods;
  private Set<String> allowedHeaders;
  private boolean requestHeadersAllowed;
  
  public DefaultCrossDomainConfiguration() {
    this("*", -1, false, asList("GET", "POST", "PUT", "DELETE", "OPTIONS"), emptySet(), true);
  }

  public DefaultCrossDomainConfiguration(
    String allowedOrigin, int maxAge, boolean credentialsAllowed, Collection<String> allowedMethods,
    Collection<String> allowedHeaders, boolean requestHeadersAllowed
  ) {
    super();
    this.allowedOrigin = allowedOrigin;
    this.maxAge = maxAge;
    this.credentialsAllowed = credentialsAllowed;
    this.allowedMethods = new HashSet<>(allowedMethods);
    this.allowedHeaders = new HashSet<>(allowedHeaders);
    this.requestHeadersAllowed = requestHeadersAllowed;
  }

  @Override
  public String getAllowedOrigin() {
    return allowedOrigin;
  }

  public void setAllowedOrigin(String allowedOrigin) {
    this.allowedOrigin = allowedOrigin;
  }

  @Override
  public int getMaxAge() {
    return maxAge;
  }

  public void setMaxAge(int maxAge) {
    this.maxAge = maxAge;
  }

  @Override
  public boolean areCredentialsAllowed() {
    return credentialsAllowed;
  }

  public void setCredentialsAllowed(boolean credentialsAllowed) {
    this.credentialsAllowed = credentialsAllowed;
  }

  @Override
  public Stream<String> getAllowedMethods() {
    return allowedMethods.stream();
  }

  public void setAllowedMethods(Set<String> allowedMethods) {
    this.allowedMethods = allowedMethods;
  }

  @Override
  public Stream<String> getAllowedHeaders() {
    return allowedHeaders.stream();
  }

  public void setAllowedHeaders(Set<String> allowedHeaders) {
    this.allowedHeaders = allowedHeaders;
  }

  @Override
  public boolean areRequestHeadersAllowed() {
    return requestHeadersAllowed;
  }

  public void setRequestHeadersAllowed(boolean requestHeadersAllowed) {
    this.requestHeadersAllowed = requestHeadersAllowed;
  }

}
