package mx.com.cinepolis.digital.booking.commons.utils;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportMovieTO;

/**
 * Class for comparisson between {@link mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportMovieTO} by
 * its screen number
 * 
 * @author rgarcia
 * @since 0.0.1
 */
public class WeeklyBookingReportMovieTOComparator implements Comparator<WeeklyBookingReportMovieTO>, Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 852784988803756010L;

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare( WeeklyBookingReportMovieTO s1, WeeklyBookingReportMovieTO s2 )
  {
    return new CompareToBuilder().append( s1.getNuScreen(), s2.getNuScreen() )
        .append( s1.getDsMovie(), s2.getDsMovie() ).toComparison();
  }

}
