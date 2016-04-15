package mx.com.cinepolis.digital.booking.service.bookingspecialevent.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventBookingTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenShowTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.AbstractTOUtils;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.dao.util.PresaleTOToPresaleDOTransformer;
import mx.com.cinepolis.digital.booking.model.BookingDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenShowDO;
import mx.com.cinepolis.digital.booking.model.BookingStatusDO;
import mx.com.cinepolis.digital.booking.model.ObservationDO;
import mx.com.cinepolis.digital.booking.model.PresaleDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;
import mx.com.cinepolis.digital.booking.model.SpecialEventWeekDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.model.utils.AbstractEntityUtils;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingSpecialEventScreenShowDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingStatusDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ObservationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.PresaleDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ReportDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ScreenDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.SpecialEventWeekDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.UserDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;
import mx.com.cinepolis.digital.booking.service.bookingspecialevent.BookingSpecialEventServiceEJB;
import mx.com.cinepolis.digital.booking.service.reports.ServiceReportsEJB;
import mx.com.cinepolis.digital.booking.service.util.EmailSenderEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;
import mx.com.cinepolis.digital.booking.service.util.RegionEmailSenderPresaleWorker;
import mx.com.cinepolis.digital.booking.service.util.TheaterEmailSenderPresaleWorker;
import mx.com.cinepolis.digital.booking.service.util.ValidatorUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que implementa los servicios para la programacion de eventos especiales
 * 
 * @author jreyesv
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class BookingSpecialEventServiceEJBImpl implements BookingSpecialEventServiceEJB
{

  @EJB
  private BookingStatusDAO bookingStatusDAO;

  @EJB
  private BookingSpecialEventDAO bookingSpecialEventDAO;

  @EJB
  private BookingDAO bookingDAO;

  @EJB
  private BookingSpecialEventScreenDAO bookingSpecialEventScrenDAO;
  @EJB
  private BookingSpecialEventScreenShowDAO bookingSpecialEScreenShowDAO;
  @EJB
  private ScreenDAO screenDAO;

  @EJB
  private ObservationDAO observationDAO;

  @EJB
  private UserDAO userDAO;

  @EJB
  private SpecialEventWeekDAO specialEventWeekDAO;

  @EJB
  private EmailSenderEJB emailSenderEJB;

  @EJB
  private ConfigurationDAO configurationDAO;

  @EJB
  private ServiceReportsEJB serviceReportsEJB;

  @EJB
  private WeekDAO weekDAO;

  @EJB
  private RegionDAO regionDAO;
  @EJB
  private ReportDAO reportDAO;
  @EJB
  private TheaterDAO theaterDAO;
  @EJB
  private PresaleDAO presaleDAO;

  private static final Logger LOG = LoggerFactory.getLogger( BookingSpecialEventServiceEJBImpl.class );
  /**
   * Variable de apoyo aplicable en pruebas
   */
  private int savedBookings = 0;

  /**
   * {@inheritDoc}
   */
  @Override
  public void saveOrUpdateBookings( List<SpecialEventTO> specialEventTOs )
  {
    if( CollectionUtils.isNotEmpty( specialEventTOs ) )
    {
      for( SpecialEventTO specialEventTO : specialEventTOs )
      {
        ValidatorUtil.validateSpecialEventBooking( specialEventTO );
        if( specialEventTO.getId() == null )
        {
          // Se crea el bookingTO asociado al evento especial
          BookingTO bookingTO = new BookingTO();
          bookingTO.setEvent( specialEventTO.getEvent() );
          bookingTO.setTheater( specialEventTO.getTheater() );
          bookingTO.setIdBookingType( specialEventTO.getIdBookingType().intValue() );
          bookingTO.setSpecialEvents( new ArrayList<SpecialEventTO>() );
          // Se crea la lista de salas con sus respectivas funciones seleccionadas
          this.createScreens( specialEventTO );
          bookingTO.getSpecialEvents().add( specialEventTO );
          this.bookingSpecialEventDAO.saveBookingSpecialEvent( bookingTO );
          StringBuilder mensaje = new StringBuilder( "Se crea el nuevo registro de programación" );
          LOG.debug( mensaje.toString() );
          this.savedBookings++;
        }
        else
        {
          // Se edita el registro existente
          BookingTO bookingTO = this.createBooking( specialEventTO );
          this.editBooking( bookingTO, specialEventTO );
          StringBuilder mensaje = new StringBuilder( "Se edita el registro de programación existente" );
          LOG.debug( mensaje.toString() );
          this.savedBookings++;
        }
      }
    }
  }

  private void createScreens( SpecialEventTO specialEventTO )
  {
    specialEventTO.setSpecialEventScreens( new ArrayList<SpecialEventScreenTO>() );
    for( ScreenTO screenTO : specialEventTO.getScreens() )
    {
      if( screenTO.getId() > 0 )
      {
        SpecialEventScreenTO specialEventScreenTO = new SpecialEventScreenTO();
        specialEventScreenTO.setIdScreen( screenTO.getId() );
        specialEventScreenTO.setStatus( specialEventTO.getStatus() );
        specialEventScreenTO.setPresaleTO( screenTO.getPresaleTO() );
        if( CollectionUtils.isNotEmpty( specialEventTO.getShowingsSelected() ) )
        {
          specialEventScreenTO.setSpecialEventScreenShows( new ArrayList<SpecialEventScreenShowTO>() );
          for( Object showing : specialEventTO.getShowingsSelected() )
          {
            SpecialEventScreenShowTO screenShow = new SpecialEventScreenShowTO();
            screenShow.setNuShow( Integer.parseInt( showing.toString() ) );
            specialEventScreenTO.getSpecialEventScreenShows().add( screenShow );
          }
        }
        specialEventTO.getSpecialEventScreens().add( specialEventScreenTO );
      }
    }
  }

  private void editBooking( BookingTO bookingTO, SpecialEventTO specialEventTO )
  {
    boolean booked = isCurrentBookingBooked( bookingTO );
    if( booked )
    {
      BookingDO bookingDO = getBookingDO( bookingTO, true );
      BookingSpecialEventDO specialEventDO = getBookingSpecialEventDO( bookingDO, specialEventTO );
      if( specialEventDO == null )
      {
        specialEventDO = new BookingSpecialEventDO();
        specialEventDO.setIdBooking( bookingDO );
        specialEventDO.setBookingSpecialEventScreenDOList( new ArrayList<BookingSpecialEventScreenDO>() );
        specialEventDO.setDtEndSpecialEvent( specialEventTO.getFinalDay() );
        specialEventDO.setDtStartSpecialEvent( specialEventTO.getStartDay() );
        specialEventDO.setFgActive( specialEventTO.getDisabled() );//
        specialEventDO.setIdBookingStatus( this.bookingStatusDAO.find( BookingStatus.BOOKED.getId() ) );
        AbstractEntityUtils.applyElectronicSign( specialEventDO, specialEventTO );
        this.bookingSpecialEventDAO.create( specialEventDO );
      }
      specialEventDO.setDtEndSpecialEvent( specialEventTO.getFinalDay() );
      specialEventDO.setDtStartSpecialEvent( specialEventTO.getStartDay() );
      specialEventDO.setFgShowDate( specialEventTO.isShowDate() );
      specialEventDO.setIdBookingStatus( this.bookingStatusDAO.find( BookingStatus.BOOKED.getId() ) );
      specialEventDO.setQtCopy( bookingTO.getCopy() );
      specialEventDO.setQtCopyScreenZero( bookingTO.getCopyScreenZero() );
      specialEventDO.setQtCopyScreenZeroTerminated( bookingTO.getCopyScreenZeroTerminated() );
      AbstractEntityUtils.applyElectronicSign( specialEventDO, specialEventTO );

      this.bookingSpecialEventDAO.edit( specialEventDO );
      this.editScreens( specialEventDO, bookingTO, specialEventTO );
      this.editSpecialEventWeeks( specialEventDO, specialEventTO );

    }

  }

  @SuppressWarnings("unchecked")
  private void editScreens( BookingSpecialEventDO specialEventDO, BookingTO bookingTO, SpecialEventTO specialEventTO )
  {
    TheaterDO theaterDO = specialEventDO.getIdBooking().getIdTheater();

    List<ScreenDO> screensChanged = this.extractScreensForExchange( bookingTO, specialEventDO );

    List<ScreenDO> zeroScreensExchanged = this.extractZeroScreensExchanged( bookingTO, specialEventDO, screensChanged );

    List<ScreenDO> screensForBooking = (List<ScreenDO>) CollectionUtils.subtract(
      this.extractScreens( bookingTO, Arrays.asList( BookingStatus.BOOKED ), theaterDO ), screensChanged );

    List<ScreenDO> screensForCancel = (List<ScreenDO>) CollectionUtils.subtract(
      extractScreens( bookingTO, Arrays.asList( BookingStatus.CANCELED, BookingStatus.TERMINATED ), theaterDO ),
      zeroScreensExchanged );

    List<BookingSpecialEventScreenDO> screensAlreadyBooked = (List<BookingSpecialEventScreenDO>) CollectionUtils
        .select( specialEventDO.getBookingSpecialEventScreenDOList(), PredicateUtils.transformedPredicate(
          TransformerUtils.invokerTransformer( "getIdBookingStatus" ),
          PredicateUtils.equalPredicate( new BookingStatusDO( BookingStatus.BOOKED.getId() ) ) ) );

    List<BookingSpecialEventScreenDO> bookingSpecialEventScreensForRemoval = new ArrayList<BookingSpecialEventScreenDO>();

    for( BookingSpecialEventScreenDO bookingSpecialEventScreenDO : screensAlreadyBooked )
    {
      ScreenDO screen = bookingSpecialEventScreenDO.getIdScreen();
      boolean screensChangedAndExchanged = !screensChanged.contains( screen )
          && !zeroScreensExchanged.contains( screen );
      if( screensChangedAndExchanged && !screensForBooking.contains( screen ) && !screensForCancel.contains( screen )
          && !bookingSpecialEventScreensForRemoval.contains( bookingSpecialEventScreenDO ) )
      {
        bookingSpecialEventScreensForRemoval.add( bookingSpecialEventScreenDO );
      }
    }

    this.cancelBookingSpecialEventScreen( specialEventDO, screensForCancel );
    this.bookBookingSpecialEventScreen( specialEventDO, screensForBooking, specialEventTO );
    this.removeBookingSpecialEventScreens( bookingSpecialEventScreensForRemoval );

  }

  private void removeBookingSpecialEventScreens( List<BookingSpecialEventScreenDO> bookingSpecialEventScreensForRemoval )
  {
    for( BookingSpecialEventScreenDO bSEEs : bookingSpecialEventScreensForRemoval )
    {
      ScreenDO screenDO = this.screenDAO.find( bSEEs.getIdScreen().getIdScreen() );
      screenDO.getBookingSpecialEventScreenDOList().remove( bSEEs );

      if( CollectionUtils.isNotEmpty( bSEEs.getBookingSpecialEventScreenShowDOList() ) )
      {
        List<BookingSpecialEventScreenShowDO> bwssInRemoval = new ArrayList<BookingSpecialEventScreenShowDO>();
        bwssInRemoval.addAll( bSEEs.getBookingSpecialEventScreenShowDOList() );
        for( BookingSpecialEventScreenShowDO bSEESs : bwssInRemoval )
        {
          bSEEs.getBookingSpecialEventScreenShowDOList().remove( bSEESs );
          this.bookingSpecialEScreenShowDAO.remove( bSEESs );

        }
      }
      BookingSpecialEventDO bSE = bSEEs.getIdBookingSpecialEvent();
      bSE.getBookingSpecialEventScreenDOList().remove( bSEEs );
      this.bookingSpecialEventDAO.edit( bSE );
      this.bookingSpecialEventScrenDAO.remove( bSEEs );
    }
  }

  private void updateShows( BookingSpecialEventScreenDO bookingSpecialEventScreenDO, SpecialEventTO specialEventTO )
  {
    if( CollectionUtils.isNotEmpty( bookingSpecialEventScreenDO.getBookingSpecialEventScreenShowDOList() ) )
    {
      List<BookingSpecialEventScreenShowDO> showsToDelete = new ArrayList<BookingSpecialEventScreenShowDO>();
      showsToDelete.addAll( bookingSpecialEventScreenDO.getBookingSpecialEventScreenShowDOList() );
      for( BookingSpecialEventScreenShowDO showD : showsToDelete )
      {
        bookingSpecialEventScreenDO.getBookingSpecialEventScreenShowDOList().remove( showD );
        this.bookingSpecialEventScrenDAO.edit( bookingSpecialEventScreenDO );
        this.bookingSpecialEScreenShowDAO.remove( showD );
        this.bookingSpecialEScreenShowDAO.flush();
      }
    }
    if( CollectionUtils.isNotEmpty( specialEventTO.getShowingsSelected() ) )
    {
      createShows( bookingSpecialEventScreenDO, specialEventTO );
    }
  }

  private void editSpecialEventWeeks( BookingSpecialEventDO specialEventDO, SpecialEventTO specialEventTO )
  {
    if( CollectionUtils.isNotEmpty( specialEventDO.getSpecialEventWeekDOList() ) )
    {
      List<SpecialEventWeekDO> weeksToDelete = new ArrayList<SpecialEventWeekDO>();
      weeksToDelete.addAll( specialEventDO.getSpecialEventWeekDOList() );
      for( SpecialEventWeekDO weekD : weeksToDelete )
      {
        weekD.setFgActive( false );
        this.specialEventWeekDAO.edit( weekD );
        this.specialEventWeekDAO.flush();
      }
    }
    this.createSpecialEventWeek( specialEventDO, specialEventTO );
  }

  private void createShows( BookingSpecialEventScreenDO bookingSpecialEventScreenDO, SpecialEventTO specialEventTO )
  {
    List<BookingSpecialEventScreenShowDO> showsToPut = new ArrayList<BookingSpecialEventScreenShowDO>();
    for( Object show : specialEventTO.getShowingsSelected() )
    {
      Object showC = (Object) show;
      BookingSpecialEventScreenShowDO showToPut = new BookingSpecialEventScreenShowDO();
      showToPut.setIdBookingSpecialEventScreen( bookingSpecialEventScreenDO );
      showToPut.setNuShow( Integer.valueOf( showC.toString() ) );
      showsToPut.add( showToPut );
      this.bookingSpecialEScreenShowDAO.create( showToPut );
    }
    bookingSpecialEventScreenDO.setBookingSpecialEventScreenShowDOList( showsToPut );
    this.bookingSpecialEventScrenDAO.edit( bookingSpecialEventScreenDO );
  }

  /**
   * Method that creates or updates the elements from a {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO}
   * list for a special event booking.
   * 
   * @param specialEventDO, with the {@link mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO} information.
   * @param screensForBooking, with the list of {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} for
   *          booking.
   * @param specialEventTO, with the {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO} information.
   */
  private void bookBookingSpecialEventScreen( BookingSpecialEventDO specialEventDO, List<ScreenDO> screensForBooking,
      SpecialEventTO specialEventTO )
  {
    BookingStatusDO status = this.bookingStatusDAO.find( BookingStatus.BOOKED.getId() );

    for( ScreenDO screenForBooking : screensForBooking )
    {
      BookingSpecialEventScreenDO bSEEs = this.getSpecialEventScreenDO( screenForBooking, specialEventDO );
      /* jreyesv: Se obtiene la sala que contiene la información actualizada. */
      ScreenTO screenTO = this.extractScreenTO( specialEventTO, screenForBooking.getIdScreen().longValue() );
      if( bSEEs != null )
      {
        bSEEs.setIdBookingStatus( status );
        bSEEs.setIdObservation( createObservation( bSEEs, specialEventTO ) );
        /* jreyesv: Se valida que exista una preventa para la sala de evento especial. */
        if( CollectionUtils.isNotEmpty( bSEEs.getPresaleDOList() ) )
        {
          /* jreyesv: Se actualizan los datos de preventa para la sala de evento especial. */
          this.updatePresaleForScreen( bSEEs.getPresaleDOList().get( 0 ), screenTO );
        }
        else if( screenTO.getPresaleTO().isFgActive() )
        {
          /* jreyesv: Se crea y asocia una preventa para la sala de evento especial. */
          this.createPresaleForScreen( bSEEs, screenTO );
        }
        this.bookingSpecialEventScrenDAO.edit( bSEEs );
      }
      else
      {
        bSEEs = new BookingSpecialEventScreenDO();
        bSEEs.setIdBookingSpecialEvent( specialEventDO );
        bSEEs.setIdScreen( screenForBooking );
        bSEEs.setIdBookingStatus( status );
        bSEEs.setIdObservation( createObservation( bSEEs, specialEventTO ) );
        if( screenTO.getPresaleTO().isFgActive() )
        {
          /* jreyesv: Se crea y asocia una preventa para la sala de evento especial. */
          this.createPresaleForScreen( bSEEs, screenTO );
        }
        this.createBookingSpecialEventScreenDO( bSEEs, specialEventDO );
      }
      this.updateShows( bSEEs, specialEventTO );
    }
  }

  /**
   * Method that extracts a {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} object from a
   * {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} list.
   * 
   * @param specialEventTO, with the {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO} information.
   * @param idScreenDO, a {@link java.lang.Long} with the screen identifier.
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} object.
   * @author jreyesv
   */
  private ScreenTO extractScreenTO( SpecialEventTO specialEventTO, Long idScreenDO )
  {
    ScreenTO screenTOResponse = null;
    if( CollectionUtils.isNotEmpty( specialEventTO.getScreens() ) )
    {
      for( ScreenTO screenTO : specialEventTO.getScreens() )
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
   * {@link mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO}.
   * 
   * @param bookingSpecialEventScreenDO, with the special event screen information.
   * @param screenTO, with the screen information.
   * @author jreyesv
   */
  private void createPresaleForScreen( BookingSpecialEventScreenDO bookingSpecialEventScreenDO, ScreenTO screenTO )
  {
    bookingSpecialEventScreenDO.setPresaleDOList( new ArrayList<PresaleDO>() );
    bookingSpecialEventScreenDO.getPresaleDOList().add(
      (PresaleDO) new PresaleTOToPresaleDOTransformer().transform( screenTO.getPresaleTO() ) );
    bookingSpecialEventScreenDO.getPresaleDOList().get( 0 )
        .setIdBookingSpecialEventScreen( bookingSpecialEventScreenDO );
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
    presaleDO.setDtStartDayPresale( screenTO.getPresaleTO().getDtStartDayPresale() );
    presaleDO.setDtFinalDayPresale( screenTO.getPresaleTO().getDtFinalDayPresale() );
    presaleDO.setDtReleaseDay( screenTO.getPresaleTO().getDtReleaseDay() );
    presaleDO.setFgActive( screenTO.getPresaleTO().isFgActive() );
  }

  private ObservationDO createObservation( BookingSpecialEventScreenDO bSEEs, SpecialEventTO specialEventTO )
  {
    ObservationDO observationDO = bSEEs.getIdObservation();

    if( observationDO != null )
    {
      if( specialEventTO.getNotes() != null )
      {
        observationDO.setDsObservation( specialEventTO.getNotes() );
      }
      else
      {
        observationDO.setDsObservation( " " );
      }
      this.observationDAO.edit( observationDO );
    }
    else
    {
      observationDO = new ObservationDO();
      if( specialEventTO.getNotes() == null )
      {
        observationDO.setDsObservation( " " );
      }
      else
      {
        observationDO.setDsObservation( specialEventTO.getNotes() );
      }
      observationDO.setFgActive( true );
      observationDO.setIdUser( this.userDAO.find( specialEventTO.getUserId().intValue() ) );
      AbstractEntityUtils.applyElectronicSign( observationDO, specialEventTO );
      this.observationDAO.create( observationDO );
    }
    return observationDO;
  }

  private BookingSpecialEventScreenDO getSpecialEventScreenDO( ScreenDO screenDO, BookingSpecialEventDO specialEventDO )
  {
    BookingSpecialEventScreenDO specialEventScreenDO = null;
    if( CollectionUtils.isNotEmpty( specialEventDO.getBookingSpecialEventScreenDOList() ) )
    {
      for( BookingSpecialEventScreenDO bookingSpecialEventDO : specialEventDO.getBookingSpecialEventScreenDOList() )
      {
        if( screenDO.getIdScreen().equals( bookingSpecialEventDO.getIdScreen().getIdScreen() ) )
        {
          specialEventScreenDO = bookingSpecialEventDO;
          break;
        }
      }
    }
    return specialEventScreenDO;
  }

  private void cancelBookingSpecialEventScreen( BookingSpecialEventDO specialEventDO, List<ScreenDO> screensForCancel )
  {
    BookingStatusDO status = this.bookingStatusDAO.find( BookingStatus.BOOKED.getId() );

    for( ScreenDO screenForCancel : screensForCancel )
    {
      BookingSpecialEventScreenDO bSEEs = (BookingSpecialEventScreenDO) CollectionUtils.find(
        specialEventDO.getBookingSpecialEventScreenDOList(),
        PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getIdScreen" ),
          PredicateUtils.equalPredicate( screenForCancel ) ) );

      if( bSEEs != null )
      {
        bSEEs.setIdBookingStatus( status );
        if( CollectionUtils.isNotEmpty( bSEEs.getPresaleDOList() ) )
        {
          bSEEs.getPresaleDOList().get( 0 ).setFgActive( Boolean.FALSE );
        }
        this.bookingSpecialEventScrenDAO.edit( bSEEs );
      }
      else
      {
        bSEEs = new BookingSpecialEventScreenDO();
        bSEEs.setIdBookingSpecialEvent( specialEventDO );
        bSEEs.setIdScreen( screenForCancel );
        bSEEs.setIdBookingStatus( status );
        createBookingSpecialEventScreenDO( bSEEs, specialEventDO );
      }
    }
  }

  private void createBookingSpecialEventScreenDO( BookingSpecialEventScreenDO bSEEs,
      BookingSpecialEventDO bookingSpecialEventDO )
  {
    this.bookingSpecialEventScrenDAO.create( bSEEs );
    bookingSpecialEventDO.getBookingSpecialEventScreenDOList().add( bSEEs );
    this.bookingSpecialEventDAO.edit( bookingSpecialEventDO );
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

          ScreenDO screenDO = this.extractScreenDO( theater, screen );
          if( screenDO != null && !list.contains( screenDO ) )
          {
            list.add( screenDO );
          }
        }
      }
    }
    return list;
  }

  private List<ScreenDO> extractZeroScreensExchanged( BookingTO bookingTO, BookingSpecialEventDO specialEventDO,
      List<ScreenDO> screensExchanged )
  {
    TheaterDO theater = specialEventDO.getIdBooking().getIdTheater();
    // "Elimina" las salas que se mueven de sala numerada a Zero
    List<ScreenDO> screensZeroExchanged = new ArrayList<ScreenDO>();
    if( CollectionUtils.isNotEmpty( bookingTO.getNuScreenToZero() ) )
    {
      for( Integer screenNumber : bookingTO.getNuScreenToZero() )
      {
        ScreenTO screen = new ScreenTO();
        screen.setNuScreen( screenNumber );
        ScreenDO screenDO = extractScreenDO( theater, screen );
        if( !screensExchanged.contains( screenDO ) )
        {
          BookingSpecialEventScreenDO bSEEInRemoval = null;
          for( BookingSpecialEventScreenDO bSEEs : specialEventDO.getBookingSpecialEventScreenDOList() )
          {
            if( bSEEs.getIdScreen().equals( screenDO ) )
            {
              bSEEInRemoval = bSEEs;
              break;
            }
          }
          if( bSEEInRemoval != null )
          {
            removeBookinSpecialEventScreen( specialEventDO, screensZeroExchanged, screenDO, bSEEInRemoval );
          }
        }
      }
    }

    return screensZeroExchanged;
  }

  private void removeBookinSpecialEventScreen( BookingSpecialEventDO specialEventDO,
      List<ScreenDO> screensZeroExchanged, ScreenDO screenDO, BookingSpecialEventScreenDO bSEEInRemoval )
  {
    specialEventDO.getBookingSpecialEventScreenDOList().remove( bSEEInRemoval );
    this.bookingSpecialEventDAO.edit( specialEventDO );
    if( CollectionUtils.isNotEmpty( bSEEInRemoval.getBookingSpecialEventScreenShowDOList() ) )
    {
      List<BookingSpecialEventScreenShowDO> bwssInRemoval = new ArrayList<BookingSpecialEventScreenShowDO>();
      bwssInRemoval.addAll( bSEEInRemoval.getBookingSpecialEventScreenShowDOList() );
      for( BookingSpecialEventScreenShowDO bSEESs : bwssInRemoval )
      {
        bSEEInRemoval.getBookingSpecialEventScreenShowDOList().remove( bSEESs );
        this.bookingSpecialEScreenShowDAO.remove( bSEESs );
      }
    }
    this.bookingSpecialEventScrenDAO.remove( bSEEInRemoval );
    screensZeroExchanged.add( screenDO );
  }

  private List<ScreenDO> extractScreensForExchange( BookingTO bookingTO, BookingSpecialEventDO specialEventDO )
  {
    TheaterDO theater = specialEventDO.getIdBooking().getIdTheater();
    List<ScreenDO> screensExchanged = new ArrayList<ScreenDO>();
    if( CollectionUtils.isNotEmpty( bookingTO.getScreens() ) )
    {
      for( ScreenTO screen : bookingTO.getScreens() )
      {
        if( screen.getOriginalNuScreen() != null && !screen.getOriginalNuScreen().equals( 0 )
            && !screen.getOriginalNuScreen().equals( screen.getNuScreen() ) )
        {
          ScreenTO screenLast = new ScreenTO();
          screenLast.setNuScreen( screen.getOriginalNuScreen() );
          ScreenDO screenDOLast = extractScreenDO( theater, screenLast );
          ScreenDO screenDONew = extractScreenDO( theater, screen );

          for( BookingSpecialEventScreenDO bSES : specialEventDO.getBookingSpecialEventScreenDOList() )
          {
            if( bSES.getIdScreen().equals( screenDOLast ) )
            {
              screenDOLast.getBookingSpecialEventScreenDOList().remove( bSES );
              bSES.setIdScreen( screenDONew );
              this.bookingSpecialEventScrenDAO.edit( bSES );
              if( bSES.getIdBookingStatus().getIdBookingStatus().equals( BookingStatus.BOOKED.getId() ) )
              {
                // Sólo se consideran las salas que estaban programadas
                screensExchanged.add( screenDONew );
              }
              break;
            }
          }
        }
      }
    }
    return screensExchanged;
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

  private BookingSpecialEventDO getBookingSpecialEventDO( BookingDO bookingDO, SpecialEventTO specialEventTO )
  {
    BookingSpecialEventDO specialEventDO = null;

    if( CollectionUtils.isNotEmpty( bookingDO.getBookingSpecialEventDOList() ) )
    {
      for( BookingSpecialEventDO bookingSpecialEventDO : bookingDO.getBookingSpecialEventDOList() )
      {
        if( bookingSpecialEventDO.getIdBookingSpecialEvent().equals( specialEventTO.getId() ) )
        {
          specialEventDO = bookingSpecialEventDO;
          break;
        }
      }
    }

    return specialEventDO;
  }

  private boolean isCurrentBookingBooked( BookingTO bookingTO )
  {
    boolean booked = bookingTO.getCopyScreenZero() > 0;
    if( !booked && CollectionUtils.isNotEmpty( bookingTO.getScreens() ) )
    {
      for( ScreenTO screen : bookingTO.getScreens() )
      {
        if( screen.getBookingStatus().getId().equals( BookingStatus.BOOKED.getIdLong() ) )
        {
          booked = true;
        }
      }
    }
    return booked;
  }

  private BookingDO getBookingDO( BookingTO bookingTO, boolean booked )
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

  private BookingTO createBooking( SpecialEventTO specialEventTO )
  {
    BookingTO bookingTO = new BookingTO();
    bookingTO.setId( specialEventTO.getIdBooking() );
    bookingTO.setEvent( specialEventTO.getEvent() );
    bookingTO.setTheater( specialEventTO.getTheater() );
    bookingTO.setIdBookingType( specialEventTO.getIdBookingType().intValue() );
    bookingTO.setCopy( specialEventTO.getCopy() );
    bookingTO.setCopyScreenZero( specialEventTO.getCopyScreenZero() );
    bookingTO.setCopyScreenZeroTerminated( specialEventTO.getCopyScreenZeroTerminated() );
    for( ScreenTO screenTO : specialEventTO.getScreens() )
    {
      SpecialEventScreenTO screen = new SpecialEventScreenTO();
      screen.setIdScreen( screenTO.getId() );
      screen.setPresaleTO( screenTO.getPresaleTO() );
      if( screenTO.getBookingObservation() != null )
      {
        screen.setIdObservation( screenTO.getBookingObservation().getId() );
      }
      screen.setStatus( specialEventTO.getStatus() );
      if( CollectionUtils.isNotEmpty( screenTO.getShowings() ) )
      {
        screen.setSpecialEventScreenShows( new ArrayList<SpecialEventScreenShowTO>() );
        for( CatalogTO showing : screenTO.getShowings() )
        {
          SpecialEventScreenShowTO screenShow = new SpecialEventScreenShowTO();
          screenShow.setNuShow( showing.getId().intValue() );
          screen.getSpecialEventScreenShows().add( screenShow );
        }
      }
      bookingTO.setScreens( specialEventTO.getScreens() );
      bookingTO.setScreensSelected( specialEventTO.getScreensSelected() );
    }

    bookingTO.setSpecialEvents( new ArrayList<SpecialEventTO>() );
    bookingTO.getSpecialEvents().add( specialEventTO );
    bookingTO.setTimestamp( specialEventTO.getTimestamp() );
    bookingTO.setUserId( specialEventTO.getUserId() );
    bookingTO.setUsername( specialEventTO.getUsername() );

    return bookingTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cancelBookings( List<SpecialEventTO> specialEventTOs )
  {
    if( CollectionUtils.isNotEmpty( specialEventTOs ) )
    {
      for( SpecialEventTO specialEventTO : specialEventTOs )
      {
        ValidatorUtil.validateSpecialEventBooking( specialEventTO );
        // Se crea el bookingTO asociado al evento especial
        BookingTO bookingTO = new BookingTO();
        bookingTO.setId( specialEventTO.getIdBooking() );
        bookingTO.setSpecialEvents( new ArrayList<SpecialEventTO>() );
        bookingTO.getSpecialEvents().add( specialEventTO );
        AbstractTOUtils.copyElectronicSign( specialEventTO, bookingTO );
        // Se cancela el registro existente
        this.bookingSpecialEventDAO.cancelBookingSpecialEvent( bookingTO );
        StringBuilder mensaje = new StringBuilder( "Se cancela el registro de programación existente: " );
        LOG.debug( mensaje.toString() );
        this.savedBookings++;
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cancelScreenBookingTO( BookingTO bookingTO )
  {
    ValidatorUtil.validateBooking( bookingTO );
    if( CollectionUtils.isNotEmpty( bookingTO.getScreens() ) )
    {
      for( ScreenTO screenTO : bookingTO.getScreens() )
      {
        if( screenTO.getBookingStatus().getId() == BookingStatus.CANCELED.getId() )
        {
          this.bookingSpecialEventDAO.cancelBookingSpecialEventScreen( screenTO.getId(), bookingTO.getCopy() );
        }
        if( screenTO.getPresaleTO() != null )
        {
          PresaleTO presaleTO = screenTO.getPresaleTO();
          if( presaleTO.getIdPresale() != null )
          {
            PresaleDO presaleDO = this.presaleDAO.find( presaleTO.getIdPresale() );
            presaleDO.setFgActive( presaleTO.isFgActive() );
            AbstractEntityUtils.applyElectronicSign( presaleDO, bookingTO );
            this.presaleDAO.edit( presaleDO );
          }
        }
      }
    }
    this.bookingSpecialEventDAO.updateBookingSpecialEvent( bookingTO );
    StringBuilder mensaje = new StringBuilder( "Se cancela el registro de programación existente: " );
    LOG.debug( mensaje.toString() );
    this.savedBookings++;
  }

  /**
   * @return the savedBookings
   */
  public int getSavedBookings()
  {
    return savedBookings;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SpecialEventBookingTO findBookingSpecialEvent( PagingRequestTO pagingRequestTO )
  {
    List<SpecialEventTO> specialEvents = this.bookingSpecialEventDAO
        .findBookingsSpecialEventByEventRegion( pagingRequestTO );
    PagingResponseTO<SpecialEventTO> response = new PagingResponseTO<SpecialEventTO>();
    response.setElements( specialEvents );
    SpecialEventBookingTO eventBookingTO = new SpecialEventBookingTO();
    eventBookingTO.setReponse( response );
    return eventBookingTO;
  }

  @Override
  public void createSpecialEventWeek( BookingSpecialEventDO bookingspecialEventDO, SpecialEventTO specialEventTO )
  {
    this.specialEventWeekDAO.createSpecialEventWeek( bookingspecialEventDO, specialEventTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PagingResponseTO<EventTO> getEventsBookedForReport( PagingRequestTO pagingRequestTO )
  {
    ValidatorUtil.validatePagingRequest( pagingRequestTO );
    return bookingDAO.findPresaleBookingsByPaging( pagingRequestTO );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendPresalesBookedByRegionEmail( RegionEmailTO regionEmailTO, List<EventTO> eventSelected )
  {
    Long weekId = regionEmailTO.getIdWeek();
    Long regionId = regionEmailTO.getIdRegion();
    String template = regionEmailTO.getMessage();
    String subject = regionEmailTO.getSubject();
    if( StringUtils.isEmpty( template ) )
    {
      template = "<html><body><p>&nbsp; </p></body></html>";
    }
    Document doc = Jsoup.parse( template );
    String templateCleaned = doc.outerHtml();
    LOG.info( templateCleaned );

    LOG.info( "Inicia la carga de correos" );
    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    ThreadPoolExecutor executorPool = new ThreadPoolExecutor( 2, 10, 30, TimeUnit.SECONDS,
        new ArrayBlockingQueue<Runnable>( 2 ), threadFactory );
    RegionEmailSenderPresaleWorker worker = new RegionEmailSenderPresaleWorker();
    worker.setEmailSenderEJB( emailSenderEJB );
    worker.setServiceReportsEJB( serviceReportsEJB );
    worker.setUser( this.getUser( regionEmailTO.getUserId() ) );
    worker.setMessage( templateCleaned );
    worker.setSubject( subject );
    worker.setIdRegion( regionId );
    worker.setIdWeek( weekId.intValue() );
    worker.setEventSelected( eventSelected );
    worker.setRegionDAO( regionDAO );
    worker.setBookingDAO( bookingDAO );
    worker.setConfigurationDAO( configurationDAO );
    executorPool.execute( worker );
    LOG.info( "Se termino de cargar los threads" );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void sendPresalesBookedByTheaterEmail( TheaterEmailTO theaterEmailTO, List<EventTO> eventSelected )
  {
    List<TheaterTO> theaters = theaterEmailTO.getTheatersTO();
    List<BookingTO> bookings = new ArrayList<BookingTO>();
    for( TheaterTO theater : theaters )
    {
      BookingTO booking = new BookingTO();
      AbstractTOUtils.copyElectronicSign( theater, booking );
      booking.setTheater( theater );
      booking.setWeek( new WeekTO( theaterEmailTO.getIdWeek().intValue() ) );
      bookings.add( booking );
    }
    sendAsynchronousEmails( bookings, eventSelected, theaterEmailTO.getSubject() );
  }

  private void sendAsynchronousEmails( List<BookingTO> bookings, List<EventTO> eventSelected, String subject )
  {
    LOG.info( "Inicia la carga de correos" );
    ThreadFactory threadFactory = Executors.defaultThreadFactory();
    ThreadPoolExecutor executorPool = new ThreadPoolExecutor( 2, bookings.size() < 2 ? 2 : bookings.size(), 30,
        TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>( 2 ), threadFactory );
    for( BookingTO booking : bookings )
    {
      TheaterEmailSenderPresaleWorker worker = new TheaterEmailSenderPresaleWorker();
      worker.setEventSelected( eventSelected );
      worker.setIdTheater( booking.getTheater().getId() );
      worker.setIdWeek( booking.getWeek().getIdWeek().longValue() );
      worker.setTheaterName( booking.getTheater().getName() );
      worker.setConfigurationDAO( configurationDAO );
      worker.setEmailSenderEJB( emailSenderEJB );
      worker.setUser( getUser( booking.getUserId() ) );
      worker.setEmailSenderEJB( emailSenderEJB );
      worker.setConfigurationDAO( configurationDAO );
      worker.setServiceReportsEJB( serviceReportsEJB );
      worker.setTheaterDAO( theaterDAO );
      worker.setWeekDAO( weekDAO );
      worker.setReportDAO( reportDAO );
      worker.setSubject( subject );
      executorPool.execute( worker );
    }
    LOG.info( "Inicia la carga de correos" );
  }

  /**
   * Method that returns the userDO found by userId
   * 
   * @param userId
   * @return userDO
   */
  private UserDO getUser( Long userId )
  {
    return this.userDAO.find( userId.intValue() );
  }
}
