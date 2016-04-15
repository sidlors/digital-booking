package mx.com.cinepolis.digital.booking.integration.booking.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EmailTemplateTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.MovieBookingWeekTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterBookingWeekTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.book.BookingServiceEJB;
import mx.com.cinepolis.digital.booking.service.reports.ServiceReportsEJB;

/**
 * Class that implements the integration services relating to booking movies.
 * 
 * @author afuentes
 */
@Stateless
@Local(value = BookingServiceIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BookingServiceIntegratorEJBImpl implements BookingServiceIntegratorEJB
{

  @EJB
  private BookingServiceEJB bookingServiceEJB;

  @EJB
  private ServiceReportsEJB serviceReportsEJB;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.
   * BookingServiceIntegratorEJB#cancelBooking(mx.com.cinepolis .digital.booking.model.to.BookingTO)
   */
  @Override
  public void cancelBooking( List<BookingTO> bookingTOs )
  {
    bookingServiceEJB.cancelBooking( bookingTOs );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.
   * BookingServiceIntegratorEJB#saveOrUpdateBookingObservation
   * (mx.com.cinepolis.digital.booking.model.to.BookingObservationTO)
   */
  @Override
  public void saveOrUpdateBookingObservation( BookingObservationTO bookingObservationTO )
  {
    bookingServiceEJB.saveOrUpdateBookingObservation( bookingObservationTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.
   * BookingServiceIntegratorEJB#findBookingMovies(mx.com.cinepolis .digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public MovieBookingWeekTO findBookingMovies( PagingRequestTO pagingRequestTO )
  {
    return bookingServiceEJB.findBookingMovies( pagingRequestTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.
   * BookingServiceIntegratorEJB#findBookingMoviesByTheater(mx. com.cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public TheaterBookingWeekTO findBookingTheater( PagingRequestTO pagingRequestTO )
  {
    return bookingServiceEJB.findBookingTheater( pagingRequestTO );
  }

  @Override
  public PagingResponseTO<BookingTO> findBookingMoviesByTheater( PagingRequestTO pagingRequestTO )
  {
    return bookingServiceEJB.findBookingMoviesByTheater( pagingRequestTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.
   * BookingServiceIntegratorEJB#findWeeksActive(mx.com.cinepolis .digital.booking.model.to.AbstractTO)
   */
  @Override
  public List<WeekTO> findWeeksActive( AbstractTO abstractTO )
  {
    return bookingServiceEJB.findWeeksActive( abstractTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking. BookingServiceIntegratorEJB#findAllActiveMovies()
   */
  @Override
  public List<CatalogTO> findAllActiveMovies( boolean festival, boolean prerelease )
  {
    return bookingServiceEJB.findAllActiveMovies( false, festival, prerelease );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.
   * BookingServiceIntegratorEJB#findAllActiveRegions(mx.com.cinepolis .digital.booking.model.to.AbstractTO)
   */
  @Override
  public List<CatalogTO> findAllActiveRegions( AbstractTO abstractTO )
  {
    return bookingServiceEJB.findAllActiveRegions( abstractTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking. BookingServiceIntegratorEJB#findAllPremieres()
   */
  @Override
  public List<EventMovieTO> findAllPremieres()
  {
    return bookingServiceEJB.findAllPremieres();
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.
   * BookingServiceIntegratorEJB#saveOrUpdateBookings(java.util.List)
   */
  @Override
  public void saveOrUpdateBookings( List<BookingTO> bookingTOs )
  {
    bookingServiceEJB.saveOrUpdateBookings( bookingTOs );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking. BookingServiceIntegratorEJB#v(java.util.List)
   */
  @Override
  public void cancelSpecialEventBookings( List<BookingTO> bookingTOs )
  {
    bookingServiceEJB.cancelSpecialEventBookings( bookingTOs );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking. BookingServiceIntegratorEJB#v(java.util.List)
   */
  @Override
  public void cancelPresaleInBookings( List<BookingTO> bookingTOs )
  {
    bookingServiceEJB.cancelPresaleInBookings( bookingTOs );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking. BookingServiceIntegratorEJB
   * #findTheatersByIdWeekAndIdRegion(mx.com.cinepolis .digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public PagingResponseTO<TheaterTO> findTheatersByIdWeekAndIdRegion( PagingRequestTO requestTO )
  {

    return bookingServiceEJB.findTheatersByIdWeekAndIdRegion( requestTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.
   * BookingServiceIntegratorEJB#terminateBooking(mx.com.cinepolis.digital.booking.model.to.BookingTO)
   */
  @Override
  public void terminateBooking( BookingTO bookingTO )
  {
    bookingServiceEJB.terminateBooking( bookingTO );
  }

  @Override
  public FileTO getWeeklyBookingReportByTheater( Long idWeek, Long idTheater )
  {
    BookingTO booking = new BookingTO();
    booking.setTheater( new TheaterTO() );
    booking.getTheater().setId( idTheater );
    booking.setWeek( new WeekTO( idWeek.intValue() ) );
    return serviceReportsEJB.getWeeklyBookingReportByTheater( idWeek, idTheater );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB#
   * getWeeklyDistributorReportByDistributor(java.lang.Long, java.lang.Long)
   */
  @Override
  public FileTO getWeeklyDistributorReportByDistributor( Long idWeek, Long idRegion, Long idDistributor )
  {
    return serviceReportsEJB.getWeeklyDistributorReportByDistributor( idWeek, idRegion, idDistributor );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB#
   * getWeeklyDistributorReportByAllDistributors(java.lang.Long)
   */
  @Override
  public FileTO getWeeklyDistributorReportByAllDistributors( Long idWeek, Long idRegion )
  {
    return serviceReportsEJB.getWeeklyDistributorReportByAllDistributors( idWeek, idRegion );
  }

  @Override
  public WeekTO findWeek( int weekId )
  {
    return bookingServiceEJB.findWeek( weekId );
  }

  @Override
  public RegionTO<CatalogTO, CatalogTO> findRegion( int regionId )
  {
    return bookingServiceEJB.findRegion( regionId );
  }

  @Override
  public void sendTheaterEmail( BookingTO bookingTO )
  {
    this.bookingServiceEJB.sendTheaterEmail( bookingTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendTheaterEmail( BookingTO bookingTO, Long regionId )
  {
    this.bookingServiceEJB.sendTheaterEmail( bookingTO, regionId );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB#findAvailableMovies(java.lang.
   * Long, java.lang.Long)
   */
  @Override
  public List<CatalogTO> findAvailableMovies( Long idWeek, Long idTheater )
  {
    return bookingServiceEJB.findAvailableMovies( idWeek, idTheater );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB#findTheaterScreens(java.lang.Long,
   * java.lang.Long)
   */
  @Override
  public List<ScreenTO> findTheaterScreens( Long idTheater, Long idEvent )
  {
    return bookingServiceEJB.findTheaterScreens( idTheater, idEvent );
  }

  @Override
  public PagingResponseTO<TheaterTO> findTheatersByBookingWeekAndRegion( PagingRequestTO pagingRequestTO )
  {
    return bookingServiceEJB.findTheatersByBookingWeekAndRegion( pagingRequestTO );
  }

  @Override
  public PagingResponseTO<TheaterTO> findTheatersByBookingWeekAndRegionForPresaleReport( PagingRequestTO pagingRequestTO )
  {
    return bookingServiceEJB.findTheatersByBookingWeekAndRegionForPresaleReport( pagingRequestTO );
  }

  @Override
  public void sendTheatersEmail( Long weekIdSelected, List<TheaterTO> theatersSelected, String template, String subject )
  {
    this.bookingServiceEJB.sendTheatersEmail( weekIdSelected, theatersSelected, template, subject );
  }

  @Override
  public String getEmailTemplateRegion( WeekTO weekTO )
  {
    return this.bookingServiceEJB.getEmailTemplateRegion( weekTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EmailTemplateTO getEmailTemplateRegion( WeekTO weekTO, Long regionId )
  {
    return bookingServiceEJB.getEmailTemplateRegion( weekTO, regionId );
  }

  @Override
  public FileTO getWeeklyBookingReportByRegion( Long weekId, Long regionId )
  {
    return this.serviceReportsEJB.getWeeklyBookingReportByRegion( weekId, regionId );
  }

  @Override
  public void sendRegionEmail( RegionEmailTO regionEmailTO )
  {
    this.bookingServiceEJB.sendRegionEmail( regionEmailTO );
  }

  @Override
  public boolean validatePreviewDocument( Long idWeek, Long idTheater )
  {
    BookingTO booking = new BookingTO();
    booking.setTheater( new TheaterTO() );
    booking.getTheater().setId( idTheater );
    booking.setWeek( new WeekTO( idWeek.intValue() ) );
    return this.bookingServiceEJB.validateBookingTheaterScreens( booking );
  }

  @Override
  public List<EventTO> findEventsForScreen( ScreenTO screenTO )
  {
    return this.bookingServiceEJB.findEventsForScreen( screenTO );
  }

  @Override
  public List<CatalogTO> getBookingStatus( List<Long> statusId )
  {
    return this.bookingServiceEJB.getBookingStatus( statusId );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB#findFollowingWeekBooking(java.
   * lang.Long, java.lang.Integer)
   */
  @Override
  public WeekTO findFollowingWeekBooking( Long idBooking, Integer idWeek )
  {
    return bookingServiceEJB.findFollowingWeekBooking( idBooking, idWeek );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB#cancelOrTeminateBookings(java.
   * util.List, boolean)
   */
  @Override
  public void cancelOrTeminateBookings( List<BookingTO> bookingsForCancellation, boolean isCancellation )
  {
    bookingServiceEJB.cancelOrTeminateBookings( bookingsForCancellation, isCancellation );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB#isBookingWeekSent(mx.com.cinepolis
   * .digital.booking.model.to.BookingTO)
   */
  @Override
  public boolean isBookingWeekSent( BookingTO bookingTO )
  {
    return bookingServiceEJB.isBookingWeekSent( bookingTO );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB#hasBookingWeek(mx.com.cinepolis
   * .digital.booking.model.to.BookingTO)
   */
  @Override
  public boolean hasBookingWeek( BookingTO bookingTO )
  {
    return bookingServiceEJB.hasBookingWeek( bookingTO );
  }

  /**
   * (non-Javadoc)
   */
  @Override
  public int getTheatersInRegion( Long idRegion )
  {
    return bookingServiceEJB.getTheatersInRegion( idRegion );
  }

  /**
   * (non-Javadoc)
   * 
   * @see mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB#updateMovie(mx.com.cinepolis.
   *      digital.booking.model.to.EventMovieTO)
   */
  @Override
  public Long countPrereleaseBookingBooked( EventMovieTO eventMovieTO )
  {

    return bookingServiceEJB.countPreReleaseBookingBooked( eventMovieTO );
  }

  /**
   * (non-Javadoc)
   */
  @Override
  public List<Long> findTheatersByWeekEventAndRegion( Long idWeek, Long idEvent, Long idRegion )
  {
    return bookingServiceEJB.findTheatersByWeekEventAndRegion( idWeek, idEvent, idRegion );
  }

  /**
   * (non-Javadoc)
   */
  @Override
  public List<Long> findEventsByWeekAndTheater( Long idWeek, Long idTheater )
  {
    return bookingServiceEJB.findEventsByWeekAndTheater( idWeek, idTheater );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB#getTemplateBodyMessage()
   */
  @Override
  public String getTemplateBodyMessage()
  {
    return bookingServiceEJB.getTemplateByTheater();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EmailTemplateTO getEmailTemplateTheater( WeekTO weekTO, Long regionId )
  {
    return bookingServiceEJB.getEmailTemplateTheater( weekTO, regionId );
  }

}
