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

import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.ScreenQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.ScreenDOToScreenTOTransformer;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class ScreenDAOImpl extends AbstractBaseDAO<ScreenDO> implements ScreenDAO
{
  /**
   * Entity Manager
   */
  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @EJB
  private CategoryDAO categoryDAO;

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
  public ScreenDAOImpl()
  {
    super( ScreenDO.class );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO#save(mx.com
   * .cinepolis.digital.booking.model.to.ScreenTO)
   */
  @Override
  public void save( ScreenTO screenTO )
  {
    ScreenDO entity = new ScreenDO();
    AbstractEntityUtils.applyElectronicSign( entity, screenTO );
    entity.setIdTheater( new TheaterDO( screenTO.getIdTheater() ) );
    entity.setIdVista( screenTO.getIdVista() );
    entity.setNuCapacity( screenTO.getNuCapacity() );
    entity.setNuScreen( screenTO.getNuScreen() );
    entity.setCategoryDOList( new ArrayList<CategoryDO>() );
    for( CatalogTO catalogTO : screenTO.getSoundFormats() )
    {
      CategoryDO categorySound = categoryDAO.find( catalogTO.getId().intValue() );
      categorySound.getScreenDOList().add( entity );
      entity.getCategoryDOList().add( categorySound );
    }
    for( CatalogTO catalogTO : screenTO.getMovieFormats() )
    {
      CategoryDO categoryMovieFormat = categoryDAO.find( catalogTO.getId().intValue() );
      categoryMovieFormat.getScreenDOList().add( entity );
      entity.getCategoryDOList().add( categoryMovieFormat );
    }
    if( screenTO.getScreenFormat() != null )
    {
      CategoryDO categoryMovieFormat = categoryDAO.find( screenTO.getScreenFormat().getId().intValue() );
      categoryMovieFormat.getScreenDOList().add( entity );
      entity.getCategoryDOList().add( categoryMovieFormat );
    }

    this.create( entity );
    this.flush();
    screenTO.setId( entity.getIdScreen().longValue() );

  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO#update(mx.
   * com.cinepolis.digital.booking.model.to.ScreenTO)
   */
  @Override
  public void update( ScreenTO screenTO )
  {
    ScreenDO entity = this.find( screenTO.getId().intValue() );
    if( entity != null )
    {
      AbstractEntityUtils.applyElectronicSign( entity, screenTO );
      entity.setNuCapacity( screenTO.getNuCapacity() );
      entity.setNuScreen( screenTO.getNuScreen() );
      entity.setIdVista( screenTO.getIdVista() );

      updateCategories( screenTO, entity );

      this.edit( entity );

    }

  }

  private void updateCategories( ScreenTO screenTO, ScreenDO entity )
  {
    List<CatalogTO> categories = new ArrayList<CatalogTO>();

    // Limpieza de categorías
    for( CategoryDO categoryDO : entity.getCategoryDOList() )
    {
      categoryDO.getScreenDOList().remove( entity );
      this.categoryDAO.edit( categoryDO );
    }
    entity.setCategoryDOList( new ArrayList<CategoryDO>() );

    for( CatalogTO to : screenTO.getMovieFormats() )
    {     
      categories.add( to );
    }
    for( CatalogTO to : screenTO.getSoundFormats() )
    {
      categories.add( to );
    }
    if( screenTO.getScreenFormat() != null )
    {
      categories.add( screenTO.getScreenFormat() );
    }

    for( CatalogTO catalogTO : categories )
    {
      CategoryDO category = this.categoryDAO.find( catalogTO.getId().intValue() );
      category.getScreenDOList().add( entity );
      entity.getCategoryDOList().add( category );
    }

  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO #remove(java.lang.Object)
   */
  @Override
  public void remove( ScreenDO screenDO )
  {
    ScreenDO remove = super.find( screenDO.getIdScreen() );
    if( remove != null )
    {
      AbstractEntityUtils.copyElectronicSign( remove, screenDO );
      remove.setFgActive( false );
      super.edit( remove );
    }
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO#delete(mx.
   * com.cinepolis.digital.booking.model.to.ScreenTO)
   */
  @Override
  public void delete( ScreenTO screenTO )
  {
    ScreenDO screenDO = new ScreenDO( screenTO.getId().intValue() );
    AbstractEntityUtils.applyElectronicSign( screenDO, screenTO );
    this.remove( screenDO );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO#findAllByPaging
   * (mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @SuppressWarnings("unchecked")
  @Override
  public PagingResponseTO<ScreenTO> findAllByPaging( PagingRequestTO pagingRequestTO )
  {

    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = getFilters( pagingRequestTO );

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<ScreenDO> q = cb.createQuery( ScreenDO.class );
    Root<ScreenDO> screenDO = q.from( ScreenDO.class );

    Join<ScreenDO, TheaterDO> theatherDO = screenDO.join( "idTheater" );

    q.select( screenDO );
    applySorting( sortFields, sortOrder, cb, q, screenDO, theatherDO );

    Predicate filterCondition = applyFilters( filters, cb, screenDO, theatherDO );

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( screenDO ) );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );
    }

    // pagination
    TypedQuery<ScreenDO> tq = em.createQuery( q );
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

    PagingResponseTO<ScreenTO> response = new PagingResponseTO<ScreenTO>();
    response.setElements( (List<ScreenTO>) CollectionUtils.collect( tq.getResultList(),
      new ScreenDOToScreenTOTransformer( pagingRequestTO.getLanguage() ) ) );
    response.setTotalCount( count );

    return response;
  }

  /**
   * Aplicamos los filtros
   * 
   * @param filters
   * @param cb
   * @param screenDO
   * @param theaterDO
   * @param categoryLanguage
   * @return
   */
  private Predicate applyFilters( Map<ModelQuery, Object> filters, CriteriaBuilder cb, Root<ScreenDO> screenDO,
      Join<ScreenDO, TheaterDO> theaterDO )
  {
    Predicate filterCondition = null;
    if( filters != null && !filters.isEmpty() )
    {
      filterCondition = cb.conjunction();

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( filters, ScreenQuery.SCREEN_ID, screenDO,
        cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( filters, ScreenQuery.SCREEN_NUMBER,
        screenDO, cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( filters, ScreenQuery.SCREEN_CAPACITY,
        screenDO, cb, filterCondition );
      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters, ScreenQuery.SCREEN_THEATER_ID,
        theaterDO, cb, filterCondition );

    }
    return filterCondition;
  }

  /**
   * Metodo que aplica el campo por le cual se ordenaran
   * 
   * @param sortField
   * @param sortOrder
   * @param cb
   * @param q
   * @param screenDO
   * @param theather
   */
  private void applySorting( List<ModelQuery> sortFields, SortOrder sortOrder, CriteriaBuilder cb,
      CriteriaQuery<ScreenDO> q, Root<ScreenDO> screenDO, Join<ScreenDO, TheaterDO> theather )
  {
    if( sortOrder != null && CollectionUtils.isNotEmpty( sortFields ) )
    {
      List<Order> order = new ArrayList<Order>();
      for( ModelQuery sortField : sortFields )
      {
        if( sortField instanceof ScreenQuery )
        {
          Path<?> path = null;
          switch( (ScreenQuery) sortField )
          {
            case SCREEN_ID:
              path = screenDO.get( sortField.getQuery() );
              break;
            case SCREEN_NUMBER:
              path = screenDO.get( sortField.getQuery() );
              break;
            case SCREEN_CAPACITY:
              path = screenDO.get( sortField.getQuery() );
              break;
            case SCREEN_THEATER_ID:
              path = theather.get( sortField.getQuery() );
              break;
            default:
              path = screenDO.get( ScreenQuery.SCREEN_ID.getQuery() );
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
   * Obtiene los filtros y añade el lenguaje
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

  @SuppressWarnings("unchecked")
  @Override
  public List<ScreenDO> findAllActiveByIdCinema( Integer idTheater )
  {
    Query q = this.em.createNamedQuery( "ScreenDO.findAllActiveByIdCinema" );
    q.setParameter( "idTheater", idTheater );
    return q.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ScreenDO> findByIdVistaAndActive( String idVista )
  {
    Query q = this.em.createNamedQuery( "ScreenDO.findByIdVistaAndActive" );
    q.setParameter( "idVista", idVista );
    return q.getResultList();
  }
}
