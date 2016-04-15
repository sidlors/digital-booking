package mx.com.cinepolis.digital.booking.integration.home.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.integration.home.ServiceHomeIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.book.BookingServiceEJB;
import mx.com.cinepolis.digital.booking.service.movie.ServiceAdminMovieEJB;
import mx.com.cinepolis.digital.booking.service.newsfeed.ServiceNewsFeedEJB;
import mx.com.cinepolis.digital.booking.service.theater.ServiceAdminTheaterEJB;

/**
 * Class that implements the integration services relating to home.
 * 
 * @author kperez
 */
@Stateless
@Local(value = ServiceHomeIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceHomeIntegratorEJBImpl implements ServiceHomeIntegratorEJB
{

  @EJB
  private BookingServiceEJB bookingServiceEJB;

  @EJB
  private ServiceAdminTheaterEJB serviceAdminTheaterEJB;

  @EJB
  private ServiceAdminMovieEJB serviceAdminMovieEJB;

  @EJB
  private ServiceNewsFeedEJB serviceNewsFeedEJB;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.home.ServiceHomeIntegratorEJB#findAllPremieres()
   */
  @Override
  public List<EventMovieTO> findAllPremieres()
  {
    return bookingServiceEJB.findAllPremieres();
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.theater.ServiceAdminTheaterEJB#getMyTheaters(AbstractTO)
   */
  @Override
  public List<TheaterTO> getMyTheaters( AbstractTO abstractTO )
  {
    return serviceAdminTheaterEJB.getMyTheaters( abstractTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.theater.ServiceAdminTheaterEJB#getTopWeek(AbstractTO)
   */
  @Override
  public List<IncomeTO> getTopWeek( AbstractTO abstractTO )
  {
    return serviceAdminMovieEJB.getTopWeek( abstractTO );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.service.theater.ServiceAdminTheaterEJB#getCurrentWeek(mx.com.cinepolis.digital
   * .booking.commons.to.AbstractTO)
   */
  @Override
  public WeekTO getTopWeekTO( AbstractTO abstractTO )
  {
    return serviceAdminMovieEJB.getTopWeekTO( abstractTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.theater.ServiceAdminTheaterEJB#getNewsFeeds(AbstractTO)
   */
  @Override
  public List<NewsFeedObservationTO> getNewsFeeds( AbstractTO abstractTO )
  {
    return serviceNewsFeedEJB.getNewsFeeds( abstractTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.theater.ServiceAdminTheaterEJB#createNewsFeed(NewsFeedObservationTO)
   */
  @Override
  public void createNewsFeed( NewsFeedObservationTO newsFeedObservationTO )
  {
    serviceNewsFeedEJB.createNewsFeed( newsFeedObservationTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.theater.ServiceAdminTheaterEJB#editNewsFeed(NewsFeedObservationTO)
   */
  @Override
  public void editNewsFeed( NewsFeedObservationTO newsFeedObservationTO )
  {
    serviceNewsFeedEJB.editNewsFeed( newsFeedObservationTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.theater.ServiceAdminTheaterEJB#deleteNewsFeed(NewsFeedObservationTO)
   */
  @Override
  public void deleteNewsFeed( NewsFeedObservationTO newsFeedObservationTO )
  {
    serviceNewsFeedEJB.deleteNewsFeed( newsFeedObservationTO );
  }

  /*
   * {@inheritDoc}
   * @see mx.com.cinepolis.digital.booking.integration.home.ServiceHomeIntegratorEJB#validateNewsFeed(java.lang.Long)
   */
  @Override
  public boolean validateNewsFeed( NewsFeedObservationTO newsFeedObservationTO )
  {
    return serviceNewsFeedEJB.validateNewsFeed( newsFeedObservationTO );
  }
}
