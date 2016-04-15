package mx.com.cinepolis.digital.booking.commons.to;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class TheaterBookingWeekTO implements Serializable
{

  private static final long serialVersionUID = -4175203422419203301L;

  private PagingResponseTO<BookingTheaterTO> response;
  private boolean editable;
  private boolean sent;
  private boolean zero;
  private String imageSemaphore;
  private Map<Long, List<EventTO>> availableEvents;
  private WeekTO currentWeek;

  /**
   * @return the response
   */
  public PagingResponseTO<BookingTheaterTO> getResponse()
  {
    return response;
  }

  /**
   * @param response the response to set
   */
  public void setResponse( PagingResponseTO<BookingTheaterTO> response )
  {
    this.response = response;
  }

  /**
   * @return the editable
   */
  public boolean isEditable()
  {
    return editable;
  }

  /**
   * @param editable the editable to set
   */
  public void setEditable( boolean editable )
  {
    this.editable = editable;
  }

  /**
   * @return the sent
   */
  public boolean isSent()
  {
    return sent;
  }

  /**
   * @param sent the sent to set
   */
  public void setSent( boolean sent )
  {
    this.sent = sent;
  }

  /**
   * @return the imageSemaphore
   */
  public String getImageSemaphore()
  {
    return imageSemaphore;
  }

  /**
   * @param imageSemaphore the imageSemaphore to set
   */
  public void setImageSemaphore( String imageSemaphore )
  {
    this.imageSemaphore = imageSemaphore;
  }

  /**
   * @return the availableEvents
   */
  public Map<Long, List<EventTO>> getAvailableEvents()
  {
    return availableEvents;
  }

  /**
   * @param availableEvents the availableEvents to set
   */
  public void setAvailableEvents( Map<Long, List<EventTO>> availableEvents )
  {
    this.availableEvents = availableEvents;
  }

  /**
   * @return the zero
   */
  public boolean isZero()
  {
    return zero;
  }

  /**
   * @param zero the zero to set
   */
  public void setZero( boolean zero )
  {
    this.zero = zero;
  }

  /**
   * @return the currentWeek
   */
  public WeekTO getCurrentWeek()
  {
    return currentWeek;
  }

  /**
   * @param currentWeek the currentWeek to set
   */
  public void setCurrentWeek( WeekTO currentWeek )
  {
    this.currentWeek = currentWeek;
  }

}
