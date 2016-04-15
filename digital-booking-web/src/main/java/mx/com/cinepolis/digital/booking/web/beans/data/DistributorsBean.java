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

import mx.com.cinepolis.digital.booking.commons.query.DistributorQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.integration.distributor.ServiceAdminDistributorIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.synchronize.ServiceDataSynchronizerIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * @author kperez
 */
@ManagedBean(name = "distributorsBean")
@ViewScoped
public class DistributorsBean extends BaseManagedBean implements Serializable
{
  private static final long serialVersionUID = -2399455899402669253L;
  private LazyDataModel<DistributorTO> distributorTOs;
  private DistributorTO selectedDistributor;
  private String newName = "";
  private String newIdVista = "";
  private String newShortName = "";

  @EJB
  private ServiceAdminDistributorIntegratorEJB serviceAdminDistributorIntegratorEJB;
  @EJB
  private ServiceDataSynchronizerIntegratorEJB dataSynchronizerIntegratorEJB;

  /**
   * Constructor default
   */
  @PostConstruct
  public void init()
  {
    this.distributorTOs = new DistributorLazyDataModel( this.serviceAdminDistributorIntegratorEJB );
    ((DistributorLazyDataModel) this.distributorTOs).setUserId( super.getUserId() );
  }

  /**
   * Método que elimina distribuidor seleccionado
   */
  public void deleteDistributor()
  {
    serviceAdminDistributorIntegratorEJB.deleteDistributor( getSelectedDistributorFromView() );
    this.selectedDistributor = null;
  }

  private DistributorTO getSelectedDistributorFromView()
  {
    DistributorTO distributor = new DistributorTO();
    super.fillSessionData( distributor );
    distributor.setId( selectedDistributor.getId() );
    distributor.setName( selectedDistributor.getName() );
    distributor.setIdVista( selectedDistributor.getIdVista() );
    distributor.setShortName( selectedDistributor.getShortName() );
    return distributor;
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   */
  public void validateSelection()
  {
    if( selectedDistributor == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
    }
  }

  /**
   * @return the selectedDistributor
   */
  public DistributorTO getSelectedDistributor()
  {
    return selectedDistributor;
  }

  /**
   * @param selectedDistributor the selectedDistributor to set
   */
  public void setSelectedDistributor( DistributorTO selectedDistributor )
  {
    this.selectedDistributor = selectedDistributor;
  }

  /**
   * Método que guarda un distribuidor
   */
  public void saveDistributor()
  {
    DistributorTO distributor = new DistributorTO();
    super.fillSessionData( distributor );
    distributor.setName( newName );
    distributor.setIdVista( newIdVista );
    distributor.setShortName( newShortName );
    serviceAdminDistributorIntegratorEJB.saveDistributor( distributor );
  }

  /**
   * Método que se ejecuta al cerrar una ventana
   * 
   * @param event
   */
  public void handleClose( CloseEvent event )
  {
    this.newName = null;
    this.newIdVista = null;
    this.newShortName = null;
  }

  /**
   * Método que actualiza distribuidor seleccionado
   */
  public void updateDistributor()
  {
    serviceAdminDistributorIntegratorEJB.updateDistributor( getSelectedDistributorFromView() );
  }

  public void synchronizeWithView()
  {
    dataSynchronizerIntegratorEJB.synchronizeDistributors();
  }

  /**
   * @return the distributorTOs
   */
  public LazyDataModel<DistributorTO> getDistributorTOs()
  {
    return this.distributorTOs;
  }

  /**
   * @param distributorTOs the distributorTOs to set
   */
  public void setDistributorTOs( LazyDataModel<DistributorTO> distributorTOs )
  {
    this.distributorTOs = distributorTOs;
  }

  /**
   * @return the newName
   */
  public String getNewName()
  {
    return newName;
  }

  /**
   * @param newName the newName to set
   */
  public void setNewName( String newName )
  {
    this.newName = newName;
  }

  /**
   * @return the newIdVista
   */
  public String getNewIdVista()
  {
    return newIdVista;
  }

  /**
   * @param newIdVista the newIdVista to set
   */
  public void setNewIdVista( String newIdVista )
  {
    this.newIdVista = newIdVista;
  }

  /**
   * @return the newShortName
   */
  public String getNewShortName()
  {
    return newShortName;
  }

  /**
   * @param newShortName the newShortName to set
   */
  public void setNewShortName( String newShortName )
  {
    this.newShortName = newShortName;
  }

  static class DistributorLazyDataModel extends LazyDataModel<DistributorTO>
  {

    private static final long serialVersionUID = 5196427890790836748L;
    private ServiceAdminDistributorIntegratorEJB serviceAdminDistributorIntegratorEJB;
    private Long userId;

    public DistributorLazyDataModel( ServiceAdminDistributorIntegratorEJB serviceAdminDistributorIntegratorEJB )
    {
      this.serviceAdminDistributorIntegratorEJB = serviceAdminDistributorIntegratorEJB;
    }

    @Override
    public List<DistributorTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      int page = first / pageSize;
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( userId );
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( DistributorQuery.DISTRIBUTOR_SHORT_NAME );
      pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
      pagingRequestTO.getFilters().put( DistributorQuery.DISTRIBUTOR_ACTIVE, true );
      PagingResponseTO<DistributorTO> response = serviceAdminDistributorIntegratorEJB
          .getCatalogDistributorSummary( pagingRequestTO );
      List<DistributorTO> result = response.getElements();
      this.setRowCount( response.getTotalCount() );
      return result;
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

  }

}
