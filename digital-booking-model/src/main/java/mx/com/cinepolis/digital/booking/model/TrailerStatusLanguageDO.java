/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
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
 * JPA entity for C_TRAILER_STATUS_LANGUAGE
 * 
 * @author jcarbajal
 */
@Entity
@Table(name = "C_TRAILER_STATUS_LANGUAGE", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID_TRAILER_STATUS",
"ID_LANGUAGE" }) })
@NamedQueries({
  @NamedQuery(name = "TrailerStatusLanguageDO.findAll", query = "SELECT tsl FROM TrailerStatusLanguageDO tsl"),
  @NamedQuery(name = "TrailerStatusLanguageDO.findById", query = "SELECT tsl FROM TrailerStatusLanguageDO tsl where tsl.idTrailerStatusLanguage = :idTrailerStatusLanguage")})
public class TrailerStatusLanguageDO extends AbstractEntity<TrailerStatusLanguageDO>
{

  /**
   * 
   */
  private static final long serialVersionUID = -1181740328005620966L;

  @Id
  @Column(name = "ID_TRAILER_STATUS_LANGUAGE", nullable = false)
  private  Integer idTrailerStatusLanguage; 
  
  @JoinColumn(name = "ID_TRAILER_STATUS", referencedColumnName = "ID_TRAILER_STATUS", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TrailerStatusDO idTrailerStatus;
  
  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;
  
  @Column(name = "DS_NAME", nullable = false)
  private String dsName;
  /**
   * Constructor by default
   */
  public TrailerStatusLanguageDO( )
  {
  }
  /**
   * Constructor by identifier
   * @param idTrailerStatusLanguage
   */
  public TrailerStatusLanguageDO( Integer idTrailerStatusLanguage )
  {
    this.idTrailerStatusLanguage = idTrailerStatusLanguage;
  }

  /**
   * @return the idTrailerStatusLanguage
   */
  public Integer getIdTrailerStatusLanguage()
  {
    return idTrailerStatusLanguage;
  }

  /**
   * @param idTrailerStatusLanguage the idTrailerStatusLanguage to set
   */
  public void setIdTrailerStatusLanguage( Integer idTrailerStatusLanguage )
  {
    this.idTrailerStatusLanguage = idTrailerStatusLanguage;
  }

  /**
   * @return the idTrailerStatus
   */
  public TrailerStatusDO getIdTrailerStatus()
  {
    return idTrailerStatus;
  }

  /**
   * @param idTrailerStatus the idTrailerStatus to set
   */
  public void setIdTrailerStatus( TrailerStatusDO idTrailerStatus )
  {
    this.idTrailerStatus = idTrailerStatus;
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

  @Override
  public int compareTo( TrailerStatusLanguageDO o )
  {
    return this.idTrailerStatusLanguage.compareTo( o.idTrailerStatusLanguage );
  }

  @Override
  public boolean equals( Object obj )
  {
    if( !(obj instanceof TrailerStatusLanguageDO) )
    {
      return false;
    }
    TrailerStatusLanguageDO other = (TrailerStatusLanguageDO) obj;
    if( (this.idTrailerStatusLanguage == null && other.idTrailerStatusLanguage != null)
        || (this.idTrailerStatusLanguage != null && !this.idTrailerStatusLanguage.equals( other.idTrailerStatusLanguage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idTrailerStatusLanguage != null ? idTrailerStatusLanguage.hashCode() : 0);
    return hash;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return "TrailerStatusLanguageDO [idTrailerStatusLanguage=" + idTrailerStatusLanguage + ", idTrailerStatus="
        + idTrailerStatus + ", idLanguage=" + idLanguage + ", dsName=" + dsName + "]";
  }
  

}
