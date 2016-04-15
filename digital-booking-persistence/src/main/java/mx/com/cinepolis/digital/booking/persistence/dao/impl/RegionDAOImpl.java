package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
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
import mx.com.cinepolis.digital.booking.commons.query.RegionQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.RegionDOToRegionTOTransformer;
import mx.com.cinepolis.digital.booking.model.EmailDO;
import mx.com.cinepolis.digital.booking.model.LanguageDO;
import mx.com.cinepolis.digital.booking.model.PersonDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.RegionLanguageDO;
import mx.com.cinepolis.digital.booking.model.TerritoryDO;
import mx.com.cinepolis.digital.booking.model.TerritoryLanguageDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EmailDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.IncomeSettingsDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.PersonDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TerritoryDAO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class RegionDAOImpl extends AbstractBaseDAO<RegionDO> implements RegionDAO
{
  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @EJB
  private TerritoryDAO territoryDAO;

  @EJB
  private PersonDAO personDAO;

  @EJB
  private EmailDAO emailDAO;

  @EJB
  private IncomeSettingsDAO incomeSettingsDAO;

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
  public RegionDAOImpl()
  {
    super( RegionDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( RegionDO regionDO )
  {
    RegionDO remove = super.find( regionDO.getIdRegion() );
    if( remove != null )
    {
      AbstractEntityUtils.copyElectronicSign( regionDO, remove );
      remove.setFgActive( false );
      super.edit( remove );
    }
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> findAllByPaging( PagingRequestTO pagingRequestTO )
  {

    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = getFilters( pagingRequestTO );
    filters.put( RegionQuery.REGION_ID_USER, pagingRequestTO.getUserId() );
    Language language = pagingRequestTO.getLanguage();

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<RegionDO> q = cb.createQuery( RegionDO.class );
    Root<RegionDO> regionDO = q.from( RegionDO.class );
    Join<RegionDO, RegionLanguageDO> regionLanguageDO = regionDO.join( "regionLanguageDOList" );
    Join<RegionLanguageDO, LanguageDO> languageDO = regionLanguageDO.join( "idLanguage" );
    Join<TerritoryDO, RegionDO> territoryDO = regionDO.join( "idTerritory" );
    Join<TerritoryDO, TerritoryLanguageDO> territoryLanguageDO = territoryDO.join( "territoryLanguageDOList" );
    Join<TerritoryLanguageDO, LanguageDO> languageTerritoryDO = territoryLanguageDO.join( "idLanguage" );
    // Se filtran los usuarios de la region
    Join<UserDO, RegionDO> userDO = regionDO.join( "userDOList" );

    q.distinct( true ).select( regionDO );
    applySorting( sortFields, sortOrder, cb, q, regionDO, regionLanguageDO, languageDO, territoryDO,
      territoryLanguageDO );

    Predicate filterCondition = applyFilters( filters, cb, regionDO, regionLanguageDO, languageDO, territoryDO,
      territoryLanguageDO, languageTerritoryDO, userDO );

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( territoryDO ) );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );
    }

    // pagination
    this.em.getEntityManagerFactory().getCache().evictAll();
    TypedQuery<RegionDO> tq = em.createQuery( q );
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

    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = new PagingResponseTO<RegionTO<CatalogTO, CatalogTO>>();
    response.setElements( new ArrayList<RegionTO<CatalogTO, CatalogTO>>() );
    List<RegionDO> listaX = tq.getResultList();
    response.getElements().addAll(
      (List<RegionTO<CatalogTO, CatalogTO>>) CollectionUtils.collect( listaX,
        new RegionDOToRegionTOTransformer( language ) ) );
    response.setTotalCount( count );

    return response;
  }

  private Predicate applyFilters( Map<ModelQuery, Object> filters, CriteriaBuilder cb, Root<RegionDO> regionDO,
      Join<RegionDO, RegionLanguageDO> regionLanguageDO, Join<RegionLanguageDO, LanguageDO> languageRegionDO,
      Join<TerritoryDO, RegionDO> territoryDO, Join<TerritoryDO, TerritoryLanguageDO> territoryLanguageDO,
      Join<TerritoryLanguageDO, LanguageDO> languageTerritoryDO, Join<UserDO, RegionDO> userDO )
  {
    Predicate filterCondition = null;
    if( filters != null && !filters.isEmpty() )
    {
      filterCondition = cb.conjunction();

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( filters, RegionQuery.REGION_ID, regionDO,
        cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinLike( filters, RegionQuery.REGION_NAME, regionLanguageDO,
        cb, filterCondition );

      if( filters.containsKey( RegionQuery.REGION_LANGUAGE_ID ) )
      {
        filterCondition = CriteriaQueryBuilder.applyFilterJoinEqual( filters, RegionQuery.REGION_LANGUAGE_ID,
          languageRegionDO, cb, filterCondition );

        filterCondition = CriteriaQueryBuilder.applyFilterJoinEqual( filters, RegionQuery.REGION_LANGUAGE_ID,
          languageTerritoryDO, cb, filterCondition );
      }

      filterCondition = CriteriaQueryBuilder.applyFilterJoinEqual( filters, RegionQuery.TERRITORY_ID, territoryDO, cb,
        filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterJoinLike( filters, RegionQuery.TERRITORY_NAME,
        territoryLanguageDO, cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( filters, RegionQuery.REGION_ACTIVE,
        regionDO, cb, filterCondition );

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters, RegionQuery.REGION_ID_USER,
        userDO, cb, filterCondition );
    }
    return filterCondition;
  }

  private void applySorting( List<ModelQuery> sortFields, SortOrder sortOrder, CriteriaBuilder cb,
      CriteriaQuery<RegionDO> q, Root<RegionDO> regionDO, Join<RegionDO, RegionLanguageDO> regionLanguageDO,
      Join<RegionLanguageDO, LanguageDO> languageRegionDO, Join<TerritoryDO, RegionDO> territoryDO,
      Join<TerritoryDO, TerritoryLanguageDO> territoryLanguageDO )
  {
    if( sortOrder != null && CollectionUtils.isNotEmpty( sortFields ) )
    {
      List<Order> order = new ArrayList<Order>();

      Map<ModelQuery, Path<?>> sortMap = new HashMap<ModelQuery, Path<?>>();
      sortMap.put( RegionQuery.REGION_ID, regionDO );
      sortMap.put( RegionQuery.REGION_NAME, regionLanguageDO );
      sortMap.put( RegionQuery.REGION_LANGUAGE_ID, languageRegionDO );
      sortMap.put( RegionQuery.TERRITORY_ID, territoryDO );
      sortMap.put( RegionQuery.TERRITORY_NAME, territoryLanguageDO );

      for( ModelQuery sortField : sortFields )
      {
        Path<?> path = null;
        if( sortMap.containsKey( sortField ) )
        {
          path = sortMap.get( sortField ).get( sortField.getQuery() );
        }
        else
        {
          path = regionDO.get( RegionQuery.REGION_ID.getQuery() );
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
    if( !filters.containsKey( RegionQuery.REGION_LANGUAGE_ID ) )
    {
      filters.put( RegionQuery.REGION_LANGUAGE_ID, pagingRequestTO.getLanguage().getId() );
    }
    return filters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( RegionTO<CatalogTO, CatalogTO> region )
  {
    save( region, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( RegionTO<CatalogTO, CatalogTO> region, Language language )
  {
    RegionDO regionDO = new RegionDO();

    if( region.getCatalogRegion() != null && region.getCatalogRegion().getId() != null )
    {
      regionDO = this.find( region.getCatalogRegion().getId().intValue() );
      if( CollectionUtils.isEmpty( regionDO.getRegionLanguageDOList() ) )
      {
        regionDO.setRegionLanguageDOList( new ArrayList<RegionLanguageDO>() );
      }
    }
    else
    {
      regionDO.setIdTerritory( new TerritoryDO( region.getIdTerritory().getId().intValue() ) );
      regionDO.setRegionLanguageDOList( new ArrayList<RegionLanguageDO>() );
    }
    AbstractEntityUtils.applyElectronicSign( regionDO, region.getCatalogRegion() );
    RegionLanguageDO regionLanguageDO = new RegionLanguageDO();
    regionLanguageDO.setIdRegion( regionDO );
    regionLanguageDO.setDsName( region.getCatalogRegion().getName() );
    regionLanguageDO.setIdLanguage( new LanguageDO( language.getId() ) );
    regionDO.getRegionLanguageDOList().add( regionLanguageDO );

    this.associatePersons( regionDO, region );

    this.create( regionDO );
    this.flush();
    region.getCatalogRegion().setId( regionDO.getIdRegion().longValue() );
  }

  private void associatePersons( RegionDO regionDO, RegionTO<CatalogTO, CatalogTO> region )
  {
    if( CollectionUtils.isNotEmpty( region.getPersons() ) )
    {
      List<PersonDO> persons = new ArrayList<PersonDO>();
      for( PersonTO person : region.getPersons() )
      {
        PersonDO personDO = new PersonDO();
        AbstractEntityUtils.applyElectronicSign( personDO, region.getCatalogRegion() );
        personDO.setIdPerson( person.getId() != null ? person.getId().intValue() : null );
        personDO.setDsName( person.getName() );
        personDO.setDsLastname( person.getDsLastname() );
        personDO.setDsMotherLastname( person.getDsMotherLastname() );
        if( CollectionUtils.isNotEmpty( person.getEmails() ) )
        {
          CatalogTO to = person.getEmails().get( 0 );
          EmailDO emailDO = new EmailDO();
          emailDO.setIdPerson( personDO );
          emailDO.setDsEmail( to.getName() );
          emailDO.setIdEmail( to.getId() != null ? to.getId().intValue() : null );
          if( emailDO.getIdEmail() == null )
          {
            emailDAO.create( emailDO );
          }
          personDO.setEmailDOList( Arrays.asList( emailDO ) );
        }
        personDO.setRegionDOList( Arrays.asList( regionDO ) );

        if( person.getId() == null )
        {
          personDAO.create( personDO );
        }
        persons.add( personDO );
      }
      regionDO.setPersonDOList( persons );

    }
    else
    {
      regionDO.setPersonDOList( null );
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update( RegionTO<CatalogTO, CatalogTO> region )
  {
    update( region, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update( RegionTO<CatalogTO, CatalogTO> region, Language language )
  {
    RegionDO regionDO = this.find( region.getCatalogRegion().getId().intValue() );
    if( regionDO != null )
    {
      AbstractEntityUtils.applyElectronicSign( regionDO, region.getCatalogRegion() );
      for( RegionLanguageDO regionLanguageDO : regionDO.getRegionLanguageDOList() )
      {
        if( regionLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
        {
          regionLanguageDO.setDsName( region.getCatalogRegion().getName() );
          break;
        }
      }
      regionDO.setIdTerritory( getTerritory( region.getIdTerritory().getId().intValue() ) );
      associatePersons( regionDO, region );
      this.edit( regionDO );
    }
  }

  private TerritoryDO getTerritory( int idTerritory )
  {
    TerritoryDO territoryDO = this.territoryDAO.find( idTerritory );
    if( territoryDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_NOT_FOUND );
    }
    return territoryDO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete( RegionTO<CatalogTO, CatalogTO> region )
  {
    RegionDO entity = new RegionDO( region.getCatalogRegion().getId().intValue() );
    AbstractEntityUtils.applyElectronicSign( entity, region.getCatalogRegion() );
    this.remove( entity );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RegionDO> findByDsNameActive( String dsName )
  {
    return findByDsNameActive( dsName, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<RegionDO> findByDsNameActive( String dsName, Language language )
  {
    Query query = em.createNamedQuery( "RegionDO.findByDsNameActive" );
    query.setParameter( "dsName", dsName );
    query.setParameter( "idLanguage", language.getId() );
    return query.getResultList();
  }

  @SuppressWarnings("unchecked")
  @Override
  public RegionTO<CatalogTO, CatalogTO> getRegionById( Integer id )
  {
    RegionDO regionDO = this.find( id );
    return (RegionTO<CatalogTO, CatalogTO>) new RegionDOToRegionTOTransformer().transform( regionDO );
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<RegionDO> findActiveRegions()
  {
    Query query = em.createNamedQuery( "RegionDO.findByFgActive" );
    query.setParameter( "fgActive", true );
    return query.getResultList();
  }

}
