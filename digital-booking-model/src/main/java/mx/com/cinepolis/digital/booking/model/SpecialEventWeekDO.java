package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * JPA entity for K_SPECIAL_EVENT_WEEK
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "K_SPECIAL_EVENT_WEEK")
@NamedQueries({
    @NamedQuery(name = "SpecialEventWeekDO.findAll", query = "SELECT e FROM SpecialEventWeekDO e"),
    @NamedQuery(name = "SpecialEventWeekDO.findByidSpecialEventWeek", query = "SELECT e FROM SpecialEventWeekDO e WHERE e.idSpecialEventWeek = :idSpecialEventWeek") })
public class SpecialEventWeekDO extends AbstractSignedEntity<SpecialEventWeekDO>
{

  /**
   * Serialización
   */
  private static final long serialVersionUID = 975009467352888349L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_SPECIAL_EVENT_WEEK")
  private Long idSpecialEventWeek;

  @JoinColumn(name = "ID_BOOKING_SPECIAL_EVENT", referencedColumnName = "ID_BOOKING_SPECIAL_EVENT")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private BookingSpecialEventDO idBookingSpecialEvent;

  @JoinColumn(name = "ID_WEEK", referencedColumnName = "ID_WEEK")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private WeekDO idWeek;

  /**
   * Contructor deafult
   */
  
  public SpecialEventWeekDO()
  {
    
  }

  /**
   * @return the idSpecialEventWeek
   */
  public Long getIdSpecialEventWeek()
  {
    return idSpecialEventWeek;
  }

  public BookingSpecialEventDO getIdBookingSpecialEvent()
  {
    return idBookingSpecialEvent;
  }
  /**
   * @param idSpecialEventWeek the idSpecialEventWeek to set
   */
  public void setIdSpecialEventWeek( Long idSpecialEventWeek )
  {
    this.idSpecialEventWeek = idSpecialEventWeek;
  }

  /**
   * @return the idSpecialEvent
   */
  public BookingSpecialEventDO getIdBookingSpecialEventt()
  {
    return idBookingSpecialEvent;
  }

  /**
   * @param idSpecialEvent the idSpecialEvent to set
   */
  public void setIdBookingSpecialEvent( BookingSpecialEventDO idBookingSpecialEvent )
  {
    this.idBookingSpecialEvent = idBookingSpecialEvent;
  }

  /**
   * @return the idWeek
   */
  public WeekDO getIdWeek()
  {
    return idWeek;
  }

  /**
   * @param idWeek the idWeek to set
   */
  public void setIdWeek( WeekDO idWeek )
  {
    this.idWeek = idWeek;
  }

  @Override
  public int compareTo( SpecialEventWeekDO o )
  {
    return this.idSpecialEventWeek.compareTo( o.idSpecialEventWeek );
  }

  @Override
  public boolean equals( Object obj )
  {
    if( !(obj instanceof SpecialEventWeekDO) )
    {
      return false;
    }
    SpecialEventWeekDO object = (SpecialEventWeekDO) obj;
    if( (this.idSpecialEventWeek == null && object.idSpecialEventWeek != null)
        || (this.idSpecialEventWeek != null && !this.idSpecialEventWeek.equals( object.idSpecialEventWeek )) )
    {
      return false;
    }
    return false;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idSpecialEventWeek != null ? idSpecialEventWeek.hashCode() : 0);
    return hash;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "SpecialEventWeekDO [idSpecialEventWeek=" + idSpecialEventWeek + ", idBookingSpecialEvent=" + idBookingSpecialEvent
        + ", idWeek=" + idWeek + "]";
  }
  
}
