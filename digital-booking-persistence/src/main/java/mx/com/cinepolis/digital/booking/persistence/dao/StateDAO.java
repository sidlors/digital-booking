package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.model.StateDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.StateDO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public interface StateDAO extends GenericDAO<StateDO>
{

  /**
   * Find all active states
   * 
   * @return a list from {@link java.util.List<StateTO<CatalogTO, Number>>}
   */
  List<StateTO<CatalogTO, Number>> findAllStates();

  /**
   * Find all active states
   * 
   * @param language
   * @return a list from {@link java.util.List<StateTO<CatalogTO, Number>>}
   */
  List<StateTO<CatalogTO, Number>> findAllStates( Language language );

  /**
   * Find all active states by its Vista Id
   * 
   * @param idVista
   * @return
   */
  List<StateDO> findByIdVistaAndActive( String idVista );

  /**
   * Find all registers by its Vista Id
   * 
   * @param idVista
   * @return
   */
  List<StateDO> findByIdVista( String idVista );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} associated with record
   * 
   * @param stateTO The catalog
   */
  void update( StateTO<CatalogTO, Number> stateTO );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} associated with record
   * 
   * @param stateTO The catalog
   * @param language The language of the catalog
   */
  void update( StateTO<CatalogTO, Number> stateTO, Language language );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} into a record
   * 
   * @param stateTO The catalog
   */
  void save( StateTO<CatalogTO, Number> stateTO );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO} into a record
   * 
   * @param stateTO The catalog
   * @param language The language of the catalog
   */
  void save( StateTO<CatalogTO, Number> stateTO, Language language );

}
