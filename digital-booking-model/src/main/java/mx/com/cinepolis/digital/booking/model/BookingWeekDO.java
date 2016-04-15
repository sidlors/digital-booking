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
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * JPA for entity K_BOOKING_WEEK
 * 
 * @author gsegura
 * @since 0.2.7
 */
@Entity
@Table(name = "K_BOOKING_WEEK")
@NamedQueries({
    @NamedQuery(name = "BookingWeekDO.findAll", query = "SELECT b FROM BookingWeekDO b"),
    @NamedQuery(name = "BookingWeekDO.findByIdBookingWeek", query = "SELECT b FROM BookingWeekDO b WHERE b.idBookingWeek = :idBookingWeek"),
    @NamedQuery(name = "BookingWeekDO.findByDtExhibitionEndDate", query = "SELECT b FROM BookingWeekDO b WHERE b.dtExhibitionEndDate = :dtExhibitionEndDate"),
    @NamedQuery(name = "BookingWeekDO.findByNuExhibitionWeek", query = "SELECT b FROM BookingWeekDO b WHERE b.nuExhibitionWeek = :nuExhibitionWeek"),
    @NamedQuery(name = "BookingWeekDO.findByQtCopy", query = "SELECT b FROM BookingWeekDO b WHERE b.qtCopy = :qtCopy"),
    @NamedQuery(name = "BookingWeekDO.findByFgSend", query = "SELECT b FROM BookingWeekDO b WHERE b.fgSend = :fgSend"),
    @NamedQuery(name = "BookingWeekDO.findByFgActive", query = "SELECT b FROM BookingWeekDO b WHERE b.fgActive = :fgActive"),
    @NamedQuery(name = "BookingWeekDO.findByDtLastModification", query = "SELECT b FROM BookingWeekDO b WHERE b.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "BookingWeekDO.findByIdLastUserModifier", query = "SELECT b FROM BookingWeekDO b WHERE b.idLastUserModifier = :idLastUserModifier"),
    @NamedQuery(name = "BookingWeekDO.updateSentStatus", query = "UPDATE BookingWeekDO bw SET bw.fgSend = true WHERE bw.idBookingWeek = :idBookingWeek"),
    @NamedQuery(name = "BookingWeekDO.countBookingWeek", query = "SELECT COUNT(b) FROM BookingWeekDO b WHERE b.idWeek = :idWeek and b.fgActive = true"),
    @NamedQuery(name = "BookingWeekDO.findByIdTheaterAndIdWeek", query = "SELECT bw FROM BookingWeekDO bw INNER JOIN bw.idBooking AS b INNER JOIN b.idTheater AS t WHERE t.idTheater = :idTheater  AND b.fgActive = true AND bw.idWeek.idWeek = :idWeek AND bw.fgActive = true"),
    @NamedQuery(name = "BookingWeekDO.findfollowingWeekBooking", query = "SELECT b FROM BookingWeekDO b WHERE b.idBooking.idBooking = :idBooking and b.idWeek.dtStartingDayWeek > :startingDay and b.fgActive = true"),
    @NamedQuery(name = "BookingWeekDO.findByBookingAndWeek", query = "SELECT b FROM BookingWeekDO b WHERE b.idBooking.idBooking = :idBooking and b.idWeek.idWeek = :idWeek and b.fgActive = true"),
    @NamedQuery(name = "BookingWeekDO.findByTheaterAndWeek", query = "SELECT bw FROM BookingWeekDO bw INNER JOIN bw.idBooking as b WHERE bw.idWeek.idWeek = :idWeek and b.idTheater.idTheater = :idTheater and bw.fgActive = true and b.fgActive = true") })
public class BookingWeekDO extends AbstractSignedEntity<BookingWeekDO>
{

  private static final long serialVersionUID = -5919017097887309366L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_BOOKING_WEEK")
  private Long idBookingWeek;

  @Column(name = "DT_EXHIBITION_END_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date dtExhibitionEndDate;

  @Column(name = "NU_EXHIBITION_WEEK")
  private int nuExhibitionWeek;

  @Column(name = "QT_COPY")
  private int qtCopy;

  @Column(name = "QT_COPY_SCREEN_ZERO")
  private int qtCopyScreenZero;

  @Column(name = "QT_COPY_SCREEN_ZERO_TERMINATED")
  private int qtCopyScreenZeroTerminated;

  @Column(name = "FG_SEND")
  private boolean fgSend;

  @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "idBookingWeek", fetch = FetchType.LAZY)
  private List<BookingWeekScreenDO> bookingWeekScreenDOList;

  @JoinColumn(name = "ID_BOOKING", referencedColumnName = "ID_BOOKING")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private BookingDO idBooking;

  @JoinColumn(name = "ID_WEEK", referencedColumnName = "ID_WEEK")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private WeekDO idWeek;

  @JoinColumn(name = "ID_BOOKING_STATUS", referencedColumnName = "ID_BOOKING_STATUS")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private BookingStatusDO idBookingStatus;

  /**
   * Constructor default
   */
  public BookingWeekDO()
  {
  }

  /**
   * Constructor by id
   * 
   * @param idBookingWeek
   */
  public BookingWeekDO( Long idBookingWeek )
  {
    this.idBookingWeek = idBookingWeek;
  }

  /**
   * @return the idBookingWeek
   */
  public Long getIdBookingWeek()
  {
    return idBookingWeek;
  }

  /**
   * @param idBookingWeek the idBookingWeek to set
   */
  public void setIdBookingWeek( Long idBookingWeek )
  {
    this.idBookingWeek = idBookingWeek;
  }

  /**
   * @return the dtExhibitionEndDate
   */
  public Date getDtExhibitionEndDate()
  {
    return CinepolisUtils.enhancedClone( dtExhibitionEndDate );
  }

  /**
   * @param dtExhibitionEndDate the dtExhibitionEndDate to set
   */
  public void setDtExhibitionEndDate( Date dtExhibitionEndDate )
  {
    this.dtExhibitionEndDate = CinepolisUtils.enhancedClone( dtExhibitionEndDate );
  }

  /**
   * @return the nuExhibitionWeek
   */
  public int getNuExhibitionWeek()
  {
    return nuExhibitionWeek;
  }

  /**
   * @param nuExhibitionWeek the nuExhibitionWeek to set
   */
  public void setNuExhibitionWeek( int nuExhibitionWeek )
  {
    this.nuExhibitionWeek = nuExhibitionWeek;
  }

  /**
   * @return the qtCopy
   */
  public int getQtCopy()
  {
    return qtCopy;
  }

  /**
   * @param qtCopy the qtCopy to set
   */
  public void setQtCopy( int qtCopy )
  {
    this.qtCopy = qtCopy;
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
   * @return the fgSend
   */
  public boolean isFgSend()
  {
    return fgSend;
  }

  /**
   * @param fgSend the fgSend to set
   */
  public void setFgSend( boolean fgSend )
  {
    this.fgSend = fgSend;
  }

  /**
   * @return the bookingWeekScreenDOList
   */
  public List<BookingWeekScreenDO> getBookingWeekScreenDOList()
  {
    return bookingWeekScreenDOList;
  }

  /**
   * @param bookingWeekScreenDOList the bookingWeekScreenDOList to set
   */
  public void setBookingWeekScreenDOList( List<BookingWeekScreenDO> bookingWeekScreenDOList )
  {
    this.bookingWeekScreenDOList = bookingWeekScreenDOList;
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

  @Override
  public int compareTo( BookingWeekDO that )
  {
    return this.idBookingWeek.compareTo( that.idBookingWeek );
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof BookingWeekDO) )
    {
      return false;
    }
    BookingWeekDO other = (BookingWeekDO) object;
    if( (this.idBookingWeek == null && other.idBookingWeek != null)
        || (this.idBookingWeek != null && !this.idBookingWeek.equals( other.idBookingWeek )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idBookingWeek != null ? idBookingWeek.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "idBookingWeek", this.idBookingWeek )
        .append( "idBooking", this.idBooking.getIdBooking() ).append( "idWeek", this.idWeek.getIdWeek() ).toString();
  }

}
