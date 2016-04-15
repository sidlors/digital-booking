package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.BookingStatusDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingStatusDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BookingStatusDAOTest extends AbstractDBEJBTestUnit
{

  private BookingStatusDAO bookingStatusDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    bookingStatusDAO = new BookingStatusDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( bookingStatusDAO );

  }

  @Test
  public void testFind()
  {
    BookingStatusDO bookingStatusDO = this.bookingStatusDAO.find( 1 );
    Assert.assertNotNull( bookingStatusDO );
  }

  @Test
  public void testFindAll()
  {
    List<BookingStatusDO> list = this.bookingStatusDAO.findAll();
    Assert.assertNotNull( list );
    Assert.assertFalse( list.isEmpty() );
  }

  @Test
  public void testGet()
  {
    CatalogTO to = this.bookingStatusDAO.get( 1 );
    Assert.assertNotNull( to );
    System.out.println( to );
  }

  @Test
  public void testGetLanguage()
  {
    CatalogTO to = this.bookingStatusDAO.get( 1, Language.SPANISH );
    Assert.assertNotNull( to );
    System.out.println( to );
  }

  @Test
  public void testGetAll()
  {
    List<CatalogTO> list = this.bookingStatusDAO.getAll();
    Assert.assertNotNull( list );
    Assert.assertFalse( list.isEmpty() );
    for( CatalogTO to : list )
    {
      System.out.println( to );
    }
  }

  @Test
  public void testGetAllLanguage()
  {
    List<CatalogTO> list = this.bookingStatusDAO.getAll( Language.SPANISH );
    Assert.assertNotNull( list );
    Assert.assertFalse( list.isEmpty() );
    for( CatalogTO to : list )
    {
      System.out.println( to );
    }
  }
}
