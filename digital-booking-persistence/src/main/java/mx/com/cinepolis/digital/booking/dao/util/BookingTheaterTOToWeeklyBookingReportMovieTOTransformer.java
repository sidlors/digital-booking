package mx.com.cinepolis.digital.booking.dao.util;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportMovieTO;

import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.time.DateUtils;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportMovieTO}
 * 
 * @author jcarbajal
 */
public class BookingTheaterTOToWeeklyBookingReportMovieTOTransformer implements Transformer
{
  private static final int BOOKING_TYPE_SPECIAL_EVENT = 3;
  private static final int BOOKING_TYPE_PRERELEASE = 2;
  private static final String SPECIAL_EVENT = " (Evento Especial)";
  private static final String PRERELEASE = " (Preestreno)";
  private static final String A_TO_DATE = " a ";
  private static final String DE_TO_DATE = " de ";

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    WeeklyBookingReportMovieTO movieTO = null;

    if( object instanceof BookingTheaterTO )
    {
      BookingTheaterTO bookingTheaterTO = (BookingTheaterTO) object;
      movieTO = new WeeklyBookingReportMovieTO();
      // CatalogTO status = bookingTheaterTO.getScreen().getBookingStatus();
      CatalogTO status = bookingTheaterTO.getScreenTO().getBookingStatus();
      movieTO.setDsStatus( status.getName() );
      movieTO.setBookingStatus( BookingStatus.fromId( status.getId().intValue() ) );

      movieTO.setDsMovie( bookingTheaterTO.getEventTO().getDsEventName() );
      if( bookingTheaterTO.getIdBookingType() == BOOKING_TYPE_PRERELEASE )
      {
        movieTO.setDsMovie( movieTO.getDsMovie().concat( PRERELEASE ) );
      }
      else if( bookingTheaterTO.getIdBookingType() == BOOKING_TYPE_SPECIAL_EVENT )
      {
        movieTO.setDsMovie( movieTO.getDsMovie().concat( SPECIAL_EVENT ) );
      }
      if( bookingTheaterTO.getEventTO() instanceof EventMovieTO )
      {
        EventMovieTO eventMovie = (EventMovieTO) bookingTheaterTO.getEventTO();
        movieTO.setDsDistributor( eventMovie.getDistributor().getShortName() );
      }
      else
      {
        movieTO.setDsDistributor( bookingTheaterTO.getDistributor() );
      }
      ScreenTO screenTO = bookingTheaterTO.getScreenTO();
      StringBuilder sbFunctions = new StringBuilder();
      List<Object> showings = bookingTheaterTO.getSelectedShowings();
      // Collections.sort( showings, new CatalogTOComparator( true ) );
      for( int i = 0; i < showings.size(); i++ )
      {
        sbFunctions.append( showings.get( i ).toString() );
        if( i < showings.size() - 1 )
        {
          sbFunctions.append( ", " );
        }
      }
      movieTO.setDsFunctions( sbFunctions.toString() );
      if( bookingTheaterTO.getScreenTO().getBookingObservation() != null )
      {
        if( bookingTheaterTO.getStartDay() != null && bookingTheaterTO.getFinalDay() != null )
        {
          putDate( bookingTheaterTO );
          if( bookingTheaterTO.isFgShowDate() )
          {
            movieTO.setDsNote( bookingTheaterTO.getStrDateSpecialEvents().concat(
              bookingTheaterTO.getScreenTO().getBookingObservation().getObservation() ) );
          }
          else
          {
            movieTO.setDsNote( bookingTheaterTO.getScreenTO().getBookingObservation().getObservation() );
          }
        }
        else
        {
          movieTO.setDsNote( bookingTheaterTO.getScreenTO().getBookingObservation().getObservation() );
        }

      }
      movieTO.setNuCapacity( bookingTheaterTO.getCapacity() );
      movieTO.setNuScreen( screenTO.getNuScreen() );
      // movieTO.setNuWeek( bookingTheaterTO.getExhibitionWeek() );
      movieTO.setNuWeek( bookingTheaterTO.getWeek() );
    }
    return movieTO;
  }

  /**
   * TODO Por cambiar el Locale de los simple DateFormat dependiendo del lenguaje a utilizar
   */

  private void putDate( BookingTheaterTO bookingTheaterTO )
  {
    StringBuilder sb = new StringBuilder();
    SimpleDateFormat format = new SimpleDateFormat( "EEEE dd", new Locale( "es", "MX" ) );
    SimpleDateFormat format2 = new SimpleDateFormat( "MMMM", new Locale( "es", "MX" ) );
    if( DateUtils.isSameDay( bookingTheaterTO.getStartDay(), bookingTheaterTO.getFinalDay() ) )
    {
      sb.append( format.format( bookingTheaterTO.getStartDay() ) ).append( DE_TO_DATE )
          .append( format2.format( bookingTheaterTO.getStartDay() ) ).append( ". " );

      bookingTheaterTO.setStrDateSpecialEvents( sb.toString() );
    }
    else
    {
      sb.append( format.format( bookingTheaterTO.getStartDay() ) ).append( DE_TO_DATE )
          .append( format2.format( bookingTheaterTO.getStartDay() ) ).append( A_TO_DATE )
          .append( format.format( bookingTheaterTO.getFinalDay() ) ).append( DE_TO_DATE )
          .append( format2.format( bookingTheaterTO.getFinalDay() ) ).append( ". " );
      bookingTheaterTO.setStrDateSpecialEvents( sb.toString() );
    }

  }

}
