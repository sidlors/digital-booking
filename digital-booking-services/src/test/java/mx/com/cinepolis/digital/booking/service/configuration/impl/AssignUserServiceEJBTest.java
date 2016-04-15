package mx.com.cinepolis.digital.booking.service.configuration.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.RegionDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.TheaterDAOImpl;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.UserDAOImpl;
import mx.com.cinepolis.digital.booking.service.configuration.AssignUserServiceEJB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas de AssignUserService
 * 
 * @author rgarcia
 */
public class AssignUserServiceEJBTest extends AbstractDBEJBTestUnit
{

  /**
   * Servicio
   */
  private AssignUserServiceEJB assignUserServiceEJB;

  /**
   * DAO's
   */
  private RegionDAO regionDAO;
  private UserDAO userDAO;
  private TheaterDAO theaterDAO;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp ()
   */
  @Before
  public void setUp()
  {
    // instanciar el servicio
    assignUserServiceEJB = new AssignUserServiceEJBImpl();
    regionDAO = new RegionDAOImpl();
    userDAO = new UserDAOImpl();
    theaterDAO = new TheaterDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    connect( assignUserServiceEJB );
    connect( regionDAO );
    connect( userDAO );
    connect( theaterDAO );
  }

  /**
   * Tests testAssignRegionsUser
   */
  @Test
  public void testAssignRegionsUser_Ok()
  {
    UserTO userTO = generateUserTO( true );
    Assert.assertNotNull( userTO.getRegions() );
    CatalogTO regions = userTO.getRegions().get( 0 );
    Assert.assertNotNull( regions );
    assignUserServiceEJB.assignRegionsUser( userTO );

    UserDO userDO = userDAO.find( userTO.getId().intValue() );
    Assert.assertNotNull( userDO );
    Assert.assertNotNull( userDO.getRegionDOList() );
    for( RegionDO regionDO2 : userDO.getRegionDOList() )
    {
      Assert.assertNotNull( regionDO2 );
      Assert.assertEquals( 2L, regionDO2.getIdRegion().longValue() );
    }

    RegionDO regionDO = regionDAO.find( regions.getId().intValue() );
    for( UserDO userDO2 : regionDO.getUserDOList() )
    {
      Assert.assertNotNull( userDO2 );
      System.out.println( "User ID: " + userDO2.getIdUser() );
    }
  }

  /**
   * Tests testAssignTheatersUser_Ok
   */
  @Test
  public void testAssignTheatersUser_Ok()
  {
    UserTO userTO = generateUserTO( true );
    userTO.setRegionSelected( 1L );
    Assert.assertNotNull( userTO.getTheaters() );
    CatalogTO theaters = userTO.getTheaters().get( 0 );
    Assert.assertNotNull( theaters );
    assignUserServiceEJB.assignTheatersUser( userTO );

    UserDO userDO = userDAO.find( userTO.getId().intValue() );
    Assert.assertNotNull( userDO );
    Assert.assertNotNull( userDO.getTheaterDOList() );
    for( TheaterDO theaterDO : userDO.getTheaterDOList() )
    {
      Assert.assertNotNull( theaterDO );
      Assert.assertEquals( 1L, theaterDO.getIdTheater().longValue() );
    }

    TheaterDO theaterDO2 = theaterDAO.find( theaters.getId().intValue() );
    for( UserDO userDO2 : theaterDO2.getUserDOList() )
    {
      Assert.assertNotNull( userDO2 );
      System.out.println( "User ID: " + userDO2.getIdUser() );
    }
  }

  /**
   * Tests testAssignTheatersUser_Ok_SeveralTheaters
   */
  @Test
  public void testAssignTheatersUser_Ok_SeveralTheaters()
  {
    UserTO userTO = generateUserTO( false );
    Assert.assertNotNull( userTO.getTheaters() );
    assignUserServiceEJB.assignTheatersUser( userTO );
    UserDO userDO = userDAO.find( userTO.getId().intValue() );
    Assert.assertNotNull( userDO );
    Assert.assertNotNull( userDO.getTheaterDOList() );
    List<Integer> theatherIds = new ArrayList<Integer>();
    theatherIds.add( 1 );
    theatherIds.add( 2 );
    theatherIds.add( 3 );
    theatherIds.add( 4 );
    for( TheaterDO theaterDO : userDO.getTheaterDOList() )
    {
      Assert.assertNotNull( theaterDO );
      System.out.println( "Theater ID: " + theaterDO.getIdTheater() );
      Assert.assertTrue( theatherIds.contains( theaterDO.getIdTheater().intValue() ) );
    }
    for( CatalogTO theaters : userTO.getTheaters() )
    {
      TheaterDO theaterDO2 = theaterDAO.find( theaters.getId().intValue() );
      for( UserDO userDO2 : theaterDO2.getUserDOList() )
      {
        Assert.assertNotNull( userDO2 );
        System.out.println( "User ID: " + userDO2.getIdUser() );
      }
    }

  }

  private UserTO generateUserTO( boolean isOneCatalog )
  {
    UserTO userTO = new UserTO();
    userTO.setFgActive( Boolean.TRUE );
    userTO.setName( "Uset" );
    userTO.setTimestamp( new Date() );
    userTO.setUserId( 4L );
    userTO.setId( 4L );
    PersonTO p = new PersonTO();
    p.setId( 4L );
    p.setName( "test" );
    p.setDsLastname( "test" );
    p.setDsMotherLastname( "las" );
    p.setEmails( new ArrayList<CatalogTO>() );
    CatalogTO email = new CatalogTO( 50L, "Correo de Prueba" );
    p.getEmails().add( email );
    userTO.setPersonTO( p );
    userTO.setRoles( new ArrayList<CatalogTO>() );
    userTO.setRegions( new ArrayList<CatalogTO>() );
    userTO.setTheaters( new ArrayList<CatalogTO>() );

    userTO.getRoles().add( new CatalogTO( 1L ) );
    userTO.getRegions().add( new CatalogTO( 2L ) );
    userTO.getTheaters().add( new CatalogTO( 1L ) );
    if( !isOneCatalog )
    {
      userTO.getTheaters().add( new CatalogTO( 2L ) );
      userTO.getTheaters().add( new CatalogTO( 3L ) );
      userTO.getTheaters().add( new CatalogTO( 4L ) );
    }
    return userTO;
  }

  /**
   * Tests testGetAllUsersActive_happy_path
   */
  @Test
  public void testGetAllUsersActive_happy_path()
  {
    List<UserTO> userTOs = assignUserServiceEJB.getAllUsersActive();
    Assert.assertNotNull( userTOs );
    Assert.assertEquals( userDAO.count(), userTOs.size() );
  }

  /**
   * Tests testGetTheatersByRegionId_happy_path
   */
  @Test
  public void testGetTheatersByRegionId_happy_path()
  {
    AbstractTO abstractTO = new AbstractTO();
    abstractTO.setFgActive( true );
    abstractTO.setIdLanguage( 1L );
    abstractTO.setTimestamp( new Date() );
    abstractTO.setUserId( 1L );
    List<CatalogTO> theaterTOs = assignUserServiceEJB.getTheatersAvailable( 1L, abstractTO );
    Assert.assertNotNull( theaterTOs );
    Assert.assertEquals( 3, theaterTOs.size() );
  }

  @Test
  public void testGetRegionsAvailable()
  {
    List<CatalogTO> regionTOs = assignUserServiceEJB.getRegionsAvailable( 1L );
    Assert.assertNotNull( regionTOs );
    Assert.assertEquals( 13, regionTOs.size() );
  }

  @Test
  public void testGetRegionsAssociated()
  {
    List<CatalogTO> regionTOs = assignUserServiceEJB.getRegionsAssociated( 1L );
    Assert.assertNotNull( regionTOs );
    Assert.assertEquals( 3, regionTOs.size() );
  }

  @Test
  public void testGetTheatersAssociated()
  {
    AbstractTO abstractTO = new AbstractTO();
    abstractTO.setFgActive( true );
    abstractTO.setIdLanguage( 1L );
    abstractTO.setTimestamp( new Date() );
    abstractTO.setUserId( 1L );
    List<CatalogTO> theaterTOs = assignUserServiceEJB.getTheatersAssociated( 1L, abstractTO );
    Assert.assertNotNull( theaterTOs );
    Assert.assertEquals( 2, theaterTOs.size() );
  }
}
