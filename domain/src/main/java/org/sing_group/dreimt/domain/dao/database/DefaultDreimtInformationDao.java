package org.sing_group.dreimt.domain.dao.database;

import static javax.transaction.Transactional.TxType.MANDATORY;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.sing_group.dreimt.domain.dao.DaoHelper;
import org.sing_group.dreimt.domain.dao.spi.database.DreimtInformationDao;
import org.sing_group.dreimt.domain.entities.database.DreimtInformation;

@Default
@Transactional(MANDATORY)
public class DefaultDreimtInformationDao implements DreimtInformationDao {

  @PersistenceContext
  private EntityManager em;

  private DaoHelper<Integer, DreimtInformation> dh;

  DefaultDreimtInformationDao() {}

  public DefaultDreimtInformationDao(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  private void createDAOHelper() {
    this.dh = DaoHelper.of(Integer.class, DreimtInformation.class, this.em);
  }
  
  @Override
  public Optional<DreimtInformation> get() {
    return this.dh.list().stream().findFirst();
  }
}
