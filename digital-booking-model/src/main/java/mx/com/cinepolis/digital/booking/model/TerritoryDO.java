package mx.com.cinepolis.digital.booking.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for C_TERRITORY
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_TERRITORY")
@NamedQueries({
    @NamedQuery(name = "TerritoryDO.findAll", query = "SELECT t FROM TerritoryDO t"),
    @NamedQuery(name = "TerritoryDO.findByIdTerritory", query = "SELECT t FROM TerritoryDO t WHERE t.idTerritory = :idTerritory"),
    @NamedQuery(name = "TerritoryDO.findByFgActive", query = "SELECT t FROM TerritoryDO t WHERE t.fgActive = :fgActive"),
    @NamedQuery(name = "TerritoryDO.findByDtLastModification", query = "SELECT t FROM TerritoryDO t WHERE t.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "TerritoryDO.findByIdLastUserModifier", query = "SELECT t FROM TerritoryDO t WHERE t.idLastUserModifier = :idLastUserModifier") })
public class TerritoryDO extends AbstractSignedEntity<TerritoryDO>
{
  private static final long serialVersionUID = 5274456330947948367L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_TERRITORY", nullable = false)
  private Integer idTerritory;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTerritory", fetch = FetchType.LAZY)
  private List<RegionDO> regionDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTerritory", fetch = FetchType.LAZY)
  private List<TerritoryLanguageDO> territoryLanguageDOList;

  /**
   * Constructor default
   */
  public TerritoryDO()
  {
  }

  /**
   * Constructor by idTerritory
   * 
   * @param idTerritory
   */
  public TerritoryDO( Integer idTerritory )
  {
    this.idTerritory = idTerritory;
  }

  /**
   * @return the idTerritory
   */
  public Integer getIdTerritory()
  {
    return idTerritory;
  }

  /**
   * @param idTerritory the idTerritory to set
   */
  public void setIdTerritory( Integer idTerritory )
  {
    this.idTerritory = idTerritory;
  }

  /**
   * @return the regionDOList
   */
  public List<RegionDO> getRegionDOList()
  {
    return regionDOList;
  }

  /**
   * @param regionDOList the regionDOList to set
   */
  public void setRegionDOList( List<RegionDO> regionDOList )
  {
    this.regionDOList = regionDOList;
  }

  /**
   * @return the territoryLanguageDOList
   */
  public List<TerritoryLanguageDO> getTerritoryLanguageDOList()
  {
    return territoryLanguageDOList;
  }

  /**
   * @param territoryLanguageDOList the territoryLanguageDOList to set
   */
  public void setTerritoryLanguageDOList( List<TerritoryLanguageDO> territoryLanguageDOList )
  {
    this.territoryLanguageDOList = territoryLanguageDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idTerritory != null ? idTerritory.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof TerritoryDO) )
    {
      return false;
    }
    TerritoryDO other = (TerritoryDO) object;
    if( (this.idTerritory == null && other.idTerritory != null)
        || (this.idTerritory != null && !this.idTerritory.equals( other.idTerritory )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.TerritoryDO[ idTerritory=" + idTerritory + " ]";
  }

  @Override
  public int compareTo( TerritoryDO other )
  {
    return this.idTerritory.compareTo( other.idTerritory );
  }

}
