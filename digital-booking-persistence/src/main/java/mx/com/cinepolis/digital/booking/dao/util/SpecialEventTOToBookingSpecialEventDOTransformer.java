package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.model.BookingStatusDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO} to a
 * {@link mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO}
 * 
 * @author jreyesv
 */
public class SpecialEventTOToBookingSpecialEventDOTransformer implements Transformer
{

  /**
   * Default constructor
   */
  public SpecialEventTOToBookingSpecialEventDOTransformer()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    BookingSpecialEventDO specialEventDO = null;
    if( object instanceof SpecialEventTO )
    {
      SpecialEventTO specialEventTO = (SpecialEventTO) object;
      specialEventDO = new BookingSpecialEventDO();
      specialEventDO.setIdBookingSpecialEvent( specialEventTO.getId() );
      specialEventDO.setIdBooking( null );
      BookingStatusDO bookingStatus = new BookingStatusDO();
      bookingStatus.setIdBookingStatus( specialEventTO.getStatus().getId().intValue() );
      specialEventDO.setIdBookingStatus( bookingStatus );
      specialEventDO.setDtStartSpecialEvent( specialEventTO.getStartDay() );
      specialEventDO.setDtEndSpecialEvent( specialEventTO.getFinalDay() );
      specialEventDO.setQtCopy( specialEventTO.getCopy() );
      specialEventDO.setQtCopyScreenZero( specialEventTO.getCopyScreenZero() );
      specialEventDO.setQtCopyScreenZeroTerminated( specialEventTO.getCopyScreenZeroTerminated() );
      specialEventDO.setFgShowDate( specialEventTO.isShowDate() );
    }
    return specialEventDO;
  }

}
