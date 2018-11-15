/*-
 * #%L
 * DREIMT - Domain
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
package org.sing_group.dreimt.domain.entities.execution;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

import java.util.Set;

public enum ExecutionStatus {
  COMPLETED,
  FAILED,
  RUNNING(true, COMPLETED, FAILED),
  SCHEDULED(RUNNING, FAILED),
  CREATED(SCHEDULED, FAILED);
  
  private final Set<ExecutionStatus> validFollowingStatus;
  
  private ExecutionStatus(ExecutionStatus ... validFollowingStatus) {
    this(false, validFollowingStatus);
  }
  
  private ExecutionStatus(boolean allowsSameStatusChange, ExecutionStatus ... validFollowingStatus) {
    this.validFollowingStatus = stream(validFollowingStatus).collect(toSet());
    
    if (allowsSameStatusChange)
      this.validFollowingStatus.add(this);
  }
  
  public ExecutionStatus changeTo(ExecutionStatus nextStatus) {
    if (this.validFollowingStatus.contains(nextStatus)) {
      return nextStatus;
    } else {
      throw new IllegalArgumentException(this + " status can't be changed to " + nextStatus);
    }
  }
}
