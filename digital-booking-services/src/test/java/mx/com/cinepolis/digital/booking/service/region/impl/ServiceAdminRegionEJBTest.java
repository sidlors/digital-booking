/**
 * 
 */
package mx.com.cinepolis.digital.booking.service.region.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.service.region.ServiceAdminRegionEJB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Prueba unitaria para el ServiceAdminRegionEJB
 * 
 * @author rgarcia
 */
public class ServiceAdminRegionEJBTest extends AbstractDBEJBTestUnit
{
  private static final Logger logger = LoggerFactory.getLogger( ServiceAdminRegionEJBTest.class );

  /**
   * Servicio EJB
   */
  private ServiceAdminRegionEJB serviceAdminRegionEJB;

  private final Long userID = 1L;
  private final String userName = "Usuario de Prueba";
  private final String regionDescription = "REGION MEXICO";
  private Date timestamp = null;
  private final CatalogTO territoryId = new CatalogTO( 1L );
  private final Integer CURRENT_REGION_SIZE = 16;
  private final Integer CURRENT_COUNTRY_SIZE = 2;
  private final Integer CURRENT_STATE_SIZE = 5;
  private final Integer CURRENT_CITY_SIZE = 4;
  private final Integer CURRENT_TERRITORY_SIZE = 3;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp()
   */
  @Before
  public void setUp()
  {
    // instanciar el servicio
    serviceAdminRegionEJB = new ServiceAdminRegionEJBImpl();
    // regionDAO = new RegionDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // Conectar el EntityManager al servicio y sus daos
    connect( serviceAdminRegionEJB );
    // connect( regionDAO );
    timestamp = Calendar.getInstance().getTime();
  }

  /**
   * Prueba unitaria metodo guardar la region
   */
  @Test
  public void testSaveRegion()
  {
    CatalogTO region = new CatalogTO();
    region.setFgActive( true );
    region.setName( regionDescription );
    region.setTimestamp( timestamp );
    region.setUserId( userID );
    region.setUsername( userName );
    RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( region, territoryId );
    serviceAdminRegionEJB.saveRegion( regionTO );
  }

  /**
   * Prueba unitaria metodo guardar la region, fallida
   */
  @Test
  public void testSaveRegion_IsNull()
  {
    try
    {
      RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( null, territoryId );
      serviceAdminRegionEJB.saveRegion( regionTO );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar la region, fallida
   */
  @Test
  public void testSaveRegion_InvalidTerritory()
  {
    try
    {
      CatalogTO region = new CatalogTO();
      region.setFgActive( true );
      region.setName( regionDescription );
      region.setTimestamp( timestamp );
      region.setUserId( userID );
      region.setUsername( userName );
      RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( region, null );
      serviceAdminRegionEJB.saveRegion( regionTO );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar la region, fallida
   */
  @Test
  public void testSaveRegion_InexistentTerritory()
  {
    try
    {
      CatalogTO region = new CatalogTO();
      region.setFgActive( true );
      region.setName( regionDescription );
      region.setTimestamp( timestamp );
      region.setUserId( userID );
      region.setUsername( userName );
      RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( region, new CatalogTO( 100L ) );
      serviceAdminRegionEJB.saveRegion( regionTO );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  @Test
  public void testDeleteRegion()
  {
    CatalogTO region = new CatalogTO();
    region.setFgActive( true );
    region.setName( regionDescription );
    region.setTimestamp( timestamp );
    region.setUserId( userID );
    region.setUsername( userName );
    region.setId( 15L );
    RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( region, territoryId );
    serviceAdminRegionEJB.deleteRegion( regionTO );
  }

  @Test
  public void testDeleteRegion_inexistent()
  {
    CatalogTO region = new CatalogTO();
    region.setFgActive( true );
    region.setName( regionDescription );
    region.setTimestamp( timestamp );
    region.setUserId( userID );
    region.setUsername( userName );
    region.setId( 16L );
    try
    {
      RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( region, territoryId );
      serviceAdminRegionEJB.deleteRegion( regionTO );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  @Test
  public void testDeleteRegion_IsNull()
  {

    try
    {
      RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( null, territoryId );
      serviceAdminRegionEJB.deleteRegion( regionTO );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  @Test
  public void testUpdateRegion()
  {
    CatalogTO region = new CatalogTO();
    region.setFgActive( true );
    region.setName( "REGION UPDATED" );
    region.setTimestamp( timestamp );
    region.setUserId( userID );
    region.setUsername( userName );
    region.setId( 15L );
    RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( region, territoryId );
    serviceAdminRegionEJB.updateRegion( regionTO );
  }

  @Test
  public void testUpdateRegion_inexistent()
  {
    CatalogTO region = new CatalogTO();
    region.setFgActive( true );
    region.setName( "REGION UPDATED" );
    region.setTimestamp( timestamp );
    region.setUserId( userID );
    region.setUsername( userName );
    region.setId( 16L );
    try
    {
      RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( region, territoryId );
      serviceAdminRegionEJB.updateRegion( regionTO );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  @Test
  public void testUpdateRegion_IsNull()
  {

    try
    {
      RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( null, territoryId );
      serviceAdminRegionEJB.updateRegion( regionTO );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  @Test
  public void testGetAllRegions()
  {
    List<RegionTO<CatalogTO, CatalogTO>> regions = serviceAdminRegionEJB.getAllRegions();
    Assert.assertNotNull( regions );
    Assert.assertEquals( CURRENT_REGION_SIZE.intValue(), regions.size() );
  }

  @Test
  public void testGetCatalogRegionSummary()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setNeedsPaging( true );
    logger.debug( "page 0" );
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 2 );
    pagingRequestTO.setLanguage( Language.ENGLISH );
    pagingRequestTO.setUserId( 1L );
    // Se obtiene la pagina 1 deberia regresar 10
    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> pagingResponse = serviceAdminRegionEJB
        .getCatalogRegionSummary( pagingRequestTO );
    Assert.assertNotNull( pagingResponse );
    Assert.assertNotNull( pagingResponse.getElements() );
    // Se obtiene la pagina 2, deberia regresar 5
    PagingRequestTO pagingRequestTO2 = new PagingRequestTO();
    pagingRequestTO2.setNeedsPaging( true );
    logger.debug( "page 1" );
    pagingRequestTO2.setPage( 1 );
    pagingRequestTO2.setPageSize( 2 );
    pagingRequestTO2.setLanguage( Language.SPANISH );
    pagingRequestTO2.setUserId( 1L );
    pagingResponse = serviceAdminRegionEJB.getCatalogRegionSummary( pagingRequestTO2 );
    Assert.assertNotNull( pagingResponse );
    Assert.assertNotNull( pagingResponse.getElements() );
    Assert.assertEquals( 1, pagingResponse.getElements().size() );

  }

  @Test
  public void testGetAllCountries()
  {
    List<CatalogTO> countries = serviceAdminRegionEJB.getAllCountries();
    Assert.assertNotNull( countries );
    Assert.assertEquals( CURRENT_COUNTRY_SIZE.intValue(), countries.size() );
  }

  @Test
  public void testGetAllStates()
  {
    List<StateTO<CatalogTO, Number>> states = serviceAdminRegionEJB.getAllStates();
    Assert.assertNotNull( states );
    Assert.assertEquals( CURRENT_STATE_SIZE.intValue(), states.size() );
  }

  @Test
  public void testGetAllRegionsByTerritory()
  {
    CatalogTO territory = new CatalogTO();
    territory.setId( 1L );
    List<RegionTO<CatalogTO, CatalogTO>> regions = serviceAdminRegionEJB.getAllRegionsByTerritory( territory );
    Assert.assertNotNull( regions );
    Assert.assertEquals( 6, regions.size() );
  }

  @Test
  public void testGetAllCities()
  {
    List<CatalogTO> countries = serviceAdminRegionEJB.getAllCities();
    Assert.assertNotNull( countries );
    Assert.assertEquals( CURRENT_CITY_SIZE.intValue(), countries.size() );
  }

  @Test
  public void getAllTerritories()
  {
    List<CatalogTO> territories = serviceAdminRegionEJB.getAllTerritories();
    Assert.assertNotNull( territories );
    Assert.assertFalse( territories.isEmpty() );
    Assert.assertEquals( CURRENT_TERRITORY_SIZE.intValue(), territories.size() );
  }
  /**
   * method for test the function that get all states by country
   */
  @Test 
  public void testGetAllStatesByCountry()
  {
    CatalogTO country=new CatalogTO();
    country.setFgActive( true );
    country.setId( 1L );
    country.setIdLanguage( 1L );
    country.setIdVista( new String("1001") );
    country.setName( "Mexcico" );
    country.setTimestamp( new Date() );
    country.setUserId( 1L );
    country.setUsername( "USER 1s" );
    List<StateTO <CatalogTO,Number>> states=this.serviceAdminRegionEJB.getAllStatesByCountry( country );
    Assert.assertNotNull( states );
    for(StateTO state:states)
    {
      Assert.assertNotNull( state );
    }
  }
  /**
   * method for test the function that find all active regions
   */
  @Test 
  public void testFindAllActiveRegion()
  {
    AbstractTO abstracTO = new AbstractTO();
    abstracTO.setFgActive( true );
    abstracTO.setIdLanguage( 1L );
    abstracTO.setTimestamp( new Date() );
    abstracTO.setUserId( 1L );
    abstracTO.setUsername( "USER 1" );
    List<CatalogTO> regions = this.serviceAdminRegionEJB.findAllActiveRegions( abstracTO );
    Assert.assertNotNull( regions );
    for(CatalogTO region:regions)
    {
      Assert.assertNotNull( region );
    }
    
  }

}
