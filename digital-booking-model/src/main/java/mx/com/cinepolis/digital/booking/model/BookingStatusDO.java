package mx.com.cinepolis.digital.booking.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for C_BOOKING_STATUS
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_BOOKING_STATUS")
@NamedQueries({
    @NamedQuery(name = "BookingStatusDO.findAll", query = "SELECT b FROM BookingStatusDO b"),
    @NamedQuery(name = "BookingStatusDO.findByIdBookingStatus", query = "SELECT b FROM BookingStatusDO b WHERE b.idBookingStatus = :idBookingStatus") })
public class BookingStatusDO extends AbstractEntity<BookingStatusDO>
{

  private static final long serialVersionUID = 2420535024179791231L;

  @Id
  @Column(name = "ID_BOOKING_STATUS", nullable = false)
  private Integer idBookingStatus;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "idBookingStatus", fetch = FetchType.LAZY)
  private List<BookingWeekScreenDO> bookingWeekScreenDOList;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "idBookingStatus", fetch = FetchType.LAZY)
  private List<BookingStatusLanguageDO> bookingStatusLanguageDOList;

  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "idBookingStatus", fetch = FetchType.LAZY)
  private List<BookingWeekDO> bookingWeekDOList;
  
  @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "idBookingStatus", fetch = FetchType.LAZY)
  private List<BookingSpecialEventScreenDO> BookingSpecialEventScreenDOList;

  /**
   * Constructor default
   */
  public BookingStatusDO()
  {
  }

  /**
   * Constructor by idBookingStatus
   * 
   * @param idBookingStatus
   */
  public BookingStatusDO( Integer idBookingStatus )
  {
    this.idBookingStatus = idBookingStatus;
  }

  /**
   * @return the idBookingStatus
   */
  public Integer getIdBookingStatus()
  {
    return idBookingStatus;
  }

  /**
   * @param idBookingStatus the idBookingStatus to set
   */
  public void setIdBookingStatus( Integer idBookingStatus )
  {
    this.idBookingStatus = idBookingStatus;
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
   * @return the bookingStatusLanguageDOList
   */
  public List<BookingStatusLanguageDO> getBookingStatusLanguageDOList()
  {
    return bookingStatusLanguageDOList;
  }

  /**
   * @param bookingStatusLanguageDOList the bookingStatusLanguageDOList to set
   */
  public void setBookingStatusLanguageDOList( List<BookingStatusLanguageDO> bookingStatusLanguageDOList )
  {
    this.bookingStatusLanguageDOList = bookingStatusLanguageDOList;
  }

  /**
   * @return the bookingSpecialEventScreenDOList
   */
  public List<BookingSpecialEventScreenDO> getBookingSpecialEventScreenDOList()
  {
    return BookingSpecialEventScreenDOList;
  }

  /**
   * @param bookingSpecialEventScreenDOList the bookingSpecialEventScreenDOList to set
   */
  public void setBookingSpecialEventScreenDOList( List<BookingSpecialEventScreenDO> bookingSpecialEventScreenDOList )
  {
    BookingSpecialEventScreenDOList = bookingSpecialEventScreenDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idBookingStatus != null ? idBookingStatus.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof BookingStatusDO) )
    {
      return false;
    }
    BookingStatusDO other = (BookingStatusDO) object;
    if( (this.idBookingStatus == null && other.idBookingStatus != null)
        || (this.idBookingStatus != null && !this.idBookingStatus.equals( other.idBookingStatus )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.BookingStatusDO[ idBookingStatus=" + idBookingStatus + " ]";
  }

  @Override
  public int compareTo( BookingStatusDO other )
  {
    return this.idBookingStatus.compareTo( other.idBookingStatus );
  }

}
