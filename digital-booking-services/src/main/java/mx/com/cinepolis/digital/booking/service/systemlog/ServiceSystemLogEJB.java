package mx.com.cinepolis.digital.booking.service.systemlog;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemLogTO;

/**
 * interface for manipulate the service of System Log  
 * @author jcarbajal
 *
 */
@Local
public interface ServiceSystemLogEJB
{
/**
 * method for consult the System Log by pagingRequestTO
 * 
 * @param pagingRequestTO 
 * @return 
 */
  PagingResponseTO<SystemLogTO> getSystemLogByPaging( PagingRequestTO pagingRequestTO );
  
  /**
   * method for consult operations
   * @return list of catalog
   */
  List<CatalogTO> findOperations();
  /**
   * method for consult the process
   * @return list of catalog
   */
  List<CatalogTO> findProcess();
}
