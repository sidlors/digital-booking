package mx.com.cinepolis.digital.booking;


import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LoadIncomeTest
{
  @Before
  public void setUp()
  {

  }

 /**
  * Method for test load of incomes
  * @throws InstantiationException
  * @throws IllegalAccessException
  * @throws ClassNotFoundException
  * @throws SQLException
  */
  @Test
  public void loadRecords() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
  {
    
    Driver driver = (Driver) Class.forName( "net.sourceforge.jtds.jdbc.Driver" ).newInstance();
    DriverManager.registerDriver( driver );
    StringBuilder sb = connectionSqlServer( );
    Connection conn = null;
    Properties conectionProperties = new Properties();
    conectionProperties.put( "user", "cinepolis");
    conectionProperties.put( "password", "56789" );
    conn = DriverManager.getConnection( sb.toString(), conectionProperties );
    conn.setAutoCommit( false );
    Statement st = conn.createStatement();
    String query = "SELECT COUNT(*) AS TOTAL FROM K_BOOKING_INCOME";
    ResultSet rs = st.executeQuery( query );
    Assert.assertNotNull( rs );
    Assert.assertTrue( rs.next() );
    //Assert.assertTrue( rs.getInt("TOTAL") ==0);
    AutomaticLoadIncomes obj = new AutomaticLoadIncomes();
    obj.executeRun();
    rs = st.executeQuery( query );
    Assert.assertNotNull( rs );
    Assert.assertTrue( rs.next() );
    int totalRecords= rs.getInt("TOTAL");
    Assert.assertTrue( totalRecords!=0);
    System.out.println(":::::::::::::_-------------"+totalRecords+"---------------::::::::::::");
    
    
  }
  /**
   * Method for delete the historic of the record of the table K_BOOKING_INCOME
   * this test is valid only in the Sunday because the process of removal is executed the Sunday at 09:00 AM 
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  @Test 
  public void deleteHistoricRecords_Test() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
  {
    Driver driver = (Driver) Class.forName( "net.sourceforge.jtds.jdbc.Driver" ).newInstance();
    DriverManager.registerDriver( driver );
    StringBuilder sb = connectionSqlServer( );
    Connection conn = null;
    Properties conectionProperties = new Properties();
    conectionProperties.put( "user", "cinepolis");
    conectionProperties.put( "password", "56789" );
    conn = DriverManager.getConnection( sb.toString(), conectionProperties );
    conn.setAutoCommit( false );
    Statement st = conn.createStatement();
    String query = "SELECT COUNT(*) AS TOTAL FROM K_BOOKING_INCOME";
    ResultSet rs = st.executeQuery( query );
    Assert.assertNotNull( rs );
    Assert.assertTrue( rs.next() );
    int records=rs.getInt("TOTAL");
    AutomaticLoadIncomes obj = new AutomaticLoadIncomes();
    obj.executeRun();
    rs = st.executeQuery( query );
    Assert.assertNotNull( rs );
    Assert.assertTrue( rs.next() );
    int totalRecords= rs.getInt("TOTAL");
    System.out.println(":::::::::::::_-------------"+records+"--"+totalRecords+"---------------::::::::::::");
    Assert.assertNotEquals( records, totalRecords );
    
  }
  /**
   * Method for update incomes 
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws ClassNotFoundException
   * @throws SQLException
   */
  @Test 
  public void updateRecords() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException
  {
    Driver driver = (Driver) Class.forName( "net.sourceforge.jtds.jdbc.Driver" ).newInstance();
    DriverManager.registerDriver( driver );
    StringBuilder sb = connectionSqlServer( );
    Connection conn = null;
    Properties conectionProperties = new Properties();
    conectionProperties.put( "user", "cinepolis");
    conectionProperties.put( "password", "56789" );
    conn = DriverManager.getConnection( sb.toString(), conectionProperties );
    conn.setAutoCommit( false );
    Statement st = conn.createStatement();
    String query = "SELECT COUNT(*) AS TOTAL FROM K_BOOKING_INCOME WHERE FG_ACTIVE = 0 ";
    ResultSet rs = st.executeQuery( query );
    Assert.assertNotNull( rs );
    Assert.assertTrue( rs.next() );
    int records=rs.getInt("TOTAL");
    AutomaticLoadIncomes obj = new AutomaticLoadIncomes();
    obj.executeRun();
    rs = st.executeQuery( query );
    Assert.assertNotNull( rs );
    Assert.assertTrue( rs.next() );
    int totalRecords= rs.getInt("TOTAL");
    System.out.println(":::::::::::::_-------------"+records+"--"+totalRecords+"---------------::::::::::::");
    Assert.assertNotEquals( records, totalRecords );
    
  }
  /**
   * method for get the url connection
   * @return
   */
  private static StringBuilder connectionSqlServer( )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "jdbc:jtds:sqlserver://" );
    sb.append("192.168.0.25"  );
    sb.append( ":1433");
    sb.append( ";DatabaseName=DIGITAL_BOOKING_SP2" );
    return sb;
  }
}
