/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
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
 * JPA entity for C_CITY_LANGUAGE
 * 
 * @author kperez
 */
@Entity
@Table(name = "C_CITY_LANGUAGE", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID_CITY", "ID_LANGUAGE" }) })
@NamedQueries({
    @NamedQuery(name = "CityLanguageDO.findAll", query = "SELECT c FROM CityLanguageDO c"),
    @NamedQuery(name = "CityLanguageDO.findByIdCityInternationalization", query = "SELECT c FROM CityLanguageDO c WHERE c.idCityInternationalization = :idCityInternationalization"),
    @NamedQuery(name = "CityLanguageDO.findByDsName", query = "SELECT c FROM CityLanguageDO c WHERE c.dsName = :dsName") })
public class CityLanguageDO extends AbstractEntity<CityLanguageDO>
{

  private static final long serialVersionUID = 4057049923339131146L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_CITY_INTERNATIONALIZATION", nullable = false)
  private Integer idCityInternationalization;

  @Column(name = "DS_NAME", nullable = false, length = 160)
  private String dsName;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @JoinColumn(name = "ID_CITY", referencedColumnName = "ID_CITY", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private CityDO idCity;

  /**
   * Constructor default
   */
  public CityLanguageDO()
  {
  }

  /**
   * Constructor by idCityInternationalization
   * @param idCityInternationalization
   */
  public CityLanguageDO( Integer idCityInternationalization )
  {
    this.idCityInternationalization = idCityInternationalization;
  }

  /**
   * @return the idCityInternationalization
   */
  public Integer getIdCityInternationalization()
  {
    return idCityInternationalization;
  }

  /**
   * @param idCityInternationalization the idCityInternationalization to set
   */
  public void setIdCityInternationalization( Integer idCityInternationalization )
  {
    this.idCityInternationalization = idCityInternationalization;
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
   * @return the idCity
   */
  public CityDO getIdCity()
  {
    return idCity;
  }

  /**
   * @param idCity the idCity to set
   */
  public void setIdCity( CityDO idCity )
  {
    this.idCity = idCity;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idCityInternationalization != null ? idCityInternationalization.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof CityLanguageDO) )
    {
      return false;
    }
    CityLanguageDO other = (CityLanguageDO) object;
    if( (this.idCityInternationalization == null && other.idCityInternationalization != null)
        || (this.idCityInternationalization != null && !this.idCityInternationalization
            .equals( other.idCityInternationalization )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.CityLanguageDO[ idCityInternationalization="
        + idCityInternationalization + " ]";
  }

  @Override
  public int compareTo( CityLanguageDO o )
  {
    return this.idCityInternationalization.compareTo( o.idCityInternationalization );
  }
}
