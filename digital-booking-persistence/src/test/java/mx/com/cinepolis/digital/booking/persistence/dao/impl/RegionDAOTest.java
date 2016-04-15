package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.RegionQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.RegionLanguageDO;
import mx.com.cinepolis.digital.booking.model.TerritoryDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RegionDAOTest extends AbstractDBEJBTestUnit
{
  private RegionDAO regionDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    regionDAO = new RegionDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( regionDAO );

  }

  @Test
  public void testCreate()
  {
    int n = this.regionDAO.count();
    RegionDO regionDO = new RegionDO();
    regionDO.setDtLastModification( new Date() );
    regionDO.setIdLastUserModifier( 1 );
    regionDO.setIdTerritory( new TerritoryDO( 1 ) );
    regionDO.setRegionLanguageDOList( new ArrayList<RegionLanguageDO>() );

    RegionLanguageDO lang = new RegionLanguageDO();
    lang.setDsName( "Region A ENG" );
    lang.setIdLanguage( new LanguageDO( Language.ENGLISH.getId() ) );
    lang.setIdRegion( regionDO );
    regionDO.getRegionLanguageDOList().add( lang );

    lang = new RegionLanguageDO();
    lang.setDsName( "Region A ESP" );
    lang.setIdLanguage( new LanguageDO( Language.SPANISH.getId() ) );
    lang.setIdRegion( regionDO );
    regionDO.getRegionLanguageDOList().add( lang );

    this.regionDAO.create( regionDO );

    Assert.assertEquals( n + 1, this.regionDAO.count() );

  }

  @Test
  public void testEdit()
  {
    String updated = "Updated name";
    RegionDO regionDO = this.regionDAO.find( 1 );
    for( RegionLanguageDO regionLanguageDO : regionDO.getRegionLanguageDOList() )
    {
      if( regionLanguageDO.getIdLanguage().getIdLanguage().equals( Language.ENGLISH.getId() ) )
      {
        regionLanguageDO.setDsName( updated );
        break;
      }
    }
    this.regionDAO.edit( regionDO );

    regionDO = this.regionDAO.find( 1 );
    boolean isUpdated = false;
    for( RegionLanguageDO regionLanguageDO : regionDO.getRegionLanguageDOList() )
    {
      if( regionLanguageDO.getIdLanguage().getIdLanguage().equals( Language.ENGLISH.getId() ) )
      {
        isUpdated = regionLanguageDO.getDsName().equals( updated );
        break;
      }
    }
    Assert.assertTrue( isUpdated );
  }

  @Test
  public void testRemove()
  {
    int n = this.regionDAO.count();

    RegionDO regionDO = this.regionDAO.find( 1 );
    Assert.assertTrue( regionDO.isFgActive() );
    this.regionDAO.remove( regionDO );

    Assert.assertEquals( n, this.regionDAO.count() );

    regionDO = this.regionDAO.find( 1 );
    Assert.assertNotNull( regionDO );
    Assert.assertFalse( regionDO.isFgActive() );

  }

  @Test
  public void testFind()
  {
    RegionDO regionDO = this.regionDAO.find( 1 );
    Assert.assertNotNull( regionDO );
  }

  @Test
  public void testGetRegionById()
  {
    RegionTO<CatalogTO, CatalogTO> region = this.regionDAO.getRegionById( 1 );
    Assert.assertNotNull( region );
    System.out.println( ToStringBuilder.reflectionToString( region ) );
  }

  @Test
  public void testFindAll()
  {
    List<RegionDO> regions = this.regionDAO.findAll();
    Assert.assertNotNull( regions );
    Assert.assertFalse( regions.isEmpty() );
    Assert.assertEquals( 16, regions.size() );
  }

  @Test
  public void testFindRange()
  {
    List<RegionDO> regions = this.regionDAO.findRange( new int[] { 0, 3 } );
    Assert.assertNotNull( regions );
    Assert.assertFalse( regions.isEmpty() );
    Assert.assertEquals( 3, regions.size() );
  }

  @Test
  public void testCount()
  {
    Assert.assertEquals( 16, this.regionDAO.count() );
  }

  @Test
  public void testDelete()
  {
    int n = this.regionDAO.count();

    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );

    RegionTO<CatalogTO, CatalogTO> region = new RegionTO<CatalogTO, CatalogTO>( catalogTO, new CatalogTO( 1L ) );

    this.regionDAO.delete( region );
    Assert.assertEquals( n, this.regionDAO.count() );

  }

  @Test
  public void testSaveRegionTOOfCatalogTOInteger()
  {
    int n = this.regionDAO.count();
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    catalogTO.setName( "Region new" );

    RegionTO<CatalogTO, CatalogTO> region = new RegionTO<CatalogTO, CatalogTO>( catalogTO, new CatalogTO( 1L ) );

    this.regionDAO.save( region );
    int idRegion = region.getCatalogRegion().getId().intValue();

    Assert.assertEquals( n + 1, this.regionDAO.count() );
    Assert.assertNotNull( this.regionDAO.find( idRegion ) );
  }

  @Test
  public void testSaveRegionWithPerson()
  {
    int n = this.regionDAO.count();
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    catalogTO.setName( "Region new" );

    RegionTO<CatalogTO, CatalogTO> region = new RegionTO<CatalogTO, CatalogTO>( catalogTO, new CatalogTO( 1L ) );

    List<PersonTO> persons = new ArrayList<PersonTO>();
    for( int i = 0; i < 3; i++ )
    {
      int id = i + 1;
      PersonTO person = new PersonTO();
      person.setName( "Name" + id );
      person.setDsLastname( "Lastname" + id );
      person.setDsMotherLastname( "Mother'sLastname" + id );
      CatalogTO email = new CatalogTO();
      email.setName( "email" + id );
      person.setEmails( Arrays.asList( email ) );
      persons.add( person );
    }
    region.setPersons( persons );

    this.regionDAO.save( region );
    int idRegion = region.getCatalogRegion().getId().intValue();

    Assert.assertEquals( n + 1, this.regionDAO.count() );
    Assert.assertNotNull( this.regionDAO.find( idRegion ) );
  }

  @Test
  public void testSaveRegionTOOfCatalogTOIntegerLanguage()
  {
    int n = this.regionDAO.count();
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    catalogTO.setName( "Region new" );

    RegionTO<CatalogTO, CatalogTO> region = new RegionTO<CatalogTO, CatalogTO>( catalogTO, new CatalogTO( 1L ) );

    this.regionDAO.save( region, Language.ENGLISH );
    int idRegion = region.getCatalogRegion().getId().intValue();

    Assert.assertEquals( n + 1, this.regionDAO.count() );
    Assert.assertNotNull( this.regionDAO.find( idRegion ) );

    catalogTO = new CatalogTO();
    catalogTO.setId( Long.valueOf( idRegion ) );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    catalogTO.setName( "Region nueva" );

    region = new RegionTO<CatalogTO, CatalogTO>( catalogTO, new CatalogTO( 1L ) );

    this.regionDAO.save( region, Language.SPANISH );
    idRegion = region.getCatalogRegion().getId().intValue();

    Assert.assertEquals( n + 1, this.regionDAO.count() );
    Assert.assertNotNull( this.regionDAO.find( idRegion ) );

  }

  @Test
  public void testUpdateRegionTOOfCatalogTOInteger()
  {
    String updated = "updated";
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    catalogTO.setName( updated );

    RegionTO<CatalogTO, CatalogTO> region = new RegionTO<CatalogTO, CatalogTO>( catalogTO, new CatalogTO( 1L ) );

    this.regionDAO.update( region );

    RegionDO regionDO = this.regionDAO.find( 1 );
    boolean isUpdated = false;
    for( RegionLanguageDO regionLanguageDO : regionDO.getRegionLanguageDOList() )
    {
      if( regionLanguageDO.getIdLanguage().getIdLanguage().equals( Language.ENGLISH.getId() ) )
      {
        isUpdated = regionLanguageDO.getDsName().equals( updated );
        break;
      }
    }
    Assert.assertTrue( isUpdated );

  }

  @Test
  public void testUpdateRegionWithPersons_addNewPerson()
  {
    String updated = "updated";
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    catalogTO.setName( updated );

    RegionTO<CatalogTO, CatalogTO> region = this.regionDAO.getRegionById( 1 );
    int n = region.getPersons().size();
    PersonTO person = new PersonTO();
    person.setName( "name" );
    person.setDsLastname( "lastname" );
    person.setDsMotherLastname( "motherLastname" );
    CatalogTO email = new CatalogTO( null, "email@mail.com" );
    person.setEmails( Arrays.asList( email ) );
    region.getPersons().add( person );

    this.regionDAO.update( region );

    RegionDO regionDO = this.regionDAO.find( 1 );
    Assert.assertEquals( n + 1, regionDO.getPersonDOList().size() );

  }

  @Test
  public void testUpdateRegionWithPersons_RemovePerson()
  {
    String updated = "updated";
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    catalogTO.setName( updated );

    RegionTO<CatalogTO, CatalogTO> region = this.regionDAO.getRegionById( 1 );
    int n = region.getPersons().size();
    region.getPersons().remove( 1 );

    this.regionDAO.update( region );

    RegionDO regionDO = this.regionDAO.find( 1 );
    Assert.assertEquals( n - 1, regionDO.getPersonDOList().size() );
    this.regionDAO.flush();
  }

  @Test
  public void testUpdateRegionWithPersons_RemoveAllPersonAddNewPerson()
  {
    String updated = "updated";
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    catalogTO.setName( updated );

    RegionTO<CatalogTO, CatalogTO> region = this.regionDAO.getRegionById( 1 );
    region.setPersons( new ArrayList<PersonTO>() );
    PersonTO person = new PersonTO();
    person.setName( "name" );
    person.setDsLastname( "lastname" );
    person.setDsMotherLastname( "motherLastname" );
    CatalogTO email = new CatalogTO( null, "email@mail.com" );
    person.setEmails( Arrays.asList( email ) );
    region.getPersons().add( person );

    this.regionDAO.update( region );

    RegionDO regionDO = this.regionDAO.find( 1 );
    Assert.assertEquals( 1, regionDO.getPersonDOList().size() );
    this.regionDAO.flush();
  }

  @Test
  public void testUpdateRegionTOOfCatalogTOIntegerLanguage()
  {
    String updated = "actualizado";
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setTimestamp( new Date() );
    catalogTO.setUserId( 1L );
    catalogTO.setName( updated );

    RegionTO<CatalogTO, CatalogTO> region = new RegionTO<CatalogTO, CatalogTO>( catalogTO, new CatalogTO( 1L ) );

    this.regionDAO.update( region, Language.SPANISH );

    RegionDO regionDO = this.regionDAO.find( 1 );
    boolean isUpdated = false;
    for( RegionLanguageDO regionLanguageDO : regionDO.getRegionLanguageDOList() )
    {
      if( regionLanguageDO.getIdLanguage().getIdLanguage().equals( Language.SPANISH.getId() ) )
      {
        isUpdated = regionLanguageDO.getDsName().equals( updated );
        break;
      }
    }
    Assert.assertTrue( isUpdated );
  }

  @Test
  public void testFindAllByPaging()
  {
    int page = 1;
    int pageSize = 5;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );
    // Assert.assertEquals(pageSize, response.getElements().size());

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_SortByRegionIdASC()
  {
    int page = 0;
    int pageSize = 20;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( RegionQuery.REGION_ID );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_SortByRegionIdDESC()
  {
    int page = 0;
    int pageSize = 20;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( RegionQuery.REGION_ID );
    pagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_SortByRegionLanguageIdASC()
  {
    int page = 0;
    int pageSize = 20;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( RegionQuery.REGION_LANGUAGE_ID );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_SortByRegionLanguageIdDESC()
  {
    int page = 0;
    int pageSize = 20;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( RegionQuery.REGION_LANGUAGE_ID );
    pagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_SortByRegionNameASC()
  {
    int page = 0;
    int pageSize = 20;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( RegionQuery.REGION_NAME );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_SortByRegionNameDESC()
  {
    int page = 0;
    int pageSize = 20;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( RegionQuery.REGION_NAME );
    pagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_SortByTerritoryIdASC()
  {
    int page = 0;
    int pageSize = 20;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( RegionQuery.TERRITORY_ID );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_SortByTerritoryIdDESC()
  {
    int page = 0;
    int pageSize = 20;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( RegionQuery.TERRITORY_ID );
    pagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_SortByTerritoryNameASC()
  {
    int page = 0;
    int pageSize = 20;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( RegionQuery.TERRITORY_NAME );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_SortByTerritoryNameDESC()
  {
    int page = 0;
    int pageSize = 20;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( RegionQuery.TERRITORY_NAME );
    pagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_FilterByRegionId()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( RegionQuery.REGION_ID, 1 );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 1, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_FilterByRegionIdLangSP()
  {
    int page = 0;
    int pageSize = 5;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( RegionQuery.REGION_ID, 1 );
    pagingRequestTO.setLanguage( Language.SPANISH );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 1, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_FilterByRegionName()
  {
    int page = 0;
    int pageSize = 10;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( RegionQuery.REGION_NAME, "1" );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 1, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_FilterByRegionNameLangSP()
  {
    int page = 0;
    int pageSize = 10;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( RegionQuery.REGION_NAME, "1" );
    pagingRequestTO.setLanguage( Language.SPANISH );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 1, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_FilterByTerritoryId()
  {
    int page = 0;
    int pageSize = 10;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( RegionQuery.TERRITORY_ID, 1 );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_FilterByTerritoryIdLangSP()
  {
    int page = 0;
    int pageSize = 10;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( RegionQuery.TERRITORY_ID, 1 );
    pagingRequestTO.setLanguage( Language.SPANISH );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_FilterByTerritoryName()
  {
    int page = 0;
    int pageSize = 10;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( RegionQuery.TERRITORY_NAME, "Terr" );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_FilterByTerritoryNameLangSP()
  {
    int page = 0;
    int pageSize = 10;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( RegionQuery.TERRITORY_NAME, "Terr" );
    pagingRequestTO.setLanguage( Language.SPANISH );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }

  }

  @Test
  public void testFindAllByPaging_aa()
  {
    int page = 3;
    int pageSize = 10;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.setLanguage( Language.SPANISH );

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = this.regionDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 3, response.getTotalCount() );
    Assert.assertEquals( 0, response.getElements().size() );

    for( RegionTO<CatalogTO, CatalogTO> region : response.getElements() )
    {
      System.out.println( "regionId: " + region.getCatalogRegion().getId() + ", territoryId: "
          + region.getIdTerritory() + ", regionName: " + region.getCatalogRegion().getName() );
    }
  }

  @Test
  public void testFindActiveRegions()
  {
    List<RegionDO> regionDOs = regionDAO.findActiveRegions();
    Assert.assertNotNull( regionDOs );
    Assert.assertEquals( 16, regionDOs.size() );
  }
  
  @Test
  public void test_findByDsNameActive()
  {
    List<RegionDO> regionsDOs=this.regionDAO.findByDsNameActive( "Region 1 ENG" );
    Assert.assertNotNull( regionsDOs );
    for(RegionDO regionDO:regionsDOs)
    {
      Assert.assertNotNull( regionDO );
    }
  }

}
