package mx.com.cinepolis.digital.booking.commons.to;

import java.io.Serializable;

public class MovieBookingWeekTO implements Serializable
{
  private static final long serialVersionUID = -8546090302384204311L;
  private PagingResponseTO<BookingTO> response;
  private Boolean sameOrBeforeWeek;
  private WeekTO currentWeek;

  /**
   * @return the response
   */
  public PagingResponseTO<BookingTO> getResponse()
  {
    return response;
  }

  /**
   * @param response the response to set
   */
  public void setResponse( PagingResponseTO<BookingTO> response )
  {
    this.response = response;
  }

  /**
   * @return the sameOrBeforeWeek
   */
  public Boolean getSameOrBeforeWeek()
  {
    return sameOrBeforeWeek;
  }

  /**
   * @param sameOrBeforeWeek the sameOrBeforeWeek to set
   */
  public void setSameOrBeforeWeek( Boolean sameOrBeforeWeek )
  {
    this.sameOrBeforeWeek = sameOrBeforeWeek;
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
