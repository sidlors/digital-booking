package mx.com.cinepolis.digital.booking;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutomaticDeleteIncomes
{
  private static final Logger LOG = LoggerFactory.getLogger( AutomaticDeleteIncomes.class );
  
  public Properties getProperties()
  {
    Properties prop = new Properties();
    try
    {
      String pathProperties = "/data/IncomesDigitalBooking/connectionSQLServer.properties";
      prop.load( new FileInputStream( pathProperties ) );
      Driver driver = (Driver) Class.forName( "net.sourceforge.jtds.jdbc.Driver" ).newInstance();
      DriverManager.registerDriver( driver );
    }
    catch( IOException e )
    {
      LOG.error( e.toString() );
    }
    catch( InstantiationException e )
    {
      LOG.error( e.toString() );
    }
    catch( IllegalAccessException e )
    {
      LOG.error( e.toString() );
    }
    catch( ClassNotFoundException e )
    {
      LOG.error( e.toString() );
    }
    catch( SQLException e )
    {
      LOG.error( e.toString() );
    }
    return prop;
  }
  public void deleteIncomes(Connection conn)
  {
    try
    {
      LOG.info( ":::iniciamos eliminación de ingresos:::" );
      deleteHistoricRecord(conn);
      LOG.info( ":::Termina  eliminación de ingresos:::" );
    }
    catch( SQLException e )
    {
      LOG.error( e.toString() );
    }
  }
  
  public Connection getConnectionDelete(StringBuilder connSQLServer,Properties propConn)
  {
    Connection conn = null;
    Properties conectionProperties = new Properties();
    conectionProperties.put( "user", propConn.getProperty( "user" ) );
    conectionProperties.put( "password", propConn.getProperty( "password" ) );
    try
    {
      conn = DriverManager.getConnection( connSQLServer.toString(), conectionProperties );
      conn.setAutoCommit( false );
      LOG.info( ":::Conectado a " + connSQLServer.toString() + "::::para borrar datos" );
    }
    catch( SQLException e )
    {
      LOG.error( e.toString() );
    }
    
    
    return conn;
  } 
  /**
   * This method erase the records for delete 
   * 
   * @param rs
   * @param st
   * @throws SQLException
   * @throws ParseException
   */
  private static void deleteHistoricRecord( Connection conn ) throws SQLException
  {
    Date dt = new Date();
    if( getDayOfTheWeek( dt ) == Integer.parseInt( getStartDayWeek( conn ) ) )
    {
      Calendar calt = Calendar.getInstance();
      StringBuilder sbQueryWeek = new StringBuilder();
      sbQueryWeek.append( "SELECT * FROM C_WEEK  WHERE '" );
      sbQueryWeek.append( formatDate( calt ) );
      sbQueryWeek.append( "' BETWEEN DT_STARTING_DAY_WEEK AND  DT_FINAL_DAY_WEEK AND FG_ACTIVE =1;" );
      LOG.info( ":::::::Query obtine la semana ->" + sbQueryWeek.toString() );
      PreparedStatement stw = conn.prepareStatement( sbQueryWeek.toString() );
      ResultSet rs = stw.executeQuery();
      Date startDay = new Date();
      if( rs != null )
      {
        if( rs.next() )
        {
          startDay = rs.getDate( "DT_STARTING_DAY_WEEK" );
        }
      }
      calt.setTime( startDay );

      calt.add( Calendar.DATE, -7 );
      StringBuilder delRecords = new StringBuilder();
      String dateFormated = formatDate( calt );
      delRecords.append( "DELETE FROM K_BOOKING_INCOME WHERE DT_SHOW < '" );
      delRecords.append( dateFormated ).append( "';" );
      LOG.info( "::Query para Borrar los Registros::-->" + delRecords.toString() );
      PreparedStatement st = conn.prepareStatement( delRecords.toString() );
      st.executeUpdate();
    }
  }
  /**
   * method for get the number of the day in the week
   * 
   * @param d
   * @return
   */
  public static int getDayOfTheWeek( Date d )
  {
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime( d );
    return cal.get( Calendar.DAY_OF_WEEK );
  }
  /**
   * Method for get the parameter of configuration of the start day of the week
   * 
   * @param conn
   * @return
   * @throws SQLException
   */
  private static String getStartDayWeek( Connection conn ) throws SQLException
  {
    String day = null;
    StringBuilder sbQuery = new StringBuilder();
    sbQuery.append( "SELECT DS_VALUE FROM W_CONFIGURATION WHERE DS_PARAMETER = 'WEEK_START_DAY';" );
    LOG.info( ":::Query inicio de semana ::--> ::" + sbQuery.toString() );
    PreparedStatement st = conn.prepareStatement( sbQuery.toString() );
    ResultSet rs = st.executeQuery();
    if( rs != null )
    {
      if( rs.next() )
      {
        day = rs.getString( 1 );
      }
    }
    return day;
  }
  /**
   * method for format the date in string
   * 
   * @param cal
   * @return
   */
  private static String formatDate( Calendar cal )
  {
    StringBuilder sbDate = new StringBuilder();
    int v = cal.get( Calendar.DAY_OF_MONTH );
    sbDate.append( cal.get( Calendar.YEAR ) ).append( "-" );
    if( cal.get( Calendar.MONTH ) + 1 < 10 )
    {
      sbDate.append( "0" ).append( cal.get( Calendar.MONTH ) + 1 ).append( "-" );
    }
    else
    {
      sbDate.append( cal.get( Calendar.MONTH ) + 1 ).append( "-" );
    }
    if( v < 10 )
    {
      sbDate.append( "0" ).append( v );
    }
    else
    {
      sbDate.append( v );
    }
    return sbDate.toString();
  }

}
