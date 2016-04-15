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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * JPA entity for K_BOOKING
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "K_BOOKING")
@NamedQueries({
    @NamedQuery(name = "BookingDO.findAll", query = "SELECT b FROM BookingDO b"),
    @NamedQuery(name = "BookingDO.findByIdBooking", query = "SELECT b FROM BookingDO b WHERE b.idBooking = :idBooking"),
    @NamedQuery(name = "BookingDO.findByFgActive", query = "SELECT b FROM BookingDO b WHERE b.fgActive = :fgActive"),
    @NamedQuery(name = "BookingDO.findByDtLastModification", query = "SELECT b FROM BookingDO b WHERE b.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "BookingDO.findByIdLastUserModifier", query = "SELECT b FROM BookingDO b WHERE b.idLastUserModifier = :idLastUserModifier"),
    @NamedQuery(name = "BookingDO.findTheatersByRegionAndWeekAndBookingStatus", query = "SELECT DISTINCT b.idTheater FROM  BookingDO b INNER JOIN b.bookingWeekDOList as bw WHERE bw.idWeek.idWeek = :idWeek AND b.idTheater.idRegion.idRegion = :idRegion AND bw.idBookingStatus.idBookingStatus IN :idBookingStatus AND b.fgActive = 1 AND bw.fgActive = 1 ORDER BY b.idTheater.idTheater "),
    @NamedQuery(name = "BookingDO.findTheatersByRegionAndWeekAndBookingStatusCount", query = "SELECT  COUNT(DISTINCT b.idTheater) FROM  BookingDO b INNER JOIN b.bookingWeekDOList as bw WHERE bw.idWeek.idWeek = :idWeek AND b.idTheater.idRegion.idRegion = :idRegion AND bw.idBookingStatus.idBookingStatus IN :idBookingStatus AND b.fgActive = 1 AND bw.fgActive = 1"),
    @NamedQuery(name = "BookingDO.countBookingTheaterWithNoScreensSelected", query = "SELECT COUNT(b) FROM  BookingDO b INNER JOIN b.bookingWeekDOList as bw WHERE b.idTheater.idTheater = :idTheater AND b.fgActive = 1 AND bw.idWeek.idWeek = :idWeek AND bw.idBookingStatus.idBookingStatus = 1 AND bw.fgActive = 1 AND bw.qtCopy > (SELECT COUNT(bws) FROM BookingWeekScreenDO bws WHERE bws.idBookingWeek.idBookingWeek = bw.idBookingWeek AND bws.idBookingStatus.idBookingStatus = 1 )"),
    @NamedQuery(name = "BookingDO.countBookingTheater", query = "SELECT COUNT(b) FROM  BookingDO b INNER JOIN b.bookingWeekDOList as bw WHERE b.idTheater.idTheater = :idTheater AND b.fgActive = 1 AND bw.idWeek.idWeek = :idWeek AND bw.idBookingStatus.idBookingStatus = 1 AND bw.fgActive = 1 AND bw.qtCopy > 0 "),
    @NamedQuery(name = "BookingDO.findByIdTheaterAndBooked", query = "SELECT b FROM BookingDO b WHERE b.idTheater.idTheater = :idTheater AND b.fgBooked = true AND b.fgActive = true"),
    @NamedQuery(name = "BookingDO.findByEventIdAndTheaterId", query = "SELECT b FROM BookingDO b WHERE b.idTheater.idTheater = :idTheater AND b.idEvent.idEvent = :idEvent and b.fgActive = true"),
    @NamedQuery(name = "BookingDO.findByEventAndTheaterAndType", query = "SELECT b FROM BookingDO b WHERE b.idTheater.idTheater = :idTheater AND b.idEvent.idEvent = :idEvent and b.idBookingType.idBookingType = :idBookingType and  b.fgActive = true"),
    @NamedQuery(name = "BookingDO.findBookedByTheater", query = "SELECT b FROM BookingDO b WHERE b.idTheater.idTheater = :idTheater AND b.fgBooked = true and b.fgActive = true"),
    @NamedQuery(name = "BookingDO.findExhibitionWeeks", query = "SELECT e.idEvent, t.idTheater, COUNT(bw) AS n FROM BookingDO b INNER JOIN b.idEvent AS e INNER JOIN b.idTheater AS t INNER JOIN b.bookingWeekDOList AS bw WHERE e.idEvent = :idEvent GROUP BY e.idEvent, t.idTheater ORDER BY COUNT(bw) DESC"),
    @NamedQuery(name = "BookingDO.countBookedEventMovie", query = "SELECT COUNT(b) FROM BookingDO b INNER JOIN b.idEvent e WHERE e.idEvent = :idEvent AND b.fgBooked = true and b.fgActive = true"),
    @NamedQuery(name = "BookingDO.findExhibitionWeeks", query = "SELECT e.idEvent, t.idTheater, COUNT(bw) AS n FROM BookingDO b INNER JOIN b.idEvent AS e INNER JOIN b.idTheater AS t INNER JOIN b.bookingWeekDOList AS bw WHERE e.idEvent = :idEvent GROUP BY e.idEvent, t.idTheater ORDER BY COUNT(bw) DESC"),
    @NamedQuery(name = "BookingDO.countPrereleaseBooked", query = "SELECT COUNT(b) FROM BookingDO b WHERE b.idEvent.idEvent = :idEvent AND b.fgBooked = true and b.idBookingType.idBookingType = 2 AND b.fgActive = 1" ),
    @NamedQuery(name = "BookingDO.findPremiereBooking", query = "SELECT DISTINCT e.idVista FROM EventDO e INNER JOIN e.bookingDOList as b WHERE e.fgActive = true AND e.fgActiveIa = true AND b.fgBooked = true" )
    })
public class BookingDO extends AbstractSignedEntity<BookingDO>
{
  private static final long serialVersionUID = -637478864144090349L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_BOOKING", nullable = false)
  private Long idBooking;

  @JoinColumn(name = "ID_EVENT", referencedColumnName = "ID_EVENT")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private EventDO idEvent;

  @JoinColumn(name = "ID_THEATER", referencedColumnName = "ID_THEATER")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TheaterDO idTheater;


  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "idBooking", fetch = FetchType.LAZY)
  private List<BookingWeekDO> bookingWeekDOList;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "idBooking", fetch = FetchType.LAZY)
  private List<BookingSpecialEventDO> bookingSpecialEventDOList;

  @Column(name = "FG_BOOKED", nullable = false)
  private Boolean fgBooked;
  
  @JoinColumn(name = "ID_BOOKING_TYPE", referencedColumnName = "ID_BOOKING_TYPE",  nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private BookingTypeDO idBookingType;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBooking", fetch = FetchType.LAZY)
  private List<BookingIncomeDO> bookingIncomeDOList;
  
  /**
   * Constructor default
   */
  public BookingDO()
  {
  }

  /**
   * Constructor by id
   * 
   * @param idBooking
   */
  public BookingDO( Long idBooking )
  {
    this.idBooking = idBooking;
  }

  /**
   * @return the idBooking
   */
  public Long getIdBooking()
  {
    return idBooking;
  }

  /**
   * @param idBooking the idBooking to set
   */
  public void setIdBooking( Long idBooking )
  {
    this.idBooking = idBooking;
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
   * @return the bookingWeekDOList
   */
  public List<BookingWeekDO> getBookingWeekDOList()
  {
    return bookingWeekDOList;
  }

  /**
   * @param bookingWeekDOList the bookingWeekDOList to set
   */
  public void setBookingWeekDOList( List<BookingWeekDO> bookingWeekDOList )
  {
    this.bookingWeekDOList = bookingWeekDOList;
  }

  /**
   * @return the bookingSpecialEventDOList
   */
  public List<BookingSpecialEventDO> getBookingSpecialEventDOList()
  {
    return bookingSpecialEventDOList;
  }

  /**
   * @param bookingSpecialEventDOList the bookingSpecialEventDOList to set
   */
  public void setBookingSpecialEventDOList( List<BookingSpecialEventDO> bookingSpecialEventDOList )
  {
    this.bookingSpecialEventDOList = bookingSpecialEventDOList;
  }

  /**
   * @return the fgBooked
   */
  public Boolean getFgBooked()
  {
    return fgBooked;
  }

  /**
   * @param fgBooked the fgBooked to set
   */
  public void setFgBooked( Boolean fgBooked )
  {
    this.fgBooked = fgBooked;
  }

  /**
   * @return the idBookingType
   */
  public BookingTypeDO getIdBookingType()
  {
    return idBookingType;
  }

  /**
   * @param idBookingType the idBookingType to set
   */
  public void setIdBookingType( BookingTypeDO idBookingType )
  {
    this.idBookingType = idBookingType;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idBooking != null ? idBooking.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof BookingDO) )
    {
      return false;
    }
    BookingDO other = (BookingDO) object;
    if( (this.idBooking == null && other.idBooking != null)
        || (this.idBooking != null && !this.idBooking.equals( other.idBooking )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "idBooking", this.idBooking )
        .append( "idEvent", this.idEvent.getIdEvent() ).append( "idTheater", this.idTheater.getIdTheater() ).toString();
  }

  @Override
  public int compareTo( BookingDO other )
  {
    return this.idBooking.compareTo( other.idBooking );
  }

  public List<BookingIncomeDO> getBookingIncomeDOList()
  {
    return bookingIncomeDOList;
  }
/**
 * 
 * @param bookingIncomeDOList the bookingIncomeDOList to set 
 */
  public void setBookingIncomeDOList( List<BookingIncomeDO> bookingIncomeDOList )
  {
    this.bookingIncomeDOList = bookingIncomeDOList;
  }

}
