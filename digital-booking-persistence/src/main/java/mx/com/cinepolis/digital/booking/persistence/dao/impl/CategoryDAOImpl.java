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

import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.query.CategoryQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.utils.CatalogTOComparator;
import mx.com.cinepolis.digital.booking.dao.util.CategoryDOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.CategoryLanguageDO;
import mx.com.cinepolis.digital.booking.model.CategoryTypeDO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class CategoryDAOImpl extends AbstractBaseDAO<CategoryDO> implements CategoryDAO
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
  public CategoryDAOImpl()
  {
    super( CategoryDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( CategoryDO categoryDO )
  {
    CategoryDO remove = super.find( categoryDO.getIdCategory() );
    if( remove != null )
    {
      AbstractEntityUtils.copyElectronicSign( categoryDO, remove );
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

    List<ModelQuery> sortField = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = getFilters( pagingRequestTO );
    Language language = pagingRequestTO.getLanguage();

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<CategoryDO> q = cb.createQuery( CategoryDO.class );
    Root<CategoryDO> categoryDO = q.from( CategoryDO.class );

    Join<CategoryDO, CategoryTypeDO> categoryTypeDO = categoryDO.join( "idCategoryType" );
    Join<CategoryDO, CategoryLanguageDO> categoryLanguageDO = categoryDO.join( "categoryLanguageDOList" );
    Join<CategoryLanguageDO, LanguageDO> languageDO = categoryLanguageDO.join( "idLanguage" );

    q.select( categoryDO );
    applySorting( sortField, sortOrder, cb, q, categoryDO, categoryTypeDO, categoryLanguageDO, languageDO );

    Predicate filterCondition = applyFilters( filters, cb, categoryDO, categoryTypeDO, categoryLanguageDO, languageDO );

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( categoryDO ) );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );
    }

    // pagination
    TypedQuery<CategoryDO> tq = em.createQuery( q );
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
    PagingResponseTO<CatalogTO> response = new PagingResponseTO<CatalogTO>();
    response.setElements( (List<CatalogTO>) CollectionUtils.collect( tq.getResultList(),
      new CategoryDOToCatalogTOTransformer( language ) ) );
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
    filters.put( CategoryQuery.CATEGORY_LANGUAGE_ID, pagingRequestTO.getLanguage().getId() );
    return filters;
  }

  private void applySorting( List<ModelQuery> sortFields, SortOrder sortOrder, CriteriaBuilder cb,
      CriteriaQuery<CategoryDO> q, Root<CategoryDO> categoryDO, Join<CategoryDO, CategoryTypeDO> categoryTypeDO,
      Join<CategoryDO, CategoryLanguageDO> categoryLanguageDO, Join<CategoryLanguageDO, LanguageDO> languageDO )
  {
    if( sortOrder != null && CollectionUtils.isNotEmpty( sortFields ) )
    {
      List<Order> order = new ArrayList<Order>();

      for( ModelQuery sortField : sortFields )
      {
        if( sortField instanceof CategoryQuery )
        {
          Path<?> path = null;
          switch( (CategoryQuery) sortField )
          {
            case CATEGORY_ID:
              path = categoryDO.get( sortField.getQuery() );
              break;
            case CATEGORY_LANGUAGE_ID:
              path = languageDO.get( sortField.getQuery() );
              break;
            case CATEGORY_NAME:
              path = categoryLanguageDO.get( sortField.getQuery() );
              break;
            case CATEGORY_TYPE_ID:
              path = categoryTypeDO.get( sortField.getQuery() );
              break;
            default:
              path = categoryDO.get( CategoryQuery.CATEGORY_ID.getQuery() );
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

  private Predicate applyFilters( Map<ModelQuery, Object> filters, CriteriaBuilder cb, Root<CategoryDO> categoryDO,
      Join<CategoryDO, CategoryTypeDO> categoryTypeDO, Join<CategoryDO, CategoryLanguageDO> categoryLanguageDO,
      Join<CategoryLanguageDO, LanguageDO> languageDO )
  {
    Predicate filterCondition = null;
    if( filters != null && !filters.isEmpty() )
    {
      filterCondition = cb.conjunction();
      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( filters, CategoryQuery.CATEGORY_ID,
        categoryDO, cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( filters, CategoryQuery.CATEGORY_ACTIVE,
        categoryDO, cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters, CategoryQuery.CATEGORY_TYPE_ID,
        categoryTypeDO, cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters,
        CategoryQuery.CATEGORY_LANGUAGE_ID, languageDO, cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinLike( filters, CategoryQuery.CATEGORY_NAME,
        categoryLanguageDO, cb, filterCondition );
    }
    return filterCondition;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( CatalogTO catalogTO, CategoryType categoryType )
  {
    save( catalogTO, categoryType, Language.ENGLISH );

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( CatalogTO catalogTO, CategoryType categoryType, Language language )
  {
    CategoryDO categoryDO = new CategoryDO();
    if( catalogTO.getId() != null )
    {
      categoryDO = this.find( catalogTO.getId().intValue() );
      if( CollectionUtils.isEmpty( categoryDO.getCategoryLanguageDOList() ) )
      {
        categoryDO.setCategoryLanguageDOList( new ArrayList<CategoryLanguageDO>() );
      }
    }
    else
    {
      categoryDO.setIdCategoryType( new CategoryTypeDO( categoryType.getId() ) );
      categoryDO.setCategoryLanguageDOList( new ArrayList<CategoryLanguageDO>() );
    }
    AbstractEntityUtils.applyElectronicSign( categoryDO, catalogTO );

    CategoryLanguageDO categoryLanguageDO = new CategoryLanguageDO();
    categoryLanguageDO.setDsName( catalogTO.getName() );
    categoryLanguageDO.setIdCategory( categoryDO );
    categoryLanguageDO.setIdLanguage( new LanguageDO( language.getId() ) );
    categoryDO.getCategoryLanguageDOList().add( categoryLanguageDO );

    this.create( categoryDO );
    this.flush();
    catalogTO.setId( categoryDO.getIdCategory().longValue() );
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
    CategoryDO categoryDO = this.find( catalogTO.getId().intValue() );
    if( categoryDO != null )
    {
      AbstractEntityUtils.applyElectronicSign( categoryDO, catalogTO );
      for( CategoryLanguageDO categoryLanguageDO : categoryDO.getCategoryLanguageDOList() )
      {
        if( categoryLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
        {
          categoryLanguageDO.setDsName( catalogTO.getName() );
          break;
        }
      }
      this.edit( categoryDO );
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete( CatalogTO catalogTO )
  {
    CategoryDO entity = new CategoryDO( catalogTO.getId().intValue() );
    AbstractEntityUtils.applyElectronicSign( entity, catalogTO );
    this.remove( entity );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CategoryDO> findByDsNameActive( String dsName, Integer idCategoryType )
  {
    return findByDsNameActive( dsName, idCategoryType, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<CategoryDO> findByDsNameActive( String dsName, Integer idCategoryType, Language language )
  {
    Query query = em.createNamedQuery( "CategoryDO.findByDsNameActive" );
    query.setParameter( "dsName", dsName );
    query.setParameter( "idCategoryType", idCategoryType );
    query.setParameter( "idLanguage", language.getId() );
    return query.getResultList();
  }

  @Override
  public List<CatalogTO> getAllByCategoryType( CategoryType categoryType )
  {
    return this.getAllByCategoryType( categoryType, Language.ENGLISH );
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<CatalogTO> getAllByCategoryType( CategoryType categoryType, Language language )
  {
    Query query = em.createNamedQuery( "CategoryDO.findByIdCategoryTypeActive" );
    query.setParameter( "idCategoryType", categoryType.getId() );
    List<CatalogTO> categories = (List<CatalogTO>) CollectionUtils.collect( query.getResultList(),
      new CategoryDOToCatalogTOTransformer( language ) );
    Collections.sort( categories, new CatalogTOComparator( false ) );
    return categories;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public List<CatalogTO> findByIdCategory( Integer idCategory, Language language )
  {
	Query query = em.createNamedQuery( "CategoryDO.findByIdCategory" );
	query.setParameter( "idCategory", idCategory );
	List<CatalogTO> categories = (List<CatalogTO>) CollectionUtils.collect( query.getResultList(),
      new CategoryDOToCatalogTOTransformer( language ) );
    Collections.sort( categories, new CatalogTOComparator( false ) );
    return categories;
  }
  
}
