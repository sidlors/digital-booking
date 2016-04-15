package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.query.TerritoryQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.model.TerritoryDO;
import mx.com.cinepolis.digital.booking.model.TerritoryLanguageDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.TerritoryDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TerritoryDAOTest extends AbstractDBEJBTestUnit
{

  private TerritoryDAO territoryDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    territoryDAO = new TerritoryDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( territoryDAO );

  }

  @Test
  public void testFindAllByPaging()
  {
    int page = 0;
    int pageSize = 3;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );

    PagingResponseTO<CatalogTO> response = this.territoryDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ", " + catalogTO.getName() );
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortTerritoryIdASC()
  {
    int page = 0;
    int pageSize = 3;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagePagingRequestTO.getSort().add( TerritoryQuery.TERRITORY_ID );
    pagePagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<CatalogTO> response = this.territoryDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ", " + catalogTO.getName() );
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortTerritoryIdDESC()
  {
    int page = 0;
    int pageSize = 3;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagePagingRequestTO.getSort().add( TerritoryQuery.TERRITORY_ID );
    pagePagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<CatalogTO> response = this.territoryDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ", " + catalogTO.getName() );
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortTerritoryLanguageIdASC()
  {
    int page = 0;
    int pageSize = 3;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagePagingRequestTO.getSort().add( TerritoryQuery.TERRITORY_LANGUAGE_ID );
    pagePagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<CatalogTO> response = this.territoryDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ", " + catalogTO.getName() );
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortTerritoryLanguageIdDESC()
  {
    int page = 0;
    int pageSize = 3;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagePagingRequestTO.getSort().add( TerritoryQuery.TERRITORY_LANGUAGE_ID );
    pagePagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<CatalogTO> response = this.territoryDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ", " + catalogTO.getName() );
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortTerritoryNameASC()
  {
    int page = 0;
    int pageSize = 3;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagePagingRequestTO.getSort().add( TerritoryQuery.TERRITORY_NAME );
    pagePagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<CatalogTO> response = this.territoryDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ", " + catalogTO.getName() );
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_SortTerritoryNameDESC()
  {
    int page = 0;
    int pageSize = 3;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagePagingRequestTO.getSort().add( TerritoryQuery.TERRITORY_NAME );
    pagePagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<CatalogTO> response = this.territoryDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ", " + catalogTO.getName() );
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_FilterTerritoryId()
  {
    int page = 0;
    int pageSize = 3;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagePagingRequestTO.getSort().add( TerritoryQuery.TERRITORY_ID );
    pagePagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagePagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagePagingRequestTO.getFilters().put( TerritoryQuery.TERRITORY_ID, 1 );

    PagingResponseTO<CatalogTO> response = this.territoryDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ", " + catalogTO.getName() );
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );

  }

  @Test
  public void testFindAllByPaging_FilterTerritoryName()
  {
    int page = 0;
    int pageSize = 3;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagePagingRequestTO.getSort().add( TerritoryQuery.TERRITORY_NAME );
    pagePagingRequestTO.setSortOrder( SortOrder.ASCENDING );
    pagePagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagePagingRequestTO.getFilters().put( TerritoryQuery.TERRITORY_NAME, "Territor" );

    PagingResponseTO<CatalogTO> response = this.territoryDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    for( CatalogTO catalogTO : response.getElements() )
    {
      System.out.println( catalogTO.getId() + ", " + catalogTO.getName() );
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );

  }

  @Test
  public void testSaveCatalogTO()
  {
    int n = this.territoryDAO.count();
    Assert.assertEquals( 3, n );

    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setName( "Territory new" );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );

    this.territoryDAO.save( catalogTO );
    int territoryId = catalogTO.getId().intValue();

    n = this.territoryDAO.count();
    Assert.assertEquals( 4, n );

    TerritoryDO territoryDO = this.territoryDAO.find( territoryId );
    Assert.assertNotNull( territoryDO );
    Assert.assertNotNull( territoryDO.getTerritoryLanguageDOList() );
    Assert.assertEquals( 1, territoryDO.getTerritoryLanguageDOList().size() );

  }

  @Test
  public void testSaveCatalogTOLanguage()
  {
    int n = this.territoryDAO.count();
    Assert.assertEquals( 3, n );

    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setName( "Territory new" );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );

    this.territoryDAO.save( catalogTO, Language.ENGLISH );
    int territoryId = catalogTO.getId().intValue();

    catalogTO = new CatalogTO( Long.valueOf( territoryId ) );
    catalogTO.setName( "Territorio nuevo" );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );

    this.territoryDAO.save( catalogTO, Language.SPANISH );
    territoryId = catalogTO.getId().intValue();

    n = this.territoryDAO.count();
    Assert.assertEquals( 4, n );

    TerritoryDO territoryDO = this.territoryDAO.find( territoryId );
    Assert.assertNotNull( territoryDO );
    Assert.assertNotNull( territoryDO.getTerritoryLanguageDOList() );
    Assert.assertEquals( 2, territoryDO.getTerritoryLanguageDOList().size() );
  }

  @Test
  public void testUpdateCatalogTO()
  {
    String updated = "Territory updated";
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setName( updated );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    this.territoryDAO.update( catalogTO );

    TerritoryDO territoryDO = this.territoryDAO.find( 1 );
    Assert.assertNotNull( territoryDO );
    Assert.assertNotNull( territoryDO.getTerritoryLanguageDOList() );
    Assert.assertEquals( 2, territoryDO.getTerritoryLanguageDOList().size() );

    boolean isUpdated = false;
    for( TerritoryLanguageDO languageDO : territoryDO.getTerritoryLanguageDOList() )
    {
      if( languageDO.getIdLanguage().getIdLanguage().equals( Language.ENGLISH.getId() ) )
      {
        isUpdated = languageDO.getDsName().equals( updated );
        break;
      }
    }
    Assert.assertTrue( isUpdated );
  }

  @Test
  public void testUpdateCatalogTOLanguage()
  {
    String updated = "Territorio actualizado";
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setName( updated );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    this.territoryDAO.update( catalogTO, Language.SPANISH );

    TerritoryDO territoryDO = this.territoryDAO.find( 1 );
    Assert.assertNotNull( territoryDO );
    Assert.assertNotNull( territoryDO.getTerritoryLanguageDOList() );
    Assert.assertEquals( 2, territoryDO.getTerritoryLanguageDOList().size() );

    boolean isUpdated = false;
    for( TerritoryLanguageDO languageDO : territoryDO.getTerritoryLanguageDOList() )
    {
      if( languageDO.getIdLanguage().getIdLanguage().equals( Language.SPANISH.getId() ) )
      {
        isUpdated = languageDO.getDsName().equals( updated );
        break;
      }
    }
    Assert.assertTrue( isUpdated );
  }

  @Test
  public void testDelete()
  {
    int n = this.territoryDAO.count();
    Assert.assertEquals( 3, n );

    TerritoryDO territoryDO = this.territoryDAO.find( 1 );
    Assert.assertNotNull( territoryDO );
    Assert.assertTrue( territoryDO.isFgActive() );

    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );

    this.territoryDAO.delete( catalogTO );

    n = this.territoryDAO.count();
    Assert.assertEquals( 3, n );

    territoryDO = this.territoryDAO.find( 1 );
    Assert.assertNotNull( territoryDO );
    Assert.assertFalse( territoryDO.isFgActive() );

  }

  @Test
  public void testCreate()
  {
    int n = this.territoryDAO.count();
    Assert.assertEquals( 3, n );

    TerritoryDO territoryDO = new TerritoryDO();
    territoryDO.setDtLastModification( new Date() );
    territoryDO.setIdLastUserModifier( 1 );
    territoryDO.setTerritoryLanguageDOList( new ArrayList<TerritoryLanguageDO>() );

    TerritoryLanguageDO territoryLanguageDO = new TerritoryLanguageDO();
    territoryLanguageDO.setDsName( "Territory 4" );
    territoryLanguageDO.setIdTerritory( territoryDO );
    territoryLanguageDO.setIdLanguage( new LanguageDO( Language.ENGLISH.getId() ) );
    territoryDO.getTerritoryLanguageDOList().add( territoryLanguageDO );

    territoryLanguageDO = new TerritoryLanguageDO();
    territoryLanguageDO.setDsName( "Territorio 4" );
    territoryLanguageDO.setIdTerritory( territoryDO );
    territoryLanguageDO.setIdLanguage( new LanguageDO( Language.SPANISH.getId() ) );
    territoryDO.getTerritoryLanguageDOList().add( territoryLanguageDO );

    this.territoryDAO.create( territoryDO );
    int territoryId = territoryDO.getIdTerritory();

    n = this.territoryDAO.count();
    Assert.assertEquals( 4, n );

    territoryDO = this.territoryDAO.find( territoryId );
    Assert.assertNotNull( territoryDO );
    Assert.assertNotNull( territoryDO.getTerritoryLanguageDOList() );
    Assert.assertEquals( 2, territoryDO.getTerritoryLanguageDOList().size() );

  }

  @Test
  public void testEdit()
  {
    String updated = "Updated name";
    TerritoryDO territoryDO = this.territoryDAO.find( 1 );
    Assert.assertNotNull( territoryDO );
    for( TerritoryLanguageDO territoryLanguageDO : territoryDO.getTerritoryLanguageDOList() )
    {
      if( territoryLanguageDO.getIdTerritoryLanguage().equals( 1 ) )
      {
        territoryLanguageDO.setDsName( updated );
        break;
      }
    }

    this.territoryDAO.edit( territoryDO );

    territoryDO = this.territoryDAO.find( 1 );
    Assert.assertNotNull( territoryDO );
    boolean isUpdated = false;
    for( TerritoryLanguageDO territoryLanguageDO : territoryDO.getTerritoryLanguageDOList() )
    {
      if( territoryLanguageDO.getIdTerritoryLanguage().equals( 1 ) )
      {
        Assert.assertEquals( updated, territoryLanguageDO.getDsName() );
        isUpdated = territoryLanguageDO.getDsName().equals( updated );
        break;
      }
    }
    Assert.assertTrue( isUpdated );

  }

  @Test
  public void testRemove()
  {
    int n = this.territoryDAO.count();
    Assert.assertEquals( 3, n );

    TerritoryDO territoryDO = this.territoryDAO.find( 1 );
    Assert.assertNotNull( territoryDO );
    Assert.assertTrue( territoryDO.isFgActive() );

    this.territoryDAO.remove( territoryDO );

    n = this.territoryDAO.count();
    Assert.assertEquals( 3, n );

    territoryDO = this.territoryDAO.find( 1 );
    Assert.assertNotNull( territoryDO );
    Assert.assertFalse( territoryDO.isFgActive() );
  }

  @Test
  public void testFind()
  {
    TerritoryDO territoryDO = this.territoryDAO.find( 1 );
    Assert.assertNotNull( territoryDO );
  }

  @Test
  public void testFindAll()
  {
    List<TerritoryDO> territories = this.territoryDAO.findAll();
    Assert.assertNotNull( territories );
    Assert.assertFalse( territories.isEmpty() );
    Assert.assertEquals( 3, territories.size() );

  }

  @Test
  public void testFindRange()
  {
    List<TerritoryDO> territories = this.territoryDAO.findRange( new int[] { 0, 2 } );
    Assert.assertNotNull( territories );
    Assert.assertFalse( territories.isEmpty() );
    Assert.assertEquals( 2, territories.size() );
  }

  @Test
  public void testCount()
  {
    int n = this.territoryDAO.count();
    Assert.assertEquals( 3, n );
  }

  @Test
  public void test()
  {
    List<CatalogTO> tos = territoryDAO.getAll();
    Assert.assertNotNull( tos );
    for( CatalogTO to : tos )
    {
      System.out.println( to );
    }
  }

}
