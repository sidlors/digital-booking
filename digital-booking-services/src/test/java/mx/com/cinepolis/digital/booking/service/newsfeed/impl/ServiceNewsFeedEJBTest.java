package mx.com.cinepolis.digital.booking.service.newsfeed.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.persistence.AbstractDBEJBTestUnit;
import mx.com.cinepolis.digital.booking.service.newsfeed.ServiceNewsFeedEJB;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServiceNewsFeedEJBTest extends AbstractDBEJBTestUnit
{

  private ServiceNewsFeedEJB serviceNewsFeedEJB;

  @Before
  public void setUp()
  {
    // instanciar el servicio
    serviceNewsFeedEJB = new ServiceNewsFeedEJBImpl();
    // regionDAO = new RegionDAOImpl();
    // Llamar la prueba padre para obtener el EntityManager
    super.setUp();
    // Llamar los datos de negocio
    // Conectar el EntityManager al servicio y sus daos
    connect( serviceNewsFeedEJB );
    // connect( regionDAO );
  }

  @Test
  public void testGetNewsFeeds()
  {
    AbstractTO abstractTO = new AbstractTO();
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.APRIL, 10 );
    abstractTO.setTimestamp( cal.getTime() );
    List<NewsFeedObservationTO> list = serviceNewsFeedEJB.getNewsFeeds( abstractTO );
    Assert.assertNotNull( list );
    Assert.assertFalse( list.isEmpty() );
    for( NewsFeedObservationTO news : list )
    {
      System.out.println( news );
    }
  }

  @Test
  public void testCreateNewsFeed()
  {
    AbstractTO abstractTO = new AbstractTO();
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.APRIL, 10 );
    abstractTO.setTimestamp( cal.getTime() );

    int n = serviceNewsFeedEJB.getNewsFeeds( abstractTO ).size();
    NewsFeedObservationTO news = new NewsFeedObservationTO();
    news.setStart( cal.getTime() );
    news.setEnd( DateUtils.addDays( cal.getTime(), 15 ) );
    news.setUserId( 1L );
    news.setTimestamp( cal.getTime() );
    news.setObservation( "Observation" );
    serviceNewsFeedEJB.createNewsFeed( news );

    Assert.assertEquals( n + 1, serviceNewsFeedEJB.getNewsFeeds( abstractTO ).size() );
  }

  @Test
  public void testGetNewsFeed()
  {
    NewsFeedObservationTO news = serviceNewsFeedEJB.getNewsFeed( 1L );
    Assert.assertNotNull( news );
  }

  @Test(expected = DigitalBookingException.class)
  public void testGetNewsFeed_nonExistent()
  {
    serviceNewsFeedEJB.getNewsFeed( 10000L );
  }

  @Test
  public void testEditNewsFeed()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.APRIL, 10 );
    NewsFeedObservationTO news = serviceNewsFeedEJB.getNewsFeed( 1L );

    String update = "new observation";
    news.setObservation( update );
    news.setStart( cal.getTime() );
    news.setEnd( DateUtils.addDays( cal.getTime(), 15 ) );
    news.setUserId( 1L );
    serviceNewsFeedEJB.editNewsFeed( news );

    Assert.assertEquals( update, serviceNewsFeedEJB.getNewsFeed( 1L ).getObservation() );
  }

  @Test
  public void testEditNewsFeed_wrongUser()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.APRIL, 10 );
    NewsFeedObservationTO news = serviceNewsFeedEJB.getNewsFeed( 1L );

    String update = "new observation";
    news.setObservation( update );
    news.setStart( cal.getTime() );
    news.setEnd( DateUtils.addDays( cal.getTime(), 15 ) );
    serviceNewsFeedEJB.editNewsFeed( news );

  }

  @Test(expected = DigitalBookingException.class)
  public void testEditNewsFeed_nonExistent()
  {
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.APRIL, 10 );
    NewsFeedObservationTO news = serviceNewsFeedEJB.getNewsFeed( 1L );
    news.setIdNewsFeed( 10000L );

    String update = "new observation";
    news.setObservation( update );
    news.setStart( cal.getTime() );
    news.setEnd( DateUtils.addDays( cal.getTime(), 15 ) );
    news.setUserId( 11L );
    serviceNewsFeedEJB.editNewsFeed( news );

  }

  @Test
  public void testDeleteNewsFeed()
  {
    AbstractTO abstractTO = new AbstractTO();
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.APRIL, 10 );
    abstractTO.setTimestamp( cal.getTime() );

    int n = serviceNewsFeedEJB.getNewsFeeds( abstractTO ).size();

    NewsFeedObservationTO news = new NewsFeedObservationTO();
    news.setIdNewsFeed( 1L );
    news.setUserId( 1L );
    news.setTimestamp( new Date() );
    serviceNewsFeedEJB.deleteNewsFeed( news );

    Assert.assertEquals( n - 1, serviceNewsFeedEJB.getNewsFeeds( abstractTO ).size() );
  }

  @Test(expected = DigitalBookingException.class)
  public void testDeleteNewsFeed_nonExistent()
  {
    AbstractTO abstractTO = new AbstractTO();
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.APRIL, 10 );
    abstractTO.setTimestamp( cal.getTime() );

    int n = serviceNewsFeedEJB.getNewsFeeds( abstractTO ).size();

    NewsFeedObservationTO news = new NewsFeedObservationTO();
    news.setIdNewsFeed( 100L );
    news.setUserId( 1L );
    news.setTimestamp( new Date() );
    serviceNewsFeedEJB.deleteNewsFeed( news );
  }

  @Test(expected = DigitalBookingException.class)
  public void testDeleteNewsFeed_wrongUser()
  {
    AbstractTO abstractTO = new AbstractTO();
    Calendar cal = Calendar.getInstance();
    cal.set( 2014, Calendar.APRIL, 10 );
    abstractTO.setTimestamp( cal.getTime() );

    int n = serviceNewsFeedEJB.getNewsFeeds( abstractTO ).size();

    NewsFeedObservationTO news = new NewsFeedObservationTO();
    news.setIdNewsFeed( 100L );
    news.setUserId( 111L );
    news.setTimestamp( new Date() );
    serviceNewsFeedEJB.deleteNewsFeed( news );
  }
/**
 * method for validate news feed
 */
  @Test
  public void testValidateNewsFeed()
  {
    NewsFeedObservationTO feedObservation=new NewsFeedObservationTO();
    feedObservation.setId( 1L );
    feedObservation.setIdLanguage( 1L );
    feedObservation.setEnd( new Date() );
    feedObservation.setFgActive( true );
    feedObservation.setIdNewsFeed( 1L );
    feedObservation.setPersonTO( new PersonTO() );
    feedObservation.setRegions( new ArrayList<CatalogTO>() );
    feedObservation.setStart( new Date() );
    feedObservation.setTimestamp( new Date() );
    feedObservation.setUser( new UserTO() );
    feedObservation.setUserId( 1L );
    feedObservation.setUsername( "USER 1" );
    boolean fg=this.serviceNewsFeedEJB.validateNewsFeed( feedObservation );
    Assert.assertTrue( fg );
  }
}
