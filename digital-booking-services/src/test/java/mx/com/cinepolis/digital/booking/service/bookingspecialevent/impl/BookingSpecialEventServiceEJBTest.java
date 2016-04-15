package mx.com.cinepolis.digital.booking.service.bookingspecialevent.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.mail.MessagingException;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.EventQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventBookingTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.dao.util.BookingDOToBookingTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.BookingSpecialEventDOToSpecialEventTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.ScreenDOToScreenTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.TheaterDOToTheaterTOTransformer;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.BookingSpecialEventDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.EventDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.ScreenDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.TheaterDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.WeekDAOImpl;
import mx.com.cinepolis.digital.booking.service.bookingspecialevent.BookingSpecialEventServiceEJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de prueba para el servicio de programación de eventos especiales
 * 
 * @author jreyesv
 */

public class BookingSpecialEventServiceEJBTest extends AbstractDBEJBTestUnit
{
  private BookingSpecialEventServiceEJB bookingSpecialEventServiceEJB;
  private BookingDAO bookingDAO;
  private TheaterDAO theaterDAO;
  private EventDAO eventDAO;
  private WeekDAO weekDAO;
  private ScreenDAO screenDAO;
  private BookingSpecialEventDAO specialEventDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    bookingSpecialEventServiceEJB = new BookingSpecialEventServiceEJBImpl();
    bookingDAO = new BookingDAOImpl();
    theaterDAO = new TheaterDAOImpl();
    eventDAO = new EventDAOImpl();
    weekDAO = new WeekDAOImpl();
    screenDAO = new ScreenDAOImpl();
    specialEventDAO = new BookingSpecialEventDAOImpl();

    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    connect( bookingSpecialEventServiceEJB );
    connect( bookingDAO );
    connect( theaterDAO );
    connect( eventDAO );
    connect( weekDAO );
    connect( screenDAO );
    connect( specialEventDAO );
    initializeData( "dataset/business/presale.sql" );

  }

  /**
   * Description: Caso que prueba el guardado de una lista de 3 objetos de programación de eventos epseciales. Result: 3
   * objetos de programación de eventos especiales guardados.
   */
  @Test
  public void testSaveOrUpdateBookingCase1()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );
    /**
     * Número esperado de registros guardados
     */
    int numBookingsBefore = 1;

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();
    SpecialEventTO newBookingTO = new SpecialEventTO();
    newBookingTO.setTheater( (TheaterTO) new TheaterDOToTheaterTOTransformer().transform( theaterDAO.find( 1 ) ) );
    newBookingTO.setEvent( eventDAO.getEvent( 1L ) );
    newBookingTO.getEvent().setId( newBookingTO.getEvent().getIdEvent() );
    newBookingTO.setCopy( 3 );
    newBookingTO.setTimestamp( cal.getTime() );
    newBookingTO.setUserId( 1L );
    newBookingTO.setIdBookingType( 3L );
    // Screens
    newBookingTO.setScreens( new ArrayList<ScreenTO>() );
    ScreenTO screenTO1 = (ScreenTO) new ScreenDOToScreenTOTransformer().transform( screenDAO.find( 1 ) );
    ScreenTO screenTO2 = (ScreenTO) new ScreenDOToScreenTOTransformer().transform( screenDAO.find( 2 ) );
    ScreenTO screenTO3 = (ScreenTO) new ScreenDOToScreenTOTransformer().transform( screenDAO.find( 3 ) );
    newBookingTO.getScreens().add( screenTO1 );
    newBookingTO.getScreens().add( screenTO2 );
    newBookingTO.getScreens().add( screenTO3 );

    newBookingTO.setScreensSelected( new ArrayList<Object>() );
    Long selected1 = new Long( 1L );
    Long selected2 = new Long( 3L );
    newBookingTO.getScreensSelected().add( selected1 );
    newBookingTO.getScreensSelected().add( selected2 );
    // Shows selected
    newBookingTO.setShowingsSelected( new ArrayList<Object>() );
    Long selectedShow1 = new Long( 1 );
    Long selectedShow2 = new Long( 4 );
    Long selectedShow3 = new Long( 6 );
    newBookingTO.getShowingsSelected().add( selectedShow1 );
    newBookingTO.getShowingsSelected().add( selectedShow2 );
    newBookingTO.getShowingsSelected().add( selectedShow3 );
    // BooingStatus
    CatalogTO status = new CatalogTO();
    status.setId( 1L );
    newBookingTO.setStatus( status );
    // Notas
    newBookingTO.setNotes( "Mis notas!!!!!" );
    bookingTOs.add( newBookingTO );

    bookingSpecialEventServiceEJB.saveOrUpdateBookings( bookingTOs );

    int numBookingsAfter = bookingSpecialEventServiceEJB.getSavedBookings();
    Assert.assertEquals( numBookingsBefore, numBookingsAfter );
  }

  /**
   * Description: Caso que prueba el guardado de una lista de 1 objetos de programación de eventos epseciales. Result: 1
   * objetos de programación de eventos especiales guardados en sala cero.
   */
  @Test
  public void testSaveOrUpdateBookingCaseSalaCero()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );
    /**
     * Número esperado de registros guardados
     */
    int numBookingsBefore = 1;

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();
    SpecialEventTO newBookingTO = new SpecialEventTO();
    newBookingTO.setTheater( (TheaterTO) new TheaterDOToTheaterTOTransformer().transform( theaterDAO.find( 1 ) ) );
    newBookingTO.setEvent( eventDAO.getEvent( 1L ) );
    newBookingTO.getEvent().setId( newBookingTO.getEvent().getIdEvent() );
    newBookingTO.setCopy( 3 );
    newBookingTO.setTimestamp( cal.getTime() );
    newBookingTO.setUserId( 1L );
    newBookingTO.setIdBookingType( 3L );
    // Screens
    newBookingTO.setScreens( new ArrayList<ScreenTO>() );
    ScreenTO screenTO0 = new ScreenTO();
    screenTO0.setId( 0L );
    screenTO0.setNuScreen( 0 );
    ScreenTO screenTO1 = (ScreenTO) new ScreenDOToScreenTOTransformer().transform( screenDAO.find( 1 ) );
    ScreenTO screenTO2 = (ScreenTO) new ScreenDOToScreenTOTransformer().transform( screenDAO.find( 2 ) );
    ScreenTO screenTO3 = (ScreenTO) new ScreenDOToScreenTOTransformer().transform( screenDAO.find( 3 ) );
    newBookingTO.getScreens().add( screenTO0 );
    newBookingTO.getScreens().add( screenTO1 );
    newBookingTO.getScreens().add( screenTO2 );
    newBookingTO.getScreens().add( screenTO3 );

    newBookingTO.setScreensSelected( new ArrayList<Object>() );
    Long selected1 = new Long( 0L );
    newBookingTO.getScreensSelected().add( selected1 );
    // BooingStatus
    CatalogTO status = new CatalogTO();
    status.setId( 1L );
    newBookingTO.setStatus( status );
    // Notas
    newBookingTO.setNotes( "Mis notas!!!!!" );
    bookingTOs.add( newBookingTO );

    bookingSpecialEventServiceEJB.saveOrUpdateBookings( bookingTOs );

    int numBookingsAfter = bookingSpecialEventServiceEJB.getSavedBookings();
    Assert.assertEquals( numBookingsBefore, numBookingsAfter );
  }

  /**
   * Description: Caso que prueba el guardado de una lista de programación de eventos epseciales nula. Result: 0 objetos
   * de programación de eventos especiales guardados.
   */
  @Test
  public void testSaveOrUpdateBookingCase2()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );
    /**
     * Número esperado de registros guardados
     */
    int numBookingsBefore = 0;

    List<SpecialEventTO> bookingTOs = null;

    bookingSpecialEventServiceEJB.saveOrUpdateBookings( bookingTOs );

    int numBookingsAfter = bookingSpecialEventServiceEJB.getSavedBookings();
    Assert.assertEquals( numBookingsBefore, numBookingsAfter );
  }

  /**
   * Description: Caso que prueba el guardado de una lista vacia de programación de eventos epseciales. Result: 0
   * objetos de programación de eventos especiales guardados.
   */
  @Test
  public void testSaveOrUpdateBookingCase3()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );
    /**
     * Número esperado de registros guardados
     */
    int numBookingsBefore = 0;

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();

    bookingSpecialEventServiceEJB.saveOrUpdateBookings( bookingTOs );

    int numBookingsAfter = bookingSpecialEventServiceEJB.getSavedBookings();
    Assert.assertEquals( numBookingsBefore, numBookingsAfter );
  }

  /**
   * Description: Caso que prueba el guardado de una lista con elementos vacios de programación de eventos epseciales.
   * Result: Se espera una excepción.
   */
  @Test(expected = DigitalBookingException.class)
  public void testSaveOrUpdateBookingCase4()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();
    SpecialEventTO newBookingTO1 = null;
    SpecialEventTO newBookingTO2 = null;
    SpecialEventTO newBookingTO3 = null;
    SpecialEventTO newBookingTO4 = null;
    bookingTOs.add( newBookingTO1 );
    bookingTOs.add( newBookingTO2 );
    bookingTOs.add( newBookingTO3 );
    bookingTOs.add( newBookingTO4 );
    bookingSpecialEventServiceEJB.saveOrUpdateBookings( bookingTOs );
  }

  /**
   * Description: Caso que actualiza un objeto de programación de eventos epseciales.
   * Result: 1 objetos de programación de eventos especiales actualizados.
   */
  @Test(expected = DigitalBookingException.class)
  public void testSaveOrUpdateBookingCase5()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );
    /**
     * Número esperado de registros guardados
     */
    int numBookingsBefore = 1;

    //Observation
    BookingObservationTO observation = new BookingObservationTO();
    observation.setId( 1L );
    //Shows
    CatalogTO show1 = new CatalogTO();
    show1.setId( 1L );
    show1.setId( 3L );
    CatalogTO show2 = new CatalogTO();
    show2.setId( 2L );
    show2.setId( 4L );
    List<CatalogTO> showings = new ArrayList<CatalogTO>();
    showings.add( show1 );
    showings.add( show2 );
  //Shows seleceted
    List<Object> showsSelected = new ArrayList<Object>();
    showsSelected.add( 1L );
    showsSelected.add( 3L );
    showsSelected.add( 6L );

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();
    SpecialEventTO newBookingTO = (SpecialEventTO) new BookingSpecialEventDOToSpecialEventTOTransformer()
        .transform( specialEventDAO.find( 20L ) );
    newBookingTO.getStatus().setId( 1L );
    newBookingTO.setScreens( new ArrayList<ScreenTO>() );
    ScreenTO screen1 = new ScreenTO();
    screen1.setId( 1L );
    screen1.setNuScreen( 1 );
    screen1.setBookingStatus( newBookingTO.getStatus() );
    screen1.setBookingObservation(observation);
    screen1.setShowings( showings );
    newBookingTO.getScreens().add( screen1 );
    ScreenTO screen2 = new ScreenTO();
    screen2.setId( 2L );
    screen2.setNuScreen( 2 );
    screen2.setBookingStatus( newBookingTO.getStatus() );
    screen2.setBookingObservation(observation);
    screen2.setShowings( showings );
    newBookingTO.getScreens().add( screen2 );
    newBookingTO.setShowingsSelected( showsSelected );
    bookingTOs.add( newBookingTO );

    bookingSpecialEventServiceEJB.saveOrUpdateBookings( bookingTOs );

    int numBookingsAfter = bookingSpecialEventServiceEJB.getSavedBookings();
    Assert.assertEquals( numBookingsBefore, numBookingsAfter );
  }

  /**
   * Description: Caso que actualiza un objeto de programación de eventos epseciales.
   * Result: 1 objetos de programación de eventos especiales actualizados.
   */
  @Test
  public void testSaveOrUpdateBooking_WithShowsSelected()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );
    /**
     * Número esperado de registros guardados
     */
    int numBookingsBefore = 1;

    //Observation
    BookingObservationTO observation = new BookingObservationTO();
    observation.setId( 1L );
    //Shows
    CatalogTO show1 = new CatalogTO();
    show1.setId( 1L );
    show1.setId( 3L );
    CatalogTO show2 = new CatalogTO();
    show2.setId( 2L );
    show2.setId( 4L );
    List<CatalogTO> showings = new ArrayList<CatalogTO>();
    showings.add( show1 );
    showings.add( show2 );
    //Shows seleceted
    List<Object> showsSelected = new ArrayList<Object>();
    showsSelected.add( 1L );
    showsSelected.add( 3L );
    showsSelected.add( 6L );

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();
    SpecialEventTO newBookingTO = (SpecialEventTO) new BookingSpecialEventDOToSpecialEventTOTransformer()
        .transform( specialEventDAO.find( 4L ) );
    newBookingTO.getStatus().setId( 1L );
    newBookingTO.setScreens( new ArrayList<ScreenTO>() );
    ScreenTO screen1 = new ScreenTO();
    screen1.setId( 4L );
    screen1.setNuScreen( 4 );
    screen1.setBookingStatus( newBookingTO.getStatus() );
    screen1.setBookingObservation(observation);
    screen1.setShowings( showings );
    newBookingTO.getScreens().add( screen1 );
    ScreenTO screen2 = new ScreenTO();
    screen2.setId( 5L );
    screen2.setNuScreen( 5 );
    screen2.setBookingStatus( newBookingTO.getStatus() );
    screen2.setBookingObservation(observation);
    screen2.setShowings( showings );
    newBookingTO.getScreens().add( screen2 );
    newBookingTO.setShowingsSelected( showsSelected );
    bookingTOs.add( newBookingTO );

    bookingSpecialEventServiceEJB.saveOrUpdateBookings( bookingTOs );

    int numBookingsAfter = bookingSpecialEventServiceEJB.getSavedBookings();
    Assert.assertEquals( numBookingsBefore, numBookingsAfter );
  }

  /**
   * Description: Caso que actualiza un objeto de programación de eventos epseciales.
   * Result: 1 objetos de programación de eventos especiales actualizados.
   */
  @Test
  public void testSaveOrUpdateBooking_WithScreensForExchange()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );
    /**
     * Número esperado de registros guardados
     */
    int numBookingsBefore = 1;

    //Observation
    BookingObservationTO observation = new BookingObservationTO();
    observation.setId( 1L );
    //Shows
    CatalogTO show1 = new CatalogTO();
    show1.setId( 1L );
    show1.setId( 3L );
    CatalogTO show2 = new CatalogTO();
    show2.setId( 2L );
    show2.setId( 4L );
    List<CatalogTO> showings = new ArrayList<CatalogTO>();
    showings.add( show1 );
    showings.add( show2 );
    //Shows seleceted
    List<Object> showsSelected = new ArrayList<Object>();
    showsSelected.add( 1L );
    showsSelected.add( 3L );
    showsSelected.add( 6L );

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();
    SpecialEventTO newBookingTO = (SpecialEventTO) new BookingSpecialEventDOToSpecialEventTOTransformer()
        .transform( specialEventDAO.find( 4L ) );
    newBookingTO.getStatus().setId( 1L );
    newBookingTO.setScreens( new ArrayList<ScreenTO>() );
    ScreenTO screen1 = new ScreenTO();
    screen1.setId( 1L );
    screen1.setNuScreen( 1 );
    screen1.setOriginalNuScreen( 2 );
    screen1.setBookingStatus( newBookingTO.getStatus() );
    screen1.setBookingObservation(observation);
    screen1.setShowings( showings );
    newBookingTO.getScreens().add( screen1 );
    ScreenTO screen2 = new ScreenTO();
    screen2.setId( 2L );
    screen2.setNuScreen( 2 );
    screen2.setOriginalNuScreen( 1 );
    screen2.setBookingStatus( newBookingTO.getStatus() );
    screen2.setBookingObservation(observation);
    screen2.setShowings( showings );
    newBookingTO.getScreens().add( screen2 );
    newBookingTO.setShowingsSelected( showsSelected );
    newBookingTO.setNotes( "Notas nuevas!!!!" );
    bookingTOs.add( newBookingTO );

    bookingSpecialEventServiceEJB.saveOrUpdateBookings( bookingTOs );

    int numBookingsAfter = bookingSpecialEventServiceEJB.getSavedBookings();
    Assert.assertEquals( numBookingsBefore, numBookingsAfter );
  }

  /**
   * Description: Caso que actualiza un objeto de programación de eventos epseciales.
   * Result: 1 objetos de programación de eventos especiales actualizados.
   */
  @Test
  public void testSaveOrUpdateBooking_SpecialEventDONew()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );
    /**
     * Número esperado de registros guardados
     */
    int numBookingsBefore = 1;

    //Observation
    BookingObservationTO observation = new BookingObservationTO();
    observation.setId( 1L );
    //Shows
    CatalogTO show1 = new CatalogTO();
    show1.setId( 1L );
    show1.setId( 3L );
    CatalogTO show2 = new CatalogTO();
    show2.setId( 2L );
    show2.setId( 4L );
    List<CatalogTO> showings = new ArrayList<CatalogTO>();
    showings.add( show1 );
    showings.add( show2 );
    //Shows seleceted
    List<Object> showsSelected = new ArrayList<Object>();
    showsSelected.add( 1L );
    showsSelected.add( 3L );
    showsSelected.add( 6L );

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();
    SpecialEventTO newBookingTO = (SpecialEventTO) new BookingSpecialEventDOToSpecialEventTOTransformer()
        .transform( specialEventDAO.find( 4L ) );
    newBookingTO.getStatus().setId( 1L );
    newBookingTO.setScreens( new ArrayList<ScreenTO>() );
    PresaleTO presaleTO =new PresaleTO();
    presaleTO.setDtFinalDayPresale( new Date() );
    presaleTO.setDtReleaseDay( new Date() );
    presaleTO.setDtStartDayPresale( new Date() );
    presaleTO.setFgActive( true );
    presaleTO.setIdBookingSpecialEventScreen( 4L );
    presaleTO.setTimestamp( new Date() );
    presaleTO.setUserId( 1L );
    
    ScreenTO screen1 = new ScreenTO();
    screen1.setId( 1L );
    screen1.setNuScreen( 1 );
    screen1.setOriginalNuScreen( 2 );
    screen1.setBookingStatus( newBookingTO.getStatus() );
    screen1.setBookingObservation(observation);
    screen1.setShowings( showings );
    screen1.setPresaleTO( presaleTO );
    newBookingTO.getScreens().add( screen1 );
    ScreenTO screen2 = new ScreenTO();
    screen2.setId( 2L );
    screen2.setNuScreen( 2 );
    screen2.setOriginalNuScreen( 1 );
    screen2.setBookingStatus( newBookingTO.getStatus() );
    screen2.setBookingObservation(observation);
    screen2.setShowings( showings );
    screen2.setPresaleTO( presaleTO );
    newBookingTO.getScreens().add( screen2 );
    newBookingTO.setShowingsSelected( showsSelected );
    newBookingTO.setNotes( "Notas nuevas!!!!" );
    newBookingTO.setIdBooking( 5L );
    newBookingTO.setDisabled( Boolean.TRUE );
    bookingTOs.add( newBookingTO );

    bookingSpecialEventServiceEJB.saveOrUpdateBookings( bookingTOs );

    int numBookingsAfter = bookingSpecialEventServiceEJB.getSavedBookings();
    Assert.assertEquals( numBookingsBefore, numBookingsAfter );
  }

  /**
   * Description: Caso que actualiza una lista de 1 objeto de programación de eventos epseciales con idBookingType
   * diferente de 3. Result: Se espera una excepción.
   */
  @Test(expected = DigitalBookingException.class)
  public void testSaveOrUpdateBookingCase6()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();
    SpecialEventTO newBookingTO = new SpecialEventTO();
    newBookingTO.setId( 1L );
    newBookingTO.setTheater( (TheaterTO) new TheaterDOToTheaterTOTransformer().transform( theaterDAO.find( 1 ) ) );
    newBookingTO.setEvent( eventDAO.getEvent( 1L ) );
    newBookingTO.setCopy( 3 );
    newBookingTO.setTimestamp( cal.getTime() );
    newBookingTO.setUserId( 1L );
    newBookingTO.setIdBookingType( 1L );
    bookingTOs.add( newBookingTO );

    bookingSpecialEventServiceEJB.saveOrUpdateBookings( bookingTOs );
  }

  /**
   * Description: Caso que actualiza una lista de 1 objeto de programación de eventos epseciales con evento nulo.
   * Result: Se espera una excepción.
   */
  @Test(expected = DigitalBookingException.class)
  public void testSaveOrUpdateBookingCase7()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();
    SpecialEventTO newBookingTO = new SpecialEventTO();
    newBookingTO.setId( 1L );
    newBookingTO.setTheater( (TheaterTO) new TheaterDOToTheaterTOTransformer().transform( theaterDAO.find( 1 ) ) );
    newBookingTO.setEvent( null );
    newBookingTO.setCopy( 3 );
    newBookingTO.setTimestamp( cal.getTime() );
    newBookingTO.setUserId( 1L );
    newBookingTO.setIdBookingType( 3L );
    bookingTOs.add( newBookingTO );

    bookingSpecialEventServiceEJB.saveOrUpdateBookings( bookingTOs );
  }

  /**
   * Description: Caso que actualiza una lista de 1 objeto de programación de eventos epseciales con cine nulo. Result:
   * Se espera una excepción.
   */
  @Test(expected = DigitalBookingException.class)
  public void testSaveOrUpdateBookingCase8()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();
    SpecialEventTO newBookingTO = new SpecialEventTO();
    newBookingTO.setId( 1L );
    newBookingTO.setTheater( null );
    newBookingTO.setEvent( eventDAO.getEvent( 1L ) );
    newBookingTO.setCopy( 3 );
    newBookingTO.setTimestamp( cal.getTime() );
    newBookingTO.setUserId( 1L );
    newBookingTO.setIdBookingType( 3L );
    bookingTOs.add( newBookingTO );

    bookingSpecialEventServiceEJB.saveOrUpdateBookings( bookingTOs );
  }

  /**
   * Description: Caso que prueba la cancelación de una lista de 1 objetos de programación de eventos epseciales.
   * Result: 1 objetos de programación de eventos especiales cancelados.
   */
  @Test
  public void testCancelBookingCase1()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );
    /**
     * Número esperado de registros cancelados
     */
    int numBookingsBefore = 1;

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();
    SpecialEventTO newBookingTO = (SpecialEventTO) new BookingSpecialEventDOToSpecialEventTOTransformer()
        .transform( specialEventDAO.find( 4L ) );
    bookingTOs.add( newBookingTO );

    bookingSpecialEventServiceEJB.cancelBookings( bookingTOs );

    int numBookingsAfter = bookingSpecialEventServiceEJB.getSavedBookings();
    Assert.assertEquals( numBookingsBefore, numBookingsAfter );
  }

  /**
   * Description: Caso que prueba la cancelación de una lista de 2 objetos de programación de eventos epseciales, uno
   * valido y el otro nulo. Result: Se espera una excepción.
   */
  @Test(expected = DigitalBookingException.class)
  public void testCancelBookingCase2()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();
    SpecialEventTO newBookingTO = null;
    bookingTOs.add( newBookingTO );

    SpecialEventTO newBookingTO2 = new SpecialEventTO();
    newBookingTO2.setTheater( (TheaterTO) new TheaterDOToTheaterTOTransformer().transform( theaterDAO.find( 1 ) ) );
    newBookingTO2.setEvent( eventDAO.getEvent( 1L ) );
    newBookingTO2.setCopy( 3 );
    newBookingTO2.setTimestamp( cal.getTime() );
    newBookingTO2.setUserId( 1L );
    newBookingTO2.setCopyScreenZero( 0 );
    newBookingTO2.setCopy( 3 );
    newBookingTO2.setIdBookingType( 3L );
    newBookingTO2.setStatus( new CatalogTO( 1L ) );
    bookingTOs.add( newBookingTO2 );

    bookingSpecialEventServiceEJB.cancelBookings( bookingTOs );
  }

  /**
   * Description: Caso que prueba la cancelación de una lista de objetos de programación de eventos epseciales vacia.
   * Result: 0 objetos de programación de eventos especiales cancelados.
   */
  @Test
  public void testCancelBookingCase3()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 2, 12, 0, 0 );
    /**
     * Número esperado de registros cancelados
     */
    int numBookingsBefore = 0;

    List<SpecialEventTO> bookingTOs = new ArrayList<SpecialEventTO>();

    bookingSpecialEventServiceEJB.cancelBookings( bookingTOs );
    int numBookingsAfter = bookingSpecialEventServiceEJB.getSavedBookings();
    Assert.assertEquals( numBookingsBefore, numBookingsAfter );
  }

  /**
   * Description: Caso que prueba la cancelación de una lista de salas canceladas de la programación de un evento especial.
   * Result: Cancelación de las salas de 1 objeto de programación de eventos especiales.
   */
  @Test
  public void testCancelScreenBookingTO()
  {
    int bookingsToBeAfected = 1;
    BookingTO bookingTO = (BookingTO) new BookingDOToBookingTOTransformer().transform( bookingDAO.find( 15L ) );
    bookingTO.setScreens( new ArrayList<ScreenTO>() );
    CatalogTO status = new CatalogTO();
    status.setId( (long) BookingStatus.CANCELED.getId() );
    //Se establece el listado de screens para el bookingTO
    if(CollectionUtils.isNotEmpty( bookingTO.getSpecialEvents() ))
    {
      for(SpecialEventTO specialEventTO : bookingTO.getSpecialEvents())
      {
        if(CollectionUtils.isNotEmpty( specialEventTO.getSpecialEventScreens() ))
        {
          for(SpecialEventScreenTO specialEventScreenTO : specialEventTO.getSpecialEventScreens())
          {
            ScreenTO screenTO = new ScreenTO();
            screenTO.setId( specialEventScreenTO.getId() );
            screenTO.setBookingStatus( status );
            bookingTO.getScreens().add( screenTO );
          }
        }
      }
    }
    bookingTO.setUserId( 1L );
    bookingTO.setUsername( "User1" );
    bookingTO.setTimestamp( new Date() );
    this.bookingSpecialEventServiceEJB.cancelScreenBookingTO( bookingTO );
    int bookingsAfected = bookingSpecialEventServiceEJB.getSavedBookings();
    Assert.assertEquals( bookingsToBeAfected, bookingsAfected );
  }

  /**
   * Description: Caso que prueba la búsqueda de programación de eventos especiales. Result: Lista de objetos de
   * programación de eventos especiales.
   */
  @Test
  public void testFindBookingSpecialEvent()
  {
    Long idBookingType = 3L;
    Long specialEventIdSelected = 5L;
    Long regionId = 1L;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_EVENT_ID );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_REGION_ID );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_TYPE_ID );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_TYPE_ID, idBookingType );

    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, specialEventIdSelected );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, regionId );

    PagingResponseTO<SpecialEventTO> response = null;
    List<SpecialEventTO> result = null;
    SpecialEventBookingTO bookingSpecialEvent = this.bookingSpecialEventServiceEJB
        .findBookingSpecialEvent( pagingRequestTO );
    response = bookingSpecialEvent.getReponse();
    result = response.getElements();
    Assert.assertNotNull( response );
    Assert.assertNotNull( result );
    System.out.println( "Result size: " + result.size() );
    for( SpecialEventTO to : result )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }

  /**
   * Description: Caso que prueba la consulta paginada de eventos programados como preventas, tomando como parámetros de
   * búsqueda el id de semana, el id de la región y el nombre del evento. Result: Lista de eventos programados como
   * preventas.
   */
  @Test
  public void testGetEventsBookedForReport()
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
    PagingResponseTO<EventTO> response = this.bookingSpecialEventServiceEJB.getEventsBookedForReport( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( EventTO to : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
    Assert.assertTrue( response.getElements().size() <= pageSize );
  }

  /**
   * Description: Caso que prueba el envío de correo de preventas programadas a los cines relacionados.
   * 
   * @throws MessagingException
   * @throws IOException
   */
  @Test
  public void testSendPresalesBookedByRegionEmail()
  {
    Long userId = 2L;
    Long regionIdSelected = 1L;
    Long weekIdSelected = 1L;
    String template = "Hola mi estimado, este es un e-mail de prueba ;) !!!";
    String subject = "Email test";
    RegionEmailTO regionEmailTO = new RegionEmailTO();
    regionEmailTO.setIdRegion( regionIdSelected );
    regionEmailTO.setIdWeek( weekIdSelected );
    regionEmailTO.setMessage( template );
    regionEmailTO.setSubject( subject );
    regionEmailTO.setUserId( userId );
    List<EventTO> eventSelected = new ArrayList<EventTO>();
    this.bookingSpecialEventServiceEJB.sendPresalesBookedByRegionEmail( regionEmailTO, eventSelected );
  }

  /**
   * Description: Caso que prueba el envío de correo de preventas programadas a los cines seleccionados.
   * 
   * @throws MessagingException
   * @throws IOException
   */
  @Test
  public void testSendPresalesBookedByTheaterEmail()
  {
    Long weekIdSelected = 4L;
    String subject = "Hola a todos!!!!";
    List<TheaterTO> theatersSelected = new ArrayList<TheaterTO>();
    TheaterTO theaterTO1 = (TheaterTO) new TheaterDOToTheaterTOTransformer().transform( theaterDAO.find( 1 ) );
    theatersSelected.add( theaterTO1 );
    List<EventTO> eventSelected = new ArrayList<EventTO>();
    TheaterEmailTO theaterEmailTO = new TheaterEmailTO();
    theaterEmailTO.setIdWeek( weekIdSelected );
    theaterEmailTO.setTheatersTO( theatersSelected );
    theaterEmailTO.setSubject( subject );
    this.bookingSpecialEventServiceEJB.sendPresalesBookedByTheaterEmail( theaterEmailTO, eventSelected );
  }
}
