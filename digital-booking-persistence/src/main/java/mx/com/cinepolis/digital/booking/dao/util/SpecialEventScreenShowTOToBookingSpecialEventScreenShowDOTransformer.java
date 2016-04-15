package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenShowTO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenShowDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenShowTO} to a
 * {@link mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenShowDO}
 * 
 * @author jreyesv
 */
public class SpecialEventScreenShowTOToBookingSpecialEventScreenShowDOTransformer implements Transformer
{

  /**
   * Default constructor
   */
  public SpecialEventScreenShowTOToBookingSpecialEventScreenShowDOTransformer()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    BookingSpecialEventScreenShowDO bookingSpecialEventScreenShowDO = null;
    if( object instanceof SpecialEventScreenShowTO )
    {
      SpecialEventScreenShowTO specialEventScreenShowTO = (SpecialEventScreenShowTO) object;
      bookingSpecialEventScreenShowDO = new BookingSpecialEventScreenShowDO();
      bookingSpecialEventScreenShowDO.setIdBookingSpecialEventScreenShow( specialEventScreenShowTO.getId() );
      bookingSpecialEventScreenShowDO.setIdBookingSpecialEventScreen( null );
      bookingSpecialEventScreenShowDO.setNuShow( specialEventScreenShowTO.getNuShow() );
    }
    return bookingSpecialEventScreenShowDO;
  }

}
