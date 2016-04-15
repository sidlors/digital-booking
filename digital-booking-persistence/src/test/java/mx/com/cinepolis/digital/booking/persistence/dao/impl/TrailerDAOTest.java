package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.model.TrailerDO;
import mx.com.cinepolis.digital.booking.model.TrailerStatusDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TrailerDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
/**
 * Clase para realizar pruebas del TrailerDAO
 * @author jcarbajal
 *
 */
public class TrailerDAOTest extends AbstractDBEJBTestUnit
{
  private TrailerDAO trailerDAO;
  private DistributorDAO distributorDAO;
  

  @Before
  public void setUp()
  {
    trailerDAO = new TrailerDAOImpl();
    distributorDAO=new DistributorDAOImpl();
    super.setUp();
    connect(trailerDAO);
    connect(distributorDAO);
    this.initializeData( "dataset/business/trailer.sql" );
  }
  
  /**
   * method for test find all the trailers
   */
  @Test
  public void findAll_Test()
  {
    List<TrailerDO> trailerDOList=this.trailerDAO.findAll();
    Assert.assertNotNull( trailerDOList );
    for(TrailerDO trailer:trailerDOList)
    {
      Assert.assertNotNull( trailer );
    }
  }
  /**
   * method for tests the function count
   */
  @Test
  public void count_Test()
  {
    int size =this.trailerDAO.count();
    Assert.assertEquals( size, size );
  }
  
  /**
   * Realize the test for remove method
   */
  @Ignore
  @Test
  public void remove_Test()
  {
    int sizeBeforeToRemove = this.trailerDAO.count();
    TrailerDO trailerToRemove=this.trailerDAO.find( 1L );
    Assert.assertNotNull( trailerToRemove );
    this.trailerDAO.remove( trailerToRemove );
    int sizeAfterToRemove= this.trailerDAO.count();
    Assert.assertEquals( sizeBeforeToRemove+1, sizeAfterToRemove );
  }
  /**
   * realize the test for the update method 
   */
  @Test
  public void update_Test()
  {
    TrailerDO trailerToUpdate=this.trailerDAO.find( 1L );
    Assert.assertNotNull( trailerToUpdate );
    trailerToUpdate.setFgActive( false );
    this.trailerDAO.edit( trailerToUpdate );
    TrailerDO trailerUpdated=this.trailerDAO.find( 1L );
    Assert.assertTrue(!( trailerToUpdate.isFgActive() && trailerUpdated.isFgActive() ));
  }
  
  /**
   *realize the test for create a trailer 
   */
  @Test
  public void create_Test()
  {
    int sizeBeforeCreate=this.trailerDAO.count();
    TrailerDO trailer=new TrailerDO();
    trailer.setDsGenre( "Horror" );
    trailer.setDsName( "Desapariciones " );
    trailer.setDtLastModification( new Date() );
    trailer.setDtRelease( new Date() );
    trailer.setFgActive( true );
    trailer.setFgCurrent( true );
    DistributorDO idDistributor=this.distributorDAO.find( 1 );
    Assert.assertNotNull( idDistributor );
    trailer.setIdDistributor( idDistributor );
    trailer.setIdLastUserModifier( 1 );
    trailer.setIdTrailer( 100L );
    TrailerStatusDO idTrailerStatus=new TrailerStatusDO();
    idTrailerStatus.setIdTrailerStatus( 4 );
    trailer.setIdTrailerStatus( idTrailerStatus );
    trailer.setQtDuration( 156 );
    
    this.trailerDAO.create( trailer );
    int sizeAfterCreate=this.trailerDAO.count();
    Assert.assertEquals( sizeBeforeCreate+1, sizeAfterCreate );
  }
  
  
  
  
}
