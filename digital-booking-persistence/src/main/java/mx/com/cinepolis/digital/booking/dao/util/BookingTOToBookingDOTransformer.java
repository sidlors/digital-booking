package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingTypeDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} to a
 * {@link mx.com.cinepolis.digital.booking.model.BookingDO}
 * 
 * @author jreyesv
 */
public class BookingTOToBookingDOTransformer implements Transformer
{

  /**
   * Default constructor
   */
  public BookingTOToBookingDOTransformer()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    BookingDO bookingDO = null;
    if( object instanceof BookingTO )
    {
      BookingTO bookingTO = (BookingTO) object;
      bookingDO = new BookingDO();
      bookingDO.setIdBooking( bookingTO.getId() );
      BookingTypeDO bookingType = new BookingTypeDO();
      bookingType.setIdBookingType( bookingTO.getIdBookingType() );
      bookingDO.setIdBookingType( bookingType );
    }
    return bookingDO;
  }

}
