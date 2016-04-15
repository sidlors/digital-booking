package mx.com.cinepolis.digital.booking.web.beans.data;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;
import mx.com.cinepolis.digital.booking.web.beans.data.TheaterRegionsBean.PersonLazyDataModel;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;

/**
 * Bean encargado de la logica de actualizacion de regiones de cine
 * 
 * @author jreyesv
 */
@ManagedBean(name = "editTheaterRegionBean")
@ViewScoped
public class EditTheaterRegionBean extends BaseManagedBean implements Serializable
{
  private static final long serialVersionUID = 1396136409822981136L;
  private RegionTO<CatalogTO, CatalogTO> selectedTheaterRegion;
  private PersonLazyDataModel personEmailDataModel;
  private List<CatalogTO> editTerritoryTOs;
  private String editTerritoryName;
  private Long editTerritoryId;
  private String editTerritoryDsName;
  private PersonTO selectedPersonEmail;
  private PersonTO editPersonEmailTO;
  private PersonTO newPersonEmailTO;
  private String personEmail;

  @EJB
  private ServiceAdminRegionIntegratorEJB serviceAdminRegionIntegratorEJB;

  /**
   * Método que obtiene los datos de sesión para realizar la modificación
   */
  @SuppressWarnings("unchecked")
  @PostConstruct
  public void loadData()
  {
    selectedTheaterRegion = (RegionTO<CatalogTO, CatalogTO>) getSession().getAttribute( "selectedTheaterRegion" );
    if( selectedTheaterRegion != null )
    {
      this.personEmailDataModel = new PersonLazyDataModel();
      this.personEmailDataModel.setPersons( new ArrayList<PersonTO>() );

      editTerritoryName = selectedTheaterRegion.getCatalogRegion().getName();
      editTerritoryId = selectedTheaterRegion.getIdTerritory().getId();
      editTerritoryDsName = selectedTheaterRegion.getIdTerritory().getName();
      personEmailDataModel.setPersons( selectedTheaterRegion.getPersons() );
      editTerritoryTOs = serviceAdminRegionIntegratorEJB.getAllTerritories();

      getSession().removeAttribute( "selectedTheaterRegion" );
    }
  }


  /**
   * Metodo que setea los datos del objeto seleccionado del grid.
   */
  public void seEdittPersonData()
  {
    this.editPersonEmailTO = this.selectedPersonEmail;
    this.personEmail = getEmailSelectedPerson();
  }

  /**
   * Método que obtiene el email dela fila seleccionada del grid.
   * 
   * @return
   */
  private String getEmailSelectedPerson()
  {
    String email;
    if( CollectionUtils.isNotEmpty( this.selectedPersonEmail.getEmails() ) )
    {
      email = this.selectedPersonEmail.getEmails().get( 0 ).getName();
    }
    else
    {
      email = "";
    }
    return email;
  }

  /**
   * Método que valida que esté seleccionada una fila del grid.
   */
  public void validatePersonSelection()
  {
    if( this.selectedPersonEmail == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
      FacesContext fc = FacesContext.getCurrentInstance();
      fc.validationFailed();
    }
  }

  /**
   * Método que limpia las variables utilizadas para agregar una persona.
   */
  public void addNewPerson()
  {
    this.newPersonEmailTO = new PersonTO();
    this.personEmail = "";
  }

  /**
   * Método que se ejecuta al cerrar ventanas modales
   * 
   * @param event
   */
  public void closeModalWindow( CloseEvent event )
  {
    this.addNewPerson();
  }

  /**
   * Método que actualiza region seleccionada
   */
  public void updateTheaterRegion()
  {
    super.fillSessionData( selectedTheaterRegion.getCatalogRegion() );
    super.fillSessionData( selectedTheaterRegion.getIdTerritory() );
    selectedTheaterRegion.getCatalogRegion().setName( this.editTerritoryName );
    selectedTheaterRegion.setIdTerritory( new CatalogTO( this.editTerritoryId ) );
    for( PersonTO person : this.personEmailDataModel.getPersons() )
    {
      super.fillSessionData( person );
    }
    selectedTheaterRegion.setPersons( this.personEmailDataModel.getPersons() );
    serviceAdminRegionIntegratorEJB.updateRegion( selectedTheaterRegion );
  }

  /**
   * Metodo para direccionar a la pantalla theaterRegions
   * 
   * @throws IOException
   */
  public void goTheaterRegions() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( "theaterRegions.do" );
  }

  /**
   * Método que remueve un elemento de personEmailDataModel.
   */
  public void deletePersonFromDataModel()
  {
    String emailSearch = this.getEmailSelectedPerson();
    int i = 0;
    for( PersonTO personTO : this.personEmailDataModel.getPersons() )
    {
      if( CollectionUtils.isNotEmpty( personTO.getEmails() )
          && emailSearch.equals( personTO.getEmails().get( 0 ).getName() ) )
      {
        break;
      }
      i++;
    }
    this.personEmailDataModel.getPersons().remove( i );
  }

  /**
   * Método que agrega a la lista del grid de personas una nueva persona.
   */
  public void addPersonDataModel()
  {
    validateAddPerson();
    PersonTO person = this.newPersonEmailTO;
    CatalogTO email = new CatalogTO();
    email.setName( this.personEmail.trim() );
    person.setEmails( Arrays.asList( email ) );
    this.personEmailDataModel.getPersons().add( person );
  }

  /**
   * Mátodo que valida los datos obligatorios de un nuevo registro persona.
   */
  private void validateAddPerson()
  {
    String email = this.personEmail.trim();
    validateEmailExpression( email );
    if( CollectionUtils.isNotEmpty( personEmailDataModel.getPersons() ) )
    {
      for( PersonTO personTO : personEmailDataModel.getPersons() )
      {
        if( CollectionUtils.isNotEmpty( personTO.getEmails() )
            && email.equals( personTO.getEmails().get( 0 ).getName() ) )
        {
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.EMAIL_IS_REPEATED );
        }
      }
    }
  }

  /**
   * Método que valida el formato del correo electrónico
   * 
   * @param email
   */
  private void validateEmailExpression( String email )
  {
    String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    if( !email.matches( regex ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.EMAIL_DOES_NOT_COMPLIES_REGEX );
    }
  }

  /**
   * Método que edita los datos de la lista del grid
   */
  public void editPerson()
  {
    this.validateEditPerson();
    this.deletePersonFromDataModel();
    PersonTO person = this.editPersonEmailTO;
    CatalogTO email = null;
    if( CollectionUtils.isNotEmpty( person.getEmails() ) )
    {
      email = person.getEmails().get( 0 );
    }
    else
    {
      email = new CatalogTO();
    }

    email.setName( this.personEmail.trim() );
    person.setEmails( Arrays.asList( email ) );
    this.personEmailDataModel.getPersons().add( person );
  }

  /**
   * Método que valida los datos de la edición.
   */
  private void validateEditPerson()
  {
    String email = this.personEmail.trim();
    String selectedEmail = this.getEmailSelectedPerson();
    validateEmailExpression( email );
    if( CollectionUtils.isNotEmpty( personEmailDataModel.getPersons() ) )
    {
      for( PersonTO personTO : personEmailDataModel.getPersons() )
      {
        if( !email.equals( selectedEmail ) && CollectionUtils.isNotEmpty( personTO.getEmails() )
            && email.equals( personTO.getEmails().get( 0 ).getName() ) )
        {
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.EMAIL_IS_REPEATED );
        }
      }
    }
  }

  /**
   * @return the selectedTheaterRegion
   */
  public RegionTO<CatalogTO, CatalogTO> getSelectedTheaterRegion()
  {
    return selectedTheaterRegion;
  }

  /**
   * @param selectedTheaterRegion the selectedTheaterRegion to set
   */
  public void setSelectedTheaterRegion( RegionTO<CatalogTO, CatalogTO> selectedTheaterRegion )
  {
    this.selectedTheaterRegion = selectedTheaterRegion;
  }

  /**
   * @return the personEmailDataModel
   */
  public PersonLazyDataModel getPersonEmailDataModel()
  {
    return personEmailDataModel;
  }

  /**
   * @param personEmailDataModel the personEmailDataModel to set
   */
  public void setPersonEmailDataModel( PersonLazyDataModel personEmailDataModel )
  {
    this.personEmailDataModel = personEmailDataModel;
  }

  /**
   * @return the editTerritoryTOs
   */
  public List<CatalogTO> getEditTerritoryTOs()
  {
    return editTerritoryTOs;
  }

  /**
   * @param editTerritoryTOs the editTerritoryTOs to set
   */
  public void setEditTerritoryTOs( List<CatalogTO> editTerritoryTOs )
  {
    this.editTerritoryTOs = editTerritoryTOs;
  }

  /**
   * @return the editTerritoryName
   */
  public String getEditTerritoryName()
  {
    return editTerritoryName;
  }

  /**
   * @param editTerritoryName the editTerritoryName to set
   */
  public void setEditTerritoryName( String editTerritoryName )
  {
    this.editTerritoryName = editTerritoryName;
  }

  /**
   * @return the editTerritoryId
   */
  public Long getEditTerritoryId()
  {
    return editTerritoryId;
  }

  /**
   * @param editTerritoryId the editTerritoryId to set
   */
  public void setEditTerritoryId( Long editTerritoryId )
  {
    this.editTerritoryId = editTerritoryId;
  }

  /**
   * @return the selectedPersonEmail
   */
  public PersonTO getSelectedPersonEmail()
  {
    return selectedPersonEmail;
  }

  /**
   * @param selectedPersonEmail the selectedPersonEmail to set
   */
  public void setSelectedPersonEmail( PersonTO selectedPersonEmail )
  {
    this.selectedPersonEmail = selectedPersonEmail;
  }

  /**
   * @return the editPersonEmailTO
   */
  public PersonTO getEditPersonEmailTO()
  {
    return editPersonEmailTO;
  }

  /**
   * @param editPersonEmailTO the editPersonEmailTO to set
   */
  public void setEditPersonEmailTO( PersonTO editPersonEmailTO )
  {
    this.editPersonEmailTO = editPersonEmailTO;
  }

  /**
   * @return the personEmail
   */
  public String getPersonEmail()
  {
    return personEmail;
  }

  /**
   * @param personEmail the personEmail to set
   */
  public void setPersonEmail( String personEmail )
  {
    this.personEmail = personEmail;
  }

  /**
   * @return the newPersonEmailTO
   */
  public PersonTO getNewPersonEmailTO()
  {
    return newPersonEmailTO;
  }

  /**
   * @param newPersonEmailTO the newPersonEmailTO to set
   */
  public void setNewPersonEmailTO( PersonTO newPersonEmailTO )
  {
    this.newPersonEmailTO = newPersonEmailTO;
  }

  /**
   * @return the editTerritoryDsName
   */
  public String getEditTerritoryDsName()
  {
    return editTerritoryDsName;
  }

}
