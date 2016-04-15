package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO;
import mx.com.cinepolis.digital.booking.model.IncomeSettingsTypeDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsTypeDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test for IncomeSettingsTypeDAO class.
 * 
 * @author jreyesv
 */
public class IncomeSettingsTypeDAOTest extends AbstractDBEJBTestUnit
{

  private IncomeSettingsTypeDAO incomeSettingsTypeDAO;

  /**
   * Data initialization for Junit test execution.
   */
  @Before
  public void setUp()
  {
    this.incomeSettingsTypeDAO = new IncomeSettingsTypeDAOImpl();
    super.setUp();
    connect( this.incomeSettingsTypeDAO );
  }

  /**
   * Description: Test find method.
   * Expected: An IncomeSettingsTypeDO object not null.
   */
  @Test
  public void testFind()
  {
    IncomeSettingsTypeDO incomeSettingsTypeDO = this.incomeSettingsTypeDAO.find( 1 );
    Assert.assertNotNull( incomeSettingsTypeDO );
  }

  /**
   * Description: Test findAll method.
   * Expected: An IncomeSettingsTypeDO list not null and not empty.
   */
  @Test
  public void testFindAll()
  {
    List<IncomeSettingsTypeDO> list = this.incomeSettingsTypeDAO.findAll();
    Assert.assertNotNull( list );
    Assert.assertFalse( list.isEmpty() );
  }

  /**
   * Description: Test get method.
   * Expected: An IncomeSettingsTypeTO object not null.
   */
  @Test
  public void testGet()
  {
    IncomeSettingsTypeTO to = this.incomeSettingsTypeDAO.get( 1 );
    Assert.assertNotNull( to );
    System.out.println( to );
  }

  /**
   * Description: Test get method with language parameter.
   * Expected: An IncomeSettingsTypeTO object not null.
   */
  @Test
  public void testGetLanguage()
  {
    IncomeSettingsTypeTO to = this.incomeSettingsTypeDAO.get( 1, Language.SPANISH );
    Assert.assertNotNull( to );
    System.out.println( to );
  }

  /**
   * Description: Test getAll method.
   * Expected: An IncomeSettingsTypeTO list not null and not empty.
   */
  @Test
  public void testGetAll()
  {
    List<IncomeSettingsTypeTO> list = this.incomeSettingsTypeDAO.getAll();
    Assert.assertNotNull( list );
    Assert.assertFalse( list.isEmpty() );
    for( CatalogTO to : list )
    {
      System.out.println( to );
    }
  }

  /**
   * Description: Test getAll method with language parameter.
   * Expected: An IncomeSettingsTypeTO list not null and not empty.
   */
  @Test
  public void testGetAllLanguage()
  {
    List<IncomeSettingsTypeTO> list = this.incomeSettingsTypeDAO.getAll( Language.SPANISH );
    Assert.assertNotNull( list );
    Assert.assertFalse( list.isEmpty() );
    for( CatalogTO to : list )
    {
      System.out.println( to );
    }
  }
}
