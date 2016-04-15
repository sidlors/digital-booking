package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemMenuTO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.SystemMenuDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for the Data Access Object related to a system menu element.
 * 
 * @author afuentes
 */
public class SystemMenuDAOTest extends AbstractDBEJBTestUnit
{

  private SystemMenuDAO systemMenuDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    systemMenuDAO = new SystemMenuDAOImpl();

    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();

    // Conectar el EntityManager al servicio y sus daos
    connect( systemMenuDAO );

  }

  @Test
  public void testFindMenuByUser_Success()
  {
    Long idUser = 5L;

    SystemMenuTO menuTO = systemMenuDAO.findMenuByUser( idUser );

    Assert.assertNotNull( menuTO );
    Assert.assertNotNull( menuTO.getChildren() );
    Assert.assertEquals( 3, menuTO.getChildren().size() );
  }

  @Test
  public void testFindMenuByUser_UserWithNoRole()
  {
    Long idUser = 6L;

    SystemMenuTO menuTO = systemMenuDAO.findMenuByUser( idUser );

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

    boolean systemMenuAllowed = systemMenuDAO.isSystemMenuAllowed( url, userRoles );

    Assert.assertTrue( systemMenuAllowed );
  }

  @Test
  public void testIsSystemMenuAllowed_Denied()
  {
    String url = "/views/booking/movie/movieBooking.do";
    List<CatalogTO> userRoles = new ArrayList<CatalogTO>();
    userRoles.add( new CatalogTO( 1L ) );

    boolean systemMenuAllowed = systemMenuDAO.isSystemMenuAllowed( url, userRoles );

    Assert.assertFalse( systemMenuAllowed );
  }

  @Test
  public void testClearJPACache()
  {
    try
    {
      systemMenuDAO.clearJPACache();
      Assert.assertTrue( true );
    }
    catch( Exception ex )
    {
      Assert.fail();
    }
  }

}
