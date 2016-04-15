package mx.com.cinepolis.digital.booking.commons.exception;

/**
 * Clase con los códigos de errores para las excepciones
 * @author rgarcia
 *
 */
public enum DigitalBookingExceptionCode
{

  /** Error desconocido */
  GENERIC_UNKNOWN_ERROR(0),
  /***
   * CATALOG NULO
   */
  CATALOG_ISNULL(1),
  
  /**
   * Paging Request Nulo
   */
  PAGING_REQUEST_ISNULL(2),
  
  /**
   * Errores de persistencia
   */
  PERSISTENCE_ERROR_GENERIC(3),
  PERSISTENCE_ERROR_NEW_OBJECT_FOUND_DURING_COMMIT(4),
  PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED(5),
  PERSISTENCE_ERROR_CATALOG_NOT_FOUND(6),
  PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED_WITH_ID_VISTA(7),
  PERSISTENCE_ERROR_WEEK_ALREADY_REGISTERED(8),

  /**
   * Errores de Administración de Catalogos
   */
   THEATER_NUM_THEATER_ALREADY_EXISTS(9),
   CANNOT_DELETE_REGION(10),
   INEXISTENT_REGION(11),
   INVALID_TERRITORY(12),
   THEATER_IS_NULL(13),
   THEATER_IS_NOT_IN_ANY_REGION(14),
   THEATER_NOT_HAVE_SCREENS(15),
   INEXISTENT_THEATER(16),
   THEATER_IS_NOT_IN_ANY_CITY(17),
   THEATER_IS_NOT_IN_ANY_STATE(18),
   INVALID_SCREEN(19),
   INVALID_MOVIE_FORMATS(20),
   INVALID_SOUND_FORMATS(21),
   FILE_NULL(22),
   EVENT_MOVIE_NULL(23),
   
   /**
    * Errores de Administración de cines
    */
   SCREEN_NUMBER_ALREADY_EXISTS(24),
   SCREEN_NEEDS_AT_LEAST_ONE_SOUND_FORMAT(25),
   SCREEN_NEEDS_AT_LEAST_ONE_MOVIE_FORMAT(26),
   DISTRIBUTOR_IS_ASSOCIATED_WITH_AN_EXISTING_MOVIE(27),
   CATEGORY_SOUND_FORMAT_IS_ASSOCIATED_WITH_AN_EXISTING_MOVIE(28),
   CATEGORY_SOUND_FORMAT_IS_ASSOCIATED_WITH_AN_EXISTING_SCREEN(29),
   CATEGORY_MOVIE_FORMAT_IS_ASSOCIATED_WITH_AN_EXISTING_MOVIE(30),
   CATEGORY_MOVIE_FORMAT_IS_ASSOCIATED_WITH_AN_EXISTING_SCREEN(31),
   CATEGORY_SCREEN_FORMAT_IS_ASSOCIATED_WITH_AN_EXISTING_SCREEN(39),
   SCREEN_NEEDS_SCREEN_FORMAT(32),
   
   MOVIE_NAME_BLANK(33),
   MOVIE_DISTRIBUTOR_NULL(35),
   MOVIE_COUNTRIES_EMPTY(36),
   MOVIE_DETAIL_EMPTY(37),
   MOVIE_IMAGE_NULL(38),
   
   /**
    * Booking errors
    */
   BOOKING_PERSISTENCE_ERROR_STATUS_NOT_FOUND(40),
   BOOKING_PERSISTENCE_ERROR_EVENT_NOT_FOUND(41),
   BOOKING_PERSISTENCE_ERROR_THEATER_NOT_FOUND(42),
   BOOKING_PERSISTENCE_ERROR_WEEK_NOT_FOUND(43),
   BOOKING_PERSISTENCE_ERROR_BOOKING_NOT_FOUND(44),
   BOOKING_PERSISTENCE_ERROR_USER_NOT_FOUND(45),
   BOOKING_PERSISTENCE_ERROR_EVENT_TYPE_NOT_FOUND(46),
   BOOKING_PERSISTENCE_ERROR_AT_CONSULT_BOOKINGS(221),
   
   /**
    * Week errors
    */
   WEEK_PERSISTENCE_ERROR_NOT_FOUND(50),
   
   /**
    * Observation errors
    */
   NEWS_FEED_OBSERVATION_NOT_FOUND(51),
   BOOKING_OBSERVATION_NOT_FOUND2(52),
   OBSERVATION_NOT_FOUND(53),
   NEWS_FEED_OBSERVATION_ASSOCIATED_TO_ANOTHER_USER(103),
   
   /**
    * Email Errors
    */
   EMAIL_DOES_NOT_COMPLIES_REGEX(54),
   EMAIL_IS_REPEATED(55),
   
   /**
    * Configuration Errors
    */
   CONFIGURATION_ID_IS_NULL(60),
   CONFIGURATION_NAME_IS_NULL(61),
   CONFIGURATION_PARAMETER_NOT_FOUND(62),

   /**
    * Email Errors
    */
   ERROR_SENDING_EMAIL_NO_DATA(70),
   ERROR_SENDING_EMAIL_NO_RECIPIENTS(71),
   ERROR_SENDING_EMAIL_SUBJECT(72),
   ERROR_SENDING_EMAIL_MESSAGE(73),
   
   /**
    * Booking errors
    */
   BOOKING_IS_NULL(74),
   BOOKING_COPIES_IS_NULL(75),
   BOOKING_WEEK_NULL(76),
   BOOKING_EVENT_NULL(77),
   BOOKING_WRONG_STATUS_FOR_CANCELLATION(78),
   BOOKING_WRONG_STATUS_FOR_TERMINATION(79),
   BOOKING_THEATER_NEEDS_WEEK_ID(80),
   BOOKING_THEATER_NEEDS_THEATER_ID(81),
   BOOKING_NUMBER_COPIES_ZERO(82),
   BOOKING_NUM_SCREENS_GREATER_NUM_COPIES(83),
   BOOKING_CAN_NOT_BE_CANCELED(84),
   BOOKING_CAN_NOT_BE_TERMINATED(85),
   BOOKING_WRONG_STATUS_FOR_EDITION(86),
   
   ERROR_THEATER_HAS_NO_EMAIL(87),
   BOOKING_NUMBER_OF_COPIES_EXCEEDS_NUMBER_OF_SCREENS(88),
   BOOKING_NON_EDITABLE_WEEK(89),
   
   BOOKING_THEATER_REPEATED(90),
   BOOKING_IS_WEEK_ONE(91),
   BOOKING_THEATER_HAS_SCREEN_ZERO(92),
   BOOKING_MAXIMUM_COPY(93),
   BOOKING_NOT_SAVED_FOR_CANCELLATION(94),
   BOOKING_NOT_SAVED_FOR_TERMINATION(194),
   BOOKING_NOT_THEATERS_IN_REGION(196),
   BOOKING_NUMBER_OF_COPIES_EXCEEDS_NUMBER_OF_SCREENS_WHIT_THIS_FORMAT(195),
   BOOKING_NUM_SCREENS_GREATER_NUM_COPIES_NO_THEATER(95),
   BOOKING_SENT_CAN_NOT_BE_CANCELED(96),
   BOOKING_SENT_CAN_NOT_BE_TERMINATED(97),
   BOOKING_WITH_FOLLOWING_WEEK_CAN_NOT_BE_CANCELED(98),
   BOOKING_WITH_FOLLOWING_WEEK_CAN_NOT_BE_TERMINATED(99),

   /**
    * Report errors
    */
   CREATE_XLSX_ERROR(100),  
   BOOKING_THEATER_IS_NOT_ASSIGNED_TO_ANY_SCREEN(101),
   SEND_EMAIL_REGION_ERROR_CAN_ONLY_UPLOAD_UP_TO_TWO_FILES(102),
   
   
   /**
    * Security errors
    */
   SECURITY_ERROR_USER_DOES_NOT_EXISTS(200),
   SECURITY_ERROR_PASSWORD_INVALID(201),
   SECURITY_ERROR_INVALID_USER(202), 
   MENU_EXCEPTION(203),
   
   /**
    * UserErrors
    */
   USER_IS_NULL(204),
   USER_USERNAME_IS_BLANK(205),
   USER_NAME_IS_BLANK(206),
   USER_LAST_NAME_IS_BLANK(207),
   USER_ROLE_IS_NULL(208),
   USER_EMAIL_IS_NULL(209),
   USER_THE_USERNAME_IS_DUPLICATE(210),
   USER_TRY_DELETE_OWN_USER(211),
  
   /**
    * Week errors
    */
   WEEK_IS_NULL(212),
   WEEK_INVALID_NUMBER(213),
   WEEK_INVALID_YEAR(214),
   WEEK_INVALID_FINAL_DAY(215),
   
   /**
    * EventMovie errors
    */
   EVENT_CODE_DBS_NULL(216),
   CATALOG_ALREADY_REGISTERED_WITH_DS_CODE_DBS(217),
   CATALOG_ALREADY_REGISTERED_WITH_SHORT_NAME(218),
   CANNOT_DELETE_WEEK(219),
   CANNOT_REMOVE_EVENT_MOVIE(220),

   /**
    * Income errors
    */
   INCOMES_COULD_NOT_OBTAIN_DATABASE_PROPERTIES(300),
   INCOMES_DRIVER_ERROR(301),
   INCOMES_CONNECTION_ERROR(302),
   
  /**
   * SynchronizeErrors
   */
  CANNOT_CONNECT_TO_SERVICE(500),
  
  /**
   * Transaction timeout
   */
  TRANSACTION_TIMEOUT(501),
  
  /**
   * INVALID PARAMETERS FOR BOOKING PRE RELEASE
   */
  INVALID_COPIES_PARAMETER(600),
  INVALID_DATES_PARAMETERS(601),
  INVALID_SCREEN_PARAMETER_CASE_ONE(602),
  INVALID_SCREEN_PARAMETER_CASE_TWO(603),
  INVALID_DATES_BEFORE_TODAY_PARAMETERS(604),
  INVALID_PRESALE_DATES_PARAMETERS(605),

  /**
   * VALIDATIONS FOR PRESALE IN BOOKING MOVIE
   */
  ERROR_BOOKING_PRESALE_FOR_NO_PREMIERE(606),
  ERROR_BOOKING_PRESALE_FOR_ZERO_SCREEN(607),
  ERROR_BOOKING_PRESALE_NOT_ASSOCIATED_AT_BOOKING(608),

  /**
   * INVALID SELECTION OF PARAMETERS
   * TO APPLY IN SPECIAL EVENT
   */
  INVALID_STARTING_DATES(617),
  INVALID_FINAL_DATES(618),
  INVALID_STARTING_AND_RELREASE_DATES(619),
  INVALID_THEATER_SELECTION(620),
  INVALID_SCREEN_SELECTION(621),
  BOOKING_THEATER_NULL(622),
  BOOKING_TYPE_INVALID(623),
  BOOKING_SPECIAL_EVENT_LIST_EMPTY(624),
  
  /**
   * Invalid datetime range for system log.
   */
  LOG_FINAL_DATE_BEFORE_START_DATE(625),
  LOG_INVALID_DATE_RANGE(626),
  LOG_INVALID_TIME_RANGE(627),
  
  /**
   * Invavlid cityTO
   */
  CITY_IS_NULL(628),
  CITY_HAS_NO_NAME(629),
  CITY_HAS_NO_COUNTRY(630),
  CITY_INVALID_LIQUIDATION_ID(631),
  PERSISTENCE_ERROR_CATALOG_ALREADY_REGISTERED_WITH_LIQUIDATION_ID(632)
  ;
  /**
   * Constructor interno
   * 
   * @param id
   */
  private DigitalBookingExceptionCode( int id )
  {
    this.id = id;
  }

  private int id;

  /**
   * @return the id
   */
  public int getId()
  {
    return id;
  }
  
}

