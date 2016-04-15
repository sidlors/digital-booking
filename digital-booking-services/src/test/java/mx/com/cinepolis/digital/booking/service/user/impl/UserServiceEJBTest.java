package mx.com.cinepolis.digital.booking.service.user.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.UserDAOImpl;
import mx.com.cinepolis.digital.booking.service.user.UserServiceEJB;

/**
 * Class for the test for UserServiceEJB
 * 
 * @author agustin.ramirez
 */
public class UserServiceEJBTest extends AbstractDBEJBTestUnit
{

  /**
   * User Service EJB
   */
  private UserServiceEJB userServiceEJB;

  /**
   * UserDAO
   */
  private UserDAO userDAO;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp ()
   */
  @Before
  public void setUp()
  {
    // instanciar el servicio
    userServiceEJB = new UserServiceEJBImpl();
    userDAO = new UserDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    connect( userServiceEJB );
    connect( userDAO );
  }

  /**
   * Test save User
   */
  @Test
  public void testSaveUser()
  {
    UserTO userTO = new UserTO();
    userTO.setFgActive( Boolean.TRUE );
    userTO.setName( "Uset" );
    userTO.setTimestamp( new Date() );
    userTO.setUserId( 1L );
    PersonTO p = new PersonTO();
    p.setEmails( new ArrayList<CatalogTO>() );
    CatalogTO email = new CatalogTO();
    email.setName( "Prueba@Test" );
    p.getEmails().add( email );
    p.setName( "Usuario Prueba" );
    p.setDsLastname( "LastNamePrueba" );
    userTO.setPersonTO( p );
    userTO.setRoles( new ArrayList<CatalogTO>() );
    userTO.getRoles().add( new CatalogTO( 1L ) );
    userServiceEJB.saveUser( userTO );
    Assert.assertNotNull( userTO.getId() );
    Assert.assertNotNull( userTO.getPersonTO().getId() );
    Assert.assertNotNull( userTO.getPersonTO().getEmails().get( 0 ).getId() );
  }

  /**
   * Test save User
   */
  @Test
  public void testSaveUser_userNameDuplicate()
  {
    UserTO userTO = new UserTO();
    userTO.setFgActive( Boolean.TRUE );
    userTO.setName( "USER 1" );
    userTO.setTimestamp( new Date() );
    userTO.setUserId( 1L );
    PersonTO p = new PersonTO();
    p.setEmails( new ArrayList<CatalogTO>() );
    CatalogTO email = new CatalogTO();
    email.setName( "Prueba@Test" );
    p.getEmails().add( email );
    p.setName( "Usuario Prueba" );
    p.setDsLastname( "LastNamePrueba" );
    userTO.setPersonTO( p );
    userTO.setRoles( new ArrayList<CatalogTO>() );
    userTO.getRoles().add( new CatalogTO( 1L ) );
    try
    {
      userServiceEJB.saveUser( userTO );
    }
    catch( DigitalBookingException e )
    {
      Assert.assertEquals( DigitalBookingExceptionCode.USER_THE_USERNAME_IS_DUPLICATE.getId(), e.getCode() );
    }

  }

  /**
   * Save User when user is null
   */
  @Test
  public void testSaveUser_userNull()
  {
    try
    {
      userServiceEJB.saveUser( null );
    }
    catch( DigitalBookingException e )
    {
      Assert.assertEquals( DigitalBookingExceptionCode.USER_IS_NULL.getId(), e.getCode() );
    }
  }

  /**
   * Save user when username is blank
   */
  @Test
  public void testSaveUser_userNameIsBlank()
  {
    try
    {
      UserTO userTO = new UserTO();
      userServiceEJB.saveUser( userTO );
    }
    catch( DigitalBookingException e )
    {
      Assert.assertEquals( DigitalBookingExceptionCode.USER_USERNAME_IS_BLANK.getId(), e.getCode() );
    }
  }

  /**
   * Save user when person is blank
   */
  @Test
  public void testSaveUser_personIsNull()
  {
    try
    {
      UserTO userTO = new UserTO();
      userTO.setName( "tester" );
      userServiceEJB.saveUser( userTO );
    }
    catch( DigitalBookingException e )
    {
      Assert.assertEquals( DigitalBookingExceptionCode.USER_NAME_IS_BLANK.getId(), e.getCode() );
    }
  }

  /**
   * Save user when person is blank
   */
  @Test
  public void testSaveUser_personNameIsBlank()
  {
    try
    {
      UserTO userTO = new UserTO();
      userTO.setName( "tester" );
      PersonTO personTO = new PersonTO();
      userTO.setPersonTO( personTO );
      userServiceEJB.saveUser( userTO );
    }
    catch( DigitalBookingException e )
    {
      Assert.assertEquals( DigitalBookingExceptionCode.USER_NAME_IS_BLANK.getId(), e.getCode() );
    }
  }

  /**
   * Save user when person lastName is blank
   */
  @Test
  public void testSaveUser_personLastNameIsBlank()
  {
    try
    {
      UserTO userTO = new UserTO();
      userTO.setName( "tester" );
      PersonTO personTO = new PersonTO();
      personTO.setName( "Tester" );
      userTO.setPersonTO( personTO );
      userServiceEJB.saveUser( userTO );
    }
    catch( DigitalBookingException e )
    {
      Assert.assertEquals( DigitalBookingExceptionCode.USER_LAST_NAME_IS_BLANK.getId(), e.getCode() );
    }
  }

  /**
   * Save user when role is null
   */
  @Test
  public void testSaveUser_RoleIsNull()
  {
    try
    {
      UserTO userTO = new UserTO();
      userTO.setName( "tester" );
      PersonTO personTO = new PersonTO();
      personTO.setName( "Tester" );
      personTO.setDsLastname( "LastName" );
      userTO.setPersonTO( personTO );
      userServiceEJB.saveUser( userTO );
    }
    catch( DigitalBookingException e )
    {
      Assert.assertEquals( DigitalBookingExceptionCode.USER_ROLE_IS_NULL.getId(), e.getCode() );
    }
  }

  /**
   * Save user when email is null
   */
  @Test
  public void testSaveUser_EmailIsNull()
  {
    try
    {
      UserTO userTO = new UserTO();
      userTO.setName( "tester" );
      PersonTO personTO = new PersonTO();
      personTO.setName( "Tester" );
      personTO.setDsLastname( "LastName" );
      userTO.setPersonTO( personTO );
      userTO.setRoles( new ArrayList<CatalogTO>() );
      userTO.getRoles().add( new CatalogTO( 1L ) );
      userServiceEJB.saveUser( userTO );
    }
    catch( DigitalBookingException e )
    {
      Assert.assertEquals( DigitalBookingExceptionCode.USER_EMAIL_IS_NULL.getId(), e.getCode() );
    }
  }

  /**
   * Test Update User
   */
  @Test
  public void testUpdateUser()
  {
    UserTO userTO = new UserTO();
    userTO.setFgActive( Boolean.TRUE );
    userTO.setName( "Uset" );
    userTO.setTimestamp( new Date() );
    userTO.setUserId( 1L );
    userTO.setId( 1L );
    userTO.setRegionSelected( 1L );
    PersonTO p = new PersonTO();
    p.setId( 1L );
    p.setName( "test" );
    p.setDsLastname( "test" );
    p.setDsMotherLastname( "las" );
    p.setEmails( new ArrayList<CatalogTO>() );
    CatalogTO email = new CatalogTO( 1L, "Prueba" );
    p.getEmails().add( email );
    userTO.setPersonTO( p );
    userTO.setRoles( new ArrayList<CatalogTO>() );
    userTO.getRoles().add( new CatalogTO( 1L ) );
    userTO.setRegions( new ArrayList<CatalogTO>() );
    userTO.getRegions().add( new CatalogTO( 1L ) );
    userTO.setTheaters( new ArrayList<CatalogTO>() );
    userTO.getTheaters().add( new CatalogTO( 1L ) );
    userServiceEJB.updateUser( userTO );
    UserDO userDO = userDAO.find( 1 );
    Assert.assertEquals( userTO.getPersonTO().getName(), userDO.getIdPerson().getDsName() );
  }

  /**
   * Test Delete
   */
  @Test
  public void testDeleteUser()
  {
    UserTO userTO = new UserTO();
    userTO.setFgActive( Boolean.TRUE );
    userTO.setTimestamp( new Date() );
    userTO.setUserId( 1L );
    userTO.setId( 1L );
    userServiceEJB.deleteUser( userTO );
    Assert.assertFalse( userDAO.find( 1 ).isFgActive() );
  }

  /**
   * Test Delete when user is null
   */
  @Test
  public void testDeleteUser_userIsNull()
  {
    try
    {
      userServiceEJB.deleteUser( null );
    }
    catch( DigitalBookingException e )
    {
      Assert.assertEquals( DigitalBookingExceptionCode.USER_IS_NULL.getId(), e.getCode() );
    }

  }

  /**
   * Test findAll user
   */
  @Test
  public void testFindAllUser()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 2 );
    PagingResponseTO<UserTO> response = userServiceEJB.findAllUsers( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    Assert.assertTrue( response.getTotalCount() == 6 );
    for( UserTO to : response.getElements() )
    {
      System.out.println( "User TO =>" + ReflectionToStringBuilder.toString( to, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }

  /**
   * Test find All Roles Active
   */
  @Test
  public void testFindAllRolesActive()
  {
    List<CatalogTO> roles = null;
    roles = userServiceEJB.findAllRolesActive();
    Assert.assertNotNull( roles );
    Assert.assertTrue( roles.size() > 0 );
  }

}
