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
 * JPA entity for C_REGION_LANGUAGE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_REGION_LANGUAGE", uniqueConstraints = { @UniqueConstraint(columnNames = { "ID_LANGUAGE", "ID_REGION" }) })
@NamedQueries({
    @NamedQuery(name = "RegionLanguageDO.findAll", query = "SELECT r FROM RegionLanguageDO r"),
    @NamedQuery(name = "RegionLanguageDO.findByIdRegionLanguage", query = "SELECT r FROM RegionLanguageDO r WHERE r.idRegionLanguage = :idRegionLanguage"),
    @NamedQuery(name = "RegionLanguageDO.findByDsName", query = "SELECT r FROM RegionLanguageDO r WHERE r.dsName = :dsName") })
public class RegionLanguageDO extends AbstractEntity<RegionLanguageDO>
{
  private static final long serialVersionUID = 5612624047347297820L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_REGION_LANGUAGE", nullable = false)
  private Integer idRegionLanguage;

  @Column(name = "DS_NAME", length = 160)
  private String dsName;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @JoinColumn(name = "ID_REGION", referencedColumnName = "ID_REGION", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private RegionDO idRegion;

  /**
   * Constructor default
   */
  public RegionLanguageDO()
  {
  }

  /**
   * Constructor by idRegionLanguage
   * 
   * @param idRegionLanguage
   */
  public RegionLanguageDO( Integer idRegionLanguage )
  {
    this.idRegionLanguage = idRegionLanguage;
  }

  /**
   * @return the idRegionLanguage
   */
  public Integer getIdRegionLanguage()
  {
    return idRegionLanguage;
  }

  /**
   * @param idRegionLanguage the idRegionLanguage to set
   */
  public void setIdRegionLanguage( Integer idRegionLanguage )
  {
    this.idRegionLanguage = idRegionLanguage;
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
   * @return the idRegion
   */
  public RegionDO getIdRegion()
  {
    return idRegion;
  }

  /**
   * @param idRegion the idRegion to set
   */
  public void setIdRegion( RegionDO idRegion )
  {
    this.idRegion = idRegion;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idRegionLanguage != null ? idRegionLanguage.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof RegionLanguageDO) )
    {
      return false;
    }
    RegionLanguageDO other = (RegionLanguageDO) object;
    if( (this.idRegionLanguage == null && other.idRegionLanguage != null)
        || (this.idRegionLanguage != null && !this.idRegionLanguage.equals( other.idRegionLanguage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.RegionLanguageDO[ idRegionLanguage=" + idRegionLanguage + " ]";
  }

  @Override
  public int compareTo( RegionLanguageDO other )
  {
    return this.idRegionLanguage.compareTo( other.idRegionLanguage );
  }

}
