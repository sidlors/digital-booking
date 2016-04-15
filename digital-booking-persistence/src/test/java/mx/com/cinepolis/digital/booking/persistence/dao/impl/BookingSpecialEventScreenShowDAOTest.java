package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenShowDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenShowDAO;
/**
 * @author jcarbajal
 *
 */
public class BookingSpecialEventScreenShowDAOTest extends AbstractDBEJBTestUnit
{

  private BookingSpecialEventScreenShowDAO bookingSpecialEventScreenShowDAO;
  BookingSpecialEventScreenShowDO bookingSpecialEventScreenShowDO;
  
  @Before
  public void setUp()
  {
    
    BookingSpecialEventScreenDO bookingSpecialEventScreenDO= new BookingSpecialEventScreenDO();
    bookingSpecialEventScreenDO.setIdBookingSpecialEventScreen( 1000L );
    
    bookingSpecialEventScreenShowDO = new BookingSpecialEventScreenShowDO();
    bookingSpecialEventScreenShowDO.setIdBookingSpecialEventScreen( bookingSpecialEventScreenDO );
    bookingSpecialEventScreenShowDO.setIdBookingSpecialEventScreenShow( 50L );
    bookingSpecialEventScreenShowDO.setNuShow( 5 );
    
    
    
    bookingSpecialEventScreenShowDAO = new BookingSpecialEventScreenShowDAOImpl();
    super.setUp();
    connect(bookingSpecialEventScreenShowDAO);
  }
  /**
   *Funcion para realizar pruebas en la funcion count
   */
  @Test
  public void testCount()
  {
    Integer size=this.bookingSpecialEventScreenShowDAO.count();
    Assert.assertEquals( 29, size.intValue() );
  }
  
  /**
   * funcion para realizar prueba en la funcion de guardar
   *
   */
  @Test
  public void testSave()
  {
    int size=this.bookingSpecialEventScreenShowDAO.count();
    this.bookingSpecialEventScreenShowDAO.create( bookingSpecialEventScreenShowDO );
    int sized=this.bookingSpecialEventScreenShowDAO.count();
    
    Assert.assertNotEquals( size, sized );
  }
  /**
   * funcion para realizar prueba de buscar todo
   */
  @Test
  public void testFindAll()
  {
    List<BookingSpecialEventScreenShowDO> bookingSpecialEventList =this.bookingSpecialEventScreenShowDAO.findAll();
    
    System.out.println("##### "+ bookingSpecialEventList.size());
    Assert.assertFalse( ! (bookingSpecialEventList.size() >=0) );
  }
  
  /**
   * funcion para realizar prueba en la funcion de actualizar un registro,
   * engloba la busqueda por id
   *
   */
  @Test
  public void testUpdate()
  {
    BookingSpecialEventScreenShowDO bookingSpecialEventScreenShow=this.bookingSpecialEventScreenShowDAO.find( 1L );
    Assert.assertFalse( bookingSpecialEventScreenShow == null );
    int capacity=bookingSpecialEventScreenShow.getNuShow();
    bookingSpecialEventScreenShow.setNuShow( 189 );
    this.bookingSpecialEventScreenShowDAO.edit( bookingSpecialEventScreenShow );
    int capacity2=this.bookingSpecialEventScreenShowDAO.find( 1L ).getNuShow();
    Assert.assertNotEquals( capacity,  capacity2);
  }
  
  /**
   * Metodo para borrar un registro
   */
  @Test
  public void testRemove()
  {
    BookingSpecialEventScreenShowDO bookingSpecialEventScreenShow=this.bookingSpecialEventScreenShowDAO.find( 1L );
    Assert.assertNotNull( bookingSpecialEventScreenShow );
    int size=this.bookingSpecialEventScreenShowDAO.count();
    this.bookingSpecialEventScreenShowDAO.remove( bookingSpecialEventScreenShow );
    int sizef=this.bookingSpecialEventScreenShowDAO.count();
    Assert.assertEquals( size-1, sizef );
  }
}
