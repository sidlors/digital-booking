package mx.com.cinepolis.digital.booking.service.security;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemMenuTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;

/**
 * Interface for security services
 * 
 * @author gsegura
 * @since 0.3.0
 */
@Local
public interface ServiceSecurityEJB
{

  /**
   * Authenticates a user, throws an exception if is invalid
   * 
   * @param userTO
   */
  void authenticate( UserTO userTO );

  /**
   * Method that finds a user detailed information.
   * 
   * @param username {@link String} with the username.
   * @return {@link UserTO} with the user detailed information or <code>null</code> if the user was not found.
   * @author afuentes
   */
  UserTO findUserDetail( String username );

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

  /**
   * Method that determines whether the system should show the configuration option to the user or not.
   * 
   * @param idUser The user identifier.
   * @return <code>true</code> if the configuration option should be showed, <code>false</code> otherwise.
   * @author afuentes
   */
  boolean hasConfigPage( Long idUser );
  
  /**
   * Method for logout.
   * @param userTO
   */
  void logOut( UserTO userTO );

}
