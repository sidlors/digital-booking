package mx.com.cinepolis.digital.booking.integration.movie.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.movie.ServiceAdminMovieEJB;

/**
 * Class that implements the integration services relating to a movie.
 * 
 * @author afuentes
 */
@Stateless
@Local(value = ServiceAdminMovieIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceAdminMovieIntegratorEJBImpl implements ServiceAdminMovieIntegratorEJB
{

  @EJB
  private ServiceAdminMovieEJB serviceAdminMovieEJB;

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB#saveMovie(mx.com.cinepolis.digital
   * .booking.model.to.EventMovieTO)
   */
  @Override
  public void saveMovie( EventMovieTO eventMovieTO )
  {
    serviceAdminMovieEJB.saveMovie( eventMovieTO );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB#deleteMovie(mx.com.cinepolis.
   * digital.booking.model.to.EventMovieTO)
   */
  @Override
  public void deleteMovie( EventMovieTO eventMovieTO )
  {
    serviceAdminMovieEJB.deleteMovie( eventMovieTO );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB#updateMovie(mx.com.cinepolis.
   * digital.booking.model.to.EventMovieTO)
   */
  @Override
  public void updateMovie( EventMovieTO eventMovieTO )
  {
    serviceAdminMovieEJB.updateMovie( eventMovieTO );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB#getCatalogMovieSummary(mx.com
   * .cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public PagingResponseTO<EventMovieTO> getCatalogMovieSummary( PagingRequestTO pagingRequestTO )
  {
    return serviceAdminMovieEJB.getCatalogMovieSummary( pagingRequestTO );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB#saveMovieImage(mx.com.cinepolis
   * .digital.booking.model.to.FileTO)
   */
  @Override
  public FileTO saveMovieImage( FileTO fileTO )
  {
    return serviceAdminMovieEJB.saveMovieImage( fileTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB#getAllCountries()
   */
  @Override
  public List<CatalogTO> getAllCountries()
  {
    return serviceAdminMovieEJB.getAllContries();
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB#findMovieImage()
   */
  @Override
  public FileTO findMovieImage( Long idMovieImage )
  {
    return serviceAdminMovieEJB.findMovieImage( idMovieImage );
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getIdDistributorParameter()
  {
    return serviceAdminMovieEJB.getIdDistributorParameter();
  }
  

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.movie.ServiceAdminMovieIntegratorEJB#isMovieInBooking( java.lang.Long )
   */
  @Override
  public Boolean isMovieInBooking( Long idMovie )
  {
    return this.serviceAdminMovieEJB.isMovieInBooking( idMovie );
  }
}
