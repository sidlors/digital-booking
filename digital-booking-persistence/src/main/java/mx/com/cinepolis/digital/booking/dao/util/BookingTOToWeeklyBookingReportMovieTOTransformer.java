package mx.com.cinepolis.digital.booking.dao.util;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportMovieTO;
import mx.com.cinepolis.digital.booking.commons.utils.CatalogTOComparator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportMovieTO}
 * 
 * @author rgarcia
 * @since 0.2.0
 */
public class BookingTOToWeeklyBookingReportMovieTOTransformer implements Transformer
{
  private static final String A_TO_DATE = " a ";
  private static final String DE_TO_DATE = " de ";
  private static final int BOOKING_TYPE_SPECIAL_EVENT = 3;
  private static final int BOOKING_TYPE_PRERELEASE = 2;
  private static final String SPECIAL_EVENT = " (Evento Especial)";
  private static final String PRERELEASE = " (Preestreno)";

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    WeeklyBookingReportMovieTO movieTO = null;

    if( object instanceof BookingTO )
    {
      BookingTO bookingTO = (BookingTO) object;
      movieTO = new WeeklyBookingReportMovieTO();
      CatalogTO status = bookingTO.getScreen().getBookingStatus();
      movieTO.setDsStatus( status.getName() );
      movieTO.setBookingStatus( BookingStatus.fromId( status.getId().intValue() ) );
      movieTO.setDsMovie( bookingTO.getEvent().getDsEventName() );
      if( bookingTO.getEvent() instanceof EventMovieTO )
      {
        EventMovieTO eventMovie = (EventMovieTO) bookingTO.getEvent();
        movieTO.setDsDistributor( eventMovie.getDistributor().getShortName() );
      }
      ScreenTO screenTO = bookingTO.getScreen();
      StringBuilder sbFunctions = new StringBuilder();
      List<CatalogTO> showings = screenTO.getShowings();
      Collections.sort( showings, new CatalogTOComparator( true ) );

      if(CollectionUtils.isNotEmpty( bookingTO.getSpecialEvents() ))
      {
        SpecialEventTO specialEvent=bookingTO.getSpecialEvents().get( 0 );
        
        if( bookingTO.getIdBookingType() == BOOKING_TYPE_PRERELEASE )
        {
          movieTO.setDsMovie( movieTO.getDsMovie().concat( PRERELEASE ) );
        }
        else if( bookingTO.getIdBookingType() == BOOKING_TYPE_SPECIAL_EVENT )
        {
          movieTO.setDsMovie( movieTO.getDsMovie().concat( SPECIAL_EVENT ) );
        }
        if(specialEvent.isShowDate())
        {
        movieTO.setDsDate( this.buildStringDate( specialEvent ) );
        }
        else
        {
          movieTO.setDsDate( "" );
        }
      }
      else
      {
        movieTO.setDsDate( "" );
      }

      for( int i = 0; i < showings.size(); i++ )
      {
        sbFunctions.append( showings.get( i ).getId() );
        if( i < showings.size() - 1 )
        {
          sbFunctions.append( ", " );
        }
      }

      movieTO.setDsFunctions( sbFunctions.toString() );
      if( bookingTO.getScreen().getBookingObservation() != null )
      {
        movieTO.setDsNote( bookingTO.getScreen().getBookingObservation().getObservation() );
      }
      movieTO.setDsCine( bookingTO.getTheater().getName() );
      movieTO.setNuCapacity( screenTO.getNuCapacity() );
      movieTO.setNuScreen( screenTO.getNuScreen() );
      movieTO.setNuWeek( bookingTO.getExhibitionWeek() );
    }
    return movieTO;
  }

  /**
   * TODO Por cambiar el Locale de los simple DateFormat dependiendo del lenguaje a utilizar
   */
  @SuppressWarnings("deprecation")
  private String buildStringDate( SpecialEventTO specialEventTO )
  {
    StringBuilder sb = new StringBuilder();
    SimpleDateFormat format = new SimpleDateFormat( "EEEE dd", new Locale( "es", "ES" ) );
    SimpleDateFormat format2 = new SimpleDateFormat( "MMMM", new Locale( "es", "ES" ) );
    if( specialEventTO.getStartDay().getDate() == specialEventTO.getFinalDay().getDate()
        && specialEventTO.getStartDay().getMonth() == specialEventTO.getFinalDay().getMonth()
        && specialEventTO.getStartDay().getYear() == specialEventTO.getFinalDay().getYear() )
    {
      sb.append( format.format( specialEventTO.getStartDay() ) ).append( DE_TO_DATE )
          .append( format2.format( specialEventTO.getStartDay() ) ).append( ". " );

      specialEventTO.setStrDate( sb.toString() );
    }
    else
    {
      sb.append( format.format( specialEventTO.getStartDay() ) ).append( DE_TO_DATE )
          .append( format2.format( specialEventTO.getStartDay() ) ).append( A_TO_DATE )
          .append( format.format( specialEventTO.getFinalDay() ) ).append( DE_TO_DATE )
          .append( format2.format( specialEventTO.getFinalDay() ) ).append( ". " );
      specialEventTO.setStrDate( sb.toString() );
    }
    return sb.toString();
  }
}
