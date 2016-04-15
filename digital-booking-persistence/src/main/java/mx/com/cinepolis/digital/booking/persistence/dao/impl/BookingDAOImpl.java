package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mx.com.cinepolis.digital.booking.commons.constants.BookingShow;
import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.constants.BookingTicketSemaphore;
import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.AbstractTOUtils;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.commons.utils.ScreenTOComparator;
import mx.com.cinepolis.digital.booking.dao.util.BookingDAOUtil;
import mx.com.cinepolis.digital.booking.dao.util.BookingDOToBookingTOReportTransformer;
import mx.com.cinepolis.digital.booking.dao.util.BookingDOToBookingTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.EventDOToEventTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.PresaleDOToPresaleTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.TheaterDOToTheaterTOTransformer;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO;
import mx.com.cinepolis.digital.booking.model.BookingStatusDO;
import mx.com.cinepolis.digital.booking.model.BookingTypeDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekScreenDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekScreenShowDO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.ObservationDO;
import mx.com.cinepolis.digital.booking.model.PresaleDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.SpecialEventWeekDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingStatusDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingTypeDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekScreenShowDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ObservationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class BookingDAOImpl extends AbstractBaseDAO<BookingDO> implements BookingDAO
{

  private static final String GET_ID_SCREEN = "getIdScreen";

  private static final String ID_REGION = "idRegion";

  private static final String ID_THEATER = "idTheater";

  private static final String ID_WEEK = "idWeek";

  private static final String ID_BOOKING_STATUS = "idBookingStatus";

  private static final String BOOKING_WEEK_SCREEN_DO_LIST = "bookingWeekScreenDOList";

  private static final String BOOKING_WEEK_DO_LIST = "bookingWeekDOList";

  private static final String ID_EVENT = "idEvent";

  private static final String BOOKING_SPECIAL_EVENT_DO_LIST = "bookingSpecialEventDOList";

  private static final String BOOKING_SPECIAL_EVENT_SCREEN_DO_LIST = "bookingSpecialEventScreenDOList";

  private static final String BOOKING_SCREEN_PRESALE_DO_LIST = "presaleDOList";

  private static final String BOOKING_SPECIAL_EVENT_WEEK = "specialEventWeekDOList";

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @EJB
  private BookingStatusDAO bookingStatusDAO;

  @EJB
  private BookingWeekDAO bookingWeekDAO;

  @EJB
  private EventDAO eventDAO;

  @EJB
  private TheaterDAO theaterDAO;

  @EJB
  private WeekDAO weekDAO;

  @EJB
  private ScreenDAO screenDAO;

  @EJB
  private BookingWeekScreenDAO bookingWeekScreenDAO;

  @EJB
  private BookingWeekScreenShowDAO bookingWeekScreenShowDAO;

  @EJB
  private ObservationDAO observationDAO;

  @EJB
  private UserDAO userDAO;

  @EJB
  private BookingTypeDAO bookingTypeDAO;

  @EJB
  private BookingSpecialEventDAO bookingSpecialEventDAO;

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
  public BookingDAOImpl()
  {
    super( BookingDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( BookingDO bookingDO )
  {
    BookingDO remove = this.find( bookingDO.getIdBooking() );
    if( remove != null )
    {
      remove.setFgActive( false );
      remove.setIdLastUserModifier( bookingDO.getIdLastUserModifier() );
      remove.setDtLastModification( bookingDO.getDtLastModification() );
      this.edit( remove );
    }
  }

  /**
   * {@inheritDoc}
   */

  @Override
  public PagingResponseTO<BookingTO> findPresaleBookingsByIdEventIdRegionAndIdWeek( PagingRequestTO pagingRequestTO )
  {

    Boolean isDistinct = Boolean.TRUE;
    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = getFilters( pagingRequestTO );
    Language language = pagingRequestTO.getLanguage();
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<BookingDO> q = cb.createQuery( BookingDO.class );
    Root<BookingDO> bookingDO = q.from( BookingDO.class );
    Join<EventDO, BookingDO> eventDO = bookingDO.join( ID_EVENT );
    Join<BookingWeekDO, BookingDO> bookingWeekDO = bookingDO.join( BOOKING_WEEK_DO_LIST, JoinType.LEFT );
    Join<WeekDO, BookingDO> weekDO = bookingWeekDO.join( ID_WEEK, JoinType.LEFT );
    Join<TheaterDO, BookingDO> theaterDO = bookingDO.join( ID_THEATER );
    Join<RegionDO, TheaterDO> regionDO = theaterDO.join( ID_REGION );
    // jreyesv
    Join<BookingSpecialEventDO, BookingDO> bookingSpeciaEventDO = bookingDO.join( BOOKING_SPECIAL_EVENT_DO_LIST,
      JoinType.LEFT );
    Join<SpecialEventWeekDO, BookingSpecialEventDO> bookingSpeciaEventWeekDO = bookingSpeciaEventDO.join(
      BOOKING_SPECIAL_EVENT_WEEK, JoinType.LEFT );
    Join<WeekDO, SpecialEventWeekDO> specialEventWeekDO = bookingSpeciaEventWeekDO.join( ID_WEEK, JoinType.LEFT );
    // Presale join
    Join<BookingSpecialEventScreenDO, BookingSpecialEventDO> bookingSpeciaEventScreenDO = bookingSpeciaEventDO.join(
      BOOKING_SPECIAL_EVENT_SCREEN_DO_LIST, JoinType.LEFT );
    Join<PresaleDO, BookingSpecialEventScreenDO> specialEventScreenPresaleDO = bookingSpeciaEventScreenDO.join(
      BOOKING_SCREEN_PRESALE_DO_LIST, JoinType.LEFT );
    Join<BookingWeekScreenDO, BookingWeekDO> bookingWeekScreenDO = bookingWeekDO.join( BOOKING_WEEK_SCREEN_DO_LIST,
      JoinType.LEFT );
    Join<PresaleDO, BookingWeekScreenDO> weekScreenPresaleDO = bookingWeekScreenDO.join(
      BOOKING_SCREEN_PRESALE_DO_LIST, JoinType.LEFT );

    q.select( bookingDO );
    q.distinct( isDistinct );
    BookingSortFilter bookingSortFilter = new BookingSortFilter();
    bookingSortFilter.setSortFields( sortFields );
    bookingSortFilter.setSortOrder( sortOrder );
    bookingSortFilter.setCriteriaBuilder( cb );
    bookingSortFilter.setCriteriaQuery( q );
    bookingSortFilter.setBookingDO( bookingDO );
    bookingSortFilter.setBookingWeekDO( bookingWeekDO );
    bookingSortFilter.setBookingSpecialEventWeekDO( bookingSpeciaEventWeekDO );
    bookingSortFilter.setSpecialEventWeekDO( specialEventWeekDO );
    bookingSortFilter.setEventDO( eventDO );
    bookingSortFilter.setRegionDO( regionDO );
    bookingSortFilter.setTheaterDO( theaterDO );
    bookingSortFilter.setWeekDO( weekDO );

    // jreyesv
    // Set presale join in filter
    bookingSortFilter.setBookingSpeciaEventDO( bookingSpeciaEventDO );
    bookingSortFilter.setBookingSpeciaEventScreenDO( bookingSpeciaEventScreenDO );
    bookingSortFilter.setSpecialEventScreenPresaleDO( specialEventScreenPresaleDO );
    bookingSortFilter.setBookingWeekScreenDO( bookingWeekScreenDO );
    bookingSortFilter.setWeekScreenPresaleDO( weekScreenPresaleDO );

    bookingSortFilter.setFilters( filters );
    this.applySorting( bookingSortFilter );

    Predicate filterCondition = this.applyFiltersToBookingPresale( bookingSortFilter );

    CriteriaQuery<BookingDO> queryCountRecords = cb.createQuery( BookingDO.class );
    queryCountRecords.select( eventDO ).distinct( isDistinct );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );
    }

    TypedQuery<BookingDO> tq = em.createQuery( q );
    int count = em.createQuery( queryCountRecords ).getResultList().size();

    PagingResponseTO<BookingTO> response = new PagingResponseTO<BookingTO>();
    BookingDOToBookingTOReportTransformer transformer = new BookingDOToBookingTOReportTransformer( language );
    if( pagingRequestTO.getFilters() != null && pagingRequestTO.getFilters().containsKey( BookingQuery.BOOKING_WEEK_ID ) )
    {
      Integer idWeek = ((Number) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_WEEK_ID )).intValue();
      transformer.setIdWeek( idWeek );
    }

    List<BookingDO> bookingDOs = tq.getResultList();

    List<BookingTO> bookingsTO = new ArrayList<BookingTO>();
    if( CollectionUtils.isNotEmpty( bookingDOs ) )
    {
      for( BookingDO bookingDo : bookingDOs )
      {
        BookingTO bookingTON = new BookingTO();
        bookingTON = (BookingTO) transformer.transform( bookingDo );
        bookingsTO.add( bookingTON );
      }
    }
    response.setElements( bookingsTO );
    response.setTotalCount( count );
    return response;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public PagingResponseTO<EventTO> findPresaleBookingsByPaging( PagingRequestTO pagingRequestTO )
  {
    Boolean isDistinct = Boolean.TRUE;
    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = getFilters( pagingRequestTO );
    Language language = pagingRequestTO.getLanguage();
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<BookingDO> q = cb.createQuery( BookingDO.class );
    Root<BookingDO> bookingDO = q.from( BookingDO.class );
    Join<EventDO, BookingDO> eventDO = bookingDO.join( ID_EVENT );
    Join<BookingWeekDO, BookingDO> bookingWeekDO = bookingDO.join( BOOKING_WEEK_DO_LIST, JoinType.LEFT );
    Join<WeekDO, BookingDO> weekDO = bookingWeekDO.join( ID_WEEK, JoinType.LEFT );
    Join<TheaterDO, BookingDO> theaterDO = bookingDO.join( ID_THEATER );
    Join<RegionDO, TheaterDO> regionDO = theaterDO.join( ID_REGION );
    Join<BookingSpecialEventDO, BookingDO> bookingSpeciaEventDO = bookingDO.join( BOOKING_SPECIAL_EVENT_DO_LIST,
      JoinType.LEFT );

    // jreyesv
    Join<SpecialEventWeekDO, BookingSpecialEventDO> bookingSpeciaEventWeekDO = bookingSpeciaEventDO.join(
      BOOKING_SPECIAL_EVENT_WEEK, JoinType.LEFT );
    Join<WeekDO, SpecialEventWeekDO> specialEventWeekDO = bookingSpeciaEventWeekDO.join( ID_WEEK, JoinType.LEFT );
    // Presale join
    Join<BookingSpecialEventScreenDO, BookingSpecialEventDO> bookingSpeciaEventScreenDO = bookingSpeciaEventDO.join(
      BOOKING_SPECIAL_EVENT_SCREEN_DO_LIST, JoinType.LEFT );
    Join<PresaleDO, BookingSpecialEventScreenDO> specialEventScreenPresaleDO = bookingSpeciaEventScreenDO.join(
      BOOKING_SCREEN_PRESALE_DO_LIST, JoinType.LEFT );
    Join<BookingWeekScreenDO, BookingWeekDO> bookingWeekScreenDO = bookingWeekDO.join( BOOKING_WEEK_SCREEN_DO_LIST,
      JoinType.LEFT );
    Join<PresaleDO, BookingWeekScreenDO> weekScreenPresaleDO = bookingWeekScreenDO.join(
      BOOKING_SCREEN_PRESALE_DO_LIST, JoinType.LEFT );

    q.select( eventDO );
    q.distinct( isDistinct );
    BookingSortFilter bookingSortFilter = new BookingSortFilter();
    bookingSortFilter.setSortFields( sortFields );
    bookingSortFilter.setSortOrder( sortOrder );
    bookingSortFilter.setCriteriaBuilder( cb );
    bookingSortFilter.setCriteriaQuery( q );
    bookingSortFilter.setBookingDO( bookingDO );
    bookingSortFilter.setBookingWeekDO( bookingWeekDO );

    // jreyesv
    bookingSortFilter.setBookingSpecialEventWeekDO( bookingSpeciaEventWeekDO );
    bookingSortFilter.setSpecialEventWeekDO( specialEventWeekDO );
    // Set presale join in filter
    bookingSortFilter.setBookingSpeciaEventDO( bookingSpeciaEventDO );
    bookingSortFilter.setBookingSpeciaEventScreenDO( bookingSpeciaEventScreenDO );
    bookingSortFilter.setSpecialEventScreenPresaleDO( specialEventScreenPresaleDO );
    bookingSortFilter.setBookingWeekScreenDO( bookingWeekScreenDO );
    bookingSortFilter.setWeekScreenPresaleDO( weekScreenPresaleDO );

    bookingSortFilter.setEventDO( eventDO );
    bookingSortFilter.setRegionDO( regionDO );
    bookingSortFilter.setTheaterDO( theaterDO );
    bookingSortFilter.setWeekDO( weekDO );
    bookingSortFilter.setFilters( filters );
    this.applySorting( bookingSortFilter );

    Predicate filterCondition = this.applyFiltersToBookingPresale( bookingSortFilter );

    CriteriaQuery<BookingDO> queryCountRecords = cb.createQuery( BookingDO.class );
    queryCountRecords.select( eventDO ).distinct( isDistinct );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );
    }

    TypedQuery<BookingDO> tq = em.createQuery( queryCountRecords );
    int count = em.createQuery( queryCountRecords ).getResultList().size();
    if( pagingRequestTO.getNeedsPaging() )
    {
      int page = pagingRequestTO.getPage();
      int pageSize = pagingRequestTO.getPageSize();
      if( pageSize > 0 )
      {
        tq.setMaxResults( pageSize );
      }
      if( page >= 0 )
      {
        tq.setFirstResult( page * pageSize );
      }
    }
    PagingResponseTO<EventTO> response = new PagingResponseTO<EventTO>();

    EventDOToEventTOTransformer transformer = new EventDOToEventTOTransformer( language );
    List<BookingDO> eventDOs = tq.getResultList();
    response.setElements( (List<EventTO>) CollectionUtils.collect( eventDOs, transformer ) );
    response.setTotalCount( count );

    return response;
  }

  private Predicate applyFiltersToBookingPresale( BookingSortFilter bookingSortFilter )
  {
    Predicate filterCondition = null;
    if( bookingSortFilter.getFilters() != null && !bookingSortFilter.getFilters().isEmpty() )
    {
      filterCondition = bookingSortFilter.getCriteriaBuilder().conjunction();

      filterCondition = CriteriaQueryBuilder.<Long> applyFilterJoinEqualOrSecondJoinEqual(
        bookingSortFilter.getFilters(), BookingQuery.BOOKING_WEEK_ID, bookingSortFilter.getWeekDO(),
        bookingSortFilter.getSpecialEventWeekDO(), bookingSortFilter.getCriteriaBuilder(),
        bookingSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinLike( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_EVENT_NAME, bookingSortFilter.getEventDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_REGION_ID, bookingSortFilter.getRegionDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_STATUS_ID, bookingSortFilter.getBookingWeekStatusDO(),
        bookingSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_STATUS_ID, bookingSortFilter.getBookingWeekScreenStatusDO(),
        bookingSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_ACTIVE, bookingSortFilter.getBookingDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );
      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_BOOKED, bookingSortFilter.getBookingDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      /**
       * TODO: verificar este filtro para la consulta.
       */
      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterJoinEqualOrSecondJoinEqual(
        bookingSortFilter.getFilters(), BookingQuery.BOOKING_PRESALE_ACTIVE,
        bookingSortFilter.getWeekScreenPresaleDO(), bookingSortFilter.getSpecialEventScreenPresaleDO(),
        bookingSortFilter.getCriteriaBuilder(), bookingSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_EVENT_ID, bookingSortFilter.getEventDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );
      filterCondition = CriteriaQueryBuilder.applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_THEATER_ID, bookingSortFilter.theaterDO, bookingSortFilter.getCriteriaBuilder(),
        filterCondition );
    }
    return filterCondition;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public PagingResponseTO<BookingTO> findAllByPaging( PagingRequestTO pagingRequestTO )
  {
    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = getFilters( pagingRequestTO );
    Language language = pagingRequestTO.getLanguage();

    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<BookingDO> q = cb.createQuery( BookingDO.class );
    q.distinct( Boolean.TRUE );
    Root<BookingDO> bookingDO = q.from( BookingDO.class );
    Join<EventDO, BookingDO> eventDO = bookingDO.join( ID_EVENT );
    Join<BookingWeekDO, BookingDO> bookingWeekDO = bookingDO.join( BOOKING_WEEK_DO_LIST );
    Join<BookingWeekScreenDO, BookingWeekDO> bookingWeekScreenDO = bookingWeekDO.join( BOOKING_WEEK_SCREEN_DO_LIST,
      JoinType.LEFT );
    Join<BookingStatusDO, BookingWeekScreenDO> bookingWeekScreenStatusDO = bookingWeekScreenDO.join( ID_BOOKING_STATUS,
      JoinType.LEFT );
    Join<WeekDO, BookingDO> weekDO = bookingWeekDO.join( ID_WEEK );
    Join<TheaterDO, BookingDO> theaterDO = bookingDO.join( ID_THEATER );
    Join<RegionDO, TheaterDO> regionDO = theaterDO.join( ID_REGION );
    Join<BookingStatusDO, BookingWeekDO> bookingWeekStatusDO = bookingWeekDO.join( ID_BOOKING_STATUS );
    Join<BookingSpecialEventDO, BookingDO> bookingSpeciaEventDO = bookingDO.join( BOOKING_SPECIAL_EVENT_DO_LIST,
      JoinType.LEFT );
    Join<BookingSpecialEventScreenDO, BookingSpecialEventDO> bookingSpeciaEventScreenDO = bookingSpeciaEventDO.join(
      BOOKING_SPECIAL_EVENT_SCREEN_DO_LIST, JoinType.LEFT );

    q.select( bookingDO );
    BookingSortFilter bookingSortFilter = new BookingSortFilter();
    bookingSortFilter.setSortFields( sortFields );
    bookingSortFilter.setSortOrder( sortOrder );
    bookingSortFilter.setCriteriaBuilder( cb );
    bookingSortFilter.setCriteriaQuery( q );
    bookingSortFilter.setBookingDO( bookingDO );
    bookingSortFilter.setBookingWeekDO( bookingWeekDO );
    bookingSortFilter.setBookingWeekScreenDO( bookingWeekScreenDO );
    bookingSortFilter.setBookingWeekScreenStatusDO( bookingWeekScreenStatusDO );
    bookingSortFilter.setBookingWeekStatusDO( bookingWeekStatusDO );
    bookingSortFilter.setEventDO( eventDO );
    bookingSortFilter.setRegionDO( regionDO );
    bookingSortFilter.setTheaterDO( theaterDO );
    bookingSortFilter.setWeekDO( weekDO );
    bookingSortFilter.setBookingSpeciaEventDO( bookingSpeciaEventDO );
    bookingSortFilter.setBookingSpeciaEventScreenDO( bookingSpeciaEventScreenDO );
    bookingSortFilter.setFilters( filters );
    applySorting( bookingSortFilter );

    Predicate filterCondition = applyFilters( bookingSortFilter );

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( bookingDO ) );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );
    }

    TypedQuery<BookingDO> tq = em.createQuery( q );
    int count = em.createQuery( queryCountRecords ).getSingleResult().intValue();
    if( pagingRequestTO.getNeedsPaging() )
    {
      int page = pagingRequestTO.getPage();
      int pageSize = pagingRequestTO.getPageSize();
      if( pageSize > 0 )
      {
        tq.setMaxResults( pageSize );
      }
      if( page >= 0 )
      {
        tq.setFirstResult( page * pageSize );
      }
    }
    PagingResponseTO<BookingTO> response = new PagingResponseTO<BookingTO>();

    BookingDOToBookingTOTransformer transformer = new BookingDOToBookingTOTransformer( language );
    if( pagingRequestTO.getFilters() != null && pagingRequestTO.getFilters().containsKey( BookingQuery.BOOKING_WEEK_ID ) )
    {
      transformer.setIdWeek( ((Number) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_WEEK_ID )).intValue() );
    }

    response.setElements( (List<BookingTO>) CollectionUtils.collect( tq.getResultList(), transformer ) );
    response.setTotalCount( count );

    return response;
  }

  private Predicate applyFilters( BookingSortFilter bookingSortFilter )
  {
    Predicate filterCondition = null;
    if( bookingSortFilter.getFilters() != null && !bookingSortFilter.getFilters().isEmpty() )
    {
      filterCondition = bookingSortFilter.getCriteriaBuilder().conjunction();

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_ID, bookingSortFilter.getBookingDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );
      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_EXHIBITION_WEEK, bookingSortFilter.getBookingWeekDO(),
        bookingSortFilter.getCriteriaBuilder(), filterCondition );
      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_COPIES, bookingSortFilter.getBookingWeekDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );
      filterCondition = CriteriaQueryBuilder.applyFilterJoinEqualDate( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_EXHIBITION_END, bookingSortFilter.getBookingWeekDO(),
        bookingSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_EVENT_ID, bookingSortFilter.getEventDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );
      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_EVENT_ID_VISTA, bookingSortFilter.getEventDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );
      filterCondition = CriteriaQueryBuilder.applyFilterJoinLike( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_EVENT_NAME, bookingSortFilter.getEventDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );
      filterCondition = CriteriaQueryBuilder.applyFilterJoinLike( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_EVENT_DBS_CODE, bookingSortFilter.getEventDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_WEEK_ID, bookingSortFilter.getWeekDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );
      filterCondition = CriteriaQueryBuilder.applyFilterJoinEqualDate( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_WEEK_START, bookingSortFilter.getWeekDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );
      filterCondition = CriteriaQueryBuilder.applyFilterJoinEqualDate( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_WEEK_END, bookingSortFilter.getWeekDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_REGION_ID, bookingSortFilter.getRegionDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_STATUS_ID, bookingSortFilter.getBookingWeekStatusDO(),
        bookingSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_STATUS_ID, bookingSortFilter.getBookingWeekScreenStatusDO(),
        bookingSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_THEATER_ID, bookingSortFilter.getTheaterDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_ACTIVE, bookingSortFilter.getBookingDO(), bookingSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_EVENT_SPECIAL_EVENT, bookingSortFilter.bookingSpeciaEventDO,
        bookingSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( bookingSortFilter.getFilters(),
        BookingQuery.BOOKING_EVENTSPECIAL_EVENT_SCREEN, bookingSortFilter.bookingSpeciaEventScreenDO,
        bookingSortFilter.getCriteriaBuilder(), filterCondition );
    }
    return filterCondition;
  }

  private void applySorting( BookingSortFilter bookingSort )
  {

    List<ModelQuery> sortFields = bookingSort.getSortFields();
    SortOrder sortOrder = bookingSort.getSortOrder();

    CriteriaBuilder cb = bookingSort.getCriteriaBuilder();

    CriteriaQuery<BookingDO> q = bookingSort.getCriteriaQuery();
    Root<BookingDO> bookingDO = bookingSort.getBookingDO();
    Join<EventDO, BookingDO> eventDO = bookingSort.getEventDO();
    Join<WeekDO, BookingDO> weekDO = bookingSort.getWeekDO();
    Join<TheaterDO, BookingDO> theaterDO = bookingSort.getTheaterDO();
    Join<RegionDO, TheaterDO> regionDO = bookingSort.getRegionDO();
    Join<BookingWeekDO, BookingDO> bookingWeekDO = bookingSort.getBookingWeekDO();
    Join<BookingStatusDO, BookingWeekDO> bookingWeekStatusDO = bookingSort.getBookingWeekStatusDO();
    Join<BookingSpecialEventDO, BookingDO> bookingSpecialEventDO = bookingSort.getBookingSpeciaEventDO();
    Join<BookingSpecialEventScreenDO, BookingSpecialEventDO> bookingSpecialEventScreenDO = bookingSort
        .getBookingSpeciaEventScreenDO();

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
      sortMap.put( BookingQuery.BOOKING_STATUS_ID, bookingWeekStatusDO );
      sortMap.put( BookingQuery.BOOKING_THEATER_ID, theaterDO );
      sortMap.put( BookingQuery.BOOKING_EVENT_SPECIAL_EVENT, bookingSpecialEventDO );
      sortMap.put( BookingQuery.BOOKING_EVENTSPECIAL_EVENT_SCREEN, bookingSpecialEventScreenDO );

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

  private Map<ModelQuery, Object> getFilters( PagingRequestTO pagingRequestTO )
  {
    Map<ModelQuery, Object> filters = pagingRequestTO.getFilters();
    if( filters == null )
    {
      filters = new HashMap<ModelQuery, Object>();
    }
    return filters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( BookingTO bookingTO )
  {
    BookingDO bookingDO = new BookingDO();
    AbstractEntityUtils.applyElectronicSign( bookingDO, bookingTO );
    bookingDO.setFgBooked( bookingTO.getStatus().getId().equals( BookingStatus.BOOKED.getIdLong() ) );

    bookingDO.setIdEvent( getEvent( bookingTO.getEvent().getIdEvent() ) );
    bookingDO.setIdTheater( getTheater( bookingTO.getTheater().getId().intValue() ) );
    bookingDO.setIdBookingType( getBookingType( bookingTO.getIdBookingType() ) );

    BookingWeekDO bookingWeekDO = new BookingWeekDO();
    AbstractEntityUtils.applyElectronicSign( bookingWeekDO, bookingTO );
    BookingStatusDO idBookingStatus = getBookingStatus( BookingStatus.BOOKED.getId() );
    bookingWeekDO.setIdBookingStatus( idBookingStatus );
    bookingWeekDO.setIdWeek( getWeek( bookingTO.getWeek().getIdWeek() ) );
    bookingWeekDO.setNuExhibitionWeek( bookingTO.getExhibitionWeek() );
    bookingWeekDO.setQtCopy( bookingTO.getCopy() );
    bookingWeekDO.setQtCopyScreenZero( bookingTO.getCopyScreenZero() );
    bookingWeekDO.setQtCopyScreenZeroTerminated( 0 );
    bookingWeekDO.setDtExhibitionEndDate( bookingTO.getExhibitionEnd() );

    if( CollectionUtils.isNotEmpty( bookingTO.getScreens() ) )
    {
      List<BookingWeekScreenDO> bookingWeekScreenDOList = new ArrayList<BookingWeekScreenDO>();
      for( ScreenTO screenTO : bookingTO.getScreens() )
      {
        if( screenTO.getId() != null && screenTO.getId().intValue() != 0 )
        {
          ScreenDO screenDO = screenDAO.find( screenTO.getId().intValue() );

          BookingWeekScreenDO bookingWeekScreenDO = new BookingWeekScreenDO();
          bookingWeekScreenDO.setIdBookingStatus( idBookingStatus );
          bookingWeekScreenDO.setIdBookingWeek( bookingWeekDO );
          bookingWeekScreenDO.setIdScreen( screenDO );

          updateScreenStatusAndShows( screenTO, bookingWeekScreenDO );

          bookingWeekScreenDOList.add( bookingWeekScreenDO );
        }
      }

      bookingWeekDO.setBookingWeekScreenDOList( bookingWeekScreenDOList );
    }

    bookingWeekDO.setIdBooking( bookingDO );
    bookingDO.setBookingWeekDOList( Arrays.asList( bookingWeekDO ) );

    this.create( bookingDO );
    this.flush();
    bookingTO.setId( bookingDO.getIdBooking() );
  }

  private WeekDO getWeek( Integer id )
  {
    WeekDO weekDO = this.weekDAO.find( id );
    if( weekDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_PERSISTENCE_ERROR_WEEK_NOT_FOUND );
    }
    return weekDO;
  }

  private TheaterDO getTheater( int id )
  {
    TheaterDO theaterDO = this.theaterDAO.find( id );
    if( theaterDO == null )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.BOOKING_PERSISTENCE_ERROR_THEATER_NOT_FOUND );
    }
    return theaterDO;
  }

  private EventDO getEvent( Long idEvent )
  {
    EventDO eventDO = this.eventDAO.find( idEvent );
    if( eventDO == null )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.BOOKING_PERSISTENCE_ERROR_EVENT_NOT_FOUND );
    }
    return eventDO;
  }

  private BookingTypeDO getBookingType( int idEventType )
  {
    BookingTypeDO bookingType = this.bookingTypeDAO.find( idEventType );
    if( bookingType == null )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.BOOKING_PERSISTENCE_ERROR_EVENT_TYPE_NOT_FOUND );
    }
    return bookingType;
  }

  private BookingStatusDO getBookingStatus( int id )
  {
    BookingStatusDO status = this.bookingStatusDAO.find( id );
    if( status == null )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.BOOKING_PERSISTENCE_ERROR_STATUS_NOT_FOUND );
    }
    return status;
  }

  /**
   * {@inheritDoc}
   * 
   * @param bookingTO
   */
  @Override
  public void update( BookingTO bookingTO )
  {
    BookingDO bookingDO = this.find( bookingTO.getId() );
    if( bookingDO == null )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.BOOKING_PERSISTENCE_ERROR_BOOKING_NOT_FOUND );
    }
    AbstractEntityUtils.applyElectronicSign( bookingDO, bookingTO );

    BookingStatusDO status = null;
    BookingWeekDO bookingWeekDO = null;
    WeekTO weekTO = this.weekDAO.getWeek( bookingTO.getWeek().getIdWeek() );
    Date start = weekTO.getStartingDayWeek();
    if( CollectionUtils.isNotEmpty( bookingDO.getBookingWeekDOList() ) )
    {
      for( BookingWeekDO bookingWeek : bookingDO.getBookingWeekDOList() )
      {
        if( bookingWeek.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.BOOKED.getId() )
            && bookingWeek.getIdWeek().getDtStartingDayWeek().getTime() < start.getTime() )
        {
          start = bookingWeek.getIdWeek().getDtStartingDayWeek();
        }
        if( bookingWeek.getIdWeek().getIdWeek().equals( bookingTO.getWeek().getIdWeek() ) )
        {
          bookingWeekDO = bookingWeek;
          AbstractEntityUtils.applyElectronicSign( bookingWeekDO, bookingTO );

          bookingWeekDO.setDtExhibitionEndDate( bookingTO.getExhibitionEnd() );
          bookingWeekDO.setQtCopy( bookingTO.getCopy() );
          bookingWeekDO.setQtCopyScreenZero( bookingTO.getCopyScreenZero() );
          bookingWeekDO.setQtCopyScreenZeroTerminated( bookingTO.getCopyScreenZeroTerminated() );

          List<ScreenTO> filteredScreens = filterRepeatedScreens( bookingTO.getScreens() );

          List<BookingWeekScreenDO> bookingWeekScreenDOList = getSelectedScreens(
            bookingWeekDO.getBookingWeekScreenDOList(), filteredScreens, bookingWeekDO );

          status = extractBookingStatus( bookingWeekDO, bookingWeekScreenDOList, bookingTO );
          bookingWeekDO.setIdBookingStatus( status );

          bookingWeekDO.setBookingWeekScreenDOList( bookingWeekScreenDOList );
          break;
        }
      }

    }

    if( bookingWeekDO != null )
    {
      updateBookingStatusFoward( bookingWeekDO );
    }
    else
    {
      status = getBookingStatus( BookingStatus.BOOKED.getId() );
      bookingWeekDO = new BookingWeekDO();
      AbstractEntityUtils.applyElectronicSign( bookingWeekDO, bookingTO );
      bookingWeekDO.setIdWeek( getWeek( bookingTO.getWeek().getIdWeek() ) );
      bookingWeekDO.setDtExhibitionEndDate( bookingTO.getExhibitionEnd() );
      bookingWeekDO.setIdBookingStatus( status );
      bookingWeekDO.setIdBooking( bookingDO );

      int exhibitionWeek = this.weekDAO.countWeeks( start, weekTO.getStartingDayWeek() );
      if( weekTO.isSpecialWeek() )
      {
        exhibitionWeek++;
      }

      bookingWeekDO.setNuExhibitionWeek( exhibitionWeek );
      bookingWeekDO.setQtCopy( bookingTO.getCopy() );
      List<BookingWeekScreenDO> screens = new ArrayList<BookingWeekScreenDO>();
      List<ScreenTO> filteredScreens = filterRepeatedScreens( bookingTO.getScreens() );
      bookingWeekDO.setBookingWeekScreenDOList( getSelectedScreens( screens, filteredScreens, bookingWeekDO ) );
      bookingDO.getBookingWeekDOList().add( bookingWeekDO );
    }

    if( status != null )
    {
      bookingDO.setFgBooked( status.getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() );
    }
    else
    {
      bookingDO.setFgBooked( Boolean.FALSE );
    }

    this.edit( bookingDO );
    this.flush();
  }

  private void updateBookingStatusFoward( BookingWeekDO bookingWeekDO )
  {
    Date date = bookingWeekDO.getIdWeek().getDtStartingDayWeek();
    if( bookingWeekDO.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.BOOKED.getId() ) )
    {

      int copies = 0;
      List<Integer> screenIds = new ArrayList<Integer>();
      if( CollectionUtils.isNotEmpty( bookingWeekDO.getBookingWeekScreenDOList() ) )
      {
        for( BookingWeekScreenDO bookingWeekScreenDO : bookingWeekDO.getBookingWeekScreenDOList() )
        {
          if( !bookingWeekScreenDO.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.BOOKED.getId() ) )
          {
            screenIds.add( bookingWeekScreenDO.getIdScreen().getIdScreen() );
          }
          else
          {
            copies++;
          }
        }
        bookingWeekDO.setQtCopy( copies );

        this.updateScreensFoward( bookingWeekDO, screenIds, date );
      }

    }
    else
    {
      List<Long> weekIds = new ArrayList<Long>();
      List<BookingWeekDO> weeks = new ArrayList<BookingWeekDO>();
      for( BookingWeekDO week : bookingWeekDO.getIdBooking().getBookingWeekDOList() )
      {
        if( week.getIdWeek().getDtStartingDayWeek().getTime() > date.getTime() )
        {
          weekIds.add( week.getIdBookingWeek() );
        }
        else
        {
          weeks.add( week );
        }
      }
      bookingWeekDO.getIdBooking().setBookingWeekDOList( weeks );
      for( Long weekId : weekIds )
      {
        BookingWeekDO weekDO = this.bookingWeekDAO.find( weekId );
        this.bookingWeekDAO.remove( weekDO );
      }
    }

  }

  /**
   * Method that updates the screens of a bookingWeekDO.
   * 
   * @param bookingWeekDO
   * @param screenIds
   * @param date
   */
  private void updateScreensFoward( BookingWeekDO bookingWeekDO, List<Integer> screenIds, Date date )
  {
    if( CollectionUtils.isNotEmpty( screenIds ) )
    {
      List<Long> weekScreenIds = new ArrayList<Long>();
      for( BookingWeekDO week : bookingWeekDO.getIdBooking().getBookingWeekDOList() )
      {
        if( week.getIdWeek().getDtStartingDayWeek().getTime() > date.getTime()
            && CollectionUtils.isNotEmpty( week.getBookingWeekScreenDOList() ) )
        {
          List<BookingWeekScreenDO> screens = new ArrayList<BookingWeekScreenDO>();
          for( BookingWeekScreenDO ws : week.getBookingWeekScreenDOList() )
          {
            if( ws.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.BOOKED.getId() )
                && screenIds.contains( ws.getIdScreen().getIdScreen() ) )
            {
              weekScreenIds.add( ws.getIdBookingWeekScreen() );
            }
            else
            {
              screens.add( ws );
            }
          }
          week.setBookingWeekScreenDOList( screens );
          week.setQtCopy( screens.size() );
        }
      }

      for( Long weekScreenId : weekScreenIds )
      {
        BookingWeekScreenDO weekScreenDO = this.bookingWeekScreenDAO.find( weekScreenId );
        this.bookingWeekScreenDAO.remove( weekScreenDO );
      }

    }
  }

  private List<ScreenTO> filterRepeatedScreens( List<ScreenTO> screens )
  {
    List<ScreenTO> filteredScreens = new ArrayList<ScreenTO>();
    if( CollectionUtils.isNotEmpty( screens ) )
    {
      Set<ScreenTO> bookedScreens = extractScreensByStatus( screens, BookingStatus.BOOKED );
      Set<ScreenTO> canceledScreens = extractScreensByStatus( screens, BookingStatus.CANCELED );
      Set<ScreenTO> terminatedScreens = extractScreensByStatus( screens, BookingStatus.TERMINATED );
      for( ScreenTO screen : bookedScreens )
      {
        if( canceledScreens.contains( screen ) )
        {
          canceledScreens.remove( screen );
        }
        if( terminatedScreens.contains( screen ) )
        {
          terminatedScreens.remove( screen );
        }
        filteredScreens.add( screen );
      }
      for( ScreenTO screen : canceledScreens )
      {
        filteredScreens.add( screen );
      }
      for( ScreenTO screen : terminatedScreens )
      {
        filteredScreens.add( screen );
      }
    }
    return filteredScreens;
  }

  private Set<ScreenTO> extractScreensByStatus( List<ScreenTO> screens, BookingStatus status )
  {
    Set<ScreenTO> extractedScreens = new HashSet<ScreenTO>();
    for( ScreenTO screen : screens )
    {
      if( screen.getBookingStatus() != null && screen.getBookingStatus().getId().equals( status.getIdLong() ) )
      {
        extractedScreens.add( screen );
      }
    }
    return extractedScreens;
  }

  private BookingStatusDO extractBookingStatus( BookingWeekDO bookingWeekDO,
      List<BookingWeekScreenDO> bookingWeekScreenDOList, BookingTO bookingTO )
  {
    BookingStatusDO status = null;
    for( BookingWeekScreenDO b : bookingWeekScreenDOList )
    {
      if( b.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.BOOKED.getId() ) )
      {
        status = getBookingStatus( BookingStatus.BOOKED.getId() );
        break;
      }
    }
    if( status == null && bookingTO.getStatus().getId().equals( BookingStatus.BOOKED.getIdLong() )
        && bookingTO.getCopy() > 0 )
    {
      status = getBookingStatus( BookingStatus.BOOKED.getId() );
    }
    if( status == null && bookingWeekDO.getNuExhibitionWeek() == 1 )
    {
      status = getBookingStatus( BookingStatus.CANCELED.getId() );
    }
    else if( status == null && bookingWeekDO.getNuExhibitionWeek() > 1 )
    {
      status = getBookingStatus( BookingStatus.TERMINATED.getId() );
    }
    return status;
  }

  private List<BookingWeekScreenDO> getSelectedScreens( List<BookingWeekScreenDO> bookingWeekScreens,
      List<ScreenTO> screens, BookingWeekDO bookingWeekDO )
  {
    List<BookingWeekScreenDO> list = new ArrayList<BookingWeekScreenDO>();
    list.addAll( bookingWeekScreens );

    List<BookingWeekScreenDO> listForRemoval = new ArrayList<BookingWeekScreenDO>();
    for( BookingWeekScreenDO bookingWeekScreenDO : list )
    {
      ScreenTO screenTO = new ScreenTO( bookingWeekScreenDO.getIdScreen().getIdScreen().longValue() );
      if( CollectionUtils.isEmpty( screens ) || !screens.contains( screenTO ) )
      {
        listForRemoval.add( bookingWeekScreenDO );
      }
    }
    for( BookingWeekScreenDO bookingWeekScreenDO : listForRemoval )
    {
      if( CollectionUtils.isNotEmpty( bookingWeekScreenDO.getBookingWeekScreenShowDOList() ) )
      {
        for( BookingWeekScreenShowDO bookingWeekScreenShowDO : bookingWeekScreenDO.getBookingWeekScreenShowDOList() )
        {
          this.bookingWeekScreenShowDAO.remove( bookingWeekScreenShowDO );
        }
      }
      bookingWeekScreenDO.setBookingWeekScreenShowDOList( null );
      if( bookingWeekScreenDO.getIdObservation() != null )
      {
        bookingWeekScreenDO.getIdObservation().setBookingWeekScreenDOList( null );
        this.observationDAO.edit( bookingWeekScreenDO.getIdObservation() );
        this.observationDAO.remove( bookingWeekScreenDO.getIdObservation() );
        bookingWeekScreenDO.setIdObservation( null );
      }
      bookingWeekScreenDAO.remove( bookingWeekScreenDO );

      list.remove( bookingWeekScreenDO );
    }

    updateScreens( screens, bookingWeekDO, list );

    return list;
  }

  private void updateScreens( List<ScreenTO> screens, BookingWeekDO bookingWeekDO, List<BookingWeekScreenDO> list )
  {
    if( CollectionUtils.isNotEmpty( screens ) )
    {
      for( ScreenTO screen : screens )
      {
        if( screen == null || screen.getId() == null || screen.getId().equals( 0L ) )
        {
          continue;
        }
        boolean notFound = true;
        for( BookingWeekScreenDO bookingWeekScreen : list )
        {
          if( bookingWeekScreen.getIdScreen() != null
              && bookingWeekScreen.getIdScreen().getIdScreen().equals( screen.getId().intValue() ) )
          {
            notFound = false;
            updateScreenStatusAndShows( screen, bookingWeekScreen );
            break;
          }
        }
        if( notFound )
        {
          BookingWeekScreenDO bookingWeekScreen = new BookingWeekScreenDO();
          bookingWeekScreen.setIdBookingWeek( bookingWeekDO );
          ScreenDO idScreen = this.screenDAO.find( screen.getId().intValue() );
          bookingWeekScreen.setIdScreen( idScreen );
          updateScreenStatusAndShows( screen, bookingWeekScreen );
          list.add( bookingWeekScreen );
        }

      }
    }
  }

  private void updateScreenStatusAndShows( ScreenTO screen, BookingWeekScreenDO bookingWeekScreen )
  {
    if( screen.getBookingStatus() == null || screen.getBookingStatus().getId() == null )
    {
      screen.setBookingStatus( new CatalogTO( BookingStatus.BOOKED.getIdLong() ) );
    }
    bookingWeekScreen.setIdBookingStatus( getBookingStatus( screen.getBookingStatus().getId().intValue() ) );
   
    List<BookingWeekScreenShowDO> bookingWeekScreenShowDOList = updateScreenShows(
      bookingWeekScreen.getBookingWeekScreenShowDOList(), screen.getShowings(), bookingWeekScreen );
    bookingWeekScreen.setBookingWeekScreenShowDOList( bookingWeekScreenShowDOList );

    if( screen.getBookingObservation() != null
        && StringUtils.isNotBlank( screen.getBookingObservation().getObservation() ) )
    {
      ObservationDO observationDO = null;
      if( bookingWeekScreen.getIdObservation() == null )
      {
        observationDO = new ObservationDO();
      }
      else
      {
        observationDO = bookingWeekScreen.getIdObservation();
      }
      AbstractEntityUtils.applyElectronicSign( observationDO, screen );
      observationDO.setIdUser( userDAO.find( screen.getUserId().intValue() ) );
      observationDO.setDsObservation( screen.getBookingObservation().getObservation() );
      observationDO.setBookingWeekScreenDOList( Arrays.asList( bookingWeekScreen ) );
      saveOrUpdateObservation( observationDO );
      bookingWeekScreen.setIdObservation( observationDO );
    }
    else if( bookingWeekScreen.getIdObservation() != null )
    {
      AbstractEntityUtils.applyElectronicSign( bookingWeekScreen.getIdObservation(), screen );
      this.observationDAO.remove( bookingWeekScreen.getIdObservation() );
      bookingWeekScreen.setIdObservation( null );
    }

  }

  private void saveOrUpdateObservation( ObservationDO observationDO )
  {
    if( observationDO.getIdObservation() == null )
    {
      this.observationDAO.create( observationDO );
    }
    else
    {
      this.observationDAO.edit( observationDO );
    }
  }

  @SuppressWarnings("unchecked")
  private List<BookingWeekScreenShowDO> updateScreenShows( List<BookingWeekScreenShowDO> bookingWeekScreenShowDOList,
      List<CatalogTO> showings, BookingWeekScreenDO bookingWeekScreen )
  {
    List<BookingWeekScreenShowDO> shows = new ArrayList<BookingWeekScreenShowDO>();
    List<CatalogTO> showsSaved = new ArrayList<CatalogTO>();
    List<BookingWeekScreenShowDO> showsForRemoval = new ArrayList<BookingWeekScreenShowDO>();

    if( CollectionUtils.isNotEmpty( bookingWeekScreenShowDOList ) )
    {
      if( CollectionUtils.isNotEmpty( showings ) )
      {
        for( BookingWeekScreenShowDO bookingWeekScreenShowDO : bookingWeekScreenShowDOList )
        {
          for( CatalogTO showing : showings )
          {
            if( bookingWeekScreenShowDO.getNuShow() == showing.getId().intValue() )
            {
              shows.add( bookingWeekScreenShowDO );
              showsSaved.add( showing );
              break;
            }
          }
        }
      }

      showsForRemoval.addAll( (List<BookingWeekScreenShowDO>) CollectionUtils.disjunction( bookingWeekScreenShowDOList,
        shows ) );
    }

    for( BookingWeekScreenShowDO bookingWeekScreenShowDO : showsForRemoval )
    {
      this.bookingWeekScreenShowDAO.remove( bookingWeekScreenShowDO );
    }

    if( CollectionUtils.isNotEmpty( showings ) )
    {
      for( CatalogTO showing : showings )
      {
        if( !showsSaved.contains( showing ) )
        {
          BookingWeekScreenShowDO bookingWeekScreenShowDO = new BookingWeekScreenShowDO();
          bookingWeekScreenShowDO.setIdBookingWeekScreen( bookingWeekScreen );
          bookingWeekScreenShowDO.setNuShow( showing.getId().intValue() );
          shows.add( bookingWeekScreenShowDO );
        }
      }
    }

    return shows;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete( BookingTO bookingTO )
  {
    BookingDO bookingDO = this.find( bookingTO.getId() );
    if( bookingDO == null )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.BOOKING_PERSISTENCE_ERROR_BOOKING_NOT_FOUND );
    }
    AbstractEntityUtils.applyElectronicSign( bookingDO, bookingTO );
    this.remove( bookingDO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BookingTO get( Long id, Integer idWeek )
  {
    return get( id, idWeek, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BookingTO get( Long id, Integer idWeek, Language language )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    BookingDO bookingDO = this.find( id );
    if( bookingDO == null )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.BOOKING_PERSISTENCE_ERROR_BOOKING_NOT_FOUND );
    }
    BookingDOToBookingTOTransformer transformer = new BookingDOToBookingTOTransformer( language );
    transformer.setIdWeek( idWeek );
    return (BookingTO) transformer.transform( bookingDO );
  }

  static class BookingSortFilter
  {
    private List<ModelQuery> sortFields;
    private SortOrder sortOrder;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<BookingDO> criteriaQuery;
    private Root<BookingDO> bookingDO;
    private Join<BookingWeekDO, BookingDO> bookingWeekDO;
    private Join<BookingWeekScreenDO, BookingWeekDO> bookingWeekScreenDO;
    private Join<BookingStatusDO, BookingWeekScreenDO> bookingWeekScreenStatusDO;
    private Join<EventDO, BookingDO> eventDO;
    private Join<WeekDO, BookingDO> weekDO;
    private Join<TheaterDO, BookingDO> theaterDO;
    private Join<RegionDO, TheaterDO> regionDO;
    private Join<BookingSpecialEventDO, BookingDO> bookingSpeciaEventDO;
    private Join<BookingSpecialEventScreenDO, BookingSpecialEventDO> bookingSpeciaEventScreenDO;
    private Join<BookingStatusDO, BookingWeekDO> bookingWeekStatusDO;
    private Map<ModelQuery, Object> filters;

    // jreyesv
    private Join<SpecialEventWeekDO, BookingSpecialEventDO> bookingSpecialEventWeekDO;
    private Join<WeekDO, SpecialEventWeekDO> specialEventWeekDO;
    // Presale
    private Join<PresaleDO, BookingSpecialEventScreenDO> specialEventScreenPresaleDO;
    private Join<PresaleDO, BookingWeekScreenDO> weekScreenPresaleDO;

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
     * @return the weekDO
     */
    public Join<WeekDO, BookingDO> getWeekDO()
    {
      return weekDO;
    }

    /**
     * @param weekDO the weekDO to set
     */
    public void setWeekDO( Join<WeekDO, BookingDO> weekDO )
    {
      this.weekDO = weekDO;
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
     * @return the bookingWeekScreenDO
     */
    public Join<BookingWeekScreenDO, BookingWeekDO> getBookingWeekScreenDO()
    {
      return bookingWeekScreenDO;
    }

    /**
     * @param bookingWeekScreenDO the bookingWeekScreenDO to set
     */
    public void setBookingWeekScreenDO( Join<BookingWeekScreenDO, BookingWeekDO> bookingWeekScreenDO )
    {
      this.bookingWeekScreenDO = bookingWeekScreenDO;
    }

    /**
     * @return the bookingWeekScreenStatusDO
     */
    public Join<BookingStatusDO, BookingWeekScreenDO> getBookingWeekScreenStatusDO()
    {
      return bookingWeekScreenStatusDO;
    }

    /**
     * @param bookingWeekScreenStatusDO the bookingWeekScreenStatusDO to set
     */
    public void setBookingWeekScreenStatusDO( Join<BookingStatusDO, BookingWeekScreenDO> bookingWeekScreenStatusDO )
    {
      this.bookingWeekScreenStatusDO = bookingWeekScreenStatusDO;
    }

    /**
     * @return the bookingWeekStatusDO
     */
    public Join<BookingStatusDO, BookingWeekDO> getBookingWeekStatusDO()
    {
      return bookingWeekStatusDO;
    }

    /**
     * @param bookingWeekStatusDO the bookingWeekStatusDO to set
     */
    public void setBookingWeekStatusDO( Join<BookingStatusDO, BookingWeekDO> bookingWeekStatusDO )
    {
      this.bookingWeekStatusDO = bookingWeekStatusDO;
    }

    /**
     * @return the bookingSpeciaEventDO
     */
    public Join<BookingSpecialEventDO, BookingDO> getBookingSpeciaEventDO()
    {
      return bookingSpeciaEventDO;
    }

    /**
     * @param bookingSpeciaEventDO the bookingSpeciaEventDO to set
     */
    public void setBookingSpeciaEventDO( Join<BookingSpecialEventDO, BookingDO> bookingSpeciaEventDO )
    {
      this.bookingSpeciaEventDO = bookingSpeciaEventDO;
    }

    /**
     * @return the bookingSpeciaEventScreenDO
     */
    public Join<BookingSpecialEventScreenDO, BookingSpecialEventDO> getBookingSpeciaEventScreenDO()
    {
      return bookingSpeciaEventScreenDO;
    }

    /**
     * @param bookingSpeciaEventScreenDO the bookingSpeciaEventScreenDO to set
     */
    public void setBookingSpeciaEventScreenDO(
        Join<BookingSpecialEventScreenDO, BookingSpecialEventDO> bookingSpeciaEventScreenDO )
    {
      this.bookingSpeciaEventScreenDO = bookingSpeciaEventScreenDO;
    }

    /**
     * @return the bookingSpecialEventWeekDO
     */
    public Join<SpecialEventWeekDO, BookingSpecialEventDO> getBookingSpecialEventWeekDO()
    {
      return bookingSpecialEventWeekDO;
    }

    /**
     * @param bookingSpecialEventWeekDO the bookingSpecialEventWeekDO to set
     */
    public void setBookingSpecialEventWeekDO( Join<SpecialEventWeekDO, BookingSpecialEventDO> bookingSpecialEventWeekDO )
    {
      this.bookingSpecialEventWeekDO = bookingSpecialEventWeekDO;
    }

    /**
     * @return the specialEventWeekDO
     */
    public Join<WeekDO, SpecialEventWeekDO> getSpecialEventWeekDO()
    {
      return specialEventWeekDO;
    }

    /**
     * @param specialEventWeekDO the specialEventWeekDO to set
     */
    public void setSpecialEventWeekDO( Join<WeekDO, SpecialEventWeekDO> specialEventWeekDO )
    {
      this.specialEventWeekDO = specialEventWeekDO;
    }

    /**
     * @return the specialEventScreenPresaleDO
     */
    public Join<PresaleDO, BookingSpecialEventScreenDO> getSpecialEventScreenPresaleDO()
    {
      return specialEventScreenPresaleDO;
    }

    /**
     * @param specialEventScreenPresaleDO the specialEventScreenPresaleDO to set
     */
    public void setSpecialEventScreenPresaleDO( Join<PresaleDO, BookingSpecialEventScreenDO> specialEventScreenPresaleDO )
    {
      this.specialEventScreenPresaleDO = specialEventScreenPresaleDO;
    }

    /**
     * @return the weekScreenPresaleDO
     */
    public Join<PresaleDO, BookingWeekScreenDO> getWeekScreenPresaleDO()
    {
      return weekScreenPresaleDO;
    }

    /**
     * @param weekScreenPresaleDO the weekScreenPresaleDO to set
     */
    public void setWeekScreenPresaleDO( Join<PresaleDO, BookingWeekScreenDO> weekScreenPresaleDO )
    {
      this.weekScreenPresaleDO = weekScreenPresaleDO;
    }

  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO#findTheatersByIdWeekAndIdRegionAndLanguage(java.lang
   * .Integer, java.lang.Integer)
   */
  @SuppressWarnings("unchecked")
  @Override
  public PagingResponseTO<TheaterTO> findTheatersByIdWeekAndIdRegion( PagingRequestTO request )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    Map<ModelQuery, Object> filters = getFilters( request );
    Language language = request.getLanguage();
    TypedQuery<TheaterDO> tq = em.createNamedQuery( "BookingDO.findTheatersByRegionAndWeekAndBookingStatus",
      TheaterDO.class );
    tq.setParameter( BookingQuery.BOOKING_WEEK_ID.getQuery(), filters.get( BookingQuery.BOOKING_WEEK_ID ) );
    tq.setParameter( BookingQuery.BOOKING_REGION_ID.getQuery(), filters.get( BookingQuery.BOOKING_REGION_ID ) );
    tq.setParameter( BookingQuery.BOOKING_STATUS_ID.getQuery(), Arrays.asList( BookingStatus.BOOKED.getId() ) );

    TypedQuery<Long> tqCount = em.createNamedQuery( "BookingDO.findTheatersByRegionAndWeekAndBookingStatusCount",
      Long.class );
    tqCount.setParameter( BookingQuery.BOOKING_WEEK_ID.getQuery(), filters.get( BookingQuery.BOOKING_WEEK_ID ) );
    tqCount.setParameter( BookingQuery.BOOKING_REGION_ID.getQuery(), filters.get( BookingQuery.BOOKING_REGION_ID ) );
    tqCount.setParameter( BookingQuery.BOOKING_STATUS_ID.getQuery(), Arrays.asList( BookingStatus.BOOKED.getId() ) );
    if( request.getNeedsPaging() )
    {
      int page = request.getPage();
      int pageSize = request.getPageSize();
      if( pageSize > 0 )
      {
        tq.setMaxResults( pageSize );
      }
      if( page >= 0 )
      {
        tq.setFirstResult( page * pageSize );
      }
    }

    PagingResponseTO<TheaterTO> response = new PagingResponseTO<TheaterTO>();
    response.setTotalCount( tqCount.getSingleResult().intValue() );
    response.setElements( (List<TheaterTO>) CollectionUtils.collect( tq.getResultList(),
      new TheaterDOToTheaterTOTransformer( language ) ) );

    return response;
  }

  @Override
  public PagingResponseTO<TheaterTO> findTheatersByBookingWeekAndRegionForPresaleReport( PagingRequestTO pagingRequestTO )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    Long regionId = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_REGION_ID );

    CatalogTO catalogTO = new CatalogTO( regionId );
    AbstractTOUtils.copyElectronicSign( pagingRequestTO, catalogTO );
    List<TheaterTO> theaters = this.theaterDAO.findByRegionId( catalogTO );

    PagingResponseTO<TheaterTO> response = new PagingResponseTO<TheaterTO>();
    response.setElements( theaters );
    response.setTotalCount( theaters.size() );

    this.applyRedSemaphores( response );

    return response;
  }

  private void applyRedSemaphores( PagingResponseTO<TheaterTO> response )
  {
    String imageSemaphore = BookingTicketSemaphore.RED.getImage();
    for( TheaterTO theater : response.getElements() )
    {
      theater.setImageSemaphore( imageSemaphore );
    }
  }

  @Override
  public PagingResponseTO<TheaterTO> findTheatersByBookingWeekAndRegion( PagingRequestTO pagingRequestTO )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    Long regionId = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_REGION_ID );
    Long idWeek = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_WEEK_ID );

    CatalogTO catalogTO = new CatalogTO( regionId );
    AbstractTOUtils.copyElectronicSign( pagingRequestTO, catalogTO );
    List<TheaterTO> theaters = this.theaterDAO.findByRegionId( catalogTO );

    PagingResponseTO<TheaterTO> response = new PagingResponseTO<TheaterTO>();
    response.setElements( theaters );
    response.setTotalCount( theaters.size() );

    applySemaphores( response, idWeek );

    return response;
  }

  private void applySemaphores( PagingResponseTO<TheaterTO> response, Long idWeek )
  {
    for( TheaterTO theater : response.getElements() )
    {
      String imageSemaphore = this.getTheaterSemaphore( idWeek, theater );

      theater.setImageSemaphore( imageSemaphore );
    }
  }

  @Override
  public String getTheaterSemaphore( Long idWeek, TheaterTO theater )
  {
    String imageSemaphore = null;
    boolean noBookingsRegistered = this.bookingWeekDAO.findByIdTheaterAndIdWeek( theater.getId(), idWeek.intValue() )
        .isEmpty();

    if( noBookingsRegistered )
    {
      imageSemaphore = BookingTicketSemaphore.RED.getImage();
    }
    else
    {
      TheaterDO theaterDO = this.theaterDAO.find( theater.getId().intValue() );
      int screens = 0;
      if( CollectionUtils.isNotEmpty( theaterDO.getScreenDOList() ) )
      {
        for( ScreenDO screenDO : theaterDO.getScreenDOList() )
        {
          if( screenDO.isFgActive() )
          {
            screens++;
          }
        }
      }

      int bookings = countBookingTheaterWithScreensSelected( theater.getId(), idWeek );
      int zero = countBookingTheaterWithNoScreensSelected( theater.getId(), idWeek );
      if( screens - bookings == 0 && zero == 0 )
      {
        imageSemaphore = BookingTicketSemaphore.GREEN.getImage();
      }
      else
      {
        imageSemaphore = BookingTicketSemaphore.YELLOW.getImage();
      }
    }

    return imageSemaphore;
  }

  private int countBookingTheaterWithNoScreensSelected( Long idTheater, Long idWeek )
  {
    Query q = em.createNamedQuery( "BookingDO.namedQuery.countBookingTheaterWithNoScreensSelected" );
    q.setParameter( "idTheater", idTheater );
    q.setParameter( "idWeek", idWeek );
    Number n = ((Number) q.getSingleResult());
    if( n == null )
    {
      n = 0;
    }
    return n.intValue();
  }

  private int countBookingTheaterWithScreensSelected( Long idTheater, Long idWeek )
  {
    Query q = em.createNamedQuery( "BookingDO.namedQuery.countBookingTheaterWithScreensSelected" );

    q.setParameter( "idTheater", idTheater );
    q.setParameter( "idWeek", idWeek );
    Number n = ((Number) q.getSingleResult());
    if( n == null )
    {
      n = 0;
    }
    return n.intValue();
  }

  static class BookingTheaterSortFilter
  {
    private List<ModelQuery> sortFields;
    private SortOrder sortOrder;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<TheaterDO> criteriaQuery;
    private Map<ModelQuery, Object> filters;
    private Root<TheaterDO> theaterDO;
    private Join<RegionDO, TheaterDO> regionDO;;
    private Join<BookingDO, TheaterDO> bookingDO;
    private Join<BookingWeekDO, BookingDO> bookingWeekDO;
    private Join<WeekDO, BookingWeekDO> weekDO;
    private Join<BookingStatusDO, BookingWeekDO> bookingStatusDO;

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

    /**
     * @return the criteriaQuery
     */
    public CriteriaQuery<TheaterDO> getCriteriaQuery()
    {
      return criteriaQuery;
    }

    /**
     * @param criteriaQuery the criteriaQuery to set
     */
    public void setCriteriaQuery( CriteriaQuery<TheaterDO> criteriaQuery )
    {
      this.criteriaQuery = criteriaQuery;
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
     * @return the theaterDO
     */
    public Root<TheaterDO> getTheaterDO()
    {
      return theaterDO;
    }

    /**
     * @param theaterDO the theaterDO to set
     */
    public void setTheaterDO( Root<TheaterDO> theaterDO )
    {
      this.theaterDO = theaterDO;
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
     * @return the bookingDO
     */
    public Join<BookingDO, TheaterDO> getBookingDO()
    {
      return bookingDO;
    }

    /**
     * @param bookingDO the bookingDO to set
     */
    public void setBookingDO( Join<BookingDO, TheaterDO> bookingDO )
    {
      this.bookingDO = bookingDO;
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

  @Override
  public BookingDO findByEventIdAndTheaterId( Long idTheater, Long idEvent )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    Query q = em.createNamedQuery( "BookingDO.findByEventIdAndTheaterId" );
    q.setParameter( ID_THEATER, idTheater );
    q.setParameter( ID_EVENT, idEvent );
    return (BookingDO) CollectionUtils.find( q.getResultList(), PredicateUtils.notNullPredicate() );
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<BookingDO> findBookedByTheater( Long idTheater )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    Query q = em.createNamedQuery( "BookingDO.findBookedByTheater" );
    q.setParameter( ID_THEATER, idTheater );
    return q.getResultList();
  }

  @Override
  public List<BookingTO> findBookingsByEventRegionWeek( PagingRequestTO pagingRequestTO )
  {
    Long idEvent = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_EVENT_ID );
    Long idWeek = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_WEEK_ID );

    this.em.getEntityManagerFactory().getCache().evictAll();
    List<BookingTO> bookings = this.searchBookings( pagingRequestTO );
    WeekTO weekTO = this.weekDAO.getWeek( idWeek.intValue() );

    for( BookingTO booking : bookings )
    {
      booking.setPresaleTO( new PresaleTO( null, false ) );
      if( booking.getStatus() == null )
      {
        if( booking.getExhibitionWeek() == 1 )
        {
          booking.setStatus( new CatalogTO( BookingStatus.BOOKED.getIdLong() ) );
        }
        else
        {
          booking.setStatus( new CatalogTO( BookingStatus.CONTINUE.getIdLong() ) );
        }

      }
      if( booking.getId() == null || booking.getExhibitionWeek() == 0 )
      {
        Long idTheater = booking.getTheater().getId();
        BookingDO bookingDO = this.findByEventIdAndTheaterId( idTheater, idEvent );
        if( bookingDO == null )
        {
          booking.setCopy( 1 );
          booking.setExhibitionWeek( 1 );
        }
        else
        {
          this.extractExhibitionWeekAndScreensSelected( weekTO, booking, bookingDO );
        }
      }

      boolean disabled = booking.getStatus() != null
          && (booking.getStatus().getId().equals( BookingStatus.TERMINATED.getIdLong() ));
      booking.setDisabled( disabled );
      booking.setWeek( weekTO );
      EventTO eventTO = new EventTO();
      eventTO.setIdEvent( idEvent );
      booking.setEvent( eventTO );

      this.completeScreens( booking );
    }

    return bookings;
  }

  private void extractExhibitionWeekAndScreensSelected( WeekTO weekTO, BookingTO booking, BookingDO bookingDO )
  {
    Date start = null;
    Date weekStart = null;
    List<BookingWeekScreenDO> screens = null;
    int exhibitionWeek = 1;
    for( BookingWeekDO bookingWeek : bookingDO.getBookingWeekDOList() )
    {
      if( bookingWeek.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.BOOKED.getId() )
          && bookingWeek.isFgActive() )
      {

        if( start == null )
        {
          start = CinepolisUtils.enhancedClone( bookingWeek.getIdWeek().getDtStartingDayWeek() );
          weekStart = CinepolisUtils.enhancedClone( bookingWeek.getIdWeek().getDtStartingDayWeek() );
          screens = extractScreensBooked( bookingWeek.getBookingWeekScreenDOList() );
        }
        else if( bookingWeek.getIdWeek().getDtStartingDayWeek().after( weekStart ) )
        {
          weekStart = CinepolisUtils.enhancedClone( bookingWeek.getIdWeek().getDtStartingDayWeek() );
          screens = extractScreensBooked( bookingWeek.getBookingWeekScreenDOList() );
        }
      }
    }
    if( start != null )
    {
      exhibitionWeek = this.weekDAO.countWeeks( start, weekTO.getStartingDayWeek() );
      if( weekTO.isSpecialWeek() )
      {
        exhibitionWeek++;
      }
    }

    booking.setExhibitionWeek( exhibitionWeek );
    this.extractScreensSelected( booking, screens );
  }

  /**
   * Method that extracts the list of screens selected for a booking.
   * 
   * @param booking
   * @param screens
   */
  private void extractScreensSelected( BookingTO booking, List<BookingWeekScreenDO> screens )
  {
    if( booking.getCopy() > 0 )
    {
      for( ScreenTO screen : booking.getTheater().getScreens() )
      {
        ScreenDO screenDO = new ScreenDO( screen.getId().intValue() );
        if( CollectionUtils.exists(
          screens,
          PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( GET_ID_SCREEN ),
            PredicateUtils.equalPredicate( screenDO ) ) ) )
        {
          booking.getScreensSelected().add( screen.getId().toString() );
        }
      }
    }
  }

  private List<BookingWeekScreenDO> extractScreensBooked( List<BookingWeekScreenDO> kbwsList )
  {
    List<BookingWeekScreenDO> screens = new ArrayList<BookingWeekScreenDO>();
    if( CollectionUtils.isNotEmpty( kbwsList ) )
    {
      for( BookingWeekScreenDO kbws : kbwsList )
      {
        if( kbws.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.BOOKED.getId() ) )
        {
          screens.add( kbws );
        }
      }
    }
    return screens;
  }

  private void completeScreens( BookingTO booking )
  {
    Collections.sort( booking.getTheater().getScreens(), new ScreenTOComparator() );
    BookingDAOUtil.addScreenZero( booking.getTheater().getScreens(), 0 );
    booking.setScreens( booking.getTheater().getScreens() );
    /* Se verifica que el booking este guardado en base de datos */
    if( booking.getId() != null )
    {
      BookingDO bookingDO = this.find( booking.getId() );
      /* Se verifica que el booking guardado contenga una lista de semanas programadas */
      if( CollectionUtils.isNotEmpty( bookingDO.getBookingWeekDOList() ) )
      {
        int lastPosition = bookingDO.getBookingWeekDOList().size() - 1;
        /* Se obtiene la ultima semana programada y verifica que contenga salas programadas */
        if( CollectionUtils.isNotEmpty( bookingDO.getBookingWeekDOList().get( lastPosition )
            .getBookingWeekScreenDOList() ) )
        {
          booking.setScreens( new ArrayList<ScreenTO>() );
          /*
           * Se recorren las salas programadas en la ultima semana de programación del booking para completar la lista
           * de screeTOs
           */
          for( BookingWeekScreenDO bookingWeekScreenDO : bookingDO.getBookingWeekDOList().get( lastPosition )
              .getBookingWeekScreenDOList() )
          {
            ScreenTO screenTO = new ScreenTO();
            screenTO.setId( bookingWeekScreenDO.getIdScreen().getIdScreen().longValue() );
            screenTO.setPresaleTO( new PresaleTO( null, false ) );
            if( CollectionUtils.isNotEmpty( bookingWeekScreenDO.getPresaleDOList() ) )
            {
              PresaleDO presaleDO = bookingWeekScreenDO.getPresaleDOList().get( 0 );
              if( !presaleDO.getDtFinalDayPresale().before( booking.getWeek().getStartingDayWeek() ) )
              {
                screenTO.setPresaleTO( (PresaleTO) new PresaleDOToPresaleTOTransformer().transform( presaleDO ) );
              }
            }
            booking.getScreens().add( screenTO );
            /* Se valida que la sala programada tenga activada una preventa */
            if( !booking.getPresaleTO().isFgActive() && screenTO.getPresaleTO().isFgActive() )
            {
              /* Se activa la preventa a nivel del booking y se completa su información */
              booking.getPresaleTO().setFgActive( true );
              booking.getPresaleTO().setDtStartDayPresale( screenTO.getPresaleTO().getDtStartDayPresale() );
              booking.getPresaleTO().setDtFinalDayPresale( screenTO.getPresaleTO().getDtFinalDayPresale() );
              booking.getPresaleTO().setDtReleaseDay( screenTO.getPresaleTO().getDtReleaseDay() );
              booking.getPresaleTO().setStrPresaleDates( "" );
              booking.getPresaleTO().setStrReleaseDate( "" );
            }
          }
        }
      }
    }
  }

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

  @SuppressWarnings("unchecked")
  private List<BookingTO> searchBookings( PagingRequestTO pagingRequestTO )
  {
    CatalogTO continueStatus = this.bookingStatusDAO
        .get( BookingStatus.CONTINUE.getId(), pagingRequestTO.getLanguage() );

    Long idRegion = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_REGION_ID );
    Long idEvent = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_EVENT_ID );
    Long idWeek = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_WEEK_ID );
    int idLanguage = pagingRequestTO.getLanguage().getId();

    Integer movieFormat = extractMovieFormat( idEvent );
    List<BookingTO> bookings = new ArrayList<BookingTO>();
    String sqlString = "BookingDO.nativeQuery.findBookingsByEventRegionWeek";
    Query q = em.createNamedQuery( sqlString );
    q.setParameter( 1, idRegion );
    q.setParameter( 2, idEvent );
    q.setParameter( 3, idWeek );
    q.setParameter( 4, idLanguage );
    q.setParameter( 5, movieFormat );
    List<Object[]> result = q.getResultList();

    BookingTO booking = null;
    Set<Integer> idTheaters = new HashSet<Integer>();
    for( Object[] data : result )
    {
      Integer idTheater = ((Number) data[0]).intValue();
      String theaterName = (String) data[1];
      Integer idScreen = ((Number) data[2]).intValue();
      Integer nuScreen = ((Number) data[3]).intValue();
      Long idBooking = this.getLongValue( data[4] );
      int qtCopy = ((Number) data[5]).intValue();
      int qtCopyScreenZero = ((Number) data[6]).intValue();
      int qtCopyScreenZeroTerminated = ((Number) data[7]).intValue();

      Integer nuExhibitionWeek = ((Number) data[8]).intValue();
      Integer idBookingStatus = this.getIntegerValue( data[9] );
      String statusName = (String) data[10];
      boolean disabled = ((Number) data[11]).intValue() == 0;
      boolean selected = ((Number) data[12]).intValue() > 0;

      if( booking == null || !booking.getTheater().getId().equals( idTheater.longValue() ) )
      {
        if( booking != null )
        {
          bookings.add( booking );
        }
        booking = new BookingTO();
        booking.setId( idBooking );
        booking.setTheater( new TheaterTO() );
        booking.getTheater().setId( idTheater.longValue() );
        booking.getTheater().setName( theaterName );
        booking.getTheater().setScreens( new ArrayList<ScreenTO>() );
        booking.setCopy( qtCopy + qtCopyScreenZero );
        booking.setCopyScreenZero( qtCopyScreenZero );
        booking.setCopyScreenZeroTerminated( qtCopyScreenZeroTerminated );

        if( nuExhibitionWeek != null )
        {
          booking.setExhibitionWeek( nuExhibitionWeek );

          if( nuExhibitionWeek.intValue() > 1 && idBookingStatus != null
              && idBookingStatus.intValue() == BookingStatus.BOOKED.getId() )
          {

            booking.setStatus( continueStatus );
          }
          else
          {
            booking.setStatus( idBookingStatus != null ? new CatalogTO( idBookingStatus.longValue(), statusName )
                : null );
          }
        }

        booking.setScreensSelected( new ArrayList<Object>() );
        idTheaters.add( idTheater );
      }
      addScreen( booking, idScreen, nuScreen, disabled, selected );
    }
    bookings.add( booking );

    return filterBookings( pagingRequestTO, bookings );

  }

  private List<BookingTO> filterBookings( PagingRequestTO pagingRequestTO, List<BookingTO> bookings )
  {
    List<BookingTO> filteredBookings = new ArrayList<BookingTO>();
    if( pagingRequestTO.getUserId() != null )
    {
      UserDO userDO = this.userDAO.find( pagingRequestTO.getUserId().intValue() );

      if( CollectionUtils.isNotEmpty( userDO.getTheaterDOList() ) )
      {
        for( BookingTO bookingTO : bookings )
        {
          TheaterDO theaterDO = new TheaterDO( bookingTO.getTheater().getId().intValue() );
          if( userDO.getTheaterDOList().contains( theaterDO ) )
          {
            filteredBookings.add( bookingTO );
          }
        }
      }
      else
      {
        filteredBookings.addAll( bookings );
      }
    }
    else
    {
      filteredBookings.addAll( bookings );
    }
    return filteredBookings;
  }

  private void addScreen( BookingTO booking, Integer idScreen, Integer nuScreen, boolean disabled, boolean selected )
  {
    ScreenTO screenTO = new ScreenTO( idScreen.longValue() );
    if( !booking.getTheater().getScreens().contains( screenTO ) )
    {
      screenTO.setDisabled( disabled );
      if( selected )
      {
        booking.getScreensSelected().add( idScreen.toString() );
      }
      screenTO.setSelected( selected );
      screenTO.setNuScreen( nuScreen );
      booking.getTheater().getScreens().add( screenTO );
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public int findNumberOfExhibitionWeeks( EventTO eventTO )
  {
    Query q = em.createNamedQuery( "BookingDO.findExhibitionWeeks" );
    q.setParameter( ID_EVENT, eventTO.getIdEvent() );

    int n = 0;
    List<Object[]> list = q.getResultList();
    for( Object[] data : list )
    {
      n = ((Number) data[2]).intValue();
      break;
    }

    return n;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<EventMovieTO> findTopWeekBookedMovies( List<WeekTO> currentWeeks, AbstractTO abstractTO )
  {
    Integer week1 = null;
    Integer week2 = null;
    Date date1 = null;
    Date date2 = null;
    if( CollectionUtils.isNotEmpty( currentWeeks ) )
    {
      if( currentWeeks.size() >= 2 )
      {
        week1 = currentWeeks.get( 0 ).getIdWeek();
        date1 = currentWeeks.get( 0 ).getFinalDayWeek();
        week2 = currentWeeks.get( 1 ).getIdWeek();
        date2 = currentWeeks.get( 1 ).getFinalDayWeek();
      }
      else
      {
        week1 = currentWeeks.get( 0 ).getIdWeek();
        week2 = week1;
        date1 = currentWeeks.get( 0 ).getFinalDayWeek();
        date2 = date1;
      }
    }
    else
    {
      date1 = abstractTO.getTimestamp();
      date2 = date1;
    }

    Query q = em.createNamedQuery( "BookingDO.nativeQuery.findTopWeekBookedMovies" );
    q.setParameter( 1, week1 );
    q.setParameter( 2, week2 );
    q.setParameter( 3, date1.getTime() > date2.getTime() ? date1 : date2 );

    List<EventMovieTO> movies = new ArrayList<EventMovieTO>();
    List<Object[]> result = q.getResultList();
    for( Object[] data : result )
    {
      EventMovieTO movie = new EventMovieTO();
      movie.setDsEventName( (String) data[0] );
      movie.setExhibitionWeeks( ((Number) data[1]).intValue() );
      movies.add( movie );
    }
    return movies;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<BookingTO> findBookingsExceeded( Integer weekId, Long theaterId )
  {
    List<BookingTO> bookingsExceeded = new ArrayList<BookingTO>();
    Query q = em.createNamedQuery( "BookingDO.nativeQuery.findBookingsExceeded" );
    q.setParameter( 1, weekId );
    q.setParameter( 2, theaterId );
    List<Object[]> result = q.getResultList();
    for( Object[] data : result )
    {
      BookingTO bookingTO = new BookingTO();
      ScreenTO screenTO = new ScreenTO();
      bookingTO.setId( ((Number) data[0]).longValue() );
      bookingTO.setIdBookingWeek( ((Number) data[1]).longValue() );
      bookingTO.setCopy( ((Number) data[2]).intValue() );
      screenTO.setId( ((Number) data[3]).longValue() );
      bookingTO.setScreen( screenTO );
      bookingsExceeded.add( bookingTO );
    }
    return bookingsExceeded;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void adjustBookings( Integer idWeek, Long idTheater, List<BookingTO> bookingTOs )
  {
    Query q = em.createNamedQuery( "BookingWeekDO.findByTheaterAndWeek" );
    q.setParameter( ID_WEEK, idWeek );
    q.setParameter( ID_THEATER, idTheater );

    List<BookingWeekDO> bookingWeeks = q.getResultList();
    if( CollectionUtils.isNotEmpty( bookingWeeks ) )
    {
      for( BookingWeekDO bookingWeek : bookingWeeks )
      {
        Long idBooking = bookingWeek.getIdBooking().getIdBooking();
        // GSE buscar la cantidad de copias programadas de esa programación IdBooking
        // este dato debe ser ser igual a la suma de salas 0 Booked/continue + salas físicas Booked/continue

        BookingTO bookingTO = extractBookingTO( bookingTOs, idBooking );
        if( bookingTO != null )
        {
          int qtCopy = bookingTO.getCopy();
          int idStatus = bookingTO.getStatus().getId().intValue();

          // Ajustar las copias
          if( qtCopy == 0 )
          {
            // GSE, si hay 0 copias activas de cancela/termina la
            bookingWeek.setQtCopy( 0 );
            bookingWeek.setIdBookingStatus( new BookingStatusDO( idStatus ) );
            bookingWeek.getIdBooking().setFgBooked( false );
          }
          else if( qtCopy > 0 )
          {
            bookingWeek.setQtCopy( qtCopy );
            bookingWeek.setIdBookingStatus( new BookingStatusDO( BookingStatus.BOOKED.getId() ) );
            bookingWeek.getIdBooking().setFgBooked( true );
          }
          // GSE, se copian las firmas electrónicas
          AbstractEntityUtils.applyElectronicSign( bookingWeek.getIdBooking(), bookingTO );
          AbstractEntityUtils.applyElectronicSign( bookingWeek, bookingTO );

          // Se actualizan las tablas K_BOOKING y K_BOOKING_WEEK
          this.bookingWeekDAO.edit( bookingWeek );
          this.edit( bookingWeek.getIdBooking() );
          this.flush();
        }
      }
    }

  }

  private BookingTO extractBookingTO( List<BookingTO> bookingTOs, Long idBooking )
  {
    BookingTO to = null;
    for( BookingTO bookingTO : bookingTOs )
    {
      if( bookingTO != null && bookingTO.getId() != null && bookingTO.getId().equals( idBooking ) )
      {
        to = bookingTO;
        break;
      }
    }
    return to;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<BookingDO> findByIdTheaterAndBooked( Long idTheater )
  {
    Query q = em.createNamedQuery( "BookingWeekDO.findByIdTheaterAndBooked" );
    q.setParameter( ID_THEATER, idTheater );
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasZeroBookings( Long idWeek, TheaterTO theater )
  {
    return countBookingTheaterWithNoScreensSelected( theater.getId(), idWeek ) > 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int countBookedByIdEventMovie( Long idMovie )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    Query q = em.createNamedQuery( "BookingDO.countBookedEventMovie" );
    q.setParameter( ID_EVENT, idMovie );
    Number n = ((Number) q.getSingleResult());
    if( n == null )
    {
      n = 0;
    }
    return n.intValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long countPrereleaseBooked( Long idEvent )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    Query q = em.createNamedQuery( "BookingDO.countPrereleaseBooked" );
    q.setParameter( "idEvent", idEvent );
    return (Long) q.getSingleResult();
  }

  /**
   * Method that extracts the special event booking and transforms it in BookingTheaterTO objects.
   * 
   * @return bookingTheaterTOs The list of special event bookings
   */
  @SuppressWarnings("unchecked")
  public List<BookingTheaterTO> extractBookingSpecialEvents( Long idTheater, int idWeek )
  {
    List<BookingTheaterTO> bookingTheaterTOs = new ArrayList<BookingTheaterTO>();
    boolean inRemoval = false;
    boolean available = false;
    int idBookingStatusCanceled = 2;
    int nuWeek = 0;
    Double incomeCantityInit = 0.0;
    Long totalTicketInit = 0L;
    CatalogTO statusAvailable = this.bookingStatusDAO.get( BookingStatus.CANCELED.getId() );
    CatalogTO status = this.bookingStatusDAO.get( BookingStatus.BOOKED.getId() );
    String sqlString = "SpecialEventDO.nativeQuery.findBookingsSpecialEventByTheaterAndWeek";
    Query q = em.createNamedQuery( sqlString );
    q.setParameter( 1, idTheater );
    q.setParameter( 2, idWeek );
    List<Object[]> result = q.getResultList();

    BookingTheaterTO bookingTheaterTO = null;
    List<CatalogTO> showings = Arrays.asList( BookingShow.FIRST.getShow(), BookingShow.SECOND.getShow(),
      BookingShow.THIRD.getShow(), BookingShow.FOURTH.getShow(), BookingShow.FIVETH.getShow(),
      BookingShow.SIXTH.getShow() );
    for( Object[] data : result )
    {
      Long idBookingSpecialEventScreen = this.getLongValue( data[0] );
      Long idEvent = this.getLongValue( data[1] );
      String eventName = this.getStringValue( data[2] );
      String formatName = this.getStringValue( data[3] );
      Integer capacity = this.getIntegerValue( data[4] );
      String distributorName = this.getStringValue( data[5] );
      Integer nuScreen = this.getIntegerValue( data[6] );
      Long idBookingSpecialEvent = this.getLongValue( data[7] );
      Long idBooking = this.getLongValue( data[8] );
      Boolean presaleObj = this.getBooleanValue( data[9] );
      Integer copy = this.getIntegerValue( data[10] );
      Integer copyScreenZero = this.getIntegerValue( data[11] );
      Long idBookingStatus = this.getLongValue( data[12] );
      Integer idBookingType = this.getIntegerValue( data[13] );
      Date startDay = this.getDateValue( data[14] );
      Date finalDate = this.getDateValue( data[15] );
      Long idBookingStatusScreen = this.getLongValue( data[16] );

      boolean showDate = this.getBooleanValue( data[17] );
      Long idPresale = this.getLongValue( data[18] );
      boolean presale = getBooleanValue( presaleObj );
      Date startDayPresale = this.getDateValue( data[19] );
      Date finalDatePresale = this.getDateValue( data[20] );
      Date releaseDatePresale = this.getDateValue( data[21] );

      if( idBookingSpecialEventScreen == null )
      {
        nuScreen = 0;
      }
      if( idBookingStatusScreen == null )
      {
        idBookingStatusScreen = 1L;
      }

      if( bookingTheaterTO == null || bookingTheaterTO.getScreenTO().getId() != idBookingSpecialEventScreen )
      {
        StringBuilder concatObservation = new StringBuilder( "" );
        bookingTheaterTO = new BookingTheaterTO();
        bookingTheaterTO.setId( idBooking );
        bookingTheaterTO.setInRemoval( inRemoval );
        bookingTheaterTO.setAvailable( available );
        // ScreenTO
        ScreenTO screenTO = new ScreenTO();
        screenTO.setId( idBookingSpecialEventScreen );
        screenTO.setNuScreen( nuScreen );
        bookingTheaterTO.setScreenTO( screenTO );
        bookingTheaterTO.setCopies( copy );
        bookingTheaterTO.setCopyScreenZero( copyScreenZero );
        // NuScreens
        bookingTheaterTO.setNuScreens( new ArrayList<Integer>() );
        bookingTheaterTO.getNuScreens().add( nuScreen );
        bookingTheaterTO.setFormat( formatName );
        bookingTheaterTO.setCapacity( capacity );
        // Showings
        bookingTheaterTO.setSelectedShowings( new ArrayList<Object>() );
        bookingTheaterTO.setShowings( showings );
        // EventTO
        EventTO eventTO = new EventTO();
        eventTO.setId( idEvent );
        eventTO.setIdEvent( idEvent );
        eventTO.setDsEventName( eventName );
        bookingTheaterTO.setEventTO( eventTO );
        bookingTheaterTO.setSelectedEventId( idEvent );
        bookingTheaterTO.setDistributor( distributorName );
        bookingTheaterTO.setWeek( nuWeek );
        bookingTheaterTO.setFridayIncome( incomeCantityInit );
        bookingTheaterTO.setSaturdayIncome( incomeCantityInit );
        bookingTheaterTO.setSundayIncome( incomeCantityInit );
        bookingTheaterTO.setTotalIncome( incomeCantityInit );
        bookingTheaterTO.setTotalTickets( totalTicketInit );
        bookingTheaterTO.setImage( BookingTicketSemaphore.GREEN.getImage() );
        bookingTheaterTO.setStatusIdSelected( idBookingStatus );
        // StatusTOs
        bookingTheaterTO.setStatusTOs( new ArrayList<CatalogTO>() );
        bookingTheaterTO.getStatusTOs().add( status );
        bookingTheaterTO.getStatusTOs().add( statusAvailable );
        bookingTheaterTO.setStatusTO( status );
        bookingTheaterTO.setBookingObservationTO( new BookingObservationTO() );
        bookingTheaterTO.setPresaleTO( new PresaleTO( idPresale, presale, startDayPresale, finalDatePresale,
            releaseDatePresale ) );
        bookingTheaterTO.setFgSpecialEvent( true );
        bookingTheaterTO.setIdBookingType( idBookingType );
        // fecha
        this.setSpecialEventDate( startDay, finalDate, bookingTheaterTO );
        bookingTheaterTO.setStartDay( startDay );
        bookingTheaterTO.setFinalDay( finalDate );
        bookingTheaterTO.setFgShowDate( showDate );
        // Observation
        this.setSpecialEventObservation( bookingTheaterTO, idBookingSpecialEvent );
        concatObservation.append( "\n" ).append( bookingTheaterTO.getBookingObservationTO().getObservation() );
        bookingTheaterTO.getBookingObservationTO().setObservation( concatObservation.toString() );

        this.addBookingTheaterToList( bookingTheaterTOs, bookingTheaterTO,
          (idBookingStatusScreen.intValue() == idBookingStatusCanceled) );
      }
    }
    return bookingTheaterTOs;
  }

  /**
   * Method that sets the date of a special event booking.
   * 
   * @param startDay
   * @param finalDate
   * @param bookingTheaterTO
   */
  private void setSpecialEventDate( Date startDay, Date finalDate, BookingTheaterTO bookingTheaterTO )
  {
    if( startDay != null && finalDate != null )
    {
      SpecialEventTO specialEventTO = new SpecialEventTO();
      specialEventTO.setStartDay( startDay );
      specialEventTO.setFinalDay( finalDate );
      specialEventTO.setDate( new Date() );
      bookingTheaterTO.setStrDateSpecialEvents( specialEventTO.getStrDate() );
    }
    else
    {
      bookingTheaterTO.setStrDateSpecialEvents( " " );
    }
  }

  /**
   * Method that sets the observation of a special event booking.
   * 
   * @param bookingTheaterTO
   * @param idBookingSpecialEvent
   */
  private void setSpecialEventObservation( BookingTheaterTO bookingTheaterTO, Long idBookingSpecialEvent )
  {
    BookingObservationTO bookingObservationTO = new BookingObservationTO();
    bookingObservationTO.setObservation( new String( " " ) );
    bookingTheaterTO.setBookingObservationTO( bookingObservationTO );
    if( idBookingSpecialEvent != null )
    {
      bookingTheaterTO.setBookingObservationTO( this.bookingSpecialEventDAO
          .getObservationByIdSpecialEvent( idBookingSpecialEvent ) );
      bookingTheaterTO.setSelectedShowings( this.bookingSpecialEventDAO
          .getShowingsSelectedByIdSpecialEvent( idBookingSpecialEvent ) );
    }
  }

  /**
   * Method that adds elements to a list of booking theater.
   * 
   * @param bookingTheaterTOs
   * @param bookingTheaterTO
   * @param copiesOfZeroScreen
   */
  private void addBookingTheaterToList( List<BookingTheaterTO> bookingTheaterTOs, BookingTheaterTO bookingTheaterTO,
      boolean isNotValidScreen )
  {
    if( bookingTheaterTO != null )
    {
      int copyToZero = bookingTheaterTO.getCopyScreenZero();
      if( !validateWasBookedZeroScreen( bookingTheaterTOs, bookingTheaterTO )
          && bookingTheaterTO.getCopyScreenZero() > 0 )
      {
        for( int i = 1; i <= copyToZero; i++ )
        {
          bookingTheaterTOs.add( this.duplicateBookingTheater( bookingTheaterTO ) );
        }
      }

      if( bookingTheaterTO.getCopies() > 0 && !isNotValidScreen )
      {
        bookingTheaterTOs.add( bookingTheaterTO );
      }
    }
  }

  private boolean validateWasBookedZeroScreen( List<BookingTheaterTO> bookingTheaterTOs,
      BookingTheaterTO bookingTheaterTO )
  {
    boolean valid = false;
    for( BookingTheaterTO bookingTheater : bookingTheaterTOs )
    {
      if( bookingTheater.getId().equals( bookingTheaterTO.getId() ) )
      {
        valid = true;
        break;
      }
    }
    return valid;
  }

  /**
   * Method that duplicates a booking theater object in a new object.
   * 
   * @param bookingTheaterTO
   * @return
   */
  private BookingTheaterTO duplicateBookingTheater( BookingTheaterTO bookingTheaterTO )
  {
    int nuZeroScreen = 0;
    BookingTheaterTO bookingTheaterTOTemp = new BookingTheaterTO();
    bookingTheaterTOTemp.setId( bookingTheaterTO.getId() );
    bookingTheaterTOTemp.setInRemoval( bookingTheaterTO.isInRemoval() );
    bookingTheaterTOTemp.setAvailable( bookingTheaterTO.isAvailable() );
    // ScreenTO
    ScreenTO screenTOTemp = new ScreenTO();
    screenTOTemp.setId( null );
    screenTOTemp.setNuScreen( nuZeroScreen );
    bookingTheaterTOTemp.setScreenTO( screenTOTemp );
    bookingTheaterTOTemp.setCopies( bookingTheaterTO.getCopies() );
    // NuScreens
    bookingTheaterTOTemp.setNuScreens( new ArrayList<Integer>() );
    bookingTheaterTOTemp.getNuScreens().add( nuZeroScreen );

    bookingTheaterTOTemp.setFormat( bookingTheaterTO.getFormat() );
    bookingTheaterTOTemp.setCapacity( bookingTheaterTO.getCapacity() );
    // Showings
    bookingTheaterTOTemp.setSelectedShowings( new ArrayList<Object>() );
    bookingTheaterTOTemp.setShowings( bookingTheaterTO.getShowings() );
    // EventTO
    EventTO eventTO = new EventTO();
    eventTO.setId( bookingTheaterTO.getEventTO().getId() );
    eventTO.setIdEvent( bookingTheaterTO.getEventTO().getIdEvent() );
    eventTO.setDsEventName( bookingTheaterTO.getEventTO().getDsEventName() );
    bookingTheaterTOTemp.setEventTO( eventTO );
    bookingTheaterTOTemp.setSelectedEventId( bookingTheaterTO.getEventTO().getId() );
    bookingTheaterTOTemp.setDistributor( bookingTheaterTO.getDistributor() );
    bookingTheaterTOTemp.setWeek( bookingTheaterTO.getWeek() );
    bookingTheaterTOTemp.setFridayIncome( bookingTheaterTO.getFridayIncome() );
    bookingTheaterTOTemp.setSaturdayIncome( bookingTheaterTO.getSaturdayIncome() );
    bookingTheaterTOTemp.setSundayIncome( bookingTheaterTO.getSundayIncome() );
    bookingTheaterTOTemp.setTotalIncome( bookingTheaterTO.getTotalIncome() );
    bookingTheaterTOTemp.setTotalTickets( bookingTheaterTO.getTotalTickets() );
    bookingTheaterTOTemp.setImage( BookingTicketSemaphore.GREEN.getImage() );
    bookingTheaterTOTemp.setStatusIdSelected( bookingTheaterTO.getStatusIdSelected() );
    // StatusTOs
    bookingTheaterTOTemp.setStatusTOs( bookingTheaterTO.getStatusTOs() );
    bookingTheaterTOTemp.setStatusTO( bookingTheaterTO.getStatusTO() );
    bookingTheaterTOTemp.setBookingObservationTO( new BookingObservationTO() );
    bookingTheaterTOTemp.setPresaleTO( new PresaleTO( null, Boolean.FALSE ) );
    bookingTheaterTOTemp.setFgSpecialEvent( true );
    bookingTheaterTOTemp.setIdBookingType( bookingTheaterTO.getIdBookingType() );
    bookingTheaterTOTemp.setStrDateSpecialEvents( bookingTheaterTO.getStrDateSpecialEvents() );

    return bookingTheaterTOTemp;
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
   * Gets the string value or null from an Oject.
   * 
   * @param data
   * @return The String value or null
   */
  private String getStringValue( Object data )
  {
    return data != null ? (String) data : null;
  }

  /**
   * Gets the integer value or null from an Oject.
   * 
   * @param data
   * @return The Integer value or null
   */
  private Integer getIntegerValue( Object data )
  {
    return data != null ? ((Number) data).intValue() : null;
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

  /**
   * Gets the boolean value or null from an Oject.
   * 
   * @param data
   * @return The Boolean value or null
   */
  private boolean getBooleanValue( Object data )
  {
    return data != null ? ((Boolean) data).booleanValue() : false;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Object[]> findTheaterBookedPresalesByWeekAndRegion( Long idWeek, Long idRegion )
  {
    this.em.getEntityManagerFactory().getCache().evictAll();
    String queryName = "BookingDO.nativeQuery.findTheaterBookedPresalesByWeekAndRegion";
    Query q = em.createNamedQuery( queryName );
    q.setParameter( 1, idWeek );
    q.setParameter( 2, idRegion );
    return q.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Long> findTheatersByWeekEventAndRegion( Long idWeek, Long idEvent, Long idRegion )
  {
    List<Long> theaterIds = new ArrayList<Long>();
    this.em.getEntityManagerFactory().getCache().evictAll();
    String queryName = "BookingDO.nativeQuery.findTheatersByWeekEventAndRegion";
    Query q = em.createNamedQuery( queryName );
    q.setParameter( 1, idWeek );
    q.setParameter( 2, idEvent );
    q.setParameter( 3, idRegion );
    List<Integer> dataList = q.getResultList();
    for( Integer data : dataList )
    {
      theaterIds.add( this.getLongValue( data ) );
    }
    return theaterIds;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Long> findEventsByWeekAndTheater( Long idWeek, Long idTheater )
  {
    List<Long> eventIds = new ArrayList<Long>();
    this.em.getEntityManagerFactory().getCache().evictAll();
    String queryName = "BookingDO.nativeQuery.findEventsByWeekAndTheater";
    Query q = em.createNamedQuery( queryName );
    q.setParameter( 1, idWeek );
    q.setParameter( 2, idTheater );
    List<Long> dataList = q.getResultList();
    for( Long data : dataList )
    {
      eventIds.add( this.getLongValue( data ) );
    }
    return eventIds;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<String> findPremiereBooking()
  {
    Query q = em.createNamedQuery( "BookingDO.findPremiereBooking" );
    return q.getResultList();
  }

}
