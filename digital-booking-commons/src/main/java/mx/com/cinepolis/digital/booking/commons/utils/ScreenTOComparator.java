package mx.com.cinepolis.digital.booking.commons.utils;

import java.io.Serializable;
import java.util.Comparator;

import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;

/**
 * Class for comparisson between {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} by its screen number
 * 
 * @author gsegura
 * @since 0.0.1
 */
public class ScreenTOComparator implements Comparator<ScreenTO>, Serializable
{
  private static final long serialVersionUID = -4005184844666750820L;

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare( ScreenTO s1, ScreenTO s2 )
  {
    return s1.getNuScreen().compareTo( s2.getNuScreen() );
  }

}
