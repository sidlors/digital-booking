package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.EventQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.query.TheaterQuery;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportTheaterTO;
import mx.com.cinepolis.digital.booking.commons.utils.WeeklyBookingReportMovieTOComparator;
import mx.com.cinepolis.digital.booking.commons.utils.WeeklyBookingReportTheaterTOComparator;
import mx.com.cinepolis.digital.booking.dao.util.BookingDOToBookingTOReportTransformer;
import mx.com.cinepolis.digital.booking.dao.util.BookingDOToBookingTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.BookingTOToWeeklyBookingReportMovieTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.BookingTheaterTOToWeeklyBookingReportMovieTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.PredicateSearchByIdTheater;
import mx.com.cinepolis.digital.booking.dao.util.TheaterDOToTheaterTOTransformer;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.model.BookingStatusDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekDO;
import mx.com.cinepolis.digital.booking.model.CityLanguageDO;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.EventMovieDO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.SpecialEventWeekDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.TheaterLanguageDO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingStatusDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ReportDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.TransformerUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.ReportDAO}
 * 
 * @author rgarcia
 * @since 0.2.0
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class ReportDAOImpl extends AbstractBaseDAO<BookingDO> implements ReportDAO
{

  private static final String ID_LANGUAGE = "idLanguage";
  private static final String THEATER_LANGUAGE_DO_LIST = "theaterLanguageDOList";
  private static final String BOOKING_WEEK_DO_LIST = "bookingWeekDOList";
  private static final String BOOKING_SPECIAL_EVENT_DO_LIST = "bookingSpecialEventDOList";
  private static final String ID_DISTRIBUTOR = "idDistributor";
  private static final String BOOKING_SPECIAL_EVENT_WEEK = "specialEventWeekDOList";
  private static final String ID_BOOKING_STATUS = "idBookingStatus";
  private static final String ID_REGION = "idRegion";
  private static final String ID_THEATER = "idTheater";
  private static final String ID_WEEK = "idWeek";
  private static final String EVENT_MOVIE_DO_LIST = "eventMovieDOList";
  private static final String ID_EVENT = "idEvent";
  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @EJB
  private TheaterDAO theaterDAO;

  @EJB
  private BookingStatusDAO bookingStatusDAO;

  @EJB
  private BookingDAO bookingDAO;

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
  public ReportDAOImpl()
  {
    super( BookingDO.class );
  }

  @SuppressWarnings("unchecked")
  @Override
  public WeeklyBookingReportTheaterTO findWeeklyBookingReportTheaterTO( Long idWeek, Long idTheater, Language language )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    // Filtros
    Map<ModelQuery, Object> filters = new HashMap<ModelQuery, Object>();
    filters.put( BookingQuery.BOOKING_THEATER_ID, idTheater );
    filters.put( BookingQuery.BOOKING_WEEK_ID, idWeek );

    // Se ejecuta el query para obtener las programaciones del cine
    TypedQuery<BookingDO> tq = executeQueryFindWeeklyBookingReportTheaterByTheater( filters );
    // Se recupera la lista de los bookings
    List<BookingDO> bookings = tq.getResultList();
    BookingDOToBookingTOTransformer transformer = new BookingDOToBookingTOTransformer( language );
    transformer.setIdWeek( idWeek.intValue() );

    List<BookingTO> bookingsTO = (List<BookingTO>) CollectionUtils.collect( bookings, transformer );

    List<BookingTheaterTO> bookingsSpecialEvents = bookingDAO
        .extractBookingSpecialEvents( idTheater, idWeek.intValue() );
    List<WeeklyBookingReportMovieTO> specialEvents = extractSpecialEventsByScreen( bookingsSpecialEvents );

    WeeklyBookingReportTheaterTO theater = null;
    if( bookingsTO != null && !bookingsTO.isEmpty() )
    {
      // Se obtiene la informacion general del Cine
      theater = new WeeklyBookingReportTheaterTO();
      BookingTO booking = bookingsTO.get( 0 );
      theater.setIdTheater( Long.valueOf( booking.getTheater().getNuTheater() ) );
      theater.setTheaterName( booking.getTheater().getName() );
      theater.setDsCity( booking.getTheater().getCity().getName() );
      // Se obtiene las programaciones por cada sala
      List<WeeklyBookingReportMovieTO> movies = extractBookingsByScreen( bookingsTO );
      theater.setMovies( movies );
    }
    else
    {
      theater = new WeeklyBookingReportTheaterTO();
      TheaterTO theaterTO = (TheaterTO) new TheaterDOToTheaterTOTransformer().transform( this.theaterDAO
          .find( idTheater.intValue() ) );
      theater.setIdTheater( Long.valueOf( theaterTO.getNuTheater() ) );
      theater.setTheaterName( theaterTO.getName() );
      theater.setDsCity( theaterTO.getCity().getName() );
      theater.setMovies( new ArrayList<WeeklyBookingReportMovieTO>() );
    }
    if( CollectionUtils.isNotEmpty( specialEvents ) )
    {
      theater.getMovies().addAll( specialEvents );
    }
    if( CollectionUtils.isNotEmpty( theater.getMovies() ) )
    {
      Collections.sort( theater.getMovies(), new WeeklyBookingReportMovieTOComparator() );
    }
    return theater;
  }

  private List<WeeklyBookingReportMovieTO> extractSpecialEventsByScreen( List<BookingTheaterTO> bookingsTheater )
  {
    List<WeeklyBookingReportMovieTO> movies = new ArrayList<WeeklyBookingReportMovieTO>();
    if( CollectionUtils.isNotEmpty( bookingsTheater ) )
    {
      for( BookingTheaterTO bookingTheater : bookingsTheater )
      {
        ScreenTO screenTO = bookingTheater.getScreenTO();
        screenTO.setBookingObservation( bookingTheater.getBookingObservationTO() );
        screenTO.setBookingStatus( bookingTheater.getStatusTO() );
        screenTO.setShowings( bookingTheater.getShowings() );
        if( screenTO.getBookingStatus().getId().equals( BookingStatus.BOOKED.getIdLong() ) && !screenTO.getNuScreen().equals( new Integer(0) ))
        {
          BookingTheaterTO bookingTheaterTO = bookingTheater;
          bookingTheaterTO.setScreenTO( screenTO );
          movies.add( (WeeklyBookingReportMovieTO) new BookingTheaterTOToWeeklyBookingReportMovieTOTransformer()
              .transform( bookingTheaterTO ) );
        }
      }
    }
    return movies;
  }

  /*
   * Se obtiene las programaciones de pelicula por cada sala
   */
  private List<WeeklyBookingReportMovieTO> extractBookingsByScreen( List<BookingTO> bookingsTO )
  {
    List<WeeklyBookingReportMovieTO> movies = new ArrayList<WeeklyBookingReportMovieTO>();
    for( BookingTO bookingTO : bookingsTO )
    {
      if( CollectionUtils.isNotEmpty( bookingTO.getScreens() ) )
      {
        for( ScreenTO screenTO : bookingTO.getScreens() )
        {
          if( screenTO.getBookingStatus().getId().equals( BookingStatus.BOOKED.getIdLong() ) )
          {
            BookingTO booking = bookingTO;
            booking.setScreen( screenTO );
            movies.add( (WeeklyBookingReportMovieTO) new BookingTOToWeeklyBookingReportMovieTOTransformer()
                .transform( booking ) );
          }
        }
        bookingTO.setScreen( null );
      }
    }
    Collections.sort( movies, new WeeklyBookingReportMovieTOComparator() );
    return movies;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<WeeklyBookingReportTheaterTO> findAllWeeklyBookingReportTheaterTO( Long idWeek, Long idRegion,
      Language language )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    // Filtros, solo por semana, todos los cines
    Map<ModelQuery, Object> filters = new HashMap<ModelQuery, Object>();
    filters.put( BookingQuery.BOOKING_WEEK_ID, idWeek );
    filters.put( BookingQuery.BOOKING_REGION_ID, idRegion );
    // filters.put( EventQuery.EVENT_LANGUAGE_ID, language.getId() );
    // Se ejecuta el query para obtener las programaciones del cine
    TypedQuery<BookingDO> tq = executeQueryFindAllWeeklyBookingReportTheater( filters );
    // Se recupera la lista de los bookings
    List<BookingDO> bookings = tq.getResultList();

    BookingDOToBookingTOTransformer transformer = new BookingDOToBookingTOTransformer( language );
    transformer.setIdWeek( idWeek.intValue() );

    List<BookingTO> bookingsTO = (List<BookingTO>) CollectionUtils.collect( bookings, transformer );
    return generateWeeklyBookingReportTheaterList( bookingsTO, language, idRegion, true );
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<WeeklyBookingReportTheaterTO> findWeeklyBookingReportTheaterTOByDistributor( Long idWeek, Long idRegion,
      Long idDistributor, Language language )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    // Filtros
    Map<ModelQuery, Object> filters = new HashMap<ModelQuery, Object>();
    filters.put( EventQuery.EVENT_MOVIE_DISTRIBUTOR_ID, idDistributor );
    filters.put( BookingQuery.BOOKING_REGION_ID, idRegion );
    filters.put( BookingQuery.BOOKING_WEEK_ID, idWeek );
    filters.put( TheaterQuery.THEATER_ID_LANGUAGE, language.getId() );

    // Se ejecuta el query para obtener las programaciones del cine
    TypedQuery<BookingDO> tq = executeQueryFindWeeklyBookingReportTheaterTOByDistributor( filters );
    List<BookingDO> bookings = tq.getResultList();

    // Se cambia el tranformer para incluir los eventos especiales jcarbajal
    // BookingDOToBookingTOTransformer transformer = new BookingDOToBookingTOTransformer( language );
    BookingDOToBookingTOReportTransformer transformer = new BookingDOToBookingTOReportTransformer( language );
    transformer.setIdWeek( idWeek.intValue() );

    List<BookingTO> bookingsTO = (List<BookingTO>) CollectionUtils.collect( bookings, transformer );
    return generateWeeklyBookingReportTheaterList( bookingsTO, language, idRegion, false );
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<WeeklyBookingReportTheaterTO> findWeeklyBookingReportTheaterTOByAllDistributors( Long idWeek,
      Long idRegion, Language language )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    // Filtros
    Map<ModelQuery, Object> filters = new HashMap<ModelQuery, Object>();
    filters.put( BookingQuery.BOOKING_REGION_ID, idRegion );
    filters.put( BookingQuery.BOOKING_WEEK_ID, idWeek );
    filters.put( TheaterQuery.THEATER_ID_LANGUAGE, language.getId() );

    // Se ejecuta el query para obtener las programaciones del cine
    TypedQuery<BookingDO> tq = executeQueryFindWeeklyBookingReportTheaterTOByAllDistributors( filters );
    List<BookingDO> bookings = tq.getResultList();

    // se cambia el transformer jcarbajal
    BookingDOToBookingTOReportTransformer transformer = new BookingDOToBookingTOReportTransformer( language );
    transformer.setIdWeek( idWeek.intValue() );
    List<BookingTO> bookingsTO = (List<BookingTO>) CollectionUtils.collect( bookings, transformer );
    return generateWeeklyBookingReportTheaterList( bookingsTO, language, idRegion, false );
  }

  /**
   * Ejecucion del query
   * 
   * @param filters
   * @return
   */
  private TypedQuery<BookingDO> executeQueryFindWeeklyBookingReportTheaterByTheater( Map<ModelQuery, Object> filters )
  {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    List<ModelQuery> sortFields = new ArrayList<ModelQuery>();
    CriteriaQuery<BookingDO> q = cb.createQuery( BookingDO.class );
    q.distinct( Boolean.TRUE );
    Root<BookingDO> bookingDO = q.from( BookingDO.class );
    Join<BookingWeekDO, BookingDO> bookingWeekDO = bookingDO.join( BOOKING_WEEK_DO_LIST );
    Join<EventDO, BookingDO> eventDO = bookingDO.join( ID_EVENT );
    Join<EventDO, EventMovieDO> eventMovieDO = eventDO.join( EVENT_MOVIE_DO_LIST );
    Join<WeekDO, BookingWeekDO> weekDO = bookingWeekDO.join( ID_WEEK );
    Join<TheaterDO, BookingDO> theaterDO = bookingDO.join( ID_THEATER );
    Join<RegionDO, TheaterDO> regionDO = theaterDO.join( ID_REGION );
    Join<BookingStatusDO, BookingWeekDO> bookingStatusDO = bookingWeekDO.join( ID_BOOKING_STATUS );

    q.select( bookingDO );
    BookingSortFilter bookingSortFilter = new BookingSortFilter();
    bookingSortFilter.setCriteriaBuilder( cb );
    bookingSortFilter.setCriteriaQuery( q );
    bookingSortFilter.setBookingDO( bookingDO );
    bookingSortFilter.setBookingStatusDO( bookingStatusDO );
    bookingSortFilter.setEventDO( eventDO );
    bookingSortFilter.setRegionDO( regionDO );
    bookingSortFilter.setTheaterDO( theaterDO );
    bookingSortFilter.setWeekDO( weekDO );
    bookingSortFilter.setEventMovieDO( eventMovieDO );

    bookingSortFilter.setFilters( filters );
    bookingSortFilter.setSortFields( sortFields );

    applySorting( bookingSortFilter );
    Predicate filterCondition = null;

    filterCondition = applyFilters( bookingSortFilter, new Object[] { BookingStatus.BOOKED.getId() } );
    if( filterCondition != null )
    {
      q.where( filterCondition );
    }
    return em.createQuery( q );
  }

  /**
   * Ejecucion del query
   * 
   * @param filters
   * @return
   */
  private TypedQuery<BookingDO> executeQueryFindAllWeeklyBookingReportTheater( Map<ModelQuery, Object> filters )
  {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    List<ModelQuery> sortFields = new ArrayList<ModelQuery>();
    CriteriaQuery<BookingDO> q = cb.createQuery( BookingDO.class );
    q.distinct( Boolean.TRUE );
    Root<BookingDO> bookingDO = q.from( BookingDO.class );
    Join<EventDO, BookingDO> eventDO = bookingDO.join( ID_EVENT );
    Join<EventDO, EventMovieDO> eventMovieDO = eventDO.join( EVENT_MOVIE_DO_LIST, JoinType.LEFT );
    Join<TheaterDO, BookingDO> theaterDO = bookingDO.join( ID_THEATER );
    Join<TheaterDO, TheaterLanguageDO> theaterLanguageDO = theaterDO.join( THEATER_LANGUAGE_DO_LIST );
    Join<TheaterLanguageDO, LanguageDO> languageDO = theaterLanguageDO.join( ID_LANGUAGE );
    Join<RegionDO, TheaterDO> regionDO = theaterDO.join( ID_REGION );

    Join<BookingWeekDO, BookingDO> bookingWeekDO = bookingDO.join( BOOKING_WEEK_DO_LIST );
    Join<WeekDO, BookingWeekDO> weekDO = bookingWeekDO.join( ID_WEEK );
    Join<BookingStatusDO, BookingWeekDO> bookingStatusDO = bookingWeekDO.join( ID_BOOKING_STATUS );

    q.select( bookingDO );
    BookingSortFilter bookingSortFilter = new BookingSortFilter();
    bookingSortFilter.setCriteriaBuilder( cb );
    bookingSortFilter.setCriteriaQuery( q );
    bookingSortFilter.setBookingDO( bookingDO );
    bookingSortFilter.setBookingStatusDO( bookingStatusDO );
    bookingSortFilter.setEventDO( eventDO );
    bookingSortFilter.setRegionDO( regionDO );
    bookingSortFilter.setTheaterDO( theaterDO );
    bookingSortFilter.setTheaterLanguageDO( theaterLanguageDO );
    bookingSortFilter.setLanguageDO( languageDO );
    bookingSortFilter.setWeekDO( weekDO );
    bookingSortFilter.setEventMovieDO( eventMovieDO );
    bookingSortFilter.setFilters( filters );
    bookingSortFilter.setSortFields( sortFields );

    applySorting( bookingSortFilter );
    Predicate filterCondition = null;

    filterCondition = applyFilters( bookingSortFilter, new Object[] { BookingStatus.BOOKED.getId() } );
    if( filterCondition != null )
    {
      q.where( filterCondition );
    }
    return em.createQuery( q );
  }

  /**
   * Ejecucion del query jcarbajal funcion de prueba para obtener reporte
   * 
   * @param filters
   * @return
   */
  private TypedQuery<BookingDO> executeQueryFindWeeklyBookingReportTheaterTOByAllDistributors(
      Map<ModelQuery, Object> filters )
  {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    // Se ordena por nombre de cine
    List<ModelQuery> sortFields = new ArrayList<ModelQuery>();
    CriteriaQuery<BookingDO> q = cb.createQuery( BookingDO.class );
    q.distinct( Boolean.TRUE );
    Root<BookingDO> bookingDO = q.from( BookingDO.class );
    Join<EventDO, BookingDO> eventDO = bookingDO.join( ID_EVENT );
    Join<EventDO, EventMovieDO> eventMovieDO = eventDO.join( EVENT_MOVIE_DO_LIST, JoinType.LEFT );
    Join<TheaterDO, BookingDO> theaterDO = bookingDO.join( ID_THEATER );
    Join<TheaterDO, TheaterLanguageDO> theaterLanguageDO = theaterDO.join( THEATER_LANGUAGE_DO_LIST );
    Join<TheaterLanguageDO, LanguageDO> languageDO = theaterLanguageDO.join( ID_LANGUAGE );
    Join<RegionDO, TheaterDO> regionDO = theaterDO.join( ID_REGION );
    Join<BookingWeekDO, BookingDO> bookingWeekDO = bookingDO.join( BOOKING_WEEK_DO_LIST, JoinType.LEFT );
    Join<WeekDO, BookingWeekDO> weekDO = bookingWeekDO.join( ID_WEEK, JoinType.LEFT );
    Join<BookingStatusDO, BookingWeekDO> bookingStatusDO = bookingWeekDO.join( ID_BOOKING_STATUS, JoinType.LEFT );

    //jreyesv
    Join<BookingSpecialEventDO, BookingDO> bookingSpeciaEventDO = bookingDO.join( BOOKING_SPECIAL_EVENT_DO_LIST,
      JoinType.LEFT );
    Join<SpecialEventWeekDO, BookingSpecialEventDO> bookingSpeciaEventWeekDO = bookingSpeciaEventDO.join(
      BOOKING_SPECIAL_EVENT_WEEK, JoinType.LEFT );
    Join<WeekDO, SpecialEventWeekDO> specialEventWeekDO = bookingSpeciaEventWeekDO.join( ID_WEEK, JoinType.LEFT );
    Join<BookingStatusDO, BookingSpecialEventDO> bookingStatusSpecialDO = bookingSpeciaEventDO.join( ID_BOOKING_STATUS,
      JoinType.LEFT );

    q.select( bookingDO );
    BookingSortFilter bookingSortFilter = new BookingSortFilter();
    bookingSortFilter.setCriteriaBuilder( cb );
    bookingSortFilter.setCriteriaQuery( q );
    bookingSortFilter.setBookingDO( bookingDO );
    bookingSortFilter.setBookingStatusDO( bookingStatusDO );
    bookingSortFilter.setEventDO( eventDO );
    bookingSortFilter.setRegionDO( regionDO );
    bookingSortFilter.setTheaterDO( theaterDO );
    bookingSortFilter.setTheaterLanguageDO( theaterLanguageDO );
    bookingSortFilter.setLanguageDO( languageDO );
    bookingSortFilter.setWeekDO( weekDO );
    bookingSortFilter.setEventMovieDO( eventMovieDO );

    bookingSortFilter.setBookingSpecialEventDO( bookingSpeciaEventDO );
    bookingSortFilter.setBookingSpecialEventWeekDO( bookingSpeciaEventWeekDO );
    bookingSortFilter.setSpecialEventWeekDO( specialEventWeekDO );
    bookingSortFilter.setBookingStatusSpecialDO( bookingStatusSpecialDO );

    bookingSortFilter.setFilters( filters );
    bookingSortFilter.setSortFields( sortFields );
    // bookingSortFilter.setSortOrder( sortOrder );

    this.applySorting( bookingSortFilter );
    Predicate filterCondition = null;

    filterCondition = this.applyFiltersReport( bookingSortFilter, new Object[] { BookingStatus.BOOKED.getId(),
        BookingStatus.TERMINATED.getId() } );
    if( filterCondition != null )
    {
      q.where( filterCondition );
    }
    return em.createQuery( q );
  }

  /**
   * Ejecucion del query
   * 
   * @param filters
   * @return
   */
  private TypedQuery<BookingDO> executeQueryFindWeeklyBookingReportTheaterTOByDistributor(
      Map<ModelQuery, Object> filters )
  {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<BookingDO> q = cb.createQuery( BookingDO.class );
    q.distinct( Boolean.TRUE );
    Root<BookingDO> bookingDO = q.from( BookingDO.class );
    Join<EventDO, BookingDO> eventDO = bookingDO.join( ID_EVENT );
    Join<EventDO, EventMovieDO> eventMovieDO = eventDO.join( EVENT_MOVIE_DO_LIST, JoinType.LEFT );
    Join<EventMovieDO, DistributorDO> distributorDO = eventMovieDO.join( ID_DISTRIBUTOR, JoinType.LEFT );
    Join<TheaterDO, BookingDO> theaterDO = bookingDO.join( ID_THEATER );
    Join<TheaterDO, TheaterLanguageDO> theaterLanguageDO = theaterDO.join( THEATER_LANGUAGE_DO_LIST );
    Join<TheaterLanguageDO, LanguageDO> languageDO = theaterLanguageDO.join( ID_LANGUAGE );
    Join<RegionDO, TheaterDO> regionDO = theaterDO.join( ID_REGION );
    Join<BookingWeekDO, BookingDO> bookingWeekDO = bookingDO.join( BOOKING_WEEK_DO_LIST, JoinType.LEFT );
    Join<BookingStatusDO, BookingWeekDO> bookingStatusDO = bookingWeekDO.join( ID_BOOKING_STATUS, JoinType.LEFT );
    Join<WeekDO, BookingWeekDO> weekDO = bookingWeekDO.join( ID_WEEK, JoinType.LEFT );

    // Se agrega a la consulta Jcarbajal
    Join<BookingSpecialEventDO, BookingDO> bookingSpeciaEventDO = bookingDO.join( BOOKING_SPECIAL_EVENT_DO_LIST,
      JoinType.LEFT );
    Join<SpecialEventWeekDO, BookingSpecialEventDO> bookingSpeciaEventWeekDO = bookingSpeciaEventDO.join(
      BOOKING_SPECIAL_EVENT_WEEK, JoinType.LEFT );
    Join<WeekDO, SpecialEventWeekDO> specialEventWeekDO = bookingSpeciaEventWeekDO.join( ID_WEEK, JoinType.LEFT );
    Join<BookingStatusDO, BookingSpecialEventDO> bookingStatusSpecialDO = bookingSpeciaEventDO.join( ID_BOOKING_STATUS,
      JoinType.LEFT );

    q.select( bookingDO );
    BookingSortFilter bookingSortFilter = new BookingSortFilter();
    bookingSortFilter.setCriteriaBuilder( cb );
    bookingSortFilter.setCriteriaQuery( q );
    bookingSortFilter.setBookingDO( bookingDO );
    bookingSortFilter.setBookingStatusDO( bookingStatusDO );
    bookingSortFilter.setEventDO( eventDO );
    bookingSortFilter.setRegionDO( regionDO );
    bookingSortFilter.setTheaterDO( theaterDO );
    bookingSortFilter.setTheaterLanguageDO( theaterLanguageDO );
    bookingSortFilter.setLanguageDO( languageDO );
    bookingSortFilter.setWeekDO( weekDO );
    bookingSortFilter.setEventMovieDO( eventMovieDO );
    bookingSortFilter.setDistributorDO( distributorDO );
    bookingSortFilter.setFilters( filters );
    bookingSortFilter.setBookingSpecialEventDO( bookingSpeciaEventDO );
    bookingSortFilter.setBookingSpecialEventWeekDO( bookingSpeciaEventWeekDO );
    bookingSortFilter.setSpecialEventWeekDO( specialEventWeekDO );
    bookingSortFilter.setBookingStatusSpecialDO( bookingStatusSpecialDO );
    // bookingSortFilter.setSortFields( sortFields );
    // bookingSortFilter.setSortOrder( sortOrder );

    this.applySorting( bookingSortFilter );
    Predicate filterCondition = null;
    filterCondition = this.applyFiltersReport( bookingSortFilter, new Object[] { BookingStatus.BOOKED.getId(),
        BookingStatus.TERMINATED.getId() } );
    if( filterCondition != null )
    {
      q.where( filterCondition );
    }
    return em.createQuery( q );
  }

  /**
   * Se genera los objetos WeeklyBookingReportTheaterTO con la informacion obtenida en el query
   * 
   * @param bookingsTO
   * @param language
   * @param idRegion
   * @param isReportByRegion TODO
   * @return
   */
  @SuppressWarnings("unchecked")
  private List<WeeklyBookingReportTheaterTO> generateWeeklyBookingReportTheaterList( List<BookingTO> bookingsTO,
      Language language, Long idRegion, boolean isReportByRegion )
  {
    List<WeeklyBookingReportTheaterTO> weeklyBookingReportTheaterTOs = new ArrayList<WeeklyBookingReportTheaterTO>();
    WeeklyBookingReportTheaterTO theater = null;
    if( bookingsTO != null && !bookingsTO.isEmpty() )
    {
      // Se obtienen los ids de los cines
      Set<Long> ids = getTheaterIds( bookingsTO );

      for( Long id : ids )
      {
        // Despues se obtiene la lista de bookings correspondientes al cine
        List<BookingTO> bookingsTOByTheater = (List<BookingTO>) CollectionUtils.select( bookingsTO,
          new PredicateSearchByIdTheater( id ) );

        // Se obtiene la informacion del cine
        theater = new WeeklyBookingReportTheaterTO();
        BookingTO mainBookingTO = bookingsTOByTheater.get( 0 );
        theater.setIdTheater( Long.valueOf( mainBookingTO.getTheater().getNuTheater() ) );
        theater.setTheaterName( mainBookingTO.getTheater().getName() );
        theater.setDsCity( mainBookingTO.getTheater().getCity().getName() );
        theater.setDsTheater( mainBookingTO.getTheater().getName() );

        List<WeeklyBookingReportMovieTO> moviesByTheater = extractMoviesByTheater( isReportByRegion,
          bookingsTOByTheater );

        theater.setMovies( moviesByTheater );
        weeklyBookingReportTheaterTOs.add( theater );

      }

    }
    Collections.sort( weeklyBookingReportTheaterTOs, new WeeklyBookingReportTheaterTOComparator() );
    addTheatersWithoutBookings( weeklyBookingReportTheaterTOs, language, idRegion );

    return weeklyBookingReportTheaterTOs;
  }

  private List<WeeklyBookingReportMovieTO> extractMoviesByTheater( boolean isReportByRegion,
      List<BookingTO> bookingsTOByTheater )
  {
    // Se obtiene la informacion de peliculas - sala
    List<WeeklyBookingReportMovieTO> moviesByTheater = new ArrayList<WeeklyBookingReportMovieTO>();
    for( BookingTO bookingTO : bookingsTOByTheater )
    {
      if( CollectionUtils.isNotEmpty( bookingTO.getScreens() ) )
      {
        for( ScreenTO screenTO : bookingTO.getScreens() )
        {
          BookingTO booking = bookingTO;
          booking.setScreen( screenTO );
          if( isValidBookingAndScreenStatus( booking, isReportByRegion ) )
          {
            moviesByTheater.add( (WeeklyBookingReportMovieTO) new BookingTOToWeeklyBookingReportMovieTOTransformer()
                .transform( booking ) );
          }

        }
        bookingTO.setScreen( null );
      }

      if( !isReportByRegion )
      {
        CatalogTO bookingStatusBooked = this.bookingStatusDAO.get( BookingStatus.BOOKED.getId(), Language.SPANISH );
        CatalogTO bookingStatusContinue = this.bookingStatusDAO.get( BookingStatus.CONTINUE.getId(), Language.SPANISH );
        CatalogTO bookingStatusTerminated = this.bookingStatusDAO.get( BookingStatus.TERMINATED.getId(),
          Language.SPANISH );
        for( int i = 0; i < bookingTO.getCopyScreenZero(); i++ )
        {
          BookingTO booking = bookingTO;
          ScreenTO screenTO = new ScreenTO();
          if( booking.getExhibitionWeek() == 1 )
          {
            screenTO.setBookingStatus( bookingStatusBooked );
          }
          else
          {
            screenTO.setBookingStatus( bookingStatusContinue );
          }

          screenTO.setShowings( new ArrayList<CatalogTO>() );
          screenTO.setNuCapacity( 0 );
          screenTO.setNuScreen( 0 );
          booking.setScreen( screenTO );
          moviesByTheater.add( (WeeklyBookingReportMovieTO) new BookingTOToWeeklyBookingReportMovieTOTransformer()
              .transform( booking ) );
        }

        for( int i = 0; i < bookingTO.getCopyScreenZeroTerminated(); i++ )
        {
          BookingTO booking = bookingTO;
          ScreenTO screenTO = new ScreenTO();
          screenTO.setBookingStatus( bookingStatusTerminated );
          screenTO.setShowings( new ArrayList<CatalogTO>() );
          screenTO.setNuCapacity( 0 );
          screenTO.setNuScreen( 0 );
          booking.setScreen( screenTO );
          moviesByTheater.add( (WeeklyBookingReportMovieTO) new BookingTOToWeeklyBookingReportMovieTOTransformer()
              .transform( booking ) );
        }
      }
    }
    // Se ordena por Sala
    Collections.sort( moviesByTheater, new WeeklyBookingReportMovieTOComparator() );
    return moviesByTheater;
  }

  /*
   * Validates the Booking Status and Screen Status according the report type
   */
  private boolean isValidBookingAndScreenStatus( BookingTO booking, boolean isReportByRegion )
  {
    boolean flag = false;

    if( isReportByRegion )
    {
      // It's a report by region and the Booking status is Booked
      flag = booking.getScreen().getBookingStatus().getId().equals( BookingStatus.BOOKED.getIdLong() );
    }
    else if( booking.getStatus().getId().equals( BookingStatus.BOOKED.getIdLong() ) )
    {
      // It's NOT a report by region and the Booking status is Booked and the Screen Status is not Canceled
      flag = !booking.getScreen().getBookingStatus().getId().equals( BookingStatus.CANCELED.getIdLong() );
    }
    else if( booking.getStatus().getId().equals( BookingStatus.TERMINATED.getIdLong() ) )
    {
      // It's NOT a report by region and the Booking status is Terminated
      flag = true;
    }

    return flag;

  }

  private void addTheatersWithoutBookings( List<WeeklyBookingReportTheaterTO> weeklyBookingReportTheaterTOs,
      Language language, Long idRegion )
  {
    List<WeeklyBookingReportTheaterTO> theaters = new ArrayList<WeeklyBookingReportTheaterTO>();

    List<TheaterDO> theaterDOs = this.theaterDAO.findAll();
    for( TheaterDO theaterDO : theaterDOs )
    {
      if( idRegion != null && theaterDO.getIdRegion().getIdRegion().intValue() != idRegion.intValue() )
      {
        continue;
      }
      Long theaterId = (long) theaterDO.getNuTheater();
      if( theaterDO.isFgActive()
          && !CollectionUtils.exists(
            weeklyBookingReportTheaterTOs,
            PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdTheater" ),
              PredicateUtils.equalPredicate( theaterId ) ) ) )
      {
        WeeklyBookingReportTheaterTO theater = extractTheaterData( language, theaterDO );
        theaters.add( theater );
      }
    }
    Collections.sort( theaters, new WeeklyBookingReportTheaterTOComparator() );
    weeklyBookingReportTheaterTOs.addAll( theaters );

  }

  private WeeklyBookingReportTheaterTO extractTheaterData( Language language, TheaterDO theaterDO )
  {
    WeeklyBookingReportTheaterTO theater = new WeeklyBookingReportTheaterTO();
    theater.setIdTheater( Long.valueOf( theaterDO.getNuTheater() ) );
    for( TheaterLanguageDO theaterLanguageDO : theaterDO.getTheaterLanguageDOList() )
    {
      if( theaterLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
      {
        theater.setTheaterName( theaterLanguageDO.getDsName() );
        break;
      }
    }
    for( CityLanguageDO cityLanguageDO : theaterDO.getIdCity().getCityLanguageDOList() )
    {
      if( cityLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
      {
        theater.setDsCity( cityLanguageDO.getDsName() );
        break;
      }
    }
    theater.setMovies( new ArrayList<WeeklyBookingReportMovieTO>() );
    return theater;
  }

  /**
   * Se obtienen los ids de los cines
   * 
   * @param bookingsTO
   * @return
   */
  @SuppressWarnings("unchecked")
  Set<Long> getTheaterIds( List<BookingTO> bookingsTO )
  {
    // Primero Se obtienen los ids de los cines para no iterar todo
    Collection<Long> bookings = CollectionUtils.collect( bookingsTO, new Transformer()
    {
      @Override
      public Object transform( Object object )
      {
        Long idTheater = null;

        if( object instanceof BookingTO )
        {
          idTheater = ((BookingTO) object).getTheater().getId();
        }
        return idTheater;
      }
    } );
    // Se obtienen los id's unicos
    Set<Long> ids = new java.util.HashSet<Long>();
    ids.addAll( bookings );
    return ids;
  }

  /**
   * jcarbajal funcion para agregar los filtros
   * 
   * @param bookingSortFilter
   * @param status
   * @return filterCondition
   */
  private Predicate applyFiltersReport( BookingSortFilter bookingSortFilter, Object[] status )
  {
    Predicate filterCondition = null;
    if( bookingSortFilter.getFilters() != null && !bookingSortFilter.getFilters().isEmpty() )
    {
      filterCondition = bookingSortFilter.getCriteriaBuilder().conjunction();

      filterCondition = CriteriaQueryBuilder.<Long> applyFilterJoinEqualOrSecondJoinEqual(
        bookingSortFilter.getFilters(), BookingQuery.BOOKING_WEEK_ID, bookingSortFilter.getWeekDO(),
        bookingSortFilter.getSpecialEventWeekDO(), bookingSortFilter.getCriteriaBuilder(),
        bookingSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_THEATER_ID, bookingSortFilter.getTheaterDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_REGION_ID, bookingSortFilter.getRegionDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_BOOKED, bookingSortFilter.getBookingDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_ACTIVE, bookingSortFilter.getBookingDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      CriteriaBuilder cbAux = em.getCriteriaBuilder();

      // jreyesv
      filterCondition = CriteriaQueryBuilder.<Long> applyFilterJoinEqualOrSecondJoinEqualForDataSet( status,
        bookingSortFilter.getFilters(), BookingQuery.BOOKING_STATUS_ID, bookingSortFilter.getBookingStatusDO(),
        bookingSortFilter.getBookingStatusSpecialDO(), bookingSortFilter.getCriteriaBuilder(), cbAux, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinNotNull( EventQuery.EVENT_ID,
        bookingSortFilter.getEventMovieDO(), bookingSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_EVENT_ID, bookingSortFilter.getEventDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        EventQuery.EVENT_MOVIE_DISTRIBUTOR_ID, bookingSortFilter.getDistributorDO(),
        bookingSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        TheaterQuery.THEATER_ID_LANGUAGE, bookingSortFilter.getLanguageDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

    }
    return filterCondition;
  }

  /**
   * Aplica los filtros de busqueda para el reporte
   * 
   * @param bookingSortFilter
   * @param status TODO
   * @param weekDO
   * @param theaterDO
   * @return
   */
  private Predicate applyFilters( BookingSortFilter bookingSortFilter, Object[] status )
  {
    Predicate filterCondition = bookingSortFilter.getCriteriaBuilder().conjunction();

    filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
      BookingQuery.BOOKING_THEATER_ID, bookingSortFilter.getTheaterDO(), bookingSortFilter.getCriteriaBuilder(),
      filterCondition );

    filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
      BookingQuery.BOOKING_REGION_ID, bookingSortFilter.getRegionDO(), bookingSortFilter.getCriteriaBuilder(),
      filterCondition );

    filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
      BookingQuery.BOOKING_WEEK_ID, bookingSortFilter.getWeekDO(), bookingSortFilter.getCriteriaBuilder(),
      filterCondition );

    CriteriaBuilder cbAux = em.getCriteriaBuilder();

    filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqualOr( status, BookingQuery.BOOKING_STATUS_ID,
      bookingSortFilter.getBookingStatusDO(), bookingSortFilter.getCriteriaBuilder(), cbAux, filterCondition );

    filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinNotNull( EventQuery.EVENT_ID,
      bookingSortFilter.getEventMovieDO(), bookingSortFilter.getCriteriaBuilder(), filterCondition );

    filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
      EventQuery.EVENT_MOVIE_DISTRIBUTOR_ID, bookingSortFilter.getDistributorDO(),
      bookingSortFilter.getCriteriaBuilder(), filterCondition );

    filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
      TheaterQuery.THEATER_ID_LANGUAGE, bookingSortFilter.getLanguageDO(), bookingSortFilter.getCriteriaBuilder(),
      filterCondition );

    return filterCondition;
  }

  /**
   * Aplica el ordenamiento para el reporte
   * 
   * @param bookingSort
   */
  private void applySorting( BookingSortFilter bookingSort )
  {

    List<ModelQuery> sortFields = bookingSort.getSortFields();
    SortOrder sortOrder = bookingSort.getSortOrder();

    CriteriaBuilder cb = bookingSort.getCriteriaBuilder();

    CriteriaQuery<BookingDO> q = bookingSort.getCriteriaQuery();
    Root<BookingDO> bookingDO = bookingSort.getBookingDO();
    Join<BookingWeekDO, BookingDO> bookingWeekDO = bookingSort.getBookingWeekDO();
    Join<EventDO, BookingDO> eventDO = bookingSort.getEventDO();
    Join<WeekDO, BookingWeekDO> weekDO = bookingSort.getWeekDO();
    Join<TheaterDO, BookingDO> theaterDO = bookingSort.getTheaterDO();
    Join<TheaterDO, TheaterLanguageDO> theaterLanguageDO = bookingSort.getTheaterLanguageDO();
    Join<RegionDO, TheaterDO> regionDO = bookingSort.getRegionDO();
    Join<BookingStatusDO, BookingWeekDO> bookingStatusDO = bookingSort.getBookingStatusDO();

    if( sortOrder != null && CollectionUtils.isNotEmpty( sortFields ) )
    {
      List<Order> order = new ArrayList<Order>();

      Map<ModelQuery, Path<?>> sortMap = new HashMap<ModelQuery, Path<?>>();
      sortMap.put( BookingQuery.BOOKING_COPIES, bookingWeekDO );
      sortMap.put( BookingQuery.BOOKING_EXHIBITION_END, bookingWeekDO );
      sortMap.put( BookingQuery.BOOKING_EXHIBITION_WEEK, bookingWeekDO );
      sortMap.put( BookingQuery.BOOKING_ID, bookingDO );
      sortMap.put( BookingQuery.BOOKING_EVENT_DBS_CODE, eventDO );
      sortMap.put( BookingQuery.BOOKING_EVENT_ID, eventDO );
      sortMap.put( BookingQuery.BOOKING_EVENT_ID_VISTA, eventDO );
      sortMap.put( BookingQuery.BOOKING_EVENT_NAME, eventDO );
      sortMap.put( BookingQuery.BOOKING_WEEK_ID, weekDO );
      sortMap.put( BookingQuery.BOOKING_WEEK_END, weekDO );
      sortMap.put( BookingQuery.BOOKING_WEEK_START, weekDO );
      sortMap.put( BookingQuery.BOOKING_REGION_ID, regionDO );
      sortMap.put( BookingQuery.BOOKING_STATUS_ID, bookingStatusDO );
      sortMap.put( BookingQuery.BOOKING_THEATER_ID, theaterDO );
      sortMap.put( TheaterQuery.THEATER_ID, theaterDO );
      sortMap.put( TheaterQuery.THEATER_ID, theaterDO );
      sortMap.put( TheaterQuery.THEATER_NAME, theaterLanguageDO );

      for( ModelQuery sortField : sortFields )
      {
        Path<?> path = null;
        if( sortMap.containsKey( sortField ) )
        {
          path = sortMap.get( sortField ).get( sortField.getQuery() );
        }
        else
        {
          path = bookingDO.get( BookingQuery.BOOKING_EVENT_ID.getQuery() );
        }
        if( sortOrder.equals( SortOrder.ASCENDING ) )
        {
          order.add( cb.asc( path ) );
        }
        else
        {
          order.add( cb.desc( path ) );
        }
      }
      q.orderBy( order );

    }

  }

  static class BookingSortFilter
  {
    private List<ModelQuery> sortFields;
    private SortOrder sortOrder;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<BookingDO> criteriaQuery;
    private Root<BookingDO> bookingDO;
    private Join<BookingWeekDO, BookingDO> bookingWeekDO;
    private Join<BookingSpecialEventDO, BookingDO> bookingSpecialEventDO;
    private Join<EventDO, BookingDO> eventDO;
    private Join<WeekDO, BookingWeekDO> weekDO;
    private Join<TheaterDO, BookingDO> theaterDO;
    private Join<TheaterDO, TheaterLanguageDO> theaterLanguageDO;
    private Join<TheaterLanguageDO, LanguageDO> languageDO;
    private Join<RegionDO, TheaterDO> regionDO;
    private Join<BookingStatusDO, BookingWeekDO> bookingStatusDO;
    private Join<EventDO, EventMovieDO> eventMovieDO;
    private Join<EventMovieDO, DistributorDO> distributorDO;

    private Join<BookingStatusDO, BookingSpecialEventDO> bookingStatusSpecialDO;

    private Join<SpecialEventWeekDO, BookingSpecialEventDO> bookingSpecialEventWeekDO;
    private Join<WeekDO, SpecialEventWeekDO> specialEventWeekDO;

    public Join<SpecialEventWeekDO, BookingSpecialEventDO> getBookingSpecialEventWeekDO()
    {
      return bookingSpecialEventWeekDO;
    }

    public void setBookingSpecialEventWeekDO( Join<SpecialEventWeekDO, BookingSpecialEventDO> bookingSpecialEventWeekDO )
    {
      this.bookingSpecialEventWeekDO = bookingSpecialEventWeekDO;
    }

    public Join<WeekDO, SpecialEventWeekDO> getSpecialEventWeekDO()
    {
      return specialEventWeekDO;
    }

    public void setSpecialEventWeekDO( Join<WeekDO, SpecialEventWeekDO> specialEventWeekDO )
    {
      this.specialEventWeekDO = specialEventWeekDO;
    }

    /**
     * @return the bookingStatusSpecialDO
     */
    public Join<BookingStatusDO, BookingSpecialEventDO> getBookingStatusSpecialDO()
    {
      return bookingStatusSpecialDO;
    }

    /**
     * @param bookingStatusSpecialDO the bookingStatusSpecialDO to set
     */
    public void setBookingStatusSpecialDO( Join<BookingStatusDO, BookingSpecialEventDO> bookingStatusSpecialDO )
    {
      this.bookingStatusSpecialDO = bookingStatusSpecialDO;
    }

    private Map<ModelQuery, Object> filters;

    /**
     * @return the sortFields
     */
    public List<ModelQuery> getSortFields()
    {
      return sortFields;
    }

    /**
     * @param sortFields the sortFields to set
     */
    public void setSortFields( List<ModelQuery> sortFields )
    {
      this.sortFields = sortFields;
    }

    /**
     * @return the sortOrder
     */
    public SortOrder getSortOrder()
    {
      return sortOrder;
    }

    /**
     * @param sortOrder the sortOrder to set
     */
    public void setSortOrder( SortOrder sortOrder )
    {
      this.sortOrder = sortOrder;
    }

    /**
     * @return the criteriaBuilder
     */
    public CriteriaBuilder getCriteriaBuilder()
    {
      return criteriaBuilder;
    }

    /**
     * @param criteriaBuilder the criteriaBuilder to set
     */
    public void setCriteriaBuilder( CriteriaBuilder criteriaBuilder )
    {
      this.criteriaBuilder = criteriaBuilder;
    }

    public Join<BookingSpecialEventDO, BookingDO> getBookingSpecialEventDO()
    {
      return bookingSpecialEventDO;
    }

    public void setBookingSpecialEventDO( Join<BookingSpecialEventDO, BookingDO> bookingSpecialEventDO )
    {
      this.bookingSpecialEventDO = bookingSpecialEventDO;
    }

    /**
     * @return the criteriaQuery
     */
    public CriteriaQuery<BookingDO> getCriteriaQuery()
    {
      return criteriaQuery;
    }

    /**
     * @param criteriaQuery the criteriaQuery to set
     */
    public void setCriteriaQuery( CriteriaQuery<BookingDO> criteriaQuery )
    {
      this.criteriaQuery = criteriaQuery;
    }

    /**
     * @return the bookingDO
     */
    public Root<BookingDO> getBookingDO()
    {
      return bookingDO;
    }

    /**
     * @param bookingDO the bookingDO to set
     */
    public void setBookingDO( Root<BookingDO> bookingDO )
    {
      this.bookingDO = bookingDO;
    }

    /**
     * @return the eventDO
     */
    public Join<EventDO, BookingDO> getEventDO()
    {
      return eventDO;
    }

    /**
     * @param eventDO the eventDO to set
     */
    public void setEventDO( Join<EventDO, BookingDO> eventDO )
    {
      this.eventDO = eventDO;
    }

    /**
     * @return the theaterDO
     */
    public Join<TheaterDO, BookingDO> getTheaterDO()
    {
      return theaterDO;
    }

    /**
     * @param theaterDO the theaterDO to set
     */
    public void setTheaterDO( Join<TheaterDO, BookingDO> theaterDO )
    {
      this.theaterDO = theaterDO;
    }

    /**
     * @return the theaterLanguageDO
     */
    public Join<TheaterDO, TheaterLanguageDO> getTheaterLanguageDO()
    {
      return theaterLanguageDO;
    }

    /**
     * @param theaterLanguageDO the theaterLanguageDO to set
     */
    public void setTheaterLanguageDO( Join<TheaterDO, TheaterLanguageDO> theaterLanguageDO )
    {
      this.theaterLanguageDO = theaterLanguageDO;
    }

    /**
     * @return the languageDO
     */
    public Join<TheaterLanguageDO, LanguageDO> getLanguageDO()
    {
      return languageDO;
    }

    /**
     * @param languageDO the languageDO to set
     */
    public void setLanguageDO( Join<TheaterLanguageDO, LanguageDO> languageDO )
    {
      this.languageDO = languageDO;
    }

    /**
     * @return the regionDO
     */
    public Join<RegionDO, TheaterDO> getRegionDO()
    {
      return regionDO;
    }

    /**
     * @param regionDO the regionDO to set
     */
    public void setRegionDO( Join<RegionDO, TheaterDO> regionDO )
    {
      this.regionDO = regionDO;
    }

    /**
     * @return the eventMovieDO
     */
    public Join<EventDO, EventMovieDO> getEventMovieDO()
    {
      return eventMovieDO;
    }

    /**
     * @param eventMovieDO the eventMovieDO to set
     */
    public void setEventMovieDO( Join<EventDO, EventMovieDO> eventMovieDO )
    {
      this.eventMovieDO = eventMovieDO;
    }

    /**
     * @return the distributorDO
     */
    public Join<EventMovieDO, DistributorDO> getDistributorDO()
    {
      return distributorDO;
    }

    /**
     * @param distributorDO the distributorDO to set
     */
    public void setDistributorDO( Join<EventMovieDO, DistributorDO> distributorDO )
    {
      this.distributorDO = distributorDO;
    }

    /**
     * @return the filters
     */
    public Map<ModelQuery, Object> getFilters()
    {
      return filters;
    }

    /**
     * @param filters the filters to set
     */
    public void setFilters( Map<ModelQuery, Object> filters )
    {
      this.filters = filters;
    }

    /**
     * @return the bookingWeekDO
     */
    public Join<BookingWeekDO, BookingDO> getBookingWeekDO()
    {
      return bookingWeekDO;
    }

    /**
     * @param bookingWeekDO the bookingWeekDO to set
     */
    public void setBookingWeekDO( Join<BookingWeekDO, BookingDO> bookingWeekDO )
    {
      this.bookingWeekDO = bookingWeekDO;
    }

    /**
     * @return the weekDO
     */
    public Join<WeekDO, BookingWeekDO> getWeekDO()
    {
      return weekDO;
    }

    /**
     * @param weekDO the weekDO to set
     */
    public void setWeekDO( Join<WeekDO, BookingWeekDO> weekDO )
    {
      this.weekDO = weekDO;
    }

    /**
     * @return the bookingStatusDO
     */
    public Join<BookingStatusDO, BookingWeekDO> getBookingStatusDO()
    {
      return bookingStatusDO;
    }

    /**
     * @param bookingStatusDO the bookingStatusDO to set
     */
    public void setBookingStatusDO( Join<BookingStatusDO, BookingWeekDO> bookingStatusDO )
    {
      this.bookingStatusDO = bookingStatusDO;
    }

  }

}
