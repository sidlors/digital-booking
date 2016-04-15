/**
 * 
 */
package mx.com.cinepolis.digital.booking.service.theater.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsTypeDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.IncomeSettingsDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.IncomeSettingsTypeDAOImpl;
import mx.com.cinepolis.digital.booking.service.theater.ServiceAdminTheaterEJB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Prueba unitaria para el ServiceAdminTheater
 * 
 * @author rgarcia
 */
public class ServiceAdminTheaterEJBTest extends AbstractDBEJBTestUnit
{
  private static final Logger logger = LoggerFactory.getLogger( ServiceAdminTheaterEJBTest.class );

  /**
   * Servicio EJB
   */
  private ServiceAdminTheaterEJB serviceAdminTheaterEJB;
  private IncomeSettingsDAO incomeSettingsDAO;
  private IncomeSettingsTypeDAO incomeSettingsTypeDAO;

  private final Long userID = 1L;
  private final String userName = "Usuario de Prueba";
  private Date timestamp = null;
  private final Integer CURRENT_THEATERS_SIZE = 6;
  CatalogTO city = null;
  RegionTO<CatalogTO, CatalogTO> regionTO = null;

  StateTO<CatalogTO, Integer> state = null;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp()
   */
  @Before
  public void setUp()
  {
    // instanciar el servicio
    serviceAdminTheaterEJB = new ServiceAdminTheaterEJBImpl();
    this.incomeSettingsDAO = new IncomeSettingsDAOImpl();
    this.incomeSettingsTypeDAO = new IncomeSettingsTypeDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // Conectar el EntityManager al servicio y sus daos
    connect( serviceAdminTheaterEJB );
    connect( this.incomeSettingsDAO );
    connect( this.incomeSettingsTypeDAO );
    timestamp = Calendar.getInstance().getTime();
    city = new CatalogTO();
    city.setId( 1L );
    city.setName( "City 1" );

    CatalogTO region = new CatalogTO();
    region.setId( 1L );
    region.setName( "Region 1" );
    CatalogTO territory = new CatalogTO();
    territory.setId( 1L );
    regionTO = new RegionTO<CatalogTO, CatalogTO>( region, territory );

    CatalogTO stateTO = new CatalogTO();
    stateTO.setId( 1L );
    stateTO.setName( "State 1" );
    state = new StateTO<CatalogTO, Integer>( stateTO, 1 );
  }

  /**
   * Se crea las salas
   */
  private List<ScreenTO> createScreenTO()
  {
    List<ScreenTO> screens = new ArrayList<ScreenTO>();
    ScreenTO screenTO = new ScreenTO();
    screenTO.setName( "Screen 1" );
    screenTO.setTimestamp( timestamp );
    screenTO.setUserId( userID );
    screenTO.setUsername( userName );
    screenTO.setNuCapacity( 100 );
    screenTO.setNuScreen( 1 );
    CatalogTO movieFormats = new CatalogTO();
    movieFormats.setId( 3L );
    movieFormats.setName( "Movie Formats" );
    List<CatalogTO> movieFormatList = new ArrayList<CatalogTO>();
    movieFormatList.add( movieFormats );
    CatalogTO soundFormats = new CatalogTO();
    soundFormats.setId( 1L );
    soundFormats.setName( "Sound Formats" );
    List<CatalogTO> soundFormatList = new ArrayList<CatalogTO>();
    soundFormatList.add( soundFormats );
    screenTO.setMovieFormats( movieFormatList );
    screenTO.setSoundFormats( soundFormatList );
    screens.add( screenTO );
    return screens;
  }

  /**
   * Se crea un cine
   * 
   * @return
   */
  private TheaterTO createTheater()
  {
    TheaterTO theater = new TheaterTO();
    theater.setIdVista( "9999" );
    theater.setFgActive( true );
    theater.setDsTelephone( "12345678" );
    theater.setFgActive( true );
    theater.setName( "CINE DE PRUEBA" );
    theater.setTimestamp( timestamp );
    theater.setUserId( userID );
    theater.setUsername( userName );
    theater.setCity( city );
    theater.setRegion( regionTO );
    theater.setScreens( createScreenTO() );
    theater.setState( state );
    return theater;
  }

  /**
   * Prueba unitaria metodo guardar el cine
   */
  @Test
  public void testSaveTheater()
  {
    TheaterTO theater = createTheater();
    serviceAdminTheaterEJB.saveTheater( theater );
  }

  /**
   * Prueba unitaria metodo guardar el cine con configuración de ingresos.
   */
  @Test
  public void testSaveTheaterWithIncomeSettings()
  {
    int noRecords = this.serviceAdminTheaterEJB.getAllTheaters().size();
    int noRecordsExpected = noRecords + 1;
    List<IncomeSettingsTO> incomeSettingsList = new ArrayList<IncomeSettingsTO>();
    Long idTheater = 1L;
    Long idUser = 1L;
    TheaterTO theater = createTheater();
    // Income settings
    IncomeSettingsTypeTO incomeSettingsType = this.incomeSettingsTypeDAO.get( 1 );
    IncomeSettingsTO incomeSettingsTOType1 = new IncomeSettingsTO();
    incomeSettingsTOType1.setId( null );
    incomeSettingsTOType1.setIdTheater( idTheater );
    incomeSettingsTOType1.setIncomeSettingsType( incomeSettingsType );
    incomeSettingsTOType1.setGreenSemaphore( 85.55 );
    incomeSettingsTOType1.setYellowSemaphore( 40.09 );
    incomeSettingsTOType1.setRedSemaphore( 38.99 );
    incomeSettingsTOType1.setTimestamp( new Date() );
    incomeSettingsTOType1.setUserId( idUser );
    incomeSettingsList.add( incomeSettingsTOType1 );

    IncomeSettingsTypeTO incomeSettingsType2 = this.incomeSettingsTypeDAO.get( 2 );
    IncomeSettingsTO incomeSettingsTO2 = new IncomeSettingsTO();
    incomeSettingsTO2.setId( null );
    incomeSettingsTO2.setIdTheater( idTheater );
    incomeSettingsTO2.setIncomeSettingsType( incomeSettingsType2 );
    incomeSettingsTO2.setGreenSemaphore( 15.99 );
    incomeSettingsTO2.setYellowSemaphore( 40.09 );
    incomeSettingsTO2.setRedSemaphore( 45.99 );
    incomeSettingsTO2.setTimestamp( new Date() );
    incomeSettingsTO2.setUserId( idUser );
    incomeSettingsList.add( incomeSettingsTO2 );
    theater.setIncomeSettingsList( incomeSettingsList );

    serviceAdminTheaterEJB.saveTheater( theater );
    noRecords = this.serviceAdminTheaterEJB.getAllTheaters().size();
    Assert.assertEquals( noRecords, noRecordsExpected );

  }

  /**
   * Test Save
   */
  @Test(expected = DigitalBookingException.class)
  public void testSave_nameAlreadyExists()
  {
    TheaterTO theater = createTheater();
    serviceAdminTheaterEJB.saveTheater( theater );

    theater = createTheater();
    serviceAdminTheaterEJB.saveTheater( theater );
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testSaveTheater_IsNull()
  {
    try
    {
      serviceAdminTheaterEJB.saveTheater( null );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testSaveTheater_RegionIsNull()
  {
    try
    {
      CatalogTO territory = new CatalogTO();
      territory.setId( 1L );
      TheaterTO theater = createTheater();
      RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( null, territory );
      theater.setRegion( regionTO );

      serviceAdminTheaterEJB.saveTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testSaveTheater_CityIsNull()
  {
    try
    {
      TheaterTO theater = createTheater();
      theater.setCity( null );
      serviceAdminTheaterEJB.saveTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testSaveTheater_StateIsNull()
  {
    try
    {
      TheaterTO theater = createTheater();
      theater.setState( null );
      serviceAdminTheaterEJB.saveTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testSaveTheater_ScreensAreNull()
  {
    try
    {
      TheaterTO theater = createTheater();
      theater.setScreens( null );
      serviceAdminTheaterEJB.saveTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testSaveTheater_InvalidRegion()
  {
    try
    {
      CatalogTO region = new CatalogTO();
      region.setFgActive( true );
      region.setTimestamp( timestamp );
      region.setUserId( userID );
      region.setUsername( userName );
      TheaterTO theater = createTheater();
      CatalogTO territory = new CatalogTO();
      territory.setId( 100L );
      RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( region, territory );
      theater.setRegion( regionTO );
      serviceAdminTheaterEJB.saveTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testSaveTheater_InvalidCity()
  {
    try
    {
      CatalogTO city = new CatalogTO();
      city.setId( 100L );
      TheaterTO theater = createTheater();
      theater.setCity( city );
      serviceAdminTheaterEJB.saveTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testSaveTheater_InvalidState()
  {
    try
    {
      CatalogTO stateTO = new CatalogTO();
      TheaterTO theater = createTheater();
      StateTO<CatalogTO, Integer> state = new StateTO<CatalogTO, Integer>( stateTO, 100 );
      theater.setState( state );
      serviceAdminTheaterEJB.saveTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testSaveTheater_InvalidScreens()
  {
    try
    {
      List<ScreenTO> screens = new ArrayList<ScreenTO>();
      ScreenTO screenTO = new ScreenTO();
      screenTO.setName( "Screen 1" );
      screenTO.setTimestamp( timestamp );
      screenTO.setUserId( userID );
      screenTO.setUsername( userName );
      screenTO.setNuCapacity( null );
      screens.add( screenTO );

      TheaterTO theater = createTheater();
      theater.setScreens( screens );
      serviceAdminTheaterEJB.saveTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testSaveTheater_InvalidScreens_MovieFormatsIsNull()
  {
    try
    {
      List<ScreenTO> screens = new ArrayList<ScreenTO>();
      ScreenTO screenTO = new ScreenTO();
      screenTO.setName( "Screen 1" );
      screenTO.setTimestamp( timestamp );
      screenTO.setUserId( userID );
      screenTO.setUsername( userName );
      screenTO.setNuCapacity( null );
      screenTO.setNuScreen( 1 );
      screenTO.setMovieFormats( null );
      screens.add( screenTO );

      TheaterTO theater = createTheater();
      theater.setScreens( screens );
      serviceAdminTheaterEJB.saveTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testSaveTheater_InvalidScreens_SoundFormatsIsNull()
  {
    try
    {
      List<ScreenTO> screens = new ArrayList<ScreenTO>();
      ScreenTO screenTO = new ScreenTO();
      screenTO.setName( "Screen 1" );
      screenTO.setTimestamp( timestamp );
      screenTO.setUserId( userID );
      screenTO.setUsername( userName );
      screenTO.setNuCapacity( null );
      screenTO.setNuScreen( 1 );
      CatalogTO movieFormats = new CatalogTO();
      movieFormats.setId( 1L );
      movieFormats.setName( "Movie Formats" );
      List<CatalogTO> movieFormatList = new ArrayList<CatalogTO>();
      movieFormatList.add( movieFormats );
      screenTO.setMovieFormats( null );
      screenTO.setSoundFormats( null );
      screens.add( screenTO );

      TheaterTO theater = createTheater();
      theater.setScreens( screens );
      serviceAdminTheaterEJB.saveTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria del metodo de eliminar cine
   */
  @Test
  public void testDeleteTheater()
  {
    TheaterTO theater = createTheater();
    theater.setId( 1L );
    serviceAdminTheaterEJB.deleteTheater( theater );
  }

  /**
   * Prueba unitaria del metodo de eliminar cine junto con su configuración de ingresos.
   */
  @Test
  public void testDeleteTheaterWithIncomeSettings()
  {
    TheaterTO theaterTO = this.serviceAdminTheaterEJB.getTheater( 1L );
    this.serviceAdminTheaterEJB.deleteTheater( theaterTO );
  }

  /**
   * Prueba unitaria fallida del metodo de eliminar cine
   */
  @Test
  public void testDeleteTheater_inexistent()
  {
    TheaterTO theater = createTheater();
    theater.setId( 16L );
    try
    {
      serviceAdminTheaterEJB.deleteTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria fallida del metodo de eliminar cine
   */
  @Test
  public void testDeleteTheater_IsNull()
  {

    try
    {
      serviceAdminTheaterEJB.deleteTheater( null );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  @Test
  public void testUpdateTheater()
  {
    TheaterTO theater = createTheater();
    theater.setId( 1L );
    serviceAdminTheaterEJB.updateTheater( theater );
  }

  /**
   * Method that tests the procedure of update a Theater with income settings in database.
   */
  @Test
  public void testUpdateTheaterWithIncomeSettings()
  {
    List<IncomeSettingsTO> incomeSettingsList = new ArrayList<IncomeSettingsTO>();
    TheaterTO theater = createTheater();
    theater.setId( 1L );
    // Income settings
    IncomeSettingsTypeTO incomeSettingsType = this.incomeSettingsTypeDAO.get( 1 );
    IncomeSettingsTO incomeSettingsTOType1 = new IncomeSettingsTO();
    incomeSettingsTOType1.setId( null );
    incomeSettingsTOType1.setIdTheater( 1L );
    incomeSettingsTOType1.setIncomeSettingsType( incomeSettingsType );
    incomeSettingsTOType1.setGreenSemaphore( 85.55 );
    incomeSettingsTOType1.setYellowSemaphore( 40.09 );
    incomeSettingsTOType1.setRedSemaphore( 38.99 );
    incomeSettingsTOType1.setTimestamp( new Date() );
    incomeSettingsTOType1.setUserId( 1L );
    incomeSettingsList.add( incomeSettingsTOType1 );
    theater.setIncomeSettingsList( incomeSettingsList );
    this.serviceAdminTheaterEJB.updateTheater( theater );
  }

  @Test
  public void testUpdateTheater_inexistent()
  {
    TheaterTO theater = createTheater();
    theater.setId( 16L );
    try
    {
      serviceAdminTheaterEJB.updateTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  @Test
  public void testUpdateTheater_IsNull()
  {
    try
    {
      serviceAdminTheaterEJB.updateTheater( null );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  @Test
  public void testUpdateTheater_RegionIsNull()
  {
    try
    {
      CatalogTO territory = new CatalogTO();
      territory.setId( 1L );
      TheaterTO theater = createTheater();
      theater.setId( 1L );
      RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( null, territory );
      theater.setRegion( regionTO );
      serviceAdminTheaterEJB.updateTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  @Test
  public void testUpdateTheater_CityNull()
  {
    try
    {
      TheaterTO theater = createTheater();
      theater.setId( 1L );
      theater.setCity( null );
      serviceAdminTheaterEJB.updateTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  @Test
  public void testUpdateTheater_StateNull()
  {
    try
    {
      TheaterTO theater = createTheater();
      theater.setId( 1L );
      theater.setState( null );
      serviceAdminTheaterEJB.updateTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /*****
   * 
   */
  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testUpdateTheater_ScreensAreNull()
  {
    try
    {
      TheaterTO theater = createTheater();
      theater.setId( 1L );
      theater.setScreens( null );
      serviceAdminTheaterEJB.updateTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testUpdateTheater_InvalidRegion()
  {
    try
    {
      CatalogTO region = new CatalogTO();
      region.setId( 100L );
      TheaterTO theater = createTheater();
      theater.setId( 1L );
      CatalogTO territory = new CatalogTO();
      territory.setId( 100L );
      RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( region, territory );
      theater.setRegion( regionTO );
      serviceAdminTheaterEJB.updateTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testUpdateTheater_InvalidCity()
  {
    try
    {
      CatalogTO city = new CatalogTO();
      city.setId( 100L );
      TheaterTO theater = createTheater();
      theater.setId( 1L );
      theater.setCity( city );
      serviceAdminTheaterEJB.updateTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testUpdateTheater_InvalidState()
  {
    try
    {
      CatalogTO stateTO = new CatalogTO();
      stateTO.setId( 100L );
      TheaterTO theater = createTheater();
      theater.setId( 1L );
      StateTO<CatalogTO, Integer> state = new StateTO<CatalogTO, Integer>( stateTO, 100 );
      theater.setState( state );
      serviceAdminTheaterEJB.updateTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testUpdateTheater_InvalidScreens()
  {
    try
    {
      List<ScreenTO> screens = new ArrayList<ScreenTO>();
      ScreenTO screenTO = new ScreenTO();
      screenTO.setName( "Screen 1" );
      screenTO.setTimestamp( timestamp );
      screenTO.setUserId( userID );
      screenTO.setUsername( userName );
      screenTO.setNuCapacity( null );
      screens.add( screenTO );

      TheaterTO theater = createTheater();
      theater.setId( 1L );
      theater.setScreens( screens );
      serviceAdminTheaterEJB.updateTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testUpdateTheater_InvalidScreens_MovieFormatsIsNull()
  {
    try
    {
      List<ScreenTO> screens = new ArrayList<ScreenTO>();
      ScreenTO screenTO = new ScreenTO();
      screenTO.setName( "Screen 1" );
      screenTO.setTimestamp( timestamp );
      screenTO.setUserId( userID );
      screenTO.setUsername( userName );
      screenTO.setNuCapacity( null );
      screenTO.setNuScreen( 1 );
      screenTO.setMovieFormats( null );
      screens.add( screenTO );

      TheaterTO theater = createTheater();
      theater.setId( 1L );
      theater.setScreens( screens );
      serviceAdminTheaterEJB.updateTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  /**
   * Prueba unitaria metodo guardar el cine, fallida
   */
  @Test
  public void testUpdateTheater_InvalidScreens_SoundFormatsIsNull()
  {
    try
    {
      List<ScreenTO> screens = new ArrayList<ScreenTO>();
      ScreenTO screenTO = new ScreenTO();
      screenTO.setName( "Screen 1" );
      screenTO.setTimestamp( timestamp );
      screenTO.setUserId( userID );
      screenTO.setUsername( userName );
      screenTO.setNuCapacity( null );
      screenTO.setNuScreen( 1 );
      CatalogTO movieFormats = new CatalogTO();
      movieFormats.setId( 1L );
      movieFormats.setName( "Movie Formats" );
      List<CatalogTO> movieFormatList = new ArrayList<CatalogTO>();
      movieFormatList.add( movieFormats );
      screenTO.setMovieFormats( null );
      screenTO.setSoundFormats( null );
      screens.add( screenTO );

      TheaterTO theater = createTheater();
      theater.setId( 1L );
      theater.setScreens( screens );
      serviceAdminTheaterEJB.updateTheater( theater );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( Exception ex )
    {
      logger.error( "Ocurrio un error", ex );
    }
  }

  @Test
  public void testGetAllTheaters()
  {
    List<TheaterTO> theaters = serviceAdminTheaterEJB.getAllTheaters();
    Assert.assertNotNull( theaters );
    Assert.assertEquals( CURRENT_THEATERS_SIZE.intValue(), theaters.size() );
  }

  @Test
  public void testGetCatalogTheaterSummary()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setNeedsPaging( true );
    logger.debug( "page 0" );
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 3 );
    pagingRequestTO.setLanguage( Language.SPANISH );
    // Se obtiene la pagina 1 deberia regresar 3
    PagingResponseTO<TheaterTO> pagingResponse = serviceAdminTheaterEJB.getCatalogTheaterSummary( pagingRequestTO );
    Assert.assertNotNull( pagingResponse );
    Assert.assertNotNull( pagingResponse.getElements() );
    Assert.assertEquals( 3, pagingResponse.getElements().size() );
    // Se obtiene la pagina 2, deberia regresar 1
    PagingRequestTO pagingRequestTO2 = new PagingRequestTO();
    logger.debug( "page 1" );
    pagingRequestTO2.setPage( 1 );
    pagingRequestTO2.setPageSize( 3 );
    pagingRequestTO2.setLanguage( Language.SPANISH );
    pagingResponse = serviceAdminTheaterEJB.getCatalogTheaterSummary( pagingRequestTO2 );
    Assert.assertNotNull( pagingResponse );
    Assert.assertNotNull( pagingResponse.getElements() );
    Assert.assertEquals( 3, pagingResponse.getElements().size() );
  }

  @Test
  public void testGetMyTheaters()
  {
    AbstractTO abstractTO = new AbstractTO();
    abstractTO.setUserId( 1L );
    List<TheaterTO> theaters = this.serviceAdminTheaterEJB.getMyTheaters( abstractTO );
    Assert.assertNotNull( theaters );
    Assert.assertFalse( theaters.isEmpty() );
    for( TheaterTO theater : theaters )
    {
      System.out.println( theater );
    }
  }

  @Test(expected = DigitalBookingException.class)
  public void testGetMyTheaters_invalidUser()
  {
    AbstractTO abstractTO = new AbstractTO();
    abstractTO.setUserId( 1001L );
    this.serviceAdminTheaterEJB.getMyTheaters( abstractTO );
  }
  /**
   * method for test the function that get the format of the movies
   */
  @Test 
  public void testGetMovieFormats()
  {
    List<CatalogTO>formats =this.serviceAdminTheaterEJB.getMovieFormats();
    Assert.assertNotNull( formats );
    for(CatalogTO format:formats)
    {
      Assert.assertNotNull( format );
    }
  }
  /**
   * method for test the function that get the Sound format
   */
  @Test 
  public void testGetSoundFormats()
  {
    List<CatalogTO> soundFormats =this.serviceAdminTheaterEJB.getSoundFormats();
    Assert.assertNotNull( soundFormats );
    for(CatalogTO format:soundFormats)
    {
      Assert.assertNotNull( format );
    }
  }
  /**
   * method for test the function that get the screen formats
   */
  @Test 
  public void testGetScrenFormats()
  {
    List<CatalogTO> screenFormats =this.serviceAdminTheaterEJB.getScreenFormats();
    Assert.assertNotNull( screenFormats );
    for(CatalogTO format:screenFormats)
    {
      Assert.assertNotNull( format );
    }
  }
  /**
   * method for get te theater for region
   */
  @Test 
  public void testGetTheatersByRegion()
  {
    CatalogTO region=new CatalogTO();
    region.setFgActive( true );
    region.setId( 1L );
    region.setIdLanguage( 1L );
    region.setUserId( 1L );
    region.setUsername( " " );
    region.setTimestamp( new Date() );
    List<TheaterTO> theaters=this.serviceAdminTheaterEJB.getTheatersByRegionId( region );
    Assert.assertNotNull( theaters );
    for(TheaterTO theaterTO:theaters)
    {
      Assert.assertNotNull( theaterTO );
    }
  }
  /**
   * Realiza prueba del metod que obtien el tipo de indicador 
   * para income settings 
   */
  @Test 
  public void testGetIndicatorType()
  {
   int indicatorType=1;
   IncomeSettingsTypeTO incomeSettingsTypeTO = this.serviceAdminTheaterEJB.getIndicatorTypeById( indicatorType, Language.ENGLISH );
   Assert.assertNotNull( incomeSettingsTypeTO );
   Assert.assertEquals( "Screen Occupancy", incomeSettingsTypeTO.getName() );
   
  }
}
