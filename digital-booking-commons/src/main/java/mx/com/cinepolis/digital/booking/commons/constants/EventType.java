package mx.com.cinepolis.digital.booking.commons.constants;

/**
 * Event type constants
 * 
 * @author gsegura
 * @since 0.0.1
 */
public enum EventType
{
  MOVIE(1), SPECIAL_EVENT(2), MOVIE_TRAILER(3);
  private int id;

  private EventType( int id )
  {
    this.id = id;
  }

  /**
   * Obtains an {@link mx.com.cinepolis.digital.booking.commons.constants.EventType} according its id
   * 
   * @param id
   * @return an {@link mx.com.cinepolis.digital.booking.commons.constants.EventType}
   */
  public static EventType fromId( int id )
  {
    EventType et = null;
    for( EventType eventType : EventType.values() )
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
