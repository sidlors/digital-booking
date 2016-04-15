package mx.com.cinepolis.digital.booking.commons.utils;

import java.io.Serializable;
import java.util.Comparator;

import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportTheaterTO;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * Class for comparisson between {@link mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportTheaterTO} by
 * its screen number
 * 
 * @author rgarcia
 * @since 0.0.1
 */
public class WeeklyBookingReportTheaterTOComparator implements Comparator<WeeklyBookingReportTheaterTO>, Serializable
{

  /**
   * 
   */
  private static final long serialVersionUID = 2666791818063695749L;

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare( WeeklyBookingReportTheaterTO s1, WeeklyBookingReportTheaterTO s2 )
  {
    return new CompareToBuilder().append( s1.getTheaterName(), s2.getTheaterName() ).toComparison();
  }

}
