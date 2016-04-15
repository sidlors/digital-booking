package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.ScreenDO}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
public interface ScreenDAO extends GenericDAO<ScreenDO>
{

  /**
   * Finds all records that satisfies the query
   * 
   * @param pagingRequestTO A paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<ScreenTO>} with the results
   */
  PagingResponseTO<ScreenTO> findAllByPaging( PagingRequestTO pagingRequestTO );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} into a record
   * 
   * @param screen
   */
  void save( ScreenTO screenTO );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} associated with record
   * 
   * @param region The region
   */
  void update( ScreenTO screenTO );

  /**
   * Removes a record associated with the screen
   * 
   * @param catalogTO The screen
   */
  void delete( ScreenTO screenTO );

  /**
   * Finds all screens from a Theater Id
   * 
   * @param idTheater
   * @return
   */
  List<ScreenDO> findAllActiveByIdCinema( Integer idTheater );

  /**
   * Find all active screens by its Vista Id
   * 
   * @param idVista
   * @return
   */
  List<ScreenDO> findByIdVistaAndActive( String idVista );
}
