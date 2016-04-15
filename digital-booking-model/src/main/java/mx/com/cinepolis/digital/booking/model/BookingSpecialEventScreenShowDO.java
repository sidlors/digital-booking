package mx.com.cinepolis.digital.booking.model;

import javax.persistence.CascadeType;
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
 * JPA entity for K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "K_BOOKING_SPECIAL_EVENT_SCREEN_SHOW")
@NamedQueries({
    @NamedQuery(name = "BookingSpecialEventScreenShowDO.findAll", query = "SELECT e FROM BookingSpecialEventScreenShowDO e"),
    @NamedQuery(name = "BookingSpecialEventScreenShowDO.findByidBookingSpecialEventScreenShow", query = "SELECT e FROM BookingSpecialEventScreenShowDO e WHERE e.idBookingSpecialEventScreenShow = :idBookingSpecialEventScreenShow"),
    @NamedQuery(name = "BookingSpecialEventScreenShowDO.findByidBookingSpecialEvent", query = "SELECT DISTINCT e.nuShow FROM BookingSpecialEventScreenShowDO e WHERE e.idBookingSpecialEventScreen.idBookingSpecialEventScreen IN (SELECT DISTINCT kbses.idBookingSpecialEventScreen FROM BookingSpecialEventScreenDO kbses WHERE kbses.idBookingSpecialEvent.idBookingSpecialEvent = ?1)") })
public class BookingSpecialEventScreenShowDO extends AbstractEntity<BookingSpecialEventScreenShowDO>
{
  private static final long serialVersionUID = 9186317817801449153L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_BOOKING_SPECIAL_EVENT_SCREEN_SHOW")
  private Long idBookingSpecialEventScreenShow;

  @JoinColumn(name = "ID_BOOKING_SPECIAL_EVENT_SCREEN", referencedColumnName = "ID_BOOKING_SPECIAL_EVENT_SCREEN")
  @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  private BookingSpecialEventScreenDO idBookingSpecialEventScreen;

  @Column(name = "NU_SHOW")
  private Integer nuShow;

  public BookingSpecialEventScreenShowDO()
  {
  }

  @Override
  public int compareTo( BookingSpecialEventScreenShowDO o )
  {
    return this.idBookingSpecialEventScreenShow.compareTo( o.idBookingSpecialEventScreenShow );
  }

  @Override
  public boolean equals( Object obj )
  {
    if( !(obj instanceof BookingSpecialEventScreenShowDO) )
    {
      return false;
    }
    BookingSpecialEventScreenShowDO other = (BookingSpecialEventScreenShowDO) obj;
    if( (this.idBookingSpecialEventScreenShow == null && other.idBookingSpecialEventScreenShow != null)
        || (this.idBookingSpecialEventScreenShow != null && !this.idBookingSpecialEventScreenShow
            .equals( other.idBookingSpecialEventScreenShow )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idBookingSpecialEventScreenShow != null ? idBookingSpecialEventScreenShow.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenShowDO[ idBookingSpecialEventScreenShow="
        + idBookingSpecialEventScreenShow + " ]";
  }

  /**
   * @return the idBookingSpecialEventScreenShow
   */
  public Long getIdBookingSpecialEventScreenShow()
  {
    return idBookingSpecialEventScreenShow;
  }

  /**
   * @param idBookingSpecialEventScreenShow the idBookingSpecialEventScreenShow to set
   */
  public void setIdBookingSpecialEventScreenShow( Long idBookingSpecialEventScreenShow )
  {
    this.idBookingSpecialEventScreenShow = idBookingSpecialEventScreenShow;
  }

  /**
   * @return the idBookingSpecialEventScreen
   */
  public BookingSpecialEventScreenDO getIdBookingSpecialEventScreen()
  {
    return idBookingSpecialEventScreen;
  }

  /**
   * @param idBookingSpecialEventScreen the idBookingSpecialEventScreen to set
   */
  public void setIdBookingSpecialEventScreen( BookingSpecialEventScreenDO idBookingSpecialEventScreen )
  {
    this.idBookingSpecialEventScreen = idBookingSpecialEventScreen;
  }

  /**
   * @return the nuShow
   */
  public Integer getNuShow()
  {
    return nuShow;
  }

  /**
   * @param nuShow the nuShow to set
   */
  public void setNuShow( Integer nuShow )
  {
    this.nuShow = nuShow;
  }

}
