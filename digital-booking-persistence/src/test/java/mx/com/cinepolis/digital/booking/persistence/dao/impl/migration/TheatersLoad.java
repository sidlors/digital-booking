package mx.com.cinepolis.digital.booking.persistence.dao.impl.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.utils.AbstractTOUtils;
import mx.com.cinepolis.digital.booking.model.CityDO;
import mx.com.cinepolis.digital.booking.model.CityLanguageDO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTest_MSSQLUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.CityDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.CityDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.ScreenDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.TheaterDAOImpl;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class TheatersLoad extends AbstractDBEJBTest_MSSQLUnit
{

  private TheaterDAO theaterDAO;
  private ScreenDAO screenDAO;
  private CityDAO cityDAO;

  public void setUp()
  {
    // instanciar el servicio
    theaterDAO = new TheaterDAOImpl();
    screenDAO = new ScreenDAOImpl();
    cityDAO = new CityDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( theaterDAO );
    connect( screenDAO );
    connect( cityDAO );
  }

  @Test
  public void test()
  {
    List<Data> data = new ArrayList<Data>();
    

    for( Data d : data )
    {
      CityDO cityDO = getCity( d.getCityName() );

      TheaterTO theaterTO = new TheaterTO();
      theaterTO.setUserId( 1L );
      theaterTO.setTimestamp( new Date() );
      theaterTO.setCity( new CatalogTO( cityDO.getIdCity().longValue() ) );
      theaterTO.setDsTelephone( "X" );
      theaterTO.setName( d.getTheaterName() );
      theaterTO.setRegion( new RegionTO<CatalogTO, CatalogTO>( new CatalogTO( Long.valueOf( d.getRegionId() ) ),
          new CatalogTO( 1L ) ) );
      theaterTO.setState( new StateTO<CatalogTO, Integer>( new CatalogTO( Long.valueOf( d.getStateId() ) ), 1 ) );
      theaterTO.setScreens( new ArrayList<ScreenTO>() );
      this.theaterDAO.save( theaterTO );
      this.theaterDAO.flush();
      for( int i = 0; i < d.getScreens(); i++ )
      {
        ScreenTO screen = new ScreenTO();
        AbstractTOUtils.copyElectronicSign( theaterTO, screen );
        screen.setIdTheater( theaterTO.getId().intValue() );
        screen.setNuCapacity( 1 );
        screen.setNuScreen( i + 1 );
        screen.setMovieFormats( new ArrayList<CatalogTO>() );
        screen.setSoundFormats( new ArrayList<CatalogTO>() );
        this.screenDAO.save( screen );
      }

    }

  }

  private CityDO getCity( String cityName )
  {
    List<CityDO> cities = this.cityDAO.findAll();
    CityDO cityDO = null;
    for( CityDO c : cities )
    {
      for( CityLanguageDO cl : c.getCityLanguageDOList() )
      {
        if( cl.getDsName().equals( cityName ) )
        {
          cityDO = c;
          break;
        }
      }
    }
    if( cityDO == null )
    {
      cityDO = new CityDO();
      cityDO.setDtLastModification( new Date() );
      cityDO.setFgActive( true );
      cityDO.setIdLastUserModifier( 1 );

      CityLanguageDO cl = new CityLanguageDO();
      cl.setDsName( cityName );
      cl.setIdCity( cityDO );
      cl.setIdLanguage( new LanguageDO( 1 ) );
      cityDO.setCityLanguageDOList( Arrays.asList( cl ) );
      this.cityDAO.create( cityDO );
      this.cityDAO.flush();
    }

    return cityDO;
  }

  class Data
  {

    private int regionId;
    private String cityName;
    private String theaterName;
    private int screens;
    private int stateId;

    public Data()
    {
    }

    public Data( int regionId, String cityName, String theaterName, int screens, int stateId )
    {
      this.regionId = regionId;
      this.cityName = cityName;
      this.theaterName = theaterName;
      this.screens = screens;
      this.stateId = stateId;
    }

    /**
     * @return the regionId
     */
    public int getRegionId()
    {
      return regionId;
    }

    /**
     * @param regionId the regionId to set
     */
    public void setRegionId( int regionId )
    {
      this.regionId = regionId;
    }

    /**
     * @return the cityName
     */
    public String getCityName()
    {
      return cityName;
    }

    /**
     * @param cityName the cityName to set
     */
    public void setCityName( String cityName )
    {
      this.cityName = cityName;
    }

    /**
     * @return the theaterName
     */
    public String getTheaterName()
    {
      return theaterName;
    }

    /**
     * @param theaterName the theaterName to set
     */
    public void setTheaterName( String theaterName )
    {
      this.theaterName = theaterName;
    }

    /**
     * @return the screens
     */
    public int getScreens()
    {
      return screens;
    }

    /**
     * @param screens the screens to set
     */
    public void setScreens( int screens )
    {
      this.screens = screens;
    }

    /**
     * @return the stateId
     */
    public int getStateId()
    {
      return stateId;
    }

    /**
     * @param stateId the stateId to set
     */
    public void setStateId( int stateId )
    {
      this.stateId = stateId;
    }

  }

}
