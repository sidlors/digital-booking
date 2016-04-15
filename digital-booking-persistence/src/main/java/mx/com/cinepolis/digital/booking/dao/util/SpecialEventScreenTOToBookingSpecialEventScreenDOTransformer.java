package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO} to a
 * {@link mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO}
 * 
 * @author jreyesv
 */
public class SpecialEventScreenTOToBookingSpecialEventScreenDOTransformer implements Transformer
{

  /**
   * Default constructor
   */
  public SpecialEventScreenTOToBookingSpecialEventScreenDOTransformer()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    BookingSpecialEventScreenDO bookingSpecialEventScreenDO = null;
    if( object instanceof SpecialEventScreenTO )
    {
      SpecialEventScreenTO specialEventScreenTO = (SpecialEventScreenTO) object;
      bookingSpecialEventScreenDO = new BookingSpecialEventScreenDO();
      bookingSpecialEventScreenDO.setIdBookingSpecialEventScreen( specialEventScreenTO.getId() );
      bookingSpecialEventScreenDO.setIdBookingSpecialEvent( null );
      bookingSpecialEventScreenDO.setIdObservation( null );
      bookingSpecialEventScreenDO.setIdScreen( null );
      bookingSpecialEventScreenDO.setIdBookingStatus( null );

    }
    return bookingSpecialEventScreenDO;
  }

}
