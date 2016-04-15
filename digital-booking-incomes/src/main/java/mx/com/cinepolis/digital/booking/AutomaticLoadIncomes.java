/*
 * Class for extract information from DB Vista , procces the information and load the information in DB Cinema Booker
 */
package mx.com.cinepolis.digital.booking;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jcarbajal
 */
public class AutomaticLoadIncomes
{
  private static final Logger LOG = LoggerFactory.getLogger( AutomaticLoadIncomes.class );

  HashMap<Integer, String> events = new HashMap<Integer, String>();
  HashMap<Integer, String> theaters = new HashMap<Integer, String>();
  HashMap<String, String> screens = new HashMap<String, String>();
  List<Integer> idVistaTheaters = new ArrayList<Integer>();

  public static void main( String[] args )
  {
    AutomaticLoadIncomes objLoad = new AutomaticLoadIncomes();
    Calendar timeDelete = Calendar.getInstance();
    if( timeDelete.get( Calendar.HOUR_OF_DAY ) == 5 )
    {
      AutomaticDeleteIncomes objDelete = new AutomaticDeleteIncomes();
      StringBuilder sbConn = connectionSqlServer( objDelete.getProperties() );
      objDelete.deleteIncomes( objDelete.getConnectionDelete( sbConn, objDelete.getProperties() ) );
    }
    else
    {
      objLoad.runLoad();
    }
  }

  public void executeRun()
  {
    runLoad();
  }

  /**
   * method for load incomes
   */
  private void runLoad()
  {

    try
    {
      LOG.info( "Iniciando la carga  de registros" );
     // String pathProperties = "/data/IncomesDigitalBooking/connectionSQLServer.properties";// concatenamos ruta donde se
                                                                                           // ejcuta el jar
      Properties prop = new Properties();
      //prop.load( new FileInputStream( pathProperties ) );
      prop.load(ClassLoader.getSystemClassLoader().getResourceAsStream( "connectionSQLServer.properties" )  );
      Driver driver = (Driver) Class.forName( "net.sourceforge.jtds.jdbc.Driver" ).newInstance();
      DriverManager.registerDriver( driver );

      Properties propSybase = new Properties();
      propSybase.load( ClassLoader.getSystemClassLoader().getResourceAsStream( "connectionSybase.properties" ) );
      //Driver driverSybase = (Driver) Class.forName( "com.sybase.jdbc3.jdbc.SybDriver" ).newInstance();
      Driver driverSybase = (Driver) Class.forName( "net.sourceforge.jtds.jdbc.Driver" ).newInstance();
      DriverManager.registerDriver( driverSybase );
      StringBuilder sb = connectionSqlServer( prop );
      Connection conn = null;
      Properties conectionProperties = new Properties();
      conectionProperties.put( "user", prop.getProperty( "user" ) );
      conectionProperties.put( "password", prop.getProperty( "password" ) );
      LOG.info( ":::Conectando a " + sb.toString() + "::::"+":::"+conectionProperties.toString() );
      conn = DriverManager.getConnection( sb.toString(), conectionProperties );
      conn.setAutoCommit( false );
      LOG.info( ":::Conectado a " + sb.toString() + "::::" );
      Statement st = conn.createStatement();

      PreparedStatement pst = conn.prepareStatement( getQueryBooking() );
      PreparedStatement pstscreen = conn.prepareStatement( getQueryScreen() );
      PreparedStatement pstInsert = conn.prepareStatement( getQueryInsert() );
      PreparedStatement pstInsert2 = conn.prepareStatement( getQueryInsert2() );
      PreparedStatement pstGetEvent = conn.prepareStatement( "SELECT ID_EVENT FROM K_EVENT WHERE ID_VISTA = ?" );
      PreparedStatement pstGetTheater = conn.prepareStatement( "SELECT ID_THEATER FROM C_THEATER WHERE ID_VISTA=?" );
      StringBuilder sbs = connectionSybase( propSybase );
      Connection connSyb = null;
      Properties conectionPropertiesSyb = new Properties();
      conectionPropertiesSyb.put( "user", propSybase.getProperty( "user" ) );
      conectionPropertiesSyb.put( "password", propSybase.getProperty( "password" ) );
      connSyb = DriverManager.getConnection( sbs.toString(), conectionPropertiesSyb );
      connSyb.setAutoCommit( false );
      Statement stSyb = connSyb.createStatement();
      LOG.info( ":::Conectado a " + sbs.toString() + "::::" );
      List<String> dates = datesOfWeekToConsult();// obtenemos los dias que vamos a consultar para validar si ya se
      LOG.info( ":::Conectado a " + dates.toString() + "::::" );                                           // insertaron ingresos
      for( String date : dates )
      {
        String query = getQueryTheaters( date );
        ResultSet rs = st.executeQuery( query );
        if( rs != null )
        {
          while( rs.next() )
          {
            idVistaTheaters.add( Integer.parseInt( rs.getString( "ID_VISTA" ) ) );
          }
        }
        LOG.info( "::Query obtine ids " + query + "::::numero de cines " + this.idVistaTheaters.size()
            + " cargados en " + date );
        LOG.info( "::Ids de los cines:" + this.idVistaTheaters.toString() );
        String querySyb = getQueryIncomes( this.idVistaTheaters, date );
        LOG.info( "::Query:" + querySyb );
        ResultSet rsSyb = stSyb.executeQuery( querySyb );
        int idVistaComplejo = -1;
        int idVistaPelicula = -1;
        boolean flagWeek = true;
        int idWeek = -1;
        int idTheater = -1;
        int idScreen = -1;
        int idBooking = -1;
        int idEvent = -1;

        if( rsSyb != null )
        {
          while( rsSyb.next() )
          {
            if( flagWeek )
            {
              idWeek = getIdWeek( rs, st, rsSyb.getString( "fechaFuncion" ) );// calculamos la semana de los ingresos
              flagWeek = false;
            }
            idVistaComplejo = rsSyb.getInt( "idVistaComplejo" );
            idVistaPelicula = rsSyb.getInt( "idVistaPelicula" );
            pst.setInt( 1, idWeek );
            pst.setInt( 2, idWeek );
            pst.setInt( 3, 1 );
            pst.setInt( 4, 1 );
            pst.setInt( 5, 3 );
            pst.setInt( 6, 3 );
            pst.setInt( 7, idVistaComplejo );
            pst.setInt( 8, idVistaPelicula );
            rs = pst.executeQuery();
            if( rs.next() )
            {
              idTheater = rs.getInt( "ID_THEATER" );
              idBooking = rs.getInt( "ID_BOOKING" );
              idEvent = rs.getInt( "ID_EVENT" );
              idScreen = getIdScreen( rs, pstscreen, rsSyb.getInt( "numeroSalaComplejo" ), idTheater );// Se Calcula el
                                                                                                       // idScreen
            }
            if( idWeek != -1 && idTheater != -1 && idScreen != -1 && idEvent != -1 )
            {
              pstInsert.setLong( 1, idBooking );// id del booking
              pstInsert.setInt( 2, idScreen );// id del screen
              pstInsert.setInt( 3, idWeek );// id de la semana
              pstInsert.setInt( 4, idTheater );
              pstInsert.setInt( 5, idEvent );
              pstInsert.setString( 6, rsSyb.getString( "fechaFuncion" ) );// fecha funcion
              pstInsert.setString( 7, rsSyb.getString( "horaFuncion" ).concat( ":00" ) );// hora del evento
              pstInsert.setBigDecimal( 8, rsSyb.getBigDecimal( "ingresos" ) );// ingresos en efectivo
              pstInsert.setLong( 9, rsSyb.getLong( "numeroBoletos" ) );// tickets
              pstInsert.setBoolean( 10, true );// bandera para el registro activo
              pstInsert.setTimestamp( 11, getCurrentTimeStamp() );
              pstInsert.setInt( 12, 1 );// usuario modificador
              pstInsert.execute();
              conn.commit();
              idTheater = -1;
              idScreen = -1;
              idBooking = -1;
            }
            else
            {// obtener idTheater , idEvent, idScreen
              idEvent = getIdEvent( rs, pstGetEvent, idVistaPelicula );
              idTheater = getIdTheater( rs, pstGetTheater, idVistaComplejo );
              idScreen = getIdScreen( rs, pstscreen, rsSyb.getInt( "numeroSalaComplejo" ), idTheater );// Se Calcul el
                                                                                                       // idScreen
              if( idEvent == -1 )
              {
                if( !events.containsKey( idVistaPelicula ) )
                {
                  events.put( idVistaPelicula, rsSyb.getString( "nombrePelicula" ) );
                }
              }
              if( idTheater == -1 )
              {
                if( !theaters.containsKey( idVistaComplejo ) )
                {
                  theaters.put( idVistaPelicula, rsSyb.getString( "nombreComplejo" ) );
                }
              }
              if( theaters.containsKey( idVistaComplejo ) && idScreen == -1 )
              {
                if( !screens.containsKey( String.valueOf( idVistaComplejo )
                    + String.valueOf( rsSyb.getInt( "numeroSalaComplejo" ) ) ) )
                  screens.put(
                    String.valueOf( idVistaComplejo ) + String.valueOf( rsSyb.getInt( "numeroSalaComplejo" ) ),
                    "don´t exist screen:" + String.valueOf( rsSyb.getInt( "numeroSalaComplejo" ) ) );
              }
              if( idEvent != -1 && idTheater != -1 && idScreen != -1 )
              {
                // pstInsert.setLong( 1, (Long) null );// id del booking
                pstInsert2.setInt( 1, idScreen );// id del screen
                pstInsert2.setInt( 2, idWeek );// id de la semana
                pstInsert2.setInt( 3, idTheater );
                pstInsert2.setInt( 4, idEvent );
                pstInsert2.setString( 5, rsSyb.getString( "fechaFuncion" ) );// fecha funcion
                pstInsert2.setString( 6, rsSyb.getString( "horaFuncion" ).concat( ":00" ) );// hora del evento
                pstInsert2.setBigDecimal( 7, rsSyb.getBigDecimal( "ingresos" ) );// ingresos en efectivo
                pstInsert2.setLong( 8, rsSyb.getLong( "numeroBoletos" ) );// tickets
                pstInsert2.setBoolean( 9, true );// bandera para el registro activo
                pstInsert2.setTimestamp( 10, getCurrentTimeStamp() );
                pstInsert2.setInt( 11, 1 );// usuario modificador
                pstInsert2.execute();
                conn.commit();
                idTheater = -1;
                idScreen = -1;
                idEvent = -1;
              }
            }
          }
        }
        rs.close();
        rsSyb.close();
        this.idVistaTheaters.clear();
      }
      pst.close();
      pstscreen.close();
      pstInsert.close();
      pstInsert2.close();
      pstGetEvent.close();
      pstGetTheater.close();
      conn.close();
      connSyb.close();
      printInvalidEvents( events );
      printInvalidTheaters( theaters );
      printInvalidScreens( screens );
      LOG.info( ":: operation succeful ::" );
    }
    catch( IOException ex )
    {
      LOG.error( ex.getMessage(), ex );
    }
    catch( SQLException e )
    {
      LOG.error( e.getMessage(), e );
    }
    catch( ClassNotFoundException e )
    {
      LOG.error( e.getMessage(), e );
    }
    catch( InstantiationException e )
    {
      LOG.error( e.getMessage(), e );
    }
    catch( IllegalAccessException e )
    {
      LOG.error( e.getMessage(), e );
    }

  }

  /**
 * 
 */
  private static String getQueryIncomes( List<Integer> idVistaTheaters, String date )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "SELECT * FROM RPT.V_INGRESOS_TAQUILLA_DIGITAL_BOOKING " );
    if( idVistaTheaters.size() > 0 )
    {
      sb.append( " WHERE ( " );
      for( int idTheater = 0; idTheater < idVistaTheaters.size(); idTheater++ )
      {
        if( idTheater == idVistaTheaters.size() - 1 )
        {
          sb.append( "idVistaComplejo <> " ).append( idVistaTheaters.get( idTheater ) ).append( "  ) " );
        }
        else
        {
          sb.append( "idVistaComplejo <> " ).append( idVistaTheaters.get( idTheater ) ).append( " AND  " );
        }
      }
      sb.append( " AND " ).append( "fechaFuncion = '" ).append( date ).append( "' ;" );
    }
    return sb.toString();
  }

  /**
   * method for print events not found
   * 
   * @param map
   */
  private static void printInvalidEvents( HashMap<Integer, String> map )
  {
    Iterator it = map.keySet().iterator();
    LOG.info( "::: ----Events not found-------:::::" );
    while( it.hasNext() )
    {
      Integer key = (Integer) it.next();
      LOG.info( " id Vista Pelicula :" + key + " nombre Pelicula:" + map.get( key ) );
    }
    LOG.info( "::::::::::::::::::::::::::::::::::::::" );
    LOG.info( "::: -----End of Events-----------:::::" );
  }

  /**
   * method for print theater not found
   * 
   * @param map
   */
  private static void printInvalidTheaters( HashMap<Integer, String> map )
  {
    Iterator it = map.keySet().iterator();
    LOG.info( "::: ----Theaters not found-------:::::" );
    while( it.hasNext() )
    {
      Integer key = (Integer) it.next();
      LOG.info( " id Vista Theater :" + key + " nombre Theater:" + map.get( key ) );
    }
    LOG.info( "::::::::::::::::::::::::::::::::::::::" );
    LOG.info( "::: -----End of Thaters-----------:::::" );
  }

  /**
   * method for print screens not found
   * 
   * @param map
   */
  private static void printInvalidScreens( HashMap<String, String> map )
  {
    Iterator it = map.keySet().iterator();
    LOG.info( "::: ----Screens not found-------:::::" );
    while( it.hasNext() )
    {
      String key = (String) it.next();
      LOG.info( " nímero de sala :" + key + " información  adicional " + map.get( key ) );
    }
    LOG.info( "::::::::::::::::::::::::::::::::::::::" );
    LOG.info( "::: -----End of Screens-----------:::::" );
  }

  /**
   * method for get the url for the connection
   * 
   * @param prop
   * @return
   */
  private static StringBuilder connectionSqlServer( Properties prop )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "jdbc:jtds:sqlserver://" );
    sb.append( prop.getProperty( "host" ) );
    sb.append( ":" ).append( prop.getProperty( "port" ) );
    sb.append( ";DatabaseName=" ).append( prop.getProperty( "database" ) );
    return sb;
  }

  /**
   * method for get the url for the connection
   * 
   * @param prop
   * @return
   */
  private static StringBuilder connectionSybase( Properties prop )
  {
    StringBuilder sb = new StringBuilder();
    //sb.append( "jdbc:sybase:Tds:" );
    sb.append( "jdbc:jtds:sqlserver://" );
    sb.append( prop.getProperty( "host" ) );
    sb.append( ":" ).append( prop.getProperty( "port" ) );
    //sb.append( "//" ).append( prop.getProperty( "database" ) );
    sb.append( ";DatabaseName=" ).append( prop.getProperty( "database" ) );
    return sb;
  }

  /**
   * method for get idScreen
   * 
   * @param rs
   * @param pstScreen
   * @param numScreen
   * @param idTheater
   * @return
   * @throws SQLException
   */
  private static int getIdScreen( ResultSet rs, PreparedStatement pstScreen, int numScreen, int idTheater )
      throws SQLException
  {
    int idScreen = -1;
    pstScreen.setInt( 1, numScreen );
    pstScreen.setInt( 2, idTheater );
    rs = pstScreen.executeQuery();
    if( rs != null )
    {
      if( rs.next() )
      {
        idScreen = rs.getInt( 1 );
      }
    }
    return idScreen;
  }

  /**
   * method for get idEvent
   * 
   * @param rs
   * @param pstEvent
   * @param idVistaEvent
   * @return
   * @throws SQLException
   */
  private static int getIdEvent( ResultSet rs, PreparedStatement pstEvent, int idVistaEvent ) throws SQLException
  {
    int idEvent = -1;
    pstEvent.setString( 1, String.valueOf( idVistaEvent ) );
    rs = pstEvent.executeQuery();
    if( rs.next() )
    {
      idEvent = rs.getInt( 1 );
    }
    return idEvent;
  }

  /**
   * method for get the idTheater
   * 
   * @param rs
   * @param pstTheater
   * @param idVistaTheater
   * @return
   * @throws SQLException
   */
  private static int getIdTheater( ResultSet rs, PreparedStatement pstTheater, int idVistaTheater ) throws SQLException
  {
    int idTheater = -1;
    pstTheater.setInt( 1, idVistaTheater );
    rs = pstTheater.executeQuery();
    if( rs.next() )
    {
      idTheater = rs.getInt( 1 );
    }
    return idTheater;
  }

  /**
   * method for get the dates for consult the incomes loaded
   * 
   * @return
   */
  private static List<String> datesOfWeekToConsult()
  {
    List<String> dates = new ArrayList<String>();
    Calendar today = Calendar.getInstance();
    if( getDayOfTheWeek( today.getTime() ) == 2 )
    {
      today.add( Calendar.DATE, -1 );
      dates.add( formatDate( today ) );
      today.add( Calendar.DATE, -1 );
      dates.add( formatDate( today ) );
      today.add( Calendar.DATE, -1 );
      dates.add( formatDate( today ) );
    }
    else if( getDayOfTheWeek( today.getTime() ) == 6 )
    {
      today.add( Calendar.DATE, -1 );
      dates.add( formatDate( today ) );
      today.add( Calendar.DATE, -1 );
      dates.add( formatDate( today ) );
      today.add( Calendar.DATE, -1 );
      dates.add( formatDate( today ) );
      today.add( Calendar.DATE, -1 );
      dates.add( formatDate( today ) );
      today.add( Calendar.DATE, -1 );
      dates.add( formatDate( today ) );
      today.add( Calendar.DATE, -1 );
      dates.add( formatDate( today ) );
      today.add( Calendar.DATE, -1 );
      dates.add( formatDate( today ) );
    }
    else
    {
      today.add( Calendar.DATE, -1 );
      dates.add( formatDate( today ) );
    }
    return dates;
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
   * method for get teh id Week
   * 
   * @param rs
   * @param st
   * @param fecha
   * @return
   * @throws SQLException
   */
  private static int getIdWeek( ResultSet rs, Statement st, String fecha ) throws SQLException
  {
    int idWeek = -1;
    StringBuilder queryGetWeek = new StringBuilder();
    queryGetWeek.append( "SELECT ID_WEEK FROM C_WEEK  WHERE convert(datetime,'" ).append( fecha ).append( "', 101)" );
    queryGetWeek.append( " BETWEEN DT_STARTING_DAY_WEEK AND DT_FINAL_DAY_WEEK AND " );
    queryGetWeek.append( " FG_ACTIVE = 1 AND FG_SPECIAL_WEEK = 0 ORDER BY DT_STARTING_DAY_WEEK, DT_FINAL_DAY_WEEK" );
    rs = st.executeQuery( queryGetWeek.toString() );
    if( rs != null )
    {
      if( rs.next() )
      {
        idWeek = rs.getInt( 1 );
      }
    }
    return idWeek;
  }

  /**
   * method for insert record in K_BOOKING_INCOME
   * 
   * @return
   */
  private static String getQueryInsert()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "INSERT INTO K_BOOKING_INCOME (ID_BOOKING,ID_SCREEN,ID_WEEK,ID_THEATER,ID_EVENT" );
    sb.append( " ,DT_SHOW,HR_SHOW,QT_INCOME,QT_TICKETS,FG_ACTIVE,DT_LAST_MODIFICATION,ID_LAST_USER_MODIFIER)" );
    sb.append( " VALUES (?,?,?,?,?,convert(datetime,?, 101),?,?,?,?,convert(datetime,?,101),?)" );
    return sb.toString();
  }

  /**
   * method for insert record in K_BOOKING_INCOME
   * 
   * @return
   */
  private static String getQueryInsert2()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "INSERT INTO K_BOOKING_INCOME (ID_SCREEN,ID_WEEK,ID_THEATER,ID_EVENT" );
    sb.append( " ,DT_SHOW,HR_SHOW,QT_INCOME,QT_TICKETS,FG_ACTIVE,DT_LAST_MODIFICATION,ID_LAST_USER_MODIFIER)" );
    sb.append( " VALUES (?,?,?,?,convert(datetime,?, 101),?,?,?,?,convert(datetime,?,101),?)" );
    return sb.toString();
  }

  /**
   * method for associate incomes at booking
   * 
   * @return
   */
  private static String getQueryBooking()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "SELECT DISTINCT kb.ID_BOOKING, kb.DT_LAST_MODIFICATION, kb.FG_ACTIVE, kb.FG_BOOKED, kb.ID_LAST_USER_MODIFIER, kb.ID_BOOKING_TYPE, kb.ID_EVENT, kb.ID_THEATER " );
    sb.append( "FROM K_BOOKING kb LEFT OUTER JOIN K_BOOKING_WEEK kbw ON (kbw.ID_BOOKING = kb.ID_BOOKING) " );
    sb.append( "LEFT OUTER JOIN C_WEEK cw ON (cw.ID_WEEK = kbw.ID_WEEK) " );
    sb.append( "LEFT OUTER JOIN C_BOOKING_STATUS cbs ON (cbs.ID_BOOKING_STATUS = kbw.ID_BOOKING_STATUS) " );
    sb.append( "LEFT OUTER JOIN K_BOOKING_SPECIAL_EVENT kbse ON (kbse.ID_BOOKING = kb.ID_BOOKING) " );
    sb.append( "LEFT OUTER JOIN K_SPECIAL_EVENT_WEEK ksew ON (ksew.ID_BOOKING_SPECIAL_EVENT = kbse.ID_BOOKING_SPECIAL_EVENT) " );
    sb.append( "LEFT OUTER JOIN C_WEEK cww ON (cww.ID_WEEK = ksew.ID_WEEK) " );
    sb.append( "LEFT OUTER JOIN C_BOOKING_STATUS cbss ON (cbss.ID_BOOKING_STATUS = kbse.ID_BOOKING_STATUS),K_EVENT ke " );
    sb.append( "LEFT OUTER JOIN K_EVENT_MOVIE kem ON (kem.ID_EVENT = ke.ID_EVENT), W_LANGUAGE wl, C_THEATER_LANGUAGE ctl, C_THEATER ct " );
    sb.append( "WHERE (((((((cw.ID_WEEK = ?) OR (cww.ID_WEEK = ?)) AND ((((cbs.ID_BOOKING_STATUS = ?) OR (cbss.ID_BOOKING_STATUS = ?)) OR " );
    sb.append( "(cbs.ID_BOOKING_STATUS = ?)) OR (cbss.ID_BOOKING_STATUS = ?))) AND NOT ((kem.ID_EVENT IS NULL))) AND (ct.ID_VISTA = cast(? as nvarchar(20)))) " );
    sb.append( "AND (ke.ID_VISTA = cast(? as nvarchar(20)))) AND ((((ke.ID_EVENT = kb.ID_EVENT) AND (ct.ID_THEATER = kb.ID_THEATER)) " );
    sb.append( "AND (ctl.ID_THEATER = ct.ID_THEATER)) AND (wl.ID_LANGUAGE = ctl.ID_LANGUAGE)))" );
    return sb.toString();
  }

  /**
   * method for get the current time
   * 
   * @return
   */
  private static java.sql.Timestamp getCurrentTimeStamp()
  {
    java.util.Date today = new java.util.Date();
    return new java.sql.Timestamp( today.getTime() );
  }

  /**
   * method that return the query for identified the screen in DB Cineme Booker
   * 
   * @return
   */
  private static String getQueryScreen()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "SELECT ID_SCREEN FROM C_SCREEN cs " );
    sb.append( "JOIN C_THEATER ct " );
    sb.append( "ON cs.ID_THEATER=ct.ID_THEATER " );
    sb.append( "WHERE cs.NU_SCREEN= ? AND ct.ID_THEATER=? " );
    return sb.toString();
  }

  /**
   * method for get the id_vista of the theaters loaded in incomes table
   * 
   * @return
   */
  private static String getQueryTheaters( String date )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "SELECT DISTINCT(ct.ID_VISTA) FROM K_BOOKING_INCOME kbi" );
    sb.append( " JOIN C_THEATER ct" );
    sb.append( " ON ct.ID_THEATER = kbi.ID_THEATER" );
    sb.append( " WHERE  CAST(kbi.DT_SHOW AS DATE ) = '" );
    sb.append( date ).append( "' ;" );
    return sb.toString();
  }
}
