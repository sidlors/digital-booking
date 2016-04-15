package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.query.WeekQuery;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WeekDAOTest extends AbstractDBEJBTestUnit
{
  private WeekDAO weekDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    weekDAO = new WeekDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( weekDAO );

  }

  @Test
  public void testRemoveWeekDO()
  {
    int n = this.weekDAO.count();
    WeekDO weekDO = this.weekDAO.find( 1 );
    Assert.assertTrue( weekDO.isFgActive() );
    this.weekDAO.remove( weekDO );
    Assert.assertEquals( n, this.weekDAO.count() );
    weekDO = this.weekDAO.find( 1 );
    Assert.assertFalse( weekDO.isFgActive() );
  }

  @Test(expected = DigitalBookingException.class)
  public void testRemoveWeekDO_NonExistent()
  {
    WeekDO weekDO = new WeekDO( 1000 );
    Assert.assertTrue( weekDO.isFgActive() );
    this.weekDAO.remove( weekDO );
  }

  @Test
  public void testSave()
  {
    Calendar cal1 = Calendar.getInstance();
    cal1.set( 2014, Calendar.APRIL, 18 );

    Calendar cal2 = Calendar.getInstance();
    cal2.set( 2014, Calendar.APRIL, 24 );

    WeekTO weekTO = new WeekTO();
    weekTO.setNuWeek( 16 );
    weekTO.setNuYear( 2014 );
    weekTO.setStartingDayWeek( cal1.getTime() );
    weekTO.setFinalDayWeek( cal2.getTime() );
    weekTO.setTimestamp( new Date() );
    weekTO.setUserId( 1L );

    this.weekDAO.save( weekTO );

    int id = weekTO.getIdWeek();

    WeekDO weekDO = this.weekDAO.find( id );
    Assert.assertNotNull( weekDO );

  }

  @Test
  public void testGet()
  {
    WeekTO to = this.weekDAO.getWeek( 1 );
    Assert.assertNotNull( to );
    System.out.println( to );
  }

  @Test(expected = DigitalBookingException.class)
  public void testGet_NonExistent()
  {
    this.weekDAO.getWeek( 10000 );
  }

  @Test
  public void testUpdate()
  {
    WeekTO to = this.weekDAO.getWeek( 1 );
    Assert.assertEquals( 2014, DateUtils.toCalendar( to.getStartingDayWeek() ).get( Calendar.YEAR ) );
    Assert.assertEquals( Calendar.JANUARY, DateUtils.toCalendar( to.getStartingDayWeek() ).get( Calendar.MONTH ) );
    Assert.assertEquals( 3, DateUtils.toCalendar( to.getStartingDayWeek() ).get( Calendar.DATE ) );

    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 11 );

    to.setNuWeek( 16 );
    to.setNuYear( 2014 );
    to.setStartingDayWeek( cal.getTime() );
    this.weekDAO.update( to );

    to = this.weekDAO.getWeek( 1 );

    Assert.assertEquals( 2014, DateUtils.toCalendar( to.getStartingDayWeek() ).get( Calendar.YEAR ) );
    Assert.assertEquals( Calendar.JANUARY, DateUtils.toCalendar( to.getStartingDayWeek() ).get( Calendar.MONTH ) );
    Assert.assertEquals( 11, DateUtils.toCalendar( to.getStartingDayWeek() ).get( Calendar.DATE ) );

  }
  
  @Test(expected=DigitalBookingException.class)
  public void testUpdate_NonExistent()
  {
    WeekTO to = new WeekTO();
    to.setIdWeek( 10000 );

    this.weekDAO.update( to );

  }

  @Test
  public void testDelete()
  {
    int n = this.weekDAO.count();

    WeekTO weekTO = new WeekTO();
    weekTO.setIdWeek( 1 );
    weekTO.setUserId( 1L );
    weekTO.setTimestamp( new Date() );
    this.weekDAO.delete( weekTO );
    Assert.assertEquals( n, this.weekDAO.count() );
  }
  
  @Test(expected=DigitalBookingException.class)
  public void testDelete_NonExistent()
  {
    WeekTO weekTO = new WeekTO();
    weekTO.setUserId( 1L );
    weekTO.setTimestamp( new Date() );
    weekTO.setIdWeek( 10000 );
    this.weekDAO.delete( weekTO );
  }

  @Test
  public void testFindAllByPaging()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO request = new PagingRequestTO();
    request.setPage( page );
    request.setPageSize( pageSize );
    request.setFilters(new HashMap<ModelQuery, Object>());
    request.getFilters().put(WeekQuery.WEEK_ACTIVE, true);

    PagingResponseTO<WeekTO> response = this.weekDAO.findAllByPaging( request );

    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );

    for( WeekTO weekTO : response.getElements() )
    {
      System.out.println( weekTO );
    }

  }

  @Test
  public void testFindAllByPaging_SortByWeekIdAsc()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO request = new PagingRequestTO();
    request.setPage( page );
    request.setPageSize( pageSize );
    request.setSort( new ArrayList<ModelQuery>() );
    request.getSort().add( WeekQuery.WEEK_ID );
    request.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<WeekTO> response = this.weekDAO.findAllByPaging( request );

    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );

    for( WeekTO weekTO : response.getElements() )
    {
      System.out.println( weekTO );
    }

  }

  @Test
  public void testFindAllByPaging_SortByWeekIdDesc()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO request = new PagingRequestTO();
    request.setPage( page );
    request.setPageSize( pageSize );
    request.setSort( new ArrayList<ModelQuery>() );
    request.getSort().add( WeekQuery.WEEK_ID );
    request.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<WeekTO> response = this.weekDAO.findAllByPaging( request );

    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );

    for( WeekTO weekTO : response.getElements() )
    {
      System.out.println( weekTO );
    }

  }

  @Test
  public void testGetCurrentWeeks()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 17 );
    Date date = cal.getTime();

    List<WeekTO> weeks = this.weekDAO.getCurrentWeeks( date,true );
    Assert.assertNotNull( weeks );
    Assert.assertEquals( 5, weeks.size() );
    for( WeekTO week : weeks )
    {
      System.out.println( week );
    }

  }

  @Test
  public void testFindAllByPaging_SortByWeekStartAsc()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO request = new PagingRequestTO();
    request.setPage( page );
    request.setPageSize( pageSize );
    request.setSort( new ArrayList<ModelQuery>() );
    request.getSort().add( WeekQuery.WEEK_START );
    request.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<WeekTO> response = this.weekDAO.findAllByPaging( request );

    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );

    for( WeekTO weekTO : response.getElements() )
    {
      System.out.println( weekTO );
    }

  }

  @Test
  public void testFindAllByPaging_FilterByWeekId()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO request = new PagingRequestTO();
    request.setPage( page );
    request.setPageSize( pageSize );
    request.setFilters( new HashMap<ModelQuery, Object>() );
    request.getFilters().put( WeekQuery.WEEK_ID, 1 );

    PagingResponseTO<WeekTO> response = this.weekDAO.findAllByPaging( request );

    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );

    for( WeekTO weekTO : response.getElements() )
    {
      System.out.println( weekTO );
    }

  }

  @Test
  public void testFindAllByPaging_FilterByWeekStart()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO request = new PagingRequestTO();
    request.setPage( page );
    request.setPageSize( pageSize );
    request.setFilters( new HashMap<ModelQuery, Object>() );
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 3 );
    request.getFilters().put( WeekQuery.WEEK_START, cal.getTime() );

    PagingResponseTO<WeekTO> response = this.weekDAO.findAllByPaging( request );

    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );

    for( WeekTO weekTO : response.getElements() )
    {
      System.out.println( weekTO );
    }

  }

  @Test
  public void testFindAllByPaging_FilterByWeekEnd()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO request = new PagingRequestTO();
    request.setPage( page );
    request.setPageSize( pageSize );
    request.setFilters( new HashMap<ModelQuery, Object>() );
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 16 );
    request.getFilters().put( WeekQuery.WEEK_END, cal.getTime() );

    PagingResponseTO<WeekTO> response = this.weekDAO.findAllByPaging( request );

    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );

    for( WeekTO weekTO : response.getElements() )
    {
      System.out.println( weekTO );
    }

  }

  @Test
  public void testFindAllByPaging_FilterByWeekDay()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO request = new PagingRequestTO();
    request.setPage( page );
    request.setPageSize( pageSize );
    request.setFilters( new HashMap<ModelQuery, Object>() );
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.JANUARY, 10 );
    request.getFilters().put( WeekQuery.WEEK_DAY, cal.getTime() );

    PagingResponseTO<WeekTO> response = this.weekDAO.findAllByPaging( request );

    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );

    for( WeekTO weekTO : response.getElements() )
    {
      System.out.println( weekTO );
    }

  }

  @Test
  public void testFindAllByPaging_SortByWeekEndAsc()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO request = new PagingRequestTO();
    request.setPage( page );
    request.setPageSize( pageSize );
    request.setSort( new ArrayList<ModelQuery>() );
    request.getSort().add( WeekQuery.WEEK_END );
    request.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<WeekTO> response = this.weekDAO.findAllByPaging( request );

    Assert.assertNotNull( response );
    Assert.assertNotNull( response.getElements() );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );

    for( WeekTO weekTO : response.getElements() )
    {
      System.out.println( weekTO );
    }

  }

  @Test
  public void testGetSpecialWeeks()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.FEBRUARY, 21 );
    Date start = cal.getTime();

    cal = Calendar.getInstance();
    cal.set( 2014, Calendar.MARCH, 13 );
    Date end = cal.getTime();
    List<WeekTO> weeks = this.weekDAO.getSpecialWeeks( start, end );

    Assert.assertNotNull( weeks );
    Assert.assertEquals( 1, weeks.size() );
    for( WeekTO week : weeks )
    {
      System.out.println( week );
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
	  WeekTO weekTO = this.weekDAO.getNextWeek(2014);
	  Assert.assertNotNull(weekTO);
	  Assert.assertEquals( 53, weekTO.getNuWeek());
	  Assert.assertEquals(2014, weekTO.getNuYear());
  }
  
  @Test
  public void testGetWeekByDates()
  {
	  WeekTO weekTO = new WeekTO();
	  Calendar cal = Calendar.getInstance();
	  cal.set( 2014, Calendar.DECEMBER, 18 );
	  weekTO.setStartingDayWeek(DateUtils.truncate( cal.getTime(), Calendar.DATE ));
	  
	  cal = Calendar.getInstance();
	  cal.set( 2014, Calendar.DECEMBER, 24 );
	  weekTO.setFinalDayWeek(DateUtils.truncate( cal.getTime(), Calendar.DATE ));
	  weekTO.setSpecialWeek(false);
	  
	  WeekTO resultTO = this.weekDAO.getWeekByDates(weekTO);
	  Assert.assertNotNull(resultTO);
	  Assert.assertEquals( weekTO.getStartingDayWeek(),  resultTO.getStartingDayWeek());
	  Assert.assertEquals( weekTO.getFinalDayWeek(),  resultTO.getFinalDayWeek());
	  
  }
}
