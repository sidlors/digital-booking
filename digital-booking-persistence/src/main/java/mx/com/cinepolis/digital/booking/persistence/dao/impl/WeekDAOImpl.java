package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Calendar;
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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mx.com.cinepolis.digital.booking.commons.constants.Configuration;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.query.WeekQuery;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.WeekDOToWeekTOTransformer;
import mx.com.cinepolis.digital.booking.model.ConfigurationDO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
@Stateless
@SuppressWarnings("unchecked")
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class WeekDAOImpl extends AbstractBaseDAO<WeekDO> implements WeekDAO
{
  private static final String PARAMETER_DATE = "date";
  private static final String QUERY_SPECIAL_WEEKS = "WeekDO.findBySpecialWeeks";
  private static final String QUERY_WEEKS_BETWEEN_STARTDAY_AND_FINALDAY = "WeekDO.findByStartDayAndFinalDay";
  private static final String QUERY_CURRENT_WEEK = "WeekDO.findByCurrentWeek";
  private static final String QUERY_MAX_NUM_WEEK = "WeekDO.findMaxNumWeek";
  private static final String QUERY_FIND_BY_DATES = "WeekDO.findByDates";
  private static final String PARAMETER_CURRENT_YEAR = "currentYear";
  private static final String PARAMETER_START_DATE = "startDate";
  private static final String PARAMETER_FINAL_DATE = "finalDate";
  private static final String PARAMETER_SPECIAL_WEEK = "specialWeek";

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @EJB
  private ConfigurationDAO configurationDAO;

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
  public WeekDAOImpl()
  {
    super( WeekDO.class );
  }

  public WeekDAOImpl( Class<WeekDO> entityClass )
  {
    super( entityClass );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( WeekDO weekDO )
  {
    WeekDO remove = this.find( weekDO.getIdWeek() );
    if( remove == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.WEEK_PERSISTENCE_ERROR_NOT_FOUND );
    }
    remove.setIdLastUserModifier( weekDO.getIdLastUserModifier() );
    remove.setDtLastModification( weekDO.getDtLastModification() );
    remove.setFgActive( false );
    this.edit( remove );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( WeekTO weekTO )
  {
    WeekDO weekDO = new WeekDO();
    AbstractEntityUtils.applyElectronicSign( weekDO, weekTO );
    weekDO.setNuWeek( weekTO.getNuWeek() );
    weekDO.setNuYear( weekTO.getNuYear() );
    weekDO.setDtFinalDayWeek( weekTO.getFinalDayWeek() );
    weekDO.setDtStartingDayWeek( weekTO.getStartingDayWeek() );
    weekDO.setFgSpecialWeek( weekTO.isSpecialWeek() );
    this.create( weekDO );
    this.flush();
    weekTO.setIdWeek( weekDO.getIdWeek() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update( WeekTO weekTO )
  {
    WeekDO weekDO = this.find( weekTO.getIdWeek() );
    if( weekDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.WEEK_PERSISTENCE_ERROR_NOT_FOUND );
    }
    AbstractEntityUtils.applyElectronicSign( weekDO, weekTO );
    weekDO.setNuWeek( weekTO.getNuWeek() );
    weekDO.setNuYear( weekTO.getNuYear() );
    weekDO.setDtFinalDayWeek( weekTO.getFinalDayWeek() );
    weekDO.setDtStartingDayWeek( weekTO.getStartingDayWeek() );
    weekDO.setFgSpecialWeek( weekTO.isSpecialWeek() );
    this.edit( weekDO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete( WeekTO weekTO )
  {
    WeekDO weekDO = new WeekDO();
    AbstractEntityUtils.applyElectronicSign( weekDO, weekTO );
    weekDO.setIdWeek( weekTO.getIdWeek() );
    this.remove( weekDO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PagingResponseTO<WeekTO> findAllByPaging( PagingRequestTO pagingRequestTO )
  {
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<WeekDO> q = cb.createQuery( WeekDO.class );
    q.distinct( Boolean.TRUE );
    Root<WeekDO> weekDO = q.from( WeekDO.class );

    q.select( weekDO );

    WeekSortingFilter weekSortingFilter = new WeekSortingFilter();
    weekSortingFilter.setCriteriaBuilder( cb );
    weekSortingFilter.setCriteriaQuery( q );
    weekSortingFilter.setFilters( getFilters( pagingRequestTO ) );
    weekSortingFilter.setSortFields( pagingRequestTO.getSort() );
    weekSortingFilter.setSortOrder( pagingRequestTO.getSortOrder() );
    weekSortingFilter.setWeekDO( weekDO );

    applySorting( weekSortingFilter );

    Predicate filterCondition = applyFilters( weekSortingFilter );

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( weekDO ) );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );
    }

    TypedQuery<WeekDO> tq = em.createQuery( q );
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
    PagingResponseTO<WeekTO> response = new PagingResponseTO<WeekTO>();
    response
        .setElements( (List<WeekTO>) CollectionUtils.collect( tq.getResultList(), new WeekDOToWeekTOTransformer() ) );
    response.setTotalCount( count );

    return response;
  }

  private Predicate applyFilters( WeekSortingFilter weekSortingFilter )
  {
    Predicate filterCondition = null;
    if( weekSortingFilter.getFilters() != null && !weekSortingFilter.getFilters().isEmpty() )
    {
      filterCondition = weekSortingFilter.getCriteriaBuilder().conjunction();

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( weekSortingFilter.getFilters(),
        WeekQuery.WEEK_ID, weekSortingFilter.getWeekDO(), weekSortingFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterRootEqualDate( weekSortingFilter.getFilters(),
        WeekQuery.WEEK_START, weekSortingFilter.getWeekDO(), weekSortingFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterRootEqualDate( weekSortingFilter.getFilters(),
        WeekQuery.WEEK_END, weekSortingFilter.getWeekDO(), weekSortingFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( weekSortingFilter.getFilters(),
        WeekQuery.WEEK_ACTIVE, weekSortingFilter.getWeekDO(), weekSortingFilter.getCriteriaBuilder(), filterCondition );

      if( weekSortingFilter.getFilters().containsKey( WeekQuery.WEEK_YEAR ) )
      {
        filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( weekSortingFilter.getFilters(),
          WeekQuery.WEEK_YEAR, weekSortingFilter.getWeekDO(), weekSortingFilter.getCriteriaBuilder(), filterCondition );
      }
      if( weekSortingFilter.getFilters().containsKey( WeekQuery.WEEK_NUM ) )
      {
        filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( weekSortingFilter.getFilters(),
          WeekQuery.WEEK_NUM, weekSortingFilter.getWeekDO(), weekSortingFilter.getCriteriaBuilder(), filterCondition );
      }
      if( weekSortingFilter.getFilters().containsKey( WeekQuery.SPECIAL_WEEK ) )
      {
        filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( weekSortingFilter.getFilters(),
          WeekQuery.SPECIAL_WEEK, weekSortingFilter.getWeekDO(), weekSortingFilter.getCriteriaBuilder(),
          filterCondition );
      }

      if( weekSortingFilter.getFilters().containsKey( WeekQuery.WEEK_DAY )
          && weekSortingFilter.getFilters().get( WeekQuery.WEEK_DAY ) instanceof Date )
      {
        Date date = (Date) weekSortingFilter.getFilters().get( WeekQuery.WEEK_DAY );
        date = DateUtils.truncate( date, Calendar.DATE );
        Path<Date> initDate = weekSortingFilter.getWeekDO().get( WeekQuery.WEEK_START.getQuery() );
        filterCondition = weekSortingFilter.getCriteriaBuilder().and( filterCondition,
          weekSortingFilter.getCriteriaBuilder().lessThanOrEqualTo( initDate, date ) );

        Path<Date> endDate = weekSortingFilter.getWeekDO().get( WeekQuery.WEEK_END.getQuery() );
        filterCondition = weekSortingFilter.getCriteriaBuilder().and( filterCondition,
          weekSortingFilter.getCriteriaBuilder().greaterThanOrEqualTo( endDate, date ) );
      }

    }
    return filterCondition;
  }

  private void applySorting( WeekSortingFilter weekSortingFilter )
  {
    CriteriaQuery<WeekDO> q = weekSortingFilter.getCriteriaQuery();

    if( weekSortingFilter.getSortOrder() != null && CollectionUtils.isNotEmpty( weekSortingFilter.getSortFields() ) )
    {
      List<Order> order = new ArrayList<Order>();
      for( ModelQuery sortField : weekSortingFilter.getSortFields() )
      {
        sortByField( order, sortField, weekSortingFilter );
      }
      q.orderBy( order );

    }

  }

  private void sortByField( List<Order> order, ModelQuery sortField, WeekSortingFilter weekSortingFilter )
  {
    SortOrder sortOrder = weekSortingFilter.getSortOrder();
    CriteriaBuilder cb = weekSortingFilter.getCriteriaBuilder();
    Root<WeekDO> weekDO = weekSortingFilter.getWeekDO();
    if( sortField instanceof WeekQuery )
    {
      Path<?> path = null;
      switch( (WeekQuery) sortField )
      {
        case WEEK_ID:
        case WEEK_START:
        case WEEK_END:
          path = weekDO.get( sortField.getQuery() );
          break;
        default:
          path = weekDO.get( WeekQuery.WEEK_ID.getQuery() );
          break;
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
  public List<WeekTO> getCurrentWeeks( Date date, boolean getMoreWeeks )
  {
    Date startDate = null;
    Date endDate = null;
    Set<WeekTO> weeks = new HashSet<WeekTO>();
    int dayOffest = getDaysOffset();

    // Se obtienen las semana corriente
    Date dateQuery = DateUtils.truncate( date, Calendar.DATE );
    dateQuery = DateUtils.addDays( dateQuery, dayOffest );

    Query q = this.em.createNamedQuery( QUERY_CURRENT_WEEK );
    q.setParameter( PARAMETER_DATE, dateQuery );

    List<WeekTO> activeWeeks = (List<WeekTO>) CollectionUtils.collect( q.getResultList(),
      new WeekDOToWeekTOTransformer( true ) );
    weeks.addAll( activeWeeks );

    // Se obtiene la semana anterior
    dateQuery = DateUtils.addDays( dateQuery, -7 );
    startDate = (Date) dateQuery.clone();
    q = this.em.createNamedQuery( QUERY_CURRENT_WEEK );
    q.setParameter( PARAMETER_DATE, dateQuery );

    weeks.addAll( CollectionUtils.collect( q.getResultList(), new WeekDOToWeekTOTransformer() ) );

    // Se obtienen las siguientes 2 semanas
    dateQuery = DateUtils.truncate( date, Calendar.DATE );
    dateQuery = DateUtils.addDays( dateQuery, dayOffest + 7 );
    endDate = DateUtils.addDays( dateQuery, dayOffest + 13 );

    q = this.em.createNamedQuery( QUERY_CURRENT_WEEK );
    q.setParameter( PARAMETER_DATE, dateQuery );

    weeks.addAll( CollectionUtils.collect( q.getResultList(), new WeekDOToWeekTOTransformer() ) );

    dateQuery = DateUtils.addDays( dateQuery, 7 );

    q = this.em.createNamedQuery( QUERY_CURRENT_WEEK );
    q.setParameter( PARAMETER_DATE, dateQuery );

    weeks.addAll( CollectionUtils.collect( q.getResultList(), new WeekDOToWeekTOTransformer() ) );

    /**
     * Se obtienen las siguientes 10 semanas, en caso de que getMoreWeeks sea igual a true.
     * 
     * @author jreyesv
     */
    if( getMoreWeeks )
    {
      int startWeek = 4;
      int endWeek = 14;
      dateQuery = DateUtils.truncate( date, Calendar.DATE );
      Date today = dateQuery;
      for( int i = startWeek; i < endWeek; i++ )
      {
        dateQuery = DateUtils.addDays( today, (dayOffest * i) );

        q = this.em.createNamedQuery( QUERY_CURRENT_WEEK );
        q.setParameter( PARAMETER_DATE, dateQuery );

        weeks.addAll( CollectionUtils.collect( q.getResultList(), new WeekDOToWeekTOTransformer() ) );
      }
      endDate = DateUtils.addDays( today, (dayOffest * endWeek) - 1 );
    }

    weeks.addAll( this.getSpecialWeeks( startDate, endDate ) );

    List<WeekTO> weekList = new ArrayList<WeekTO>();
    weekList.addAll( weeks );
    return weekList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<WeekTO> getSpecialWeeks( Date start, Date end )
  {
    Query q = this.em.createNamedQuery( QUERY_SPECIAL_WEEKS );
    q.setParameter( "dateStart", DateUtils.truncate( start, Calendar.DATE ) );
    q.setParameter( "dateEnd", DateUtils.truncate( end, Calendar.DATE ) );
    return (List<WeekTO>) CollectionUtils.collect( q.getResultList(), new WeekDOToWeekTOTransformer() );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO#findWeeksByStartDayAndFinalDay(mx.com.cinepolis.digital
   * .booking.commons.to.SpecialEventTO)
   */
  @Override
  public List<WeekDO> findWeeksByStartDayAndFinalDay( Date startDay, Date finalDay )
  {
    Query q = this.em.createNamedQuery( QUERY_WEEKS_BETWEEN_STARTDAY_AND_FINALDAY );
    q.setParameter( "dateStart", startDay );
    q.setParameter( "dateEnd", finalDay );

    return (List<WeekDO>) q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WeekTO getWeek( int id )
  {
    WeekDO weekDO = this.find( id );
    if( weekDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.WEEK_PERSISTENCE_ERROR_NOT_FOUND );
    }

    boolean activeWeek = isActiveWeek( weekDO );
    return (WeekTO) new WeekDOToWeekTOTransformer( activeWeek ).transform( weekDO );
  }

  private boolean isActiveWeek( WeekDO weekDO )
  {

    Date dateQuery = DateUtils.truncate( new Date(), Calendar.DATE );
    dateQuery = DateUtils.addDays( dateQuery, getDaysOffset() );

    return weekDO.isFgActive() && weekDO.getDtStartingDayWeek().getTime() <= dateQuery.getTime()
        && weekDO.getDtFinalDayWeek().getTime() >= dateQuery.getTime();

  }

  private int getDaysOffset()
  {
    int daysOffset = 0;
    ConfigurationDO configurationDO = configurationDAO.findByParameterName( Configuration.CURRENT_WEEK_DAYS_OFFSET
        .getParameter() );
    if( configurationDO != null )
    {
      daysOffset = Integer.parseInt( configurationDO.getDsValue() );
    }
    return daysOffset;
  }

  /**
   * Inner class for sorting and filtering
   * 
   * @author gsegura
   * @since 0.2.0
   */
  static class WeekSortingFilter
  {
    private List<ModelQuery> sortFields;
    private SortOrder sortOrder;
    private Map<ModelQuery, Object> filters;;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<WeekDO> criteriaQuery;
    private Root<WeekDO> weekDO;

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
    public CriteriaQuery<WeekDO> getCriteriaQuery()
    {
      return criteriaQuery;
    }

    /**
     * @param criteriaQuery the criteriaQuery to set
     */
    public void setCriteriaQuery( CriteriaQuery<WeekDO> criteriaQuery )
    {
      this.criteriaQuery = criteriaQuery;
    }

    /**
     * @return the weekDO
     */
    public Root<WeekDO> getWeekDO()
    {
      return weekDO;
    }

    /**
     * @param weekDO the weekDO to set
     */
    public void setWeekDO( Root<WeekDO> weekDO )
    {
      this.weekDO = weekDO;
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WeekTO getCurrentWeek( Date date )
  {
    List<WeekTO> weeks = new ArrayList<WeekTO>();

    // Se obtienen las semana corriente
    Date dateQuery = DateUtils.truncate( date, Calendar.DATE );
    dateQuery = DateUtils.addDays( dateQuery, getDaysOffset() );

    Query q = this.em.createNamedQuery( QUERY_CURRENT_WEEK );
    q.setParameter( PARAMETER_DATE, dateQuery );

    List<WeekTO> activeWeeks = (List<WeekTO>) CollectionUtils.collect( q.getResultList(),
      new WeekDOToWeekTOTransformer( true ) );
    weeks.addAll( activeWeeks );

    return (WeekTO) CollectionUtils.find( activeWeeks, PredicateUtils.notNullPredicate() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int countWeeks( Date firstDate1, Date firstDate2 )
  {
    Query q = em.createNamedQuery( "WeekDO.countWeeks" );
    q.setParameter( "firstDate1", firstDate1 );
    q.setParameter( "firstDate2", firstDate2 );
    return ((Long) q.getSingleResult()).intValue();
  }

  /**
   * {@inheritDoc}
   */
  public WeekTO getNextWeek( int currentYear )
  {
    Query q = this.em.createNamedQuery( QUERY_MAX_NUM_WEEK );
    q.setParameter( PARAMETER_CURRENT_YEAR, currentYear );
    List<WeekTO> activeWeeks = (List<WeekTO>) CollectionUtils.collect( q.getResultList(),
      new WeekDOToWeekTOTransformer( true ) );
    return (WeekTO) CollectionUtils.find( activeWeeks, PredicateUtils.notNullPredicate() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WeekTO getWeekByDates( WeekTO weekTO )
  {
    Query q = em.createNamedQuery( QUERY_FIND_BY_DATES );
    q.setParameter( PARAMETER_START_DATE, weekTO.getStartingDayWeek() );
    q.setParameter( PARAMETER_FINAL_DATE, weekTO.getFinalDayWeek() );
    q.setParameter( PARAMETER_SPECIAL_WEEK, weekTO.isSpecialWeek() );
    List<WeekTO> weeks = (List<WeekTO>) CollectionUtils.collect( q.getResultList(),
      new WeekDOToWeekTOTransformer( true ) );
    return (WeekTO) CollectionUtils.find( weeks, PredicateUtils.notNullPredicate() );
  }

  @Override
  public List<WeekTO> getCurrentAndLasWeek( Date date )
  {
    Set<WeekTO> weeks = new HashSet<WeekTO>();
    int dayOffest = getDaysOffset();

    // Se obtienen las semana corriente
    Date dateQuery = DateUtils.truncate( date, Calendar.DATE );
    dateQuery = DateUtils.addDays( dateQuery, dayOffest );

    Query q = this.em.createNamedQuery( QUERY_CURRENT_WEEK );
    q.setParameter( PARAMETER_DATE, dateQuery );

    List<WeekTO> activeWeeks = (List<WeekTO>) CollectionUtils.collect( q.getResultList(),
      new WeekDOToWeekTOTransformer( true ) );
    weeks.addAll( activeWeeks );

    // Se obtiene la semana anterior
    dateQuery = DateUtils.addDays( dateQuery, -7 );
    q = this.em.createNamedQuery( QUERY_CURRENT_WEEK );
    q.setParameter( PARAMETER_DATE, dateQuery );

    weeks.addAll( CollectionUtils.collect( q.getResultList(), new WeekDOToWeekTOTransformer() ) );

    List<WeekTO> currentWeeks = new ArrayList<WeekTO>();
    currentWeeks.addAll( weeks );

    return currentWeeks;
  }
}
