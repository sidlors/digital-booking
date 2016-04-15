package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.ProcessDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;
/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.ProcessDO}
 * 
 * @author jcarbajal
 */

public interface ProcessDAO extends GenericDAO<ProcessDO>
{

  /**
   * method for consult process
   * @return
   */
  List<CatalogTO> findAllProcces();
}
