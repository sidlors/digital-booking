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

/**
 * JPA entity for C_TERRITORY_LANGUAGE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_TERRITORY_LANGUAGE")
@NamedQueries({
    @NamedQuery(name = "TerritoryLanguageDO.findAll", query = "SELECT t FROM TerritoryLanguageDO t"),
    @NamedQuery(name = "TerritoryLanguageDO.findByIdTerritoryLanguage", query = "SELECT t FROM TerritoryLanguageDO t WHERE t.idTerritoryLanguage = :idTerritoryLanguage"),
    @NamedQuery(name = "TerritoryLanguageDO.findByDsName", query = "SELECT t FROM TerritoryLanguageDO t WHERE t.dsName = :dsName") })
public class TerritoryLanguageDO extends AbstractEntity<TerritoryLanguageDO>
{
  private static final long serialVersionUID = 6568643263547167439L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_TERRITORY_LANGUAGE", nullable = false)
  private Integer idTerritoryLanguage;

  @Column(name = "DS_NAME", length = 160)
  private String dsName;

  @JoinColumn(name = "ID_LANGUAGE", referencedColumnName = "ID_LANGUAGE", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private LanguageDO idLanguage;

  @JoinColumn(name = "ID_TERRITORY", referencedColumnName = "ID_TERRITORY", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TerritoryDO idTerritory;

  /**
   * Constructor default
   */
  public TerritoryLanguageDO()
  {
  }

  /**
   * Constructor by idTerritoryLanguage
   * 
   * @param idTerritoryLanguage
   */
  public TerritoryLanguageDO( Integer idTerritoryLanguage )
  {
    this.idTerritoryLanguage = idTerritoryLanguage;
  }

  public Integer getIdTerritoryLanguage()
  {
    return idTerritoryLanguage;
  }

  public void setIdTerritoryLanguage( Integer idTerritoryLanguage )
  {
    this.idTerritoryLanguage = idTerritoryLanguage;
  }

  public String getDsName()
  {
    return dsName;
  }

  public void setDsName( String dsName )
  {
    this.dsName = dsName;
  }

  public LanguageDO getIdLanguage()
  {
    return idLanguage;
  }

  public void setIdLanguage( LanguageDO idLanguage )
  {
    this.idLanguage = idLanguage;
  }

  public TerritoryDO getIdTerritory()
  {
    return idTerritory;
  }

  public void setIdTerritory( TerritoryDO idTerritory )
  {
    this.idTerritory = idTerritory;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idTerritoryLanguage != null ? idTerritoryLanguage.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof TerritoryLanguageDO) )
    {
      return false;
    }
    TerritoryLanguageDO other = (TerritoryLanguageDO) object;
    if( (this.idTerritoryLanguage == null && other.idTerritoryLanguage != null)
        || (this.idTerritoryLanguage != null && !this.idTerritoryLanguage.equals( other.idTerritoryLanguage )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.TerritoryLanguageDO[ idTerritoryLanguage=" + idTerritoryLanguage
        + " ]";
  }

  @Override
  public int compareTo( TerritoryLanguageDO other )
  {
    return this.idTerritoryLanguage.compareTo( other.idTerritoryLanguage );
  }

}
