package mx.com.cinepolis.digital.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * JPA entity for C_SYSTEM_MENU_LANGUAGE
 * 
 * @author afuentes
 */
@Entity
@Table(name = "C_SYSTEM_MENU_LANGUAGE", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID_SYSTEM_MENU",
    "ID_LANGUAGE" }) })
public class SystemMenuLanguageDO extends AbstractEntity<SystemMenuLanguageDO>
{
  private static final long serialVersionUID = 7379116943044855495L;

  @Id
  @Column(name = "ID_SYSTEM_MENU_LANGUAGE", nullable = false)
  private Integer idSystemMenuLanguage;

  @Column(name = "DS_NAME", length = 160)
  private String dsName;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @JoinColumn(name = "ID_SYSTEM_MENU", referencedColumnName = "ID_SYSTEM_MENU", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private SystemMenuDO idSystemMenu;

  /**
   * Default constructor
   */
  public SystemMenuLanguageDO()
  {
  }

  /**
   * @return the idSystemMenuLanguage
   */
  public Integer getIdSystemMenuLanguage()
  {
    return idSystemMenuLanguage;
  }

  /**
   * @param idSystemMenuLanguage the idSystemMenuLanguage to set
   */
  public void setIdSystemMenuLanguage( Integer idSystemMenuLanguage )
  {
    this.idSystemMenuLanguage = idSystemMenuLanguage;
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
   * @return the idSystemMenu
   */
  public SystemMenuDO getIdSystemMenu()
  {
    return idSystemMenu;
  }

  /**
   * @param idSystemMenu the idSystemMenu to set
   */
  public void setIdSystemMenu( SystemMenuDO idSystemMenu )
  {
    this.idSystemMenu = idSystemMenu;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idSystemMenuLanguage != null ? idSystemMenuLanguage.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof SystemMenuLanguageDO) )
    {
      return false;
    }
    SystemMenuLanguageDO other = (SystemMenuLanguageDO) object;
    if( (this.idSystemMenuLanguage == null && other.idSystemMenuLanguage != null)
        || (this.idSystemMenuLanguage != null && !this.idSystemMenuLanguage.equals( other.idSystemMenuLanguage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.SystemMenuLanguageDO[ idSystemMenuLanguage=" + idSystemMenuLanguage
        + " ]";
  }

  @Override
  public int compareTo( SystemMenuLanguageDO other )
  {
    return this.idSystemMenuLanguage.compareTo( other.idSystemMenuLanguage );
  }

}
