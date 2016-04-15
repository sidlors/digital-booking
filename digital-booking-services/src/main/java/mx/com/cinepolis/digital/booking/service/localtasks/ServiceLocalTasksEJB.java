package mx.com.cinepolis.digital.booking.service.localtasks;

import javax.ejb.Remote;

/**
 * Interface that defines the methods for local tasks.
 * 
 * @author jreyesv
 */
@Remote
public interface ServiceLocalTasksEJB
{

  /**
   * Deactivates all presales that expired yesterday.
   */
  void deactivatePresales();

}
