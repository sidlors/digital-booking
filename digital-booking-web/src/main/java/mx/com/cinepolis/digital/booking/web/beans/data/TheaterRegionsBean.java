package mx.com.cinepolis.digital.booking.web.beans.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.RegionQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.utils.PersonTOComparator;
import mx.com.cinepolis.digital.booking.integration.region.ServiceAdminRegionIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.collections.CollectionUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
/**
 * 
 * @author 
 *
 */
@ManagedBean(name = "theaterRegionsBean")
@ViewScoped
public class TheaterRegionsBean extends BaseManagedBean
{

  private static final long serialVersionUID = -851576056834266947L;
  private LazyDataModel<RegionTO<CatalogTO, CatalogTO>> theaterRegionsList;
  private PersonLazyDataModel personList;
  private RegionTO<CatalogTO, CatalogTO> selectedTheaterRegion;
  private String addedName;
  private CatalogTO territoryTO;
  private Long territoryId;
  private String editName;
  private Long editTerritoryId;
  private List<CatalogTO> territoryTOs;
  private PersonTO selectedPerson;
  private PersonTO personDialog;
  private String personEmailDialog;

  @EJB
  private ServiceAdminRegionIntegratorEJB serviceAdminRegionIntegratorEJB;

  @PostConstruct
  public void init()
  {
    theaterRegionsList = new TheaterRegionLazyDataModel( serviceAdminRegionIntegratorEJB, super.getUserId() );
    territoryTOs = serviceAdminRegionIntegratorEJB.getAllTerritories();
    this.personList = new PersonLazyDataModel();
    this.personList.setPersons( new ArrayList<PersonTO>() );
  }

  /**
   * Método que elimina region seleccionada
   */
  public void deleteTheaterRegion()
  {
    super.fillSessionData( selectedTheaterRegion.getCatalogRegion() );
    super.fillSessionData( selectedTheaterRegion.getIdTerritory() );
    serviceAdminRegionIntegratorEJB.deleteRegion( selectedTheaterRegion );
    this.selectedTheaterRegion = null;
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo **
   * @throws IOException 
   */
  public void validateSelection() throws IOException
  {
    this.personList.setPersons( new ArrayList<PersonTO>() );
    if( getSelectedTheaterRegion() == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
    }
    else
    {
      getSession().setAttribute( "selectedTheaterRegion", getSelectedTheaterRegion() );
      
      FacesContext ctx = FacesContext.getCurrentInstance();
      ctx.getExternalContext().redirect( "editTheaterRegion.do" );
    }
  }
  
  /**
   * Método que valida que el objeto seleccionado no sea nulo
   * @throws IOException 
   */
  public void validateSelectionForDelete() throws IOException
  {
    this.personList.setPersons( new ArrayList<PersonTO>() );
    if( getSelectedTheaterRegion() == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
    }
    else
    {
      this.editName = selectedTheaterRegion.getCatalogRegion().getName();
      this.editTerritoryId = selectedTheaterRegion.getIdTerritory().getId();
      this.personList.setPersons( selectedTheaterRegion.getPersons() );
    }
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
   * Metodo para direccionar a la pantalla addTheaterRegion
   * 
   * @throws IOException
   */
  public void goAddTheaterRegion() throws IOException
  {
    FacesContext ctx = FacesContext.getCurrentInstance();
    ctx.getExternalContext().redirect( "addTheaterRegion.do" );
  }

  /**
   * Método que obtiene todas las regiones
   * @return
   */
  public List<CatalogTO> getAllTerritories()
  {
    return serviceAdminRegionIntegratorEJB.getAllTerritories();
  }

  /**
   * @return the theaterRegionsList
   */
  public LazyDataModel<RegionTO<CatalogTO, CatalogTO>> getTheaterRegionsList()
  {
    return theaterRegionsList;
  }

  /**
   * @param theaterRegionsList the theaterRegionsList to set
   */
  public void setTheaterRegionsList( LazyDataModel<RegionTO<CatalogTO, CatalogTO>> theaterRegionsList )
  {
    this.theaterRegionsList = theaterRegionsList;
  }

  /**
   * @return the personList
   */
  public PersonLazyDataModel getPersonList()
  {
    return personList;
  }

  /**
   * @param personList the personList to set
   */
  public void setPersonList( PersonLazyDataModel personList )
  {
    this.personList = personList;
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
   * @return the addedName
   */
  public String getAddedName()
  {
    return addedName;
  }

  /**
   * @param addedName the addedName to set
   */
  public void setAddedName( String addedName )
  {
    this.addedName = addedName;
  }

  /**
   * @return the territoryTO
   */
  public CatalogTO getTerritoryTO()
  {
    return territoryTO;
  }

  /**
   * @param territoryTO the territoryTO to set
   */
  public void setTerritoryTO( CatalogTO territoryTO )
  {
    this.territoryTO = territoryTO;
  }

  /**
   * @return the territoryId
   */
  public Long getTerritoryId()
  {
    return territoryId;
  }

  /**
   * @param territoryId the territoryId to set
   */
  public void setTerritoryId( Long territoryId )
  {
    this.territoryId = territoryId;
  }

  /**
   * @return the territoryTOs
   */
  public List<CatalogTO> getTerritoryTOs()
  {
    return territoryTOs;
  }

  /**
   * @param territoryTOs the territoryTOs to set
   */
  public void setTerritoryTOs( List<CatalogTO> territoryTOs )
  {
    this.territoryTOs = territoryTOs;
  }

  /**
   * @return the selectedPerson
   */
  public PersonTO getSelectedPerson()
  {
    return selectedPerson;
  }

  /**
   * @param selectedPerson the selectedPerson to set
   */
  public void setSelectedPerson( PersonTO selectedPerson )
  {
    this.selectedPerson = selectedPerson;
  }

  /**
   * @return the personDialog
   */
  public PersonTO getPersonDialog()
  {
    return personDialog;
  }

  /**
   * @param personDialog the personDialog to set
   */
  public void setPersonDialog( PersonTO personDialog )
  {
    this.personDialog = personDialog;
  }

  /**
   * @return the personEmailDialog
   */
  public String getPersonEmailDialog()
  {
    return personEmailDialog;
  }

  /**
   * @param personEmailDialog the personEmailDialog to set
   */
  public void setPersonEmailDialog( String personEmailDialog )
  {
    this.personEmailDialog = personEmailDialog;
  }

  /**
   * @return the editName
   */
  public String getEditName()
  {
    return editName;
  }

  /**
   * @param editName the editName to set
   */
  public void setEditName( String editName )
  {
    this.editName = editName;
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
   * Clase que se encarga de consultar todas las regiones asociadas 
   * al usuario logueado y las carga a un LazyDataModel.
   *
   */
  static class TheaterRegionLazyDataModel extends LazyDataModel<RegionTO<CatalogTO, CatalogTO>>
  {
    private static final long serialVersionUID = -5238994506674137022L;

    private ServiceAdminRegionIntegratorEJB serviceAdminRegionIntegratorEJB;
    private Long userId;

    /**
     * Constructor by ServiceAdminRegionIntegratorEJB
     * 
     * @param serviceAdminRegionIntegratorEJB
     * @param userId
     */
    public TheaterRegionLazyDataModel( ServiceAdminRegionIntegratorEJB serviceAdminRegionIntegratorEJB, Long userId )
    {
      this.serviceAdminRegionIntegratorEJB = serviceAdminRegionIntegratorEJB;
      this.userId = userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RegionTO<CatalogTO, CatalogTO>> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      int page = first / pageSize;
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( userId );
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( RegionQuery.REGION_NAME );
      pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
      pagingRequestTO.getFilters().put( RegionQuery.REGION_ACTIVE, true );
      PagingResponseTO<RegionTO<CatalogTO, CatalogTO>> response = serviceAdminRegionIntegratorEJB
          .getCatalogRegionSummary( pagingRequestTO );
      List<RegionTO<CatalogTO, CatalogTO>> result = response.getElements();
      this.setRowCount( response.getTotalCount() );
      return result;
    }
  }
  
  /**
   * Clasee que carga los datos del grid de regiones.
   *
   */
  static class PersonLazyDataModel extends LazyDataModel<PersonTO>
  {

    private static final long serialVersionUID = -467582341974757592L;
    private List<PersonTO> persons;

    /**
     * @return the persons
     */
    public List<PersonTO> getPersons()
    {
      return persons;
    }

    /**
     * @param persons the persons to set
     */
    public void setPersons( List<PersonTO> persons )
    {
      this.persons = persons;
    }
    
    /**
     * Método que consulta las regiones y carga los datos en la lista de tipo LazyDataModel
     */
    @Override
    public List<PersonTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      List<PersonTO> personsFiltered = new ArrayList<PersonTO>();
      this.setRowCount( 0 );
      if( CollectionUtils.isNotEmpty( this.persons ) )
      {
        Collections.sort( this.persons, new PersonTOComparator() );
        for( int i = first; i < this.persons.size(); i++ )
        {
          personsFiltered.add( this.persons.get( i ) );
        }
        this.setRowCount( personsFiltered.size() );
      }
      return personsFiltered;
    }

  }
}
