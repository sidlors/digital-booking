package mx.com.cinepolis.digital.booking.web.beans.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.WeekQuery;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.integration.week.ServiceAdminWeekIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

/**
 * Controlador para la administración de semanas.
 * 
 * @author shernandezl
 */
@ManagedBean(name = "weeksBean")
@ViewScoped
public class WeeksBean extends BaseManagedBean
{

  private static final long serialVersionUID = -3071297938497048555L;
  private WeeksLazyDataModel weeksTOs;
  private WeekTO selectedWeek;
  private int nuWeek = 0;
  private int nuYear = 0;
  private Date startingDayWeek;
  private Date finalDayWeek;
  private boolean specialWeek = false;
  
  private WeeksLazyDataModel weekTO;
  private Integer filterYear;
  private Integer filterNumWeek;
  private boolean filterSpecialWeek=false;
 
  @EJB
  private ServiceAdminWeekIntegratorEJB serviceAdminWeekIntegratorEJB;

  /**
   * Constructor default
   */
  @PostConstruct
  public void init()
  {
    this.weeksTOs = new WeeksLazyDataModel( this.serviceAdminWeekIntegratorEJB );
    ((WeeksLazyDataModel) this.weeksTOs).setUserId( super.getUserId() );
  }

  /**
   * Método que setea los valores iniciales de los campos.
   */
  public void getNextNumWeek()
  {
    WeekTO week = serviceAdminWeekIntegratorEJB.getNextWeek();
    nuWeek = week.getNuWeek();
    nuYear = week.getNuYear();
    startingDayWeek = week.getStartingDayWeek();
    finalDayWeek = week.getFinalDayWeek();
  }

  /**
   * Método que guarda una semana
   */
  public void saveWeek()
  {
    WeekTO week = new WeekTO();
    super.fillSessionData( week );
    week.setNuWeek( nuWeek );
    week.setNuYear( nuYear );
    week.setStartingDayWeek( startingDayWeek );
    week.setFinalDayWeek( finalDayWeek );
    week.setSpecialWeek( isSpecialWeek() );
    serviceAdminWeekIntegratorEJB.saveWeek( week );
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   */
  public void validateSelection()
  {
    if( selectedWeek == null )
    {
      RequestContext.getCurrentInstance().addCallbackParam( "fail", true );
    }
  }

  /**
   * Método que obtiene el elemento seleccionado de la tabla de semanas.
   * 
   * @return
   */
  private WeekTO getSelectedWeekFromView()
  {
    WeekTO week = new WeekTO();
    super.fillSessionData( week );
    week.setIdWeek( selectedWeek.getIdWeek() );
    week.setNuWeek( selectedWeek.getNuWeek() );
    week.setNuYear( selectedWeek.getNuYear() );
    week.setStartingDayWeek( selectedWeek.getStartingDayWeek() );
    week.setFinalDayWeek( selectedWeek.getFinalDayWeek() );
    week.setSpecialWeek( selectedWeek.isSpecialWeek() );
    return week;
  }
  /**
   * 
   */
  
  public void searchFilters()
  {
    weeksTOs.setYear( filterYear);
    weeksTOs.setNumWeek( filterNumWeek );
    weeksTOs.setSpecialWeek(filterSpecialWeek);
  }
/**
 * 
 */
  public void resetFilters()
  {
    filterYear=null;
    filterNumWeek=null;
    filterSpecialWeek=false;
    weeksTOs.setYear( filterYear);
    weeksTOs.setNumWeek( filterNumWeek );
    weeksTOs.setSpecialWeek(filterSpecialWeek);
  }
  /**
   * 
   */
  
  public void handleToggleFilters( ToggleEvent event )
  {
    resetFilters();
  }
  /**
   * Método que actualiza una semana.
   */
  public void updateWeek()
  {
    serviceAdminWeekIntegratorEJB.updateWeeK( getSelectedWeekFromView() );
  }

  /**
   * Método que elimina una semana.
   */
  public void deleteWeek()
  {
    serviceAdminWeekIntegratorEJB.deleteWeek( getSelectedWeekFromView() );
    this.selectedWeek = null;
  }

  /**
   * Método que se ejecuta al cerrar una ventana
   * 
   * @param event
   */
  public void handleClose( CloseEvent event )
  {
    this.nuWeek = 0;
    this.nuYear = 0;
    this.startingDayWeek = null;
    this.finalDayWeek = null;
    this.specialWeek = false;
  }

  /**
   * Clase que realiza la carga inicial de los datos.
   * 
   * @author shernandezl
   */
  static class WeeksLazyDataModel extends LazyDataModel<WeekTO>
  {

    private static final long serialVersionUID = -2925746118158911443L;
    private ServiceAdminWeekIntegratorEJB serviceAdminWeekIntegratorEJB;
    private Long userId;
    
    private Integer year;
    private Integer numWeek;
    private boolean specialWeek;
    /**
     * Constructor de la clase.
     * 
     * @param serviceAdminWeekIntegratorEJB
     */
    public WeeksLazyDataModel( ServiceAdminWeekIntegratorEJB serviceAdminWeekIntegratorEJB )
    {
      this.serviceAdminWeekIntegratorEJB = serviceAdminWeekIntegratorEJB;
    }

    /**
     * Método que realiza una consulta paginada de semanas.
     */
    @Override
    public List<WeekTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      int page = first / pageSize;
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( userId );
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( WeekQuery.WEEK_NUM );
      pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
      pagingRequestTO.getFilters().put( WeekQuery.WEEK_ACTIVE, true );
      if(this.year != null && this.year!=0)
      {
        pagingRequestTO.getFilters().put( WeekQuery.WEEK_YEAR, this.year );
      }
      if(this.numWeek != null && this.numWeek !=0)
      {
        pagingRequestTO.getFilters().put( WeekQuery.WEEK_NUM, this.numWeek );
      }
      if(this.isSpecialWeek())
      {
        pagingRequestTO.getFilters().put( WeekQuery.SPECIAL_WEEK, this.specialWeek );
      }
      
      
      PagingResponseTO<WeekTO> response = serviceAdminWeekIntegratorEJB.getCatalogWeekSummary( pagingRequestTO );
      List<WeekTO> result = response.getElements();
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

    public Integer getYear()
    {
      return year;
    }

    public void setYear( Integer year )
    {
      this.year = year;
    }

    public Integer getNumWeek()
    {
      return numWeek;
    }

    public void setNumWeek( Integer numWeek )
    {
      this.numWeek = numWeek;
    }

    public boolean isSpecialWeek()
    {
      return specialWeek;
    }

    public void setSpecialWeek( boolean specialWeek )
    {
      this.specialWeek = specialWeek;
    }

    

  }

  /**
   * @return the weeksTOs
   */
  public LazyDataModel<WeekTO> getWeeksTOs()
  {
    return weeksTOs;
  }

  /**
   * @return the selectedWeek
   */
  public WeekTO getSelectedWeek()
  {
    return selectedWeek;
  }

  /**
   * @param selectedWeek the selectedWeek to set
   */
  public void setSelectedWeek( WeekTO selectedWeek )
  {
    this.selectedWeek = selectedWeek;
  }

  /**
   * @return the nuWeek
   */
  public int getNuWeek()
  {
    return nuWeek;
  }

  /**
   * @param nuWeek the nuWeek to set
   */
  public void setNuWeek( int nuWeek )
  {
    this.nuWeek = nuWeek;
  }

  /**
   * @return the nuYear
   */
  public int getNuYear()
  {
    return nuYear;
  }

  /**
   * @param nuYear the nuYear to set
   */
  public void setNuYear( int nuYear )
  {
    this.nuYear = nuYear;
  }

  /**
   * @return the startingDayWeek
   */
  public Date getStartingDayWeek()
  {
    return CinepolisUtils.enhancedClone( startingDayWeek );
  }

  /**
   * @param startingDayWeek the startingDayWeek to set
   */
  public void setStartingDayWeek( Date startingDayWeek )
  {
    this.startingDayWeek = CinepolisUtils.enhancedClone( startingDayWeek );
  }

  /**
   * @return the finalDayWeek
   */
  public Date getFinalDayWeek()
  {
    return CinepolisUtils.enhancedClone( finalDayWeek );
  }

  /**
   * @param finalDayWeek the finalDayWeek to set
   */
  public void setFinalDayWeek( Date finalDayWeek )
  {
    this.finalDayWeek = CinepolisUtils.enhancedClone( finalDayWeek );
  }

  /**
   * @return the specialWeek
   */
  public boolean isSpecialWeek()
  {
    return specialWeek;
  }

  /**
   * @param specialWeek the specialWeek to set
   */
  public void setSpecialWeek( boolean specialWeek )
  {
    this.specialWeek = specialWeek;
  }

 

  public WeeksLazyDataModel getWeekTO()
  {
    return weekTO;
  }

  public void setWeekTO( WeeksLazyDataModel weekTO )
  {
    this.weekTO = weekTO;
  }
  public Integer getFilterYear()
  {
    return filterYear;
  }

  public void setFilterYear( Integer filterYear )
  {
    this.filterYear = filterYear;
  }

  public Integer getFilterNumWeek()
  {
    return filterNumWeek;
  }

  public void setFilterNumWeek( Integer filterNumWeek )
  {
    this.filterNumWeek = filterNumWeek;
  }

  public boolean isFilterSpecialWeek()
  {
    return filterSpecialWeek;
  }

  public void setFilterSpecialWeek( boolean filterSpecialWeek )
  {
    this.filterSpecialWeek = filterSpecialWeek;
  }

}
