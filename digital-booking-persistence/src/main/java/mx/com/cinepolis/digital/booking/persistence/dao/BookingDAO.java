package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.BookingDO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
@Local
public interface BookingDAO extends GenericDAO<BookingDO>
{

  /**
   * Finds all bookings that satisfies the query
   * 
   * @param pagingRequestTO a paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<BookingTO>} with the results
   */
  PagingResponseTO<EventTO> findPresaleBookingsByPaging( PagingRequestTO pagingRequestTO );

  /**
   * Finds all records that satisfies the query
   * 
   * @param pagingRequestTO a paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<BookingTO>} with the results
   */
  PagingResponseTO<BookingTO> findAllByPaging( PagingRequestTO pagingRequestTO );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} into a record
   * 
   * @param bookingTO
   */
  void save( BookingTO bookingTO );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} associated with record
   * 
   * @param bookingTO
   */
  void update( BookingTO bookingTO );

  /**
   * Removes a record associated with the catalog
   * 
   * @param bookingTO
   */
  void delete( BookingTO bookingTO );

  /**
   * Finds a booking by its id
   * 
   * @param id
   * @param idWeek
   * @return
   */
  BookingTO get( Long id, Integer idWeek );

  /**
   * Finds a booking by its id and language
   * 
   * @param id
   * @param idWeek
   * @param language
   * @return
   */
  BookingTO get( Long id, Integer idWeek, Language language );

  /**
   * Find Theater by idWeek(almost a program booking) and idRegion
   * 
   * @param idWeek
   * @param idRegion
   * @return
   */
  PagingResponseTO<TheaterTO> findTheatersByIdWeekAndIdRegion( PagingRequestTO requestTO );

  /**
   * Finds Theater by Week and Region
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<TheaterTO> findTheatersByBookingWeekAndRegion( PagingRequestTO pagingRequestTO );

  /**
   * Finds Theater by Week and Region
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<TheaterTO> findTheatersByBookingWeekAndRegionForPresaleReport( PagingRequestTO pagingRequestTO );

  /**
   * @param idTheater
   * @param idEvent
   * @return
   */
  BookingDO findByEventIdAndTheaterId( Long idTheater, Long idEvent );

  /**
   * @param idTheater
   * @return
   */
  List<BookingDO> findBookedByTheater( Long idTheater );

  /**
   * @param pagingRequestTO
   * @return
   */
  List<BookingTO> findBookingsByEventRegionWeek( PagingRequestTO pagingRequestTO );

  /**
   * Counts the exhibition weeks (bookings) of a movie
   * 
   * @param eventTO
   * @return
   */
  int findNumberOfExhibitionWeeks( EventTO eventTO );

  /**
   * Gets the booking semaphore for the week and theater
   * 
   * @param idWeek
   * @param theater
   * @return
   */
  String getTheaterSemaphore( Long idWeek, TheaterTO theater );

  /**
   * Finds the current top movies booked
   * 
   * @param currentWeeks
   * @param abstractTO
   * @return
   */
  List<EventMovieTO> findTopWeekBookedMovies( List<WeekTO> currentWeeks, AbstractTO abstractTO );

  /**
   * Method to get a list of completed schedules with active copies.
   * 
   * @param date
   * @return
   */
  List<BookingTO> findBookingsExceeded( Integer weekId, Long theaterId );

  /**
   * Method used to adjust the BookingDO and BookingWeekDO
   * 
   * @param idWeek
   * @param idTheater
   * @param to
   * @param bookingTOs
   */
  void adjustBookings( Integer idWeek, Long idTheater, List<BookingTO> bookingTOs );

  /**
   * Method that finds the Bookings associated with a given id theater which are booked and active
   * 
   * @param idTheater
   * @return List of {@link mx.com.cinepolis.digital.booking.model.BookingDO}
   */
  List<BookingDO> findByIdTheaterAndBooked( Long idTheater );

  /**
   * Method that finds if a booking week has at least a zero screen booked
   * 
   * @param idWeek
   * @param theater
   * @return
   */
  boolean hasZeroBookings( Long idWeek, TheaterTO theater );

  /**
   * Method that counts the number of bookings that contains the idEvent.
   * 
   * @param idMovie Event identifier.
   * @return {@link java.lang.Number} with the number of bookings containing the idEvent.
   * @author jreyesv
   */
  int countBookedByIdEventMovie( Long idMovie );
  
  /**
   * @param idEvent
   * @return
   */
  Long countPrereleaseBooked( Long idEvent );

  /**
   * Method that extracts the special event booking and transforms it in BookingTheaterTO objects.
   * 
   * @return bookingTheaterTOs The list of special event bookings
   */
  List<BookingTheaterTO> extractBookingSpecialEvents( Long idTheater, int idWeek );

  /**
   * Method that find the theaters with presales booked by week and region.
   * 
   * @param idWeek
   * @param idRegion
   * @return resultList
   */
  List<Object[]> findTheaterBookedPresalesByWeekAndRegion( Long idWeek, Long idRegion );

  /**
   * Method to find bookings presales booked by week ,region. and event
   * 
   * @param pagingRequestTO
   * @return PagingResponseTO<BookingTO>
   */
  PagingResponseTO<BookingTO> findPresaleBookingsByIdEventIdRegionAndIdWeek( PagingRequestTO pagingRequestTO );

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
   * Method that finds if a premiere is booked
   * 
   * @return
   * @author shernandez
   */
  List<String> findPremiereBooking();
}
