package mx.com.cinepolis.digital.booking.commons.utils;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.lang.builder.CompareToBuilder;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;

/**
 * Class for comparisson between {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public class CatalogTOComparator implements Comparator<CatalogTO>, Serializable
{

  private static final long serialVersionUID = -279548573903106403L;
  private boolean compareById;

  /**
   * Constructor by type of comparisson
   * 
   * @param compareById
   */
  public CatalogTOComparator( boolean compareById )
  {
    this.compareById = compareById;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare( CatalogTO c1, CatalogTO c2 )
  {
    int n;
    if( this.compareById )
    {
      n = c1.getId().compareTo( c2.getId() );
      n = new CompareToBuilder().append( c1.getId(), c2.getId() ).toComparison();
    }
    else
    {
      n = new CompareToBuilder().append( c1.getName(), c2.getName() ).toComparison();
    }
    return n;
  }

}
