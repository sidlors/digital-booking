package mx.com.cinepolis.digital.booking.service.book.impl;

import java.util.Calendar;
import java.util.HashMap;

import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterBookingWeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.ReflectionHelper;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingWeekDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingWeekScreenDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.EventDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.ScreenDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.TheaterDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.WeekDAOImpl;
import mx.com.cinepolis.digital.booking.service.book.BookingServiceEJB;
import mx.com.cinepolis.digital.booking.service.income.IncomeServiceEJB;
import mx.com.cinepolis.digital.booking.service.income.impl.IncomeServiceEJBImpl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

public class BookingFindBookingTheaterTest extends AbstractDBEJBTestUnit
{
  private BookingServiceEJB bookingServiceEJB;
  private IncomeServiceEJB incomeServiceEJB;
  private BookingDAO bookingDAO;
  private BookingWeekDAO bookingWeekDAO;
  private BookingWeekScreenDAO bookingWeekScreenDAO;
  private TheaterDAO theaterDAO;
  private EventDAO eventDAO;
  private WeekDAO weekDAO;
  private ScreenDAO screenDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    bookingServiceEJB = new BookingServiceEJBImpl();
    bookingDAO = new BookingDAOImpl();
    bookingWeekDAO = new BookingWeekDAOImpl();
    bookingWeekScreenDAO = new BookingWeekScreenDAOImpl();
    theaterDAO = new TheaterDAOImpl();
    eventDAO = new EventDAOImpl();
    weekDAO = new WeekDAOImpl();
    screenDAO = new ScreenDAOImpl();
    incomeServiceEJB = new IncomeServiceEJBImpl();

    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    connect( bookingServiceEJB );
    connect( bookingDAO );
    connect( bookingWeekDAO );
    connect( bookingWeekScreenDAO );
    connect( theaterDAO );
    connect( eventDAO );
    connect( weekDAO );
    connect( screenDAO );
    connect( incomeServiceEJB );
    initializeData( "dataset/business/booking.sql" );
    ReflectionHelper.set( incomeServiceEJB, "incomeServiceEJB", bookingServiceEJB );
    ReflectionHelper.set( weekDAO, "weekDAO", incomeServiceEJB );

  }

  @Test
  public void test1()
  {
    Long idWeek = 4L;
    Long idTheater = 1L;

    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );
    pagingRequestTO.setTimestamp( cal.getTime() );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, idWeek );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_THEATER_ID, idTheater );

    TheaterBookingWeekTO theaterBookingWeekTO = this.bookingServiceEJB.findBookingTheater( pagingRequestTO );
    Gson gson = new Gson();
    System.out.println( gson.toJson( theaterBookingWeekTO ) );
    Assert.assertNotNull( theaterBookingWeekTO );
  }

 }
