package mx.com.cinepolis.digital.booking.persistence.dao.impl.migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTest_MSSQLUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.CategoryDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.DistributorDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.EventDAOImpl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class MovieLoad extends AbstractDBEJBTest_MSSQLUnit
{

  private EventDAO eventDAO;
  private DistributorDAO distributorDAO;
  private CategoryDAO categoryDAO;

  public void setUp()
  {
    // instanciar el servicio
    eventDAO = new EventDAOImpl();
    distributorDAO = new DistributorDAOImpl();
    categoryDAO = new CategoryDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( eventDAO );
    connect( distributorDAO );
    connect( categoryDAO );
  }

  @Test
  public void test() throws SQLException
  {
    System.out.println( "Movies ---->" + this.eventDAO.count() );
    
    CategoryDO sound = this.categoryDAO.find( 1 );
    CategoryDO movie = this.categoryDAO.find( 7 );

    Connection conn = this.getConnectionDBS();
    Assert.assertNotNull( conn );
    
    DistributorTO defaultDistributor = new DistributorTO();
    defaultDistributor.setId( -1L );
    
    PreparedStatement ps = conn.prepareStatement( "select distinct tp.* from tblDBSTheaterScreens tts inner join tblProjects tp on tp.ProjectID = tts.FilmID where 1 = 1 and tts.Accepted != -2 and tts.startDate >= '2012-01-01' AND tp.ProjectID > 196985" ) ;
    ResultSet rs =  ps.executeQuery();
    while(rs.next()){
      

      
      EventMovieTO event = new EventMovieTO();
      event.setCodeDBS( rs.getString( "ProjectID" ) );
      event.setDsEventName( StringUtils.abbreviate( rs.getString( "Title" ), 160 ) );
      
      event.setIdVista( "0" );
      event.setMovieFormats( Arrays.asList( new CatalogTO( movie.getIdCategory().longValue() ) ) );
      event.setPremiere( false );
      event.setQtCopy( 1 );
      event.setSoundFormats( Arrays.asList( new CatalogTO(sound.getIdCategory().longValue()) ) );
      event.setDistributor( defaultDistributor);
      event.setDsGenre( "" );
      event.setDsScript( "" );
      event.setDsActor( "" );
      event.setDsCountry( "" );
      event.setDsDirector( "" );
      event.setDsOriginalName( StringUtils.abbreviate(rs.getString( "ShortTitle" ), 160 ));
      event.setDsRating( rs.getString( "Rating" ) );
      event.setDsScript( "" );
      event.setDsSynopsis( "" );
      event.setDsUrl( "" );
      event.setDtRelease( rs.getDate( "ProjectDateAdded" ) );
      event.setDuration( rs.getInt( "RunningTimeHour" ) * 60 + rs.getInt( "RunningTimeMinute" ) );
      event.setExhibitionWeeks( 1 );
      event.setIdEventMovie( null );
      event.setUserId( 1L );
      event.setTimestamp( new Date() );
      System.out.println(ToStringBuilder.reflectionToString( event ));
      this.eventDAO.save( event, 1L );
    }


  }

  private Connection getConnectionDBS() throws SQLException
  {

    Connection conn = null;
    Properties connectionProps = new Properties();
    //connectionProps.put( "user", "sa" );
    //connectionProps.put( "password", "edtdcci77" );
    connectionProps.put( "user", "DBS-MX" );
    connectionProps.put( "password", "jmdrrc" );

    conn = DriverManager.getConnection( "jdbc:jtds:sqlserver://10.20.20.31:1433;databaseName=DBS-MX", connectionProps );

    System.out.println( "Connected to database DBS-MX" );
    return conn;
  }

}
