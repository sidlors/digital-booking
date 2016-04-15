package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenShowDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenShowDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenShowDO}
 * 
 * @author jcarbajal
 * 
 * */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class BookingSpecialEventScreenShowDAOImpl extends AbstractBaseDAO<BookingSpecialEventScreenShowDO> implements BookingSpecialEventScreenShowDAO
{

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;
  
  public BookingSpecialEventScreenShowDAOImpl( )
  {
    super( BookingSpecialEventScreenShowDO.class );
    // TODO Auto-generated constructor stub
  }

  @Override
  protected EntityManager getEntityManager()
  {
    // TODO Auto-generated method stub
    return em;
  }

}
