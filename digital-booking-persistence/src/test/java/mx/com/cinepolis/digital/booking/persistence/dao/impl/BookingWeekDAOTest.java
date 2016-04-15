package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BookingWeekDAOTest extends AbstractDBEJBTestUnit
{

  private BookingWeekDAO bookingWeekDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    bookingWeekDAO = new BookingWeekDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( bookingWeekDAO );
  }
  
  /**
   * 
   */
  @Test
  public void testCountBookingWeek()
  {
    Integer result = 12;
    WeekDO idWeek = new WeekDO();
    idWeek.setIdWeek( 2 );
    Integer theaters = this.bookingWeekDAO.countBookingWeek( idWeek );
    Assert.assertEquals( result, theaters );
  }
}
