package mx.com.cinepolis.digital.booking.commons.utils;

import java.io.Serializable;
import java.util.Comparator;

import mx.com.cinepolis.digital.booking.commons.to.WeekTO;

/**
 * Class for comparisson between {@link mx.com.cinepolis.digital.booking.commons.to.WeekTO} by its starting week day.
 * 
 * @author afuentes
 */
public class WeekTOComparator implements Comparator<WeekTO>, Serializable
{
  private static final long serialVersionUID = 4498738996903939133L;

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare( WeekTO w1, WeekTO w2 )
  {
    return w1.getStartingDayWeek().compareTo( w2.getStartingDayWeek() );
  }

}
