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
 * JPA for entity K_BOOKING_WEEK_SCREEN
 * 
 * @author gsegura
 * @since 0.2.7
 */
@Entity
@Table(name = "K_BOOKING_WEEK_SCREEN")
@NamedQueries({
    @NamedQuery(name = "BookingWeekScreenDO.findAll", query = "SELECT b FROM BookingWeekScreenDO b"),
    @NamedQuery(name = "BookingWeekScreenDO.findByIdBookingWeekScreen", query = "SELECT b FROM BookingWeekScreenDO b WHERE b.idBookingWeekScreen = :idBookingWeekScreen") })
public class BookingWeekScreenDO extends AbstractEntity<BookingWeekScreenDO>
{

  private static final long serialVersionUID = -8244169101108664281L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_BOOKING_WEEK_SCREEN")
  private Long idBookingWeekScreen;

  @JoinColumn(name = "ID_OBSERVATION", referencedColumnName = "ID_OBSERVATION")
  @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
  private ObservationDO idObservation;

  @JoinColumn(name = "ID_BOOKING_WEEK", referencedColumnName = "ID_BOOKING_WEEK")
  @ManyToOne(optional = false, fetch = FetchType.LAZY )
  private BookingWeekDO idBookingWeek;

  @JoinColumn(name = "ID_SCREEN", referencedColumnName = "ID_SCREEN")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private ScreenDO idScreen;

  @JoinColumn(name = "ID_BOOKING_STATUS", referencedColumnName = "ID_BOOKING_STATUS")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private BookingStatusDO idBookingStatus;

  @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "idBookingWeekScreen", fetch = FetchType.LAZY)
  private List<BookingWeekScreenShowDO> bookingWeekScreenShowDOList;
  
  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idBookingWeekScreen", fetch = FetchType.LAZY)
  private List<PresaleDO> presaleDOList;

  /**
   * Constructor default
   */
  public BookingWeekScreenDO()
  {
  }

  /**
   * Constructor by id
   * 
   * @param idBookingWeekScreen
   */
  public BookingWeekScreenDO( Long idBookingWeekScreen )
  {
    this.idBookingWeekScreen = idBookingWeekScreen;
  }

  /**
   * @return the idBookingWeekScreen
   */
  public Long getIdBookingWeekScreen()
  {
    return idBookingWeekScreen;
  }

  /**
   * @param idBookingWeekScreen the idBookingWeekScreen to set
   */
  public void setIdBookingWeekScreen( Long idBookingWeekScreen )
  {
    this.idBookingWeekScreen = idBookingWeekScreen;
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
   * @return the idBookingWeek
   */
  public BookingWeekDO getIdBookingWeek()
  {
    return idBookingWeek;
  }

  /**
   * @param idBookingWeek the idBookingWeek to set
   */
  public void setIdBookingWeek( BookingWeekDO idBookingWeek )
  {
    this.idBookingWeek = idBookingWeek;
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
   * @return the bookingWeekScreenShowDOList
   */
  public List<BookingWeekScreenShowDO> getBookingWeekScreenShowDOList()
  {
    return bookingWeekScreenShowDOList;
  }

  /**
   * @param bookingWeekScreenShowDOList the bookingWeekScreenShowDOList to set
   */
  public void setBookingWeekScreenShowDOList( List<BookingWeekScreenShowDO> bookingWeekScreenShowDOList )
  {
    this.bookingWeekScreenShowDOList = bookingWeekScreenShowDOList;
  }

  @Override
  public int compareTo( BookingWeekScreenDO that )
  {
    return this.idBookingWeekScreen.compareTo( that.idBookingWeekScreen );
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof BookingWeekScreenDO) )
    {
      return false;
    }
    BookingWeekScreenDO other = (BookingWeekScreenDO) object;
    if( (this.idBookingWeekScreen == null && other.idBookingWeekScreen != null)
        || (this.idBookingWeekScreen != null && !this.idBookingWeekScreen.equals( other.idBookingWeekScreen )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idBookingWeekScreen != null ? idBookingWeekScreen.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return new ToStringBuilder( this ).append( "idBookingWeekScreen", this.idBookingWeekScreen )
        .append( "idScreen", this.idScreen.getIdScreen() )
        .append( "nuScreen", this.idScreen.getNuScreen() )
        .append( "idBookingWeek", this.idBookingWeek.getIdBookingWeek() ).toString();
  }
  /**
   * 
   * @return presaleDOList
   */
  public List<PresaleDO> getPresaleDOList()
  {
    return presaleDOList;
  }

  /**
   * 
   * @param presaleDOList the presaleDOList to set 
   */
  public void setPresaleDOList( List<PresaleDO> presaleDOList )
  {
    this.presaleDOList = presaleDOList;
  }

}
