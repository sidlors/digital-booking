package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenDO}
 * 
 * @author jcarbajal
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class BookingSpecialEventScreenDAOImpl extends AbstractBaseDAO<BookingSpecialEventScreenDO> implements BookingSpecialEventScreenDAO
{
  
  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  public BookingSpecialEventScreenDAOImpl( )
  {
    super( BookingSpecialEventScreenDO.class );
  }

  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

}
