package mx.com.cinepolis.digital.booking.commons.to;

import java.util.List;

/**
 * Transfer object for emailing theaters 
 * 
 * @author jcarbajal
 */
public class TheaterEmailTO extends EmailTO
{
  /**
   * 
   */
  private static final long serialVersionUID = -5258374442101151394L;
  private Long idWeek;
  private List<TheaterTO> theatersTO;
  
  public Long getIdWeek()
  {
    return idWeek;
  }
  public void setIdWeek( Long idWeek )
  {
    this.idWeek = idWeek;
  }
  public List<TheaterTO> getTheatersTO()
  {
    return theatersTO;
  }
  public void setTheatersTO( List<TheaterTO> theatersTO )
  {
    this.theatersTO = theatersTO;
  }
  

}
