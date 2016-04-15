package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.BookingTypeDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingTypeDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.BookingTypeDAO}
 * 
 * @author jcarbajal
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class BookingTypeDAOImpl extends AbstractBaseDAO<BookingTypeDO> implements BookingTypeDAO
{
  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  /**
   * Constructor method for the BookingTypeDAOImpl class.
   */
  public BookingTypeDAOImpl()
  {
    super( BookingTypeDO.class );
  }

  /**
   * Constructor method for the BookingTypeDAOImpl class, that receives an entity class parameter.
   * @param entityClass, with the entity class information.
   */
  public BookingTypeDAOImpl( Class<BookingTypeDO> entityClass )
  {
    super( entityClass );
  }

  @Override
  protected EntityManager getEntityManager()
  {
    return this.em;
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO#find(java.lang.Object)
   */
  @Override
  public BookingTypeDO find( Object id )
  {
    return super.find( id );
  }

}
