package mx.com.cinepolis.digital.booking.web.beans.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import mx.com.cinepolis.digital.booking.commons.constants.IncomeSettingsType;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.TheaterQuery;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.commons.utils.RegionTOComparator;
import mx.com.cinepolis.digital.booking.commons.utils.ScreenTOComparator;
import mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.synchronize.ServiceDataSynchronizerIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.theater.ServiceAdminTheaterIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * @author dhernandez
 */
@ManagedBean(name = "theatersBean")
@ViewScoped
public class TheatersBean extends BaseManagedBean implements Serializable
{

  private static final long serialVersionUID = -2399455899402669253L;

  private TheaterLazyDataModel theaterTOs;
  private ScreenLazyDataModel screenTOs;
  private TheaterTO selectedTheater;
  private ScreenTO selectedScreen;

  private List<CatalogTO> territories;
  private List<RegionTO<CatalogTO, CatalogTO>> regions;
  private List<CatalogTO> countries;
  private List<StateTO<CatalogTO, Number>> states;
  private List<CatalogTO> cities;
  private List<ScreenTO> screens;
  private List<CatalogTO> movieFormats;
  private List<CatalogTO> soundFormats;
  private List<CatalogTO> screenFormats;

  private Long selectedRegion;
  private Long selectedTerritory;
  private Long selectedCountry;
  private Long selectedState;
  private Long selectedCity;
  private ScreenTO screen;
  private Integer screenNumber;
  private String idVistaTheater = "";
  private int nuTheater = 0;

  private String nameTheater;
  private String telephoneTheater;
  private Boolean fgActive;

  private List<Object> soundFormatsSelected;
  private List<Object> movieFormatsSelected;
  private Long screenFormatSelected;

  private String filterNameTheater;
  private Boolean filterFgActive;
  private List<CatalogTO> filterRegions;
  private Long selectedFilterRegion;
  private String email;
  private String emailCC;

  // jreyesv Cambios Income Settings
  private Double screenOccupancyIndicatorGreenValue;
  private Double screenOccupancyIndicatorYellow;
  private Double screenOccupancyIndicatorRed;
  private Double prewWeekIndicatorGreen;
  private Double prewWeekIndicatorYellow;
  private Double prewWeekIndicatorRed;
  private String semaphoreRed;
  private String semaphoreYellow;
  private String semaphoreGreen;
  private String indicatorScreenOccupancyLabel;
  private String indicatorChgPreWeekLabel;
  private static final String VALIDATE_PARAMETER_ERROR_TEXT = "theaters.incomeSettings.mesgerror.validateParameterErrorText";
  private static final String VALIDATE_PARAMETER_RULES_ERROR_TEXT = "theaters.incomeSettings.mesgerror.validateParameterRulesErrorText";
  private Language language;

  @EJB
  private ServiceAdminTheaterIntegratorEJB serviceAdminTheaterEJB;

  @EJB
  private ServiceAdminRegionIntegratorEJB serviceAdminRegionEJB;

  @EJB
  private ServiceDataSynchronizerIntegratorEJB dataSynchronizerIntegratorEJB;

  /**
   * Constructor default
   */
  @PostConstruct
  public void init()
  {
    AbstractTO abstractTO = new AbstractTO();
    super.fillSessionData( abstractTO );
    this.theaterTOs = new TheaterLazyDataModel( serviceAdminTheaterEJB, abstractTO.getUserId() );
    this.theaterTOs.setUserId( abstractTO.getUserId() );
    this.screenTOs = new ScreenLazyDataModel();

    this.movieFormats = this.serviceAdminTheaterEJB.getMovieFormats();
    this.soundFormats = this.serviceAdminTheaterEJB.getSoundFormats();
    this.screenFormats = this.serviceAdminTheaterEJB.getScreenFormats();
    this.territories = this.serviceAdminRegionEJB.getAllTerritories();
    this.countries = this.serviceAdminRegionEJB.getAllCountries();
    this.states = new ArrayList<StateTO<CatalogTO, Number>>();
    this.regions = new ArrayList<RegionTO<CatalogTO, CatalogTO>>();
    Collections.sort( this.regions, new RegionTOComparator( false ) );

    this.cities = this.serviceAdminRegionEJB.getAllCities();

    this.filterRegions = this.serviceAdminRegionEJB.findAllActiveRegions( abstractTO );

    // jreyesv Income Settings semaphore colors
    this.semaphoreGreen = "sem_vd";
    this.semaphoreYellow = "sem_am";
    this.semaphoreRed = "sem_rj";
    // jreyesv Income Settings types labels
    this.language = Language.ENGLISH;
    this.indicatorScreenOccupancyLabel = this.serviceAdminTheaterEJB.getIndicatorTypeById(
      IncomeSettingsType.SCREEN_OCCUPANCY.getId(), this.language ).getName();
    this.indicatorChgPreWeekLabel = this.serviceAdminTheaterEJB.getIndicatorTypeById(
      IncomeSettingsType.CHANGE_PREVIOUS_WEEK.getId(), this.language ).getName();
  }

  /**
   * Método que elimina Cine seleccionado
   */
  public void deleteTheater()
  {
    super.fillSessionData( selectedTheater );
    this.serviceAdminTheaterEJB.deleteTheater( selectedTheater );
    this.selectedTheater = null;
  }

  /**
   * @return the selectedDistributor
   */
  public TheaterTO getSelectedTheater()
  {
    return selectedTheater;
  }

  /**
   * @param selectedTheater the selectedDistributor to set
   */
  public void setSelectedTheater( TheaterTO selectedTheater )
  {
    this.selectedTheater = selectedTheater;
  }

  /**
   * Método que guarda un Cine
   */
  public void saveTheater()
  {
    if( this.validateAllParametersInRange() && this.validateRulesForIncomeSettings() )
    {
      if( this.selectedTheater == null )
      {
        TheaterTO theater = new TheaterTO();
        super.fillSessionData( theater );
        this.applyChanges( theater );
        this.serviceAdminTheaterEJB.saveTheater( theater );
      }
      else
      {
        TheaterTO theater = this.selectedTheater;
        super.fillSessionData( theater );
        this.applyChanges( theater );
        this.serviceAdminTheaterEJB.updateTheater( theater );
      }
    }
  }

  /**
   * Method that validates whether parameter values for income settings are valid.
   * 
   * @return isValid, with the validation result.
   */
  private boolean validateRulesForIncomeSettings()
  {
    boolean isValid = (Double.compare( this.extractIndicatorValue( this.screenOccupancyIndicatorGreenValue ),
      this.extractIndicatorValue( this.screenOccupancyIndicatorYellow ) ) >= 0);
    isValid = isValid
        && (Double.compare( this.extractIndicatorValue( this.screenOccupancyIndicatorYellow ),
          this.extractIndicatorValue( this.screenOccupancyIndicatorRed ) ) >= 0);
    isValid = isValid
        && (Double.compare( this.extractIndicatorValue( this.prewWeekIndicatorGreen ),
          this.extractIndicatorValue( this.prewWeekIndicatorYellow ) ) <= 0);
    isValid = isValid
        && (Double.compare( this.extractIndicatorValue( this.prewWeekIndicatorYellow ),
          this.extractIndicatorValue( this.prewWeekIndicatorRed ) ) <= 0);
    if( !isValid )
    {
      this.createMessageError( VALIDATE_PARAMETER_RULES_ERROR_TEXT );
      this.validationFail();
    }
    return isValid;
  }

  /**
   * Method that validates whether all income settings parameters are in range.
   * 
   * @return isValid, with the result of validation, true or false.
   */
  private boolean validateAllParametersInRange()
  {
    boolean isValid = true;
    if( this.isValueOutOfRange( this.screenOccupancyIndicatorGreenValue ) )
    {
      isValid = false;
    }
    if( this.isValueOutOfRange( this.screenOccupancyIndicatorGreenValue ) )
    {
      isValid = false;
    }
    if( this.isValueOutOfRange( this.screenOccupancyIndicatorYellow ) )
    {
      isValid = false;
    }
    if( this.isValueOutOfRange( this.screenOccupancyIndicatorRed ) )
    {
      isValid = false;
    }
    if( this.isValueOutOfRange( this.prewWeekIndicatorGreen ) )
    {
      isValid = false;
    }
    if( this.isValueOutOfRange( this.prewWeekIndicatorYellow ) )
    {
      isValid = false;
    }
    if( this.isValueOutOfRange( this.prewWeekIndicatorRed ) )
    {
      isValid = false;
    }
    if( !isValid )
    {
      this.createMessageError( VALIDATE_PARAMETER_ERROR_TEXT );
      this.validationFail();
    }
    return isValid;
  }

  /**
   * Method that sets the form values to a {@link mx.com.cinepolis.digital.booking.commons.to.TheaterTO} object.
   * 
   * @param theater
   */
  private void applyChanges( TheaterTO theater )
  {
    theater.setDsTelephone( this.telephoneTheater );
    theater.setDsCCEmail( this.emailCC );
    theater.setName( this.nameTheater );
    theater.setFgActive( this.fgActive );
    theater.setIdVista( this.idVistaTheater );
    theater.setRegion( new RegionTO<CatalogTO, CatalogTO>( new CatalogTO( this.selectedRegion ), new CatalogTO(
        this.selectedTerritory ) ) );
    theater.setState( new StateTO<CatalogTO, Integer>( new CatalogTO( this.selectedState ), this.selectedCountry
        .intValue() ) );
    if( this.selectedCity != null )
    {
      theater.setCity( new CatalogTO( this.selectedCity ) );
    }
    theater.setScreens( this.screenTOs.getTheaterTO().getScreens() );
    theater.setNuTheater( this.nuTheater );
    validateEmail();
    if( theater.getEmail() == null )
    {
      theater.setEmail( new CatalogTO() );
    }
    theater.getEmail().setName( this.email );
    // jreyesv Guardar la configuración de ingresos para la región.
    this.extractIncomeSettingsData( theater );
  }

  /**
   * Method that extracts the income settings to save or update.
   * 
   * @param regionTO with the region data to save.
   */
  private void extractIncomeSettingsData( TheaterTO theater )
  {
    if( CollectionUtils.isEmpty( theater.getIncomeSettingsList() ) )
    {
      this.extractIncomeSettingsDataToCreate( theater );
    }
    else
    {
      this.extractIncomeSettingsDataToUpdate( theater );
    }
  }

  /**
   * Method that extracts the income settings to save.
   * 
   * @param regionTO with the region data to save.
   */
  private void extractIncomeSettingsDataToCreate( TheaterTO theater )
  {
    List<IncomeSettingsTO> incomeSettingsList = new ArrayList<IncomeSettingsTO>();
    // Configuración para el indicador screen occupancy
    IncomeSettingsTO incomeSettingsTOScreenOccupancy = new IncomeSettingsTO();
    incomeSettingsTOScreenOccupancy.setIncomeSettingsType( new IncomeSettingsTypeTO(
        IncomeSettingsType.SCREEN_OCCUPANCY.getIdLong() ) );
    incomeSettingsTOScreenOccupancy.setGreenSemaphore( this
        .extractIndicatorValue( this.screenOccupancyIndicatorGreenValue ) );
    incomeSettingsTOScreenOccupancy.setYellowSemaphore( this
        .extractIndicatorValue( this.screenOccupancyIndicatorYellow ) );
    incomeSettingsTOScreenOccupancy.setRedSemaphore( this.extractIndicatorValue( this.screenOccupancyIndicatorRed ) );
    super.fillSessionData( incomeSettingsTOScreenOccupancy );
    incomeSettingsList.add( incomeSettingsTOScreenOccupancy );
    // Configuración para el indicador Chg/Prew Week
    IncomeSettingsTO incomeSettingsTOChgPrewWeek = new IncomeSettingsTO();
    incomeSettingsTOChgPrewWeek.setIncomeSettingsType( new IncomeSettingsTypeTO(
        IncomeSettingsType.CHANGE_PREVIOUS_WEEK.getIdLong() ) );
    incomeSettingsTOChgPrewWeek.setGreenSemaphore( this.extractIndicatorValue( this.prewWeekIndicatorGreen ) );
    incomeSettingsTOChgPrewWeek.setYellowSemaphore( this.extractIndicatorValue( this.prewWeekIndicatorYellow ) );
    incomeSettingsTOChgPrewWeek.setRedSemaphore( this.extractIndicatorValue( this.prewWeekIndicatorRed ) );
    super.fillSessionData( incomeSettingsTOChgPrewWeek );
    incomeSettingsList.add( incomeSettingsTOChgPrewWeek );
    theater.setIncomeSettingsList( incomeSettingsList );
  }

  /**
   * Method that extracts the income settings to update.
   * 
   * @param regionTO with the region data to save.
   */
  private void extractIncomeSettingsDataToUpdate( TheaterTO theater )
  {
    for( IncomeSettingsTO incomeSettingsTO : theater.getIncomeSettingsList() )
    {
      if( incomeSettingsTO.getIncomeSettingsType().getId().equals( IncomeSettingsType.SCREEN_OCCUPANCY.getIdLong() ) )
      {
        incomeSettingsTO.setGreenSemaphore( this.screenOccupancyIndicatorGreenValue );
        incomeSettingsTO.setYellowSemaphore( this.screenOccupancyIndicatorYellow );
        incomeSettingsTO.setRedSemaphore( this.screenOccupancyIndicatorRed );
      }
      else if( incomeSettingsTO.getIncomeSettingsType().getId()
          .equals( IncomeSettingsType.CHANGE_PREVIOUS_WEEK.getIdLong() ) )
      {
        incomeSettingsTO.setGreenSemaphore( this.prewWeekIndicatorGreen );
        incomeSettingsTO.setYellowSemaphore( this.prewWeekIndicatorYellow );
        incomeSettingsTO.setRedSemaphore( this.prewWeekIndicatorRed );
      }
    }
  }

  /**
   * Method that extracts the Integer value from a indicatorValue.
   * 
   * @param indicatorValue with the original Long value.
   * @return the extracted Float value.
   */
  private Double extractIndicatorValue( Double indicatorValue )
  {
    return (indicatorValue != null ? indicatorValue : 0.00);
  }

  /**
   * Método que valida el formato de los campos email, emailCC
   */
  private void validateEmail()
  {
    if( StringUtils.isNotBlank( this.email ) )
    {
      String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
      if( !this.email.matches( regex ) )
      {
        throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.EMAIL_DOES_NOT_COMPLIES_REGEX );
      }
    }

    if( StringUtils.isNotBlank( this.emailCC ) )
    {
      String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
      this.emailCC = this.emailCC.replaceAll( "\\s", "" );
      String[] emails = this.emailCC.split( "," );
      for( int i = 0; i < emails.length; i++ )
      {
        if( !emails[i].matches( regex ) )
        {
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.EMAIL_DOES_NOT_COMPLIES_REGEX );
        }
      }
    }

  }

  /**
   * Método que se ejecuta al cerrar una ventana
   * 
   * @param event
   */
  public void handleClose( CloseEvent event )
  {
    // this.newName = null;
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   */
  public void validateSelection()
  {
    if( selectedTheater == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
    }
  }

  /* Getters abd Setters */

  /**
   * @return the theaterTOs
   */
  public TheaterLazyDataModel getTheaterTOs()
  {
    return theaterTOs;
  }

  /**
   * @param theaterTOs the theaterTOs to set
   */
  public void setTheaterTOs( TheaterLazyDataModel theaterTOs )
  {
    this.theaterTOs = theaterTOs;
  }

  /**
   * @return the states
   */
  public List<StateTO<CatalogTO, Number>> getStates()
  {
    return states;
  }

  /**
   * @param states the states to set
   */
  public void setStates( List<StateTO<CatalogTO, Number>> states )
  {
    this.states = states;
  }

  /**
   * @return the selectedState
   */
  public Long getSelectedState()
  {
    return selectedState;
  }

  /**
   * @param selectedState the selectedState to set
   */
  public void setSelectedState( Long selectedState )
  {
    this.selectedState = selectedState;
  }

  /**
   * @return the cities
   */
  public List<CatalogTO> getCities()
  {
    return cities;
  }

  /**
   * @param cities the cities to set
   */
  public void setCities( List<CatalogTO> cities )
  {
    this.cities = cities;
  }

  /**
   * @return the selectedCity
   */
  public Long getSelectedCity()
  {
    return selectedCity;
  }

  /**
   * @param selectedCity the selectedCity to set
   */
  public void setSelectedCity( Long selectedCity )
  {
    this.selectedCity = selectedCity;
  }

  /**
   * @return the screens
   */
  public List<ScreenTO> getScreens()
  {
    return screens;
  }

  /**
   * @param screens the screens to set
   */
  public void setScreens( List<ScreenTO> screens )
  {
    this.screens = screens;
  }

  /**
   * @return the screen
   */
  public ScreenTO getScreen()
  {
    return screen;
  }

  /**
   * @param screen the screen to set
   */
  public void setScreen( ScreenTO screen )
  {
    this.screen = screen;
  }

  /**
   * @return the screenTOs
   */
  public LazyDataModel<ScreenTO> getScreenTOs()
  {
    return screenTOs;
  }

  /**
   * @param screenTOs the screenTOs to set
   */
  public void setScreenTOs( ScreenLazyDataModel screenTOs )
  {
    this.screenTOs = screenTOs;
  }

  /**
   * @return the selectedScreen
   */
  public ScreenTO getSelectedScreen()
  {
    return selectedScreen;
  }

  /**
   * @param selectedScreen the selectedScreen to set
   */
  public void setSelectedScreen( ScreenTO selectedScreen )
  {
    this.selectedScreen = selectedScreen;
  }

  /**
   * @return the regions
   */
  public List<RegionTO<CatalogTO, CatalogTO>> getRegions()
  {
    return regions;
  }

  /**
   * @param regions the regions to set
   */
  public void setRegions( List<RegionTO<CatalogTO, CatalogTO>> regions )
  {
    this.regions = regions;
  }

  /**
   * @return the selectedRegion
   */
  public Long getSelectedRegion()
  {
    return selectedRegion;
  }

  /**
   * @param selectedRegion the selectedRegion to set
   */
  public void setSelectedRegion( Long selectedRegion )
  {
    this.selectedRegion = selectedRegion;
  }

  /**
   * @return the movieFormats
   */
  public List<CatalogTO> getMovieFormats()
  {
    return movieFormats;
  }

  /**
   * @param movieFormats the movieFormats to set
   */
  public void setMovieFormats( List<CatalogTO> movieFormats )
  {
    this.movieFormats = movieFormats;
  }

  /**
   * @return the soundFormats
   */
  public List<CatalogTO> getSoundFormats()
  {
    return soundFormats;
  }

  /**
   * @param soundFormats the soundFormats to set
   */
  public void setSoundFormats( List<CatalogTO> soundFormats )
  {
    this.soundFormats = soundFormats;
  }

  /**
   * @return the nameTheater
   */
  public String getNameTheater()
  {
    return nameTheater;
  }

  /**
   * @param nameTheater the nameTheater to set
   */
  public void setNameTheater( String nameTheater )
  {
    this.nameTheater = nameTheater;
  }

  /**
   * @return the fgActive
   */
  public Boolean getFgActive()
  {
    return fgActive;
  }

  /**
   * @param fgActive the fgActive to set
   */
  public void setFgActive( Boolean fgActive )
  {
    this.fgActive = fgActive;
  }

  /**
   * @return the telephoneTheater
   */
  public String getTelephoneTheater()
  {
    return telephoneTheater;
  }

  /**
   * @param telephoneTheater the telephoneTheater to set
   */
  public void setTelephoneTheater( String telephoneTheater )
  {
    this.telephoneTheater = telephoneTheater;
  }

  /**
   * @return the territories
   */
  public List<CatalogTO> getTerritories()
  {
    return territories;
  }

  /**
   * @param territories the territories to set
   */
  public void setTerritories( List<CatalogTO> territories )
  {
    this.territories = territories;
  }

  /**
   * @return the countries
   */
  public List<CatalogTO> getCountries()
  {
    return countries;
  }

  /**
   * @param countries the countries to set
   */
  public void setCountries( List<CatalogTO> countries )
  {
    this.countries = countries;
  }

  /**
   * @return the selectedTerritory
   */
  public Long getSelectedTerritory()
  {
    return selectedTerritory;
  }

  /**
   * @param selectedTerritory the selectedTerritory to set
   */
  public void setSelectedTerritory( Long selectedTerritory )
  {
    this.selectedTerritory = selectedTerritory;
  }

  /**
   * @return the selectedCountry
   */
  public Long getSelectedCountry()
  {
    return selectedCountry;
  }

  /**
   * @param selectedCountry the selectedCountry to set
   */
  public void setSelectedCountry( Long selectedCountry )
  {
    this.selectedCountry = selectedCountry;
  }

  /**
   * @return the soundFormatsSelected
   */
  public List<Object> getSoundFormatsSelected()
  {
    return soundFormatsSelected;
  }

  /**
   * @param soundFormatsSelected the soundFormatsSelected to set
   */
  public void setSoundFormatsSelected( List<Object> soundFormatsSelected )
  {
    this.soundFormatsSelected = soundFormatsSelected;
  }

  /**
   * @return the movieFormatsSelected
   */
  public List<Object> getMovieFormatsSelected()
  {
    return movieFormatsSelected;
  }

  /**
   * @param movieFormatsSelected the movieFormatsSelected to set
   */
  public void setMovieFormatsSelected( List<Object> movieFormatsSelected )
  {
    this.movieFormatsSelected = movieFormatsSelected;
  }

  /**
   * @return the idVistaTheater
   */
  public String getIdVistaTheater()
  {
    return idVistaTheater;
  }

  /**
   * @param idVistaTheater the idVistaTheater to set
   */
  public void setIdVistaTheater( String idVistaTheater )
  {
    this.idVistaTheater = idVistaTheater;
  }

  /**
   * @return the filterNameTheater
   */
  public String getFilterNameTheater()
  {
    return filterNameTheater;
  }

  /**
   * @param filterNameTheater the filterNameTheater to set
   */
  public void setFilterNameTheater( String filterNameTheater )
  {
    this.filterNameTheater = filterNameTheater;
  }

  /**
   * @return the selectedFilterRegion
   */
  public Long getSelectedFilterRegion()
  {
    return selectedFilterRegion;
  }

  /**
   * @param selectedFilterRegion the selectedFilterRegion to set
   */
  public void setSelectedFilterRegion( Long selectedFilterRegion )
  {
    this.selectedFilterRegion = selectedFilterRegion;
  }

  /**
   * @return the screenFormats
   */
  public List<CatalogTO> getScreenFormats()
  {
    return screenFormats;
  }

  /**
   * @param screenFormats the screenFormats to set
   */
  public void setScreenFormats( List<CatalogTO> screenFormats )
  {
    this.screenFormats = screenFormats;
  }

  /**
   * @return the screenFormatSelected
   */
  public Long getScreenFormatSelected()
  {
    return screenFormatSelected;
  }

  /**
   * @param screenFormatSelected the screenFormatSelected to set
   */
  public void setScreenFormatSelected( Long screenFormatSelected )
  {
    this.screenFormatSelected = screenFormatSelected;
  }

  /**
   * @return the email
   */
  public String getEmail()
  {
    return email;
  }

  /**
   * @param email the email to set
   */
  public void setEmail( String email )
  {
    this.email = email;
  }

  /**
   * @return the emailCC
   */
  public String getEmailCC()
  {
    return emailCC;
  }

  /**
   * @param emailCC the emailCC to set
   */
  public void setEmailCC( String emailCC )
  {
    this.emailCC = emailCC;
  }

  /**
   * @return the nuTheater
   */
  public int getNuTheater()
  {
    return nuTheater;
  }

  /**
   * @param nuTheater the nuTheater to set
   */
  public void setNuTheater( int nuTheater )
  {
    this.nuTheater = nuTheater;
  }

  /**
   * Method that loads the available regions for the logged user, to fill the combo region.
   * 
   * @param event, with the event information.
   */
  public void loadRegions( AjaxBehaviorEvent event )
  {
    Long idTerritory = (Long) event.getComponent().getAttributes().get( "value" );
    CatalogTO territory = new CatalogTO( idTerritory );
    super.fillSessionData( territory );
    this.setRegions( this.serviceAdminRegionEJB.getAllRegionsByTerritory( territory ) );
  }

  /**
   * Method that loads the available states for the logged user, to fill the combo region.
   * 
   * @param event, with the event information.
   */
  public void loadStates( AjaxBehaviorEvent event )
  {
    Long idCountry = (Long) event.getComponent().getAttributes().get( "value" );
    CatalogTO country = new CatalogTO( idCountry );
    super.fillSessionData( country );
    this.setStates( this.serviceAdminRegionEJB.getAllStatesByCountry( country ) );
  }

  /**
   * Method that loads the available states for the logged user, to fill the combo region.
   * 
   * @param event, with the event information.
   */
  public void loadCities( AjaxBehaviorEvent event )
  {
    Long idState = (Long) event.getComponent().getAttributes().get( "value" );
    this.cities = this.serviceAdminRegionEJB.getAllCitiesByState( idState );
  }

  /**
   * Method that reset values for add theaters form.
   * 
   * @param event, with the event information.
   */
  public void handleCloseAddTheater( CloseEvent event )
  {
    this.nameTheater = null;
    this.fgActive = true;
    this.telephoneTheater = null;
    this.selectedCity = null;;
    this.selectedCountry = null;
    this.selectedRegion = null;
    this.selectedState = null;
    this.selectedTerritory = null;
    this.states = new ArrayList<StateTO<CatalogTO, Number>>();
    this.regions = new ArrayList<RegionTO<CatalogTO, CatalogTO>>();
    this.screenTOs = new ScreenLazyDataModel();
    this.emailCC = null;
    // Income Settings
    this.screenOccupancyIndicatorGreenValue = null;
    this.screenOccupancyIndicatorYellow = null;
    this.screenOccupancyIndicatorRed = null;
    this.prewWeekIndicatorGreen = null;
    this.prewWeekIndicatorYellow = null;
    this.prewWeekIndicatorRed = null;
  }

  /**
   * Method that reset values for add theaters form.
   */
  public void addNewTheater()
  {
    this.nameTheater = null;
    this.telephoneTheater = null;
    this.selectedCity = null;
    this.selectedCountry = null;
    this.selectedRegion = null;
    this.selectedState = null;
    this.selectedTerritory = null;
    this.screenTOs = new ScreenLazyDataModel();
    this.screenTOs.setTheaterTO( new TheaterTO() );
    this.screenTOs.getTheaterTO().setScreens( new ArrayList<ScreenTO>() );
    this.selectedTheater = null;
    this.idVistaTheater = "";
    this.email = null;
    this.emailCC = null;
    this.nuTheater = 0;
    this.cities = null;
    // Income Settings
    this.screenOccupancyIndicatorGreenValue = null;
    this.screenOccupancyIndicatorYellow = null;
    this.screenOccupancyIndicatorRed = null;
    this.prewWeekIndicatorGreen = null;
    this.prewWeekIndicatorYellow = null;
    this.prewWeekIndicatorRed = null;
  }

  /**
   * Method that reset values for add screens form.
   */
  public void addNewScreen()
  {
    this.screen = new ScreenTO();
    this.soundFormatsSelected = new ArrayList<Object>();
    this.movieFormatsSelected = new ArrayList<Object>();
    this.screenFormatSelected = null;
    this.screenNumber = null;
  }

  /**
   * Method that reset values for add screens form.
   * 
   * @param event, with the event information.
   */
  public void handleCloseAddScreen( CloseEvent event )
  {
    this.addNewScreen();
  }

  /**
   * Method that reset values for edit screens form.
   * 
   * @param event, with the event information.
   */
  public void handleCloseEditScreen( CloseEvent event )
  {
    this.screenNumber = null;
    this.addNewScreen();
  }

  /**
   * Adds a new screen into the screenTOs of the bean
   */
  public void addScreen()
  {
    validateNewScreen();
    this.screen.setSoundFormats( new ArrayList<CatalogTO>() );
    this.screen.setMovieFormats( new ArrayList<CatalogTO>() );
    this.screen.setScreenFormat( new CatalogTO( this.screenFormatSelected ) );
    extractScreenFormat();
    extractSoundFormats();
    extractMovieFormats();
    if( this.screenNumber == null )
    {
      this.screenTOs.getTheaterTO().getScreens().add( this.screen );
    }
    else
    {
      for( ScreenTO screenTO : this.screenTOs.getTheaterTO().getScreens() )
      {
        if( screenTO.getNuScreen().equals( this.screenNumber ) )
        {
          screenTO.setNuScreen( this.screen.getNuScreen() );
          screenTO.setNuCapacity( this.screen.getNuCapacity() );
          screenTO.setSoundFormats( this.screen.getSoundFormats() );
          screenTO.setMovieFormats( this.screen.getMovieFormats() );
          screenTO.setScreenFormat( this.screen.getScreenFormat() );
          screenTO.setIdVista( this.screen.getIdVista() );
        }
      }
    }
  }

  private void extractScreenFormat()
  {
    for( CatalogTO catalogTO : this.screenFormats )
    {
      if( catalogTO.getId().equals( this.screenFormatSelected ) )
      {
        this.screen.setScreenFormat( catalogTO );
      }
    }
  }

  private void extractMovieFormats()
  {
    for( CatalogTO movieFormat : this.movieFormats )
    {

      for( Object o : this.movieFormatsSelected )
      {
        if( o.toString().equals( movieFormat.getId().toString() ) )
        {
          this.screen.getMovieFormats().add( movieFormat );
        }
      }
    }
  }

  private void extractSoundFormats()
  {
    for( CatalogTO soundFormat : this.soundFormats )
    {
      for( Object o : this.soundFormatsSelected )
      {
        if( o.toString().equals( soundFormat.getId().toString() ) )
        {
          this.screen.getSoundFormats().add( soundFormat );
        }
      }
    }
  }

  private void validateNewScreen()
  {
    validateScreen();
    if( CollectionUtils.isEmpty( this.soundFormatsSelected ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.SCREEN_NEEDS_AT_LEAST_ONE_SOUND_FORMAT );
    }
    if( CollectionUtils.isEmpty( this.movieFormatsSelected ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.SCREEN_NEEDS_AT_LEAST_ONE_MOVIE_FORMAT );
    }
    if( this.screenFormatSelected == null || this.screenFormatSelected.longValue() == 0 )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.SCREEN_NEEDS_SCREEN_FORMAT );
    }

  }

  private void validateScreen()
  {
    if( CollectionUtils.isNotEmpty( this.screenTOs.getTheaterTO().getScreens() ) )
    {
      for( ScreenTO screenTO : this.screenTOs.getTheaterTO().getScreens() )
      {
        if( screenTO.getNuScreen().equals( this.screen.getNuScreen() ) && this.screenNumber == null )
        {
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.SCREEN_NUMBER_ALREADY_EXISTS );
        }
        if( screenTO.getNuScreen().equals( this.screen.getNuScreen() ) && this.screenNumber != null
            && !this.screenNumber.equals( this.screen.getNuScreen() ) )
        {
          throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.SCREEN_NUMBER_ALREADY_EXISTS );
        }
      }
    }
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   */
  public void validateScreenSelection()
  {
    if( selectedScreen == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
      FacesContext fc = FacesContext.getCurrentInstance();
      fc.validationFailed();
    }

  }

  /**
   * Sets the screen data
   */
  public void setScreenData()
  {
    this.screen = new ScreenTO();
    this.screen.setNuCapacity( this.selectedScreen.getNuCapacity() );
    this.screen.setNuScreen( this.selectedScreen.getNuScreen() );
    this.screenNumber = this.screen.getNuScreen();
    this.screen.setIdVista( this.selectedScreen.getIdVista() );
    this.screen.setScreenFormat( this.selectedScreen.getScreenFormat() );
    if( this.selectedScreen.getScreenFormat() != null )
    {
      this.screenFormatSelected = this.selectedScreen.getScreenFormat().getId();
    }

    this.soundFormatsSelected = new ArrayList<Object>();
    movieFormatsSelected = new ArrayList<Object>();
    for( CatalogTO to : this.selectedScreen.getSoundFormats() )
    {
      soundFormatsSelected.add( to.getId().toString() );
    }
    for( CatalogTO to : this.selectedScreen.getMovieFormats() )
    {
      movieFormatsSelected.add( to.getId().toString() );
    }

  }

  /**
   * Deletes the selected screen
   */
  public void deleteScreen()
  {
    if( this.getSelectedScreen() != null )
    {
      int index = 0;
      boolean found = false;
      for( ScreenTO screenTO : this.screenTOs.getTheaterTO().getScreens() )
      {
        if( screenTO.getNuScreen().equals( this.getSelectedScreen().getNuScreen() ) )
        {
          found = true;
          break;
        }
        index++;
      }
      if( found )
      {
        this.screenTOs.getTheaterTO().getScreens().remove( index );
      }
    }
  }

  /**
   * Method that extracts the data from selected theater.
   */
  public void setTheaterData()
  {
    this.nuTheater = this.selectedTheater.getNuTheater();
    this.idVistaTheater = this.selectedTheater.getIdVista();
    this.nameTheater = this.selectedTheater.getName();
    this.telephoneTheater = this.selectedTheater.getDsTelephone();
    this.emailCC = this.selectedTheater.getDsCCEmail();
    this.selectedCity = this.selectedTheater.getCity().getId();
    this.selectedCountry = this.selectedTheater.getState().getCatalogCountry().longValue();
    this.selectedRegion = this.selectedTheater.getRegion().getCatalogRegion().getId();
    this.selectedState = this.selectedTheater.getState().getCatalogState().getId();
    this.selectedTerritory = this.selectedTheater.getRegion().getIdTerritory().getId();
    this.fgActive = this.selectedTheater.isFgActive();

    CatalogTO country = new CatalogTO( this.selectedTheater.getState().getCatalogCountry().longValue() );
    super.fillSessionData( country );
    this.states = this.serviceAdminRegionEJB.getAllStatesByCountry( country );
    this.cities = this.serviceAdminRegionEJB.getAllCitiesByState( this.selectedState );
    CatalogTO territory = this.selectedTheater.getRegion().getIdTerritory();
    super.fillSessionData( territory );
    this.regions = this.serviceAdminRegionEJB.getAllRegionsByTerritory( territory );
    this.screenTOs = new ScreenLazyDataModel();
    this.screenTOs.setTheaterTO( this.selectedTheater );
    if( this.selectedTheater.getEmail() != null )
    {
      this.email = this.selectedTheater.getEmail().getName();
    }
    else
    {
      this.email = "";
    }
    // Load income settings data.
    this.loadIncomeSettingsData( this.selectedTheater );
  }

  /**
   * Method that loads the income settings data for a region.
   * 
   * @param theaterTO with the theater data to load.
   */
  private void loadIncomeSettingsData( TheaterTO theaterTO )
  {
    if( CollectionUtils.isNotEmpty( theaterTO.getIncomeSettingsList() ) )
    {
      for( IncomeSettingsTO incomeSettingsTO : theaterTO.getIncomeSettingsList() )
      {
        if( incomeSettingsTO.getIncomeSettingsType().getId().equals( IncomeSettingsType.SCREEN_OCCUPANCY.getIdLong() ) )
        {
          this.screenOccupancyIndicatorGreenValue = incomeSettingsTO.getGreenSemaphore();
          this.screenOccupancyIndicatorYellow = incomeSettingsTO.getYellowSemaphore();
          this.screenOccupancyIndicatorRed = incomeSettingsTO.getRedSemaphore();
        }
        else if( incomeSettingsTO.getIncomeSettingsType().getId()
            .equals( IncomeSettingsType.CHANGE_PREVIOUS_WEEK.getIdLong() ) )
        {
          this.prewWeekIndicatorGreen = incomeSettingsTO.getGreenSemaphore();
          this.prewWeekIndicatorYellow = incomeSettingsTO.getYellowSemaphore();
          this.prewWeekIndicatorRed = incomeSettingsTO.getRedSemaphore();
        }
      }
    }
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   */
  public void validateTheaterSelection()
  {
    if( selectedTheater == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
    }
  }

  /**
   * Class for lazy loading
   * 
   * @author gsegura
   * @since 0.0.1
   */
  static class TheaterLazyDataModel extends LazyDataModel<TheaterTO>
  {

    private static final long serialVersionUID = 8799735020640802539L;
    private ServiceAdminTheaterIntegratorEJB serviceAdminTheaterEJB;

    private String theaterName;
    private Long regionId;
    private Long userId;
    private Boolean fgActive;

    /**
     * Constructtor by {@link mx.com.cinepolis.digital.booking.integration.theater.ServiceAdminTheaterIntegratorEJB}
     * 
     * @param serviceAdminTheaterEJB
     * @param userId
     */
    public TheaterLazyDataModel( ServiceAdminTheaterIntegratorEJB serviceAdminTheaterEJB, Long userId )
    {
      this.serviceAdminTheaterEJB = serviceAdminTheaterEJB;
      this.userId = userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TheaterTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      int page = first / pageSize;
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( userId );
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( TheaterQuery.REGION_NAME );
      pagingRequestTO.getSort().add( TheaterQuery.THEATER_NAME );
      pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
      if( this.regionId != null )
      {
        pagingRequestTO.getFilters().put( TheaterQuery.ID_REGION, this.regionId );
      }
      if( StringUtils.isNotBlank( this.theaterName ) )
      {
        pagingRequestTO.getFilters().put( TheaterQuery.THEATER_NAME, this.theaterName.trim() );
      }
      if(this.fgActive != null && this.fgActive.equals( true ))
      {
        pagingRequestTO.getFilters().put( TheaterQuery.THEATER_ACTIVE, this.fgActive );
      }

      PagingResponseTO<TheaterTO> response = serviceAdminTheaterEJB.getCatalogTheaterSummary( pagingRequestTO );
      this.setRowCount( response.getTotalCount() );
      return response.getElements();
    }

    /**
     * @return the theaterName
     */
    public String getTheaterName()
    {
      return theaterName;
    }

    /**
     * @param theaterName the theaterName to set
     */
    public void setTheaterName( String theaterName )
    {
      this.theaterName = theaterName;
    }

    /**
     * @return the regionId
     */
    public Long getRegionId()
    {
      return regionId;
    }

    /**
     * @param regionId the regionId to set
     */
    public void setRegionId( Long regionId )
    {
      this.regionId = regionId;
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

    /**
     * @return the fgActive
     */
    public Boolean getFgActive()
    {
      return fgActive;
    }

    /**
     * @param fgActive the fgActive to set
     */
    public void setFgActive( Boolean fgActive )
    {
      this.fgActive = fgActive;
    }

  }

  /**
   * @return the filterRegions
   */
  public List<CatalogTO> getFilterRegions()
  {
    return filterRegions;
  }

  /**
   * @return the filterFgActive
   */
  public Boolean getFilterFgActive()
  {
    return filterFgActive;
  }

  /**
   * @param filterFgActive the filterFgActive to set
   */
  public void setFilterFgActive( Boolean filterFgActive )
  {
    this.filterFgActive = filterFgActive;
  }

  /**
   * @param filterRegions the filterRegions to set
   */
  public void setFilterRegions( List<CatalogTO> filterRegions )
  {
    this.filterRegions = filterRegions;
  }

  /**
   * Class for lazy loading of screens
   * 
   * @author gsegura
   * @since 0.0.1
   */
  static class ScreenLazyDataModel extends LazyDataModel<ScreenTO>
  {
    private static final long serialVersionUID = -7050746880830084035L;
    private TheaterTO theaterTO;

    /**
     * @return the theaterTO
     */
    public TheaterTO getTheaterTO()
    {
      return theaterTO;
    }

    /**
     * @param theaterTO the theaterTO to set
     */
    public void setTheaterTO( TheaterTO theaterTO )
    {
      this.theaterTO = theaterTO;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScreenTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      List<ScreenTO> screens = new ArrayList<ScreenTO>();
      this.setRowCount( 0 );
      if( this.theaterTO != null && CollectionUtils.isNotEmpty( this.theaterTO.getScreens() ) )
      {
        Collections.sort( this.theaterTO.getScreens(), new ScreenTOComparator() );
        for( int i = first; i < this.theaterTO.getScreens().size(); i++ )
        {
          screens.add( this.theaterTO.getScreens().get( i ) );
        }
        this.setRowCount( this.theaterTO.getScreens().size() );
      }

      return screens;
    }

  }

  /**
   * Method that extracts the names of screens.
   * 
   * @param screens, a list of {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO} objects.
   * @return a {@link java.lang.String} object the screens names.
   */
  public String getScreensNamesTheater( List<ScreenTO> screens )
  {
    Collections.sort( screens, new ScreenTOComparator() );
    StringBuilder sb = new StringBuilder();
    if( CollectionUtils.isNotEmpty( screens ) )
    {
      int n = 0;
      for( ScreenTO to : screens )
      {
        sb.append( to.getNuScreen() );
        if( n < screens.size() - 1 )
        {
          sb.append( ", " );
        }
        n++;
      }

    }
    return sb.toString();
  }

  /**
   * @return the theaterHeader
   */
  public String getTheaterHeader()
  {
    FacesContext fc = FacesContext.getCurrentInstance();
    ResourceBundle msg = fc.getApplication().evaluateExpressionGet( fc, "#{msg}", ResourceBundle.class );

    String theaterHeader = "";
    if( this.selectedTheater == null )
    {
      theaterHeader = msg.getString( "digitalbooking.theaters.administration.addTheaterWindowTitle" );
    }
    else
    {
      theaterHeader = msg.getString( "digitalbooking.theaters.administration.editTheaterWindowTitle" );
    }

    return theaterHeader;
  }

  /**
   * Method that resets the filter values.
   */
  public void resetFilters()
  {
    theaterTOs.setRegionId( null );
    theaterTOs.setTheaterName( null );
    theaterTOs.setFgActive( null );
    filterNameTheater = null;
    selectedFilterRegion = null;
    filterFgActive = null;
  }

  /**
   * Method that sets the filter values for a search.
   */
  public void searchFilters()
  {
    theaterTOs.setTheaterName( filterNameTheater );
    if( selectedFilterRegion != null && selectedFilterRegion.longValue() > 0 )
    {
      theaterTOs.setRegionId( selectedFilterRegion );
    }
    if(filterFgActive != null )
    {
      theaterTOs.setFgActive( filterFgActive );
    }
  }

  /**
   * Method that handles the toggle filters action.
   * 
   * @param event, with the information event.
   */
  public void handleToggleFilters( ToggleEvent event )
  {
    resetFilters();
  }

  /**
   * Método que sincroniza los catálogos de distribuidores, películas y cines.
   */
  public void synchronizeWithView()
  {
    dataSynchronizerIntegratorEJB.synchronizeTheaters( Language.SPANISH );
    dataSynchronizerIntegratorEJB.synchronizeTheaters( Language.ENGLISH );
  }

  // jreyesv Income Settings
  /**
   * @return the semaphoreRed
   */
  public String getSemaphoreRed()
  {
    return semaphoreRed;
  }

  /**
   * @param semaphoreRed the semaphoreRed to set
   */
  public void setSemaphoreRed( String semaphoreRed )
  {
    this.semaphoreRed = semaphoreRed;
  }

  /**
   * @return the semaphoreYellow
   */
  public String getSemaphoreYellow()
  {
    return semaphoreYellow;
  }

  /**
   * @param semaphoreYellow the semaphoreYellow to set
   */
  public void setSemaphoreYellow( String semaphoreYellow )
  {
    this.semaphoreYellow = semaphoreYellow;
  }

  /**
   * @return the semaphoreGreen
   */
  public String getSemaphoreGreen()
  {
    return semaphoreGreen;
  }

  /**
   * @param semaphoreGreen the semaphoreGreen to set
   */
  public void setSemaphoreGreen( String semaphoreGreen )
  {
    this.semaphoreGreen = semaphoreGreen;
  }

  /**
   * @return the screenOccupancyIndicatorGreenValue
   */
  public Double getScreenOccupancyIndicatorGreenValue()
  {
    return screenOccupancyIndicatorGreenValue;
  }

  /**
   * @param screenOccupancyIndicatorGreenValue the screenOccupancyIndicatorGreenValue to set
   */
  public void setScreenOccupancyIndicatorGreenValue( Double screenOccupancyIndicatorGreenValue )
  {
    this.screenOccupancyIndicatorGreenValue = screenOccupancyIndicatorGreenValue;
  }

  /**
   * @return the screenOccupancyIndicatorYellow
   */
  public Double getScreenOccupancyIndicatorYellow()
  {
    return screenOccupancyIndicatorYellow;
  }

  /**
   * @param screenOccupancyIndicatorYellow the screenOccupancyIndicatorYellow to set
   */
  public void setScreenOccupancyIndicatorYellow( Double screenOccupancyIndicatorYellow )
  {
    this.screenOccupancyIndicatorYellow = screenOccupancyIndicatorYellow;
  }

  /**
   * @return the screenOccupancyIndicatorRed
   */
  public Double getScreenOccupancyIndicatorRed()
  {
    return screenOccupancyIndicatorRed;
  }

  /**
   * @param screenOccupancyIndicatorRed the screenOccupancyIndicatorRed to set
   */
  public void setScreenOccupancyIndicatorRed( Double screenOccupancyIndicatorRed )
  {
    this.screenOccupancyIndicatorRed = screenOccupancyIndicatorRed;
  }

  /**
   * @return the prewWeekIndicatorGreen
   */
  public Double getPrewWeekIndicatorGreen()
  {
    return prewWeekIndicatorGreen;
  }

  /**
   * @param prewWeekIndicatorGreen the prewWeekIndicatorGreen to set
   */
  public void setPrewWeekIndicatorGreen( Double prewWeekIndicatorGreen )
  {
    this.prewWeekIndicatorGreen = prewWeekIndicatorGreen;
  }

  /**
   * @return the prewWeekIndicatorYellow
   */
  public Double getPrewWeekIndicatorYellow()
  {
    return prewWeekIndicatorYellow;
  }

  /**
   * @param prewWeekIndicatorYellow the prewWeekIndicatorYellow to set
   */
  public void setPrewWeekIndicatorYellow( Double prewWeekIndicatorYellow )
  {
    this.prewWeekIndicatorYellow = prewWeekIndicatorYellow;
  }

  /**
   * @return the prewWeekIndicatorRed
   */
  public Double getPrewWeekIndicatorRed()
  {
    return prewWeekIndicatorRed;
  }

  /**
   * @param prewWeekIndicatorRed the prewWeekIndicatorRed to set
   */
  public void setPrewWeekIndicatorRed( Double prewWeekIndicatorRed )
  {
    this.prewWeekIndicatorRed = prewWeekIndicatorRed;
  }

  /**
   * @return the indicatorScreenOccupancyLabel
   */
  public String getIndicatorScreenOccupancyLabel()
  {
    return indicatorScreenOccupancyLabel;
  }

  /**
   * @return the indicatorChgPreWeekLabel
   */
  public String getIndicatorChgPreWeekLabel()
  {
    return indicatorChgPreWeekLabel;
  }

  /**
   * Method that validates whether the parameter is in range.
   * 
   * @param parameterValue
   * @return The result of validation, true or false.
   */
  private boolean isValueOutOfRange( Double parameterValue )
  {
    parameterValue = (parameterValue != null ? parameterValue : Double.valueOf( 0 ));
    return (Double.compare( Double.valueOf( 0 ), parameterValue ) > 0 || Double.compare( parameterValue,
      Double.valueOf( 100 ) ) > 0);
  }
}
