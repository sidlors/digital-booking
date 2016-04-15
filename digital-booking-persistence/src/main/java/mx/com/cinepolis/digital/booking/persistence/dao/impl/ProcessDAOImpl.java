package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.ProcessDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.model.ProcessDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ProcessDAO;
/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.ProcessDAO}
 * 
 * @author jcarbajal
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class ProcessDAOImpl extends AbstractBaseDAO<ProcessDO> implements ProcessDAO
{

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;
  public ProcessDAOImpl()
  {
    super(ProcessDO.class);
  }
  
  public ProcessDAOImpl( Class<ProcessDO> entityClass )
  {
    super( entityClass );
  }

  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

  @Override
  public List<CatalogTO> findAllProcces()
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    List<ProcessDO> processList=this.findAll();
    List<CatalogTO> procesListTO=new ArrayList<CatalogTO>();
    if(processList!=null)
    {
      for(ProcessDO process:processList)
      {
        CatalogTO proccesTO= (CatalogTO) new ProcessDOToCatalogTOTransformer( Language.ENGLISH ).transform( process );
        procesListTO.add( proccesTO );
      }
    }
    
    return procesListTO;
  }

}
