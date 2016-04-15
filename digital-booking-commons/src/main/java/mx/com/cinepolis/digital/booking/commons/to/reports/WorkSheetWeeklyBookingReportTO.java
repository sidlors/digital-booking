package mx.com.cinepolis.digital.booking.commons.to.reports;

import java.io.Serializable;
import java.util.List;

/**
 * Objeto de transferencia de las programaciones de la semana, ya sea por cine o por todos los cines
 * 
 * @author rgarcia
 * @since 0.1.0
 */
public class WorkSheetWeeklyBookingReportTO implements Serializable
{
 
  /**
   * 
   */
  private static final long serialVersionUID = 6822395968353010762L;
  private WeeklyBookingReportHeaderTO header;
  private List<WeeklyBookingReportTheaterTO> theaters;
  /**
   * @return the header
   */
  public WeeklyBookingReportHeaderTO getHeader()
  {
    return header;
  }
  /**
   * @param header the header to set
   */
  public void setHeader( WeeklyBookingReportHeaderTO header )
  {
    this.header = header;
  }
  /**
   * @return the theaters
   */
  public List<WeeklyBookingReportTheaterTO> getTheaters()
  {
    return theaters;
  }
  /**
   * @param theaters the theaters to set
   */
  public void setTheaters( List<WeeklyBookingReportTheaterTO> theaters )
  {
    this.theaters = theaters;
  }
  
  
  

}
