package mx.com.cinepolis.digital.booking.web.beans.booking;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import mx.com.cinepolis.digital.booking.commons.constants.BookingShow;
import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.constants.BookingTicketSemaphore;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterBookingWeekTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.AbstractTOUtils;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.theater.ServiceAdminTheaterIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.CloseEvent;

/**
 * Backing bean for the theater booking user interface.
 * 
 * @author kperez
 * @author afuentes
 */
@ManagedBean(name = "theaterBookingBean")
@ViewScoped
public class TheaterBookingBean extends BaseManagedBean
{
  private static final String THEATER_ID_SELECTED_ATTRIBUTE = "theaterIdSelected";
  private static final String WEEK_ID_SELECTED_ATTRIBUTE = "weekIdSelected";
  private static final String REGION_ID_SELECTED_ATTRIBUTE = "regionIdSelected";
  private static final String TICK_IMAGE_NAME = "tick";
  private static final String ADDING_NOTE_ERROR_TEXT = "booking.theater.msgerror.addingNoteText";
  private static final String CANCEL_BOOKING_ERROR_TEXT = "booking.theater.msgerror.cancelBookingText";
  private static final String TERMINATE_BOOKING_ERROR_TEXT = "booking.theater.msgerror.terminateBookingText";
  private static final String NO_SELECTED_WEEK_THEATER_ERROR_TEXT = "booking.theater.msgerror.noSelectedWeekTheaterText";
  private static final String MOVIE_DUPLICATED_ERROR_TEXT = "booking.theater.msgerror.addingMovieText";
  private static final String ZERO_STRING = "0";
  private static final String VALUE_ATTRIBUTE = "value";
  private static final String DUPLICATE_SCREEN_ERROR_TEXT = "booking.theater.msgerror.duplicateScreenText";
  private static final int ID_BOOKING_TYPE = 1;
  private static final boolean PRESALE = false;
  private static final String YELLOW_LIGHT_TEXT = "sem_am";
  private static final String RED_LIGHT_TEXT = "sem_rj";
  private static final int ID_BOOKING_TYPE_PRE_SALE = 2;
  private static final int ID_BOOKING_TYPE_SPECIAL_EVENT = 3;
  private static final int SAVE_BOOKING = 1;
  private static final int CANCEL_PRESALE = 2;
  private static final int CANCEL_SPECIAL_EVENT = 3;

  private static final String A_TO_DATE = " to ";
  private static final String DE_TO_DATE = " of ";
  private static final long serialVersionUID = 6562783656740988151L;

  @EJB
  private BookingServiceIntegratorEJB bookingServiceIntegratorEJB;

  @EJB
  private ServiceAdminTheaterIntegratorEJB serviceAdminTheaterIntegratorEJB;

  private List<BookingTheaterTO> bookingTheaterTOs;
  private BookingTheaterTO bookingTheaterTO;
  private List<CatalogTO> regions;
  private Long regionIdSelected;
  private List<CatalogTO> dateWeeks;
  private Long weekIdSelected;
  private List<TheaterTO> theaters;
  private Long theaterIdSelected;
  private List<CatalogTO> moviesTOs;
  private Long newMovieId;
  private List<WeekTO> weekList;
  private String observations;
  private Set<Long> validStatusTermination;
  private List<Object> screensSelected;
  private Set<Long> validStatusCancellation;
  private List<ScreenTO> screens;
  private Integer copies;
  private List<String> lastScreenSelection = new ArrayList<String>();
  private Boolean movieSelected = false;
  private Set<Long> validStatusAddNote;
  private boolean validPreviewDocument;
  private int numberOfScreens;
  private Boolean sameOrBeforeWeek;
  private Boolean displayShows;
  private Boolean displayPresale;
  private Boolean emailSent;
  private boolean consulted;

  private TheaterTO theater;
  private String imageSemaphore;
  private Map<Long, List<EventTO>> availableEvents;
  private List<BookingTheaterTO> newBookingTheaters;
  private BookingTheaterTO selectedBookingTheater;
  private boolean zero;
  private boolean currentWeek;

  private String specialEventLabel = " (Special Event)";
  private String preReleaseLabel = " (Pre release)";
  private int idBookingTypeSpecialEvent = 3;
  private int idBookingTypePreRelease = 2;

  private boolean displayAttendance;
  private boolean existPresaleToCancel;
  private boolean existSpecialEventToCancel;

  /**
   * Method that performs initializations.
   */
  @PostConstruct
  public void init()
  {
    validPreviewDocument = false;
    validStatusCancellation = new HashSet<Long>();
    validStatusCancellation.add( Long.valueOf( BookingStatus.BOOKED.getId() ) );
    validStatusCancellation.add( Long.valueOf( BookingStatus.BOOKED.getId() ) );

    validStatusTermination = new HashSet<Long>();
    validStatusTermination.add( Long.valueOf( BookingStatus.BOOKED.getId() ) );

    validStatusAddNote = new HashSet<Long>();
    validStatusAddNote.add( Long.valueOf( BookingStatus.BOOKED.getId() ) );
    validStatusAddNote.add( Long.valueOf( BookingStatus.BOOKED.getId() ) );
    validStatusAddNote.add( Long.valueOf( BookingStatus.BOOKED.getId() ) );

    AbstractTO abstractTO = new AbstractTO();
    super.fillSessionData( abstractTO );
    abstractTO.setFgActive( true );
    bookingTheaterTOs = new ArrayList<BookingTheaterTO>();
    this.newBookingTheaters = new ArrayList<BookingTheaterTO>();

    this.weekList = this.bookingServiceIntegratorEJB.findWeeksActive( abstractTO );
    for( WeekTO weekTO : weekList )
    {
      if( weekTO.isActiveWeek() )
      {
        setWeekIdSelected( weekTO.getIdWeek().longValue() );
      }
    }
    this.regions = this.bookingServiceIntegratorEJB.findAllActiveRegions( abstractTO );

    // Busca los datos de la consulta anterior
    Object regionIdSelectedObj = getSession().getAttribute( REGION_ID_SELECTED_ATTRIBUTE );
    Object weekIdSelectedObj = getSession().getAttribute( WEEK_ID_SELECTED_ATTRIBUTE );
    Object theaterIdSelectedObj = getSession().getAttribute( THEATER_ID_SELECTED_ATTRIBUTE );
    if( weekIdSelectedObj != null )
    {
      weekIdSelected = (Long) weekIdSelectedObj;
      getSession().removeAttribute( WEEK_ID_SELECTED_ATTRIBUTE );
    }
    if( regionIdSelectedObj != null )
    {
      regionIdSelected = (Long) regionIdSelectedObj;
      getSession().removeAttribute( REGION_ID_SELECTED_ATTRIBUTE );
      loadTheatersFromRegion();
      if( theaterIdSelectedObj != null )
      {
        theaterIdSelected = (Long) theaterIdSelectedObj;
        getSession().removeAttribute( THEATER_ID_SELECTED_ATTRIBUTE );

        searchBooking();
      }
    }
    this.displayShows = Boolean.FALSE;
    this.displayPresale = Boolean.FALSE;
    this.copies = 1;
    this.emailSent = false;
    this.consulted = false;
    this.zero = false;
    this.currentWeek = false;
    this.displayAttendance = true;
    this.existPresaleToCancel = false;
    this.existSpecialEventToCancel = false;
  }

  /**
   * Método que busca un registro
   */
  public void searchBooking()
  {
    this.consulted = false;
    this.validPreviewDocument = false;
    this.emailSent = false;
    boolean existSpecialEvent;
    if( weekIdSelected == null || weekIdSelected.equals( Long.valueOf( 0 ) ) || theaterIdSelected == null
        || theaterIdSelected.equals( Long.valueOf( 0 ) ) )
    {
      createMessageError( NO_SELECTED_WEEK_THEATER_ERROR_TEXT );
      validationFail();
    }
    else
    {
      TheaterBookingWeekTO theaterBookingWeekTO = findBookings();
      this.availableEvents = theaterBookingWeekTO.getAvailableEvents();
      this.bookingTheaterTOs = theaterBookingWeekTO.getResponse().getElements();
      this.sameOrBeforeWeek = theaterBookingWeekTO.isEditable();
      this.emailSent = theaterBookingWeekTO.isSent();
      this.zero = theaterBookingWeekTO.isZero();
      this.currentWeek = this.weekIdSelected.equals( theaterBookingWeekTO.getCurrentWeek().getIdWeek().longValue() );

      this.theater = this.serviceAdminTheaterIntegratorEJB.getTheater( theaterIdSelected );
      this.numberOfScreens = theater.getScreens().size();
      for( BookingTheaterTO bTheaterTO : bookingTheaterTOs )
      {
        if( bTheaterTO.getBookingObservationTO() != null
            && validStatusAddNote.contains( bTheaterTO.getStatusTO().getId() ) )
        {
          bTheaterTO.setNote( bTheaterTO.getBookingObservationTO().getObservation() );
          bTheaterTO.setObservationImage( TICK_IMAGE_NAME );
        }
        bTheaterTO.setFgPresaleSelected( bTheaterTO.getPresaleTO().isFgActive() );
      }
      existSpecialEvent = this.applyNoteToBookingTheaterTOList();
      this.imageSemaphore = theaterBookingWeekTO.getImageSemaphore();
      if( this.imageSemaphore.equals( RED_LIGHT_TEXT ) && existSpecialEvent )
      {
        this.imageSemaphore = YELLOW_LIGHT_TEXT;
      }
      if( this.imageSemaphore.equals( RED_LIGHT_TEXT ) )
      {
        validationFail();
      }
      this.validPreviewDocument = !this.bookingTheaterTOs.isEmpty();
      this.consulted = true;
      initializeNewBookings();
    }
  }

  /**
   * Method that sets the note for each bookingTheaterTO, returns true whether exist at least one special event in list
   * otherwise returns false.
   * 
   * @return existSpecialEvent
   */
  private boolean applyNoteToBookingTheaterTOList()
  {
    boolean existSpecialEvent = false;
    this.existPresaleToCancel = false;
    for( BookingTheaterTO bTheaterTO : this.bookingTheaterTOs )
    {
      if( bTheaterTO.getBookingObservationTO() != null
          && validStatusAddNote.contains( bTheaterTO.getStatusTO().getId() ) )
      {
        if( bTheaterTO.getIdBookingType() != ID_BOOKING_TYPE )
        {
          this.putDateToNote( bTheaterTO );
          this.putDateToObservation( bTheaterTO );
        }
        if( bTheaterTO.getBookingObservationTO() != null
            && StringUtils.isNotEmpty( bTheaterTO.getBookingObservationTO().getObservation() ) )
        {
          bTheaterTO.setNote( bTheaterTO.getBookingObservationTO().getObservation() );
        }
        bTheaterTO.setObservationImage( TICK_IMAGE_NAME );
      }
      existSpecialEvent = (bTheaterTO.isFgSpecialEvent() ? bTheaterTO.isFgSpecialEvent() : existSpecialEvent);
      if( bTheaterTO.getPresaleTO().isFgActive() )
      {
        this.existPresaleToCancel = true;
      }
    }
    this.existSpecialEventToCancel = existSpecialEvent;
    return existSpecialEvent;
  }

  /**
   * Method that concats the date with the observation of a booking theater.
   * 
   * @param bookingTheater
   */
  private void putDateToObservation( BookingTheaterTO bookingTheater )
  {
    if( bookingTheater.getBookingObservationTO() != null
        && StringUtils.isNotEmpty( bookingTheater.getBookingObservationTO().getObservation() ) )
    {
      bookingTheater.getBookingObservationTO().setObservation(
        bookingTheater.getStrDateSpecialEvents().concat( bookingTheater.getBookingObservationTO().getObservation() ) );
    }
    else
    {
      BookingObservationTO observatioTO = new BookingObservationTO();
      observatioTO.setId( 0L );
      observatioTO.setObservation( bookingTheater.getStrDateSpecialEvents() );
      bookingTheater.setBookingObservationTO( observatioTO );
    }
  }

  /**
   * TODO por cambiar el locale al lenguaje apropiado Method to build the date for the view
   * 
   * @param bookingTheater
   */
  @SuppressWarnings("deprecation")
  private void putDateToNote( BookingTheaterTO bookingTheater )
  {
    StringBuilder sb = new StringBuilder();
    SimpleDateFormat format = new SimpleDateFormat( "EEEE dd", new Locale( "en", "US" ) );
    SimpleDateFormat format2 = new SimpleDateFormat( "MMMM", new Locale( "en", "US" ) );
    if( bookingTheater.getStartDay() != null && bookingTheater.getFinalDay() != null )
    {
      if( bookingTheater.getStartDay().getDate() == bookingTheater.getFinalDay().getDate()
          && bookingTheater.getStartDay().getMonth() == bookingTheater.getFinalDay().getMonth()
          && bookingTheater.getStartDay().getYear() == bookingTheater.getFinalDay().getYear() )
      {
        sb.append( format.format( bookingTheater.getStartDay() ) ).append( DE_TO_DATE )
            .append( format2.format( bookingTheater.getStartDay() ) ).append( ". " );
        bookingTheater.setStrDateSpecialEvents( sb.toString() );
      }
      else
      {
        sb.append( format.format( bookingTheater.getStartDay() ) ).append( DE_TO_DATE )
            .append( format2.format( bookingTheater.getStartDay() ) ).append( A_TO_DATE )
            .append( format.format( bookingTheater.getFinalDay() ) ).append( DE_TO_DATE )
            .append( format2.format( bookingTheater.getFinalDay() ) ).append( ". " );
        bookingTheater.setStrDateSpecialEvents( sb.toString() );
      }
    }
    else
    {
      bookingTheater.setStrDateSpecialEvents( "" );
    }
  }

  /**
   * Method that finds the booking theater by week.
   * 
   * @return TheaterBookingWeekTO
   */
  private TheaterBookingWeekTO findBookings()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    pagingRequestTO.setUserId( super.getUserId() );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, this.weekIdSelected );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_THEATER_ID, this.theaterIdSelected );
    return this.bookingServiceIntegratorEJB.findBookingTheater( pagingRequestTO );
  }

  /**
   * Método que agrega una nueva película a la programación de cine
   */
  public void saveNewMovie()
  {
    // Se obtienen los datos que se agregarán a la programación
    BookingTheaterTO newBooking = new BookingTheaterTO();
    newBooking.setCopies( this.copies );
    newBooking.setScreens( this.screens );
    newBooking.setSelectedScreens( screensSelected );
    newBooking.setSelectedEventId( newMovieId );

    // Se agrega la película al grid de programación
    this.addMovieGridProgramming( newBooking );

    Collections.sort( bookingTheaterTOs );
  }

  /**
   * Método que agrega una lista de películas a la programación de cine
   */
  public void saveListOfMovies()
  {

    if( CollectionUtils.isEmpty( newBookingTheaters ) )
    {
      createMessageWarning( "booking.theater.msgerror.moviesNotSelectedText" );
      validationFail();
    }
    else
    {
      int n = 0;
      for( BookingTheaterTO newBooking : newBookingTheaters )
      {
        // Se valida que tenga una pelicula seleccionada
        if( newBooking != null && newBooking.getSelectedEventId() != null && newBooking.getSelectedEventId() > 0 )
        {
          // Se agrega la película al grid de programación
          addMovieGridProgramming( newBooking );
          n++;

        } // if de validación de pelicula
        initializeNewBookings();
      }

      if( n > 0 )
      {
        Collections.sort( bookingTheaterTOs );
      }
      else
      {
        createMessageWarning( "booking.theater.msgerror.moviesNotSelectedText" );
        validationFail();
      }

    }
  }

  /**
   * Método que agrega la película al grid de programación
   * 
   * @param newBooking
   */
  private void addMovieGridProgramming( BookingTheaterTO newBooking )
  {
    BookingTO booking = null;
    EventTO eventTO = null;
    TheaterTO theaterTO = null;
    WeekTO weekTO = null;
    List<BookingTheaterTO> newBookings = null;

    // Se valida las copias y salas asociadas a una película.
    validateCopiesAndScreens( newBooking );

    // Se obtiene la lista de pantallas seleccionadas
    booking = new BookingTO();
    super.fillSessionData( booking );
    List<ScreenTO> theaterScreens = listScreenSelected( newBooking );

    // Se obtiene el id de la película seleccionada
    eventTO = new EventTO();
    AbstractTOUtils.copyElectronicSign( booking, eventTO );
    this.newMovieId = newBooking.getSelectedEventId();
    eventTO.setIdEvent( newBooking.getSelectedEventId() );

    // Se obtiene el id del cine celeccionado
    theaterTO = new TheaterTO();
    AbstractTOUtils.copyElectronicSign( booking, theaterTO );
    theaterTO.setId( theaterIdSelected );

    // Se obtiene la semana seleccionada
    weekTO = new WeekTO();
    AbstractTOUtils.copyElectronicSign( booking, weekTO );
    weekTO.setIdWeek( weekIdSelected.intValue() );

    // Se setean a booking las copias, película, cine, cemana y pantallas
    booking.setCopy( newBooking.getCopies() );
    booking.setEvent( eventTO );
    booking.setTheater( theaterTO );
    booking.setWeek( weekTO );
    booking.setScreens( theaterScreens );
    booking.setIdBookingType( ID_BOOKING_TYPE );
    PresaleTO presaleTO = new PresaleTO();
    presaleTO.setFgActive( PRESALE );
    booking.setPresaleTO( presaleTO );

    // Se apaga la bandera de selección
    setMovieSelected( false );

    // Compara la información de la base con la nueva programación
    // y genera una lista de newBookings
    newBookings = convert( booking );

    // Se valida las películas que se agregarán al grid de películas
    addMovieGrid( newBookings );
  }

  /**
   * Método que genera las validaciones de negocio para las copias y salas asociadas a una película
   * 
   * @param newBooking
   */
  private void validateCopiesAndScreens( BookingTheaterTO newBooking )
  {
    // Se valida que el número de copias no sea 0
    if( newBooking.getCopies() == 0 )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_NUMBER_COPIES_ZERO );
    }

    // Se valida que el número de pantallas sea menor al número de copias.
    if( newBooking.getScreens() != null && newBooking.getCopies() >= newBooking.getScreens().size() )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.BOOKING_NUMBER_OF_COPIES_EXCEEDS_NUMBER_OF_SCREENS );
    }

    // Se valida que el número de pantallas seleccionadas sea menor que el número de copias
    List<Object> screenSelected = newBooking.getSelectedScreens();
    if( screenSelected.size() > newBooking.getCopies() )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.BOOKING_NUM_SCREENS_GREATER_NUM_COPIES_NO_THEATER );
    }
  }

  /**
   * Se obtiene la lista de pantallas seleccionadas
   * 
   * @param newBooking
   * @param booking
   * @return
   */
  private List<ScreenTO> listScreenSelected( BookingTheaterTO newBooking )
  {
    ScreenTO screen = null;
    List<Object> screenSelected = newBooking.getSelectedScreens();
    List<ScreenTO> theaterScreens = new ArrayList<ScreenTO>();
    for( Object ob : screenSelected )
    {
      screen = new ScreenTO();
      super.fillSessionData( screen );
      screen.setId( Long.valueOf( ob.toString() ) );
      theaterScreens.add( screen );
    }
    return theaterScreens;
  }

  /**
   * Método que valida que no se repitan las programaciones en el grid de películas y agrega solo las que cumplen la
   * regla.
   * 
   * @param newBookings
   */
  private void addMovieGrid( List<BookingTheaterTO> newBookings )
  {
    boolean isValid;
    for( BookingTheaterTO b : newBookings )
    {
      isValid = true;
      if( !b.getScreenTO().getId().equals( 0L ) )
      {
        for( BookingTheaterTO oldBooking : bookingTheaterTOs )
        {
          if( b.getEventTO().getIdEvent().equals( oldBooking.getEventTO().getIdEvent() )
              && b.getScreenTO().getId().equals( oldBooking.getScreenTO().getId() ) )
          {
            isValid = false;
            break;
          }
        }
      }
      if( isValid )
      {
        bookingTheaterTOs.add( b );
      }
      else
      {
        this.createMessageError( MOVIE_DUPLICATED_ERROR_TEXT );
        this.validationFail();
      }
    }
  }

  private List<BookingTheaterTO> convert( BookingTO booking )
  {
    List<BookingTheaterTO> newBookings = new ArrayList<BookingTheaterTO>();

    List<CatalogTO> status = this.bookingServiceIntegratorEJB.getBookingStatus( Arrays.asList(
      BookingStatus.BOOKED.getIdLong(), BookingStatus.CANCELED.getIdLong() ) );

    CatalogTO movie = extractMovieSelected();
    Long idBooking = extractIdBooking( movie );

    for( int i = 0; i < booking.getCopy(); i++ )
    {
      BookingTheaterTO to = new BookingTheaterTO();
      to.setSelectedEventId( movie.getId() );
      to.setDistributor( movie instanceof EventMovieTO ? ((EventMovieTO) movie).getDistributor().getName() : "" );
      to.setEventTO( new EventMovieTO() );
      to.getEventTO().setIdEvent( movie.getId() );
      to.getEventTO().setDsEventName( movie.getName() );

      // TODO gsegura, validar de dónde obtener esta información
      to.setFridayIncome( 0.0 );
      to.setSaturdayIncome( 0.0 );
      to.setSundayIncome( 0.0 );
      to.setTotalIncome( 0.0 );
      to.setTotalTickets( 0L );
      to.setImage( BookingTicketSemaphore.GREEN.getImage() );

      to.setId( idBooking );

      to.setStatusName( status.get( 0 ).getName() );
      to.setStatusTO( status.get( 0 ) );
      to.setStatusIdSelected( BookingStatus.BOOKED.getIdLong() );
      to.setStatusTOs( status );

      to.setCapacity( 0 );
      to.setFormat( "" );
      to.setNuScreens( new ArrayList<Integer>() );
      to.getNuScreens().add( 0 );
      if( CollectionUtils.isNotEmpty( booking.getScreens() ) && i < booking.getScreens().size() )
      {
        Long idScreen = booking.getScreens().get( i ).getId();
        for( ScreenTO screenTO : this.theater.getScreens() )
        {
          if( screenTO.getId().equals( idScreen ) )
          {
            to.getNuScreens().add( screenTO.getNuScreen() );
            to.setCapacity( screenTO.getNuCapacity() );
            to.setScreenTO( screenTO );
            to.setFormat( screenTO.getScreenFormat() != null ? screenTO.getScreenFormat().getName() : "" );
            break;
          }
        }
      }

      if( to.getScreenTO() == null )
      {
        to.setScreenTO( new ScreenTO() );
        to.getScreenTO().setId( 0L );
        to.getScreenTO().setNuScreen( 0 );
        to.getScreenTO().setNuCapacity( 0 );
      }

      to.setShowings( Arrays.asList( BookingShow.FIRST.getShow(), BookingShow.SECOND.getShow(),
        BookingShow.THIRD.getShow(), BookingShow.FOURTH.getShow(), BookingShow.FIVETH.getShow(),
        BookingShow.SIXTH.getShow() ) );
      to.setSelectedShowings( new ArrayList<Object>() );

      to.setWeek( null );

      to.setBookingObservationTO( new BookingObservationTO() );
      to.setNote( "" );
      to.setPresaleTO( booking.getPresaleTO() );
      newBookings.add( to );
    }

    return newBookings;
  }

  private Long extractIdBooking( CatalogTO movieSelected )
  {
    Long idBooking = null;
    for( BookingTheaterTO bookingTheater : this.bookingTheaterTOs )
    {
      if( bookingTheater.getEventTO().getIdEvent() != null
          && bookingTheater.getEventTO().getIdEvent().equals( movieSelected.getId() ) )
      {
        idBooking = bookingTheater.getId();
        break;
      }
    }
    return idBooking;
  }

  private CatalogTO extractMovieSelected()
  {
    CatalogTO movieSelected = null;
    for( CatalogTO movie : this.moviesTOs )
    {
      if( movie.getId().equals( this.newMovieId ) )
      {
        movieSelected = movie;
        break;
      }
    }
    return movieSelected;
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   * 
   * @return true si no se selecciono registro
   */
  private Boolean validateSelection()
  {
    Boolean isValid = true;
    if( bookingTheaterTO == null )
    {
      createMessageValidationSelection();
      isValid = false;
    }
    return isValid;
  }

  public void validateAddNote()
  {
    if( validateSelection() )
    {
      Long statusId = getBookingSelected().getStatusTO().getId();
      if( !validStatusAddNote.contains( statusId ) )
      {
        validationFail();
        createMessageError( ADDING_NOTE_ERROR_TEXT );
      }
    }
    else
    {
      validationFail();
    }
  }

  private Boolean validateBookingSelection()
  {
    Boolean isValid = true;
    if( bookingTheaterTO == null )
    {
      isValid = false;
      createMessageValidationSelection();
    }
    return isValid;
  }

  public void validateCancelBooking()
  {
    if( validateBookingSelection() )
    {
      Long statusId = getBookingSelected().getStatusTO().getId();
      if( !validStatusCancellation.contains( statusId ) )
      {
        validationFail();
        createMessageError( CANCEL_BOOKING_ERROR_TEXT );
      }
    }
    else
    {
      validationFail();
    }
  }

  public void validateTerminateBooking()
  {
    if( validateBookingSelection() )
    {
      Long statusId = getBookingSelected().getStatusTO().getId();
      if( !validStatusTermination.contains( statusId ) )
      {
        validationFail();
        createMessageError( TERMINATE_BOOKING_ERROR_TEXT );
      }
    }
    else
    {
      validationFail();
    }
  }

  private BookingObservationTO buildBookingObservationTO()
  {
    BookingObservationTO bObservationTO = new BookingObservationTO();
    bObservationTO.setIdBooking( this.bookingTheaterTO.getId() );
    if( this.bookingTheaterTO.getBookingObservationTO() != null )
    {
      bObservationTO.setId( this.bookingTheaterTO.getBookingObservationTO().getId() );
    }
    bObservationTO.setObservation( this.observations );
    return bObservationTO;
  }

  private BookingTheaterTO getBookingSelected()
  {
    BookingTheaterTO bookingTheater = bookingTheaterTO;
    bookingTheater.setTimestamp( new Date() );
    bookingTheater.setUserId( 1L );
    return bookingTheater;
  }

  public void cancelBooking()
  {
    BookingTO booking = createBookingTO();
    List<BookingTO> bookingTOs = new ArrayList<BookingTO>();
    bookingTOs.add( booking );
    bookingServiceIntegratorEJB.cancelBooking( bookingTOs );
    searchBooking();
  }

  public void terminateBooking()
  {
    BookingTO booking = createBookingTO();
    bookingServiceIntegratorEJB.terminateBooking( booking );
    searchBooking();
  }

  public void handleClose( CloseEvent event )
  {
    observations = null;
  }

  public void handleCloseAddMovie( CloseEvent event )
  {
    this.newMovieId = null;
    this.copies = 1;
    this.screens = null;
  }

  private BookingTO createBookingTO()
  {
    BookingTO booking = new BookingTO();
    this.fillSessionData( booking );
    if( this.bookingTheaterTO != null )
    {
      booking.setId( this.bookingTheaterTO.getId() );
      booking.setBookingObservationTO( buildBookingObservationTO() );
      booking.setStatus( bookingTheaterTO.getStatusTO() );
    }
    else
    {
      booking.setBookingObservationTO( new BookingObservationTO() );
    }

    booking.setScreens( new ArrayList<ScreenTO>() );
    return booking;
  }

  /**
   * Listener method for the theater screens selectManyButton valueChange event.
   * 
   * @param event Object {@link ValueChangeEvent} with the event information.
   * @author afuentes
   */
  @SuppressWarnings("unchecked")
  public void changeValue( ValueChangeEvent event )
  {
    if( event.getOldValue() != null )
    {
      lastScreenSelection = parseStringSelection( (List<Object>) event.getOldValue() );
    }
  }

  private List<String> parseStringSelection( List<Object> oldObjectSelection )
  {
    List<String> parsedStrings = new ArrayList<String>();
    for( Object object : oldObjectSelection )
    {
      if( object != null )
      {
        parsedStrings.add( object.toString() );
      }
    }
    return parsedStrings;
  }

  /**
   * Listener method for the theater screens selectManyButton ajax event.
   * 
   * @param event Object {@link AjaxBehaviorEvent} with the event information.
   * @author afuentes
   */
  @SuppressWarnings("unchecked")
  public void loadScreens( AjaxBehaviorEvent event )
  {
    String zeroString = ZERO_STRING;
    Long zeroLong = Long.valueOf( 0L );
    List<Object> theaterScreensSelected = ((List<Object>) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE ));
    List<String> stringsScreensSelected = parseStringSelection( theaterScreensSelected );
    if( !lastScreenSelection.contains( zeroString ) && stringsScreensSelected.contains( zeroString ) )
    {
      theaterScreensSelected.retainAll( Arrays.asList( zeroString, zeroLong ) );
    }
    else if( lastScreenSelection.contains( zeroString ) && stringsScreensSelected.size() > lastScreenSelection.size() )
    {
      theaterScreensSelected.remove( zeroString );
      theaterScreensSelected.remove( zeroLong );
    }
    else if( CollectionUtils.isEmpty( stringsScreensSelected ) )
    {
      theaterScreensSelected.add( zeroString );
    }
  }

  public void addMovie()
  {
    if( weekIdSelected != null && theaterIdSelected != null )
    {
      moviesTOs = bookingServiceIntegratorEJB.findAvailableMovies( weekIdSelected, theaterIdSelected );
    }
    else
    {
      validationFail();
      createMessageError( NO_SELECTED_WEEK_THEATER_ERROR_TEXT );
    }
  }

  public void setWeekId( AjaxBehaviorEvent event )
  {
    Long idWeek = (Long) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    setWeekIdSelected( idWeek );
  }

  public void setTheaterId( AjaxBehaviorEvent event )
  {
    Long idTheater = (Long) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    setTheaterIdSelected( idTheater );
  }

  public void setTheaterScreens( AjaxBehaviorEvent event )
  {
    Long idMovie = (Long) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    List<ScreenTO> screenTOs = bookingServiceIntegratorEJB.findTheaterScreens( theaterIdSelected, idMovie );
    List<Object> screenIdSelected = new ArrayList<Object>();
    for( ScreenTO sc : screenTOs )
    {
      if( sc.getSelected() )
      {
        screenIdSelected.add( sc.getNuScreen().toString() );
      }
    }
    setScreens( screenTOs );
    setScreensSelected( screenIdSelected );
    setMovieSelected( true );
  }

  public void saveNote()
  {
    BookingObservationTO bObservationTO = new BookingObservationTO();
    super.fillSessionData( bObservationTO );
    bObservationTO.setIdBooking( bookingTheaterTO.getId() );
    if( bookingTheaterTO.getBookingObservationTO() != null )
    {
      bObservationTO.setId( bookingTheaterTO.getBookingObservationTO().getId() );
    }
    bObservationTO.setObservation( bookingTheaterTO.getNote() );
    bookingServiceIntegratorEJB.saveOrUpdateBookingObservation( bObservationTO );
    this.searchBooking();
  }

  /* Getters and setters */

  /**
   * @return the bookingTOs
   */
  public List<BookingTheaterTO> getBookingTOs()
  {
    return bookingTheaterTOs;
  }

  /**
   * @param bookingTOs the bookingTOs to set
   */
  public void setBookingTOs( List<BookingTheaterTO> bookingTOs )
  {
    this.bookingTheaterTOs = bookingTOs;
  }

  /**
   * @return the bookingTO
   */
  public BookingTheaterTO getBookingTO()
  {
    return bookingTheaterTO;
  }

  /**
   * @param bookingTO the bookingTO to set
   */
  public void setBookingTO( BookingTheaterTO bookingTO )
  {
    this.bookingTheaterTO = bookingTO;
  }

  /**
   * @return the regions
   */
  public List<CatalogTO> getRegions()
  {
    return regions;
  }

  /**
   * @param regions the regions to set
   */
  public void setRegions( List<CatalogTO> regions )
  {
    this.regions = regions;
  }

  /**
   * @return the regionIdSelected
   */
  public Long getRegionIdSelected()
  {
    return regionIdSelected;
  }

  /**
   * @param regionIdSelected the regionIdSelected to set
   */
  public void setRegionIdSelected( Long regionIdSelected )
  {
    this.regionIdSelected = regionIdSelected;
  }

  /**
   * @return the dateWeeks
   */
  public List<CatalogTO> getDateWeeks()
  {
    return dateWeeks;
  }

  /**
   * @param dateWeeks the dateWeeks to set
   */
  public void setDateWeeks( List<CatalogTO> dateWeeks )
  {
    this.dateWeeks = dateWeeks;
  }

  /**
   * @return the weekIdSelected
   */
  public Long getWeekIdSelected()
  {
    return weekIdSelected;
  }

  /**
   * @param weekIdSelected the weekIdSelected to set
   */
  public void setWeekIdSelected( Long weekIdSelected )
  {
    this.weekIdSelected = weekIdSelected;
  }

  /**
   * @return the theaters
   */
  public List<TheaterTO> getTheaters()
  {
    return theaters;
  }

  /**
   * @param theaters the theaters to set
   */
  public void setTheaters( List<TheaterTO> theaters )
  {
    this.theaters = theaters;
  }

  /**
   * @return the theaterIdSelected
   */
  public Long getTheaterIdSelected()
  {
    return theaterIdSelected;
  }

  /**
   * @param theaterIdSelected the theaterIdSelected to set
   */
  public void setTheaterIdSelected( Long theaterIdSelected )
  {
    this.theaterIdSelected = theaterIdSelected;
  }

  /**
   * @return the moviesTOs
   */
  public List<CatalogTO> getMoviesTOs()
  {
    return moviesTOs;
  }

  /**
   * @param moviesTOs the moviesTOs to set
   */
  public void setMoviesTOs( List<CatalogTO> moviesTOs )
  {
    this.moviesTOs = moviesTOs;
  }

  /**
   * @return the newMovieId
   */
  public Long getNewMovieId()
  {
    return newMovieId;
  }

  /**
   * @param newMovieId the newMovieId to set
   */
  public void setNewMovieId( Long newMovieId )
  {
    this.newMovieId = newMovieId;
  }

  /**
   * @return the weekList
   */
  public List<WeekTO> getWeekList()
  {
    return weekList;
  }

  /**
   * @param weekList the weekList to set
   */
  public void setWeekList( List<WeekTO> weekList )
  {
    this.weekList = weekList;
  }

  /**
   * @return the observations
   */
  public String getObservations()
  {
    return observations;
  }

  /**
   * @param observations the observations to set
   */
  public void setObservations( String observations )
  {
    this.observations = observations;
  }

  /**
   * @return the screensSelected
   */
  public List<Object> getScreensSelected()
  {
    return screensSelected;
  }

  /**
   * @param screensSelected the screensSelected to set
   */
  public void setScreensSelected( List<Object> screensSelected )
  {
    this.screensSelected = screensSelected;
  }

  /**
   * @return the screens
   */
  public List<ScreenTO> getScreens()
  {
    return screens;
  }

  /**
   * @param screens the screens to set
   */
  public void setScreens( List<ScreenTO> screens )
  {
    this.screens = screens;
  }

  /**
   * @return the copies
   */
  public Integer getCopies()
  {
    return copies;
  }

  /**
   * @param copies the copies to set
   */
  public void setCopies( Integer copies )
  {
    this.copies = copies;
  }

  /**
   * @return the movieSelected
   */
  public Boolean getMovieSelected()
  {
    return movieSelected;
  }

  /**
   * @param movieSelected the movieSelected to set
   */
  public void setMovieSelected( Boolean movieSelected )
  {
    this.movieSelected = movieSelected;
  }

  /**
   * @return the validPreviewDocument
   */
  public boolean isValidPreviewDocument()
  {
    return validPreviewDocument;
  }

  /**
   * @param validPreviewDocument the validPreviewDocument to set
   */
  public void setValidPreviewDocument( boolean validPreviewDocument )
  {
    this.validPreviewDocument = validPreviewDocument;
  }

  /**
   * @return the numberOfScreens
   */
  public int getNumberOfScreens()
  {
    return numberOfScreens;
  }

  /**
   * @param numberOfScreens the numberOfScreens to set
   */
  public void setNumberOfScreens( int numberOfScreens )
  {
    this.numberOfScreens = numberOfScreens;
  }

  /**
   * @return the sameOrBeforeWeek
   */
  public Boolean getSameOrBeforeWeek()
  {
    return sameOrBeforeWeek;
  }

  /**
   * @param sameOrBeforeWeek the sameOrBeforeWeek to set
   */
  public void setSameOrBeforeWeek( Boolean sameOrBeforeWeek )
  {
    this.sameOrBeforeWeek = sameOrBeforeWeek;
  }

  /**
   * @return the emailSent
   */
  public Boolean getEmailSent()
  {
    return emailSent;
  }

  /**
   * @param emailSent the emailSent to set
   */
  public void setEmailSent( Boolean emailSent )
  {
    this.emailSent = emailSent;
  }

  /**
   * @return the displayShows
   */
  public Boolean getDisplayShows()
  {
    return displayShows;
  }

  /**
   * @return the displayPresale
   */
  public Boolean getDisplayPresale()
  {
    return displayPresale;
  }

  /**
   * @param displayPresale the displayPresale to set
   */
  public void setDisplayPresale( Boolean displayPresale )
  {
    this.displayPresale = displayPresale;
  }

  /**
   * @param displayShows the displayShows to set
   */
  public void setDisplayShows( Boolean displayShows )
  {
    this.displayShows = displayShows;
  }

  /**
   * @return the consulted
   */
  public boolean isConsulted()
  {
    return consulted;
  }

  /**
   * @param consulted the consulted to set
   */
  public void setConsulted( boolean consulted )
  {
    this.consulted = consulted;
  }

  /**
   * @return the imageSemaphore
   */
  public String getImageSemaphore()
  {
    return imageSemaphore;
  }

  /**
   * @param imageSemaphore the imageSemaphore to set
   */
  public void setImageSemaphore( String imageSemaphore )
  {
    this.imageSemaphore = imageSemaphore;
  }

  /**
   * @return the newBookingTheaters
   */
  public List<BookingTheaterTO> getNewBookingTheaters()
  {
    return newBookingTheaters;
  }

  /**
   * @param newBookingTheaters the newBookingTheaters to set
   */
  public void setNewBookingTheaters( List<BookingTheaterTO> newBookingTheaters )
  {
    this.newBookingTheaters = newBookingTheaters;
  }

  /**
   * @return the selectedBookingTheater
   */
  public BookingTheaterTO getSelectedBookingTheater()
  {
    return selectedBookingTheater;
  }

  /**
   * @param selectedBookingTheater the selectedBookingTheater to set
   */
  public void setSelectedBookingTheater( BookingTheaterTO selectedBookingTheater )
  {
    this.selectedBookingTheater = selectedBookingTheater;
  }

  /**
   * @return the zero
   */
  public boolean isZero()
  {
    return zero;
  }

  /**
   * @param zero the zero to set
   */
  public void setZero( boolean zero )
  {
    this.zero = zero;
  }

  /**
   * @return the currentWeek
   */
  public boolean isCurrentWeek()
  {
    return currentWeek;
  }

  /**
   * @param currentWeek the currentWeek to set
   */
  public void setCurrentWeek( boolean currentWeek )
  {
    this.currentWeek = currentWeek;
  }

  /**
   * Carga los cines asociados a la region
   * 
   * @param event
   */
  public void loadTheaters( AjaxBehaviorEvent event )
  {
    if( this.regionIdSelected != null && this.regionIdSelected > 0 )
    {
      loadTheatersFromRegion();
    }
    else
    {
      this.theaterIdSelected = null;
      this.theaters = null;
    }
    this.resetBookings();
  }

  private void loadTheatersFromRegion()
  {
    CatalogTO region = new CatalogTO( this.regionIdSelected );
    super.fillSessionData( region );
    this.theaters = serviceAdminTheaterIntegratorEJB.getTheatersByRegionId( region );
  }

  /**
   * Method that validates whether exist a booking marked as presale and its status is cancel or terminate.
   */
  public void validateCancelOrTerminatePresale()
  {
    if( CollectionUtils.isNotEmpty( this.bookingTheaterTOs ) )
    {
      for( BookingTheaterTO bookingTO : this.bookingTheaterTOs )
      {
        if( bookingTO.getPresaleTO().isFgActive() && this.isCancelOrTerminate( bookingTO ) )
        {
          validationFail();
          break;
        }
      }
    }
  }

  /**
   * Method that verifies whether the status of {@link mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO}
   * object is cancel or terminate.
   * 
   * @param bookingTO, the {@link mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO} object to verify.
   * @return isCancelOrTerminate, with the result of validation.
   */
  private boolean isCancelOrTerminate( BookingTheaterTO bookingTO )
  {
    boolean isCancelOrTerminate = false;
    Long bookingStatusSelected = Long.valueOf( bookingTO.getStatusIdSelected().toString() );
    if( bookingStatusSelected.equals( BookingStatus.CANCELED.getIdLong() )
        || bookingStatusSelected.equals( BookingStatus.TERMINATED.getIdLong() ) )
    {
      isCancelOrTerminate = true;
    }
    return isCancelOrTerminate;
  }

  private List<BookingTheaterTO> extractBookingsToEdit( int operationType )
  {
    List<BookingTheaterTO> responseSpecialEvent = new ArrayList<BookingTheaterTO>();
    if( CollectionUtils.isNotEmpty( this.bookingTheaterTOs ) && operationType == CANCEL_SPECIAL_EVENT )
    {
      for( BookingTheaterTO bookingTheaterTO : this.bookingTheaterTOs )
      {
        if( bookingTheaterTO.isFgSpecialEvent() )
        {
          responseSpecialEvent.add( bookingTheaterTO );
        }
      }
    }
    return (operationType == CANCEL_SPECIAL_EVENT ? responseSpecialEvent : this.bookingTheaterTOs);
  }

  /**
   * Method that is called in save or update bookings case.
   * 
   * @author jreyesv
   */
  public void editBookingTheater()
  {
    this.editBookingTheaterAllCases( SAVE_BOOKING );
  }

  /**
   * Method that is called in cancel presale in bookings case.
   * 
   * @author jreyesv
   */
  public void cancelBookingTheaterInPresale()
  {
    this.editBookingTheaterAllCases( CANCEL_PRESALE );
  }

  /**
   * Method that is called in cancel special event bookings case.
   * 
   * @author jreyesv
   */
  public void cancelBookingTheaterSpecialEvent()
  {
    this.editBookingTheaterAllCases( CANCEL_SPECIAL_EVENT );
  }

  /**
   * Method that generates the {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} list to be save.
   */
  private void editBookingTheaterAllCases( int operationType )
  {
    List<BookingTheaterTO> bookingToEdit = this.extractBookingsToEdit( operationType );
    if( CollectionUtils.isNotEmpty( bookingToEdit ) )
    {
      List<BookingTO> bookings = new ArrayList<BookingTO>();
      for( BookingTheaterTO to : bookingToEdit )
      {
        if( isValidBookingTheater( to ) )
        {
          BookingTO bookingTO = new BookingTO( to.getId() );
          EventTO event = extractEventTO( to );
          to.getScreenTO().setPresaleTO( to.getPresaleTO() );
          Predicate predicate = PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getEvent" ),
            PredicateUtils.equalPredicate( event ) );

          if( !CollectionUtils.exists( bookings, predicate ) )
          {
            this.fillSessionData( bookingTO );
            bookingTO.setCopy( 0 );
            bookingTO.setCopyScreenZero( 0 );
            bookingTO.setCopyScreenZeroTerminated( 0 );
            bookingTO.setEvent( event );
            bookingTO.setExhibitionWeek( to.getWeek() != null ? to.getWeek() : 0 );
            bookingTO.setId( to.getId() );
            bookingTO.setIdBookingWeek( this.weekIdSelected );
            bookingTO.setScreens( new ArrayList<ScreenTO>() );
            // Por defecto todos los bookings se ponen BOOKED
            bookingTO.setStatus( new CatalogTO( BookingStatus.BOOKED.getIdLong() ) );
            bookingTO.setTheater( this.theater );
            bookingTO.setWeek( new WeekTO( this.weekIdSelected.intValue() ) );
            bookingTO.setNuScreenToZero( new ArrayList<Integer>() );
            this.setPresaleAndBookingTypeToBooking( operationType, to, bookingTO );
            bookings.add( bookingTO );
          }
          else
          {
            bookingTO = (BookingTO) CollectionUtils.find( bookings, predicate );
          }
          this.extractAndAddScreenForBooking( operationType, to, bookingTO );
        }
      }
      this.validateBookingStatus( bookings );
      this.callServiceMethodToUpdate( operationType, bookings );
      this.searchBooking();
    }
  }

  /**
   * Method that calls a service method depending to selected operatio type selected
   * 
   * @param operationType, with the operation type selected.
   * @param bookings, with the bookings information.
   * @author jreyesv
   */
  private void callServiceMethodToUpdate( int operationType, List<BookingTO> bookings )
  {
    if( operationType == SAVE_BOOKING )
    {
      this.bookingServiceIntegratorEJB.saveOrUpdateBookings( bookings );
    }
    else if( operationType == CANCEL_PRESALE )
    {
      this.bookingServiceIntegratorEJB.cancelPresaleInBookings( bookings );
    }
    else if( operationType == CANCEL_SPECIAL_EVENT )
    {
      this.bookingServiceIntegratorEJB.cancelSpecialEventBookings( bookings );
    }
  }

  /**
   * Method that sets the attributes presale and booking type for a booking.
   * 
   * @param operationType, with the operatino type selected.
   * @param to, with the {@link mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO} information.
   * @param bookingTO, with the {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} information.
   * @author jreyesv
   */
  private void setPresaleAndBookingTypeToBooking( int operationType, BookingTheaterTO to, BookingTO bookingTO )
  {
    // jreyesv
    bookingTO.setPresaleTO( to.getPresaleTO() );
    if( operationType == CANCEL_PRESALE )
    {
      bookingTO.getPresaleTO().setFgActive( to.isFgPresaleSelected() );
    }
    super.fillSessionData( bookingTO.getPresaleTO() );
    // Establece el tipo de booking evaluando si es evento especial.
    if( to.isFgSpecialEvent() )
    {
      bookingTO.setIdBookingType( to.getIdBookingType() );
    }
    else
    {
      bookingTO.setIdBookingType( ID_BOOKING_TYPE );
    }
  }

  /**
   * Method that extracts and adds the attribute screen for a booking.
   * 
   * @param operationType, with the operatino type selected.
   * @param to, with the {@link mx.com.cinepolis.digital.booking.commons.to.BookingTheaterTO} information.
   * @param bookingTO, with the {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} information.
   * @author jreyesv
   */
  private void extractAndAddScreenForBooking( int operationType, BookingTheaterTO to, BookingTO bookingTO )
  {
    ScreenTO screen = to.getScreenTO();
    // Se setea el atributo preventa para la sala.
    if( to.getScreenTO().getPresaleTO() != null )
    {
      if( screen.getPresaleTO().getIdPresale() != null )
      {
        screen.setPresaleTO( to.getPresaleTO() );
        if( operationType == CANCEL_PRESALE )
        {
          screen.getPresaleTO().setFgActive( to.isFgPresaleSelected() );
        }
        super.fillSessionData( screen.getPresaleTO() );
      }
      else
      {
        screen.setPresaleTO( null );
      }
    }
    this.fillSessionData( screen );
    screen.setShowings( new ArrayList<CatalogTO>() );
    setObservationAndShowing( to, screen );
    screen.setBookingStatus( new CatalogTO( Long.valueOf( to.getStatusIdSelected().toString() ) ) );
    // Agrega la sala al bookingTO
    this.addScreenToBookingTO( screen, bookingTO );
    if( screen.getNuScreen().intValue() > 0 )
    {
      // Se agrega el screen con sala diferente a 0
      bookingTO.getScreens().add( screen );
    }
  }

  private void addScreenToBookingTO( ScreenTO screen, BookingTO bookingTO )
  {
    if( screen.getBookingStatus().getId().equals( BookingStatus.BOOKED.getIdLong() )
        || screen.getBookingStatus().getId().equals( BookingStatus.CONTINUE.getIdLong() ) )
    {
      if( screen.getNuScreen().intValue() == 0 )
      {
        // Agrega las salas que se convierten a 0
        if( screen.getOriginalNuScreen() != null && screen.getOriginalNuScreen().intValue() != 0 )
        {
          bookingTO.getNuScreenToZero().add( screen.getOriginalNuScreen() );
        }
        bookingTO.setCopyScreenZero( bookingTO.getCopyScreenZero() + 1 );
      }
      else
      {
        bookingTO.setCopy( bookingTO.getCopy() + 1 );
      }
    }
    else if( screen.getBookingStatus().getId().equals( BookingStatus.TERMINATED.getIdLong() )
        && screen.getNuScreen().intValue() == 0 )
    {
      if( screen.getOriginalNuScreen() != null && screen.getOriginalNuScreen().intValue() != 0 )
      {
        bookingTO.getNuScreenToZero().add( screen.getOriginalNuScreen() );
      }
      bookingTO.setCopyScreenZeroTerminated( bookingTO.getCopyScreenZeroTerminated() + 1 );
    }
  }

  private void setObservationAndShowing( BookingTheaterTO to, ScreenTO screen )
  {
    if( to.getBookingObservationTO() != null && to.getBookingObservationTO().getObservation() != null )
    {
      BookingObservationTO obs = new BookingObservationTO();
      this.fillSessionData( obs );
      obs.setObservation( to.getBookingObservationTO().getObservation().trim() );
      screen.setBookingObservation( obs );
    }
    if( CollectionUtils.isNotEmpty( to.getSelectedShowings() ) )
    {
      for( Object show : to.getSelectedShowings() )
      {
        screen.getShowings().add( new CatalogTO( Long.valueOf( show.toString() ) ) );
      }
    }

  }

  private void validateBookingStatus( List<BookingTO> bookings )
  {
    if( CollectionUtils.isNotEmpty( bookings ) )
    {
      for( BookingTO booking : bookings )
      {
        if( !this.isSpecialEvent( booking ) )
        {
          if( booking.getCopy() + booking.getCopyScreenZero() == 0 )
          {
            // Se convierte a estado TERMINATE el servicio se encarga de determinar si es CANCEL
            booking.setStatus( new CatalogTO( BookingStatus.TERMINATED.getIdLong() ) );
          }

          setStatus( booking );
        }
      }
    }
  }

  private void setStatus( BookingTO booking )
  {
    if( booking.getCopy() > 0 && booking.getCopyScreenZero() == 0 )
    {
      boolean terminate = true;
      for( ScreenTO screen : booking.getScreens() )
      {
        if( screen.getBookingStatus().getId().equals( BookingStatus.BOOKED.getIdLong() )
            || screen.getBookingStatus().getId().equals( BookingStatus.CONTINUE.getIdLong() ) )
        {
          terminate = false;
          break;
        }
      }
      if( terminate )
      {
        // Se convierte a estado TERMINATE el servicio se encarga de determinar si es CANCEL
        booking.setStatus( new CatalogTO( BookingStatus.TERMINATED.getIdLong() ) );
      }
    }
  }

  /**
   * Método que retorna verdadero si el bookingTO es un evento especial, en caso contrario retorna falso.
   * 
   * @param bookingTO
   * @return
   */
  private boolean isSpecialEvent( BookingTO bookingTO )
  {
    return (bookingTO.getIdBookingType() == ID_BOOKING_TYPE_SPECIAL_EVENT || bookingTO.getIdBookingType() == ID_BOOKING_TYPE_PRE_SALE);
  }

  private EventTO extractEventTO( BookingTheaterTO to )
  {
    EventTO eventTO = to.getEventTO();
    if( eventTO == null || eventTO.getIdEvent() == null )
    {
      eventTO.setIdEvent( to.getSelectedEventId() );
    }
    eventTO.setId( eventTO.getIdEvent() );

    return eventTO;
  }

  private boolean isValidBookingTheater( BookingTheaterTO to )
  {
    boolean validEvent = to.getEventTO() != null && to.getEventTO().getIdEvent() != null;
    boolean validSelectedEvent = to.getSelectedEventId() != null && !to.getSelectedEventId().equals( 0L );
    return validEvent || validSelectedEvent;
  }

  public void sendTheaterBooking()
  {
    // Se guardan las programaciones antes de enviar el correo
    this.editBookingTheater();

    BookingTO bookingTO = new BookingTO();
    super.fillSessionData( bookingTO );
    bookingTO.setTheater( new TheaterTO() );
    bookingTO.getTheater().setId( this.theaterIdSelected );
    bookingTO.setIdBookingType( ID_BOOKING_TYPE );
    PresaleTO presaleTO = new PresaleTO();
    presaleTO.setFgActive( PRESALE );
    bookingTO.setPresaleTO( presaleTO );
    bookingTO.setWeek( new WeekTO( weekIdSelected.intValue() ) );
    this.bookingServiceIntegratorEJB.sendTheaterEmail( bookingTO, regionIdSelected );
    this.searchBooking();
  }

  public void resetBookings()
  {
    this.sameOrBeforeWeek = false;
    this.bookingTheaterTOs = new ArrayList<BookingTheaterTO>();
    this.emailSent = false;
    this.consulted = false;
  }

  public void updateShows()
  {
    if( StringUtils.isNotEmpty( this.imageSemaphore ) && this.imageSemaphore.equals( RED_LIGHT_TEXT ) )
    {
      validationFail();
    }
    this.displayShows = !this.displayShows;
  }

  public void updatePresaleColumn()
  {
    if( StringUtils.isNotEmpty( this.imageSemaphore ) && this.imageSemaphore.equals( RED_LIGHT_TEXT ) )
    {
      validationFail();
    }
    this.displayPresale = !this.displayPresale;
  }

  /**
   * FUNCIONALIDAD PARA CAMBIO DE SALA
   * 
   * @param event
   */
  public void handleChangeScreen( AjaxBehaviorEvent event )
  {
    String clientId = event.getComponent().getClientId();

    StringTokenizer st = new StringTokenizer( clientId, ":" );
    int index = 0;
    if( st.countTokens() == 4 )
    {
      st.nextToken();
      st.nextToken();
      String token = st.nextToken();
      index = Integer.parseInt( token );
    }

    BookingTheaterTO booking = this.bookingTheaterTOs.get( index );
    boolean isValid = true;
    if( !booking.getScreenTO().getNuScreen().equals( 0 ) )
    {
      BookingTheaterTO oldBooking;
      for( int i = 0; i < this.bookingTheaterTOs.size(); i++ )
      {
        if( i != index )
        {
          oldBooking = bookingTheaterTOs.get( i );
          if( booking.getEventTO().getIdEvent().equals( oldBooking.getEventTO().getIdEvent() )
              && booking.getScreenTO().getNuScreen().equals( oldBooking.getScreenTO().getNuScreen() ) )
          {
            isValid = false;
            break;
          }
        }
      }
    }
    if( !isValid )
    {
      booking.getScreenTO().setNuScreen( booking.getPreviousNuScreen() );
      createMessageError( DUPLICATE_SCREEN_ERROR_TEXT );
    }

  }

  public void handleChangeStatus( AjaxBehaviorEvent event )
  {
    String clientId = event.getComponent().getClientId();

    StringTokenizer st = new StringTokenizer( clientId, ":" );
    int index = 0;
    if( st.countTokens() == 4 )
    {
      st.nextToken();
      st.nextToken();
      String token = st.nextToken();
      index = Integer.parseInt( token );
    }

    BookingTheaterTO booking = this.bookingTheaterTOs.get( index );

    if( booking.getStatusTO().getId().equals( BookingStatus.BOOKED.getIdLong() )
        || booking.getStatusTO().getId().equals( BookingStatus.CONTINUE.getIdLong() ) )
    {
      changeTheaters( booking, index );
    }
    else if( booking.getStatusTO().getId().equals( BookingStatus.TERMINATED.getIdLong() ) )
    {
      if( Long.valueOf( booking.getStatusIdSelected().toString() ).equals( BookingStatus.CONTINUE.getIdLong() ) )
      {
        this.bookingTheaterTOs.get( index ).setInRemoval( false );
        this.bookingTheaterTOs.get( index ).setFgPresaleSelected(
          this.bookingTheaterTOs.get( index ).getPresaleTO().isFgActive() );
      }
      else
      {
        this.bookingTheaterTOs.get( index ).setInRemoval( true );
        this.bookingTheaterTOs.get( index ).setFgPresaleSelected( false );
      }
    }

  }

  private void changeTheaters( BookingTheaterTO booking, int index )
  {

    if( Long.valueOf( booking.getStatusIdSelected().toString() ).equals( BookingStatus.BOOKED.getIdLong() )
        || Long.valueOf( booking.getStatusIdSelected().toString() ).equals( BookingStatus.CONTINUE.getIdLong() ) )
    {
      this.bookingTheaterTOs.get( index ).setInRemoval( false );
      this.bookingTheaterTOs.get( index ).setFgPresaleSelected(
        this.bookingTheaterTOs.get( index ).getPresaleTO().isFgActive() );
      if( this.bookingTheaterTOs.get( index ).getWeek() != null
          && !this.bookingTheaterTOs.get( index ).getScreenTO().getNuScreen().equals( 0 )
          && !this.bookingTheaterTOs.get( index ).isFgSpecialEvent() )
      {
        this.bookingTheaterTOs.remove( index + 1 );
      }
    }
    else
    {
      this.bookingTheaterTOs.get( index ).setInRemoval( true );
      this.bookingTheaterTOs.get( index ).setFgPresaleSelected( false );
      if( !this.bookingTheaterTOs.get( index ).getScreenTO().getNuScreen().equals( 0 )
          && !this.bookingTheaterTOs.get( index ).isFgSpecialEvent() )
      {
        BookingTheaterTO to = new BookingTheaterTO();
        to.setScreenTO( (ScreenTO) booking.getScreenTO().clone() );

        to.setStatusTO( booking.getStatusTO() );

        to.setStatusIdSelected( booking.getStatusTO().getId().toString() );
        to.setStatusTOs( Arrays.asList( booking.getStatusTO() ) );
        to.setFormat( booking.getFormat() );
        to.setAvailable( true );
        to.setEditable( true );
        to.setCapacity( booking.getCapacity() );
        to.setBookingObservationTO( new BookingObservationTO() );
        to.setDistributor( "" );
        to.setEditable( true );
        to.setEventTO( new EventMovieTO() );
        to.setNuScreens( Arrays.asList( booking.getScreenTO().getNuScreen() ) );
        to.setShowings( Arrays.asList( BookingShow.FIRST.getShow(), BookingShow.SECOND.getShow(),
          BookingShow.THIRD.getShow(), BookingShow.FOURTH.getShow(), BookingShow.FIVETH.getShow(),
          BookingShow.SIXTH.getShow() ) );
        to.setSelectedShowings( new ArrayList<Object>() );
        to.setEvents( availableEvents.get( booking.getScreenTO().getId() ) );
        // jreyesv: agrega preventa vacia.
        to.setPresaleTO( new PresaleTO( null, false ) );
        this.bookingTheaterTOs.add( index + 1, to );
      }
    }
  }

  private void initializeNewBookings()
  {
    if( weekIdSelected != null && theaterIdSelected != null )
    {
      this.moviesTOs = bookingServiceIntegratorEJB.findAvailableMovies( weekIdSelected, theaterIdSelected );
      this.newBookingTheaters = new ArrayList<BookingTheaterTO>();
      List<EventTO> events = new ArrayList<EventTO>();
      for( CatalogTO to : this.moviesTOs )
      {
        EventTO event = null;
        if( to instanceof EventTO )
        {
          event = (EventTO) to;
        }
        else
        {
          event = new EventTO();
          event.setIdEvent( to.getId() );
          event.setDsEventName( to.getName() );
        }
        events.add( event );
      }

      for( int i = 0; i < 5; i++ )
      {
        BookingTheaterTO bookingtheater = new BookingTheaterTO();
        bookingtheater.setId( Long.valueOf( i ) );
        bookingtheater.setSelectedEventId( null );
        bookingtheater.setEvents( events );
        bookingtheater.setSelectedScreens( new ArrayList<Object>() );
        bookingtheater.setCopies( 1 );
        bookingtheater.setScreens( this.getScreensForAddMovieTable() );
        this.newBookingTheaters.add( bookingtheater );
      }
    }
  }

  private List<ScreenTO> getScreensForAddMovieTable()
  {
    List<ScreenTO> screens = new ArrayList<ScreenTO>();
    ScreenTO s = new ScreenTO();
    s.setId( 0L );
    s.setNuScreen( 0 );
    screens.add( s );
    for( ScreenTO screen : this.theater.getScreens() )
    {
      s = new ScreenTO();
      s.setDisabled( true );
      s.setId( screen.getId() );
      s.setNuScreen( screen.getNuScreen() );
      screens.add( s );
    }
    return screens;
  }

  public void setTheaterScreensForAddMovieTable( AjaxBehaviorEvent event )
  {
    String clientId = event.getComponent().getClientId();
    StringTokenizer st = new StringTokenizer( clientId, ":" );
    st.nextToken();
    st.nextToken();
    int index = Integer.valueOf( st.nextToken() );

    Long idMovie = (Long) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    List<ScreenTO> screenTOs = bookingServiceIntegratorEJB.findTheaterScreens( theaterIdSelected, idMovie );
    List<Object> screenIdSelected = new ArrayList<Object>();
    for( ScreenTO sc : screenTOs )
    {
      if( sc.getSelected() )
      {
        screenIdSelected.add( sc.getNuScreen().toString() );
      }
    }

    this.newBookingTheaters.get( index ).setScreens( screenTOs );
    this.newBookingTheaters.get( index ).setSelectedScreens( screenIdSelected );
    this.newBookingTheaters.get( index ).setAvailable( idMovie != null && !idMovie.equals( 0L ) );

  }

  @SuppressWarnings("unchecked")
  public void loadScreensForAddMovieTable( AjaxBehaviorEvent event )
  {
    String clientId = event.getComponent().getClientId();
    StringTokenizer st = new StringTokenizer( clientId, ":" );
    st.nextToken();
    st.nextToken();
    int index = Integer.valueOf( st.nextToken() );

    String zeroString = ZERO_STRING;
    Long zeroLong = Long.valueOf( 0L );
    List<Object> theaterScreensSelected = ((List<Object>) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE ));
    List<String> stringsScreensSelected = parseStringSelection( theaterScreensSelected );
    if( !lastScreenSelection.contains( zeroString ) && stringsScreensSelected.contains( zeroString ) )
    {
      theaterScreensSelected.retainAll( Arrays.asList( zeroString, zeroLong ) );
    }
    else if( lastScreenSelection.contains( zeroString ) && stringsScreensSelected.size() > lastScreenSelection.size() )
    {
      theaterScreensSelected.remove( zeroString );
      theaterScreensSelected.remove( zeroLong );
    }
    else if( CollectionUtils.isEmpty( stringsScreensSelected ) )
    {
      theaterScreensSelected.add( zeroString );
    }
    this.newBookingTheaters.get( index ).setSelectedScreens( theaterScreensSelected );
  }

  /**
   * @return the specialEventLabel
   */
  public String getSpecialEventLabel()
  {
    return specialEventLabel;
  }

  /**
   * @return the preReleaseLabel
   */
  public String getPreReleaseLabel()
  {
    return preReleaseLabel;
  }

  /**
   * @return the idBookingTypeSpecialEvent
   */
  public int getIdBookingTypeSpecialEvent()
  {
    return idBookingTypeSpecialEvent;
  }

  /**
   * @return the idBookingTypePreRelease
   */
  public int getIdBookingTypePreRelease()
  {
    return idBookingTypePreRelease;
  }

  public void openIncomesWindow()
  {
    // TODO, GSE agregar al contexto el id del cine y de la semana
    super.setAttribute( INCOME_WEEK_ID_ATTRIBUTE, this.weekIdSelected );
    super.setAttribute( INCOME_THEATER_ID_ATTRIBUTE, this.theaterIdSelected );
  }

  /**
   * @return the displayAttendance
   */
  public boolean isDisplayAttendance()
  {
    return displayAttendance;
  }

  /**
   * @param displayAttendance the displayAttendance to set
   */
  public void setDisplayAttendance( boolean displayAttendance )
  {
    this.displayAttendance = displayAttendance;
  }

  public void toogleDisplayAttendance()
  {
    if( StringUtils.isNotEmpty( this.imageSemaphore ) && this.imageSemaphore.equals( RED_LIGHT_TEXT ) )
    {
      validationFail();
    }
    this.displayAttendance = !this.displayAttendance;
  }

  /**
   * @return the existPresaleToCancel
   */
  public boolean isExistPresaleToCancel()
  {
    return existPresaleToCancel;
  }

  /**
   * @return the existSpecialEventToCancel
   */
  public boolean isExistSpecialEventToCancel()
  {
    return existSpecialEventToCancel;
  }

}
