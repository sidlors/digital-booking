/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package mx.com.cinepolis.digital.booking.model;

import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for BookingSpecialEventScreenDO
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "K_BOOKING_SPECIAL_EVENT_SCREEN")
@NamedQueries({
    @NamedQuery(name = "BookingSpecialEventScreenDO.findAll", query = "SELECT e FROM BookingSpecialEventScreenDO e"),
    @NamedQuery(name = "BookingSpecialEventScreenDO.findByidBookingSpecialEventScreen", query = "SELECT e FROM BookingSpecialEventScreenDO e WHERE e.idBookingSpecialEventScreen = :idBookingSpecialEventScreen") })
public class BookingSpecialEventScreenDO extends AbstractEntity<BookingSpecialEventScreenDO>
{
  private static final long serialVersionUID = -2007645776852047009L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_BOOKING_SPECIAL_EVENT_SCREEN")
  private Long idBookingSpecialEventScreen;

  @JoinColumn(name = "ID_BOOKING_SPECIAL_EVENT", referencedColumnName = "ID_BOOKING_SPECIAL_EVENT")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private BookingSpecialEventDO idBookingSpecialEvent;

  @JoinColumn(name = "ID_SCREEN", referencedColumnName = "ID_SCREEN")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private ScreenDO idScreen;

  @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "idBookingSpecialEventScreen", fetch = FetchType.LAZY)
  private List<BookingSpecialEventScreenShowDO> bookingSpecialEventScreenShowDOList;

  @JoinColumn(name = "ID_OBSERVATION", referencedColumnName = "ID_OBSERVATION")
  @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.LAZY)
  private ObservationDO idObservation;

  @JoinColumn(name = "ID_BOOKING_STATUS", referencedColumnName = "ID_BOOKING_STATUS")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private BookingStatusDO idBookingStatus;

  @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "idBookingSpecialEventScreen", fetch = FetchType.LAZY)
  private List<PresaleDO> presaleDOList;

  /**
   * Constructor default
   */
  public BookingSpecialEventScreenDO()
  {
  }

  /**
   * Constructor by id
   */
  public BookingSpecialEventScreenDO(Long idBookingSpecialEventScreen)
  {
    this.idBookingSpecialEventScreen=idBookingSpecialEventScreen;
  }

  @Override
  public int compareTo( BookingSpecialEventScreenDO that )
  {
    return this.idBookingSpecialEventScreen.compareTo( that.idBookingSpecialEventScreen );
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof BookingSpecialEventScreenDO) )
    {
      return false;
    }
    BookingSpecialEventScreenDO other = (BookingSpecialEventScreenDO) object;
    if( (this.idBookingSpecialEventScreen == null && other.idBookingSpecialEventScreen != null)
        || (this.idBookingSpecialEventScreen != null && !this.idBookingSpecialEventScreen
            .equals( other.idBookingSpecialEventScreen )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idBookingSpecialEventScreen != null ? idBookingSpecialEventScreen.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO[ idBookingSpecialEventScreen="
        + idBookingSpecialEventScreen + " ]";
  }

  /**
   * @return the idBookingSpecialEventScreen
   */
  public Long getIdBookingSpecialEventScreen()
  {
    return idBookingSpecialEventScreen;
  }

  /**
   * @param idBookingSpecialEventScreen the idBookingSpecialEventScreen to set
   */
  public void setIdBookingSpecialEventScreen( Long idBookingSpecialEventScreen )
  {
    this.idBookingSpecialEventScreen = idBookingSpecialEventScreen;
  }

  /**
   * @return the idBookingSpecialEvent
   */
  public BookingSpecialEventDO getIdBookingSpecialEvent()
  {
    return idBookingSpecialEvent;
  }

  /**
   * @param idBookingSpecialEvent the idBookingSpecialEvent to set
   */
  public void setIdBookingSpecialEvent( BookingSpecialEventDO idBookingSpecialEvent )
  {
    this.idBookingSpecialEvent = idBookingSpecialEvent;
  }

  /**
   * @return the idScreen
   */
  public ScreenDO getIdScreen()
  {
    return idScreen;
  }

  /**
   * @param idScreen the idScreen to set
   */
  public void setIdScreen( ScreenDO idScreen )
  {
    this.idScreen = idScreen;
  }

  /**
   * @return the bookingSpecialEventScreenShowDOList
   */
  public List<BookingSpecialEventScreenShowDO> getBookingSpecialEventScreenShowDOList()
  {
    return bookingSpecialEventScreenShowDOList;
  }

  /**
   * @param bookingSpecialEventScreenShowDOList the bookingSpecialEventScreenShowDOList to set
   */
  public void setBookingSpecialEventScreenShowDOList(
      List<BookingSpecialEventScreenShowDO> bookingSpecialEventScreenShowDOList )
  {
    this.bookingSpecialEventScreenShowDOList = bookingSpecialEventScreenShowDOList;
  }

  /**
   * @return the idObservation
   */
  public ObservationDO getIdObservation()
  {
    return idObservation;
  }

  /**
   * @param idObservation the idObservation to set
   */
  public void setIdObservation( ObservationDO idObservation )
  {
    this.idObservation = idObservation;
  }

  /**
   * @return the idBookingStatus
   */
  public BookingStatusDO getIdBookingStatus()
  {
    return idBookingStatus;
  }

  /**
   * @param idBookingStatus the idBookingStatus to set
   */
  public void setIdBookingStatus( BookingStatusDO idBookingStatus )
  {
    this.idBookingStatus = idBookingStatus;
  }

  /**
   * @return presaleDOList
   */
  public List<PresaleDO> getPresaleDOList()
  {
    return presaleDOList;
  }

  /**
   * @param presaleDOList the presaleDOList to set
   */

  public void setPresaleDOList( List<PresaleDO> presaleDOList )
  {
    this.presaleDOList = presaleDOList;
  }

}
