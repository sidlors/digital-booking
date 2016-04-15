package mx.com.cinepolis.digital.booking.service.city.impl;

import java.util.HashMap;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.CityQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.CityTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.model.CityDO;
import mx.com.cinepolis.digital.booking.persistence.dao.CityDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CountryDAO;
import mx.com.cinepolis.digital.booking.service.city.ServiceAdminCityEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.ValidatorUtil;

import org.apache.commons.collections.CollectionUtils;

/**
 * Public class that implements the associated methods to cities administration.
 * 
 * @author jreyesv
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceAdminCityEJBImpl implements ServiceAdminCityEJB
{
  
  /**
   * The CityDAO variable.
   */
  @EJB
  private CityDAO cityDAO;

  /**
   * The CountryDAO variable.
   */
  @EJB
  private CountryDAO countryDAO;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.category.ServiceAdminCityEJB
   * #saveCity(mx.com.cinepolis.digital.booking.commons.to.CityTO)
   */
  @Override
  public void saveCity( CityTO cityTO )
  {
    ValidatorUtil.validateCityTO( cityTO );
    if( cityTO.getId() == null )
    {
      if( CollectionUtils.isNotEmpty( cityDAO.findByIdVistaAndActive( cityTO.getIdVista() ) ) )
      {
        throw DigitalBookingExceptionBuilder
            .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED_WITH_LIQUIDATION_ID );
      }
      cityTO.setFgActive( Boolean.TRUE );
      this.cityDAO.save( cityTO, Language.ALL );
    }
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.category.ServiceAdminCityEJB
   * #deleteCity(mx.com.cinepolis.digital.booking.commons.to.CityTO)
   */
  @Override
  public void deleteCity( CityTO cityTO )
  {
    ValidatorUtil.validateCityTO( cityTO );
    if( cityTO.getId() != null )
    {
      cityTO.setFgActive( Boolean.FALSE );
      this.cityDAO.update( cityTO );
    }
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.category.ServiceAdminCityEJB
   * #updateCity(mx.com.cinepolis.digital.booking.commons.to.CityTO)
   */
  @Override
  public void updateCity( CityTO cityTO )
  {
    ValidatorUtil.validateCityTO( cityTO );
    if( cityTO.getId() != null )
    {
      List<CityDO> listCityDOs = cityDAO.findByIdVistaAndActive( cityTO.getIdVista() );
      if( CollectionUtils.isNotEmpty( listCityDOs ) )
      {
        if( !listCityDOs.get( 0 ).getIdCity().equals( cityTO.getId().intValue() ) )
        {
          throw DigitalBookingExceptionBuilder
              .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED_WITH_LIQUIDATION_ID );
        }
      }
      this.cityDAO.update( cityTO, Language.ALL );
    }
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.category.ServiceAdminCityEJB #
   * findAllCitiesByPaging(mx.com.cinepolis.digital.booking.model.to. PagingRequestTO)
   */
  @Override
  public PagingResponseTO<CityTO> findAllCitiesByPaging( PagingRequestTO pagingRequestTO )
  {
    ValidatorUtil.validatePagingRequest( pagingRequestTO );
    pagingRequestTO.setNeedsPaging( Boolean.TRUE );
    if( pagingRequestTO.getFilters() == null )
    {
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    }
    pagingRequestTO.getFilters().put( CityQuery.CITY_ACTIVE, Boolean.TRUE );
    return cityDAO.findAllByPaging( pagingRequestTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.category.ServiceAdminCityEJB # findAllActiveCountries()
   */
  @Override
  public List<CatalogTO> findAllActiveCountries()
  {
    return this.countryDAO.getAll();
  }

}
