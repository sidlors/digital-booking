package mx.com.cinepolis.digital.booking.service.book.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.BookingShow;
import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.constants.BookingTicketSemaphore;
import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.constants.Configuration;
import mx.com.cinepolis.digital.booking.commons.constants.EmailType;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.RegionQuery;
import mx.com.cinepolis.digital.booking.commons.query.TheaterQuery;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EmailTemplateTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeDetailTO;
import mx.com.cinepolis.digital.booking.commons.to.MovieBookingWeekTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterBookingWeekTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.AbstractTOUtils;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.commons.utils.ScreenTOComparator;
import mx.com.cinepolis.digital.booking.commons.utils.WeekTOComparator;
import mx.com.cinepolis.digital.booking.dao.util.BookingDAOUtil;
import mx.com.cinepolis.digital.booking.dao.util.BookingDOToBookingTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.EventDOToEventTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.PresaleDOToPresaleTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.PresaleTOToPresaleDOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.ScreenDOToScreenTOTransformer;
import mx.com.cinepolis.digital.booking.dao.util.WeekDOToWeekTOTransformer;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingStatusDO;
import mx.com.cinepolis.digital.booking.model.BookingTypeDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekScreenDO;
import mx.com.cinepolis.digital.booking.model.BookingWeekScreenShowDO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.CategoryLanguageDO;
import mx.com.cinepolis.digital.booking.model.CategoryTypeDO;
import mx.com.cinepolis.digital.booking.model.ConfigurationDO;
import mx.com.cinepolis.digital.booking.model.EmailTemplateDO;
import mx.com.cinepolis.digital.booking.model.EventMovieDO;
import mx.com.cinepolis.digital.booking.model.ObservationDO;
import mx.com.cinepolis.digital.booking.model.PresaleDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingStatusDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingWeekScreenShowDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EmailTemplateDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.EventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ObservationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.PresaleDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ReportDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;
import mx.com.cinepolis.digital.booking.service.book.BookingServiceEJB;
import mx.com.cinepolis.digital.booking.service.bookingspecialevent.BookingSpecialEventServiceEJB;
import mx.com.cinepolis.digital.booking.service.income.IncomeServiceEJB;
import mx.com.cinepolis.digital.booking.service.reports.ServiceReportsEJB;
import mx.com.cinepolis.digital.booking.service.util.EmailSenderEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.RegionEmailSenderWorker;
import mx.com.cinepolis.digital.booking.service.util.RegionTOToCatalogTOTransformer;
import mx.com.cinepolis.digital.booking.service.util.TheaterDOToTheaterTOSimpleTransformer;
import mx.com.cinepolis.digital.booking.service.util.TheaterEmailSenderWorker;
import mx.com.cinepolis.digital.booking.service.util.ValidatorUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que implementa los servicios de programacion
 * 
 * @author aramirezg
 * @author afuentes
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class BookingServiceEJBImpl implements BookingServiceEJB
{
  private static final String STANDARD_DATE_FORMAT = "dd/MM/yyyy";
  private static final String END_DAY_PLACEHOLDER = "$endDay";
  private static final String START_DAY_PLACEHOLDER = "$startDay";
  private static final String WEEK_NUMBER_PLACEHOLDER = "$weekNumber";
  private static final Logger LOG = LoggerFactory.getLogger( BookingServiceEJBImpl.class );
  @EJB
  private TheaterDAO theaterDAO;

  @EJB
  private BookingDAO bookingDAO;

  @EJB
  private BookingStatusDAO bookingStatusDAO;

  @EJB
  private EventDAO eventDAO;

  @EJB
  private WeekDAO weekDAO;

  @EJB
  private RegionDAO regionDAO;

  @EJB
  private ObservationDAO observationDAO;

  @EJB
  private ConfigurationDAO configurationDAO;

  @EJB
  private ScreenDAO screenDAO;

  @EJB
  private ServiceReportsEJB serviceReportsEJB;

  @EJB
  private EmailSenderEJB emailSenderEJB;

  @EJB
  private BookingWeekDAO bookingWeekDAO;

  @EJB
  private BookingWeekScreenDAO bookingWeekScreenDAO;

  @EJB
  private BookingWeekScreenShowDAO bookingWeekScreenShowDAO;

  @EJB
  private UserDAO userDAO;

  @EJB
  private ReportDAO reportDAO;

  @EJB
  private PresaleDAO presaleDAO;

  @EJB
  private EmailTemplateDAO emailTemplateDAO;

  @EJB
  private BookingSpecialEventServiceEJB bookingSpecialEventServiceEJB;

  @EJB
  private IncomeServiceEJB incomeServiceEJB;

  private static final int ID_BOOKING_TYPE_PRE_RELEASE = 2;
  private static final int ID_BOOKING_TYPE_SPECIAL_EVENT = 3;

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveOrUpdateBookings( List<BookingTO> bookingTOs )
  {
    if( CollectionUtils.isNotEmpty( bookingTOs ) )
    {
      for( BookingTO bookingTO : bookingTOs )
      {
        if( bookingTO.getIdBookingType() == ID_BOOKING_TYPE_PRE_RELEASE
            || bookingTO.getIdBookingType() == ID_BOOKING_TYPE_SPECIAL_EVENT )
        {
          continue;
        }
        else
        {
          ensureNewBooking( bookingTO );
          if( bookingTO.getId() == null )
          {
            // Se crea un nuevo registro
            createBooking( bookingTO );
          }
          else
          {
            if( bookingTO.getCopy() + bookingTO.getCopyScreenZero() == 0
                || bookingTO.getStatus().getId().equals( BookingStatus.TERMINATED.getIdLong() )
                || bookingTO.getStatus().getId().equals( BookingStatus.CANCELED.getIdLong() ) )
            {
              cancelOrTerminateCurrentWeek( bookingTO );
            }
            else
            {
              editCurrentWeek( bookingTO );
            }
          }
        }
      }

    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cancelSpecialEventBookings( List<BookingTO> bookingTOs )
  {
    if( CollectionUtils.isNotEmpty( bookingTOs ) )
    {
      for( BookingTO bookingTO : bookingTOs )
      {
        if( ValidatorUtil.validateBookingForUpdate( bookingTO ) )
        {
          if( bookingTO.getIdBookingType() == ID_BOOKING_TYPE_PRE_RELEASE
              || bookingTO.getIdBookingType() == ID_BOOKING_TYPE_SPECIAL_EVENT )
          {
            this.bookingSpecialEventServiceEJB.cancelScreenBookingTO( bookingTO );
          }
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cancelPresaleInBookings( List<BookingTO> bookingTOs )
  {
    if( CollectionUtils.isNotEmpty( bookingTOs ) )
    {
      for( BookingTO bookingTO : bookingTOs )
      {
        if( ValidatorUtil.validateBookingForUpdate( bookingTO ) )
        {
          if( bookingTO.getIdBookingType() == ID_BOOKING_TYPE_PRE_RELEASE
              || bookingTO.getIdBookingType() == ID_BOOKING_TYPE_SPECIAL_EVENT )
          {
            this.bookingSpecialEventServiceEJB.cancelScreenBookingTO( bookingTO );
          }
          else
          {
            this.cancelPresaleForWeekScreen( bookingTO );
          }
        }
      }
    }
  }

  /**
   * Method that cancels the presale for each screen in booking.
   * 
   * @author jreyesv
   * @param bookingTO
   */
  private void cancelPresaleForWeekScreen( BookingTO bookingTO )
  {
    boolean booked = isCurrentBookingBooked( bookingTO );
    if( booked )
    {
      BookingDO bookingDO = getCurrenBookingDO( bookingTO, true );
      BookingWeekDO currentWeek = getCurrentWeekDO( bookingTO, bookingDO );
      if( currentWeek != null )
      {
        TheaterDO theater = currentWeek.getIdBooking().getIdTheater();
        List<ScreenDO> screensForBooking = this.extractScreens( bookingTO,
          Arrays.asList( BookingStatus.BOOKED, BookingStatus.CONTINUE ), theater );
        this.updateBookingWeekScreenPresale( currentWeek, screensForBooking, bookingTO );
      }
    }
  }

  private void ensureNewBooking( BookingTO bookingTO )
  {
    if( bookingTO.getId() == null )
    {
      BookingDO b = this.bookingDAO.findByEventIdAndTheaterId( bookingTO.getTheater().getId(), bookingTO.getEvent()
          .getIdEvent() );
      if( b != null )
      {
        bookingTO.setId( b.getIdBooking() );
      }
    }
  }

  private void createBooking( BookingTO bookingTO )
  {
    BookingDO bookingDO = new BookingDO();
    AbstractEntityUtils.applyElectronicSign( bookingDO, bookingTO );
    bookingDO.setFgBooked( true );
    bookingDO.setIdEvent( this.eventDAO.find( bookingTO.getEvent().getIdEvent() ) );
    bookingDO.setIdTheater( this.theaterDAO.find( bookingTO.getTheater().getId().intValue() ) );
    bookingDO.setBookingWeekDOList( new ArrayList<BookingWeekDO>() );
    BookingTypeDO bookingType = new BookingTypeDO();
    bookingType.setIdBookingType( bookingTO.getIdBookingType() );
    bookingDO.setIdBookingType( bookingType );

    this.bookingDAO.create( bookingDO );

    BookingWeekDO currentWeek = new BookingWeekDO();
    WeekDO weekDO = this.weekDAO.find( bookingTO.getWeek().getIdWeek() );
    currentWeek.setFgSend( false );
    currentWeek.setIdBooking( bookingDO );
    currentWeek.setIdWeek( weekDO );
    int exhibitionWeek = getExhibitionWeek( bookingDO, weekDO );
    currentWeek.setNuExhibitionWeek( exhibitionWeek );
    currentWeek.setBookingWeekScreenDOList( new ArrayList<BookingWeekScreenDO>() );

    currentWeek.setIdBookingStatus( this.bookingStatusDAO.find( BookingStatus.BOOKED.getId() ) );
    currentWeek.setQtCopy( bookingTO.getCopy() );
    currentWeek.setQtCopyScreenZero( bookingTO.getCopyScreenZero() );
    currentWeek.setQtCopyScreenZeroTerminated( 0 );
    AbstractEntityUtils.applyElectronicSign( currentWeek, bookingTO );
    this.bookingWeekDAO.create( currentWeek );

    bookingDO.getBookingWeekDOList().add( currentWeek );
    this.bookingDAO.edit( bookingDO );
    editScreens( bookingTO, currentWeek );
    editObservationsAndShowings( bookingTO, currentWeek );

  }

  private void editCurrentWeek( BookingTO bookingTO )
  {
    boolean booked = isCurrentBookingBooked( bookingTO );

    if( booked )
    {
      BookingDO bookingDO = getCurrenBookingDO( bookingTO, true );
      BookingWeekDO currentWeek = getCurrentWeekDO( bookingTO, bookingDO );
      if( currentWeek == null )
      {
        WeekDO weekDO = this.weekDAO.find( bookingTO.getWeek().getIdWeek() );
        currentWeek = new BookingWeekDO();
        currentWeek.setFgSend( false );
        currentWeek.setIdBooking( bookingDO );
        currentWeek.setIdWeek( weekDO );
        int exhibitionWeek = getExhibitionWeek( bookingDO, weekDO );
        currentWeek.setNuExhibitionWeek( exhibitionWeek );
        currentWeek.setBookingWeekScreenDOList( new ArrayList<BookingWeekScreenDO>() );
        currentWeek.setIdBookingStatus( this.bookingStatusDAO.find( BookingStatus.BOOKED.getId() ) );
        AbstractEntityUtils.applyElectronicSign( currentWeek, bookingTO );
        bookingDO.getBookingWeekDOList().add( currentWeek );
        this.bookingWeekDAO.create( currentWeek );
      }

      currentWeek.setIdBookingStatus( this.bookingStatusDAO.find( BookingStatus.BOOKED.getId() ) );
      currentWeek.setQtCopy( bookingTO.getCopy() );
      currentWeek.setQtCopyScreenZero( bookingTO.getCopyScreenZero() );
      currentWeek.setQtCopyScreenZeroTerminated( bookingTO.getCopyScreenZeroTerminated() );

      AbstractEntityUtils.applyElectronicSign( currentWeek, bookingTO );

      this.bookingWeekDAO.edit( currentWeek );
      editScreens( bookingTO, currentWeek );
      editObservationsAndShowings( bookingTO, currentWeek );

      // En caso de existir semanas posteriores se eliminan y se "copia" la semana al futuro
      modifyFutureWeeks( bookingDO, currentWeek );

    }
    else
    {
      cancelOrTerminateCurrentWeek( bookingTO );
    }

  }

  private void modifyFutureWeeks( BookingDO bookingDO, BookingWeekDO currentWeek )
  {
    List<BookingWeekDO> futureWeeks = new ArrayList<BookingWeekDO>();
    for( BookingWeekDO bw : bookingDO.getBookingWeekDOList() )
    {
      if( bw.getIdWeek().getDtStartingDayWeek().after( currentWeek.getIdWeek().getDtStartingDayWeek() ) )
      {
        futureWeeks.add( bw );
      }
    }
    if( CollectionUtils.isNotEmpty( futureWeeks ) )
    {
      removeFutureWeeks( bookingDO, futureWeeks );
    }

  }

  private void removeFutureWeeks( BookingDO bookingDO, List<BookingWeekDO> futureWeeks )
  {
    for( BookingWeekDO bw : futureWeeks )
    {
      bookingDO.getBookingWeekDOList().remove( bw );

      if( CollectionUtils.isNotEmpty( bw.getBookingWeekScreenDOList() ) )
      {
        removeFutureWeekScreens( bw );
      }
      this.bookingWeekDAO.remove( bw );
    }

    this.bookingDAO.edit( bookingDO );
  }

  private void removeFutureWeekScreens( BookingWeekDO bw )
  {
    List<BookingWeekScreenDO> bwsInRemoval = new ArrayList<BookingWeekScreenDO>( bw.getBookingWeekScreenDOList() );
    for( BookingWeekScreenDO bws : bwsInRemoval )
    {
      bw.getBookingWeekScreenDOList().remove( bws );

      if( CollectionUtils.isNotEmpty( bws.getBookingWeekScreenShowDOList() ) )
      {
        List<BookingWeekScreenShowDO> bwssInRemoval = new ArrayList<BookingWeekScreenShowDO>(
            bws.getBookingWeekScreenShowDOList() );
        for( BookingWeekScreenShowDO bwss : bwssInRemoval )
        {
          bws.getBookingWeekScreenShowDOList().remove( bwss );
          this.bookingWeekScreenShowDAO.remove( bwss );
        }
      }
      this.bookingWeekScreenDAO.remove( bws );
    }
  }

  private void editObservationsAndShowings( BookingTO bookingTO, BookingWeekDO currentWeek )
  {

    if( CollectionUtils.isNotEmpty( bookingTO.getScreens() ) )
    {
      for( ScreenTO screenTO : bookingTO.getScreens() )
      {
        BookingWeekScreenDO bws = getBookingWeekScreenDO( currentWeek, screenTO );
        if( bws != null )
        {
          // Se valida el escenario donde no se envíe el TO de observaciones
          if( bws.getIdObservation() != null && screenTO.getBookingObservation() == null )
          {
            screenTO.setBookingObservation( new BookingObservationTO() );
            screenTO.getBookingObservation().setId( bws.getIdObservation().getIdObservation() );
          }
          if( screenTO.getBookingObservation() != null )
          {
            AbstractTOUtils.copyElectronicSign( bookingTO, screenTO.getBookingObservation() );
            editObservation( bws, screenTO.getBookingObservation() );
          }
          AbstractTOUtils.copyElectronicSign( bookingTO, screenTO );
          editShowings( bws, screenTO );
        }
      }
    }
  }

  private void editShowings( BookingWeekScreenDO bws, ScreenTO screenTO )
  {
    if( CollectionUtils.isNotEmpty( bws.getBookingWeekScreenShowDOList() ) )
    {
      List<BookingWeekScreenShowDO> showsInRemoval = new ArrayList<BookingWeekScreenShowDO>();
      for( BookingWeekScreenShowDO bwss : bws.getBookingWeekScreenShowDOList() )
      {
        if( CollectionUtils.isEmpty( screenTO.getShowings() )
            || !screenTO.getShowings().contains( new CatalogTO( bwss.getIdBookingWeekScreenShow() ) ) )
        {
          showsInRemoval.add( bwss );
        }

      }
      for( BookingWeekScreenShowDO bwss : showsInRemoval )
      {
        bws.getBookingWeekScreenShowDOList().remove( bwss );
        bookingWeekScreenShowDAO.remove( bwss );
      }
    }

    List<BookingWeekScreenShowDO> bookingWeekScreenShowDOList = new ArrayList<BookingWeekScreenShowDO>();
    if( CollectionUtils.isNotEmpty( screenTO.getShowings() ) )
    {
      for( CatalogTO showing : screenTO.getShowings() )
      {
        if( !CollectionUtils.exists(
          bws.getBookingWeekScreenShowDOList(),
          PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getNuShow" ),
            PredicateUtils.equalPredicate( showing.getId().intValue() ) ) ) )
        {
          BookingWeekScreenShowDO show = new BookingWeekScreenShowDO();
          show.setIdBookingWeekScreen( bws );
          show.setNuShow( showing.getId().intValue() );
          this.bookingWeekScreenShowDAO.create( show );
          bookingWeekScreenShowDOList.add( show );
        }
      }
    }
    bws.setBookingWeekScreenShowDOList( bookingWeekScreenShowDOList );
    this.bookingWeekScreenDAO.edit( bws );

  }

  private void editObservation( BookingWeekScreenDO bws, BookingObservationTO bookingObservation )
  {
    if( bws != null )
    {
      if( bws.getIdObservation() == null && StringUtils.isNotBlank( bookingObservation.getObservation() ) )
      {
        ObservationDO observationDO = new ObservationDO();
        AbstractEntityUtils.applyElectronicSign( observationDO, bookingObservation );
        observationDO.setBookingWeekScreenDOList( new ArrayList<BookingWeekScreenDO>() );
        observationDO.getBookingWeekScreenDOList().add( bws );
        observationDO.setDsObservation( bookingObservation.getObservation() );
        observationDO.setIdUser( new UserDO( bookingObservation.getUserId().intValue() ) );
        bws.setIdObservation( observationDO );
        this.observationDAO.create( observationDO );
      }
      else if( bws.getIdObservation() != null )
      {
        if( StringUtils.isBlank( bookingObservation.getObservation() ) )
        {
          // Se fuerza la eliminación del comentario
          ObservationDO observationDO = bws.getIdObservation();
          this.observationDAO.remove( observationDO );
          bws.setIdObservation( null );
        }
        else
        {
          ObservationDO observationDO = bws.getIdObservation();
          observationDO.setDsObservation( bookingObservation.getObservation() );
          AbstractEntityUtils.applyElectronicSign( observationDO, bookingObservation );
        }
      }
      this.bookingWeekScreenDAO.edit( bws );
    }
  }

  private BookingWeekScreenDO getBookingWeekScreenDO( BookingWeekDO currentWeek, ScreenTO screenTO )
  {
    BookingWeekScreenDO bws = null;
    for( BookingWeekScreenDO bookingWeekScreenDO : currentWeek.getBookingWeekScreenDOList() )
    {
      if( bookingWeekScreenDO.getIdScreen().getNuScreen() == screenTO.getNuScreen().intValue() )
      {
        bws = bookingWeekScreenDO;
        break;
      }
    }

    return bws;
  }

  @SuppressWarnings("unchecked")
  private void editScreens( BookingTO bookingTO, BookingWeekDO currentWeek )
  {
    TheaterDO theater = currentWeek.getIdBooking().getIdTheater();
    // Intercambia las salas
    List<ScreenDO> screensExchanged = extractScreensForExchange( bookingTO, currentWeek );

    List<ScreenDO> screensZeroExchanged = extractScreensZeroExchanged( bookingTO, currentWeek, screensExchanged );

    // Se restan las salas intercambiadas por las salas a programar
    List<ScreenDO> screensForBooking = (List<ScreenDO>) CollectionUtils.subtract(
      extractScreens( bookingTO, Arrays.asList( BookingStatus.BOOKED, BookingStatus.CONTINUE ), theater ),
      screensExchanged );

    // Se restan las salas eliminadas por movimiento a sala 0
    List<ScreenDO> screensForCancel = (List<ScreenDO>) CollectionUtils.subtract(
      extractScreens( bookingTO, Arrays.asList( BookingStatus.CANCELED, BookingStatus.TERMINATED ), theater ),
      screensZeroExchanged );

    List<BookingWeekScreenDO> screensAlreadyBooked = (List<BookingWeekScreenDO>) CollectionUtils.select(
      currentWeek.getBookingWeekScreenDOList(),
      PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdBookingStatus" ),
        PredicateUtils.equalPredicate( new BookingStatusDO( BookingStatus.BOOKED.getId() ) ) ) );

    List<BookingWeekScreenDO> bookingWeekScreensForRemoval = new ArrayList<BookingWeekScreenDO>();
    for( BookingWeekScreenDO bookingWeekScreenDO : screensAlreadyBooked )
    {
      ScreenDO screen = bookingWeekScreenDO.getIdScreen();
      if( !screensExchanged.contains( screen ) && !screensZeroExchanged.contains( screen )
          && !screensForBooking.contains( screen ) && !screensForCancel.contains( screen )
          && !bookingWeekScreensForRemoval.contains( bookingWeekScreenDO ) )
      {
        bookingWeekScreensForRemoval.add( bookingWeekScreenDO );
      }
    }

    // Quitar las salas ya registradas
    List<ScreenDO> screensForBookingRevised = new ArrayList<ScreenDO>();
    for( ScreenDO screen : screensForBooking )
    {
      boolean screenAlreadyAdded = CollectionUtils.exists(
        screensAlreadyBooked,
        PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdScreen" ),
          PredicateUtils.equalPredicate( screen ) ) );

      if( !screenAlreadyAdded )
      {
        screensForBookingRevised.add( screen );
      }
    }

    this.cancelBookingWeekScreen( currentWeek, screensForCancel );
    this.bookBookingWeekScreen( currentWeek, screensForBookingRevised, bookingTO );
    this.updateBookingWeekScreenPresale( currentWeek, screensForBooking, bookingTO );
    this.removeBookingWeekScreens( bookingWeekScreensForRemoval );
  }

  private void removeBookingWeekScreens( List<BookingWeekScreenDO> bookingWeekScreensForRemoval )
  {
    for( BookingWeekScreenDO bws : bookingWeekScreensForRemoval )
    {
      ScreenDO screenDO = this.screenDAO.find( bws.getIdScreen().getIdScreen() );
      screenDO.getBookingWeekScreenDOList().remove( bws );

      if( CollectionUtils.isNotEmpty( bws.getBookingWeekScreenShowDOList() ) )
      {
        List<BookingWeekScreenShowDO> bwssInRemoval = new ArrayList<BookingWeekScreenShowDO>();
        bwssInRemoval.addAll( bws.getBookingWeekScreenShowDOList() );
        for( BookingWeekScreenShowDO bwss : bwssInRemoval )
        {
          bws.getBookingWeekScreenShowDOList().remove( bwss );
          this.bookingWeekScreenShowDAO.remove( bwss );
        }
      }
      // jreyesv: remover preventa.
      if( CollectionUtils.isNotEmpty( bws.getPresaleDOList() ) )
      {
        bws.getPresaleDOList().remove( 0 );
      }
      BookingWeekDO bw = bws.getIdBookingWeek();
      bw.getBookingWeekScreenDOList().remove( bws );
      this.bookingWeekDAO.edit( bw );
      this.bookingWeekScreenDAO.remove( bws );
    }
  }

  private List<ScreenDO> extractScreensZeroExchanged( BookingTO bookingTO, BookingWeekDO currentWeek,
      List<ScreenDO> screensExchanged )
  {
    TheaterDO theater = currentWeek.getIdBooking().getIdTheater();
    // "Elimina" las salas que se mueven de sala numerada a Zero
    List<ScreenDO> screensZeroExchanged = new ArrayList<ScreenDO>();
    if( CollectionUtils.isNotEmpty( bookingTO.getNuScreenToZero() ) )
    {
      for( Integer nuScreen : bookingTO.getNuScreenToZero() )
      {
        ScreenTO screen = new ScreenTO();
        screen.setNuScreen( nuScreen );
        ScreenDO screenDO = extractScreenDO( theater, screen );
        if( !screensExchanged.contains( screenDO ) )
        {
          BookingWeekScreenDO bwsInRemoval = null;
          for( BookingWeekScreenDO bws : currentWeek.getBookingWeekScreenDOList() )
          {
            if( bws.getIdScreen().equals( screenDO ) )
            {
              bwsInRemoval = bws;
              break;
            }
          }
          if( bwsInRemoval != null )
          {
            removeBookingWeekScreen( currentWeek, screensZeroExchanged, screenDO, bwsInRemoval );
          }
        }
      }
    }
    return screensZeroExchanged;
  }

  private void removeBookingWeekScreen( BookingWeekDO currentWeek, List<ScreenDO> screensZeroExchanged,
      ScreenDO screenDO, BookingWeekScreenDO bwsInRemoval )
  {
    currentWeek.getBookingWeekScreenDOList().remove( bwsInRemoval );
    this.bookingWeekDAO.edit( currentWeek );
    if( CollectionUtils.isNotEmpty( bwsInRemoval.getBookingWeekScreenShowDOList() ) )
    {
      List<BookingWeekScreenShowDO> bwssInRemoval = new ArrayList<BookingWeekScreenShowDO>();
      bwssInRemoval.addAll( bwsInRemoval.getBookingWeekScreenShowDOList() );
      for( BookingWeekScreenShowDO bwss : bwssInRemoval )
      {
        bwsInRemoval.getBookingWeekScreenShowDOList().remove( bwss );
        this.bookingWeekScreenShowDAO.remove( bwss );
      }
    }
    this.bookingWeekScreenDAO.remove( bwsInRemoval );
    screensZeroExchanged.add( screenDO );
  }

  private List<ScreenDO> extractScreensForExchange( BookingTO bookingTO, BookingWeekDO currentWeek )
  {
    TheaterDO theater = currentWeek.getIdBooking().getIdTheater();
    List<ScreenDO> screensExchanged = new ArrayList<ScreenDO>();
    if( CollectionUtils.isNotEmpty( bookingTO.getScreens() ) )
    {
      // jreyesv
      Integer originalNuScreen;
      for( ScreenTO screen : bookingTO.getScreens() )
      {
        originalNuScreen = (screen.getOriginalNuScreen() == null ? new Integer( 0 ) : screen.getOriginalNuScreen());
        if( !originalNuScreen.equals( new Integer( 0 ) ) && !originalNuScreen.equals( screen.getNuScreen() ) )
        {
          ScreenTO lastScreen = new ScreenTO();
          lastScreen.setNuScreen( originalNuScreen );
          ScreenDO lastScreenDO = extractScreenDO( theater, lastScreen );
          ScreenDO screenDO = extractScreenDO( theater, screen );

          for( BookingWeekScreenDO bws : currentWeek.getBookingWeekScreenDOList() )
          {
            if( bws.getIdScreen().equals( lastScreenDO ) )
            {
              lastScreenDO.getBookingWeekScreenDOList().remove( bws );
              bws.setIdScreen( screenDO );
              this.bookingWeekScreenDAO.edit( bws );
              if( bws.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.BOOKED.getId() ) )
              {
                // Sólo se consideran las salas que estaban programadas
                screensExchanged.add( screenDO );
              }
              break;
            }
          }
        }
        // jreyesv
        else if( !originalNuScreen.equals( screen.getNuScreen() ) )
        {
          ScreenDO screenDO = extractScreenDO( theater, screen );
          if( !screenAlreadyRegistered( currentWeek.getBookingWeekScreenDOList(), screenDO ) )
          {
            BookingWeekScreenDO bws = new BookingWeekScreenDO();
            bws.setIdBookingWeek( currentWeek );
            bws.setIdScreen( screenDO );
            if( screen.getPresaleTO() != null && screen.getPresaleTO().getDtFinalDayPresale() != null )
            {
              bws.setPresaleDOList( createPresale( screen.getPresaleTO() ) );
            }
            bws.setIdBookingStatus( this.bookingStatusDAO.find( screen.getBookingStatus().getId().intValue() ) );
            createBookingWeekScreenDO( bws, currentWeek );
          }
        }
      }
    }
    return screensExchanged;
  }

  /**
   * Method for create Array of presales
   * 
   * @param presaleTO
   * @return
   */
  private List<PresaleDO> createPresale( PresaleTO presaleTO )
  {
    List<PresaleDO> presaleDOList = new ArrayList<PresaleDO>();
    presaleDOList.add( (PresaleDO) new PresaleTOToPresaleDOTransformer().transform( presaleTO ) );
    return presaleDOList;
  }

  /**
   * Method that returns true if a given Screen is already in the list of
   * {@link mx.com.cinepolis.digital.booking.model.BookingWeekScreenDO}
   * 
   * @param registeredBookingWeekScreens
   * @param screenDO
   * @return
   */
  private boolean screenAlreadyRegistered( List<BookingWeekScreenDO> registeredBookingWeekScreens, ScreenDO screenDO )
  {

    return CollectionUtils.exists(
      registeredBookingWeekScreens,
      PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdScreen" ),
        PredicateUtils.equalPredicate( screenDO ) ) );

  }

  private ScreenDO extractScreenDO( TheaterDO theater, ScreenTO screen )
  {
    ScreenDO screenDO = (ScreenDO) CollectionUtils.find(
      theater.getScreenDOList(),
      PredicateUtils.andPredicate(
        PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getNuScreen" ),
          PredicateUtils.equalPredicate( screen.getNuScreen().intValue() ) ),
        PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "isFgActive" ),
          PredicateUtils.equalPredicate( true ) ) ) );
    return screenDO;
  }

  private void cancelBookingWeekScreen( BookingWeekDO currentWeek, List<ScreenDO> screensForCancel )
  {
    BookingStatusDO status = null;
    if( currentWeek.getNuExhibitionWeek() == 1 )
    {
      status = this.bookingStatusDAO.find( BookingStatus.CANCELED.getId() );
    }
    else
    {
      status = this.bookingStatusDAO.find( BookingStatus.TERMINATED.getId() );
    }

    for( ScreenDO screenForCancel : screensForCancel )
    {
      BookingWeekScreenDO bws = (BookingWeekScreenDO) CollectionUtils.find(
        currentWeek.getBookingWeekScreenDOList(),
        PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdScreen" ),
          PredicateUtils.equalPredicate( screenForCancel ) ) );

      if( bws != null )
      {
        bws.setIdBookingStatus( status );
        /* jreyesv: Se valida que exista una preventa para la sala programada. */
        if( CollectionUtils.isNotEmpty( bws.getPresaleDOList() ) )
        {
          /* jreyesv: Se cancela la preventa para la sala programada. */
          bws.getPresaleDOList().get( 0 ).setFgActive( false );
          AbstractEntityUtils.copyElectronicSign( currentWeek, bws.getPresaleDOList().get( 0 ) );
          bws.getPresaleDOList().get( 0 ).setIdBookingWeekScreen( bws );
        }
        this.bookingWeekScreenDAO.edit( bws );
      }
      else
      {
        bws = new BookingWeekScreenDO();
        bws.setIdBookingWeek( currentWeek );
        bws.setIdScreen( screenForCancel );
        bws.setIdBookingStatus( status );
        createBookingWeekScreenDO( bws, currentWeek );
      }
    }
  }

  /**
   * Updates the {@link mx.com.cinepolis.digital.booking.model.PresaleDO} for each
   * {@link mx.com.cinepolis.digital.booking.model.BookingWeekScreenDO} booked.
   * 
   * @param currentWeek, with the current week information.
   * @param screensForUpdate, with the screen list for update.
   * @param bookingTO, with the booking information.
   * @author jreyesv
   */
  private void updateBookingWeekScreenPresale( BookingWeekDO currentWeek, List<ScreenDO> screensForUpdate,
      BookingTO bookingTO )
  {
    for( ScreenDO screenForUpdate : screensForUpdate )
    {
      BookingWeekScreenDO bws = (BookingWeekScreenDO) CollectionUtils.find(
        currentWeek.getBookingWeekScreenDOList(),
        PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdScreen" ),
          PredicateUtils.equalPredicate( screenForUpdate ) ) );
      /* jreyesv: Se obtiene la sala que contiene la información actualizada. */
      ScreenTO screenTO = this.extractScreenTO( bookingTO, screenForUpdate.getIdScreen().longValue() );
      if( bws != null )
      {
        /* jreyesv: Se valida que exista una preventa para la sala programada. */
        if( CollectionUtils.isNotEmpty( bws.getPresaleDOList() ) )
        {
          /* jreyesv: Se actualizan los datos de preventa para la sala programada. */
          this.updatePresaleForScreen( bws.getPresaleDOList().get( 0 ), screenTO );
          bws.getPresaleDOList().get( 0 ).setIdBookingWeekScreen( bws );
        }
        else if( screenTO != null && screenTO.getPresaleTO() != null && screenTO.getPresaleTO().isFgActive() )
        {
          /* jreyesv: Se crea y asocia una preventa para la sala programada. */
          this.createPresaleForScreen( bws, screenTO );
        }
        this.bookingWeekScreenDAO.edit( bws );
      }
    }
  }

  private void bookBookingWeekScreen( BookingWeekDO currentWeek, List<ScreenDO> screensForBooking, BookingTO bookingTO )
  {
    BookingStatusDO status = this.bookingStatusDAO.find( BookingStatus.BOOKED.getId() );

    for( ScreenDO screenForBooking : screensForBooking )
    {
      BookingWeekScreenDO bws = (BookingWeekScreenDO) CollectionUtils.find(
        currentWeek.getBookingWeekScreenDOList(),
        PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdScreen" ),
          PredicateUtils.equalPredicate( screenForBooking ) ) );
      /* jreyesv: Se obtiene la sala que contiene la información actualizada. */
      ScreenTO screenTO = this.extractScreenTO( bookingTO, screenForBooking.getIdScreen().longValue() );
      if( bws != null )
      {
        bws.setIdBookingStatus( status );
        /* jreyesv: Se valida que exista una preventa para la sala programada. */
        if( CollectionUtils.isNotEmpty( bws.getPresaleDOList() ) )
        {
          /* jreyesv: Se actualizan los datos de preventa para la sala programada. */
          this.updatePresaleForScreen( bws.getPresaleDOList().get( 0 ), screenTO );
          bws.getPresaleDOList().get( 0 ).setIdBookingWeekScreen( bws );
        }
        else if( screenTO.getPresaleTO() != null && screenTO.getPresaleTO().isFgActive() )
        {
          /* jreyesv: Se crea y asocia una preventa para la sala programada. */
          this.createPresaleForScreen( bws, screenTO );
        }
        this.bookingWeekScreenDAO.edit( bws );
      }
      else
      {
        bws = new BookingWeekScreenDO();
        bws.setIdBookingWeek( currentWeek );
        bws.setIdScreen( screenForBooking );
        bws.setIdBookingStatus( status );
        /* jreyesv: Se crea y asocia una preventa para la sala de evento especial. */
        if( screenTO.getPresaleTO() != null && screenTO.getPresaleTO().isFgActive() )
        {
          this.createPresaleForScreen( bws, screenTO );
        }
        this.createBookingWeekScreenDO( bws, currentWeek );
      }
    }
  }

  /**
   * Method that extracts a {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} object from a
   * {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} list.
   * 
   * @param bookingTO, with the {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} information.
   * @param idScreenDO, a {@link java.lang.Long} with the screen identifier.
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} object.
   * @author jreyesv
   */
  private ScreenTO extractScreenTO( BookingTO bookingTO, Long idScreenDO )
  {
    ScreenTO screenTOResponse = null;
    if( CollectionUtils.isNotEmpty( bookingTO.getScreens() ) )
    {
      for( ScreenTO screenTO : bookingTO.getScreens() )
      {
        if( screenTO.getId().equals( idScreenDO ) )
        {
          screenTOResponse = screenTO;
          break;
        }
      }
    }
    return screenTOResponse;
  }

  /**
   * Method that creates and associates a {@link mx.com.cinepolis.digital.booking.model.PresaleDO} with a
   * {@link mx.com.cinepolis.digital.booking.model.BookingWeekScreenDO}.
   * 
   * @param bookingWeekScreenDO, with the special event screen information.
   * @param screenTO, with the screen information.
   * @author jreyesv
   */
  private void createPresaleForScreen( BookingWeekScreenDO bookingWeekScreenDO, ScreenTO screenTO )
  {
    bookingWeekScreenDO.setPresaleDOList( new ArrayList<PresaleDO>() );
    bookingWeekScreenDO.getPresaleDOList().add(
      (PresaleDO) new PresaleTOToPresaleDOTransformer().transform( screenTO.getPresaleTO() ) );
    bookingWeekScreenDO.getPresaleDOList().get( 0 ).setIdBookingWeekScreen( bookingWeekScreenDO );
  }

  /**
   * Method that updates a {@link mx.com.cinepolis.digital.booking.model.PresaleDO} associated with a
   * {@link mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO}.
   * 
   * @param presaleDO, with the presale information.
   * @param screenTO, with the screen information.
   * @author jreyesv
   */
  private void updatePresaleForScreen( PresaleDO presaleDO, ScreenTO screenTO )
  {
    if( screenTO.getPresaleTO().getDtFinalDayPresale() != null )
    {
      presaleDO.setDtStartDayPresale( screenTO.getPresaleTO().getDtStartDayPresale() );
      presaleDO.setDtFinalDayPresale( screenTO.getPresaleTO().getDtFinalDayPresale() );
      presaleDO.setDtReleaseDay( screenTO.getPresaleTO().getDtReleaseDay() );
      presaleDO.setFgActive( screenTO.getPresaleTO().isFgActive() );
    }
    AbstractEntityUtils.applyElectronicSign( presaleDO, screenTO );
  }

  private void createBookingWeekScreenDO( BookingWeekScreenDO bws, BookingWeekDO currentWeek )
  {
    this.bookingWeekScreenDAO.create( bws );
    currentWeek.getBookingWeekScreenDOList().add( bws );
    this.bookingWeekDAO.edit( currentWeek );
  }

  private List<ScreenDO> extractScreens( BookingTO bookingTO, List<BookingStatus> status, TheaterDO theater )
  {
    List<ScreenTO> screens = bookingTO.getScreens();
    List<ScreenDO> list = new ArrayList<ScreenDO>();

    if( CollectionUtils.isNotEmpty( screens ) )
    {
      for( ScreenTO screen : screens )
      {
        if( status.contains( BookingStatus.fromId( screen.getBookingStatus().getId().intValue() ) ) )
        {

          ScreenDO screenDO = extractScreenDO( theater, screen );
          if( screenDO != null && !list.contains( screenDO ) )
          {
            list.add( screenDO );
          }
        }
      }
    }
    return list;
  }

  private int getExhibitionWeek( BookingDO bookingDO, WeekDO weekDO )
  {
    int exhibitionWeek = 1;

    BookingWeekDO firstWeek = (BookingWeekDO) CollectionUtils.find( bookingDO.getBookingWeekDOList(), PredicateUtils
        .andPredicate( PredicateUtils.transformedPredicate(
          TransformerUtils.invokerTransformer( "getNuExhibitionWeek" ), PredicateUtils.equalPredicate( 1 ) ),
          PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdBookingStatus" ),
            PredicateUtils.equalPredicate( new BookingStatusDO( BookingStatus.BOOKED.getId() ) ) ) ) );
    if( firstWeek != null )
    {
      exhibitionWeek = getCurrentExhibitionWeek( weekDO.getDtStartingDayWeek(), firstWeek.getIdWeek()
          .getDtStartingDayWeek() );
    }

    return exhibitionWeek;
  }

  private int getCurrentExhibitionWeek( Date week, Date start )
  {
    int exhibitionWeek = 1;

    ConfigurationDO conf = this.configurationDAO.findByParameterName( Configuration.WEEK_START_DAY.getParameter() );
    int weekStartDay = Integer.parseInt( conf.getDsValue() );

    Date day = getDayOfStart( (Date) start.clone(), weekStartDay );
    Date currentExhibitionWeekDay = getDayOfStart( (Date) week.clone(), weekStartDay );

    while( day.before( currentExhibitionWeekDay ) )
    {
      day = DateUtils.addDays( day, 7 );
      exhibitionWeek++;
    }

    return exhibitionWeek;
  }

  private Date getDayOfStart( Date s, int weekStartDay )
  {
    while( true )
    {
      Calendar cal = Calendar.getInstance();
      cal.setTime( s );
      if( cal.get( Calendar.DAY_OF_WEEK ) == weekStartDay )
      {
        break;
      }
      else
      {
        s = DateUtils.addDays( s, 1 );
      }
    }
    return s;
  }

  private boolean isCurrentBookingBooked( BookingTO bookingTO )
  {
    boolean booked = bookingTO.getCopyScreenZero() > 0;
    if( !booked && CollectionUtils.isNotEmpty( bookingTO.getScreens() ) )
    {
      for( ScreenTO screen : bookingTO.getScreens() )
      {
        if( screen.getBookingStatus().getId().equals( BookingStatus.BOOKED.getIdLong() )
            || screen.getBookingStatus().getId().equals( BookingStatus.CONTINUE.getIdLong() ) )
        {
          booked = true;
        }
      }
    }
    return booked;
  }

  private void cancelOrTerminateCurrentWeek( BookingTO bookingTO )
  {
    BookingDO bookingDO = getCurrenBookingDO( bookingTO, false );
    BookingWeekDO currentWeek = getCurrentWeekDO( bookingTO, bookingDO );
    if( currentWeek != null )
    {
      if( currentWeek.getNuExhibitionWeek() == 1 )
      {
        currentWeek.setIdBookingStatus( this.bookingStatusDAO.find( BookingStatus.CANCELED.getId() ) );
        currentWeek.setQtCopy( 0 );
        currentWeek.setQtCopyScreenZero( 0 );
        currentWeek.setQtCopyScreenZeroTerminated( 0 );
      }
      else
      {
        currentWeek.setIdBookingStatus( this.bookingStatusDAO.find( BookingStatus.TERMINATED.getId() ) );
        currentWeek.setQtCopy( 0 );
        currentWeek.setQtCopyScreenZero( 0 );
        currentWeek.setQtCopyScreenZeroTerminated( bookingTO.getCopyScreenZeroTerminated() );
      }
      AbstractEntityUtils.applyElectronicSign( currentWeek, bookingTO );

      cancelOrTerminateCurrentWeekScreens( currentWeek );
      removeNextWeeks( bookingDO, currentWeek );
    }
    else
    {
      WeekDO weekDO = this.weekDAO.find( bookingTO.getWeek().getIdWeek() );
      currentWeek = new BookingWeekDO();
      currentWeek.setFgSend( false );
      currentWeek.setIdBooking( bookingDO );
      currentWeek.setIdWeek( weekDO );
      int exhibitionWeek = getExhibitionWeek( bookingDO, weekDO );
      currentWeek.setNuExhibitionWeek( exhibitionWeek );
      currentWeek.setBookingWeekScreenDOList( new ArrayList<BookingWeekScreenDO>() );
      if( currentWeek.getNuExhibitionWeek() == 1 )
      {
        currentWeek.setIdBookingStatus( this.bookingStatusDAO.find( BookingStatus.CANCELED.getId() ) );
      }
      else
      {
        currentWeek.setIdBookingStatus( this.bookingStatusDAO.find( BookingStatus.TERMINATED.getId() ) );
      }

      AbstractEntityUtils.applyElectronicSign( currentWeek, bookingTO );
      bookingDO.getBookingWeekDOList().add( currentWeek );
      this.bookingWeekDAO.create( currentWeek );

      currentWeek.setQtCopy( 0 );
      currentWeek.setQtCopyScreenZero( 0 );
      currentWeek.setQtCopyScreenZeroTerminated( bookingTO.getCopyScreenZeroTerminated() );

      AbstractEntityUtils.applyElectronicSign( currentWeek, bookingTO );

      this.bookingWeekDAO.edit( currentWeek );
      editScreens( bookingTO, currentWeek );
      editObservationsAndShowings( bookingTO, currentWeek );

    }
  }

  private BookingWeekDO getCurrentWeekDO( BookingTO bookingTO, BookingDO bookingDO )
  {
    return (BookingWeekDO) CollectionUtils.find(
      bookingDO.getBookingWeekDOList(),
      PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdWeek" ),
        PredicateUtils.equalPredicate( new WeekDO( bookingTO.getWeek().getIdWeek() ) ) ) );
  }

  private BookingDO getCurrenBookingDO( BookingTO bookingTO, boolean booked )
  {
    BookingDO bookingDO = this.bookingDAO.find( bookingTO.getId() );

    if( bookingDO == null )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.BOOKING_PERSISTENCE_ERROR_BOOKING_NOT_FOUND );
    }
    AbstractEntityUtils.applyElectronicSign( bookingDO, bookingTO );
    bookingDO.setFgBooked( booked );
    this.bookingDAO.edit( bookingDO );
    return bookingDO;
  }

  // Termina las salas de la semana seleccionada
  private void cancelOrTerminateCurrentWeekScreens( BookingWeekDO currentWeek )
  {
    if( CollectionUtils.isNotEmpty( currentWeek.getBookingWeekScreenDOList() ) )
    {
      for( BookingWeekScreenDO bookingWeekScreenDO : currentWeek.getBookingWeekScreenDOList() )
      {
        if( bookingWeekScreenDO.getIdBookingStatus().getIdBookingStatus() == BookingStatus.BOOKED.getId() )
        {
          if( CollectionUtils.isNotEmpty( bookingWeekScreenDO.getPresaleDOList() ) )
          {
            bookingWeekScreenDO.getPresaleDOList().get( 0 ).setFgActive( false );
            AbstractEntityUtils.copyElectronicSign( currentWeek, bookingWeekScreenDO.getPresaleDOList().get( 0 ) );
            bookingWeekScreenDO.getPresaleDOList().get( 0 ).setIdBookingWeekScreen( bookingWeekScreenDO );
          }
          bookingWeekScreenDO.setIdBookingStatus( this.bookingStatusDAO.find( currentWeek.getIdBookingStatus()
              .getIdBookingStatus() ) );
          this.bookingWeekScreenDAO.edit( bookingWeekScreenDO );
        }
      }
    }
  }

  private void removeNextWeeks( BookingDO bookingDO, BookingWeekDO currentWeek )
  {
    List<BookingWeekDO> nextWeeks = new ArrayList<BookingWeekDO>();

    for( BookingWeekDO bookingWeekDO : bookingDO.getBookingWeekDOList() )
    {
      if( bookingWeekDO.getIdWeek().getDtStartingDayWeek().getTime() > currentWeek.getIdWeek().getDtStartingDayWeek()
          .getTime() )
      {
        nextWeeks.add( bookingWeekDO );
      }
    }
    // Eliminar los registros de las siguientes semanas
    for( BookingWeekDO bookingWeekDO : nextWeeks )
    {

      if( CollectionUtils.isNotEmpty( bookingWeekDO.getBookingWeekScreenDOList() ) )
      {
        List<BookingWeekScreenDO> list = new ArrayList<BookingWeekScreenDO>();
        list.addAll( bookingWeekDO.getBookingWeekScreenDOList() );
        removeWeekScreens( list );
      }
      this.bookingWeekDAO.remove( bookingWeekDO );
      bookingDO.getBookingWeekDOList().remove( bookingWeekDO );
      this.bookingDAO.edit( bookingDO );
    }
  }

  private void removeWeekScreens( List<BookingWeekScreenDO> bookingWeekScreenDOList )
  {
    for( BookingWeekScreenDO kbw : bookingWeekScreenDOList )
    {
      kbw.getIdBookingWeek().getBookingWeekScreenDOList().remove( kbw );
      if( CollectionUtils.isNotEmpty( kbw.getBookingWeekScreenShowDOList() ) )
      {
        List<BookingWeekScreenShowDO> list = new ArrayList<BookingWeekScreenShowDO>();
        list.addAll( kbw.getBookingWeekScreenShowDOList() );
        for( BookingWeekScreenShowDO kbws : list )
        {
          kbws.getIdBookingWeekScreen().getBookingWeekScreenShowDOList().remove( kbws );
          this.bookingWeekScreenShowDAO.remove( kbws );
        }
      }
      this.bookingWeekScreenDAO.remove( kbw );
    }
  }

  private void validateEditableWeek( BookingTO bookingTO )
  {
    WeekTO week = this.weekDAO.getWeek( bookingTO.getWeek().getIdWeek() );
    AbstractTOUtils.copyElectronicSign( bookingTO, week );
    bookingTO.setWeek( week );
    int editableDays = Integer.parseInt( this.configurationDAO.findByParameterName(
      Configuration.EDITABLE_DAYS.getParameter() ).getDsValue() );
    ValidatorUtil.validateEditableWeek( bookingTO, editableDays );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cancelBooking( List<BookingTO> bookingTOs )
  {
    for( BookingTO bookingTO : bookingTOs )
    {
      this.cancelOrTerminateCurrentWeek( bookingTO );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void terminateBooking( BookingTO bookingTO )
  {
    BookingTO to = this.bookingDAO.get( bookingTO.getId(), bookingTO.getWeek().getIdWeek() );
    AbstractTOUtils.copyElectronicSign( bookingTO, to );

    ValidatorUtil.validateBookingForTermination( to );
    validateEditableWeek( to );
    validateTerminableWeek( to );

    CatalogTO cancellationStatus = new CatalogTO( BookingStatus.TERMINATED.getIdLong() );
    to.setStatus( cancellationStatus );
    if( CollectionUtils.isNotEmpty( to.getScreens() ) )
    {
      for( ScreenTO screen : to.getScreens() )
      {
        AbstractTOUtils.copyElectronicSign( bookingTO, screen );
        screen.setBookingStatus( cancellationStatus );
      }
    }
    this.bookingDAO.update( to );
  }

  private void validateTerminableWeek( BookingTO bookingTO )
  {
    int exhibitionWeek = bookingTO.getExhibitionWeek();
    if( exhibitionWeek == 1 )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_CAN_NOT_BE_TERMINATED );
    }
    BookingDO booking = this.bookingDAO.find( bookingTO.getId() );
    boolean lastExhibitionWeek = true;
    if( CollectionUtils.isNotEmpty( booking.getBookingWeekDOList() ) )
    {
      for( BookingWeekDO bookingWeek : booking.getBookingWeekDOList() )
      {
        if( bookingWeek.isFgActive()
            && !bookingWeek.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.CANCELED.getId() )
            && bookingWeek.getNuExhibitionWeek() > exhibitionWeek )
        {
          lastExhibitionWeek = false;
          break;
        }
      }
    }
    if( !lastExhibitionWeek )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_CAN_NOT_BE_TERMINATED );
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveOrUpdateBookingObservation( BookingObservationTO bookingObservationTO )
  {
    if( bookingObservationTO.getId() == null )
    {
      observationDAO.saveBookingObservation( bookingObservationTO );
    }
    else
    {
      observationDAO.update( bookingObservationTO );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<WeekTO> findWeeksActive( AbstractTO abstractTO )
  {
    List<WeekTO> currentWeeks = weekDAO.getCurrentWeeks( abstractTO.getTimestamp(), abstractTO.isFgActive() );
    Collections.sort( currentWeeks, new WeekTOComparator() );
    return currentWeeks;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public MovieBookingWeekTO findBookingMovies( PagingRequestTO pagingRequestTO )
  {
    Long idWeek = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_WEEK_ID );
    List<BookingTO> bookings = this.bookingDAO.findBookingsByEventRegionWeek( pagingRequestTO );

    PagingResponseTO<BookingTO> response = new PagingResponseTO<BookingTO>();

    List<BookingTO> filteredCanceledBookings = new ArrayList<BookingTO>();
    CatalogTO canceled = new CatalogTO( BookingStatus.CANCELED.getIdLong() );
    filteredCanceledBookings.addAll( CollectionUtils.select(
      bookings,
      PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getStatus" ),
        PredicateUtils.notPredicate( PredicateUtils.equalPredicate( canceled ) ) ) ) );

    response.setElements( filteredCanceledBookings );
    MovieBookingWeekTO bookingWeekTO = new MovieBookingWeekTO();
    bookingWeekTO.setCurrentWeek( this.weekDAO.getCurrentWeek( pagingRequestTO.getTimestamp() ) );
    bookingWeekTO.setResponse( response );
    bookingWeekTO.setSameOrBeforeWeek( isEditableWeek( pagingRequestTO.getTimestamp(), idWeek ) );
    return bookingWeekTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TheaterBookingWeekTO findBookingTheater( PagingRequestTO pagingRequestTO )
  {
    Long idWeek = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_WEEK_ID );
    Long idTheater = (Long) pagingRequestTO.getFilters().get( BookingQuery.BOOKING_THEATER_ID );
    TheaterBookingWeekTO theaterBookingWeekTO = new TheaterBookingWeekTO();
    PagingResponseTO<BookingTheaterTO> response = new PagingResponseTO<BookingTheaterTO>();

    List<BookingDO> bookingsActive = this.bookingDAO.findBookedByTheater( idTheater );
    List<BookingWeekDO> bookingWeeksActive = this.bookingWeekDAO
        .findByIdTheaterAndIdWeek( idTheater, idWeek.intValue() );

    List<BookingTheaterTO> elements = new ArrayList<BookingTheaterTO>();

    // Se quitan las programaciones que ya estaban y se arman los BookingTheaterTO correspondientes
    boolean sent = false;
    for( BookingWeekDO bw : bookingWeeksActive )
    {
      bookingsActive.remove( bw.getIdBooking() );
      // Se agregan las programaciones en sala cero normales y terminadas
      elements.addAll( extractBookingZeroScreens( bw, false ) );
      elements.addAll( extractBookingZeroScreens( bw, true ) );

      // Se agregan las salas programadas
      elements.addAll( extractBookingScreens( bw ) );
      if( !sent && bw.isFgSend() )
      {
        sent = true;
      }
    }

    // jreyesv: cambio para arrastre de programación, se prepara una fecha auxiliar, aumentando 7 dias a
    Calendar calendarAuxiliar = DateUtils.toCalendar( pagingRequestTO.getTimestamp() );
    calendarAuxiliar.add( Calendar.DATE, +7 );
    if( this.isEditableWeek( pagingRequestTO.getTimestamp(), idWeek )
        || this.isEditableWeek( calendarAuxiliar.getTime(), idWeek ) )
    {
      // Se agregan las programaciones de otras semanas que estaban Book/Continue
      List<BookingTheaterTO> bookingsFromLastWeeks = extractBookingsFromLastWeeks( idWeek, bookingsActive );
      for( BookingTheaterTO booking : bookingsFromLastWeeks )
      {
        booking.setBookingObservationTO( new BookingObservationTO() );
        booking.setSelectedShowings( new ArrayList<Object>() );
      }
      elements.addAll( bookingsFromLastWeeks );
    }

    // Completa los datos
    PagingRequestTO theaterRequest = new PagingRequestTO();
    theaterRequest.setNeedsPaging( Boolean.FALSE );
    theaterRequest.setFilters( new HashMap<ModelQuery, Object>() );
    theaterRequest.getFilters().put( TheaterQuery.THEATER_ID, idTheater );
    PagingResponseTO<TheaterTO> theaterResponse = this.theaterDAO.findAllByPaging( theaterRequest );
    TheaterTO theater = theaterResponse.getElements().get( 0 );
    this.addScreensWithoutBooking( theater, elements );

    // Se agregan las programaciones de eventos especiales
    elements.addAll( this.bookingDAO.extractBookingSpecialEvents( idTheater, idWeek.intValue() ) );

    Collections.sort( elements );

    WeekTO currentWeek = weekDAO.getWeek( idWeek.intValue() );
    Calendar cal = DateUtils.toCalendar( currentWeek.getStartingDayWeek() );
    cal.add( Calendar.DATE, -1 );
    WeekTO lastWeek = incomeServiceEJB.getLastWeek( currentWeek );

    for( BookingTheaterTO bookingTheater : elements )
    {
      if( bookingTheater.getEventTO() != null && bookingTheater.getScreenTO() != null
          && bookingTheater.getScreenTO().getId() != null )
      {
        Long eventId = bookingTheater.getEventTO().getIdEvent();
        Integer screenId = bookingTheater.getScreenTO().getId().intValue();

        IncomeDetailTO request = new IncomeDetailTO();
        request.setTheaterId( idTheater );
        request.setWeekId( lastWeek.getIdWeek() );
        request.setEventId( eventId );
        request.setScreenId( screenId );

        IncomeDetailTO detail = incomeServiceEJB.getIncomeDetail( request );
        if( detail != null )
        {
          bookingTheater.setFridayIncome( detail.getFridayIncome() );
          bookingTheater.setSaturdayIncome( detail.getSaturdayIncome() );
          bookingTheater.setSundayIncome( detail.getSundayIncome() );
          bookingTheater.setTotalIncome( detail.getTotalIncome() );

          bookingTheater.setFridayTickets( detail.getFridayTickets() );
          bookingTheater.setSaturdayTickets( detail.getSaturdayTickets() );
          bookingTheater.setSundayTickets( detail.getSundayTickets() );
          bookingTheater.setTotalTickets( detail.getTotalTickets() );
        }
      }

    }

    response.setElements( elements );

    theaterBookingWeekTO.setResponse( response );

    theaterBookingWeekTO.setImageSemaphore( this.bookingDAO.getTheaterSemaphore( idWeek, theater ) );
    theaterBookingWeekTO.setZero( this.bookingDAO.hasZeroBookings( idWeek, theater ) );

    Map<Long, List<EventTO>> availableEvents = new HashMap<Long, List<EventTO>>();
    for( ScreenTO screen : theater.getScreens() )
    {
      List<EventTO> events = this.findEventsForScreen( screen );
      availableEvents.put( screen.getId(), events );
    }
    theaterBookingWeekTO.setAvailableEvents( availableEvents );
    theaterBookingWeekTO.setEditable( isEditableWeek( pagingRequestTO.getTimestamp(), idWeek ) );
    theaterBookingWeekTO.setCurrentWeek( this.weekDAO.getCurrentWeek( pagingRequestTO.getTimestamp() ) );
    theaterBookingWeekTO.setSent( sent );
    return theaterBookingWeekTO;
  }

  private boolean isEditableWeek( Date date, Long idWeek )
  {
    WeekDO week = this.weekDAO.find( idWeek.intValue() );
    WeekTO currentWeek = this.weekDAO.getCurrentWeek( date );
    return week.getDtStartingDayWeek().before( currentWeek.getFinalDayWeek() );
  }

  private List<BookingTheaterTO> extractBookingsFromLastWeeks( Long idWeek, List<BookingDO> bookingsActive )
  {
    List<BookingTheaterTO> filtered = new ArrayList<BookingTheaterTO>();
    for( BookingDO bookingDO : bookingsActive )
    {
      if( CollectionUtils.isEmpty( bookingDO.getBookingWeekDOList() ) )
      {
        continue;
      }
      int nuWeek = 0;
      BookingWeekDO lastBookingWeek = null;
      WeekDO firstWeek = null;
      for( BookingWeekDO bw : bookingDO.getBookingWeekDOList() )
      {
        // Se descartan las semanas "canceladas"
        if( bw.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
        {
          if( bw.getNuExhibitionWeek() == 1 )
          {
            firstWeek = bw.getIdWeek();
          }
          if( bw.getNuExhibitionWeek() > nuWeek )
          {
            nuWeek = bw.getNuExhibitionWeek();
            lastBookingWeek = bw;
          }
        }
      }
      WeekDO weekDO = this.weekDAO.find( idWeek.intValue() );
      List<BookingTheaterTO> lastWeek = extractBookingScreens( lastBookingWeek );

      if( CollectionUtils.isNotEmpty( lastWeek )
          && weekDO.getDtStartingDayWeek().after( lastBookingWeek.getIdWeek().getDtStartingDayWeek() ) )
      {

        for( BookingTheaterTO to : lastWeek )
        {
          CatalogTO status = this.bookingStatusDAO.get( BookingStatus.CONTINUE.getId() );
          CatalogTO statusAvailable = this.bookingStatusDAO.get( BookingStatus.TERMINATED.getId() );
          to.setStatusTOs( new ArrayList<CatalogTO>() );
          to.getStatusTOs().add( status );
          to.getStatusTOs().add( statusAvailable );
        }

        int exhibitionWeek;
        if( firstWeek != null )
        {
          exhibitionWeek = getCurrentExhibitionWeek( weekDO.getDtStartingDayWeek(), firstWeek.getDtStartingDayWeek() );
        }
        else
        {
          // Escenario donde no existe la semana 1, se utiliza la última semana programada
          exhibitionWeek = lastBookingWeek.getNuExhibitionWeek()
              + getCurrentExhibitionWeek( weekDO.getDtStartingDayWeek(), lastBookingWeek.getIdWeek()
                  .getDtStartingDayWeek() ) - 1;
        }
        for( BookingTheaterTO bookingTheaterTO : lastWeek )
        {
          if( bookingTheaterTO.getStatusTO().getId().longValue() == BookingStatus.BOOKED.getIdLong()
              || bookingTheaterTO.getStatusTO().getId().longValue() == BookingStatus.CONTINUE.getIdLong() )
          {
            bookingTheaterTO.setWeek( exhibitionWeek );
            filtered.add( bookingTheaterTO );
          }
        }
      }

    }
    return filtered;
  }

  private List<BookingTheaterTO> extractBookingScreens( BookingWeekDO bw )
  {
    List<BookingTheaterTO> screens = new ArrayList<BookingTheaterTO>();
    CatalogTO status = null;
    CatalogTO statusAvailable = null;

    if( bw.getNuExhibitionWeek() == 1 )
    {
      statusAvailable = this.bookingStatusDAO.get( BookingStatus.CANCELED.getId() );
      status = this.bookingStatusDAO.get( BookingStatus.BOOKED.getId() );
    }
    else
    {
      statusAvailable = this.bookingStatusDAO.get( BookingStatus.TERMINATED.getId() );
      status = this.bookingStatusDAO.get( BookingStatus.CONTINUE.getId() );
    }
    BookingTO bookingTO = null;
    WeekTO currentWeek = null;
    EventTO eventTO = null;
    if( CollectionUtils.isNotEmpty( bw.getBookingWeekScreenDOList() ) )
    {
      bookingTO = (BookingTO) new BookingDOToBookingTOTransformer().transform( bw.getIdBooking() );
      currentWeek = (WeekTO) new WeekDOToWeekTOTransformer().transform( bw.getIdWeek() );
      currentWeek.setEditable( true );
      eventTO = (EventTO) new EventDOToEventTOTransformer().transform( bw.getIdBooking().getIdEvent() );
      for( BookingWeekScreenDO bws : bw.getBookingWeekScreenDOList() )
      {
        boolean inRemoval = false;
        // Se obtienen los estatus
        if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
        {
          if( bw.getNuExhibitionWeek() == 1 )
          {
            statusAvailable = this.bookingStatusDAO.get( BookingStatus.CANCELED.getId() );
            status = this.bookingStatusDAO.get( BookingStatus.BOOKED.getId() );
          }
          else
          {
            statusAvailable = this.bookingStatusDAO.get( BookingStatus.TERMINATED.getId() );
            status = this.bookingStatusDAO.get( BookingStatus.CONTINUE.getId() );
          }
        }
        else if( bws.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.TERMINATED.getId() )
        {
          inRemoval = true;
          statusAvailable = this.bookingStatusDAO.get( BookingStatus.CONTINUE.getId() );
          status = this.bookingStatusDAO.get( BookingStatus.TERMINATED.getId() );
        }
        else
        {
          // Las salas canceladas no se muestran
          continue;
        }

        // Se arman las salas

        BookingTheaterTO bookingTheaterTO = new BookingTheaterTO();
        bookingTheaterTO.setInRemoval( inRemoval );
        bookingTheaterTO.setCapacity( bws.getIdScreen().getNuCapacity() );
        if( CollectionUtils.isNotEmpty( bw.getIdBooking().getIdEvent().getEventMovieDOList() ) )
        {
          bookingTheaterTO.setDistributor( bw.getIdBooking().getIdEvent().getEventMovieDOList().get( 0 )
              .getIdDistributor().getDsShortName() );
        }
        bookingTheaterTO.setEventTO( eventTO );
        bookingTheaterTO.setFridayIncome( 0.0 );
        bookingTheaterTO.setSaturdayIncome( 0.0 );
        bookingTheaterTO.setSundayIncome( 0.0 );
        bookingTheaterTO.setId( bw.getIdBooking().getIdBooking() );
        bookingTheaterTO.setScreenTO( new ScreenTO() );
        bookingTheaterTO.getScreenTO().setId( bws.getIdScreen().getIdScreen().longValue() );
        bookingTheaterTO.setPreviousNuScreen( bws.getIdScreen().getNuScreen() );
        bookingTheaterTO.setFormat( getScreenFormat( bws.getIdScreen() ) );
        bookingTheaterTO.setImage( BookingTicketSemaphore.GREEN.getImage() );

        bookingTheaterTO.getScreenTO().setNuScreen( bws.getIdScreen().getNuScreen() );
        bookingTheaterTO.getScreenTO().setOriginalNuScreen( bws.getIdScreen().getNuScreen() );
        bookingTheaterTO.getScreenTO().setNuCapacity( bws.getIdScreen().getNuCapacity() );
        bookingTheaterTO.setStatusName( status.getName() );
        bookingTheaterTO.setStatusTO( status );
        bookingTheaterTO.setTotalIncome( 0.0 );
        bookingTheaterTO.setTotalTickets( 0L );
        bookingTheaterTO.setWeek( bw.getNuExhibitionWeek() );

        bookingTheaterTO.setStatusIdSelected( status.getId().toString() );

        bookingTheaterTO.setShowings( Arrays.asList( BookingShow.FIRST.getShow(), BookingShow.SECOND.getShow(),
          BookingShow.THIRD.getShow(), BookingShow.FOURTH.getShow(), BookingShow.FIVETH.getShow(),
          BookingShow.SIXTH.getShow() ) );
        bookingTheaterTO.setSelectedShowings( new ArrayList<Object>() );
        if( CollectionUtils.isNotEmpty( bws.getBookingWeekScreenShowDOList() ) )
        {
          for( BookingWeekScreenShowDO bwss : bws.getBookingWeekScreenShowDOList() )
          {
            bookingTheaterTO.getSelectedShowings().add( String.valueOf( bwss.getNuShow() ) );
          }
        }
        ScreenTO screen = (ScreenTO) new ScreenDOToScreenTOTransformer().transform( bws.getIdScreen() );
        screen.setBookingStatus( status );

        bookingTheaterTO.setStatusTOs( new ArrayList<CatalogTO>() );
        bookingTheaterTO.getStatusTOs().add( status );
        bookingTheaterTO.getStatusTOs().add( statusAvailable );

        bookingTheaterTO.setNuScreens( extractAvailableScreens( bookingTO, screen, currentWeek ) );

        bookingTheaterTO.setBookingObservationTO( new BookingObservationTO() );
        if( bws.getIdObservation() != null && StringUtils.isNotEmpty( bws.getIdObservation().getDsObservation() ) )
        {
          bookingTheaterTO.getBookingObservationTO().setObservation( bws.getIdObservation().getDsObservation() );
          bookingTheaterTO.setNote( bws.getIdObservation().getDsObservation() );
        }
        else
        {
          bookingTheaterTO.setNote( StringUtils.EMPTY );
          bookingTheaterTO.getBookingObservationTO().setObservation( StringUtils.EMPTY );
        }
        // jcarbajal
        if( CollectionUtils.isNotEmpty( bws.getPresaleDOList() ) )
        {
          bookingTheaterTO.setPresaleTO( (PresaleTO) new PresaleDOToPresaleTOTransformer().transform( bws
              .getPresaleDOList().get( 0 ) ) );
        }
        else
        {
          bookingTheaterTO.setPresaleTO( new PresaleTO( null, false ) );
        }
        screens.add( bookingTheaterTO );
      }
    }
    return screens;
  }

  private String getScreenFormat( ScreenDO screenDO )
  {
    String s = "";

    CategoryDO screenFormat = (CategoryDO) CollectionUtils.find(
      screenDO.getCategoryDOList(),
      PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdCategoryType" ),
        PredicateUtils.equalPredicate( new CategoryTypeDO( CategoryType.SCREEN_FORMAT.getId() ) ) ) );

    if( screenFormat != null && CollectionUtils.isNotEmpty( screenFormat.getCategoryLanguageDOList() ) )
    {
      for( CategoryLanguageDO cl : screenFormat.getCategoryLanguageDOList() )
      {
        if( cl.getIdLanguage().getIdLanguage().intValue() == Language.ENGLISH.getId() )
        {
          s = cl.getDsName();
          break;
        }
      }
    }

    return s;
  }

  private List<BookingTheaterTO> extractBookingZeroScreens( BookingWeekDO bw, boolean terminated )
  {
    List<BookingTheaterTO> zeroScreens = new ArrayList<BookingTheaterTO>();
    CatalogTO status = null;
    CatalogTO statusAvailable = null;
    int limit;
    boolean inRemoval = terminated;
    if( terminated )
    {
      limit = bw.getQtCopyScreenZeroTerminated();
      status = this.bookingStatusDAO.get( BookingStatus.TERMINATED.getId() );
      statusAvailable = this.bookingStatusDAO.get( BookingStatus.CONTINUE.getId() );
    }
    else
    {
      limit = bw.getQtCopyScreenZero();
      if( bw.getNuExhibitionWeek() == 1 )
      {
        statusAvailable = this.bookingStatusDAO.get( BookingStatus.CANCELED.getId() );
        status = this.bookingStatusDAO.get( BookingStatus.BOOKED.getId() );
      }
      else
      {
        statusAvailable = this.bookingStatusDAO.get( BookingStatus.TERMINATED.getId() );
        status = this.bookingStatusDAO.get( BookingStatus.CONTINUE.getId() );
      }
    }
    BookingTO bookingTO = null;
    WeekTO currentWeek = null;
    EventTO eventTO = null;
    for( int i = 0; i < limit; i++ )
    {
      if( i == 0 )
      {
        // Lazy init
        bookingTO = (BookingTO) new BookingDOToBookingTOTransformer().transform( bw.getIdBooking() );
        currentWeek = (WeekTO) new WeekDOToWeekTOTransformer().transform( bw.getIdWeek() );
        currentWeek.setEditable( true );
        eventTO = (EventTO) new EventDOToEventTOTransformer().transform( bw.getIdBooking().getIdEvent() );
      }
      BookingTheaterTO bookingTheaterTO = new BookingTheaterTO();
      bookingTheaterTO.setCapacity( 0 );
      if( CollectionUtils.isNotEmpty( bw.getIdBooking().getIdEvent().getEventMovieDOList() ) )
      {
        bookingTheaterTO.setDistributor( bw.getIdBooking().getIdEvent().getEventMovieDOList().get( 0 )
            .getIdDistributor().getDsShortName() );
      }
      bookingTheaterTO.setEventTO( eventTO );
      bookingTheaterTO.setFridayIncome( 0.0 );
      bookingTheaterTO.setSaturdayIncome( 0.0 );
      bookingTheaterTO.setSundayIncome( 0.0 );
      bookingTheaterTO.setId( bw.getIdBooking().getIdBooking() );
      bookingTheaterTO.setScreenTO( new ScreenTO() );
      bookingTheaterTO.getScreenTO().setId( 0L );
      bookingTheaterTO.setPreviousNuScreen( 0 );
      bookingTheaterTO.setImage( BookingTicketSemaphore.GREEN.getImage() );

      bookingTheaterTO.getScreenTO().setNuScreen( 0 );
      bookingTheaterTO.getScreenTO().setNuCapacity( 0 );
      bookingTheaterTO.setStatusName( status.getName() );
      bookingTheaterTO.setStatusTO( status );
      bookingTheaterTO.setTotalIncome( 0.0 );
      bookingTheaterTO.setTotalTickets( 0L );
      bookingTheaterTO.setWeek( bw.getNuExhibitionWeek() );

      bookingTheaterTO.setStatusIdSelected( status.getId().toString() );
      bookingTheaterTO.setInRemoval( inRemoval );

      bookingTheaterTO.setShowings( Arrays.asList( BookingShow.FIRST.getShow(), BookingShow.SECOND.getShow(),
        BookingShow.THIRD.getShow(), BookingShow.FOURTH.getShow(), BookingShow.FIVETH.getShow(),
        BookingShow.SIXTH.getShow() ) );
      bookingTheaterTO.setSelectedShowings( new ArrayList<Object>() );
      ScreenTO screen = new ScreenTO();
      screen.setBookingStatus( status );
      screen.setNuScreen( 0 );

      bookingTheaterTO.setStatusTOs( new ArrayList<CatalogTO>() );
      bookingTheaterTO.getStatusTOs().add( status );
      bookingTheaterTO.getStatusTOs().add( statusAvailable );

      bookingTheaterTO.setNuScreens( extractAvailableScreens( bookingTO, screen, currentWeek ) );

      bookingTheaterTO.setBookingObservationTO( new BookingObservationTO() );
      bookingTheaterTO.setNote( "" );
      zeroScreens.add( bookingTheaterTO );
      // Preventa
      bookingTheaterTO.setPresaleTO( new PresaleTO( null, false ) );
    }

    return zeroScreens;
  }

  private void addScreensWithoutBooking( TheaterTO theater, List<BookingTheaterTO> bookingTheaterTOs )
  {
    if( theater != null && CollectionUtils.isNotEmpty( theater.getScreens() ) )
    {
      CatalogTO status = this.bookingStatusDAO.get( BookingStatus.BOOKED.getId() );
      for( ScreenTO screen : theater.getScreens() )
      {
        boolean exists = false;
        for( BookingTheaterTO bookingTheaterTO : bookingTheaterTOs )
        {
          if( bookingTheaterTO.getScreenTO().equals( screen )
              && (bookingTheaterTO.getStatusTO().getId().equals( BookingStatus.BOOKED.getIdLong() ) || bookingTheaterTO
                  .getStatusTO().getId().equals( BookingStatus.CONTINUE.getIdLong() )) )
          {
            exists = true;
            break;
          }
        }

        if( !exists )
        {
          BookingTheaterTO bookingTheaterTO = new BookingTheaterTO();
          bookingTheaterTO.setAvailable( true );
          bookingTheaterTO.setCapacity( screen.getNuCapacity() );
          bookingTheaterTO.setEditable( Boolean.TRUE );
          bookingTheaterTO.setFormat( screen.getScreenFormat().getName() );
          bookingTheaterTO.setFridayIncome( 0.0 );
          bookingTheaterTO.setSaturdayIncome( 0.0 );
          bookingTheaterTO.setSundayIncome( 0.0 );
          bookingTheaterTO.setTotalIncome( 0.0 );
          bookingTheaterTO.setTotalTickets( 0L );
          bookingTheaterTO.setNuScreens( Arrays.asList( screen.getNuScreen() ) );
          bookingTheaterTO.setScreenTO( screen );
          bookingTheaterTO.setWeek( null );
          bookingTheaterTO.setEventTO( new EventTO() );
          bookingTheaterTO.getEventTO().setDsEventName( "undefined" );
          bookingTheaterTO.setEvents( new ArrayList<EventTO>() );
          bookingTheaterTO.setShowings( Arrays.asList( BookingShow.FIRST.getShow(), BookingShow.SECOND.getShow(),
            BookingShow.THIRD.getShow(), BookingShow.FOURTH.getShow(), BookingShow.FIVETH.getShow(),
            BookingShow.SIXTH.getShow() ) );
          bookingTheaterTO.setBookingObservationTO( new BookingObservationTO() );
          bookingTheaterTO.setSelectedShowings( new ArrayList<Object>() );
          bookingTheaterTO.setEvents( this.eventDAO.findAvailableEventsByScreen( screen.getId() ) );

          bookingTheaterTO.setImage( BookingTicketSemaphore.GREEN.getImage() );
          bookingTheaterTO.setStatusTO( status );
          bookingTheaterTO.setStatusTOs( Arrays.asList( status ) );
          bookingTheaterTO.setStatusIdSelected( status.getId().toString() );
          bookingTheaterTO.setPresaleTO( new PresaleTO() );

          bookingTheaterTOs.add( bookingTheaterTO );
        }
      }
    }
  }

  private List<Integer> extractAvailableScreens( BookingTO bookingTO, ScreenTO screenTO, WeekTO currentWeek )
  {
    List<Integer> availableScreens = new ArrayList<Integer>();
    // GSE, se agregan las salas terminadas
    if( currentWeek.isEditable()
        && (screenTO.getBookingStatus().getId().equals( BookingStatus.BOOKED.getIdLong() )
            || screenTO.getBookingStatus().getId().equals( BookingStatus.CONTINUE.getIdLong() ) || screenTO
            .getBookingStatus().getId().equals( BookingStatus.TERMINATED.getIdLong() )) )
    {
      availableScreens.add( 0 );
      for( ScreenTO screen : bookingTO.getTheater().getScreens() )
      {
        if( CollectionUtils.isNotEmpty( screen.getMovieFormats() )
            && CollectionUtils.isSubCollection( bookingTO.getEvent().getMovieFormats(), screen.getMovieFormats() ) )
        {
          availableScreens.add( screen.getNuScreen() );
        }
      }
      Collections.sort( availableScreens );
    }
    else
    {
      availableScreens.add( screenTO.getNuScreen() );
    }

    return availableScreens;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> findAllActiveMovies( boolean premiere, boolean festival, boolean prerelease )
  {
    return eventDAO.findAllActiveMovies( premiere, festival, prerelease );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> findAllActiveRegions( AbstractTO abstractTO )
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    AbstractTOUtils.copyElectronicSign( abstractTO, pagingRequestTO );
    pagingRequestTO.setNeedsPaging( false );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( RegionQuery.REGION_ACTIVE, true );
    pagingRequestTO.getFilters().put( RegionQuery.REGION_LANGUAGE_ID, abstractTO.getIdLanguage() );
    PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> responseTO = regionDAO.findAllByPaging( pagingRequestTO );
    List<CatalogTO> catalogTOs = new ArrayList<CatalogTO>();
    CollectionUtils.collect( responseTO.getElements(), new RegionTOToCatalogTOTransformer(), catalogTOs );
    return catalogTOs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EventMovieTO> findAllPremieres()
  {
    // TODO AFU IMPLEMENTACION TEMPORAL. Falta especificar por el cliente el mecanismo para determinar los estrenos.
    // Esta implementación trae todas las películas activas.
    List<EventMovieTO> eventMovieTOs = new ArrayList<EventMovieTO>();

    for( CatalogTO catalogTO : findAllActiveMovies( true, false, false ) )
    {
      EventMovieTO eventMovieTO = new EventMovieTO();
      eventMovieTO.setIdEvent( catalogTO.getId() );
      EventMovieDO eventMovieDO = CinepolisUtils.findFirstElement( eventDAO.find( catalogTO.getId() )
          .getEventMovieDOList() );
      if( eventMovieDO != null )
      {
        eventMovieTO.setIdMovieImage( eventMovieDO.getIdMovieImage() );
        eventMovieTO.setDsEventName( eventMovieDO.getIdEvent().getDsName() );
        eventMovieTO.setPremiere( eventMovieDO.getIdEvent().getFgPremiere() );
      }
      eventMovieTOs.add( eventMovieTO );
    }
    return eventMovieTOs;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PagingResponseTO<TheaterTO> findTheatersByIdWeekAndIdRegion( PagingRequestTO requestTO )
  {
    return bookingDAO.findTheatersByIdWeekAndIdRegion( requestTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PagingResponseTO<BookingTO> findBookingMoviesByTheater( PagingRequestTO pagingRequestTO )
  {
    ValidatorUtil.validatePagingRequestBookingTheater( pagingRequestTO );

    pagingRequestTO.setNeedsPaging( false );
    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );
    List<BookingTO> bookings = response.getElements();

    for( BookingTO booking : bookings )
    {
      booking.setScreensSelected( new ArrayList<Object>() );
      for( ScreenTO screen : booking.getTheater().getScreens() )
      {
        if( screen.getSelected() != null && screen.getSelected() )
        {
          booking.getScreensSelected().add( screen.getId().toString() );
        }
      }
    }

    return response;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WeekTO findWeek( int weekId )
  {
    return this.weekDAO.getWeek( weekId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RegionTO<CatalogTO, CatalogTO> findRegion( int regionId )
  {
    return this.regionDAO.getRegionById( regionId );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendTheaterEmail( BookingTO booking )
  {
    if( validateBookingTheaterScreens( booking ) )
    {
      updateBookingStatusForTheaterWeek( booking );
      this.sendAsynchronousEmails( Arrays.asList( booking ) );
    }
    else
    {
      TheaterDO theater = this.theaterDAO.find( booking.getTheater().getId().intValue() );
      TheaterTO t = (TheaterTO) new TheaterDOToTheaterTOSimpleTransformer( Language.ENGLISH ).transform( theater );
      throw DigitalBookingExceptionBuilder.build(
        DigitalBookingExceptionCode.BOOKING_THEATER_IS_NOT_ASSIGNED_TO_ANY_SCREEN, new Object[] { t.getName() } );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendTheaterEmail( BookingTO booking, Long regionId )
  {
    if( validateBookingTheaterScreens( booking ) )
    {
      updateBookingStatusForTheaterWeek( booking );
      EmailTemplateDO emailTemplateDO = emailTemplateDAO.findByRegionAndEmailType( regionId,
        EmailType.BOOKING_THEATER.getId() );
      if( emailTemplateDO != null )
      {
        booking.setSubject( emailTemplateDO.getDsSubject() );
        booking.setTemplate( emailTemplateDO.getDsBody() );
      }
      this.sendAsynchronousEmails( Arrays.asList( booking ) );
    }
    else
    {
      TheaterDO theater = this.theaterDAO.find( booking.getTheater().getId().intValue() );
      TheaterTO t = (TheaterTO) new TheaterDOToTheaterTOSimpleTransformer( Language.ENGLISH ).transform( theater );
      throw DigitalBookingExceptionBuilder.build(
        DigitalBookingExceptionCode.BOOKING_THEATER_IS_NOT_ASSIGNED_TO_ANY_SCREEN, new Object[] { t.getName() } );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean validateBookingTheaterScreens( BookingTO booking )
  {
    PagingRequestTO request = new PagingRequestTO();
    request.setNeedsPaging( Boolean.FALSE );
    request.setFilters( new HashMap<ModelQuery, Object>() );
    request.getFilters().put( BookingQuery.BOOKING_THEATER_ID, booking.getTheater().getId() );
    request.getFilters().put( BookingQuery.BOOKING_WEEK_ID, booking.getWeek().getIdWeek() );
    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( request );
    boolean isValid = false;
    for( BookingTO bookingTO : response.getElements() )
    {
      if( CollectionUtils.isNotEmpty( bookingTO.getScreens() ) )
      {
        isValid = true;

        int n = 0;
        for( ScreenTO screenTO : bookingTO.getScreens() )
        {
          if( screenTO.getBookingStatus().getId().equals( BookingStatus.BOOKED.getIdLong() ) )
          {
            n++;
          }
        }
        if( n != bookingTO.getCopy() )
        {
          TheaterDO theater = this.theaterDAO.find( bookingTO.getTheater().getId().intValue() );
          TheaterTO t = (TheaterTO) new TheaterDOToTheaterTOSimpleTransformer( Language.ENGLISH ).transform( theater );
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_THEATER_HAS_SCREEN_ZERO,
            new Object[] { t.getName() } );
        }
      }
    }
    return isValid;
  }

  private void updateBookingStatusForTheaterWeek( BookingTO booking )
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setNeedsPaging( false );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_THEATER_ID, booking.getTheater().getId() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, booking.getWeek().getIdWeek() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, Boolean.TRUE );
    PagingResponseTO<BookingTO> response = this.bookingDAO.findAllByPaging( pagingRequestTO );

    if( CollectionUtils.isNotEmpty( response.getElements() ) )
    {
      for( BookingTO bookingTO : response.getElements() )
      {
        if( bookingTO.getStatus().getId().intValue() == BookingStatus.BOOKED.getId() )
        {
          this.bookingWeekDAO.updateSentStatus( bookingTO );
        }
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> findAvailableMovies( Long idWeek, Long idTheater )
  {
    return findAllActiveMovies( false, false, false );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ScreenTO> findTheaterScreens( Long idTheater, Long idEvent )
  {
    ArrayList<ScreenTO> theaterScreens = new ArrayList<ScreenTO>();
    if( idEvent != null && !idEvent.equals( 0L ) )
    {
      CollectionUtils.collect( screenDAO.findAllActiveByIdCinema( idTheater.intValue() ),
        new ScreenDOToScreenTOTransformer(), theaterScreens );
      BookingDAOUtil
          .markSelectedAndDisabledScreens( theaterScreens, new ArrayList<ScreenDO>(), eventDAO.find( idEvent ) );
      BookingDAOUtil.addScreenZero( theaterScreens, 0 );
      Collections.sort( theaterScreens, new ScreenTOComparator() );
    }
    else
    {
      CollectionUtils.collect( screenDAO.findAllActiveByIdCinema( idTheater.intValue() ),
        new ScreenDOToScreenTOTransformer(), theaterScreens );
      for( ScreenTO screenTO : theaterScreens )
      {
        screenTO.setDisabled( true );
      }
      BookingDAOUtil.addScreenZero( theaterScreens, 0 );
      Collections.sort( theaterScreens, new ScreenTOComparator() );
    }
    return theaterScreens;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PagingResponseTO<TheaterTO> findTheatersByBookingWeekAndRegion( PagingRequestTO pagingRequestTO )
  {
    return this.bookingDAO.findTheatersByBookingWeekAndRegion( pagingRequestTO );
  }

  @Override
  public PagingResponseTO<TheaterTO> findTheatersByBookingWeekAndRegionForPresaleReport( PagingRequestTO pagingRequestTO )
  {
    return this.bookingDAO.findTheatersByBookingWeekAndRegionForPresaleReport( pagingRequestTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendTheatersEmail( Long idWeek, List<TheaterTO> theaters, String template, String subject )
  {
    List<BookingTO> bookings = new ArrayList<BookingTO>();
    for( TheaterTO theater : theaters )
    {
      BookingTO booking = new BookingTO();
      AbstractTOUtils.copyElectronicSign( theater, booking );
      booking.setTheater( theater );
      booking.setWeek( new WeekTO( idWeek.intValue() ) );
      booking.setTemplate( template );
      booking.setSubject( subject );
      bookings.add( booking );
      updateBookingStatusForTheaterWeek( booking );
    }
    sendAsynchronousEmails( bookings );

  }

  private void sendAsynchronousEmails( List<BookingTO> bookings )
  {
    LOG.info( "Inicia la carga de correos" );
    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    ThreadPoolExecutor executorPool = new ThreadPoolExecutor( 2, bookings.size() < 2 ? 2 : bookings.size(), 30,
        TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>( 2 ), threadFactory );
    for( BookingTO booking : bookings )
    {
      TheaterEmailSenderWorker worker = new TheaterEmailSenderWorker();
      worker.setUser( getUser( booking.getUserId() ) );
      worker.setEmailSenderEJB( emailSenderEJB );
      worker.setConfigurationDAO( configurationDAO );
      worker.setServiceReportsEJB( serviceReportsEJB );
      worker.setTheaterDAO( theaterDAO );
      worker.setWeekDAO( weekDAO );
      worker.setReportDAO( reportDAO );
      worker.setIdTheater( booking.getTheater().getId() );
      worker.setIdWeek( booking.getWeek().getIdWeek() );
      if( booking.getTemplate() != null && !booking.getTemplate().isEmpty() )
      {
        worker.setTemplate( booking.getTemplate() );
      }
      worker.setSubject( booking.getSubject() );
      executorPool.execute( worker );
    }
    LOG.info( "Se termino de cargar los threads" );
  }

  private UserDO getUser( Long userId )
  {
    return this.userDAO.find( userId.intValue() );
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.service.book.BookingServiceEJB#getTemplateByTheater()
   */
  @Override
  public String getTemplateByTheater()
  {
    return this.configurationDAO.findByParameterName( Configuration.EMAIL_BOOKING_THEATER_BODY.getParameter() )
        .getDsValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EmailTemplateTO getEmailTemplateTheater( WeekTO weekTO, Long regionId )
  {
    EmailTemplateDO emailTemplateDO = emailTemplateDAO.findByRegionAndEmailType( regionId,
      EmailType.BOOKING_THEATER.getId() );
    return buildEmailTemplateDO( weekTO, emailTemplateDO );
  }

  private EmailTemplateTO buildEmailTemplateDO( WeekTO weekTO, EmailTemplateDO emailTemplateDO )
  {
    EmailTemplateTO to = null;
    if( emailTemplateDO != null )
    {
      to = new EmailTemplateTO();
      to.setId( emailTemplateDO.getIdEmailTemplate() );
      to.setSubject( fillTemplatePlaceHolders( weekTO, emailTemplateDO.getDsSubject() ) );
      to.setBody( fillTemplatePlaceHolders( weekTO, emailTemplateDO.getDsBody() ) );
    }
    return to;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getEmailTemplateRegion( WeekTO weekTO )
  {
    String emailTemplate = this.configurationDAO.findByParameterName(
      Configuration.EMAIL_BOOKING_REGION_BODY.getParameter() ).getDsValue();
    return fillTemplatePlaceHolders( weekTO, emailTemplate );
  }

  private String fillTemplatePlaceHolders( WeekTO weekTO, String emailTemplate )
  {
    DateFormat df = new SimpleDateFormat( STANDARD_DATE_FORMAT );
    WeekTO week = this.findWeek( weekTO.getIdWeek() );
    emailTemplate = emailTemplate.replace( WEEK_NUMBER_PLACEHOLDER, String.valueOf( week.getNuWeek() ) );
    emailTemplate = emailTemplate.replace( START_DAY_PLACEHOLDER, df.format( week.getStartingDayWeek() ) );
    emailTemplate = emailTemplate.replace( END_DAY_PLACEHOLDER, df.format( week.getFinalDayWeek() ) );
    return emailTemplate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public EmailTemplateTO getEmailTemplateRegion( WeekTO weekTO, Long regionId )
  {
    EmailTemplateDO emailTemplateDO = emailTemplateDAO.findByRegionAndEmailType( regionId,
      EmailType.BOOKING_REGION.getId() );
    return buildEmailTemplateDO( weekTO, emailTemplateDO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendRegionEmail( RegionEmailTO regionEmailTO )
  {
    Long weekId = regionEmailTO.getIdWeek();
    Long regionId = regionEmailTO.getIdRegion();
    String template = regionEmailTO.getMessage();
    String subject = regionEmailTO.getSubject();
    Document doc = Jsoup.parse( template );
    String templateCleaned = doc.outerHtml();

    LOG.info( templateCleaned );

    LOG.info( "Inicia la carga de correos" );
    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    ThreadPoolExecutor executorPool = new ThreadPoolExecutor( 2, 10, 30, TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>( 2 ), threadFactory );
    RegionEmailSenderWorker worker = new RegionEmailSenderWorker();
    worker.setUser( this.getUser( regionEmailTO.getUserId() ) );
    worker.setEmailSenderEJB( emailSenderEJB );
    worker.setConfigurationDAO( configurationDAO );
    worker.setServiceReportsEJB( serviceReportsEJB );
    worker.setRegionDAO( regionDAO );
    worker.setWeekDAO( weekDAO );
    worker.setIdRegion( regionId );
    worker.setIdWeek( weekId.intValue() );
    worker.setMessage( templateCleaned );
    worker.setSubject( subject );
    worker.setAttachments( regionEmailTO.getFiles() );
    executorPool.execute( worker );
    LOG.info( "Se termino de cargar los threads" );

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<EventTO> findEventsForScreen( ScreenTO screenTO )
  {
    return this.eventDAO.findAvailableEventsByScreen( screenTO.getId() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CatalogTO> getBookingStatus( List<Long> statusId )
  {
    List<CatalogTO> status = new ArrayList<CatalogTO>();
    for( Long id : statusId )
    {
      status.add( this.bookingStatusDAO.get( id.intValue() ) );
    }
    return status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WeekTO findFollowingWeekBooking( Long idBooking, Integer idWeek )
  {
    WeekTO followingWeekTO = null;
    WeekDO weekDO = weekDAO.find( idWeek );
    BookingWeekDO followingWeekBooking = CinepolisUtils.findFirstElement( bookingWeekDAO.findfollowingWeekBooking(
      idBooking, weekDO.getDtStartingDayWeek() ) );
    if( followingWeekBooking != null
        && followingWeekBooking.getIdBookingStatus().getIdBookingStatus().intValue() == BookingStatus.BOOKED.getId() )
    {
      WeekDOToWeekTOTransformer transformer = new WeekDOToWeekTOTransformer();
      Object transformedObj = transformer.transform( followingWeekBooking.getIdWeek() );
      if( transformedObj instanceof WeekTO )
      {
        followingWeekTO = (WeekTO) transformedObj;
      }
    }
    return followingWeekTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cancelOrTeminateBookings( List<BookingTO> bookingsForCancellation, boolean isCancellation )
  {
    for( BookingTO bookingTO : bookingsForCancellation )
    {
      BookingDO bookingDO = bookingDAO.find( bookingTO.getId() );
      bookingDO.setFgBooked( false );
      AbstractEntityUtils.applyElectronicSign( bookingDO, bookingTO );
      bookingDAO.edit( bookingDO );

      BookingWeekDO bookingWeekDO = CinepolisUtils.findFirstElement( bookingWeekDAO.findByBookingAndWeek(
        bookingTO.getId(), bookingTO.getWeek().getIdWeek() ) );
      if( bookingWeekDO != null )
      {
        bookingWeekDO.setQtCopy( 0 );
        BookingStatusDO bookingStatusDO = isCancellation ? bookingStatusDAO.find( BookingStatus.CANCELED.getId() )
            : bookingStatusDAO.find( BookingStatus.TERMINATED.getId() );
        bookingWeekDO.setIdBookingStatus( bookingStatusDO );
        for( BookingWeekScreenDO bookingWeekScreenDO : bookingWeekDO.getBookingWeekScreenDOList() )
        {
          bookingWeekScreenDO.setIdBookingStatus( bookingStatusDO );
        }
        AbstractEntityUtils.applyElectronicSign( bookingWeekDO, bookingTO );
        bookingWeekDAO.edit( bookingWeekDO );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBookingWeekSent( BookingTO bookingTO )
  {
    boolean isSent = false;
    BookingWeekDO bookingWeekDO = CinepolisUtils.findFirstElement( bookingWeekDAO.findByBookingAndWeek(
      bookingTO.getId(), bookingTO.getWeek().getIdWeek() ) );
    if( bookingWeekDO != null )
    {
      isSent = bookingWeekDO.isFgSend();
    }
    return isSent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasBookingWeek( BookingTO bookingTO )
  {
    return CollectionUtils.isNotEmpty( bookingWeekDAO.findByBookingAndWeek( bookingTO.getId(), bookingTO.getWeek()
        .getIdWeek() ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Long countPreReleaseBookingBooked( EventMovieTO eventMovieTO )
  {
    return this.bookingDAO.countPrereleaseBooked( eventMovieTO.getIdEvent() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getTheatersInRegion( Long idRegion )
  {
    return this.theaterDAO.getNumberOfTheatersByRegion( idRegion ).size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Long> findTheatersByWeekEventAndRegion( Long idWeek, Long idEvent, Long idRegion )
  {
    return this.bookingDAO.findTheatersByWeekEventAndRegion( idWeek, idEvent, idRegion );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Long> findEventsByWeekAndTheater( Long idWeek, Long idTheater )
  {
    return this.bookingDAO.findEventsByWeekAndTheater( idWeek, idTheater );
  }

}
