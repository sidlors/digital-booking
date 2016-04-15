package mx.com.cinepolis.digital.booking.integration.systemlog.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemLogTO;
import mx.com.cinepolis.digital.booking.integration.systemlog.ServiceSystemLogIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.systemlog.ServiceSystemLogEJB;

/**
 * Class that implements the integration services relating to system log.
 * 
 * @author jreyesv
 */
@Stateless
@Local(value = ServiceSystemLogIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceSystemLogIntegratorEJBImpl implements ServiceSystemLogIntegratorEJB
{

  /*@EJB
  private ServiceAdminMovieEJB serviceAdminMovieEJB;*/

  @EJB
  private ServiceSystemLogEJB serviceSystemLogEJB;
  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.integration.systemlog.ServiceSystemLogIntegratorEJB#getSystemLogSummary(mx.com
   * .cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public PagingResponseTO<SystemLogTO> getSystemLogSummary( PagingRequestTO pagingRequestTO )
  {
    return serviceSystemLogEJB.getSystemLogByPaging( pagingRequestTO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.systemlog.ServiceSystemLogIntegratorEJB#getAllOperations()
   */
  @Override
  public List<CatalogTO> getAllOperations()
  {
    return serviceSystemLogEJB.findOperations();
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.integration.systemlog.ServiceSystemLogIntegratorEJB#getAllProcess()
   */
  @Override
  public List<CatalogTO> getAllProcess()
  {
    return serviceSystemLogEJB.findProcess();
  }

}
