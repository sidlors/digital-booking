package mx.com.cinepolis.digital.booking.integration.booking;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EmailTemplateTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.MovieBookingWeekTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterBookingWeekTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;

/**
 * Interface that defines the integration services relating to booking movies.
 * 
 * @author afuentes
 */
public interface BookingServiceIntegratorEJB
{
  /**
   * Cancela una programación
   * 
   * @param bookingTO
   */
  void cancelBooking( List<BookingTO> bookingTOs );

  /**
   * Guarda o actualiza una observacion asociada a una programacion(Booking)
   * 
   * @param bookingObservationTO
   */
  void saveOrUpdateBookingObservation( BookingObservationTO bookingObservationTO );

  /**
   * Se encarga de Obtener Todas las peliculas(ya programadas o no programadas) de acuerdo a los criterios de busqueda:
   * <ul>
   * <li>BookingQuery.BOOKING_WEEK_ID (Id de la semana requerido)
   * <li>BookingQuery.BOOKING_EVENT_ID (Id del evento requerido)
   * </ul>
   * 
   * @param pagingRequestTO
   * @return
   */
  MovieBookingWeekTO findBookingMovies( PagingRequestTO pagingRequestTO );

  /**
   * Se encarga de Obtener la programacion de cada cine de acuerdo a los criterios de busqueda:
   * <ul>
   * <li>BookingQuery.BOOKING_WEEK_ID (Id de la semana requerido)
   * <lI>BookingQuery.BOOKING_THEATER_ID (Id del cine requerido)
   * </ul>
   * 
   * @param pagingRequestTO
   * @return
   */
  TheaterBookingWeekTO findBookingTheater( PagingRequestTO pagingRequestTO );

  PagingResponseTO<BookingTO> findBookingMoviesByTheater( PagingRequestTO pagingRequestTO );

  /**
   * Obtiene las semanas activas
   * 
   * @param abstractTO
   * @return
   */
  List<WeekTO> findWeeksActive( AbstractTO abstractTO );

  /**
   * Method to find all active movies.
   * 
   * @return List of {@link CatalogTO} with the active movies information.
   * @author afuentes
   */
  List<CatalogTO> findAllActiveMovies( boolean festival, boolean prerelease );

  /**
   * Method to find all active regions.
   * 
   * @param abstractTO {@link AbstractTO} with the current session information.
   * @return List of {@link CatalogTO} with the active regions information.
   * @author afuentes
   */
  List<CatalogTO> findAllActiveRegions( AbstractTO abstractTO );

  /**
   * Method that finds all movie premieres.
   * 
   * @return List of {@link EventMovieTO} with the movie premieres information.
   * @author afuentes
   */
  List<EventMovieTO> findAllPremieres();

  /**
   * Method that depending on the case, saves or updates the bookings according to the list provided.
   * 
   * @param bookingTOs List of {@link BookingTO} with the bookings information.
   * @author afuentes
   */
  void saveOrUpdateBookings( List<BookingTO> bookingTOs );

  /**
   * Method that cancels each special event booking.
   * 
   * @param bookingTOs List of {@link BookingTO} with the bookings information.
   * @author jreyesv
   */
  void cancelSpecialEventBookings( List<BookingTO> bookingTOs );

  /**
   * Method that cancels the presale for each booking.
   * 
   * @param bookingTOs List of {@link BookingTO} with the bookings information.
   * @author jreyesv
   */
  void cancelPresaleInBookings( List<BookingTO> bookingTOs );

  /**
   * Find Theater by idWeek(almost a program booking) and idRegion
   * 
   * @param idWeek
   * @param idRegion
   * @return
   * @author agustin.ramirez
   */
  PagingResponseTO<TheaterTO> findTheatersByIdWeekAndIdRegion( PagingRequestTO requestTO );

  /**
   * Method that terminates a booking and saves the reason as the booking observation.
   * 
   * @param bookingTO {@link BookingTO} with the booking and observation information.
   * @author afuentes
   */
  void terminateBooking( BookingTO bookingTO );

  /**
   * Obtains the report
   * 
   * @param idWeek
   * @param idTheater
   * @return
   */
  FileTO getWeeklyBookingReportByTheater( Long idWeek, Long idTheater );

  /**
   * Obtains the week by its id
   * 
   * @param weekId
   * @return
   */
  WeekTO findWeek( int weekId );

  /**
   * Sends the booking report to the theater
   * 
   * @param bookingTO
   */
  void sendTheaterEmail( BookingTO bookingTO );

  /**
   * Method that sends the booking report to the theater. The e-mail subject and body are obtained based on the region
   * selected.
   * 
   * @param bookingTO
   * @param regionId
   * @author afuentes
   */
  void sendTheaterEmail( BookingTO bookingTO, Long regionId );

  /**
   * Method that finds all active events that have not been booked for the specified week and theather.
   * 
   * @param idWeek Week identifier.
   * @param idTheater Theater identifier.
   * @return List of {@link CatalogTO} with the avalilable events information.
   * @author afuentes
   */
  List<CatalogTO> findAvailableMovies( Long idWeek, Long idTheater );

  /**
   * Method that finds all the theater screens, marks the disabled ones and adds the screen "zero".
   * 
   * @param idTheater Theater identifier.
   * @param idEvent Event identifier.
   * @return List of {@link ScreenTO} with all the theater screens.
   * @author afuentes
   */
  List<ScreenTO> findTheaterScreens( Long idTheater, Long idEvent );

  /**
   * Method that finds a list paginated of theaters of some region which has at least one booking during the week
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<TheaterTO> findTheatersByBookingWeekAndRegion( PagingRequestTO pagingRequestTO );

  /**
   * Method that finds a list paginated of theaters of some region which has at least one booking in presale during the
   * week
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<TheaterTO> findTheatersByBookingWeekAndRegionForPresaleReport( PagingRequestTO pagingRequestTO );

  /**
   * Method that sends the email report of bookings associated with a list of theaters
   * 
   * @param weekIdSelected
   * @param theatersSelected
   */
  void sendTheatersEmail( Long weekIdSelected, List<TheaterTO> theatersSelected, String template, String subject );

  /**
   * Method that obtains the email template for the weekly report of bookings from a given region
   * 
   * @param weekTO
   * @return
   */
  String getEmailTemplateRegion( WeekTO weekTO );

  /**
   * This method gets the excel report of the bookings of a given region by week
   * 
   * @param weekId
   * @param regionId
   * @return
   */
  FileTO getWeeklyBookingReportByRegion( Long weekId, Long regionId );

  /**
   * This method sends the weekly report to the person associated with the given region and week
   * 
   * @param regionEmailTO
   */
  void sendRegionEmail( RegionEmailTO regionEmailTO );

  /**
   * Method that builds the weekly distributor report for the given week and distributor.
   * 
   * @param idWeek Week identifier.
   * @param idRegion TODO
   * @param idDistributor Distributor identifier.
   * @return {@link FileTO} with the byte array representing the report file.
   */
  FileTO getWeeklyDistributorReportByDistributor( Long idWeek, Long idRegion, Long idDistributor );

  /**
   * Method that builds the weekly distributor report for the given week and all active distributors.
   * 
   * @param idWeek Week identifier.
   * @param idRegion TODO
   * @return {@link FileTO} with the byte array representing the report file.
   */
  FileTO getWeeklyDistributorReportByAllDistributors( Long idWeek, Long idRegion );

  /**
   * Validates if user can preview the document
   * 
   * @param idWeek
   * @param idTheater
   * @return
   */
  boolean validatePreviewDocument( Long idWeek, Long idTheater );

  List<EventTO> findEventsForScreen( ScreenTO screenTO );

  List<CatalogTO> getBookingStatus( List<Long> statusId );

  /**
   * Method that finds the following week booking for a certain booking in the specified week.
   * 
   * @param idBooking Booking identifier.
   * @param idWeek Week identifier.
   * @return {@link WeekTO} with the information of the following week booking found, or <code>null</code> if no
   *         following week booking was found.
   * @author afuentes
   */
  WeekTO findFollowingWeekBooking( Long idBooking, Integer idWeek );

  /**
   * Method that cancels or terminates the bookings in the list provided depending on the value of the flag
   * <code>isCancellation</code>.
   * 
   * @param bookingsForCancellationOrTemination List of {@link BookingTO} with the information of the bookings.
   * @param isCancellation Flag that determines the operation: <code>true</code> for cancellation, <code>false</code>
   *          for termination.
   * @author afuentes
   */
  void cancelOrTeminateBookings( List<BookingTO> bookingsForCancellation, boolean isCancellation );

  /**
   * Method that determines whether the booking for the week has been sent or not.
   * 
   * @param bookingTO {@link BookingTO} with the information of the booking.
   * @return <code>true</code> if the booking for the week has been sent, <code>false</code> otherwise.
   * @author afuentes
   */
  boolean isBookingWeekSent( BookingTO bookingTO );

  /**
   * Method that determines whether the Booking for the week exist or not on the daabase.
   * 
   * @param bookingTO {@link BookingTO} with the information of the booking.
   * @return <code>true</code> if the booking for the week exists, <code>false</code> otherwise.
   * @author afuentes
   */
  boolean hasBookingWeek( BookingTO bookingTO );

  /**
   * Obtains the region by its id
   * 
   * @param regionId
   * @return EventMovieTO
   */
  RegionTO<CatalogTO, CatalogTO> findRegion( int regionId );

  /**
   * Count the preRelease Booking Booked
   * 
   * @param Ev
   */
  Long countPrereleaseBookingBooked( EventMovieTO eventMovieTO );

  /**
   * Get the number of theaters linked at the region
   * 
   * @param Long idRegion
   */
  int getTheatersInRegion( Long idRegion );

  /**
   * Method to find the theater ids with presales booked by week, event and region
   * 
   * @param idWeek
   * @param idEvent
   * @param idRegion
   * @return theaterIds
   */
  List<Long> findTheatersByWeekEventAndRegion( Long idWeek, Long idEvent, Long idRegion );

  /**
   * Method to find the event ids in presales booked by week and theater
   * 
   * @param idWeek
   * @param idTheater
   * @return eventIds
   */
  List<Long> findEventsByWeekAndTheater( Long idWeek, Long idTheater );

  /**
   * Method to get the message template for email.
   * 
   * @return message
   */
  String getTemplateBodyMessage();

  /**
   * Method that gets the email template for the specified week and region
   * 
   * @param weekTO {@link WeekTO} with the week identifier.
   * @param regionId Region identifier.
   * @return {@link EmailTemplateTO} with the email template information.
   * @author afuentes
   */
  EmailTemplateTO getEmailTemplateRegion( WeekTO weekTO, Long regionId );

  /**
   * Method that gets the theater email template for the specified region
   * 
   * @param weekTO {@link WeekTO} with the week identifier.
   * @param regionId Region identifier.
   * @return {@link EmailTemplateTO} with the email template information.
   * @author afuentes
   */
  EmailTemplateTO getEmailTemplateTheater( WeekTO weekTO, Long regionId );
}
