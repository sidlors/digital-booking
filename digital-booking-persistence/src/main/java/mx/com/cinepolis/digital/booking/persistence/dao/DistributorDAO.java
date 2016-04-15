package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.DistributorDO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public interface DistributorDAO extends GenericDAO<DistributorDO>
{

  /**
   * Finds all records that satisfies the query
   * 
   * @param pagingRequestTO a paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<CatalogTO>} with the results
   */
  PagingResponseTO<DistributorTO> findAllByPaging( PagingRequestTO pagingRequestTO );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} associated with record
   * 
   * @param catalogTO The catalog
   */
  void save( DistributorTO catalogTO );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} associated with record
   * 
   * @param catalogTO
   */
  void update( DistributorTO catalogTO );

  /**
   * Removes a record associated with the catalog
   * 
   * @param catalogTO The catalog
   */
  void delete( DistributorTO catalogTO );

  /**
   * Method that finds all the distributors with that name and active
   * 
   * @param dsName
   * @return
   */
  List<DistributorDO> findByDsNameActive( String dsName );

  /**
   * Method that finds all the distributors with that name
   * 
   * @param dsName
   * @return
   */
  List<DistributorDO> findByDsName( String dsName );

  /**
   * Method that finds all active distributors.
   * 
   * @return List of {@link CatalogTO} with the active distributors information.
   * @author afuentes
   */
  List<DistributorTO> getAll();

  /**
   * Finds all active distributors by its Vista Id
   * 
   * @param idVista
   * @return
   */
  List<DistributorDO> findByIdVistaAndActive( String idVista );

  /**
   * Method that finds a distributor with the given identifier.
   * 
   * @param idDistributor Distributor identifier.
   * @return {@link CatalogTO} with the distributor information or <code>null</code> if it cannot find the distributor.
   * @author afuentes
   */
  DistributorTO get( Integer idDistributor );

  /**
   * Finds all distributors by its Vista Id
   * 
   * @param idVista
   * @return
   */
  List<DistributorDO> findByIdVista( String idVista );

  /**
   * Method that finds all the distributors with that short name
   * 
   * @param dsName
   * @return
   */
  List<DistributorDO> findByDsShortNameActive( String dsShortName );

}
