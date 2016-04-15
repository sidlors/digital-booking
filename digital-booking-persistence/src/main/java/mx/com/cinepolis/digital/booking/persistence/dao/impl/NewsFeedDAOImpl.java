package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.NewsFeedDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.NewsFeedDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.NewsFeedDAO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class NewsFeedDAOImpl extends AbstractBaseDAO<NewsFeedDO> implements NewsFeedDAO
{

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  /**
   * {@inheritDoc}
   */
  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

  /**
   * Constructor default
   */
  public NewsFeedDAOImpl()
  {
    super( NewsFeedDO.class );
  }

}
