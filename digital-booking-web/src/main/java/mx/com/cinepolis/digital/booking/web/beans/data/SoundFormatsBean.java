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
import mx.com.cinepolis.digital.booking.integration.category.ServiceAdminSoundFormatIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

@ManagedBean(name = "soundFormatsBean")
@ViewScoped
public class SoundFormatsBean extends BaseManagedBean implements Serializable
{

  private static final long serialVersionUID = -4663845045475699832L;
  private LazyDataModel<CatalogTO> soundFormatsList;
  private CatalogTO selectedSoundFormat;
  private String addedName;

  @EJB
  private ServiceAdminSoundFormatIntegratorEJB serviceAdminSoundFormatIntegratorEJB;

  /**
   * Método que se ejecuta despues de la construcción del bean
   */
  @PostConstruct
  public void init()
  {
    soundFormatsList = new SoundFormatLazyDataModel( serviceAdminSoundFormatIntegratorEJB, getUserId() );
  }

  /**
   * Método que elimina formato de sonido seleccionado
   */
  public void deleteSoundFormat()
  {
    serviceAdminSoundFormatIntegratorEJB.deleteSoundFormat( getSelectedSoundFormatFromView() );
    this.selectedSoundFormat = null;
  }

  /**
   * Método que obtiene el registro seleccionado de la vista
   * 
   * @return CatalogTO
   */
  private CatalogTO getSelectedSoundFormatFromView()
  {
    CatalogTO soundFormat = new CatalogTO();
    super.fillSessionData( soundFormat );
    soundFormat.setId( selectedSoundFormat.getId() );
    soundFormat.setName( selectedSoundFormat.getName() );
    return soundFormat;
  }

  /**
   * Método que guarda un formato de sonido
   */
  public void saveSoundFormat()
  {
    CatalogTO soundFormat = new CatalogTO();
    super.fillSessionData( soundFormat );
    soundFormat.setName( addedName );
    serviceAdminSoundFormatIntegratorEJB.saveSoundFormat( soundFormat );
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
  public void updateSoundFormat()
  {
    serviceAdminSoundFormatIntegratorEJB.updateSoundFormat( getSelectedSoundFormatFromView() );
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   */
  public void validateSelection()
  {
    if( selectedSoundFormat == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
    }
  }

  /**
   * @return the soundFormatsList
   */
  public LazyDataModel<CatalogTO> getSoundFormatsList()
  {
    return soundFormatsList;
  }

  /**
   * @param soundFormatsList the soundFormatsList to set
   */
  public void setSoundFormatsList( LazyDataModel<CatalogTO> soundFormatsList )
  {
    this.soundFormatsList = soundFormatsList;
  }

  /**
   * @return the selectedSoundFormat
   */
  public CatalogTO getSelectedSoundFormat()
  {
    return selectedSoundFormat;
  }

  /**
   * @param selectedSoundFormat the selectedSoundFormat to set
   */
  public void setSelectedSoundFormat( CatalogTO selectedSoundFormat )
  {
    this.selectedSoundFormat = selectedSoundFormat;
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
   * 
   * @author gsegura
   * @since 0.0.1
   */
  static class SoundFormatLazyDataModel extends LazyDataModel<CatalogTO>
  {

    private static final long serialVersionUID = 8213751342907749639L;
    private ServiceAdminSoundFormatIntegratorEJB serviceAdminSoundFormatIntegratorEJB;
    private Long userId;

    /**
     * Constructor by ServiceAdminSoundFormatIntegratorEJB
     * 
     * @param serviceAdminSoundFormatIntegratorEJB
     * @param long1 
     */
    public SoundFormatLazyDataModel( ServiceAdminSoundFormatIntegratorEJB serviceAdminSoundFormatIntegratorEJB, Long userId )
    {
      this.serviceAdminSoundFormatIntegratorEJB = serviceAdminSoundFormatIntegratorEJB;
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
      pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_TYPE_ID, 1 );
      pagingRequestTO.getFilters().put( CategoryQuery.CATEGORY_ACTIVE, true );
      PagingResponseTO<CatalogTO> response = serviceAdminSoundFormatIntegratorEJB
          .getCatalogSoundFormatSumary( pagingRequestTO );
      List<CatalogTO> result = response.getElements();
      this.setRowCount( response.getTotalCount() );
      return result;
    }
  }

}
