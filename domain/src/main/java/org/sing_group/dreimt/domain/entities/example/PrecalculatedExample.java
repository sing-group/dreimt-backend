/*-
 * #%L
 * DREIMT - Domain
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
package org.sing_group.dreimt.domain.entities.example;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.sing_group.dreimt.domain.entities.execution.WorkEntity;

@Entity
@Table(name = "precalculated_example")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "resultType")
public abstract class PrecalculatedExample {

  @Id
  @Column(name = "work", length = 36, columnDefinition = "CHAR(36)", nullable = false)
  private String id;

  @OneToOne(fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn(name = "work", referencedColumnName = "id")
  private WorkEntity work;

  private String reference;
  private String url;

  protected PrecalculatedExample() {}

  public WorkEntity getWork() {
    return work;
  }

  public String getReference() {
    return reference;
  }

  public String getUrl() {
    return url;
  }
}
