package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.query.CategoryQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.CategoryLanguageDO;
import mx.com.cinepolis.digital.booking.model.CategoryTypeDO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CategoryDAOTest extends AbstractDBEJBTestUnit
{

  private CategoryDAO categoryDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    categoryDAO = new CategoryDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( categoryDAO );

  }

  @Test
  public void testCreate()
  {
    int n = this.categoryDAO.count();
    Assert.assertEquals( 10, n );

    CategoryDO categoryDO = new CategoryDO();
    categoryDO.setDtLastModification( new Date() );
    categoryDO.setFgActive( true );
    categoryDO.setIdCategoryType( new CategoryTypeDO( 1 ) );
    categoryDO.setIdLastUserModifier( 1 );
    categoryDO.setCategoryLanguageDOList( new ArrayList<CategoryLanguageDO>() );

    CategoryLanguageDO categogoryLanguage = new CategoryLanguageDO();
    categogoryLanguage.setDsName( "New" );
    categogoryLanguage.setIdCategory( categoryDO );
    categogoryLanguage.setIdLanguage( new LanguageDO( 1 ) );
    categoryDO.getCategoryLanguageDOList().add( categogoryLanguage );

    categogoryLanguage = new CategoryLanguageDO();
    categogoryLanguage.setDsName( "Nuevo" );
    categogoryLanguage.setIdCategory( categoryDO );
    categogoryLanguage.setIdLanguage( new LanguageDO( 2 ) );
    categoryDO.getCategoryLanguageDOList().add( categogoryLanguage );
    this.categoryDAO.create( categoryDO );

    Assert.assertEquals( n + 1, this.categoryDAO.count() );

  }

  @Test
  public void testEdit()
  {
    String updatedName = "updatedName";
    CategoryDO categoryDO = this.categoryDAO.find( 1 );
    Assert.assertNotNull( categoryDO );
    for( CategoryLanguageDO categoryLanguageDO : categoryDO.getCategoryLanguageDOList() )
    {
      if( categoryLanguageDO.getIdLanguage().getIdLanguage().equals( Language.ENGLISH.getId() ) )
      {
        categoryLanguageDO.setDsName( updatedName );
      }
    }
    this.categoryDAO.edit( categoryDO );

    categoryDO = this.categoryDAO.find( 1 );
    for( CategoryLanguageDO categoryLanguageDO : categoryDO.getCategoryLanguageDOList() )
    {
      if( categoryLanguageDO.getIdLanguage().getIdLanguage().equals( Language.ENGLISH.getId() ) )
      {
        Assert.assertEquals( updatedName, categoryLanguageDO.getDsName() );
        break;
      }
    }

    System.out.println( categoryDO );
    for( CategoryLanguageDO categoryLanguageDO : categoryDO.getCategoryLanguageDOList() )
    {
      System.out.println( categoryLanguageDO.getIdCategoryLanguage() + ", "
          + categoryLanguageDO.getIdLanguage().getIdLanguage() + ", " + categoryLanguageDO.getDsName() );
    }

  }

  @Test
  public void testRemove()
  {
    int n = this.categoryDAO.count();
    CategoryDO categoryDO = this.categoryDAO.find( 1 );
    Assert.assertTrue( categoryDO.isFgActive() );

    this.categoryDAO.remove( categoryDO );

    Assert.assertEquals( n, this.categoryDAO.count() );
    categoryDO = this.categoryDAO.find( 1 );
    Assert.assertFalse( categoryDO.isFgActive() );
  }

  @Test
  public void testFind()
  {
    Assert.assertNotNull( this.categoryDAO.find( 1 ) );
    Assert.assertNull( this.categoryDAO.find( 100 ) );
  }

  @Test
  public void testFindAll()
  {
    int n = 10;
    List<CategoryDO> categories = this.categoryDAO.findAll();
    Assert.assertNotNull( categories );
    Assert.assertFalse( categories.isEmpty() );
    Assert.assertEquals( n, categories.size() );
  }

  @Test
  public void testFindRange()
  {
    int n = 3;

    List<CategoryDO> categories = this.categoryDAO.findRange( new int[] { 1, 4 } );
    Assert.assertNotNull( categories );
    Assert.assertFalse( categories.isEmpty() );
    Assert.assertEquals( n, categories.size() );
  }

  @Test
  public void testCount()
  {
    int n = this.categoryDAO.count();
    Assert.assertEquals( 10, n );
  }

  @Test
  public void testSave()
  {
    int n = this.categoryDAO.count();
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setName( "New catalog" );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    this.categoryDAO.save( catalogTO, CategoryType.SOUND_FORMAT );

    Assert.assertEquals( n + 1, this.categoryDAO.count() );

  }

  @Test
  public void testSaveLanguage()
  {
    int n = this.categoryDAO.count();
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setName( "New catalog" );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    this.categoryDAO.save( catalogTO, CategoryType.SOUND_FORMAT, Language.ENGLISH );

    catalogTO = new CatalogTO( catalogTO.getId() );
    catalogTO.setName( "Catalogo nuevo" );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    this.categoryDAO.save( catalogTO, CategoryType.SOUND_FORMAT, Language.SPANISH );

    Assert.assertEquals( n + 1, this.categoryDAO.count() );

  }

  @Test
  public void testUpdate()
  {
    String updateName = "New catalog";
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setName( updateName );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );

    this.categoryDAO.update( catalogTO );

    boolean isUpdated = false;
    CategoryDO categoryDO = this.categoryDAO.find( 1 );
    for( CategoryLanguageDO categoryLanguageDO : categoryDO.getCategoryLanguageDOList() )
    {
      if( categoryLanguageDO.getIdLanguage().getIdLanguage().equals( Language.ENGLISH.getId() ) )
      {
        isUpdated = categoryLanguageDO.getDsName().equals( updateName );
        Assert.assertEquals( updateName, categoryLanguageDO.getDsName() );
        break;
      }
    }
    Assert.assertTrue( isUpdated );
  }

  @Test
  public void testUpdateLanguage()
  {
    String updateName = "Nuevo catalogo";
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setName( updateName );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );

    this.categoryDAO.update( catalogTO, Language.SPANISH );

    boolean isUpdated = false;
    CategoryDO categoryDO = this.categoryDAO.find( 1 );
    for( CategoryLanguageDO categoryLanguageDO : categoryDO.getCategoryLanguageDOList() )
    {
      if( categoryLanguageDO.getIdLanguage().getIdLanguage().equals( Language.SPANISH.getId() ) )
      {
        isUpdated = categoryLanguageDO.getDsName().equals( updateName );
        Assert.assertEquals( updateName, categoryLanguageDO.getDsName() );
        break;
      }
    }
    Assert.assertTrue( isUpdated );
  }

  @Test
  public void testDelete()
  {
    int n = this.categoryDAO.count();
    CategoryDO categoryDO = this.categoryDAO.find( 1 );
    Assert.assertTrue( categoryDO.isFgActive() );

    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );

    this.categoryDAO.delete( catalogTO );

    Assert.assertEquals( n, this.categoryDAO.count() );
    categoryDO = this.categoryDAO.find( 1 );
    Assert.assertFalse( categoryDO.isFgActive() );
  }

  @Test
  public void testFindAllByPaging()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 3 );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );

  }

  @Test
  public void testFindAllByPaging_CategoryId()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 3 );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_ID, 1 );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );
    System.out.println("---->"+response.getTotalCount());
    for (CatalogTO to : response.getElements()){
      System.out.println(to);
    }

  }

  @Test
  public void testFindAllByPaging_CategoryTypeId()
  {

    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 3 );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_TYPE_ID, 1 );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );

    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
    }
    System.out.println( response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_LanguageId()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 3 );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_LANGUAGE_ID, Language.ENGLISH.getId() );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );

    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
    }
    System.out.println( response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_CategoryName()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 3 );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_NAME, "IMAX" );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );

    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
    }
    System.out.println( response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_CategoryTypeIdLanguageId()
  {

    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 3 );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_TYPE_ID, 1 );
    pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_LANGUAGE_ID, Language.ENGLISH.getId() );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );

    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
    }
    System.out.println( response.getTotalCount() );
  }

  @Test
  public void testFindAllByPaging_SortByCategoryIdAsc()
  {

    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 3 );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CategoryQuery.CATEGORY_ID );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
    }
    System.out.println( response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortByCategoryIdDesc()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 3 );
    pagingRequestTO.setSortOrder( SortOrder.DESCENDING );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CategoryQuery.CATEGORY_ID );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
    }
    System.out.println( response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortByLanguageIdAsc()
  {

    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 6 );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CategoryQuery.CATEGORY_LANGUAGE_ID );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
    }
    System.out.println( response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortByCATEGORY_LANGUAGE_IDIdDesc()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 6 );
    pagingRequestTO.setSortOrder( SortOrder.DESCENDING );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CategoryQuery.CATEGORY_LANGUAGE_ID );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
    }
    System.out.println( response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortByCategoryNameAsc()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 6 );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CategoryQuery.CATEGORY_NAME );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
    }
    System.out.println( response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortByCategoryNameDesc()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 6 );
    pagingRequestTO.setSortOrder( SortOrder.DESCENDING );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CategoryQuery.CATEGORY_NAME );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
    }
    System.out.println( response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortByCategoryTypeIdAsc()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 6 );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CategoryQuery.CATEGORY_TYPE_ID );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
    }
    System.out.println( response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortByCategoryTypeIdDesc()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setPage( 0 );
    pagingRequestTO.setPageSize( 6 );
    pagingRequestTO.setSortOrder( SortOrder.DESCENDING );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( CategoryQuery.CATEGORY_TYPE_ID );
    PagingResponseTO<CatalogTO> response = this.categoryDAO.findAllByPaging( pagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ": " + catalogTO.getName() );
    }
    System.out.println( response.getTotalCount() );

  }

  @Test
  public void testFindByDsNameActive()
  {
    String dsName = "Esp ENG";
    Integer idCategoryType = 1;
    List<CategoryDO> categories = this.categoryDAO.findByDsNameActive( dsName, idCategoryType );
    Assert.assertNotNull( categories );
    Assert.assertFalse( categories.isEmpty() );
  }

  @Test
  public void testFindByDsNameActive_dsNameNotFound()
  {
    String dsName = "Esp";
    Integer idCategoryType = 1;
    List<CategoryDO> categories = this.categoryDAO.findByDsNameActive( dsName, idCategoryType );
    Assert.assertEquals( 0, categories.size() );
  }

  @Test
  public void testFindByDsNameActiveLanguage()
  {
    String dsName = "Esp ENG";
    Language language = Language.ENGLISH;
    Integer idCategoryType = 1;
    List<CategoryDO> categories = this.categoryDAO.findByDsNameActive( dsName, idCategoryType, language );
    Assert.assertNotNull( categories );
    Assert.assertFalse( categories.isEmpty() );
  }

  @Test
  public void testGetAllByCategoryTypeSoundFormats()
  {
    List<CatalogTO> sounds = this.categoryDAO.getAllByCategoryType( CategoryType.SOUND_FORMAT );
    Assert.assertNotNull( sounds );

    for( CatalogTO catalogTO : sounds )
    {
      System.out.println( catalogTO );
    }
  }
  
  @Test
  public void testGetAllByCategoryTypeMovieFormats()
  {
    List<CatalogTO> sounds = this.categoryDAO.getAllByCategoryType( CategoryType.MOVIE_FORMAT );
    Assert.assertNotNull( sounds );

    for( CatalogTO catalogTO : sounds )
    {
      System.out.println( catalogTO );
    }
  }
  
  @Test
  public void testFindByIdCategory()
  {
	  List<CatalogTO> categories = this.categoryDAO.findByIdCategory(1, Language.ENGLISH);
	  Assert.assertNotNull( categories );
	  Assert.assertFalse( categories.isEmpty() );
  }
}
