package mx.com.cinepolis.digital.booking.commons.to;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Transfer object from week
 * 
 * @author gsegura
 * @since 0.2.0
 */
public class WeekTO extends AbstractTO
{
  private static final long serialVersionUID = -1489994606402066018L;

  private Integer idWeek;

  private Date startingDayWeek;

  private Date finalDayWeek;

  private String weekDescription;

  private boolean specialWeek;

  private boolean activeWeek;

  private int nuWeek;

  private int nuYear;

  private boolean editable;

  /**
   * Constructor by default
   */
  public WeekTO()
  {
  }

  /**
   * Constructor by id
   * 
   * @param idWeek
   */
  public WeekTO( Integer idWeek )
  {
    this.idWeek = idWeek;
  }

  /**
   * @return the idWeek
   */
  public Integer getIdWeek()
  {
    return idWeek;
  }

  /**
   * @param idWeek the idWeek to set
   */
  public void setIdWeek( Integer idWeek )
  {
    this.idWeek = idWeek;
  }

  /**
   * @return the startingDayWeek
   */
  public Date getStartingDayWeek()
  {
    return CinepolisUtils.enhancedClone( startingDayWeek );
  }

  /**
   * @param startingDayWeek the startingDayWeek to set
   */
  public void setStartingDayWeek( Date startingDayWeek )
  {
    this.startingDayWeek = CinepolisUtils.enhancedClone( startingDayWeek );
  }

  /**
   * @return the finalDayWeek
   */
  public Date getFinalDayWeek()
  {
    return CinepolisUtils.enhancedClone( finalDayWeek );
  }

  /**
   * @param finalDayWeek the finalDayWeek to set
   */
  public void setFinalDayWeek( Date finalDayWeek )
  {
    this.finalDayWeek = CinepolisUtils.enhancedClone( finalDayWeek );
  }

  /**
   * @return the specialWeek
   */
  public boolean isSpecialWeek()
  {
    return specialWeek;
  }

  /**
   * @param specialWeek the specialWeek to set
   */
  public void setSpecialWeek( boolean specialWeek )
  {
    this.specialWeek = specialWeek;
  }

  /**
   * @return the activeWeek
   */
  public boolean isActiveWeek()
  {
    return activeWeek;
  }

  /**
   * @param activeWeek the activeWeek to set
   */
  public void setActiveWeek( boolean activeWeek )
  {
    this.activeWeek = activeWeek;
  }

  /**
   * @return the weekDescription
   */
  public String getWeekDescription()
  {
    return weekDescription;
  }

  /**
   * @param weekDescription the weekDescription to set
   */
  public void setWeekDescription( String weekDescription )
  {
    this.weekDescription = weekDescription;
  }

  /**
   * @return the nuWeek
   */
  public int getNuWeek()
  {
    return nuWeek;
  }

  /**
   * @param nuWeek the nuWeek to set
   */
  public void setNuWeek( int nuWeek )
  {
    this.nuWeek = nuWeek;
  }

  /**
   * @return the nuYear
   */
  public int getNuYear()
  {
    return nuYear;
  }

  /**
   * @param nuYear the nuYear to set
   */
  public void setNuYear( int nuYear )
  {
    this.nuYear = nuYear;
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

  @Override
  public String toString()
  {
    DateFormat df = new SimpleDateFormat( "yyyy-MM-dd" );
    ToStringBuilder builder = new ToStringBuilder( this );
    builder.append( "id", this.idWeek );
    builder.append( "start", df.format( this.startingDayWeek ) );
    builder.append( "final", df.format( this.finalDayWeek ) );
    builder.append( "special", this.specialWeek );
    builder.append( "activeWeek", this.activeWeek );
    builder.append( "active", this.isFgActive() );
    return builder.toString();
  }

  @Override
  public boolean equals( Object object )
  {
    boolean flag = false;
    if( this == object )
    {
      flag = true;
    }
    else if( object instanceof WeekTO )
    {
      flag = this.idWeek.equals( ((WeekTO) object).idWeek );
    }
    return flag;
  }

  @Override
  public int hashCode()
  {
    return this.idWeek.hashCode();
  }

}
