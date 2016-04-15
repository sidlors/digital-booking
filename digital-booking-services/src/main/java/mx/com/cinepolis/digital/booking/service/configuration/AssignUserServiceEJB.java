package mx.com.cinepolis.digital.booking.service.configuration;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;

/**
 * Interface that defines assign users to regions or theaters services.
 * 
 * @author kperez
 */
@Local
public interface AssignUserServiceEJB
{

  /**
   * Method that get all active users.
   * 
   * @return List of {@link UserTO} with all active users.
   */
  List<UserTO> getAllUsersActive();

  /**
   * 
   * Method that get all active theaters associated to region and user received.
   * 
   * @param regionId
   * @return List of {@link TheaterTO} with all active theaters associated a region.
   */
  List<CatalogTO> getTheatersAvailable( Long regionId, AbstractTO abstractTO );

  /**
   * Method that save the regions assignment to a user.
   * 
   * @param userTO {@link userTO} with regions to assign at user.
   */
  void assignRegionsUser( UserTO userTO );

  /**
   * Method that save the theaters assignment to a user.
   * 
   * @param userTO {@link userTO} with theaters to assign at user.
   */
  void assignTheatersUser( UserTO userTO );

  /**
   * Method that get regions available for this user.
   * @param userId
   * @return List of {@link CatalogTO} with regions available for this user.
   */
  List<CatalogTO> getRegionsAvailable( Long userId );
  
  /**
   * Method that get regions associated at user.
   * 
   * @param userId
   * @return List of {@link CatalogTO} with regions associated at user.
   */
  List<CatalogTO> getRegionsAssociated( Long userId );
  
  /**
   * Method that get theaters associated at user.
   * @param userId
   * @return List of {@link CatalogTO} with theaters associated at user.
   */
  List<CatalogTO> getTheatersAssociated( Long regionId, AbstractTO abstractTO );
  
}
