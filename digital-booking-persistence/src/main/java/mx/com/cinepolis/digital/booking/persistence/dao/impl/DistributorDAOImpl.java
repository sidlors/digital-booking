package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mx.com.cinepolis.digital.booking.commons.query.DistributorQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.DistributorDOToDistributorTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class DistributorDAOImpl extends AbstractBaseDAO<DistributorDO> implements DistributorDAO
{

  private static final String QUERY_FIND_BY_DS_NAME = "DistributorDO.findByDsShortName";
  private static final String PARAMETER_DS_SHORT_NAME = "dsShortName";
  
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
   * Constructor default
   */
  public DistributorDAOImpl()
  {
    super( DistributorDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( DistributorDO distributorDO )
  {
    DistributorDO remove = super.find( distributorDO.getIdDistributor() );
    if( remove != null )
    {
      AbstractEntityUtils.copyElectronicSign( distributorDO, remove );
      remove.setFgActive( false );
      super.edit( remove );
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public PagingResponseTO<DistributorTO> findAllByPaging( PagingRequestTO pagingRequestTO )
  {

    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = pagingRequestTO.getFilters();

    CriteriaBuilder cb = em.getCriteriaBuilder();

    CriteriaQuery<DistributorDO> q = cb.createQuery( DistributorDO.class );
    Root<DistributorDO> distributorDO = q.from( DistributorDO.class );
    q.select( distributorDO );
    if( sortOrder != null && CollectionUtils.isNotEmpty( sortFields ) )
    {
      List<Order> order = new ArrayList<Order>();

      for( ModelQuery sortField : sortFields )
      {
        Path<?> path = getPath( sortField, distributorDO );
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

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( distributorDO ) );

    Predicate predicate = applyFilters( filters, cb, distributorDO );
    if( predicate != null )
    {
      q.where( predicate );
      queryCountRecords.where( predicate );
    }

    // pagination
    TypedQuery<DistributorDO> tq = em.createQuery( q );
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

    PagingResponseTO<DistributorTO> response = new PagingResponseTO<DistributorTO>();
    response.setElements( (List<DistributorTO>) CollectionUtils.collect( tq.getResultList(),
      new DistributorDOToDistributorTOTransformer() ) );

    response.setTotalCount( em.createQuery( queryCountRecords ).getSingleResult().intValue() );

    return response;

  }

  private Predicate applyFilters( Map<ModelQuery, Object> filters, CriteriaBuilder cb, Root<DistributorDO> distributorDO )
  {
    Predicate filterCondition = null;
    if( filters != null && !filters.isEmpty() )
    {
      filterCondition = cb.conjunction();

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( filters, DistributorQuery.DISTRIBUTOR_ID,
        distributorDO, cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterRootLike( filters, DistributorQuery.DISTRIBUTOR_NAME,
        distributorDO, cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( filters,
        DistributorQuery.DISTRIBUTOR_ACTIVE, distributorDO, cb, filterCondition );

    }
    return filterCondition;
  }

  private Path<?> getPath( ModelQuery sortField, Root<DistributorDO> distributorDO )
  {
    // sort
    Path<?> path = null;
    if( sortField instanceof DistributorQuery )
    {
      path = distributorDO.get( sortField.getQuery() );
    }
    else
    {
      path = distributorDO.get( DistributorQuery.DISTRIBUTOR_ID.getQuery() );
    }

    return path;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( DistributorTO catalogTO )
  {
    DistributorDO entity = new DistributorDO();
    AbstractEntityUtils.applyElectronicSign( entity, catalogTO );
    entity.setDsName( catalogTO.getName() );
    entity.setIdVista( catalogTO.getIdVista() );
    entity.setDsShortName( catalogTO.getShortName() );
    this.create( entity );

    this.flush();
    catalogTO.setId( entity.getIdDistributor().longValue() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update( DistributorTO catalogTO )
  {
    DistributorDO entity = this.find( catalogTO.getId().intValue() );
    AbstractEntityUtils.applyElectronicSign( entity, catalogTO );
    entity.setDsName( catalogTO.getName() );
    entity.setIdVista( catalogTO.getIdVista() );
    entity.setDsShortName( catalogTO.getShortName() );
    this.edit( entity );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete( DistributorTO catalogTO )
  {
    DistributorDO entity = new DistributorDO( catalogTO.getId().intValue() );
    AbstractEntityUtils.applyElectronicSign( entity, catalogTO );
    this.remove( entity );

  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<DistributorDO> findByDsNameActive( String dsName )
  {
    Query query = em.createNamedQuery( "DistributorDO.findByDsNameActive" );
    query.setParameter( "dsName", dsName );
    return query.getResultList();
  }
  
  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<DistributorDO> findByDsName( String dsName )
  {
    Query query = em.createNamedQuery( "DistributorDO.findByDsName" );
    query.setParameter( "dsName", dsName );
    return query.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<DistributorTO> getAll()
  {
    List<DistributorDO> distibutors = new ArrayList<DistributorDO>();
    CollectionUtils.select(
      super.findAll(),
      PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "isFgActive" ),
        PredicateUtils.equalPredicate( true ) ), distibutors );

    List<DistributorTO> tos = new ArrayList<DistributorTO>();
    CollectionUtils.collect( distibutors, new DistributorDOToDistributorTOTransformer(), tos );
    Collections.sort( tos );
    return tos;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<DistributorDO> findByIdVistaAndActive( String idVista )
  {
    Query q = em.createNamedQuery( "DistributorDO.findByIdVistaAndActive" );
    q.setParameter( "idVista", idVista );
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DistributorTO get( Integer idDistributor )
  {
    DistributorTO catalogTO = null;
    DistributorDO distibutorDO = find( idDistributor );
    if( distibutorDO != null )
    {
      catalogTO = (DistributorTO) new DistributorDOToDistributorTOTransformer().transform( distibutorDO );
    }
    return catalogTO;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<DistributorDO> findByIdVista( String idVista )
  {
    Query q = em.createNamedQuery( "DistributorDO.findByIdVista" );
    q.setParameter( "idVista", idVista );
    return q.getResultList();
  }
  
  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<DistributorDO> findByDsShortNameActive( String dsShortName )
  {
    Query query = em.createNamedQuery( QUERY_FIND_BY_DS_NAME );
    query.setParameter( PARAMETER_DS_SHORT_NAME, dsShortName );
    return query.getResultList();
  }

}
