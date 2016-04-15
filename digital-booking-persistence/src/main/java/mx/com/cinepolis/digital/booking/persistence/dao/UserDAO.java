package mx.com.cinepolis.digital.booking.persistence.dao;

import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.UserDO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
public interface UserDAO extends GenericDAO<UserDO>
{

  /**
   * Finds all records that satisfies the query
   * 
   * @param pagingRequestTO a paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<UserTO>} with the results
   */
  PagingResponseTO<UserTO> findAllByPaging( PagingRequestTO pagingRequestTO );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.UserTO} into a record
   * 
   * @param userTO
   */
  void save( UserTO userTO );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.UserTO} associated with record
   * 
   * @param userTO
   */
  void update( UserTO userTO );

  /**
   * Removes a record associated with the catalog
   * 
   * @param userTO
   */
  void delete( UserTO userTO );

  /**
   * Method that finds a user with the given username .
   * 
   * @param username {@link String} with the username.
   * @return {@link UserTO} with the user information or <code>null</code> if the user was not found.
   * @author afuentes
   */
  UserTO getByUsername( String username );

}
