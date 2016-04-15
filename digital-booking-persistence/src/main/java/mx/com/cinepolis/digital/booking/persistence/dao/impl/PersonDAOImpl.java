package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.PersonQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.PersonDOToPersonTOTransformer;
import mx.com.cinepolis.digital.booking.model.PersonDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.PersonDAO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Class that implements the methods of the Data Access Object related to Person. Implementation of the interface
 * {@link mx.com.cinepolis.digital.booking.persistence.dao.PersonDAO}
 * 
 * @author agustin.ramirez
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class PersonDAOImpl extends AbstractBaseDAO<PersonDO> implements PersonDAO
{

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
   * Constructor
   */
  public PersonDAOImpl()
  {
    super( PersonDO.class );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.PersonDAO#findAllByPaging
   * (mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @SuppressWarnings("unchecked")
  @Override
  public PagingResponseTO<PersonTO> findAllByPaging( PagingRequestTO pagingRequestTO )
  {
    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = getFilters( pagingRequestTO );
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<PersonDO> q = cb.createQuery( PersonDO.class );
    q.distinct( Boolean.TRUE );
    Root<PersonDO> personDO = q.from( PersonDO.class );
    Join<PersonDO, TheaterDO> theaterDO = personDO.join( "theaterDOList" );
    Join<PersonDO, UserDO> userDO = personDO.join( "user" );

    q.select( personDO );

    PersonSortFilter personDAOSort = new PersonSortFilter();
    personDAOSort.setSortFields( sortFields );
    personDAOSort.setSortOrder( sortOrder );
    personDAOSort.setPersonDO( personDO );
    personDAOSort.setCriteriaBuilder( cb );
    personDAOSort.setCriteriaQuery( q );
    personDAOSort.setTheaterDO( theaterDO );
    personDAOSort.setUserDO( userDO );
    personDAOSort.setFilters( filters );

    applySorting( personDAOSort );

    Predicate filterCondition = applyFilters( personDAOSort );

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( personDO ) );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );

    }

    // pagination
    TypedQuery<PersonDO> tq = em.createQuery( q );

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
    PagingResponseTO<PersonTO> response = new PagingResponseTO<PersonTO>();

    response.setElements( (List<PersonTO>) CollectionUtils.collect( tq.getResultList(),
      new PersonDOToPersonTOTransformer() ) );
    response.setTotalCount( count );

    return response;
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

    return filters;
  }

  /**
   * Aplicamos Filtros
   * 
   * @param personSortFilter
   */
  private Predicate applyFilters( PersonSortFilter personSortFilter )
  {
    Predicate filterCondition = null;
    if( personSortFilter.getFilters() != null && !personSortFilter.getFilters().isEmpty() )
    {
      filterCondition = personSortFilter.getCriteriaBuilder().conjunction();

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( personSortFilter.getFilters(),
        PersonQuery.PERSON_ID, personSortFilter.getPersonDO(), personSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( personSortFilter.getFilters(),
        PersonQuery.PERSON_ACTIVE, personSortFilter.getPersonDO(), personSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder
          .applyFilterRootLike( personSortFilter.getFilters(), PersonQuery.PERSON_NAME, personSortFilter.getPersonDO(),
            personSortFilter.getCriteriaBuilder(), filterCondition );
      filterCondition = CriteriaQueryBuilder.applyFilterRootLike( personSortFilter.getFilters(),
        PersonQuery.PERSON_LAST_NAME, personSortFilter.getPersonDO(), personSortFilter.getCriteriaBuilder(),
        filterCondition );
      filterCondition = CriteriaQueryBuilder.applyFilterRootLike( personSortFilter.getFilters(),
        PersonQuery.PERSON_MOTHER_LAST_NAME, personSortFilter.getPersonDO(), personSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( personSortFilter.getFilters(),
        PersonQuery.PERSON_ID_USER, personSortFilter.getUserDO(), personSortFilter.getCriteriaBuilder(),
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( personSortFilter.getFilters(),
        PersonQuery.PERSON_ID_THEATER, personSortFilter.getTheaterDO(), personSortFilter.getCriteriaBuilder(),
        filterCondition );

    }
    return filterCondition;
  }

  /**
   * Aplica Filtros de Ordenamiento
   * 
   * @param personSortFilter
   */
  private void applySorting( PersonSortFilter personSortFilter )
  {
    List<ModelQuery> sortFields = personSortFilter.getSortFields();
    SortOrder sortOrder = personSortFilter.getSortOrder();
    CriteriaQuery<PersonDO> q = personSortFilter.getCriteriaQuery();

    if( sortOrder != null && CollectionUtils.isNotEmpty( sortFields ) )
    {
      List<Order> order = new ArrayList<Order>();
      for( ModelQuery sortField : sortFields )
      {
        if( sortField instanceof PersonQuery )
        {
          sortFields( personSortFilter, order, sortField );
        }
      }
      q.orderBy( order );
    }
  }

  private void sortFields( PersonSortFilter personSortFilter, List<Order> order, ModelQuery sortField )
  {
    CriteriaBuilder cb = personSortFilter.getCriteriaBuilder();
    SortOrder sortOrder = personSortFilter.getSortOrder();
    Root<PersonDO> personDO = personSortFilter.getPersonDO();
    Join<PersonDO, UserDO> userDO = personSortFilter.getUserDO();
    Join<PersonDO, TheaterDO> theaterDO = personSortFilter.getTheaterDO();
    Path<?> path = null;
    switch( (PersonQuery) sortField )
    {
      case PERSON_ID:
        path = personDO.get( sortField.getQuery() );
        break;
      case PERSON_NAME:
        path = personDO.get( sortField.getQuery() );
        break;
      case PERSON_LAST_NAME:
        path = personDO.get( sortField.getQuery() );
        break;
      case PERSON_MOTHER_LAST_NAME:
        path = personDO.get( sortField.getQuery() );
        break;

      case PERSON_ID_THEATER:
        path = theaterDO.get( sortField.getQuery() );
        break;
      case PERSON_ID_USER:
        path = userDO.get( sortField.getQuery() );
        break;
      default:
        path = personDO.get( PersonQuery.PERSON_ID.getQuery() );
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
   * Clase estatica de filtros de ordenamiento
   * 
   * @author agustin.ramirez
   */
  static class PersonSortFilter
  {
    private List<ModelQuery> sortFields;
    private SortOrder sortOrder;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<PersonDO> criteriaQuery;
    private Root<PersonDO> personDO;
    private Join<PersonDO, UserDO> userDO;
    private Join<PersonDO, TheaterDO> theaterDO;
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
    public CriteriaQuery<PersonDO> getCriteriaQuery()
    {
      return criteriaQuery;
    }

    /**
     * @param criteriaQuery the criteriaQuery to set
     */
    public void setCriteriaQuery( CriteriaQuery<PersonDO> criteriaQuery )
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
     * @return the personDO
     */
    public Root<PersonDO> getPersonDO()
    {
      return personDO;
    }

    /**
     * @param personDO the personDO to set
     */
    public void setPersonDO( Root<PersonDO> personDO )
    {
      this.personDO = personDO;
    }

    /**
     * @return the userDO
     */
    public Join<PersonDO, UserDO> getUserDO()
    {
      return userDO;
    }

    /**
     * @param userDO the userDO to set
     */
    public void setUserDO( Join<PersonDO, UserDO> userDO )
    {
      this.userDO = userDO;
    }

    /**
     * @return the theaterDO
     */
    public Join<PersonDO, TheaterDO> getTheaterDO()
    {
      return theaterDO;
    }

    /**
     * @param theaterDO the theaterDO to set
     */
    public void setTheaterDO( Join<PersonDO, TheaterDO> theaterDO )
    {
      this.theaterDO = theaterDO;
    }

  }

}
