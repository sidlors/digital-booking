package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * JPA entity for C_BOOKING_STATUS_LANGUAGE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_BOOKING_STATUS_LANGUAGE", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID_BOOKING_STATUS",
    "ID_LANGUAGE" }) })
@NamedQueries({
    @NamedQuery(name = "BookingStatusLanguageDO.findAll", query = "SELECT b FROM BookingStatusLanguageDO b"),
    @NamedQuery(name = "BookingStatusLanguageDO.findByIdBookingStatusLanguage", query = "SELECT b FROM BookingStatusLanguageDO b WHERE b.idBookingStatusLanguage = :idBookingStatusLanguage"),
    @NamedQuery(name = "BookingStatusLanguageDO.findByDsName", query = "SELECT b FROM BookingStatusLanguageDO b WHERE b.dsName = :dsName") })
public class BookingStatusLanguageDO extends AbstractEntity<BookingStatusLanguageDO>
{
  private static final long serialVersionUID = -3075770045290751592L;

  @Id
  @Column(name = "ID_BOOKING_STATUS_LANGUAGE", nullable = false)
  private Integer idBookingStatusLanguage;

  @Column(name = "DS_NAME", length = 160)
  private String dsName;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @JoinColumn(name = "ID_BOOKING_STATUS", referencedColumnName = "ID_BOOKING_STATUS", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private BookingStatusDO idBookingStatus;

  /**
   * Constructor default
   */
  public BookingStatusLanguageDO()
  {
  }

  /**
   * Constructor by idBookingStatusLanguage
   * 
   * @param idBookingStatusLanguage
   */
  public BookingStatusLanguageDO( Integer idBookingStatusLanguage )
  {
    this.idBookingStatusLanguage = idBookingStatusLanguage;
  }

  /**
   * @return the idBookingStatusLanguage
   */
  public Integer getIdBookingStatusLanguage()
  {
    return idBookingStatusLanguage;
  }

  /**
   * @param idBookingStatusLanguage the idBookingStatusLanguage to set
   */
  public void setIdBookingStatusLanguage( Integer idBookingStatusLanguage )
  {
    this.idBookingStatusLanguage = idBookingStatusLanguage;
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
  public int hashCode()
  {
    int hash = 0;
    hash += (idBookingStatusLanguage != null ? idBookingStatusLanguage.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof BookingStatusLanguageDO) )
    {
      return false;
    }
    BookingStatusLanguageDO other = (BookingStatusLanguageDO) object;
    if( (this.idBookingStatusLanguage == null && other.idBookingStatusLanguage != null)
        || (this.idBookingStatusLanguage != null && !this.idBookingStatusLanguage
            .equals( other.idBookingStatusLanguage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.BookingStatusLanguageDO[ idBookingStatusLanguage="
        + idBookingStatusLanguage + " ]";
  }

  @Override
  public int compareTo( BookingStatusLanguageDO other )
  {
    return this.idBookingStatusLanguage.compareTo( other.idBookingStatusLanguage );
  }

}
