package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO;
import mx.com.cinepolis.digital.booking.model.ObservationDO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.persistence.dao.ObservationDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ObservationDAOTest extends AbstractDBEJBTestUnit
{
  private ObservationDAO observationDAO;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    observationDAO = new ObservationDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // this.initializeData( "dataset/business/pollServiceTest.sql" );
    // Conectar el EntityManager al servicio y sus daos
    connect( observationDAO );

  }

  @Test
  public void testCount()
  {
    int n = observationDAO.count();
    Assert.assertEquals( 20, n );
  }

  @Test
  public void testSaveNewsFeedObservation()
  {
    int n = observationDAO.count();
    Assert.assertEquals( 20, n );

    NewsFeedObservationTO newsFeedObservationTO = new NewsFeedObservationTO();
    newsFeedObservationTO.setEnd( new Date() );
    newsFeedObservationTO.setObservation( "An observation" );
    newsFeedObservationTO.setRegions( Arrays.asList( new CatalogTO( 1L ) ) );
    newsFeedObservationTO.setStart( new Date() );
    newsFeedObservationTO.setTimestamp( new Date() );
    newsFeedObservationTO.setUserId( 1L );
    observationDAO.saveNewsFeedObservation( newsFeedObservationTO );

    Assert.assertEquals( n + 1, observationDAO.count() );
  }

  @Test
  public void testSaveNewsFeedObservation_withoutRegion()
  {
    int n = observationDAO.count();
    Assert.assertEquals( 20, n );

    NewsFeedObservationTO newsFeedObservationTO = new NewsFeedObservationTO();
    newsFeedObservationTO.setEnd( new Date() );
    newsFeedObservationTO.setObservation( "An observation" );
    newsFeedObservationTO.setStart( new Date() );
    newsFeedObservationTO.setTimestamp( new Date() );
    newsFeedObservationTO.setUserId( 1L );
    observationDAO.saveNewsFeedObservation( newsFeedObservationTO );

    Long idNews = newsFeedObservationTO.getIdNewsFeed();
    Long idObs = newsFeedObservationTO.getId();

    Assert.assertEquals( n + 1, observationDAO.count() );

    ObservationDO observationDO = this.observationDAO.find( idObs );
    Assert.assertNotNull( observationDO );
    Assert.assertEquals( idNews, observationDO.getNewsFeedDOList().get( 0 ).getIdNewsFeed() );
  }

  @Test
  public void testUpdateNewsFeedObservation()
  {
    NewsFeedObservationTO newsFeedObservationTO = new NewsFeedObservationTO();

    newsFeedObservationTO.setStart( new Date() );
    newsFeedObservationTO.setEnd( new Date() );
    newsFeedObservationTO.setIdNewsFeed( 1L );
    newsFeedObservationTO.setRegions( Arrays.asList( new CatalogTO( 2L ), new CatalogTO( 3L ) ) );
    newsFeedObservationTO.setObservation( "new Observation" );
    newsFeedObservationTO.setUserId( 1L );
    newsFeedObservationTO.setTimestamp( new Date() );

    observationDAO.updateNewsFeedObservation( newsFeedObservationTO );

  }

  @Test
  public void testUpdateNewsFeedObservation_SameRegions()
  {
    NewsFeedObservationTO newsFeedObservationTO = new NewsFeedObservationTO();

    newsFeedObservationTO.setStart( new Date() );
    newsFeedObservationTO.setEnd( new Date() );
    newsFeedObservationTO.setIdNewsFeed( 1L );
    newsFeedObservationTO.setRegions( Arrays.asList( new CatalogTO( 1L ) ) );
    newsFeedObservationTO.setObservation( "new Observation" );
    newsFeedObservationTO.setUserId( 1L );
    newsFeedObservationTO.setTimestamp( new Date() );

    observationDAO.updateNewsFeedObservation( newsFeedObservationTO );

  }

  @Test
  public void testUpdateNewsFeedObservation_RemoveRegions()
  {
    NewsFeedObservationTO newsFeedObservationTO = new NewsFeedObservationTO();

    newsFeedObservationTO.setStart( new Date() );
    newsFeedObservationTO.setEnd( new Date() );
    newsFeedObservationTO.setIdNewsFeed( 1L );
    newsFeedObservationTO.setRegions( null );
    newsFeedObservationTO.setObservation( "new Observation" );
    newsFeedObservationTO.setUserId( 1L );
    newsFeedObservationTO.setTimestamp( new Date() );

    observationDAO.updateNewsFeedObservation( newsFeedObservationTO );

  }

  @Test(expected = DigitalBookingException.class)
  public void testUpdateNewsFeedObservation_NotExistent()
  {
    NewsFeedObservationTO newsFeedObservationTO = new NewsFeedObservationTO();

    newsFeedObservationTO.setIdNewsFeed( 1000L );

    observationDAO.updateNewsFeedObservation( newsFeedObservationTO );
  }

  @Test
  public void testGetNewsFeedObservations()
  {

    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.APRIL, 8 );
    List<NewsFeedObservationTO> news = this.observationDAO.getNewsFeedObservations( cal.getTime() );
    Assert.assertNotNull( news );
    Assert.assertFalse( news.isEmpty() );
    Assert.assertEquals( 4, news.size() );
    for( NewsFeedObservationTO to : news )
    {
      System.out.println( to );
      System.out.println( to.getPersonTO() );
    }
  }

  @Test
  public void testGetNewsFeedObservation()
  {
    NewsFeedObservationTO to = this.observationDAO.getNewsFeedObservation( 1L );

    Assert.assertNotNull( to );
    System.out.println( to );
    System.out.println( to.getPersonTO() );
  }

  @Test
  public void testSaveBookingObservation()
  {
    int n = observationDAO.count();
    Assert.assertEquals( 20, n );
    BookingObservationTO bookingObservationTO = new BookingObservationTO();
    bookingObservationTO.setUserId( 1L );
    bookingObservationTO.setTimestamp( new Date() );
    bookingObservationTO.setIdBookingWeekScreen(  1L );
    bookingObservationTO.setObservation( "Booking observation" );
    this.observationDAO.saveBookingObservation( bookingObservationTO );

    Assert.assertEquals( n + 1, observationDAO.count() );
  }

  @Test(expected = DigitalBookingException.class)
  public void testSaveBookingObservation_NotExistent()
  {
    BookingObservationTO bookingObservationTO = new BookingObservationTO();

    bookingObservationTO.setIdBookingWeekScreen( 1000L );
    bookingObservationTO.setObservation( "Booking observation" );
    this.observationDAO.saveBookingObservation( bookingObservationTO );
  }

  @Test
  public void testUpdate()
  {
    String update = "new observation";
    BookingObservationTO bookingObservationTO = new BookingObservationTO();
    bookingObservationTO.setId( 11L );
    bookingObservationTO.setObservation( update );
    bookingObservationTO.setUserId( 1L );
    bookingObservationTO.setTimestamp( new Date() );
    this.observationDAO.update( bookingObservationTO );

    ObservationDO observationDO = this.observationDAO.find( 11L );
    Assert.assertNotNull( observationDO );
    Assert.assertEquals( update, observationDO.getDsObservation() );
  }

  @Test(expected = DigitalBookingException.class)
  public void testUpdate_NonExistent()
  {
    BookingObservationTO bookingObservationTO = new BookingObservationTO();
    bookingObservationTO.setId( 100L );
    this.observationDAO.update( bookingObservationTO );

  }

  @Test
  public void testDelete()
  {
    int n = observationDAO.count();
    Assert.assertEquals( 20, n );

    BookingObservationTO bookingObservationTO = new BookingObservationTO();
    bookingObservationTO.setId( 1L );
    bookingObservationTO.setUserId( 1L );
    bookingObservationTO.setTimestamp( new Date() );
    this.observationDAO.delete( bookingObservationTO );

    Assert.assertEquals( n, observationDAO.count() );

    ObservationDO observationDO = this.observationDAO.find( 1L );
    Assert.assertNotNull( observationDO );
    Assert.assertFalse( observationDO.isFgActive() );
  }

  @Test(expected = DigitalBookingException.class)
  public void testDelete_NonExistent()
  {

    BookingObservationTO bookingObservationTO = new BookingObservationTO();
    bookingObservationTO.setId( 1000L );
    bookingObservationTO.setUserId( 1L );
    bookingObservationTO.setTimestamp( new Date() );
    this.observationDAO.delete( bookingObservationTO );
  }
  @Test(expected = DigitalBookingException.class)
  public void test_GetNewsFeedObservation()
  {
    NewsFeedObservationTO newsFeedObservationTO = this.observationDAO.getNewsFeedObservation( 1000L );
    Assert.assertNull( newsFeedObservationTO );
  }
  
  @Test(expected = DigitalBookingException.class)
  public void test_remove()
  {
    ObservationDO observationDO=new ObservationDO();
    observationDO.setIdObservation( 1000L );
    int sizeB=this.observationDAO.count();
    this.observationDAO.remove( observationDO );
    int sizeA= this.observationDAO.count();
    Assert.assertEquals( sizeB, sizeA );
    
  }
}
