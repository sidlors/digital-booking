/**
 * 
 */
package mx.com.cinepolis.digital.booking.service.localtasks.impl;

import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.PresaleDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.impl.PresaleDAOImpl;
import mx.com.cinepolis.digital.booking.service.localtasks.ServiceLocalTasksEJB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for ServiceLocalTasksEJB
 * 
 * @author jreyesv
 */
public class ServiceLocalTasksEJBTest extends AbstractDBEJBTestUnit
{
  private static final Logger logger = LoggerFactory.getLogger( ServiceLocalTasksEJBTest.class );

  /**
   * EJB service
   */
  private ServiceLocalTasksEJB serviceLocalTasksEJB;

  /**
   * DAO for presales
   */
  private PresaleDAO presaleDAO;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp()
   */
  @Before
  public void setUp()
  {
    // instanciar el servicio
    this.serviceLocalTasksEJB = new ServiceLocalTasksEJBImpl();
    this.presaleDAO=new PresaleDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Conectar el EntityManager al servicio y sus daos
    connect( this.serviceLocalTasksEJB );
    connect(presaleDAO);
  }
  
  /**
   * Tests for deactivatePresales method.
   */
  @Test
  public void deactivatePresalesTest()
  {
    int initialSize = this.presaleDAO.countAllActivePresales();
    this.serviceLocalTasksEJB.deactivatePresales();
    int resultSize = this.presaleDAO.countAllActivePresales();
    Assert.assertTrue( initialSize >= resultSize );
    logger.info( "Preventas activas al inicio: " + initialSize + " ----- Preventas activas al final: " + resultSize );
  }

}
