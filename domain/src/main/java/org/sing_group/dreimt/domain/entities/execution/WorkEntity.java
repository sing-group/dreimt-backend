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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;
import java.util.stream.Stream;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "work")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "resultType")
public abstract class WorkEntity implements Work, Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "id", length = 36, columnDefinition = "CHAR(36)", nullable = false)
  private String id;

  @Column(name = "name", length = 255, nullable = false)
  private String name;

  @Column(name = "description", length = 255, nullable = true)
  private String description;

  @Column(name = "resultReference", length = 1023, nullable = true)
  private String resultReference;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "work", cascade = CascadeType.PERSIST, orphanRemoval = true)
  @OrderBy("order ASC")
  private SortedSet<WorkStep> steps;

  @Embedded
  private ExecutionStatusAndTime status;

  @Transient
  private final ReentrantReadWriteLock stepsLock;

  protected WorkEntity() {
    this.id = UUID.randomUUID().toString();
    this.stepsLock = new ReentrantReadWriteLock();
    this.status = new ExecutionStatusAndTime();
  }

  public WorkEntity(String name) {
    this();

    this.name = name;
    this.steps = new TreeSet<>();
  }

  public WorkEntity(String name, String description, Function<String, String> resultReferenceBuilder) {
    this(name);

    this.description = description;
    this.resultReference = resultReferenceBuilder.apply(this.id);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Optional<String> getDescription() {
    return Optional.ofNullable(description);
  }

  @Override
  public LocalDateTime getCreationDateTime() {
    return this.status.getCreationDateTime();
  }

  @Override
  public Optional<LocalDateTime> getSchedulingDateTime() {
    return this.status.getSchedulingDateTime();
  }

  @Override
  public Optional<LocalDateTime> getStartingDateTime() {
    return this.status.getStartingDateTime();
  }

  @Override
  public Optional<LocalDateTime> getFinishingDateTime() {
    return this.status.getFinishingDateTime();
  }

  @Override
  public Optional<String> getResultReference() {
    return Optional.ofNullable(resultReference);
  }

  @Override
  public Stream<WorkStep> getSteps() {
    this.stepsLock.readLock().lock();

    try {
      return steps.stream();
    } finally {
      this.stepsLock.readLock().unlock();
    }
  }
  
  public WorkStep addStep(String description, Double progress) {
    this.stepsLock.writeLock().lock();
    
    try {
      final WorkStep step = new WorkStep(this, this.steps.size() + 1, description, progress);
      
      this.steps.add(step);
      
      return step;
    } finally {
      this.stepsLock.writeLock().unlock();
    }
  }

  @Override
  public ExecutionStatus getStatus() {
    return this.status.getStatus();
  }

  @Override
  public void setScheduled() throws IllegalStateException {
    this.status.setScheduled();
  }
  
  @Override
  public void setRunning() throws IllegalStateException {
    this.status.setRunning();
  }

  @Override
  public void setFinished() throws IllegalStateException {
    this.status.setFinished();
  }

  @Override
  public void setFailed(String cause) throws IllegalStateException {
    this.status.setFailed(cause);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!getClass().isAssignableFrom(obj.getClass()))
      return false;
    WorkEntity other = (WorkEntity) obj;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }
}
