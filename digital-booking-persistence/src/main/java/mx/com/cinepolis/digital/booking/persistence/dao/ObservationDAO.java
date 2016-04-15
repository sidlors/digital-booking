package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.AbstractObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO;
import mx.com.cinepolis.digital.booking.model.ObservationDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.ObservationDO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
public interface ObservationDAO extends GenericDAO<ObservationDO>
{

  /**
   * Saves a news feed
   * 
   * @param newsFeedObservationTO
   */
  void saveNewsFeedObservation( NewsFeedObservationTO newsFeedObservationTO );

  /**
   * Edit a news feed
   * 
   * @param newsFeedObservationTO
   */
  void updateNewsFeedObservation( NewsFeedObservationTO newsFeedObservationTO );

  /**
   * Obtains the news feed for a given day
   * 
   * @param date
   * @return
   */
  List<NewsFeedObservationTO> getNewsFeedObservations( Date date );

  /**
   * Obtains the news feed by its id
   * 
   * @param id
   * @return
   */
  NewsFeedObservationTO getNewsFeedObservation( Long id );

  /**
   * Saves a booking observation
   * 
   * @param bookingObservationTO
   */
  void saveBookingObservation( BookingObservationTO bookingObservationTO );

  /**
   * Edits an observation
   * 
   * @param abstractObservationTO
   */
  void update( AbstractObservationTO abstractObservationTO );

  /**
   * Delete an observation
   * 
   * @param abstractObservationTO
   */
  void delete( AbstractObservationTO abstractObservationTO );
}
