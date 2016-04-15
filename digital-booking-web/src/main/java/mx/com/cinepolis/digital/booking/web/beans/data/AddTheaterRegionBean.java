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
 * @author shernandezl
 */
@ManagedBean(name = "addTheaterRegionBean")
@ViewScoped
public class AddTheaterRegionBean extends BaseManagedBean implements Serializable
{
  private static final long serialVersionUID = 1396136409822981136L;
  private String addTerritoryName;
  private Long addTerritoryId;
  private List<CatalogTO> addTerritoryTOs;
  private PersonLazyDataModel personEmailDataModel;
  private PersonTO selectedPersonEmail;
  private PersonTO newPersonEmailTO;
  private PersonTO editPersonEmailTO;
  private String personEmail;
  private List<PersonTO> personEmailList;

  @EJB
  private ServiceAdminRegionIntegratorEJB serviceAdminRegionIntegratorEJB;

  /**
   * Método de carga inicial
   */
  @PostConstruct
  public void init()
  {
    this.addTerritoryTOs = this.serviceAdminRegionIntegratorEJB.getAllTerritories();
    this.personEmailDataModel = new PersonLazyDataModel();
    this.personEmailDataModel.setPersons( new ArrayList<PersonTO>() );
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
   * Método que limpia las variables utilizadas para agregar una persona.
   */
  public void addNewPerson()
  {
    this.newPersonEmailTO = new PersonTO();
    this.personEmail = "";
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
   * Método que guarda una region
   */
  public void saveTheaterRegion()
  {
    CatalogTO regionCatalogTO = new CatalogTO();
    super.fillSessionData( regionCatalogTO );
    regionCatalogTO.setName( addTerritoryName );
    CatalogTO territoryCatalogTO = new CatalogTO();
    super.fillSessionData( territoryCatalogTO );
    territoryCatalogTO.setId( addTerritoryId );
    RegionTO<CatalogTO, CatalogTO> regionTO = new RegionTO<CatalogTO, CatalogTO>( regionCatalogTO, territoryCatalogTO );
    super.fillSessionData( regionTO.getCatalogRegion() );
    super.fillSessionData( regionTO.getIdTerritory() );
    regionTO.setPersons( this.personEmailDataModel.getPersons() );

    serviceAdminRegionIntegratorEJB.saveRegion( regionTO );
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
   * Metodo que setea los datos del objeto seleccionado del grid.
   */
  public void seEdittPersonData()
  {
    editPersonEmailTO = this.selectedPersonEmail;
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
   * Método que se ejecuta al cerrar ventanas modales
   * 
   * @param event
   */
  public void closeModalWindow( CloseEvent event )
  {
    this.addNewPerson();
  }

  /**
   * @return the addTerritoryName
   */
  public String getAddTerritoryName()
  {
    return addTerritoryName;
  }

  /**
   * @param addTerritoryName the addTerritoryName to set
   */
  public void setAddTerritoryName( String addTerritoryName )
  {
    this.addTerritoryName = addTerritoryName;
  }

  /**
   * @return the addTerritoryId
   */
  public Long getAddTerritoryId()
  {
    return addTerritoryId;
  }

  /**
   * @param addTerritoryId the addTerritoryId to set
   */
  public void setAddTerritoryId( Long addTerritoryId )
  {
    this.addTerritoryId = addTerritoryId;
  }

  /**
   * @return the addTerritoryTOs
   */
  public List<CatalogTO> getAddTerritoryTOs()
  {
    return addTerritoryTOs;
  }

  /**
   * @param addTerritoryTOs the addTerritoryTOs to set
   */
  public void setAddTerritoryTOs( List<CatalogTO> addTerritoryTOs )
  {
    this.addTerritoryTOs = addTerritoryTOs;
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
   * @return the newPersonEmail
   */
  public PersonTO getNewPersonEmail()
  {
    return newPersonEmailTO;
  }

  /**
   * @param newPersonEmail the newPersonEmail to set
   */
  public void setNewPersonEmail( PersonTO newPersonEmail )
  {
    this.newPersonEmailTO = newPersonEmail;
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
   * @return the personEmailList
   */
  public List<PersonTO> getPersonEmailList()
  {
    return personEmailList;
  }

  /**
   * @param personEmailList the personEmailList to set
   */
  public void setPersonEmailList( List<PersonTO> personEmailList )
  {
    this.personEmailList = personEmailList;
  }

  /**
   * @return the personEmailDataModel
   */
  public PersonLazyDataModel getPersonEmailDataModel()
  {
    return personEmailDataModel;
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
   * @param personEmailDataModel the personEmailDataModel to set
   */
  public void setPersonEmailDataModel( PersonLazyDataModel personEmailDataModel )
  {
    this.personEmailDataModel = personEmailDataModel;
  }

}
