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
package org.sing_group.dreimt.domain.dao.execution.jaccard;

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
import org.sing_group.dreimt.domain.dao.spi.execution.jaccard.JaccardUpDownSignatureResultDao;
import org.sing_group.dreimt.domain.dao.spi.signature.GeneDao;
import org.sing_group.dreimt.domain.entities.execution.jaccard.JaccardUpDownSignatureResult;
import org.sing_group.dreimt.domain.entities.signature.ExperimentalDesign;

@Default
@Transactional(MANDATORY)
public class DefaultJaccardUpDownSignatureResultDao implements JaccardUpDownSignatureResultDao {

  @Inject
  protected GeneDao geneDao;

  @PersistenceContext
  protected EntityManager em;
  protected DaoHelper<String, JaccardUpDownSignatureResult> dh;

  public DefaultJaccardUpDownSignatureResultDao() {
    super();
  }

  public DefaultJaccardUpDownSignatureResultDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DaoHelper.of(String.class, JaccardUpDownSignatureResult.class, this.em);
  }

  @Override
  public Optional<JaccardUpDownSignatureResult> get(String id) {
    return this.dh.get(id);
  }

  @Override
  public JaccardUpDownSignatureResult create(
    String name, String description,
    Function<String, String> resultReferenceBuilder,
    boolean onlyUniverseGenes,
    String cellType1, String cellSubType1, String cellTypeOrSubType1,
    String cellType2, String cellSubType2, String cellTypeOrSubType2,
    ExperimentalDesign experimentalDesign,
    String organism, String disease, String signatureSourceDb,
    Set<String> upGenes, Set<String> downGenes
  ) {
    return this.dh.persist(
      new JaccardUpDownSignatureResult(
        name, description, resultReferenceBuilder,
        onlyUniverseGenes, 
        cellType1, cellSubType1, cellTypeOrSubType1,
        cellType2, cellSubType2, cellTypeOrSubType2,
        experimentalDesign, 
        organism, disease, signatureSourceDb, 
        this.geneDao.getGenes(upGenes, true),
        this.geneDao.getGenes(downGenes, true)
      )
    );
  }
}
