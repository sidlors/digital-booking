package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.PersonQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.model.PersonDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.PersonDAO;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas de PersonDAO
 * 
 * @author agustin.ramirez
 * 
 */
public class PersonDAOTest extends AbstractDBEJBTestUnit {

	/**
	 * Person DAO
	 */
	private PersonDAO personDAO;

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
		personDAO = new PersonDAOImpl();
		// Llamar la prueba padre para obtener el EntityManager
		super.setUp();
		// Llamar los datos de negocio
		// this.initializeData( "dataset/business/pollServiceTest.sql" );
		// Conectar el EntityManager al servicio y sus daos
		connect(personDAO);

	}

	/**
	 * Test Count
	 */
	@Test
	public void testCount() {
		int persons = personDAO.count();
		Assert.assertEquals(6, persons);
	}

	/**
	 * Test find
	 */
	@Test
	public void testFind() {
		PersonDO personDO = null;
		personDO = personDAO.find(1);
		Assert.assertNotNull(personDO);
	}

	/**
	 * Test Create
	 */
	@Test
	public void testCreate() {
		int total = personDAO.count();
		PersonDO personDO = new PersonDO();
		personDO.setDsLastname("Prueba Last Name");
		personDO.setDsMotherLastname("Prueba Second Mother Last Name");
		personDO.setDsName("Prueba Name");
		personDO.setDtLastModification(new Date());
		personDO.setFgActive(Boolean.TRUE);
		personDO.setIdLastUserModifier(1);
		personDAO.create(personDO);
		Assert.assertEquals(total + 1, personDAO.count());
	}

	/**
	 * Test Edit
	 */
	@Test
	public void testEdit() {
		PersonDO personDO = personDAO.find(1);
		personDO.setDsName("Sujeto de Prueba EDIT");
		personDAO.edit(personDO);

		PersonDO personUpdate = personDAO.find(1);
		Assert.assertEquals("Sujeto de Prueba EDIT", personUpdate.getDsName());

	}

	/**
	 * Test FindAll
	 */
	@Test
	public void testFindAll() {
		List<PersonDO> personDOs = null;
		personDOs = personDAO.findAll();
		Assert.assertNotNull(personDOs);
		Assert.assertEquals(6, personDOs.size());

	}

	/**
	 * Test findAllByPaging when needsPaging = false
	 */
	@Test
	public void testFindAllByPaging_pagingNeedsIsFalse() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.FALSE);

		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(6, response.getTotalCount());

	}

	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_WithPagination() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.TRUE);
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(2);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(6, response.getTotalCount());
		Assert.assertEquals(2, response.getElements().size());
	}

	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_PersonIdsortDesc() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.TRUE);
		pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(PersonQuery.PERSON_ID);
		pagingRequestTO.setSortOrder(SortOrder.DESCENDING);
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(6);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(6, response.getTotalCount());
	}

	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_PersonIdsortAsc() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.TRUE);
		pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(PersonQuery.PERSON_ID);
		pagingRequestTO.setSortOrder(SortOrder.ASCENDING);
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(6);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(6, response.getTotalCount());
	}

	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_PersonNamesortDesc() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.TRUE);
		pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(PersonQuery.PERSON_NAME);
		pagingRequestTO.setSortOrder(SortOrder.DESCENDING);
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(6);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(6, response.getTotalCount());
	}

	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_PersonNamesortASC() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.TRUE);
		pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(PersonQuery.PERSON_NAME);
		pagingRequestTO.setSortOrder(SortOrder.ASCENDING);
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(6);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(6, response.getTotalCount());
	}

	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_PersonLastNamesortDesc() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.TRUE);
		pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(PersonQuery.PERSON_LAST_NAME);
		pagingRequestTO.setSortOrder(SortOrder.DESCENDING);
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(6);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(6, response.getTotalCount());
	}



	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_PersonTheatersortDesc() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.TRUE);
		pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(PersonQuery.PERSON_ID_THEATER);
		pagingRequestTO.setSortOrder(SortOrder.DESCENDING);
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(6);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(6, response.getTotalCount());
	}

	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_PersonIdUsersortDesc() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setNeedsPaging(Boolean.TRUE);
		pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
		pagingRequestTO.getSort().add(PersonQuery.PERSON_ID_USER);
		pagingRequestTO.setSortOrder(SortOrder.DESCENDING);
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(6);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(6, response.getTotalCount());
	}

	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_ByIdPerson() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(PersonQuery.PERSON_ID,3);
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(6);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(1, response.getTotalCount());
	}
	
	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_ByNAME() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(PersonQuery.PERSON_NAME,"PERSONA 1");
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(6);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(1, response.getTotalCount());
	}
	
	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_ByLastName() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(PersonQuery.PERSON_LAST_NAME,"PERSONA 3");
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(6);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(1, response.getTotalCount());
	}
	
	
	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_ByMotherLastName() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(PersonQuery.PERSON_MOTHER_LAST_NAME,"PERSONA 3");
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(6);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(1, response.getTotalCount());
	}
	
	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_ByActive() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(PersonQuery.PERSON_ACTIVE,true);
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(40);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(4, response.getTotalCount());
	}
	
	
	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_ByIdUser() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(PersonQuery.PERSON_ID_USER,2);
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(40);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(1, response.getTotalCount());
	}
	
	/**
	 * Tests findAllByPaging with pagination
	 */
	@Test
	public void testFindAllByPaging_ByIdTheater() {
		PagingRequestTO pagingRequestTO = new PagingRequestTO();
		pagingRequestTO.setFilters(new HashMap<ModelQuery, Object>());
		pagingRequestTO.getFilters().put(PersonQuery.PERSON_ID_THEATER,3);
		pagingRequestTO.setPage(0);
		pagingRequestTO.setPageSize(40);
		PagingResponseTO<PersonTO> response = null;
		response = personDAO.findAllByPaging(pagingRequestTO);
		Assert.assertNotNull(response);
		for (PersonTO p : response.getElements()) {
			System.out.println("PersonaTO =Z>"
					+ ReflectionToStringBuilder.toString(p,
							ToStringStyle.MULTI_LINE_STYLE));
		}
		Assert.assertEquals(1, response.getTotalCount());
	}
	
}
