package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mx.com.cinepolis.digital.booking.model.EmailDO;
import mx.com.cinepolis.digital.booking.model.PersonDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.EmailDAO;

/**
 * Clase de pruebas del DAO EmailDAO
 * @author agustin.ramirez
 *
 */
public class EmailDAOTest extends AbstractDBEJBTestUnit{
	
	/**
	 * DAO
	 */
	private EmailDAO emailDAO;
		
	/*
	 * (non-Javadoc)
	 * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp()
	 */
	  @Before
	  public void setUp()
	  {
	    // instanciar el servicio
	    emailDAO = new EmailDAOImpl();
	    // Llamar la prueba padre para obtener el EntityManager
	    super.setUp();
	    // Llamar los datos de negocio
	    // this.initializeData( "dataset/business/pollServiceTest.sql" );
	    // Conectar el EntityManager al servicio y sus daos
	    connect( emailDAO );

	  }
	  
	  /**
	   * Prueba findByPersonId
	   */
	  @Test
	  public void testFindByPersonId(){
		  List<EmailDO> emailsPerson3 =  emailDAO.findByPersonId(3L);
		  Assert.assertNotNull(emailsPerson3);
		  Assert.assertTrue(emailsPerson3.size() == 1);
	  }
	  
	  /**
	   * Prueba FindByUserId
	   */
	  @Test
	  public void testFindByUserId(){
		  List<EmailDO> emails=  emailDAO.findByUserId(1L);
		  Assert.assertNotNull(emails);
		  Assert.assertTrue(emails.size() == 2);
	  }
	  
	  /**
	   * Test count
	   */
	  @Test
	  public void testCount(){
		  int totalEmails = emailDAO.count();
		  Assert.assertEquals(9, totalEmails);
	  }
	  
	  /**
	   * Test find
	   */
	  @Test
	  public void testFind(){
		  EmailDO  emailDO = null ;
		  emailDO = emailDAO.find(1);
		  Assert.assertNotNull(emailDO);
	  }
	  
	  /**
	   * Test find All
	   */
	  @Test
	  public void testFindAll(){
		  List<EmailDO> emails = null;
		  emails = emailDAO.findAll();
		  Assert.assertNotNull(emails);
		  Assert.assertTrue(emails.size() == 9);
	  }
	  
	  /**
	   * Test Create
	   */
	  @Test
	  public void testCreate(){
		  int actual  = emailDAO.count();
		  EmailDO emailDO = new EmailDO();
		  emailDO.setDsEmail("Prueba@mail.com");
		  emailDO.setIdPerson(new PersonDO(1));
		  emailDAO.create(emailDO);
		  Assert.assertEquals(actual + 1, emailDAO.count());
		  
	  }
	  
	  /**
	   * Test Edit
	   */
	  @Test
	  public void testEdit(){

		  EmailDO emailDO = emailDAO.find(1);
		  emailDO.setDsEmail("Prueba");
		  emailDAO.edit(emailDO);
		  EmailDO emailUpdtaed = emailDAO.find(1);
		  Assert.assertEquals("Prueba" , emailUpdtaed.getDsEmail());
		  
	  }
}
