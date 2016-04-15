package mx.com.cinepolis.digital.booking.web.login;

import java.io.IOException;

import javax.ejb.EJB;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import mx.com.cinepolis.digital.booking.commons.to.SystemMenuTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.commons.utils.PersonUtils;
import mx.com.cinepolis.digital.booking.integration.security.ServiceSecurityIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "dynamicMenuBean")
@ViewScoped
public class DynamicMenuBean extends BaseManagedBean
{
  private static final Logger LOG = LoggerFactory.getLogger( DynamicMenuBean.class );
  private static final String CONFIG_DO = "../systemconfiguration/config.do";
  private static final String COMPLETE_CONFIG_DO = "/views/systemconfiguration/config.do";
  private static final String LOGIN_URL = "/views/login/login.do?faces-redirect=true";

  private static final String HYPHEN = " - ";

  private static final long serialVersionUID = 3194629955160323592L;

  private MenuModel dynamicMenuModel;
  private String personName;

  private boolean showConfigPage;

  @EJB
  private ServiceSecurityIntegratorEJB serviceSecurityIntegratorEJB;

  /**
   * @return the dynamicMenuModel
   * @throws IOException
   */
  public MenuModel getDynamicMenuModel() throws IOException
  {
    buildDynamicMenu();
    return dynamicMenuModel;
  }

  private void buildDynamicMenu() throws IOException
  {
    UserTO userTO = (UserTO) getAttribute( USER_ATTRIBUTE );
    if( userTO != null )
    {
      SystemMenuTO menuTO = serviceSecurityIntegratorEJB.findMenuByUser( userTO.getId() );
      showConfigPage = serviceSecurityIntegratorEJB.hasConfigPage( userTO.getId() );

      DefaultSubMenu topLevelSubmenu = new DefaultSubMenu();
      buildMenuElements( menuTO, topLevelSubmenu );

      dynamicMenuModel = new DefaultMenuModel();
      dynamicMenuModel.getElements().addAll( topLevelSubmenu.getElements() );
    }
    else
    {
      LOG.debug( "UserTO es null" );
      FacesContext ctx = FacesContext.getCurrentInstance();

      ctx.getExternalContext().invalidateSession();
      removeAttribute( USER_ATTRIBUTE );

      ctx.responseComplete();
      ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) ctx.getApplication().getNavigationHandler();
      nav.performNavigation( LOGIN_URL );

    }

  }

  private void buildMenuElements( SystemMenuTO menuTO, DefaultSubMenu parent )
  {
    for( SystemMenuTO childTO : menuTO.getChildren() )
    {
      if( childTO.hasChildren() )
      {
        DefaultSubMenu submenu = buildSubMenu( childTO, parent );
        buildMenuElements( childTO, submenu );
      }
      else
      {
        buildMenuItem( childTO, parent );
      }
    }
  }

  private DefaultSubMenu buildSubMenu( SystemMenuTO systemMenuTO, DefaultSubMenu parent )
  {
    DefaultSubMenu submenu = new DefaultSubMenu();
    submenu.setLabel( systemMenuTO.getName() );
    submenu.setIcon( systemMenuTO.getIcon() );
    parent.addElement( submenu );
    return submenu;
  }

  private void buildMenuItem( SystemMenuTO systemMenuTO, DefaultSubMenu parent )
  {
    DefaultMenuItem menuItem = new DefaultMenuItem();
    menuItem.setValue( systemMenuTO.getName() );
    menuItem.setIcon( systemMenuTO.getIcon() );
    menuItem.setUrl( systemMenuTO.getUrl() );
    parent.addElement( menuItem );
  }

  /**
   * @return the personName
   */
  public String getPersonName()
  {
    buildPersonName();
    addRolesToPersonName();
    return personName;
  }

  private void addRolesToPersonName()
  {
    UserTO userTO = (UserTO) getAttribute( USER_ATTRIBUTE );
    personName = userTO != null ? CinepolisUtils.buildStringUsingMutable( personName, HYPHEN,
      PersonUtils.buildRolesList( userTO.getRoles() ) ) : "";
  }

  private void buildPersonName()
  {
    UserTO userTO = (UserTO) getAttribute( USER_ATTRIBUTE );
    personName = userTO != null ? PersonUtils.buildFullName( userTO.getPersonTO() ) : "";
  }

  /**
   * Method that clears the cache.
   * 
   * @author afuentes
   */
  public void clearCache()
  {
    serviceSecurityIntegratorEJB.clearJPACache();
  }

  /**
   * Method that redirect at system configuration page.
   * 
   * @throws IOException
   * @author kperez
   */
  public void changeConfigPage() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();

    Object req = ctx.getExternalContext().getRequest();
    String config = CONFIG_DO;
    if( req instanceof HttpServletRequest )
    {
      config = ((HttpServletRequest) req).getContextPath() + COMPLETE_CONFIG_DO;
    }

    ctx.getExternalContext().redirect( config );
  }

  /**
   * @return the showConfigPage
   */
  public boolean isShowConfigPage()
  {
    return showConfigPage;
  }

  /**
   * @param showConfigPage the showConfigPage to set
   */
  public void setShowConfigPage( boolean showConfigPage )
  {
    this.showConfigPage = showConfigPage;
  }

}
