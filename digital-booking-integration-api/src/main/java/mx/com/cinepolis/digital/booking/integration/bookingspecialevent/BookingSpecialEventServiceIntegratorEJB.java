package mx.com.cinepolis.digital.booking.integration.bookingspecialevent;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventBookingTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;

/**
 * Interface that defines the integration services relating to booking special events.
 * 
 * @author shernandezl
 */
public interface BookingSpecialEventServiceIntegratorEJB
{
  /**
   * Method consulting booking special events filtered by event and region
   * 
   * @param pagingRequestTO
   * @return
   */
  SpecialEventBookingTO findBookingSpecialEvent( PagingRequestTO pagingRequestTO );

  /**
   * Method saving booking special events
   * 
   * @param specialEventTOs
   * @return
   */
  void saveOrUpdateBookings( List<SpecialEventTO> specialEventTOs );

  /**
   * Method canceling booking special events filtered by event and region
   * 
   * @param specialEventTOs
   * @return
   */
  void cancelBookings( List<SpecialEventTO> specialEventTOs );

  /**
   * Obtiene las semanas activas
   * 
   * @param abstractTO
   * @return
   */
  List<WeekTO> findWeeksActive( AbstractTO abstractTO );

  /**
   * Method to find all active regions.
   * 
   * @param abstractTO {@link AbstractTO} with the current session information.
   * @return List of {@link CatalogTO} with the active regions information.
   * @author afuentes
   */
  List<CatalogTO> findAllActiveRegions( AbstractTO abstractTO );

  /**
   * Method consulting booking special events for report.
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<EventTO> getEventsBookedForReport( PagingRequestTO pagingRequestTO );

  /**
   * This method sends the weekly report to the person associated with the given region and week
   * 
   * @param regionEmailTO
   */
  void sendPresalesBookedByRegionEmail( RegionEmailTO regionEmailTO, List<EventTO> eventSelected );

  /**
   * Method to Send Email of the Presale by zone week and Movies
   */
  FileTO getWeeklyBookingReportPresaleByRegion( List<EventTO> movies, Long idWeek, Long idRegion );
  
  /**@author jcarbajal
   * Method to Send Email of the Presale by Theater, Week and Movies
   * @param List<EventTO> movies
   * @param idWeek
   * @param idTheater
   * 
   */
  FileTO getWeeklyBookingReportPresaleByTheater( List<EventTO> movies, Long idWeek, Long idTheater );
  
  /**
   * @author jcarbajal
   * This method sends the weekly report to the person associated with the given theater and week
   * 
   * @param theaterEmailTO
   * @param eventSelected
   * 
   */
  void sendPresalesBookedByTheaterEmail( TheaterEmailTO theaterEmailTO, List<EventTO> eventSelected );

  /**
   * Method that returns the bookings in presale by idWeek, idRegion and a eventTO list.
   * 
   * @param movies
   * @param idWeek
   * @param idRegion
   * @return bookingTO list
   */
  List<BookingTO> findBookingsInPresaleByEevntZoneAndWeek( List<EventTO> movies, Long idWeek, Long idRegion );
}
