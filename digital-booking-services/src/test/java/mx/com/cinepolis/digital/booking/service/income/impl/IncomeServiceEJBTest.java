package mx.com.cinepolis.digital.booking.service.income.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import mx.com.cinepolis.digital.booking.commons.to.IncomeDetailTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO;
import mx.com.cinepolis.digital.booking.commons.to.SummaryTotalIncomesTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.service.income.IncomeServiceEJB;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class IncomeServiceEJBTest extends AbstractDBEJBTestUnit
{
  private IncomeServiceEJB incomeServiceEJB;

  @Before
  public void setUp()
  {
    incomeServiceEJB = new IncomeServiceEJBImpl();

    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    this.initializeData( "dataset/business/incomes-perisur.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( incomeServiceEJB );

  }

  @Ignore
  @Test
  public void testSynchronizeIncomes()
  {
    TheaterTO theater = new TheaterTO( 72L );
    theater.setTimestamp( new Date() );
    theater.setUserId( 1L );
    this.incomeServiceEJB.synchronizeIncomes( theater );
  }

  @Test
  public void testSearchIncomesByMovie()
  {
    IncomeTO income = new IncomeTO();
    income.setTheater( new TheaterTO( 72L ) );
    income.setWeek( new WeekTO( 289 ) );
    IncomeTreeTO root = this.incomeServiceEJB.searchIncomesByMovie( income );
    Assert.assertNotNull( root );
    Assert.assertEquals( IncomeTreeTO.Type.ROOT, root.getType() );
    // Assert.assertEquals( 1, root.getChildren().size() );

    IncomeTreeTO movie = root.getChildren().get( 0 );
    Assert.assertNotNull( movie );
    Assert.assertEquals( IncomeTreeTO.Type.MOVIE, movie.getType() );
    // Assert.assertEquals( 2, movie.getChildren().size() );

    IncomeTreeTO screen = movie.getChildren().get( 0 );
    Assert.assertNotNull( screen );
    Assert.assertEquals( IncomeTreeTO.Type.SCREEN, screen.getType() );
    // Assert.assertEquals( 2, screen.getChildren().size() );

    IncomeTreeTO show = screen.getChildren().get( 0 );
    Assert.assertNotNull( show );
    Assert.assertEquals( IncomeTreeTO.Type.SHOW, show.getType() );
    // Assert.assertEquals( 0, show.getChildren().size() );

    NumberFormat percetage = new DecimalFormat( "% ##0.00" );
    if(root.getSummaryTotals()!= null)
    {
      SummaryTotalIncomesTO totals=root.getSummaryTotals();
      System.out.println("Admissions week and end -->"+totals.getTotWeekAdmission() +"----"+ root.getSummaryTotals().getTotWeekendAdmission());
      System.out.println("Admission Per Shows week and end -->"+totals.getTotWeekAdmissionPerShows() +"----"+ root.getSummaryTotals().getTotWeekendAdmissionPerShows());
      
      System.out.println("Gross week and end -->"+totals.getTotWeekGross() +"----"+ root.getSummaryTotals().getTotWeekendGross());
      System.out.println("shows week and end -->"+totals.getTotWeekShows() +"----"+ root.getSummaryTotals().getTotWeekendShows());
    }

    
    for( IncomeTreeTO m : root.getChildren() )
    {
      System.out.println( m.getName() + "\t\t\t" + m.getWeekendIncome() + "\t" + m.getWeekendTickets() + "\t"
          + percetage.format( m.getWeekendOccupancy() ) + "\t" + m.getWeekIncome() + "\t" + m.getWeekTickets() + "\t"
          + percetage.format( m.getWeekOccupancy() ) + "\tweekend:"
          + percetage.format( m.getWeekendVariation() != null ? m.getWeekendVariation() : 0.0 ) + "\tweek:"
          + percetage.format( m.getWeekVariation() != null ? m.getWeekVariation() : 0.0 ) );

      for( IncomeTreeTO ss : m.getChildren() )
      {
        System.out.println( "\t" + ss.getName() + "\t\t" + ss.getWeekendIncome() + "\t" + ss.getWeekendTickets() + "\t"
            + percetage.format( ss.getWeekendOccupancy() ) + "\t" + ss.getWeekIncome() + "\t" + ss.getWeekTickets()
            + "\t" + percetage.format( ss.getWeekOccupancy() ) + "\t" );
        for( IncomeTreeTO sh : ss.getChildren() )
        {
          System.out.println( "\t\t" + sh.getName() + "\t" + sh.getWeekendIncome() + "\t" + sh.getWeekendTickets()
              + "\t" + percetage.format( sh.getWeekendOccupancy() ) + "\t" + sh.getWeekIncome() + "\t"
              + sh.getWeekTickets() + "\t" + percetage.format( sh.getWeekOccupancy() ) + "\t" );
        }
        
      }
    }
  }

  @Test
  public void testSearchIncomesByScreen()
  {
    IncomeTO income = new IncomeTO();
    income.setTheater( new TheaterTO( 72L ) );
    income.setWeek( new WeekTO( 289 ) );
    IncomeTreeTO root = this.incomeServiceEJB.searchIncomesByScreen( income );
    Assert.assertNotNull( root );
    Assert.assertEquals( IncomeTreeTO.Type.ROOT, root.getType() );
    // Assert.assertEquals( 1, root.getChildren().size() );

    IncomeTreeTO movie = root.getChildren().get( 0 );
    Assert.assertNotNull( movie );
    Assert.assertEquals( IncomeTreeTO.Type.SCREEN, movie.getType() );
    // Assert.assertEquals( 2, movie.getChildren().size() );

    IncomeTreeTO screen = movie.getChildren().get( 0 );
    Assert.assertNotNull( screen );
    Assert.assertEquals( IncomeTreeTO.Type.MOVIE, screen.getType() );
    // Assert.assertEquals( 2, screen.getChildren().size() );

    IncomeTreeTO show = screen.getChildren().get( 0 );
    Assert.assertNotNull( show );
    Assert.assertEquals( IncomeTreeTO.Type.SHOW, show.getType() );
    // Assert.assertEquals( 0, show.getChildren().size() );

    NumberFormat percetage = new DecimalFormat( "% ##0.00" );

    for( IncomeTreeTO m : root.getChildren() )
    {
      System.out.println( m.getName() + "\t\t\t" + m.getWeekendIncome() + "\t" + m.getWeekendTickets() + "\t"
          + percetage.format( m.getWeekendOccupancy() ) + "\t" + m.getWeekIncome() + "\t" + m.getWeekTickets() + "\t"
          + percetage.format( m.getWeekOccupancy() ) + "\tweekend:"
          + percetage.format( m.getWeekendVariation() != null ? m.getWeekendVariation() : 0.0 ) + "\tweek:"
          + percetage.format( m.getWeekVariation() != null ? m.getWeekVariation() : 0.0 ) );

      for( IncomeTreeTO ss : m.getChildren() )
      {
        System.out.println( "\t" + ss.getName() + "\t\t" + ss.getWeekendIncome() + "\t" + ss.getWeekendTickets() + "\t"
            + percetage.format( ss.getWeekendOccupancy() ) + "\t" + ss.getWeekIncome() + "\t" + ss.getWeekTickets()
            + "\t" + percetage.format( ss.getWeekOccupancy() ) + "\t" );
        for( IncomeTreeTO sh : ss.getChildren() )
        {
          System.out.println( "\t\t" + sh.getName() + "\t" + sh.getWeekendIncome() + "\t" + sh.getWeekendTickets()
              + "\t" + percetage.format( sh.getWeekendOccupancy() ) + "\t" + sh.getWeekIncome() + "\t"
              + sh.getWeekTickets() + "\t" + percetage.format( sh.getWeekOccupancy() ) + "\t" );
        }
      }
    }
  }

  @Test
  public void testGetLastWeek()
  {
    WeekTO lastWeek = this.incomeServiceEJB.getLastWeek( new WeekTO( 289 ) );
    Assert.assertNotNull( lastWeek );
    Assert.assertEquals( 288, lastWeek.getIdWeek().intValue() );

  }
  
  @Test
  public void testGetIncomeDetail(){
    IncomeDetailTO request = new IncomeDetailTO();
    request.setEventId( 2009L );
    request.setTheaterId( 72L );
    request.setScreenId( 678 );
    request.setWeekId( 288 );

    IncomeDetailTO response = this.incomeServiceEJB.getIncomeDetail( request );
    Assert.assertNotNull( response );
    System.out.println( ToStringBuilder.reflectionToString( response, ToStringStyle.MULTI_LINE_STYLE ) );

  }
  
  /**
   * method for test the global totals by movie
   */
  @Test
  public void testSearchIncomesByScreenTotals()
  {
    IncomeTO income = new IncomeTO();
    income.setTheater( new TheaterTO( 72L ) );
    income.setWeek( new WeekTO( 289 ) );
    IncomeTreeTO root = this.incomeServiceEJB.searchIncomesByScreen( income );
    Assert.assertNotNull( root );
    Assert.assertEquals( IncomeTreeTO.Type.ROOT, root.getType() );

    IncomeTreeTO movie = root.getChildren().get( 0 );
    Assert.assertNotNull( movie );
    Assert.assertEquals( IncomeTreeTO.Type.SCREEN, movie.getType() );
    // 

    IncomeTreeTO screen = movie.getChildren().get( 0 );
    Assert.assertNotNull( screen );
    Assert.assertEquals( IncomeTreeTO.Type.MOVIE, screen.getType() );

    IncomeTreeTO show = screen.getChildren().get( 0 );
    Assert.assertNotNull( show );
    Assert.assertEquals( IncomeTreeTO.Type.SHOW, show.getType() );

    NumberFormat percetage = new DecimalFormat( "% ##0.00" );
    if(root.getSummaryTotals()!= null)
    {
      SummaryTotalIncomesTO totals=root.getSummaryTotals();
      System.out.println("Admissions week and end -->"+totals.getTotWeekAdmission() +"----"+ root.getSummaryTotals().getTotWeekendAdmission());
      System.out.println("Admission Per Shows week and end -->"+totals.getTotWeekAdmissionPerShows() +"----"+ root.getSummaryTotals().getTotWeekendAdmissionPerShows());
      
      System.out.println("Gross week and end -->"+totals.getTotWeekGross() +"----"+ root.getSummaryTotals().getTotWeekendGross());
      System.out.println("shows week and end -->"+totals.getTotWeekShows() +"----"+ root.getSummaryTotals().getTotWeekendShows());
      
      System.out.println("screenOcuppancy week and end -->"+totals.getTotWeekScreenOccupancy() +"----"+ root.getSummaryTotals().getTotWeekendScreenOccupancy());
      //System.out.println("shows week and end -->"+totals.getTotWeekShows() +"----"+ root.getSummaryTotals().getTotWeekendShows());
    }

    Double totAdmissionWeek= new Double(0);
    Double totAdmissionWeekend= new Double(0);
    for( IncomeTreeTO m : root.getChildren() )
    {
      System.out.println( m.getName() + "\t\t\t" + m.getWeekendIncome() + "\t" + m.getWeekendTickets() + "\t"
          + percetage.format( m.getWeekendOccupancy() ) + "\t" + m.getWeekIncome() + "\t" + m.getWeekTickets() + "\t"
          + percetage.format( m.getWeekOccupancy() ) + "\tweekend:"
          + percetage.format( m.getWeekendVariation() != null ? m.getWeekendVariation() : 0.0 ) + "\tweek:"
          + percetage.format( m.getWeekVariation() != null ? m.getWeekVariation() : 0.0 ) );

      for( IncomeTreeTO ss : m.getChildren() )
      {
        System.out.println( "\t" + ss.getName() + "\t\t" + ss.getWeekendIncome() + "\t" + ss.getWeekendTickets() + "\t"
            + percetage.format( ss.getWeekendOccupancy() ) + "\t" + ss.getWeekIncome() + "\t" + ss.getWeekTickets()
            + "\t" + percetage.format( ss.getWeekOccupancy() ) + "\t" );
        for( IncomeTreeTO sh : ss.getChildren() )
        {
          System.out.println( "\t\t" + sh.getName() + "\t" + sh.getWeekendIncome() + "\t" + sh.getWeekendTickets()
              + "\t" + percetage.format( sh.getWeekendOccupancy() ) + "\t" + sh.getWeekIncome() + "\t"
              + sh.getWeekTickets() + "\t" + percetage.format( sh.getWeekOccupancy() ) + "\t" );
        }
        
      }
      totAdmissionWeek+=m.getWeekTickets();
      totAdmissionWeekend+=m.getWeekendTickets();
    }
    
    Assert.assertEquals( root.getSummaryTotals().getTotWeekAdmission() , totAdmissionWeek );
    Assert.assertEquals( root.getSummaryTotals().getTotWeekendAdmission() , totAdmissionWeekend );
  }
  
  
}
