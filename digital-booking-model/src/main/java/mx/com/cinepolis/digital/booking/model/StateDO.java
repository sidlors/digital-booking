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
 * JPA entity for C_STATE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_STATE")
@NamedQueries({
    @NamedQuery(name = "StateDO.findAll", query = "SELECT s FROM StateDO s"),
    @NamedQuery(name = "StateDO.findByIdState", query = "SELECT s FROM StateDO s WHERE s.idState = :idState"),
    @NamedQuery(name = "StateDO.findByIdVista", query = "SELECT s FROM StateDO s WHERE s.idVista = :idVista"),
    @NamedQuery(name = "StateDO.findByIdVistaAndActive", query = "SELECT s FROM StateDO s WHERE s.idVista = :idVista and s.fgActive = true"),
    @NamedQuery(name = "StateDO.findByFgActive", query = "SELECT s FROM StateDO s WHERE s.fgActive = :fgActive"),
    @NamedQuery(name = "StateDO.findByDtLastModification", query = "SELECT s FROM StateDO s WHERE s.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "StateDO.findByIdLastUserModifier", query = "SELECT s FROM StateDO s WHERE s.idLastUserModifier = :idLastUserModifier") })
public class StateDO extends AbstractSignedEntity<StateDO>
{
  private static final long serialVersionUID = 692339868716251238L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_STATE")
  private Integer idState;

  @Column(name = "ID_VISTA")
  private String idVista;

  @OneToMany(mappedBy = "idState", fetch = FetchType.LAZY)
  private List<TheaterDO> theaterDOList;

  @JoinColumn(name = "ID_COUNTRY", referencedColumnName = "ID_COUNTRY")
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  private CountryDO idCountry;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idState", fetch = FetchType.LAZY)
  private List<StateLanguageDO> stateLanguageDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idState", fetch = FetchType.LAZY)
  private List<CityDO> cityDOList;

  /**
   * Constructor default
   */
  public StateDO()
  {
  }

  /**
   * Constructor by idState
   * 
   * @param idState
   */
  public StateDO( Integer idState )
  {
    this.idState = idState;
  }

  /**
   * @return the idState
   */
  public Integer getIdState()
  {
    return idState;
  }

  /**
   * @param idState the idState to set
   */
  public void setIdState( Integer idState )
  {
    this.idState = idState;
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
   * @return the stateLanguageDOList
   */
  public List<StateLanguageDO> getStateLanguageDOList()
  {
    return stateLanguageDOList;
  }

  /**
   * @param stateLanguageDOList the stateLanguageDOList to set
   */
  public void setStateLanguageDOList( List<StateLanguageDO> stateLanguageDOList )
  {
    this.stateLanguageDOList = stateLanguageDOList;
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
    hash += (idState != null ? idState.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof StateDO) )
    {
      return false;
    }
    StateDO other = (StateDO) object;
    if( (this.idState == null && other.idState != null)
        || (this.idState != null && !this.idState.equals( other.idState )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.StateDO[ idState=" + idState + " ]";
  }

  @Override
  public int compareTo( StateDO other )
  {
    return this.idState.compareTo( other.idState );
  }

}
