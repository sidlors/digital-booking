package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.query.CityQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.CityTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.model.CityDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.CityDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;

public class CityDAOTest extends AbstractDBEJBTestUnit
{
  private CityDAO cityDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    cityDAO = new CityDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( cityDAO );

  }

  /**
   * {@link Description} caso de prueba para el metodo
   */
  @Test
  public void testFindAllCities()
  {
    List<CatalogTO> cities = this.cityDAO.findAllCities();
    Assert.assertNotNull( cities );
    Assert.assertFalse( cities.isEmpty() );
    for( CatalogTO city : cities )
    {
      Assert.assertNotNull( city );
    }
  }

  @Test
  public void testFindAllCitiesLanguage()
  {
    List<CatalogTO> cities = this.cityDAO.findAllCities( Language.ENGLISH );
    Assert.assertNotNull( cities );
    Assert.assertFalse( cities.isEmpty() );
    for( CatalogTO city : cities )
    {
      System.out.println( city );
    }
  }

  @Test
  public void testFindByIdVistaAndActive()
  {
    List<CityDO> cities = this.cityDAO.findByIdVistaAndActive( "1001" );
    Assert.assertNotNull( cities );
    Assert.assertEquals( 1, cities.size() );
    Assert.assertNotNull( cities.get( 0 ).getIdCountry() );
    Assert.assertEquals( 1, cities.get( 0 ).getIdCountry().getIdCountry().intValue() );
  }

  /**
   * {@link Description} caso de prueba para la busqueda de ciudades por idVista
   */
  @Test
  public void testFindAllCitiesByIdVista()
  {
    String idVista = "1001";
    List<CityDO> cities = this.cityDAO.findByIdVista( idVista );
    Assert.assertNotNull( cities );
    Assert.assertFalse( cities.isEmpty() );
    for( CityDO city : cities )
    {
      Assert.assertNotNull( city );
    }
  }

  /**
   * {@link Description} caso de prueba para actualizar ciudad
   */
  @Test
  public void testUpdate()
  {
    CityTO cityTO = new CityTO();
    cityTO.setFgActive( true );
    cityTO.setIdLanguage( 1L );
    cityTO.setTimestamp( new Date() );
    cityTO.setUserId( 1L );
    cityTO.setUsername( "yisus" );
    cityTO.setName( "New York" );
    cityTO.setId( 1L );

    CatalogTO countryTO = new CatalogTO();
    countryTO.setFgActive( true );
    countryTO.setId( 1L );
    countryTO.setIdLanguage( 1L );
    cityTO.setTimestamp( new Date() );
    cityTO.setUserId( 1L );
    cityTO.setUsername( "yisus" );
    cityTO.setName( "USA" );
    cityTO.setCountry( countryTO );
    CatalogTO catalogTO=new CatalogTO();
    Integer number = new Integer(1);
    catalogTO.setFgActive( true );
    catalogTO.setId( 1L );
    catalogTO.setIdLanguage( 1L );
    catalogTO.setName( "Tlaxcala" );
    
    
    StateTO<CatalogTO ,Number> state=new StateTO<CatalogTO,Number>(catalogTO,number);
    cityTO.setState( state );
    this.cityDAO.update( cityTO );
    CityDO city = this.cityDAO.find( 1 );
    Assert.assertNotNull( city );
    Assert.assertEquals( 1L, city.getIdCity().longValue() );
  }

  /**
   * {@link Description} caso de prueba para guardar cuidad
   */
  @Test
  public void testSave()
  {
    CityTO cityTO = new CityTO();
    cityTO.setFgActive( true );
    cityTO.setIdLanguage( 1L );
    cityTO.setTimestamp( new Date() );
    cityTO.setUserId( 1L );
    cityTO.setUsername( "yisus" );
    cityTO.setName( "New York" );

    CatalogTO countryTO = new CatalogTO();
    countryTO.setFgActive( true );
    countryTO.setId( 1L );
    countryTO.setIdLanguage( 1L );
    cityTO.setTimestamp( new Date() );
    cityTO.setUserId( 1L );
    cityTO.setUsername( "yisus" );
    cityTO.setName( "USA" );
    cityTO.setCountry( countryTO );
    CatalogTO catalogTO=new CatalogTO();
    Integer number = new Integer(1);
    catalogTO.setFgActive( true );
    catalogTO.setId( 1L );
    catalogTO.setIdLanguage( 1L );
    catalogTO.setName( "Tlaxcala" );
    
    
    StateTO<CatalogTO ,Number> state=new StateTO<CatalogTO,Number>(catalogTO,number);
    cityTO.setState( state );
    int numCities = this.cityDAO.count();
    this.cityDAO.save( cityTO );
    int numCities2 = this.cityDAO.count();
    Assert.assertNotEquals( numCities, numCities2 );
  }
  /**
   * 
   */
  @Test 
  public void test_findByIdState()
  {
    List<CityDO> cityList=this.cityDAO.findByIdState( 1L );
    Assert.assertNotNull( cityList );
    for(CityDO city:cityList)
    {
      Assert.assertNotNull( city );
    }
  }

  /**
   * {@link Description} Caso de prueba la busqueda por id del pais
   */
  @Test
  public void testfindByIdCountry()
  {
    List<CityDO> cities = this.cityDAO.findByIdCountry( 1L );
    Assert.assertNotNull( cities );
    for( CityDO cityDO : cities )
    {
      Assert.assertNotNull( cityDO );
      Assert.assertEquals( 1L, cityDO.getIdCountry().getIdCountry().longValue() );
    }
  }

  /**
   * {@link Description} Tests the pagination query with active criteria selected.
   */
  @Test
  public void testFindAllByPagingCaseOne()
  {
    Long userId = 1L;
    int page = 0, pageSize = 10;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( userId );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CityQuery.CITY_NAME );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( CityQuery.CITY_ACTIVE, true );
    PagingResponseTO<CityTO> response = this.cityDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( CityTO cityTO : response.getElements() )
    {
      System.out.println( cityTO );
    }
    System.out.println( "********************************Registros encontrados: " + response.getElements().size() );
  }

  /**
   * {@link Description} Tests the pagination query with name and language criteria selected.
   */
  @Test
  public void testFindAllByPagingCaseTwo()
  {
    Long userId = 1L;
    int page = 0, pageSize = 10;
    String cityNameSelected = "2";
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( userId );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CityQuery.CITY_NAME );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( CityQuery.CITY_ACTIVE, true );
    pagingRequestTO.getFilters().put( CityQuery.CITY_NAME, cityNameSelected );
    PagingResponseTO<CityTO> response = this.cityDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( CityTO cityTO : response.getElements() )
    {
      System.out.println( cityTO );
    }
    System.out.println( "********************************Registros encontrados: " + response.getElements().size() );
  }

  /**
   * {@link Description} Tests the pagination query with VistaId criteria selected.
   */
  @Test
  public void testFindAllByPagingCaseThree()
  {
    Long userId = 1L;
    int page = 0, pageSize = 10;
    String idVistaSelected = "1001";
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( userId );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CityQuery.CITY_NAME );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( CityQuery.CITY_ACTIVE, true );
    pagingRequestTO.getFilters().put( CityQuery.CITY_ID_VISTA, idVistaSelected );
    PagingResponseTO<CityTO> response = this.cityDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( CityTO cityTO : response.getElements() )
    {
      System.out.println( cityTO );
    }
    System.out.println( "********************************Registros encontrados: " + response.getElements().size() );
  }

  /**
   * {@link Description} Tests the pagination query with Country criteria selected.
   */
  @Test
  public void testFindAllByPagingCaseFour()
  {
    Long userId = 1L;
    int page = 0, pageSize = 10, idCountrySelected = 1;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( userId );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CityQuery.CITY_NAME );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( CityQuery.CITY_ACTIVE, true );
    pagingRequestTO.getFilters().put( CityQuery.COUNTRY_ID, idCountrySelected );
    PagingResponseTO<CityTO> response = this.cityDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( CityTO cityTO : response.getElements() )
    {
      System.out.println( cityTO );
    }
    System.out.println( "********************************Registros encontrados: " + response.getElements().size() );
  }

}
