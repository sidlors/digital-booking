package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.query.DistributorQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DistributorDAOTest extends AbstractDBEJBTestUnit
{
  private DistributorDAO distributorDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    distributorDAO = new DistributorDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( distributorDAO );

  }

  @Test
  public void testCreate()
  {
    for( int i = 0; i < 3; i++ )
    {
      int n = distributorDAO.count();
      DistributorDO distributorDO = new DistributorDO();
      distributorDO.setDsName( "test" );
      distributorDO.setDtLastModification( new Date() );
      distributorDO.setFgActive( true );
      distributorDO.setIdLastUserModifier( 1 );
      this.distributorDAO.create( distributorDO );

      Assert.assertEquals( n + 1, distributorDAO.count() );

      System.out.println( "id: " + distributorDO.getIdDistributor() );
    }

  }

  @Test
  public void testEdit()
  {
    String updated = "updated name";
    DistributorDO distributorDO = this.distributorDAO.find( 1 );
    distributorDO.setDsName( "updated name" );
    this.distributorDAO.edit( distributorDO );
    Assert.assertEquals( updated, this.distributorDAO.find( 1 ).getDsName() );
  }

  @Test
  public void testRemove()
  {
    int id = 10;
    int n = this.distributorDAO.count();

    DistributorDO distributorDO = this.distributorDAO.find( id );
    Assert.assertTrue( distributorDO.isFgActive() );
    this.distributorDAO.remove( distributorDO );
    Assert.assertEquals( n, this.distributorDAO.count() );

    Assert.assertFalse( this.distributorDAO.find( id ).isFgActive() );

  }

  @Test
  public void testFind()
  {
    DistributorDO distributorDO = this.distributorDAO.find( 10 );
    Assert.assertNotNull( distributorDO );

    distributorDO = this.distributorDAO.find( 100 );
    Assert.assertNull( distributorDO );

  }

  @Test
  public void testFindAll()
  {
    int n = this.distributorDAO.count();
    List<DistributorDO> allDistributor = this.distributorDAO.findAll();
    Assert.assertNotNull( allDistributor );

    Assert.assertEquals( n, allDistributor.size() );
  }

  @Test
  public void testFindRange()
  {
    List<DistributorDO> distributors = this.distributorDAO.findRange( new int[] { 3, 6 } );
    Assert.assertNotNull( distributors );

    Assert.assertEquals( 3, distributors.size() );
  }

  @Test
  public void testCount()
  {
    int n = distributorDAO.count();
    Assert.assertEquals( 12, n );
  }

  @Test
  public void testFindAllByPaging()
  {
    int totalCount = 12;
    int pageSize = 5;
    for( int page = 0; page < 3; page++ )
    {

      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      PagingResponseTO<DistributorTO> response = distributorDAO.findAllByPaging( pagingRequestTO );

      List<DistributorTO> distributors = response.getElements();
      Assert.assertNotNull( distributors );
      Assert.assertEquals( totalCount, response.getTotalCount() );

      Assert.assertTrue( pageSize >= distributors.size() );
      for( DistributorTO distributorDO : distributors )
      {
        System.out.println( distributorDO.getId() + ", " + distributorDO.getName() );
      }
    }

  }

  @Test
  public void testFindAllByPaging_withFilters()
  {
    int totalCount = 3;
    int pageSize = 5;
    int page = 0;

    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( DistributorQuery.DISTRIBUTOR_NAME, "Distribuidor 1" );
    PagingResponseTO<DistributorTO> response = distributorDAO.findAllByPaging( pagingRequestTO );

    List<DistributorTO> distributors = response.getElements();
    Assert.assertNotNull( distributors );
    Assert.assertEquals( totalCount, response.getTotalCount() );

    Assert.assertTrue( pageSize >= distributors.size() );
    for( DistributorTO distributorDO : distributors )
    {
      System.out.println( distributorDO.getId() + ", " + distributorDO.getName() );
    }

  }

  @Test
  public void testFindAllByPaging_withFilters2()
  {
    int totalCount = 1;
    int pageSize = 5;
    int page = 0;

    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( DistributorQuery.DISTRIBUTOR_ID, 1 );
    PagingResponseTO<DistributorTO> response = distributorDAO.findAllByPaging( pagingRequestTO );

    List<DistributorTO> distributors = response.getElements();
    Assert.assertNotNull( distributors );
    Assert.assertEquals( totalCount, response.getTotalCount() );

    Assert.assertTrue( pageSize >= distributors.size() );
    for( DistributorTO distributorDO : distributors )
    {
      System.out.println( distributorDO.getId() + ", " + distributorDO.getName() );
    }
  }

  @Test
  public void testFindAllByPaging_withFiltersActive()
  {
    int totalCount = 12;
    int pageSize = 5;
    int page = 0;

    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( DistributorQuery.DISTRIBUTOR_ACTIVE, true );
    PagingResponseTO<DistributorTO> response = distributorDAO.findAllByPaging( pagingRequestTO );

    List<DistributorTO> distributors = response.getElements();
    Assert.assertNotNull( distributors );
    Assert.assertEquals( totalCount, response.getTotalCount() );

    Assert.assertTrue( pageSize >= distributors.size() );
    for( DistributorTO distributorDO : distributors )
    {
      System.out.println( distributorDO.getId() + ", " + distributorDO.getName() );
    }
  }

  @Test
  public void testFindAllByPaging_sortASC()
  {
    int totalCount = 12;
    int pageSize = 5;
    for( int page = 0; page < 3; page++ )
    {

      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( DistributorQuery.DISTRIBUTOR_NAME );
      PagingResponseTO<DistributorTO> response = distributorDAO.findAllByPaging( pagingRequestTO );

      List<DistributorTO> distributors = response.getElements();
      Assert.assertNotNull( distributors );
      Assert.assertEquals( totalCount, response.getTotalCount() );

      Assert.assertTrue( pageSize >= distributors.size() );
      for( DistributorTO distributorDO : distributors )
      {
        System.out.println( distributorDO.getId() + ", " + distributorDO.getName() );
      }
    }

  }

  @Test
  public void testFindAllByPaging_sortDESC()
  {
    int totalCount = 12;
    int pageSize = 5;
    for( int page = 0; page < 3; page++ )
    {
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      pagingRequestTO.setSortOrder( SortOrder.DESCENDING );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( DistributorQuery.DISTRIBUTOR_NAME );
      PagingResponseTO<DistributorTO> response = distributorDAO.findAllByPaging( pagingRequestTO );

      List<DistributorTO> distributors = response.getElements();
      Assert.assertNotNull( distributors );
      Assert.assertEquals( totalCount, response.getTotalCount() );

      Assert.assertTrue( pageSize >= distributors.size() );
      for( DistributorTO distributorDO : distributors )
      {
        System.out.println( distributorDO.getId() + ", " + distributorDO.getName() );
      }
    }

  }

  @Test
  public void testSave()
  {
    int n = distributorDAO.count();
    DistributorTO to = new DistributorTO();
    to.setName( "name" );
    to.setUserId( 1L );
    to.setTimestamp( new Date() );
    this.distributorDAO.save( to );
    Assert.assertEquals( n + 1, distributorDAO.count() );
  }

  @Test
  public void testUpdate()
  {
    String updated = "updated name";
    DistributorTO to = new DistributorTO();
    to.setId( 3L );
    to.setName( updated );
    to.setUserId( 1L );
    to.setTimestamp( new Date() );
    this.distributorDAO.update( to );

    Assert.assertEquals( updated, this.distributorDAO.find( 3 ).getDsName() );

  }

  @Test
  public void testDelete()
  {
    Assert.assertTrue( this.distributorDAO.find( 3 ).isFgActive() );
    DistributorTO to = new DistributorTO();
    to.setId( 3L );
    to.setUserId( 1L );
    to.setTimestamp( new Date() );
    this.distributorDAO.delete( to );

    Assert.assertFalse( this.distributorDAO.find( 3 ).isFgActive() );
  }

  @Test
  public void testFindByDsNameActive()
  {

    String dsName = "Distribuidor 1";

    List<DistributorDO> distributorDOs = this.distributorDAO.findByDsNameActive( dsName );
    Assert.assertNotNull( distributorDOs );
    Assert.assertFalse( distributorDOs.isEmpty() );
    Assert.assertEquals( 1, distributorDOs.size() );

  }

  @Test
  public void testFindByIdVistaAndActive()
  {
    List<DistributorDO> distributors = this.distributorDAO.findByIdVistaAndActive("1001" );
    Assert.assertNotNull( distributors );
    Assert.assertEquals( 1, distributors.size() );
  }
}
