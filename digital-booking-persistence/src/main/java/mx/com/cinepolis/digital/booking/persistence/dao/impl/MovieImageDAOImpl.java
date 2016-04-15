package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.MovieImageDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.MovieImageDAO;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.MovieImageDAO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class MovieImageDAOImpl extends AbstractBaseDAO<MovieImageDO> implements MovieImageDAO
{

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  /**
   * Constructor default
   */
  public MovieImageDAOImpl()
  {
    super( MovieImageDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FileTO saveUploadedMovieImage( FileTO fileTO )
  {
    MovieImageDO movieImageDO = new MovieImageDO();
    movieImageDO.setDsFile( fileTO.getFile() );
    movieImageDO.setDsImage( fileTO.getName() );
    if(fileTO.getId() == null)
    {	
    create( movieImageDO );
    }
    else{
    	movieImageDO.setIdMovieImage(fileTO.getId());
    	edit(movieImageDO);
    }
    flush();
    fileTO.setId( movieImageDO.getIdMovieImage() );
    return fileTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FileTO findMovieImage( Long idMovieImage )
  {
    MovieImageDO movieImageDO = find( idMovieImage );
    FileTO fileTO = new FileTO();
    fileTO.setId( movieImageDO.getIdMovieImage() );
    fileTO.setName( movieImageDO.getDsImage() );
    fileTO.setFile( movieImageDO.getDsFile() );
    return fileTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

}
