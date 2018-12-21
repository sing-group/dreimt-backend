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
package org.sing_group.dreimt.rest.entity.execution;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.sing_group.dreimt.domain.entities.execution.ExecutionStatus;
import org.sing_group.dreimt.rest.entity.LocalDateTimeToIsoStringAdapter;
import org.sing_group.dreimt.rest.entity.UuidAndUri;

import io.swagger.annotations.ApiModel;

@XmlRootElement(name = "work-data", namespace = "http://entity.resource.rest.dreimt.sing-group.org")
@XmlAccessorType(XmlAccessType.FIELD)
@ApiModel(value = "work-data", description = "Information of a work.")
public class WorkData implements Serializable {
  private static final long serialVersionUID = 1L;
  
  private UuidAndUri id;

  private String name;

  private String description;
  
  @XmlJavaTypeAdapter(LocalDateTimeToIsoStringAdapter.class)
  private LocalDateTime creationDateTime;

  @XmlJavaTypeAdapter(LocalDateTimeToIsoStringAdapter.class)
  private LocalDateTime schedulingDateTime;

  @XmlJavaTypeAdapter(LocalDateTimeToIsoStringAdapter.class)
  private LocalDateTime startingDateTime;

  @XmlJavaTypeAdapter(LocalDateTimeToIsoStringAdapter.class)
  private LocalDateTime finishingDateTime;

  private String resultReference;

  private ExecutionStatus status;

  private WorkStepData[] steps;

  WorkData() {}

  public WorkData(
    UuidAndUri id,
    String name,
    String description,
    LocalDateTime creationDateTime,
    LocalDateTime schedulingDateTime,
    LocalDateTime startingDateTime,
    LocalDateTime finishingDateTime,
    String resultReference,
    ExecutionStatus status,
    WorkStepData[] steps
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.creationDateTime = creationDateTime;
    this.schedulingDateTime = schedulingDateTime;
    this.startingDateTime = startingDateTime;
    this.finishingDateTime = finishingDateTime;
    this.resultReference = resultReference;
    this.status = status;
    this.steps = steps;
  }

  public UuidAndUri getId() {
    return id;
  }

  public void setId(UuidAndUri id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public LocalDateTime getCreationDateTime() {
    return creationDateTime;
  }

  public void setCreationDateTime(LocalDateTime creationDateTime) {
    this.creationDateTime = creationDateTime;
  }

  public LocalDateTime getStartingDateTime() {
    return startingDateTime;
  }

  public void setStartingDateTime(LocalDateTime startDateTime) {
    this.startingDateTime = startDateTime;
  }

  public LocalDateTime getSchedulingDateTime() {
    return schedulingDateTime;
  }

  public void setSchedulingDateTime(LocalDateTime schedulingDateTime) {
    this.schedulingDateTime = schedulingDateTime;
  }

  public LocalDateTime getFinishingDateTime() {
    return finishingDateTime;
  }

  public void setFinishingDateTime(LocalDateTime endDateTime) {
    this.finishingDateTime = endDateTime;
  }

  public String getResultReference() {
    return resultReference;
  }

  public void setResultReference(String resultReference) {
    this.resultReference = resultReference;
  }

  public ExecutionStatus getStatus() {
    return status;
  }

  public void setStatus(ExecutionStatus status) {
    this.status = status;
  }

  public WorkStepData[] getSteps() {
    return steps;
  }

  public void setSteps(WorkStepData[] steps) {
    this.steps = steps;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((creationDateTime == null) ? 0 : creationDateTime.hashCode());
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((finishingDateTime == null) ? 0 : finishingDateTime.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((resultReference == null) ? 0 : resultReference.hashCode());
    result = prime * result + ((schedulingDateTime == null) ? 0 : schedulingDateTime.hashCode());
    result = prime * result + ((startingDateTime == null) ? 0 : startingDateTime.hashCode());
    result = prime * result + ((status == null) ? 0 : status.hashCode());
    result = prime * result + Arrays.hashCode(steps);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    WorkData other = (WorkData) obj;
    if (creationDateTime == null) {
      if (other.creationDateTime != null)
        return false;
    } else if (!creationDateTime.equals(other.creationDateTime))
      return false;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
      return false;
    if (finishingDateTime == null) {
      if (other.finishingDateTime != null)
        return false;
    } else if (!finishingDateTime.equals(other.finishingDateTime))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (resultReference == null) {
      if (other.resultReference != null)
        return false;
    } else if (!resultReference.equals(other.resultReference))
      return false;
    if (schedulingDateTime == null) {
      if (other.schedulingDateTime != null)
        return false;
    } else if (!schedulingDateTime.equals(other.schedulingDateTime))
      return false;
    if (startingDateTime == null) {
      if (other.startingDateTime != null)
        return false;
    } else if (!startingDateTime.equals(other.startingDateTime))
      return false;
    if (status == null) {
      if (other.status != null)
        return false;
    } else if (!status.equals(other.status))
      return false;
    if (!Arrays.equals(steps, other.steps))
      return false;
    return true;
  }
}
