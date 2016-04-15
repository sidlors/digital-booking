package mx.com.cinepolis.digital.booking.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for C_BOOKING_TYPE
 * 
 * @author shernandezl
 * @since 0.0.1
 */
@Entity
@Table(name = "C_BOOKING_TYPE")
public class BookingTypeDO extends AbstractEntity<BookingTypeDO>
{

  private static final long serialVersionUID = -3890596526596645377L;

  @Id
  @Column(name = "ID_BOOKING_TYPE", nullable = false)
  private Integer idBookingType;

  @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "idBookingType", fetch = FetchType.LAZY)
  private List<BookingDO> bookingDOList;

  @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, mappedBy = "idBookingType", fetch = FetchType.LAZY)
  private List<BookingTypeLanguageDO> bookingTypeLanguageDOList;

  /**
   * Constructor default
   */
  public BookingTypeDO()
  {
  }

  /**
   * @return the idBookingType
   */
  public Integer getIdBookingType()
  {
    return idBookingType;
  }

  /**
   * @param idBookingType the idBookingType to set
   */
  public void setIdBookingType( Integer idBookingType )
  {
    this.idBookingType = idBookingType;
  }

  /**
   * @return the bookingDOList
   */
  public List<BookingDO> getBookingDOList()
  {
    return bookingDOList;
  }

  /**
   * @param bookingDOList the bookingDOList to set
   */
  public void setBookingDOList( List<BookingDO> bookingDOList )
  {
    this.bookingDOList = bookingDOList;
  }

  /**
   * @return the bookingTypeLanguageDOList
   */
  public List<BookingTypeLanguageDO> getBookingTypeLanguageDOList()
  {
    return bookingTypeLanguageDOList;
  }

  /**
   * @param bookingTypeLanguageDOList the bookingTypeLanguageDOList to set
   */
  public void setBookingTypeLanguageDOList( List<BookingTypeLanguageDO> bookingTypeLanguageDOList )
  {
    this.bookingTypeLanguageDOList = bookingTypeLanguageDOList;
  }

  @Override
  public int compareTo( BookingTypeDO other )
  {
    return this.idBookingType.compareTo( other.idBookingType );
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof BookingTypeDO) )
    {
      return false;
    }
    BookingTypeDO other = (BookingTypeDO) object;
    if( (this.idBookingType == null && other.idBookingType != null)
        || (this.idBookingType != null && !this.idBookingType.equals( other.idBookingType )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idBookingType != null ? idBookingType.hashCode() : 0);
    return hash;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.BookingTypeDO[ idBookingType=" + idBookingType + " ]";
  }
}
