package mx.com.cinepolis.digital.booking.model;

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
import javax.persistence.UniqueConstraint;

/**
 * JPA entity for C_THEATER_LANGUAGE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_THEATER_LANGUAGE", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID_THEATER", "ID_LANGUAGE" }) })
@NamedQueries({
    @NamedQuery(name = "TheaterLanguageDO.findAll", query = "SELECT t FROM TheaterLanguageDO t"),
    @NamedQuery(name = "TheaterLanguageDO.findByIdTheaterLanguage", query = "SELECT t FROM TheaterLanguageDO t WHERE t.idTheaterLanguage = :idTheaterLanguage"),
    @NamedQuery(name = "TheaterLanguageDO.findByDsName", query = "SELECT t FROM TheaterLanguageDO t WHERE t.dsName = :dsName") })
public class TheaterLanguageDO extends AbstractEntity<TheaterLanguageDO>
{
  private static final long serialVersionUID = 1155864038987153902L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_THEATER_LANGUAGE", nullable = false)
  private Integer idTheaterLanguage;

  @Column(name = "DS_NAME", nullable = false, length = 160)
  private String dsName;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @JoinColumn(name = "ID_THEATER", referencedColumnName = "ID_THEATER", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TheaterDO idTheater;

  /**
   * Constructor default
   */
  public TheaterLanguageDO()
  {
  }

  /**
   * Constructor by idTheaterLanguage
   * 
   * @param idTheaterLanguage
   */
  public TheaterLanguageDO( Integer idTheaterLanguage )
  {
    this.idTheaterLanguage = idTheaterLanguage;
  }

  /**
   * @return the idTheaterLanguage
   */
  public Integer getIdTheaterLanguage()
  {
    return idTheaterLanguage;
  }

  /**
   * @param idTheaterLanguage the idTheaterLanguage to set
   */
  public void setIdTheaterLanguage( Integer idTheaterLanguage )
  {
    this.idTheaterLanguage = idTheaterLanguage;
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

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idTheaterLanguage != null ? idTheaterLanguage.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof TheaterLanguageDO) )
    {
      return false;
    }
    TheaterLanguageDO other = (TheaterLanguageDO) object;
    if( (this.idTheaterLanguage == null && other.idTheaterLanguage != null)
        || (this.idTheaterLanguage != null && !this.idTheaterLanguage.equals( other.idTheaterLanguage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.TheaterLanguageDO[ idTheaterLanguage=" + idTheaterLanguage + " ]";
  }

  @Override
  public int compareTo( TheaterLanguageDO other )
  {
    return this.idTheaterLanguage.compareTo( other.idTheaterLanguage );
  }

}
