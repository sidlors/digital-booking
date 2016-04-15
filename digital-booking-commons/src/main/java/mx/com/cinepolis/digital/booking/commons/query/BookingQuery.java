package mx.com.cinepolis.digital.booking.commons.query;

/**
 * Enumeration for criteria query for teh entity {@link mx.com.cinepolis.digital.booking.model.BookingDO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
public enum BookingQuery implements ModelQuery
{
  BOOKING_ID("idBooking"), BOOKING_EXHIBITION_WEEK("nuExhibitionWeek"), BOOKING_COPIES("qtCopy"), BOOKING_EXHIBITION_END(
      "dtExhibitionEndDate"), BOOKING_EVENT_ID("idEvent"), BOOKING_EVENT_ID_VISTA("idVista"), BOOKING_EVENT_NAME(
      "dsName"), BOOKING_EVENT_DBS_CODE("dsCodeDbs"), BOOKING_WEEK_ID("idWeek"), BOOKING_WEEK_START("dtStartingDayWeek"), BOOKING_WEEK_END(
      "dtFinalDayWeek"), BOOKING_THEATER_ID("idTheater"), BOOKING_REGION_ID("idRegion"), BOOKING_STATUS_ID(
      "idBookingStatus"), BOOKING_ACTIVE("fgActive"), BOOKING_SCREEN_LIST("screenDOList"), BOOKING_EVENT_SPECIAL_EVENT(
      "idBookingSpecialEvent"), BOOKING_EVENTSPECIAL_EVENT_SCREEN("idBookingSpecialEventScreen"), BOOKING_TYPE_ID(
      "idBookingType"), BOOKING_PRESALE_ACTIVE("fgActive"), BOOKING_BOOKED("fgBooked");

  private String query;

  private BookingQuery( String query )
  {
    this.query = query;
  }

  /**
   * 
   */
  @Override
  public String getQuery()
  {
    return query;
  }

}
