package mx.com.cinepolis.digital.booking.web.login;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.integration.security.ServiceSecurityIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

@ManagedBean(name = "loginBean")
@ViewScoped
public class LoginBean extends BaseManagedBean
{
  private static final String LOGIN_URL = "/views/login/login.do?faces-redirect=true";
  private static final String USER_ATTRIBUTE = "user";
  private static final String HOME_URL = "../home/home.do";
  private static final String REQUSTED_URL_ATTRIBUTE = "requstedUrl";

  private static final long serialVersionUID = 1364641621874110940L;

  @EJB
  private ServiceSecurityIntegratorEJB serviceSecurityIntegratorEJB;

  private String username;
  private String password;

//  @PostConstruct
//  public void initializeSession()
//  {
//    FacesContext ctx = FacesContext.getCurrentInstance();
//    ctx.getExternalContext().invalidateSession();
//    ctx.getExternalContext().getSession( true );
//  }

  /**
   * Method that performs a login to the system.
   * 
   * @throws @{@link IOException} if there is a problem concerning URL redirection.
   */
  public void logIn() throws IOException
  {
    UserTO userTO = new UserTO();
    userTO.setName( username );
    userTO.setPassword( password );
    // En caso de q no sea válido no se loggea el usuario y lanza una excepción
    serviceSecurityIntegratorEJB.authenticate( userTO );

    // Busca el detalle del usuario
    userTO = serviceSecurityIntegratorEJB.findUserDatail( username );
    setAttribute( USER_ATTRIBUTE, userTO );

    FacesContext ctx = FacesContext.getCurrentInstance();
    String requstedUrl = HOME_URL;
    Object requstedUrlObj = getAttribute( REQUSTED_URL_ATTRIBUTE );
    if( requstedUrlObj != null )
    {
      requstedUrl = (String) requstedUrlObj;
      removeAttribute( REQUSTED_URL_ATTRIBUTE );
    }
    ctx.responseComplete();
    ctx.getExternalContext().redirect( requstedUrl );

  }

  /**
   * Method that performs a logout from the system.
   * 
   * @throws IOException if there is a problem concerning URL redirection
   */
  public void logOut() throws IOException
  {
    UserTO userTO = (UserTO) getAttribute( USER_ATTRIBUTE );
    serviceSecurityIntegratorEJB.logOut( userTO );
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().invalidateSession();
    removeAttribute( USER_ATTRIBUTE );
    ConfigurableNavigationHandler nav = (ConfigurableNavigationHandler) ctx.getApplication().getNavigationHandler();
    nav.performNavigation( LOGIN_URL );
  }

  /* Getters and setters */

  /**
   * @return the username
   */
  public String getUsername()
  {
    return username;
  }

  /**
   * @param username the username to set
   */
  public void setUsername( String username )
  {
    this.username = username;
  }

  /**
   * @return the password
   */
  public String getPassword()
  {
    return password;
  }

  /**
   * @param password the password to set
   */
  public void setPassword( String password )
  {
    this.password = password;
  }
}
