package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mx.com.cinepolis.digital.booking.commons.constants.BookingShow;
import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenShowTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.commons.utils.ScreenTOComparator;
import mx.com.cinepolis.digital.booking.dao.util.BookingDAOUtil;
import mx.com.cinepolis.digital.booking.dao.util.BookingSpecialEventScreenDOToSpecialEventScreenTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.BookingTOToBookingDOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.PresaleTOToPresaleDOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.SpecialEventScreenShowTOToBookingSpecialEventScreenShowDOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.SpecialEventScreenTOToBookingSpecialEventScreenDOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.SpecialEventTOToBookingSpecialEventDOTransformer;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenShowDO;
import mx.com.cinepolis.digital.booking.model.BookingStatusDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekDO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.ObservationDO;
import mx.com.cinepolis.digital.booking.model.PresaleDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.SpecialEventWeekDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingStatusDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.PresaleDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.SpecialEventWeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventDO}
 * 
 * @author jcarbajal
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class BookingSpecialEventDAOImpl extends AbstractBaseDAO<BookingSpecialEventDO> implements
    BookingSpecialEventDAO
{
  private static final String ID_THEATER = "idTheater";

  private static final String BOOKING_TYPE_ID = "idBookingType";

  private static final String ID_EVENT = "idEvent";

  private static final String GET_ID_SCREEN = "getIdScreen";

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @EJB
  private EventDAO eventDAO;

  @EJB
  private ScreenDAO screenDAO;

  @EJB
  private BookingStatusDAO bookingStatusDAO;

  @EJB
  private BookingDAO bookingDAO;

  @EJB
  private TheaterDAO theaterDAO;

  @EJB
  private UserDAO userDAO;

  @EJB
  private BookingSpecialEventScreenDAO bookingSpecialEventScreenDAO;

  @EJB
  private SpecialEventWeekDAO specialEventWeekDAO;

  @EJB
  private PresaleDAO presaleDAO;

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
  public BookingSpecialEventDAOImpl()
  {
    super( BookingSpecialEventDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<SpecialEventTO> findBookingsSpecialEventByEventRegion( PagingRequestTO pagingRequestTO )
  {
    Long idEvent = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_EVENT_ID );
    Long idBookingType = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_TYPE_ID );

    this.em.getEntityManagerFactory().getCache().evictAll();
    List<SpecialEventTO> specialEvents = searchBookingsSpecialEvent( pagingRequestTO );

    if( CollectionUtils.isNotEmpty( specialEvents ) )
    {
      for( SpecialEventTO specialEventTO : specialEvents )
      {
        specialEventTO.setSpecialEventScreens( new ArrayList<SpecialEventScreenTO>() );
        specialEventTO.setPresaleTO( new PresaleTO( null, false ) );
        if( specialEventTO.getStatus() == null )
        {
          specialEventTO.setStatus( new CatalogTO( BookingStatus.BOOKED.getIdLong() ) );
        }
        if( specialEventTO.getId() == null )
        {
          Long idTheater = specialEventTO.getTheater().getId();
          BookingDO bookingDO = this.findByEventAndTheaterAndType( idTheater, idEvent, idBookingType );
          if( bookingDO == null )
          {
            specialEventTO.setCopy( 1 );
          }
          else
          {
            this.extractScreensSelected( specialEventTO, bookingDO );
          }
        }
        else
        {
          this.extractSpecialEventScreensBooked( this.find( specialEventTO.getId() ), specialEventTO );
        }
        completeScreens( specialEventTO );
        BookingDAOUtil.addScreenZero( specialEventTO.getScreens(), 0 );
      }
    }
    return specialEvents;
  }
/**
 * method for extract screens selected
 * @param specialEventTO
 * @param bookingDO
 */
  /**
   * Method that extracts and sets the {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO} list in
   * a {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO}.
   * 
   * @param bookingSpecialEventDO, with the {@link mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO}
   *          information.
   * @param specialEventTO, with the {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO} information.
   */
  private void extractSpecialEventScreensBooked( BookingSpecialEventDO bookingSpecialEventDO,
      SpecialEventTO specialEventTO )
  {
    if( CollectionUtils.isNotEmpty( bookingSpecialEventDO.getBookingSpecialEventScreenDOList() ) )
    {
      /* Se obtiene la sala programada */
      for( BookingSpecialEventScreenDO bookingSpecialEventScreenDO : bookingSpecialEventDO
          .getBookingSpecialEventScreenDOList() )
      {
        /* Se transforma y agrega la sala al listado specialEventScreens */
        SpecialEventScreenTO specialEventScreenTO = (SpecialEventScreenTO) new BookingSpecialEventScreenDOToSpecialEventScreenTOTransformer()
            .transform( bookingSpecialEventScreenDO );
        specialEventTO.getSpecialEventScreens().add( specialEventScreenTO );
        /* Se valida que la sala programada tenga activada una preventa */
        if( !specialEventTO.getPresaleTO().isFgActive() && specialEventScreenTO.getPresaleTO().isFgActive() )
        {
          /* Se activa la preventa a nivel del booking y se completa su información */
          specialEventTO.getPresaleTO().setFgActive( true );
          specialEventTO.getPresaleTO().setDtStartDayPresale(
            specialEventScreenTO.getPresaleTO().getDtStartDayPresale() );
          specialEventTO.getPresaleTO().setDtFinalDayPresale(
            specialEventScreenTO.getPresaleTO().getDtFinalDayPresale() );
          specialEventTO.getPresaleTO().setDtReleaseDay( specialEventScreenTO.getPresaleTO().getDtReleaseDay() );
          specialEventTO.getPresaleTO().setStrPresaleDates( "" );
          specialEventTO.getPresaleTO().setStrReleaseDate( "" );
        }
      }
    }
  }

  private void extractScreensSelected( SpecialEventTO specialEventTO, BookingDO bookingDO )
  {
    List<BookingSpecialEventScreenDO> screens = null;
    for( BookingSpecialEventDO bookingSpecialEvent : bookingDO.getBookingSpecialEventDOList() )
    {
      if( bookingSpecialEvent.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.BOOKED.getId() )
          && bookingSpecialEvent.isFgActive() )
      {
        screens = extractScreensBooked( bookingSpecialEvent.getBookingSpecialEventScreenDOList() );
      }
    }

    if( specialEventTO.getCopy() > 0 )
    {
      for( ScreenTO screen : specialEventTO.getTheater().getScreens() )
      {
        ScreenDO screenDO = new ScreenDO( screen.getId().intValue() );
        /* Se valida que exista la sala del cine en el listado de salas programadas */
        if( CollectionUtils.exists(
          screens,
          PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( GET_ID_SCREEN ),
            PredicateUtils.equalPredicate( screenDO ) ) ) )
        {
          specialEventTO.getScreensSelected().add( screen.getId().toString() );
        }
      }
    }
  }
/**
 * method for extract screens selected booked
 * @param bsesDOList
 * @return
 */
  private List<BookingSpecialEventScreenDO> extractScreensBooked( List<BookingSpecialEventScreenDO> bsesDOList )
  {
    List<BookingSpecialEventScreenDO> screens = new ArrayList<BookingSpecialEventScreenDO>();
    if( CollectionUtils.isNotEmpty( bsesDOList ) )
    {
      for( BookingSpecialEventScreenDO bses : bsesDOList )
      {
        if( bses.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.BOOKED.getId() ) )
        {
          screens.add( bses );
        }
      }
    }
    return screens;
  }
/**
 * method for add screens in the list
 * @param specialEvent
 */
  private void completeScreens( SpecialEventTO specialEvent )
  {
    Collections.sort( specialEvent.getTheater().getScreens(), new ScreenTOComparator() );
    specialEvent.setScreens( specialEvent.getTheater().getScreens() );
  }

  /**
   * Method consulting bookings special events with active state.
   * 
   * @param pagingRequestTO
   * @return
   */
  @SuppressWarnings("unchecked")
  private List<SpecialEventTO> searchBookingsSpecialEvent( PagingRequestTO pagingRequestTO )
  {
    Long idRegion = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_REGION_ID );
    Long idEvent = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_EVENT_ID );
    Long idBookingType = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_TYPE_ID );
    int idLanguage = pagingRequestTO.getLanguage().getId();

    Integer movieFormat = extractMovieFormat( idEvent );
    List<SpecialEventTO> specialEvents = new ArrayList<SpecialEventTO>();
    String sqlString = "SpecialEventDO.nativeQuery.findBookingsSpecialEventByEventRegion";
    Query q = em.createNamedQuery( sqlString );
    q.setParameter( 1, idRegion );
    q.setParameter( 2, idEvent );
    q.setParameter( 3, idBookingType );
    q.setParameter( 4, idLanguage );
    q.setParameter( 5, movieFormat );
    List<Object[]> result = q.getResultList();

    SpecialEventTO specialEvent = null;
    Set<Integer> idTheaters = new HashSet<Integer>();
    List<CatalogTO> showings = Arrays.asList( BookingShow.FIRST.getShow(), BookingShow.SECOND.getShow(),
      BookingShow.THIRD.getShow(), BookingShow.FOURTH.getShow(), BookingShow.FIVETH.getShow(),
      BookingShow.SIXTH.getShow() );
    for( Object[] data : result )
    {
      Integer idTheater = ((Number) data[0]).intValue();
      String theaterName = (String) data[1];
      Integer idScreen = ((Number) data[2]).intValue();
      Integer nuScreen = ((Number) data[3]).intValue();
      Long idBookingSpecialEvent = this.getLongValue( data[4] );
      Long idBooking = this.getLongValue( data[5] );
      int qtCopy = ((Number) data[6]).intValue();
      int qtCopyScreenZero = ((Number) data[7]).intValue();
      int qtCopyScreenZeroTerminated = ((Number) data[8]).intValue();
      Date startDay = this.getDateValue( data[9] );
      Date finalDay = this.getDateValue( data[10] );
      Long idObservacion = this.getLongValue( data[11] );
      Integer idBookingStatus = data[12] != null ? ((Number) data[12]).intValue() : null;
      String statusName = (String) data[13];
      boolean disabled = ((Number) data[14]).intValue() == 0;
      boolean selected = ((Number) data[15]).intValue() > 0;
      boolean fgShowDate = this.getBooleanValue( data[16] );

      if( specialEvent == null || !specialEvent.getTheater().getId().equals( idTheater.longValue() ) )
      {
        this.addSpecialEventToList( specialEvent, specialEvents );
        specialEvent = new SpecialEventTO();
        specialEvent.setId( idBookingSpecialEvent );
        specialEvent.setIdBooking( idBooking );

        specialEvent.setShowDate( fgShowDate );
        specialEvent.setTheater( new TheaterTO() );
        specialEvent.getTheater().setId( idTheater.longValue() );
        specialEvent.getTheater().setName( theaterName );
        specialEvent.getTheater().setScreens( new ArrayList<ScreenTO>() );
        specialEvent.setCopy( qtCopy + qtCopyScreenZero );
        specialEvent.setCopyScreenZero( qtCopyScreenZero );
        specialEvent.setStartDay( startDay );
        specialEvent.setFinalDay( finalDay );
        specialEvent.setBookingObservation( new BookingObservationTO() );
        specialEvent.getBookingObservation().setId( idObservacion );
        specialEvent.setCopyScreenZeroTerminated( qtCopyScreenZeroTerminated );
        specialEvent.setStatus( idBookingStatus != null ? new CatalogTO( idBookingStatus.longValue(), statusName )
            : null );
        specialEvent.setScreensSelected( new ArrayList<Object>() );
        specialEvent.setShows( showings );
        specialEvent.setIdBookingType( idBookingType );
        EventTO event = new EventTO();
        event.setId( idEvent );
        specialEvent.setEvent( event );
        idTheaters.add( idTheater );
      }
      else if( idBookingSpecialEvent != null )
      {
        specialEvent.setId( idBookingSpecialEvent );
        specialEvent.setIdBooking( idBooking );

        specialEvent.setShowDate( fgShowDate );
        specialEvent.setCopy( qtCopy + qtCopyScreenZero );
        specialEvent.setCopyScreenZero( qtCopyScreenZero );
        specialEvent.setStartDay( startDay );
        specialEvent.setFinalDay( finalDay );
        specialEvent.setBookingObservation( new BookingObservationTO() );
        specialEvent.getBookingObservation().setId( idObservacion );
        specialEvent.setCopyScreenZeroTerminated( qtCopyScreenZeroTerminated );
        specialEvent.setStatus( idBookingStatus != null ? new CatalogTO( idBookingStatus.longValue(), statusName )
            : null );
      }
      addScreen( specialEvent, idScreen, nuScreen, disabled, selected );
    }
    this.addSpecialEventToList( specialEvent, specialEvents );

    return filterBookings( pagingRequestTO, specialEvents );
  }

  /**
   * Method that adds a SpecialEventTO into a list.
   * 
   * @param specialEvent
   * @param specialEvents
   */
  private void addSpecialEventToList( SpecialEventTO specialEvent, List<SpecialEventTO> specialEvents )
  {
    if( specialEvent != null )
    {
      if( specialEvent.getId() != null )
      {
        this.addNote( specialEvent );
        this.addShowsSelected( specialEvent );
      }
      specialEvents.add( specialEvent );
    }
  }

  /**
   * Method that filters the special events associated with the user.
   * 
   * @param pagingRequestTO
   * @param specialEvents
   * @return
   */
  private List<SpecialEventTO> filterBookings( PagingRequestTO pagingRequestTO, List<SpecialEventTO> specialEvents )
  {
    List<SpecialEventTO> filteredBookings = new ArrayList<SpecialEventTO>();
    if( pagingRequestTO.getUserId() != null )
    {
      UserDO userDO = this.userDAO.find( pagingRequestTO.getUserId().intValue() );

      if( CollectionUtils.isNotEmpty( userDO.getTheaterDOList() ) )
      {
        for( SpecialEventTO specialEventTO : specialEvents )
        {
          TheaterDO theaterDO = new TheaterDO( specialEventTO.getTheater().getId().intValue() );
          if( userDO.getTheaterDOList().contains( theaterDO ) )
          {
            filteredBookings.add( specialEventTO );
          }
        }
      }
      else
      {
        filteredBookings.addAll( specialEvents );
      }
    }
    else
    {
      filteredBookings.addAll( specialEvents );
    }
    return filteredBookings;
  }

  /**
   * Method gets the movie format
   * 
   * @param idEvent
   * @return
   */
  private Integer extractMovieFormat( Long idEvent )
  {
    Integer movieFormat = null;
    EventDO e = this.eventDAO.find( idEvent );

    for( CategoryDO category : e.getCategoryDOList() )
    {
      if( category.isFgActive()
          && category.getIdCategoryType().getIdCategoryType().equals( CategoryType.MOVIE_FORMAT.getId() ) )
      {
        movieFormat = category.getIdCategory();
        break;
      }
    }
    return movieFormat;
  }

  /**
   * Method that adds screens in theaters
   * 
   * @param specialEvent
   * @param idScreen
   * @param nuScreen
   * @param disabled
   * @param selected
   */
  private void addScreen( SpecialEventTO specialEvent, Integer idScreen, Integer nuScreen, boolean disabled,
      boolean selected )
  {
    ScreenTO screenTO = new ScreenTO( idScreen.longValue() );
    if( !specialEvent.getTheater().getScreens().contains( screenTO ) )
    {
      screenTO.setDisabled( disabled );
      if( selected )
      {
        specialEvent.getScreensSelected().add( idScreen.toString() );
      }
      screenTO.setSelected( selected );
      screenTO.setNuScreen( nuScreen );
      specialEvent.getTheater().getScreens().add( screenTO );
    }
  }

  /**
   * Method that adds shows selected in bookingSpecialEvent
   * 
   * @param specialEvent
   * @param idBookingSpecialEventScreen
   */
  private void addShowsSelected( SpecialEventTO specialEvent )
  {
    specialEvent.setShowingsSelected( this.getShowingsSelectedByIdSpecialEvent( specialEvent.getId() ) );
  }

  /**
   * Method that gets the showings selected by id special event.
   * 
   * @param idSpecialEvent
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Object> getShowingsSelectedByIdSpecialEvent( Long idSpecialEvent )
  {
    String sqlString = "BookingSpecialEventScreenShowDO.findByidBookingSpecialEvent";
    Query q = em.createNamedQuery( sqlString );
    q.setParameter( 1, idSpecialEvent );
    List<Integer> result = q.getResultList();
    List<Object> showsSelected = new ArrayList<Object>();
    for( Integer data : result )
    {
      Long nuShow = ((Number) data).longValue();
      if( !showsSelected.contains( nuShow ) )
      {
        showsSelected.add( nuShow );
      }
    }
    return showsSelected;
  }

  /**
   * Method that adds notes to a bookingSpecialEvent.
   * 
   * @param specialEvent
   */
  private void addNote( SpecialEventTO specialEvent )
  {
    BookingObservationTO observationTO = this.getObservationByIdSpecialEvent( specialEvent.getId() );
    specialEvent.setObservation( observationTO );
    specialEvent.setNotes( observationTO.getObservation() );
  }

  /**
   * Method that gets a BookingObservation by id special event.
   * 
   * @param idSpecialEvent
   * @return
   */
  @SuppressWarnings("unchecked")
  public BookingObservationTO getObservationByIdSpecialEvent( Long idSpecialEvent )
  {
    BookingObservationTO observationTO = new BookingObservationTO();
    String sqlString = "ObservationDO.nativeQuery.findObservationsBySpecialEventId";
    Query q = em.createNamedQuery( sqlString );
    q.setParameter( 1, idSpecialEvent );
    List<Object[]> result = q.getResultList();
    for( Object[] data : result )
    {
      Long idObservation = ((Number) data[0]).longValue();
      Long iduser = ((Number) data[1]).longValue();
      String notes = (String) data[2];

      observationTO.setId( idObservation );
      UserTO user = new UserTO();
      user.setId( iduser );
      observationTO.setUser( user );
      observationTO.setObservation( notes );
      break;
    }
    return observationTO;
  }

  /**
   * Method gets special events associated with an event and theater
   */
  public BookingDO findByEventAndTheaterAndType( Long idTheater, Long idEvent, Long idBookingType )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    Query q = em.createNamedQuery( "BookingDO.findByEventAndTheaterAndType" );
    q.setParameter( ID_THEATER, idTheater );
    q.setParameter( ID_EVENT, idEvent );
    q.setParameter( BOOKING_TYPE_ID, idBookingType );
    return (BookingDO) CollectionUtils.find( q.getResultList(), PredicateUtils.notNullPredicate() );
  }

  /**
   * Method saves an special event booking.
   * 
   * @param specialEventTO
   * @param bookingTO
   */
  public void saveBookingSpecialEvent( BookingTO bookingTO )
  {
    BookingTOToBookingDOTransformer bookingTransformer = new BookingTOToBookingDOTransformer();
    BookingDO bookingDO = (BookingDO) bookingTransformer.transform( bookingTO );
    AbstractEntityUtils.applyElectronicSign( bookingDO, bookingTO.getSpecialEvents().get( 0 ) );
    bookingDO.setFgBooked( true );
    bookingDO.setFgActive( true );
    bookingDO.setIdEvent( this.eventDAO.find( bookingTO.getEvent().getId() ) );
    bookingDO.setIdTheater( this.theaterDAO.find( bookingTO.getTheater().getId().intValue() ) );
    bookingDO.setBookingSpecialEventDOList( new ArrayList<BookingSpecialEventDO>() );
    bookingDO.setBookingWeekDOList( new ArrayList<BookingWeekDO>() );

    /* Obtenemos la lista de eventos especiales relacionados a la programación */
    for( SpecialEventTO specialEventTO : bookingTO.getSpecialEvents() )
    {
      SpecialEventTOToBookingSpecialEventDOTransformer specialEventTransformer = new SpecialEventTOToBookingSpecialEventDOTransformer();
      BookingSpecialEventDO specialEventDO = (BookingSpecialEventDO) specialEventTransformer.transform( specialEventTO );
      AbstractEntityUtils.applyElectronicSign( specialEventDO, specialEventTO );
      specialEventDO.setIdBooking( bookingDO );
      specialEventDO.setFgActive( true );
      specialEventDO.setBookingSpecialEventScreenDOList( new ArrayList<BookingSpecialEventScreenDO>() );

      SpecialEventScreenTOToBookingSpecialEventScreenDOTransformer specialEventScreenTransformer = new SpecialEventScreenTOToBookingSpecialEventScreenDOTransformer();
      SpecialEventScreenShowTOToBookingSpecialEventScreenShowDOTransformer specialEventScreenShowTransformer = new SpecialEventScreenShowTOToBookingSpecialEventScreenShowDOTransformer();
      if( CollectionUtils.isNotEmpty( specialEventTO.getSpecialEventScreens() ) )
      {
        /* Obtenemos las salas donde se va a aplicar la proramación del evento */
        for( SpecialEventScreenTO specialEventScreenTO : specialEventTO.getSpecialEventScreens() )
        {
          BookingSpecialEventScreenDO specialEventScreenDO = (BookingSpecialEventScreenDO) specialEventScreenTransformer
              .transform( specialEventScreenTO );
          specialEventScreenDO.setIdBookingSpecialEvent( specialEventDO );
          specialEventScreenDO.setIdScreen( this.screenDAO.find( specialEventScreenTO.getIdScreen().intValue() ) );
          specialEventScreenDO.setIdBookingStatus( this.bookingStatusDAO.find( specialEventScreenTO.getStatus().getId()
              .intValue() ) );
          // jcarbajal crear preventas
          if( specialEventScreenTO.getPresaleTO() != null && specialEventScreenTO.getPresaleTO().getDtStartDayPresale()!=null)
          {
            specialEventScreenDO.setPresaleDOList( new ArrayList<PresaleDO>() );
            PresaleDO presaleDO = (PresaleDO) new PresaleTOToPresaleDOTransformer().transform( specialEventScreenTO.getPresaleTO() );
            presaleDO.setIdBookingSpecialEventScreen( specialEventScreenDO );
            specialEventScreenDO.getPresaleDOList().add( presaleDO );
            
          }
          /* Obetenemos la observacion para persistir en base de datos */
          ObservationDO observationDO = new ObservationDO();
          observationDO.setIdObservation( null );
          if( specialEventTO.getNotes() != null )
          {
            observationDO.setDsObservation( specialEventTO.getNotes() );
          }
          else
          {
            observationDO.setDsObservation( " " );
          }
          observationDO.setFgActive( true );
          observationDO.setIdUser( this.userDAO.find( specialEventTO.getUserId().intValue() ) );
          AbstractEntityUtils.applyElectronicSign( observationDO, specialEventTO );
          specialEventScreenDO.setIdObservation( observationDO );
          specialEventScreenDO
              .setBookingSpecialEventScreenShowDOList( new ArrayList<BookingSpecialEventScreenShowDO>() );
          if( CollectionUtils.isNotEmpty( specialEventScreenTO.getSpecialEventScreenShows() ) )
          {
            /* Obtenemos la lista de funciones relacionadas con la sala del cine seleccionado */
            for( SpecialEventScreenShowTO specialEventScreenShowTO : specialEventScreenTO.getSpecialEventScreenShows() )
            {
              BookingSpecialEventScreenShowDO specialEventScreenShowDO = (BookingSpecialEventScreenShowDO) specialEventScreenShowTransformer
                  .transform( specialEventScreenShowTO );
              specialEventScreenShowDO.setIdBookingSpecialEventScreen( specialEventScreenDO );
              specialEventScreenDO.getBookingSpecialEventScreenShowDOList().add( specialEventScreenShowDO );
            }
          }
          specialEventDO.getBookingSpecialEventScreenDOList().add( specialEventScreenDO );
        }
      }
      bookingDO.getBookingSpecialEventDOList().add( specialEventDO );
    }
    this.bookingDAO.create( bookingDO );
    this.specialEventWeekDAO.createSpecialEventWeek( bookingDO.getBookingSpecialEventDOList().get( 0 ), bookingTO
        .getSpecialEvents().get( 0 ) );
    this.flush();

  }

  /**
   * Method canceling a special event booking.
   * 
   * @param bookingTO
   */
  public void cancelBookingSpecialEvent( BookingTO bookingTO )
  {
    BookingStatusDO bookingStatus = this.bookingStatusDAO.find( BookingStatus.CANCELED.getId() );
    BookingDO bookingDO = this.bookingDAO.find( bookingTO.getId() );
    if( bookingDO == null )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.BOOKING_PERSISTENCE_ERROR_BOOKING_NOT_FOUND );
    }
    bookingDO.setFgBooked( Boolean.FALSE );
    AbstractEntityUtils.applyElectronicSign( bookingDO, bookingTO );
    /* Cancelar programación de eventos especiales */
    if( CollectionUtils.isNotEmpty( bookingDO.getBookingSpecialEventDOList() ) )
    {
      for( BookingSpecialEventDO specialEventDO : bookingDO.getBookingSpecialEventDOList() )
      {
        AbstractEntityUtils.applyElectronicSign( specialEventDO, bookingTO );
        specialEventDO.setIdBookingStatus( bookingStatus );
        /* Cancelar salas */
        if( CollectionUtils.isNotEmpty( specialEventDO.getBookingSpecialEventScreenDOList() ) )
        {
          for( BookingSpecialEventScreenDO specialEventScreenDO : specialEventDO.getBookingSpecialEventScreenDOList() )
          {
            specialEventScreenDO.setIdBookingStatus( bookingStatus );
            /* Cancelar preventas */
            if( CollectionUtils.isNotEmpty( specialEventScreenDO.getPresaleDOList() ) )
            {
              for( PresaleDO presaleDO : specialEventScreenDO.getPresaleDOList() )
              {
                AbstractEntityUtils.applyElectronicSign( presaleDO, bookingTO );
                presaleDO.setFgActive( Boolean.FALSE );
              }
            }
          }
        }
        if( CollectionUtils.isNotEmpty( specialEventDO.getSpecialEventWeekDOList() ) )
        {
          for( SpecialEventWeekDO week : specialEventDO.getSpecialEventWeekDOList() )
          {
            AbstractEntityUtils.applyElectronicSign( week, bookingTO );
            week.setFgActive( false );
            this.specialEventWeekDAO.edit( week );
          }
        }
      }
    }

    this.bookingDAO.edit( bookingDO );
    this.flush();
  }

  /**
   * Method that cancels a screen of a special event booking.
   * 
   * @param bookingTO
   */
  public void cancelBookingSpecialEventScreen( Long idScreen, int copies )
  {
    BookingStatusDO bookingStatus = this.bookingStatusDAO.find( BookingStatus.CANCELED.getId() );
    BookingSpecialEventScreenDO screenDO = this.bookingSpecialEventScreenDAO.find( idScreen );
    if(CollectionUtils.isNotEmpty(screenDO.getPresaleDOList()))
    {
      PresaleDO presaleDO=screenDO.getPresaleDOList().get( 0 );
      presaleDO.setFgActive( false );
      this.presaleDAO.edit( presaleDO );
    }
    screenDO.setIdBookingStatus( bookingStatus );
    this.bookingSpecialEventScreenDAO.edit( screenDO );
  }

  /**
   * Method that verify whether exist screens booked for a special event.
   * 
   * @param bookingTO
   */
  private void verifyScreenActives( BookingSpecialEventDO specialEventDO )
  {
    int screenActives = 0;
    int noneScrrensActives = 0;
    if( CollectionUtils.isNotEmpty( specialEventDO.getBookingSpecialEventScreenDOList() ) )
    {
      for( BookingSpecialEventScreenDO specialEventScreenDO : specialEventDO.getBookingSpecialEventScreenDOList() )
      {
        if( specialEventScreenDO.getIdBookingStatus().getIdBookingStatus() == BookingStatus.BOOKED.getId() )
        {
          screenActives++;
        }
      }
    }
    if( screenActives == noneScrrensActives && specialEventDO.getQtCopyScreenZero() == 0 )
    {
      BookingTO bookingTO = new BookingTO();
      bookingTO.setId( specialEventDO.getIdBooking().getIdBooking() );
      bookingTO.setUserId( new Long(specialEventDO.getIdLastUserModifier() ));
      bookingTO.setTimestamp( new Date() );
      this.cancelBookingSpecialEvent( bookingTO );
    }
  }

  /**
   * Method that updates a booking special event.
   * 
   * @param bookingTO
   */
  public void updateBookingSpecialEvent( BookingTO bookingTO )
  {
    BookingDO bookingDO = this.bookingDAO.find( bookingTO.getId() );
    if( CollectionUtils.isNotEmpty( bookingDO.getBookingSpecialEventDOList() ) )
    {
      BookingSpecialEventDO specialEventDO = bookingDO.getBookingSpecialEventDOList().get( 0 );
      specialEventDO.setQtCopy( bookingTO.getCopy() );
      specialEventDO.setQtCopyScreenZero( bookingTO.getCopyScreenZero() );
      this.edit( specialEventDO );
      this.flush();
      this.verifyScreenActives( specialEventDO );
    }
  }

  /**
   * Gets the long value or null from an Oject.
   * 
   * @param data
   * @return The Long value or null
   */
  private Long getLongValue( Object data )
  {
    return data != null ? ((Number) data).longValue() : null;
  }

  /**
   * Gets the boolean value or false from an Oject.
   * 
   * @param data
   * @return The boolean value or false
   */
  private boolean getBooleanValue( Object data )
  {
    return data != null ? ((Boolean) data).booleanValue() : false;
  }

  /**
   * Gets the date value or null from an Oject.
   * 
   * @param data
   * @return The Date value or null
   */
  private Date getDateValue( Object data )
  {
    return data != null ? (Date) data : null;
  }

}
