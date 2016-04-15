package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.RoleDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.RoleDAO;

/**
 * Test class for RoleDAO
 * @author agustin.ramirez
 *
 */
public class RoleDAOTest extends AbstractDBEJBTestUnit{
	
	/**
	 * Role DAO
	 */
	private RoleDAO roleDAO;
	
	/*
	   * (non-Javadoc)
	   * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp ()
	   */
	  @Before
	  public void setUp()
	  {
	    // instanciar el servicio
	    roleDAO = new RoleDAOImpl();
	    super.setUp();
	    connect( roleDAO );

	  }
	  
	  /**
	   * Test Count
	   */
	  @Test
	  public void testCount(){		  
		  Assert.assertTrue(roleDAO.count() > 0);		  
	  }
	  
	  /**
	   * Test find
	   */
	  @Test
	  public void testFind(){
		  Assert.assertNotNull(roleDAO.find(1));
	  }
	  
	  /**
	   * Test Create
	   */
	  @Test
	  public void testCreate(){
		  int count = roleDAO.count();		  
		  RoleDO role = new RoleDO();
		  role.setFgActive(Boolean.TRUE);
		  role.setDsRole("test");
		  roleDAO.create(role);
		  Assert.assertEquals(count + 1, roleDAO.count());
		  
	  }
	  
	  /**
	   * Test Edit
	   */
	  @Test
	  public void testEdit(){
		  RoleDO roleToUpdate = roleDAO.find(1);
		  roleToUpdate.setDsRole("testUpdate");
		  roleDAO.edit(roleToUpdate);
		  Assert.assertEquals(roleToUpdate.getDsRole(), roleDAO.find(1).getDsRole());
		  
	  }
	  
	  /**
	   * Test getAllRoleActive
	   */
	  @Test
	  public void testGetAllRoleActive(){
		  
		  List<CatalogTO> roles = null;
		  roles = roleDAO.getAllRoleActive();
		  Assert.assertNotNull(roles);
	  }
	

}
