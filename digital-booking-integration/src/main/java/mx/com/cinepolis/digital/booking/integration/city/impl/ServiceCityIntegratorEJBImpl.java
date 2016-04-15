package mx.com.cinepolis.digital.booking.integration.city.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.CityTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.integration.city.ServiceCityIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.city.ServiceAdminCityEJB;

/**
 * Class that implements the integration services relating to cities administration.
 * 
 * @author jreyesv
 */
@Stateless
@Local(value = ServiceCityIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceCityIntegratorEJBImpl implements ServiceCityIntegratorEJB
{

  /**
   * Service variable.
   */
  @EJB
  private ServiceAdminCityEJB serviceAdminCityEJB;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.city.ServiceCityIntegratorEJB
   * #saveCity(mx.com.cinepolis.digital.booking.commons.to.CityTO)
   */
  @Override
  public void saveCity( CityTO cityTO )
  {
    this.serviceAdminCityEJB.saveCity( cityTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.city.ServiceCityIntegratorEJB
   * #deleteCity(mx.com.cinepolis.digital.booking.commons.to.CityTO)
   */
  @Override
  public void deleteCity( CityTO cityTO )
  {
    this.serviceAdminCityEJB.deleteCity( cityTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.city.ServiceCityIntegratorEJB
   * #updateCity(mx.com.cinepolis.digital.booking.commons.to.CityTO)
   */
  @Override
  public void updateCity( CityTO cityTO )
  {
    this.serviceAdminCityEJB.updateCity( cityTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.city.ServiceCityIntegratorEJB
   * #findAllCitiesByPaging(mx.com.cinepolis.digital.booking.model.to. PagingRequestTO)
   */
  @Override
  public PagingResponseTO<CityTO> findAllCitiesByPaging( PagingRequestTO pagingRequestTO )
  {
    return this.serviceAdminCityEJB.findAllCitiesByPaging( pagingRequestTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.city.ServiceCityIntegratorEJB #findAllActiveCountries()
   */
  @Override
  public List<CatalogTO> findAllActiveCountries()
  {
    return this.serviceAdminCityEJB.findAllActiveCountries();
  }

}
