package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.query.TheaterQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.model.CityDO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.StateDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.TheaterLanguageDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Test;

/**
 * Clase de pruebas de TheaterDAO
 * 
 * @author agustin.ramirez
 */
public class TheaterDAOTest extends AbstractDBEJBTestUnit
{
  /**
   * DAO
   */
  private TheaterDAO theaterDAO;

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit#setUp()
   */
  public void setUp()
  {
    // instanciar el servicio
    theaterDAO = new TheaterDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( theaterDAO );

  }

  /**
   * Test Count
   */
  @Test
  public void testCount()
  {
    int n = this.theaterDAO.count();
    Assert.assertEquals( 6, n );
  }

  /**
   * Test find
   */
  @Test
  public void testFind()
  {
    TheaterDO theaterDO = this.theaterDAO.find( 1 );
    Assert.assertNotNull( theaterDO );
  }

  /**
   * Test listAll
   */
  @Test
  public void testFindAll()
  {
    List<TheaterDO> listAll = null;
    listAll = this.theaterDAO.findAll();
    Assert.assertNotNull( listAll );
    Assert.assertEquals( 6, listAll.size() );
  }

  /**
   * Test Range
   */
  @Test
  public void testRange()
  {
    List<TheaterDO> listAll = null;
    listAll = this.theaterDAO.findRange( new int[] { 0, 2 } );
    Assert.assertNotNull( listAll );
    Assert.assertEquals( 2, listAll.size() );
  }

  /**
   * Test Create
   */
  @Test
  public void testCreate()
  {
    int n = this.theaterDAO.count();
    TheaterDO newTheaterDO = new TheaterDO();
    newTheaterDO.setDsTelephone( "45567899" );
    newTheaterDO.setDtLastModification( new Date() );
    newTheaterDO.setFgActive( Boolean.TRUE );
    newTheaterDO.setIdCity( new CityDO( 1 ) );
    newTheaterDO.setIdLastUserModifier( 1 );
    newTheaterDO.setIdRegion( new RegionDO( 1 ) );
    newTheaterDO.setIdState( new StateDO( 1 ) );
    TheaterLanguageDO languageDO = new TheaterLanguageDO();
    languageDO.setDsName( "Test Theater" );
    languageDO.setIdLanguage( new LanguageDO( 1 ) );
    languageDO.setIdTheater( newTheaterDO );
    newTheaterDO.setTheaterLanguageDOList( new ArrayList<TheaterLanguageDO>() );
    newTheaterDO.getTheaterLanguageDOList().add( languageDO );
    this.theaterDAO.create( newTheaterDO );
    Assert.assertEquals( n + 1, theaterDAO.count() );

  }

  /**
   * Test Edit
   */
  @Test
  public void testEdit()
  {
    TheaterDO theaterToUpdate = new TheaterDO();
    theaterToUpdate.setDsTelephone( "45567899" );
    theaterToUpdate.setDtLastModification( new Date() );
    theaterToUpdate.setFgActive( Boolean.TRUE );
    theaterToUpdate.setIdCity( new CityDO( 1 ) );
    theaterToUpdate.setIdLastUserModifier( 1 );
    theaterToUpdate.setIdRegion( new RegionDO( 1 ) );
    theaterToUpdate.setIdState( new StateDO( 1 ) );
    theaterToUpdate.setIdTheater( 1 );
    this.theaterDAO.edit( theaterToUpdate );
    TheaterDO theaterDO = this.theaterDAO.find( 1 );
    Assert.assertEquals( theaterToUpdate.getDsTelephone(), theaterDO.getDsTelephone() );

  }

  /**
   * Test Remove
   */
  @Test
  public void testRemove()
  {
    int n = this.theaterDAO.count();
    TheaterDO theaterToDelete = this.theaterDAO.find( 1 );
    this.theaterDAO.remove( theaterToDelete );
    Assert.assertEquals( n, this.theaterDAO.count() );

  }

  /**
   * Test Save
   */
  @Test
  public void testSave()
  {
    TheaterTO theaterTO = new TheaterTO();
    theaterTO.setNuTheater( 11 );
    theaterTO.setCity( new CatalogTO( 1L ) );
    theaterTO.setDsTelephone( "3456789011" );
    theaterTO.setFgActive( Boolean.TRUE );
    theaterTO.setName( "Theater Test" );
    theaterTO.setRegion( new RegionTO<CatalogTO, CatalogTO>( new CatalogTO( 1L ), new CatalogTO( 1L ) ) );
    theaterTO.setState( new StateTO<CatalogTO, Integer>( new CatalogTO( 1L ), 1 ) );
    theaterTO.setTimestamp( new Date() );
    theaterTO.setUserId( 1L );
    theaterTO.setEmail( new CatalogTO() );
    theaterTO.getEmail().setName( "theater@email.com" );
    this.theaterDAO.save( theaterTO );
    Assert.assertNotNull( theaterTO.getId() );
  }

  /**
   * Test Save Language
   */
  @Test
  public void testSaveLanguage()
  {
    TheaterTO theaterTO = new TheaterTO();
    theaterTO.setNuTheater( 11 );
    theaterTO.setCity( new CatalogTO( 1L ) );
    theaterTO.setDsTelephone( "3456789011" );
    theaterTO.setFgActive( Boolean.TRUE );
    theaterTO.setName( "Theater Español" );
    theaterTO.setRegion( new RegionTO<CatalogTO, CatalogTO>( new CatalogTO( 1L ), new CatalogTO( 1L ) ) );
    theaterTO.setState( new StateTO<CatalogTO, Integer>( new CatalogTO( 1L ), 1 ) );
    theaterTO.setTimestamp( new Date() );
    theaterTO.setUserId( 1L );
    this.theaterDAO.save( theaterTO, Language.SPANISH );
    Assert.assertNotNull( theaterTO.getId() );
    TheaterDO theater = theaterDAO.find( theaterTO.getId().intValue() );
    Assert.assertEquals( 1, theater.getTheaterLanguageDOList().size() );
    Assert.assertTrue( Language.SPANISH.getId() == theater.getTheaterLanguageDOList().get( 0 ).getIdLanguage()
        .getIdLanguage() );

  }

  /**
   * Test Update
   */
  @Test
  public void testUpdate()
  {
    TheaterTO theaterTO = new TheaterTO();
    theaterTO.setNuTheater( 11 );
    theaterTO.setCity( new CatalogTO( 1L ) );
    theaterTO.setDsTelephone( "3456789011" );
    theaterTO.setFgActive( Boolean.TRUE );
    theaterTO.setName( "Theater Test" );
    theaterTO.setRegion( new RegionTO<CatalogTO, CatalogTO>( new CatalogTO( 1L ), new CatalogTO( 1L ) ) );
    theaterTO.setState( new StateTO<CatalogTO, Integer>( new CatalogTO( 1L ), 1 ) );
    theaterTO.setTimestamp( new Date() );
    theaterTO.setUserId( 1L );
    theaterTO.setId( 1L );
    theaterTO.setEmail( new CatalogTO( 10L, "theater_new@email.com" ) );
    this.theaterDAO.update( theaterTO );
    this.theaterDAO.flush();
    TheaterDO theaterDO = this.theaterDAO.find( 1 );
    Assert.assertEquals( theaterTO.getDsTelephone(), theaterDO.getDsTelephone() );
  }

  /**
   * Test Update
   */
  @Test
  public void testUpdate_newEmail()
  {
    TheaterTO theaterTO = new TheaterTO();
    theaterTO.setNuTheater( 11 );
    theaterTO.setCity( new CatalogTO( 1L ) );
    theaterTO.setDsTelephone( "3456789011" );
    theaterTO.setFgActive( Boolean.TRUE );
    theaterTO.setName( "Theater Test" );
    theaterTO.setRegion( new RegionTO<CatalogTO, CatalogTO>( new CatalogTO( 1L ), new CatalogTO( 1L ) ) );
    theaterTO.setState( new StateTO<CatalogTO, Integer>( new CatalogTO( 1L ), 1 ) );
    theaterTO.setTimestamp( new Date() );
    theaterTO.setUserId( 1L );
    theaterTO.setId( 4L );
    theaterTO.setEmail( new CatalogTO() );
    theaterTO.getEmail().setName( "theater@email.com" );
    this.theaterDAO.update( theaterTO );
    this.theaterDAO.flush();
    TheaterDO theaterDO = this.theaterDAO.find( 4 );
    Assert.assertEquals( theaterTO.getDsTelephone(), theaterDO.getDsTelephone() );
  }

  @Test(expected = DigitalBookingException.class)
  public void testUpdate_CityNonExistent()
  {
    TheaterTO theaterTO = new TheaterTO();
    theaterTO.setNuTheater( 11 );
    theaterTO.setCity( new CatalogTO( 100L ) );
    theaterTO.setDsTelephone( "3456789011" );
    theaterTO.setFgActive( Boolean.TRUE );
    theaterTO.setName( "Theater Test" );
    theaterTO.setRegion( new RegionTO<CatalogTO, CatalogTO>( new CatalogTO( 1L ), new CatalogTO( 1L ) ) );
    theaterTO.setState( new StateTO<CatalogTO, Integer>( new CatalogTO( 1L ), 1 ) );
    theaterTO.setTimestamp( new Date() );
    theaterTO.setUserId( 1L );
    theaterTO.setId( 1L );
    this.theaterDAO.update( theaterTO );
    this.theaterDAO.flush();
    TheaterDO theaterDO = this.theaterDAO.find( 1 );
    Assert.assertEquals( theaterTO.getDsTelephone(), theaterDO.getDsTelephone() );
  }

  @Test(expected = DigitalBookingException.class)
  public void testUpdate_RegionNonExistent()
  {
    TheaterTO theaterTO = new TheaterTO();
    theaterTO.setNuTheater( 11 );
    theaterTO.setCity( new CatalogTO( 1L ) );
    theaterTO.setDsTelephone( "3456789011" );
    theaterTO.setFgActive( Boolean.TRUE );
    theaterTO.setName( "Theater Test" );
    theaterTO.setRegion( new RegionTO<CatalogTO, CatalogTO>( new CatalogTO( 100L ), new CatalogTO( 1L ) ) );
    theaterTO.setState( new StateTO<CatalogTO, Integer>( new CatalogTO( 1L ), 1 ) );
    theaterTO.setTimestamp( new Date() );
    theaterTO.setUserId( 1L );
    theaterTO.setId( 1L );
    this.theaterDAO.update( theaterTO );
    this.theaterDAO.flush();
    TheaterDO theaterDO = this.theaterDAO.find( 1 );
    Assert.assertEquals( theaterTO.getDsTelephone(), theaterDO.getDsTelephone() );
  }

  @Test(expected = DigitalBookingException.class)
  public void testUpdate_StateNonExistent()
  {
    TheaterTO theaterTO = new TheaterTO();
    theaterTO.setNuTheater( 11 );
    theaterTO.setCity( new CatalogTO( 1L ) );
    theaterTO.setDsTelephone( "3456789011" );
    theaterTO.setFgActive( Boolean.TRUE );
    theaterTO.setName( "Theater Test" );
    theaterTO.setRegion( new RegionTO<CatalogTO, CatalogTO>( new CatalogTO( 1L ), new CatalogTO( 1L ) ) );
    theaterTO.setState( new StateTO<CatalogTO, Integer>( new CatalogTO( 100L ), 1 ) );
    theaterTO.setTimestamp( new Date() );
    theaterTO.setUserId( 1L );
    theaterTO.setId( 1L );
    this.theaterDAO.update( theaterTO );
    this.theaterDAO.flush();
    TheaterDO theaterDO = this.theaterDAO.find( 1 );
    Assert.assertEquals( theaterTO.getDsTelephone(), theaterDO.getDsTelephone() );
  }

  /**
   * Test Update Language
   */
  @Test
  public void testUpdateLanguage()
  {
    TheaterTO theaterTO = new TheaterTO();
    theaterTO.setNuTheater( 11 );
    theaterTO.setCity( new CatalogTO( 1L ) );
    theaterTO.setDsTelephone( "3456789011" );
    theaterTO.setFgActive( Boolean.TRUE );
    theaterTO.setName( "Theater Test Spanish " );
    theaterTO.setRegion( new RegionTO<CatalogTO, CatalogTO>( new CatalogTO( 1L ), new CatalogTO( 1L ) ) );
    theaterTO.setState( new StateTO<CatalogTO, Integer>( new CatalogTO( 1L ), 1 ) );
    theaterTO.setTimestamp( new Date() );
    theaterTO.setUserId( 1L );
    theaterTO.setId( 1L );
    this.theaterDAO.update( theaterTO, Language.SPANISH );
    TheaterDO theaterDO = this.theaterDAO.find( 1 );
    Assert.assertEquals( theaterTO.getDsTelephone(), theaterDO.getDsTelephone() );
    for( TheaterLanguageDO lang : theaterDO.getTheaterLanguageDOList() )
    {
      System.out.print( "Theater Language =>"
          + ReflectionToStringBuilder.toString( lang, ToStringStyle.MULTI_LINE_STYLE ) );
    }
  }

  /**
   * Test Delete
   */
  @Test
  public void testDelete()
  {
    TheaterTO theaterTO = new TheaterTO();
    theaterTO.setCity( new CatalogTO( 1L ) );
    theaterTO.setDsTelephone( "3456789011" );
    theaterTO.setFgActive( Boolean.TRUE );
    theaterTO.setName( "Theater Test Spanish " );
    theaterTO.setRegion( new RegionTO<CatalogTO, CatalogTO>( new CatalogTO( 1L ), new CatalogTO( 1L ) ) );
    theaterTO.setState( new StateTO<CatalogTO, Integer>( new CatalogTO( 1L ), 1 ) );
    theaterTO.setTimestamp( new Date() );
    theaterTO.setUserId( 1L );
    theaterTO.setId( 1L );
    this.theaterDAO.delete( theaterTO );
    TheaterDO theaterDO = this.theaterDAO.find( 1 );
    Assert.assertFalse( theaterDO.isFgActive() );
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
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );

    PagingResponseTO<TheaterTO> response = this.theaterDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 2, response.getTotalCount() );

    for( TheaterTO t : response.getElements() )
    {
      System.out.println( t.getName() );
    }

    for( TheaterTO screenTO : response.getElements() )
    {
      System.out.println( "Theater =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Tests Find All Without Paging
   */
  @Test
  public void testFindAllWithoutPaging()
  {

    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setNeedsPaging( Boolean.FALSE );

    PagingResponseTO<TheaterTO> response = this.theaterDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 2, response.getTotalCount() );

    for( TheaterTO screenTO : response.getElements() )
    {
      System.out.println( "Theater =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Tests Find All By Paging
   */
  @Test
  public void testFindAllByPaging_sortDesc()
  {
    int page = 0;
    int pageSize = 30;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( TheaterQuery.THEATER_ID );
    pagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<TheaterTO> response = this.theaterDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 2, response.getTotalCount() );

    for( TheaterTO screenTO : response.getElements() )
    {
      System.out.println( "Theater =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Tests Find All By Paging Language Spanish
   */
  @Test
  public void testFindAllByPaging_LanguageESP()
  {
    int page = 0;
    int pageSize = 3;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setLanguage( Language.SPANISH );

    PagingResponseTO<TheaterTO> response = this.theaterDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 2, response.getTotalCount() );

    for( TheaterTO screenTO : response.getElements() )
    {
      System.out.println( "Theater =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Tests Find All By Paging Language Spanish
   */
  @Test
  public void testFindAllByPaging_TheaterID()
  {
    int page = 0;
    int pageSize = 3;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( TheaterQuery.THEATER_ID, 1L );

    PagingResponseTO<TheaterTO> response = this.theaterDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 1, response.getTotalCount() );

    for( TheaterTO screenTO : response.getElements() )
    {
      System.out.println( "Theater =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Tests Find All By Paging IDCity
   */
  @Test
  public void testFindAllByPaging_IdCity()
  {
    int page = 0;
    int pageSize = 3;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( TheaterQuery.ID_CITY, 1L );

    PagingResponseTO<TheaterTO> response = this.theaterDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 2, response.getTotalCount() );

    for( TheaterTO screenTO : response.getElements() )
    {
      System.out.println( "Theater =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Tests Find All By Paging IDRegion
   */
  @Test
  public void testFindAllByPaging_IdRegion()
  {
    int page = 0;
    int pageSize = 3;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( TheaterQuery.ID_REGION, 1L );

    PagingResponseTO<TheaterTO> response = this.theaterDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 2, response.getTotalCount() );

    for( TheaterTO screenTO : response.getElements() )
    {
      System.out.println( "Theater =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Tests Find All By Paging IDRegion
   */
  @Test
  public void testFindAllByPaging_IdState()
  {
    int page = 0;
    int pageSize = 3;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( TheaterQuery.ID_STATE, 1L );

    PagingResponseTO<TheaterTO> response = this.theaterDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 2, response.getTotalCount() );

    for( TheaterTO screenTO : response.getElements() )
    {
      System.out.println( "Theater =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Tests Find All By Paging Theater Name
   */
  @Test
  public void testFindAllByPaging_TheaterName()
  {
    int page = 0;
    int pageSize = 3;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( TheaterQuery.THEATER_NAME, "THE 1 ENG" );

    PagingResponseTO<TheaterTO> response = this.theaterDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 1, response.getTotalCount() );

    for( TheaterTO screenTO : response.getElements() )
    {
      System.out.println( "Theater =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  /**
   * Tests Find All By Paging TheterName Spanish
   */
  @Test
  public void testFindAllByPaging_TheaterName_spanish()
  {
    int page = 0;
    int pageSize = 3;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( TheaterQuery.THEATER_NAME, "THEATER 1 ESP" );
    pagingRequestTO.setLanguage( Language.SPANISH );

    PagingResponseTO<TheaterTO> response = this.theaterDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 1, response.getTotalCount() );

    for( TheaterTO screenTO : response.getElements() )
    {
      System.out.println( "Theater =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  @Test
  public void testFindByIdVistaAndActive()
  {
    List<TheaterDO> theaters = this.theaterDAO.findByIdVistaAndActive( "1001" );
    Assert.assertNotNull( theaters );
    Assert.assertEquals( 1, theaters.size() );
  }

  @Test
  public void testFindByTheaterName()
  {
    String dsName = "THEATER 1 ENG";
    List<TheaterDO> theaters = this.theaterDAO.findByTheaterName( dsName );
    Assert.assertNotNull( theaters );
    Assert.assertEquals( 1, theaters.size() );
  }

  @Test
  public void testFindByTheaterNameLangSP()
  {
    String dsName = "THEATER 1 ESP";
    List<TheaterDO> theaters = this.theaterDAO.findByTheaterName( dsName, Language.SPANISH );
    Assert.assertNotNull( theaters );
    Assert.assertEquals( 1, theaters.size() );
  }

  /**
   * Tests Find All By Paging
   */
  @Test
  public void testFindAllByPaging_OrderByRegionNameTheaterName()
  {
    int page = 0;
    int pageSize = 3;
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( 1L );
    pagingRequestTO.setPage( page );
    pagingRequestTO.setPageSize( pageSize );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( TheaterQuery.REGION_NAME );
    pagingRequestTO.getSort().add( TheaterQuery.THEATER_NAME );
    pagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<TheaterTO> response = this.theaterDAO.findAllByPaging( pagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertEquals( 2, response.getTotalCount() );

    for( TheaterTO screenTO : response.getElements() )
    {
      System.out.println( "Theater =>" + ReflectionToStringBuilder.toString( screenTO ) );
    }

  }

  @Test
  public void testFindByRegionId()
  {
    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setUserId( 1L );

    List<TheaterTO> theaters = this.theaterDAO.findByRegionId( catalogTO );
    Assert.assertNotNull( theaters );
    Assert.assertFalse( theaters.isEmpty() );
    for( TheaterTO theaterTO : theaters )
    {
      System.out.println( theaterTO );
    }
  }

}
