package mx.com.cinepolis.digital.booking.commons.to;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author jreyesv Transfer object for Booking special event in theater
 */
public class SpecialEventScreenShowTO extends AbstractTO implements Comparable<SpecialEventScreenShowTO>
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
   * id special event screen associated
   */
  private Long idSpecialEventScreen;
  /**
   * Show number
   */
  private int nuShow;

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
   * @return the idSpecialEventScreen
   */
  public Long getIdSpecialEventScreen()
  {
    return idSpecialEventScreen;
  }

  /**
   * @param idSpecialEventScreen the idSpecialEventScreen to set
   */
  public void setIdSpecialEventScreen( Long idSpecialEventScreen )
  {
    this.idSpecialEventScreen = idSpecialEventScreen;
  }

  /**
   * @return the nuShow
   */
  public int getNuShow()
  {
    return nuShow;
  }

  /**
   * @param nuShow the nuShow to set
   */
  public void setNuShow( int nuShow )
  {
    this.nuShow = nuShow;
  }

  @Override
  public boolean equals( Object object )
  {
    boolean flag = false;
    if( this == object )
    {
      flag = true;
    }
    else if( object instanceof SpecialEventScreenShowTO )
    {
      flag = this.id.equals( ((SpecialEventScreenShowTO) object).id );
    }
    return flag;
  }

  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append( this.id ).hashCode();
  }

  @Override
  public int compareTo( SpecialEventScreenShowTO that )
  {
    return this.id.compareTo( that.id );
  }

  @Override
  public String toString()
  {
    String str = new ToStringBuilder( this ).append( "special event screen id:", this.getIdSpecialEventScreen() )
        .append( "show number:", this.getNuShow() ).toString();
    return str;
  }

}
