package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.query.TheaterQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.TheaterDOToTheaterTOTransformer;
import mx.com.cinepolis.digital.booking.model.CityDO;
import mx.com.cinepolis.digital.booking.model.EmailDO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.RegionLanguageDO;
import mx.com.cinepolis.digital.booking.model.StateDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.TheaterLanguageDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CityDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.StateDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class TheaterDAOImpl extends AbstractBaseDAO<TheaterDO> implements TheaterDAO
{

  private static final String USER_DO_LIST = "userDOList";

  private static final String REGION_LANGUAGE_DO_LIST = "regionLanguageDOList";

  private static final String ID_STATE = "idState";

  private static final String ID_REGION = "idRegion";

  private static final String ID_CITY = "idCity";

  private static final String ID_LANGUAGE = "idLanguage";

  private static final String THEATER_LANGUAGE_DO_LIST = "theaterLanguageDOList";

  /**
   * Entity Manager
   */
  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @EJB
  private CityDAO cityDAO;

  @EJB
  private RegionDAO regionDAO;

  @EJB
  private StateDAO stateDAO;

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
   * Constructor Default
   */
  public TheaterDAOImpl()
  {
    super( TheaterDO.class );
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO#findAllByPaging(mx.com.cinepolis.digital.booking.model
   * .to.PagingRequestTO)
   */
  @SuppressWarnings("unchecked")
  @Override
  public PagingResponseTO<TheaterTO> findAllByPaging( PagingRequestTO pagingRequestTO )
  {

    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = getFilters( pagingRequestTO );
    Language language = pagingRequestTO.getLanguage();

    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<TheaterDO> q = cb.createQuery( TheaterDO.class );
    q.distinct( Boolean.TRUE );
    Root<TheaterDO> theaterDO = q.from( TheaterDO.class );
    Join<TheaterDO, TheaterLanguageDO> theaterLanguageDO = theaterDO.join( THEATER_LANGUAGE_DO_LIST );
    Join<TheaterLanguageDO, LanguageDO> languageDO = theaterLanguageDO.join( ID_LANGUAGE );
    Join<TheaterDO, CityDO> cityDO = theaterDO.join( ID_CITY );
    Join<TheaterDO, RegionDO> regionDO = theaterDO.join( ID_REGION );
    Join<TheaterDO, StateDO> stateDO = theaterDO.join( ID_STATE );
    Join<RegionDO, RegionLanguageDO> regionLanguageDO = regionDO.join( REGION_LANGUAGE_DO_LIST );
    Join<RegionLanguageDO, LanguageDO> languageDO2 = regionLanguageDO.join( ID_LANGUAGE );
    Join<UserDO, TheaterDO> userDO = null;
    Join<RegionDO, UserDO> userRegionDO = null;

    if( userHasTheaters( pagingRequestTO.getUserId() ) )
    {
      userDO = theaterDO.join( USER_DO_LIST );
      filters.put( TheaterQuery.ID_USER, pagingRequestTO.getUserId() );
    }

    if( pagingRequestTO.getUserId() != null )
    {
      userRegionDO = regionDO.join( USER_DO_LIST );
      filters.put( TheaterQuery.ID_USER, pagingRequestTO.getUserId() );
    }

    q.select( theaterDO );

    TheaterDAOSort theaterDAOSort = new TheaterDAOSort();
    theaterDAOSort.setSortFields( sortFields );
    theaterDAOSort.setSortOrder( sortOrder );
    theaterDAOSort.setCb( cb );
    theaterDAOSort.setQ( q );
    theaterDAOSort.setTheaterDO( theaterDO );
    theaterDAOSort.setTheaterLanguageDO( theaterLanguageDO );
    theaterDAOSort.setLanguageDO( languageDO );
    theaterDAOSort.setCityDO( cityDO );
    theaterDAOSort.setRegionDO( regionDO );
    theaterDAOSort.setStateDO( stateDO );
    theaterDAOSort.setRegionLanguageDO( regionLanguageDO );
    theaterDAOSort.setUserDO( userDO );
    theaterDAOSort.setLanguageDO2( languageDO2 );
    theaterDAOSort.setUserRegionDO( userRegionDO );
    applySorting( theaterDAOSort );

    Predicate filterCondition = applyFilters( theaterDAOSort, filters );

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( theaterDO ) );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );
    }

    // pagination
    this.em.getEntityManagerFactory().getCache().evictAll();
    TypedQuery<TheaterDO> tq = em.createQuery( q );
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
    PagingResponseTO<TheaterTO> response = new PagingResponseTO<TheaterTO>();

    response.setElements( (List<TheaterTO>) CollectionUtils.collect( tq.getResultList(),
      new TheaterDOToTheaterTOTransformer( language ) ) );
    response.setTotalCount( count );

    return response;
  }

  private boolean userHasTheaters( Long userId )
  {
    boolean userHasTheaters = false;
    if( userId != null )
    {
      UserDO userDO = this.userDAO.find( userId.intValue() );
      userHasTheaters = CollectionUtils.isNotEmpty( userDO.getTheaterDOList() );
    }
    return userHasTheaters;
  }

  /**
   * Aplciamos Filtros
   * 
   * @param filters
   * @param filters
   * @param cb
   * @param theaterDO
   * @param theaterLanguageDO
   * @param languageDO
   * @param cityDO
   * @param regionDO
   * @param stateDO
   * @param languageDO2
   * @param screenDO
   * @return
   */
  private Predicate applyFilters( TheaterDAOSort theaterDAOSort, Map<ModelQuery, Object> filters )
  {
    Predicate filterCondition = null;
    CriteriaBuilder cb = theaterDAOSort.getCb();
    if( filters != null && !filters.isEmpty() )
    {
      filterCondition = cb.conjunction();

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( filters, TheaterQuery.THEATER_ID,
        theaterDAOSort.getTheaterDO(), cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( filters, TheaterQuery.THEATER_ACTIVE,
        theaterDAOSort.getTheaterDO(), cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinLike( filters, TheaterQuery.THEATER_NAME,
        theaterDAOSort.getTheaterLanguageDO(), cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters, TheaterQuery.THEATER_ID_LANGUAGE,
        theaterDAOSort.getLanguageDO(), cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters, TheaterQuery.THEATER_ID_LANGUAGE,
        theaterDAOSort.getLanguageDO2(), cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters, TheaterQuery.ID_CITY,
        theaterDAOSort.getCityDO(), cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters, TheaterQuery.ID_REGION,
        theaterDAOSort.getRegionDO(), cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters, TheaterQuery.ID_STATE,
        theaterDAOSort.getStateDO(), cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters, TheaterQuery.ID_USER,
        theaterDAOSort.getUserDO(), cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters, TheaterQuery.ID_USER,
        theaterDAOSort.getUserRegionDO(), cb, filterCondition );

    }
    return filterCondition;
  }

  /**
   * Aplica filtros de ordenamiento
   * 
   * @param sortField
   * @param sortOrder
   * @param cb
   * @param q
   * @param theaterDO
   * @param theaterLanguageDO
   * @param languageDO
   * @param cityDO
   * @param regionDO
   * @param stateDO
   */
  private void applySorting( TheaterDAOSort theaterDAOSort )
  {
    List<ModelQuery> sortFields = theaterDAOSort.getSortFields();
    SortOrder sortOrder = theaterDAOSort.getSortOrder();
    CriteriaQuery<TheaterDO> q = theaterDAOSort.getQ();

    if( sortOrder != null && CollectionUtils.isNotEmpty( sortFields ) )
    {
      List<Order> order = new ArrayList<Order>();
      for( ModelQuery sortField : sortFields )
      {
        if( sortField instanceof TheaterQuery )
        {
          sortFields( theaterDAOSort, order, sortField );
        }
      }
      q.orderBy( order );
    }
  }

  private void sortFields( TheaterDAOSort theaterDAOSort, List<Order> order, ModelQuery sortField )
  {
    CriteriaBuilder cb = theaterDAOSort.getCb();
    SortOrder sortOrder = theaterDAOSort.getSortOrder();
    Root<TheaterDO> theaterDO = theaterDAOSort.getTheaterDO();
    Join<TheaterDO, TheaterLanguageDO> theaterLanguageDO = theaterDAOSort.getTheaterLanguageDO();
    Join<TheaterLanguageDO, LanguageDO> languageDO = theaterDAOSort.getLanguageDO();
    Join<TheaterDO, CityDO> cityDO = theaterDAOSort.getCityDO();
    Join<TheaterDO, RegionDO> regionDO = theaterDAOSort.getRegionDO();
    Join<TheaterDO, StateDO> stateDO = theaterDAOSort.getStateDO();
    Join<RegionDO, RegionLanguageDO> regionLanguageDO = theaterDAOSort.regionLanguageDO;
    Path<?> path = null;
    switch( (TheaterQuery) sortField )
    {
      case THEATER_ID:
        path = theaterDO.get( sortField.getQuery() );
        break;
      case THEATER_ID_LANGUAGE:
        path = languageDO.get( sortField.getQuery() );
        break;
      case ID_CITY:
        path = cityDO.get( sortField.getQuery() );
        break;
      case ID_REGION:
        path = regionDO.get( sortField.getQuery() );
        break;
      case REGION_NAME:
        path = regionLanguageDO.get( sortField.getQuery() );
        break;
      case ID_STATE:
        path = stateDO.get( sortField.getQuery() );
        break;
      case THEATER_NAME:
        path = theaterLanguageDO.get( sortField.getQuery() );
        break;
      default:
        path = theaterDO.get( TheaterQuery.THEATER_ID.getQuery() );
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

  /**
   * Obtiene los filtros
   * 
   * @param pagingRequestTO
   * @return
   */
  private Map<ModelQuery, Object> getFilters( PagingRequestTO pagingRequestTO )
  {
    Map<ModelQuery, Object> filters = pagingRequestTO.getFilters();
    if( filters == null )
    {
      filters = new HashMap<ModelQuery, Object>();
    }
    if( !filters.containsKey( TheaterQuery.THEATER_ID_LANGUAGE ) )
    {
      filters.put( TheaterQuery.THEATER_ID_LANGUAGE, pagingRequestTO.getLanguage().getId() );
    }
    return filters;
  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO#save(mx.com.cinepolis.digital.booking.model.to.TheaterTO
   * )
   */
  @Override
  public void save( TheaterTO theaterTO )
  {
    save( theaterTO, Language.ENGLISH );

  }

  /*
   * (non-Javadoc)
   * @see
   * mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO#save(mx.com.cinepolis.digital.booking.model.to.TheaterTO
   * , mx.com.cinepolis.digital.booking.model.constants.Language)
   */
  @Override
  public void save( TheaterTO theaterTO, Language language )
  {
    TheaterDO entity = new TheaterDO();
    AbstractEntityUtils.applyElectronicSign( entity, theaterTO );
    entity.setDsTelephone( theaterTO.getDsTelephone() );
    entity.setDsCCEmail( theaterTO.getDsCCEmail() );
    entity.setIdRegion( new RegionDO( theaterTO.getRegion().getCatalogRegion().getId().intValue() ) );
    entity.setIdVista( theaterTO.getIdVista() );
    entity.setNuTheater( theaterTO.getNuTheater() );
    if( theaterTO.getCity() != null )
    {
      entity.setIdCity( new CityDO( theaterTO.getCity().getId().intValue() ) );
    }
    entity.setIdState( new StateDO( theaterTO.getState().getCatalogState().getId().intValue() ) );
    if( CollectionUtils.isEmpty( entity.getTheaterLanguageDOList() ) )
    {
      entity.setTheaterLanguageDOList( new ArrayList<TheaterLanguageDO>() );
    }
    TheaterLanguageDO theaterLanguageDO = new TheaterLanguageDO();
    theaterLanguageDO.setDsName( theaterTO.getName() );
    theaterLanguageDO.setIdLanguage( new LanguageDO( language.getId() ) );
    theaterLanguageDO.setIdTheater( entity );
    entity.getTheaterLanguageDOList().add( theaterLanguageDO );

    if( theaterTO.getEmail() != null )
    {
      EmailDO email = new EmailDO();
      email.setDsEmail( theaterTO.getEmail().getName() );
      entity.setIdEmail( email );
    }

    this.create( entity );
    this.flush();
    theaterTO.setId( entity.getIdTheater().longValue() );
    if( entity.getIdEmail() != null )
    {
      theaterTO.getEmail().setId( entity.getIdEmail().getIdEmail().longValue() );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update( TheaterTO theaterTO )
  {
    this.update( theaterTO, Language.ENGLISH );

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update( TheaterTO theaterTO, Language language )
  {
    TheaterDO entity = this.find( theaterTO.getId().intValue() );
    if( entity != null )
    {
      AbstractEntityUtils.applyElectronicSign( entity, theaterTO );
      for( TheaterLanguageDO theaterLanguageDO : entity.getTheaterLanguageDOList() )
      {
        if( theaterLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
        {
          theaterLanguageDO.setDsName( theaterTO.getName() );
          break;
        }
      }
      entity.setIdVista( theaterTO.getIdVista() );
      entity.setNuTheater( theaterTO.getNuTheater() );
      entity.setDsTelephone( theaterTO.getDsTelephone() );
      entity.setDsCCEmail( theaterTO.getDsCCEmail() );
      entity.setFgActive( theaterTO.isFgActive() );
      entity.setIdRegion( getRegion( theaterTO.getRegion().getCatalogRegion().getId().intValue() ) );

      if( theaterTO.getEmail() != null )
      {
        EmailDO email = new EmailDO();
        email.setIdEmail( theaterTO.getEmail().getId() != null ? theaterTO.getEmail().getId().intValue() : null );
        email.setDsEmail( theaterTO.getEmail().getName() );
        entity.setIdEmail( email );
      }
      else
      {
        entity.setIdEmail( null );
      }

      if( theaterTO.getCity() != null )
      {
        entity.setIdCity( getCity( theaterTO.getCity().getId().intValue() ) );
      }
      else
      {
        entity.setIdCity( null );
      }
      entity.setIdState( getState( theaterTO.getState().getCatalogState().getId().intValue() ) );
      this.edit( entity );
    }
  }

  private StateDO getState( int idState )
  {
    StateDO state = this.stateDAO.find( idState );
    if( state == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_NOT_FOUND );
    }
    return state;
  }

  private RegionDO getRegion( int idRegion )
  {
    RegionDO region = regionDAO.find( idRegion );
    if( region == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_NOT_FOUND );
    }
    return region;
  }

  private CityDO getCity( int idCity )
  {
    CityDO city = cityDAO.find( idCity );
    if( city == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_NOT_FOUND );
    }
    return city;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete( TheaterTO theaterTO )
  {
    TheaterDO theaterDO = new TheaterDO( theaterTO.getId().intValue() );
    AbstractEntityUtils.applyElectronicSign( theaterDO, theaterTO );
    this.remove( theaterDO );

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( TheaterDO theaterDO )
  {
    TheaterDO remove = super.find( theaterDO.getIdTheater() );
    if( remove != null )
    {
      AbstractEntityUtils.copyElectronicSign( remove, theaterDO );
      remove.setFgActive( false );
      super.edit( remove );
    }
  }

  /**
   * Inner class for parameters sorting
   * 
   * @author gsegura
   */
  static class TheaterDAOSort
  {
    private List<ModelQuery> sortFields;
    private SortOrder sortOrder;
    private CriteriaBuilder cb;
    private CriteriaQuery<TheaterDO> q;
    private Root<TheaterDO> theaterDO;
    private Join<TheaterDO, TheaterLanguageDO> theaterLanguageDO;
    private Join<TheaterLanguageDO, LanguageDO> languageDO;
    private Join<TheaterDO, CityDO> cityDO;
    private Join<TheaterDO, RegionDO> regionDO;
    private Join<TheaterDO, StateDO> stateDO;
    private Join<RegionDO, RegionLanguageDO> regionLanguageDO;
    private Join<RegionLanguageDO, LanguageDO> languageDO2;
    private Join<UserDO, TheaterDO> userDO;
    private Join<RegionDO, UserDO> userRegionDO;

    public List<ModelQuery> getSortFields()
    {
      return sortFields;
    }

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
     * @return the cb
     */
    public CriteriaBuilder getCb()
    {
      return cb;
    }

    /**
     * @param cb the cb to set
     */
    public void setCb( CriteriaBuilder cb )
    {
      this.cb = cb;
    }

    /**
     * @return the q
     */
    public CriteriaQuery<TheaterDO> getQ()
    {
      return q;
    }

    /**
     * @param q the q to set
     */
    public void setQ( CriteriaQuery<TheaterDO> q )
    {
      this.q = q;
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
     * @return the cityDO
     */
    public Join<TheaterDO, CityDO> getCityDO()
    {
      return cityDO;
    }

    /**
     * @param cityDO the cityDO to set
     */
    public void setCityDO( Join<TheaterDO, CityDO> cityDO )
    {
      this.cityDO = cityDO;
    }

    /**
     * @return the regionDO
     */
    public Join<TheaterDO, RegionDO> getRegionDO()
    {
      return regionDO;
    }

    /**
     * @param regionDO the regionDO to set
     */
    public void setRegionDO( Join<TheaterDO, RegionDO> regionDO )
    {
      this.regionDO = regionDO;
    }

    /**
     * @return the stateDO
     */
    public Join<TheaterDO, StateDO> getStateDO()
    {
      return stateDO;
    }

    /**
     * @param stateDO the stateDO to set
     */
    public void setStateDO( Join<TheaterDO, StateDO> stateDO )
    {
      this.stateDO = stateDO;
    }

    public Join<RegionDO, RegionLanguageDO> getRegionLanguageDO()
    {
      return regionLanguageDO;
    }

    public void setRegionLanguageDO( Join<RegionDO, RegionLanguageDO> regionLanguageDO )
    {
      this.regionLanguageDO = regionLanguageDO;
    }

    /**
     * @return the userDO
     */
    public Join<UserDO, TheaterDO> getUserDO()
    {
      return userDO;
    }

    /**
     * @param userDO the userDO to set
     */
    public void setUserDO( Join<UserDO, TheaterDO> userDO )
    {
      this.userDO = userDO;
    }

    /**
     * @return the languageDO2
     */
    public Join<RegionLanguageDO, LanguageDO> getLanguageDO2()
    {
      return languageDO2;
    }

    /**
     * @param languageDO2 the languageDO2 to set
     */
    public void setLanguageDO2( Join<RegionLanguageDO, LanguageDO> languageDO2 )
    {
      this.languageDO2 = languageDO2;
    }

    /**
     * @return the userRegionDO
     */
    public Join<RegionDO, UserDO> getUserRegionDO()
    {
      return userRegionDO;
    }

    /**
     * @param userRegionDO the userRegionDO to set
     */
    public void setUserRegionDO( Join<RegionDO, UserDO> userRegionDO )
    {
      this.userRegionDO = userRegionDO;
    }

  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<TheaterDO> findByIdVistaAndActive( String idVista )
  {
    Query q = this.em.createNamedQuery( "TheaterDO.findByIdVistaAndActive" );
    q.setParameter( "idVista", idVista );
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TheaterDO> findByTheaterName( String dsName )
  {
    return findByTheaterName( dsName, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<TheaterDO> findByTheaterName( String dsName, Language language )
  {
    Query q = this.em.createNamedQuery( "TheaterDO.findByTheaterNameAndLanguage" );
    q.setParameter( "dsName", dsName );
    q.setParameter( ID_LANGUAGE, language.getId() );
    return q.getResultList();
  }

  @Override
  public List<TheaterTO> findByRegionId( CatalogTO id )
  {
    return findByRegionId( id, Language.ENGLISH );
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<TheaterTO> findByRegionId( CatalogTO idRegion, Language language )
  {
    UserDO userDO = null;
    if( idRegion.getUserId() != null )
    {
      userDO = this.userDAO.find( idRegion.getUserId().intValue() );
    }
    // TheaterDO.findByRegion
    List<TheaterTO> theaters = new ArrayList<TheaterTO>();
    Query q = this.em.createNamedQuery( "TheaterDO.findByRegion" );
    q.setParameter( ID_REGION, idRegion.getId() );
    q.setParameter( ID_LANGUAGE, language.getId() );
    List<Object[]> results = q.getResultList();
    for( Object[] o : results )
    {
      TheaterTO theaterTO = new TheaterTO();
      theaterTO.setId( ((Number) o[0]).longValue() );
      theaterTO.setName( (String) o[1] );
      theaterTO.setNuTheater( ((Number) o[2]).intValue() );

      if( userDO != null && CollectionUtils.isNotEmpty( userDO.getTheaterDOList() ) )
      {
        TheaterDO theater = new TheaterDO( theaterTO.getId().intValue() );
        if( userDO.getTheaterDOList().contains( theater ) )
        {
          theaters.add( theaterTO );
        }
      }
      else
      {
        theaters.add( theaterTO );
      }

    }
    return theaters;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<TheaterDO> findByNuTheater( int nuTheater )
  {
    Query q = this.em.createNamedQuery( "TheaterDO.findByNuTheater" );
    q.setParameter( "nuTheater", nuTheater );
    return q.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<TheaterDO> findByIdVista( String idVista )
  {
    Query q = this.em.createNamedQuery( "TheaterDO.findByIdVista" );
    q.setParameter( "idVista", idVista );
    return q.getResultList();
  }
  

  @SuppressWarnings("unchecked")
  @Override
  public List<TheaterDO> getNumberOfTheatersByRegion(Long idRegion)
  {
    Query q =this.em.createNamedQuery( "TheaterDO.getNumberOfTheatersByRegion" );
    q.setParameter( "idRegion", idRegion );
    return q.getResultList();
  }
  @SuppressWarnings("unchecked")
   @Override
   public List<TheaterDO> fidTheatersByRegion(Long idRegion)
   {
     Query q =this.em.createNamedQuery( "TheaterDO.fidTheatersByRegion" );
     q.setParameter( "idRegion", idRegion );
     return q.getResultList();
   }

}
