package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenShowTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenShowDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingStatusDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas de BookingSpecialEventDAO
 * 
 * @author jcarbajal
 */
public class BookingSpecialEventDAOTest extends AbstractDBEJBTestUnit
{
  private BookingSpecialEventDAO bookingSpecialEventDAO;
  private BookingSpecialEventScreenDAO bookingSpecialEventScreenDAO;
  private BookingSpecialEventScreenShowDAO bookingSpecialEventScreenShowDAO;
  private BookingDAO bookingDAO;
  private BookingStatusDAO bookingStatusDAO;
  BookingSpecialEventDO bookingSpecialEventDO;
  
  private Long showIndex=new Long(30);

  @Before
  public void setUp()
  {
    EventDO event=new EventDO();
    event.setDsName( "EL hoobit" );
    event.setIdEvent( 134L );
    
    TheaterDO theater=new TheaterDO();
    theater.setIdTheater( 130 );
    
    bookingSpecialEventDO=new BookingSpecialEventDO();
    bookingSpecialEventDO.setDtEndSpecialEvent( new Date () );
    bookingSpecialEventDO.setDtLastModification( new Date() );
    bookingSpecialEventDO.setDtStartSpecialEvent( new Date() );
    bookingSpecialEventDO.setFgActive( true );
    BookingDO idBooking= new BookingDO();
    idBooking.setIdBooking( 990L );
    idBooking.setIdEvent( event );
    idBooking.setIdTheater( theater );
    bookingSpecialEventDO.setIdBooking( idBooking );
    bookingSpecialEventDO.setIdBookingSpecialEvent( 2L );
    bookingSpecialEventDO.setIdLastUserModifier( 1 );
    bookingSpecialEventDO.setQtCopy( 3 );
    
    bookingSpecialEventScreenShowDAO=new BookingSpecialEventScreenShowDAOImpl();
    bookingSpecialEventScreenDAO=new BookingSpecialEventScreenDAOImpl();
    bookingSpecialEventDAO = new BookingSpecialEventDAOImpl();
    bookingStatusDAO= new BookingStatusDAOImpl();
    bookingDAO = new BookingDAOImpl();
    super.setUp();
    connect(bookingDAO);
    connect(bookingStatusDAO);
    connect(bookingSpecialEventDAO);
    connect(bookingSpecialEventScreenDAO);
    connect(bookingSpecialEventScreenShowDAO);
  }
  /**
   * Funcion para realizar pruebas en la funcion count
   */
  @Test
  public void testCount()
  {
    Integer size=this.bookingSpecialEventDAO.count();
    Assert.assertEquals( 11, size.intValue() );
  }

  /**
   * funcion para realizar prueba en la funcion de guardar
   */
  @Test
  public void saveBookingSpecialEvent()
  {
    BookingObservationTO bookingObservationTO=new BookingObservationTO();
    bookingObservationTO.setId( 15L );
    bookingObservationTO.setIdBooking( 20L );
    bookingObservationTO.setIdBookingWeek( 20L );
    bookingObservationTO.setIdBookingWeekScreen( 20L );
    bookingObservationTO.setIdLanguage( 2L );
    bookingObservationTO.setObservation( "Nota para Test" );
    bookingObservationTO.setTimestamp( new Date() );
    bookingObservationTO.setUserId( 2L );
    bookingObservationTO.setUsername( "Jesus" );
    
    EventTO event=new EventTO();
    event.setCodeDBS( "MNJuuiu" );
    event.setCurrentMovie( true );
    event.setDsEventName( "El Hobbit" );
    event.setDuration( 220 );
    event.setFestival( true );
    event.setFgActive( true );
    event.setId( 2L );
    event.setIdLanguage( 2L );
    event.setIdVista( "8989k" );
    event.setUserId( 2L );
    event.setUsername( "Jesus" );
    event.setQtCopy( 4 );
    event.setName( "El Hobiit bla " );
    event.setPremiere( false );
    event.setTimestamp( new Date() );
    
    CatalogTO bookingStatus=new CatalogTO();
    bookingStatus.setFgActive( true );
    bookingStatus.setName( "No lo se " );
    bookingStatus.setUserId( 2L );
    bookingStatus.setUsername( "Jesus" );
    bookingStatus.setTimestamp( new Date () );
    bookingStatus.setId( 1L );
    bookingStatus.setIdLanguage( 2L );
    bookingStatus.setIdVista( "jojojo" );
    
    List<ScreenTO> screens=new ArrayList<ScreenTO>();
    ScreenTO screen=new ScreenTO();
    screen.setBookingObservation( bookingObservationTO );
    screen.setBookingStatus( bookingStatus );
    screen.setDisabled( false );
    screen.setFgActive( true );
    screen.setId( 2L );
    screen.setIdLanguage( 2L );
    screen.setIdTheater( 1 );
    screen.setName( "Cinepolis Ajusco" );
    screen.setNuCapacity( 220 );
    screen.setOriginalNuScreen( 5656 );
    screen.setTimestamp( new Date() );
    screen.setUserId( 2L );
    screen.setUsername( "Jesus" );
    
    ScreenTO screen2=new ScreenTO();
    screen2.setBookingObservation( bookingObservationTO );
    screen2.setBookingStatus( bookingStatus );
    screen2.setDisabled( false );
    screen2.setFgActive( true );
    screen2.setId( 3L );
    screen2.setIdLanguage( 2L );
    screen2.setIdTheater( 1 );
    screen2.setNuCapacity( 220 );
    screen2.setOriginalNuScreen( 5656 );
    screen2.setTimestamp( new Date() );
    screen2.setUserId( 2L );
    screen2.setUsername( "Jesus" );
    
    screens.add( screen );
    screens.add( screen2 );
    
    
    
    List<SpecialEventScreenTO> specialEventScreensList=new ArrayList<SpecialEventScreenTO>();
    SpecialEventScreenTO specialEventScreens=new SpecialEventScreenTO();
    specialEventScreens.setId( 30L );
    specialEventScreens.setIdLanguage( 2L );
    specialEventScreens.setIdObservation( 30L );
    specialEventScreens.setIdScreen( 32L );
    specialEventScreens.setIdSpecialEvent( 30L );
    specialEventScreens.setStatus( bookingStatus );
    specialEventScreens.setTimestamp( new Date() );
    specialEventScreens.setUserId( 2L );
    specialEventScreens.setUsername( "Jesus" );
    specialEventScreens.setSpecialEventScreenShows( createShows() );
    
    SpecialEventScreenTO specialEventScreens2=new SpecialEventScreenTO();
    specialEventScreens2.setId( 31L );
    specialEventScreens2.setIdLanguage( 2L );
    specialEventScreens2.setIdObservation( 30L );
    specialEventScreens2.setIdScreen( 31L );
    specialEventScreens2.setIdSpecialEvent( 30L );
    specialEventScreens2.setStatus( bookingStatus );
    specialEventScreens2.setTimestamp( new Date() );
    specialEventScreens2.setUserId( 2L );
    specialEventScreens2.setUsername( "Jesus" );
    specialEventScreens2.setSpecialEventScreenShows( createShows() );
    
    specialEventScreensList.add( specialEventScreens );
    specialEventScreensList.add( specialEventScreens2 );
    
    TheaterTO theater=new TheaterTO();
    theater.setId( 30L );
    theater.setFgActive( true );
    theater.setScreens( screens );
    theater.setName( "Cinepolis Ajusco" );
    theater.setNuTheater( 1500 );
    theater.setTimestamp( new Date() );
    theater.setUserId( 2L );
    theater.setUsername( "Jesus" );
    
    List<SpecialEventTO> specialEvents=new ArrayList<SpecialEventTO>();
    SpecialEventTO specialEvent=new SpecialEventTO();
    specialEvent.setBookingObservation( bookingObservationTO );
    specialEvent.setCopy( 3 );
    specialEvent.setCopyScreenZero( 0 );
    specialEvent.setCopyScreenZeroTerminated( 0 );
    specialEvent.setDisabled( false );
    specialEvent.setEvent( event );
    specialEvent.setEventTO( event );
    specialEvent.setFgActive( true );
    specialEvent.setStartDay( new Date() );
    specialEvent.setFinalDay( new Date() );
    specialEvent.setDate( new Date() );
    specialEvent.setId( 30L );
    specialEvent.setIdBooking( 30L );
    specialEvent.setIdBookingType( 3L );
    specialEvent.setIdLanguage( 2L );
    specialEvent.setInRemoval( false );
    specialEvent.setNotes( "Programacion para las funciones bla bla " );
    specialEvent.setObservation( bookingObservationTO );
    //specialEvent.setPresale( false );
    specialEvent.setScreens( screens );
    specialEvent.setShowDate( true );
    specialEvent.setSpecialEventScreens( specialEventScreensList );
    specialEvent.setStatus( bookingStatus );
    specialEvent.setStrDate( "De tal dia a tal dia" );
    specialEvent.setTheater( theater );
    specialEvent.setTimestamp( new Date() );
    specialEvent.setUserId( 2L );
    specialEvent.setUsername( "Jesus" );
    
    SpecialEventTO specialEvent2=new SpecialEventTO();
    specialEvent2.setBookingObservation( bookingObservationTO );
    specialEvent2.setCopy( 3 );
    specialEvent2.setCopyScreenZero( 0 );
    specialEvent2.setCopyScreenZeroTerminated( 0 );
    specialEvent2.setDisabled( false );
    specialEvent2.setEvent( event );
    specialEvent2.setEventTO( event );
    specialEvent2.setFgActive( true );
    specialEvent2.setStartDay( new Date() );
    specialEvent2.setFinalDay( new Date() );
    specialEvent2.setDate( new Date() );
    specialEvent2.setId( 31L );
    specialEvent2.setIdBooking( 30L );
    specialEvent2.setIdBookingType( 3L );
    specialEvent2.setIdLanguage( 2L );
    specialEvent2.setInRemoval( false );
    specialEvent2.setNotes( "Programacion para las funciones bla bla " );
    specialEvent2.setObservation( bookingObservationTO );
    //specialEvent2.setPresale( false );
    specialEvent2.setScreens( screens );
    specialEvent2.setShowDate( true );
    specialEvent2.setSpecialEventScreens( createScreens() );
    specialEvent2.setStatus( bookingStatus );
    specialEvent2.setStrDate( "De tal dia a tal dia" );
    specialEvent2.setTheater( theater );
    specialEvent2.setTimestamp( new Date() );
    specialEvent2.setUserId( 2L );
    specialEvent2.setUsername( "Jesus" );
    
    specialEvents.add( specialEvent );
    specialEvents.add( specialEvent2 );
    
    WeekTO week=new WeekTO();
    week.setActiveWeek( true );
    week.setFgActive( true );
    week.setFinalDayWeek( new Date() );
    week.setIdLanguage( 2L );
    week.setIdWeek( 30 );
    week.setNuWeek( 30 );
    week.setNuYear( 2014 );
    week.setSpecialWeek( false );
    week.setStartingDayWeek( new Date() );
    week.setTimestamp( new Date() );
    week.setUserId( 2L );
    week.setUsername( "Jesus" );
    week.setWeekDescription( "Semana 123" );
    
    BookingTO bookingTO=new BookingTO();
    bookingTO.setBookingObservationTO( bookingObservationTO );
    bookingTO.setCopy( 4 );
    bookingTO.setCopyScreenZero( 0 );
    bookingTO.setCopyScreenZeroTerminated( 0 );
    bookingTO.setDisabled( false );
    bookingTO.setEvent( event );
    bookingTO.setExhibitionEnd( new Date() );
    bookingTO.setExhibitionWeek( 20 );
    bookingTO.setFgActive( true );
    bookingTO.setId( 30L );
    bookingTO.setIdBookingType( 3 );
    bookingTO.setIdBookingWeek( 0L );
    bookingTO.setIdLanguage( 2L );
    bookingTO.setIsEditable( true );
    //bookingTO.setPresale( false );
    bookingTO.setScreens( screens );
    bookingTO.setSent( false );
    bookingTO.setSpecialEvents( specialEvents );
    bookingTO.setStatus( bookingStatus );
    bookingTO.setTheater( theater );
    bookingTO.setTimestamp( new Date() );
    bookingTO.setUserId( 2L );
    bookingTO.setUsername( "Jesus" );
    bookingTO.setWeek( week );
    
    this.bookingSpecialEventDAO.saveBookingSpecialEvent( bookingTO );
  }
private List<SpecialEventScreenShowTO> createShows()
{
  Long index=new Long(showIndex+1);
  List<SpecialEventScreenShowTO> specialEventScreenShowList=new ArrayList<SpecialEventScreenShowTO>();
  SpecialEventScreenShowTO specialEventScreenShow=new SpecialEventScreenShowTO();
  specialEventScreenShow.setFgActive( true );
  specialEventScreenShow.setId( index );
  specialEventScreenShow.setIdLanguage( 2L );
  specialEventScreenShow.setIdSpecialEventScreen( 30L );
  specialEventScreenShow.setNuShow( 3 );
  specialEventScreenShow.setTimestamp( new Date() );
  specialEventScreenShow.setUserId( 2L );
  specialEventScreenShow.setUsername( "Jesus" );
  Long index1=new Long(index+1);
  SpecialEventScreenShowTO specialEventScreenShow2=new SpecialEventScreenShowTO();
  specialEventScreenShow2.setFgActive( true );
  specialEventScreenShow2.setId( index1 );
  specialEventScreenShow2.setIdLanguage( 2L );
  specialEventScreenShow2.setIdSpecialEventScreen( 30L );
  specialEventScreenShow2.setNuShow( 3 );
  specialEventScreenShow2.setTimestamp( new Date() );
  specialEventScreenShow2.setUserId( 2L );
  specialEventScreenShow2.setUsername( "Jesus" );
  
  specialEventScreenShowList.add( specialEventScreenShow );
  specialEventScreenShowList.add( specialEventScreenShow2 );
  
  showIndex=index1+1;
  
  return specialEventScreenShowList;
  }
  /**
   * method for create screens 
   * @return
   */
  private List<SpecialEventScreenTO> createScreens()
  {
    List<SpecialEventScreenShowTO> specialEventScreenShowList=new ArrayList<SpecialEventScreenShowTO>();
    SpecialEventScreenShowTO specialEventScreenShow=new SpecialEventScreenShowTO();
    specialEventScreenShow.setFgActive( true );
    specialEventScreenShow.setId( 40L );
    specialEventScreenShow.setIdLanguage( 2L );
    specialEventScreenShow.setIdSpecialEventScreen( 30L );
    specialEventScreenShow.setNuShow( 3 );
    specialEventScreenShow.setTimestamp( new Date() );
    specialEventScreenShow.setUserId( 2L );
    specialEventScreenShow.setUsername( "Jesus" );
    SpecialEventScreenShowTO specialEventScreenShow2=new SpecialEventScreenShowTO();
    specialEventScreenShow2.setFgActive( true );
    specialEventScreenShow2.setId( 41L );
    specialEventScreenShow2.setIdLanguage( 2L );
    specialEventScreenShow2.setIdSpecialEventScreen( 30L );
    specialEventScreenShow2.setNuShow( 3 );
    specialEventScreenShow2.setTimestamp( new Date() );
    specialEventScreenShow2.setUserId( 2L );
    specialEventScreenShow2.setUsername( "Jesus" );
    
    specialEventScreenShowList.add( specialEventScreenShow );
    specialEventScreenShowList.add( specialEventScreenShow2 );
    
    CatalogTO bookingStatus=new CatalogTO();
    bookingStatus.setFgActive( true );
    bookingStatus.setName( "No lo se " );
    bookingStatus.setUserId( 2L );
    bookingStatus.setUsername( "Jesus" );
    bookingStatus.setTimestamp( new Date () );
    bookingStatus.setId( 1L );
    bookingStatus.setIdLanguage( 2L );
    bookingStatus.setIdVista( "jojojo" );
    
    List<SpecialEventScreenTO> specialEventScreensList=new ArrayList<SpecialEventScreenTO>();
    SpecialEventScreenTO specialEventScreens=new SpecialEventScreenTO();
    specialEventScreens.setId( 32L );
    specialEventScreens.setIdLanguage( 2L );
    specialEventScreens.setIdObservation( 30L );
    specialEventScreens.setIdScreen( 32L );
    specialEventScreens.setIdSpecialEvent( 30L );
    specialEventScreens.setStatus( bookingStatus );
    specialEventScreens.setTimestamp( new Date() );
    specialEventScreens.setUserId( 2L );
    specialEventScreens.setUsername( "Jesus" );
    specialEventScreens.setSpecialEventScreenShows( createShows() );
    
    SpecialEventScreenTO specialEventScreens2=new SpecialEventScreenTO();
    specialEventScreens2.setId( 33L );
    specialEventScreens2.setIdLanguage( 2L );
    specialEventScreens2.setIdObservation( 30L );
    specialEventScreens2.setIdScreen( 31L );
    specialEventScreens2.setIdSpecialEvent( 30L );
    specialEventScreens2.setStatus( bookingStatus );
    specialEventScreens2.setTimestamp( new Date() );
    specialEventScreens2.setUserId( 2L );
    specialEventScreens2.setUsername( "Jesus" );
    specialEventScreens2.setSpecialEventScreenShows( createShows() );
    
    specialEventScreensList.add( specialEventScreens );
    specialEventScreensList.add( specialEventScreens2 );
    return specialEventScreensList;
  }
  /**
   * funcion para realizar prueba de buscar todo
   */
  @Test
  public void testFindAll()
  {
    Assert.assertNotNull( this.bookingSpecialEventDAO.findAll() );
    List<BookingSpecialEventDO> bookingSpecialEventList =this.bookingSpecialEventDAO.findAll();
    Assert.assertFalse( ! (bookingSpecialEventList.size() >=0) );
  }

  /**
   * funcion para realizar prueba en la funcion de actualizar un registro, engloba la busqueda por id
   */
  @Test
  public void testUpdate()
  {
    BookingSpecialEventDO bookingSpecialEvent = this.bookingSpecialEventDAO.find( 1L );
    bookingSpecialEvent.setFgActive( false );
    this.bookingSpecialEventDAO.edit( bookingSpecialEvent );
    Assert.assertEquals( true, !this.bookingSpecialEventDAO.find( 1L ).isFgActive() );
  }
  
  /**
   * Test for search by event , theater and bookingType
   */
  @Test
  public void testFindByEventAndTheaterAndType()
  {
    Long idTheater = 2L;
    Long idEvent = 13L; 
    Long idBookingType = 3L;
    
    BookingDO bookingSpecialEvent = this.bookingSpecialEventDAO.findByEventAndTheaterAndType(idTheater, idEvent, idBookingType);
    Assert.assertNotNull( bookingSpecialEvent );
    Assert.assertEquals( 2L, bookingSpecialEvent.getIdTheater().getIdTheater().longValue() );
    Assert.assertEquals( 3L, bookingSpecialEvent.getIdBookingType().getIdBookingType().longValue() );
    Assert.assertEquals( 13L, bookingSpecialEvent.getIdEvent().getIdEvent().longValue() );
  }
  /**
   * Prueba para busqueda de specialEvent bookings por evento y region
   */
  @Test
  public void testFindbyEventRegion_WhitoutSpecialEvents()
  {
    Long booking_type_id=3L; 
    Long eventId=1L;
    Long regionId=15L;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 2L );
    pagingRequestTO.setUsername( "Jesus" );
    pagingRequestTO.setTimestamp( new Date() );
    //super.fillSessionData( pagingRequestTO );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_EVENT_ID );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_REGION_ID );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_TYPE_ID );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, eventId );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, regionId );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_TYPE_ID, booking_type_id );
    
    List<SpecialEventTO> specialEventList=this.bookingSpecialEventDAO.findBookingsSpecialEventByEventRegion( pagingRequestTO );
    Assert.assertNotNull( specialEventList );
    Assert.assertEquals( 0, specialEventList.size() );
   
      
  }
  
  /**
   * Prueba para busqueda de specialEvent bookings por evento y region
   */
  @Test
  public void testFindbyEventRegion()
  {
    Long booking_type_id=3L; 
    Long specialEventId=13L;
    Long regionId=2L;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 2L );
    pagingRequestTO.setUsername( "Jesus" );
    pagingRequestTO.setTimestamp( new Date() );
    //super.fillSessionData( pagingRequestTO );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_EVENT_ID );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_REGION_ID );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_TYPE_ID );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, specialEventId );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, regionId );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_TYPE_ID, booking_type_id );
    
    List<SpecialEventTO> specialEventList=this.bookingSpecialEventDAO.findBookingsSpecialEventByEventRegion( pagingRequestTO );
    Assert.assertNotNull( specialEventList );
    Assert.assertEquals( 1, specialEventList.size() );
    for(SpecialEventTO specialEvent:specialEventList)
    {
      Assert.assertEquals( 13L, specialEvent.getEvent().getId().longValue() );
    }
    System.out.println(specialEventList.size());
      
  }
 /**
  * Test for cancel SpecialEvent Scree
  */
  @Test 
  public void cancelBookingSpecialEventScreenTest()
  {
    Long idScreen=1L;
    BookingSpecialEventScreenDO screenDO = this.bookingSpecialEventScreenDAO.find( idScreen );
    Integer status=screenDO.getIdBookingStatus().getIdBookingStatus();
    this.bookingSpecialEventDAO.cancelBookingSpecialEventScreen( idScreen, 0 );
    BookingSpecialEventScreenDO screenDO2 = this.bookingSpecialEventScreenDAO.find( idScreen );
    Assert.assertNotEquals( status, screenDO2.getIdBookingStatus().getIdBookingStatus() );
    
  }
  /**
   * Test for Cancel SpeciaEvent
   */
  @Test
  public void cancelBookingSpecialEventTest()
  {
    BookingTO bookingTO=new BookingTO();
    bookingTO.setId( 13L );
    bookingTO.setUserId( 1L );
    BookingDO bookingDO = this.bookingDAO.find( bookingTO.getId() );
    boolean booked=bookingDO.getFgBooked();
    this.bookingSpecialEventDAO.cancelBookingSpecialEvent( bookingTO );
    BookingDO bookingDO2 = this.bookingDAO.find( bookingTO.getId() );
    Assert.assertNotEquals( booked, bookingDO2.getFgBooked() );
    
  }
  /**
   * Test for update SpecialEvent  
   */
  @Test
  public void updateBookingSpecialEventTest()
  {
    BookingTO bookingTO=new BookingTO();
    bookingTO.setId( 1L );
    bookingTO.setCopy( 10 );
    Integer copy=0;
    BookingSpecialEventDO specialEvent=this.bookingSpecialEventDAO.find( bookingTO.getId() );
    copy=specialEvent.getQtCopy();
    this.bookingSpecialEventDAO.updateBookingSpecialEvent( bookingTO );
    
    Assert.assertNotEquals( copy, specialEvent.getQtCopy() );
    
  }
  /**
   * Test to get ShowingSelecteds by idSpecialEvent
   */
  @Test
  public void getShowingsSelectedByIdSpecialEvent_Test()
  {
    Long idSpecialEvent=8L;
    List<Object> shows=this.bookingSpecialEventDAO.getShowingsSelectedByIdSpecialEvent( idSpecialEvent );
    Assert.assertNotNull( shows );
    Assert.assertEquals( 2, shows.size() );
  }
  /**
   * Test to get Observation  by idSpecialEvent
   */
  @Test
  public void getObservationByIdSpecialEvent_Test()
  {
    String observation="Observation 2";
    Long idSpecialEvent=8L;
    BookingObservationTO obs=this.bookingSpecialEventDAO.getObservationByIdSpecialEvent( idSpecialEvent );
    Assert.assertNotNull( obs );
    Assert.assertEquals( observation ,obs.getObservation());
  }
  
}
