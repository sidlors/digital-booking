package mx.com.cinepolis.digital.booking.model;

import java.util.Date;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;

/**
 * JPA entity for BookingIncome
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "K_BOOKING_INCOME")
@NamedQueries({
    @NamedQuery(name = "BookingIncomeDO.findAll", query = "SELECT e FROM BookingIncomeDO e"),
    @NamedQuery(name = "BookingIncomeDO.findByidBookingIncome", query = "SELECT e FROM BookingIncomeDO e WHERE e.idBookingIncome = :idBookingIncome"),
    @NamedQuery(name = "BookingIncomeDO.findAllByDetail", query = "SELECT b FROM BookingIncomeDO b WHERE b.idTheater.idTheater = :idTheater AND b.idWeek.idWeek = :idWeek AND b.idScreen.idScreen = :idScreen AND b.idEvent.idEvent = :idEvent AND b.fgActive = true"),
    @NamedQuery(name = "BookingIncomeDO.findAllByIdTheaterIdWeek", query = "SELECT b FROM BookingIncomeDO b WHERE b.idTheater.idTheater = :idTheater AND b.idWeek.idWeek = :idWeek AND b.fgActive = true"),
    @NamedQuery(name = "BookingIncomeDO.findAllByIdTheaterIdWeekDtShow", query = "SELECT count(b) FROM BookingIncomeDO b WHERE b.idTheater.idTheater = :idTheater AND b.idWeek.idWeek = :idWeek AND b.dtShow = :dtShow AND b.fgActive = true"),
    @NamedQuery(name = "BookingIncomeDO.findIncomesByEventTheaterWeek", query = "SELECT b FROM BookingIncomeDO b WHERE b.idEvent.idEvent = :idEvent AND b.idTheater.idTheater = :idTheater AND b.idWeek.idWeek = :idWeek AND b.fgActive = true"),
    @NamedQuery(name = "BookingIncomeDO.findIncomesByScreenTheaterWeek", query = "SELECT b FROM BookingIncomeDO b WHERE b.idScreen.idScreen = :idScreen AND b.idTheater.idTheater = :idTheater AND b.idWeek.idWeek = :idWeek AND b.fgActive = true") })
public class BookingIncomeDO extends AbstractSignedEntity<BookingIncomeDO>
{
  /**
   * 
   */
  private static final long serialVersionUID = 1615959350108940718L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_BOOKING_INCOME")
  private Long idBookingIncome;

  @JoinColumn(name = "ID_BOOKING", referencedColumnName = "ID_BOOKING", nullable = true)
  @ManyToOne(optional = true, fetch = FetchType.LAZY)
  private BookingDO idBooking;

  @JoinColumn(name = "ID_SCREEN", referencedColumnName = "ID_SCREEN")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private ScreenDO idScreen;

  @JoinColumn(name = "ID_WEEK", referencedColumnName = "ID_WEEK")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private WeekDO idWeek;

  @JoinColumn(name = "ID_THEATER", referencedColumnName = "ID_THEATER")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TheaterDO idTheater;

  @JoinColumn(name = "ID_EVENT", referencedColumnName = "ID_EVENT")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private EventDO idEvent;

  @Column(name = "DT_SHOW")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dtShow;

  @Column(name = "HR_SHOW")
  private String hrShow;

  @Column(name = "QT_INCOME", precision = 13, scale = 2)
  private Double qtIncome;

  @Column(name = "QT_TICKETS")
  private Integer qtTickets;

  /**
   * Constructor default
   */
  public BookingIncomeDO()
  {
  }
  /**
   * Constructor by id
   */
  public BookingIncomeDO(Long idBookingIncome )
  {
    this.idBookingIncome=idBookingIncome;
  }
  /**
   * @return the idBookingIncome
   */
  public Long getIdBookingIncome()
  {
    return idBookingIncome;
  }

  /**
   * @param idBookingIncome the idBookingIncome to set
   */
  public void setIdBookingIncome( Long idBookingIncome )
  {
    this.idBookingIncome = idBookingIncome;
  }

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

  /**
   * @return the idTheater
   */
  public TheaterDO getIdTheater()
  {
    return idTheater;
  }

  /**
   * @param idTheater the idTheater to set
   */
  public void setIdTheater( TheaterDO idTheater )
  {
    this.idTheater = idTheater;
  }

  /**
   * @return the idEvent
   */
  public EventDO getIdEvent()
  {
    return idEvent;
  }

  /**
   * @param idEvent the idEvent to set
   */
  public void setIdEvent( EventDO idEvent )
  {
    this.idEvent = idEvent;
  }

  /**
   * @return the dtShow
   */
  public Date getDtShow()
  {
    return CinepolisUtils.enhancedClone( dtShow );
  }

  /**
   * @param dtShow the dtShow to set
   */
  public void setDtShow( Date dtShow )
  {
    this.dtShow = CinepolisUtils.enhancedClone( dtShow );
  }

  /**
   * @return the hrShow
   */
  public String getHrShow()
  {
    return hrShow;
  }

  /**
   * @param hrShow the hrShow to set
   */
  public void setHrShow( String hrShow )
  {
    this.hrShow = hrShow;
  }

  /**
   * @return the qtIncome
   */
  public Double getQtIncome()
  {
    return qtIncome;
  }

  /**
   * @param qtIncome the qtIncome to set
   */
  public void setQtIncome( Double qtIncome )
  {
    this.qtIncome = qtIncome;
  }

  /**
   * @return the qtTickets
   */
  public Integer getQtTickets()
  {
    return qtTickets;
  }

  /**
   * @param qtTickets the qtTickets to set
   */
  public void setQtTickets( Integer qtTickets )
  {
    this.qtTickets = qtTickets;
  }

  @Override
  public int compareTo( BookingIncomeDO that )
  {
    return this.idBookingIncome.compareTo( that.idBookingIncome );
  }

  @Override
  public boolean equals( Object obj )
  {

    if( !(obj instanceof BookingIncomeDO) )
    {
      return false;
    }
    BookingIncomeDO other = (BookingIncomeDO) obj;
    if( (this.idBookingIncome == null && other.idBookingIncome != null)
        || (this.idBookingIncome != null && !this.idBookingIncome.equals( other.idBookingIncome )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idBookingIncome != null ? idBookingIncome.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return "BookingIncomeDO [idBookingIncome=" + idBookingIncome + ", idBooking=" + idBooking + ", isScreen="
        + idScreen + ", idWeek=" + idWeek + ", dtShow=" + dtShow + ", hrShow=" + hrShow + ", qtIncome=" + qtIncome
        + ", qtTickets=" + qtTickets + "]";
  }

}
