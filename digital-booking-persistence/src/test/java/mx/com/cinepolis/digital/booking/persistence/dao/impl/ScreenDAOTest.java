package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.ScreenQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO;

/**
 * Clase de Pruebas de SCreenDAO
 * 
 * @author agustin.ramirez
 */
public class ScreenDAOTest extends AbstractDBEJBTestUnit
{
  /**
   * DAO
   */
  private ScreenDAO screenDAO;

  private CategoryDAO categoryDAO;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp ()
   */
  @Before
  public void setUp()
  {
    // instanciar el servicio
    screenDAO = new ScreenDAOImpl();
    categoryDAO = new CategoryDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( screenDAO );
    connect( categoryDAO );
  }

  /**
   * Prueba Count
   */
  @Test
  public void testCount()
  {
    Assert.assertEquals( 6, this.screenDAO.count() );
  }

  /**
   * Prueba Create
   */
  @Test
  public void testCreate()
  {
    int n = screenDAO.count();
    System.out.print( "Pantallas capturadas" + n );
    ScreenDO newScreenDO = new ScreenDO();
    newScreenDO.setFgActive( Boolean.TRUE );
    newScreenDO.setDtLastModification( new Date() );
    newScreenDO.setIdLastUserModifier( 1 );
    newScreenDO.setIdTheater( new TheaterDO( 1 ) );
    newScreenDO.setNuCapacity( 1200 );
    newScreenDO.setNuScreen( 10 );
    newScreenDO.setCategoryDOList( new ArrayList<CategoryDO>() );
    newScreenDO.getCategoryDOList().add( this.categoryDAO.find( 1 ) );
    newScreenDO.getCategoryDOList().add( this.categoryDAO.find( 3 ) );

    this.screenDAO.create( newScreenDO );
    Assert.assertEquals( n + 1, this.screenDAO.count() );
  }

  /**
   * Test Remove
   */
  @Test
  public void testRemove()
  {
    int n = this.screenDAO.count();

    ScreenDO screenDO = this.screenDAO.find( 1 );
    Assert.assertTrue( screenDO.isFgActive() );
    this.screenDAO.remove( screenDO );

    Assert.assertEquals( n, this.screenDAO.count() );

    screenDO = this.screenDAO.find( 1 );
    Assert.assertNotNull( screenDO );
    Assert.assertFalse( screenDO.isFgActive() );

  }

  /**
   * Test Edit
   */
  @Test
  public void testEdit()
  {
    ScreenDO screenDO = screenDAO.find( 1 );
    screenDO.setNuScreen( 102 );
    this.screenDAO.edit( screenDO );

    ScreenDO screenDO2 = screenDAO.find( 1 );
    Assert.assertTrue( screenDO2.getNuScreen() == 102 );
  }

  /**
   * Test Save
   */
  @Test
  public void testSave()
  {
    ScreenTO screenTO = new ScreenTO();
    screenTO.setFgActive( Boolean.TRUE );
    screenTO.setIdTheater( 1 );
    screenTO.setMovieFormats( Arrays.asList( new CatalogTO( 1L ) ) );
    screenTO.setName( "10" );
    screenTO.setNuCapacity( 100 );
    screenTO.setNuScreen( 10 );
    screenTO.setSoundFormats( Arrays.asList( new CatalogTO( 3L ) ) );
    screenTO.setTimestamp( new Date() );
    screenTO.setUserId( 1L );
    screenDAO.save( screenTO );
    System.out.println( "ID SAVED =>" + screenTO.getId() );
    Assert.assertNotNull( screenTO.getId() );

    this.screenDAO.flush();
    ScreenDO screenDO = this.screenDAO.find( screenTO.getId().intValue() );
    Assert.assertNotNull( screenDO );
    Assert.assertNotNull( screenDO.getCategoryDOList() );
    Assert.assertEquals( 2, screenDO.getCategoryDOList().size() );

  }

  /**
   * Test Update
   */
  @Test
  public void testUpdate()
  {
    ScreenTO screenTO = new ScreenTO();
    screenTO.setFgActive( Boolean.TRUE );
    screenTO.setIdTheater( 2 );
    screenTO.setMovieFormats( Arrays.asList( new CatalogTO( 1L ) ) );
    screenTO.setName( "10" );
    screenTO.setNuCapacity( 1000 );
    screenTO.setNuScreen( 10 );
    screenTO.setSoundFormats( Arrays.asList( new CatalogTO( 3L ) ) );
    screenTO.setTimestamp( new Date() );
    screenTO.setUserId( 1L );
    screenTO.setId( 1L );
    screenDAO.update( screenTO );

    ScreenDO screenUpdated = screenDAO.find( 1 );
    Assert.assertNotNull( screenUpdated );
    Assert.assertTrue( screenUpdated.getNuCapacity() == 1000 );
  }

  /**
   * Test Find
   */
  @Test
  public void testFind()
  {
    ScreenDO screenDO = null;
    screenDO = screenDAO.find( 2 );
    Assert.assertNotNull( screenDO );
  }

  /**
   * TestFindAll
   */
  @Test
  public void testFindAll()
  {
    List<ScreenDO> listAll = null;
    listAll = screenDAO.findAll();
    Assert.assertNotNull( listAll );
    Assert.assertTrue( listAll.size() > 0 );
  }

  /**
   * TestFindRange
   */
  @Test
  public void testRange()
  {
    List<ScreenDO> screens = screenDAO.findRange( new int[] { 0, 3 } );
    Assert.assertNotNull( screens );
    Assert.assertFalse( screens.isEmpty() );
    Assert.assertEquals( 3, screens.size() );
  }

  /**
   * TestDelete
   */
  @Test
  public void testDelete()
  {
    int n = screenDAO.count();
    ScreenTO screenTO = new ScreenTO();
    screenTO.setId( 1L );
    screenTO.setTimestamp( new Date() );
    screenTO.setUserId( 1L );
    this.screenDAO.delete( screenTO );
    Assert.assertEquals( n, screenDAO.count() );

  }

  /**
   * Tests Find All By Paging
   */
  @Test
  public void testFindAllByPaging()
  {
    int page = 0;
    int pageSize = 3;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );

    PagingResponseTO<ScreenTO> response = this.screenDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 6, response.getTotalCount() );

    for( ScreenTO screenTO : response.getElements() )
    {
      System.out.println( "Screen =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Tests Find All Without Paging
   */
  @Test
  public void testFindAllWithoutPaging()
  {

    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setNeedsPaging( Boolean.FALSE );

    PagingResponseTO<ScreenTO> response = this.screenDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 6, response.getTotalCount() );

    for( ScreenTO screenTO : response.getElements() )
    {
      System.out.println( "Screen =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Filtrado por Screen ID
   */
  @Test
  public void testFindAllByPaging_FilterByScreenId()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( ScreenQuery.SCREEN_ID, 2 );

    PagingResponseTO<ScreenTO> response = this.screenDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 1, response.getTotalCount() );

    for( ScreenTO screenTO : response.getElements() )
    {
      System.out.println( "Screen =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /***
   * Filtrado por ScreendID e idioma español para categorias
   */
  @Test
  public void testFindAllByPaging_FilterByScreenIdCategoriesLangSP()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( ScreenQuery.SCREEN_ID, 2 );
    pagingRequestTO.setLanguage( Language.SPANISH );

    PagingResponseTO<ScreenTO> response = this.screenDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 1, response.getTotalCount() );

    for( ScreenTO screenTO : response.getElements() )
    {
      System.out.println( "Screen =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Filtrado por Screen ID
   */
  @Test
  public void testFindAllByPaging_FilterByTheater()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( ScreenQuery.SCREEN_THEATER_ID, 2 );

    PagingResponseTO<ScreenTO> response = this.screenDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( ScreenTO screenTO : response.getElements() )
    {
      System.out.println( "Screen =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Filtrado por Numero de sala
   */
  @Test
  public void testFindAllByPaging_FilterByNumScreen()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( ScreenQuery.SCREEN_NUMBER, 2 );

    PagingResponseTO<ScreenTO> response = this.screenDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 2, response.getTotalCount() );

    for( ScreenTO screenTO : response.getElements() )
    {
      System.out.println( "Screen =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Filtrado por Capacidad
   */
  @Test
  public void testFindAllByPaging_FilterByCapacity()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( ScreenQuery.SCREEN_CAPACITY, 100 );

    PagingResponseTO<ScreenTO> response = this.screenDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 6, response.getTotalCount() );

    for( ScreenTO screenTO : response.getElements() )
    {
      System.out.println( "Screen =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  @Test
  public void testFindAllActiveByIdCinema()
  {
    List<ScreenDO> screenDOs = this.screenDAO.findAllActiveByIdCinema( 1 );

    Assert.assertNotNull( screenDOs );
    Assert.assertEquals( 3, screenDOs.size() );
  }

  @Test
  public void testFindByIdVistaAndActive()
  {
    List<ScreenDO> screens = this.screenDAO.findByIdVistaAndActive( "1001" );
    Assert.assertNotNull( screens );
    Assert.assertEquals( 1, screens.size() );
  }
}
