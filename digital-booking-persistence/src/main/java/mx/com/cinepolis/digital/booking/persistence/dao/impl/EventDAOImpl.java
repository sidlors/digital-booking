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
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.constants.EventType;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.EventQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SortOrder;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.CriteriaQueryBuilder;
import mx.com.cinepolis.digital.booking.dao.util.EventDOToEventTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekDO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.DistributorDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.EventMovieDO;
import mx.com.cinepolis.digital.booking.model.EventTypeDO;
import mx.com.cinepolis.digital.booking.model.MovieImageDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.CategoryDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.DistributorDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.MovieImageDAO;

import org.apache.commons.collections.CollectionUtils;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.EventDAO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
@SuppressWarnings("unchecked")
public class EventDAOImpl extends AbstractBaseDAO<EventDO> implements EventDAO
{
  private static final String QUERY_FIND_ACTIVE_MOVIES = "EventDO.findActiveMovies";
  private static final String QUERY_FIND_ACTIVE_MOVIES_PREMIERE = "EventDO.findActiveMoviesPremiere";
  private static final String QUERY_FIND_ACTIVE_FESTIVALS = "EventDO.findActiveFestival";
  private static final String QUERY_FIND_ACTIVE_PRERELEASE = "EventDO.findActiveMoviesPrerelease";

  private static final String QUERY_TURN_OFF_PREMIERE = "EventDO.turnOffPremiere";
  private static final String QUERY_FIND_DS_CODE = "EventDO.findByDsCodeDbs";
  private static final String PARAMETER_DS_CODE = "dsCodeDbs";

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  @EJB
  private MovieImageDAO movieImageDAO;

  @EJB
  private DistributorDAO distributorDAO;

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
   * Constructor default
   */
  public EventDAOImpl()
  {
    super( EventDO.class );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( EventDO eventDO )
  {
    EventDO remove = super.find( eventDO.getIdEvent() );
    if( remove != null )
    {
      AbstractEntityUtils.copyElectronicSign( eventDO, remove );
      remove.setFgActive( false );

      super.edit( remove );
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PagingResponseTO<EventTO> findAllByPaging( PagingRequestTO pagingRequestTO )
  {
    List<ModelQuery> sortFields = pagingRequestTO.getSort();
    SortOrder sortOrder = pagingRequestTO.getSortOrder();
    Map<ModelQuery, Object> filters = getFilters( pagingRequestTO );
    Language language = pagingRequestTO.getLanguage();

    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<EventDO> q = cb.createQuery( EventDO.class );
    Root<EventDO> eventDO = q.from( EventDO.class );
    Join<EventDO, EventTypeDO> eventTypeDO = eventDO.join( "idEventType" );
    Join<EventMovieDO, EventDO> eventMovieDO = eventDO.join( "eventMovieDOList", JoinType.LEFT );
    Join<DistributorDO, EventMovieDO> distributorDO = eventMovieDO.join( "idDistributor", JoinType.LEFT );

    q.distinct( true ).select( eventDO );
    applySorting( sortFields, sortOrder, cb, q, eventDO );

    Predicate filterCondition = applyFilters( filters, cb, eventDO, eventTypeDO, distributorDO );

    CriteriaQuery<Long> queryCountRecords = cb.createQuery( Long.class );
    queryCountRecords.select( cb.count( eventDO ) );

    if( filterCondition != null )
    {
      q.where( filterCondition );
      queryCountRecords.where( filterCondition );
    }

    // pagination
    TypedQuery<EventDO> tq = em.createQuery( q );
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

    PagingResponseTO<EventTO> response = new PagingResponseTO<EventTO>();
    response.setElements( (List<EventTO>) CollectionUtils.collect( tq.getResultList(), new EventDOToEventTOTransformer(
        language ) ) );
    response.setTotalCount( count );

    return response;
  }

  /**
   * Método que aplica los filtros para la consulta
   * 
   * @param filters
   * @param cb
   * @param eventDO
   * @param eventTypeDO
   * @param distributorDO
   * @return
   */
  private Predicate applyFilters( Map<ModelQuery, Object> filters, CriteriaBuilder cb, Root<EventDO> eventDO,
      Join<EventDO, EventTypeDO> eventTypeDO, Join<DistributorDO, EventMovieDO> distributorDO )
  {
    Predicate filterCondition = null;
    if( filters != null && !filters.isEmpty() )
    {
      filterCondition = cb.conjunction();

      filterCondition = CriteriaQueryBuilder.<Long> applyFilterRootEqual( filters, EventQuery.EVENT_ID, eventDO, cb,
        filterCondition );

      filterCondition = CriteriaQueryBuilder.applyFilterRootLike( filters, EventQuery.EVENT_NAME, eventDO, cb,
        filterCondition );

      filterCondition = CriteriaQueryBuilder.<Boolean> applyFilterRootEqual( filters, EventQuery.EVENT_ACTIVE, eventDO,
        cb, filterCondition );

      if( filters.containsKey( EventQuery.EVENT_PREMIERE ) )
      {
        Path<Boolean> pathFilter = eventDO.get( EventQuery.EVENT_PREMIERE.getQuery() );
        filterCondition = cb.and( filterCondition, cb.equal( pathFilter, filters.get( EventQuery.EVENT_PREMIERE ) ) );
      }
      if( filters.containsKey( EventQuery.EVENT_PRERELEASE ) )
      {
        Path<Boolean> pathFilter = eventDO.get( EventQuery.EVENT_PRERELEASE.getQuery() );
        filterCondition = cb.and( filterCondition, cb.equal( pathFilter, filters.get( EventQuery.EVENT_PRERELEASE ) ) );
      }
      if( filters.containsKey( EventQuery.EVENT_FESTIVAL ) )
      {
        Path<Boolean> pathFilter = eventDO.get( EventQuery.EVENT_FESTIVAL.getQuery() );
        filterCondition = cb.and( filterCondition, cb.equal( pathFilter, filters.get( EventQuery.EVENT_FESTIVAL ) ) );
      }
      if( filters.containsKey( EventQuery.EVENT_CURRENT_MOVIE ) )
      {
        Path<Boolean> pathFilter = eventDO.get( EventQuery.EVENT_CURRENT_MOVIE.getQuery() );
        filterCondition = cb
            .and( filterCondition, cb.equal( pathFilter, filters.get( EventQuery.EVENT_CURRENT_MOVIE ) ) );
      }
      if( filters.containsKey( EventQuery.EVENT_MOVIE_DISTRIBUTOR_ID ) )
      {
        Path<Integer> pathFilter = distributorDO.get( EventQuery.EVENT_MOVIE_DISTRIBUTOR_ID.getQuery() );
        filterCondition = cb.and( filterCondition,
          cb.equal( pathFilter, filters.get( EventQuery.EVENT_MOVIE_DISTRIBUTOR_ID ) ) );
      }
      if( filters.containsKey( EventQuery.EVENT_CODE_DBS ) )
      {
        Path<String> pathFilter = eventDO.get( EventQuery.EVENT_CODE_DBS.getQuery() );
        filterCondition = cb.and( filterCondition,
          cb.like( pathFilter, "%" + filters.get( EventQuery.EVENT_CODE_DBS ) + "%" ) );
      }
      if (filters.containsKey( EventQuery.EVENT_ALTERNATE_CONTENT ))
      {
        Path<Boolean> pathFilter = eventDO.get( EventQuery.EVENT_ALTERNATE_CONTENT.getQuery() );
        filterCondition = cb
            .and( filterCondition, cb.equal( pathFilter, filters.get( EventQuery.EVENT_ALTERNATE_CONTENT ) ) );
      }
      filterCondition = CriteriaQueryBuilder.<Integer> applyFilterJoinEqual( filters, EventQuery.EVENT_TYPE_ID,
        eventTypeDO, cb, filterCondition );

    }
    return filterCondition;
  }

  /**
   * Método que aplica el ordenamiento de los resultados
   * 
   * @param sortFields
   * @param sortOrder
   * @param cb
   * @param q
   * @param eventDO
   */
  private void applySorting( List<ModelQuery> sortFields, SortOrder sortOrder, CriteriaBuilder cb,
      CriteriaQuery<EventDO> q, Root<EventDO> eventDO )
  {
    if( sortOrder != null && CollectionUtils.isNotEmpty( sortFields ) )
    {
      List<Order> order = new ArrayList<Order>();

      for( ModelQuery sortField : sortFields )
      {
        if( sortField instanceof EventQuery )
        {
          Path<?> path = eventDO.get( sortField.getQuery() );

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
   * Método que obtene los filtros para la consulta
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
    filters.put( EventQuery.EVENT_LANGUAGE_ID, pagingRequestTO.getLanguage().getId() );
    return filters;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void save( EventTO eventTO, Long idDistAltContent )
  {
    EventTO savedEvent = saveEventDO( eventTO, idDistAltContent );
    if( savedEvent instanceof EventMovieTO )
    {
      saveEventMovieDO( (EventMovieTO) savedEvent );
    }
    this.flush();
  }

  /**
   * Método que guarda un evento
   * 
   * @param eventTO
   * @return
   */
  private EventTO saveEventDO( EventTO eventTO, Long idDistAltContent )
  {
    EventDO eventDO = new EventDO();
    AbstractEntityUtils.applyElectronicSign( eventDO, eventTO );
    eventDO.setDsName( eventTO.getDsEventName() );
    eventDO.setIdVista( eventTO.getIdVista() );
    eventDO.setQtDuration( eventTO.getDuration().intValue() );
    eventDO.setDsCodeDbs( eventTO.getCodeDBS() );
    eventDO.setQtCopy( eventTO.getQtCopy() );
    eventDO.setCategoryDOList( new ArrayList<CategoryDO>() );
    eventDO.setFgPremiere( eventTO.isPremiere() );
    eventDO.setCurrentMovie( eventTO.isCurrentMovie() );
    eventDO.setFgPrerelease( eventTO.isPrerelease() );
    eventDO.setFgFestival( eventTO.isFestival() );
    eventDO.setFgActiveIa( eventTO.isFgActiveIa() );
    eventDO.setFgAlternateContent( eventTO.isFgAlternateContent() );

    // Agrega tipo de evento según la distribuidora
    if( eventTO instanceof EventMovieTO )
    {
      eventDO.setIdEventType( addEventType( eventTO, idDistAltContent ) );
    }

    for( CatalogTO catalogTO : eventTO.getSoundFormats() )
    {
      CategoryDO categorySound = this.getCategory( catalogTO.getId().intValue() );
      categorySound.getEventDOList().add( eventDO );
      eventDO.getCategoryDOList().add( categorySound );
    }
    for( CatalogTO catalogTO : eventTO.getMovieFormats() )
    {
      CategoryDO categoryMovieFormat = this.getCategory( catalogTO.getId().intValue() );
      categoryMovieFormat.getEventDOList().add( eventDO );
      eventDO.getCategoryDOList().add( categoryMovieFormat );
    }

    this.create( eventDO );
    this.flush();
    eventTO.setIdEvent( eventDO.getIdEvent().longValue() );
    return eventTO;
  }

  /**
   * Métódo que asigna el tipo de evento según la distribuidora asociada.
   * 
   * @param eventTO
   * @return
   */
  private EventTypeDO addEventType( EventTO eventTO, Long idDistAltContent )
  {
    EventMovieTO entMovieTO = (EventMovieTO) eventTO;
    EventTypeDO eventTypeDO = null;

    if( entMovieTO.getDistributor().getId().equals( idDistAltContent ) )
    {
      eventTypeDO = new EventTypeDO( EventType.SPECIAL_EVENT.getId() );
    }
    else
    {
      eventTypeDO = new EventTypeDO( EventType.MOVIE.getId() );
    }
    return eventTypeDO;
  }

  /**
   * Método que guarda un eventMovie.
   * 
   * @param eventMovieTO
   */
  private void saveEventMovieDO( EventMovieTO eventMovieTO )
  {
    EventDO eventDO = this.find( eventMovieTO.getIdEvent().longValue() );
    EventMovieDO eventMovieDO = new EventMovieDO();
    eventMovieDO.setIdEvent( eventDO );
    eventMovieDO.setIdDistributor( new DistributorDO( eventMovieTO.getDistributor().getId().intValue() ) );
    eventMovieDO.setDtRelease( eventMovieTO.getDtRelease() );
    eventMovieDO.setDsDirector( eventMovieTO.getDsDirector() );
    eventMovieDO.setDsSynopsis( eventMovieTO.getDsSynopsis() );
    eventMovieDO.setDsRating( eventMovieTO.getDsRating() );
    eventMovieDO.setDsCountry( eventMovieTO.getDsCountry() );

    eventMovieDO.setDsActor( eventMovieTO.getDsActor() );
    eventMovieDO.setDsGenre( eventMovieTO.getDsGenre() );
    eventMovieDO.setDsUrl( eventMovieTO.getDsUrl() );
    eventMovieDO.setIdMovieImage( eventMovieTO.getIdMovieImage() );
    eventMovieDO.setDsOriginalName( eventMovieTO.getDsOriginalName() );

    eventDO.setEventMovieDOList( Arrays.asList( eventMovieDO ) );

    this.edit( eventDO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void update( EventTO eventTO, Long idDistAltContent )
  {
    EventDO eventDO = super.find( eventTO.getIdEvent().longValue() );
    if( eventDO != null )
    {
      AbstractEntityUtils.applyElectronicSign( eventDO, eventTO );
      eventDO.setDsName( eventTO.getDsEventName() );
      eventDO.setQtDuration( eventTO.getDuration().intValue() );
      eventDO.setIdVista( eventTO.getIdVista() );
      eventDO.setQtCopy( eventTO.getQtCopy() );
      eventDO.setDsCodeDbs( eventTO.getCodeDBS() );
      eventDO.setFgPremiere( eventTO.isPremiere() );
      eventDO.setCurrentMovie( eventTO.isCurrentMovie() );
      eventDO.setFgPrerelease( eventTO.isPrerelease() );
      eventDO.setIdEventType( addEventType( eventTO, idDistAltContent ) );
      eventDO.setFgFestival( eventTO.isFestival() );
      eventDO.setFgActiveIa( eventTO.isFgActiveIa() );
      eventDO.setFgAlternateContent( eventTO.isFgAlternateContent() );

      if( eventTO instanceof EventMovieTO )
      {
        for( EventMovieDO eventMovieDO : eventDO.getEventMovieDOList() )
        {
          eventMovieDO.setDsCountry( ((EventMovieTO) eventTO).getDsCountry() );
          eventMovieDO.setDsActor( ((EventMovieTO) eventTO).getDsActor() );
          eventMovieDO.setDsDirector( ((EventMovieTO) eventTO).getDsDirector() );
          eventMovieDO.setDsGenre( ((EventMovieTO) eventTO).getDsGenre() );
          eventMovieDO.setDsRating( ((EventMovieTO) eventTO).getDsRating() );
          eventMovieDO.setDsSynopsis( ((EventMovieTO) eventTO).getDsSynopsis() );
          eventMovieDO.setDsUrl( ((EventMovieTO) eventTO).getDsUrl() );
          eventMovieDO.setDtRelease( ((EventMovieTO) eventTO).getDtRelease() );
          eventMovieDO.setIdMovieImage( ((EventMovieTO) eventTO).getIdMovieImage() );
          eventMovieDO.setDsOriginalName( ((EventMovieTO) eventTO).getDsOriginalName() );
          eventMovieDO
              .setIdDistributor( getDistributor( ((EventMovieTO) eventTO).getDistributor().getId().intValue() ) );
          break;
        }
      }

      if( eventTO.getMovieFormats() != null )
      {
        updateCategories( eventTO, eventDO );
      }

      super.edit( eventDO );
      this.flush();
    }

  }

  /**
   * Método que obtiene la distribuidora
   * 
   * @param idDistributor
   * @return
   */
  private DistributorDO getDistributor( int idDistributor )
  {
    DistributorDO distributor = this.distributorDAO.find( idDistributor );
    if( distributor == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_NOT_FOUND );
    }
    return distributor;
  }

  /**
   * Método que actualiza las categorías
   * 
   * @param eventTO
   * @param eventDO
   */
  private void updateCategories( EventTO eventTO, EventDO eventDO )
  {
    List<CatalogTO> categories = new ArrayList<CatalogTO>();

    // Limpieza de categorías
    for( CategoryDO categoryDO : eventDO.getCategoryDOList() )
    {
      categoryDO.getEventDOList().remove( eventDO );
      this.categoryDAO.edit( categoryDO );
    }
    eventDO.setCategoryDOList( new ArrayList<CategoryDO>() );

    categories.addAll( eventTO.getMovieFormats() );
    categories.addAll( eventTO.getSoundFormats() );

    for( CatalogTO to : categories )
    {

      CategoryDO category = this.getCategory( to.getId().intValue() );
      category.getEventDOList().add( eventDO );
      eventDO.getCategoryDOList().add( category );

    }

  }

  /**
   * Método que obtiene una cattegoría de la base de datos.
   * 
   * @param idCategory
   * @return
   */
  private CategoryDO getCategory( int idCategory )
  {
    CategoryDO category = this.categoryDAO.find( idCategory );
    if( category == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_CATALOG_NOT_FOUND );
    }
    return category;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete( EventTO eventTO )
  {
    EventDO entity = this.find( eventTO.getIdEvent() );
    validateExistingBooking( entity );

    AbstractEntityUtils.applyElectronicSign( entity, eventTO );
    this.remove( entity );
  }

  private void validateExistingBooking( EventDO entity )
  {
    if( CollectionUtils.isNotEmpty( entity.getBookingDOList() ) )
    {
      for( BookingDO booking : entity.getBookingDOList() )
      {
        // Si K_BOOKING está activa y tiene al menos un registro asociado a K_BOOKING_WEEK
        if( booking.isFgActive() && CollectionUtils.isNotEmpty( booking.getBookingWeekDOList() ) )
        {
          validateExistingBookingWeek( booking );
        }
      }
    }
  }

  private void validateExistingBookingWeek( BookingDO booking )
  {
    for( BookingWeekDO bw : booking.getBookingWeekDOList() )
    {
      // Si K_BOOKING_WEEK está activa y tiene al menos el estado BOOKED/TERMINATED no puede eliminarse
      if( bw.isFgActive() && !bw.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.CANCELED.getId() ) )
      {
        throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CANNOT_REMOVE_EVENT_MOVIE,
          new Object[] { booking.getIdEvent().getDsName() } );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveMovieImage( EventMovieTO eventMovieTO, FileTO fileTO )
  {
    EventDO eventDO = this.find( eventMovieTO.getIdEvent() );
    MovieImageDO movieImageDO = null;
    Long idMovieImage = null;
    if( eventDO != null && CollectionUtils.isNotEmpty( eventDO.getEventMovieDOList() ) )
    {
      AbstractEntityUtils.applyElectronicSign( eventDO, eventMovieTO );
      for( EventMovieDO eventMovieDO : eventDO.getEventMovieDOList() )
      {
        if( eventMovieDO.getIdMovieImage() == null )
        {
          movieImageDO = new MovieImageDO();
          movieImageDO.setDsFile( fileTO.getFile() );
          movieImageDO.setDsImage( fileTO.getName() );
          this.movieImageDAO.create( movieImageDO );
        }
        else
        {
          movieImageDO = this.movieImageDAO.find( eventMovieDO.getIdMovieImage() );
          movieImageDO.setDsFile( fileTO.getFile() );
          movieImageDO.setDsImage( fileTO.getName() );
          this.movieImageDAO.edit( movieImageDO );
        }
        idMovieImage = movieImageDO.getIdMovieImage();
        eventMovieDO.setIdMovieImage( idMovieImage );
        break;
      }

      this.edit( eventDO );
      this.flush();
      eventMovieTO.setIdMovieImage( idMovieImage );
      fileTO.setId( idMovieImage );
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FileTO getMovieImage( Long idMovieImage )
  {
    FileTO fileTO = null;
    MovieImageDO movieImageDO = this.movieImageDAO.find( idMovieImage );
    if( movieImageDO != null )
    {
      fileTO = new FileTO();
      fileTO.setName( movieImageDO.getDsImage() );
      fileTO.setId( movieImageDO.getIdMovieImage() );
      fileTO.setFile( movieImageDO.getDsFile() );
    }
    return fileTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EventTO getEvent( Long id )
  {
    return getEvent( id, Language.ENGLISH );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EventTO getEvent( Long id, Language language )
  {
    EventDO eventDO = super.find( id );
    return (EventTO) new EventDOToEventTOTransformer( language ).transform( eventDO );

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EventDO> findByIdVistaAndActive( String idVista )
  {
    Query q = em.createNamedQuery( "EventDO.findByIdVistaAndActive" );
    q.setParameter( "idVista", idVista );
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EventTO> findAvailableEventsByScreen( Long id )
  {
    List<EventTO> eventTOs = new ArrayList<EventTO>();
    Query q = em.createNamedQuery( "EventDO.nativeQuery.findAvailableEventsByScreen" );
    q.setParameter( 1, id );

    for( Object[] data : (List<Object[]>) q.getResultList() )
    {
      EventMovieTO eventTO = new EventMovieTO();
      eventTO.setIdEvent( (Long) data[0] );

      boolean fgPremiere = (Boolean) data[5];
      StringBuilder movieName = new StringBuilder();
      if( fgPremiere )
      {
        movieName.append( "(*) " );
      }
      movieName.append( (String) data[1] );

      eventTO.setDistributor( new DistributorTO() );
      eventTO.getDistributor().setId( ((Integer) data[2]).longValue() );
      eventTO.getDistributor().setName( (String) data[3] );
      eventTO.getDistributor().setShortName( (String) data[4] );
      eventTO.setDsEventName( movieName.toString() );
      eventTO.setPremiere( fgPremiere );
      eventTOs.add( eventTO );
    }
    return eventTOs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePremiere()
  {
    Query q = em.createNamedQuery( QUERY_TURN_OFF_PREMIERE );
    q.executeUpdate();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EventTO> findByDsCodeDbs( String dsCodeDbs )
  {
    Query q = em.createNamedQuery( QUERY_FIND_DS_CODE );
    q.setParameter( PARAMETER_DS_CODE, dsCodeDbs );
    List<EventTO> eventTOs = (List<EventTO>) CollectionUtils.collect( q.getResultList(),
      new EventDOToEventTOTransformer() );
    return eventTOs;
  }

  /**
   * Método que obtiene todas las películas activas filtradas por tipo
   */
  @Override
  public List<CatalogTO> findAllActiveMovies( boolean premiere, boolean festival, boolean prerelease )
  {
    Query q = null;
    if( premiere )
    {
      q = em.createNamedQuery( QUERY_FIND_ACTIVE_MOVIES_PREMIERE );
    }
    else
    {
      if( festival )
      {
        q = em.createNamedQuery( QUERY_FIND_ACTIVE_FESTIVALS );
      }
      else
      {
        if( prerelease )
        {
          q = em.createNamedQuery( QUERY_FIND_ACTIVE_PRERELEASE );
        }
        else
        {
          q = em.createNamedQuery( QUERY_FIND_ACTIVE_MOVIES );
        }
      }

    }
    List<Object[]> result = q.getResultList();
    List<CatalogTO> movies = new ArrayList<CatalogTO>();
    for( Object[] data : result )
    {
      boolean fgPremiere = (Boolean) data[2];
      StringBuilder movieName = new StringBuilder();
      if( fgPremiere )
      {
        movieName.append( "(*) " );
      }
      movieName.append( (String) data[1] );

      EventMovieTO movie = new EventMovieTO();
      movie.setId( ((Number) data[0]).longValue() );
      movie.setIdEvent( ((Number) data[0]).longValue() );
      movie.setName( movieName.toString() );
      movie.setDsEventName( movieName.toString() );
      movie.setPremiere( fgPremiere );

      if( data[4] != null )
      {
        movie.setDistributor( new DistributorTO() );
        movie.getDistributor().setId( ((Number) data[3]).longValue() );
        movie.getDistributor().setName( (String) data[4] );
      }

      movies.add( movie );
    }
    return movies;
  }
}
