package mx.com.cinepolis.digital.booking.service.movie.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.utils.ReflectionHelper;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.service.income.IncomeServiceEJB;
import mx.com.cinepolis.digital.booking.service.income.impl.IncomeServiceEJBImpl;
import mx.com.cinepolis.digital.booking.service.movie.ServiceAdminMovieEJB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceAdminMovieEJBTest extends AbstractDBEJBTestUnit
{

  private static final Logger logger = LoggerFactory.getLogger( ServiceAdminMovieEJBTest.class );
  private ServiceAdminMovieEJB serviceAdminMovieEJB;
  private Date timestamp = null;
  CatalogTO movieFormats;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    serviceAdminMovieEJB = new ServiceAdminMovieEJBImpl();
    IncomeServiceEJB incomeServiceEJB = new IncomeServiceEJBImpl();
    // regionDAO = new RegionDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    this.initializeData( "dataset/business/incomes-perisur.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( serviceAdminMovieEJB );
    connect( incomeServiceEJB );
    ReflectionHelper.set( incomeServiceEJB, "incomeServiceEJB", serviceAdminMovieEJB );
    timestamp = Calendar.getInstance().getTime();

  }

  /**
   * Prueba unitaria metodo guardar event
   */
  @Test
  public void testSave_Movie()
  {
    try
    {
      EventMovieTO eventMovieTO = createEventMovieTO();
      serviceAdminMovieEJB.saveMovie( eventMovieTO );
      Assert.fail( "Deberia Lanzar exception" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Método para cargar los datos de un EventMovieTO
   * 
   * @return
   */
  private EventMovieTO createEventMovieTO()
  {
    EventMovieTO eventMovieTO = new EventMovieTO();
    DistributorTO distributorTO = new DistributorTO();
    distributorTO.setFgActive( true );
    distributorTO.setId( 1L );
    distributorTO.setIdLanguage( 1L );
    distributorTO.setIdVista( "1L" );
    distributorTO.setName( "Amarok" );
    distributorTO.setShortName( "amarok" );
    distributorTO.setTimestamp( timestamp );
    distributorTO.setUserId( 1L );
    distributorTO.setUsername( "usuario de prueba" );
    eventMovieTO.setDistributor( distributorTO );
    eventMovieTO.setCodeDBS( "12345" );
    eventMovieTO.setCurrentMovie( false );
    eventMovieTO.setDsEventName( "original nombre de prueba pelicula" );
    eventMovieTO.setDuration( 220 );
    eventMovieTO.setIdEvent( 60L );
    ArrayList<CatalogTO> movieFormats = new ArrayList<CatalogTO>();
    CatalogTO movieFormat = new CatalogTO();
    movieFormat.setId( 1L );
    movieFormat.setName( "3d " );
    movieFormats.add( movieFormat );
    eventMovieTO.setMovieFormats( movieFormats );
    // event.set
    eventMovieTO.setName( "nombre pelicula" );
    eventMovieTO.setPremiere( true );
    eventMovieTO.setPrerelease( true );
    eventMovieTO.setQtCopy( 5 );
    ArrayList<CatalogTO> soundFormats = new ArrayList<CatalogTO>();
    CatalogTO soundFormat = new CatalogTO();
    soundFormat.setId( 1L );
    soundFormat.setName( "Sub Esp " );
    soundFormats.add( soundFormat );
    eventMovieTO.setSoundFormats( movieFormats );
    eventMovieTO.setTimestamp( timestamp );
    eventMovieTO.setUserId( 1L );
    eventMovieTO.setUsername( "usuario de prueba " );
    eventMovieTO.setIdVista( "10012" );
    eventMovieTO.setIdMovieImage( 1L );
    
    return eventMovieTO;
  }
  /**
   * method for test the method that save a movie 
   */
  /*@Test
  public void testSaveMovie()
  {
    EventMovieTO eventMovie=createEventMovieTO();
    eventMovie.setIdEvent( 13L );
    eventMovie.set
    this.serviceAdminMovieEJB.saveMovie( eventMovie );
  }*/

  /**
   * method for test that save the movie image 
   */
  @Test
  public void testSaveMovieImage()
  {
    FileTO fileTO=new FileTO();
    fileTO.setFgActive( true );
    fileTO.setFile( new byte[100] );
    fileTO.setId( 2L );
    fileTO.setIdLanguage( 1L );
    fileTO.setIdVista( "10012" );
    fileTO.setName( "file 1" );
    fileTO.setTimestamp( new  Date() );
    fileTO.setUserId( 1L );
    fileTO.setUsername( "User 1" );
    FileTO fileFinal=this.serviceAdminMovieEJB.saveMovieImage( fileTO );
    Assert.assertNotNull( fileFinal );
    Assert.assertEquals( fileTO.getId(), fileFinal.getId() );
    
  }
  /**
   * method for test the function that find the movie image for id 
   */
  @Test
  public void testFindMovieImage()
  {
      FileTO image=this.serviceAdminMovieEJB.findMovieImage( 1L );
      Assert.assertNotNull( image );
      Assert.assertEquals( 1L, image.getId().longValue() );
  }
/**
 * method ofr test the function that find all countries 
 */
  @Test
  public void testGetAllContries()
  {
    List<CatalogTO> countries=this.serviceAdminMovieEJB.getAllContries();
    Assert.assertNotNull( countries );
    for(CatalogTO country:countries)
    {
      Assert.assertNotNull( country );
    }
  }

  /**
   * Prueba unitaria metodo borrar movie
   */
  @Test(expected = NullPointerException.class)
  public void testDeleteMovie_Exception()
  {

    EventMovieTO eventMovieTO = createEventMovieTO();
    this.serviceAdminMovieEJB.deleteMovie( eventMovieTO );
    
  }

  /**
   * Prueba unitaria para actualizar movie
   */
  @Test
  public void testUpdateMovie()
  {
    
   
      EventMovieTO eventMovieTO = createEventMovieTO();
      this.serviceAdminMovieEJB.updateMovie( eventMovieTO );
  }
  /**
   *method for test the function that get the id Distributor parameter 
   */
  @Test
  public void testgetIdDistributorParameter()
  {
    String parameter=this.serviceAdminMovieEJB.getIdDistributorParameter();
    Assert.assertNotNull( parameter );
    
  }

/**
 * prueba unitaria para obtener el atalogo de peliculas 
 */
  @Test
  public void testGetCatalogMovieSummary()
  {
    PagingRequestTO pagingRequestTO=new PagingRequestTO();
    pagingRequestTO.setFgActive( true );
    pagingRequestTO.setPage( 10 );
    pagingRequestTO.setPageSize( 10 );
    PagingResponseTO<EventMovieTO> movieCatalog=this.serviceAdminMovieEJB.getCatalogMovieSummary( pagingRequestTO );
    Assert.assertNotNull( movieCatalog );
    Assert.assertEquals( 36, movieCatalog.getTotalCount() );
    
  }

  /**
   * Method for test the method that check if 
   * the movie is in booking
   */
  @Test 
  public void testisMovieInBooking()
  {
    boolean fgMovieinBooking=this.serviceAdminMovieEJB.isMovieInBooking( 1L );
    Assert.assertTrue( fgMovieinBooking );
        
  }
  
  /**
   * Prueba unitaria para obtener el topWeek
   */
  // TODO CORREGIR PRUEBA UNITARIA,
  // YA QUE FALLA CON LOS CAMBIOS DE SALA CERO
  @Test
  public void testGetTopWeek()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2015, Calendar.FEBRUARY, 16 );
    AbstractTO abstractTO = new AbstractTO();
    abstractTO.setTimestamp( cal.getTime() );

    List<IncomeTO> incomes = this.serviceAdminMovieEJB.getTopWeek( abstractTO );
    Assert.assertNotNull( incomes );
    Assert.assertFalse( incomes.isEmpty() );
    for( IncomeTO income : incomes )
    {
      System.out.println( income.getEvent().getDsEventName() + ", " + income.getIncome() + ", " + income.getTickets() );
    }
  }
}
