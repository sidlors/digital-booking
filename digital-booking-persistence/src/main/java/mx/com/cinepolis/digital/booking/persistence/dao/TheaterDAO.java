package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.TheaterDO}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
public interface TheaterDAO extends GenericDAO<TheaterDO>
{

  /**
   * Finds all records that satisfies the query
   * 
   * @param pagingRequestTO A paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<TheaterTO>} with the results
   */
  PagingResponseTO<TheaterTO> findAllByPaging( PagingRequestTO pagingRequestTO );

  /**
   * Saves a {@link mx.com.cinepolis.digital.booking.commons.to.TheaterTO} into a record
   * 
   * @param theater
   */
  void save( TheaterTO theaterTO );

  /**
   * Save a Theater with the languaje specified
   * 
   * @param theaterTO
   * @param language
   */
  void save( TheaterTO theaterTO, Language language );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.TheaterTO} associated with record
   * 
   * @param theater The theater
   */
  void update( TheaterTO theaterTO );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.TheaterTO} associated with record
   * 
   * @param theaterTO The theater
   */
  void update( TheaterTO theaterTO, Language language );

  /**
   * Removes a record associated with the theater
   * 
   * @param catalogTO The theater
   */
  void delete( TheaterTO theaterTO );

  /**
   * Find all active theaters by its Vista Id
   * 
   * @param idVista
   * @return
   */
  List<TheaterDO> findByIdVistaAndActive( String idVista );

  /**
   * Finds all active theaters by its name
   * 
   * @param dsName
   * @return
   */
  List<TheaterDO> findByTheaterName( String dsName );

  /**
   * Finds all active theaters by its name and language of internationalization
   * 
   * @param dsName
   * @param language
   * @return
   */
  List<TheaterDO> findByTheaterName( String dsName, Language language );

  /**
   * Simplified method that obtains a list of {@link mx.com.cinepolis.digital.booking.commons.to.TheaterTO}given its
   * region id
   * 
   * @param id
   * @return
   */
  List<TheaterTO> findByRegionId( CatalogTO id );

  /**
   * Simplified method that obtains a list of {@link mx.com.cinepolis.digital.booking.commons.to.TheaterTO}given its
   * region id and language
   * 
   * @param id
   * @param language
   * @return
   */
  List<TheaterTO> findByRegionId( CatalogTO id, Language language );

  /**
   * Finds all active theaters by nuTheater
   * 
   * @param nuTheater
   * @return A list of {@link mx.com.cinepolis.digital.booking.model.TheaterDO}
   */
  List<TheaterDO> findByNuTheater( int nuTheater );

  /**
   * Find all theaters by its Vista Id
   * 
   * @param idVista
   * @return
   */
  List<TheaterDO> findByIdVista( String idVista );

  /**
   * Find all theaters by region
   * 
   * @param idRegion
   * @return
   */
  List<TheaterDO> getNumberOfTheatersByRegion( Long idRegion );
  
  /**
   * Find all  theaters by region
   * 
   * @param idRegion
   * @return
   */
  List<TheaterDO> fidTheatersByRegion(Long idRegion);
}
