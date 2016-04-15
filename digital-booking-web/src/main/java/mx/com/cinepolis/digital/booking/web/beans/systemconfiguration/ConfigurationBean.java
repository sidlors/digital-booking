package mx.com.cinepolis.digital.booking.web.beans.systemconfiguration;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

@ManagedBean(name = "configurationBean")
@ViewScoped
public class ConfigurationBean extends BaseManagedBean
{

  private static final String ASSIGN_USERS_REGION_DO = "../systemconfiguration/assignUserRegions.do";
  private static final String ASSIGN_USERS_ADM_TEMPLATE_REGION_DO = "../systemconfiguration/assignAdmTemplateRegions.do";
  private static final String ASSIGN_USERS_THEATER_DO = "../systemconfiguration/assignUserTheaters.do";
  private static final String ADMIN_USERS = "../systemconfiguration/adminUsers.do";
  private static final String SYSTEM_LOG = "../systemconfiguration/systemlog/systemlog.do";
  private static final long serialVersionUID = -8974995643735116106L;

  /**
   * Method that redirect at assign user to regions page.
   * 
   * @throws IOException
   * @author kperez
   */
  public void changeAssignUserRegionsPage() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( ASSIGN_USERS_REGION_DO );
  }

  /**
   * Method that redirect at assign user administrator template to regions page.
   * 
   * @throws IOException
   * @author jreyesv
   */
  public void changeAssignUserAdminTemplateRegionsPage() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( ASSIGN_USERS_ADM_TEMPLATE_REGION_DO );
  }

  public void changeAssignUserTheatersPage() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( ASSIGN_USERS_THEATER_DO );
  }

  public void changeAdminUsers() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( ADMIN_USERS );
  }

  public void changeSystemLogPage() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( SYSTEM_LOG );
  }
}
