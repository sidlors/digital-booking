/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for C_CITY
 * 
 * @author kperez
 */
@Entity
@Table(name = "C_CITY")
@NamedQueries({
    @NamedQuery(name = "CityDO.findAll", query = "SELECT c FROM CityDO c"),
    @NamedQuery(name = "CityDO.findByIdCity", query = "SELECT c FROM CityDO c WHERE c.idCity = :idCity"),
    @NamedQuery(name = "CityDO.findByIdVista", query = "SELECT c FROM CityDO c WHERE c.idVista = :idVista"),
    @NamedQuery(name = "CityDO.findByIdVistaAndActive", query = "SELECT c FROM CityDO c WHERE c.idVista = :idVista and c.fgActive = true"),
    @NamedQuery(name = "CityDO.findByFgActive", query = "SELECT c FROM CityDO c WHERE c.fgActive = :fgActive"),
    @NamedQuery(name = "CityDO.findByDtLastModification", query = "SELECT c FROM CityDO c WHERE c.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "CityDO.findByIdLastUserModifier", query = "SELECT c FROM CityDO c WHERE c.idLastUserModifier = :idLastUserModifier"),
    @NamedQuery(name = "CityDO.findByIdCountry", query = "SELECT c FROM CityDO c WHERE c.idCountry.idCountry = :idCountry"),
    @NamedQuery(name = "CityDO.findByIdStateAndActive", query = "SELECT c FROM CityDO c WHERE c.idState.idState = :idState and c.fgActive = true")})
public class CityDO extends AbstractSignedEntity<CityDO>
{

  private static final long serialVersionUID = 6540871870483222194L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_CITY")
  private Integer idCity;

  @Column(name = "ID_VISTA")
  private String idVista;

  @OneToMany(mappedBy = "idCity", fetch = FetchType.LAZY)
  private List<TheaterDO> theaterDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCity", fetch = FetchType.LAZY)
  private List<CityLanguageDO> cityLanguageDOList;

  @JoinColumn(name = "ID_COUNTRY", referencedColumnName = "ID_COUNTRY")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private CountryDO idCountry;

  @JoinColumn(name = "ID_STATE", referencedColumnName = "ID_STATE")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private StateDO idState;

  /**
   * Constructor default
   */
  public CityDO()
  {
  }

  /**
   * Constructor by idCity
   * 
   * @param idCity
   */
  public CityDO( Integer idCity )
  {
    this.idCity = idCity;
  }

  /**
   * @return the idCity
   */
  public Integer getIdCity()
  {
    return idCity;
  }

  /**
   * @return the idVista
   */
  public String getIdVista()
  {
    return idVista;
  }

  /**
   * @param idVista the idVista to set
   */
  public void setIdVista( String idVista )
  {
    this.idVista = idVista;
  }

  /**
   * @param idCity the idCity to set
   */
  public void setIdCity( Integer idCity )
  {
    this.idCity = idCity;
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
   * @return the cityLanguageDOList
   */
  public List<CityLanguageDO> getCityLanguageDOList()
  {
    return cityLanguageDOList;
  }

  /**
   * @param cityLanguageDOList the cityLanguageDOList to set
   */
  public void setCityLanguageDOList( List<CityLanguageDO> cityLanguageDOList )
  {
    this.cityLanguageDOList = cityLanguageDOList;
  }

  /**
   * @return the idCountry
   */
  public CountryDO getIdCountry()
  {
    return idCountry;
  }

  /**
   * @param idCountry the idCountry to set
   */
  public void setIdCountry( CountryDO idCountry )
  {
    this.idCountry = idCountry;
  }

  /**
   * @return the idState
   */
  public StateDO getIdState()
  {
    return idState;
  }

  /**
   * @param idState the idState to set
   */
  public void setIdState( StateDO idState )
  {
    this.idState = idState;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idCity != null ? idCity.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof CityDO) )
    {
      return false;
    }
    CityDO other = (CityDO) object;
    if( (this.idCity == null && other.idCity != null) || (this.idCity != null && !this.idCity.equals( other.idCity )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.CityDO[ idCity=" + idCity + " ]";
  }

  @Override
  public int compareTo( CityDO o )
  {
    return this.idCity.compareTo( o.idCity );
  }

}
