package mx.com.cinepolis.digital.booking.dao.util;

import java.io.Serializable;
import java.util.Comparator;

import mx.com.cinepolis.digital.booking.commons.to.SystemMenuTO;

/**
 * Class for comparison of {@link mx.com.cinepolis.digital.booking.commons.to.SystemMenuTO} objects based on their order
 * property.
 * 
 * @author afuentes
 */
public class MenuElementComparator<T> implements Comparator<SystemMenuTO>, Serializable
{
  private static final long serialVersionUID = 8784540460511071873L;

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare( SystemMenuTO m1, SystemMenuTO m2 )
  {
    return m1.getOrder().compareTo( m2.getOrder() );
  }

}
