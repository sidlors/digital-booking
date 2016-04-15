package mx.com.cinepolis.digital.booking.commons.utils;

import java.io.Serializable;
import java.util.Comparator;

import mx.com.cinepolis.digital.booking.commons.to.RegionTO;

/**
 * Comparator class for {@link mx.com.cinepolis.digital.booking.commons.to.RegionTO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
@SuppressWarnings("rawtypes")
public class RegionTOComparator implements Comparator<RegionTO>, Serializable
{
  private static final long serialVersionUID = 5418647605802465998L;
  private boolean compareById;

  /**
   * Constructor default
   */
  public RegionTOComparator()
  {
    this.compareById = true;
  }

  /**
   * Constructor by boolean
   * 
   * @param compareById
   */
  public RegionTOComparator( boolean compareById )
  {
    this.compareById = compareById;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare( RegionTO region1, RegionTO region2 )
  {
    int comparisson;
    if( this.compareById )
    {
      comparisson = region1.getCatalogRegion().getId().compareTo( region2.getCatalogRegion().getId() );
    }
    else
    {
      comparisson = region1.getCatalogRegion().getName().compareTo( region2.getCatalogRegion().getName() );
    }
    return comparisson;
  }

}
