package mx.com.cinepolis.digital.booking.persistence.dao.impl;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeDetailTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.BookingIncomeDOTOIncomeTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.ExceptionHandlerDAOInterceptor;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingIncomeDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.IncomeViewDO;
import mx.com.cinepolis.digital.booking.model.IncomeViewPK;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.base.dao.AbstractBaseDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingIncomeDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the interface {@link mx.com.cinepolis.digital.booking.persistence.dao.BookingIncomeDO}
 * 
 * @author jcarbajal
 */
@Stateless
@Interceptors({ ExceptionHandlerDAOInterceptor.class })
public class BookingIncomeDAOImpl extends AbstractBaseDAO<BookingIncomeDO> implements BookingIncomeDAO
{
  private static final Logger LOG = LoggerFactory.getLogger( BookingIncomeDAOImpl.class );

  @EJB
  private TheaterDAO theaterDAO;

  @EJB
  private BookingDAO bookingDAO;

  @EJB
  private WeekDAO weekDAO;

  @EJB
  private EventDAO eventDAO;

  @PersistenceContext(unitName = "DigitalBookingPU")
  private EntityManager em;

  /**
   * Constructor default
   */
  public BookingIncomeDAOImpl()
  {
    super( BookingIncomeDO.class );
  }

  @Override
  protected EntityManager getEntityManager()
  {
    return em;
  }

  /**
   * Remove logical of the entity
   */
  @Override
  public void remove( BookingIncomeDO entity )
  {
    BookingIncomeDO remove = this.find( entity.getIdBookingIncome() );
    if( remove != null )
    {
      remove.setFgActive( false );
      AbstractEntityUtils.copyElectronicSign( entity, remove );
      this.edit( remove );
    }
  }

  
  
 

  /**
   * {@inheritDoc}
   */
  @Override
  public void synchronizeIncomes( TheaterTO theater )
  {
    TheaterDO theaterDO = this.theaterDAO.find( theater.getId().intValue() );
    if( theaterDO == null )
    {
      // En caso de no encontrars el cine se lanza una excepción
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INEXISTENT_THEATER );
    }
    // Busca los ingresos por Id de Vista del cine
    List<IncomeViewDO> incomes = findIncomesByIdVista( theaterDO.getIdVista() );

    // Se utiliza para llevar los eventos consultados
    Map<Integer, EventDO> events = new HashMap<Integer, EventDO>();
    // Se utiliza para llevar las fechas consultadas vs la C_WEEK
    Map<Date, WeekDO> weeks = new HashMap<Date, WeekDO>();
    // Si es una fecha válida lo deja registrar
    Set<Date> validDates = new HashSet<Date>();

    DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );

    for( IncomeViewDO income : incomes )
    {
      EventDO event = getEventByIdVista( events, income );
      if( event == null )
      {
        // En caso de no encontrarse el evento no se inserta por los constraints
        continue;
      }

      Date date = null;
      try
      {
        date = df.parse( income.getId().getDate() );
      }
      catch( ParseException e )
      {
        // En caso de que la fecha no tenga un formato válido no se ingresa
        LOG.warn( e.getMessage(), e );
        continue;
      }
      WeekDO week = getWeek( weeks, date );

      if( week == null )
      {
        // En caso de no existir la semana no se ingresa el registro
        continue;
      }

      boolean isValidDate = isValidDate( theater, validDates, date, week );

      if( isValidDate )
      {
        // Busca la programación, puede ser nula
        BookingDO booking = bookingDAO.findByEventIdAndTheaterId( theaterDO.getIdTheater().longValue(),
          event.getIdEvent() );

        synchronizeIncome( income, theater, theaterDO, event, week, booking, date );
      }
    }

  }

  private boolean isValidDate( TheaterTO theater, Set<Date> validDates, Date date, WeekDO week )
  {
    boolean isValidDate = false;
    if( !validDates.contains( date ) )
    {
      IncomeTO search = new IncomeTO();
      search.setDate( date );
      search.setTheater( theater );
      search.setWeek( new WeekTO( week.getIdWeek() ) );
      int n = this.countByIdTheaterIdWeekDate( search );
      if( n == 0 )
      {
        validDates.add( date );
        isValidDate = true;
      }
    }
    else
    {
      isValidDate = true;
    }
    return isValidDate;
  }

  private void synchronizeIncome( IncomeViewDO income, TheaterTO theater, TheaterDO idTheater, EventDO idEvent,
      WeekDO idWeek, BookingDO idBooking, Date dtShow )
  {
    BookingIncomeDO entity = new BookingIncomeDO();
    AbstractEntityUtils.applyElectronicSign( entity, theater );
    entity.setIdBooking( idBooking );
    entity.setIdEvent( idEvent );
    entity.setIdTheater( idTheater );
    ScreenDO idScreen = null;
    for( ScreenDO screenDO : idTheater.getScreenDOList() )
    {
      if( income.getId().getScreen() == screenDO.getNuScreen() )
      {
        idScreen = screenDO;
        break;
      }
    }
    entity.setIdScreen( idScreen );
    entity.setIdWeek( idWeek );
    entity.setDtShow( dtShow );
    entity.setHrShow( income.getId().getShow() );
    entity.setQtIncome( income.getIncome() );
    entity.setQtTickets( income.getTickets() );

    if( idScreen != null )
    {
      // Sólo se ingresa si existe una sala que esté asociada al cine
      this.create( entity );
    }

  }

  private WeekDO getWeek( Map<Date, WeekDO> weeks, Date date )
  {
    WeekDO week = null;
    if( weeks.containsKey( date ) )
    {
      week = weeks.get( date );
    }
    else
    {
      week = (WeekDO) CollectionUtils.find( this.weekDAO.findWeeksByStartDayAndFinalDay( date, date ),
        PredicateUtils.notNullPredicate() );
      weeks.put( date, week );
    }
    return week;
  }

  private EventDO getEventByIdVista( Map<Integer, EventDO> events, IncomeViewDO income )
  {
    EventDO event = null;
    if( events.containsKey( income.getId().getMovieId() ) )
    {
      event = events.get( income.getId().getMovieId() );
    }
    else
    {
      event = (EventDO) CollectionUtils.find(
        this.eventDAO.findByIdVistaAndActive( String.valueOf( income.getId().getMovieId() ) ),
        PredicateUtils.notNullPredicate() );
    }
    return event;
  }

  private List<IncomeViewDO> findIncomesByIdVista( String idVista )
  {
    List<IncomeViewDO> incomes = new ArrayList<IncomeViewDO>();

    Properties properties = loadProperties();
    registerDriver();

    Connection connection = getConnection( properties );
    PreparedStatement ps = null;
    ResultSet rs = null;

    try
    {
      String query = properties.getProperty( "database.income.query" );
      ps = connection.prepareStatement( query );
      ps.setInt( 1, NumberUtils.toInt( idVista ) );
      rs = ps.executeQuery();
      while( rs.next() )
      {
        IncomeViewDO income = new IncomeViewDO();
        IncomeViewPK id = new IncomeViewPK();
        id.setTheaterId( rs.getInt( "idVistaComplejo" ) );
        id.setScreen( rs.getInt( "numeroSalaComplejo" ) );
        id.setMovieId( rs.getInt( "idVistaPelicula" ) );
        id.setDate( rs.getString( "fechaFuncion" ) );
        id.setShow( rs.getString( "horaFuncion" ) );
        income.setId( id );
        income.setIncome( rs.getBigDecimal( "ingresos" ).doubleValue() );
        income.setTickets( rs.getInt( "numeroBoletos" ) );
        income.setMovieName( rs.getString( "nombrePelicula" ) );
        income.setTheaterName( rs.getString( "nombreComplejo" ) );
        incomes.add( income );
      }
    }
    catch( SQLException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INCOMES_DRIVER_ERROR, e );
    }
    finally
    {
      closePreparedStatement( ps );
      closeResultSet( rs );
      closeConnection( connection );
    }
    return incomes;
  }

  private void closeResultSet( ResultSet rs )
  {
    ResultSet resultset = rs;
    try
    {
      CinepolisUtils.safeResultSetClose( resultset );
    }
    catch( SQLException e )
    {
      resultset = null;
      LOG.error( e.getMessage() );
    }

  }

  private void closePreparedStatement( PreparedStatement ps )
  {
    PreparedStatement preparedStatement = ps;
    try
    {
      CinepolisUtils.safePreparedStatementClose( preparedStatement );
    }
    catch( SQLException e )
    {
      preparedStatement = null;
      LOG.error( e.getMessage() );
    }
  }

  private void closeConnection( Connection connection )
  {
    Connection conn = connection;
    try
    {
      CinepolisUtils.safeConnectionClose( conn );
    }
    catch( SQLException e )
    {
      conn = null;
      LOG.error( e.getMessage() );
    }
  }

  private void registerDriver()
  {
    Driver driverSybase;
    try
    {
      driverSybase = (Driver) Class.forName( "com.sybase.jdbc3.jdbc.SybDriver" ).newInstance();
      DriverManager.registerDriver( driverSybase );
    }
    catch( InstantiationException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INCOMES_DRIVER_ERROR, e );
    }
    catch( IllegalAccessException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INCOMES_DRIVER_ERROR, e );
    }
    catch( ClassNotFoundException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INCOMES_DRIVER_ERROR, e );
    }
    catch( SQLException e )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INCOMES_DRIVER_ERROR, e );
    }
  }

  private Properties loadProperties()
  {
    Properties properties = new Properties();
    try
    {
      properties.load( Thread.currentThread().getContextClassLoader().getResourceAsStream( "application.properties" ) );
    }
    catch( IOException e )
    {
      throw DigitalBookingExceptionBuilder.build(
        DigitalBookingExceptionCode.INCOMES_COULD_NOT_OBTAIN_DATABASE_PROPERTIES, e );
    }
    return properties;
  }

  private Connection getConnection( Properties properties )
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "jdbc:sybase:Tds:" );
    sb.append( properties.getProperty( "database.income.host" ) );
    sb.append( ":" ).append( properties.getProperty( "database.income.port" ) );
    sb.append( "//" ).append( properties.getProperty( "database.income.database" ) );

    Connection connection = null;
    Properties conectionPropertiesSyb = new Properties();
    conectionPropertiesSyb.put( "user", properties.getProperty( "database.income.user" ) );
    conectionPropertiesSyb.put( "password", properties.getProperty( "database.income.password" ) );
    try
    {
      connection = DriverManager.getConnection( sb.toString(), conectionPropertiesSyb );
    }
    catch( SQLException e )
    {
      LOG.error( sb.toString() );
      try
      {
        CinepolisUtils.safeConnectionClose( connection );
      }
      catch( SQLException e1 )
      {
        connection = null;
        LOG.error( e1.getMessage(), e1 );
      }
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INCOMES_CONNECTION_ERROR, e );
    }
    return connection;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<IncomeTO> findAllByIdTheaterIdWeek( IncomeTO income )
  {
    Query q = this.em.createNamedQuery( "BookingIncomeDO.findAllByIdTheaterIdWeek" );
    q.setParameter( "idTheater", income.getTheater().getId() );
    q.setParameter( "idWeek", income.getWeek().getIdWeek() );

    List<BookingIncomeDO> result = q.getResultList();
    List<IncomeTO> incomes = new ArrayList<IncomeTO>();
    incomes.addAll( CollectionUtils.collect( result, new BookingIncomeDOTOIncomeTOTransformer() ) );
    return incomes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int countByIdTheaterIdWeekDate( IncomeTO income )
  {
    Query q = this.em.createNamedQuery( "BookingIncomeDO.findAllByIdTheaterIdWeekDtShow" );
    q.setParameter( "idTheater", income.getTheater().getId() );
    q.setParameter( "idWeek", income.getWeek().getIdWeek() );
    q.setParameter( "dtShow", DateUtils.truncate( income.getDate(), Calendar.DATE ) );

    return ((Number) q.getSingleResult()).intValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public IncomeDetailTO findIncomeDetail( IncomeDetailTO request )
  {
    Query q = this.em.createNamedQuery( "BookingIncomeDO.findAllByDetail" );
    q.setParameter( "idTheater", request.getTheaterId() );
    q.setParameter( "idWeek", request.getWeekId() );
    q.setParameter( "idScreen", request.getScreenId() );
    q.setParameter( "idEvent", request.getEventId() );

    IncomeDetailTO detail = new IncomeDetailTO();
    detail.setEventId( request.getEventId() );
    detail.setTheaterId( request.getTheaterId() );
    detail.setScreenId( request.getScreenId() );
    detail.setWeekId( request.getWeekId() );
    List<BookingIncomeDO> incomes = q.getResultList();

    for( BookingIncomeDO income : incomes )
    {
      if( detail.getBookingId() == null && income.getIdBooking() != null )
      {
        detail.setBookingId( income.getIdBooking().getIdBooking() );
      }
      Calendar cal = DateUtils.toCalendar( income.getDtShow() );
      if( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.FRIDAY )
      {
        detail.setFridayIncome( detail.getFridayIncome() + income.getQtIncome() );
        detail.setFridayTickets( detail.getFridayTickets() + income.getQtTickets().longValue() );
      }
      else if( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY )
      {
        detail.setSaturdayIncome( detail.getSaturdayIncome() + income.getQtIncome() );
        detail.setSaturdayTickets( detail.getSaturdayTickets() + income.getQtTickets().longValue() );
      }
      else if( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY )
      {
        detail.setSundayIncome( detail.getSundayIncome() + income.getQtIncome() );
        detail.setSundayTickets( detail.getSundayTickets() + income.getQtTickets().longValue() );
      }

    }
    detail.setTotalIncome( detail.getFridayIncome() + detail.getSaturdayIncome() + detail.getSundayIncome() );
    detail.setTotalTickets( detail.getFridayTickets() + detail.getSaturdayTickets() + detail.getSundayTickets() );

    return detail;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public List<IncomeTO> findTopEvents( WeekTO week )
  {

    List<IncomeTO> topEvents = new ArrayList<IncomeTO>();
    Query q = this.em.createNamedQuery( "BookingIncomeDO.nativeQuery.findTopEvents" );
    q.setParameter( 1, week.getIdWeek() );

    List<Object[]> records = q.getResultList();
    for( Object[] record : records )
    {
      IncomeTO income = new IncomeTO();
      income.setEvent( new EventTO() );
      income.getEvent().setIdEvent( ((Number) record[0]).longValue() );
      income.getEvent().setDsEventName( (String) record[1] );
      income.setIncome( record[2] != null ? ((Number) record[2]).doubleValue() : 0.0 );
      income.setTickets( record[3] != null ? ((Number) record[3]).intValue() : 0 );
      topEvents.add( income );
    }

    return topEvents;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getWeekendIncomeByMovie( IncomeTreeTO movie, WeekTO week )
  {
    double weekendIncomeByMovie = 0.0;
    List<BookingIncomeDO> incomes = this.findIncomesByEventTheaterWeek( movie.getBooking().getEvent().getIdEvent(),
      movie.getBooking().getTheater().getId(), week.getIdWeek() );

    for( BookingIncomeDO income : incomes )
    {
      Calendar cal = DateUtils.toCalendar( income.getDtShow() );
      if( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.FRIDAY || cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY
          || cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY )
      {
        weekendIncomeByMovie += income.getQtIncome() != null ? income.getQtIncome() : 0.0;
      }
    }
    return weekendIncomeByMovie;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public double getWeekIncomeByMovie( IncomeTreeTO movie, WeekTO week )
  {
    double weekIncomeByMovie = 0.0;
    List<BookingIncomeDO> incomes = this.findIncomesByEventTheaterWeek( movie.getBooking().getEvent().getIdEvent(),
      movie.getBooking().getTheater().getId(), week.getIdWeek() );
    for( BookingIncomeDO income : incomes )
    {
      weekIncomeByMovie += income.getQtIncome() != null ? income.getQtIncome() : 0.0;
    }
    return weekIncomeByMovie;
  }

  @SuppressWarnings("unchecked")
  private List<BookingIncomeDO> findIncomesByEventTheaterWeek( Long idEvent, Long idTheater, Integer idWeek )
  {
    Query q = this.em.createNamedQuery( "BookingIncomeDO.findIncomesByEventTheaterWeek" );
    q.setParameter( "idEvent", idEvent );
    q.setParameter( "idTheater", idTheater );
    q.setParameter( "idWeek", idWeek );
    return q.getResultList();
  }

  @Override
  public double getWeekendIncomeByScreen( IncomeTreeTO screen, WeekTO week )
  {
    double weekendIncomeByScreen = 0.0;
    List<BookingIncomeDO> incomes = this.findIncomesByScreenTheaterWeek( screen.getBooking().getScreen().getId(),
      screen.getBooking().getTheater().getId(), week.getIdWeek() );

    for( BookingIncomeDO income : incomes )
    {
      Calendar cal = DateUtils.toCalendar( income.getDtShow() );
      if( cal.get( Calendar.DAY_OF_WEEK ) == Calendar.FRIDAY || cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SATURDAY
          || cal.get( Calendar.DAY_OF_WEEK ) == Calendar.SUNDAY )
      {
        weekendIncomeByScreen += income.getQtIncome() != null ? income.getQtIncome() : 0.0;
      }
    }
    return weekendIncomeByScreen;
  }

  @Override
  public double getWeekIncomeByScreen( IncomeTreeTO screen, WeekTO week )
  {
    double weekendIncomeByScreen = 0.0;
    List<BookingIncomeDO> incomes = this.findIncomesByScreenTheaterWeek( screen.getBooking().getScreen().getId(),
      screen.getBooking().getTheater().getId(), week.getIdWeek() );

    for( BookingIncomeDO income : incomes )
    {
      weekendIncomeByScreen += income.getQtIncome() != null ? income.getQtIncome() : 0.0;
    }
    return weekendIncomeByScreen;
  }

  @SuppressWarnings("unchecked")
  private List<BookingIncomeDO> findIncomesByScreenTheaterWeek( Long idScreen, Long idTheater, Integer idWeek )
  {
    Query q = this.em.createNamedQuery( "BookingIncomeDO.findIncomesByScreenTheaterWeek" );
    q.setParameter( "idScreen", idScreen );
    q.setParameter( "idTheater", idTheater );
    q.setParameter( "idWeek", idWeek );
    return q.getResultList();
  }

  /* (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.BookingIncomeDAO#getTotalWeekendAdmissionByTheaterAndWeek(java.lang.Long, java.lang.Long)
   */
  @Override
  public double getTotalWeekendAdmissionByTheaterAndWeek( Long idTheater, Long idWeek )
  {
    double  totalAdmission=0.0;
    Query q = this.em.createNamedQuery( "BookingIncomeDO.nativeQuery.findTotalAdmissionWeek" );
    q.setParameter(1, idTheater );
    q.setParameter(2, idWeek );
    if(q.getSingleResult()!=null)
    {
    totalAdmission=((Integer)q.getSingleResult()).doubleValue();
    }
    
    return totalAdmission; 
  }
    
  /* (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.persistence.dao.BookingIncomeDAO#getTotalWeekAdmissionByTheaterAndWeek(java.lang.Long, java.lang.Long)
   */
  @Override
  public double getTotalWeekAdmissionByTheaterAndWeek( Long idTheater, Long idWeek )
  {
    double  totalAdmission=0.0;
    Query q = this.em.createNamedQuery( "BookingIncomeDO.nativeQuery.findTotalAdmissionWeekend" );
    q.setParameter(1, idTheater );
    q.setParameter(2, idWeek );
    if(q.getSingleResult()!=null)
    {
      totalAdmission=((Integer)q.getSingleResult()).doubleValue();
    }
    
    return totalAdmission;
  }

  
}