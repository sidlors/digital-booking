package mx.com.cinepolis.digital.booking.web.beans.data;

import java.io.Serializable;
import java.util.ArrayList;
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

import mx.com.cinepolis.digital.booking.commons.query.CityQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.CityTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.integration.city.ServiceCityIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.lang.StringUtils;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * Cities catalog controller
 * 
 * @author jreyesv
 */
@ManagedBean(name = "citiesBean")
@ViewScoped
public class CitiesBean extends BaseManagedBean implements Serializable
{

  /**
   * Serialization variable
   */
  private static final long serialVersionUID = 1267032251245901431L;

  /**
   * Constants
   */
  private static final String STRING_TITLE_ADD_CITY = "digitalbooking.cities.administration.addCityWindowTitle";
  private static final String STRING_TITLE_EDIT_CITY = "digitalbooking.cities.administration.editCityWindowTitle";

  /**
   * Cities integrator service
   */
  @EJB
  private ServiceCityIntegratorEJB serviceCityIntegratorEJB;

  /**
   * Region integrator service
   */
  @EJB
  private ServiceAdminRegionIntegratorEJB serviceAdminRegionEJB;

  /***
   * 
   */
  private CityLazyDatamodel cities;

  /**
   * Countries list
   */
  private List<CatalogTO> countryTOList;

  /**
   * States list filter
   */
  private List<StateTO<CatalogTO, Number>> stateTOListFilter;

  /**
   * States list
   */
  private List<StateTO<CatalogTO, Number>> stateTOList;

  /**
   * City selected
   */
  private CityTO citySelected;

  /**
   * Filters
   */
  private String cityNameFilter;
  private String liquidationIdFilter;
  private Long countryIdFilter;
  private Long stateIdFilter;

  /**
   * Data city
   */
  private Long cityId;
  private String cityName;
  private String liquidationId;
  private Long countryId;
  private Long stateId;

  /**
   * Default constructor
   */
  @PostConstruct
  public void init()
  {
    this.cities = new CityLazyDatamodel( serviceCityIntegratorEJB, super.getUserId() );
    this.countryTOList = this.serviceCityIntegratorEJB.findAllActiveCountries();
    this.setFilters();
  }

  /**
   * Method that loads the available states for the logged user, to fill the combo region.
   * 
   * @param event, with the event information.
   */
  public void loadStatesFilter( AjaxBehaviorEvent event )
  {
    if(this.countryIdFilter != null && this.countryIdFilter > 0 )
    {
      Long idCountry = (Long) event.getComponent().getAttributes().get( "value" );
      this.stateTOListFilter = this.loadStatesForCombo( idCountry );
    }
    else 
    {
      this.countryIdFilter = null;
      this.stateIdFilter = null;
    }
  }

  /**
   * Method that consults the available states of a country selected.
   * @param idCountry
   * @return a list with available states
   */
  private List<StateTO<CatalogTO, Number>> loadStatesForCombo( Long idCountry )
  {
    CatalogTO country = new CatalogTO( idCountry );
    super.fillSessionData( country );
    return this.serviceAdminRegionEJB.getAllStatesByCountry( country );
  }

  /**
   * Method that loads the available states for the logged user, to fill the combo region.
   * 
   * @param event, with the event information.
   */
  public void loadStates( AjaxBehaviorEvent event )
  {
    Long idCountry = (Long) event.getComponent().getAttributes().get( "value" );
    this.stateTOList = this.loadStatesForCombo( idCountry );
  }

  /**
   * Method that resets the filters variables.
   */
  public void resetFilters()
  {
    this.cityNameFilter = null;
    this.liquidationIdFilter = null;
    this.countryIdFilter = null;
    this.stateIdFilter = null;
    this.stateTOListFilter = null;
    this.setFilters();
  }

  /**
   * Method that sets the filters variables.
   */
  public void setFilters()
  {
    this.cities.setCityNameFilter( this.cityNameFilter );
    this.cities.setLiquidationIdFilter( this.liquidationIdFilter );
    this.cities.setCountryIdFilter( this.countryIdFilter );
    this.cities.setStateIdFilter( this.stateIdFilter );
  }

  /**
   * Method that logically deletes the selected city.
   */
  public void deleteMovie()
  {
    super.fillSessionData( this.citySelected );
    this.serviceCityIntegratorEJB.deleteCity( this.citySelected );
  }

  /**
   * Method that validates whether "citySelected" variable is not null.
   */
  public void validateSelection()
  {
    if( this.citySelected == null )
    {
      super.validationFail();
    }
  }

  /**
   * Method that saves or updates a city.
   */
  public void saveCity()
  {
    this.setCityDataToSave();
    super.fillSessionData( this.citySelected );
    if( this.citySelected.getId() == null )
    {
      this.serviceCityIntegratorEJB.saveCity( this.citySelected );
    }
    else
    {
      this.serviceCityIntegratorEJB.updateCity( this.citySelected );
    }
  }

  /**
   * Method that validates whether "citySelected" variable is not null.
   */
  private boolean validateCity()
  {
    return (this.citySelected != null);
  }

  /**
   * Method that resets the city data.
   */
  public void resetCityData()
  {
    this.cityId = null;
    this.cityName = null;
    this.liquidationId = null;
    this.countryId = null;
    this.stateId = null;
    this.stateTOList = null;
  }

  /**
   * Method that sets the city data for save or update.
   */
  public void setCityData()
  {
    if( this.validateCity() )
    {
      this.cityId = this.citySelected.getId();
      this.cityName = this.citySelected.getName();
      this.liquidationId = this.citySelected.getIdVista();
      this.countryId = this.citySelected.getCountry().getId();
      this.stateTOList = this.loadStatesForCombo( this.countryId );
      if( this.citySelected.getState() != null )
      {
        this.stateId = this.citySelected.getState().getCatalogState().getId();
      }
    }
    else
    {
      this.resetCityData();
    }
  }

  /**
   * Method that sets the city data to save or update.
   */
  private void setCityDataToSave()
  {
    if( !this.validateCity() )
    {
      this.citySelected = new CityTO();
    }
    this.citySelected.setId( this.cityId );
    this.citySelected.setName( this.cityName );
    this.citySelected.setIdVista( this.liquidationId );
    this.citySelected.setCountry( new CatalogTO( this.countryId ) );
    this.citySelected.setState( new StateTO<CatalogTO, Number>( new CatalogTO( this.stateId ), this.countryId ) );
  }

  /**
   * Method that gets the corresponding title for add/edit modal.
   * 
   * @return the cityHeader
   */
  public String getCityHeader()
  {
    FacesContext fc = FacesContext.getCurrentInstance();
    ResourceBundle msg = fc.getApplication().evaluateExpressionGet( fc, "#{msg}", ResourceBundle.class );
    String cityHeader = "";
    if( this.validateCity() )
    {
      cityHeader = msg.getString( STRING_TITLE_EDIT_CITY );
    }
    else
    {
      cityHeader = msg.getString( STRING_TITLE_ADD_CITY );
    }
    return cityHeader;
  }

  /**
   * @return the cities
   */
  public CityLazyDatamodel getCities()
  {
    return cities;
  }

  /**
   * @param cities the cities to set
   */
  public void setCities( CityLazyDatamodel cities )
  {
    this.cities = cities;
  }

  /**
   * @return the countryTOList
   */
  public List<CatalogTO> getCountryTOList()
  {
    return countryTOList;
  }

  /**
   * @param countryTOList the countryTOList to set
   */
  public void setCountryTOList( List<CatalogTO> countryTOList )
  {
    this.countryTOList = countryTOList;
  }

  /**
   * @return the stateTOList
   */
  public List<StateTO<CatalogTO, Number>> getStateTOList()
  {
    return stateTOList;
  }

  /**
   * @param stateTOList the stateTOList to set
   */
  public void setStateTOList( List<StateTO<CatalogTO, Number>> stateTOList )
  {
    this.stateTOList = stateTOList;
  }

  /**
   * @return the stateTOListFilter
   */
  public List<StateTO<CatalogTO, Number>> getStateTOListFilter()
  {
    return stateTOListFilter;
  }

  /**
   * @param stateTOListFilter the stateTOListFilter to set
   */
  public void setStateTOListFilter( List<StateTO<CatalogTO, Number>> stateTOListFilter )
  {
    this.stateTOListFilter = stateTOListFilter;
  }

  /**
   * @return the citySelected
   */
  public CityTO getCitySelected()
  {
    return citySelected;
  }

  /**
   * @param citySelected the citySelected to set
   */
  public void setCitySelected( CityTO citySelected )
  {
    this.citySelected = citySelected;
  }

  /**
   * @return the cityNameFilter
   */
  public String getCityNameFilter()
  {
    return cityNameFilter;
  }

  /**
   * @param cityNameFilter the cityNameFilter to set
   */
  public void setCityNameFilter( String cityNameFilter )
  {
    this.cityNameFilter = cityNameFilter;
  }

  /**
   * @return the liquidationIdFilter
   */
  public String getLiquidationIdFilter()
  {
    return liquidationIdFilter;
  }

  /**
   * @param liquidationIdFilter the liquidationIdFilter to set
   */
  public void setLiquidationIdFilter( String liquidationIdFilter )
  {
    this.liquidationIdFilter = liquidationIdFilter;
  }

  /**
   * @return the countryIdFilter
   */
  public Long getCountryIdFilter()
  {
    return countryIdFilter;
  }

  /**
   * @param countryIdFilter the countryIdFilter to set
   */
  public void setCountryIdFilter( Long countryIdFilter )
  {
    this.countryIdFilter = countryIdFilter;
  }

  /**
   * @return the stateIdFilter
   */
  public Long getStateIdFilter()
  {
    return stateIdFilter;
  }

  /**
   * @param stateIdFilter the stateIdFilter to set
   */
  public void setStateIdFilter( Long stateIdFilter )
  {
    this.stateIdFilter = stateIdFilter;
  }

  public void handleToggleFilters( ToggleEvent event )
  {
    this.resetFilters();
  }

  /**
   * @return the cityName
   */
  public String getCityName()
  {
    return cityName;
  }

  /**
   * @param cityName the cityName to set
   */
  public void setCityName( String cityName )
  {
    this.cityName = cityName;
  }

  /**
   * @return the liquidationId
   */
  public String getLiquidationId()
  {
    return liquidationId;
  }

  /**
   * @param liquidationId the liquidationId to set
   */
  public void setLiquidationId( String liquidationId )
  {
    this.liquidationId = liquidationId;
  }

  /**
   * @return the countryId
   */
  public Long getCountryId()
  {
    return countryId;
  }

  /**
   * @param countryId the countryId to set
   */
  public void setCountryId( Long countryId )
  {
    this.countryId = countryId;
  }

  /**
   * @return the stateId
   */
  public Long getStateId()
  {
    return stateId;
  }

  /**
   * @param stateId the stateId to set
   */
  public void setStateId( Long stateId )
  {
    this.stateId = stateId;
  }

  static class CityLazyDatamodel extends LazyDataModel<CityTO>
  {

    /**
     * Serial version
     */
    private static final long serialVersionUID = -5388499097950977600L;

    /**
     * Filters
     */
    private String cityNameFilter;
    private String liquidationIdFilter;
    private Long countryIdFilter;
    private Long stateIdFilter;
    /**
     * User identifier
     */
    private Long userId;

    /**
     * Service integrator
     */
    private ServiceCityIntegratorEJB serviceCityIntegratorEJB;

    public CityLazyDatamodel( ServiceCityIntegratorEJB serviceCityIntegratorEJB, Long userId )
    {
      this.serviceCityIntegratorEJB = serviceCityIntegratorEJB;
      this.userId = userId;
    }

    @Override
    public List<CityTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      int page = first / pageSize;
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( userId );
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( CityQuery.CITY_ID_VISTA );
      pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
      pagingRequestTO.getFilters().put( CityQuery.CITY_ACTIVE, true );

      this.setFiltersToRequest( pagingRequestTO );
      PagingResponseTO<CityTO> response = this.serviceCityIntegratorEJB.findAllCitiesByPaging( pagingRequestTO );
      this.setRowCount( response.getTotalCount() );
      return response.getElements();

    }

    private void setFiltersToRequest( PagingRequestTO pagingRequestTO )
    {
      if( StringUtils.isNotBlank( this.cityNameFilter ) )
      {
        pagingRequestTO.getFilters().put( CityQuery.CITY_NAME, this.cityNameFilter );
      }
      if( StringUtils.isNotBlank( this.liquidationIdFilter ) )
      {
        pagingRequestTO.getFilters().put( CityQuery.CITY_ID_VISTA, this.liquidationIdFilter );
      }
      if( this.countryIdFilter != null && countryIdFilter.longValue() > 0 )
      {
        pagingRequestTO.getFilters().put( CityQuery.COUNTRY_ID, this.countryIdFilter );
      }
      if( this.stateIdFilter != null && stateIdFilter.longValue() > 0 )
      {
        pagingRequestTO.getFilters().put( CityQuery.STATE_ID, this.stateIdFilter );
      }
    }

    /**
     * @return the cityNameFilter
     */
    public String getCityNameFilter()
    {
      return cityNameFilter;
    }

    /**
     * @param cityNameFilter the cityNameFilter to set
     */
    public void setCityNameFilter( String cityNameFilter )
    {
      this.cityNameFilter = cityNameFilter;
    }

    /**
     * @return the liquidationIdFilter
     */
    public String getLiquidationIdFilter()
    {
      return liquidationIdFilter;
    }

    /**
     * @param liquidationIdFilter the liquidationIdFilter to set
     */
    public void setLiquidationIdFilter( String liquidationIdFilter )
    {
      this.liquidationIdFilter = liquidationIdFilter;
    }

    /**
     * @return the countryIdFilter
     */
    public Long getCountryIdFilter()
    {
      return countryIdFilter;
    }

    /**
     * @param countryIdFilter the countryIdFilter to set
     */
    public void setCountryIdFilter( Long countryIdFilter )
    {
      this.countryIdFilter = countryIdFilter;
    }

    /**
     * @return the stateIdFilter
     */
    public Long getStateIdFilter()
    {
      return stateIdFilter;
    }

    /**
     * @param stateIdFilter the stateIdFilter to set
     */
    public void setStateIdFilter( Long stateIdFilter )
    {
      this.stateIdFilter = stateIdFilter;
    }

  }

}