package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemMenuTO;
import mx.com.cinepolis.digital.booking.model.SystemMenuDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * Interface for the Data Access Object related to a system menu element.
 * 
 * @author afuentes
 */
public interface SystemMenuDAO extends GenericDAO<SystemMenuDO>
{

  /**
   * Method that finds the user menu based on its roles.
   * 
   * @param idUser User identifier.
   * @return {@link SystemMenuTO} with the user menu information in a hierarchical structure.
   * @author afuentes
   */
  SystemMenuTO findMenuByUser( Long idUser );

  /**
   * Method that determines if a user based on its roles is allowed to access a given URL.
   * 
   * @param url {@link String} with the URL.
   * @param userRoles List of {@link CatalogTO} with the user roles.
   * @return <code>true</code> if the user is allowed to access a given URL, <code>false</code> otherwise.
   * @author afuentes
   */
  boolean isSystemMenuAllowed( String url, List<CatalogTO> userRoles );

  /**
   * Method that clears the JPA cache.
   * 
   * @author afuentes
   */
  void clearJPACache();

}
