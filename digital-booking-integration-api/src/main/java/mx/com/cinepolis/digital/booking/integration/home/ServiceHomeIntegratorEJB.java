package mx.com.cinepolis.digital.booking.integration.home;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;

/**
 * Interface that defines the integration services relating to home.
 * 
 * @author kperez
 */
public interface ServiceHomeIntegratorEJB
{

  /**
   * Method that finds all movie premieres.
   * 
   * @return List of {@link EventMovieTO} with the movie premieres information.
   */
  List<EventMovieTO> findAllPremieres();

  /**
   * Method that finds all user associated theaters .
   * 
   * @param abstractTO
   * @return List of {@link TheaterTO} with the theaters information.
   */
  List<TheaterTO> getMyTheaters( AbstractTO abstractTO );

  /**
   * Method that finds all top movie of the current week.
   * 
   * @param abstractTO
   * @return List of {@link EventMovieTO} with the top movies of the current week information.
   */
  List<IncomeTO> getTopWeek( AbstractTO abstractTO );

  /**
   * Method that returns the current week for cinepolis.
   * 
   * @param abstractTO, with the signed information.
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.WeekTO}, with the current week information.
   * @author jreyesv
   */
  WeekTO getTopWeekTO( AbstractTO abstractTO );

  /**
   * Method that finds all news feeds of the users.
   * 
   * @param abstractTO
   * @return List of {@link NewsFeedObservationTO} with all news feeds.
   */
  List<NewsFeedObservationTO> getNewsFeeds( AbstractTO abstractTO );

  /**
   * Method that create news feeds.
   * 
   * @param newsFeedObservationTO
   */
  void createNewsFeed( NewsFeedObservationTO newsFeedObservationTO );

  /**
   * @param newsFeedObservationTO
   */
  void editNewsFeed( NewsFeedObservationTO newsFeedObservationTO );

  /**
   * @param newsFeedObservationTO
   */
  void deleteNewsFeed( NewsFeedObservationTO newsFeedObservationTO );

  /**
   * @param newsFeedObservationTO
   * @return
   */
  boolean validateNewsFeed( NewsFeedObservationTO newsFeedObservationTO );
}
