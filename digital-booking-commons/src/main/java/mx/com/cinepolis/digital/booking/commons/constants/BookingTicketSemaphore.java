package mx.com.cinepolis.digital.booking.commons.constants;

/**
 * Ticket semaphore constant
 * 
 * @author gsegura
 */
public enum BookingTicketSemaphore
{
  GREEN(1, "sem_vd"), YELLOW(2, "sem_am"), RED(3, "sem_rj"), GRAY(4, "sem_gr");
  private BookingTicketSemaphore( int id, String image )
  {
    this.id = id;
    this.image = image;
  }
  private int id;
  private String image;

  /**
   * @return the id
   */
  public int getId()
  {
    return id;
  }

  /**
   * @return the image
   */
  public String getImage()
  {
    return image;
  }

  /**
   * Obtains a {@link mx.com.cinepolis.digital.booking.commons.constants.BookingTicketSemaphore} from its id
   * 
   * @param id
   * @return
   */
  public static BookingTicketSemaphore fromId( int id )
  {
    BookingTicketSemaphore s = null;
    for( BookingTicketSemaphore bookingTicketSemaphore : BookingTicketSemaphore.values() )
    {
      if( bookingTicketSemaphore.getId() == id )
      {
        s = bookingTicketSemaphore;
        break;
      }
    }
    return s;
  }

  /**
   * Obtains a {@link mx.com.cinepolis.digital.booking.commons.constants.BookingTicketSemaphore} from its image
   * 
   * @param image
   * @return
   */
  public static BookingTicketSemaphore fromImage( String image )
  {
    BookingTicketSemaphore s = null;
    for( BookingTicketSemaphore bookingTicketSemaphore : BookingTicketSemaphore.values() )
    {
      if( bookingTicketSemaphore.getImage().equals( image ) )
      {
        s = bookingTicketSemaphore;
        break;
      }
    }
    return s;
  }

}
