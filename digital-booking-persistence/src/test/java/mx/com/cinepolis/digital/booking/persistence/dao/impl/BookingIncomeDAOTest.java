package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeDetailTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingIncomeDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingIncomeDAO;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class BookingIncomeDAOTest extends AbstractDBEJBTestUnit
{

  private BookingIncomeDAO bookingIncomeDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    bookingIncomeDAO = new BookingIncomeDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    this.initializeData( "dataset/business/incomes.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( bookingIncomeDAO );
  }

  @Test
  public void testFindAll()
  {
    List<BookingIncomeDO> incomes = bookingIncomeDAO.findAll();
    Assert.assertNotNull( incomes );
    Assert.assertEquals( 28, incomes.size() );
  }

  @Test
  public void testFindById()
  {
    BookingIncomeDO bookingIncomeDO = bookingIncomeDAO.find( 1L );
    Assert.assertNotNull( bookingIncomeDO );
  }

  @Test
  public void testInsert()
  {
    int qt = this.bookingIncomeDAO.count();
    BookingIncomeDO entity = new BookingIncomeDO();

    entity.setIdBooking( new BookingDO( 1L ) );
    entity.setIdScreen( new ScreenDO( 1 ) );
    entity.setIdWeek( new WeekDO( 1 ) );
    entity.setIdTheater( new TheaterDO( 1 ) );
    entity.setIdEvent( new EventDO( 1L ) );

    Calendar cal = Calendar.getInstance();
    cal.set( 2015, Calendar.FEBRUARY, 6 );

    entity.setDtShow( cal.getTime() );
    entity.setHrShow( "19:00" );
    entity.setQtIncome( 1000.0 );
    entity.setQtTickets( 20 );

    entity.setFgActive( true );
    entity.setIdLastUserModifier( 1 );
    entity.setDtLastModification( new Date() );

    bookingIncomeDAO.create( entity );

    Assert.assertEquals( qt + 1, this.bookingIncomeDAO.count() );
  }

  @Test
  public void testRemove()
  {
    Date timestamp = new Date();
    int user = 10;
    BookingIncomeDO bookingIncomeDO = bookingIncomeDAO.find( 1L );
    Assert.assertNotNull( bookingIncomeDO );
    bookingIncomeDO.setDtLastModification( timestamp );
    bookingIncomeDO.setIdLastUserModifier( user );

    bookingIncomeDAO.remove( bookingIncomeDO );
    bookingIncomeDAO.flush();
    bookingIncomeDO = bookingIncomeDAO.find( 1L );
    Assert.assertNotNull( bookingIncomeDO );
    Assert.assertFalse( bookingIncomeDO.isFgActive() );
    Assert.assertEquals( user, bookingIncomeDO.getIdLastUserModifier() );
    Assert.assertEquals( timestamp, bookingIncomeDO.getDtLastModification() );

  }

  @Test
  @Ignore
  public void testSynchronizeIncomes()
  {
    int qt = bookingIncomeDAO.count();
    TheaterTO theater = new TheaterTO( 72L );
    theater.setUserId( 1L );
    theater.setTimestamp( new Date() );
    bookingIncomeDAO.synchronizeIncomes( theater );

    Assert.assertTrue( bookingIncomeDAO.count() >= qt );

    IncomeTO income = new IncomeTO();
    income.setTheater( new TheaterTO( 72L ) );
    income.setWeek( new WeekTO( 288 ) );
    List<IncomeTO> incomes = this.bookingIncomeDAO.findAllByIdTheaterIdWeek( income );
    Assert.assertNotNull( incomes );
    // for( IncomeTO i : incomes )
    // {
    // System.out.println( i );
    // }

    DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
    List<BookingIncomeDO> saved = this.bookingIncomeDAO.findAll();
    List<String> inserts = new ArrayList<String>();
    for( BookingIncomeDO s : saved )
    {
      StringBuilder sb = new StringBuilder();
      sb.append( "INSERT INTO K_BOOKING_INCOME ( ID_BOOKING_INCOME, ID_BOOKING, ID_SCREEN, ID_WEEK, ID_THEATER, " );
      sb.append( "ID_EVENT, DT_SHOW, HR_SHOW,  QT_INCOME, QT_TICKETS, FG_ACTIVE, DT_LAST_MODIFICATION, ID_LAST_USER_MODIFIER ) VALUES (" );
      sb.append( s.getIdBookingIncome() ).append( "," );
      sb.append( s.getIdBooking().getIdBooking() ).append( "," );
      sb.append( s.getIdScreen().getIdScreen() ).append( "," );
      sb.append( s.getIdWeek().getIdWeek() ).append( "," );
      sb.append( s.getIdTheater().getIdTheater() ).append( "," );
      sb.append( s.getIdEvent().getIdEvent() ).append( "," );
      sb.append( "'" ).append( df.format( s.getDtShow() ) ).append( "'," );
      sb.append( "'" ).append( s.getHrShow() ).append( "'," );
      sb.append( s.getQtIncome() ).append( "," );
      sb.append( s.getQtTickets() ).append( "," );
      sb.append( "1, '2014-03-10 15:00:00', 1)" );
      inserts.add( sb.toString() );
    }
    for( String insert : inserts )
    {
      System.out.println( insert );
    }
  }

  @Test
  public void testFindAllByIdTheaterIdWeek()
  {
    IncomeTO income = new IncomeTO();
    income.setTheater( new TheaterTO( 1L ) );
    income.setWeek( new WeekTO( 1 ) );
    List<IncomeTO> incomes = this.bookingIncomeDAO.findAllByIdTheaterIdWeek( income );
    Assert.assertNotNull( incomes );
    Assert.assertEquals( 28, incomes.size() );
  }

  @Test
  public void testFindIncomeDetail()
  {
    IncomeDetailTO request = new IncomeDetailTO();
    request.setEventId( 1L );
    request.setTheaterId( 1L );
    request.setScreenId( 1 );
    request.setWeekId( 1 );

    IncomeDetailTO response = this.bookingIncomeDAO.findIncomeDetail( request );
    Assert.assertNotNull( response );
    System.out.println( ToStringBuilder.reflectionToString( response, ToStringStyle.MULTI_LINE_STYLE ) );

  }

  @Test
  public void testGetWeekendIncomeByMovie()
  {
    IncomeTreeTO movie = new IncomeTreeTO();
    movie.setBooking( new BookingTO() );
    movie.getBooking().setTheater( new TheaterTO( 1L ) );
    movie.getBooking().setEvent( new EventTO() );
    movie.getBooking().getEvent().setIdEvent( 1L );
    WeekTO week = new WeekTO( 1 );
    double income = this.bookingIncomeDAO.getWeekendIncomeByMovie( movie, week );
    Assert.assertEquals( 60000.0, income, 0.1 );
  }

  @Test
  public void testGetWeekIncomeByMovie()
  {
    IncomeTreeTO movie = new IncomeTreeTO();
    movie.setBooking( new BookingTO() );
    movie.getBooking().setTheater( new TheaterTO( 1L ) );
    movie.getBooking().setEvent( new EventTO() );
    movie.getBooking().getEvent().setIdEvent( 1L );
    WeekTO week = new WeekTO( 1 );
    double income = this.bookingIncomeDAO.getWeekIncomeByMovie( movie, week );
    Assert.assertEquals( 140000.0, income, 0.1 );
  }

  @Test
  public void testGetWeekendIncomeByScreen()
  {
    IncomeTreeTO screen = new IncomeTreeTO();
    screen.setBooking( new BookingTO() );
    screen.getBooking().setTheater( new TheaterTO( 1L ) );
    screen.getBooking().setScreen( new ScreenTO( 1L ) );
    WeekTO week = new WeekTO( 1 );
    double income = this.bookingIncomeDAO.getWeekendIncomeByScreen( screen, week );
    Assert.assertEquals( 30000.0, income, 0.1 );
  }

  @Test
  public void testGetWeekIncomeByScreen()
  {
    IncomeTreeTO screen = new IncomeTreeTO();
    screen.setBooking( new BookingTO() );
    screen.getBooking().setTheater( new TheaterTO( 1L ) );
    screen.getBooking().setScreen( new ScreenTO( 1L ) );
    WeekTO week = new WeekTO( 1 );
    double income = this.bookingIncomeDAO.getWeekIncomeByScreen( screen, week );
    Assert.assertEquals( 70000.0, income, 0.1 );
  }
}
