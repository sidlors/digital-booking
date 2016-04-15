package mx.com.cinepolis.digital.booking.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * JPA entity for C_SYSTEM_MENU
 * 
 * @author gsegura
 * @since 0.0.1
 */
@Entity
@Table(name = "C_SYSTEM_MENU")
@NamedQueries({
    @NamedQuery(name = "SystemMenuDO.findAll", query = "SELECT s FROM SystemMenuDO s"),
    @NamedQuery(name = "SystemMenuDO.findByIdSystemMenu", query = "SELECT s FROM SystemMenuDO s WHERE s.idSystemMenu = :idSystemMenu"),
    @NamedQuery(name = "SystemMenuDO.findByUrl", query = "SELECT s FROM SystemMenuDO s WHERE s.dsSystemMenuUrl LIKE :dsSystemMenuUrl AND s.fgActive = 1"),
    @NamedQuery(name = "SystemMenuDO.findByFgActive", query = "SELECT s FROM SystemMenuDO s WHERE s.fgActive = :fgActive") })
public class SystemMenuDO extends AbstractEntity<SystemMenuDO>
{
  private static final long serialVersionUID = -4751780846290612995L;

  @Id
  @Column(name = "ID_SYSTEM_MENU")
  private Integer idSystemMenu;

  @Column(name = "FG_ACTIVE")
  private boolean fgActive;

  @Column(name = "DS_SYSTEM_MENU_URL")
  private String dsSystemMenuUrl;

  @Column(name = "DS_SYSTEM_MENU_ICON")
  private String dsSystemMenuIcon;

  @Column(name = "FG_FUNCTION")
  private boolean fgFunction;

  @Column(name = "NU_ORDER")
  private Integer order;

  @OneToMany(mappedBy = "idParentSystemMenu")
  private List<SystemMenuDO> systemMenuDOList;

  @JoinColumn(name = "ID_PARENT_SYSTEM_MENU", referencedColumnName = "ID_SYSTEM_MENU")
  @ManyToOne
  private SystemMenuDO idParentSystemMenu;

  @ManyToMany(mappedBy = "systemMenuDOList", fetch = FetchType.LAZY)
  private List<RoleDO> roleDOList;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSystemMenu", fetch = FetchType.LAZY)
  private List<SystemMenuLanguageDO> systemMenuLanguageDOList;

  /**
   * Constructor default
   */
  public SystemMenuDO()
  {
  }

  /**
   * Constructor by idSystemFunction
   * 
   * @param idSystemFunction
   */
  public SystemMenuDO( Integer idSystemMenu )
  {
    this.idSystemMenu = idSystemMenu;
  }

  /**
   * @return the idSystemMenu
   */
  public Integer getIdSystemMenu()
  {
    return idSystemMenu;
  }

  /**
   * @param idSystemMenu the idSystemMenu to set
   */
  public void setIdSystemMenu( Integer idSystemMenu )
  {
    this.idSystemMenu = idSystemMenu;
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

  /**
   * @return the idParentSystemMenu
   */
  public SystemMenuDO getIdParentSystemMenu()
  {
    return idParentSystemMenu;
  }

  /**
   * @param idParentSystemMenu the idParentSystemMenu to set
   */
  public void setIdParentSystemMenu( SystemMenuDO idParentSystemMenu )
  {
    this.idParentSystemMenu = idParentSystemMenu;
  }

  /**
   * @return the dsSystemMenuUrl
   */
  public String getDsSystemMenuUrl()
  {
    return dsSystemMenuUrl;
  }

  /**
   * @param dsSystemMenuUrl the dsSystemMenuUrl to set
   */
  public void setDsSystemMenuUrl( String dsSystemMenuUrl )
  {
    this.dsSystemMenuUrl = dsSystemMenuUrl;
  }

  /**
   * @return the dsSystemMenuIcon
   */
  public String getDsSystemMenuIcon()
  {
    return dsSystemMenuIcon;
  }

  /**
   * @param dsSystemMenuIcon the dsSystemMenuIcon to set
   */
  public void setDsSystemMenuIcon( String dsSystemMenuIcon )
  {
    this.dsSystemMenuIcon = dsSystemMenuIcon;
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
   * @return the fgFunction
   */
  public boolean isFgFunction()
  {
    return fgFunction;
  }

  /**
   * @param fgFunction the fgFunction to set
   */
  public void setFgFunction( boolean fgFunction )
  {
    this.fgFunction = fgFunction;
  }

  /**
   * @return the order
   */
  public Integer getOrder()
  {
    return order;
  }

  /**
   * @param order the order to set
   */
  public void setOrder( Integer order )
  {
    this.order = order;
  }

  /**
   * @return the systemMenuLanguageDOList
   */
  public List<SystemMenuLanguageDO> getSystemMenuLanguageDOList()
  {
    return systemMenuLanguageDOList;
  }

  /**
   * @param systemMenuLanguageDOList the systemMenuLanguageDOList to set
   */
  public void setSystemMenuLanguageDOList( List<SystemMenuLanguageDO> systemMenuLanguageDOList )
  {
    this.systemMenuLanguageDOList = systemMenuLanguageDOList;
  }

  @Override
  public int hashCode()
  {
    int hash = 0;
    hash += (idSystemMenu != null ? idSystemMenu.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals( Object object )
  {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if( !(object instanceof SystemMenuDO) )
    {
      return false;
    }
    SystemMenuDO other = (SystemMenuDO) object;
    if( (this.idSystemMenu == null && other.idSystemMenu != null)
        || (this.idSystemMenu != null && !this.idSystemMenu.equals( other.idSystemMenu )) )
    {
      return false;
    }
    return true;
  }

  @Override
  public String toString()
  {
    return "mx.com.cinepolis.digital.booking.model.SystemMenuDO[ idSystemMenu=" + idSystemMenu + " ]";
  }

  @Override
  public int compareTo( SystemMenuDO other )
  {
    return this.idSystemMenu.compareTo( other.idSystemMenu );
  }

}
