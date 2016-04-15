package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.EventDO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public interface EventDAO extends GenericDAO<EventDO>
{

  /**
   * Finds all records that satisfies the query
   * 
   * @param pagingRequestTO a paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<EventTO>} with the results
   */
  PagingResponseTO<EventTO> findAllByPaging( PagingRequestTO pagingRequestTO );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.EventTO} associated with record
   * 
   * @param eventTO The event
   */
  void save( EventTO eventTO, Long idDistAltContent );

  /**
   * Updates a {@link mx.com.cinepolis.digital.booking.commons.to.EventTO} associated with record
   * 
   * @param event
   */
  void update( EventTO eventTO, Long idDistAltContent );

  /**
   * Removes a record associated with the event
   * 
   * @param eventTO The event
   */
  void delete( EventTO eventTO );

  /**
   * Saves or updates the image of a movie {@link mx.com.cinepolis.digital.booking.commons.to.EventMovieTO}
   * 
   * @param eventMovieTO It uses the attribute, idEvent. Returns the idMovieImage
   * @param fileTO The image
   */
  void saveMovieImage( EventMovieTO eventMovieTO, FileTO fileTO );

  /**
   * Obtains the Movie Image
   * 
   * @param idMovieImage
   * @return
   */
  FileTO getMovieImage( Long idMovieImage );

  /**
   * Obtains an {@link mx.com.cinepolis.digital.booking.commons.to.EventTO} of an
   * {@link mx.com.cinepolis.digital.booking.model.EventDO.idEvent}
   * 
   * @param idEvent The id Event
   * @return a TO representing the Event
   */
  EventTO getEvent( Long idEvent );

  /**
   * Obtains an {@link mx.com.cinepolis.digital.booking.commons.to.EventTO} of an
   * {@link mx.com.cinepolis.digital.booking.model.EventDO.idEvent}
   * 
   * @param idEvent The id Event
   * @param language The language
   * @return a TO representing the Event
   */
  EventTO getEvent( Long idEvent, Language language );

  /**
   * Finds all active events by its Vista Id
   * 
   * @param idVista
   * @return
   */
  List<EventDO> findByIdVistaAndActive( String idVista );

  /**
   * finds all active events by Screen.
   * 
   * @param id
   * @return
   */
  List<EventTO> findAvailableEventsByScreen( Long id );

  /**
   * Turn all the premiers for all events.
   */
  void updatePremiere();

  /**
   * Find all active events by dsCodeDbs.
   * 
   * @param dsCodeDbs
   * @return
   */
  List<EventTO> findByDsCodeDbs( String dsCodeDbs );

  /**
   * Find all active movies
   * 
   * @param premiere, festival
   * @return
   */
  List<CatalogTO> findAllActiveMovies( boolean premiere, boolean festival, boolean prerelease );
}
