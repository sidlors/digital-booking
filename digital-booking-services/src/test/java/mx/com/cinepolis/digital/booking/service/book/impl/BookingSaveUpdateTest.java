package mx.com.cinepolis.digital.booking.service.book.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekScreenDO;
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

import org.apache.commons.collections.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BookingSaveUpdateTest extends AbstractDBEJBTestUnit
{
  private static final long THEATER_ID = 10L;
  private BookingServiceEJB bookingServiceEJB;
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
    initializeData( "dataset/business/booking.sql" );

  }

  /**
   * Se programa con 0 salas y 1 copia 0
   */
  @Test
  public void test()
  {
    int copy = 0;
    int copyZero = 1;
    Long eventId = 20L;
    Integer weekId = 1;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED ) );
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );
    bookingDAO.flush();
  }

  /**
   * Se programa con 2 slas y 1 sala 0
   */
  @Test
  public void test2()
  {
    int copy = 2;
    int copyZero = 1;
    Long eventId = 20L;
    Integer weekId = 1;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );
    bookingDAO.flush();
  }

  /**
   * Se programa con 2 salas y 1 sala 0, se actualiza la semana y se quita 1 semana 0
   */
  @Test
  public void test3()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 1;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 1, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    copy = 2;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );

  }

  /**
   * Se programa con 2 salas y 1 sala 0, se actualiza la semana y se quita 1 sala 0
   */
  @Test
  public void test4()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 1;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 1, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    BookingWeekDO bw = bookingDO.getBookingWeekDOList().get( 0 );

    int booked = 0;
    int canceled = 0;
    int terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 2, booked );
    Assert.assertEquals( 0, canceled );
    Assert.assertEquals( 0, terminated );

    copy = 1;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.CANCELED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );

    bw = bookingDO.getBookingWeekDOList().get( 0 );

    booked = 0;
    canceled = 0;
    terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 1, booked );
    Assert.assertEquals( 1, canceled );
    Assert.assertEquals( 0, terminated );
  }

  @Test
  public void test5()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 1;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 1, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    BookingWeekDO bw = bookingDO.getBookingWeekDOList().get( 0 );

    int booked = 0;
    int canceled = 0;
    int terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 2, booked );
    Assert.assertEquals( 0, canceled );
    Assert.assertEquals( 0, terminated );

    copy = 1;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 1, bookingWeekScreenDAO.count() );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );

    bw = bookingDO.getBookingWeekDOList().get( 0 );
    Assert.assertEquals( copy, bw.getQtCopy() );

    booked = 0;
    canceled = 0;
    terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 1, booked );
    Assert.assertEquals( 0, canceled );
    Assert.assertEquals( 0, terminated );
  }

  /**
   * Se cancelan las copias, sólo se manda 0 en QtCopy y QtCopyZero
   */
  @Test
  public void test6()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 1;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 1, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    BookingWeekDO bw = bookingDO.getBookingWeekDOList().get( 0 );

    int booked = 0;
    int canceled = 0;
    int terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 2, booked );
    Assert.assertEquals( 0, canceled );
    Assert.assertEquals( 0, terminated );

    copy = 0;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    Assert.assertEquals( BookingStatus.CANCELED.getId(), bookingDO.getBookingWeekDOList().get( 0 ).getIdBookingStatus()
        .getIdBookingStatus().intValue() );
    Assert.assertFalse( bookingDO.getFgBooked() );

    bw = bookingDO.getBookingWeekDOList().get( 0 );
    Assert.assertEquals( 0, bw.getQtCopy() );
    Assert.assertEquals( 0, bw.getQtCopyScreenZero() );
    Assert.assertEquals( 0, bw.getQtCopyScreenZeroTerminated() );

    booked = 0;
    canceled = 0;
    terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 0, booked );
    Assert.assertEquals( 2, canceled );
    Assert.assertEquals( 0, terminated );
  }
  
  
  /**
   * Se cancelan las copias, sólo se manda el estado Cancel
   */
  @Test
  public void test7()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 1;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 1, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    BookingWeekDO bw = bookingDO.getBookingWeekDOList().get( 0 );

    int booked = 0;
    int canceled = 0;
    int terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 2, booked );
    Assert.assertEquals( 0, canceled );
    Assert.assertEquals( 0, terminated );

    copy = 0;
    copyZero = 1;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.CANCELED );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    Assert.assertEquals( BookingStatus.CANCELED.getId(), bookingDO.getBookingWeekDOList().get( 0 ).getIdBookingStatus()
        .getIdBookingStatus().intValue() );
    Assert.assertFalse( bookingDO.getFgBooked() );

    bw = bookingDO.getBookingWeekDOList().get( 0 );
    Assert.assertEquals( 0, bw.getQtCopy() );
    Assert.assertEquals( 0, bw.getQtCopyScreenZero() );
    Assert.assertEquals( 0, bw.getQtCopyScreenZeroTerminated() );

    booked = 0;
    canceled = 0;
    terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 0, booked );
    Assert.assertEquals( 2, canceled );
    Assert.assertEquals( 0, terminated );
  }
  
  /**
   * Se cancelan las copias, se manda copias zero = 0 y no se incluyen salas
   */
  @Test
  public void test8()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 1;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 1, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    BookingWeekDO bw = bookingDO.getBookingWeekDOList().get( 0 );

    int booked = 0;
    int canceled = 0;
    int terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 2, booked );
    Assert.assertEquals( 0, canceled );
    Assert.assertEquals( 0, terminated );

    copy = 2;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    Assert.assertEquals( BookingStatus.CANCELED.getId(), bookingDO.getBookingWeekDOList().get( 0 ).getIdBookingStatus()
        .getIdBookingStatus().intValue() );
    Assert.assertFalse( bookingDO.getFgBooked() );

    bw = bookingDO.getBookingWeekDOList().get( 0 );
    Assert.assertEquals( 0, bw.getQtCopy() );
    Assert.assertEquals( 0, bw.getQtCopyScreenZero() );
    Assert.assertEquals( 0, bw.getQtCopyScreenZeroTerminated() );

    booked = 0;
    canceled = 0;
    terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 0, booked );
    Assert.assertEquals( 2, canceled );
    Assert.assertEquals( 0, terminated );
  }
  
  /**
   * Se cancelan las copias, se manda copias zero = 0 y todas incluyen las salas en estado cancelado
   */
  @Test
  public void test9()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 1;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 1, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    BookingWeekDO bw = bookingDO.getBookingWeekDOList().get( 0 );

    int booked = 0;
    int canceled = 0;
    int terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 2, booked );
    Assert.assertEquals( 0, canceled );
    Assert.assertEquals( 0, terminated );

    copy = 2;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED, 
      Arrays.asList( new Screen( 1, BookingStatus.CANCELED ), new Screen( 2, BookingStatus.CANCELED ) ));
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    Assert.assertEquals( BookingStatus.CANCELED.getId(), bookingDO.getBookingWeekDOList().get( 0 ).getIdBookingStatus()
        .getIdBookingStatus().intValue() );
    Assert.assertFalse( bookingDO.getFgBooked() );

    bw = bookingDO.getBookingWeekDOList().get( 0 );
    Assert.assertEquals( 0, bw.getQtCopy() );
    Assert.assertEquals( 0, bw.getQtCopyScreenZero() );
    Assert.assertEquals( 0, bw.getQtCopyScreenZeroTerminated() );

    booked = 0;
    canceled = 0;
    terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 0, booked );
    Assert.assertEquals( 2, canceled );
    Assert.assertEquals( 0, terminated );
  }
  
  /**
   * Se programa con 2 salas, y se programa la siguiente semana igual
   */
  @Test
  public void test10()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 0;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    weekId = 2;
    copy = 2;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 2,bookingDO.getBookingWeekDOList().size());
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZeroTerminated() );

  }
  
  
  /**
   * Se programa con 2 salas, y se programa la siguiente semana similar, se mueven las salas
   */
  @Test
  public void test11()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 0;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    weekId = 2;
    copy = 2;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 3, BookingStatus.BOOKED ), new Screen( 4, BookingStatus.BOOKED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 2,bookingDO.getBookingWeekDOList().size());
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZeroTerminated() );

  }
  
  
  /**
   * Se programa con 2 salas, y se programa la siguiente semana similar, se mueven las salas
   * Se termina 1 sala en la 2a semana
   */
  @Test
  public void test12()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 0;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    weekId = 2;
    copy = 2;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 3, BookingStatus.BOOKED ), new Screen( 4, BookingStatus.BOOKED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 2,bookingDO.getBookingWeekDOList().size());
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZeroTerminated() );
    
    
    weekId = 2;
    copy = 1;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 3, BookingStatus.TERMINATED ), new Screen( 4, BookingStatus.BOOKED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );
    
    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    BookingWeekDO bw = bookingDO.getBookingWeekDOList().get( 1 );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 2,bookingDO.getBookingWeekDOList().size());
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    
    Assert.assertEquals( 1, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZeroTerminated() );
    
    
    int booked = 0;
    int canceled = 0;
    int terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 1, booked );
    Assert.assertEquals( 0, canceled );
    Assert.assertEquals( 1, terminated );

  }
  
  
  
  /**
   * Se programa con 2 salas, y se programa la siguiente semana similar, se mueven las salas
   * Se terminan 2 salas en la 2a semana
   */
  @Test
  public void test13()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 0;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    weekId = 2;
    copy = 2;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 3, BookingStatus.BOOKED ), new Screen( 4, BookingStatus.BOOKED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 2,bookingDO.getBookingWeekDOList().size());
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZeroTerminated() );
    
    
    weekId = 2;
    copy = 0;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 3, BookingStatus.TERMINATED ), new Screen( 4, BookingStatus.TERMINATED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );
    
    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertFalse( bookingDO.getFgBooked() );
    BookingWeekDO bw = bookingDO.getBookingWeekDOList().get( 1 );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( BookingStatus.TERMINATED.getId(), bw.getIdBookingStatus().getIdBookingStatus().intValue() );
    Assert.assertEquals( 2,bookingDO.getBookingWeekDOList().size());
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZeroTerminated() );
    
    
    int booked = 0;
    int canceled = 0;
    int terminated = 0;
    for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
    {
      if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
      {
        booked++;
      }
      else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.CANCELED.getId() )
      {
        canceled++;
      }
      else
      {
        terminated++;
      }
    }
    Assert.assertEquals( 0, booked );
    Assert.assertEquals( 0, canceled );
    Assert.assertEquals( 2, terminated );

  }
  
  
  /**
   * Se programa con 2 salas, y se programa la siguiente semana igual
   * Se cancela la semana 1
   */
  @Test
  public void test14()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 0;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    weekId = 2;
    copy = 2;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 2,bookingDO.getBookingWeekDOList().size());
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZeroTerminated() );
    
    
    
    weekId = 1;
    copy = 0;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.CANCELED,
      Arrays.asList( new Screen( 1, BookingStatus.CANCELED ), new Screen( 2, BookingStatus.CANCELED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );
    
    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 1,bookingDO.getBookingWeekDOList().size());
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );

  }
  
  
  /**
   * Se programa con 2 salas, y se programa la siguiente semana similar, se mueven las salas y se termina 1
   */
  @Test
  public void test15()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 0;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    weekId = 2;
    copy = 1;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 3, BookingStatus.BOOKED ), new Screen( 4, BookingStatus.TERMINATED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 2,bookingDO.getBookingWeekDOList().size());
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    
    Assert.assertEquals( 1, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopy() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 1 ).getQtCopyScreenZeroTerminated() );

  }
  
  
  /**
   * Se programa con 2 salas y 1 sala 0, se actualiza la semana y se quita 1 semana 0
   * Se agregan comentarios y shows
   */
  @Test
  public void test16()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 1;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED, "Observations", Arrays.asList( 1,2 ) ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 1, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    copy = 2;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED ), new Screen( 2, BookingStatus.BOOKED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );

  }
  
  /**
   * Se programa con 2 salas y 1 sala 0, se actualiza la semana y se quita 1 semana 0
   * Se agregan comentarios y shows
   */
  @Test
  public void test17()
  {
    Long eventId = 20L;
    Integer weekId = 1;
    int bookingCount = bookingDAO.count();
    int bookingWeekCount = bookingWeekDAO.count();
    int bookingWeekScreenCount = bookingWeekScreenDAO.count();

    int copy = 2;
    int copyZero = 1;
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED, "Observations", Arrays.asList( 1,2 ) ), new Screen( 2, BookingStatus.BOOKED ) ) ) );
    int counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    Assert.assertEquals( bookingCount + 1, bookingDAO.count() );
    Assert.assertEquals( bookingWeekCount + 1, bookingWeekDAO.count() );
    Assert.assertEquals( bookingWeekScreenCount + 2, bookingWeekScreenDAO.count() );

    BookingDO bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertEquals( 2, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopy() );
    Assert.assertEquals( 1, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertNotNull( bookingDO );

    copy = 2;
    copyZero = 0;
    bookingTOs = new ArrayList<BookingTO>();
    BookingTO bookingTO = mockBookingTO( eventId, weekId, copy, copyZero, BookingStatus.BOOKED,
      Arrays.asList( new Screen( 1, BookingStatus.BOOKED, "cambio", Arrays.asList( 3)  ), new Screen( 2, BookingStatus.BOOKED ) ) );
    bookingTO.setId( bookingDO.getIdBooking() );
    bookingTOs.add( bookingTO );
    counter = 1;
    for(ScreenTO screenTO : bookingTOs.get( 0 ).getScreens())
    {
      screenTO.setId( Long.valueOf( counter ) );
      counter ++;
    }
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    bookingDO = bookingDAO.findByEventIdAndTheaterId( THEATER_ID, eventId );
    Assert.assertNotNull( bookingDO );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZero() );
    Assert.assertEquals( 0, bookingDO.getBookingWeekDOList().get( 0 ).getQtCopyScreenZeroTerminated() );
    
    BookingWeekDO bw = bookingDO.getBookingWeekDOList().get( 0 );
    Assert.assertNotNull( bw );
    Assert.assertEquals( 2, bw.getBookingWeekScreenDOList().size() );
    BookingWeekScreenDO bws = bw.getBookingWeekScreenDOList().get( 0 );
    Assert.assertNotNull( bws );
    Assert.assertNotNull( bws.getIdObservation() );
    Assert.assertNotNull("cambio", bws.getIdObservation().getDsObservation() );
    Assert.assertEquals( 1, bws.getBookingWeekScreenShowDOList().size() );
    
    


  }

  private BookingTO mockBookingTO( Long eventId, Integer weekId, int copy, int copyZero, BookingStatus status )
  {
    return mockBookingTO( eventId, weekId, copy, copyZero, status, null );
  }

  private BookingTO mockBookingTO( Long eventId, Integer weekId, int copy, int copyZero, BookingStatus status,
      List<Screen> screens )
  {
    BookingTO booking = new BookingTO();
    booking.setEvent( new EventTO() );
    booking.getEvent().setIdEvent( eventId );
    booking.setCopy( copy );
    booking.setCopyScreenZero( copyZero );
    booking.setStatus( new CatalogTO( status.getIdLong() ) );
    booking.setUserId( 1L );
    booking.setTimestamp( new Date() );
    booking.setTheater( new TheaterTO() );
    booking.getTheater().setId( THEATER_ID );
    booking.setWeek( new WeekTO( weekId ) );
    if( CollectionUtils.isNotEmpty( screens ) )
    {
      booking.setScreens( new ArrayList<ScreenTO>() );
      for( Screen screen : screens )
      {
        ScreenTO s = new ScreenTO();
        s.setNuScreen( screen.getNumber() );
        s.setBookingStatus( new CatalogTO( screen.getBookingStatus().getIdLong() ) );
        if( screen.getObservations() != null )
        {
          s.setBookingObservation( new BookingObservationTO() );
          s.getBookingObservation().setObservation( screen.getObservations() );
        }
        if( CollectionUtils.isNotEmpty( screen.getShowings() ) )
        {
          s.setShowings( new ArrayList<CatalogTO>() );
          for( Integer show : screen.getShowings() )
          {
            s.getShowings().add( new CatalogTO( show.longValue() ) );
          }
        }
        booking.getScreens().add( s );
      }
    }

    return booking;
  }
  static class Screen implements Serializable
  {
    private static final long serialVersionUID = -2624724291518189453L;

    private int number;
    private BookingStatus bookingStatus;
    private String observations;
    private List<Integer> showings;

    public Screen()
    {
    }

    public Screen( int number, BookingStatus bookingStatus )
    {
      this.number = number;
      this.bookingStatus = bookingStatus;
    }

    public Screen( int number, BookingStatus bookingStatus, String observations )
    {
      this.number = number;
      this.bookingStatus = bookingStatus;
      this.observations = observations;
    }

    public Screen( int number, BookingStatus bookingStatus, String observations, List<Integer> showings )
    {
      this.number = number;
      this.bookingStatus = bookingStatus;
      this.observations = observations;
      this.showings = showings;
    }

    /**
     * @return the number
     */
    public int getNumber()
    {
      return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber( int number )
    {
      this.number = number;
    }

    /**
     * @return the bookingStatus
     */
    public BookingStatus getBookingStatus()
    {
      return bookingStatus;
    }

    /**
     * @param bookingStatus the bookingStatus to set
     */
    public void setBookingStatus( BookingStatus bookingStatus )
    {
      this.bookingStatus = bookingStatus;
    }

    /**
     * @return the observations
     */
    public String getObservations()
    {
      return observations;
    }

    /**
     * @param observations the observations to set
     */
    public void setObservations( String observations )
    {
      this.observations = observations;
    }

    /**
     * @return the showings
     */
    public List<Integer> getShowings()
    {
      return showings;
    }

    /**
     * @param showings the showings to set
     */
    public void setShowings( List<Integer> showings )
    {
      this.showings = showings;
    }

  }
}
