package mx.com.cinepolis.digital.booking.integration.systemlog;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemLogTO;

/**
 * Interface that defines the integration services relating to system log
 * 
 * @author jreyesv
 */
public interface ServiceSystemLogIntegratorEJB
{

  /**
   * Method that consults the system log by paging
   * 
   * @param pagingRequestTO, with the paging request information.
   * @return {@link mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO} with the paging response information.
   * @author jreyesv
   */
  PagingResponseTO<SystemLogTO> getSystemLogSummary( PagingRequestTO pagingRequestTO );

  /**
   * Method that gets all active operations.
   * 
   * @return List of {@link CatalogTO} with the active operations information.
   * @author jreyesv
   */
  List<CatalogTO> getAllOperations();

  /**
   * Method that gets all active process.
   * 
   * @return List of {@link CatalogTO} with the active process information.
   * @author jreyesv
   */
  List<CatalogTO> getAllProcess();

}
