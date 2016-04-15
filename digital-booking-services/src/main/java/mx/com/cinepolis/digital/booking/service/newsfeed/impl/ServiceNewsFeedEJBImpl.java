package mx.com.cinepolis.digital.booking.service.newsfeed.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO;
import mx.com.cinepolis.digital.booking.persistence.dao.ObservationDAO;
import mx.com.cinepolis.digital.booking.service.newsfeed.ServiceNewsFeedEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;

/**
 * @author gsegura
 * @since 0.3.0
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceNewsFeedEJBImpl implements ServiceNewsFeedEJB
{

  @EJB
  private ObservationDAO observationDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<NewsFeedObservationTO> getNewsFeeds( AbstractTO abstractTO )
  {
    return observationDAO.getNewsFeedObservations( abstractTO.getTimestamp() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createNewsFeed( NewsFeedObservationTO newsFeedObservationTO )
  {
    observationDAO.saveNewsFeedObservation( newsFeedObservationTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void editNewsFeed( NewsFeedObservationTO newsFeedObservationTO )
  {
    observationDAO.updateNewsFeedObservation( newsFeedObservationTO );
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean validateNewsFeed( NewsFeedObservationTO newsFeedObservationTO )
  {
    NewsFeedObservationTO newsFeed = observationDAO.getNewsFeedObservation( newsFeedObservationTO.getIdNewsFeed() );
    return newsFeed.getUser().getId().equals( newsFeedObservationTO.getUserId() );
  }
  

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteNewsFeed( NewsFeedObservationTO newsFeedObservationTO )
  {
    NewsFeedObservationTO newsFeed = observationDAO.getNewsFeedObservation( newsFeedObservationTO.getIdNewsFeed() );
    observationDAO.delete( newsFeed );

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NewsFeedObservationTO getNewsFeed( Long idNewsFeed )
  {
    return observationDAO.getNewsFeedObservation( idNewsFeed );
  }
}
