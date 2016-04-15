package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.query.CityQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.CityTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.dao.util.CityDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.CityDOToCityTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.CityTOToCityDOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.CityDO;
import mx.com.cinepolis.digital.booking.model.CityLanguageDO;
import mx.com.cinepolis.digital.booking.model.CountryDO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.model.StateDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CityDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.CityDAO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class CityDAOImpl extends AbstractBaseDAO<CityDO> implements CityDAO
{

  private static final String ID_COUNTRY = "idCountry";
  private static final String CITY_LANGUAGE_LIST = "cityLanguageDOList";
  private static final String ID_LANGUAGE = "idLanguage";
  private static final String ID_STATE = "idState";

  /**
   * Entity Manager
   */
  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

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
  public CityDAOImpl()
  {
    super( CityDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> findAllCities()
  {
    return findAllCities( Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<CatalogTO> findAllCities( Language language )
  {
    List<CityDO> cities = new ArrayList<CityDO>();
    cities.addAll( CollectionUtils.select(
      super.findAll(),
      PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "isFgActive" ),
        PredicateUtils.equalPredicate( true ) ) ) );

    List<CatalogTO> citieTOs = new ArrayList<CatalogTO>();
    citieTOs.addAll( CollectionUtils.collect( cities, new CityDOToCatalogTOTransformer( language ) ) );
    return citieTOs;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CityDO> findByIdVistaAndActive( String idVista )
  {
    Query q = em.createNamedQuery( "CityDO.findByIdVistaAndActive" );
    q.setParameter( "idVista", idVista );
    return q.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CityDO> findByIdVista( String idVista )
  {
    Query q = em.createNamedQuery( "CityDO.findByIdVista" );
    q.setParameter( "idVista", idVista );
    return q.getResultList();
  }

  @SuppressWarnings("unchecked")
  private List<LanguageDO> findAllLanguage()
  {
    Query q = em.createNamedQuery( "LanguageDO.findAll" );
    return q.getResultList();
  }

  @Override
  public void update( CityTO cityTO )
  {
    this.update( cityTO, Language.ENGLISH );
  }

  @Override
  public void update( CityTO cityTO, Language language )
  {
    CityDO cityDO = this.find( cityTO.getId().intValue() );
    if( cityDO != null )
    {
      AbstractEntityUtils.applyElectronicSign( cityDO, cityTO );
      cityDO.setFgActive( cityTO.isFgActive() );
      cityDO.setIdVista( cityTO.getIdVista() );
      cityDO.setIdCountry( new CountryDO( cityTO.getCountry().getId().intValue() ) );
      cityDO.setIdState( new StateDO( cityTO.getState().getCatalogState().getId().intValue() ) );
      for( CityLanguageDO cityLanguageDO : cityDO.getCityLanguageDOList() )
      {
        if( language.getId() == Language.ALL.getId() )
        {
          cityLanguageDO.setDsName( cityTO.getName() );
        }
        else if( cityLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
        {
          cityLanguageDO.setDsName( cityTO.getName() );
          break;
        }
      }
      this.edit( cityDO );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( CityTO city )
  {
    this.save( city, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( CityTO city, Language language )
  {
    CityDO cityDO = (CityDO) new CityTOToCityDOTransformer().transform( city );
    AbstractEntityUtils.applyElectronicSign( cityDO, city );
    if( language.getId() == Language.ALL.getId() )
    {
      for( LanguageDO languageDO : this.findAllLanguage() )
      {
        CityLanguageDO cityLanguageDO = new CityLanguageDO();
        cityLanguageDO.setDsName( city.getName() );
        cityLanguageDO.setIdCity( cityDO );
        cityLanguageDO.setIdLanguage( languageDO );
        cityDO.getCityLanguageDOList().add( cityLanguageDO );
      }
    }
    else
    {
      CityLanguageDO cityLanguageDO = new CityLanguageDO();
      cityLanguageDO.setDsName( city.getName() );
      cityLanguageDO.setIdCity( cityDO );
      cityLanguageDO.setIdLanguage( new LanguageDO( language.getId() ) );
      cityDO.getCityLanguageDOList().add( cityLanguageDO );
    }
    cityDO.setIdCountry( new CountryDO( city.getCountry().getId().intValue() ) );
    cityDO.setIdState( new StateDO( city.getState().getCatalogState().getId().intValue() ) );
    this.create( cityDO );
    this.flush();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CityDO> findByIdCountry( Long idCountry )
  {
    Query q = em.createNamedQuery( "CityDO.findByIdCountry" );
    q.setParameter( "idCountry", idCountry );
    return q.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CityDO> findByIdState( Long idState )
  {
    Query q = em.createNamedQuery( "CityDO.findByIdStateAndActive" );
    q.setParameter( "idState", idState );
    return q.getResultList();
  }

  /*
   * @SuppressWarnings("unchecked") private void updateCitiesAndStates() { Query q = this.em.createNamedQuery(
   * "TheaterDO.findByFgActive" ); q.setParameter( "fgActive", Boolean.TRUE ); for(TheaterDO theaterDO :
   * (List<TheaterDO>)q.getResultList()) { CityDO cityDO = theaterDO.getIdCity(); cityDO.setIdState(
   * theaterDO.getIdState() ); this.edit( cityDO ); } this.flush(); }
   */

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public PagingResponseTO<CityTO> findAllByPaging( PagingRequestTO pagingRequestTO )
  {
    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = this.getFilters( pagingRequestTO );
    Language language = pagingRequestTO.getLanguage();

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<CityDO> q = cb.createQuery( CityDO.class );
    Root<CityDO> cityDO = q.from( CityDO.class );
    Join<CityLanguageDO, CityDO> cityLanguageDO = cityDO.join( CITY_LANGUAGE_LIST );
    Join<LanguageDO, CityLanguageDO> languageDOForCity = cityLanguageDO.join( ID_LANGUAGE );
    Join<CountryDO, CityDO> countryDO = cityDO.join( ID_COUNTRY );
    Join<StateDO, CityDO> stateDO = cityDO.join( ID_STATE, JoinType.LEFT );

    q.distinct( true ).select( cityDO );
    CitySortFilter citySortFilter = new CitySortFilter();
    citySortFilter.setCriteriaBuilder( cb );
    citySortFilter.setCriteriaQuery( q );
    citySortFilter.setFilters( filters );
    citySortFilter.setCityDO( cityDO );
    citySortFilter.setCityLanguageDO( cityLanguageDO );
    citySortFilter.setLanguageDOForCity( languageDOForCity );
    citySortFilter.setCountryDO( countryDO );
    citySortFilter.setStateDO( stateDO );
    citySortFilter.setSortFields( sortFields );
    citySortFilter.setSortOrder( sortOrder );

    this.applySorting( citySortFilter );

    Predicate filterCondition = this.applyFilters( citySortFilter );

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( cityDO ) );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );
    }

    // pagination
    TypedQuery<CityDO> tq = em.createQuery( q );
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

    PagingResponseTO<CityTO> response = new PagingResponseTO<CityTO>();
    response.setElements( (List<CityTO>) CollectionUtils.collect( tq.getResultList(), new CityDOToCityTOTransformer(
        language ) ) );
    response.setTotalCount( count );

    // this.updateCitiesAndStates();

    return response;
  }

  /**
   * Method that gets the criteria filters to consultation.
   * 
   * @param pagingRequestTO, with the request information.
   * @return filters, a {@link java.util.Map<ModelQuery, Object>} with the filters.
   */
  private Map<ModelQuery, Object> getFilters( PagingRequestTO pagingRequestTO )
  {
    Map<ModelQuery, Object> filters = pagingRequestTO.getFilters();
    if( filters == null )
    {
      filters = new HashMap<ModelQuery, Object>();
    }
    filters.put( CityQuery.LANGUAGE_ID, pagingRequestTO.getLanguage().getId() );
    return filters;
  }

  /**
   * Method that applies the sort order for query results.
   * 
   * @param citySortFilter, with the criteria query information.
   */
  private void applySorting( CitySortFilter citySortFilter )
  {
    List<ModelQuery> sortFields = citySortFilter.getSortFields();
    SortOrder sortOrder = citySortFilter.getSortOrder();
    CriteriaBuilder cb = citySortFilter.getCriteriaBuilder();
    CriteriaQuery<CityDO> q = citySortFilter.getCriteriaQuery();
    Root<CityDO> cityDO = citySortFilter.getCityDO();
    Join<CityLanguageDO, CityDO> cityLanguageDO = citySortFilter.getCityLanguageDO();
    if( sortOrder != null && CollectionUtils.isNotEmpty( sortFields ) )
    {
      List<Order> order = new ArrayList<Order>();
      for( ModelQuery sortField : sortFields )
      {
        if( sortField instanceof CityQuery )
        {
          Path<?> path = null;
          if( sortField.equals( CityQuery.CITY_NAME ) )
          {
            path = cityLanguageDO.get( sortField.getQuery() );
          }
          else
          {
            path = cityDO.get( sortField.getQuery() );
          }
          if( sortOrder.equals( SortOrder.ASCENDING ) && path != null )
          {
            order.add( cb.asc( path ) );
          }
          else
          {
            order.add( cb.desc( path ) );
          }
        }
      }
      q.orderBy( order );
    }
  }

  /**
   * Method that applies the filter criteria to query.
   * 
   * @param citySortFilter, with the criteria query information.
   * @return filterCondition, with the applied conditions to criteria query.
   */
  private Predicate applyFilters( CitySortFilter citySortFilter )
  {
    Predicate filterCondition = null;
    if( citySortFilter.getFilters() != null && !citySortFilter.getFilters().isEmpty() )
    {
      filterCondition = citySortFilter.getCriteriaBuilder().conjunction();

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( citySortFilter.getFilters(),
        CityQuery.CITY_ID, citySortFilter.getCityDO(), citySortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinLike( citySortFilter.getFilters(), CityQuery.CITY_NAME,
        citySortFilter.getCityLanguageDO(), citySortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( citySortFilter.getFilters(),
        CityQuery.LANGUAGE_ID, citySortFilter.getLanguageDOForCity(), citySortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( citySortFilter.getFilters(),
        CityQuery.CITY_ACTIVE, citySortFilter.getCityDO(), citySortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( citySortFilter.getFilters(),
        CityQuery.COUNTRY_ID, citySortFilter.getCountryDO(), citySortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( citySortFilter.getFilters(),
        CityQuery.STATE_ID, citySortFilter.getStateDO(), citySortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterRootLike( citySortFilter.getFilters(), CityQuery.CITY_ID_VISTA,
        citySortFilter.getCityDO(), citySortFilter.getCriteriaBuilder(), filterCondition );

    }
    return filterCondition;
  }

  /**
   * Class that contains the filter information for a query about cities.
   * 
   * @author jreyesv
   */
  static class CitySortFilter
  {
    private List<ModelQuery> sortFields;
    private SortOrder sortOrder;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<CityDO> criteriaQuery;
    private Root<CityDO> cityDO;
    private Join<CityLanguageDO, CityDO> cityLanguageDO;
    private Join<LanguageDO, CityLanguageDO> languageDOForCity;
    private Join<CountryDO, CityDO> countryDO;
    private Join<StateDO, CityDO> stateDO;
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

    /**
     * @return the criteriaQuery
     */
    public CriteriaQuery<CityDO> getCriteriaQuery()
    {
      return criteriaQuery;
    }

    /**
     * @param criteriaQuery the criteriaQuery to set
     */
    public void setCriteriaQuery( CriteriaQuery<CityDO> criteriaQuery )
    {
      this.criteriaQuery = criteriaQuery;
    }

    /**
     * @return the cityDO
     */
    public Root<CityDO> getCityDO()
    {
      return cityDO;
    }

    /**
     * @param cityDO the cityDO to set
     */
    public void setCityDO( Root<CityDO> cityDO )
    {
      this.cityDO = cityDO;
    }

    /**
     * @return the countryDO
     */
    public Join<CountryDO, CityDO> getCountryDO()
    {
      return countryDO;
    }

    /**
     * @param countryDO the countryDO to set
     */
    public void setCountryDO( Join<CountryDO, CityDO> countryDO )
    {
      this.countryDO = countryDO;
    }

    /**
     * @return the stateDO
     */
    public Join<StateDO, CityDO> getStateDO()
    {
      return stateDO;
    }

    /**
     * @param stateDO the stateDO to set
     */
    public void setStateDO( Join<StateDO, CityDO> stateDO )
    {
      this.stateDO = stateDO;
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
     * @return the cityLanguageDO
     */
    public Join<CityLanguageDO, CityDO> getCityLanguageDO()
    {
      return cityLanguageDO;
    }

    /**
     * @param cityLanguageDO the cityLanguageDO to set
     */
    public void setCityLanguageDO( Join<CityLanguageDO, CityDO> cityLanguageDO )
    {
      this.cityLanguageDO = cityLanguageDO;
    }

    /**
     * @return the languageDOForCity
     */
    public Join<LanguageDO, CityLanguageDO> getLanguageDOForCity()
    {
      return languageDOForCity;
    }

    /**
     * @param languageDOForCity the languageDOForCity to set
     */
    public void setLanguageDOForCity( Join<LanguageDO, CityLanguageDO> languageDOForCity )
    {
      this.languageDOForCity = languageDOForCity;
    }

  }

}
