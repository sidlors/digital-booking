package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * JPA entity for C_BOOKING_TYPE_LANGUAGE
 * @author shernandezl
 * @since 0.0.1
 */
@Entity
@Table(name = "C_BOOKING_TYPE_LANGUAGE")
public class BookingTypeLanguageDO extends AbstractEntity<BookingTypeLanguageDO>
{
  
  private static final long serialVersionUID = -7690120521793501242L;

  @Id
  @Column(name = "ID_BOOKING_TYPE_LANGUAGE", nullable = false)
  private Integer idBookingTypeLanguage;

  @Column(name = "DS_NAME", nullable = false, length = 160)
  private String dsName;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @JoinColumn(name = "ID_BOOKING_TYPE", referencedColumnName = "ID_BOOKING_TYPE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private BookingTypeDO idBookingType;
  
  /**
   * Constructor por default
   */
  public BookingTypeLanguageDO()
  {
  }

  /**
   * @return the idBookingTypeLanguage
   */
  public Integer getIdBookingTypeLanguage()
  {
    return idBookingTypeLanguage;
  }

  /**
   * @param idBookingTypeLanguage the idBookingTypeLanguage to set
   */
  public void setIdBookingTypeLanguage( Integer idBookingTypeLanguage )
  {
    this.idBookingTypeLanguage = idBookingTypeLanguage;
  }

  /**
   * @return the dsName
   */
  public String getDsName()
  {
    return dsName;
  }

  /**
   * @param dsName the dsName to set
   */
  public void setDsName( String dsName )
  {
    this.dsName = dsName;
  }

  /**
   * @return the idLanguage
   */
  public LanguageDO getIdLanguage()
  {
    return idLanguage;
  }

  /**
   * @param idLanguage the idLanguage to set
   */
  public void setIdLanguage( LanguageDO idLanguage )
  {
    this.idLanguage = idLanguage;
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
  public int compareTo( BookingTypeLanguageDO other )
  {
    return this.idBookingTypeLanguage.compareTo( other.idBookingTypeLanguage );
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof BookingTypeLanguageDO) )
    {
      return false;
    }
    BookingTypeLanguageDO other = (BookingTypeLanguageDO) object;
    if( (this.idBookingTypeLanguage == null && other.idBookingTypeLanguage != null)
        || (this.idBookingTypeLanguage != null && !this.idBookingTypeLanguage.equals( other.idBookingTypeLanguage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idBookingTypeLanguage != null ? idBookingTypeLanguage.hashCode() : 0);
    return hash;
  }
  
  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.BookingTypeLanguageDO[ idBookingTypeLanguage=" + idBookingTypeLanguage
        + " ]";
  }

}
