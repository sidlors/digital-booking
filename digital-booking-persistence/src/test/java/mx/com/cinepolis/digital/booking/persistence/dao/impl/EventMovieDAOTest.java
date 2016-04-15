package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;



import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.model.EventMovieDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventMovieDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EventMovieDAOTest extends AbstractDBEJBTestUnit
{

  private EventMovieDAO eventMovieDAO;
  private CategoryDAO categoryDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    eventMovieDAO = new EventMovieDAOImpl();
    categoryDAO = new CategoryDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( eventMovieDAO );
    connect( categoryDAO );
  }
  
  /**
   * Description: Caso de prueba exitoso para la consulta de EventMovieDO 
   * filtrada por id de distribuidor.
   * Result: Lista con un registro encontrado.
   */
  @Test
  public void testFindByIdDistributor()
  {
    DistributorDO dstributor = new DistributorDO();
    dstributor.setIdDistributor( 12 );
    List<EventMovieDO> eventMovies = this.eventMovieDAO.findByIdDistributor( dstributor );
    Assert.assertNotNull( eventMovies );
    Assert.assertFalse( eventMovies.isEmpty() );
    Assert.assertEquals( 3, eventMovies.size() );
  }
  
  /**
   * Description: Caso de prueba exitoso para la consulta de EventMovieDO 
   * filtrada por id de vista y estatus activo.
   * Result: Lista con un registro encontrado.
   */
  @Test
  public void testFindByIdVistaAndActive()
  {
    EventMovieTO eventMovieTO = this.eventMovieDAO.findByIdVistaAndActive( "1001" );
    Assert.assertNotNull( eventMovieTO );
    Assert.assertTrue( eventMovieTO.getIdVista().equals( "1001" ) );
  }
}
