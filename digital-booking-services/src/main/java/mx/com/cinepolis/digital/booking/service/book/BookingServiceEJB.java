package mx.com.cinepolis.digital.booking.service.book;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EmailTemplateTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
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
 * Interface that defines movie and theater booking services.
 * 
 * @author agustin.ramirez
 * @author afuentes
 */
@Local
public interface BookingServiceEJB
{

  /**
   * Method that cancels a booking and saves the reason as the booking observation.
   * 
   * @param bookingTO {@link BookingTO} with the booking and observation information.
   * @author afuentes
   */
  void cancelBooking( List<BookingTO> bookingTOs );

  /**
   * Method that terminates a booking and saves the reason as the booking observation.
   * 
   * @param bookingTO {@link BookingTO} with the booking and observation information.
   * @author afuentes
   */
  void terminateBooking( BookingTO bookingTO );

  /**
   * Method that depending on the case, saves or updates the booking observation provided.
   * 
   * @param bookingObservationTO {@link BookingObservationTO} with the booking observation information.
   * @author afuentes
   */
  void saveOrUpdateBookingObservation( BookingObservationTO bookingObservationTO );

  /**
   * Method that finds all theaters whether the specified movie is booked or not, using the provided search criterion.
   * <ul>
   * <li>BookingQuery.BOOKING_WEEK_ID (Week identifier)
   * <li>BookingQuery.BOOKING_EVENT_ID (Event identifier)
   * </ul>
   * 
   * @param pagingRequestTO {@link PagingRequestTO} with the search criterion.
   * @return {@link PagingResponseTO} with the list of {@link BookingTO} with the theater booking information.
   * @author gsegura
   * @author afuentes
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

  /**
   * Obtains the bookings of a Theater during a week
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<BookingTO> findBookingMoviesByTheater( PagingRequestTO pagingRequestTO );

  /**
   * Method that finds all active Weeks, list first the active week, then the last week, then two weeks later and later
   * the special weeks if any during those periods.
   * 
   * @param abstractTO {@link AbstractTO} with the current date and session information.
   * @return List of {@link WeekTO} with all active Weeks.
   * @author gsegura
   */
  List<WeekTO> findWeeksActive( AbstractTO abstractTO );

  /**
   * Method that finds all active movies.
   * 
   * @return List of {@link CatalogTO} with the active movies information.
   * @author afuentes
   */
  List<CatalogTO> findAllActiveMovies( boolean premiere, boolean festival, boolean prerelease );

  /**
   * Method that finds all active regions.
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
   * Finds the week given its id
   * 
   * @param weekId
   * @return
   */
  WeekTO findWeek( int weekId );

  /**
   * Sends the booking report to a theater given its id and week
   * 
   * @param bookingTO
   */
  void sendTheaterEmail( BookingTO bookingTO );

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
   * Finds the bookings associated to the theaters given its region and week
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<TheaterTO> findTheatersByBookingWeekAndRegion( PagingRequestTO pagingRequestTO );

  /**
   * Finds the bookings associated to the theaters given its region and week
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<TheaterTO> findTheatersByBookingWeekAndRegionForPresaleReport( PagingRequestTO pagingRequestTO );

  /**
   * Sends the booking report for a list of theaters during a week
   * 
   * @param weekIdSelected
   * @param theatersSelected
   */
  void sendTheatersEmail( Long weekIdSelected, List<TheaterTO> theatersSelected, String template, String subject );

  /**
   * Method that gets the theater email template for the specified region
   * 
   * @param weekTO {@link WeekTO} with the week identifier.
   * @param regionId Region identifier.
   * @return {@link EmailTemplateTO} with the email template information.
   * @author afuentes
   */
  EmailTemplateTO getEmailTemplateTheater( WeekTO weekTO, Long regionId );

  /**
   * Gets the email template for the region responsibles
   * 
   * @param weekTO
   * @return
   */
  String getEmailTemplateRegion( WeekTO weekTO );

  /**
   * Method that gets the region email template for the specified week and region
   * 
   * @param weekTO {@link WeekTO} with the week identifier.
   * @param regionId Region identifier.
   * @return {@link EmailTemplateTO} with the email template information.
   * @author afuentes
   */
  EmailTemplateTO getEmailTemplateRegion( WeekTO weekTO, Long regionId );

  /**
   * Sends the email for the region responsibles
   * 
   * @param regionEmailTO
   */
  void sendRegionEmail( RegionEmailTO regionEmailTO );

  /**
   * Validates if theaters have assigned at least 1 screen for each booking
   * 
   * @param booking
   * @return
   */
  boolean validateBookingTheaterScreens( BookingTO booking );

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
  void cancelOrTeminateBookings( List<BookingTO> bookingsForCancellationOrTemination, boolean isCancellation );

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
   * Finds the region given its id
   * 
   * @param regionId
   * @return
   */
  RegionTO<CatalogTO, CatalogTO> findRegion( int regionId );

  /**
   * @param eventMovieTo
   * @return int the number of PreRelease Bookings Booked
   */
  Long countPreReleaseBookingBooked( EventMovieTO eventMovieTo );

  /**
   * @param Long idRegion
   * @return int the number of Theaters in the Region
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
   * Method for get the defualt template of the mail.
   * 
   * @return message
   */
  String getTemplateByTheater();

  /**
   * Method that sends the booking report to the theater. The e-mail subject and body are obtained based on the region
   * selected.
   * 
   * @param bookingTO
   * @param regionId
   * @author afuentes
   */
  void sendTheaterEmail( BookingTO bookingTO, Long regionId );

}
