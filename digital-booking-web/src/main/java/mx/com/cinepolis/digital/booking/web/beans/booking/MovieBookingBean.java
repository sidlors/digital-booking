package mx.com.cinepolis.digital.booking.web.beans.booking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.MovieBookingWeekTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.BookingTOComparator;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.time.DateUtils;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.RowEditEvent;

/**
 * Backing bean for the movie booking user interface.
 * 
 * @author keperez
 * @author afuentes
 */
@ManagedBean(name = "movieBookingBean")
@ViewScoped
public class MovieBookingBean extends BaseManagedBean
{
  private static final int INDEX_SCREENS_NUMBER_SELECT_MANY_BUTTON = 0;
  private static final String BOOKING_MOVIE_APPLY_COPIES_ERROR_TEXT = "booking.movie.applyCopiesErrorText";
  private static final String CANCEL_BOOKING_ERROR_TEXT = "booking.movie.mesgerror.cancelBookingText";
  private static final String BOOKINGS_UNEDITED_ERROR_TEXT = "booking.movie.mesgerror.bookingsUneditedText";
  private static final String NO_SELECTED_MOVIE_WEEK_ERROR_TEXT = "booking.movie.mesgerror.noSelectedMovieWeekText";
  private static final String INTERVAL_DATES_BEFORE_TODAY_ERROR_TEXT = "booking.movie.mesgerror.intervalDatesBeforeToday";
  private static final String INTERVAL_DATES_FINAL_BEFORE_START_ERROR_TEXT = "booking.movie.mesgerror.intervalDatesFinalBeforeStart";
  //private static final String RELEASE_BOFORE_INTERVAL_PRESALE_ERROR_TEXT = "booking.movie.mesgerror.invalidRelease";
  private static final String SAVE_BOOKING_PRESALE_WITHOUT_DATES_ERROR_TEXT = "booking.prerelease.mesgerror.savePrereleaseAsPresaleWithoutDates";
  private static final String ERROR_RELEASE_DATE_NO_IN_WEEK_SELECTED = "booking.movie.mesgerror.releaseDateNotInWeekSelected";
  private static final String ERROR_RELEASE_DATE_BEFORE_START_PRESALE = "booking.movie.mesgerror.releaseDateBeforeStartPresale";
  private static final String INTERVAL_DATES_AFTER_LAST_DATE_WEEK_SELECTED_TEXT = "booking.movie.mesgerror.intervalDatesNotAfterEndWeek";

  private static final int ID_BOOKING_TYPE = 1;

  private static final boolean FESTIVAL = false;
  private static final boolean PRERELEASE = false;

  private static final long serialVersionUID = 2469470583971595207L;

  private static final String ZERO_STRING = "0";
  private static final String VALUE_ATTRIBUTE = "value";
  private static final String NAME_SCREEN_NUMBERS_DISPLAY_FACET = "output";
  private static final int INDEX_SCREEN_NUMBERS_COLUMN = 3;
  private static final int INDEX_SCREEN_NUMBERS_CELL_EDITOR = 0;
  private static final int INDEX_COPIES_COLUMN = 1;
  private static final int INDEX_COPIES_CELL_EDITOR = 0;
  private static final String NAME_COPIES_DISPLAY_FACET = "output";

  private List<BookingTO> bookingTOs;
  private List<EventMovieTO> eventMovieTOs;
  private List<BookingTO> bookingTOsSelected;
  private List<CatalogTO> movieTOs;
  private Long movieIdSelected;
  private Integer numCopies;
  private EventMovieTO imageEventMovieTO;
  private Long regionId;
  private List<WeekTO> weekList;
  private Long weekIdSelected;
  private String observations;
  private List<CatalogTO> regions;
  private List<String> lastScreenSelection = new ArrayList<String>();
  private List<Object> originalScreenSelection = new ArrayList<Object>();
  private Integer originalNumCopies;
  private List<BookingTO> foundBookingTOs;
  private Set<Long> validStatusCancellation;
  private Set<Long> editableBookingStatus;
  private Set<Long> preselectedForSavingBookingStatus;
  private Boolean sameWeek;
  private Integer copy;
  private boolean currentWeek;

  /**
   * Variables for apply rules presale
   */
  private Date rulePresaleStartDate;
  private Date rulePresaleFinalDate;
  private boolean rulePresaleFlgStatus;
  private Date rulePresaleReleaseDate;
  private boolean markAllInPresale;
  private Date minDatePresale = new Date();
  private Date maxDatePresale;
  private Date minDateRelease;
  private Date maxDateRelease;

  @EJB
  private BookingServiceIntegratorEJB bookingServiceIntegratorEJB;

  /**
   * Method that performs initializations.
   */
  @PostConstruct
  public void init()
  {
    validStatusCancellation = new HashSet<Long>();
    validStatusCancellation.add( Long.valueOf( BookingStatus.BOOKED.getId() ) );

    editableBookingStatus = new HashSet<Long>();
    editableBookingStatus.add( BookingStatus.BOOKED.getIdLong() );
    preselectedForSavingBookingStatus = new HashSet<Long>();
    preselectedForSavingBookingStatus.add( BookingStatus.BOOKED.getIdLong() );

    AbstractTO abstractTO = new AbstractTO();
    fillSessionData( abstractTO );
    abstractTO.setFgActive( true );
    movieTOs = this.bookingServiceIntegratorEJB.findAllActiveMovies( FESTIVAL, PRERELEASE );
    eventMovieTOs = bookingServiceIntegratorEJB.findAllPremieres();
    regions = this.bookingServiceIntegratorEJB.findAllActiveRegions( abstractTO );
    weekList = this.bookingServiceIntegratorEJB.findWeeksActive( abstractTO );
    currentWeek = false;
    for( WeekTO weekTO : weekList )
    {
      if( weekTO.isActiveWeek() )
      {
        setWeekIdSelected( weekTO.getIdWeek().longValue() );
        this.setMinAndMaxDatesForPresaleAndRelease( weekTO );
      }
    }
    this.copy = 1;

    this.resetPresaleParameters();
  }

  /**
   * Method that finds the theater bookings for the given region and week.
   * 
   * @author kperez
   */
  public void searchTheater()
  {
    if( this.validateFilters() )
    {
      validateTheatersInRegion();
      loadBookings();
      setBookingTOsSelected( null );
    }
  }

  /**
   * Method for validate Theaters linked at the Region selected
   */
  private void validateTheatersInRegion()
  {
    int theatersInRegion = this.bookingServiceIntegratorEJB.getTheatersInRegion( getRegionId() );
    if( theatersInRegion <= 0 )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_NOT_THEATERS_IN_REGION );
    }
  }

  private void loadBookings()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    super.fillSessionData( pagingRequestTO );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_EVENT_NAME );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, true );
    addFilterEvent( pagingRequestTO );
    addFilterWeek( pagingRequestTO );
    addFilterRegion( pagingRequestTO );
    PagingResponseTO<BookingTO> response = null;
    List<BookingTO> result = null;
    MovieBookingWeekTO bookingWeekTO = bookingServiceIntegratorEJB.findBookingMovies( pagingRequestTO );
    this.currentWeek = this.weekIdSelected.equals( bookingWeekTO.getCurrentWeek().getIdWeek().longValue() );
    response = bookingWeekTO.getResponse();
    bookingTOs = response.getElements();
    result = response.getElements();
    applyData( result );
    this.sameWeek = bookingWeekTO.getSameOrBeforeWeek() || this.isPremiere( this.movieIdSelected );
  }

  private void applyData( List<BookingTO> result )
  {
    for( BookingTO bookingTO : result )
    {
      if( CollectionUtils.isEmpty( bookingTO.getScreensSelected() ) )
      {
        for( ScreenTO screenTO : bookingTO.getTheater().getScreens() )
        {
          if( screenTO.getId().equals( Long.valueOf( 0 ) ) )
          {
            screenTO.setSelected( true );
            bookingTO.getScreensSelected().add( screenTO.getId().toString() );
          }
        }
      }
      CatalogTO bookingStatus = bookingTO.getStatus();
      bookingTO.setIsEditable( isEditableStatus( bookingStatus ) );
      bookingTO.setSelectedForSaving( bookingTO.getId() == null && isPreselectedForSavingStatus( bookingStatus ) );
    }
  }

  private boolean isPreselectedForSavingStatus( CatalogTO bookingStatus )
  {
    return bookingStatus != null && preselectedForSavingBookingStatus.contains( bookingStatus.getId() );
  }

  private boolean isEditableStatus( CatalogTO bookingStatus )
  {
    return bookingStatus == null || editableBookingStatus.contains( bookingStatus.getId() );
  }

  private void addFilterRegion( PagingRequestTO pagingRequestTO )
  {
    if( getRegionId() != null && !getRegionId().equals( Long.valueOf( 0 ) ) )
    {
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, getRegionId() );
    }
  }

  private void addFilterWeek( PagingRequestTO pagingRequestTO )
  {
    if( getWeekIdSelected() != null && !getWeekIdSelected().equals( Long.valueOf( 0 ) ) )
    {
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, getWeekIdSelected() );
    }
  }

  private void addFilterEvent( PagingRequestTO pagingRequestTO )
  {
    if( getMovieIdSelected() != null && !getMovieIdSelected().equals( Long.valueOf( 0 ) ) )
    {
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, getMovieIdSelected() );
    }
  }

  /**
   * Method that ensures a movie and a week are selected.
   * 
   * @return {@link Boolean} <code>true</code> if a movie and a week are selected, <code>false</code> otherwise.
   * @author kperez
   */
  private Boolean validateFilters()
  {
    Boolean isValid = true;
    if( !validateMovieId() || !validateIdSelected( weekIdSelected ) || !validateIdSelected( regionId ) )
    {
      isValid = false;
      createMessageError( NO_SELECTED_MOVIE_WEEK_ERROR_TEXT );
    }
    return isValid;
  }

  private Boolean validateIdSelected( Long id )
  {
    Boolean isValid = true;
    if( id == null || id.equals( Long.valueOf( 0 ) ) )
    {
      isValid = false;
    }
    return isValid;
  }

  public void searchTheatersByImageSelected()
  {
    this.setMovieIdSelected( null );
    this.searchTheater();
  }

  private Boolean validateMovieId()
  {
    Boolean isValid = true;
    if( movieIdSelected == null || movieIdSelected.equals( Long.valueOf( 0 ) ) )
    {
      if( imageEventMovieTO == null )
      {
        isValid = false;
      }
      else
      {
        if( imageEventMovieTO.getIdEvent() == null || imageEventMovieTO.getIdEvent().equals( Long.valueOf( 0 ) ) )
        {
          isValid = false;
        }
        else
        {
          setMovieImageId( imageEventMovieTO.getIdEvent() );
        }
      }

    }
    return isValid;
  }

  private void setMovieImageId( Long id )
  {
    for( EventMovieTO eventMovieTO : eventMovieTOs )
    {
      if( eventMovieTO.getIdEvent().equals( id ) )
      {
        this.setMovieIdSelected( eventMovieTO.getIdEvent() );
      }
    }
  }

  /**
   * Método que guarda una programación de pelicula
   */
  public void saveBooking()
  {
    List<BookingTO> bookingsForSaving = new ArrayList<BookingTO>();
    for( BookingTO bookingTO : getBookingsForSave() )
    {
      int copy = bookingTO.getCopy();

      int screens = 0;
      if( bookingTO.getScreensSelected().size() == 1 )
      {
        if( !Long.valueOf( bookingTO.getScreensSelected().get( 0 ).toString() ).equals( 0L ) )
        {
          screens = bookingTO.getScreensSelected().size();
        }
      }
      else
      {
        screens = bookingTO.getScreensSelected().size();
      }

      bookingTO.setCopy( screens );
      bookingTO.setCopyScreenZero( copy - screens );
      bookingTO.setCopyScreenZeroTerminated( 0 );
      bookingTO.setIdBookingType( ID_BOOKING_TYPE );
      super.fillSessionData( bookingTO.getPresaleTO() );
      bookingsForSaving.add( bookingTO );
      /* Se actualiza la preventa en caso de que aplique */
      if( CollectionUtils.isNotEmpty( bookingTO.getScreens() ) )
      {
        this.updatePresaleForScreenTO( bookingTO.getScreens(), bookingTO.getPresaleTO() );
      }
    }
    bookingServiceIntegratorEJB.saveOrUpdateBookings( bookingsForSaving );
    searchTheater();
  }

  /**
   * Method that updates the {@link mx.com.cinepolis.digital.booking.commons.to.PresaleTO} for each
   * {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO}.
   * 
   * @param screensTO, with the list of {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO}.
   * @param bookingPresale, with the presale status.
   * @author jreyesv
   */
  private void updatePresaleForScreenTO( List<ScreenTO> screensTO, PresaleTO presaleTO )
  {
    boolean existScreenAsPresale = false;
    for( ScreenTO screenTO : screensTO )
    {
      super.fillSessionData( screenTO );
      screenTO.getPresaleTO().setDtStartDayPresale( presaleTO.getDtStartDayPresale() );
      screenTO.getPresaleTO().setDtFinalDayPresale( presaleTO.getDtFinalDayPresale() );
      screenTO.getPresaleTO().setDtReleaseDay( presaleTO.getDtReleaseDay() );
      super.fillSessionData( screenTO.getPresaleTO() );
      if( !existScreenAsPresale && screenTO.getPresaleTO().isFgActive() )
      {
        existScreenAsPresale = true;
      }
    }
    if( presaleTO.isFgActive() != existScreenAsPresale )
    {
      for( ScreenTO screenTO : screensTO )
      {
        screenTO.getPresaleTO().setFgActive( presaleTO.isFgActive() );
      }
    }
  }

  private List<BookingTO> getBookingsForSave()
  {
    List<BookingTO> bookingsForSaving = new ArrayList<BookingTO>();
    for( BookingTO bookingTO : this.bookingTOsSelected )
    {
      super.fillSessionData( bookingTO );
      if( bookingTO.getId() == null )
      {
        if( bookingTO.getCopy() != 0 )
        {
          validateNumCopies( bookingTO );
          if( hasMoreScreensThanCopies( bookingTO ) )
          {
            throwMoreScreensThanCopiesException( bookingTO );
          }
          else
          {
            List<ScreenTO> screensSelected = createScreenTO( bookingTO );
            bookingTO.setScreens( screensSelected );

            bookingsForSaving.add( bookingTO );
          }
        }
        else
        {
          // Cancellation of unsaved booking is not possible
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_NOT_SAVED_FOR_CANCELLATION,
            new Object[] { bookingTO.getTheater().getName(), bookingTO.getWeek().getWeekDescription() } );
        }
      }
      else
      {
        if( !bookingTO.getStatus().getId().equals( BookingStatus.TERMINATED.getIdLong() ) )
        {
          validateNumCopies( bookingTO );
          validateMoreScreensThanCopies( bookingTO );
          // validateCancellationOrTermination( bookingTO );
          this.getBookingsForUpdate( bookingsForSaving, bookingTO );
        }
      }
      // En caso de que el booking este marcado como preventa, valida las reglas para la preventa en el booking.
      if( bookingTO.getPresaleTO().isFgActive() )
      {
        this.validateRulesForPresale( bookingTO );
      }
    }
    return bookingsForSaving;
  }

  /*
   * private boolean isCancellation( BookingTO bookingTO ) { int exhibitionWeek = bookingTO.getExhibitionWeek(); String
   * statusName = bookingTO.getStatus().getName(); return exhibitionWeek == 1 && statusName != null; }
   */
  private void throwMoreScreensThanCopiesException( BookingTO bookingTO )
  {
    throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_NUM_SCREENS_GREATER_NUM_COPIES,
      new Object[] { bookingTO.getTheater().getName() } );
  }

  private void validateMoreScreensThanCopies( BookingTO bookingTO )
  {
    if( hasMoreScreensThanCopies( bookingTO ) )
    {
      throwMoreScreensThanCopiesException( bookingTO );
    }
  }

  private boolean hasMoreScreensThanCopies( BookingTO bookingTO )
  {
    boolean hasMoreScreens = bookingTO.getScreensSelected().size() > bookingTO.getCopy();
    return hasMoreScreens && !isCancellationOrTermination( bookingTO );
  }

  private boolean isCancellationOrTermination( BookingTO bookingTO )
  {
    return bookingTO.getCopy() == 0;
  }

  private void getBookingsForUpdate( List<BookingTO> bookingsForSaving, BookingTO bookingTO )
  {
    if( bookingTO.getStatus().getId().equals( BookingStatus.CANCELED.getIdLong() ) )
    {
      if( bookingTO.getCopy() != 0 )
      {
        CatalogTO statusTO = new CatalogTO();
        statusTO.setId( BookingStatus.BOOKED.getIdLong() );
        bookingTO.setStatus( statusTO );
        List<ScreenTO> screensSelected = createScreenTO( bookingTO );
        bookingTO.setScreens( screensSelected );
        bookingsForSaving.add( bookingTO );
      }
    }
    else if( bookingTO.getStatus().getId().equals( BookingStatus.BOOKED.getIdLong() ) && bookingTO.getCopy() == 0 )
    {
      bookingTO.setScreens( new ArrayList<ScreenTO>() );
      bookingsForSaving.add( bookingTO );
    }
    else
    {
      List<ScreenTO> screensSelected = createScreenTO( bookingTO );
      bookingTO.setScreens( screensSelected );
      bookingsForSaving.add( bookingTO );
    }
  }

  private void validateNumCopies( BookingTO bookingTO )
  {
    if( bookingTO.getCopy() > 100 )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_MAXIMUM_COPY );
    }
    if( bookingTO.getCopy() > bookingTO.getTheater().getScreens().size() )
    {
      throw DigitalBookingExceptionBuilder.build(
        DigitalBookingExceptionCode.BOOKING_NUMBER_OF_COPIES_EXCEEDS_NUMBER_OF_SCREENS, new Object[] { bookingTO
            .getTheater().getName() } );
    }
  }

  private List<ScreenTO> createScreenTO( BookingTO bookingTO )
  {
    List<ScreenTO> screensSelected = new ArrayList<ScreenTO>();
    for( Object ob : bookingTO.getScreensSelected() )
    {
      ScreenTO screen = new ScreenTO();
      super.fillSessionData( screen );
      screen.setId( Long.valueOf( ob.toString() ) );

      /* jreyesv: Se obtiene la preventa de la sala anterior */
      ScreenTO screenTOSelected = this.extractScreenTO( bookingTO, screen );
      if( screenTOSelected != null )
      {
        screen.setPresaleTO( screenTOSelected.getPresaleTO() );
      }
      else
      {
        screen.setPresaleTO( bookingTO.getPresaleTO() );
      }

      ScreenTO screenFromTheater = (ScreenTO) CollectionUtils.find( bookingTO.getTheater().getScreens(),
        PredicateUtils.equalPredicate( screen ) );
      if( screenFromTheater != null )
      {
        screen.setNuScreen( screenFromTheater.getNuScreen() );
      }

      screensSelected.add( screen );
      if( bookingTO.getStatus() != null )
      {
        screen.setBookingStatus( bookingTO.getStatus() );
      }
    }
    return screensSelected;
  }

  /**
   * Method that extracts the {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} object.
   * 
   * @param bookingspecialEventTO, with the booking special event information.
   * @param screen, with the screen information.
   * @return {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} object.
   */
  private ScreenTO extractScreenTO( BookingTO bookingTO, ScreenTO screen )
  {
    ScreenTO screenTOSelected = null;
    for( ScreenTO screenTO : bookingTO.getScreens() )
    {
      if( screenTO.getId().equals( screen.getId() ) )
      {
        if( screenTO.getPresaleTO() != null )
        {
          screenTOSelected = screenTO;
          break;
        }
        else
        {
          break;
        }
      }
    }
    return screenTOSelected;
  }

  /**
   * Method that validates the rules for save the bookings before process the request.
   */
  public void validationSaveBooking()
  {
    if( validateBookingSelection() )
    {
      List<BookingTO> bookingsForSaving = getBookingsForSave();
      if( CollectionUtils.isEmpty( bookingsForSaving ) )
      {
        createMessageError( BOOKINGS_UNEDITED_ERROR_TEXT );
      }
      for( BookingTO bookingTO : this.bookingTOsSelected )
      {
        if( bookingTO.getPresaleTO().isFgActive() )
        {
          this.validateRulesForPresale( bookingTO );
          this.validateDatesForPresaleInBooking( bookingTO );
        }
      }
    }
    else
    {
      validationFail();
    }
  }

  /**
   * Method that validates the date fields for a presale.
   */
  private void validateDatesForPresaleInBooking( BookingTO bookingTO )
  {

    if( bookingTO.getPresaleTO().isFgActive() )
    {
      if( bookingTO.getPresaleTO().getDtStartDayPresale() == null
          || bookingTO.getPresaleTO().getDtFinalDayPresale() == null
          || bookingTO.getPresaleTO().getDtReleaseDay() == null )
      {
        createMessageError( SAVE_BOOKING_PRESALE_WITHOUT_DATES_ERROR_TEXT );
        validationFail();
      }
    }
  }

  /**
   * Method that validates that a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} object marked as a
   * Presale be marked as Premiere and is not booked in zero screen.
   * 
   * @param bookingTO, with the booking data to validate.
   */
  private void validateRulesForPresale( BookingTO bookingTO )
  {
    // Validación para saber si se intenta guardar una película normal como preventa.
    if( !this.isPremiere( this.movieIdSelected ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.ERROR_BOOKING_PRESALE_FOR_NO_PREMIERE );
    }
    // Validación para saber si se intenta guardar una preventa en sala cero.
    if( CollectionUtils.isNotEmpty( bookingTO.getScreensSelected() ) )
    {
      for( Object screenSelected : bookingTO.getScreensSelected() )
      {
        if( screenSelected.toString().equals( "0" ) && bookingTO.getPresaleTO().isFgActive() )
        {
          throw DigitalBookingExceptionBuilder
              .build( DigitalBookingExceptionCode.ERROR_BOOKING_PRESALE_FOR_ZERO_SCREEN );
        }
      }
    }
  }

  /**
   * Method that validates whether the selected movie is a premiere.
   * 
   * @param idMovie, with the identifier of movie selected.
   * @return isPremiere, with result of validation.
   */
  private boolean isPremiere( Long idMovie )
  {
    boolean isPremiere = false;
    for( EventMovieTO eventMovieTO : eventMovieTOs )
    {
      if( eventMovieTO.getIdEvent().equals( idMovie ) && eventMovieTO.isPremiere() )
      {
        isPremiere = true;
        break;
      }
    }
    return isPremiere;
  }

  /**
   * Marks or unmarks all bookings as presale.
   */
  public void markAllBookingsAsPresale()
  {
    if( CollectionUtils.isNotEmpty( this.bookingTOs ) )
    {
      for( BookingTO bookingTO : this.bookingTOs )
      {
        if( this.markAllInPresale )
        {
          this.changePresaleTOStatus( bookingTO, Boolean.TRUE );
        }
        else
        {
          this.changePresaleTOStatus( bookingTO, Boolean.FALSE );
        }
      }
    }
  }

  /**
   * Method that changes the presale status for a booking.
   * 
   * @param bookingTO, a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} object with the information of
   *          booking to change.
   * @param status, variable with the presale status to set in booking.
   */
  private void changePresaleTOStatus( BookingTO bookingTO, boolean status )
  {
    if( bookingTO.getPresaleTO() != null )
    {
      bookingTO.getPresaleTO().setFgActive( status );
    }
    else
    {
      bookingTO.setPresaleTO( new PresaleTO( null, status ) );
    }
  }

  /**
   * Method that sets the value of presale parameters to a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO}
   * object.
   * 
   * @param bookingTO, a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} object without presale
   *          configuration.
   * @return bookingTO, a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} object with presale
   *         configuration.
   */
  private BookingTO setPresaleConfigurationToBooking( BookingTO bookingTO )
  {
    if( this.rulePresaleStartDate != null )
    {
      bookingTO.getPresaleTO().setDtStartDayPresale( this.rulePresaleStartDate );
    }
    if( this.rulePresaleFinalDate != null )
    {
      bookingTO.getPresaleTO().setDtFinalDayPresale( this.rulePresaleFinalDate );
    }
    if( this.rulePresaleReleaseDate != null )
    {
      bookingTO.getPresaleTO().setDtReleaseDay( this.rulePresaleReleaseDate );
      bookingTO.getPresaleTO().setStrReleaseDate( "" );
      this.rulePresaleFlgStatus = true;
    }
    if( this.rulePresaleFlgStatus )
    {
      this.changePresaleTOStatus( bookingTO, this.rulePresaleFlgStatus );
    }
    bookingTO.getPresaleTO().setStrPresaleDates( "" );
    return bookingTO;
  }

  /**
   * Method that applies the presale configuration specified for bookings selected.
   */
  public void applyPresaleConfiguration()
  {
    if( this.validateBookingSelection() && this.validatePresaleConfiguration() )
    {
      for( BookingTO bookingTO : this.bookingTOs )
      {
        if( CollectionUtils.exists(
          this.bookingTOsSelected,
          PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getTheater" ),
            PredicateUtils.equalPredicate( bookingTO.getTheater() ) ) ) )
        {
          this.setPresaleConfigurationToBooking( bookingTO );
        }
      }
      this.resetPresaleParameters();
      this.bookingTOsSelected = null;
    }
    else
    {
      validationFail();
    }
  }

  /**
   * Method that validates the presale configuration to apply.
   * 
   * @return isValid, with the result of validation.
   */
  private boolean validatePresaleConfiguration()
  {
    boolean isValid = true;
    boolean areValidIntervalDates = (this.rulePresaleStartDate != null && this.rulePresaleFinalDate != null && this.rulePresaleReleaseDate != null);
    Date today = DateUtils.truncate( new Date(), Calendar.DATE );
    isValid = this.validatePresaleConfigurationIntervalDates( areValidIntervalDates, today );
    isValid = (isValid && this.validateReleaseDateForPresale());
    /* TODO: Se quita validación, ya que la preventa puede durar hasta el final de la semana de estreno.
    if( isValid && this.rulePresaleReleaseDate.before( this.rulePresaleStartDate ) )
    {
      isValid = false;
      createMessageError( RELEASE_BOFORE_INTERVAL_PRESALE_ERROR_TEXT );
    }*/
    return isValid;
  }

  /**
   * Method that validates whether the release date are in range of week selected.
   * 
   * @return isValid, with the result of validation.
   * @author jreyesv
   */
  private boolean validateReleaseDateForPresale()
  {
    WeekTO currentWeekTO = this.extractWeekSelected( this.weekIdSelected );
    boolean isValid = !(this.rulePresaleReleaseDate.before( currentWeekTO.getStartingDayWeek() ) || currentWeekTO
        .getFinalDayWeek().before( this.rulePresaleReleaseDate ));
    if( !isValid )
    {
      isValid = false;
      createMessageError( ERROR_RELEASE_DATE_NO_IN_WEEK_SELECTED );
    }
    if( isValid && this.rulePresaleReleaseDate.before( this.rulePresaleStartDate ) )
    {
      isValid = false;
      createMessageError( ERROR_RELEASE_DATE_BEFORE_START_PRESALE );
    }
    return isValid;
  }

  /**
   * Method that validates the interval dates for presale configuration.
   * 
   * @param areValidIntervalDates, True whether dates of interval are not null, False in other case.
   * @param today, {@link java.util.Date} object with current day information.
   * @return isValid, with result of validation.
   */
  private boolean validatePresaleConfigurationIntervalDates( boolean areValidIntervalDates, Date today )
  {
    boolean isValid = true;
    if( !areValidIntervalDates )
    {
      isValid = false;
      super.validationFail();
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INVALID_PRESALE_DATES_PARAMETERS );
    }
    if( areValidIntervalDates && this.rulePresaleStartDate.before( today ) )
    {
      isValid = false;
      createMessageError( INTERVAL_DATES_BEFORE_TODAY_ERROR_TEXT );
    }
    else if( areValidIntervalDates && this.rulePresaleFinalDate.before( this.rulePresaleStartDate ) )
    {
      isValid = false;
      createMessageError( INTERVAL_DATES_FINAL_BEFORE_START_ERROR_TEXT );
    }
    else if( this.extractWeekSelected( this.weekIdSelected ).getFinalDayWeek().before( this.rulePresaleFinalDate ) )
    {
      isValid = false;
      createMessageError( INTERVAL_DATES_AFTER_LAST_DATE_WEEK_SELECTED_TEXT );
    }
    return isValid;
  }

  /**
   * Método que cancela una programación de película
   */
  public void cancelBooking()
  {
    bookingServiceIntegratorEJB.cancelBooking( this.getBookingSelected() );
    searchTheater();
  }

  public void setWeekId( AjaxBehaviorEvent event )
  {
    Long idWeek = (Long) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    this.resetForm();
    this.setMinAndMaxDatesForPresaleAndRelease( this.extractWeekSelected( idWeek ) );
    this.setWeekIdSelected( idWeek );
  }

  /**
   * Method that extracts the week selected by user in combo.
   * 
   * @param idWeek
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.WeekTO} object with the information of week selected.
   * @author jreyesv
   */
  private WeekTO extractWeekSelected( Long idWeek )
  {
    WeekTO response = null;
    for( WeekTO weekTO : weekList )
    {
      if( weekTO.getIdWeek().intValue() == idWeek.intValue() )
      {
        response = weekTO;
        break;
      }
    }
    return response;
  }

  /**
   * Method that sets the minimum and maximum dates for presale date component.
   * 
   * @param weekTO, with the week information.
   * @author jreyesv
   */
  private void setMinAndMaxDatesForPresaleAndRelease( WeekTO weekTO )
  {
    this.minDateRelease = weekTO.getStartingDayWeek();
    this.maxDateRelease = weekTO.getFinalDayWeek();
    this.minDatePresale = new Date();
    this.maxDatePresale = weekTO.getFinalDayWeek();
  }

  public void setMovieId( AjaxBehaviorEvent event )
  {
    Long idMovie = (Long) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    this.resetForm();
    this.setMovieIdSelected( idMovie );
    for( EventMovieTO eventMovieTO : this.eventMovieTOs )
    {
      if( eventMovieTO.getIdEvent().equals( idMovie ) )
      {
        this.imageEventMovieTO = new EventMovieTO();
        this.imageEventMovieTO.setIdEvent( idMovie );
        this.imageEventMovieTO.setIdMovieImage( eventMovieTO.getIdMovieImage() );
      }
    }
  }

  public void setRegionIdSelected( AjaxBehaviorEvent event )
  {
    Long idRegion = (Long) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    this.resetForm();
    this.setRegionId( idRegion );
  }

  /**
   * Método que obtiene el registro seleccionado
   * 
   * @return BookingTO
   */
  private List<BookingTO> getBookingSelected()
  {
    List<BookingTO> bookingSelected = bookingTOsSelected;
    for( BookingTO booking : bookingTOsSelected )
    {
      super.fillSessionData( booking );
    }

    return bookingSelected;
  }

  public void handleClose( CloseEvent event )
  {
    observations = null;
  }

  private Boolean validateBookingSelection()
  {
    Boolean isValid = true;
    if( CollectionUtils.isEmpty( getBookingTOsSelected() ) )
    {
      isValid = false;
      createMessageError( "common.oneSelectionErrorText" );
    }
    return isValid;
  }

  public void validateCancelBooking()
  {
    if( validateBookingSelection() )
    {
      for( BookingTO bookingTO : this.bookingTOsSelected )
      {
        CatalogTO statusTO = bookingTO.getStatus();
        if( statusTO == null || !validStatusCancellation.contains( statusTO.getId() ) )
        {
          createMessageError( CANCEL_BOOKING_ERROR_TEXT );
          validationFail();
        }
        else if( bookingTO.getExhibitionWeek() != 1 )
        {
          createMessageError( "business.error.code.91" );
          validationFail();
        }
        else if( bookingTO.getId() == null )
        {
          createMessageError( "business.error.code.84" );
          validationFail();
        }
      }
    }
    else
    {
      validationFail();
    }
  }

  /**
   * Listener method for the theater list dataTable rowEdit event.
   * 
   * @param event Object {@link RowEditEvent} with the event information.
   * @author afuentes
   */
  public void onEdit( RowEditEvent event )
  {
    BookingTO bookingTO = (BookingTO) event.getObject();
    int numberOfCopies = bookingTO.getCopy();
    if( numberOfCopies == 0 )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_NUMBER_COPIES_ZERO );
    }
    List<Object> screenSelected = bookingTO.getScreensSelected();
    if( screenSelected.size() > numberOfCopies )
    {
      restoreOriginalNumCopies( bookingTO );
      restoreOriginalScreenSelection( screenSelected );
    }
    bookingTO.setSelectedForSaving( true );
  }

  /**
   * Listener method for the theater list dataTable rowEditInit event.
   * 
   * @param event Object {@link RowEditEvent} with the event information.
   * @author afuentes
   */
  @SuppressWarnings("unchecked")
  public void onRowEditInit( RowEditEvent event )
  {
    originalScreenSelection = (List<Object>) event.getComponent().getChildren().get( INDEX_SCREEN_NUMBERS_COLUMN )
        .getChildren().get( INDEX_SCREEN_NUMBERS_CELL_EDITOR ).getFacet( NAME_SCREEN_NUMBERS_DISPLAY_FACET )
        .getAttributes().get( VALUE_ATTRIBUTE );
    originalNumCopies = (Integer) event.getComponent().getChildren().get( INDEX_COPIES_COLUMN ).getChildren()
        .get( INDEX_COPIES_CELL_EDITOR ).getFacet( NAME_COPIES_DISPLAY_FACET ).getAttributes().get( VALUE_ATTRIBUTE );
  }

  /**
   * Listener method for the theater list dataTable rowEditCancel event.
   * 
   * @param event Object {@link RowEditEvent} with the event information.
   * @author afuentes
   */
  @SuppressWarnings("unchecked")
  public void onRowEditCancel( RowEditEvent event )
  {
    List<Object> screensSelected = (List<Object>) event.getComponent().getChildren().get( INDEX_SCREEN_NUMBERS_COLUMN )
        .getChildren().get( INDEX_SCREEN_NUMBERS_CELL_EDITOR ).getFacet( NAME_SCREEN_NUMBERS_DISPLAY_FACET )
        .getAttributes().get( VALUE_ATTRIBUTE );
    restoreOriginalScreenSelection( screensSelected );
  }

  private void restoreOriginalScreenSelection( List<Object> screensSelected )
  {
    List<Object> copyScreensSelected = new ArrayList<Object>();
    copyScreensSelected.addAll( screensSelected );
    for( Object object : copyScreensSelected )
    {
      if( !originalScreenSelection.contains( object ) )
      {
        screensSelected.remove( object );
      }
    }
    for( Object oldObject : originalScreenSelection )
    {
      if( !screensSelected.contains( oldObject ) )
      {
        screensSelected.add( oldObject );
      }
    }
  }

  private void restoreOriginalNumCopies( BookingTO bookingTO )
  {
    bookingTO.setCopy( originalNumCopies );
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
    lastScreenSelection = parseStringSelection( (List<Object>) event.getOldValue() );
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
    List<Object> screensSelected = ((List<Object>) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE ));
    List<String> stringsScreensSelected = parseStringSelection( screensSelected );
    if( !lastScreenSelection.contains( zeroString ) && stringsScreensSelected.contains( zeroString ) )
    {
      screensSelected.retainAll( Arrays.asList( zeroString, zeroLong ) );
    }
    else if( lastScreenSelection.contains( zeroString ) && stringsScreensSelected.size() > lastScreenSelection.size() )
    {
      screensSelected.remove( zeroString );
      screensSelected.remove( zeroLong );
    }
    else if( CollectionUtils.isEmpty( stringsScreensSelected ) )
    {
      screensSelected.add( zeroString );
    }
  }

  public void applyCopies()
  {
    if( validateBookingSelection() )
    {
      if( this.copy != null )
      {
        List<BookingTO> bookingsSelected = getBookingSelected();

        for( BookingTO bookingTO : this.bookingTOs )
        {
          if( !bookingTO.getDisabled()
              && CollectionUtils.exists( bookingsSelected, PredicateUtils.transformedPredicate(
                TransformerUtils.invokerTransformer( "getTheater" ),
                PredicateUtils.equalPredicate( bookingTO.getTheater() ) ) ) )
          {
            bookingTO.setCopy( this.copy );
          }
        }
        this.copy = 1;
        setBookingTOsSelected( null );
      }
      else
      {
        createMessageError( BOOKING_MOVIE_APPLY_COPIES_ERROR_TEXT );
        validationFail();
      }
    }
    else
    {
      validationFail();
    }

  }

  /**
   * Listener method for the numCopies inputText valueChange event.
   * 
   * @param event Object {@link ValueChangeEvent} with the event information.
   * @author afuentes
   */
  @SuppressWarnings("unchecked")
  public void onNumCopiesChanged( ValueChangeEvent event )
  {
    Object newValue = event.getNewValue();
    if( newValue != null && newValue instanceof Integer )
    {
      int value = (Integer) newValue;
      if( value == 0 )
      {
        List<Object> screensSelected = (List<Object>) event.getComponent().getParent().getParent().getChildren()
            .get( INDEX_SCREEN_NUMBERS_COLUMN ).getChildren().get( INDEX_SCREENS_NUMBER_SELECT_MANY_BUTTON )
            .getAttributes().get( VALUE_ATTRIBUTE );
        screensSelected.clear();
        screensSelected.add( 0L );
      }
    }
  }

  /**
   * Method that resets the search criteria and whole form.
   */
  public void resetSearch()
  {
    movieIdSelected = null;
    regionId = null;
    for( WeekTO weekTO : weekList )
    {
      if( weekTO.isActiveWeek() )
      {
        setWeekIdSelected( weekTO.getIdWeek().longValue() );
        this.setMinAndMaxDatesForPresaleAndRelease( weekTO );
      }
    }
    this.resetForm();
  }

  /**
   * Listener method for the reset button of the form used to specify filters for the theater search.
   * 
   * @author afuentes
   */
  public void resetForm()
  {
    bookingTOs = null;
    bookingTOsSelected = null;
    this.resetPresaleParameters();
  }

  /**
   * Method that resets the values of presale parameters.
   */
  private void resetPresaleParameters()
  {
    this.rulePresaleStartDate = null;
    this.rulePresaleFinalDate = null;
    this.rulePresaleFlgStatus = Boolean.FALSE;
    this.rulePresaleReleaseDate = null;
  }

  /* Getters and setters */

  /**
   * @return the eventMovieTOs
   */
  public List<EventMovieTO> getEventMovieTOs()
  {
    return eventMovieTOs;
  }

  /**
   * @param eventMovieTOs the eventMovieTOs to set
   */
  public void setEventMovieTOs( List<EventMovieTO> eventMovieTOs )
  {
    this.eventMovieTOs = eventMovieTOs;
  }

  /**
   * @return the bookingTOs
   */
  public List<BookingTO> getBookingTOs()
  {
    return bookingTOs;
  }

  /**
   * @param bookingTOs the bookingTOs to set
   */
  public void setBookingTOs( List<BookingTO> bookingTOs )
  {
    this.bookingTOs = bookingTOs;
  }

  /**
   * @return the bookingTOsSelected
   */
  public List<BookingTO> getBookingTOsSelected()
  {
    if( CollectionUtils.isNotEmpty( bookingTOsSelected ) )
    {
      Collections.sort( bookingTOsSelected, new BookingTOComparator() );
    }
    return bookingTOsSelected;
  }

  /**
   * @param bookingTOsSelected the bookingTOsSelected to set
   */
  public void setBookingTOsSelected( List<BookingTO> bookingTOsSelected )
  {
    this.bookingTOsSelected = bookingTOsSelected;
  }

  /**
   * @return the foundBookingTOs
   */
  public List<BookingTO> getFoundBookingTOs()
  {
    return foundBookingTOs;
  }

  /**
   * @param foundBookingTOs the foundBookingTOs to set
   */
  public void setFoundBookingTOs( List<BookingTO> foundBookingTOs )
  {
    this.foundBookingTOs = foundBookingTOs;
  }

  /**
   * @return the movieTOs
   */
  public List<CatalogTO> getMovieTOs()
  {
    return movieTOs;
  }

  /**
   * @param movieTOs the movieTOs to set
   */
  public void setMovieTOs( List<CatalogTO> movieTOs )
  {
    this.movieTOs = movieTOs;
  }

  /**
   * @return the movieIdSelected
   */
  public Long getMovieIdSelected()
  {
    return movieIdSelected;
  }

  /**
   * @param movieIdSelected the movieIdSelected to set
   */
  public void setMovieIdSelected( Long movieIdSelected )
  {
    this.movieIdSelected = movieIdSelected;
  }

  /**
   * @return the numCopies
   */
  public Integer getNumCopies()
  {
    return numCopies;
  }

  /**
   * @param numCopies the numCopies to set
   */
  public void setNumCopies( Integer numCopies )
  {
    this.numCopies = numCopies;
  }

  /**
   * @return the imageEventMovieTO
   */
  public EventMovieTO getImageEventMovieTO()
  {
    return imageEventMovieTO;
  }

  /**
   * @param imageEventMovieTO the imageEventMovieTO to set
   */
  public void setImageEventMovieTO( EventMovieTO imageEventMovieTO )
  {
    this.imageEventMovieTO = imageEventMovieTO;
  }

  /**
   * @return the regionId
   */
  public Long getRegionId()
  {
    return regionId;
  }

  /**
   * @param regionId the regionId to set
   */
  public void setRegionId( Long regionId )
  {
    this.regionId = regionId;
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
   * @return the sameWeek
   */
  public Boolean getSameWeek()
  {
    return sameWeek;
  }

  /**
   * @param sameWeek the sameWeek to set
   */
  public void setSameWeek( Boolean sameWeek )
  {
    this.sameWeek = sameWeek;
  }

  /**
   * @return the copy
   */
  public Integer getCopy()
  {
    return copy;
  }

  /**
   * @param copy the copy to set
   */
  public void setCopy( Integer copy )
  {
    this.copy = copy;
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
   * @return the rulePresaleStartDate
   */
  public Date getRulePresaleStartDate()
  {
    return rulePresaleStartDate;
  }

  /**
   * @param rulePresaleStartDate the rulePresaleStartDate to set
   */
  public void setRulePresaleStartDate( Date rulePresaleStartDate )
  {
    this.rulePresaleStartDate = rulePresaleStartDate;
  }

  /**
   * @return the rulePresaleFinalDate
   */
  public Date getRulePresaleFinalDate()
  {
    return rulePresaleFinalDate;
  }

  /**
   * @param rulePresaleFinalDate the rulePresaleFinalDate to set
   */
  public void setRulePresaleFinalDate( Date rulePresaleFinalDate )
  {
    this.rulePresaleFinalDate = rulePresaleFinalDate;
  }

  /**
   * @return the rulePresaleFlgStatus
   */
  public boolean isRulePresaleFlgStatus()
  {
    return rulePresaleFlgStatus;
  }

  /**
   * @param rulePresaleFlgStatus the rulePresaleFlgStatus to set
   */
  public void setRulePresaleFlgStatus( boolean rulePresaleFlgStatus )
  {
    this.rulePresaleFlgStatus = rulePresaleFlgStatus;
  }

  /**
   * @return the rulePresaleReleaseDate
   */
  public Date getRulePresaleReleaseDate()
  {
    return rulePresaleReleaseDate;
  }

  /**
   * @param rulePresaleReleaseDate the rulePresaleReleaseDate to set
   */
  public void setRulePresaleReleaseDate( Date rulePresaleReleaseDate )
  {
    this.rulePresaleReleaseDate = rulePresaleReleaseDate;
  }

  /**
   * @return the markAllInPresale
   */
  public boolean isMarkAllInPresale()
  {
    return markAllInPresale;
  }

  /**
   * @param markAllInPresale the markAllInPresale to set
   */
  public void setMarkAllInPresale( boolean markAllInPresale )
  {
    this.markAllInPresale = markAllInPresale;
  }

  /**
   * @return the minDate
   */
  public Date getMinDatePresale()
  {
    return minDatePresale;
  }

  /**
   * @param minDate the minDate to set
   */
  public void setMinDatePresale( Date minDatePresale )
  {
    this.minDatePresale = minDatePresale;
  }

  /**
   * @return the maxDatePresale
   */
  public Date getMaxDatePresale()
  {
    return maxDatePresale;
  }

  /**
   * @return the minDateRelease
   */
  public Date getMinDateRelease()
  {
    return minDateRelease;
  }

  /**
   * @return the maxDateRelease
   */
  public Date getMaxDateRelease()
  {
    return maxDateRelease;
  }

}
