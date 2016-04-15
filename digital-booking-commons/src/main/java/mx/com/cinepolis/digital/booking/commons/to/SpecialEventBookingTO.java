package mx.com.cinepolis.digital.booking.commons.to;

import java.io.Serializable;

/**
 * Transfer object for Booking
 * 
 * @author jcarbajal
 * @since 0.2.0
 */
public class SpecialEventBookingTO extends AbstractTO implements Serializable
{
  private static final long serialVersionUID = 340826687726381972L;
  private PagingResponseTO<SpecialEventTO> reponse;

  /**
   * @return the reponse
   */
  public PagingResponseTO<SpecialEventTO> getReponse()
  {
    return reponse;
  }

  /**
   * @param reponse the reponse to set
   */
  public void setReponse( PagingResponseTO<SpecialEventTO> reponse )
  {
    this.reponse = reponse;
  }


}
