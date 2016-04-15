package mx.com.cinepolis.digital.booking.service.util;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import mx.com.cinepolis.digital.booking.commons.constants.BookingStatus;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.CityTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.model.ws.org.datacontract.schemas._2004._07.apicinepolis.Pelicula;
import mx.com.cinepolis.digital.booking.service.synchronize.impl.ServiceDataSynchronizerEJBImpl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase de utileria para validaciones reutilizables en los servicios
 * 
 * @author agustin.ramirez
 */
public final class ValidatorUtil
{
  private static final int MIN_NUM_WEEK = 0;
  private static final int MAX_YEAR_WEEK = 9999;
  private static final int MINIMUM_ID = 1;
  private static final long BOOKING_TYPE_SPECIAL_EVENT = 3L;
  private static final long BOOKING_TYPE_PRE_RELEASE = 2L;
  private static Set<Long> validStatusForCancellation;
  private static Set<Long> validStatusForTermination;

  private static final String ERROR_EVENT_MOVIE = "Error in: ";
  private static final String ERROR_EVENT_MOVIE_NULL = "Error: The movie is not valid.";
  private static final String ERROR_EVENT_MOVIE_NAME = "Error: Movie name must not be blank.";
  private static final String ERROR_EVENT_MOVIE_DISTRIBUTOR = ", Distributor is required.";
  private static final String ERROR_EVENT_MOVIE_ID_VISTA_UNDEFINED = "Error: The idVista of the movie is not valid.";
  private static final String ID_VISTA_UNDEFINED = "0";

  static
  {
    validStatusForCancellation = new HashSet<Long>();
    validStatusForCancellation.add( BookingStatus.BOOKED.getIdLong() );

    validStatusForTermination = new HashSet<Long>();
    validStatusForTermination.add( BookingStatus.BOOKED.getIdLong() );
  }

  /**
   * Constructor Privado
   */
  private ValidatorUtil()
  {

  }

  /**
   * Metodo que realiza la validacion de un catalogTO
   * 
   * @param catalogTO CatalogTO a validar
   * @throws DigitalBookingException
   */
  public static void validateCatalog( CatalogTO catalogTO )
  {
    if( catalogTO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CATALOG_ISNULL );

    }
    if( StringUtils.isEmpty( catalogTO.getName() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CATALOG_ISNULL );

    }
  }

  /**
   * Metodo que se encarga de validar los datos esenciales de un cine
   * 
   * @param theaterTO
   */
  public static void validateTheater( TheaterTO theaterTO )
  {
    if( theaterTO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.THEATER_IS_NULL.getId() );
    }
    // Se valida que el nombre del cine no sea nulo
    if( StringUtils.isEmpty( theaterTO.getName() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.THEATER_IS_NULL.getId() );
    }
    // Se valida que se le asigne al menos una región
    if( theaterTO.getRegion() == null || theaterTO.getRegion().getCatalogRegion() == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.THEATER_IS_NOT_IN_ANY_REGION.getId() );
    }
    // Se valida que se le asigne al menos una sala
    if( theaterTO.getScreens() == null || theaterTO.getScreens().isEmpty() )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.THEATER_NOT_HAVE_SCREENS.getId() );
    }
    // Se valida que el cine pertenezca a una ciudad
    if( theaterTO.getCity() == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.THEATER_IS_NOT_IN_ANY_CITY.getId() );
    }
    // Se valida que el cine pertenezca a un estado
    if( theaterTO.getState() == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.THEATER_IS_NOT_IN_ANY_STATE.getId() );
    }
  }

  /**
   * Metodo que se encarga de validar los datos esenciales de la sala
   * 
   * @param screenTO
   */
  public static void validateScreen( ScreenTO screenTO )
  {
    if( screenTO.getNuCapacity() == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INVALID_SCREEN.getId() );
    }
    if( screenTO.getMovieFormats() == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INVALID_MOVIE_FORMATS.getId() );
    }
    if( screenTO.getSoundFormats() == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INVALID_SOUND_FORMATS.getId() );
    }

  }

  /**
   * Metodo que realiza la validacion de un PagingRequestTO
   * 
   * @param PagingRequestTO a validar
   * @throws DigitalBookingException
   */
  public static void validatePagingRequest( PagingRequestTO pagingRequestTO )
  {
    if( pagingRequestTO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.PAGING_REQUEST_ISNULL );
    }
  }

  /**
   * Validates if a {@link mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO} has at least
   * {@link mx.com.cinepolis.digital.booking.commons.query.BookingQuery.BOOKING_WEEK_ID} and
   * {@link mx.com.cinepolis.digital.booking.commons.query.BookingQuery.BOOKING_THEATER_ID} as filters
   * 
   * @param pagingRequestTO
   */
  public static void validatePagingRequestBookingTheater( PagingRequestTO pagingRequestTO )
  {
    validatePagingRequest( pagingRequestTO );
    if( !pagingRequestTO.getFilters().containsKey( BookingQuery.BOOKING_WEEK_ID ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_THEATER_NEEDS_WEEK_ID );

    }
    if( !pagingRequestTO.getFilters().containsKey( BookingQuery.BOOKING_THEATER_ID ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_THEATER_NEEDS_THEATER_ID );
    }
  }

  /**
   * Method that ensures a {@link FileTO} is valid.
   * 
   * @param fileTO {@link FileTO} to validate.
   */
  public static void validateFileTO( FileTO fileTO )
  {
    if( fileTO == null || fileTO.getFile() == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.FILE_NULL.getId() );
    }
  }

  /**
   * Method that ensures a {@link EventMovieTO} is valid.
   * 
   * @param eventMovieTO {@link EventMovieTO} to validate.
   */
  public static void validateEventMovie( EventMovieTO eventMovieTO )
  {
    if( eventMovieTO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.EVENT_MOVIE_NULL.getId() );
    }
    // Movie name must not be blank
    if( StringUtils.isBlank( eventMovieTO.getDsEventName() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.MOVIE_NAME_BLANK.getId() );
    }
    // Distributor is required
    if( eventMovieTO.getDistributor() == null || eventMovieTO.getDistributor().getId() == null
        || eventMovieTO.getDistributor().getId() < 1 )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.MOVIE_DISTRIBUTOR_NULL.getId() );
    }
    validateMovieImage( eventMovieTO );

    // LiquidationId is required
    if( eventMovieTO.getCodeDBS() == null || eventMovieTO.getCodeDBS().isEmpty() )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.EVENT_CODE_DBS_NULL.getId() );
    }
  }

  private static void validateMovieImage( EventMovieTO eventMovieTO )
  {
    // Movie image is required
    if( eventMovieTO.getIdMovieImage() == null || eventMovieTO.getIdMovieImage() < 1 )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.MOVIE_IMAGE_NULL.getId() );
    }
  }

  /**
   * Method that ensures a {@link BookingTO} is valid.
   * 
   * @param bookingTO {@link BookingTO} to validate.
   */
  public static void validateBooking( BookingTO bookingTO )
  {
    if( bookingTO == null || bookingTO.getId() == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_IS_NULL.getId() );
    }
  }

  /**
   * Method that ensures a {@link BookingTO} is not null.
   * 
   * @param bookingTO {@link BookingTO} is not null.
   */
  public static void validateBookingNotNull( BookingTO bookingTO )
  {
    if( bookingTO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_IS_NULL.getId() );
    }
  }

  /**
   * Method that ensures that a {@link PagingRequestTO} includes the mandatory fields to find theater bookings.
   * 
   * @param pagingRequestTO {@link PagingRequestTO} to validate.
   * @author afuentes
   */
  public static void validateMandatoryBookingTheaterCriterias( PagingRequestTO pagingRequestTO )
  {
    if( pagingRequestTO.getFilters().containsKey( BookingQuery.BOOKING_WEEK_ID ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_WEEK_NULL.getId() );
    }
    if( pagingRequestTO.getFilters().containsKey( BookingQuery.BOOKING_EVENT_ID ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_EVENT_NULL.getId() );
    }
  }

  /**
   * Validates if a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} has the correct status for
   * cancellation
   * 
   * @param bookingTO
   */
  public static void validateBookingForCancellation( BookingTO bookingTO )
  {
    validateBookingNotNull( bookingTO );
    CatalogTO status = bookingTO.getStatus();
    if( status == null || !validStatusForCancellation.contains( status.getId() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_WRONG_STATUS_FOR_CANCELLATION
          .getId() );
    }

    if( bookingTO.getExhibitionWeek() != 1 )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_IS_WEEK_ONE );
    }
  }

  /**
   * Validates if a {@link mx.com.cinepolis.digital.booking.commons.to.BookingTO} has the correct status for termination
   * 
   * @param bookingTO
   */
  public static void validateBookingForTermination( BookingTO bookingTO )
  {
    validateBookingNotNull( bookingTO );
    CatalogTO status = bookingTO.getStatus();
    if( status == null || !validStatusForTermination.contains( status.getId() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_WRONG_STATUS_FOR_TERMINATION
          .getId() );
    }
  }

  /**
   * Validates if the week of a booking is still editable.
   * 
   * @param bookingTO {@link BookingTO} to validate.
   * @author afuentes
   */
  public static void validateEditableWeek( BookingTO bookingTO, int editableDays )
  {
    WeekTO weekTO = bookingTO.getWeek();
    if( !isEditableWeek( weekTO, editableDays ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_NON_EDITABLE_WEEK );
    }
  }

  /**
   * Validates if a week is still editable.
   * 
   * @param weekTO {@link WeekTO} to validate.
   * @return <code>true</code> if the week is editable, <code>false</code> otherwise.
   * @author afuentes
   */
  public static boolean isEditableWeek( WeekTO weekTO, int editableDays )
  {
    boolean isEditableWeek = false;
    if( weekTO != null )
    {
      Date today = DateUtils.truncate( weekTO.getTimestamp(), Calendar.DATE );
      // GSE, modificar que se programable los 3 dias
      Date limit = DateUtils.addDays( DateUtils.truncate( weekTO.getStartingDayWeek(), Calendar.DATE ), editableDays );
      isEditableWeek = today.before( limit ) || DateUtils.isSameDay( today, limit );
    }
    return isEditableWeek;
  }

  /**
   * Validate if {@link mx.mx.com.cinepolis.digital.booking.model.to.UserTO} is valid
   */
  public static void validateUser( UserTO userTO )
  {
    if( userTO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.USER_IS_NULL );
    }
    if( StringUtils.isBlank( userTO.getName() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.USER_USERNAME_IS_BLANK );
    }
    if( userTO.getPersonTO() == null || StringUtils.isBlank( userTO.getPersonTO().getName() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.USER_NAME_IS_BLANK );
    }

    if( StringUtils.isBlank( userTO.getPersonTO().getDsLastname() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.USER_LAST_NAME_IS_BLANK );
    }
    if( CollectionUtils.isEmpty( userTO.getRoles() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.USER_ROLE_IS_NULL );
    }
    if( CollectionUtils.isEmpty( userTO.getPersonTO().getEmails() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.USER_EMAIL_IS_NULL );
    }
  }

  /**
   * Validate if week has the correct parameters for to save.
   * 
   * @param weekTO
   */
  public static void validateWeek( WeekTO weekTO )
  {
    Calendar date = Calendar.getInstance();

    if( weekTO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.WEEK_IS_NULL );
    }
    if( weekTO.getNuWeek() == MIN_NUM_WEEK )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.WEEK_INVALID_NUMBER );
    }
    if( weekTO.getNuYear() > MAX_YEAR_WEEK || weekTO.getNuYear() < date.get( Calendar.YEAR ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.WEEK_INVALID_YEAR );
    }
    if( weekTO.getStartingDayWeek().after( weekTO.getFinalDayWeek() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.WEEK_INVALID_FINAL_DAY );
    }
  }

  /**
   * Validate if week has the correct parameters for to delete.
   * 
   * @param weekTO
   */
  public static void validateDeleteWeek( WeekTO weekTO )
  {
    if( weekTO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.WEEK_IS_NULL );
    }
  }

  /**
   * Validate event movie
   * 
   * @param pelicula
   */
  public static void validateMovie( Pelicula pelicula )
  {
    if( pelicula == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.EVENT_MOVIE_NULL.getId() );
    }
  }

  /**
   * Method that validates required data to save a movie.
   * 
   * @param eventMovieTO
   * @return
   */
  public static boolean validatEventMove( EventMovieTO eventMovieTO )
  {
    boolean save = true;
    StringBuilder error = new StringBuilder( ERROR_EVENT_MOVIE );
    final Logger logger = LoggerFactory.getLogger( ServiceDataSynchronizerEJBImpl.class );
    if( eventMovieTO == null )
    {
      logger.debug( ERROR_EVENT_MOVIE_NULL );
      save = false;
    }
    else if( StringUtils.isBlank( eventMovieTO.getDsEventName() ) )
    {
      logger.debug( ERROR_EVENT_MOVIE_NAME );
      save = false;
    }
    else if( eventMovieTO.getDistributor() == null || eventMovieTO.getDistributor().getId() == null
        || eventMovieTO.getDistributor().getId() < MINIMUM_ID )
    {
      logger.debug( error.append( eventMovieTO.getDsEventName() ).append( ERROR_EVENT_MOVIE_DISTRIBUTOR ).toString() );
      save = false;
    }
    else if( eventMovieTO.getIdVista().equals( ID_VISTA_UNDEFINED ) )
    {
      logger.debug( error.append( eventMovieTO.getDsEventName() ).append( ERROR_EVENT_MOVIE_ID_VISTA_UNDEFINED )
          .toString() );
      save = false;
    }
    return save;
  }

  /**
   * Method that ensures a {@link BookingTO} is valid.
   * 
   * @param eventMovieTO {@link BookingTO} to validate.
   */
  public static void validateSpecialEventBooking( SpecialEventTO specialEventTO )
  {
    if( specialEventTO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_IS_NULL.getId() );
    }
    if( specialEventTO.getIdBookingType() == null
        || (specialEventTO.getIdBookingType() != BOOKING_TYPE_SPECIAL_EVENT && specialEventTO.getIdBookingType() != BOOKING_TYPE_PRE_RELEASE) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_TYPE_INVALID.getId() );
    }
    if( specialEventTO.getEvent() == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_EVENT_NULL.getId() );
    }
    if( specialEventTO.getTheater() == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.BOOKING_THEATER_NULL.getId() );
    }
  }

  /**
   * Method that ensures a {@link BookingTO} is valid to update.
   * 
   * @param bookingTO {@link BookingTO} to validate.
   * @return the boolean result of validation.
   */
  public static boolean validateBookingForUpdate( BookingTO bookingTO )
  {
    boolean response = true;
    if( bookingTO == null || bookingTO.getId() == null )
    {
      response = false;
    }
    return response;
  }

  /**
   * Method that validates whether a city is valid to save, update or delete.
   * 
   * @param cityTO, the {@link mx.com.cinepolis.digital.booking.commons.to.CityTO} object to be validated.
   * @throws DigitalBookingException
   * @author jreyesv
   */
  public static void validateCityTO( CityTO cityTO )
  {
    if( cityTO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CITY_IS_NULL );
    }
    if( StringUtils.isEmpty( cityTO.getName() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CITY_HAS_NO_NAME );
    }
    if( cityTO.getCountry() == null || cityTO.getCountry().getId() == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CITY_HAS_NO_COUNTRY );
    }
    if( cityTO.getIdVista().equals( ID_VISTA_UNDEFINED ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.CITY_INVALID_LIQUIDATION_ID );
    }
  }

}
