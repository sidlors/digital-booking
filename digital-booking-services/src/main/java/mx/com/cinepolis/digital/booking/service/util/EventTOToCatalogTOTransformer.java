package mx.com.cinepolis.digital.booking.service.util;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.EventTO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}
 * 
 * @author afuentes
 */
public class EventTOToCatalogTOTransformer implements Transformer
{
  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object obj )
  {
    CatalogTO catalogTO = null;
    if( obj instanceof EventTO )
    {
      EventTO eventTO = (EventTO) obj;
      catalogTO = new CatalogTO( eventTO.getIdEvent(), eventTO.getDsEventName() );
    }
    return catalogTO;
  }

}
