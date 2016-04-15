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
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * JPA entity for K_USER
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "K_USER", uniqueConstraints = { @UniqueConstraint(columnNames = { "DS_USERNAME" }) })
@NamedQueries({
    @NamedQuery(name = "UserDO.findAll", query = "SELECT u FROM UserDO u"),
    @NamedQuery(name = "UserDO.findByIdUser", query = "SELECT u FROM UserDO u WHERE u.idUser = :idUser"),
    @NamedQuery(name = "UserDO.findByDsUsername", query = "SELECT u FROM UserDO u WHERE u.dsUsername = :dsUsername AND u.fgActive = 1"),
    @NamedQuery(name = "UserDO.findByFgActive", query = "SELECT u FROM UserDO u WHERE u.fgActive = :fgActive"),
    @NamedQuery(name = "UserDO.findByDtLastModification", query = "SELECT u FROM UserDO u WHERE u.dtLastModification = :dtLastModification"),
    @NamedQuery(name = "UserDO.findByIdLastUserModifier", query = "SELECT u FROM UserDO u WHERE u.idLastUserModifier = :idLastUserModifier") })
public class UserDO extends AbstractSignedEntity<UserDO>
{
  private static final long serialVersionUID = 8330417212307244874L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID_USER", nullable = false)
  private Integer idUser;

  @Column(name = "DS_USERNAME", nullable = false, length = 64)
  private String dsUsername;

  @ManyToMany(mappedBy = "userDOList", fetch = FetchType.EAGER)
  private List<RoleDO> roleDOList;

  @ManyToMany(mappedBy = "userDOList", fetch = FetchType.LAZY, cascade={CascadeType.ALL})
  private List<TheaterDO> theaterDOList;
  
  @ManyToMany(mappedBy = "userDOList", fetch = FetchType.LAZY, cascade={CascadeType.ALL})
  private List<RegionDO> regionDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser", fetch = FetchType.LAZY)
  private List<ObservationDO> observationDOList;

  @JoinColumn(name = "ID_PERSON", referencedColumnName = "ID_PERSON", nullable = false)
  @OneToOne(optional = false, fetch = FetchType.LAZY )
  private PersonDO idPerson;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUser", fetch = FetchType.LAZY)
  private List<SystemLogDO> systemLogDOList;
  /**
   * Constructor default
   */
  public UserDO()
  {
  }

  /**
   * Constructor by idUser
   * 
   * @param idUser
   */
  public UserDO( Integer idUser )
  {
    this.idUser = idUser;
  }

  /**
   * @return the idUser
   */
  public Integer getIdUser()
  {
    return idUser;
  }

  /**
   * @param idUser the idUser to set
   */
  public void setIdUser( Integer idUser )
  {
    this.idUser = idUser;
  }

  /**
   * @return the dsUsername
   */
  public String getDsUsername()
  {
    return dsUsername;
  }

  /**
   * @param dsUsername the dsUsername to set
   */
  public void setDsUsername( String dsUsername )
  {
    this.dsUsername = dsUsername;
  }

  /**
   * @return the roleDOList
   */
  public List<RoleDO> getRoleDOList()
  {
    return roleDOList;
  }

  /**
   * @param roleDOList the roleDOList to set
   */
  public void setRoleDOList( List<RoleDO> roleDOList )
  {
    this.roleDOList = roleDOList;
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
   * @return the observationDOList
   */
  public List<ObservationDO> getObservationDOList()
  {
    return observationDOList;
  }

  /**
   * @param observationDOList the observationDOList to set
   */
  public void setObservationDOList( List<ObservationDO> observationDOList )
  {
    this.observationDOList = observationDOList;
  }

  /**
   * @return the idPerson
   */
  public PersonDO getIdPerson()
  {
    return idPerson;
  }

  /**
   * @param idPerson the idPerson to set
   */
  public void setIdPerson( PersonDO idPerson )
  {
    this.idPerson = idPerson;
  }

  /**
   * @return the systemLogDOList
   */
  public List<SystemLogDO> getSystemLogDOList()
  {
    return systemLogDOList;
  }

  /**
   * @param systemLogDOList the systemLogDOList to set
   */
  public void setSystemLogDOList( List<SystemLogDO> systemLogDOList )
  {
    this.systemLogDOList = systemLogDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idUser != null ? idUser.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof UserDO) )
    {
      return false;
    }
    UserDO other = (UserDO) object;
    if( (this.idUser == null && other.idUser != null) || (this.idUser != null && !this.idUser.equals( other.idUser )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.UserDO[ idUser=" + idUser + " ]";
  }

  @Override
  public int compareTo( UserDO other )
  {
    return this.idUser.compareTo( other.idUser );
  }

}
