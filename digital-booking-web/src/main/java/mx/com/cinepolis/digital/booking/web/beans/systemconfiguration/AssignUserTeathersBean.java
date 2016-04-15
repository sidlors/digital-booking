package mx.com.cinepolis.digital.booking.web.beans.systemconfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.convert.FacesConverter;
import javax.faces.event.AjaxBehaviorEvent;

import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.integration.configuration.AssignUserServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.primefaces.model.DualListModel;

/**
 * Backing bean for the assign user to theaters interface.
 * 
 * @author keperez
 */
@ManagedBean(name = "assignUserTeathersBean")
@ViewScoped
@FacesConverter(value = "assignUserTeathersBean")
public class AssignUserTeathersBean extends BaseManagedBean
{

  private static final String HOME_ASSIGN_USER_REGIONS_DO = "./config.do";

  private static final long serialVersionUID = 5595331260916527554L;

  private List<UserTO> userTOs;
  private DualListModel<CatalogTO> theaterList;
  private List<CatalogTO> regionTOs;
  private Long regionIdSelected;
  private Long userIdSelected;

  @EJB
  private AssignUserServiceIntegratorEJB assignUserServiceIntegratorEJB;

  /**
   * Constructor default
   */
  @PostConstruct
  public void init()
  {
    userTOs = assignUserServiceIntegratorEJB.getAllUsersActive();
    theaterList = new DualListModel<CatalogTO>();
  }

  private AbstractTO createAbstractTO()
  {
    AbstractTO abstractTO = new AbstractTO();
    fillSessionData( abstractTO );
    return abstractTO;
  }

  public void assignUserTheaters()
  {
    UserTO userTO = new UserTO();
    userTO.setId( userIdSelected );
    userTO.setTheaters( theaterList.getTarget() );
    userTO.setRegionSelected( this.regionIdSelected );
    fillSessionData( userTO );
    assignUserServiceIntegratorEJB.assignTheatersUser( userTO );
  }

  public void loadTheatersByRegionId( AjaxBehaviorEvent event )
  {
    List<CatalogTO> theatersSelected = new ArrayList<CatalogTO>();
    setTheaterList( new DualListModel<CatalogTO>( new ArrayList<CatalogTO>(), theatersSelected ) );
    Long idRegion = (Long) event.getComponent().getAttributes().get( "value" );
    AbstractTO abstractTO = createAbstractTO();
    abstractTO.setUserId( getUserIdSelected() );
    theatersSelected = assignUserServiceIntegratorEJB.getTheatersAssociated( idRegion,  abstractTO );
    List<CatalogTO> theaterTOs = assignUserServiceIntegratorEJB.getTheatersAvailable( idRegion,  abstractTO);
    setTheaterList( new DualListModel<CatalogTO>( theaterTOs, theatersSelected ) );
  }

  public void back() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( HOME_ASSIGN_USER_REGIONS_DO );
  }

  public void loadRegionsByUserId( AjaxBehaviorEvent event )
  {
    clearUserInfo();
    Long idUser = (Long) event.getComponent().getAttributes().get( "value" );
    setUserIdSelected( idUser );
    setRegionTOs( assignUserServiceIntegratorEJB.getRegionsAssociated( idUser ) );
  }

  private void clearUserInfo()
  {
    setRegionIdSelected( null );
    setRegionTOs( null );
    setTheaterList( new DualListModel<CatalogTO>() );
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
   * @return the theaterList
   */
  public DualListModel<CatalogTO> getTheaterList()
  {
    return theaterList;
  }

  /**
   * @param theaterList the theaterList to set
   */
  public void setTheaterList( DualListModel<CatalogTO> theaterList )
  {
    this.theaterList = theaterList;
  }

  /**
   * @return the regionTOs
   */
  public List<CatalogTO> getRegionTOs()
  {
    return regionTOs;
  }

  /**
   * @param regionTOs the regionTOs to set
   */
  public void setRegionTOs( List<CatalogTO> regionTOs )
  {
    this.regionTOs = regionTOs;
  }

  /**
   * @return the regionIdSelected
   */
  public Long getRegionIdSelected()
  {
    return regionIdSelected;
  }

  /**
   * @param regionIdSelected the regionIdSelected to set
   */
  public void setRegionIdSelected( Long regionIdSelected )
  {
    this.regionIdSelected = regionIdSelected;
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
}
