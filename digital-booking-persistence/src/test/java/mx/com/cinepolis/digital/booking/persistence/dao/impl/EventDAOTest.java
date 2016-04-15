package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.EventType;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.query.EventQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.EventMovieDO;
import mx.com.cinepolis.digital.booking.model.EventTypeDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author jcarbajal
 */
public class EventDAOTest extends AbstractDBEJBTestUnit
{

  private EventDAO eventDAO;
  private CategoryDAO categoryDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    eventDAO = new EventDAOImpl();
    categoryDAO = new CategoryDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( eventDAO );
    connect( categoryDAO );
  }

  @Test
  public void testFindAllByPaging()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );
  }

  @Test
  public void testFindAllByPaging_SortByEventIdASC()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagePagingRequestTO.getSort().add( EventQuery.EVENT_ID );
    pagePagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );
  }

  @Test
  public void testFindAllByPaging_SortByEventIdDESC()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagePagingRequestTO.getSort().add( EventQuery.EVENT_ID );
    pagePagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );
  }

  @Test
  public void testFindAllByPaging_SortByEventNameASC()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagePagingRequestTO.getSort().add( EventQuery.EVENT_NAME );
    pagePagingRequestTO.setSortOrder( SortOrder.ASCENDING );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );
  }

  @Test
  public void testFindAllByPaging_SortByEventNameDESC()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagePagingRequestTO.getSort().add( EventQuery.EVENT_NAME );
    pagePagingRequestTO.setSortOrder( SortOrder.DESCENDING );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );
  }

  /**
   * Test de busqueda filtrando por premiere
   */
  @Test
  public void testFindAllByPaging_FilterByPremiere()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagePagingRequestTO.getFilters().put( EventQuery.EVENT_PREMIERE, false );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() < pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Total count is " + response.getTotalCount() );
  }

  /**
   * Author Test de busqueda filtrando por preview
   */
  @Test
  public void testFindAllByPaging_FilterByPreview()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagePagingRequestTO.getFilters().put( EventQuery.EVENT_PRERELEASE, true );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );
    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() < pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Total count is " + response.getTotalCount() );
  }

  @Test
  public void testFindAllByPaging_FilterByEventId()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagePagingRequestTO.getFilters().put( EventQuery.EVENT_ID, 1L );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() < pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );
  }

  @Test
  public void testFindAllByPaging_FilterByEventName()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagePagingRequestTO.getFilters().put( EventQuery.EVENT_NAME, "300" );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() < pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );
  }

  @Test
  public void testFindAllByPaging_FilterByEventTypeId()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagePagingRequestTO.getFilters().put( EventQuery.EVENT_TYPE_ID, EventType.MOVIE.getId() );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );
  }

  @Test
  public void testFindAllByPaging_FilterByEventIsActive()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagePagingRequestTO.getFilters().put( EventQuery.EVENT_ACTIVE, true );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );
  }

  @Test
  public void testFindAllByPaging_FilterByEventLanguage()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setLanguage( Language.SPANISH );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );
  }

  @Test
  public void testSave()
  {
    EventMovieTO eventTO = new EventMovieTO();
    eventTO.setDsCountry( "MEXICO" );

    eventTO.setSoundFormats( Arrays.asList( new CatalogTO( 1L ) ) );
    eventTO.setMovieFormats( Arrays.asList( new CatalogTO( 4L ) ) );
    eventTO.setIdVista( "129" );
    eventTO.setCodeDBS( "00011" );
    eventTO.setQtCopy( 1 );

    DistributorTO distributorTO = new DistributorTO();
    distributorTO.setId( 1L );

    eventTO.setDistributor( distributorTO );
    // eventTO.set
    eventTO.setDsActor( "dsActor" );
    eventTO.setDsDirector( "dsDirector" );
    eventTO.setDsEventName( "dsEventName" );
    eventTO.setDsGenre( "dsGenre" );
    eventTO.setDsRating( "dsRating" );
    eventTO.setDsSynopsis( "dsSynopsis" );
    eventTO.setDsUrl( "dsUrl" );
    eventTO.setPrerelease( true );
    eventTO.setDtRelease( new Date() );
    eventTO.setDuration( 129 );
    eventTO.setMovieLanguage( new CatalogTO( 1L ) );
    eventTO.setTimestamp( new Date() );
    eventTO.setUserId( 1L );

    this.eventDAO.save( eventTO, 1L );

    Assert.assertNotNull( eventTO.getIdEvent() );
    EventDO eventDO = this.eventDAO.find( eventTO.getIdEvent().longValue() );
    Assert.assertNotNull( eventDO );
    Assert.assertTrue( CollectionUtils.isNotEmpty( eventDO.getCategoryDOList() ) );

  }

  @Test
  public void testUpdate()
  {

    EventTO eventTO = this.eventDAO.getEvent( 1L );
    System.out.println( eventTO.getMovieFormats() );
    System.out.println( eventTO.getSoundFormats() );

    ((EventMovieTO) eventTO).setDsGenre( "updated" );
    ((EventMovieTO) eventTO).setDsCountry( "E.U.A." );
    ((EventMovieTO) eventTO).setDuration( 112 );

    eventTO.setIdVista( "1" );
    eventTO.setDsEventName( "updated DsEventName" );

    eventTO.setSoundFormats( Arrays.asList( new CatalogTO( 1L ) ) );
    eventTO.setMovieFormats( Arrays.asList( new CatalogTO( 6L ) ) );
    eventTO.setCodeDBS( "111999" );
    eventTO.setQtCopy( 11 );
    eventTO.setPrerelease( true );

    eventTO.setTimestamp( new Date() );
    eventTO.setUserId( 2L );
    this.eventDAO.update( eventTO, 1L );
    this.eventDAO.flush();

    eventTO = this.eventDAO.getEvent( 1L );

    Assert.assertNotNull( eventTO );
    Assert.assertEquals( "updated DsEventName", eventTO.getDsEventName() );
    Assert.assertEquals( "updated", ((EventMovieTO) eventTO).getDsGenre() );

    System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
    for( CatalogTO to : eventTO.getSoundFormats() )
    {
      System.out.println( to );
    }
    for( CatalogTO to : eventTO.getMovieFormats() )
    {
      System.out.println( to );
    }

  }

  @Test(expected = DigitalBookingException.class)
  public void testUpdate_CategoryNonExistent()
  {

    EventTO eventTO = this.eventDAO.getEvent( 1L );

    ((EventMovieTO) eventTO).setDsGenre( "updated" );
    ((EventMovieTO) eventTO).setDsCountry( "E.U.A." );
    ((EventMovieTO) eventTO).setDuration( 112 );

    eventTO.setIdVista( "1" );
    eventTO.setDsEventName( "updated DsEventName" );

    eventTO.getSoundFormats().add( new CatalogTO( 50L ) );
    eventTO.setCodeDBS( "111999" );
    eventTO.setQtCopy( 11 );

    eventTO.setTimestamp( new Date() );
    eventTO.setUserId( 2L );
    this.eventDAO.update( eventTO, 1L );

  }

  @Test(expected = DigitalBookingException.class)
  public void testUpdate_DistributorNonExistent()
  {

    EventTO eventTO = this.eventDAO.getEvent( 1L );

    ((EventMovieTO) eventTO).setDsGenre( "updated" );
    ((EventMovieTO) eventTO).setDsCountry( "E.U.A." );
    ((EventMovieTO) eventTO).setDuration( 112 );
    DistributorTO distributorTO = new DistributorTO();
    distributorTO.setId( 100L );
    ((EventMovieTO) eventTO).setDistributor( distributorTO );

    eventTO.setTimestamp( new Date() );
    eventTO.setUserId( 2L );
    eventTO.setPrerelease( true );
    eventTO.setPremiere( true );
    this.eventDAO.update( eventTO, 1L );

    eventTO = this.eventDAO.getEvent( 1L );

  }

  @Test(expected = DigitalBookingException.class)
  public void testDelete()
  {
    int n = this.eventDAO.count();
    EventTO eventTO = new EventMovieTO();
    eventTO.setIdEvent( 1L );
    eventTO.setTimestamp( new Date() );
    eventTO.setUserId( 1L );
    eventTO.setPrerelease( true );
    this.eventDAO.delete( eventTO );
    Assert.assertEquals( n, this.eventDAO.count() );
    EventDO eventDO = this.eventDAO.find( 1L );
    Assert.assertNotNull( eventDO );
  }

  @Test
  public void testCreateSpecialEvent()
  {
    int n = this.eventDAO.count();
    EventDO eventDO = new EventDO();
    eventDO.setDtLastModification( new Date() );
    eventDO.setIdLastUserModifier( 1 );
    eventDO.setDsName( "DSNameSpecialEvent" );

    eventDO.setIdEventType( new EventTypeDO( EventType.SPECIAL_EVENT.getId() ) );
    eventDO.setQtDuration( 180 );
    eventDO.setQtCopy( 5 );
    eventDO.setIdVista( "1" );
    eventDO.setFgPrerelease( true );
    eventDO.setFgPremiere( true );
    eventDO.setDsCodeDbs( "111111" );
    CategoryDO ct1 = categoryDAO.find( 1 );
    CategoryDO ct2 = categoryDAO.find( 4 );
    eventDO.setCategoryDOList( Arrays.asList( ct1, ct2 ) );
    ct1.getEventDOList().add( eventDO );
    ct2.getEventDOList().add( eventDO );

    EventMovieDO eventMovieDO = new EventMovieDO();
    eventMovieDO.setIdEvent( eventDO );
    eventDO.setEventMovieDOList( Arrays.asList( eventMovieDO ) );

    eventMovieDO.setIdDistributor( new DistributorDO( 1 ) );
    eventMovieDO.setDtRelease( new Date() );
    eventMovieDO.setDsDirector( "dsDirector" );
    eventMovieDO.setDsSynopsis( "dsSynopsis" );
    eventMovieDO.setDsRating( "dsRating" );
    eventMovieDO.setDsScript( "dsScript" );
    eventMovieDO.setDsActor( "dsActor" );
    eventMovieDO.setDsGenre( "dsGenre" );
    eventMovieDO.setDsUrl( "dsUrl" );

    this.eventDAO.create( eventDO );

    Assert.assertEquals( n + 1, this.eventDAO.count() );
  }

  @Test
  public void testCreate()
  {
    int n = this.eventDAO.count();
    EventDO eventDO = new EventDO();
    eventDO.setDtLastModification( new Date() );
    eventDO.setIdLastUserModifier( 1 );
    eventDO.setDsName( "dsName" );

    eventDO.setIdEventType( new EventTypeDO( EventType.MOVIE.getId() ) );
    eventDO.setQtDuration( 100 );
    eventDO.setQtCopy( 10 );
    eventDO.setIdVista( "1" );
    eventDO.setFgPrerelease( true );
    eventDO.setFgPremiere( true );
    eventDO.setDsCodeDbs( "111111" );
    CategoryDO ct1 = categoryDAO.find( 1 );
    CategoryDO ct2 = categoryDAO.find( 4 );
    eventDO.setCategoryDOList( Arrays.asList( ct1, ct2 ) );
    ct1.getEventDOList().add( eventDO );
    ct2.getEventDOList().add( eventDO );

    EventMovieDO eventMovieDO = new EventMovieDO();
    eventMovieDO.setIdEvent( eventDO );
    eventDO.setEventMovieDOList( Arrays.asList( eventMovieDO ) );

    eventMovieDO.setIdDistributor( new DistributorDO( 1 ) );
    eventMovieDO.setDtRelease( new Date() );
    eventMovieDO.setDsDirector( "dsDirector" );
    eventMovieDO.setDsSynopsis( "dsSynopsis" );
    eventMovieDO.setDsRating( "dsRating" );
    eventMovieDO.setDsScript( "dsScript" );
    eventMovieDO.setDsActor( "dsActor" );
    eventMovieDO.setDsGenre( "dsGenre" );
    eventMovieDO.setDsUrl( "dsUrl" );

    this.eventDAO.create( eventDO );

    Assert.assertEquals( n + 1, this.eventDAO.count() );

  }

  @Test
  public void testEdit()
  {
    EventDO eventDO = this.eventDAO.find( 1L );
    Assert.assertNotNull( eventDO );
    eventDO.setDsName( "newDSName" );
    eventDO.setDsCodeDbs( "9999" );
    for( EventMovieDO eventMovieDO : eventDO.getEventMovieDOList() )
    {
      eventMovieDO.setDsActor( "new Actor" );
    }
    this.eventDAO.edit( eventDO );

    eventDO = this.eventDAO.find( 1L );
    Assert.assertNotNull( eventDO );

    Assert.assertEquals( "newDSName", eventDO.getDsName() );
    Assert.assertEquals( "9999", eventDO.getDsCodeDbs() );

  }

  @Test
  public void testRemove()
  {
    EventDO eventDO = this.eventDAO.find( 1L );

    Assert.assertNotNull( eventDO );
    Assert.assertTrue( eventDO.isFgActive() );

    eventDO = new EventDO( 1L );
    eventDO.setDtLastModification( new Date() );
    eventDO.setIdLastUserModifier( 1 );

    this.eventDAO.remove( eventDO );
    eventDO = this.eventDAO.find( 1L );
    Assert.assertNotNull( eventDO );
    Assert.assertFalse( eventDO.isFgActive() );
  }

  @Test
  public void testFind()
  {
    EventDO eventDO = this.eventDAO.find( 13L );
    System.out.println( eventDO.getDsName() );
    Assert.assertNotNull( eventDO );
  }

  @Test
  public void testFindAll()
  {
    List<EventDO> events = this.eventDAO.findAll();
    Assert.assertNotNull( events );
    Assert.assertFalse( events.isEmpty() );
    Assert.assertEquals( 17, events.size() );
  }

  @Test
  public void testFindRange()
  {
    List<EventDO> events = this.eventDAO.findRange( new int[] { 0, 3 } );
    Assert.assertNotNull( events );
    Assert.assertFalse( events.isEmpty() );
    Assert.assertEquals( 3, events.size() );
  }

  @Test
  public void testCount()
  {
    int n = this.eventDAO.count();
    Assert.assertEquals( 17, n );
  }

  @Test
  public void testSaveMovieImageNew()
  {
    EventMovieTO eventTO = new EventMovieTO();
    eventTO.setDsCountry( "E.U.A." );

    eventTO.setSoundFormats( Arrays.asList( new CatalogTO( 1L ) ) );
    eventTO.setMovieFormats( Arrays.asList( new CatalogTO( 4L ) ) );
    eventTO.setCodeDBS( "00001" );
    eventTO.setQtCopy( 1 );

    DistributorTO distributorTO = new DistributorTO();
    distributorTO.setId( 1L );

    eventTO.setDistributor( distributorTO );
    eventTO.setDsActor( "dsActor" );
    eventTO.setDsDirector( "dsDirector" );
    eventTO.setDsEventName( "dsEventName" );
    eventTO.setDsGenre( "dsGenre" );
    eventTO.setDsRating( "dsRating" );
    eventTO.setDsSynopsis( "dsSynopsis" );
    eventTO.setDsUrl( "dsUrl" );
    eventTO.setDtRelease( new Date() );
    eventTO.setDuration( 129 );
    eventTO.setMovieLanguage( new CatalogTO( 1L ) );
    eventTO.setTimestamp( new Date() );
    eventTO.setUserId( 1L );

    this.eventDAO.save( eventTO, 1L );

    Assert.assertNotNull( eventTO.getIdEvent() );
    EventDO eventDO = this.eventDAO.find( eventTO.getIdEvent().longValue() );
    Assert.assertNotNull( eventDO );

    EventMovieTO eventMovieTO = new EventMovieTO();

    eventMovieTO.setIdEvent( eventTO.getIdEvent().longValue() );
    eventMovieTO.setTimestamp( new Date() );
    eventMovieTO.setUserId( 1L );

    FileTO fileTO = new FileTO();
    fileTO.setName( "myFile.jpg" );
    fileTO.setFile( new byte[] { 1, 2, 3, 4, 5, 6 } );
    this.eventDAO.saveMovieImage( eventMovieTO, fileTO );

    Assert.assertNotNull( eventMovieTO.getIdMovieImage() );
    Assert.assertNotNull( fileTO.getId() );
    Assert.assertEquals( eventMovieTO.getIdMovieImage(), fileTO.getId() );

  }

  @Test
  public void testSaveMovieImageExistent()
  {
    EventMovieTO eventMovieTO = new EventMovieTO();

    eventMovieTO.setIdEvent( 1L );
    eventMovieTO.setTimestamp( new Date() );
    eventMovieTO.setUserId( 1L );

    FileTO fileTO = new FileTO();
    fileTO.setName( "myFile.jpg" );
    fileTO.setFile( new byte[] { 1, 2, 3, 4, 5, 6 } );
    this.eventDAO.saveMovieImage( eventMovieTO, fileTO );

    Assert.assertNotNull( eventMovieTO.getIdMovieImage() );
    Assert.assertNotNull( fileTO.getId() );
    Assert.assertEquals( eventMovieTO.getIdMovieImage(), fileTO.getId() );
  }

  @Test
  public void testGetMovieImage()
  {
    FileTO fileTO = this.eventDAO.getMovieImage( 1L );
    Assert.assertNotNull( fileTO );
    Assert.assertNotNull( fileTO.getId() );
    Assert.assertNotNull( fileTO.getName() );
    Assert.assertNotNull( fileTO.getFile() );
  }

  @Test
  public void testGet()
  {
    EventTO eventTO = this.eventDAO.getEvent( 1L );
    Assert.assertNotNull( eventTO );

    System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );

  }

  @Test
  public void testFindByIdVistaAndActive()
  {
    List<EventDO> events = this.eventDAO.findByIdVistaAndActive( "1001" );
    Assert.assertNotNull( events );
    Assert.assertEquals( 1, events.size() );
  }

  @Test
  public void testFindAllByPaging_FilterByDistributorId()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagePagingRequestTO.getFilters().put( EventQuery.EVENT_MOVIE_DISTRIBUTOR_ID, 1 );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );
  }

  @Test
  public void testFindAllByPaging_FilterByCodeDBS()
  {
    int page = 0;
    int pageSize = 5;

    PagingRequestTO pagePagingRequestTO = new PagingRequestTO();
    pagePagingRequestTO.setPage( page );
    pagePagingRequestTO.setPageSize( pageSize );
    pagePagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagePagingRequestTO.getFilters().put( EventQuery.EVENT_CODE_DBS, "10003" );

    PagingResponseTO<EventTO> response = this.eventDAO.findAllByPaging( pagePagingRequestTO );

    Assert.assertNotNull( response );
    Assert.assertFalse( response.getElements().isEmpty() );
    Assert.assertTrue( response.getElements().size() <= pageSize );
    for( EventTO eventTO : response.getElements() )
    {
      System.out.println( ToStringBuilder.reflectionToString( eventTO, ToStringStyle.MULTI_LINE_STYLE ) );
      for( CatalogTO to : eventTO.getSoundFormats() )
      {
        System.out.println( to );
      }
      for( CatalogTO to : eventTO.getMovieFormats() )
      {
        System.out.println( to );
      }
    }
    System.out.println( "Totalcount: " + response.getTotalCount() );
  }

  @Test
  public void testupdatePremiere()
  {
    this.eventDAO.updatePremiere();
    List<EventDO> events = this.eventDAO.findAll();
    Assert.assertNotNull( events );
    for( EventDO eventDO : events )
    {
      Assert.assertFalse( eventDO.getFgPremiere() );
    }
  }

  @Test
  public void testfindByDsCodeDbs()
  {
    List<EventTO> events = this.eventDAO.findByDsCodeDbs( "10006" );
    Assert.assertNotNull( events );
    Assert.assertEquals( events.size(), 1 );
  }
}
