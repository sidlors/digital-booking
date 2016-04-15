package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.CategoryDO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public interface CategoryDAO extends GenericDAO<CategoryDO>
{

  /**
   * Finds all records that satisfies the query
   * 
   * @param pagingRequestTO a paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<CatalogTO>} with the results
   */
  PagingResponseTO<CatalogTO> findAllByPaging( PagingRequestTO pagingRequestTO );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} into a record
   * 
   * @param catalogTO The catalog
   * @param categoryType The category
   */
  void save( CatalogTO catalogTO, CategoryType categoryType );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} into a record
   * 
   * @param catalogTO The catalog
   * @param categoryType The category
   * @param language The language of the catalog
   */
  void save( CatalogTO catalogTO, CategoryType categoryType, Language language );

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
   * Find record with the name and Category type id received
   * 
   * @param dsName Category name
   * @param idCategoryType Category type id
   * @return List CatalogTO
   */
  List<CategoryDO> findByDsNameActive( String dsName, Integer idCategoryType );

  /**
   * Find record with the name, language id and Category type id received
   * 
   * @param dsName Category name
   * @param idCategoryType Category type id
   * @param language language id
   * @return List CatalogTO
   */
  List<CategoryDO> findByDsNameActive( String dsName, Integer idCategoryType, Language language );

  /**
   * Gets all categories by its category type
   * 
   * @param categoryType
   * @return
   */
  List<CatalogTO> getAllByCategoryType( CategoryType categoryType );

  /**
   * Gets all categories by its category type
   * 
   * @param categoryType
   * @param language
   * @return
   */
  List<CatalogTO> getAllByCategoryType( CategoryType categoryType, Language language );

  /**
   * Get Catalogo by category id.
   * 
   * @param idCategory
   * @param language
   * @return
   */
  List<CatalogTO> findByIdCategory( Integer idCategory, Language language );

}
