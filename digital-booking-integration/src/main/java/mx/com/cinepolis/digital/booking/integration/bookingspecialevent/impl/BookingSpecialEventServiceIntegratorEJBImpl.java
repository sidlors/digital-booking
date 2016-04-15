package mx.com.cinepolis.digital.booking.integration.bookingspecialevent.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventBookingTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.integration.bookingspecialevent.BookingSpecialEventServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.book.BookingServiceEJB;
import mx.com.cinepolis.digital.booking.service.bookingspecialevent.BookingSpecialEventServiceEJB;
import mx.com.cinepolis.digital.booking.service.reports.ServiceReportsEJB;

/**
 * Class that implements the integration services relating to booking special events.
 * 
 * @author shernandezl
 */
@Stateless
@Local(value = BookingSpecialEventServiceIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BookingSpecialEventServiceIntegratorEJBImpl implements BookingSpecialEventServiceIntegratorEJB
{
  @EJB
  private BookingSpecialEventServiceEJB bookingSpecialEventServiceEJB;

  @EJB
  private BookingServiceEJB bookingServiceEJB;

  @EJB
  private ServiceReportsEJB serviceReportsEJB;

  /**
   * {@inheritDoc}
   */
  @Override
  public SpecialEventBookingTO findBookingSpecialEvent( PagingRequestTO pagingRequestTO )
  {
    return bookingSpecialEventServiceEJB.findBookingSpecialEvent( pagingRequestTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveOrUpdateBookings( List<SpecialEventTO> specialEventTOs )
  {
    bookingSpecialEventServiceEJB.saveOrUpdateBookings( specialEventTOs );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cancelBookings( List<SpecialEventTO> specialEventTOs )
  {
    bookingSpecialEventServiceEJB.cancelBookings( specialEventTOs );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<WeekTO> findWeeksActive( AbstractTO abstractTO )
  {
    return bookingServiceEJB.findWeeksActive( abstractTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> findAllActiveRegions( AbstractTO abstractTO )
  {
    return bookingServiceEJB.findAllActiveRegions( abstractTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PagingResponseTO<EventTO> getEventsBookedForReport( PagingRequestTO pagingRequestTO )
  {
    return this.bookingSpecialEventServiceEJB.getEventsBookedForReport( pagingRequestTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendPresalesBookedByRegionEmail( RegionEmailTO regionEmailTO, List<EventTO> eventSelected )
  {
    this.bookingSpecialEventServiceEJB.sendPresalesBookedByRegionEmail( regionEmailTO, eventSelected );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FileTO getWeeklyBookingReportPresaleByRegion( List<EventTO> movies, Long idWeek, Long idRegion )
  {
    return this.serviceReportsEJB.getWeeklyBookingReportPresaleByRegion( movies, idWeek, idRegion );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FileTO getWeeklyBookingReportPresaleByTheater( List<EventTO> movies, Long idWeek, Long idTheater )
  {
    return this.serviceReportsEJB.getWeeklyBookingReportPresaleByTheater( movies, idWeek, idTheater );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendPresalesBookedByTheaterEmail( TheaterEmailTO theaterEmailTO, List<EventTO> eventSelected )
  {
    this.bookingSpecialEventServiceEJB.sendPresalesBookedByTheaterEmail( theaterEmailTO, eventSelected );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<BookingTO> findBookingsInPresaleByEevntZoneAndWeek( List<EventTO> movies, Long idWeek, Long idRegion )
  {
    return this.serviceReportsEJB.findBookingsInPresaleByEevntZoneAndWeek( movies, idWeek, idRegion );
  }
}
