package mx.com.cinepolis.digital.booking.persistence.dao;

import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.model.MovieImageDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.MovieImageDO}
 * 
 * @author gsegura
 */
public interface MovieImageDAO extends GenericDAO<MovieImageDO>
{

  /**
   * Method that saves an uploaded movie image.
   * 
   * @param fileTO {@link FileTO} with the movie image information.
   * @return {@link FileTO} with the saved movie image identifier.
   * @author afuentes
   */
  FileTO saveUploadedMovieImage( FileTO fileTO );

  /**
   * Method to find a movie image.
   * 
   * @param idMovieImage Movie image identifier.
   * @return {@link FileTO} with the movie image information.
   * @author afuentes
   */
  FileTO findMovieImage( Long idMovieImage );
}
