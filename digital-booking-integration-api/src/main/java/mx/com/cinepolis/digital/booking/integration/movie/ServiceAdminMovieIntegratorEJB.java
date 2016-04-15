package mx.com.cinepolis.digital.booking.integration.movie;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;

/**
 * Interface that defines the integration services relating to a movie
 * 
 * @author afuentes
 */
public interface ServiceAdminMovieIntegratorEJB
{
  /**
   * Método que se encarga de registrar los datos de la película
   * 
   * @param eventMovieTO {@link EventMovieTO} con la información de la película.
   */
  void saveMovie( EventMovieTO eventMovieTO );

  /**
   * Método que se encarga de eliminar la película
   * 
   * @param eventMovieTO {@link EventMovieTO} con la información de la película.
   */
  void deleteMovie( EventMovieTO eventMovieTO );

  /**
   * Método que se encarga de actualizar los datos de la película
   * 
   * @param eventMovieTO {@link EventMovieTO} con la información de la película.
   */
  void updateMovie( EventMovieTO eventMovieTO );

  /**
   * Método que se encarga de obtener secciones por páginas del catálogo de películas
   * 
   * @param pagingRequestTO
   * @return {@link PagingResponseTO} con la lista paginada de objetos {@link EventMovieTO} con información de las
   *         películas.
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
  List<CatalogTO> getAllCountries();

  /**
   * Method to find a movie image.
   * 
   * @param idMovieImage Movie image identifier.
   * @return {@link FileTO} with the movie image information.
   */
  FileTO findMovieImage( Long idMovieImage );

  /**
   * Method that verifies whether a movie is actually in booking.
   * 
   * @param idMovie Movie identifier.
   * @return {@link java.lang.Boolean} with response True whether movie is in booking, else response be False.
   * @author jreyesv
   */
  Boolean isMovieInBooking( Long idMovie );

  /**
   * Method returns the id of the Alternate Content Distributor
   * 
   * @return idDistributor
   */
  public String getIdDistributorParameter();

}
