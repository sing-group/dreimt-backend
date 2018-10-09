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
package org.sing_group.dreimt.domain.dao.signature;

import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.DAOHelper;
import org.sing_group.dreimt.domain.dao.spi.signature.SignatureDao;
import org.sing_group.dreimt.domain.entities.signature.Signature;

@Default
@Transactional(MANDATORY)
public class DefaultSignatureDao implements SignatureDao {

  @PersistenceContext
  private EntityManager em;

  private DAOHelper<Integer, Signature> dh;

  DefaultSignatureDao() {}

  public DefaultSignatureDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DAOHelper.of(Integer.class, Signature.class, this.em);
  }

  @Override
  public Stream<Signature> list() {
    return this.dh.list().stream();
  }
}
