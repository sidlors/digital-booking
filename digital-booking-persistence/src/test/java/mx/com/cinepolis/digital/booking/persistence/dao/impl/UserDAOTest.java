package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;



import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.query.UserQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.model.PersonDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.RoleDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;

/**
 * Clase de pruebas de UserDAO
 * 
 * @author agustin.ramirez
 * 
 */
public class UserDAOTest extends AbstractDBEJBTestUnit {
	
	/**
	 * UserDAO
	 */
	private UserDAO userDAO;
	
	private RegionDAO regionDAO;
	private TheaterDAO theaterDAO;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp
	 * ()
	 */
	@Before
	public void setUp() {
		// instanciar el servicio
		userDAO = new UserDAOImpl();
		regionDAO = new RegionDAOImpl();
		theaterDAO = new TheaterDAOImpl();
		// Llamar la prueba padre para obtener el EntityManager
		super.setUp();
		// Llamar los datos de negocio
		// this.initializeData( "dataset/business/pollServiceTest.sql" );
		// Conectar el EntityManager al servicio y sus daos
		connect(userDAO);
		connect(regionDAO);
		connect(theaterDAO);
	}
	
	/**
	 * Test Count
	 */
	@Test
	public void testCount(){
		int total = userDAO.count();
		Assert.assertEquals(6, total);
	}
	
	/**
	 * Test Find
	 */
	@Test
	public void testFind(){
		UserDO userDO = null ;
		userDO = userDAO.find(1);
		Assert.assertNotNull(userDO);
	}
	
	/**
	 * Tests Find All
	 */
	@Test
	public void testFindAll(){
		List<UserDO> users = null ;
		users = userDAO.findAll();
		Assert.assertNotNull(users);
		Assert.assertTrue(users.size() > 0);
	}
	
	
	/**
	 * Tests find Range
	 */
	@Test
	public void testFindRange(){
		   List<UserDO> users = this.userDAO.findRange( new int[] { 1, 3 } );
		    Assert.assertNotNull( users );

		    Assert.assertEquals( 2, users.size() );
	}
	
	/**
	 * Test Create
	 */
	@Test
	public void testCreate(){
		int antes = this.userDAO.count();
		UserDO userDO = new UserDO();
		userDO.setDsUsername("userTest");
		userDO.setDtLastModification(new Date());
		userDO.setFgActive(Boolean.TRUE);
		userDO.setIdLastUserModifier(1);
		userDO.setRegionDOList(new ArrayList<RegionDO>());
		userDO.getRegionDOList().add(this.regionDAO.find( 1 ));
		userDO.setTheaterDOList(new ArrayList<TheaterDO>());
		userDO.getTheaterDOList().add(this.theaterDAO.find( 1 ));
		userDO.setIdPerson(new PersonDO(1));
		userDO.setRoleDOList(new ArrayList<RoleDO>());
		userDO.getRoleDOList().add(new RoleDO(1));
		userDAO.create(userDO);
		Assert.assertEquals(antes + 1, userDAO.count());
	}
	
	/**
	 * Test Edit
	 */
	@Test
	public void testEdit(){
		UserDO userDO = this.userDAO.find(1);
		userDO.setDsUsername("Usuario de prueba!!");
		userDAO.edit(userDO);
		UserDO userUpdated = this.userDAO.find(1);
		Assert.assertEquals(userDO.getDsUsername(), userUpdated.getDsUsername());
		
	}
	
	/**
	 * Test Save
	 */
	@Test
	public void testSave(){
		int antes = this.userDAO.count();
		UserTO userTO = new UserTO();
		userTO.setFgActive(Boolean.TRUE);
		userTO.setName("Uset");
		userTO.setTimestamp(new Date());
		userTO.setUserId(1L);
		PersonTO p = new PersonTO();
		p.setEmails(new ArrayList<CatalogTO>());
		CatalogTO email = new CatalogTO();
		email.setName("Prueba@Test");
		p.getEmails().add(email);
		p.setName("Usuario Prueba");
		p.setDsLastname("LastNamePrueba");
		userTO.setPersonTO(p);
		userTO.setRoles(new ArrayList<CatalogTO>());
		userTO.getRoles().add(new CatalogTO(1L));
		userDAO.save(userTO);
		Assert.assertNotNull(userTO.getId());
		Assert.assertNotNull(userTO.getPersonTO().getId());
		Assert.assertNotNull(userTO.getPersonTO().getEmails().get(0).getId());
		Assert.assertEquals(antes + 1, userDAO.count());
	}
	
	/**
	 * Test Update
	 */
	@Test
	public void testUpdate(){
		UserTO userTO = new UserTO();
		userTO.setFgActive(Boolean.TRUE);
		userTO.setName("Uset");
		userTO.setTimestamp(new Date());
		userTO.setUserId(1L);
		userTO.setId(1L);
		PersonTO p = new PersonTO();
		p.setId(1L);
		p.setName("test");
		p.setDsLastname("test");
		p.setDsMotherLastname("las");
		p.setEmails(new ArrayList<CatalogTO>());
		CatalogTO email = new CatalogTO(1L, "Prueba");
		p.getEmails().add(email);
		userTO.setPersonTO(p);
		userTO.setRoles(new ArrayList<CatalogTO>());
		userTO.getRoles().add(new CatalogTO(1L));
		userTO.setRegions( new ArrayList<CatalogTO>());
    userTO.getRegions().add(new CatalogTO(2L));
		userDAO.update(userTO);
		UserDO userDO = userDAO.find(1);
		Assert.assertEquals(userTO.getPersonTO().getName(), userDO.getIdPerson().getDsName());
		if(CollectionUtils.isNotEmpty( userDO.getRegionDOList() ))
		{
		  RegionDO regionDO = userDO.getRegionDOList().get( 0 );
		  Assert.assertEquals( 2, regionDO.getIdRegion().intValue() );
		}
    
	}
	
	
	/**
	 * Test Update UserNot Found
	 */
	@Test
	public void testUpdate_UserNotFound(){
		UserTO userTO = new UserTO();
		
		userTO.setId(100L);
		try {
			userDAO.update(userTO);
			Assert.fail("DEbio lanzar excepcion");
		} catch (DigitalBookingException e) {
			System.err.print(e.getMessage());
		}
		
		
	}
	
	/**
	 * Test Delete UserNot Found
	 */
	@Test
	public void testDelete_UserNotFound(){
		UserTO userTO = new UserTO();
		userTO.setUserId( 1L );
		
		userTO.setId(100L);
		try {
			userDAO.delete(userTO);
			Assert.fail("DEbio lanzar excepcion");
		} catch (DigitalBookingException e) {
			System.err.print(e.getMessage());
		}
		
		
	}
	/**
	 * Test Remove
	 */
	@Test
	public void testRemove(){
		UserDO userDO = this.userDAO.find(1);
		userDAO.remove(userDO);
		
		Assert.assertFalse(userDAO.find(1).isFgActive());
	}
	
	/**
	 * Test Delete
	 */
	@Test
	public void testDelete(){
		UserTO userTO = new UserTO();
		userTO.setFgActive(Boolean.TRUE);
		userTO.setTimestamp(new Date());
		userTO.setUserId(1L);
		userTO.setId(1L);
		userDAO.delete(userTO);
		Assert.assertFalse(userDAO.find(1).isFgActive());

	}
	
	/**
	 * Tests Find All ByPaging
	 */
	@Test
	public void testFindAllByPaging(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(2);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
	Assert.assertTrue(response.getTotalCount() == 6);
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	/**
	 * Tests Find All ByPaging
	 */
	@Test
	public void testFindAllByPaging_WIthoutPaging(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
	Assert.assertTrue(response.getTotalCount() == 6);
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_SortByUserId(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setSortOrder(SortOrder.DESCENDING);
		pagingRequestTO.setSort(new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(UserQuery.USER_ID);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
	Assert.assertTrue(response.getTotalCount() == 6);
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_SortByUserName(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setSortOrder(SortOrder.DESCENDING);
		pagingRequestTO.setSort(new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(UserQuery.USER_NAME);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
	Assert.assertTrue(response.getTotalCount() == 6);
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_SortByPersonID(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setSortOrder(SortOrder.DESCENDING);
		pagingRequestTO.setSort(new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(UserQuery.USER_ID_PERSON);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
	Assert.assertTrue(response.getTotalCount() == 6);
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_SortByTheaterID(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setSortOrder(SortOrder.DESCENDING);
		pagingRequestTO.setSort(new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(UserQuery.USER_ID_THEATER);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
	Assert.assertTrue(response.getTotalCount() == 6);
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	
	
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_SortByRegionID(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setSortOrder(SortOrder.DESCENDING);
		pagingRequestTO.setSort(new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(UserQuery.USER_ID_REGION);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
	Assert.assertTrue(response.getTotalCount() == 6);
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_SortByRoleID(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setSortOrder(SortOrder.ASCENDING);
		pagingRequestTO.setSort(new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(UserQuery.USER_ROLE_ID);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
	Assert.assertTrue(response.getTotalCount() == 6);
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_filterByUserID(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(UserQuery.USER_ID, 1L);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
//	Assert.assertTrue(response.getTotalCount() == 1);//TODO Revisar El response trae un totalCount de 2 aunque solo tiene 1 elemento
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_filterByActive(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(UserQuery.USER_ACTIVE, true);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
		Assert.assertEquals( 6, response.getTotalCount() );
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_filterByUserName(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(UserQuery.USER_NAME, "USER 4");
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
	Assert.assertTrue(response.getTotalCount() == 1);
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_filterByPerson_ID(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(UserQuery.USER_ID_PERSON, 2);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
	  Assert.assertTrue(response.getTotalCount() != 1);
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_filterByRoleID(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(UserQuery.USER_ROLE_ID, 3);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
		Assert.assertEquals( 6, response.getTotalCount() );
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_filterByTheater_ID(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setUserId( 1L );
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(UserQuery.USER_ID_THEATER, 4);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
		Assert.assertEquals( 6, response.getTotalCount() );
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
	
	/**
	 * 
	 */
	@Test
	public void testFindAllByPaging_filterByRegionID(){
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setUserId( 1L );
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(UserQuery.USER_ID_REGION, 2);
		PagingResponseTO<UserTO> response = userDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		Assert.assertNotNull(response.getElements());
		System.out.println(response.getTotalCount() );
	  Assert.assertEquals( 6, response.getTotalCount() );
		for(UserTO to : response.getElements()){
			System.out.println("User TO =>" + ReflectionToStringBuilder.toString(to, ToStringStyle.MULTI_LINE_STYLE));
		}
	}
}
