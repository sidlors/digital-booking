package mx.com.cinepolis.digital.booking.dao.util;

import java.util.ArrayList;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO} to a
 * {@link mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO}
 * 
 * @author jreyesv
 */
public class BookingSpecialEventDOToSpecialEventTOTransformer implements Transformer
{

  /**
   * Default constructor
   */
  public BookingSpecialEventDOToSpecialEventTOTransformer()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    SpecialEventTO specialEventTO = null;
    if( object instanceof BookingSpecialEventDO )
    {
      BookingSpecialEventDO specialEventDO = (BookingSpecialEventDO) object;
      specialEventTO = new SpecialEventTO();
      specialEventTO.setId( specialEventDO.getIdBookingSpecialEvent() );
      specialEventTO.setCopy( specialEventDO.getQtCopy() );
      CatalogTO status = new CatalogTO();
      status.setId( specialEventDO.getIdBookingStatus().getIdBookingStatus().longValue() );
      specialEventTO.setStatus( status );
      specialEventTO.setIdBooking( specialEventDO.getIdBooking().getIdBooking() );
      specialEventTO.setStartDay( specialEventDO.getDtStartSpecialEvent() );
      specialEventTO.setFinalDay( specialEventDO.getDtEndSpecialEvent() );
      specialEventTO.setFgActive( specialEventDO.isFgActive() );
      specialEventTO.setCopyScreenZero( specialEventDO.getQtCopyScreenZero() );
      specialEventTO.setCopyScreenZeroTerminated( specialEventDO.getQtCopyScreenZeroTerminated() );
      specialEventTO.setIdBookingType( specialEventDO.getIdBooking().getIdBookingType().getIdBookingType().longValue() );
      EventTO event = new EventTO();
      event.setId( specialEventDO.getIdBooking().getIdEvent().getIdEvent() );
      specialEventTO.setEvent( event );
      TheaterTO theater = new TheaterTO();
      theater.setId( specialEventDO.getIdBooking().getIdTheater().getIdTheater().longValue() );
      specialEventTO.setTheater( theater );
      specialEventTO.setUserId( (long) specialEventDO.getIdLastUserModifier() );
      specialEventTO.setTimestamp( specialEventDO.getDtLastModification() );
      specialEventTO.setShowDate( specialEventDO.getFgShowDate() != null ? specialEventDO.getFgShowDate() : Boolean.FALSE );
      specialEventTO.setSpecialEventScreens( new ArrayList<SpecialEventScreenTO>() );
      if( CollectionUtils.isNotEmpty( specialEventDO.getBookingSpecialEventScreenDOList() ) )
      {
        for( BookingSpecialEventScreenDO specialEventScreenDO : specialEventDO.getBookingSpecialEventScreenDOList() )
        {
          specialEventTO.getSpecialEventScreens().add(
            (SpecialEventScreenTO) new BookingSpecialEventScreenDOToSpecialEventScreenTOTransformer()
                .transform( specialEventScreenDO ) );
        }
      }
    }
    return specialEventTO;
  }

}
