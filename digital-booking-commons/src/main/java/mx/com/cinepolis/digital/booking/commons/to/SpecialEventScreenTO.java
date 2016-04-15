package mx.com.cinepolis.digital.booking.commons.to;

import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author jreyesv Transfer object for Booking special event in theater
 */
public class SpecialEventScreenTO extends AbstractTO implements Comparable<SpecialEventScreenTO>
{
  /**
   * Serial version
   */
  private static final long serialVersionUID = -2979171921878347129L;
  /**
   * id
   */
  private Long id;
  /**
   * id special event associated
   */
  private Long idSpecialEvent;
  /**
   * id observation associated
   */
  private Long idObservation;
  /**
   * id screen associated
   */
  private Long idScreen;
  /**
   * Booking status associated
   */
  private CatalogTO status;
  /**
   * List of shows for a screen of special event booking
   */
  private List<SpecialEventScreenShowTO> specialEventScreenShows;

  /**
   * Indicates whether exist presale in screen.
   */
  private PresaleTO presaleTO;

  /**
   * @return the id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId( Long id )
  {
    this.id = id;
  }

  /**
   * @return the idSpecialEvent
   */
  public Long getIdSpecialEvent()
  {
    return idSpecialEvent;
  }

  /**
   * @param idSpecialEvent the idSpecialEvent to set
   */
  public void setIdSpecialEvent( Long idSpecialEvent )
  {
    this.idSpecialEvent = idSpecialEvent;
  }

  /**
   * @return the idObservation
   */
  public Long getIdObservation()
  {
    return idObservation;
  }

  /**
   * @param idObservation the idObservation to set
   */
  public void setIdObservation( Long idObservation )
  {
    this.idObservation = idObservation;
  }

  /**
   * @return the idScreen
   */
  public Long getIdScreen()
  {
    return idScreen;
  }

  /**
   * @param idScreen the idScreen to set
   */
  public void setIdScreen( Long idScreen )
  {
    this.idScreen = idScreen;
  }

  /**
   * @return the status
   */
  public CatalogTO getStatus()
  {
    return status;
  }

  /**
   * @param status the status to set
   */
  public void setStatus( CatalogTO status )
  {
    this.status = status;
  }

  /**
   * @return the specialEventScreenShows
   */
  public List<SpecialEventScreenShowTO> getSpecialEventScreenShows()
  {
    return specialEventScreenShows;
  }

  /**
   * @param specialEventScreenShows the specialEventScreenShows to set
   */
  public void setSpecialEventScreenShows( List<SpecialEventScreenShowTO> specialEventScreenShows )
  {
    this.specialEventScreenShows = specialEventScreenShows;
  }

  /**
   * @return the presaleTO
   */
  public PresaleTO getPresaleTO()
  {
    return presaleTO;
  }

  /**
   * @param presaleTO the presaleTO to set
   */
  public void setPresaleTO( PresaleTO presaleTO )
  {
    this.presaleTO = presaleTO;
  }

  @Override
  public boolean equals( Object object )
  {
    boolean flag = false;
    if( this == object )
    {
      flag = true;
    }
    else if( object instanceof SpecialEventScreenTO )
    {
      flag = this.id.equals( ((SpecialEventScreenTO) object).id );
    }
    return flag;
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append( this.id ).hashCode();
  }

  @Override
  public int compareTo( SpecialEventScreenTO that )
  {
    return this.id.compareTo( that.id );
  }

  @Override
  public String toString()
  {
    String str = new ToStringBuilder( this ).append( "special event id:", this.getIdSpecialEvent() )
        .append( "screen id:", this.getIdScreen() ).toString();
    return str;
  }

}
