package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.OperationDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.OperationDO}
 * 
 * @author jcarbajal
 */
public interface OperationDAO extends GenericDAO<OperationDO>
{
  /**
   * method for consult all operations
   * @return list of catalog 
   */
  List<CatalogTO> findAllperations();
}
