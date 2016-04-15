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
import mx.com.cinepolis.digital.booking.dao.util.OperationDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.model.OperationDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.OperationDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.OperationDO}
 * 
 * @author jcarbajal
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class OperationDAOImpl extends AbstractBaseDAO<OperationDO> implements OperationDAO
{
  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  /**
   * Constructor by default
   */
  public OperationDAOImpl()
  {
    super( OperationDO.class );
  }

  public OperationDAOImpl( Class<OperationDO> entityClass )
  {
    super( entityClass );
  }

  /**
   * get the entity manager
   */
  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

  @Override
  public List<CatalogTO> findAllperations()
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    List<OperationDO> operationList = this.findAll();
    List<CatalogTO> operationTOList = new ArrayList<CatalogTO>();
    if( operationList != null )
    {
      for( OperationDO operation : operationList )
      {
        CatalogTO operationTO = (CatalogTO) new OperationDOToCatalogTOTransformer( Language.ENGLISH )
            .transform( operation );
        operationTOList.add( operationTO );
      }
    }
    return operationTOList;
  }
}
