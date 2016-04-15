package mx.com.cinepolis.digital.booking.web.beans.booking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventBookingTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.bookingspecialevent.BookingSpecialEventServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

/**
 * Clase para la pantalla de Special Events
 * 
 * @author jcarbajal
 */
@ManagedBean(name = "specialEventBookingBean")
@ViewScoped
public class SpecialEventBookingBean extends BaseManagedBean
{
  /**
   * Serial version.
   */
  private static final long serialVersionUID = 7815308612641879863L;
  private static final String VALUE_ATTRIBUTE = "value";
  private static final boolean FESTIVAL = true;
  private static final boolean PRERELEASE = false;
  private static final String NO_SELECTED_MOVIE_AND_REGION_ERROR_TEXT = "booking.specialevent.mesgerror.noSelectedMovieAndRegionText";
  private static final String BOOKINGS_UNEDITED_ERROR_TEXT = "booking.movie.mesgerror.bookingsUneditedText";
  private static final String CANCEL_BOOKING_ERROR_TEXT = "booking.movie.mesgerror.cancelBookingText";
  private static final String SAVE_BOOKING_ERROR_PRESALE_TEXT = "booking.specialevent.mesgerror.savePresaleInZeroScreen";
  private static final String SAVE_BOOKING_WITHOUT_DATES_ERROR_TEXT = "booking.specialevent.mesgerror.saveSpecialEventWithoutDates";
  private static final String SAVE_BOOKING_PRESALE_WITHOUT_DATES_ERROR_TEXT = "booking.specialevent.mesgerror.savePrereleaseAsPresaleWithoutDates";
  private static final String INTERVAL_DATES_BEFORE_TODAY_ERROR_TEXT = "booking.specialevent.mesgerror.intervalDatesBeforeToday";
  private static final String INTERVAL_DATES_FINAL_BEFORE_START_ERROR_TEXT = "booking.specialevent.mesgerror.intervalDatesFinalBeforeStart";
  // private static final String RELEASE_BOFORE_INTERVAL_PRESALE_ERROR_TEXT =
  // "booking.specialevent.mesgerror.invalidRelease";

  private static final int INDEX_SCREENS_NUMBER_SELECT_MANY_BUTTON = 0;
  private static final int INDEX_SCREEN_NUMBERS_COLUMN = 3;

  private static final String ZERO_STRING = "0";
  private static final Long BOOKING_TYPE_ID = 3L;
  /**
   * Vars for apply rules
   */
  private Integer copiesRule;
  private Date startingDayRule;
  private Date finalDayRule;
  private String showsRule;
  private String notesRule;
  private boolean showdate;
  private List<CatalogTO> showListRule;
  private List<Object> showListSelectedRule;

  /**
   * List to display the catalogs Special Event and theater region
   */
  private List<CatalogTO> specialEventTOs;
  private List<CatalogTO> regions;

  private List<SpecialEventTO> bookingTheaterTOs;
  private List<EventMovieTO> eventMovieTOs;

  private Long regionId;
  private Long specialEventIdSelected;
  private List<SpecialEventTO> bookingTOs;
  private List<SpecialEventTO> bookingTOsSelected;
  private Date minDate = new Date();
  private List<Object> screensSelected;
  private List<String> lastScreenSelection = new ArrayList<String>();
  private List<String> lastGlobalShowSelection = new ArrayList<String>();
  private List<SpecialEventTO> newBookingTheater;
  private Set<Long> validStatusCancellation;
  private boolean markAllInPresale;

  /**
   * Variables for apply rules presale
   */
  private Date rulePresaleStartDate;
  private Date rulePresaleFinalDate;
  private boolean rulePresaleFlgStatus;
  private Date rulePresaleReleaseDate;

  @EJB
  private BookingServiceIntegratorEJB bookingServiceIntegratorEJB;

  @EJB
  private BookingSpecialEventServiceIntegratorEJB specialEventServiceIntegratorEJB;

  /**
   * Method that performs initializations.
   */
  @PostConstruct
  public void init()
  {
    validStatusCancellation = new HashSet<Long>();
    validStatusCancellation.add( Long.valueOf( BookingStatus.BOOKED.getId() ) );
    AbstractTO abstractTO = new AbstractTO();
    fillSessionData( abstractTO );
    specialEventTOs = this.bookingServiceIntegratorEJB.findAllActiveMovies( FESTIVAL, PRERELEASE );
    regions = this.bookingServiceIntegratorEJB.findAllActiveRegions( abstractTO );

    this.startingDayRule = new Date();
    this.finalDayRule = new Date();
    this.markAllInPresale = Boolean.FALSE;
    this.initializeshowsListRule();
    this.showdate = false;
    this.copiesRule = new Integer( 1 );
  }

  private void initializeshowsListRule()
  {
    this.showListRule = new ArrayList<CatalogTO>();
    this.showListSelectedRule = new ArrayList<Object>();

    CatalogTO catalogTO = new CatalogTO();
    catalogTO.setId( 1L );
    catalogTO.setFgActive( false );
    catalogTO.setName( "1st" );

    CatalogTO catalogTO1 = new CatalogTO();
    catalogTO1.setId( 2L );
    catalogTO1.setFgActive( true );
    catalogTO1.setName( "2nd" );

    CatalogTO catalogTO2 = new CatalogTO();
    catalogTO2.setId( 3L );
    catalogTO2.setFgActive( true );
    catalogTO2.setName( "3rd" );

    CatalogTO catalogTO3 = new CatalogTO();
    catalogTO3.setId( 4L );
    catalogTO3.setFgActive( false );
    catalogTO3.setName( "4th" );

    CatalogTO catalogTO4 = new CatalogTO();
    catalogTO4.setId( 5L );
    catalogTO4.setFgActive( true );
    catalogTO4.setName( "5th" );

    CatalogTO catalogTO5 = new CatalogTO();
    catalogTO5.setId( 6L );
    catalogTO5.setFgActive( true );
    catalogTO5.setName( "6th" );

    this.showListRule.add( catalogTO );
    this.showListRule.add( catalogTO1 );
    this.showListRule.add( catalogTO2 );
    this.showListRule.add( catalogTO3 );
    this.showListRule.add( catalogTO4 );
    this.showListRule.add( catalogTO5 );
  }

  /**
   * This method get idSpecialEvent from selctOneMenu in the form
   * 
   * @return void
   * @param event
   */
  public void setMovieId( AjaxBehaviorEvent event )
  {
    Long idSpecialEvent = (Long) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    this.setSpecialEventIdSelected( idSpecialEvent );
  }

  /**
   * This method get idRegion from selctOneMenu in the form
   * 
   * @return void
   * @param event
   */
  public void setRegionIdSelected( AjaxBehaviorEvent event )
  {
    Long idRegion = (Long) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    this.setRegionId( idRegion );
  }

  /**
   * method for cancel special event
   */
  public void cancelBooking()
  {
    this.specialEventServiceIntegratorEJB.cancelBookings( this.getBookingSelected() );
    searchTheater();
  }

  /**
   * Method that finds the theater bookings for zone.and Festival
   * 
   * @author jcarbajal
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

  /**
   * Method for load bookings in the form
   */
  private void loadBookings()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    super.fillSessionData( pagingRequestTO );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_EVENT_ID );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_REGION_ID );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_TYPE_ID );
    addFilterSpecialEvent( pagingRequestTO );
    addFilterRegion( pagingRequestTO );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_TYPE_ID, BOOKING_TYPE_ID );
    PagingResponseTO<SpecialEventTO> response = null;
    List<SpecialEventTO> result = null;
    SpecialEventBookingTO bookingSpecialEvent = this.specialEventServiceIntegratorEJB
        .findBookingSpecialEvent( pagingRequestTO );
    response = bookingSpecialEvent.getReponse();
    bookingTOs = response.getElements();
    result = response.getElements();
    applyData( result );
  }

  /**
   * The method apply the data at the view (screen,status, theater name, copies, etc) List EpecialEventTO
   * 
   * @param result
   */
  private void applyData( List<SpecialEventTO> result )
  {
    for( SpecialEventTO bookingTO : result )
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

      putPresaleAndDate( bookingTO );
      bookingTO.setDisabled( false );
    }
  }

  /**
   * Método que setea el valor de los campos "Presale" y "Date"
   * 
   * @param specialEventTO
   */
  private void putPresaleAndDate( SpecialEventTO specialEventTO )
  {

    if( specialEventTO.getStartDay() != null && specialEventTO.getFinalDay() != null )
    {
      specialEventTO.setDate( new Date() );
    }
  }

  /**
   * Method adds the filter region to the request
   * 
   * @param pagingRequestTO
   */
  private void addFilterRegion( PagingRequestTO pagingRequestTO )
  {
    if( getRegionId() != null && !getRegionId().equals( Long.valueOf( 0 ) ) )
    {
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, getRegionId() );
    }
  }

  /**
   * Method adds the filter Special Event to the request
   * 
   * @param pagingRequestTO
   */

  private void addFilterSpecialEvent( PagingRequestTO pagingRequestTO )
  {
    if( getSpecialEventIdSelected() != null && !getSpecialEventIdSelected().equals( Long.valueOf( 0 ) ) )
    {
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_ID, getSpecialEventIdSelected() );
    }
  }

  /**
   * Method to save bookings
   */
  public void saveBooking()
  {
    List<SpecialEventTO> bookingsForSaving = new ArrayList<SpecialEventTO>();
    for( SpecialEventTO bookingSpecialEventTO : getBookingsForSave() )
    {
      int copy = bookingSpecialEventTO.getCopy();
      int screens = 0;
      if( bookingSpecialEventTO.getScreensSelected().size() == 1 )
      {
        if( !Long.valueOf( bookingSpecialEventTO.getScreensSelected().get( 0 ).toString() ).equals( 0L ) )
        {
          screens = bookingSpecialEventTO.getScreensSelected().size();
        }
      }
      else
      {
        screens = bookingSpecialEventTO.getScreensSelected().size();
      }
      bookingSpecialEventTO.setCopy( screens );
      bookingSpecialEventTO.setCopyScreenZero( copy - screens );
      bookingSpecialEventTO.setCopyScreenZeroTerminated( 0 );
      bookingSpecialEventTO.setIdBookingType( BOOKING_TYPE_ID );
      bookingsForSaving.add( bookingSpecialEventTO );
      /* Se actualiza la preventa en caso de que aplique */
      if( CollectionUtils.isNotEmpty( bookingSpecialEventTO.getScreens() ) )
      {
        this.updatePresaleForScreenTO( bookingSpecialEventTO.getScreens(), bookingSpecialEventTO.getPresaleTO() );
      }
    }
    this.specialEventServiceIntegratorEJB.saveOrUpdateBookings( bookingsForSaving );
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

  /**
   * Get booking special event for each theater
   * 
   * @return List of SpecialEventTO
   */
  private List<SpecialEventTO> getBookingsForSave()
  {
    List<SpecialEventTO> bookingForSaving = new ArrayList<SpecialEventTO>();
    for( SpecialEventTO bookingSpecialEventTO : this.bookingTOsSelected )
    {
      bookingSpecialEventTO
          .setScreenAvailable( bookingSpecialEventTO.getScreensSelected().size() < bookingSpecialEventTO.getCopy() );
      super.fillSessionData( bookingSpecialEventTO );
      if( bookingSpecialEventTO.getId() == null )
      {
        EventTO eventTO = new EventTO();
        eventTO.setIdEvent( this.specialEventIdSelected );
        bookingSpecialEventTO.setEvent( eventTO );
        if( bookingSpecialEventTO.getCopy() != 0 )
        {
          validateNumberOfCopies( bookingSpecialEventTO );
          if( hasMoreScreensThanCopies( bookingSpecialEventTO ) )
          {
            throwMoreScreensThanCopiesException( bookingSpecialEventTO );
          }
          else
          {
            putEvent( bookingSpecialEventTO );
            List<ScreenTO> screensSelecteds = createScreenTO( bookingSpecialEventTO );
            bookingSpecialEventTO.setScreens( screensSelecteds );
            bookingForSaving.add( bookingSpecialEventTO );
          }
        }
        else
        {// Cancellation of unsaved booking is not possible
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_NOT_SAVED_FOR_CANCELLATION,
            new Object[] { bookingSpecialEventTO.getTheater().getName(),
                bookingSpecialEventTO.getBookingObservation().getObservation() } );
        }
      }
      else
      {// Posiblemente tambien se deba de validar el status continued
        if( bookingSpecialEventTO.getStatus().getId().equals( BookingStatus.BOOKED.getIdLong() ) )
        {
          EventTO eventTO = new EventTO();
          eventTO.setIdEvent( this.specialEventIdSelected );
          bookingSpecialEventTO.setEvent( eventTO );
          this.validateNumberOfCopies( bookingSpecialEventTO );
          this.validateMoreScreensThanCopies( bookingSpecialEventTO );
          this.getBookingsForUpdate( bookingForSaving, bookingSpecialEventTO );
        }
      }
    }
    return bookingForSaving;
  }

  /**
   * 
   */
  private void putEvent( SpecialEventTO especialEventTO )
  {
    EventTO eventTO = new EventTO();
    eventTO.setId( this.getSpecialEventIdSelected() );
    especialEventTO.setEvent( eventTO );
  }

  /**
   * Method gets bookings for update
   * 
   * @param bookingsForSaving
   * @param bookingSpecialEventTO
   */
  private void getBookingsForUpdate( List<SpecialEventTO> bookingsForSaving, SpecialEventTO bookingSpecialEventTO )
  {
    if( bookingSpecialEventTO.getStatus().getId().equals( BookingStatus.CANCELED.getIdLong() ) )
    {
      if( bookingSpecialEventTO.getCopy() != 0 )
      {
        CatalogTO statusTO = new CatalogTO();
        statusTO.setId( BookingStatus.BOOKED.getIdLong() );
        bookingSpecialEventTO.setStatus( statusTO );
        List<ScreenTO> screensSelecteds = createScreenTO( bookingSpecialEventTO );
        bookingSpecialEventTO.setScreens( screensSelecteds );
        bookingsForSaving.add( bookingSpecialEventTO );
      }
    }
    else if( bookingSpecialEventTO.getStatus().getId().equals( BookingStatus.BOOKED.getIdLong() )
        && bookingSpecialEventTO.getCopy() == 0 )
    {
      bookingSpecialEventTO.setScreens( new ArrayList<ScreenTO>() );
      bookingsForSaving.add( bookingSpecialEventTO );
    }
    else
    {
      List<ScreenTO> screensSelecteds = createScreenTO( bookingSpecialEventTO );
      bookingSpecialEventTO.setScreens( screensSelecteds );
      bookingsForSaving.add( bookingSpecialEventTO );
    }
    // Se llenan los datos de sesión para el objeto PresaleTO en caso de que su id no sea null.
    if( bookingSpecialEventTO.getPresaleTO().getIdPresale() != null )
    {
      super.fillSessionData( bookingSpecialEventTO.getPresaleTO() );
    }
  }

  /**
   * Method valid that the number of the screen must be bigger than the number of copies
   * 
   * @param bookingTO
   */
  private void validateMoreScreensThanCopies( SpecialEventTO bookingTO )
  {
    if( hasMoreScreensThanCopies( bookingTO ) )
    {
      throwMoreScreensThanCopiesException( bookingTO );
    }
  }

  /**
   * Method for create screens for each theater
   * 
   * @param bookingspecialEventTO
   * @return List ScreenTo
   */
  private List<ScreenTO> createScreenTO( SpecialEventTO bookingspecialEventTO )
  {
    List<ScreenTO> screens = new ArrayList<ScreenTO>();
    for( Object ob : bookingspecialEventTO.getScreensSelected() )
    {
      ScreenTO screen = new ScreenTO();
      screen.setId( Long.valueOf( ob.toString() ) );

      /* jreyesv: Se obtiene la preventa de la sala anterior */
      SpecialEventScreenTO specialEventScreenTOSelected = this.extractSpecialEventScreenTO( bookingspecialEventTO,
        screen );
      if( specialEventScreenTOSelected != null )
      {
        screen.setPresaleTO( specialEventScreenTOSelected.getPresaleTO() );
      }
      else
      {
        screen.setPresaleTO( bookingspecialEventTO.getPresaleTO() );
      }

      ScreenTO screenFromTheater = (ScreenTO) CollectionUtils.find( bookingspecialEventTO.getTheater().getScreens(),
        PredicateUtils.equalPredicate( screen ) );
      if( screenFromTheater != null )
      {
        int index = bookingspecialEventTO.getTheater().getScreens().indexOf( screenFromTheater );
        screen.setNuScreen( screenFromTheater.getNuScreen() );
        screen.setShowings( createShows( bookingspecialEventTO, index ) );
      }
      screens.add( screen );
      if( bookingspecialEventTO.getStatus() != null )
      {
        screen.setBookingStatus( bookingspecialEventTO.getStatus() );
      }
    }
    return screens;
  }

  /**
   * Method that extracts the {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO} object.
   * 
   * @param bookingspecialEventTO, with the booking special event information.
   * @param screen, with the screen information.
   * @return {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO} object.
   */
  private SpecialEventScreenTO extractSpecialEventScreenTO( SpecialEventTO bookingspecialEventTO, ScreenTO screen )
  {
    SpecialEventScreenTO specialEventScreenTOSelected = null;
    for( SpecialEventScreenTO specialEventScreenTO : bookingspecialEventTO.getSpecialEventScreens() )
    {
      if( specialEventScreenTO.getIdScreen().equals( screen.getId() ) )
      {
        specialEventScreenTOSelected = specialEventScreenTO;
        break;
      }
    }
    return specialEventScreenTOSelected;
  }

  /**
   * The method create a list of shows
   * 
   * @param SpecislEvent ,int
   * @return List Catalog
   */
  private List<CatalogTO> createShows( SpecialEventTO specialEvent, int index )
  {
    List<CatalogTO> shows = new ArrayList<CatalogTO>();
    for( Object obj : specialEvent.getShowingsSelected() )
    {
      CatalogTO show = new CatalogTO();
      super.fillSessionData( show );
      show.setId( Long.valueOf( obj.toString() ) );

      CatalogTO showFromScreen = (CatalogTO) CollectionUtils.find( specialEvent.getTheater().getScreens().get( index )
          .getShowings(), PredicateUtils.equalPredicate( show ) );
      if( showFromScreen != null )
      {
        show.setId( showFromScreen.getId() );
        show.setFgActive( true );
      }
      shows.add( show );
    }

    return shows;

  }

  /**
   * Method that valid that the copies must be minor than the number of screens
   * 
   * @param bookingSpecialEventTO
   * @return boolean
   */
  private boolean hasMoreScreensThanCopies( SpecialEventTO bookingSpecialEvent )
  {
    return bookingSpecialEvent.getScreensSelected().size() > bookingSpecialEvent.getCopy();
  }

  /**
   * The method send the exception invalid number of copies
   * 
   * @param bookingSpecialEventTO
   */
  private void throwMoreScreensThanCopiesException( SpecialEventTO bookingSpecialEventTO )
  {
    throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_NUM_SCREENS_GREATER_NUM_COPIES,
      new Object[] { bookingSpecialEventTO.getTheater().getName() } );
  }

  /**
   * Valid number of copies
   * 
   * @param bookingSpecialEventTO
   */
  private void validateNumberOfCopies( SpecialEventTO bookingSpecialEventTO )
  {
    validateAvaliableScreens( bookingSpecialEventTO );
    if( bookingSpecialEventTO.getCopy() >= bookingSpecialEventTO.getTheater().getScreens().size() )
    {
      throw DigitalBookingExceptionBuilder.build(
        DigitalBookingExceptionCode.BOOKING_NUMBER_OF_COPIES_EXCEEDS_NUMBER_OF_SCREENS,
        new Object[] { bookingSpecialEventTO.getTheater().getName() } );
    }
  }

  /**
   * 
   */
  private int getAvailableScreen( SpecialEventTO bookingSpecialEvent )
  {
    int numScreens = 0;
    if( CollectionUtils.isNotEmpty( bookingSpecialEvent.getTheater().getScreens() ) )
    {
      for( ScreenTO screen : bookingSpecialEvent.getTheater().getScreens() )
      {
        if( !screen.getDisabled() )
        {
          numScreens++;
        }
      }
    }
    return numScreens;
  }

  /**
   * @param bookingTO
   * @return
   */
  private void validateAvaliableScreens( SpecialEventTO bookingSpecialEvent )
  {
    int numberScreen = getAvailableScreen( bookingSpecialEvent );
    if( bookingSpecialEvent.getCopy() >= numberScreen )
    {
      throw DigitalBookingExceptionBuilder.build(
        DigitalBookingExceptionCode.BOOKING_NUMBER_OF_COPIES_EXCEEDS_NUMBER_OF_SCREENS_WHIT_THIS_FORMAT,
        new Object[] { bookingSpecialEvent.getTheater().getName() } );
    }
  }

  /**
   * Method to change value in shows
   * 
   * @param event
   * @author jcarbajal
   */
  @SuppressWarnings("unchecked")
  public void changeShowsValue( ValueChangeEvent event )
  {
    if( event.getOldValue() != null )
    {
      lastGlobalShowSelection = parseStringSelection( (List<Object>) event.getOldValue() );
    }
  }

  /**
   * Method to load screens in the form
   * 
   * @param event
   * @author jcarbajal
   */
  @SuppressWarnings("unchecked")
  public void loadScreens( AjaxBehaviorEvent event )
  {
    String zeroString = ZERO_STRING;
    Long zeroLong = Long.valueOf( 0L );
    List<Object> screensSelectedl = ((List<Object>) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE ));
    List<String> stringsScreensSelected = parseStringSelection( screensSelectedl );
    if( !lastScreenSelection.contains( zeroString ) && stringsScreensSelected.contains( zeroString ) )
    {
      screensSelectedl.retainAll( Arrays.asList( zeroString, zeroLong ) );
    }
    else if( lastScreenSelection.contains( zeroString ) && stringsScreensSelected.size() > lastScreenSelection.size() )
    {
      screensSelectedl.remove( zeroString );
      screensSelectedl.remove( zeroLong );
    }
    else if( CollectionUtils.isEmpty( stringsScreensSelected ) )
    {
      screensSelectedl.add( zeroString );
    }
  }

  /**
   * Parse the objects selected in list of strings
   * 
   * @author jcarbajal
   * @param oldObjectSelection
   * @return List String
   */
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
   * Parse the list of objects screens selected to list of strings
   * 
   * @param event
   * @author jcarbajal
   */
  @SuppressWarnings("unchecked")
  public void changeValue( ValueChangeEvent event )
  {
    if( event.getOldValue() != null )
    {
      lastScreenSelection = parseStringSelection( (List<Object>) event.getOldValue() );
    }
  }

  /**
   * Method that ensures a Festival and a region are selected.
   * 
   * @return {@link Boolean} <code>true</code> if a festival and a region are selected, <code>false</code> otherwise.
   * @author jcarbajal
   */
  private boolean validateFilters()
  {
    Boolean isValid = true;
    if( !validateIdSelected( specialEventIdSelected ) || !validateIdSelected( regionId ) )
    {
      isValid = false;
      createMessageError( NO_SELECTED_MOVIE_AND_REGION_ERROR_TEXT );
    }
    return isValid;
  }

  /**
   * Valid the id
   * 
   * @param id
   * @return boolean
   */
  private boolean validateIdSelected( Long id )
  {
    Boolean isValid = true;
    if( id == null || id.equals( Long.valueOf( 0 ) ) )
    {
      isValid = false;
    }
    return isValid;
  }

  /**
   * Listener method for the reset button of the form used to specify filters for the theater search.
   * 
   * @author jcarbajal
   */
  public void resetForm()
  {
    this.specialEventIdSelected = null;
    this.regionId = null;
    this.bookingTOs = null;
    this.bookingTOsSelected = null;
    this.resetFormRules();
  }

  /**
   * Método que resetea los datos del formulario "parametros de configuración" en la vista.
   */
  private void resetFormRules()
  {
    this.copiesRule = new Integer( 1 );
    this.startingDayRule = new Date();
    this.finalDayRule = new Date();
    this.initializeshowsListRule();
    this.notesRule = null;
    this.showdate = false;
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

  /**
   * Method that validates whether exist a booking with different Starting and Release dates.
   * 
   * @param specialEventTO, the {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO} to save.
   */
  private void validateReleaseDate( SpecialEventTO specialEventTO )
  {
    if( specialEventTO.getStartDay() == null || !DateUtils.isSameDay( specialEventTO.getStartDay(), this.rulePresaleReleaseDate ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INVALID_STARTING_AND_RELREASE_DATES,
        new Object[] { specialEventTO.getTheater().getName() } );
    }
  }

  /**
   * Method that validates whether exist a booking with invalid final dates.
   * 
   * @param specialEventTO, the {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO} to save.
   */
  private void validateFinalDates( SpecialEventTO specialEventTO )
  {
    if( specialEventTO.getFinalDay() == null
        || this.rulePresaleFinalDate.after( specialEventTO.getFinalDay() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INVALID_FINAL_DATES,
        new Object[] { specialEventTO.getTheater().getName() } );
    }
  }

  /**
   * Method that validates whether exist a booking with invalid starting dates.
   * 
   * @param specialEventTO, the {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO} to save.
   */
  private void validateStartingDates( SpecialEventTO specialEventTO )
  {
    if( specialEventTO.getFinalDay() == null
        || this.rulePresaleStartDate.after( specialEventTO.getStartDay() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INVALID_STARTING_DATES,
        new Object[] { specialEventTO.getTheater().getName() } );
    }
  }

  /**
   * Method that applies the presale configuration specified for bookings selected.
   */
  public void applyPresaleConfiguration()
  {
    if( this.validateBookingSelection() && this.validatePresaleConfiguration() )
    {
      for( SpecialEventTO specialEventTO : this.bookingTOs )
      {
        if( CollectionUtils.exists(
          this.bookingTOsSelected,
          PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getTheater" ),
            PredicateUtils.equalPredicate( specialEventTO.getTheater() ) ) ) )
        {
          this.validateReleaseDate( specialEventTO );
          this.validateStartingDates( specialEventTO );
          this.validateFinalDates( specialEventTO );
          this.setPresaleConfigurationToBooking( specialEventTO );
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
    /*
     * TODO: Se quita validación, ya que la preventa puede durar hasta el final de la duración del evento especial. if(
     * isValid && this.rulePresaleReleaseDate.before( this.rulePresaleFinalDate ) ) { isValid = false;
     * createMessageError( RELEASE_BOFORE_INTERVAL_PRESALE_ERROR_TEXT ); }
     */
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
    return isValid;
  }

  /**
   * Method that sets the value of presale parameters to a
   * {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO} object.
   * 
   * @param specialEventTO, a {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO} object without presale
   *          configuration.
   * @return specialEventTO, a {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO} object with presale
   *         configuration.
   */
  private SpecialEventTO setPresaleConfigurationToBooking( SpecialEventTO specialEventTO )
  {
    if( this.rulePresaleStartDate != null )
    {
      specialEventTO.getPresaleTO().setDtStartDayPresale( this.rulePresaleStartDate );
    }
    if( this.rulePresaleFinalDate != null )
    {
      specialEventTO.getPresaleTO().setDtFinalDayPresale( this.rulePresaleFinalDate );
    }
    if( this.rulePresaleReleaseDate != null )
    {
      specialEventTO.getPresaleTO().setDtReleaseDay( this.rulePresaleReleaseDate );
      specialEventTO.getPresaleTO().setStrReleaseDate( "" );
      this.rulePresaleFlgStatus = true;
    }
    if( this.rulePresaleFlgStatus )
    {
      this.changePresaleTOStatus( specialEventTO, this.rulePresaleFlgStatus );
    }
    specialEventTO.getPresaleTO().setStrPresaleDates( "" );
    return specialEventTO;
  }

  /**
   * Method to apply parameters in the theaters
   */
  public void applyRulesToBooking()
  {
    if( validateRulesToBooking() && validateBookingSelection() )
    {
      List<SpecialEventTO> bookingsSelected = getBookingSelected();
      for( SpecialEventTO specialEventTO : this.bookingTOs )
      {
        if( CollectionUtils.exists(
          bookingsSelected,
          PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( "getTheater" ),
            PredicateUtils.equalPredicate( specialEventTO.getTheater() ) ) ) )
        {
          this.setRulesToBooking( specialEventTO );
        }
      }
      this.resetFormRules();
      setBookingTOsSelected( null );
    }
    else
    {
      validationFail();
    }
  }

  /**
   * @param specialEventTO
   * @return
   */
  private SpecialEventTO setRulesToBooking( SpecialEventTO specialEventTO )
  {
    if( this.copiesRule != null && this.copiesRule > 0 )
    {
      specialEventTO.setCopy( this.copiesRule );
    }
    if( StringUtils.isNotBlank( this.notesRule ) )
    {
      specialEventTO.setNotes( this.notesRule );
    }
    if( this.startingDayRule != null )
    {
      specialEventTO.setStartDay( this.startingDayRule );
    }
    if( this.finalDayRule != null )
    {
      specialEventTO.setFinalDay( this.finalDayRule );
    }
    if( this.startingDayRule != null && this.finalDayRule != null )
    {
      specialEventTO.setDate( new Date() );
    }
    if( CollectionUtils.isNotEmpty( this.showListSelectedRule ) )
    {
      specialEventTO.setShowingsSelected( this.showListSelectedRule );
    }
    if( this.showdate )
    {
      specialEventTO.setShowDate( Boolean.TRUE );
    }
    specialEventTO.setShowDate( this.showdate );
    return specialEventTO;
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
    if( newValue instanceof Integer )
    {
      if( newValue != null )
      {
        int value = (Integer) newValue;
        if( value == 0 )
        {
          List<Object> screensSelecteds = (List<Object>) event.getComponent().getParent().getParent().getChildren()
              .get( INDEX_SCREEN_NUMBERS_COLUMN ).getChildren().get( INDEX_SCREENS_NUMBER_SELECT_MANY_BUTTON )
              .getAttributes().get( VALUE_ATTRIBUTE );
          screensSelecteds.clear();
          screensSelecteds.add( 0L );
        }
      }
    }
  }

  /**
   * metodo para validar que al momento de aplicar reglas(copies,notes,dates,etc) tenga selecionado theater, y screens
   * 
   * @return
   */
  private boolean validateScreensSelectedToApplyScreens()
  {
    boolean valid = true;
    if( CollectionUtils.isNotEmpty( this.showListSelectedRule ) )
    {
      if( CollectionUtils.isNotEmpty( this.bookingTOsSelected ) )
      {
        // List<SpecialEventTO> specialEventTO=this.bookingTOsSelected;
        for( SpecialEventTO specialEventTO : this.bookingTOsSelected )
        {
          if( CollectionUtils.isEmpty( specialEventTO.getScreensSelected() ) )
          {
            valid = false;
            throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INVALID_SCREEN_SELECTION );
          }
        }
      }
      else
      {
        // mandar Exception
        valid = false;
        throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INVALID_THEATER_SELECTION );
      }
    }
    return valid;
  }

  /**
   * Método que obtiene el registro seleccionado
   * 
   * @return BookingTO
   */
  private List<SpecialEventTO> getBookingSelected()
  {
    List<SpecialEventTO> bookingSelected = bookingTOsSelected;
    for( SpecialEventTO booking : bookingTOsSelected )
    {
      super.fillSessionData( booking );
    }

    return bookingSelected;
  }

  /**
   * Validate and apply the input parameters to the booking list
   */
  private boolean validateRulesToBooking()
  {
    boolean valid = true;
    Calendar cal = Calendar.getInstance();
    cal.set( Calendar.HOUR_OF_DAY, 00 );
    cal.set( Calendar.MINUTE, 00 );
    cal.set( Calendar.SECOND, 0 );
    cal.set( Calendar.MILLISECOND, 0 );
    Date today = cal.getTime();
    /**
     * Lanza excepción si el valor de los parámetros startingDayRule y finalDayRule no es null, y el valor del parametro
     * finalDayRule es menor al valor del parametro startingDayRule
     */
    if( this.isDatesForApplyNull() && this.finalDayRule.before( this.startingDayRule ) )
    {
      valid = false;
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INVALID_DATES_PARAMETERS );
    }
    else if( this.isDatesForApplyNull() && this.startingDayRule.before( today ) )
    {
      valid = false;
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INVALID_DATES_BEFORE_TODAY_PARAMETERS );
    }
    valid = validateScreensSelectedToApplyScreens();
    return valid;
  }

  /**
   * Validates whether the starting and final dates are different to null.
   * 
   * @return isDatesForApplyNull
   */
  private boolean isDatesForApplyNull()
  {
    return (this.finalDayRule != null && this.startingDayRule != null);
  }

  /**
   * Validate parameters and selection records before saving
   * 
   * @return void
   **/
  public void validationSaveBooking()
  {
    if( validateBookingSelection() )
    {
      boolean isValid = true;
      List<SpecialEventTO> bookingsForSaving = getBookingsForSave();
      if( CollectionUtils.isEmpty( bookingsForSaving ) )
      {
        createMessageError( BOOKINGS_UNEDITED_ERROR_TEXT );
        validationFail();
      }
      else
      {
        isValid = this.validateDateInBooking();
        if( isValid )
        {
          isValid = this.validatePresaleInZeroScreen();
        }
        if( isValid )
        {
          isValid = this.validateDatesForPresaleInBooking();
        }
      }
    }
    else
    {
      validationFail();
    }
  }

  /**
   * Método que valida que las programaciones tengan fecha seleccionada.
   * 
   * @return isValid, con el resultado de la validación.
   */
  private boolean validateDateInBooking()
  {
    boolean isValid = true;
    for( SpecialEventTO specialEventTO : this.bookingTOsSelected )
    {
      if( specialEventTO.getStartDay() == null || specialEventTO.getFinalDay() == null )
      {
        isValid = false;
        createMessageError( SAVE_BOOKING_WITHOUT_DATES_ERROR_TEXT );
        validationFail();
        break;
      }
    }
    return isValid;
  }

  /**
   * Method that validates the date fields for a presale.
   * 
   * @return isValid, con el resultado de la validación.
   */
  private boolean validateDatesForPresaleInBooking()
  {
    boolean isValid = true;
    for( SpecialEventTO specialEventTO : this.bookingTOsSelected )
    {
      if( specialEventTO.getPresaleTO().isFgActive() )
      {
        if( specialEventTO.getPresaleTO().getDtStartDayPresale() == null
            || specialEventTO.getPresaleTO().getDtFinalDayPresale() == null
            || specialEventTO.getPresaleTO().getDtReleaseDay() == null )
        {
          isValid = false;
          createMessageError( SAVE_BOOKING_PRESALE_WITHOUT_DATES_ERROR_TEXT );
          validationFail();
          break;
        }
      }
    }
    return isValid;
  }

  /**
   * Método que valida que no exista una programación en sala cero marcada como preventa.
   * 
   * @return isValid, con el resultado de la validación.
   */
  private boolean validatePresaleInZeroScreen()
  {
    boolean isValid = true;
    firstLoop: for( SpecialEventTO specialEventTO : this.bookingTOsSelected )
    {
      if( CollectionUtils.isNotEmpty( specialEventTO.getScreensSelected() ) )
      {
        for( Object screenSelected : specialEventTO.getScreensSelected() )
        {
          if( screenSelected.toString().equals( "0" ) && specialEventTO.getPresaleTO().isFgActive() )
          {
            isValid = false;
            createMessageError( SAVE_BOOKING_ERROR_PRESALE_TEXT );
            validationFail();
            break firstLoop;
          }
        }
      }
    }
    return isValid;
  }

  /**
   * Validates the selected records before cancel
   */
  public void validateCancelBooking()
  {
    if( validateBookingSelection() )
    {

      for( SpecialEventTO bookingSpecialEventTO : this.bookingTOsSelected )
      {
        CatalogTO statusTO = bookingSpecialEventTO.getStatus();
        if( statusTO == null || !validStatusCancellation.contains( statusTO.getId() ) )
        {
          createMessageError( CANCEL_BOOKING_ERROR_TEXT );
          validationFail();
        }
        else if( bookingSpecialEventTO.getId() == null )
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
   * Validates whether are selected items
   * 
   * @return boolean isValid
   */
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

  /**
   * Marks or unmarks all special event bookings like presale.
   */
  public void markAllBookingsInPresale()
  {
    if( CollectionUtils.isNotEmpty( this.bookingTOs ) )
    {
      for( SpecialEventTO specialEventTO : this.bookingTOs )
      {
        if( this.markAllInPresale )
        {
          this.changePresaleTOStatus( specialEventTO, Boolean.TRUE );
        }
        else
        {
          this.changePresaleTOStatus( specialEventTO, Boolean.FALSE );
        }
      }
    }
  }

  /**
   * Method that changes the presale status for a booking.
   * 
   * @param bookingTO, a {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO} object with the information
   *          of booking to change.
   * @param status, variable with the presale status to set in booking.
   */
  private void changePresaleTOStatus( SpecialEventTO specialEventTO, boolean status )
  {
    if( specialEventTO.getPresaleTO() != null )
    {
      specialEventTO.getPresaleTO().setFgActive( status );
    }
    else
    {
      specialEventTO.setPresaleTO( new PresaleTO( null, status ) );
    }
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
   * @return the specialEventIdSelected
   */
  public Long getSpecialEventIdSelected()
  {
    return specialEventIdSelected;
  }

  /**
   * @param specialEventIdSelected the specialEventIdSelected to set
   */
  public void setSpecialEventIdSelected( Long specialEventIdSelected )
  {
    this.specialEventIdSelected = specialEventIdSelected;
  }

  /**
   * @return the specialEventTOs
   */
  public List<CatalogTO> getSpecialEventTOs()
  {
    return specialEventTOs;
  }

  /**
   * @param specialEventTOs the specialEventTOs to set
   */
  public void setSpecialEventTOs( List<CatalogTO> specialEventTOs )
  {
    this.specialEventTOs = specialEventTOs;
  }

  /**
   * @return the copiesRule
   */
  public Integer getCopiesRule()
  {
    return copiesRule;
  }

  /**
   * @param copiesRule the copiesRule to set
   */
  public void setCopiesRule( Integer copiesRule )
  {
    this.copiesRule = copiesRule;
  }

  /**
   * @return the startingDayRule
   */
  public Date getStartingDayRule()
  {
    return CinepolisUtils.enhancedClone( startingDayRule );
  }

  /**
   * @param startingDayRule the startingDayRule to set
   */
  public void setStartingDayRule( Date startingDayRule )
  {
    this.startingDayRule = CinepolisUtils.enhancedClone( startingDayRule );
  }

  /**
   * @return the finalDayRule
   */
  public Date getFinalDayRule()
  {
    return CinepolisUtils.enhancedClone( finalDayRule );
  }

  /**
   * @param finalDayRule the finalDayRule to set
   */
  public void setFinalDayRule( Date finalDayRule )
  {
    this.finalDayRule = CinepolisUtils.enhancedClone( finalDayRule );
  }

  /**
   * @return the showsRule
   */
  public String getShowsRule()
  {
    return showsRule;
  }

  /**
   * @param showsRule the showsRule to set
   */
  public void setShowsRule( String showsRule )
  {
    this.showsRule = showsRule;
  }

  /**
   * @return the notesRule
   */
  public String getNotesRule()
  {
    return notesRule;
  }

  /**
   * @param notesRule the notesRule to set
   */
  public void setNotesRule( String notesRule )
  {
    this.notesRule = notesRule;
  }

  public List<Object> getScreensSelected()
  {
    return screensSelected;
  }

  public void setScreensSelected( List<Object> screensSelected )
  {
    this.screensSelected = screensSelected;
  }

  /**
   * @return the bookingTheaterTOs
   */
  public List<SpecialEventTO> getBookingTheaterTOs()
  {
    return bookingTheaterTOs;
  }

  /**
   * @param bookingTheaterTOs the bookingTheaterTOs to set
   */
  public void setBookingTheaterTOs( List<SpecialEventTO> bookingTheaterTOs )
  {
    this.bookingTheaterTOs = bookingTheaterTOs;
  }

  /**
   * @return the bookingTOs
   */
  public List<SpecialEventTO> getBookingTOs()
  {
    return bookingTOs;
  }

  /**
   * @param bookingTOs the bookingTOs to set
   */
  public void setBookingTOs( List<SpecialEventTO> bookingTOs )
  {
    this.bookingTOs = bookingTOs;
  }

  /**
   * @return the bookingTOsSelected
   */
  public List<SpecialEventTO> getBookingTOsSelected()
  {
    return bookingTOsSelected;
  }

  /**
   * @param bookingTOsSelected the bookingTOsSelected to set
   */
  public void setBookingTOsSelected( List<SpecialEventTO> bookingTOsSelected )
  {
    this.bookingTOsSelected = bookingTOsSelected;
  }

  public List<SpecialEventTO> getNewBookingTheater()
  {
    return newBookingTheater;
  }

  public void setNewBookingTheater( List<SpecialEventTO> newBookingTheater )
  {
    this.newBookingTheater = newBookingTheater;
  }

  /**
   * @return the showListRule
   */
  public List<CatalogTO> getShowListRule()
  {
    return showListRule;
  }

  /**
   * @param showListRule the showListRule to set
   */
  public void setShowListRule( List<CatalogTO> showListRule )
  {
    this.showListRule = showListRule;
  }

  /**
   * @return the showListSelectedRule
   */
  public List<Object> getShowListSelectedRule()
  {
    return showListSelectedRule;
  }

  /**
   * @param showListSelectedRule the showListSelectedRule to set
   */
  public void setShowListSelectedRule( List<Object> showListSelectedRule )
  {
    this.showListSelectedRule = showListSelectedRule;
  }

  /**
   * @return the lastGlobalShowSelection
   */
  public List<String> getLastGlobalShowSelection()
  {
    return lastGlobalShowSelection;
  }

  /**
   * @param lastGlobalShowSelection the lastGlobalShowSelection to set
   */
  public void setLastGlobalShowSelection( List<String> lastGlobalShowSelection )
  {
    this.lastGlobalShowSelection = lastGlobalShowSelection;
  }

  /**
   * @return the showdate
   */
  public boolean isShowdate()
  {
    return showdate;
  }

  /**
   * @param showdate the showdate to set
   */
  public void setShowdate( boolean showdate )
  {
    this.showdate = showdate;
  }

  /**
   * @return the minDate
   */
  public Date getMinDate()
  {
    return CinepolisUtils.enhancedClone( minDate );
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

}
