package mx.com.cinepolis.digital.booking.service.util.docx;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportHeaderTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportTheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyDistributorReportHeaderTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WorkSheetReportTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WorkSheetWeeklyBookingReportTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WorkSheetWeeklyDistributorReportTO;
import mx.com.cinepolis.digital.booking.service.util.collection.ListUtils;
import mx.com.cinepolis.digital.booking.service.util.docx.converters.AbstractXlsxColumn;
import mx.com.cinepolis.digital.booking.service.util.docx.converters.XlsxBooleanColumn;
import mx.com.cinepolis.digital.booking.service.util.docx.converters.XlsxStringColumn;

import org.junit.Test;

public class XlsxUtilsTest
{
  @Test
  public void testCreateXlsxFromList_Strings() throws IOException
  {
    byte[] bytes = generateExcelFile();
    FileOutputStream fileOutputStream = new FileOutputStream( "test1.xlsx" );
    fileOutputStream.write( bytes );
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  private byte[] generateExcelFile()
  {
    WorkSheetReportTO example = new WorkSheetReportTO();
    example.setName( "Prueba de generacion de archivo" );
    example.setFullAddress( "Mexico DF" );
    example.setIsActive( true );

    List<AbstractXlsxColumn> xlsxColumns = new ArrayList<AbstractXlsxColumn>();
    xlsxColumns.add( new XlsxStringColumn( "name", "Título" ) );
    xlsxColumns.add( new XlsxStringColumn( "fullAddress", "Dirección" ) );
    xlsxColumns.add( new XlsxBooleanColumn( "isActive", "Estado", "Activo", "Inactivo" ) );
    XlsxUtils xlsxUtils = new XlsxUtils();
    byte[] bytes = xlsxUtils.createXlsxFromList( ListUtils.asList( example, example ), xlsxColumns, "DBS" );
    return bytes;
  }

  @Test
  public void testCreateFileWeeklyBookingFromList_Strings() throws IOException
  {
    byte[] bytes = generateFileWeeklyBooking();
    FileOutputStream fileOutputStream = new FileOutputStream( "test2.xlsx" );
    fileOutputStream.write( bytes );
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  private byte[] generateFileWeeklyBooking()
  {

    WorkSheetWeeklyBookingReportTO report = new WorkSheetWeeklyBookingReportTO();
    report.setHeader( new WeeklyBookingReportHeaderTO() );
    report.getHeader().setTitle( "HOJA DE TRABAJO DE PROGRAMACIÓN" );
    report.getHeader().setStrExhibitionWeek( "DIAS DE EXHIBICIÓN SEMANA: 1, 2014: 03/01 - 09/01" );
    report.getHeader().setStrCurrentDate( "Fecha: 30/12/2013 5:27:29 PM (PST)" );

    report.setTheaters( new ArrayList<WeeklyBookingReportTheaterTO>() );

    String[] theaters = { "264 - Cinépolis Alameda Guadalajara, Guadalajara Jalisco",
        "264 - Cinepolis Galerias Cuernavaca ", "265 - Cinepolis La Cuspide" };
    for( int i = 0; i < 1; i++ )
    {
      WeeklyBookingReportTheaterTO theater = new WeeklyBookingReportTheaterTO();
      theater.setDsTheater( theaters[i] );
      theater.setMovies( new ArrayList<WeeklyBookingReportMovieTO>() );

      theater.getMovies().add( mockMovie( "Aventura Congelada, Una (3-D - DOB)", "BVCOL", 3, 1, 222, null ) );
      theater.getMovies().add(
        mockMovie( "Juegos del Hambre, Los: En Llamas (Digital - DOB)", "VIDEO", 7, 1, 222, "Ultima funcion" ) );
      theater.getMovies()
          .add( mockMovie( "Increíble Vida de Walter Mitty (Digital - SUB)", "FOX-MX", 2, 2, 208, null ) );
      report.getTheaters().add( theater );
    }

    XlsxUtils xlsxUtils = new XlsxUtils();
    byte[] bytes = xlsxUtils.createFileWeeklyBookingFromList( report,  "DBS" );
    return bytes;
  }

  private WeeklyBookingReportMovieTO mockMovie( String movie, String distributor, int week, int screen, int capacity,
      String note )
  {
    WeeklyBookingReportMovieTO movieTO = new WeeklyBookingReportMovieTO();
    movieTO.setDsMovie( movie );
    movieTO.setDsDistributor( distributor );
    movieTO.setNuWeek( week );
    movieTO.setNuScreen( screen );
    movieTO.setNuCapacity( capacity );
    movieTO.setDsNote( note );
    return movieTO;
  }
  
  private WeeklyBookingReportMovieTO mockMovieDistributors( String movie, String distributor, int week, int screen, int capacity,
      String status, BookingStatus bookingStatus )
  {
    WeeklyBookingReportMovieTO movieTO = new WeeklyBookingReportMovieTO();
    movieTO.setDsMovie( movie );
    movieTO.setDsDistributor( distributor );
    movieTO.setNuWeek( week );
    movieTO.setNuScreen( screen );
    movieTO.setNuCapacity( capacity );
    movieTO.setDsStatus( status );
    movieTO.setBookingStatus( bookingStatus );
    return movieTO;
  }
  
  @Test
  public void testCreateFileWeeklyDistributorFromList() throws IOException
  {
    byte[] bytes = generateFileWeeklyDistributor();
    FileOutputStream fileOutputStream = new FileOutputStream( "test3.xlsx" );
    fileOutputStream.write( bytes );
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  private byte[] generateFileWeeklyDistributor()
  {
    WorkSheetWeeklyDistributorReportTO report = new WorkSheetWeeklyDistributorReportTO();
    report.setHeader( new WeeklyDistributorReportHeaderTO() );
    report.getHeader().setTitle( "Estado de programación" );
    report.getHeader().setStrDistributor( "Distribuidor: Todos los distribuidores" );
    report.getHeader().setStrExhibitionWeek( "Fecha de exhibición: 01/11/2013" );
    
    report.setTheaters( new ArrayList<WeeklyBookingReportTheaterTO>() );
    WeeklyBookingReportTheaterTO theater1 = new WeeklyBookingReportTheaterTO();
    theater1.setDsTheater( "Cinépolis Altabrisa" );
    theater1.setDsCity( "Mérida" );
    theater1.setMovies( new ArrayList<WeeklyBookingReportMovieTO>() );
    theater1.getMovies().add( this.mockMovieDistributors( "Thor un Mundo Oscuro (3-D - SUB)", null, 1, 1, 0, "Programar (01/11)", BookingStatus.BOOKED ) );
    theater1.getMovies().add( this.mockMovieDistributors( "Thor un Mundo Oscuro (Digital - SUB)", null, 1, 2, 0, "Continuar (07/11)", BookingStatus.BOOKED ) );
    theater1.getMovies().add( this.mockMovieDistributors( "Gravedad (3-D - SUB)", null, 3, 3, 0, "Continuar (07/11)", BookingStatus.BOOKED ) );
    theater1.getMovies().add( this.mockMovieDistributors( "Familia Peligrosa, Una (Digital - SUB)", null, 3, 4, 0, "Continuar (07/11)", BookingStatus.BOOKED ) );
    theater1.getMovies().add( this.mockMovieDistributors( "Armados y Peligrosos (Digital - SUB)", null, 2, 4, 0, "Programar (01/11)", BookingStatus.BOOKED ) );
    theater1.getMovies().add( this.mockMovieDistributors( "Thor un Mundo Oscuro (3-D - DOB)", null, 1, 5, 0, "Programar (01/11)", BookingStatus.BOOKED ) );
    report.getTheaters().add( theater1  );
    
    WeeklyBookingReportTheaterTO theater2 = new WeeklyBookingReportTheaterTO();
    theater2.setDsTheater( "Cinépolis Angelópolis" );
    theater2.setDsCity( "Puebla" );
    theater2.setMovies( new ArrayList<WeeklyBookingReportMovieTO>() );
    theater2.getMovies().add( this.mockMovieDistributors( "Thor un Mundo Oscuro (3-D - SUB)", null, 1, 1, 0, "Programar (01/11)", BookingStatus.BOOKED ) );
    theater2.getMovies().add( this.mockMovieDistributors( "Thor un Mundo Oscuro (Digital - SUB)", null, 1, 2, 0, "Continuar (07/11)", BookingStatus.BOOKED ) );
    theater2.getMovies().add( this.mockMovieDistributors( "Gravedad (3-D - SUB)", null, 3, 3, 0, "Continuar (07/11)", BookingStatus.BOOKED ) );
    theater2.getMovies().add( this.mockMovieDistributors( "Familia Peligrosa, Una (Digital - SUB)", null, 3, 4, 0, "Continuar (07/11)", BookingStatus.BOOKED ) );
    theater2.getMovies().add( this.mockMovieDistributors( "Armados y Peligrosos (Digital - SUB)", null, 2, 4, 0, "Programar (01/11)", BookingStatus.BOOKED ) );
    theater2.getMovies().add( this.mockMovieDistributors( "Thor un Mundo Oscuro (3-D - DOB)", null, 1, 5, 0, "Programar (01/11)", BookingStatus.BOOKED ) );
    report.getTheaters().add( theater2 );
    
    WeeklyBookingReportTheaterTO theater3 = new WeeklyBookingReportTheaterTO();
    theater3.setDsTheater( "Cinépolis Boulevard" );
    theater3.setDsCity( "Puebla" );
    theater3.setMovies( new ArrayList<WeeklyBookingReportMovieTO>() );
    theater3.getMovies().add( this.mockMovieDistributors( "Thor un Mundo Oscuro (3-D - SUB)", null, 1, 1, 0, "Programar (01/11)", BookingStatus.BOOKED ) );
    theater3.getMovies().add( this.mockMovieDistributors( "Thor un Mundo Oscuro (Digital - SUB)", null, 1, 2, 0, "Continuar (07/11)", BookingStatus.BOOKED ) );
    theater3.getMovies().add( this.mockMovieDistributors( "Gravedad (3-D - SUB)", null, 3, 3, 0, "Continuar (07/11)", BookingStatus.BOOKED ) );
    theater3.getMovies().add( this.mockMovieDistributors( "Familia Peligrosa, Una (Digital - SUB)", null, 3, 4, 0, "Continuar (07/11)", BookingStatus.BOOKED ) );
    theater3.getMovies().add( this.mockMovieDistributors( "Armados y Peligrosos (Digital - SUB)", null, 2, 4, 0, "Programar (01/11)", BookingStatus.BOOKED ) );
    theater3.getMovies().add( this.mockMovieDistributors( "Thor un Mundo Oscuro (3-D - DOB)", null, 1, 5, 0, "Programar (01/11)", BookingStatus.BOOKED ) );
    report.getTheaters().add( theater2 );
    
    XlsxUtils xlsxUtils = new XlsxUtils();
    return xlsxUtils.createFileWeeklyDistributorFromList( report ,  "Todos_los_distribuidores" );
  }
  
  /**
   * method for test the creation presle email
   * @throws IOException
   */
  @Test 
  public void testCreateFileWeeklyBookingPresaleFromList_String()throws IOException
  {
    byte[] bytes =getnerateFileWeeklyReportTOPresale();
    FileOutputStream fileOutput = new FileOutputStream( "testPresale.xlsx" );
    fileOutput.write( bytes );
    fileOutput.flush();
    fileOutput.close();
        
  }
  
  /**
   * Method for generate file in array of bites
   * @return
   */
  private  byte[] getnerateFileWeeklyReportTOPresale()
  {
    
    WorkSheetWeeklyBookingReportTO report = new WorkSheetWeeklyBookingReportTO();
    report.setHeader( new WeeklyBookingReportHeaderTO() );
    report.getHeader().setTitle( "HOJA DE TRABAJO DE PROGRAMACIÓN" );
    report.getHeader().setStrExhibitionWeek( "DIAS DE PREVENTA SEMANA: 7, 2015: 03/03 - 09/03" );
    report.getHeader().setStrCurrentDate( "Fecha: 30/03/2015 5:27:29 PM (PST)" );

    report.setTheaters( new ArrayList<WeeklyBookingReportTheaterTO>() );

    String[] theaters = { "6767 - Cinépolis Ajuscoa, Estado de Mexico",
        "264 - Cinepolis Galerias metepec Cuernavaca ", "265 -Cinepolis Plaza Sendero" };
    for( int i = 0; i < 1; i++ )
    {
      WeeklyBookingReportTheaterTO theater = new WeeklyBookingReportTheaterTO();
      theater.setDsTheater( theaters[i] );
      theater.setMovies( new ArrayList<WeeklyBookingReportMovieTO>() );

      theater.getMovies().add( mockMovie( "Cincuenta Sombras de Grey, Una (3-D - DOB)", "BVCOL", 3, 1, 222, null ) );
      theater.getMovies().add(
        mockMovie( "El Francotirador", "VIDEO", 7, 1, 222, "Ultima funcion" ) );
      theater.getMovies()
          .add( mockMovie( "Bob esponja un heroe fuera del agua ", "FOX-MX", 2, 2, 208, null ) );
      report.getTheaters().add( theater );
    }
    XlsxUtils xlsxUtils = new XlsxUtils();
    byte [] bytes = xlsxUtils.createFileWeeklyBookingPresaleFromList( report,  "DBS" );
    return bytes;
  }
 

 

}
