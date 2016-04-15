package mx.com.cinepolis.digital.booking.integration.dashboard;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;

/**
 * Interface for the services for the Home Dashboard
 * 
 * @author gsegura
 * @since 0.3.0
 */
public interface ServiceDashboardIntegratorEJB
{

  /**
   * Obtains a {@link java.util.List<TheaterTO>} with the theaters assigned to the user by region
   * 
   * @param abstractTO
   * @return
   */
  List<TheaterTO> getMyTheaters( AbstractTO abstractTO );

  /**
   * Obtains a {@link java.util.List<EventMovieTO>} with the list of movies that have being most weeks
   * 
   * @param abstractTO
   * @return
   */
  List<IncomeTO> getTopWeek( AbstractTO abstractTO );

  /**
   * Obtains a {@link java.util.List<NewsFeedObservationTO>} with the list of news feeds that are available for the user
   * 
   * @param abstractTO
   * @return
   */
  List<NewsFeedObservationTO> getNewsFeeds( AbstractTO abstractTO );

  /**
   * Creates a news feed
   * 
   * @param newsFeedObservationTO
   */
  void createNewsFeed( NewsFeedObservationTO newsFeedObservationTO );

  /**
   * Edits an existing news feed
   * 
   * @param newsFeedObservationTO
   */
  void editNewsFeed( NewsFeedObservationTO newsFeedObservationTO );

  /**
   * Delete an existing news feed belonging to the logged user
   * 
   * @param abstractTO
   */
  void deleteNewsFeed( NewsFeedObservationTO newsFeedObservationTO );

}
