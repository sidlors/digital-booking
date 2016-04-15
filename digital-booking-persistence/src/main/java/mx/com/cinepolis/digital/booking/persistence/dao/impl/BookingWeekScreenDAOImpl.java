package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.BookingWeekScreenDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekScreenDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekScreenDAO}
 * 
 * @author gsegura
 * @since 0.2.7
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class BookingWeekScreenDAOImpl extends AbstractBaseDAO<BookingWeekScreenDO> implements BookingWeekScreenDAO
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
  public BookingWeekScreenDAOImpl()
  {
    super( BookingWeekScreenDO.class );
  }

}
