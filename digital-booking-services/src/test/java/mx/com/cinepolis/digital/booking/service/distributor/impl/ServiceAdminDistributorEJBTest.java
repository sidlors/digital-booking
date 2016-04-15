/**
 * 
 */
package mx.com.cinepolis.digital.booking.service.distributor.impl;

import java.util.Calendar;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.dao.util.DistributorDOToDistributorTOTransformer;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.DistributorDAOImpl;
import mx.com.cinepolis.digital.booking.service.distributor.ServiceAdminDistributorEJB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Prueba unitaria  para el ServiceAdminDistributorEJB
 * @author agustin.ramirez
 *
 */
public class ServiceAdminDistributorEJBTest extends AbstractDBEJBTestUnit{
	
	/**
	 * Servicio EJB
	 */
	private ServiceAdminDistributorEJB serviceAdminDistributorEJB;
	
	/**
	 * DAO
	 */
	private DistributorDAO distributorDAO;
	
	/**
	 * Transformer
	 */
	  private DistributorDOToDistributorTOTransformer transformer; 
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp()
	 */
	  @Before
	  public void setUp()
	  {
		  transformer = new DistributorDOToDistributorTOTransformer();
	    // instanciar el servicio
		  serviceAdminDistributorEJB = new ServiceAdminDistributorEJBImpl();
		  distributorDAO = new DistributorDAOImpl();
	    // Llamar la prueba padre para obtener el EntityManager
	    super.setUp();
	    // Llamar los datos de negocio
	    // this.initializeData( "dataset/business/pollServiceTest.sql" );
	    // Conectar el EntityManager al servicio y sus daos
	    connect( serviceAdminDistributorEJB );
	    connect(distributorDAO);

	  }
	  
	  /**
	   * Prueba unitaria metodo guardar distribuidor
	   */
	  @Test
	  public void testSaveDistributor(){
		  DistributorTO  newDistributor = new DistributorTO();
		  newDistributor.setFgActive(Boolean.TRUE);
		  newDistributor.setName("Paramount");
		  newDistributor.setTimestamp(Calendar.getInstance().getTime());
		  newDistributor.setUserId(1L);
		  newDistributor.setUsername("admin");
		  newDistributor.setIdVista( "" );
		  serviceAdminDistributorEJB.saveDistributor(newDistributor);
		  Assert.assertNotNull(newDistributor.getId());
	  }
	  /**
     * Prueba unitaria metodo guardar distribuidor espera una excepcion 
     */
	  @Test(expected = DigitalBookingException.class)
    public void testSaveDistributor_withName(){
      DistributorTO  newDistributor = new DistributorTO();
      newDistributor.setFgActive(Boolean.TRUE);
      newDistributor.setName( "Distributor 1000");
      newDistributor.setShortName( "Dist. 1");
      newDistributor.setTimestamp(Calendar.getInstance().getTime());
      newDistributor.setUserId(1L);
      newDistributor.setUsername("admin");
      newDistributor.setIdVista( "" );
      serviceAdminDistributorEJB.saveDistributor(newDistributor);
      Assert.assertNotNull(newDistributor.getId());
    }
	  /**
	   * Prueba unitaria para cuando el catalogo recibido es nulo 
	   */
	  @Test
	  public void testSaveDistributor_CatalogTOIsNull(){
		  try {
			  
			  serviceAdminDistributorEJB.saveDistributor(null);
			Assert.fail("Debio Lanzar Excepcion");
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	  
	  /**
	   * Prueba unitaria para cuando el nombre del catalogo recibido es nulo
	   */
	  @Test
	  public void testSaveDistributor_CatalogTONameIsNull(){
		  try {
		    DistributorTO  newDistributor = new DistributorTO();
			  newDistributor.setFgActive(Boolean.TRUE);
			  newDistributor.setName(null);
			  newDistributor.setTimestamp(Calendar.getInstance().getTime());
			  newDistributor.setUserId(1L);
			  newDistributor.setUsername("admin");
			  serviceAdminDistributorEJB.saveDistributor(newDistributor);
			Assert.fail("Debio Lanzar Excepcion");
		} catch (Exception e) {
			e.printStackTrace();
		}
	  }
	  
	  /**
	   * Caso de prueba para el borrado logico de un distribuidor
	   */
	  @Test
	  public void testDeleteDistributor(){
	
		  DistributorDO distributor = distributorDAO.find(11);
		  DistributorTO catalog = (DistributorTO) transformer.transform(distributor);
		  serviceAdminDistributorEJB.deleteDistributor(catalog);
		  DistributorDO distributorDeleted = null; 
		  distributorDeleted = distributorDAO.find(11);
		  Assert.assertFalse(distributorDeleted.isFgActive());
	  }
	  /**
	   * method for test the function that delete the Distributor and exception 
	   * is expected
	   */
	  @Test(expected = DigitalBookingException.class)
	  public void testDeleteDistributor_Exception()
	  {
	    DistributorDO distributor = distributorDAO.find(12);
      DistributorTO ditributorTO = (DistributorTO) transformer.transform(distributor);
      serviceAdminDistributorEJB.deleteDistributor(ditributorTO);
      
	  }
	  /**
	   * Caso de prueba para la actualizacion de datos del distribuidor 
	   */
	  @Test
	  public void testUpdateDistributor(){
		
		  DistributorDO distributor = distributorDAO.find(1);
		  DistributorTO catalog = (DistributorTO) transformer.transform(distributor);
		  catalog.setName("UNIVERSAL");
		  serviceAdminDistributorEJB.updateDistributor(catalog);
		  DistributorDO distributorUpdated = null; 
		  distributorUpdated = distributorDAO.find(1);
		  Assert.assertTrue(distributorUpdated.getDsName().equals("UNIVERSAL"));
	  }
	  /**
     * Caso de prueba para la actualizacion de datos del distribuidor Distribuidor 4
     * y se esperaba una excepcion
     */
	  @Test(expected = DigitalBookingException.class)
    public void testUpdateDistributor_Exception(){
    
      DistributorDO distributor = distributorDAO.find(1);
      DistributorTO catalog = (DistributorTO) transformer.transform(distributor);
      catalog.setName("Distribuidor 4");
      serviceAdminDistributorEJB.updateDistributor(catalog);
    }
	  /**
	   * Caso de  prueba para la obtencion de todos los distribuidores
	   */
	  @Test
	  public void testGetAll(){
		  List<DistributorTO> allDistributors = null;
		  allDistributors = serviceAdminDistributorEJB.getAll();
		  Assert.assertTrue(allDistributors.size() > 0);
	  }
	  
	  /**
	   * Caso de  prueba  para la obtencion de los distribuidores por pagina
	   */
	  @Test
	  public void testGetCatalogDistributorSummary(){
		  int totalCount = 12;
		    int pageSize = 5;
		    for( int page = 0; page < 3; page++ )
		    {

		      PagingRequestTO pagingRequestTO = new PagingRequestTO();
		      pagingRequestTO.setPage( page );
		      pagingRequestTO.setPageSize( pageSize );
		      PagingResponseTO<DistributorTO> response = serviceAdminDistributorEJB.getCatalogDistributorSummary( pagingRequestTO );

		      List<DistributorTO> distributors = response.getElements();
		      Assert.assertNotNull( distributors );
		      Assert.assertEquals( totalCount, response.getTotalCount() );

		      Assert.assertTrue( pageSize >= distributors.size() );
		      for( DistributorTO distributorDO : distributors )
		      {
		        System.out.println( distributorDO.getId() + ", " + distributorDO.getName() );
		      }
		    }
	  }
	  /**
	   * method for test the method that get distributor by id
	   */
	  @Test
	  public void testGetDistributorById()
	  {
	    DistributorTO distributorTO=this.serviceAdminDistributorEJB.getDistributor( 1 );
	    Assert.assertNotNull( distributorTO );
	    
	  }
}
