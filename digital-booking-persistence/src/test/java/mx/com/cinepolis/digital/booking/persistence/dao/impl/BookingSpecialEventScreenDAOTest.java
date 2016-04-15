package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;












import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO;
import mx.com.cinepolis.digital.booking.model.BookingStatusDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.ObservationDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;
/**
 * @author jcarbajal
 *
 */

public class BookingSpecialEventScreenDAOTest extends AbstractDBEJBTestUnit
{

  private BookingSpecialEventScreenDAO bookingSpecialEventScreenDAO;
  private UserDAO userDAO;
  BookingSpecialEventScreenDO bookingSpecialEventScreenDO;
  
  @Before
  public void setUp()
  {
    bookingSpecialEventScreenDAO = new BookingSpecialEventScreenDAOImpl();
    userDAO = new UserDAOImpl();
    super.setUp();
    connect(bookingSpecialEventScreenDAO);
    connect(userDAO);
    
    BookingDO idBooking=new BookingDO();
    idBooking.setIdBooking( 10L );
    idBooking.setDtLastModification(new Date());
    idBooking.setFgBooked( true );
    idBooking.setFgActive( true );
    idBooking.setIdEvent( new EventDO() );
    idBooking.setIdLastUserModifier( 5 );
    idBooking.setIdTheater( new TheaterDO() );
    
    BookingSpecialEventDO idBookingSpecialEvent=new BookingSpecialEventDO();
    idBookingSpecialEvent.setIdBookingSpecialEvent( 4L );
    idBookingSpecialEvent.setDtEndSpecialEvent( new Date() );
    idBookingSpecialEvent.setDtLastModification( new Date() );
    idBookingSpecialEvent.setDtStartSpecialEvent( new Date() );
    idBookingSpecialEvent.setFgActive( true );
    idBookingSpecialEvent.setIdBooking( idBooking );
    idBookingSpecialEvent.setIdLastUserModifier( 1 );
    idBookingSpecialEvent.setQtCopy( 4 );
    
    bookingSpecialEventScreenDO=new BookingSpecialEventScreenDO();
    
    bookingSpecialEventScreenDO.setIdBookingSpecialEvent( idBookingSpecialEvent );
    bookingSpecialEventScreenDO.setIdBookingSpecialEventScreen( 100L);
    ScreenDO screen=new ScreenDO();
    screen.setIdScreen( 1 );
    bookingSpecialEventScreenDO.setIdScreen( screen );
    BookingStatusDO idBookingStatus=new BookingStatusDO();
    idBookingStatus.setIdBookingStatus( 1 );
    bookingSpecialEventScreenDO.setIdBookingStatus( idBookingStatus );
    ObservationDO idObservation=new ObservationDO();
    idObservation.setIdObservation( 100L );
    idObservation.setFgActive( true );
    idObservation.setDsObservation( "no lo se " );
    idObservation.setDtLastModification( new Date() );
    idObservation.setIdLastUserModifier( 1 );
    UserDO idUser=this.userDAO.find( 1 );
    
    idObservation.setIdUser( idUser );
    bookingSpecialEventScreenDO.setIdObservation( idObservation );
    
  }
  /**
   *Funcion para realizar pruebas en la funcion count
   */
  @Test
  public void testCount()
  {
    Integer size=this.bookingSpecialEventScreenDAO.count();
    Assert.assertNotNull( this.bookingSpecialEventScreenDAO.count() );
    Assert.assertEquals( 23, size.intValue() );
  }
  /**
   * funcion para realizar prueba en la funcion de guardar
   *
   */
  @Test
  public void testSave()
  {
    int size=this.bookingSpecialEventScreenDAO.count();
    this.bookingSpecialEventScreenDAO.create( bookingSpecialEventScreenDO );
    int sized=this.bookingSpecialEventScreenDAO.count();
    
    Assert.assertEquals( size+1, sized );
  }
  /**
   * funcion para realizar prueba de buscar todo
   */
  @Test
  public void testFindAll()
  {
    Assert.assertNotNull( this.bookingSpecialEventScreenDAO.findAll() );
    List<BookingSpecialEventScreenDO> bookingSpecialEventList =this.bookingSpecialEventScreenDAO.findAll();
    Assert.assertFalse( ! (bookingSpecialEventList.size() >=0) );
    System.out.println("##### "+ bookingSpecialEventList.size());
  }
  
  /**
   * funcion para realizar prueba en la funcion de actualizar un registro,
   * engloba la busqueda por id
   *
   */
  @Test
  public void testUpdate()
  {
    BookingSpecialEventScreenDO bookingSpecialEvent=this.bookingSpecialEventScreenDAO.find( 1L );
    Assert.assertFalse( bookingSpecialEvent == null );
    int capacity=bookingSpecialEvent.getIdScreen().getNuCapacity();
    ScreenDO idScreen=new ScreenDO();
    idScreen.setNuCapacity( 100 );
    bookingSpecialEvent.setIdScreen( idScreen );
    this.bookingSpecialEventScreenDAO.edit( bookingSpecialEvent );
    int capacity2=this.bookingSpecialEventScreenDAO.find( 1L ).getIdScreen().getNuCapacity();
    Assert.assertEquals( capacity,  capacity2);
  }
  
}
