package mx.com.cinepolis.digital.booking.integration.theater.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.integration.theater.ServiceAdminTheaterIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.theater.ServiceAdminTheaterEJB;

/**
 * Clase que implementa los servicios de integracion de la administracion de los cines
 * 
 * @author rgarcia
 */

@Stateless
@Local(value = ServiceAdminTheaterIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceAdminTheaterIntegratorEJBImpl implements ServiceAdminTheaterIntegratorEJB
{

  @EJB
  private ServiceAdminTheaterEJB serviceAdminTheaterEJB;

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.theater.ServiceAdminTheaterIntegratorEJB#saveTheater(mx.com.cinepolis
   * .digital.booking.model.to.TheaterTO)
   */
  @Override
  public void saveTheater( TheaterTO theaterTO )
  {
    serviceAdminTheaterEJB.saveTheater( theaterTO );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.theater.ServiceAdminTheaterIntegratorEJB#deleteTheater(mx.com.cinepolis
   * .digital.booking.model.to.TheaterTO)
   */
  @Override
  public void deleteTheater( TheaterTO theaterTO )
  {
    serviceAdminTheaterEJB.deleteTheater( theaterTO );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.theater.ServiceAdminTheaterIntegratorEJB#updateTheater(mx.com.cinepolis
   * .digital.booking.model.to.TheaterTO)
   */
  @Override
  public void updateTheater( TheaterTO theaterTO )
  {
    serviceAdminTheaterEJB.updateTheater( theaterTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.theater.ServiceAdminTheaterIntegratorEJB#getAllTheaters()
   */
  @Override
  public List<TheaterTO> getAllTheaters()
  {
    return serviceAdminTheaterEJB.getAllTheaters();
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.theater.ServiceAdminTheaterIntegratorEJB#getCatalogTheaterSummary(
   * mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public PagingResponseTO<TheaterTO> getCatalogTheaterSummary( PagingRequestTO pagingRequestTO )
  {
    return serviceAdminTheaterEJB.getCatalogTheaterSummary( pagingRequestTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getMovieFormats()
  {
    return this.serviceAdminTheaterEJB.getMovieFormats();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getSoundFormats()
  {
    return this.serviceAdminTheaterEJB.getSoundFormats();
  }

  @Override
  public List<CatalogTO> getScreenFormats()
  {
    return this.serviceAdminTheaterEJB.getScreenFormats();
  }

  @Override
  public List<TheaterTO> getTheatersByRegionId( CatalogTO region )
  {
    return this.serviceAdminTheaterEJB.getTheatersByRegionId( region );
  }

  @Override
  public TheaterTO getTheater( Long theaterIdSelected )
  {
    return this.serviceAdminTheaterEJB.getTheater( theaterIdSelected );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.booking.
   * BookingServiceIntegratorEJB#getIndicatorTypeById(int, Language)
   */
  @Override
  public IncomeSettingsTypeTO getIndicatorTypeById( int idIndicatorType, Language language )
  {
    return this.serviceAdminTheaterEJB.getIndicatorTypeById( idIndicatorType, language );
  }

}
