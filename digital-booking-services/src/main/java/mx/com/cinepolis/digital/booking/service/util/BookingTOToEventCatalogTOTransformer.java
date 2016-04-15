package mx.com.cinepolis.digital.booking.service.util;

import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} with the event information.
 * 
 * @author afuentes
 */
public class BookingTOToEventCatalogTOTransformer implements Transformer
{

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object obj )
  {
    CatalogTO eventCatalogTO = null;
    if( obj instanceof BookingTO )
    {
      BookingTO bookingTO = (BookingTO) obj;
      EventTO eventTO = bookingTO.getEvent();
      eventCatalogTO = new CatalogTO( eventTO.getIdEvent(), eventTO.getDsEventName() );
    }
    return eventCatalogTO;
  }

}
