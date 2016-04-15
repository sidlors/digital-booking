package mx.com.cinepolis.digital.booking.web.beans.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import mx.com.cinepolis.digital.booking.commons.query.CategoryQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.integration.category.ServiceAdminScreenFormatIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "screenFormatsBean")
@ViewScoped
public class ScreenFormatsBean extends BaseManagedBean implements Serializable
{

  private static final long serialVersionUID = 7887145763308631281L;

  private LazyDataModel<CatalogTO> screenFormatsList;
  private CatalogTO selectedScreenFormat;
  private String addedName;

  @EJB
  private ServiceAdminScreenFormatIntegratorEJB serviceAdminScreenFormatIntegratorEJB;

  @PostConstruct
  public void init()
  {
    screenFormatsList = new ScreenFormatLazyDataModel( serviceAdminScreenFormatIntegratorEJB, super.getUserId() );
  }

  /**
   * Método que elimina formato de sala seleccionado
   */
  public void deleteScreenFormat()
  {
    serviceAdminScreenFormatIntegratorEJB.deleteScreenFormat( getSelectedScreenFormatFromView() );
    this.selectedScreenFormat = null;
  }

  /**
   * Método que obtiene el registro seleccionado de la vista
   * 
   * @return CatalogTO
   */
  private CatalogTO getSelectedScreenFormatFromView()
  {
    CatalogTO screenFormat = new CatalogTO();
    super.fillSessionData( screenFormat );
    screenFormat.setId( selectedScreenFormat.getId() );
    screenFormat.setName( selectedScreenFormat.getName() );
    return screenFormat;
  }

  /**
   * Método que guarda un formato de la sala
   */
  public void saveScreenFormat()
  {
    CatalogTO screenFormat = new CatalogTO();
    super.fillSessionData( screenFormat );
    screenFormat.setName( addedName );
    serviceAdminScreenFormatIntegratorEJB.saveScreenFormat( screenFormat );
  }

  /**
   * Método que se ejecuta al cerrar una ventana
   * 
   * @param event
   */
  public void handleClose( CloseEvent event )
  {
    this.addedName = null;
  }

  /**
   * Método que actualiza el formato de sala seleccionado
   */
  public void updateScreenFormat()
  {
    serviceAdminScreenFormatIntegratorEJB.updateScreenFormat( getSelectedScreenFormatFromView() );
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   */
  public void validateSelection()
  {
    if( selectedScreenFormat == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
    }
  }

  /**
   * @return the screenFormatsList
   */
  public LazyDataModel<CatalogTO> getScreenFormatsList()
  {
    return screenFormatsList;
  }

  /**
   * @param screenFormatsList the screenFormatsList to set
   */
  public void setScreenFormatsList( LazyDataModel<CatalogTO> screenFormatsList )
  {
    this.screenFormatsList = screenFormatsList;
  }

  /**
   * @return the selectedScreenFormat
   */
  public CatalogTO getSelectedScreenFormat()
  {
    return selectedScreenFormat;
  }

  /**
   * @param selectedScreenFormat the selectedScreenFormat to set
   */
  public void setSelectedScreenFormat( CatalogTO selectedScreenFormat )
  {
    this.selectedScreenFormat = selectedScreenFormat;
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
   * Class for lazy loading of sound formats
   */
  static class ScreenFormatLazyDataModel extends LazyDataModel<CatalogTO>
  {

    private static final long serialVersionUID = 8213751342907749639L;
    private ServiceAdminScreenFormatIntegratorEJB serviceAdminScreenFormatIntegratorEJB;
    private Long userId;

    /**
     * Constructor by ServiceAdminSoundFormatIntegratorEJB
     * 
     * @param serviceAdminSoundFormatIntegratorEJB
     */
    public ScreenFormatLazyDataModel( ServiceAdminScreenFormatIntegratorEJB serviceAdminScreenFormatIntegratorEJB, Long userId )
    {
      this.serviceAdminScreenFormatIntegratorEJB = serviceAdminScreenFormatIntegratorEJB;
      this.userId = userId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<CatalogTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      int page = first / pageSize;
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( userId );
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( CategoryQuery.CATEGORY_NAME );
      pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
      pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_TYPE_ID, 3 );
      pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_ACTIVE, true );
      PagingResponseTO<CatalogTO> response = serviceAdminScreenFormatIntegratorEJB
          .getCatalogScreenFormatSumary( pagingRequestTO );
      List<CatalogTO> result = response.getElements();
      this.setRowCount( response.getTotalCount() );

      return result;
    }
  }
}
