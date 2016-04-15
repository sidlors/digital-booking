package mx.com.cinepolis.digital.booking.service.user;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
/**
 * Interface for userService
 * @author agustin.ramirez
 *
 */
@Local
public interface UserServiceEJB {
	
	
	
	/**
	 * Save a user
	 * @param userTO
	 */
	void saveUser(UserTO userTO);
		
	/**
	 * Remove a user
	 * @param userTO
	 */
	void deleteUser(UserTO userTO);
	
	/**
	 * Update the userData
	 * @param userTO
	 */
	void updateUser(UserTO userTO);
	
	/**
	 * Get all the users by parameters
	 * @param request
	 * @return
	 */
	PagingResponseTO<UserTO> findAllUsers(PagingRequestTO request);
	
	/**
	 * Get All roles actives
	 * @return
	 */
	List<CatalogTO> findAllRolesActive();

}
