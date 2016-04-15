package mx.com.cinepolis.digital.booking.commons.to.reports;

import java.io.Serializable;

/**
 * Objeto de transferencia del encabezado del reporte de programacion semanal
 * 
 * @author rgarcia
 * @since 0.1.0
 */
public class WeeklyDistributorReportHeaderTO implements Serializable
{
  /**
   * 
   */
  private static final long serialVersionUID = -3196485397782222609L;
  private String title;
  private String strDistributor;
  private String strExhibitionWeek;

  /**
   * @return the title
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * @param title the title to set
   */
  public void setTitle( String title )
  {
    this.title = title;
  }

  /**
   * @return the strDistributor
   */
  public String getStrDistributor()
  {
    return strDistributor;
  }

  /**
   * @param strDistributor the strDistributor to set
   */
  public void setStrDistributor( String strDistributor )
  {
    this.strDistributor = strDistributor;
  }

  /**
   * @return the strExhibitionWeek
   */
  public String getStrExhibitionWeek()
  {
    return strExhibitionWeek;
  }

  /**
   * @param strExhibitionWeek the strExhibitionWeek to set
   */
  public void setStrExhibitionWeek( String strExhibitionWeek )
  {
    this.strExhibitionWeek = strExhibitionWeek;
  }

}
