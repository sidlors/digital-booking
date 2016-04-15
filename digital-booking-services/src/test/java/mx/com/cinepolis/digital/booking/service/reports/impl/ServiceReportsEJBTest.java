package mx.com.cinepolis.digital.booking.service.reports.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.service.reports.ServiceReportsEJB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServiceReportsEJBTest extends AbstractDBEJBTestUnit
{

  private ServiceReportsEJB serviceReportsEJB;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    serviceReportsEJB = new ServiceReportsEJBImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
     //this.initializeData( "dataset/business/weeklybookingreport.sql" );
     this.initializeData( "dataset/business/presale.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( serviceReportsEJB );

  }

  @Test
  public void testGetWeeklyBookingReportByTheater() throws IOException
  {
    byte[] bytes = serviceReportsEJB.getWeeklyBookingReportByTheater( 1L, 1L ).getFile();
    FileOutputStream fileOutputStream = new FileOutputStream( "reports.xlsx" );
    fileOutputStream.write( bytes );
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  @Test
  public void testGetWeeklyBookingReportByRegion() throws IOException
  {
    byte[] bytes = serviceReportsEJB.getWeeklyBookingReportByRegion( 15L, 1L ).getFile();
    FileOutputStream fileOutputStream = new FileOutputStream( "weeklyBookingReport.xlsx" );
    fileOutputStream.write( bytes );
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  @Test
  public void testGetWeeklyDistributorReportByDistributor() throws IOException
  {
    byte[] bytes = serviceReportsEJB.getWeeklyDistributorReportByDistributor( 15L, 1L, 2L ).getFile();
    FileOutputStream fileOutputStream = new FileOutputStream( "weeklyDistributorReportByDistributor.xlsx" );
    fileOutputStream.write( bytes );
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  @Test
  public void testGetWeeklyDistributorReportByAllDistributors() throws IOException
  {
    byte[] bytes = serviceReportsEJB.getWeeklyDistributorReportByAllDistributors( 15L, 1L ).getFile();
    FileOutputStream fileOutputStream = new FileOutputStream( "weeklyDistributorReportByAllDistributors.xlsx" );
    fileOutputStream.write( bytes );
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  /**
   * @author jcarbajal 
   * Description -
   */
  @Test
  public void getWeeklyBookingReportPresaleByTheater_Test() throws IOException
  {
    List<EventTO> eventSelected = new ArrayList<EventTO>();
    EventTO event = new EventTO();
    event.setDsEventName( "Hobiittt" );
    event.setId( 1762L );
    event.setPrerelease( true );
    event.setIdEvent( 1762L );

    EventTO event2 = new EventTO();
    event2.setDsEventName( "Hobiittt" );
    event2.setId( 1901L );
    event.setIdEvent( 1901L );
    event2.setPrerelease( true );
    eventSelected.add( event );
    eventSelected.add( event2 );

    Long idWeek = 289L;
    Long idTheater = 72L;

    byte[] bytes = this.serviceReportsEJB.getWeeklyBookingReportPresaleByTheater( eventSelected, idWeek, idTheater )
        .getFile();
    FileOutputStream fileOutputStream = new FileOutputStream( "weeklyPresaleReportByTheater.xlsx" );
    fileOutputStream.write( bytes );
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  /**
   * @author jcarbajal
   *  Description -realiza prueba en el metodoque obtiene el reporte de preventa por region
   */
  @Test
  public void findBookingByEevntZoneAndWeek_Test() throws IOException
  {
    List<EventTO> eventSelected = new ArrayList<EventTO>();
    EventTO event = new EventTO();
    event.setDsEventName( "Hobiittt" );
    event.setId( 1901L );
    event.setPrerelease( true );
    event.setUserId( 2L );
    event.setUsername( "Jesus" );
    event.setTimestamp( new Date() );
    event.setIdEvent( 1901L );
    eventSelected.add( event );
    // eventSelected.add( event2 );
    Long idWeek = 289L;
    Long idRegion = 2L;
    byte[] bytes = this.serviceReportsEJB.getWeeklyBookingReportPresaleByRegion( eventSelected, idWeek, idRegion )
        .getFile();
    FileOutputStream fileOutputStream = new FileOutputStream( "weeklyPresaleReportByRegion.xlsx" );
    fileOutputStream.write( bytes );
    fileOutputStream.flush();
    fileOutputStream.close();
  }
  /**
   * @author jcarbajal
   *  Description -realiza prueba en el metodoque obtiene el reporte de preventa
   *  por eventos zona y semana 
   */
  @Test
  public void findBookingByEevntZoneAndWeek_Test2()
  {
    List<EventTO> eventSelected = new ArrayList<EventTO>();
    EventTO event = new EventTO();
    event.setDsEventName( "Hobiittt" );
    event.setId( 1901L );
    event.setPrerelease( true );
    event.setUserId( 2L );
    event.setUsername( "Jesus" );
    event.setTimestamp( new Date() );
    event.setIdEvent( 1901L );
    eventSelected.add( event );
    // eventSelected.add( event2 );
    Long idWeek = 289L;
    Long idRegion = 2L;
    List<BookingTO> bookings=this.serviceReportsEJB.findBookingsInPresaleByEevntZoneAndWeek( eventSelected, idWeek, idRegion );
    Assert.assertNotNull( bookings );
    for(BookingTO booking:bookings)
    {
      Assert.assertNotNull( booking );
    }
    
  }
}
