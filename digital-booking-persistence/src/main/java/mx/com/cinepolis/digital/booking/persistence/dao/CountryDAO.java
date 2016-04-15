package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.CountryDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * Interface for the Data Access Object related to Country.
 * 
 * @author afuentes
 */
public interface CountryDAO extends GenericDAO<CountryDO>
{

  /**
   * Edits a register
   * 
   * @param countryDO
   */
  void edit( CountryDO countryDO );

  /**
   * Removes a register
   * 
   * @param countryDO
   */
  void remove( CountryDO countryDO );

  /**
   * Finds a register by its id
   * 
   * @param id
   * @return An instance of
   *         {@link mx.com.cinepolis.digital.booking.model.CountryDO}
   */
  CountryDO find( Object id );

  /**
   * Find all registers
   * 
   * @return A list of {@link mx.com.cinepolis.digital.booking.model.CountryDO}
   */
  List<CountryDO> findAll();

  /**
   * Finds a ranged registers
   * 
   * @param range A range represented by a initial record(int[0]) and a final
   *          record (int[1])
   * @return A list of {@link mx.com.cinepolis.digital.booking.model.CountryDO}
   */
  List<CountryDO> findRange( int[] range );

  /**
   * Counts the records
   * 
   * @return
   */
  int count();

  /**
   * Forces the CRUD
   */
  void flush();

  /**
   * Find all registers active
   * 
   * @return
   */
  List<CatalogTO> getAll();

  /**
   * Find all registers active
   * 
   * @param language the language
   * @return
   */
  List<CatalogTO> getAll( Language language );

  /**
   * Find all registers active by its Vista Id
   * 
   * @param idVista
   * @return
   */
  List<CountryDO> findByIdVistaAndActive( String idVista );

  /**
   * Find all registers by its Vista Id
   * 
   * @param idVista
   * @return
   */
  List<CountryDO> findByIdVista( String idVista );

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
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} into a record
   * 
   * @param catalogTO The catalog
   */
  void save( CatalogTO catalogTO );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} into a record
   * 
   * @param catalogTO The catalog
   * @param language The language of the catalog
   */
  void save( CatalogTO catalogTO, Language language );

}
