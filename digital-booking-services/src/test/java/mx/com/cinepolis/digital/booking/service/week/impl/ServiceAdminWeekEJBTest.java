/**
 * 
 */
package mx.com.cinepolis.digital.booking.service.week.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.service.week.ServiceAdminWeekEJB;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase que prueba los métos del servicio ServiceAdminWeekEJB
 * 
 * @author shernandezl
 *
 */
public class ServiceAdminWeekEJBTest extends AbstractDBEJBTestUnit 
{	
	private ServiceAdminWeekEJB serviceAdminWeekEJB;
	
	public void setUp()
	{
		serviceAdminWeekEJB = new ServiceAdminWeekEJBImpl();
		super.setUp();
		connect( serviceAdminWeekEJB );
	}
	
	/**
	 * Description: Caso que prueba la consulta paginada de semanas.
	 * Result: Un objeto de tipo PagingRequestTO, con 5 páginas y 16
	 * registros enconmtrados.
	 */
	@Test
	public void testGetCatalogWeekSummary()
	{
		int totalCount = 17;
		int pageSize = 5;
		for( int page = 0; page < 3; page++ )
		{
		  PagingRequestTO pagingRequestTO = new PagingRequestTO();
	      pagingRequestTO.setPage( page );
	      pagingRequestTO.setPageSize( pageSize );
	      PagingResponseTO<WeekTO> response = serviceAdminWeekEJB.getCatalogWeekSummary(pagingRequestTO);
	      
	      List<WeekTO> weeks = response.getElements();
	      Assert.assertNotNull( weeks );
	      Assert.assertEquals( totalCount, response.getTotalCount() );
	      Assert.assertTrue( pageSize >= weeks.size() );
	      for( WeekTO weekTO : weeks)
	      {
	    	  System.out.println( weekTO.getIdWeek() + ", " + weekTO.getNuWeek() );
	      }
		}
	}
	
	/**
	 * Description: Caso que obtiene la siguiente semana a registrar en base de datos.
	 * Result: Un Objeto de tipo weekTO con los siguientes datos:
	 *   -Número de Semana: 54
	 *   -Año: 2014
	 *   -Fecha inicio de semana: 26-12-2014
	 *   -Fecha fin de semana: 1-01-2015
	 */
	@Test
	  public void testGetNextNumWeek()
	  {
		WeekTO weekTO = this.serviceAdminWeekEJB.getNextWeek();
		Calendar startDay = Calendar.getInstance();
	   Calendar finalDay = Calendar.getInstance();
	   startDay.setTime( weekTO.getStartingDayWeek() );
	   finalDay.setTime( weekTO.getFinalDayWeek() );
	    
		Assert.assertNotNull(weekTO);
		Assert.assertEquals( 8, weekTO.getNuWeek());
		Assert.assertEquals(2015, weekTO.getNuYear());
		Assert.assertEquals(DateUtils.truncate( startDay.getTime(), Calendar.DATE ), weekTO.getStartingDayWeek());
		Assert.assertEquals(DateUtils.truncate( finalDay.getTime(), Calendar.DATE ), weekTO.getFinalDayWeek());
		
	  }
	
	/**
	 * Description: Caso que prueba el flujo básico para guardar una semana.
	 * Result: Un Objeto de tipo WeekTO con id asignado por la bd y espera una excepcion.
	 */
	@Test(expected = DigitalBookingException.class)
	public void testSave_Exception()
	{
		Calendar cal1 = Calendar.getInstance();
	    cal1.set( 2014, Calendar.APRIL, 18 );

	    Calendar cal2 = Calendar.getInstance();
	    cal2.set( 2014, Calendar.APRIL, 24 );

	    WeekTO weekTO = new WeekTO();
	    weekTO.setIdWeek( 1 );
	    weekTO.setNuWeek( 7 );
	    weekTO.setNuYear( 2015 );
	    weekTO.setStartingDayWeek( cal2.getTime() );
	    weekTO.setFinalDayWeek( cal1.getTime() );
	    weekTO.setTimestamp( new Date() );
	    weekTO.setUserId( 1L );
	    
	    this.serviceAdminWeekEJB.saveWeek(weekTO);
	}
	
	/**
   * Description: Caso que prueba el flujo básico para guardar una semana.
   * Result: Un Objeto de tipo WeekTO con id asignado por la bd.
   */
  @Test
  public void testSave()
  {
      Calendar cal1 = Calendar.getInstance();
      Calendar cal2 = Calendar.getInstance();
      WeekTO weekTO = new WeekTO();
      weekTO.setNuWeek( 7 );
      weekTO.setNuYear( 2015 );
      weekTO.setStartingDayWeek( cal1.getTime() );
      weekTO.setFinalDayWeek( cal2.getTime() );
      weekTO.setTimestamp( new Date() );
      weekTO.setUserId( 1L );
      
      this.serviceAdminWeekEJB.saveWeek(weekTO);
      Assert.assertNotNull(weekTO.getIdWeek());
  }
/**
 * method for save special week
 */
  @Test
  public void testSaveSpecialWeek()
  {
      Calendar cal1 = Calendar.getInstance();
      Calendar cal2 = Calendar.getInstance();
      WeekTO weekTO = new WeekTO();
      weekTO.setNuWeek( 7 );
      weekTO.setNuYear( 2015 );
      weekTO.setStartingDayWeek( cal1.getTime() );
      weekTO.setFinalDayWeek( cal2.getTime() );
      weekTO.setTimestamp( new Date() );
      weekTO.setSpecialWeek( true );
      weekTO.setUserId( 1L );
      
      this.serviceAdminWeekEJB.saveWeek(weekTO);
      Assert.assertNotNull(weekTO.getIdWeek());
  }

	/**
	 * Descripton: Método que prueba el caso base de la 
	 * actualización de un registro.
	 * Result: Un objeto de tipo WeekTO con el número de semana igual a 17.y espera una axcepcion
	 */
	@Test(expected = DigitalBookingException.class)
	public void testUpdate_Exception()
	{
		Calendar cal1 = Calendar.getInstance();
	    cal1.set( 2014, Calendar.APRIL, 11 );

	    Calendar cal2 = Calendar.getInstance();
	    cal2.set( 2014, Calendar.APRIL, 17 );

	    WeekTO weekTO = new WeekTO();
	    weekTO.setIdWeek( 15 );
	    weekTO.setNuWeek( 17 );
	    weekTO.setNuYear( 2014 );
	    weekTO.setStartingDayWeek( cal1.getTime() );
	    weekTO.setFinalDayWeek( cal2.getTime() );
	    weekTO.setTimestamp( new Date() );
	    weekTO.setUserId( 1L );
	    weekTO.setSpecialWeek(false);
	    this.serviceAdminWeekEJB.updateWeek(weekTO);
	    Assert.assertNotNull(weekTO);
	    Assert.assertEquals( 17, weekTO.getNuWeek());
	}
	/**
   * Descripton: Método que prueba el caso base de la 
   * actualización de un registro.
   * Result: Un objeto de tipo WeekTO con el número de semana igual a 17.
   */
  @Test
  public void testUpdate()
  {
    Calendar cal1 = Calendar.getInstance();
      Calendar cal2 = Calendar.getInstance();
      WeekTO weekTO = new WeekTO();
      weekTO.setIdWeek( 15 );
      weekTO.setNuWeek( 17 );
      weekTO.setNuYear( 2015 );
      weekTO.setStartingDayWeek( cal1.getTime() );
      weekTO.setFinalDayWeek( cal2.getTime() );
      weekTO.setTimestamp( new Date() );
      weekTO.setUserId( 1L );
      weekTO.setSpecialWeek(false);
      this.serviceAdminWeekEJB.updateWeek(weekTO);
      Assert.assertNotNull(weekTO);
      Assert.assertEquals( 17, weekTO.getNuWeek());
  }
  /**
   * method for test delete week and expected a exception
   */
  @Test(expected = DigitalBookingException.class)
  public void testDelete_Exception()
  {
    WeekTO weekTO=new WeekTO();
    weekTO.setIdWeek( 1 );
    weekTO.setActiveWeek( true );
    weekTO.setEditable( true );
    weekTO.setFgActive( true );
    weekTO.setFinalDayWeek( new Date() );
    weekTO.setIdLanguage( 1L );
    weekTO.setNuWeek( 7 );
    weekTO.setNuYear( 2015 );
    weekTO.setSpecialWeek( false );
    weekTO.setStartingDayWeek( new Date() );
    weekTO.setTimestamp( new Date() );
    weekTO.setUserId( 1L );
    weekTO.setUsername( "yisus" );
    this.serviceAdminWeekEJB.deleteWeek( weekTO );
  }
  /**
   * method for test delete week and expected a exception
   */
  @Test
  public void testDelete()
  {
    
    WeekTO weekTO=new WeekTO();
    weekTO.setIdWeek( 13 );
    weekTO.setActiveWeek( true );
    weekTO.setEditable( true );
    weekTO.setFgActive( true );
    weekTO.setFinalDayWeek( new Date() );
    weekTO.setIdLanguage( 1L );
    weekTO.setNuWeek( 7 );
    weekTO.setNuYear( 2015 );
    weekTO.setSpecialWeek( false );
    weekTO.setStartingDayWeek( new Date() );
    weekTO.setTimestamp( new Date() );
    weekTO.setUserId( 1L );
    weekTO.setUsername( "yisus" );
    this.serviceAdminWeekEJB.deleteWeek( weekTO );
  }
  /**
   *method for update specialWeek
   */
  @Test
  public void testUpdateSpecialWeek()
  {
    Calendar cal1 = Calendar.getInstance();
      //cal1.set( 2014, Calendar.APRIL, 11 );

      Calendar cal2 = Calendar.getInstance();
      //cal2.set( 2014, Calendar.APRIL, 17 );

      WeekTO weekTO = new WeekTO();
      weekTO.setIdWeek( 15 );
      weekTO.setNuWeek( 17 );
      weekTO.setNuYear( 2015 );
      weekTO.setStartingDayWeek( cal1.getTime() );
      weekTO.setFinalDayWeek( cal2.getTime() );
      weekTO.setTimestamp( new Date() );
      weekTO.setUserId( 1L );
      weekTO.setSpecialWeek(true);
      this.serviceAdminWeekEJB.updateWeek(weekTO);
  }

  /**
   *method for update with invalid number of weeks 
   */
  @Test(expected = DigitalBookingException.class)
  public void testUpdate_invalid_NumberOf_Week()
  {
      Calendar cal1 = Calendar.getInstance();
      Calendar cal2 = Calendar.getInstance();
      WeekTO weekTO = new WeekTO();
      weekTO.setIdWeek( 15 );
      weekTO.setNuWeek( 60 );
      weekTO.setNuYear( 2015 );
      weekTO.setStartingDayWeek( cal1.getTime() );
      weekTO.setFinalDayWeek( cal2.getTime() );
      weekTO.setTimestamp( new Date() );
      weekTO.setUserId( 1L );
      weekTO.setSpecialWeek(true);
      this.serviceAdminWeekEJB.updateWeek(weekTO);
  }
  /**
   * method for test save with the weekTO null
   */
  @Test(expected = DigitalBookingException.class)
  public void testSave_WeekTONull()
  {
    WeekTO weekTO=null;
   this.serviceAdminWeekEJB.saveWeek( weekTO ); 
  }
  /**
   * method for test save with the week = 0
   */
  @Test(expected = DigitalBookingException.class)
  public void testSave_MinNumWeek()
  {
    WeekTO weekTO=new WeekTO();
    weekTO.setNuWeek( 0 );
   this.serviceAdminWeekEJB.saveWeek( weekTO ); 
  }
  /**
   * method for test save with the invalid number of week
   */
  @Test(expected = DigitalBookingException.class)
  public void testSave_invalidNumberOfWeek()
  {
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    WeekTO weekTO = new WeekTO();
    weekTO.setIdWeek( 15 );
    weekTO.setNuWeek( 60 );
    weekTO.setNuYear( 2015 );
    weekTO.setStartingDayWeek( cal1.getTime() );
    weekTO.setFinalDayWeek( cal2.getTime() );
    weekTO.setTimestamp( new Date() );
    weekTO.setUserId( 1L );
    weekTO.setSpecialWeek(true);
   this.serviceAdminWeekEJB.saveWeek( weekTO ); 
  }
}
