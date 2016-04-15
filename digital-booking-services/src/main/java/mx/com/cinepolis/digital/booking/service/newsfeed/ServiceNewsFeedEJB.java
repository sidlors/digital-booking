package mx.com.cinepolis.digital.booking.service.newsfeed;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO;

/**
 * @author gsegura
 * @since 0.3.0
 */
@Local
public interface ServiceNewsFeedEJB
{

  /**
   * @param abstractTO
   * @return
   */
  List<NewsFeedObservationTO> getNewsFeeds( AbstractTO abstractTO );

  /**
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
   * 
   * @param idNewsFeed
   * @return
   */
  NewsFeedObservationTO getNewsFeed(Long idNewsFeed);
  
  /**
   * 
   * @param newsFeedObservationTO
   * @return
   */
  boolean validateNewsFeed( NewsFeedObservationTO newsFeedObservationTO );

}
