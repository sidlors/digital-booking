package mx.com.cinepolis.digital.booking.service.systemlog.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemLogTO;
import mx.com.cinepolis.digital.booking.persistence.dao.OperationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ProcessDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.SystemLogDAO;
import mx.com.cinepolis.digital.booking.service.systemlog.ServiceSystemLogEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;

/**
 * Class for implement the system log methods  
 * 
 * @author jcarbajal
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceSystemLogEJBImpl implements ServiceSystemLogEJB
{
  @EJB
  private SystemLogDAO systemLogDAO;
  
  @EJB
  private ProcessDAO processDAO;
  
  @EJB
  private OperationDAO operationDAO;
  
  @Override
  public PagingResponseTO<SystemLogTO> getSystemLogByPaging( PagingRequestTO pagingRequestTO )
  {
    return this.systemLogDAO.findAllSystemLogByPaging( pagingRequestTO );
  }

  @Override
  public List<CatalogTO> findOperations()
  {
  return this.operationDAO.findAllperations();
  }

  @Override
  public List<CatalogTO> findProcess()
  {
    return this.processDAO.findAllProcces();
  }
  
  
}
