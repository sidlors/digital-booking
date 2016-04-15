/**
 * 
 */
package mx.com.cinepolis.digital.booking.service.synchronize.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.CityDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CountryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.StateDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.CityDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.CountryDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.DistributorDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.StateDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.TheaterDAOImpl;
import mx.com.cinepolis.digital.booking.service.synchronize.ServiceDataSynchronizerEJB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Prueba unitaria para el ServiceAdminTheater
 * 
 * @author rgarcia
 */
public class ServiceDataSynchronizerEJBTest extends AbstractDBEJBTestUnit
{
  private static final Logger logger = LoggerFactory.getLogger( ServiceDataSynchronizerEJBTest.class );

  /**
   * Servicio EJB
   */
  private ServiceDataSynchronizerEJB serviceDataSynchronizerEJB;
  private Date timestamp = null;

  StateTO<CatalogTO, Integer> state = null;
  
  @EJB
  private DistributorDAO distributorDAO;
  @EJB
  private CountryDAO countryDAO;
  @EJB
  private CityDAO cityDAO;
  @EJB
  private StateDAO stateDAO;
  @EJB 
  private  TheaterDAO theaterDAO;
  

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp()
   */
  @Before
  public void setUp()
  {
    // instanciar el servicio
    serviceDataSynchronizerEJB = new ServiceDataSynchronizerEJBImpl();
    distributorDAO = new DistributorDAOImpl();
    countryDAO = new CountryDAOImpl();
    cityDAO = new CityDAOImpl();
    stateDAO = new StateDAOImpl();
    theaterDAO = new TheaterDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    super.initializeData("dataset/business/catalogbysynchronize.sql"  );
    // Conectar el EntityManager al servicio y sus daos
    connect( serviceDataSynchronizerEJB );
    connect( distributorDAO );
    connect( countryDAO );
    connect( cityDAO );
    connect( stateDAO );
    connect( theaterDAO );
    timestamp = Calendar.getInstance().getTime();
   
  }
  
  /**
   * Prueba unitaria de la sincronizacion de los distribuidores
   */
  @Test
  public void testSynchronizeDistributors_ok()
  {
    try
    {
      List<DistributorTO> currentDistributors = distributorDAO.getAll();
      Assert.assertNotNull( currentDistributors );
      int currentSize = currentDistributors.size() ;
      logger.debug( "Current distributors size: " + currentSize );
      serviceDataSynchronizerEJB.synchronizeDistributors();
      List<DistributorTO> newDistributors = distributorDAO.getAll();
      int newSize = newDistributors.size() ;
      logger.debug( "New distributors size: " + newSize );
      Assert.assertTrue(  currentSize != newSize );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error: ", ex );
      Assert.fail("No debio ocurrir excepcion");
    }
  }
  
  
  /**
   * Prueba unitaria de la sincronizacion de los paises
   */
  @Test
  public void testSynchronizeCountries_ok()
  {
    try
    {
      List<CatalogTO> currentCountries = countryDAO.getAll( Language.SPANISH );
      Assert.assertNotNull( currentCountries );
      int currentSize = currentCountries.size() ;
      logger.debug( "Current countries size: " + currentSize );
      serviceDataSynchronizerEJB.synchronizeCountries( Language.SPANISH );
      List<CatalogTO> newCountries = countryDAO.getAll( Language.SPANISH );
      int newSize = newCountries.size() ;
      logger.debug( "New countries size: " + newSize );
      Assert.assertTrue(  currentSize != newSize );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error: ", ex );
      Assert.fail("No debio ocurrir excepcion");
    }
  }

  
  /**
   * Prueba unitaria de la sincronizacion de las ciudades
   */
  @Test
  public void testSynchronizeCities_ok()
  {
    try
    {
      List<CatalogTO> currentCities = cityDAO.findAllCities( Language.SPANISH );
      Assert.assertNotNull( currentCities );
      int currentSize = currentCities.size() ;
      logger.debug( "Current cities size: " + currentSize );
      serviceDataSynchronizerEJB.synchronizeCities( 1L,  Language.SPANISH );
      List<CatalogTO> newCities = cityDAO.findAllCities( Language.SPANISH );
      int newSize = newCities.size() ;
      logger.debug( "New cities size: " + newSize );
      Assert.assertTrue(  currentSize != newSize );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error: ", ex );
      Assert.fail("No debio ocurrir excepcion");
    }
  }
  
  /**
   * Prueba unitaria de la sincronizacion de los estados
   */
  @Test
  public void testSynchronizeStates_ok()
  {
    try
    {
      List<StateTO<CatalogTO, Number>> currentStates = stateDAO.findAllStates( Language.SPANISH );
      Assert.assertNotNull( currentStates );
      int currentSize = currentStates.size() ;
      logger.debug( "Current states size: " + currentSize );
      serviceDataSynchronizerEJB.synchronizeStates( 1L,  Language.SPANISH );
      List<StateTO<CatalogTO, Number>> newStates = stateDAO.findAllStates( Language.SPANISH );
      int newSize = newStates.size() ;
      logger.debug( "New states size: " + newSize );
      Assert.assertTrue(  currentSize != newSize );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error: ", ex );
      Assert.fail("No debio ocurrir excepcion");
    }
  }

  
  /**
   * Prueba unitaria de la sincronizacion de los cines
   */
  @Test
  public void testSynchronizeTheaters_ok()
  {
    try
    {
      List<TheaterDO> currentTheaters = theaterDAO.findAll();
      Assert.assertNotNull( currentTheaters );
      int currentSize = currentTheaters.size() ;
      logger.debug( "Current theaters size: " + currentSize );
      serviceDataSynchronizerEJB.synchronizeTheaters(  Language.SPANISH );
      List<TheaterDO> newTheaters = theaterDAO.findAll();
      int newSize = newTheaters.size() ;
      logger.debug( "New theaters size: " + newSize );
      Assert.assertTrue(  currentSize != newSize );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error: ", ex );
      Assert.fail("No debio ocurrir excepcion");
    }
  }
  
  @Ignore
  @Test
  public void testSynchronizeEventMovies()
  {
    try
    {
      serviceDataSynchronizerEJB.synchronizeEventMovies();
    }
    catch( Exception ex )
    {
      logger.error( ex.getMessage(), ex );
      Assert.fail("No debio ocurrir excepcion");
    }
  }
 
}
