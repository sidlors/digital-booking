package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO}
 * 
 * @author jcarbajal
 */
public interface BookingSpecialEventDAO  extends GenericDAO<BookingSpecialEventDO>
{
  /**
   * Find bookings special events by special event and region
   * 
   * @param pagingRequestTO
   * @return
   */
  List<SpecialEventTO> findBookingsSpecialEventByEventRegion( PagingRequestTO pagingRequestTO );
  
  /**
   * Find bookings by idTheater, idEvent and idBookingType
   * 
   * @param idTheater
   * @param idEvent
   * @return
   */
  BookingDO findByEventAndTheaterAndType( Long idTheater, Long idEvent, Long idBookingType );
  
  /**
   * Method saves an special event booking.
   * @param bookingTO
   */
  void saveBookingSpecialEvent( BookingTO bookingTO );

  /**
   * Method canceling an special event booking.
   * @param bookingTO
   */
  void cancelBookingSpecialEvent( BookingTO bookingTO );

  /**
   * Method that gets a BookingObservation by id special event
   * 
   * @param idSpecialEvent
   * @return
   */
  BookingObservationTO getObservationByIdSpecialEvent( Long idSpecialEvent );
  
  /**
   * Method that gets the showings selected by id special event.
   * 
   * @param idSpecialEvent
   * @return
   */
  List<Object> getShowingsSelectedByIdSpecialEvent( Long idSpecialEvent );
  
  /**
   * Method that cancels a screen of a special event booking.
   * 
   * @param bookingTO
   */
  void cancelBookingSpecialEventScreen( Long idScreen, int copies );

  /**
   * Method that updates a booking special event.
   * 
   * @param bookingTO
   */
  void updateBookingSpecialEvent( BookingTO bookingTO );

}
