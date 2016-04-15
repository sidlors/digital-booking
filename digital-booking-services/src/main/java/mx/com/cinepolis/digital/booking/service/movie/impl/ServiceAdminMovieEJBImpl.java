package mx.com.cinepolis.digital.booking.service.movie.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.Configuration;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingIncomeDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CountryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.MovieImageDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;
import mx.com.cinepolis.digital.booking.service.income.IncomeServiceEJB;
import mx.com.cinepolis.digital.booking.service.movie.ServiceAdminMovieEJB;
import mx.com.cinepolis.digital.booking.service.util.EventTOToEventMovieTOTransformer;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.ValidatorUtil;

import org.apache.commons.collections.CollectionUtils;

/**
 * Clase que implementa los metodos asociados a la administración de películas
 * 
 * @author rgarcia
 * @author afuentes
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceAdminMovieEJBImpl implements ServiceAdminMovieEJB
{
  private static final String ID_VISTA_UNDEFINED = "0";

  @EJB
  private EventDAO eventDAO;

  @EJB
  private MovieImageDAO movieImageDAO;

  @EJB
  private CountryDAO countryDAO;

  @EJB
  private BookingDAO bookingDAO;

  @EJB
  private WeekDAO weekDAO;
  
  @EJB
  private ConfigurationDAO configurationDAO;
  
  @EJB
  private BookingIncomeDAO bookingIncomeDAO;
  
  @EJB
  private IncomeServiceEJB incomeServiceEJB;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.movie. ServiceAdminMovieEJB
   * #saveMovie(mx.com.cinepolis.digital.booking.model.to.EventMovieTO)
   */
  @Override
  public void saveMovie( EventMovieTO eventMovieTO )
  {
    ValidatorUtil.validateEventMovie( eventMovieTO );
    if( eventMovieTO.getIdVista().equals( ID_VISTA_UNDEFINED )
        || CollectionUtils.isEmpty( eventDAO.findByIdVistaAndActive( eventMovieTO.getIdVista() ) ) )
    {
      if( !CollectionUtils.isEmpty( eventDAO.findByDsCodeDbs( eventMovieTO.getCodeDBS() ) ) )
      {
        throw DigitalBookingExceptionBuilder
            .build( DigitalBookingExceptionCode.CATALOG_ALREADY_REGISTERED_WITH_DS_CODE_DBS );
      }
      eventDAO.save( eventMovieTO, Long.valueOf( getIdDistributorParameter() ) );
    }
    else
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED_WITH_ID_VISTA );
    }
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.service.movie.ServiceAdminMovieEJB#saveMovieImage(mx.com.cinepolis.digital.booking
   * .model.to.FileTO)
   */
  @Override
  public FileTO saveMovieImage( FileTO fileTO )
  {
    ValidatorUtil.validateFileTO( fileTO );
    return movieImageDAO.saveUploadedMovieImage( fileTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.movie.ServiceAdminMovieEJB#findMovieImage(java.lang.Long)
   */
  @Override
  public FileTO findMovieImage( Long idMovieImage )
  {
    return movieImageDAO.findMovieImage( idMovieImage );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.movie.ServiceAdminMovieEJB#getAllContries()
   */
  @Override
  public List<CatalogTO> getAllContries()
  {
    return countryDAO.getAll();
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.movie. ServiceAdminMovieEJB
   * #deleteMovie(mx.com.cinepolis.digital.booking.model.to.EventMovieTO)
   */
  @Override
  public void deleteMovie( EventMovieTO eventMovieTO )
  {
    eventDAO.delete( eventMovieTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.movie. ServiceAdminMovieEJB
   * #updateMovie(mx.com.cinepolis.digital.booking.model.to.EventMovieTO)
   */
  @Override
  public void updateMovie( EventMovieTO eventMovieTO )
  {
    if( !eventMovieTO.getIdVista().equals( ID_VISTA_UNDEFINED ) )
    {
      List<EventDO> eventDOs = eventDAO.findByIdVistaAndActive( eventMovieTO.getIdVista() );
      List<EventTO> eventTOs = eventDAO.findByDsCodeDbs( eventMovieTO.getCodeDBS() );
      for( EventDO eventDO : eventDOs )
      {
        if( !eventDO.getIdEvent().equals( eventMovieTO.getIdEvent() ) )
        {
          throw DigitalBookingExceptionBuilder
              .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED_WITH_ID_VISTA );
        }
        if( !eventTOs.isEmpty() && !eventMovieTO.getIdEvent().equals( eventTOs.get( 0 ).getIdEvent() ) )
        {
          throw DigitalBookingExceptionBuilder
              .build( DigitalBookingExceptionCode.CATALOG_ALREADY_REGISTERED_WITH_DS_CODE_DBS );
        }
      }
    }
    eventDAO.update( eventMovieTO, Long.valueOf(getIdDistributorParameter() ) );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.movie. ServiceAdminMovieEJB
   * #getCatalogMovieSummary(mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public PagingResponseTO<EventMovieTO> getCatalogMovieSummary( PagingRequestTO pagingRequestTO )
  {
    ValidatorUtil.validatePagingRequest( pagingRequestTO );
    PagingResponseTO<EventTO> eventTOResponse = eventDAO.findAllByPaging( pagingRequestTO );
    List<EventMovieTO> eventMovieTOs = new ArrayList<EventMovieTO>();
    CollectionUtils.collect( eventTOResponse.getElements(), new EventTOToEventMovieTOTransformer(), eventMovieTOs );
    PagingResponseTO<EventMovieTO> eventMovieTOResponse = new PagingResponseTO<EventMovieTO>();
    eventMovieTOResponse.setTotalCount( eventTOResponse.getTotalCount() );
    eventMovieTOResponse.setElements( eventMovieTOs );
    return eventMovieTOResponse;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<IncomeTO> getTopWeek( AbstractTO abstractTO )
  {
    WeekTO currentWeek =  weekDAO.getCurrentWeek( abstractTO.getTimestamp() );
    WeekTO lastWeek = incomeServiceEJB.getLastWeek( currentWeek );
    return this.bookingIncomeDAO.findTopEvents( lastWeek);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WeekTO getTopWeekTO( AbstractTO abstractTO )
  {
    WeekTO currentWeek =  weekDAO.getCurrentWeek( abstractTO.getTimestamp() );
    return incomeServiceEJB.getLastWeek( incomeServiceEJB.getLastWeek( currentWeek ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getIdDistributorParameter()
  {
    String idDistributor = this.configurationDAO.findByParameterName(
      Configuration.ID_DISTRIBUTOR_ALTERNATIVE_CONTENT.getParameter() ).getDsValue();
    return idDistributor;
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.movie.ServiceAdminMovieEJB#Boolean isMovieInBooking( java.lang.Long )
   */
  @Override
  public Boolean isMovieInBooking( Long idMovie )
  {
    int countBookings = this.bookingDAO.countBookedByIdEventMovie( idMovie );
    return countBookings > 0;
  }

}
