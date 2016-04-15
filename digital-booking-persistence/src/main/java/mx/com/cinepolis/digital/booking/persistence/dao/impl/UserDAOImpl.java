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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.query.UserQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.dao.util.UserDOToUserTOTransformer;
import mx.com.cinepolis.digital.booking.model.EmailDO;
import mx.com.cinepolis.digital.booking.model.PersonDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.RoleDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EmailDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.PersonDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RoleDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Class that implements the methods of the Data Access Object related to User. Implementation of the interface
 * {@link mx.com.cinepolis.digital.booking.persistence.dao.UserDAO}
 * 
 * @author agustin.ramirez
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class UserDAOImpl extends AbstractBaseDAO<UserDO> implements UserDAO
{
  /**
   * PersonDAO
   */
  @EJB
  private PersonDAO personDAO;

  /**
   * Email DAO
   */
  @EJB
  private EmailDAO emailDAO;

  /**
   * Role DAO
   */
  @EJB
  private RoleDAO roleDAO;

  /**
   * Region DAO
   */
  @EJB
  private RegionDAO regionDAO;

  /**
   * Theater DAO
   */
  @EJB
  private TheaterDAO theaterDAO;

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
  public UserDAOImpl()
  {
    super( UserDO.class );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.UserDAO#findByUsername (java.lang.String)
   */
  @SuppressWarnings("unchecked")
  @Override
  public UserTO getByUsername( String username )
  {
    UserTO userTO = null;
    Query q = em.createNamedQuery( "UserDO.findByDsUsername" );
    q.setParameter( "dsUsername", username );
    UserDO userDO = (UserDO) CinepolisUtils.findFirstElement( q.getResultList() );
    if( userDO != null )
    {
      UserDOToUserTOTransformer transformer = new UserDOToUserTOTransformer();
      userTO = (UserTO) transformer.transform( userDO );
    }
    return userTO;
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.UserDAO#findAllByPaging
   * (mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @SuppressWarnings("unchecked")
  @Override
  public PagingResponseTO<UserTO> findAllByPaging( PagingRequestTO pagingRequestTO )
  {
    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = getFilters( pagingRequestTO );
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<UserDO> q = cb.createQuery( UserDO.class );
    q.distinct( Boolean.TRUE );
    Root<UserDO> userDO = q.from( UserDO.class );

    q.select( userDO );

    UserSortFilter userDAOSort = new UserSortFilter();
    userDAOSort.setSortFields( sortFields );
    userDAOSort.setSortOrder( sortOrder );
    userDAOSort.setCriteriaBuilder( cb );
    userDAOSort.setCriteriaQuery( q );
    userDAOSort.setUserDO( userDO );
    userDAOSort.setFilters( filters );

    applySorting( userDAOSort );

    Predicate filterCondition = applyFilters( userDAOSort );

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( userDO ) );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );

    }

    // pagination
    TypedQuery<UserDO> tq = em.createQuery( q );

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
    PagingResponseTO<UserTO> response = new PagingResponseTO<UserTO>();
    response
        .setElements( (List<UserTO>) CollectionUtils.collect( tq.getResultList(), new UserDOToUserTOTransformer() ) );
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
   * Aplica filtros
   * 
   * @param userSortFilter
   * @return
   */

  private Predicate applyFilters( UserSortFilter userSortFilter )
  {
    Predicate filterCondition = null;
    if( userSortFilter.getFilters() != null && !userSortFilter.getFilters().isEmpty() )
    {
      filterCondition = userSortFilter.getCriteriaBuilder().conjunction();

      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterRootEqual( userSortFilter.getFilters(),
        UserQuery.USER_ID, userSortFilter.getUserDO(), userSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( userSortFilter.getFilters(),
        UserQuery.USER_ACTIVE, userSortFilter.getUserDO(), userSortFilter.getCriteriaBuilder(), filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterRootLike( userSortFilter.getFilters(), UserQuery.USER_NAME,
        userSortFilter.getUserDO(), userSortFilter.getCriteriaBuilder(), filterCondition );

    }
    return filterCondition;
  }

  /**
   * Aplica Filtros de Ordenamiento
   * 
   * @param userSortFilter
   */
  private void applySorting( UserSortFilter userSortFilter )
  {
    List<ModelQuery> sortFields = userSortFilter.getSortFields();
    SortOrder sortOrder = userSortFilter.getSortOrder();
    CriteriaBuilder cb = userSortFilter.getCriteriaBuilder();
    CriteriaQuery<UserDO> q = userSortFilter.getCriteriaQuery();
    Root<UserDO> userDO = userSortFilter.getUserDO();

    if( sortOrder != null && CollectionUtils.isNotEmpty( sortFields ) )
    {
      List<Order> order = new ArrayList<Order>();
      Map<ModelQuery, Path<?>> sortMap = new HashMap<ModelQuery, Path<?>>();
      sortMap.put( UserQuery.USER_ID, userDO );
      sortMap.put( UserQuery.USER_NAME, userDO );

      for( ModelQuery sortField : sortFields )
      {
        Path<?> path = null;
        if( sortMap.containsKey( sortField ) )
        {
          path = sortMap.get( sortField ).get( sortField.getQuery() );
        }
        else
        {
          path = userDO.get( UserQuery.USER_ID.getQuery() );
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

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.UserDAO#save(mx.com.
   * cinepolis.digital.booking.model.to.UserTO)
   */
  @Override
  public void save( UserTO userTO )
  {
    UserDO newUser = new UserDO();
    newUser.setDsUsername( userTO.getName() );
    newUser.setFgActive( Boolean.TRUE );
    AbstractEntityUtils.applyElectronicSign( newUser, userTO );
    if( userTO.getPersonTO() != null )
    {
      PersonDO newPerson = new PersonDO();
      AbstractEntityUtils.applyElectronicSign( newPerson, userTO );
      newPerson.setDsName( userTO.getPersonTO().getName() );
      newPerson.setDsLastname( userTO.getPersonTO().getDsLastname() );
      newPerson.setFgActive( Boolean.TRUE );
      newPerson.setDsMotherLastname( userTO.getPersonTO().getDsMotherLastname() );
      personDAO.create( newPerson );
      personDAO.flush();
      userTO.getPersonTO().setId( newPerson.getIdPerson().longValue() );
      EmailDO emailDO = new EmailDO();
      emailDO.setDsEmail( userTO.getPersonTO().getEmails().get( 0 ).getName() );
      emailDO.setIdPerson( newPerson );
      emailDAO.create( emailDO );
      emailDAO.flush();
      newPerson.setEmailDOList( new ArrayList<EmailDO>() );
      newPerson.getEmailDOList().add( emailDO );
      personDAO.edit( newPerson );
      userTO.getPersonTO().getEmails().get( 0 ).setId( emailDO.getIdEmail().longValue() );
      newUser.setIdPerson( newPerson );
    }
    if( !CollectionUtils.isEmpty( userTO.getRoles() ) )
    {
      newUser.setRoleDOList( new ArrayList<RoleDO>() );
      for( CatalogTO c : userTO.getRoles() )
      {
        RoleDO role = roleDAO.find( c.getId().intValue() );
        role.getUserDOList().add( newUser );
        newUser.getRoleDOList().add( role );
      }
    }

    this.create( newUser );
    this.flush();
    userTO.setId( Long.valueOf( newUser.getIdUser() ) );

  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.UserDAO#edit(mx.com.
   * cinepolis.digital.booking.model.to.UserTO)
   */
  @Override
  public void update( UserTO userTO )
  {
    UserDO userUpdate = this.find( userTO.getId().intValue() );
    if( userUpdate == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_PERSISTENCE_ERROR_USER_NOT_FOUND );

    }
    AbstractEntityUtils.applyElectronicSign( userUpdate, userTO );
    PersonDO personUpdate = userUpdate.getIdPerson();
    AbstractEntityUtils.applyElectronicSign( personUpdate, userTO );
    personUpdate.setDsName( userTO.getPersonTO().getName() );
    personUpdate.setDsLastname( userTO.getPersonTO().getDsLastname() );
    personUpdate.setDsMotherLastname( userTO.getPersonTO().getDsMotherLastname() );
    this.personDAO.edit( personUpdate );

    if( CollectionUtils.isNotEmpty( userTO.getPersonTO().getEmails() ) )
    {
      EmailDO emailUpdate = emailDAO.find( userTO.getPersonTO().getEmails().get( 0 ).getId().intValue() );
      if( emailUpdate != null )
      {
        emailUpdate.setDsEmail( userTO.getPersonTO().getEmails().get( 0 ).getName() );
        emailDAO.edit( emailUpdate );
      }
    }

    updateRoles( userTO, userUpdate );
    updateRegion( userTO, userUpdate );
    updateTheater( userTO, userUpdate );
    super.edit( userUpdate );
  }

  private void updateRoles( UserTO userTO, UserDO userUpdate )
  {
    List<RoleDO> rolesToRemove = new ArrayList<RoleDO>();
    List<RoleDO> rolesToCreate = new ArrayList<RoleDO>();
    for( int i = 0; i < userUpdate.getRoleDOList().size(); i++ )
    {
      RoleDO categoryDO = userUpdate.getRoleDOList().get( i );
      if( !userTO.getRoles().contains( new CatalogTO( categoryDO.getIdRole().longValue() ) ) )
      {
        rolesToRemove.add( categoryDO );
      }
    }

    for( RoleDO roles : rolesToRemove )
    {
      roles.getUserDOList().remove( userUpdate );
      userUpdate.getRoleDOList().remove( roles );
    }

    for( int i = 0; i < userTO.getRoles().size(); i++ )
    {
      CatalogTO c = userTO.getRoles().get( i );
      if( !userUpdate.getRoleDOList().contains( new RoleDO( c.getId().intValue() ) ) )
      {
        rolesToCreate.add( new RoleDO( c.getId().intValue() ) );
      }
    }

    for( RoleDO roles : rolesToCreate )
    {
      RoleDO role = roleDAO.find( roles.getIdRole() );
      userUpdate.getRoleDOList().add( role );
      role.getUserDOList().add( userUpdate );
    }
  }

  /**
   * Se actualizan las regiones en los datos del usuario a actualizar
   * 
   * @param userTO
   * @param userUpdate
   */
  private void updateRegion( UserTO userTO, UserDO userUpdate )
  {
    List<RegionDO> regionsToRemove = new ArrayList<RegionDO>();
    List<RegionDO> regionsToCreate = new ArrayList<RegionDO>();
    for( RegionDO currentRegion : userUpdate.getRegionDOList() )
    {
      if( !userTO.getRegions().contains( new CatalogTO( currentRegion.getIdRegion().longValue() ) ) )
      {
        regionsToRemove.add( currentRegion );
      }
    }

    for( RegionDO regionToRemove : regionsToRemove )
    {
      regionToRemove.getUserDOList().remove( userUpdate );
      userUpdate.getRegionDOList().remove( regionToRemove );
    }

    for( CatalogTO c : userTO.getRegions() )
    {
      if( !userUpdate.getRegionDOList().contains( new RegionDO( c.getId().intValue() ) ) )
      {
        regionsToCreate.add( new RegionDO( c.getId().intValue() ) );
      }
    }

    for( RegionDO regionToCreate : regionsToCreate )
    {
      RegionDO newRegion = regionDAO.find( regionToCreate.getIdRegion() );
      userUpdate.getRegionDOList().add( newRegion );
      newRegion.getUserDOList().add( userUpdate );
    }
  }

  /**
   * Se actualiza la información de los cines en los datos del usuario a actualizar.
   * 
   * @param userTO
   * @param userUpdate
   */
  private void updateTheater( UserTO userTO, UserDO userUpdate )
  {
    List<TheaterDO> theatersToRemove = new ArrayList<TheaterDO>();
    List<TheaterDO> theatersToCreate = new ArrayList<TheaterDO>();
    for( TheaterDO currentTheater : userUpdate.getTheaterDOList() )
    {
      if( userTO.getTheaters() != null
          && !userTO.getTheaters().contains( new CatalogTO( currentTheater.getIdTheater().longValue() ) ) )
      {
        boolean isSameRegion = currentTheater.getIdRegion().getIdRegion().intValue() == userTO.getRegionSelected().intValue();
        if( isSameRegion )
        {
          theatersToRemove.add( currentTheater );
        }
      }
    }

    for( TheaterDO theaterToRemove : theatersToRemove )
    {
      theaterToRemove.getUserDOList().remove( userUpdate );
      userUpdate.getTheaterDOList().remove( theaterToRemove );
    }

    if( CollectionUtils.isNotEmpty( userTO.getTheaters() ) )
    {
      for( CatalogTO c : userTO.getTheaters() )
      {
        if( !userUpdate.getTheaterDOList().contains( new TheaterDO( c.getId().intValue() ) ) )
        {
          theatersToCreate.add( new TheaterDO( c.getId().intValue() ) );
        }
      }
    }

    for( TheaterDO theaterToCreate : theatersToCreate )
    {
      TheaterDO newTheater = theaterDAO.find( theaterToCreate.getIdTheater().intValue() );
      userUpdate.getTheaterDOList().add( newTheater );
      newTheater.getUserDOList().add( userUpdate );
    }
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.UserDAO#delete(mx.com
   * .cinepolis.digital.booking.model.to.UserTO)
   */
  @Override
  public void delete( UserTO userTO )
  {
    UserDO userDO = new UserDO( userTO.getId().intValue() );
    AbstractEntityUtils.applyElectronicSign( userDO, userTO );
    this.remove( userDO );

  }

  /**
   * Removes a register
   * 
   * @param userDO
   */
  public void remove( UserDO userDO )
  {
    UserDO remove = super.find( userDO.getIdUser() );
    if( remove == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_PERSISTENCE_ERROR_USER_NOT_FOUND );
    }
    AbstractEntityUtils.copyElectronicSign( remove, userDO );
    remove.setFgActive( Boolean.FALSE );
    PersonDO personTodelete = remove.getIdPerson();
    AbstractEntityUtils.copyElectronicSign( personTodelete, userDO );
    personTodelete.setFgActive( Boolean.FALSE );
    personDAO.edit( personTodelete );
    super.edit( remove );
  }

  /**
   * Clase estatica de filtros de ordenamiento
   * 
   * @author agustin.ramirez
   */
  static class UserSortFilter
  {
    private List<ModelQuery> sortFields;
    private SortOrder sortOrder;
    private CriteriaBuilder criteriaBuilder;
    private CriteriaQuery<UserDO> criteriaQuery;
    private Root<UserDO> userDO;
    private Map<ModelQuery, Object> filters;

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
     * @return the criteriaQuery
     */
    public CriteriaQuery<UserDO> getCriteriaQuery()
    {
      return criteriaQuery;
    }

    /**
     * @param criteriaQuery the criteriaQuery to set
     */
    public void setCriteriaQuery( CriteriaQuery<UserDO> criteriaQuery )
    {
      this.criteriaQuery = criteriaQuery;
    }

    /**
     * @return the userDO
     */
    public Root<UserDO> getUserDO()
    {
      return userDO;
    }

    /**
     * @param userDO the userDO to set
     */
    public void setUserDO( Root<UserDO> userDO )
    {
      this.userDO = userDO;
    }

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

  }

}
