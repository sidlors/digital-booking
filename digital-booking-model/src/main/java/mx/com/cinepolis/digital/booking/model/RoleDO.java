package mx.com.cinepolis.digital.booking.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * JPA entity for C_ROLE
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_ROLE")
@NamedQueries({ @NamedQuery(name = "RoleDO.findAll", query = "SELECT r FROM RoleDO r"),
    @NamedQuery(name = "RoleDO.findByIdRole", query = "SELECT r FROM RoleDO r WHERE r.idRole = :idRole"),
    @NamedQuery(name = "RoleDO.findByDsRole", query = "SELECT r FROM RoleDO r WHERE r.dsRole = :dsRole"),
    @NamedQuery(name = "RoleDO.findByFgActive", query = "SELECT r FROM RoleDO r WHERE r.fgActive = :fgActive") })
public class RoleDO extends AbstractEntity<RoleDO>
{
  private static final long serialVersionUID = -1221167292181611957L;

  @Id
  @Column(name = "ID_ROLE")
  private Integer idRole;

  @Column(name = "DS_ROLE")
  private String dsRole;

  @Column(name = "FG_ACTIVE")
  private boolean fgActive;

  @JoinTable(name = "K_ROLE_X_USER", joinColumns = { @JoinColumn(name = "ID_ROLE", referencedColumnName = "ID_ROLE") }, inverseJoinColumns = { @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER") })
  @ManyToMany(fetch = FetchType.LAZY)
  private List<UserDO> userDOList;

  @JoinTable(name = "C_SYSTEM_MENU_X_ROLE", joinColumns = { @JoinColumn(name = "ID_ROLE", referencedColumnName = "ID_ROLE") }, inverseJoinColumns = { @JoinColumn(name = "ID_SYSTEM_MENU", referencedColumnName = "ID_SYSTEM_MENU") })
  @ManyToMany(fetch = FetchType.LAZY)
  private List<SystemMenuDO> systemMenuDOList;

  /**
   * Constructor default
   */
  public RoleDO()
  {
  }

  /**
   * Constructor by idRole
   * 
   * @param idRole
   */
  public RoleDO( Integer idRole )
  {
    this.idRole = idRole;
  }

  /**
   * @return the idRole
   */
  public Integer getIdRole()
  {
    return idRole;
  }

  /**
   * @param idRole the idRole to set
   */
  public void setIdRole( Integer idRole )
  {
    this.idRole = idRole;
  }

  /**
   * @return the dsRole
   */
  public String getDsRole()
  {
    return dsRole;
  }

  /**
   * @param dsRole the dsRole to set
   */
  public void setDsRole( String dsRole )
  {
    this.dsRole = dsRole;
  }

  /**
   * @return the fgActive
   */
  public boolean isFgActive()
  {
    return fgActive;
  }

  /**
   * @param fgActive the fgActive to set
   */
  public void setFgActive( boolean fgActive )
  {
    this.fgActive = fgActive;
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
   * @return the systemMenuDOList
   */
  public List<SystemMenuDO> getSystemMenuDOList()
  {
    return systemMenuDOList;
  }

  /**
   * @param systemMenuDOList the systemMenuDOList to set
   */
  public void setSystemMenuDOList( List<SystemMenuDO> systemMenuDOList )
  {
    this.systemMenuDOList = systemMenuDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idRole != null ? idRole.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    if( !(object instanceof RoleDO) )
    {
      return false;
    }
    RoleDO other = (RoleDO) object;
    if( (this.idRole == null && other.idRole != null) || (this.idRole != null && !this.idRole.equals( other.idRole )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.RoleDO[ idRole=" + idRole + " ]";
  }

  @Override
  public int compareTo( RoleDO other )
  {
    return this.idRole.compareTo( other.idRole );
  }

}
