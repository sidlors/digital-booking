package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportTheaterTO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ReportDAO;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReportDAOTest extends AbstractDBEJBTestUnit
{

  private ReportDAO reportDAO;
  private BookingDAO bookingDAO;
  
  @Before
  public void setUp()
  {
    // instanciar el servicio
    reportDAO = new ReportDAOImpl();
    bookingDAO = new BookingDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
     this.initializeData( "dataset/business/weeklybookingreport.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( reportDAO );
    connect( bookingDAO );
  }
  
  @Test
  public void testFindWeeklyBookingReportTheaterTO()
  {

    WeeklyBookingReportTheaterTO weeklyBookingReportTheaterTO = this.reportDAO.findWeeklyBookingReportTheaterTO( 15L,  1L, Language.SPANISH );
    Assert.assertNotNull( weeklyBookingReportTheaterTO );
    System.out.println( ToStringBuilder.reflectionToString( weeklyBookingReportTheaterTO,
        ToStringStyle.MULTI_LINE_STYLE ) );
    for( WeeklyBookingReportMovieTO movie : weeklyBookingReportTheaterTO.getMovies() )
    {
      System.out.println( ToStringBuilder.reflectionToString( movie,
          ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }
  
  @Test
  public void testFindAllWeeklyBookingReportTheaterTO()
  {

    List<WeeklyBookingReportTheaterTO> weeklyBookingReportTheatersTO = this.reportDAO.findAllWeeklyBookingReportTheaterTO( 15L, 1L, Language.SPANISH );
    Assert.assertNotNull( weeklyBookingReportTheatersTO );

    System.out.println("Size:::"+weeklyBookingReportTheatersTO.size());
    for( WeeklyBookingReportTheaterTO theater : weeklyBookingReportTheatersTO )
    {
      System.out.println( ToStringBuilder.reflectionToString( theater, ToStringStyle.MULTI_LINE_STYLE ) );
      for( WeeklyBookingReportMovieTO movie : theater.getMovies() )
      {
        System.out.println( ToStringBuilder.reflectionToString( movie, ToStringStyle.MULTI_LINE_STYLE ) );
      }
    }
  }
  
  @Test
  public void testFindWeeklyBookingReportTheaterTOByDistributor()
  {

    List<WeeklyBookingReportTheaterTO> weeklyBookingReportTheatersTO = this.reportDAO.findWeeklyBookingReportTheaterTOByDistributor( 15L, 1L, 1L,Language.SPANISH );
    Assert.assertNotNull( weeklyBookingReportTheatersTO );

    System.out.println("Size:::"+weeklyBookingReportTheatersTO.size());
    for( WeeklyBookingReportTheaterTO theater : weeklyBookingReportTheatersTO )
    {
      System.out.println( ToStringBuilder.reflectionToString( theater, ToStringStyle.MULTI_LINE_STYLE ) );
      for( WeeklyBookingReportMovieTO movie : theater.getMovies() )
      {
        System.out.println( ToStringBuilder.reflectionToString( movie, ToStringStyle.MULTI_LINE_STYLE ) );
      }
    }
  }
  
  @Test
  public void testFindWeeklyBookingReportTheaterTOByAllDistributors()
  {

    List<WeeklyBookingReportTheaterTO> weeklyBookingReportTheatersTO = this.reportDAO.findWeeklyBookingReportTheaterTOByAllDistributors(15L,1L, Language.SPANISH );
    Assert.assertNotNull( weeklyBookingReportTheatersTO );

    System.out.println("Size:::"+weeklyBookingReportTheatersTO.size());
    for( WeeklyBookingReportTheaterTO theater : weeklyBookingReportTheatersTO )
    {
      System.out.println( ToStringBuilder.reflectionToString( theater, ToStringStyle.MULTI_LINE_STYLE ) );
      for( WeeklyBookingReportMovieTO movie : theater.getMovies() )
      {
        System.out.println( ToStringBuilder.reflectionToString( movie, ToStringStyle.MULTI_LINE_STYLE ) );
      }
    }
  }
  /**
   * Funcion para buscar programacion de movies y eventos especiales 
   * 
   */
  @Test
  public void testFindAllWeeklyBookingSpecialEventReportTheaterTO()
  {

    WeeklyBookingReportTheaterTO weeklyBookingReportSpecialEvent = this.reportDAO.findWeeklyBookingReportTheaterTO( 15L, 1L,  Language.SPANISH );
    Assert.assertNotNull( weeklyBookingReportSpecialEvent );//findWeeklyBookingReportTheaterTO

    System.out.println( ToStringBuilder.reflectionToString( weeklyBookingReportSpecialEvent, ToStringStyle.MULTI_LINE_STYLE ) );
    
    for( WeeklyBookingReportMovieTO theater : weeklyBookingReportSpecialEvent.getMovies() )
    {
      System.out.println( ToStringBuilder.reflectionToString( theater, ToStringStyle.MULTI_LINE_STYLE ) );
     
      System.out.println("##############################################");
    }
  }
 

}
