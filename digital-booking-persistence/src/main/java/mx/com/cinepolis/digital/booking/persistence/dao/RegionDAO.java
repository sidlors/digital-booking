/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.RegionDAO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public interface RegionDAO extends GenericDAO<RegionDO>
{

  /**
   * Finds all records that satisfies the query
   * 
   * @param pagingRequestTO A paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<RegionTO<CatalogTO, Integer>>} with the
   *         results
   */
  PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> findAllByPaging( PagingRequestTO pagingRequestTO );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.model.to.RegionTO<CatalogTO, Integer>} into a record
   * 
   * @param region The region
   */
  void save( RegionTO<CatalogTO, CatalogTO> region );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.model.to.RegionTO<CatalogTO, Integer>} into a record
   * 
   * @param region The region
   * @param language The language of the region
   */
  void save( RegionTO<CatalogTO, CatalogTO> region, Language language );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.model.to.RegionTO<CatalogTO, Integer>} associated with record
   * 
   * @param region The region
   */
  void update( RegionTO<CatalogTO, CatalogTO> region );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} associated with record
   * 
   * @param region The region
   * @param language The language of the region
   */
  void update( RegionTO<CatalogTO, CatalogTO> region, Language language );

  /**
   * Removes a record associated with the region
   * 
   * @param catalogTO The region
   */
  void delete( RegionTO<CatalogTO, CatalogTO> region );

  /**
   * Find record with the region name received
   * 
   * @param dsName Region name
   * @return List RegionTO
   */
  List<RegionDO> findByDsNameActive( String dsName );

  /**
   * Find record with the region name and language id received
   * 
   * @param dsName Region name
   * @param language language id
   * @return List RegionTO
   */
  List<RegionDO> findByDsNameActive( String dsName, Language language );

  /**
   * Finds a {@link mx.com.cinepolis.digital.booking.model.to.RegionTO<CatalogTO, CatalogTO>} given its id
   * 
   * @param id
   * @return
   */
  RegionTO<CatalogTO, CatalogTO> getRegionById( Integer id );

  /**
   * Method that get all active regions.
   * 
   * @return List of {@link RegionDO} with active regions.
   */
  List<RegionDO> findActiveRegions();

}
