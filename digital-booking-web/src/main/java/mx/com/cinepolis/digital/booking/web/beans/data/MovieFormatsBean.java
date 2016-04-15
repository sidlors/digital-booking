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
import mx.com.cinepolis.digital.booking.integration.category.ServiceAdminMovieFormatIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "movieFormatsBean")
@ViewScoped
public class MovieFormatsBean extends BaseManagedBean implements Serializable
{

  private static final long serialVersionUID = 2918318920118534149L;
  private LazyDataModel<CatalogTO> movieFormatsList;
  private CatalogTO selectedMovieFormat;
  private String addedName;

  @EJB
  private ServiceAdminMovieFormatIntegratorEJB serviceAdminMovieFormatIntegratorEJB;

  /**
   * Método que se ejecuta despues de la construcción del bean
   */
  @PostConstruct
  public void init()
  {
    movieFormatsList = new MovieFormatLazyDataModel( serviceAdminMovieFormatIntegratorEJB, super.getUserId() );
  }

  /**
   * Método que elimina formato de sonido seleccionado
   */
  public void deleteMovieFormat()
  {
    serviceAdminMovieFormatIntegratorEJB.deleteMovieFormat( getSelectedMovieFormatFromView() );
    this.selectedMovieFormat = null;
  }

  /**
   * Método que obtiene el registro seleccionado de la vista
   * 
   * @return CatalogTO
   */
  private CatalogTO getSelectedMovieFormatFromView()
  {
    CatalogTO movieFormat = new CatalogTO();
    super.fillSessionData( movieFormat );
    movieFormat.setId( selectedMovieFormat.getId() );
    movieFormat.setName( selectedMovieFormat.getName() );
    return movieFormat;
  }

  /**
   * Método que guarda un formato de sonido
   */
  public void saveMovieFormat()
  {
    CatalogTO movieFormat = new CatalogTO();
    super.fillSessionData( movieFormat );
    movieFormat.setName( addedName );
    serviceAdminMovieFormatIntegratorEJB.saveMovieFormat( movieFormat );
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
   * Método que actualiza distribuidor seleccionado
   */
  public void updateMovieFormat()
  {
    serviceAdminMovieFormatIntegratorEJB.updateMovieFormat( getSelectedMovieFormatFromView() );
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   */
  public void validateSelection()
  {
    if( selectedMovieFormat == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
    }
  }

  /**
   * @return the movieFormatsList
   */
  public LazyDataModel<CatalogTO> getMovieFormatsList()
  {
    return movieFormatsList;
  }

  /**
   * @param movieFormatsList the movieFormatsList to set
   */
  public void setMovieFormatsList( LazyDataModel<CatalogTO> movieFormatsList )
  {
    this.movieFormatsList = movieFormatsList;
  }

  /**
   * @return the selectedMovieFormat
   */
  public CatalogTO getSelectedMovieFormat()
  {
    return selectedMovieFormat;
  }

  /**
   * @param selectedMovieFormat the selectedMovieFormat to set
   */
  public void setSelectedMovieFormat( CatalogTO selectedMovieFormat )
  {
    this.selectedMovieFormat = selectedMovieFormat;
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
   * Class for lazy loading of movie formats
   * 
   * @author gsegura
   * @since 0.0.1
   */
  static class MovieFormatLazyDataModel extends LazyDataModel<CatalogTO>
  {

    private static final long serialVersionUID = 8632783455219046506L;
    private ServiceAdminMovieFormatIntegratorEJB serviceAdminMovieFormatIntegratorEJB;
    private Long userId;

    /**
     * Constructor by ServiceAdminMovieFormatIntegratorEJB
     * 
     * @param serviceAdminMovieFormatIntegratorEJB
     */
    public MovieFormatLazyDataModel( ServiceAdminMovieFormatIntegratorEJB serviceAdminMovieFormatIntegratorEJB, Long userId )
    {
      this.serviceAdminMovieFormatIntegratorEJB = serviceAdminMovieFormatIntegratorEJB;
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
      pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_TYPE_ID, 2 );
      pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_ACTIVE, true );

      PagingResponseTO<CatalogTO> response = serviceAdminMovieFormatIntegratorEJB
          .getCatalogMovieFormatSumary( pagingRequestTO );
      List<CatalogTO> result = response.getElements();
      this.setRowCount( response.getTotalCount() );
      return result;
    }
  }

}
