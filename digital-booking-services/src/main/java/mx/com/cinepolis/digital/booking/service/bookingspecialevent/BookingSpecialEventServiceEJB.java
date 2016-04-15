package mx.com.cinepolis.digital.booking.service.bookingspecialevent;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventBookingTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterEmailTO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;

/**
 * Interface that defines special events booking services.
 * 
 * @author jreyesv
 */
@Local
public interface BookingSpecialEventServiceEJB
{

  /**
   * Method that depending on the case, saves or updates the bookings according to the list provided.
   * 
   * @param bookingTOs List of {@link BookingTOMock} with the bookings information.
   * @author jreyesv
   */
  void saveOrUpdateBookings( List<SpecialEventTO> specialEventTOs );

  /**
   * Method that cancels the bookings according to the list provided.
   * 
   * @param bookingTOs List of {@link BookingTOMock} with the bookings information.
   * @author jreyesv
   */
  void cancelBookings( List<SpecialEventTO> specialEventTOs );

  /**
   * Method that cancels a bookingSpecialEventScreen provided.
   * 
   * @param bookingTO
   * @author jreyesv
   */
  void cancelScreenBookingTO( BookingTO bookingTO );

  /**
   * This method applies to test cases
   * 
   * @return savedBookings Number of saved records in database
   */
  int getSavedBookings();

  /**
   * Method consulting booking special events.
   * 
   * @param pagingRequestTO
   * @return
   */
  SpecialEventBookingTO findBookingSpecialEvent( PagingRequestTO pagingRequestTO );

  /**
   * Method create SpecialEventWeek
   * 
   * @param
   */
  void createSpecialEventWeek( BookingSpecialEventDO bookingspecialEventDO, SpecialEventTO specialEventTO );

  /**
   * Method consulting booking special events for report.
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<EventTO> getEventsBookedForReport( PagingRequestTO pagingRequestTO );

  /**
   * Sends the email for the region responsibles
   * @param regionEmailTO
   */
  void sendPresalesBookedByRegionEmail( RegionEmailTO regionEmailTO, List<EventTO> eventSelected );
  
  /**@author jcarbajal
   * Sends the email for the Theaters responsibles
   * @param theaterEmailTO
   * @param eventSelected
   */
  void sendPresalesBookedByTheaterEmail( TheaterEmailTO theaterEmailTO, List<EventTO> eventSelected );
}
