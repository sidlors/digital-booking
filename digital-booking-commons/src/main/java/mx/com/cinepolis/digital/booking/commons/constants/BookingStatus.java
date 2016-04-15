package mx.com.cinepolis.digital.booking.commons.constants;

/**
 * Booking status
 * 
 * @author kperez
 * @since 0.2.0
 */
public enum BookingStatus
{
  BOOKED(1),  CANCELED(2), TERMINATED(3), CONTINUE(4);
  private int id;

  private BookingStatus( int id )
  {
    this.id = id;
  }

  /**
   * Obtains a {@link mx.com.cinepolis.digital.booking.commons.constants.BookingStatus} from its id
   * 
   * @param id
   * @return
   */
  public static BookingStatus fromId( int id )
  {
    BookingStatus st = null;
    for( BookingStatus bookingStatus : BookingStatus.values() )
    {
      if( bookingStatus.id == id )
      {
        st = bookingStatus;
        break;
      }
    }
    return st;
  }

  /**
   * @return the id
   */
  public int getId()
  {
    return id;
  }

  public Long getIdLong()
  {
    return Long.valueOf( id );
  }
}
