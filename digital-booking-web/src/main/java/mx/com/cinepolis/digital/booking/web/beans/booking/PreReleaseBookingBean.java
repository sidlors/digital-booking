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
 * Controlador para la programación de eventos especiales.
 * 
 * @author jreyesv
 */
@ManagedBean(name = "preReleaseBookingBean")
@ViewScoped
public class PreReleaseBookingBean extends BaseManagedBean
{

  /**
   * Serial version.
   */
  private static final long serialVersionUID = -2985862162939683628L;
  private static final String VALUE_ATTRIBUTE = "value";
  private static final boolean FESTIVAL = false;
  private static final boolean PRERELEASE = true;
  private static final String NO_SELECTED_MOVIE_AND_REGION_ERROR_TEXT = "booking.prerelease.mesgerror.noSelectedMovieAndRegionText";
  private static final String BOOKINGS_UNEDITED_ERROR_TEXT = "booking.prerelease.mesgerror.bookingsUneditedText";
  private static final String CANCEL_BOOKING_ERROR_TEXT = "booking.movie.mesgerror.cancelBookingText";
  private static final String SAVE_BOOKING_ERROR_PRESALE_TEXT = "booking.prerelease.mesgerror.savePresaleInZeroScreen";
  private static final String SAVE_BOOKING_WITHOUT_DATES_ERROR_TEXT = "booking.prerelease.mesgerror.savePrereleaseWithoutDates";
  private static final String SAVE_BOOKING_PRESALE_WITHOUT_DATES_ERROR_TEXT = "booking.prerelease.mesgerror.savePrereleaseAsPresaleWithoutDates";
  private static final String INTERVAL_DATES_BEFORE_TODAY_ERROR_TEXT = "booking.prerelease.mesgerror.intervalDatesBeforeToday";
  private static final String INTERVAL_DATES_FINAL_BEFORE_START_ERROR_TEXT = "booking.prerelease.mesgerror.intervalDatesFinalBeforeStart";
  //private static final String RELEASE_BOFORE_INTERVAL_PRESALE_ERROR_TEXT = "booking.prerelease.mesgerror.invalidRelease";

  private static final String ZERO_STRING = "0";
  private static final Long BOOKING_TYPE_ID = 2L;
  private static final Integer MIN_SCREEN_SELECTED = 1;

  /**
   * Vars for apply rules.
   */
  private Integer copiesRule;
  private Date startingDayRule;
  private Date finalDayRule;
  private String notesRule;
  private List<CatalogTO> showListRule;
  private List<Object> showListSelectedRule;
  private boolean showDate;
  private Date minDate = new Date();

  private List<SpecialEventTO> bookingTOs;
  private List<SpecialEventTO> bookingTOsSelected;
  private List<EventMovieTO> eventMovieTOs;
  private List<CatalogTO> regions;
  private List<CatalogTO> specialEventTOs;
  private Long regionId;
  private Long specialEventIdSelected;
  private List<String> lastScreenSelection = new ArrayList<String>();
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
   * Método para la inicialización del controlador.
   */
  @PostConstruct
  public void init()
  {
    this.validStatusCancellation = new HashSet<Long>();
    this.validStatusCancellation.add( Long.valueOf( BookingStatus.BOOKED.getId() ) );
    AbstractTO abstractTO = new AbstractTO();
    fillSessionData( abstractTO );
    specialEventTOs = this.bookingServiceIntegratorEJB.findAllActiveMovies( FESTIVAL, PRERELEASE );
    regions = this.bookingServiceIntegratorEJB.findAllActiveRegions( abstractTO );

    this.startingDayRule = new Date();
    this.finalDayRule = new Date();

    this.initializeshowsListRule();
    this.bookingTOs = null;
    this.bookingTOsSelected = null;
    this.markAllInPresale = Boolean.FALSE;
    this.showDate = false;
    this.copiesRule = new Integer( 1 );
  }

  /**
   * Método que inicializa la lista de funciones de los parámetros de configuración.
   */
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
   * Método que establece el valor del evento especial seleccionado.
   * 
   * @param event
   */
  public void setMovieId( AjaxBehaviorEvent event )
  {
    Long idSpecialEvent = (Long) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    this.setSpecialEventIdSelected( idSpecialEvent );
  }

  /**
   * Método que establece el valor de la región seleccionada.
   * 
   * @param event
   */
  public void setRegionIdSelected( AjaxBehaviorEvent event )
  {
    Long idRegion = (Long) event.getComponent().getAttributes().get( VALUE_ATTRIBUTE );
    this.setRegionId( idRegion );
  }

  /**
   * Método para cancelar la programación.
   */
  public void cancelBooking()
  {
    this.specialEventServiceIntegratorEJB.cancelBookings( this.getBookingSelected() );
    this.searchTheater();
  }

  /**
   * Método para buscar la programación de eventos especiales tomando como parametros un evento especial y una región.
   * 
   * @author jcarbajal
   */
  public void searchTheater()
  {
    if( this.validateFilters() )
    {
      validateTheatersInRegion();
      this.loadBookings();
      this.setBookingTOsSelected( null );
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
   * Método que carga la información de la programación consultada de acuerdo al criterio.
   */
  private void loadBookings()
  {
    PagingRequestTO pagingRequestTO = new PagingRequestTO();
    super.fillSessionData( pagingRequestTO );
    pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_EVENT_ID );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_REGION_ID );
    pagingRequestTO.getSort().add( BookingQuery.BOOKING_TYPE_ID );
    pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
    pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
    this.addFilterSpecialEvent( pagingRequestTO );
    this.addFilterRegion( pagingRequestTO );
    pagingRequestTO.getFilters().put( BookingQuery.BOOKING_TYPE_ID, BOOKING_TYPE_ID );
    PagingResponseTO<SpecialEventTO> response = null;
    List<SpecialEventTO> result = null;
    SpecialEventBookingTO bookingSpecialEvent = this.specialEventServiceIntegratorEJB
        .findBookingSpecialEvent( pagingRequestTO );
    response = bookingSpecialEvent.getReponse();
    this.bookingTOs = response.getElements();
    result = response.getElements();
    this.applyData( result );
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
      this.putPresaleAndDate( bookingTO );
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
   * Método que agrega el filtro "Region" a la petición.
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
   * Método que agrega el filtro "Evento" a la petición.
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
   * Método que permite cargar las salas seleccionadas desde la vista por el usuario.
   * 
   * @param event
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
   * Método que convierte una lista de objetos a una lista de strings.
   * 
   * @param oldObjectSelection
   * @return
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
   * Método que detecta cambios en la selección de salas en vista.
   * 
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
   * Método que valida que hayan sido seleccionados un evento y una región como criterios de búsqueda.
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
   * Método que valida que un id seleccionado sea valido.
   * 
   * @param id
   * @return isValid Resultado de la validación.
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
   * Método que resetea los datos de la vista.
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
    this.showDate = false;
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
     * TODO: Se quita validación, ya que la preventa puede durar hasta el final de la duración del evento especial.
    if( isValid && this.rulePresaleReleaseDate.before( this.rulePresaleFinalDate ) )
    {
      isValid = false;
      createMessageError( RELEASE_BOFORE_INTERVAL_PRESALE_ERROR_TEXT );
    }*/
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
   * Método que aplica los parámetros de configuración en los registros de programación seleccionados.
   */
  public void applyRulesToBooking()
  {
    if( validateBookingSelection() && validateRulesToBooking() )
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
      this.setBookingTOsSelected( null );
    }
    else
    {
      validationFail();
    }
  }

  /**
   * Método para establecer los parametros de configuración en un registro de programación.
   * 
   * @param specialEventTO Registro de programación sin configuración.
   * @return specialEventTO Registro de programación configurado.
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
    if( this.showDate )
    {
      specialEventTO.setShowDate( Boolean.TRUE );
    }
    specialEventTO.setShowDate( this.showDate );
    return specialEventTO;
  }

  /**
   * Valida los parámetros de configuración
   * 
   * @return valid Resultado de la validación
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
   * Método que obtiene los registros seleccionados
   * 
   * @return BookingTO
   */
  private List<SpecialEventTO> getBookingSelected()
  {
    List<SpecialEventTO> bookingSelected = bookingTOsSelected;
    for( SpecialEventTO booking : bookingTOsSelected )
    {
      super.fillSessionData( booking );
      super.fillSessionData( booking.getPresaleTO() );
    }

    return bookingSelected;
  }

  /**
   * Método que valida que se hayan seleccionado registros antes del guardado.
   * 
   * @return void
   **/
  public void validationSaveBooking()
  {
    if( this.validateBookingSelection() )
    {
      boolean isValid = true;
      List<SpecialEventTO> bookingsForSaving = this.getBookingsSelectedForSave();
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
   * Método que valida que se hayan seleccionado registros antes del cancelado.
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
   * Método que valida que se hayan seleccionado registros.
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
   * Método que carga y envia la programación para ser guardada.
   */
  public void saveBooking()
  {
    int screenZeroTerminated = 0;
    List<SpecialEventTO> bookingsForSaving = new ArrayList<SpecialEventTO>();
    for( SpecialEventTO bookingSpecialEventTO : this.getBookingsSelectedForSave() )
    {
      int copy = bookingSpecialEventTO.getCopy();
      int screens = 0;
      if( bookingSpecialEventTO.getScreensSelected().size() == MIN_SCREEN_SELECTED )
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
      bookingSpecialEventTO.setCopyScreenZeroTerminated( screenZeroTerminated );
      bookingSpecialEventTO.setIdBookingType( BOOKING_TYPE_ID );
      bookingsForSaving.add( bookingSpecialEventTO );

      /* Se actualiza la preventa en caso de que aplique */
      if( CollectionUtils.isNotEmpty( bookingSpecialEventTO.getScreens() ) )
      {
        this.updatePresaleForScreenTO( bookingSpecialEventTO.getScreens(), bookingSpecialEventTO.getPresaleTO() );
      }

    }
    this.specialEventServiceIntegratorEJB.saveOrUpdateBookings( bookingsForSaving );
    this.searchTheater();
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
   * Métodoo que obtiene la programación de eventos especiales por cada cine.
   * 
   * @return List of SpecialEventTO
   */
  private List<SpecialEventTO> getBookingsSelectedForSave()
  {
    List<SpecialEventTO> bookingForSaving = new ArrayList<SpecialEventTO>();
    for( SpecialEventTO bookingSpecialEventTO : this.bookingTOsSelected )
    {
      bookingSpecialEventTO
          .setScreenAvailable( bookingSpecialEventTO.getScreensSelected().size() < bookingSpecialEventTO.getCopy() );
      this.setEventToBooking( bookingSpecialEventTO );
      super.fillSessionData( bookingSpecialEventTO );
      if( bookingSpecialEventTO.getId() == null )
      {
        if( bookingSpecialEventTO.getCopy() != 0 )
        {
          this.validateNumberOfCopies( bookingSpecialEventTO );
          this.validateAvaliableScreens( bookingSpecialEventTO );
          if( hasMoreScreensThanCopies( bookingSpecialEventTO ) )
          {
            throwMoreScreensThanCopiesException( bookingSpecialEventTO );
          }
          else
          {
            List<ScreenTO> screensSelected = this.createScreenTO( bookingSpecialEventTO );
            bookingSpecialEventTO.setScreens( screensSelected );
            bookingForSaving.add( bookingSpecialEventTO );
          }
          // Se llenan los datos de sesión para el objeto PresaleTO en caso de estar marcado como activo.
          if( bookingSpecialEventTO.getPresaleTO().isFgActive() )
          {
            super.fillSessionData( bookingSpecialEventTO.getPresaleTO() );
          }
        }
        else
        {// No es posible cancelar una programación no guardada previamente
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_NOT_SAVED_FOR_CANCELLATION,
            new Object[] { bookingSpecialEventTO.getTheater().getName(),
                bookingSpecialEventTO.getBookingObservation().getObservation() } );
        }
      }
      else
      {// Tambien se aplican validaciones para programaciones con status continued
        if( bookingSpecialEventTO.getStatus().getId() != null
            && bookingSpecialEventTO.getStatus().getId().equals( BookingStatus.BOOKED.getIdLong() ) )
        {
          this.validateNumberOfCopies( bookingSpecialEventTO );
          this.validateAvaliableScreens( bookingSpecialEventTO );
          this.validateMoreScreensThanCopies( bookingSpecialEventTO );
          this.getBookingsForUpdate( bookingForSaving, bookingSpecialEventTO );
        }
      }
    }
    return bookingForSaving;
  }

  /**
   * Método que setea el evento para una programación.
   * 
   * @param bookingSpecialEventTO
   */
  private void setEventToBooking( SpecialEventTO bookingSpecialEventTO )
  {
    if( bookingSpecialEventTO.getEvent() == null )
    {
      EventTO eventTO = new EventTO();
      eventTO.setId( this.specialEventIdSelected );
      bookingSpecialEventTO.setEvent( eventTO );
    }
  }

  /**
   * Método que obtiene las programaciones para actualizar.
   * 
   * @param bookingsForSaving
   * @param bookingSpecialEventTO
   */
  private void getBookingsForUpdate( List<SpecialEventTO> bookingsForSaving, SpecialEventTO bookingSpecialEventTO )
  {
    if( bookingSpecialEventTO.getStatus().getId() != null
        && bookingSpecialEventTO.getStatus().getId().equals( BookingStatus.CANCELED.getIdLong() ) )
    {
      if( bookingSpecialEventTO.getCopy() != 0 )
      {
        CatalogTO statusTO = new CatalogTO();
        statusTO.setId( BookingStatus.BOOKED.getIdLong() );
        bookingSpecialEventTO.setStatus( statusTO );
        List<ScreenTO> screensSelected = this.createScreenTO( bookingSpecialEventTO );
        bookingSpecialEventTO.setScreens( screensSelected );
        bookingsForSaving.add( bookingSpecialEventTO );
      }
    }
    else if( bookingSpecialEventTO.getStatus().getId() != null
        && bookingSpecialEventTO.getStatus().getId().equals( BookingStatus.BOOKED.getIdLong() )
        && bookingSpecialEventTO.getCopy() == 0 )
    {
      bookingSpecialEventTO.setScreens( new ArrayList<ScreenTO>() );
      bookingsForSaving.add( bookingSpecialEventTO );
    }
    else
    {
      List<ScreenTO> screensSelected = this.createScreenTO( bookingSpecialEventTO );
      bookingSpecialEventTO.setScreens( screensSelected );
      bookingsForSaving.add( bookingSpecialEventTO );
    }
    // Se llenan los datos de sesión para el objeto PresaleTO en caso de que su id no sea null.
    if( bookingSpecialEventTO.getPresaleTO().getIdPresale() != null )
    {
      super.fillSessionData( bookingSpecialEventTO.getPresaleTO() );
    }
  }

  /**
   * Valida que existan más salas que copias.
   * 
   * @param specialEventTO
   */
  private void validateMoreScreensThanCopies( SpecialEventTO specialEventTO )
  {
    if( this.hasMoreScreensThanCopies( specialEventTO ) )
    {
      throwMoreScreensThanCopiesException( specialEventTO );
    }
  }

  /**
   * Método que envia una ecepción en caso que el número de copias sea invalido.
   * 
   * @param bookingSpecialEventTO
   */
  private void throwMoreScreensThanCopiesException( SpecialEventTO bookingSpecialEventTO )
  {
    throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_NUM_SCREENS_GREATER_NUM_COPIES,
      new Object[] { bookingSpecialEventTO.getTheater().getName() } );
  }

  /**
   * Valida que el número de copias sea menor que el numero de salas.
   * 
   * @param bookingSpecialEventTO
   * @return boolean
   */
  private boolean hasMoreScreensThanCopies( SpecialEventTO bookingSpecialEvent )
  {
    return bookingSpecialEvent.getScreensSelected().size() > bookingSpecialEvent.getCopy();
  }

  /**
   * Método que valida el número de copias.
   * 
   * @param bookingSpecialEventTO
   */
  private void validateNumberOfCopies( SpecialEventTO bookingSpecialEventTO )
  {
    if( bookingSpecialEventTO.getCopy() > bookingSpecialEventTO.getTheater().getScreens().size() )
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
   * Método para crear las salas por cada cine.
   * 
   * @param bookingspecialEventTO
   * @return List ScreenTo
   */
  private List<ScreenTO> createScreenTO( SpecialEventTO bookingspecialEventTO )
  {
    List<ScreenTO> screensSelected = new ArrayList<ScreenTO>();
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
        screen.setNuScreen( screenFromTheater.getNuScreen() );
      }

      if( bookingspecialEventTO.getStatus() != null )
      {
        screen.setBookingStatus( bookingspecialEventTO.getStatus() );
      }

      screensSelected.add( screen );
    }
    return screensSelected;
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
   * @return the lastScreenSelection
   */
  public List<String> getLastScreenSelection()
  {
    return lastScreenSelection;
  }

  /**
   * @param lastScreenSelection the lastScreenSelection to set
   */
  public void setLastScreenSelection( List<String> lastScreenSelection )
  {
    this.lastScreenSelection = lastScreenSelection;
  }

  /**
   * @return the showdate
   */
  public boolean isShowDate()
  {
    return showDate;
  }

  /**
   * @param showdate the showdate to set
   */
  public void setShowDate( boolean showdate )
  {
    this.showDate = showdate;
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
