package mx.com.cinepolis.digital.booking.service.security.impl;

import java.util.ArrayList;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemMenuTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.service.configuration.ConfigurationServiceEJB;
import mx.com.cinepolis.digital.booking.service.configuration.impl.ConfigurationServiceEJBImpl;
import mx.com.cinepolis.digital.booking.service.security.ServiceSecurityEJB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the security services.
 * 
 * @author afuentes
 */
public class ServiceSecurityEJBTest extends AbstractDBEJBTestUnit
{
  private ServiceSecurityEJB serviceSecurityEJB;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    serviceSecurityEJB = new ServiceSecurityEJBImpl();
    ConfigurationServiceEJB configurationServiceEJB = new ConfigurationServiceEJBImpl();

    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    connect( serviceSecurityEJB );
    connect( configurationServiceEJB );

    ((ServiceSecurityEJBImpl) serviceSecurityEJB).setConfigurationServiceEJB( configurationServiceEJB );
    ((ServiceSecurityEJBImpl) serviceSecurityEJB).init();

  }

  @Test
  public void testAuthenticate_Success()
  {
    UserTO userTO = new UserTO();
    userTO.setName( "gsegura" );
    userTO.setPassword( "Cinepolis2014" );
    try
    {
      serviceSecurityEJB.authenticate( userTO );
      Assert.assertTrue( true );
    }
    catch( DigitalBookingException ex )
    {
      Assert.fail();
    }
  }

  @Test
  public void testAuthenticate_WrongPassword()
  {
    UserTO userTO = new UserTO();
    userTO.setName( "gsegura" );
    userTO.setPassword( "wrongPassword" );
    try
    {
      serviceSecurityEJB.authenticate( userTO );
      Assert.fail();
    }
    catch( DigitalBookingException ex )
    {
      Assert.assertTrue( true );
    }
  }

  @Test
  public void testAuthenticate_UserNotInDB()
  {
    UserTO userTO = new UserTO();
    userTO.setName( "vhlopez" );
    userTO.setPassword( "Cinepolis2014" );
    try
    {
      serviceSecurityEJB.authenticate( userTO );
      Assert.fail();
    }
    catch( DigitalBookingException ex )
    {
      Assert.assertTrue( true );
    }
  }

  @Test
  public void testFindUserDetail_Success()
  {
    String username = "gsegura";

    UserTO userTO = serviceSecurityEJB.findUserDetail( username );

    Assert.assertNotNull( userTO );
    Assert.assertEquals( "gsegura", userTO.getUsername() );
    Assert.assertEquals( "Guillermo", userTO.getPersonTO().getName() );
    Assert.assertEquals( "Segura", userTO.getPersonTO().getDsLastname() );
  }

  @Test
  public void testFindUserDetail_NonExistingUser()
  {
    String username = "vhlopez";

    UserTO userTO = serviceSecurityEJB.findUserDetail( username );

    Assert.assertNull( userTO );
  }

  @Test
  public void testFindMenuByUser_Success()
  {
    Long idUser = 5L;

    SystemMenuTO menuTO = serviceSecurityEJB.findMenuByUser( idUser );

    Assert.assertNotNull( menuTO );
    Assert.assertNotNull( menuTO.getChildren() );
    Assert.assertEquals( 3, menuTO.getChildren().size() );

  }

  @Test
  public void testFindMenuByUser_UserWithNoRole()
  {
    Long idUser = 6L;

    SystemMenuTO menuTO = serviceSecurityEJB.findMenuByUser( idUser );

    Assert.assertNotNull( menuTO );
    Assert.assertNotNull( menuTO.getChildren() );
    Assert.assertEquals( 0, menuTO.getChildren().size() );

  }

  @Test
  public void testIsSystemMenuAllowed_Success()
  {
    String url = "/views/data/distributors/distributors.do";
    List<CatalogTO> userRoles = new ArrayList<CatalogTO>();
    userRoles.add( new CatalogTO( 1L ) );

    boolean systemMenuAllowed = serviceSecurityEJB.isSystemMenuAllowed( url, userRoles );

    Assert.assertTrue( systemMenuAllowed );
  }

  @Test
  public void testIsSystemMenuAllowed_Denied()
  {
    String url = "/views/booking/movie/movieBooking.do";
    List<CatalogTO> userRoles = new ArrayList<CatalogTO>();
    userRoles.add( new CatalogTO( 1L ) );

    boolean systemMenuAllowed = serviceSecurityEJB.isSystemMenuAllowed( url, userRoles );

    Assert.assertFalse( systemMenuAllowed );
  }

  @Test
  public void testClearJPACache()
  {
    try
    {
      serviceSecurityEJB.clearJPACache();
      Assert.assertTrue( true );
    }
    catch( Exception ex )
    {
      Assert.fail();
    }
  }

}
