package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.model.BookingWeekDO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.BookingWeekDO}
 * 
 * @author gsegura
 * @since 0.2.7
 */
public interface BookingWeekDAO extends GenericDAO<BookingWeekDO>
{

  /**
   * Updates the send flag of a BookingWeek
   * 
   * @param bookingTO
   */
  void updateSentStatus( BookingTO bookingTO );

  /**
   * Count records associated to a week.
   * 
   * @param idWeek
   * @return
   */
  int countBookingWeek( WeekDO idWeek );

  /**
   * Method that finds all BookingWeeks associated to the booking specified that has a <code>startingDayWeek</code>
   * after the given <code>referenceStartingDay</code>.
   * 
   * @param idBooking Booking identifier.
   * @param referenceStartingDay Reference {@link Date} used to compare the BookingWeeks.
   * @return List of {@link BookingWeekDO} associated to the BookingWeeks found.
   * @author afuentes
   */
  List<BookingWeekDO> findfollowingWeekBooking( Long idBooking, Date referenceStartingDay );

  /**
   * Method that finds the BookingWeeks associated with the booking and week provided.
   * 
   * @param idBooking The booking identifier.
   * @param idWeek The week identifier.
   * @return List of {@link BookingWeekDO} associated to the BookingWeeks found.
   * @author afuentes
   */
  List<BookingWeekDO> findByBookingAndWeek( Long idBooking, Integer idWeek );

  /**
   * Method that finds the BookingWeeks associated withe the id Theater and the id week
   * 
   * @param idTheater
   * @param idWeek
   * @return List of @ BookingWeekDO} associated to the BookingWeeks found.
   */
  List<BookingWeekDO> findByIdTheaterAndIdWeek( Long idTheater, Integer idWeek );
}
