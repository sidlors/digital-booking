package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.collections.CollectionUtils;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.dao.util.BookingStatusDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.BookingStatusDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingStatusDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.BookingStatusDAO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class BookingStatusDAOImpl extends AbstractBaseDAO<BookingStatusDO> implements BookingStatusDAO
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
  public BookingStatusDAOImpl()
  {
    super( BookingStatusDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CatalogTO get( int id )
  {
    return get( id, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CatalogTO get( int id, Language language )
  {
    BookingStatusDO bookingStatusDO = this.find( id );
    return (CatalogTO) new BookingStatusDOToCatalogTOTransformer( language ).transform( bookingStatusDO );

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getAll()
  {
    return this.getAll( Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<CatalogTO> getAll( Language language )
  {
    return (List<CatalogTO>) CollectionUtils.collect( this.findAll(), new BookingStatusDOToCatalogTOTransformer(
        language ) );
  }

}
