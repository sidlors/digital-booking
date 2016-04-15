/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package mx.com.cinepolis.digital.booking.model;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

/**
 * JPA entity for K_BOOKING_SPECIAL_EVENT
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "K_BOOKING_SPECIAL_EVENT")
@NamedQueries({
    @NamedQuery(name = "BookingSpecialEventDO.findAll", query = "SELECT e FROM BookingSpecialEventDO e"),
    @NamedQuery(name = "BookingSpecialEventDO.findByidBookingSpecialEvent", query = "SELECT e FROM BookingSpecialEventDO e WHERE e.idBookingSpecialEvent = :idBookingSpecialEvent") })
public class BookingSpecialEventDO extends AbstractSignedEntity<BookingSpecialEventDO>
{
  private static final long serialVersionUID = -4881122773010202589L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_BOOKING_SPECIAL_EVENT")
  private Long idBookingSpecialEvent;

  @JoinColumn(name = "ID_BOOKING", referencedColumnName = "ID_BOOKING")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private BookingDO idBooking;

  @Column(name = "DT_START_SPECIAL_EVENT")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dtStartSpecialEvent;

  @Column(name = "DT_END_SPECIAL_EVENT")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dtEndSpecialEvent;

  @Column(name = "QT_COPY")
  private Integer qtCopy;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "idBookingSpecialEvent", fetch = FetchType.LAZY)
  private List<BookingSpecialEventScreenDO> bookingSpecialEventScreenDOList;

  @JoinColumn(name = "ID_BOOKING_STATUS", referencedColumnName = "ID_BOOKING_STATUS")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private BookingStatusDO idBookingStatus;

  @Column(name = "QT_COPY_SCREEN_ZERO")
  private int qtCopyScreenZero;

  @Column(name = "QT_COPY_SCREEN_ZERO_TERMINATED")
  private int qtCopyScreenZeroTerminated;
  
  @OneToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE}, mappedBy="idBookingSpecialEvent", fetch= FetchType.LAZY)
  private List<SpecialEventWeekDO> specialEventWeekDOList;
  
/**
   * Constructor default
   */
  public BookingSpecialEventDO()
  {
  }
/**
 * Constructor using idBookingSpecialEvent
 * @param idBookingSpecialEvent
 */
  public BookingSpecialEventDO( Long idBookingSpecialEvent )
  {
    this.idBookingSpecialEvent = idBookingSpecialEvent;
  }

  @Column(name = "FG_SHOW_DATE", nullable = true)
  private Boolean fgShowDate;

  /**
   * @return the idBooking
   */
  public BookingDO getIdBooking()
  {
    return idBooking;
  }

  /**
   * @param idBooking the idBooking to set
   */
  public void setIdBooking( BookingDO idBooking )
  {
    this.idBooking = idBooking;
  }

  /**
   * @return the idBookingSpecialEvent
   */
  public Long getIdBookingSpecialEvent()
  {
    return idBookingSpecialEvent;
  }

  /**
   * @param idBookongSpecialEvent the idBookingSpecialEvent to set
   */
  public void setIdBookingSpecialEvent( Long idBookingSpecialEvent )
  {
    this.idBookingSpecialEvent = idBookingSpecialEvent;
  }

  /**
   * @return the dtStartSpecialEvent
   */
  public Date getDtStartSpecialEvent()
  {
    return CinepolisUtils.enhancedClone( dtStartSpecialEvent );
  }

  /**
   * @param dtStartSpecialEvent the dtStartSpecialEvent to set
   */
  public void setDtStartSpecialEvent( Date dtStartSpecialEvent )
  {
    this.dtStartSpecialEvent = CinepolisUtils.enhancedClone( dtStartSpecialEvent );
  }

  /**
   * @return the dtEndSpecialEvent
   */
  public Date getDtEndSpecialEvent()
  {
    return CinepolisUtils.enhancedClone( dtEndSpecialEvent );
  }

  /**
   * @param dtEndSpecialEvent the dtEndSpecialEvent to set
   */
  public void setDtEndSpecialEvent( Date dtEndSpecialEvent )
  {
    this.dtEndSpecialEvent = CinepolisUtils.enhancedClone( dtEndSpecialEvent );
  }

  /**
   * @return the qtCopy
   */
  public Integer getQtCopy()
  {
    return qtCopy;
  }

  /**
   * @param qtCopy the qtCopy to set
   */
  public void setQtCopy( Integer qtCopy )
  {
    this.qtCopy = qtCopy;
  }

  @Override
  public int compareTo( BookingSpecialEventDO o )
  {
    // TODO Auto-generated method stub
    return this.idBookingSpecialEvent.compareTo( o.idBookingSpecialEvent );
  }

  @Override
  public boolean equals( Object obj )
  {
    // TODO Auto-generated method stub
    if( !(obj instanceof BookingSpecialEventDO) )
    {
      return false;
    }
    BookingSpecialEventDO other = (BookingSpecialEventDO) obj;
    if( (this.idBookingSpecialEvent == null && other.idBookingSpecialEvent != null)
        || (this.idBookingSpecialEvent != null && !this.idBookingSpecialEvent.equals( other.idBookingSpecialEvent )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idBookingSpecialEvent != null ? idBookingSpecialEvent.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO[ idBookingSpecialEvent="
        + idBookingSpecialEvent + " ]";
  }

  /**
   * @return the bookingSpecialEventScreenDOList
   */
  public List<BookingSpecialEventScreenDO> getBookingSpecialEventScreenDOList()
  {
    return bookingSpecialEventScreenDOList;
  }

  /**
   * @param bookingSpecialEventScreenDOList the bookingSpecialEventScreenDOList to set
   */
  public void setBookingSpecialEventScreenDOList( List<BookingSpecialEventScreenDO> bookingSpecialEventScreenDOList )
  {
    this.bookingSpecialEventScreenDOList = bookingSpecialEventScreenDOList;
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
   * @return the qtCopyScreenZero
   */
  public int getQtCopyScreenZero()
  {
    return qtCopyScreenZero;
  }

  /**
   * @param qtCopyScreenZero the qtCopyScreenZero to set
   */
  public void setQtCopyScreenZero( int qtCopyScreenZero )
  {
    this.qtCopyScreenZero = qtCopyScreenZero;
  }

  /**
   * @return the qtCopyScreenZeroTerminated
   */
  public int getQtCopyScreenZeroTerminated()
  {
    return qtCopyScreenZeroTerminated;
  }

  /**
   * @param qtCopyScreenZeroTerminated the qtCopyScreenZeroTerminated to set
   */
  public void setQtCopyScreenZeroTerminated( int qtCopyScreenZeroTerminated )
  {
    this.qtCopyScreenZeroTerminated = qtCopyScreenZeroTerminated;
  }

  /**
   * @return the fgShowDate
   */
  public Boolean getFgShowDate()
  {
    return fgShowDate;
  }

  /**
   * @param fgShowDate the fgShowDate to set
   */
  public void setFgShowDate( Boolean fgShowDate )
  {
    this.fgShowDate = fgShowDate;
  }
  public List<SpecialEventWeekDO> getSpecialEventWeekDOList() {
		return specialEventWeekDOList;
  }
  public void setSpecialEventWeekDOList(List<SpecialEventWeekDO> specialEventWeekDOList) {
		this.specialEventWeekDOList = specialEventWeekDOList;
  }
}
