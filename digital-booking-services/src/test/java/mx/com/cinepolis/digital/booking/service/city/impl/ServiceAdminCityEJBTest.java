package mx.com.cinepolis.digital.booking.service.city.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.query.CityQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.CityTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.dao.util.CityDOToCityTOTransformer;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.CityDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.CityDAOImpl;
import mx.com.cinepolis.digital.booking.service.city.ServiceAdminCityEJB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;

/**
 * Clase de pruebas de ServiceAdminCityEJB
 * 
 * @author jreyesv
 */
public class ServiceAdminCityEJBTest extends AbstractDBEJBTestUnit
{

  /**
   * Service
   */
  private ServiceAdminCityEJB serviceAdminCityEJB;

  /**
   * DAO to consult records.
   */
  private CityDAO cityDAO;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp()
   */
  @Before
  public void setUp()
  {
    // instanciar el servicio
    this.serviceAdminCityEJB = new ServiceAdminCityEJBImpl();
    this.cityDAO = new CityDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    connect( this.serviceAdminCityEJB );
    connect( cityDAO );
  }

  /**
   * {@link Description} method that tests the save operation of a null city.
   */
  @Test(expected = DigitalBookingException.class)
  public void saveCityTestCaseOne()
  {
    this.serviceAdminCityEJB.saveCity( null );
  }

  /**
   * {@link Description} method that tests the save operation of a city with valid country and empty name.
   */
  @Test(expected = DigitalBookingException.class)
  public void saveCityTestCaseTwo()
  {
    Long coountryId = 1L;
    CityTO cityTO = new CityTO();
    cityTO.setCountry( new CatalogTO( coountryId ) );
    this.serviceAdminCityEJB.saveCity( cityTO );
  }

  /**
   * {@link Description} method that tests the save operation of a city with valid name and null country.
   */
  @Test(expected = DigitalBookingException.class)
  public void saveCityTestCaseThree()
  {
    String cityName = "My city";
    CityTO cityTO = new CityTO();
    cityTO.setName( cityName );
    this.serviceAdminCityEJB.saveCity( cityTO );
  }

  /**
   * {@link Description} method that tests the save operation of a valid city.
   */
  @Test
  public void saveCityTestCaseFour()
  {
    Long userId = 1L, coountryId = 1L;
    String cityName = "My city";
    CityTO cityTO = new CityTO();
    cityTO.setCountry( new CatalogTO( coountryId ) );
    cityTO.setName( cityName );
    cityTO.setFgActive( Boolean.TRUE );
    cityTO.setUserId( userId );
    cityTO.setTimestamp( new Date() );
    int previousCities = this.cityDAO.count();
    this.serviceAdminCityEJB.saveCity( cityTO );
    int ultimateCities = this.cityDAO.count();
    Assert.assertTrue( (previousCities < ultimateCities) );
  }

  /**
   * {@link Description} method that tests the update operation of a null city.
   */
  @Test(expected = DigitalBookingException.class)
  public void updateCityTestCaseOne()
  {
    this.serviceAdminCityEJB.updateCity( null );
  }

  /**
   * {@link Description} method that tests the update operation of a city with valid country and empty name.
   */
  @Test(expected = DigitalBookingException.class)
  public void updateCityTestCaseTwo()
  {
    int cityId = 1;
    CityTO cityTO = (CityTO) new CityDOToCityTOTransformer().transform( this.cityDAO.find( cityId ) );
    cityTO.setName( null );
    this.serviceAdminCityEJB.updateCity( cityTO );
  }

  /**
   * {@link Description} method that tests the update operation of a city with valid name and null country.
   */
  @Test(expected = DigitalBookingException.class)
  public void updateCityTestCaseThree()
  {
    int cityId = 1;
    CityTO cityTO = (CityTO) new CityDOToCityTOTransformer().transform( this.cityDAO.find( cityId ) );
    cityTO.setCountry( null );
    this.serviceAdminCityEJB.updateCity( cityTO );
  }

  /**
   * {@link Description} method that tests the update operation of a valid city.
   */
  @Test
  public void updateCityTestCaseFour()
  {
    Long userId = 1L;
    int cityId = 1;
    String cityName = "My city modified";
    CityTO cityTO = (CityTO) new CityDOToCityTOTransformer().transform( this.cityDAO.find( cityId ) );
    cityTO.setName( cityName );
    cityTO.setUserId( userId );
    cityTO.setTimestamp( new Date() );
    int previousCities = this.cityDAO.count();
    this.serviceAdminCityEJB.updateCity( cityTO );
    int ultimateCities = this.cityDAO.count();
    Assert.assertTrue( (previousCities == ultimateCities) );
  }

  /**
   * {@link Description} method that tests the delete operation of a null city.
   */
  @Test(expected = DigitalBookingException.class)
  public void deleteCityTestCaseOne()
  {
    this.serviceAdminCityEJB.deleteCity( null );
  }

  /**
   * {@link Description} method that tests the delete operation of a city with valid country and empty name.
   */
  @Test(expected = DigitalBookingException.class)
  public void deleteCityTestCaseTwo()
  {
    int cityId = 1;
    CityTO cityTO = (CityTO) new CityDOToCityTOTransformer().transform( this.cityDAO.find( cityId ) );
    cityTO.setName( null );
    this.serviceAdminCityEJB.deleteCity( cityTO );
  }

  /**
   * {@link Description} method that tests the delete operation of a city with valid name and null country.
   */
  @Test(expected = DigitalBookingException.class)
  public void deleteCityTestCaseThree()
  {
    int cityId = 1;
    CityTO cityTO = (CityTO) new CityDOToCityTOTransformer().transform( this.cityDAO.find( cityId ) );
    cityTO.setCountry( null );
    this.serviceAdminCityEJB.deleteCity( cityTO );
  }

  /**
   * {@link Description} method that tests the delete operation of a valid city.
   */
  @Test
  public void deleteCityTestCaseFour()
  {
    int cityId = 1;
    CityTO cityTO = (CityTO) new CityDOToCityTOTransformer().transform( this.cityDAO.find( cityId ) );
    int previousCities = this.cityDAO.findAllCities().size();
    this.serviceAdminCityEJB.deleteCity( cityTO );
    int ultimateCities = this.cityDAO.findAllCities().size();
    Assert.assertTrue( (previousCities > ultimateCities) );
  }

  /**
   * {@link Description} method that tests the find cities by paging operation.
   */
  @Test
  public void findAllCitiesByPagingTestCaseOne()
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
    PagingResponseTO<CityTO> response = this.serviceAdminCityEJB.findAllCitiesByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( CityTO cityTO : response.getElements() )
    {
      System.out.println( cityTO );
    }
    System.out.println( "********************************Registros encontrados: " + response.getElements().size() );
  }

  /**
   * {@link Description} method that tests the find cities by paging operation, without filters.
   */
  @Test
  public void findAllCitiesByPagingTestCaseTwo()
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
    PagingResponseTO<CityTO> response = this.serviceAdminCityEJB.findAllCitiesByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    for( CityTO cityTO : response.getElements() )
    {
      System.out.println( cityTO );
    }
    System.out.println( "********************************Registros encontrados: " + response.getElements().size() );
  }

}
