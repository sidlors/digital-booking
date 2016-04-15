package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.AbstractObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.NewsFeedDOToNewsFeedObservationTOTransformer;
import mx.com.cinepolis.digital.booking.model.BookingWeekScreenDO;
import mx.com.cinepolis.digital.booking.model.NewsFeedDO;
import mx.com.cinepolis.digital.booking.model.ObservationDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.NewsFeedDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ObservationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * Implementation class of {@link mx.com.cinepolis.digital.booking.persistence.dao.ObservationDAO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class ObservationDAOImpl extends AbstractBaseDAO<ObservationDO> implements ObservationDAO
{
  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @EJB
  private BookingWeekScreenDAO bookingWeekScreenDAO;

  @EJB
  private NewsFeedDAO newsFeedDAO;

  @EJB
  private RegionDAO regionDAO;

  @EJB
  private UserDAO userDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

  /**
   * Constructor default
   */
  public ObservationDAOImpl()
  {
    super( ObservationDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveNewsFeedObservation( NewsFeedObservationTO newsFeedObservationTO )
  {

    ObservationDO observationDO = new ObservationDO();
    AbstractEntityUtils.applyElectronicSign( observationDO, newsFeedObservationTO );
    observationDO.setDsObservation( newsFeedObservationTO.getObservation() );
    observationDO.setIdUser( new UserDO( newsFeedObservationTO.getUserId().intValue() ) );

    this.create( observationDO );

    NewsFeedDO newsFeedDO = new NewsFeedDO();
    AbstractEntityUtils.applyElectronicSign( newsFeedDO, newsFeedObservationTO );
    newsFeedDO.setIdObservation( observationDO );
    newsFeedDO.setDtStart( newsFeedObservationTO.getStart() );
    newsFeedDO.setDtEnd( newsFeedObservationTO.getEnd() );

    if( CollectionUtils.isNotEmpty( newsFeedObservationTO.getRegions() ) )
    {
      newsFeedDO.setRegionDOList( new ArrayList<RegionDO>() );
      for( CatalogTO region : newsFeedObservationTO.getRegions() )
      {
        RegionDO regionDO = this.regionDAO.find( region.getId().intValue() );
        if( regionDO != null )
        {
          newsFeedDO.getRegionDOList().add( regionDO );
          regionDO.getNewsFeedDOList().add( newsFeedDO );
        }
      }
    }
    this.newsFeedDAO.create( newsFeedDO );
    this.flush();
    observationDO.setNewsFeedDOList( Arrays.asList( newsFeedDO ) );
    this.edit( observationDO );
    newsFeedObservationTO.setIdNewsFeed( newsFeedDO.getIdNewsFeed() );
    newsFeedObservationTO.setId( observationDO.getIdObservation() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateNewsFeedObservation( NewsFeedObservationTO newsFeedObservationTO )
  {
    NewsFeedDO newsFeedDO = this.newsFeedDAO.find( newsFeedObservationTO.getIdNewsFeed() );
    if( newsFeedDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.NEWS_FEED_OBSERVATION_NOT_FOUND );
    }
    AbstractEntityUtils.applyElectronicSign( newsFeedDO, newsFeedObservationTO );
    newsFeedDO.setDtStart( newsFeedObservationTO.getStart() );
    newsFeedDO.setDtEnd( newsFeedObservationTO.getEnd() );
    if( CollectionUtils.isNotEmpty( newsFeedObservationTO.getRegions() ) )
    {
      removeRegions( newsFeedObservationTO.getRegions(), newsFeedDO );
      addNewRegions( newsFeedObservationTO.getRegions(), newsFeedDO );
    }
    else
    {
      removeRegions( new ArrayList<CatalogTO>(), newsFeedDO );
    }
    this.newsFeedDAO.edit( newsFeedDO );

    ObservationDO observationDO = newsFeedDO.getIdObservation();
    AbstractEntityUtils.applyElectronicSign( observationDO, newsFeedObservationTO );
    observationDO.setDsObservation( newsFeedObservationTO.getObservation() );
    this.edit( observationDO );
    this.flush();

  }

  private void addNewRegions( List<CatalogTO> regions, NewsFeedDO newsFeedDO )
  {
    List<CatalogTO> regionsForAdding = new ArrayList<CatalogTO>();
    for( CatalogTO region : regions )
    {
      if( !newsFeedDO.getRegionDOList().contains( new RegionDO( region.getId().intValue() ) ) )
      {
        regionsForAdding.add( region );
      }
    }

    for( CatalogTO region : regionsForAdding )
    {
      RegionDO regionDO = this.regionDAO.find( region.getId().intValue() );
      if( regionDO != null )
      {
        newsFeedDO.getRegionDOList().add( regionDO );
        regionDO.getNewsFeedDOList().add( newsFeedDO );
      }
    }
  }

  private void removeRegions( List<CatalogTO> regions, NewsFeedDO newsFeedDO )
  {
    List<RegionDO> regionsForRemoval = new ArrayList<RegionDO>();

    for( RegionDO regionDO : newsFeedDO.getRegionDOList() )
    {
      if( !regions.contains( new CatalogTO( regionDO.getIdRegion().longValue() ) ) )
      {
        regionsForRemoval.add( regionDO );
      }
    }

    for( RegionDO regionDO : regionsForRemoval )
    {
      regionDO.getNewsFeedDOList().remove( newsFeedDO );
      newsFeedDO.getRegionDOList().remove( regionDO );
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<NewsFeedObservationTO> getNewsFeedObservations( Date date )
  {
    Query q = this.em.createNamedQuery( "NewsFeedDO.findByDate" );
    q.setParameter( "date", DateUtils.truncate( date, Calendar.DATE ) );

    return (List<NewsFeedObservationTO>) CollectionUtils.collect( q.getResultList(),
      new NewsFeedDOToNewsFeedObservationTOTransformer() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NewsFeedObservationTO getNewsFeedObservation( Long id )
  {
    NewsFeedDO newsFeedDO = this.newsFeedDAO.find( id );
    if( newsFeedDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.NEWS_FEED_OBSERVATION_NOT_FOUND );
    }
    return (NewsFeedObservationTO) new NewsFeedDOToNewsFeedObservationTOTransformer().transform( newsFeedDO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveBookingObservation( BookingObservationTO bookingObservationTO )
  {
    BookingWeekScreenDO bookingWeekScreenDO = this.bookingWeekScreenDAO.find( bookingObservationTO
        .getIdBookingWeekScreen() );
    if( bookingWeekScreenDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_OBSERVATION_NOT_FOUND2 );
    }

    ObservationDO observationDO = new ObservationDO();
    AbstractEntityUtils.applyElectronicSign( observationDO, bookingObservationTO );
    observationDO.setDsObservation( bookingObservationTO.getObservation() );
    observationDO.setIdUser( userDAO.find( bookingObservationTO.getUserId().intValue() ) );
    observationDO.setBookingWeekScreenDOList( new ArrayList<BookingWeekScreenDO>() );
    observationDO.getBookingWeekScreenDOList().add( bookingWeekScreenDO );
    this.create( observationDO );

    bookingWeekScreenDO.setIdObservation( observationDO );
    this.bookingWeekScreenDAO.edit( bookingWeekScreenDO );

    this.flush();
    bookingObservationTO.setId( observationDO.getIdObservation() );
    bookingObservationTO.setIdBooking( bookingWeekScreenDO.getIdBookingWeek().getIdBooking().getIdBooking() );
    bookingObservationTO.setIdBookingWeek( bookingWeekScreenDO.getIdBookingWeek().getIdBookingWeek() );

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update( AbstractObservationTO abstractObservationTO )
  {
    ObservationDO observationDO = this.find( abstractObservationTO.getId() );
    if( observationDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.OBSERVATION_NOT_FOUND );
    }
    AbstractEntityUtils.applyElectronicSign( observationDO, abstractObservationTO );
    observationDO.setDsObservation( abstractObservationTO.getObservation() );
    this.edit( observationDO );
    this.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( ObservationDO observationDO )
  {
    ObservationDO remove = this.find( observationDO.getIdObservation() );
    if( remove == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.OBSERVATION_NOT_FOUND );
    }
    remove.setFgActive( false );
    remove.setIdLastUserModifier( remove.getIdLastUserModifier() );
    remove.setDtLastModification( remove.getDtLastModification() );
    this.edit( remove );
    this.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete( AbstractObservationTO abstractObservationTO )
  {
    ObservationDO observationDO = this.find( abstractObservationTO.getId() );
    if( observationDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.NEWS_FEED_OBSERVATION_NOT_FOUND );
    }
    AbstractEntityUtils.applyElectronicSign( observationDO, abstractObservationTO );
    this.remove( observationDO );

    if( CollectionUtils.isNotEmpty( observationDO.getNewsFeedDOList() ) )
    {
      for( NewsFeedDO news : observationDO.getNewsFeedDOList() )
      {
        AbstractEntityUtils.applyElectronicSign( news, abstractObservationTO );
        news.setFgActive( Boolean.FALSE );
        this.newsFeedDAO.edit( news );
      }
    }
  }

}
