package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.MovieImageDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Class that implements the {@link MovieImageDAO} unit tests.
 * 
 * @author afuentes
 */
public class MovieImageDAOTest extends AbstractDBEJBTestUnit
{
  private MovieImageDAO movieImageDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    movieImageDAO = new MovieImageDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( movieImageDAO );

  }

  @Test
  public void testSaveUploadedMovieImage()
  {
    int imagesBefore = movieImageDAO.count();
    FileTO fileTO = new FileTO();
    fileTO.setName( "image.jpg" );
    fileTO.setFile( "Image file contents".getBytes() );

    fileTO = movieImageDAO.saveUploadedMovieImage( fileTO );

    int imagesAfter = movieImageDAO.count();
    Assert.assertNotNull( fileTO.getId() );
    Assert.assertEquals( imagesBefore + 1, imagesAfter );
  }

  @Test
  public void test_saveUploadedMovieImage()
  {
    FileTO fileTO = new FileTO();
    fileTO.setName( "image.jpg" );
    fileTO.setFile( "Image file contents".getBytes() );
    fileTO.setId(1L);
    int size=this.movieImageDAO.count();
    this.movieImageDAO.saveUploadedMovieImage( fileTO );
    int sizeb=this.movieImageDAO.count();
    Assert.assertEquals( size, sizeb );
  }
  
  @Test
  public void test_findMovieImage()
  {
    FileTO fileTO=this.movieImageDAO.findMovieImage( 1L );
    Assert.assertNotNull( fileTO );
  }
}
