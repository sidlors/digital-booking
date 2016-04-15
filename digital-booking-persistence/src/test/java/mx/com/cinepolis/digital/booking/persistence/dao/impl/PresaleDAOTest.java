package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.model.PresaleDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.PresaleDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Class to test the presales booking
 * 
 * @author jcarbajal
 */
public class PresaleDAOTest extends AbstractDBEJBTestUnit
{

  private PresaleDAO presaleDAO;

  @Before
  public void setUp()
  {
    presaleDAO = new PresaleDAOImpl();
    super.setUp();
    connect( presaleDAO );
  }

  /**
   * Tests the result for the method "findAll".
   */
  @Test
  public void findAllTest()
  {
    List<PresaleDO> listpresaleDO = presaleDAO.findAll();
    Assert.assertNotNull( listpresaleDO );
  }

  /**
   * Tests the result for the method "findActivePresaleForDeactivate".
   */
  @Test
  public void findActivePresaleForDeactivateTest()
  {
    List<PresaleDO> listpresaleDO = presaleDAO.findActivePresaleForDeactivate();
    Assert.assertNotNull( listpresaleDO );
    for( PresaleDO presaleDO : listpresaleDO )
    {
      Assert.assertTrue( presaleDO.isFgActive() );
    }
    Assert.assertEquals( 11, listpresaleDO.size() );
  }

  /**
   * Tests the result for the method "findActivePresaleForDeactivate".
   */
  @Test
  public void countAllActivePresalesTest()
  {
    int totalPresales = this.presaleDAO.findAll().size();
    int result = presaleDAO.countAllActivePresales();
    Assert.assertTrue( totalPresales >= result );
  }

  /**
   * method for test the function for save presale of the normal booking
   */
  @Test
  public void savePresaleBooking_Test()
  {
    PresaleTO presaleTO = new PresaleTO();
    presaleTO.setDtFinalDayPresale( new Date() );
    presaleTO.setDtReleaseDay( new Date() );
    presaleTO.setDtStartDayPresale( new Date() );
    presaleTO.setFgActive( true );
    presaleTO.setIdBookingWeekScreen( 1L );
    presaleTO.setIdLanguage( 1L );
    presaleTO.setTimestamp( new Date() );
    presaleTO.setUserId( 1L );
    presaleTO.setUsername( "yisus" );
    int size = this.presaleDAO.count();
    PresaleDO presaleDO = this.presaleDAO.save( presaleTO );
    Assert.assertNotNull( presaleDO );
    this.presaleDAO.create( presaleDO );
    int size2 = this.presaleDAO.count();
    Assert.assertNotEquals( size, size2 );
  }

  /**
   * method for test the function for save presale of the normal booking and is expected a exception
   */
  @Test(expected = DigitalBookingException.class)
  public void savePresaleBooking_TestException()
  {
    PresaleTO presaleTO = new PresaleTO();
    presaleTO.setDtFinalDayPresale( new Date() );
    presaleTO.setDtReleaseDay( new Date() );
    presaleTO.setDtStartDayPresale( new Date() );
    presaleTO.setFgActive( true );
    presaleTO.setIdLanguage( 1L );
    presaleTO.setTimestamp( new Date() );
    presaleTO.setUserId( 1L );
    presaleTO.setUsername( "yisus" );
    int size = this.presaleDAO.count();
    PresaleDO presaleDO = this.presaleDAO.save( presaleTO );
    Assert.assertNotNull( presaleDO );
    this.presaleDAO.create( presaleDO );
    int size2 = this.presaleDAO.count();
    Assert.assertNotEquals( size, size2 );
  }

  /**
   * method for test the function for save presale of the special event booking
   */
  @Test
  public void savePresaleSpecialEvent_Test()
  {
    PresaleTO presaleTO = new PresaleTO();
    presaleTO.setDtFinalDayPresale( new Date() );
    presaleTO.setDtReleaseDay( new Date() );
    presaleTO.setDtStartDayPresale( new Date() );
    presaleTO.setFgActive( true );
    presaleTO.setIdBookingSpecialEventScreen( 1L );
    presaleTO.setIdLanguage( 1L );
    // presaleTO.setIdPresale( 20L );
    presaleTO.setTimestamp( new Date() );
    presaleTO.setUserId( 1L );
    presaleTO.setUsername( "yisus" );
    int size = this.presaleDAO.count();
    PresaleDO presaleDO = this.presaleDAO.save( presaleTO );
    Assert.assertNotNull( presaleDO );
    this.presaleDAO.create( presaleDO );
    int size2 = this.presaleDAO.count();
    Assert.assertNotEquals( size, size2 );
  }

  /**
   * method for test the function for update the presale 
   */
  @Test
  public void updatePresale_Test()
  {
    PresaleDO presaleDO = this.presaleDAO.find( 1L );
    Assert.assertNotNull( presaleDO );
    boolean fgactive = presaleDO.isFgActive();
    PresaleTO presaleTO = new PresaleTO();
    presaleTO.setDtFinalDayPresale( new Date() );
    presaleTO.setDtReleaseDay( new Date() );
    presaleTO.setDtStartDayPresale( new Date() );
    if( fgactive )
      presaleTO.setFgActive( false );
    else
      presaleTO.setFgActive( true );
    presaleTO.setIdLanguage( 1L );
    presaleTO.setIdPresale( 1L );
    presaleTO.setTimestamp( new Date() );
    presaleTO.setUserId( 1L );
    presaleTO.setUsername( "yisus" );

    this.presaleDAO.update( presaleTO );
    PresaleDO presaleDO2 = this.presaleDAO.find( 1L );
    Assert.assertNotNull( presaleDO2 );

    Assert.assertNotEquals( fgactive, presaleDO2.isFgActive() );
  }
}
