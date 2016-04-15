package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO;
import mx.com.cinepolis.digital.booking.model.IncomeSettingsDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsTypeDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit Test for IncomeSettingsDAO class.
 * 
 * @author jreyesv
 */
public class IncomeSettingsDAOTest extends AbstractDBEJBTestUnit
{

  private IncomeSettingsDAO incomeSettingsDAO;
  private IncomeSettingsTypeDAO incomeSettingsTypeDAO;

  /**
   * Data initialization for Junit test execution.
   */
  @Before
  public void setUp()
  {
    this.incomeSettingsDAO = new IncomeSettingsDAOImpl();
    this.incomeSettingsTypeDAO = new IncomeSettingsTypeDAOImpl();
    super.setUp();
    connect( this.incomeSettingsDAO );
    connect( this.incomeSettingsTypeDAO );
  }

  /**
   * Description: Test count method. Expected: Number of records in income settings table.
   */
  @Test
  public void testCount()
  {
    int expected = 2;
    Integer size = this.incomeSettingsDAO.count();
    Assert.assertEquals( expected, size.intValue() );
  }

  /**
   * Description: Test save method. Expected: Number of records in income settings table increases at one.
   */
  @Test
  public void saveIncomeSettings()
  {
    int expected = 3;
    Long idTheater = 1L;
    int idIncomeSettingsType = 2;
    Double greenSemaphore = 39.15;
    Double yellowSemaphore = 40.50;
    Double redSemaphore = 50.50;
    Long idUser = 1L;

    IncomeSettingsTypeTO incomeSettingsType = this.incomeSettingsTypeDAO.get( idIncomeSettingsType );

    IncomeSettingsTO incomeSettingsTO = new IncomeSettingsTO();
    incomeSettingsTO.setId( null );
    incomeSettingsTO.setIdTheater( idTheater );
    incomeSettingsTO.setIncomeSettingsType( incomeSettingsType );
    incomeSettingsTO.setGreenSemaphore( greenSemaphore );
    incomeSettingsTO.setYellowSemaphore( yellowSemaphore );
    incomeSettingsTO.setRedSemaphore( redSemaphore );
    incomeSettingsTO.setTimestamp( new Date() );
    incomeSettingsTO.setUserId( idUser );
    this.incomeSettingsDAO.save( incomeSettingsTO );

    Integer size = this.incomeSettingsDAO.count();
    Assert.assertEquals( expected, size.intValue() );
  }

  /**
   * Description: Test update method. Expected: The value of attribute QtRoomCapacity does not equals after update..
   */
  @Test
  public void updateIncomeSettings()
  {
    Double greenSemaphore = 39.11;
    Long idUser = 1L;
    Long idIncomeSetting = 1L;

    Double oldAttendance = this.incomeSettingsDAO.find( idIncomeSetting.intValue() ).getQtGreenSemaphore();

    IncomeSettingsTO incomeSettingsTO = new IncomeSettingsTO();
    incomeSettingsTO.setId( idIncomeSetting );
    incomeSettingsTO.setGreenSemaphore( greenSemaphore );
    incomeSettingsTO.setTimestamp( new Date() );
    incomeSettingsTO.setUserId( idUser );
    this.incomeSettingsDAO.update( incomeSettingsTO );

    IncomeSettingsDO updated = this.incomeSettingsDAO.find( idIncomeSetting.intValue() );
    Assert.assertNotEquals( oldAttendance, updated.getQtGreenSemaphore() );
  }

  @Test
  public void testFindIncomeSettingsByTheater()
  {
    Long idTheater = 1L;
    List<IncomeSettingsTO> settings = this.incomeSettingsDAO.findIncomeSettingsByTheater( idTheater );

    Assert.assertNotNull( settings );
    Assert.assertFalse( settings.isEmpty() );
    Assert.assertEquals( 2, settings.size() );
    for( IncomeSettingsTO to : settings )
    {
      Assert.assertNotNull( to );
    }
  }
  
  @Test
  public void test_delete()
  {
    IncomeSettingsTO incomeSettingTO=new IncomeSettingsTO();
    incomeSettingTO.setId( 1L );
    incomeSettingTO.setUserId( 1L );
    this.incomeSettingsDAO.delete( incomeSettingTO );
    IncomeSettingsDO incomeSettingsDO= this.incomeSettingsDAO.find( 1 );
    Assert.assertNotNull( incomeSettingsDO );
    Assert.assertEquals( incomeSettingsDO.isFgActive(), false );
    
    
    
    
  }
}
