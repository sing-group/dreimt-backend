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
package org.sing_group.dreimt.domain.dao.execution.cmap;

import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.DaoHelper;
import org.sing_group.dreimt.domain.dao.spi.execution.cmap.CmapUpDownSignatureResultDao;
import org.sing_group.dreimt.domain.dao.spi.signature.GeneDao;
import org.sing_group.dreimt.domain.entities.execution.cmap.CmapUpDownSignatureResult;

@Default
@Transactional(MANDATORY)
public class DefaultCmapUpDownSignatureResultDao implements CmapUpDownSignatureResultDao {

  @Inject
  protected GeneDao geneDao;

  @PersistenceContext
  protected EntityManager em;
  protected DaoHelper<String, CmapUpDownSignatureResult> dh;

  public DefaultCmapUpDownSignatureResultDao() {
    super();
  }

  public DefaultCmapUpDownSignatureResultDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DaoHelper.of(String.class, CmapUpDownSignatureResult.class, this.em);
  }

  @Override
  public Optional<CmapUpDownSignatureResult> get(String id) {
    return this.dh.get(id);
  }

  @Override
  public CmapUpDownSignatureResult create(
    String name,
    String description,
    Function<String, String> resultReferenceBuilder,
    Set<String> upGenes,
    Set<String> downGenes,
    int numPerm,
    double maxPvalue
  ) {
    return this.dh.persist(
      new CmapUpDownSignatureResult(
        name, description, resultReferenceBuilder,
        this.geneDao.getGenes(upGenes, true),
        this.geneDao.getGenes(downGenes, true),
        numPerm, maxPvalue
      )
    );
  }
}
