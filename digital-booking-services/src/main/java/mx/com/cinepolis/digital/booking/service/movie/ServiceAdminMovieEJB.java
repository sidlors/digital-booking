package mx.com.cinepolis.digital.booking.service.movie;

import java.util.List;

import javax.ejb.Local;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;

/**
 * Interface que define los metodos asociados a la administración de películas
 * 
 * @author rgarcia
 * @author afuentes
 */
@Local
public interface ServiceAdminMovieEJB
{

  /**
   * Método que se encarga de registrar los datos de la película
   * 
   * @param eventMovieTO
   */
  void saveMovie( EventMovieTO eventMovieTO );

  /**
   * Método que se encarga de eliminar la película
   * 
   * @param eventMovieTO
   */
  void deleteMovie( EventMovieTO eventMovieTO );

  /**
   * Método que se encarga de actualizar los datos de la película
   * 
   * @param eventMovieTO
   */
  void updateMovie( EventMovieTO eventMovieTO );

  /**
   * Método que se encarga de obtener secciones por páginas del catálogo de películas
   * 
   * @param pagingRequestTO
   * @return
   */
  PagingResponseTO<EventMovieTO> getCatalogMovieSummary( PagingRequestTO pagingRequestTO );

  /**
   * Method that saves a movie image file to the database.
   * 
   * @param fileTO {@link FileTO} with the byte array representing the movie image file.
   * @return {@link FileTO} with the movie image identifier.
   * @author afuentes
   */
  FileTO saveMovieImage( FileTO fileTO );

  /**
   * Method that gets all active countries.
   * 
   * @return List of {@link CatalogTO} with the active countries information.
   * @author afuentes
   */
  List<CatalogTO> getAllContries();

  /**
   * Method to find a movie image.
   * 
   * @param idMovieImage Movie image identifier.
   * @return {@link FileTO} with the movie image information.
   * @author afuentes
   */
  FileTO findMovieImage( Long idMovieImage );

  /**
   * Method that finds all top movie of the current week.
   * 
   * @param abstractTO
   * @return List of {@link EventMovieTO} with the top movies of the current week information.
   */
  List<IncomeTO> getTopWeek( AbstractTO abstractTO );

  /**
   * Method that returns the current week for cinepolis.
   * 
   * @param abstractTO, with the signed information.
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.WeekTO}, with the current week information.
   * @author jreyesv
   */
  WeekTO getTopWeekTO( AbstractTO abstractTO );

  /**
   * Method returns the id of the Alternate Content Distributor
   * 
   * @return idDistributor
   */
  public String getIdDistributorParameter();

  /**
   * Method that verifies whether a movie is actually in booking.
   * 
   * @param idMovie Movie identifier.
   * @return {@link java.lang.Boolean} with response True whether movie is in booking, else response be False.
   * @author jreyesv
   */
  Boolean isMovieInBooking( Long idMovie );

}
