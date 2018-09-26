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

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.joining;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

public class CrossDomainBuilder {
  public void buildCorsHeaders(
    final CrossDomainConfiguration configuration,
    final ResponseHeaderBuilder responseHeaders
  ) {
    this.buildCorsHeaders(configuration, responseHeaders::header, __ -> null);
  }

  public void buildCorsHeaders(
    final CrossDomainConfiguration configuration,
    final BiConsumer<String, Object> responseHeaders
  ) {
    this.buildCorsHeaders(configuration, responseHeaders, __ -> null);
  }

  public void buildCorsHeaders(
    final CrossDomainConfiguration configuration,
    final ResponseHeaderBuilder responseHeaders,
    final Function<String, String> requestHeadersProvider
  ) {
    this.buildCorsHeaders(configuration, responseHeaders::header, requestHeadersProvider);
  }
  
  public void buildCorsHeaders(
    final CrossDomainConfiguration configuration,
    final BiConsumer<String, Object> responseHeaders,
    final Function<String, String> requestHeadersProvider
  ) {
    processOrigin(configuration, responseHeaders, requestHeadersProvider);
    processAllowedCredentials(configuration, responseHeaders);
    processAllowedHeaders(configuration, responseHeaders, requestHeadersProvider, false);
  }

  public void buildCorsPreFlightHeaders(
    final CrossDomainConfiguration configuration,
    final ResponseHeaderBuilder responseHeaders
  ) {
    this.buildCorsPreFlightHeaders(configuration, responseHeaders::header, __ -> null);
  }
  public void buildCorsPreFlightHeaders(
    final CrossDomainConfiguration configuration,
    final BiConsumer<String, Object> responseHeaders
  ) {
    this.buildCorsPreFlightHeaders(configuration, responseHeaders, __ -> null);
  }
  
  public void buildCorsPreFlightHeaders(
    final CrossDomainConfiguration configuration,
    final ResponseHeaderBuilder responseHeaders,
    final Function<String, String> requestHeadersProvider
  ) {
    this.buildCorsPreFlightHeaders(configuration, responseHeaders::header, requestHeadersProvider);
  }

  public void buildCorsPreFlightHeaders(
    final CrossDomainConfiguration configuration,
    final BiConsumer<String, Object> responseHeaders,
    final Function<String, String> requestHeadersProvider
  ) {
    processOrigin(configuration, responseHeaders, requestHeadersProvider);
    processMaxAge(configuration, responseHeaders);
    processAllowedCredentials(configuration, responseHeaders);
    processAllowedMethods(configuration, responseHeaders);
    processAllowedHeaders(configuration, responseHeaders, requestHeadersProvider, true);
  }

  private static void processAllowedHeaders(
    final CrossDomainConfiguration configuration,
    final BiConsumer<String, Object> headerConsumer,
    final Function<String, String> requestHeadersProvider,
    final boolean preflight
  ) {
    final List<String> allowedHeaders = new ArrayList<>();
    
    if (configuration.areRequestHeadersAllowed()) {
      final String requestHeaders = requestHeadersProvider.apply("Access-Control-Request-Headers");
      
      if (requestHeaders != null)
        allowedHeaders.add(requestHeaders);
    }
    configuration.getAllowedHeaders().forEach(allowedHeaders::add);

    if (!allowedHeaders.isEmpty()) {
      final String header = preflight
        ? "Access-Control-Allow-Headers"
        : "Access-Control-Expose-Headers";
      
      headerConsumer.accept(header, String.join(", ", allowedHeaders));
    }
  }

  private static void processOrigin(
    final CrossDomainConfiguration configuration,
    final BiConsumer<String, Object> headerConsumer,
    final Function<String, String> requestHeadersProvider
  ) {
    if (configuration.getAllowedOrigin() == null) {
      final String originHeader = requestHeadersProvider.apply("Origin");
      
      if (originHeader != null) {
        headerConsumer.accept("Access-Control-Allow-Origin", originHeader);
      }
    } else {
      headerConsumer.accept("Access-Control-Allow-Origin", configuration.getAllowedOrigin());
    }
  }

  private static void processMaxAge(final CrossDomainConfiguration configuration, final BiConsumer<String, Object> headerConsumer) {
    headerConsumer.accept("Access-Control-Max-Age", Integer.toString(configuration.getMaxAge() < 0 ? -1 : configuration.getMaxAge()));
  }

  private static void processAllowedCredentials(final CrossDomainConfiguration configuration, final BiConsumer<String, Object> headerConsumer) {
    headerConsumer.accept("Access-Control-Allow-Credentials", Boolean.toString(configuration.areCredentialsAllowed()));
  }

  private static void processAllowedMethods(final CrossDomainConfiguration configuration, final BiConsumer<String, Object> headerConsumer) {
    headerConsumer.accept("Access-Control-Allow-Methods", configuration.getAllowedMethods().collect(joining(", ")));
  }
  
  public static class ResponseHeaderBuilder {
    private ResponseBuilder response;
  
    public ResponseHeaderBuilder(Response response) {
      this.response = Response.fromResponse(requireNonNull(response));
    }
    
    public void header(String name, Object value) {
      this.response = response.header(name, value);
    }
    
    public Response build() {
      return this.response.build();
    }
  }
}
