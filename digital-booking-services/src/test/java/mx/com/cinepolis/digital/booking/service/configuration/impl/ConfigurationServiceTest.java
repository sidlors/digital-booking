package mx.com.cinepolis.digital.booking.service.configuration.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.model.ConfigurationDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.service.configuration.ConfigurationServiceEJB;

/**
 * Clase de pruebas de ConfigurationService
 * 
 * @author agustin.ramirez
 * 
 */
public class ConfigurationServiceTest extends AbstractDBEJBTestUnit {

	/**
	 * Servicio
	 */
	private ConfigurationServiceEJB configurationServiceEJB;

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
		configurationServiceEJB = new ConfigurationServiceEJBImpl();

		// Llamar la prueba padre para obtener el EntityManager
		super.setUp();

		connect(configurationServiceEJB);
	}
	
	/**
	 * Tests findParameterById
	 */
	@Test
	public void testFindParameterById(){
		ConfigurationDO cfg = null ;
		cfg = configurationServiceEJB.findParameterById(1L);
		
		Assert.assertNotNull(cfg);
	}
	
	/**
	 * Tests findParameterById
	 */
	@Test
	public void testFindParameterById_idIsNull(){
		try {
			configurationServiceEJB.findParameterById(null);
			Assert.fail("Debio lanzar excepcion");
		} catch (DigitalBookingException e) {
			e.printStackTrace();
		}
	
	}
	
	/**
	 * Tests findParameterByName
	 */
	@Test
	public void testFindParameterByName(){
		ConfigurationDO cfg = null ;
		cfg = configurationServiceEJB.findParameterByName("EMAIL_HOST");
		
		Assert.assertNotNull(cfg);
	}
	
	/**
	 * Tests findParameterByName
	 */
	@Test
	public void testFindParameterByName_idIsNull(){
		try {
			configurationServiceEJB.findParameterByName("");
			Assert.fail("Debio lanzar excepcion");
		} catch (DigitalBookingException e) {
			e.printStackTrace();
		}
	
	}

}
