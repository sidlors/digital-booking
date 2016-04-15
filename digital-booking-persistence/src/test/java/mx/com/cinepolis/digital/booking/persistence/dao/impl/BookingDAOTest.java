package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.EventQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.query.TheaterQuery;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingTypeDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;

/**
 * Class that implements the {@link BookingDAO} unit tests.
 * 
 * @author gsegura
 */
public class BookingDAOTest extends AbstractDBEJBTestUnit
{

  private BookingDAO bookingDAO;

  private BookingWeekDAO bookingWeekDAO;

  private BookingTypeDAO bookingTypeDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    bookingDAO = new BookingDAOImpl();
    bookingWeekDAO = new BookingWeekDAOImpl();
    bookingTypeDAO = new BookingTypeDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( bookingDAO );
    connect( bookingWeekDAO );
    connect( bookingTypeDAO );
  }

  @Test
  public void testRemoveBookingDO()
  {
    int n = this.bookingDAO.count();
    BookingDO bookingDO = this.bookingDAO.find( 1L );
    Assert.assertTrue( bookingDO.isFgActive() );
    this.bookingDAO.remove( bookingDO );
    Assert.assertEquals( n, this.bookingDAO.count() );
    bookingDO = this.bookingDAO.find( 1L );
    Assert.assertFalse( bookingDO.isFgActive() );
  }

  @Test
  public void testSave()
  {
    int idBookingType = 1;
    BookingTO to = new BookingTO();
    to.setCopy( 10 );
    to.setEvent( new EventMovieTO() );
    to.getEvent().setIdEvent( 9L );
    to.setExhibitionEnd( null );
    to.setExhibitionWeek( 1 );
    to.setStatus( new CatalogTO( 1L ) );
    to.setTheater( new TheaterTO() );
    to.getTheater().setId( 1L );
    to.setWeek( new WeekTO() );
    to.getWeek().setIdWeek( 2 );
    to.setTimestamp( new Date() );
    to.setUserId( 1L );
    to.setIdBookingType( idBookingType );

    this.bookingDAO.save( to );
    Long id = to.getId();

    BookingDO bookingDO = this.bookingDAO.find( id );
    Assert.assertNotNull( bookingDO );

  }

  @Test
  public void testSave_withScreens()
  {
    BookingTO to = new BookingTO();
    to.setCopy( 3 );
    to.setEvent( new EventMovieTO() );
    to.getEvent().setIdEvent( 9L );
    to.setExhibitionEnd( null );
    to.setExhibitionWeek( 1 );
    to.setStatus( new CatalogTO( 1L ) );
    to.setTheater( new TheaterTO() );
    to.getTheater().setId( 1L );
    to.setWeek( new WeekTO() );
    to.getWeek().setIdWeek( 2 );
    to.setTimestamp( new Date() );
    to.setUserId( 1L );
    to.setScreens( Arrays.asList( new ScreenTO( 1L ), new ScreenTO( 2L ), new ScreenTO( 3L ) ) );
    int idBookingType = 1;
    to.setIdBookingType( idBookingType  );

    this.bookingDAO.save( to );
    Long id = to.getId();

    BookingDO bookingDO = this.bookingDAO.find( id );
    Assert.assertNotNull( bookingDO );

  }

  @Test(expected = DigitalBookingException.class)
  public void testSave_UnknownEvent()
  {
    BookingTO to = new BookingTO();
    to.setCopy( 10 );
    to.setEvent( new EventMovieTO() );
    to.getEvent().setIdEvent( 100L );
    to.setExhibitionEnd( null );
    to.setExhibitionWeek( 1 );
    to.setStatus( new CatalogTO( 1L ) );
    to.setTheater( new TheaterTO() );
    to.getTheater().setId( 1L );
    to.setWeek( new WeekTO() );
    to.getWeek().setIdWeek( 2 );
    to.setTimestamp( new Date() );
    to.setUserId( 1L );

    this.bookingDAO.save( to );
  }

  @Test(expected = DigitalBookingException.class)
  public void testSave_UnknownWeek()
  {
    BookingTO to = new BookingTO();
    to.setCopy( 10 );
    to.setEvent( new EventMovieTO() );
    to.getEvent().setIdEvent( 9L );
    to.setExhibitionEnd( null );
    to.setExhibitionWeek( 1 );
    to.setStatus( new CatalogTO( 1L ) );
    to.setTheater( new TheaterTO() );
    to.getTheater().setId( 1L );
    to.setWeek( new WeekTO() );
    to.getWeek().setIdWeek( 1000 );
    to.setTimestamp( new Date() );
    to.setUserId( 1L );

    this.bookingDAO.save( to );
  }

  @Test(expected = DigitalBookingException.class)
  @Ignore
  public void testSave_UnknownStatus()
  {
    BookingTO to = new BookingTO();
    to.setCopy( 10 );
    to.setEvent( new EventMovieTO() );
    to.getEvent().setIdEvent( 1L );
    to.setExhibitionEnd( null );
    to.setExhibitionWeek( 1 );
    to.setStatus( new CatalogTO( 1000L ) );
    to.setTheater( new TheaterTO() );
    to.getTheater().setId( 1L );
    to.setWeek( new WeekTO() );
    to.getWeek().setIdWeek( 2 );
    to.setTimestamp( new Date() );
    to.setUserId( 1L );

    this.bookingDAO.save( to );
  }

  @Test(expected = DigitalBookingException.class)
  public void testSave_UnknownTheater()
  {
    BookingTO to = new BookingTO();
    to.setCopy( 10 );
    to.setEvent( new EventMovieTO() );
    to.getEvent().setIdEvent( 100L );
    to.setExhibitionEnd( null );
    to.setExhibitionWeek( 1 );
    to.setStatus( new CatalogTO( 1L ) );
    to.setTheater( new TheaterTO() );
    to.getTheater().setId( 1111L );
    to.setWeek( new WeekTO() );
    to.getWeek().setIdWeek( 2 );
    to.setTimestamp( new Date() );
    to.setUserId( 1L );

    this.bookingDAO.save( to );
  }

  @Test
  public void testUpdate()
  {
    BookingTO to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 1, to.getCopy() );
    Assert.assertEquals( 1, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertNull( to.getExhibitionEnd() );
    to.setCopy( 3 );
    ScreenTO screen2 = new ScreenTO( 2L );
    screen2.setBookingStatus( new CatalogTO( BookingStatus.BOOKED.getIdLong() ) );
    ScreenTO screen3 = new ScreenTO( 3L );
    screen3.setBookingStatus( new CatalogTO( BookingStatus.BOOKED.getIdLong() ) );
    to.getScreens().add( screen2 );
    to.getScreens().add( screen3 );
    to.setUserId( 1L );
    to.setTimestamp( new Date() );

    this.bookingDAO.update( to );
    this.bookingDAO.flush();

    to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 3, to.getCopy() );
    Assert.assertEquals( 3, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 1 ).getBookingStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 2 ).getBookingStatus().getId() );

  }

  @Test
  public void testUpdate_switchScreen()
  {
    BookingTO to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 1, to.getCopy() );
    Assert.assertEquals( 1, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertNull( to.getExhibitionEnd() );
    to.getScreens().clear();

    ScreenTO screen2 = new ScreenTO( 2L );
    screen2.setBookingStatus( new CatalogTO( BookingStatus.BOOKED.getIdLong() ) );
    screen2.setShowings( Arrays.asList( new CatalogTO( 1L ), new CatalogTO( 2L ) ) );
    to.getScreens().add( screen2 );

    to.setUserId( 1L );
    to.setTimestamp( new Date() );

    this.bookingDAO.update( to );
    this.bookingDAO.flush();

    to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 1, to.getCopy() );
    Assert.assertEquals( 1, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertEquals( 2, to.getScreens().get( 0 ).getShowings().size() );
  }

  @Test
  public void testUpdate_switchScreenAddOneScreen()
  {
    BookingTO to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 1, to.getCopy() );
    Assert.assertEquals( 1, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertNull( to.getExhibitionEnd() );
    to.getScreens().clear();

    ScreenTO screen2 = new ScreenTO( 2L );
    screen2.setBookingStatus( new CatalogTO( BookingStatus.BOOKED.getIdLong() ) );
    screen2.setShowings( Arrays.asList( new CatalogTO( 1L ), new CatalogTO( 2L ) ) );
    to.getScreens().add( screen2 );

    ScreenTO screen3 = new ScreenTO( 3L );
    screen3.setBookingStatus( new CatalogTO( BookingStatus.BOOKED.getIdLong() ) );
    to.getScreens().add( screen3 );
    to.setCopy( 2 );

    to.setUserId( 1L );
    to.setTimestamp( new Date() );

    this.bookingDAO.update( to );
    this.bookingDAO.flush();

    to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 2, to.getCopy() );
    Assert.assertEquals( 2, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertEquals( 2, to.getScreens().get( 0 ).getShowings().size() );

    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 1 ).getBookingStatus().getId() );
    Assert.assertEquals( 0, to.getScreens().get( 1 ).getShowings().size() );
  }

  @Test
  public void testUpdate_remove1Screen()
  {
    BookingTO to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 1, to.getCopy() );
    Assert.assertEquals( 1, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertNull( to.getExhibitionEnd() );
    to.getScreens().clear();
    to.setUserId( 1L );
    to.setTimestamp( new Date() );

    this.bookingDAO.update( to );
    this.bookingDAO.flush();

    to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 1, to.getCopy() );
    Assert.assertEquals( 0, to.getScreens().size() );

  }

  @Test
  public void testUpdate_removeShows()
  {
    BookingTO to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 1, to.getCopy() );
    Assert.assertEquals( 1, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertNull( to.getExhibitionEnd() );

    to.getScreens().get( 0 ).setShowings( null );
    to.setUserId( 1L );
    to.setTimestamp( new Date() );

    this.bookingDAO.update( to );
    this.bookingDAO.flush();

    to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 1, to.getCopy() );
    Assert.assertEquals( 1, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertTrue( to.getScreens().get( 0 ).getShowings().isEmpty() );

  }

  @Test
  public void testUpdate_removeShows2()
  {
    BookingTO to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 1, to.getCopy() );
    Assert.assertEquals( 1, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertNull( to.getExhibitionEnd() );

    to.getScreens().get( 0 ).setShowings( new ArrayList<CatalogTO>() );
    to.setUserId( 1L );
    to.setTimestamp( new Date() );

    this.bookingDAO.update( to );
    this.bookingDAO.flush();

    to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 1, to.getCopy() );
    Assert.assertEquals( 1, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertTrue( to.getScreens().get( 0 ).getShowings().isEmpty() );

  }

  @Test
  public void testUpdate_removeShows3()
  {
    BookingTO to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 1, to.getCopy() );
    Assert.assertEquals( 1, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertNull( to.getExhibitionEnd() );

    to.getScreens().get( 0 ).setShowings( Arrays.asList( new CatalogTO( 1L ), new CatalogTO( 2L ) ) );
    to.setUserId( 1L );
    to.setTimestamp( new Date() );

    this.bookingDAO.update( to );
    this.bookingDAO.flush();

    to = this.bookingDAO.get( 3L, 1 );
    Assert.assertEquals( 1, to.getCopy() );
    Assert.assertEquals( 1, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getScreens().get( 0 ).getBookingStatus().getId() );
    Assert.assertEquals( 2, to.getScreens().get( 0 ).getShowings().size() );

  }

  @Test
  public void testUpdate_addNewWeek()
  {
    BookingTO to = this.bookingDAO.get( 1L, 4 );
    to.setId( 1L );
    to.setWeek( new WeekTO( 5 ) );
    to.setCopy( 3 );
    to.setScreens( null );

    to.setUserId( 1L );
    to.setTimestamp( new Date() );

    this.bookingDAO.update( to );
    this.bookingDAO.flush();

    to = this.bookingDAO.get( 1L, 5 );
    Assert.assertEquals( 3, to.getCopy() );
    Assert.assertEquals( 0, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );
  }

  @Test
  public void testUpdate_addNewWeekWithScreens()
  {
    BookingTO to = this.bookingDAO.get( 1L, 4 );
    to.setId( 1L );
    to.setWeek( new WeekTO( 5 ) );
    to.setCopy( 3 );
    to.setScreens( new ArrayList<ScreenTO>() );

    ScreenTO screen1 = new ScreenTO( 1L );
    screen1.setBookingStatus( new CatalogTO( BookingStatus.BOOKED.getIdLong() ) );
    to.getScreens().add( screen1 );

    ScreenTO screen2 = new ScreenTO( 2L );
    screen2.setBookingStatus( new CatalogTO( BookingStatus.BOOKED.getIdLong() ) );
    screen2.setShowings( Arrays.asList( new CatalogTO( 1L ), new CatalogTO( 2L ) ) );
    to.getScreens().add( screen2 );

    ScreenTO screen3 = new ScreenTO( 3L );
    screen3.setBookingStatus( new CatalogTO( BookingStatus.BOOKED.getIdLong() ) );
    to.getScreens().add( screen3 );

    to.setUserId( 1L );
    to.setTimestamp( new Date() );

    this.bookingDAO.update( to );
    this.bookingDAO.flush();

    to = this.bookingDAO.get( 1L, 5 );
    Assert.assertEquals( 3, to.getCopy() );
    Assert.assertEquals( 3, to.getScreens().size() );
    Assert.assertEquals( BookingStatus.BOOKED.getIdLong(), to.getStatus().getId() );

  }

  @Test(expected = DigitalBookingException.class)
  public void testUpdate_UnknownBooking()
  {
    BookingTO to = this.bookingDAO.get( 1L, 1 );
    to.setId( 999999L );
    to.setCopy( 19 );
    to.setUserId( 1L );
    to.setTimestamp( new Date() );
    to.setStatus( new CatalogTO( 2L ) );
    this.bookingDAO.update( to );

  }

  @Test
  public void testDelete()
  {
    int n = this.bookingDAO.count();
    BookingTO to = new BookingTO();
    to.setId( 1L );
    to.setUserId( 1L );
    to.setTimestamp( new Date() );
    this.bookingDAO.delete( to );

    Assert.assertEquals( n, this.bookingDAO.count() );

    BookingDO bookingDO = this.bookingDAO.find( 1L );
    Assert.assertFalse( bookingDO.isFgActive() );
  }

  @Test
  public void testGet()
  {
    BookingTO to = this.bookingDAO.get( 1L, 1 );

    Assert.assertNotNull( to );

    System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    for( ScreenTO screen : to.getScreens() )
    {
      System.out.println( ToStringBuilder.reflectionToString( screen, ToStringStyle.MULTI_LINE_STYLE ) );
    }

  }

  @Test
  public void testCount()
  {
    int expected = 29;
    Assert.assertEquals( expected, bookingDAO.count() );
  }

  @Test
  public void testFindAll()
  {
    int expected = 65;
    int counter = 0;
    List<BookingDO> bookings = this.bookingDAO.findAll();
    for( BookingDO booking : bookings )
    {
      counter += booking.getBookingWeekDOList().size();
    }

    Assert.assertEquals( expected , counter );
  }

  /**
   * Description: Caso que prueba la consulta paginada de eventos programados como preventas. 
   * Result: 1 lista de eventos programados como preventas.
   */
  @Test
  public void testFindPresaleBookingsByPaging()
  {
    int pageSize = 10;
    int page = 0;
    Long userId = 2L;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( userId );
    // Paging
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    // Sorting
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( EventQuery.EVENT_NAME );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    // Filtering
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_BOOKED, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_PRESALE_ACTIVE, true );

    PagingResponseTO<EventTO> response = this.bookingDAO.findPresaleBookingsByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( EventTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  /**
   * Description: Caso que prueba la consulta paginada de eventos programados como preventas, tomando como parámetro de búsqueda el id de semana. 
   * Result: 1 lista de eventos programados como preventas.
   */
  @Test
  public void testFindPresaleBookingsByPaging_FilterByIdWeek()
  {
    int pageSize = 10;
    int page = 0;
    Long userId = 2L;
    Long filterIdWeek = 4L;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( userId );
    // Paging
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    // Sorting
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( EventQuery.EVENT_NAME );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    // Filtering
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_BOOKED, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_PRESALE_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, filterIdWeek );

    PagingResponseTO<EventTO> response = this.bookingDAO.findPresaleBookingsByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( EventTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  /**
   * Description: Caso que prueba la consulta paginada de eventos programados como preventas, tomando como parámetro de búsqueda el id de la región. 
   * Result: 1 lista de eventos programados como preventas.
   */
  @Test
  public void testFindPresaleBookingsByPaging_FilterByIdRegion()
  {
    int pageSize = 10;
    int page = 0;
    Long userId = 2L;
    Long filterIdRegion = 2L;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( userId );
    // Paging
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    // Sorting
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( EventQuery.EVENT_NAME );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    // Filtering
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_BOOKED, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_PRESALE_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, filterIdRegion );

    PagingResponseTO<EventTO> response = this.bookingDAO.findPresaleBookingsByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( EventTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  /**
   * Description: Caso que prueba la consulta paginada de eventos programados como preventas, tomando como parámetro de búsqueda el nombre del evento. 
   * Result: 1 lista de eventos programados como preventas.
   */
  @Test
  public void testFindPresaleBookingsByPaging_FilterByEventName()
  {
    int pageSize = 10;
    int page = 0;
    Long userId = 2L;
    Long filterIdWeek = 4L;
    Long filterIdRegion = 2L;
    String filterMovieName = "Noé";
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( userId );
    // Paging
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    // Sorting
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( EventQuery.EVENT_NAME );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    // Filtering
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_BOOKED, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_PRESALE_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, filterIdWeek );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, filterIdRegion );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_NAME, filterMovieName );

    PagingResponseTO<EventTO> response = this.bookingDAO.findPresaleBookingsByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( EventTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  /**
   * Description: Caso que prueba la consulta paginada de eventos programados como preventas, tomando como parámetros de búsqueda el id de semana,
   * el id de la región y el nombre del evento. 
   * Result: 1 lista de eventos programados como preventas.
   */
  @Test
  public void testFindPresaleBookingsByPaging_FilterByIdWeekIdRegionAndEventName()
  {
    int pageSize = 10;
    int page = 0;
    Long userId = 2L;
    String filterMovieName = "Noé";
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( userId );
    // Paging
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    // Sorting
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( EventQuery.EVENT_NAME );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    // Filtering
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_BOOKED, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_PRESALE_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_NAME, filterMovieName );

    PagingResponseTO<EventTO> response = this.bookingDAO.findPresaleBookingsByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( EventTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingIdAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_ID );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingIdDesc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_ID );
    pagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingCopiesAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_COPIES );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingCopiesDesc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_COPIES );
    pagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( BookingTO to : response.getElements() )
    {
      Assert.assertNotNull( to );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingExhibitionEndAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_EXHIBITION_END );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
     for( BookingTO to : response.getElements() )
     {
     Assert.assertNotNull( to );
     }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingEventDBSAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_EVENT_DBS_CODE );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
     for( BookingTO to : response.getElements() )
     {
       Assert.assertNotNull( to );
     }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingEventIdSAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_EVENT_ID );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
     for( BookingTO to : response.getElements() )
     {
       Assert.assertNotNull( to );
     }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingEventIdVistaAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_EVENT_ID_VISTA );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
     for( BookingTO to : response.getElements() )
     {
       Assert.assertNotNull( to );
     }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingEventNameAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_EVENT_NAME );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
     for( BookingTO to : response.getElements() )
     {
       Assert.assertNotNull( to );
     }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingExhibitionWeekAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_EXHIBITION_WEEK );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
     for( BookingTO to : response.getElements() )
     {
         Assert.assertNotNull( to );
     }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingRegionIdAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_REGION_ID );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
     for( BookingTO to : response.getElements() )
     {
         Assert.assertNotNull( to );  
     }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingStatusIdAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_STATUS_ID );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
     for( BookingTO to : response.getElements() )
     {
       Assert.assertNotNull( to );
     }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingTheaterIdAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_THEATER_ID );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
     for( BookingTO to : response.getElements() )
     {
         Assert.assertNotNull( to );
     }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingWeekIdAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_WEEK_ID );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
     for( BookingTO to : response.getElements() )
     {
         Assert.assertNotNull( to );
     }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingTheaterStartAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_WEEK_START );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
     for( BookingTO to : response.getElements() )
     {
         Assert.assertNotNull( to );
     }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_SortByBookingWeekEndAsc()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_WEEK_END );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
     for( BookingTO to : response.getElements() )
     {
         Assert.assertNotNull( to );
     }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingCopies()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_COPIES, 10 );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      Assert.assertNotNull( to );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingEventDBSCode()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_DBS_CODE, "10001" );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      Assert.assertNotNull( to );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingEventId()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, 1 );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      Assert.assertNotNull( to );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  /**
   * @jcarbajal
   */
  @Test
  public void testFindAllBypaging_FilterByEventIdAndRegion()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, 1 );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, 1 );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() + " - " + response.getElements().size() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( to.getEvent().getDsEventName() + " -"
          + to.getTheater().getRegion().getCatalogRegion().toString() );
      System.out.println( ToStringBuilder.reflectionToString( to.getTheater().getRegion() ) + " -" );
      System.out.println( ToStringBuilder.reflectionToString( to ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingEventIdVista()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID_VISTA, 1001 );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      Assert.assertNotNull( to );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingEventName()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_NAME, "Cap" );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      Assert.assertNotNull( to );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingExhibitionEnd()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );

    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.APRIL, 10 );
    Date date = cal.getTime();

    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EXHIBITION_END, date );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingExhibitionWeek()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EXHIBITION_WEEK, 1 );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingId()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ID, 1 );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingRegionId()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, 1 );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingStatusId()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_STATUS_ID, 1 );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingTheaterId()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_THEATER_ID, 1 );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingWeekId()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, 1 );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingWeekStart()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );

    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 3 );
    Date date = cal.getTime();

    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_START, date );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_FilterByBookingWeekEnd()
  {
    int pageSize = 5;
    int page = 0;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );

    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 9 );
    Date date = cal.getTime();

    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_END, date );

    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    System.out.println( response.getTotalCount() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  @Test
  public void testFindAllByPaging_withoutPaging()
  {
    int expected = 29;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setNeedsPaging( false );
    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( BookingTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
    Assert.assertEquals( expected, response.getElements().size() );
  }

  /**
   * Prueba unitaria con paginacion
   */
  @Test
  public void testFindTheatersByIdWeekAndIdRegionAndLanguage_withoutPaging()
  {
    PagingResponseTO<TheaterTO> theaters = null;
    PagingRequestTO requestTO = new PagingRequestTO();
    requestTO.setNeedsPaging( Boolean.FALSE );
    requestTO.setFilters( new HashMap<ModelQuery, Object>() );
    requestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, 1 );
    requestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, 1 );
    requestTO.setLanguage( Language.SPANISH );
    theaters = bookingDAO.findTheatersByIdWeekAndIdRegion( requestTO );
    Assert.assertNotNull( theaters );
    Assert.assertEquals( 1, theaters.getTotalCount() );

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
    theaters = bookingDAO.findTheatersByIdWeekAndIdRegion( requestTO );
    Assert.assertNotNull( theaters );
    Assert.assertEquals( 1, theaters.getTotalCount() );
    Assert.assertEquals( 1, theaters.getElements().size() );

  }

  @Test
  public void testFindTheatersByBookingWeekAndRegion()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 5 );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, Boolean.TRUE );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, 1L );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, 4L );
    PagingResponseTO<TheaterTO> theaters = bookingDAO.findTheatersByBookingWeekAndRegion( pagingRequestTO );
    Assert.assertNotNull( theaters );
    Assert.assertFalse( theaters.getElements().isEmpty() );
    for( TheaterTO theater : theaters.getElements() )
    {
      System.out.println( theater + " " + theater.getImageSemaphore() );
    }
  }

  @Test
  public void testFindNumberOfExhibitionWeeks()
  {
    EventTO eventTO = new EventMovieTO();
    eventTO.setIdEvent( 1L );
    int n = this.bookingDAO.findNumberOfExhibitionWeeks( eventTO );
    System.out.println( "n: " + n );
  }

  @Test
  public void testFindBookingsByEventRegionWeek()
  {
    Long idRegion = 1L;
    Long idEvent = 1L;
    Long idWeek = 2L;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setLanguage( Language.ENGLISH );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );

    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, idRegion );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, idEvent );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, idWeek );

    List<BookingTO> bookings = this.bookingDAO.findBookingsByEventRegionWeek( pagingRequestTO );
    Assert.assertNotNull( bookings );
    for( BookingTO bookingTO : bookings )
    {
      System.out.println( ToStringBuilder.reflectionToString( bookingTO ) );
      for( ScreenTO screen : bookingTO.getTheater().getScreens() )
      {
        System.out.println( "\t" + ToStringBuilder.reflectionToString( screen ) );
      }
    }
  }

  @Test
  public void test()
  {
    BookingTO bookingTO = this.bookingDAO.get( 1L, 1 );
    this.bookingWeekDAO.updateSentStatus( bookingTO );
  }

  @Test
  public void testAdjustBookings()
  {
    List<BookingTO> data = new ArrayList<BookingTO>();

    Long idTheater = 2L;
    Integer idWeek = 1;
    AbstractTO to = new AbstractTO();
    to.setUserId( 1L );
    to.setTimestamp( new Date() );
    this.bookingDAO.adjustBookings( idWeek, idTheater, data );

  }

  /**
   * Prueba para el caso base del conteo de bokings asociados a un evento.
   */
  @Test
  public void testCountPrereleaseBooked()
  {
    Long noBokings = this.bookingDAO.countPrereleaseBooked( 2L );
    Assert.assertNotNull( noBokings );
  }

  /**
   * Description: Caso que prueba la consulta de cines que contienen preventas programadas, teniendo como parámetros
   * de búsqueda un id de semana y un id de región. 
   * Result: 1 lista de arreglo de objetos con el id del cine y el id del evento.
   */
  @Test
  public void testFindTheaterBookedPresalesByWeekAndRegion()
  {
    Long idWeek = 4L;
    Long idRegion = 1L;
    List<Object[]> response = this.bookingDAO.findTheaterBookedPresalesByWeekAndRegion( idWeek, idRegion );
    Assert.assertNotNull( response );
    for( Object object : response )
    {
      System.out.println( ToStringBuilder.reflectionToString( object, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }

  /**
   * Description: Caso que prueba la consulta de cines que contienen preventas programadas, teniendo como parámetros
   * de búsqueda un id de semana y un id de región. 
   * Result: 1 lista vacia de arreglo de objetos con el id del cine y el id del evento.
   */
  @Test
  public void testFindTheaterBookedPresalesByWeekAndRegion_NullParameters()
  {
    Long idWeek = null;
    Long idRegion = null;
    List<Object[]> response = this.bookingDAO.findTheaterBookedPresalesByWeekAndRegion( idWeek, idRegion );
    Assert.assertNotNull( response );
    Assert.assertEquals( 0, response.size() );
  }

  /**
   * Description: Caso que prueba la consulta de los ids de cines tomando como parámetros id de semana, id de evento, id de región. 
   * Result: 1 lista de ids de cine.
   */
  @Test
  public void testFindTheatersByWeekEventAndRegion()
  {
    Long idWeek = 4L;
    Long idEvent = 5L;
    Long idRegion = 1L;
    List<Long> response = this.bookingDAO.findTheatersByWeekEventAndRegion( idWeek, idEvent, idRegion );
    Assert.assertNotNull( response );
    for( Long object : response )
    {
      System.out.println( ToStringBuilder.reflectionToString( object, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }

  /**
   * Description: Caso que prueba la consulta de los ids de cines tomando como parámetros id de semana, id de evento, id de región. 
   * Result: 1 lista vacia de ids de cine.
   */
  @Test
  public void testFindTheatersByWeekEventAndRegion_NullParameters()
  {
    Long idWeek = null;
    Long idEvent = null;
    Long idRegion = null;
    List<Long> response = this.bookingDAO.findTheatersByWeekEventAndRegion( idWeek, idEvent, idRegion );
    Assert.assertNotNull( response );
    Assert.assertEquals( 0, response.size() );
  }

  /**
   * Description: Caso que prueba la consulta de los ids de eventos programados como preventas de un cine, 
   * tomando como parámetros id de semana, id de cine. 
   * Result: 1 lista de ids de eventos.
   */
  @Test
  public void testFindEventsByWeekAndTheater()
  {
    Long idWeek = 4L;
    Long idTheater = 1L;
    List<Long> response = this.bookingDAO.findEventsByWeekAndTheater( idWeek, idTheater );
    Assert.assertNotNull( response );
    for( Long object : response )
    {
      System.out.println( ToStringBuilder.reflectionToString( object, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }

  /**
   * Description: Caso que prueba la consulta de los ids de eventos programados como preventas de un cine, 
   * tomando como parámetros id de semana, id de cine. 
   * Result: 1 lista vacia de ids de eventos.
   */
  @Test
  public void testFindEventsByWeekAndTheater_NullParameters()
  {
    Long idWeek = null;
    Long idTheater = null;
    List<Long> response = this.bookingDAO.findEventsByWeekAndTheater( idWeek, idTheater );
    Assert.assertNotNull( response );
    Assert.assertEquals( 0, response.size() );
  }

  /**
   * Description: Caso que prueba la consulta de eventos especiales y su transformación a objetos BookingTheaterTO. 
   * Result: 1 lista de objetos BookingTheaterTO.
   */
  @Test
  public void testExtractBookingSpecialEvents()
  {
    Long idTheater = 1L;
    int idWeek = 4;
    List<BookingTheaterTO> response = this.bookingDAO.extractBookingSpecialEvents( idTheater, idWeek );
    Assert.assertNotNull( response );
    for( BookingTheaterTO object : response )
    {
      System.out.println( ToStringBuilder.reflectionToString( object, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }

  /**
   * Description: Caso que prueba la consulta de eventos especiales con sala cero y su transformación a objetos BookingTheaterTO. 
   * Result: 1 lista de objetos BookingTheaterTO.
   */
  @Test
  public void testExtractBookingSpecialEvents_WithZeroScreen()
  {
    Long idTheater = 2L;
    int idWeek = 4;
    List<BookingTheaterTO> response = this.bookingDAO.extractBookingSpecialEvents( idTheater, idWeek );
    Assert.assertNotNull( response );
    for( BookingTheaterTO object : response )
    {
      System.out.println( ToStringBuilder.reflectionToString( object, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }

  /**
   * Description: Caso que prueba la consulta de eventos especiales con sala cero y su transformación a objetos BookingTheaterTO. 
   * Result: 1 lista de objetos BookingTheaterTO.
   */
  @Test
  public void testFindTheatersByBookingWeekAndRegionForPresaleReport()
  {
    Long regionIdSelected = 1L;
    Long weekIdSelected = 4L;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setNeedsPaging( Boolean.FALSE );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( TheaterQuery.THEATER_ACTIVE, Boolean.TRUE );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, Boolean.TRUE );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, regionIdSelected );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, weekIdSelected );
    PagingResponseTO<TheaterTO> response = this.bookingDAO
        .findTheatersByBookingWeekAndRegionForPresaleReport( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( TheaterTO object : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( object, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }
  /**
   *@author jcarbajal
   *Description ---Test to find presales booked by event , region and week 
   */
  @Test 
  public void findPresaleBookingsByIdEventIdRegionAndIdWeek_Test()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    Long idRegion = 1L;
    Long idWeek = 4L;
    
    EventTO event = new EventTO();
    event.setUserId( 2L );
    event.setIdEvent( 13L );
    
    pagingRequestTO.setNeedsPaging( false );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_ID );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_BOOKED, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_PRESALE_ACTIVE, true );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, idRegion );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, idWeek );

    pagingRequestTO.setUserId( event.getUserId() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, event.getIdEvent() );
    
    PagingResponseTO<BookingTO> response=this.bookingDAO.findPresaleBookingsByIdEventIdRegionAndIdWeek( pagingRequestTO );
    Assert.assertNotNull( response.getElements() );
    for(BookingTO bookingTO:response.getElements())
    {
      Assert.assertNotNull( bookingTO );
    }
    
  }
  /**
   *@author jcarbajal
   *Description ---Test to find booking by event and Theater 
   */
  @Test 
  public void findByEventIdAndTheaterId_Test()
  {
    Long idTheater=1L;
    Long idEvent=5L;
    BookingDO  booking=this.bookingDAO.findByEventIdAndTheaterId( idTheater, idEvent );
    Assert.assertNotNull( booking );
    Assert.assertEquals( idTheater.intValue(), booking.getIdTheater().getIdTheater().intValue() );
  }
  /**
   *@author jcarbajal
   *Description ---Test to find booking by Theater 
   *
   */
  @Test 
  public void findBookedByTheater_Test()
  {
    Long idTheater=1L;
    List<BookingDO>  bookings=this.bookingDAO.findBookedByTheater( idTheater );
    Assert.assertNotNull( bookings );
    for(BookingDO bookingDO:bookings)
    {
      Assert.assertNotNull( bookingDO );
      Assert.assertEquals( idTheater.intValue(), bookingDO.getIdTheater().getIdTheater().intValue() );
    }
  }
  /**
   *@author jcarbajal
   *Description ---Test to find booking  exceeded by week and theater 
   *
   */
  @Test 
  public void findBookingsExceeded_test()
  {
    Long idTheater=1L;
    Long idWeek = 4L;
    List<BookingTO> bookingExceeded=this.bookingDAO.findBookingsExceeded( idWeek.intValue(), idTheater );
    Assert.assertNotNull( bookingExceeded );
    for(BookingTO bookingTO:bookingExceeded)
    {
      Assert.assertNotNull( bookingTO );
    }
    
  }
  
  /**
   * Description: Caso que prueba el flijo básico de consultar todas la peliculas que sean estrenos.
   * Result: Una lista con ocho registros.
   */
  @Test
  public void findPremiereBookingTest()
  {
    List<String> bookingPremiereList= this.bookingDAO.findPremiereBooking();
    Assert.assertNotNull( bookingPremiereList );
    Assert.assertEquals( 8, bookingPremiereList.size() );
  }
  /**
   * {@link Description} Caso de prueba para el flujo basico de consultar bookings por IdEventMovie
   * y en estado booked 
   * 
   */
  @Test 
  public void countBookedByIdEventMovie_Test()
  {
    int bookings=this.bookingDAO.countBookedByIdEventMovie( 5L );
    Assert.assertEquals( 6, bookings );
    
  }
  /**
   *{@link Description} Caso de prueba para el flujo basico de consultar si existen bookings 
   *por cine y semana  
   */
  @Test
  public void hasZeroBookings_Test()
  {
    boolean fgExistBooking=false;
    TheaterTO theaterTO=new TheaterTO();
    theaterTO.setId( 1L );
    fgExistBooking=this.bookingDAO.hasZeroBookings( 4L, theaterTO );
    Assert.assertTrue( !fgExistBooking );
  }
  /**
   * {@link Description} Caso de prueba para el metodo get
   * espera una excepción
   * 
   */
  @Test(expected=DigitalBookingException.class)
  public void get_Test_Exception()
  {
    BookingTO bookingTO= new BookingTO();
    bookingTO=this.bookingDAO.get( 90909090L, 3,Language.ENGLISH );
    Assert.assertNull( bookingTO );
  }
  /**
   * {@link Description} Caso de prueba para el metodo delete
   * espera una excepción
   * 
   */
  @Test(expected=DigitalBookingException.class)
  public void delete_Test_Exception()
  {
    BookingTO bookingTO= new BookingTO();
    bookingTO.setId( 90009L );
    this.bookingDAO.delete( bookingTO );
  }
}
