package mx.com.cinepolis.digital.booking.commons.utils;

import java.io.Serializable;
import java.util.Comparator;

import mx.com.cinepolis.digital.booking.commons.to.BookingTO;

/**
 * Class for comparison of {@link BookingTO} objects based on their theater name.
 * 
 * @author afuentes
 */
public class BookingTOComparator implements Comparator<BookingTO>, Serializable
{
  private static final long serialVersionUID = -3389078700078562127L;

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare( BookingTO b1, BookingTO b2 )
  {
    return b1.getTheater().getName().compareTo( b2.getTheater().getName() );
  }

}
