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

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.DaoHelper;
import org.sing_group.dreimt.domain.dao.spi.signature.GeneDao;
import org.sing_group.dreimt.domain.entities.signature.Gene;

@Default
@Transactional(MANDATORY)
public class DefaultGeneDao implements GeneDao {

  @PersistenceContext
  private EntityManager em;

  private DaoHelper<Integer, Gene> dh;

  DefaultGeneDao() {}

  public DefaultGeneDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DaoHelper.of(Integer.class, Gene.class, this.em);
  }

  @Override
  public Optional<Gene> get(String geneId) {
    try {
      return of(this.dh.getBy("gene", geneId));
    } catch (NoResultException e) {
      return empty();
    }
  }

  @Override
  public Gene create(String geneId, boolean universe) {
    return this.dh.persist(new Gene(geneId, universe));
  }

  @Override
  public Set<Gene> getGenes(Set<String> geneIds, boolean createGeneIfNotExists) {
    Set<Gene> genes = new HashSet<>();
    for (String gene : geneIds) {
      Optional<Gene> dbGene = get(gene);
      if (dbGene.isPresent()) {
        genes.add(dbGene.get());
      } else if (createGeneIfNotExists) {
        genes.add(create(gene, false));
      }
    }
    return genes;
  }

  @Override
  public Stream<Gene> list(boolean onlyUniverseGenes) {
    return this.dh.list().stream().filter(g -> onlyUniverseGenes || g.isUniverseGene());
  }
}
