package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.SpecialEventWeekDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.SpecialEventWeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SpecialEventWeekDAOTest  extends AbstractDBEJBTestUnit
{
  private SpecialEventWeekDAO specialEventWeekDAO;
  
  private WeekDAO weekDAO;
  
  @Before
  public void setUp()
  {
    specialEventWeekDAO=new SpecialEventWeekDAOImpl();
    weekDAO=new WeekDAOImpl();
    super.setUp();
    connect(specialEventWeekDAO);
    connect(weekDAO);
  }
  
  @Test 
  public void findWeek()
  {
    Calendar cal1 = Calendar.getInstance();
    cal1.set( 2014, Calendar.DECEMBER, 17 );
    Calendar cal2 = Calendar.getInstance();
    cal1.set( 2015, Calendar.JANUARY, 10 );
    //DateUtils.truncate( cal.getTime(), Calendar.DATE )
    List<WeekDO> allweeks=this.weekDAO.findAll();
    for(WeekDO weekk:allweeks)
    {
      System.out.println(weekk.getDtFinalDayWeek()+"--- "+weekk.getDtStartingDayWeek()+"---"+weekk.getIdWeek());
    }
   
    List<WeekDO> weeks=this.weekDAO.findWeeksByStartDayAndFinalDay( DateUtils.truncate(cal1.getTime(),Calendar.DATE ), DateUtils.truncate(cal2.getTime(),Calendar.DATE ) );
    System.out.println( weeks.size()+"------------------------------" );
    Assert.assertNotNull( weeks );
    Assert.assertEquals( 1, weeks.size() );
    System.out.println( weeks.size()+"------------------------------" );
    
  }
  
  @Test 
  public void testcreateSpecialEventWeek()
  {
    SpecialEventTO specialEventTO=new SpecialEventTO();
    Calendar cal1 = Calendar.getInstance();
    cal1.set( 2014, Calendar.DECEMBER, 17 );
    Calendar cal2 = Calendar.getInstance();
    cal1.set( 2015, Calendar.JANUARY, 10 );
    //DateUtils.truncate( cal.getTime(), Calendar.DATE )
    specialEventTO.setStartDay(DateUtils.truncate(cal1.getTime(),Calendar.DATE ));
    specialEventTO.setFinalDay( DateUtils.truncate(cal2.getTime(),Calendar.DATE ) );
    specialEventTO.setUserId( 1L );
    specialEventTO.setTimestamp( new Date() );
    
    
    BookingSpecialEventDO specialEventDO=new BookingSpecialEventDO();
    specialEventDO.setIdBookingSpecialEvent( 1L );
    specialEventDO.setDtLastModification( new Date() );
    specialEventDO.setIdLastUserModifier( 1 );
    specialEventDO.setDtStartSpecialEvent( DateUtils.truncate(cal1.getTime(),Calendar.DATE ) );
    specialEventDO.setDtEndSpecialEvent( DateUtils.truncate(cal2.getTime(),Calendar.DATE ) );
    int size=this.specialEventWeekDAO.count();
    
    
    
    this.specialEventWeekDAO.createSpecialEventWeek(specialEventDO, specialEventTO );
    int sizef=this.specialEventWeekDAO.count();
    Assert.assertNotEquals( size, sizef );
    System.out.println("jojojojojojojojoo"+size+"  -  "+sizef);
  }
  
  @Test 
  public void testFindAll()
  {
    SpecialEventWeekDO specialEventWeek=new SpecialEventWeekDO();
    specialEventWeek.setIdSpecialEventWeek( 1L );
    List<SpecialEventWeekDO> sEWeekList =this.specialEventWeekDAO.findAll();
    Assert.assertNotNull( sEWeekList );
    System.out.println(sEWeekList.size()+"---------");
    
  }
  
  @Test 
  public void testInsertSpecialEventWeek()
  {
    EventDO event=new EventDO();
    event.setDsName( "EL hoobit" );
    event.setIdEvent( 134L );
    
    TheaterDO theater=new TheaterDO();
    theater.setIdTheater( 130 );
    
    BookingSpecialEventDO bookingSpecialEventDO=null;
    
    bookingSpecialEventDO=new BookingSpecialEventDO();
    bookingSpecialEventDO.setDtEndSpecialEvent( new Date () );
    bookingSpecialEventDO.setDtLastModification( new Date() );
    bookingSpecialEventDO.setDtStartSpecialEvent( new Date() );
    bookingSpecialEventDO.setFgActive( true );
    BookingDO idBooking= new BookingDO();
    idBooking.setIdBooking( 990L );
    idBooking.setIdEvent( event );
    idBooking.setIdTheater( theater );
    bookingSpecialEventDO.setIdBooking( idBooking );
    bookingSpecialEventDO.setIdBookingSpecialEvent( 2L );
    bookingSpecialEventDO.setIdLastUserModifier( 1 );
    bookingSpecialEventDO.setQtCopy( 3 );
    
    WeekDO weekDO=new WeekDO();
    weekDO.setIdWeek( 1 );
    
    SpecialEventWeekDO specialEventWeek=new SpecialEventWeekDO();
    specialEventWeek.setIdSpecialEventWeek( 100L );
    specialEventWeek.setIdWeek( weekDO );
    specialEventWeek.setIdBookingSpecialEvent( bookingSpecialEventDO );
    specialEventWeek.setIdLastUserModifier( 1 );
    specialEventWeek.setDtLastModification( new Date() );
    specialEventWeek.setFgActive( true );
    int size=this.specialEventWeekDAO.count();
    this.specialEventWeekDAO.create( specialEventWeek );
    int sizef=this.specialEventWeekDAO.count();
    System.out.print( "sizes "+size+"   ---  "+sizef);
    Assert.assertNotEquals( size, sizef );
  }

}
