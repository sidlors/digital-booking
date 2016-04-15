package mx.com.cinepolis.digital.booking.web.beans.systemconfiguration;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.UserQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.integration.user.UserServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

/**
 * User Managements
 * 
 * @author agustin.ramirez
 */
@ManagedBean(name = "adminUserBean")
@ViewScoped
public class AdminUserBean extends BaseManagedBean implements Serializable
{
  private static final Logger LOG = LoggerFactory.getLogger( AdminUserBean.class );
  /**
	 * 
	 */
  private static final long serialVersionUID = 6897552243100797620L;

  private static final String HOME_ASSIGN_USER_REGIONS_DO = "./config.do";

  /**
   * User Service Integrator
   */
  @EJB
  private UserServiceIntegratorEJB userServiceIntegratorEJB;

  /**
   * Data Grid
   */
  private LazyDataModel<UserTO> users;

  /**
   * UserSelected in Grid
   */
  private UserTO userSelected = new UserTO();

  /**
   * User To Save
   */
  private UserTO newUser = new UserTO();

  /**
   * Roles ComboBox
   */
  private List<CatalogTO> roles;

  /**
   * Roles Selected from comboBox
   */
  private List<Long> rolesSelected = new ArrayList<Long>();

  /**
   * Email Saved
   */
  private String newEmail;

  /**
   * Constructor default
   */
  @PostConstruct
  public void init()
  {
    this.setUsers( new UserLazyDataModel( this.userServiceIntegratorEJB ) );
    ((UserLazyDataModel) this.getUsers()).setUserId( super.getUserId() );
    roles = userServiceIntegratorEJB.findAllRolesActive();
    newUser.setRolesSelected( new ArrayList<Long>() );
  }

  public void back() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( HOME_ASSIGN_USER_REGIONS_DO );
  }

  /**
   * Close de pop-up
   * 
   * @param event
   */
  public void handleClose( CloseEvent event )

  {
    userSelected = null;
    newEmail = null;
    rolesSelected = null;
    newUser = new UserTO();
  }

  /**
   * Save a User
   */
  public void saveUser()
  {
    newUser.setIsUpdated( false );
    validateEmail( this.newEmail );
    newUser.getPersonTO().setEmails( new ArrayList<CatalogTO>() );
    CatalogTO newEmailTO = new CatalogTO();
    newEmailTO.setName( newEmail );
    newUser.getPersonTO().getEmails().add( newEmailTO );
    newUser.setRoles( new ArrayList<CatalogTO>() );
    for( Long id : newUser.getRolesSelected() )
    {
      newUser.getRoles().add( new CatalogTO( id ) );
    }
    super.fillSessionData( newUser );
    userServiceIntegratorEJB.saveUser( newUser );
    newUser = new UserTO();
    newEmailTO = null;
    rolesSelected = null;
  }

  /**
   * Update User
   */
  public void updateUser()
  {
    LOG.debug( "Usuarios" + ReflectionToStringBuilder.toString( userSelected, ToStringStyle.MULTI_LINE_STYLE ) );
    userSelected.setIsUpdated( false );
    validateEmail( userSelected.getEmailToUpdate() );
    super.fillSessionData( userSelected );
    userSelected.setRoles( new ArrayList<CatalogTO>() );
    for( Long r : userSelected.getRolesSelected() )
    {
      userSelected.getRoles().add( new CatalogTO( r ) );
    }
    userSelected.getPersonTO().getEmails().get( 0 ).setName( userSelected.getEmailToUpdate() );

    userServiceIntegratorEJB.updateUser( userSelected );
    userSelected = new UserTO();

  }

  /**
   * Delete User
   */
  public void deleteUser()
  {
    super.fillSessionData( userSelected );
    if( userSelected.getName().equals( userSelected.getUsername() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.USER_TRY_DELETE_OWN_USER );
    }
    userServiceIntegratorEJB.deleteUser( userSelected );
    userSelected = new UserTO();
  }

  /**
   * Validate Email
   */
  private void validateEmail( String email )
  {
    if( StringUtils.isNotBlank( email ) )
    {
      String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
      if( !email.matches( regex ) )
      {
        throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.EMAIL_DOES_NOT_COMPLIES_REGEX );
      }
    }
  }

  /**
   * Validate the selected data grid
   */
  public void validateSelection()
  {

    if( userSelected == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
    }
  }

  /**
   * @return the users
   */
  public LazyDataModel<UserTO> getUsers()
  {
    return users;
  }

  /**
   * @param users the users to set
   */
  public void setUsers( LazyDataModel<UserTO> users )
  {
    this.users = users;
  }

  /**
   * @return the userSelected
   */
  public UserTO getUserSelected()
  {
    return userSelected;
  }

  /**
   * @param userSelected the userSelected to set
   */
  public void setUserSelected( UserTO userSelected )
  {
    this.userSelected = userSelected;
  }

  /**
   * @return the newUser
   */
  public UserTO getNewUser()
  {
    return newUser;
  }

  /**
   * @param newUser the newUser to set
   */
  public void setNewUser( UserTO newUser )
  {
    this.newUser = newUser;
  }

  /**
   * @return the roles
   */
  public List<CatalogTO> getRoles()
  {
    return roles;
  }

  /**
   * @param roles the roles to set
   */
  public void setRoles( List<CatalogTO> roles )
  {
    this.roles = roles;
  }

  /**
   * @return the rolesSelected
   */
  public List<Long> getRolesSelected()
  {
    return rolesSelected;
  }

  /**
   * @param rolesSelected the rolesSelected to set
   */
  public void setRolesSelected( List<Long> rolesSelected )
  {
    this.rolesSelected = rolesSelected;
  }

  /**
   * @return the newEmail
   */
  public String getNewEmail()
  {
    return newEmail;
  }

  /**
   * @param newEmail the newEmail to set
   */
  public void setNewEmail( String newEmail )
  {
    this.newEmail = newEmail;
  }

  /**
   * Class for the data of data grid
   * 
   * @author agustin.ramirez
   */
  static class UserLazyDataModel extends LazyDataModel<UserTO>
  {

    private static final long serialVersionUID = 415176692334849300L;
    private UserServiceIntegratorEJB userServiceIntegratorEJB;
    private Long userId;

    /**
     * Static Class for the data grid
     * 
     * @param userServiceIntegratorEJB
     */
    public UserLazyDataModel( UserServiceIntegratorEJB userServiceIntegratorEJB )
    {
      this.userServiceIntegratorEJB = userServiceIntegratorEJB;
    }

    @Override
    public List<UserTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      int page = first / pageSize;
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( userId );
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( UserQuery.USER_NAME );
      pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
      pagingRequestTO.getFilters().put( UserQuery.USER_ACTIVE, true );
      PagingResponseTO<UserTO> response = userServiceIntegratorEJB.findAllUsers( pagingRequestTO );
      List<UserTO> result = response.getElements();
      this.setRowCount( response.getTotalCount() );
      return result;
    }

    /**
     * @return the userId
     */
    public Long getUserId()
    {
      return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId( Long userId )
    {
      this.userId = userId;
    }
  }

}
