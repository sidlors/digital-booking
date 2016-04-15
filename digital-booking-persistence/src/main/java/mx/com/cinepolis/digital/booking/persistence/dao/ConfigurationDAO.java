package mx.com.cinepolis.digital.booking.persistence.dao;

import mx.com.cinepolis.digital.booking.model.ConfigurationDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.ConfigurationDO}
 * 
 * @author agustin.ramirez
 * @since 0.2.0
 */
public interface ConfigurationDAO extends GenericDAO<ConfigurationDO>
{

  /**
   * Finds a record by Parameter Name
   * 
   * @param parameterName
   * @return {@link ConfigurationDO}
   */
  ConfigurationDO findByParameterName( String parameterName );

}
