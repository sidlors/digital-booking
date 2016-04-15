package mx.com.cinepolis.digital.booking.service.util;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.RegionTO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}
 * 
 * @author afuentes
 */
public class RegionTOToCatalogTOTransformer implements Transformer
{

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object transform( Object obj )
  {
    CatalogTO catalogTO = null;
    if( obj instanceof RegionTO<?, ?> )
    {
      RegionTO<CatalogTO, CatalogTO> regionTO = (RegionTO<CatalogTO, CatalogTO>) obj;
      catalogTO = new CatalogTO( regionTO.getCatalogRegion().getId(), regionTO.getCatalogRegion().getName() );
    }
    return catalogTO;
  }
}
