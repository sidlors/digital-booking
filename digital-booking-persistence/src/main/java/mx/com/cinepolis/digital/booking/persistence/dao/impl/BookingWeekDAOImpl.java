package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.BookingWeekDO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO}
 * 
 * @author gsegura
 * @since 0.2.7
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class BookingWeekDAOImpl extends AbstractBaseDAO<BookingWeekDO> implements BookingWeekDAO
{

  private static final String PARAMETER_ID_THEATER = "idTheater";
  private static final String QUERY_FIND_BY_ID_THEATER_AND_ID_WEEK = "BookingWeekDO.findByIdTheaterAndIdWeek";
  private static final String QUERY_BOOKING_WEEK_DO_FIND_BY_BOOKING_AND_WEEK = "BookingWeekDO.findByBookingAndWeek";
  private static final String PARAMETER_STARTING_DAY = "startingDay";
  private static final String PARAMETER_ID_BOOKING = "idBooking";
  private static final String QUERY_FIND_FOLLOWING_WEEK_BOOKING = "BookingWeekDO.findfollowingWeekBooking";
  private static final String QUERY_COUNT_BOOKING_WEEK = "BookingWeekDO.countBookingWeek";
  private static final String PARAMETER_ID_WEEK = "idWeek";
  
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
  public BookingWeekDAOImpl()
  {
    super( BookingWeekDO.class );
  }
  
  /*
   * {@inheritDoc}
   * @see mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO#updateSentStatus(
   * mx.com.cinepolis.digital.booking.model.to.BookingTO)
   */
  @Override
  public void updateSentStatus( BookingTO bookingTO )
  {
    Query q = em.createNamedQuery( "BookingWeekDO.updateSentStatus" );
    q.setParameter( "idBookingWeek", bookingTO.getIdBookingWeek() );
    q.executeUpdate();
  }
  
  /*
   * {@inheritDoc}
   * @see mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO#countBookingWeek(
   * mx.com.cinepolis.digital.booking.model.WeekDO)
   */
  @Override
  public int countBookingWeek( WeekDO idWeek )
  {
    Query q = em.createNamedQuery( QUERY_COUNT_BOOKING_WEEK );
    q.setParameter( PARAMETER_ID_WEEK, idWeek );
    return ((Long) q.getSingleResult()).intValue();
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO#findfollowingWeekBooking(java.lang.Long,
   * java.util.Date)
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<BookingWeekDO> findfollowingWeekBooking( Long idBooking, Date referenceStartingDay )
  {
    Query q = em.createNamedQuery( QUERY_FIND_FOLLOWING_WEEK_BOOKING );
    q.setParameter( PARAMETER_ID_BOOKING, idBooking );
    q.setParameter( PARAMETER_STARTING_DAY, referenceStartingDay );
    return (List<BookingWeekDO>) q.getResultList();
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO#findByBookingAndWeek(java.lang.Long, java.lang.Integer)
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<BookingWeekDO> findByBookingAndWeek( Long idBooking, Integer idWeek )
  {
    Query q = em.createNamedQuery( QUERY_BOOKING_WEEK_DO_FIND_BY_BOOKING_AND_WEEK );
    q.setParameter( PARAMETER_ID_BOOKING, idBooking );
    q.setParameter( PARAMETER_ID_WEEK, idWeek );
    return (List<BookingWeekDO>) q.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<BookingWeekDO> findByIdTheaterAndIdWeek( Long idTheater, Integer idWeek )
  {
    Query q = em.createNamedQuery( QUERY_FIND_BY_ID_THEATER_AND_ID_WEEK );
    q.setParameter( PARAMETER_ID_THEATER, idTheater );
    q.setParameter( PARAMETER_ID_WEEK, idWeek );
    return q.getResultList();
  }
  
}
