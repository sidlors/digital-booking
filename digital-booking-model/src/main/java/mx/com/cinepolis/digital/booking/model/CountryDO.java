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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for C_COUNTRY
 * 
 * @author kperez
 */
@Entity
@Table(name = "C_COUNTRY")
@NamedQueries({
    @NamedQuery(name = "CountryDO.findAll", query = "SELECT c FROM CountryDO c"),
    @NamedQuery(name = "CountryDO.findByIdCountry", query = "SELECT c FROM CountryDO c WHERE c.idCountry = :idCountry"),
    @NamedQuery(name = "CountryDO.findByIdVista", query = "SELECT c FROM CountryDO c WHERE c.idVista = :idVista"),
    @NamedQuery(name = "CountryDO.findByIdVistaAndActive", query = "SELECT c FROM CountryDO c WHERE c.idVista = :idVista and c.fgActive = true"),
    @NamedQuery(name = "CountryDO.findByFgActive", query = "SELECT c FROM CountryDO c WHERE c.fgActive = :fgActive"),
    @NamedQuery(name = "CountryDO.findByDtLastModification", query = "SELECT c FROM CountryDO c WHERE c.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "CountryDO.findByIdLastUserModifier", query = "SELECT c FROM CountryDO c WHERE c.idLastUserModifier = :idLastUserModifier") })
public class CountryDO extends AbstractSignedEntity<CountryDO>
{

  private static final long serialVersionUID = 6386645014134196695L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_COUNTRY", nullable = false)
  private Integer idCountry;

  @Column(name = "ID_VISTA")
  private String idVista;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCountry", fetch = FetchType.LAZY)
  private List<StateDO> stateDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCountry", fetch = FetchType.LAZY)
  private List<CountryLanguageDO> countryLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idCountry", fetch = FetchType.LAZY)
  private List<CityDO> cityDOList;

  /**
   * Constructor default
   */
  public CountryDO()
  {
  }

  /**
   * Constructor by idCountry
   * 
   * @param idCountry
   */
  public CountryDO( Integer idCountry )
  {
    this.idCountry = idCountry;
  }

  /**
   * @return the idCountry
   */
  public Integer getIdCountry()
  {
    return idCountry;
  }

  /**
   * @param idCountry the idCountry to set
   */
  public void setIdCountry( Integer idCountry )
  {
    this.idCountry = idCountry;
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
   * @return the stateDOList
   */
  public List<StateDO> getStateDOList()
  {
    return stateDOList;
  }

  /**
   * @param stateDOList the stateDOList to set
   */
  public void setStateDOList( List<StateDO> stateDOList )
  {
    this.stateDOList = stateDOList;
  }

  /**
   * @return the countryLanguageDOList
   */
  public List<CountryLanguageDO> getCountryLanguageDOList()
  {
    return countryLanguageDOList;
  }

  /**
   * @param countryLanguageDOList the countryLanguageDOList to set
   */
  public void setCountryLanguageDOList( List<CountryLanguageDO> countryLanguageDOList )
  {
    this.countryLanguageDOList = countryLanguageDOList;
  }

  /**
   * @return the cityDOList
   */
  public List<CityDO> getCityDOList()
  {
    return cityDOList;
  }

  /**
   * @param cityDOList the cityDOList to set
   */
  public void setCityDOList( List<CityDO> cityDOList )
  {
    this.cityDOList = cityDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idCountry != null ? idCountry.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof CountryDO) )
    {
      return false;
    }
    CountryDO other = (CountryDO) object;
    if( (this.idCountry == null && other.idCountry != null)
        || (this.idCountry != null && !this.idCountry.equals( other.idCountry )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.CountryDO[ idCountry=" + idCountry + " ]";
  }

  @Override
  public int compareTo( CountryDO o )
  {
    return this.idCountry.compareTo( o.idCountry );
  }

}
