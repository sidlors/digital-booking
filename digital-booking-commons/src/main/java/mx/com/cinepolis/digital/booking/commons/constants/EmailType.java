package mx.com.cinepolis.digital.booking.commons.constants;

/**
 * Mail type constants
 * 
 * @author afuentes
 */
public enum EmailType
{
  BOOKING_REGION(1), BOOKING_THEATER(2);
  private int id;

  private EmailType( int id )
  {
    this.id = id;
  }

  /**
   * Obtains an {@link mx.com.cinepolis.digital.booking.commons.constants.EmailType} according its id
   * 
   * @param id
   * @return an {@link mx.com.cinepolis.digital.booking.commons.constants.EmailType}
   */
  public static EmailType fromId( int id )
  {
    EmailType et = null;
    for( EmailType eventType : EmailType.values() )
    {
      if( eventType.id == id )
      {
        et = eventType;
        break;
      }
    }
    return et;
  }

  /**
   * @return the id
   */
  public int getId()
  {
    return id;
  }
}
