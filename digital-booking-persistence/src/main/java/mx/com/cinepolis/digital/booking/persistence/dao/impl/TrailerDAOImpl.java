package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.TrailerDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TrailerDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.TrailerDAO}
 * 
 * @author jcarbajal
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class TrailerDAOImpl extends AbstractBaseDAO<TrailerDO> implements TrailerDAO 
{

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;
  /**get the entity manager
   * {@inheritDoc}
   */
  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }
  /**
   * Constyructor by default
   */
  public TrailerDAOImpl()
  {
    super(TrailerDO.class);
  }

  public TrailerDAOImpl( Class<TrailerDO> entityClass )
  {
    super( entityClass );
  }

}
