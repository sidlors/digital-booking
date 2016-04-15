package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.CityTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.model.CityDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.CityDO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public interface CityDAO extends GenericDAO<CityDO>
{

  /**
   * Find all active cities
   * 
   * @return a list from {@link java.util.List<CatalogTO>}
   */
  List<CatalogTO> findAllCities();

  /**
   * Find all active cities
   * 
   * @param language
   * @return a list from {@link java.util.List<CatalogTO>}
   */
  List<CatalogTO> findAllCities( Language language );

  /**
   * Find all active cities by its Vista Id
   * 
   * @param idVista
   * @return a list of {@link mx.com.cinepolis.digital.booking.model.CityDO}
   */
  List<CityDO> findByIdVistaAndActive( String idVista );
  
  
  /**
   * Find all registers by its Vista Id
   * @param idVista
   * @return
   */
  List<CityDO> findByIdVista( String idVista );
  
  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.CityTO} object associated with record.
   * 
   * @param cityTO, with the city information to update.
   */
  void update( CityTO cityTO );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.CityTO} object associated with record.
   * 
   * @param cityTO, with the city information to update.
   * @param language, with the language of the catalog.
   */
  void update( CityTO cityTO, Language language );
  
  
  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.CityTO} object into a record
   * 
   * @param city, with the city information to save.
   */
  void save( CityTO city );
  
  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.CityTO} object into a record
   * 
   * @param city, with the city information to save.
   * @param language, with the language of the catalog.
   */
  void save( CityTO city, Language language );

  /**
   * Find all active cities by its id country
   * 
   * @param idVista
   * @return a list of {@link mx.com.cinepolis.digital.booking.model.CityDO}
   */
  List<CityDO> findByIdCountry( Long idCountry );

  /**
   * Find all active cities by its id state
   * 
   * @param idState
   * @return a list of {@link mx.com.cinepolis.digital.booking.model.CityDO}
   */
  List<CityDO> findByIdState( Long idState );

  /**
   * Finds all records that satisfies the query
   * 
   * @param pagingRequestTO a paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<CityTO>} with the results
   */
  PagingResponseTO<CityTO> findAllByPaging( PagingRequestTO pagingRequestTO );

}
