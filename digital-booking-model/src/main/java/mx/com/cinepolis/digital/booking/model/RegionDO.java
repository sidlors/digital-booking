package mx.com.cinepolis.digital.booking.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for C_REGION
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_REGION")
@NamedQueries({
    @NamedQuery(name = "RegionDO.findAll", query = "SELECT r FROM RegionDO r"),
    @NamedQuery(name = "RegionDO.findByIdRegion", query = "SELECT r FROM RegionDO r WHERE r.idRegion = :idRegion"),
    @NamedQuery(name = "RegionDO.findByDsNameActive", query = "SELECT r FROM RegionDO r INNER JOIN r.regionLanguageDOList as rl INNER JOIN rl.idLanguage l WHERE rl.dsName = :dsName and l.idLanguage = :idLanguage and r.fgActive = true"),
    @NamedQuery(name = "RegionDO.findByFgActive", query = "SELECT r FROM RegionDO r WHERE r.fgActive = :fgActive"),
    @NamedQuery(name = "RegionDO.findByDtLastModification", query = "SELECT r FROM RegionDO r WHERE r.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "RegionDO.findByIdLastUserModifier", query = "SELECT r FROM RegionDO r WHERE r.idLastUserModifier = :idLastUserModifier") })
public class RegionDO extends AbstractSignedEntity<RegionDO>
{
  private static final long serialVersionUID = 9103217433270584131L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_REGION", nullable = false)
  private Integer idRegion;

  @JoinTable(name = "K_USER_X_REGION", joinColumns = { @JoinColumn(name = "ID_REGION", referencedColumnName = "ID_REGION", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER", nullable = false) })
  @ManyToMany(fetch = FetchType.LAZY)
  private List<UserDO> userDOList;

  @JoinColumn(name = "ID_TERRITORY", referencedColumnName = "ID_TERRITORY", nullable = false)
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private TerritoryDO idTerritory;

  @OneToMany(mappedBy = "idRegion", fetch = FetchType.LAZY)
  private List<TheaterDO> theaterDOList;

  @JoinTable(name = "K_NEWS_FEED_X_REGION", joinColumns = { @JoinColumn(name = "ID_REGION", referencedColumnName = "ID_REGION") }, inverseJoinColumns = { @JoinColumn(name = "ID_NEWS_FEED", referencedColumnName = "ID_NEWS_FEED") })
  @ManyToMany(fetch = FetchType.LAZY)
  private List<NewsFeedDO> newsFeedDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idRegion", fetch = FetchType.LAZY)
  private List<RegionLanguageDO> regionLanguageDOList;

  @JoinTable(name = "K_PERSON_X_REGION", joinColumns = { @JoinColumn(name = "ID_REGION", referencedColumnName = "ID_REGION") }, inverseJoinColumns = { @JoinColumn(name = "ID_PERSON", referencedColumnName = "ID_PERSON") })
  @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.ALL })
  private List<PersonDO> personDOList;

  /**
   * Constructor default
   */
  public RegionDO()
  {
  }

  /**
   * Constructor by idRegion
   * 
   * @param idRegion
   */
  public RegionDO( Integer idRegion )
  {
    this.idRegion = idRegion;
  }

  /**
   * @return the idRegion
   */
  public Integer getIdRegion()
  {
    return idRegion;
  }

  /**
   * @param idRegion the idRegion to set
   */
  public void setIdRegion( Integer idRegion )
  {
    this.idRegion = idRegion;
  }

  /**
   * @return the userDOList
   */
  public List<UserDO> getUserDOList()
  {
    return userDOList;
  }

  /**
   * @param userDOList the userDOList to set
   */
  public void setUserDOList( List<UserDO> userDOList )
  {
    this.userDOList = userDOList;
  }

  /**
   * @return the idTerritory
   */
  public TerritoryDO getIdTerritory()
  {
    return idTerritory;
  }

  /**
   * @param idTerritory the idTerritory to set
   */
  public void setIdTerritory( TerritoryDO idTerritory )
  {
    this.idTerritory = idTerritory;
  }

  /**
   * @return the theaterDOList
   */
  public List<TheaterDO> getTheaterDOList()
  {
    return theaterDOList;
  }

  /**
   * @param theaterDOList the theaterDOList to set
   */
  public void setTheaterDOList( List<TheaterDO> theaterDOList )
  {
    this.theaterDOList = theaterDOList;
  }

  /**
   * @return the newsFeedDOList
   */
  public List<NewsFeedDO> getNewsFeedDOList()
  {
    return newsFeedDOList;
  }

  /**
   * @param newsFeedDOList the newsFeedDOList to set
   */
  public void setNewsFeedDOList( List<NewsFeedDO> newsFeedDOList )
  {
    this.newsFeedDOList = newsFeedDOList;
  }

  /**
   * @return the regionLanguageDOList
   */
  public List<RegionLanguageDO> getRegionLanguageDOList()
  {
    return regionLanguageDOList;
  }

  /**
   * @param regionLanguageDOList the regionLanguageDOList to set
   */
  public void setRegionLanguageDOList( List<RegionLanguageDO> regionLanguageDOList )
  {
    this.regionLanguageDOList = regionLanguageDOList;
  }

  /**
   * @return the personDOList
   */
  public List<PersonDO> getPersonDOList()
  {
    return personDOList;
  }

  /**
   * @param personDOList the personDOList to set
   */
  public void setPersonDOList( List<PersonDO> personDOList )
  {
    this.personDOList = personDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idRegion != null ? idRegion.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof RegionDO) )
    {
      return false;
    }
    RegionDO other = (RegionDO) object;
    if( (this.idRegion == null && other.idRegion != null)
        || (this.idRegion != null && !this.idRegion.equals( other.idRegion )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.RegionDO[ idRegion=" + idRegion + " ]";
  }

  @Override
  public int compareTo( RegionDO other )
  {
    return this.idRegion.compareTo( other.idRegion );
  }

}
