package mx.com.cinepolis.digital.booking.integration.distributor.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.integration.distributor.ServiceAdminDistributorIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.distributor.ServiceAdminDistributorEJB;

/**
 * Clase que implementa los servicios de integracion de un distribuidor
 * 
 * @author agustin.ramirez
 */

@Stateless
@Local(value = ServiceAdminDistributorIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceAdminDistributorIntegratorEJBImpl implements ServiceAdminDistributorIntegratorEJB
{

  @EJB
  private ServiceAdminDistributorEJB serviceAdminDistributorEJB;

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.distributor.ServiceAdminDistributorIntegratorEJB#saveDistributor(mx
   * .com.cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void saveDistributor( DistributorTO distributor )
  {
    serviceAdminDistributorEJB.saveDistributor( distributor );

  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.distributor.ServiceAdminDistributorIntegratorEJB#deleteDistributor
   * (mx.com.cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void deleteDistributor( DistributorTO distributor )
  {
    serviceAdminDistributorEJB.deleteDistributor( distributor );

  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.distributor.ServiceAdminDistributorIntegratorEJB#updateDistributor
   * (mx.com.cinepolis.digital.booking.model.to.CatalogTO)
   */
  @Override
  public void updateDistributor( DistributorTO distributor )
  {
    serviceAdminDistributorEJB.updateDistributor( distributor );

  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.distributor.ServiceAdminDistributorIntegratorEJB#getAll()
   */
  @Override
  public List<DistributorTO> getAll()
  {
    return serviceAdminDistributorEJB.getAll();
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.distributor.ServiceAdminDistributorIntegratorEJB#
   * getCatalogDistributorSummary(mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public PagingResponseTO<DistributorTO> getCatalogDistributorSummary( PagingRequestTO pagingRequestTO )
  {
    return serviceAdminDistributorEJB.getCatalogDistributorSummary( pagingRequestTO );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.distributor.ServiceAdminDistributorIntegratorEJB#getDistributor(java
   * .lang.Integer)
   */
  @Override
  public DistributorTO getDistributor( Integer idDistributor )
  {
    return serviceAdminDistributorEJB.getDistributor( idDistributor );
  }

}
