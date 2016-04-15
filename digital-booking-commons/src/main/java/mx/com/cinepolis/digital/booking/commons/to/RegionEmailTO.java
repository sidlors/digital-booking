package mx.com.cinepolis.digital.booking.commons.to;

/**
 * Transfer object for emailing regions
 * 
 * @author gsegura
 * @since 0.2.5
 */
public class RegionEmailTO extends EmailTO
{

  /**
   * 
   */
  private static final long serialVersionUID = 7609178428357151294L;
  private Long idWeek;
  private Long idRegion;

  /**
   * @return the idWeek
   */
  public Long getIdWeek()
  {
    return idWeek;
  }

  /**
   * @param idWeek the idWeek to set
   */
  public void setIdWeek( Long idWeek )
  {
    this.idWeek = idWeek;
  }

  /**
   * @return the idRegion
   */
  public Long getIdRegion()
  {
    return idRegion;
  }

  /**
   * @param idRegion the idRegion to set
   */
  public void setIdRegion( Long idRegion )
  {
    this.idRegion = idRegion;
  }

}
