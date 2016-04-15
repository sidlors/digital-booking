package mx.com.cinepolis.digital.booking.service.util.docx;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;

import org.junit.Test;

public class XlsUtilsExampleTest
{

  @Test
  public void test() throws IOException
  {
    byte[] bytes = generateExcelFile();
    FileOutputStream fileOutputStream = new FileOutputStream( "test1.xlsx" );
    fileOutputStream.write( bytes );
    fileOutputStream.flush();
    fileOutputStream.close();
  }

  private byte[] generateExcelFile()
  {
    XlsUtilsExample x = new XlsUtilsExample();
    List<BookingTO> bookings = new ArrayList<BookingTO>();

    String[] cinemas = { "Cine A", "Cine B", "Cine C" };
    String[] movies = { "Grandes Héroes 3D Esp", "Grandes Héroes Dig Esp", "Grandes Héroes IMAX Esp" };

    for( String cinema : cinemas )
    {
      for( String movie : movies )
      {
        BookingTO to = new BookingTO();
        to.setTheater( new TheaterTO() );
        to.getTheater().setName( cinema );
        to.setEvent( new EventMovieTO() );
        to.getEvent().setDsEventName( movie );
        ((EventMovieTO) to.getEvent()).setDsRating( "A" );
        bookings.add( to );
      }
    }

    return x.generateReport( bookings );
  }

}
