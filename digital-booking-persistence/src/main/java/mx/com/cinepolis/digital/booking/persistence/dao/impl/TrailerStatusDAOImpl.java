package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.TrailerStatusDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TrailerStatusDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.TrailerStatusDAO}
 * 
 * @author jcarbajal
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class TrailerStatusDAOImpl extends AbstractBaseDAO<TrailerStatusDO> implements TrailerStatusDAO
{
  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  /**
   * Constructor by Default
   */
  public TrailerStatusDAOImpl()
  {
    super( TrailerStatusDO.class );
  }

  /**
   * Constructor by EntityClass
   * 
   * @param entityClass
   */
  public TrailerStatusDAOImpl( Class<TrailerStatusDO> entityClass )
  {
    super( entityClass );
  }

  /**
   * get the entity manager
   */
  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

}
