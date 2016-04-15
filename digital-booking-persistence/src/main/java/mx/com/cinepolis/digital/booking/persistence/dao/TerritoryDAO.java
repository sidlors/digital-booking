package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.model.TerritoryDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.TerritoryDO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public interface TerritoryDAO extends GenericDAO<TerritoryDO>
{
  /**
   * /** Finds all records that satisfies the query
   * 
   * @param pagingRequestTO A paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<CatalogTO>} with the results
   */
  PagingResponseTO<CatalogTO> findAllByPaging( PagingRequestTO pagingRequestTO );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} into a record
   * 
   * @param catalogTO The catalog
   * @param categoryType The category
   */
  void save( CatalogTO catalogTO );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} into a record
   * 
   * @param catalogTO The catalog
   * @param categoryType The category
   * @param language The language of the catalog
   */
  void save( CatalogTO catalogTO, Language language );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} associated with record
   * 
   * @param catalogTO The catalog
   */
  void update( CatalogTO catalogTO );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} associated with record
   * 
   * @param catalogTO The catalog
   * @param language The language of the catalog
   */
  void update( CatalogTO catalogTO, Language language );

  /**
   * Removes a record associated with the catalog
   * 
   * @param catalogTO The catalog
   */
  void delete( CatalogTO catalogTO );

  /**
   * Obtains the territories
   * 
   * @return
   */
  List<CatalogTO> getAll();

  /**
   * Obtains the territories
   * 
   * @param language
   * @return
   */
  List<CatalogTO> getAll( Language language );
}
