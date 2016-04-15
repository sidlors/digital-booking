package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;

import mx.com.cinepolis.digital.booking.model.ConfigurationDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Clase de pruebas del DAO ConfigurationDAO
 * 
 * @author agustin.ramirez
 */
public class ConfigurationDAOTest extends AbstractDBEJBTestUnit
{
  /**
   * DAO
   */
  private ConfigurationDAO configurationDAO;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp()
   */
  @Before
  public void setUp()
  {
    // instanciar el servicio
    configurationDAO = new ConfigurationDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( configurationDAO );

  }

  /**
   * Test Count
   */
  @Test
  public void testCount()
  {
    int total = configurationDAO.count();
    Assert.assertEquals( 30, total );
  }

  /**
   * Test Find
   */
  @Test
  public void testFind()
  {
    ConfigurationDO cfg = null;
    cfg = configurationDAO.find( 2 );

    Assert.assertNotNull( cfg );

  }

  /**
   * Test Find all
   */
  @Test
  public void testFindAll()
  {
    List<ConfigurationDO> cfgs = null;
    cfgs = configurationDAO.findAll();

    Assert.assertNotNull( cfgs );
    Assert.assertEquals( 30, cfgs.size() );
  }

  /**
   * Test find Range
   */
  @Test
  public void testFindRange()
  {
    List<ConfigurationDO> cfgs = this.configurationDAO.findRange( new int[] { 1, 3 } );
    Assert.assertNotNull( cfgs );

    Assert.assertEquals( 2, cfgs.size() );
  }

  /**
   * Test create
   */
  @Test
  public void testCreate()
  {
    int antes = this.configurationDAO.count();
    ConfigurationDO configurationDO = new ConfigurationDO();
    configurationDO.setDsParameter( "Test" );
    configurationDO.setDsValue( "Value Test" );

    configurationDAO.create( configurationDO );

    Assert.assertEquals( antes + 1, configurationDAO.count() );
  }

  /**
   * Test Edit
   */
  @Test
  public void testEdit()
  {
    ConfigurationDO cfg = configurationDAO.find( 1 );
    cfg.setDsParameter( "tester" );

    configurationDAO.edit( cfg );

    Assert.assertEquals( cfg.getDsParameter(), configurationDAO.find( 1 ).getDsParameter() );
  }

  /**
   * Test remove
   */
  @Test
  public void testRemove()
  {
    int antes = configurationDAO.count();
    ConfigurationDO cfg = configurationDAO.find( 1 );
    configurationDAO.remove( cfg );
    Assert.assertEquals( antes - 1, configurationDAO.count() );
  }

  /**
   * Test findByParameterName
   */
  @Test
  public void testFindByParameterName()
  {
    ConfigurationDO cfg = configurationDAO.findByParameterName( "WEEK_FINAL_DAY" );
    Assert.assertNotNull( cfg );
  }

  /**
   * Test findByParameterName and expected exception notSingle Result
   */
  @Test(expected = NoResultException.class)
  public void testFindByParameterName_exceptionSingleResult()
  {
    ConfigurationDO cfg = configurationDAO.findByParameterName( "WEEK_FINAL_DAYX" );
    Assert.assertNotNull( cfg );
  }
}
