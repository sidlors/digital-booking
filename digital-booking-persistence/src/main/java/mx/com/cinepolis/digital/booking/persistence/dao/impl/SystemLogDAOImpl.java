package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Date;
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

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.query.SystemLogQuery;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemLogTO;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.SystemLogDOToSystemLogTOTransformer;
import mx.com.cinepolis.digital.booking.model.OperationLanguageDO;
import mx.com.cinepolis.digital.booking.model.PersonDO;
import mx.com.cinepolis.digital.booking.model.ProcessLanguageDO;
import mx.com.cinepolis.digital.booking.model.SystemLogDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.SystemLogDAO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.SystemLogDO}
 * 
 * @author jcarbajal
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class SystemLogDAOImpl extends AbstractBaseDAO<SystemLogDO> implements SystemLogDAO
{
  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  private static final String ID_USER = "idUser";
  private static final String ID_PROCESS = "idProcess";
  private static final String ID_OPERATION = "idOperation";
  private static final String ID_PERSON = "idPerson";

  /**
   * Constructor by default
   */
  public SystemLogDAOImpl()
  {
    super( SystemLogDO.class );
  }

  public SystemLogDAOImpl( Class<SystemLogDO> entityClass )
  {
    super( entityClass );
  }

  /**
   * Get the entity manager
   */
  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

  @SuppressWarnings("unchecked")
  @Override
  public PagingResponseTO<SystemLogTO> findAllSystemLogByPaging( PagingRequestTO pagingRequestTO )
  {
    Boolean isDistinct = Boolean.TRUE;
    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = getFilters( pagingRequestTO );
    Language language = Language.ENGLISH;
    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<SystemLogDO> q = cb.createQuery( SystemLogDO.class );
    Root<SystemLogDO> systemLogDO = q.from( SystemLogDO.class );
    Join<UserDO, SystemLogDO> userDO = systemLogDO.join( ID_USER );
    Join<PersonDO, UserDO> personDO = userDO.join( ID_PERSON );
    Join<ProcessLanguageDO, SystemLogDO> processLanguageDO = systemLogDO.join( ID_PROCESS );
    Join<OperationLanguageDO, SystemLogDO> operationLanguageDO = systemLogDO.join( ID_OPERATION );

    q.select( systemLogDO );
    q.distinct( isDistinct );
    SystemLogSortFilter systemLogSortFilter = new SystemLogSortFilter();
    systemLogSortFilter.setSortFields( sortFields );
    systemLogSortFilter.setSortOrder( sortOrder );
    systemLogSortFilter.setCriteriaBuilder( cb );
    systemLogSortFilter.setCriteriaQuery( q );
    systemLogSortFilter.setSystemLogDO( systemLogDO );
    systemLogSortFilter.setUserDO( userDO );
    systemLogSortFilter.setPersonDO( personDO );
    systemLogSortFilter.setProcessLanguageDO( processLanguageDO );
    systemLogSortFilter.setOperationLanguageDO( operationLanguageDO );
    systemLogSortFilter.setFilters( filters );
    applySorting( systemLogSortFilter );

    Predicate filterCondition = applyFiltersToSystemLog( systemLogSortFilter );

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( systemLogDO ) );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );
    }

    TypedQuery<SystemLogDO> tq = em.createQuery( q );
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
    PagingResponseTO<SystemLogTO> response = new PagingResponseTO<SystemLogTO>();
    SystemLogDOToSystemLogTOTransformer transformer = new SystemLogDOToSystemLogTOTransformer( language );
    response.setElements( (List<SystemLogTO>) CollectionUtils.collect( tq.getResultList(), transformer ) );
    response.setTotalCount( count );
    return response;
  }

/**
 * apply sorting at the result of the query
 * @param systemLogSort
 */

  private void applySorting( SystemLogSortFilter systemLogSort )
  {

    List<ModelQuery> sortFields = systemLogSort.getSortFields();
    SortOrder sortOrder = systemLogSort.getSortOrder();

    CriteriaBuilder cb = systemLogSort.getCriteriaBuilder();

    CriteriaQuery<SystemLogDO> q = systemLogSort.getCriteriaQuery();
    Root<SystemLogDO> systemLogDO = systemLogSort.getSystemLogDO();
    Join<UserDO, SystemLogDO> userDO = systemLogSort.getUserDO();
    Join<PersonDO, UserDO> personDO = systemLogSort.getPersonDO();
    // Join<ProcessLanguageDO, SystemLogDO> processLanguageDO = systemLogSort.getProcessLanguageDO();
    // Join<OperationLanguageDO, SystemLogDO> operationLanguageDO = systemLogSort.getOperationLanguageDO();
    if( sortOrder != null && CollectionUtils.isNotEmpty( sortFields ) )
    {
      List<Order> order = new ArrayList<Order>();
      Map<ModelQuery, Path<?>> sortMap = new HashMap<ModelQuery, Path<?>>();
      sortMap.put( SystemLogQuery.DT_OPERATION, systemLogDO );
      sortMap.put( SystemLogQuery.PERSON_NAME, personDO );
      sortMap.put( SystemLogQuery.USER_NAME, userDO );
      for( ModelQuery sortField : sortFields )
      {
        Path<?> path = null;
        if( sortMap.containsKey( sortField ) )
        {
          path = sortMap.get( sortField ).get( sortField.getQuery() );
        }
        else
        {
          path = systemLogDO.get( SystemLogQuery.ID_SYSTEM_LOG.getQuery() );
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

  private Predicate applyFiltersToSystemLog( SystemLogSortFilter systemLogSortFilter )
  {
    Predicate filterCondition = null;
    if( systemLogSortFilter.getFilters() != null && !systemLogSortFilter.getFilters().isEmpty() )
    {
      filterCondition = systemLogSortFilter.getCriteriaBuilder().conjunction();

      filterCondition = CriteriaQueryBuilder.applyFilterJoinEqual( systemLogSortFilter.getFilters(),
        SystemLogQuery.ID_OPERATION, systemLogSortFilter.operationLanguageDO, systemLogSortFilter.criteriaBuilder,
        filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinEqual( systemLogSortFilter.getFilters(),
        SystemLogQuery.ID_USER, systemLogSortFilter.userDO, systemLogSortFilter.criteriaBuilder, filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinEqual( systemLogSortFilter.getFilters(),
        SystemLogQuery.ID_PROCESS, systemLogSortFilter.processLanguageDO, systemLogSortFilter.criteriaBuilder,
        filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinLike( systemLogSortFilter.getFilters(),
        SystemLogQuery.PERSON_NAME, systemLogSortFilter.personDO, systemLogSortFilter.criteriaBuilder, filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinLike( systemLogSortFilter.getFilters(),
        SystemLogQuery.PERSON_LAST_NAME, systemLogSortFilter.personDO, systemLogSortFilter.criteriaBuilder,
        filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinLike( systemLogSortFilter.getFilters(),
        SystemLogQuery.USER_NAME, systemLogSortFilter.userDO, systemLogSortFilter.criteriaBuilder, filterCondition );

      if( systemLogSortFilter.getFilters().containsKey( SystemLogQuery.START_DATE )
          && systemLogSortFilter.getFilters().get( SystemLogQuery.START_DATE ) instanceof Date )
      {
        Date startDate = (Date) systemLogSortFilter.getFilters().get( SystemLogQuery.START_DATE );
        //startDate = DateUtils.truncate( startDate, Calendar.DATE );
        
        Date finalDate = (Date) systemLogSortFilter.getFilters().get( SystemLogQuery.FINAL_DATE );
        //finalDate = DateUtils.truncate( finalDate, Calendar.DATE );


        Path<Date> operationDate = systemLogSortFilter.systemLogDO.get( SystemLogQuery.DT_OPERATION.getQuery() );
        filterCondition = systemLogSortFilter.getCriteriaBuilder().and( filterCondition,
          systemLogSortFilter.getCriteriaBuilder().between( operationDate, startDate, finalDate ));//( operationDate, finalDate ) );

        //filterCondition = systemLogSortFilter.getCriteriaBuilder().and( filterCondition,
          //systemLogSortFilter.getCriteriaBuilder().greaterThanOrEqualTo( operationDate, startDate ) );

      }
    }
    return filterCondition;
  }

  /**
   * Method for get the filters
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
  static class SystemLogSortFilter
  {
    private List<ModelQuery> sortFields;
    private SortOrder sortOrder;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<SystemLogDO> criteriaQuery;
    private Root<SystemLogDO> systemLogDO;
    private Join<UserDO, SystemLogDO> userDO;
    private Join<PersonDO, UserDO> personDO;
    private Join<ProcessLanguageDO, SystemLogDO> processLanguageDO;
    private Join<OperationLanguageDO, SystemLogDO> operationLanguageDO;
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
    public CriteriaQuery<SystemLogDO> getCriteriaQuery()
    {
      return criteriaQuery;
    }

    /**
     * @param criteriaQuery the criteriaQuery to set
     */
    public void setCriteriaQuery( CriteriaQuery<SystemLogDO> criteriaQuery )
    {
      this.criteriaQuery = criteriaQuery;
    }

    /**
     * @return the systemLogDO
     */
    public Root<SystemLogDO> getSystemLogDO()
    {
      return systemLogDO;
    }

    /**
     * @param systemLogDO the systemLogDO to set
     */
    public void setSystemLogDO( Root<SystemLogDO> systemLogDO )
    {
      this.systemLogDO = systemLogDO;
    }

    /**
     * @return the userDO
     */
    public Join<UserDO, SystemLogDO> getUserDO()
    {
      return userDO;
    }

    /**
     * @param userDO the userDO to set
     */
    public void setUserDO( Join<UserDO, SystemLogDO> userDO )
    {
      this.userDO = userDO;
    }

    /**
     * @return the personDO
     */
    public Join<PersonDO, UserDO> getPersonDO()
    {
      return personDO;
    }

    /**
     * @param personDO the personDO to set
     */
    public void setPersonDO( Join<PersonDO, UserDO> personDO )
    {
      this.personDO = personDO;
    }

    /**
     * @return the processLanguageDO
     */
    public Join<ProcessLanguageDO, SystemLogDO> getProcessLanguageDO()
    {
      return processLanguageDO;
    }

    /**
     * @param processLanguageDO the processLanguageDO to set
     */
    public void setProcessLanguageDO( Join<ProcessLanguageDO, SystemLogDO> processLanguageDO )
    {
      this.processLanguageDO = processLanguageDO;
    }

    /**
     * @return the operationLanguageDO
     */
    public Join<OperationLanguageDO, SystemLogDO> getOperationLanguageDO()
    {
      return operationLanguageDO;
    }

    /**
     * @param operationLanguageDO the operationLanguageDO to set
     */
    public void setOperationLanguageDO( Join<OperationLanguageDO, SystemLogDO> operationLanguageDO )
    {
      this.operationLanguageDO = operationLanguageDO;
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

  }

}
