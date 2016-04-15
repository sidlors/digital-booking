package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.query.TerritoryQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.utils.CatalogTOComparator;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.TerritoryDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.model.TerritoryDO;
import mx.com.cinepolis.digital.booking.model.TerritoryLanguageDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TerritoryDAO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.TerritoryDAO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class TerritoryDAOImpl extends AbstractBaseDAO<TerritoryDO> implements TerritoryDAO
{
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
  public TerritoryDAOImpl()
  {
    super( TerritoryDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( TerritoryDO territoryDO )
  {
    TerritoryDO remove = super.find( territoryDO.getIdTerritory() );
    if( remove != null )
    {
      AbstractEntityUtils.copyElectronicSign( territoryDO, remove );
      remove.setFgActive( false );
      super.edit( remove );
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public PagingResponseTO<CatalogTO> findAllByPaging( PagingRequestTO pagingRequestTO )
  {
    int page = pagingRequestTO.getPage();
    int pageSize = pagingRequestTO.getPageSize();
    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = getFilters( pagingRequestTO );
    Language language = pagingRequestTO.getLanguage();

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<TerritoryDO> q = cb.createQuery( TerritoryDO.class );
    Root<TerritoryDO> territoryDO = q.from( TerritoryDO.class );
    Join<TerritoryDO, TerritoryLanguageDO> territoryLanguageDO = territoryDO.join( "territoryLanguageDOList" );
    Join<TerritoryLanguageDO, LanguageDO> languageDO = territoryLanguageDO.join( "idLanguage" );

    q.distinct( true ).select( territoryDO );
    applySorting( sortFields, sortOrder, cb, q, territoryDO, territoryLanguageDO, languageDO );

    Predicate filterCondition = applyFilters( filters, cb, territoryDO, territoryLanguageDO, languageDO );

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( territoryDO ) );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );
    }

    // pagination
    TypedQuery<TerritoryDO> tq = em.createQuery( q );
    int count = em.createQuery( queryCountRecords ).getSingleResult().intValue();

    if( pageSize > 0 )
    {
      tq.setMaxResults( pageSize );
    }
    if( page >= 0 )
    {
      tq.setFirstResult( page * pageSize );
    }

    PagingResponseTO<CatalogTO> response = new PagingResponseTO<CatalogTO>();
    response.setElements( (List<CatalogTO>) CollectionUtils.collect( tq.getResultList(),
      new TerritoryDOToCatalogTOTransformer( language ) ) );
    response.setTotalCount( count );

    return response;
  }

  private Map<ModelQuery, Object> getFilters( PagingRequestTO pagingRequestTO )
  {
    Map<ModelQuery, Object> filters = pagingRequestTO.getFilters();
    if( filters == null )
    {
      filters = new HashMap<ModelQuery, Object>();
    }
    filters.put( TerritoryQuery.TERRITORY_LANGUAGE_ID, pagingRequestTO.getLanguage().getId() );
    return filters;
  }

  private Predicate applyFilters( Map<ModelQuery, Object> filters, CriteriaBuilder cb, Root<TerritoryDO> territoryDO,
      Join<TerritoryDO, TerritoryLanguageDO> territoryLanguageDO, Join<TerritoryLanguageDO, LanguageDO> languageDO )
  {
    Predicate filterCondition = null;
    if( filters != null && !filters.isEmpty() )
    {
      filterCondition = cb.conjunction();

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( filters, TerritoryQuery.TERRITORY_ID,
        territoryDO, cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters,
        TerritoryQuery.TERRITORY_LANGUAGE_ID, languageDO, cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinLike( filters, TerritoryQuery.TERRITORY_NAME,
        territoryLanguageDO, cb, filterCondition );
    }
    return filterCondition;
  }

  private void applySorting( List<ModelQuery> sortFields, SortOrder sortOrder, CriteriaBuilder cb,
      CriteriaQuery<TerritoryDO> q, Root<TerritoryDO> territoryDO,
      Join<TerritoryDO, TerritoryLanguageDO> territoryLanguageDO, Join<TerritoryLanguageDO, LanguageDO> languageDO )
  {
    if( sortOrder != null && CollectionUtils.isNotEmpty( sortFields ) )
    {
      List<Order> order = new ArrayList<Order>();
      for( ModelQuery sortField : sortFields )
      {
        if( sortField instanceof TerritoryQuery )
        {
          Path<?> path = null;
          switch( (TerritoryQuery) sortField )
          {
            case TERRITORY_ID:
              path = territoryDO.get( sortField.getQuery() );
              break;
            case TERRITORY_LANGUAGE_ID:
              path = languageDO.get( sortField.getQuery() );
              break;
            case TERRITORY_NAME:
              path = territoryLanguageDO.get( sortField.getQuery() );
              break;
            default:
              path = territoryDO.get( TerritoryQuery.TERRITORY_ID.getQuery() );
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
      q.orderBy( order );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( CatalogTO catalogTO )
  {
    save( catalogTO, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( CatalogTO catalogTO, Language language )
  {
    TerritoryDO territory = new TerritoryDO();
    if( catalogTO.getId() != null )
    {
      territory = this.find( catalogTO.getId().intValue() );
      if( CollectionUtils.isEmpty( territory.getTerritoryLanguageDOList() ) )
      {
        territory.setTerritoryLanguageDOList( new ArrayList<TerritoryLanguageDO>() );
      }
    }
    else
    {
      territory.setTerritoryLanguageDOList( new ArrayList<TerritoryLanguageDO>() );
    }
    AbstractEntityUtils.applyElectronicSign( territory, catalogTO );

    TerritoryLanguageDO territoryLanguageDO = new TerritoryLanguageDO();
    territoryLanguageDO.setDsName( catalogTO.getName() );
    territoryLanguageDO.setIdTerritory( territory );
    territoryLanguageDO.setIdLanguage( new LanguageDO( language.getId() ) );
    territory.getTerritoryLanguageDOList().add( territoryLanguageDO );

    this.create( territory );
    this.flush();
    catalogTO.setId( territory.getIdTerritory().longValue() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update( CatalogTO catalogTO )
  {
    update( catalogTO, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update( CatalogTO catalogTO, Language language )
  {
    TerritoryDO territoryDO = this.find( catalogTO.getId().intValue() );
    if( territoryDO != null )
    {
      AbstractEntityUtils.applyElectronicSign( territoryDO, catalogTO );
      for( TerritoryLanguageDO territoryLanguageDO : territoryDO.getTerritoryLanguageDOList() )
      {
        if( territoryLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
        {
          territoryLanguageDO.setDsName( catalogTO.getName() );
          break;
        }
      }
      this.edit( territoryDO );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete( CatalogTO catalogTO )
  {
    TerritoryDO entity = new TerritoryDO( catalogTO.getId().intValue() );
    AbstractEntityUtils.applyElectronicSign( entity, catalogTO );
    this.remove( entity );
  }

  @Override
  public List<CatalogTO> getAll()
  {
    return getAll( Language.ENGLISH );
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CatalogTO> getAll( Language language )
  {
    Query q = this.em.createNamedQuery( "TerritoryDO.findByFgActive" );
    q.setParameter( "fgActive", true );

    List<CatalogTO> territories = (List<CatalogTO>) CollectionUtils.collect( q.getResultList(),
      new TerritoryDOToCatalogTOTransformer( language ) );
    Collections.sort( territories, new CatalogTOComparator( true ) );

    return territories;
  }

}
