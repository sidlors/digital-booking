package mx.com.cinepolis.digital.booking.integration.dashboard.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.integration.dashboard.ServiceDashboardIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.movie.ServiceAdminMovieEJB;
import mx.com.cinepolis.digital.booking.service.newsfeed.ServiceNewsFeedEJB;
import mx.com.cinepolis.digital.booking.service.theater.ServiceAdminTheaterEJB;

/**
 * Implementation of the interface
 * {@link mx.com.cinepolis.digital.booking.integration.dashboard.ServiceDashboardIntegratorEJB}
 * 
 * @author gsegura
 * @since 0.3.0
 */
@Stateless
@Local(value = ServiceDashboardIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceDashboardIntegratorEJBImpl implements ServiceDashboardIntegratorEJB
{

  @EJB
  private ServiceAdminTheaterEJB serviceAdminTheaterEJB;

  @EJB
  private ServiceAdminMovieEJB serviceAdminMovieEJB;

  @EJB
  private ServiceNewsFeedEJB serviceNewsFeedEJB;

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TheaterTO> getMyTheaters( AbstractTO abstractTO )
  {
    return serviceAdminTheaterEJB.getMyTheaters( abstractTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IncomeTO> getTopWeek( AbstractTO abstractTO )
  {
    return serviceAdminMovieEJB.getTopWeek( abstractTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<NewsFeedObservationTO> getNewsFeeds( AbstractTO abstractTO )
  {
    return serviceNewsFeedEJB.getNewsFeeds( abstractTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createNewsFeed( NewsFeedObservationTO newsFeedObservationTO )
  {
    serviceNewsFeedEJB.createNewsFeed( newsFeedObservationTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void editNewsFeed( NewsFeedObservationTO newsFeedObservationTO )
  {
    serviceNewsFeedEJB.editNewsFeed( newsFeedObservationTO );

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteNewsFeed( NewsFeedObservationTO newsFeedObservationTO )
  {
    serviceNewsFeedEJB.deleteNewsFeed( newsFeedObservationTO );

  }

}
