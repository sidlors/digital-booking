package mx.com.cinepolis.digital.booking.service.book.impl;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.MovieBookingWeekTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterBookingWeekTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.ReflectionHelper;
import mx.com.cinepolis.digital.booking.dao.util.TheaterDOToTheaterTOTransformer;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.EventDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.ScreenDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.TheaterDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.WeekDAOImpl;
import mx.com.cinepolis.digital.booking.service.book.BookingServiceEJB;
import mx.com.cinepolis.digital.booking.service.income.IncomeServiceEJB;
import mx.com.cinepolis.digital.booking.service.income.impl.IncomeServiceEJBImpl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;

public class BookingServiceEJBTest extends AbstractDBEJBTestUnit
{
  private BookingServiceEJB bookingServiceEJB;
  private IncomeServiceEJB incomeServiceEJB;
  private BookingDAO bookingDAO;
  private TheaterDAO theaterDAO;
  private EventDAO eventDAO;
  private WeekDAO weekDAO;
  private ScreenDAO screenDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    bookingServiceEJB = new BookingServiceEJBImpl();
    incomeServiceEJB= new IncomeServiceEJBImpl();
    bookingDAO = new BookingDAOImpl();
    theaterDAO = new TheaterDAOImpl();
    eventDAO = new EventDAOImpl();
    weekDAO = new WeekDAOImpl();
    screenDAO = new ScreenDAOImpl();

    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    connect( bookingServiceEJB );
    connect( bookingDAO );
    connect( theaterDAO );
    connect( eventDAO );
    connect( weekDAO );
    connect( screenDAO );
    connect(incomeServiceEJB);
    ReflectionHelper.set( incomeServiceEJB, "incomeServiceEJB", bookingServiceEJB );
  }

  @Test
  public void testSaveOrUpdateBooking()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );
    int numBookingsBefore = bookingDAO.count();

    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    BookingTO newBookingTO = new BookingTO();
    newBookingTO.setTheater( (TheaterTO) new TheaterDOToTheaterTOTransformer().transform( theaterDAO.find( 1 ) ) );
    newBookingTO.setEvent( eventDAO.getEvent( 1L ) );
    newBookingTO.setWeek( weekDAO.getWeek( 1 ) );
    newBookingTO.setCopy( 3 );
    newBookingTO.setTimestamp( cal.getTime() );
    newBookingTO.setUserId( 1L );
    CatalogTO status=new CatalogTO();
    status.setFgActive( true );
    status.setId( 1L );
    status.setIdLanguage( 1L );
    status.setName( "BOOKED" );
    status.setTimestamp( new Date() );
    status.setUserId( 1L );
    status.setUsername( "User1" );
    newBookingTO.setStatus( status );
    bookingTOs.add( newBookingTO );

    BookingTO existingBookingTO = bookingDAO.get( 1L, 1 );
    existingBookingTO.setCopy( 3 );
    AbstractEntityUtils.copyElectronicSignature( newBookingTO, existingBookingTO );
    existingBookingTO.setTimestamp( cal.getTime() );
    bookingTOs.add( existingBookingTO );

    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );

    int numBookingsAfter = bookingDAO.count();
    Assert.assertEquals( numBookingsBefore, numBookingsAfter );
    Assert.assertEquals( 3, bookingDAO.find( 1L ).getBookingWeekDOList().get( 0 ).getQtCopy() );
  }

  @Test
  public void testCancelBooking_withScreens()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 23, 12, 0, 0 );

    BookingTO existingBookingTO = bookingDAO.get( 13L, 4 );
    Assert.assertEquals( BookingStatus.BOOKED.getId(), existingBookingTO.getStatus().getId().intValue() );
    Assert.assertFalse( existingBookingTO.getScreens().isEmpty() );
    Assert.assertEquals( 1, existingBookingTO.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getId(), existingBookingTO.getScreens().get( 0 ).getBookingStatus()
        .getId().intValue() );

    existingBookingTO.setTimestamp( cal.getTime() );
    existingBookingTO.setUserId( 1L );
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( existingBookingTO );

    bookingServiceEJB.cancelBooking( bookingTOs );

    BookingTO cancelledBookingTO = bookingDAO.get( 13L, 4 );
    Assert.assertEquals( BookingStatus.CANCELED.getId(), cancelledBookingTO.getStatus().getId().intValue() );
    for( ScreenTO screen : cancelledBookingTO.getScreens() )
    {
      Assert.assertEquals( BookingStatus.CANCELED.getId(), screen.getBookingStatus().getId().intValue() );
    }

  }

  @Test
  public void testCancelBooking_withoutScreens()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 23, 12, 0, 0 );

    BookingTO existingBookingTO = bookingDAO.get( 14L, 4 );
    Assert.assertEquals( BookingStatus.BOOKED.getId(), existingBookingTO.getStatus().getId().intValue() );
    Assert.assertTrue( existingBookingTO.getScreens().isEmpty() );
    existingBookingTO.setTimestamp( cal.getTime() );
    existingBookingTO.setUserId( 1L );

    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( existingBookingTO );
    bookingServiceEJB.cancelBooking( bookingTOs );

    BookingTO cancelledBookingTO = bookingDAO.get( 14L, 4 );
    Assert.assertEquals( BookingStatus.CANCELED.getId(), cancelledBookingTO.getStatus().getId().intValue() );
    Assert.assertTrue( cancelledBookingTO.getScreens().isEmpty() );

  }

  @Test(expected = DigitalBookingException.class)
  @Ignore
  public void testCancelBooking_dateInvalid()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 10, 0, 0, 0 );

    BookingTO existingBookingTO = bookingDAO.get( 13L, 4 );
    Assert.assertEquals( BookingStatus.BOOKED.getId(), existingBookingTO.getStatus().getId().intValue() );
    Assert.assertFalse( existingBookingTO.getScreens().isEmpty() );
    Assert.assertEquals( 1, existingBookingTO.getScreens().size() );

    existingBookingTO.setTimestamp( cal.getTime() );
    existingBookingTO.setUserId( 1L );

    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( existingBookingTO );
    bookingServiceEJB.cancelBooking( bookingTOs );

  }

  @Test(expected = DigitalBookingException.class)
  public void testCancelBooking_hasMoreBookings()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 0, 0, 0 );

    BookingTO existingBookingTO = bookingDAO.get( 1L, 1 );
    Assert.assertEquals( BookingStatus.BOOKED.getId(), existingBookingTO.getStatus().getId().intValue() );
    Assert.assertFalse( existingBookingTO.getScreens().isEmpty() );
    Assert.assertEquals( 1, existingBookingTO.getScreens().size() );

    existingBookingTO.setTimestamp( cal.getTime() );
    existingBookingTO.setUserId( 1L );
    existingBookingTO.setId( 9090L );

    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( existingBookingTO );
    bookingServiceEJB.cancelBooking( bookingTOs );

  }

  @Test
  public void testTerminateBooking_withScreens()
  {
    BookingTO booking = bookingDAO.get( 2L, 4 );
    Assert.assertEquals( BookingStatus.BOOKED.getId(), booking.getStatus().getId().intValue() );
    Assert.assertFalse( booking.getScreens().isEmpty() );
    Assert.assertEquals( 1, booking.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getId(), booking.getScreens().get( 0 ).getBookingStatus().getId()
        .intValue() );

    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 23, 0, 0, 0 );

    booking.setTimestamp( cal.getTime() );
    booking.setUserId( 1L );

    bookingServiceEJB.terminateBooking( booking );

    BookingTO terminatedBookingTO = bookingDAO.get( 2L, 4 );
    Assert.assertEquals( BookingStatus.TERMINATED.getId(), terminatedBookingTO.getStatus().getId().intValue() );
    for( ScreenTO screen : terminatedBookingTO.getScreens() )
    {
      Assert.assertEquals( BookingStatus.TERMINATED.getId(), screen.getBookingStatus().getId().intValue() );
    }
  }

  @Test
  public void testTerminateBooking_withoutScreens()
  {
    BookingTO booking = bookingDAO.get( 12L, 4 );
    Assert.assertEquals( BookingStatus.BOOKED.getId(), booking.getStatus().getId().intValue() );
    Assert.assertTrue( booking.getScreens().isEmpty() );

    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 23, 0, 0, 0 );

    booking.setTimestamp( cal.getTime() );
    booking.setUserId( 1L );

    bookingServiceEJB.terminateBooking( booking );

    BookingTO terminatedBookingTO = bookingDAO.get( 12L, 4 );
    Assert.assertEquals( BookingStatus.TERMINATED.getId(), terminatedBookingTO.getStatus().getId().intValue() );
    Assert.assertTrue( terminatedBookingTO.getScreens().isEmpty() );
  }
  @Test
  public void testFindBookingMovies()
  {
    List<TheaterDO> theaters = theaterDAO.findAll();
    StringBuilder sb = new StringBuilder();
    for( TheaterDO to : theaters )
    {
      if( to.getIdRegion().getIdRegion().intValue() == 1 )
      {
        sb.append( "id:" + to.getIdTheater() + ", name: " + to.getTheaterLanguageDOList().get( 0 ).getDsName() );
        sb.append( "\n" );
        if( CollectionUtils.isNotEmpty( to.getScreenDOList() ) )
          for( ScreenDO s : to.getScreenDOList() )
          {
            sb.append( "\tScreen id: " + s.getIdScreen() + ", num:" + s.getNuScreen() );
            sb.append( "\n" );
            for( CategoryDO mf : s.getCategoryDOList() )
            {
              if( mf.getIdCategoryType().getIdCategoryType().intValue() == 2 )
              {
                sb.append( "\t\t\tid:" + mf.getIdCategory() + ", name:"
                    + mf.getCategoryLanguageDOList().get( 0 ).getDsName() );
                sb.append( "\n" );
              }
            }
          }
      }
    }
    
    System.out.println(sb.toString());
    
    Long eventId = 1L;
    Long weekId = 1L;
    Long regionId = 1L;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 10 );
    
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 23, 0, 0, 0 );
    
    pagingRequestTO.setTimestamp( cal.getTime() );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, eventId );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, weekId );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, regionId );

    MovieBookingWeekTO movieBookingWeekTO = this.bookingServiceEJB.findBookingMovies( pagingRequestTO );
    PagingResponseTO<BookingTO> response = movieBookingWeekTO.getResponse();
    Assert.assertNotNull( response );
    Assert.assertEquals( 0, response.getTotalCount() );

    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }

  @Test
  @Ignore
  public void testFindBookingMovies_NextWeeks()
  {
    Long eventId = 1L;
    long weekId = 1L;
    Long regionId = 1L;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 5 );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, eventId );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, weekId );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, regionId );

    for( int i = 0; i < 5; i++ )
    {
      weekId += 1;
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, weekId );
      MovieBookingWeekTO movieBookingWeekTO = this.bookingServiceEJB.findBookingMovies( pagingRequestTO );
      PagingResponseTO<BookingTO> response = movieBookingWeekTO.getResponse();
      Assert.assertNotNull( response );
      Assert.assertEquals( 5, response.getTotalCount() );

      BookingTO to = response.getElements().get( 0 );
      Assert.assertEquals( weekId, to.getExhibitionWeek() );

    }

  }

  @Test
  @Ignore
  public void testFindBookingMovies_NewEvent()
  {
    Long eventId = 7L;
    Long weekId = 2L;
    Long regionId = 1L;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 5 );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, eventId );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, weekId );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, regionId );

    MovieBookingWeekTO movieBookingWeekTO = this.bookingServiceEJB.findBookingMovies( pagingRequestTO );
    PagingResponseTO<BookingTO> response = movieBookingWeekTO.getResponse();
    Assert.assertNotNull( response );
    Assert.assertEquals( 5, response.getTotalCount() );

    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }

  @Test
  public void testFindBookingTheaterByCriteria()
  {
    Long weekId = 2L;
    Long theaterId = 1L;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    Calendar cal = Calendar.getInstance();
    cal.set( 2014,  Calendar.JANUARY, 2 );
    pagingRequestTO.setTimestamp( cal.getTime() );
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 10 );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, weekId );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_THEATER_ID, theaterId );

    TheaterBookingWeekTO response = this.bookingServiceEJB.findBookingTheater( pagingRequestTO );
    Assert.assertNotNull( response );
    for( BookingTheaterTO bookingTheaterTO : response.getResponse().getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( bookingTheaterTO ) );
    }
  }

  @Test
  public void testFindWeeksActive()
  {
    AbstractTO abstractTO = new AbstractTO();
    abstractTO.setTimestamp( new GregorianCalendar( 2014, 0, 9 ).getTime() );

    List<WeekTO> weekTOs = bookingServiceEJB.findWeeksActive( abstractTO );

    Assert.assertNotNull( weekTOs );
    Assert.assertEquals( 4, weekTOs.size() );
  }

  @Test
  public void testFindAllActiveMovies()
  {
    List<CatalogTO> catalogTOs = bookingServiceEJB.findAllActiveMovies(false,false,false);

    Assert.assertNotNull( catalogTOs );
    Assert.assertEquals( 10, catalogTOs.size() );
  }

  @Test
  public void testFindAllActiveRegions()
  {
    AbstractTO abstractTO = new AbstractTO();
    abstractTO.setUserId( 1L );

    List<CatalogTO> catalogTOs = bookingServiceEJB.findAllActiveRegions( abstractTO );

    Assert.assertNotNull( catalogTOs );
    Assert.assertEquals( 3, catalogTOs.size() );
  }

  @Test
  public void testFindAllPremieres()
  {
    List<EventMovieTO> eventMovieTOs = bookingServiceEJB.findAllPremieres();

    Assert.assertNotNull( eventMovieTOs );
    Assert.assertEquals( 11, eventMovieTOs.size() );
  }

  /**
   * Prueba unitaria con Paginacion
   */
  @Test
  public void testFindTheatersByIdWeekAndIdRegionAndLanguage()
  {
    PagingResponseTO<TheaterTO> theaters = null;
    PagingRequestTO requestTO = new PagingRequestTO();
    requestTO.setNeedsPaging( Boolean.TRUE );
    requestTO.setPage( 0 );
    requestTO.setPageSize( 2 );
    requestTO.setFilters( new HashMap<ModelQuery, Object>() );
    requestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, 1 );
    requestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, 1 );
    requestTO.setLanguage( Language.SPANISH );
    theaters = bookingServiceEJB.findTheatersByIdWeekAndIdRegion( requestTO );
    Assert.assertNotNull( theaters );
    Assert.assertEquals( 1, theaters.getTotalCount() );
    Assert.assertEquals( 1, theaters.getElements().size() );

  }

  @Test
  public void testFindAvailableMovies()
  {
    Long idWeek = 1L;
    Long idTheater = 1L;

    List<CatalogTO> availableMovies = bookingServiceEJB.findAvailableMovies( idWeek, idTheater );

    Assert.assertNotNull( availableMovies );
    Assert.assertEquals( 10, availableMovies.size() );
  }

  @Test
  public void testFindTheaterScreens()
  {
    Long idTheater = 1L;
    Long idEvent = 1L;

    List<ScreenTO> theaterScreens = bookingServiceEJB.findTheaterScreens( idTheater, idEvent );

    int numScreens = screenDAO.findAllActiveByIdCinema( idTheater.intValue() ).size();
    Assert.assertNotNull( theaterScreens );
    Assert.assertEquals( numScreens + 1, theaterScreens.size() );
    Assert.assertEquals( 4, theaterScreens.size() );
  }

  @Test
  public void testSaveOrUpdateBookingObservation_ExistentObservation()
  {
    Long idBooking = 1L;
    String observationText = "Observation text";
    BookingObservationTO bookingObservationTO = new BookingObservationTO();
    bookingObservationTO.setUserId( 1L );
    bookingObservationTO.setId( 1L );
    bookingObservationTO.setObservation( observationText );
    bookingObservationTO.setIdBooking( idBooking );
    bookingObservationTO.setIdBookingWeek( 1L );
    bookingObservationTO.setIdBookingWeekScreen( 1L );

    bookingServiceEJB.saveOrUpdateBookingObservation( bookingObservationTO );

    Assert.assertEquals( observationText, bookingDAO.find( idBooking ).getBookingWeekDOList().get( 0 )
        .getBookingWeekScreenDOList().get( 0 ).getIdObservation().getDsObservation() );
  }

  @Test
  public void testSaveOrUpdateBookingObservation_NewObservation()
  {
    String observationText = "Observation text";
    BookingObservationTO bookingObservationTO = new BookingObservationTO();
    bookingObservationTO.setUserId( 1L );
    bookingObservationTO.setId( null );
    bookingObservationTO.setObservation( observationText );
    bookingObservationTO.setIdBookingWeekScreen( 12L );

    bookingServiceEJB.saveOrUpdateBookingObservation( bookingObservationTO );

    Assert.assertEquals( observationText, bookingDAO.find( 12L ).getBookingWeekDOList().get( 0 )
        .getBookingWeekScreenDOList().get( 0 ).getIdObservation().getDsObservation() );
  }
  /**
   * @author jcarbajal
   * Description- test to count prerelease booking booked 
   */
  @Test
  public void countPreReleaseBookingBooked_Test()
  {
    EventMovieTO eventMovieTO=new EventMovieTO();
    eventMovieTO.setIdEvent( 13L );
    Long numOfPresales=this.bookingServiceEJB.countPreReleaseBookingBooked( eventMovieTO );
    Assert.assertEquals( 0, numOfPresales.intValue() );
  }
  /**
   * @author jcarbajal
   * Description- test to count theater by region 
   */
  @Test
  public void getTheatersInRegion_Test()
  {
    Long idRegion= 2L;
    int numOfTheaters=this.bookingServiceEJB.getTheatersInRegion( idRegion );
    Assert.assertEquals( 1, numOfTheaters );
  }
  /**
   * @author jcarbajal
   * Description- test to count theater whit presale booking  by region, week and Event 
   */
  @Test
  public void findTheatersByWeekEventAndRegion_Test()
  {
    Long idWeek=4L;
    Long idEvent=13L;
    Long idRegion=2L;
    
    List<Long> theaters=this.bookingServiceEJB.findTheatersByWeekEventAndRegion( idWeek, idEvent, idRegion );
    Assert.assertNotNull( theaters );
    for(Long theater:theaters)
    {
      Assert.assertNotNull( theater );
    }
    
  }
  /**
   * @author jcarbajal
   * Description- test to count events by week and theater 
   */
  @Test
  public void findEventsByWeekAndTheater_Test()
  {
    Long idWeek=4L;
    Long idTheater=2L;
    List<Long> events=this.bookingServiceEJB.findEventsByWeekAndTheater( idWeek, idTheater );
    Assert.assertNotNull( events );
    for(Long event:events)
    {
      Assert.assertNotNull( event );
    }
  }
  /**
   * {@link Description} method for test the function 
   */
  @Test
  public void testHasBookingWeek()
  {
    BookingTO bookingTO=new BookingTO();
    bookingTO.setId( 1L );
    WeekTO weekTO=new WeekTO();
    weekTO.setIdWeek( 1 );
    bookingTO.setWeek( weekTO );
    boolean fgHasMoreWeek=this.bookingServiceEJB.hasBookingWeek( bookingTO );
    Assert.assertTrue( fgHasMoreWeek );
  }
  /**
   * 
   */
  @Test
  public void testIsBookingWeekSent()
  {
    BookingTO bookingTO=new BookingTO();
    bookingTO.setId( 1L );
    WeekTO weekTO=new WeekTO();
    weekTO.setIdWeek( 1 );
    bookingTO.setWeek( weekTO );
    boolean fgIsSent=this.bookingServiceEJB.isBookingWeekSent( bookingTO );
    Assert.assertTrue( !fgIsSent );
  }
}
