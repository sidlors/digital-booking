package mx.com.cinepolis.digital.booking.service.distributor.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventMovieDAO;
import mx.com.cinepolis.digital.booking.service.distributor.ServiceAdminDistributorEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.ValidatorUtil;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author agustin.ramirez
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceAdminDistributorEJBImpl implements ServiceAdminDistributorEJB
{
  private static final String ID_VISTA_UNDEFINED = "0";
 
  @EJB
  private DistributorDAO distributorDAO;
  @EJB
  private EventMovieDAO eventMovieDAO;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.distributor. ServiceAdminDistributorEJB
   * #saveDistributor(mx.com.cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void saveDistributor( DistributorTO distributor )
  {
    ValidatorUtil.validateCatalog( distributor );
    if( !CollectionUtils.isEmpty( distributorDAO.findByDsShortNameActive( distributor.getShortName() ) ) )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.CATALOG_ALREADY_REGISTERED_WITH_SHORT_NAME );
    }
    if( CollectionUtils.isEmpty( distributorDAO.findByDsNameActive( distributor.getName() ) ) )
    {
      if( distributor.getIdVista().equals( ID_VISTA_UNDEFINED )
          || CollectionUtils.isEmpty( distributorDAO.findByIdVistaAndActive( distributor.getIdVista() ) ) )
      {
        distributorDAO.save( distributor );
      }
      else
      {
        throw DigitalBookingExceptionBuilder
            .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED_WITH_ID_VISTA );
      }
    }
    else
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED );
    }
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.distributor.
   * ServiceAdminDistributorEJB#deleteDistributor(java.lang.Long)
   */
  @Override
  public void deleteDistributor( DistributorTO distributor )
  {
    ValidatorUtil.validateCatalog( distributor );
    validateExistingMovies( distributor );
    distributorDAO.delete( distributor );
  }

  /**
   * Método que valida que el distribuidor no tenga películas asociadas
   * para poder eliminarlo.
   * 
   * @param distributorTO
   */
  private void validateExistingMovies( DistributorTO distributorTO )
  {
    DistributorDO distributorDO = new DistributorDO();
    distributorDO.setIdDistributor( distributorTO.getId().intValue() );
    if( CollectionUtils.isNotEmpty( eventMovieDAO.findByIdDistributor( distributorDO ) ) )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.DISTRIBUTOR_IS_ASSOCIATED_WITH_AN_EXISTING_MOVIE );
    }
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.distributor. ServiceAdminDistributorEJB
   * #updateDistributor(mx.com.cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void updateDistributor( DistributorTO distributor )
  {
    ValidatorUtil.validateCatalog( distributor );
    List<DistributorDO> distributorDOs = distributorDAO.findByDsNameActive( distributor.getName() );
    for( DistributorDO distributorDO : distributorDOs )
    {
      if( !distributorDO.getIdDistributor().equals( distributor.getId().intValue() ) )
      {
        throw DigitalBookingExceptionBuilder
            .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED );
      }
    }
    distributorDOs = distributorDAO.findByDsShortNameActive( distributor.getShortName() );
    for( DistributorDO distributorDO : distributorDOs )
    {
      if( !distributorDO.getIdDistributor().equals( distributor.getId().intValue() ) )
      {
        throw DigitalBookingExceptionBuilder
            .build( DigitalBookingExceptionCode.CATALOG_ALREADY_REGISTERED_WITH_SHORT_NAME );
      }
    }
    if( !distributor.getIdVista().equals( ID_VISTA_UNDEFINED ) )
    {
      distributorDOs = distributorDAO.findByIdVistaAndActive( distributor.getIdVista() );
      for( DistributorDO distributorDO : distributorDOs )
      {
        if( !distributorDO.getIdDistributor().equals( distributor.getId().intValue() ) )
        {
          throw DigitalBookingExceptionBuilder
              .build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED_WITH_ID_VISTA );
        }
      }
    }
    distributorDAO.update( distributor );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.distributor. ServiceAdminDistributorEJB#getAll()
   */
  @Override
  public List<DistributorTO> getAll()
  {
    return distributorDAO.getAll();
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.distributor. ServiceAdminDistributorEJB
   * #getCatalogDistributorSummary(mx.com.cinepolis.digital .booking.model.to.PagingRequestTO)
   */
  @Override
  public PagingResponseTO<DistributorTO> getCatalogDistributorSummary( PagingRequestTO pagingRequestTO )
  {
    ValidatorUtil.validatePagingRequest( pagingRequestTO );
    PagingResponseTO<DistributorTO> distributors = null;
    distributors = distributorDAO.findAllByPaging( pagingRequestTO );
    return distributors;
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.service.distributor.ServiceAdminDistributorEJB#getDistributor(java.lang.Integer)
   */
  @Override
  public DistributorTO getDistributor( Integer idDistributor )
  {
    return distributorDAO.get( idDistributor );
  }

}
