package mx.com.cinepolis.digital.booking.web.beans.systemconfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.integration.configuration.AssignUserServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.model.DualListModel;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * Backing bean for the assign user to regions interface.
 * 
 * @author keperez
 */
@ManagedBean(name = "assignUserRegionsBean")
@ViewScoped
public class AssignUserRegionsBean extends BaseManagedBean
{

  private static final String HOME_ASSIGN_USER_REGIONS_DO = "./config.do";

  private static final long serialVersionUID = -2779947114568994747L;

  private List<UserTO> userTOs;
  private Long userIdSelected;
  private List<CatalogTO> regionsSelected;
  private DualListModel<CatalogTO> regionsModel;
  private UserLazyDataModel users;
  private UserTO selectedUser;

  @EJB
  private AssignUserServiceIntegratorEJB assignUserServiceIntegratorEJB;

  /**
   * Constructor default
   */
  @PostConstruct
  public void init()
  {
    userTOs = assignUserServiceIntegratorEJB.getAllUsersActive();
    regionsModel = new DualListModel<CatalogTO>();
    users = new UserLazyDataModel();
    users.setAssignUserServiceIntegratorEJB( assignUserServiceIntegratorEJB );
  }

  public void assignUserRegions()
  {
    UserTO userTO = new UserTO();
    userTO.setId( userIdSelected );
    userTO.setRegions( regionsModel.getTarget() );
    fillSessionData( userTO );
    assignUserServiceIntegratorEJB.assignRegionsUser( userTO );
  }

  public void back() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( HOME_ASSIGN_USER_REGIONS_DO );
  }

  public void loadRegionsByUserId( AjaxBehaviorEvent event )
  {
    setRegionsModel( new DualListModel<CatalogTO>() );
    Long idUser = (Long) event.getComponent().getAttributes().get( "value" );
    List<CatalogTO> regions = assignUserServiceIntegratorEJB.getRegionsAvailable( idUser );
    regionsSelected = assignUserServiceIntegratorEJB.getRegionsAssociated( idUser );
    regionsModel = new DualListModel<CatalogTO>( regions, regionsSelected );
  }

  /**
   * @return the userTOs
   */
  public List<UserTO> getUserTOs()
  {
    return userTOs;
  }

  /**
   * @param userTOs the userTOs to set
   */
  public void setUserTOs( List<UserTO> userTOs )
  {
    this.userTOs = userTOs;
  }

  /**
   * @return the userIdSelected
   */
  public Long getUserIdSelected()
  {
    return userIdSelected;
  }

  /**
   * @param userIdSelected the userIdSelected to set
   */
  public void setUserIdSelected( Long userIdSelected )
  {
    this.userIdSelected = userIdSelected;
  }

  /**
   * @return the regionsModel
   */
  public DualListModel<CatalogTO> getRegionsModel()
  {
    return regionsModel;
  }

  /**
   * @return the users
   */
  public UserLazyDataModel getUsers()
  {
    return users;
  }

  /**
   * @param users the users to set
   */
  public void setUsers( UserLazyDataModel users )
  {
    this.users = users;
  }

  /**
   * @param regionsModel the regionsModel to set
   */
  public void setRegionsModel( DualListModel<CatalogTO> regionsModel )
  {
    this.regionsModel = regionsModel;
  }

  /**
   * @return the selectedUser
   */
  public UserTO getSelectedUser()
  {
    return selectedUser;
  }

  /**
   * @param selectedUser the selectedUser to set
   */
  public void setSelectedUser( UserTO selectedUser )
  {
    this.selectedUser = selectedUser;
  }

  public void setUserData()
  {
    this.userIdSelected = this.selectedUser.getId();
    List<CatalogTO> regions = assignUserServiceIntegratorEJB.getRegionsAvailable( this.userIdSelected );
    regionsSelected = assignUserServiceIntegratorEJB.getRegionsAssociated( this.userIdSelected );
    regionsModel = new DualListModel<CatalogTO>( regions, regionsSelected );
  }
  
  
  private static class UserLazyDataModel extends LazyDataModel<UserTO>
  {
    private static final long serialVersionUID = -1905600049360323950L;
    private AssignUserServiceIntegratorEJB assignUserServiceIntegratorEJB;

    /**
     * @param assignUserServiceIntegratorEJB the assignUserServiceIntegratorEJB to set
     */
    public void setAssignUserServiceIntegratorEJB( AssignUserServiceIntegratorEJB assignUserServiceIntegratorEJB )
    {
      this.assignUserServiceIntegratorEJB = assignUserServiceIntegratorEJB;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UserTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      List<UserTO> filtered = new ArrayList<UserTO>();
      List<UserTO> users = assignUserServiceIntegratorEJB.getAllUsersActive();
      if( CollectionUtils.isNotEmpty( users ) )
      {
        for( int i = first; i < first + pageSize && i < users.size(); i++ )
        {
          filtered.add( users.get( i ) );
        }
      }
      this.setRowCount( users.size() );
      return filtered;
    }
  }
}
