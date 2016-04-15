package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.RoleDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.RoleDO}
 * 
 * @author agustin.ramirez
 */
public interface RoleDAO extends GenericDAO<RoleDO>
{

  /**
   * Method that finds all active roles.
   * 
   * @return List of {@link CatalogTO} with the active role information.
   */
  List<CatalogTO> getAllRoleActive();

}
