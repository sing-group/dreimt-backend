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

import java.lang.reflect.Method;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

@CrossDomain
@Interceptor
@Dependent
public class CrossDomainInterceptor {
  @Inject
  private HttpServletRequest request;
  
  private CrossDomainBuilder builder;
  
  public CrossDomainInterceptor() {
    this.builder = new CrossDomainBuilder();
  }

  public void setRequest(HttpServletRequest request) {
    this.request = request;
  }
  
  @AroundInvoke
  public Object invoke(InvocationContext ctx) throws Exception {
    final CrossDomain annotation;
    final Object responseObject;
    
    if (request.getHeader("Origin") == null
      || (annotation = getAnnotation(ctx.getMethod(), ctx.getTarget().getClass())) == null
      || !((responseObject = ctx.proceed()) instanceof Response)) {
      return ctx.proceed();
    }
    
    final Response response = (Response) responseObject;
    final CrossDomainBuilder.ResponseHeaderBuilder newResponse = new CrossDomainBuilder.ResponseHeaderBuilder(response);

    this.builder.buildCorsHeaders(new AnnotationCrossDomainConfiguration(annotation), newResponse::header, request::getHeader);
    
    return newResponse.build();
  }
  
  private static CrossDomain getAnnotation(Method method, Class<?> targetClass) {
    CrossDomain annotation = null;
    
    if (method != null) {
      method.getAnnotation(CrossDomain.class);
    }
    
    if (annotation == null && targetClass != null) {
      annotation = targetClass.getAnnotation(CrossDomain.class);
    }
    
    return annotation;
  }
}
