package mx.com.cinepolis.digital.booking.web.beans.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;

import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.bookingspecialevent.BookingSpecialEventServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Clase que maneja la vista sendEmailPresaleRegion
 * 
 * @author jreyesv
 */

@ManagedBean(name = "sendEmailPresaleRegionBean")
@ViewScoped
public class SendEmailPresaleRegionBean extends BaseManagedBean
{

  private static final long serialVersionUID = -4434573631220083562L;
  private static final Logger LOG = LoggerFactory.getLogger( SendEmailPresaleRegionBean.class );
  private static final String NO_SELECTED_WEEK_REGION_ERROR_TEXT = "reports.sendemailpresaleregion.mesgerror.noSelectedWeekRegionText";
  private static final String NO_SPECIFIED_SUBJECT_ERROR_TEXT = "reports.sendemailpresaleregion.mesgerror.noSpecifiedSubjectText";
  private static final String NO_EVENTS_SELECTED_ERROR_TEXT = "reports.sendemailpresaleregion.mesgerror.noEventsSelectedText";
  private static final String EDITOR_EMAIL_TEMPLATE = "<html><body><p>&nbsp;</p></body></html>";

  private List<CatalogTO> regions;
  private List<WeekTO> weeks;
  private String template;
  private Long weekIdSelected;
  private Long regionIdSelected;
  private String movieName;
  private String subject;
  private Boolean isWeekEditable = false;
  private BookingsLazyDataModel movieList;
  private List<EventTO> movieListSelected;
  private boolean enablePreviewButton = false;
  private boolean enableSendButton = false;

  @EJB
  private BookingSpecialEventServiceIntegratorEJB bookingSpecialEventServiceIntegratorEJB;

  @EJB
  private BookingServiceIntegratorEJB bookingServiceIntegratorEJB;

  /**
   * Constructor default
   */
  @PostConstruct
  public void init()
  {
    AbstractTO abstractTO = new AbstractTO();
    super.fillSessionData( abstractTO );
    abstractTO.setFgActive( true );

    weeks = bookingSpecialEventServiceIntegratorEJB.findWeeksActive( abstractTO );
    regions = bookingSpecialEventServiceIntegratorEJB.findAllActiveRegions( abstractTO );
    this.movieList = new BookingsLazyDataModel( bookingSpecialEventServiceIntegratorEJB, getUserId() );

    for( WeekTO weekTO : weeks )
    {
      if( weekTO.isActiveWeek() )
      {
        this.setWeekIdSelected( weekTO.getIdWeek().longValue() );
        this.movieList.filterIdWeek = this.getWeekIdSelected();
      }
    }
    this.template = EDITOR_EMAIL_TEMPLATE;
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   * 
   * @return true si no se selecciono registro
   */
  private Boolean validateFiltersSelection()
  {
    Boolean isValid = true;
    isValid &= !(weekIdSelected == null || weekIdSelected.equals( 0L ));
    isValid &= !(regionIdSelected == null || regionIdSelected.equals( 0L ));
    if( !isValid )
    {
      createMessageError( NO_SELECTED_WEEK_REGION_ERROR_TEXT );
    }
    return isValid;
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   * 
   * @return true si no se selecciono registro
   */
  private Boolean validateSelection()
  {
    Boolean isValid = this.validateFiltersSelection();
    isValid = StringUtils.isNotEmpty( this.subject );
    if( !isValid )
    {
      createMessageError( NO_SPECIFIED_SUBJECT_ERROR_TEXT );
    }
    isValid = CollectionUtils.isNotEmpty( this.movieListSelected );
    if( !isValid )
    {
      createMessageError( NO_EVENTS_SELECTED_ERROR_TEXT );
    }
    return isValid;
  }

  /**
   * Método que envia un correo con el documento a una region
   */
  public void sendDocument()
  {
    if( this.validateSelection() )
    {
      RegionEmailTO regionEmailTO = new RegionEmailTO();
      super.fillSessionData( regionEmailTO );
      regionEmailTO.setIdRegion( this.regionIdSelected );
      regionEmailTO.setIdWeek( this.weekIdSelected );
      regionEmailTO.setMessage( this.template );
      regionEmailTO.setSubject( this.subject );
      this.bookingSpecialEventServiceIntegratorEJB.sendPresalesBookedByRegionEmail( regionEmailTO,
        this.movieListSelected );
    }
    else
    {
      validationFail();
    }
  }

  /**
   * Method set the filter parameters for lazy search.
   */
  public void setFilterValues()
  {
    if( this.validateFiltersSelection() )
    {
      this.movieList.setFilterMovieName( this.movieName );
      this.movieList.setFilterIdWeek( this.weekIdSelected );
      this.movieList.setFilterIdRegion( this.regionIdSelected );
    }
  }

  /**
   * Method reset the values shows in view.
   */
  public void reset()
  {
    this.movieName = null;
    this.weekIdSelected = null;
    for( WeekTO weekTO : this.weeks )
    {
      if( weekTO.isActiveWeek() )
      {
        setWeekIdSelected( weekTO.getIdWeek().longValue() );
      }
    }
    this.regionIdSelected = null;
    this.subject = null;
    this.template = EDITOR_EMAIL_TEMPLATE;
    this.movieList.setFilterIdRegion( null );
    this.movieList.setFilterIdWeek( this.weekIdSelected );
    this.movieList.setFilterMovieName( null );
    this.movieListSelected = null;
    this.enableButtons( null );
  }

  /**
   * Method that clears the movies lists.
   */
  public void clearEvents( AjaxBehaviorEvent event )
  {
    this.movieList.setFilterIdRegion( null );
    this.movieList.setFilterIdWeek( this.weekIdSelected );
    this.movieList.setFilterMovieName( null );
    this.movieListSelected = null;
    this.enableButtons( event );
  }

  /**
   * Method validate whether the preview and send buttons be enable or disable.
   * 
   * @param event
   */
  public void enableButtons( AjaxBehaviorEvent event )
  {
    this.enablePreviewButton = !(this.isValidWeekMenu() && this.isValidRegionMenu() && this.isThereSelectedMovies());
    this.enableSendButton = this.enablePreviewButton;
  }

  /**
   * Method validate whether weekIdSelected is valid.
   * 
   * @return
   */
  private boolean isValidWeekMenu()
  {
    return (this.weekIdSelected != null && this.weekIdSelected > 0 && this.isWeekEditable);
  }

  /**
   * Method validate whether regionIdSelected is valid.
   * 
   * @return
   */
  private boolean isValidRegionMenu()
  {
    return (this.regionIdSelected != null && this.regionIdSelected > 0);
  }

  /**
   * Method validate whether exist movies selected.
   * 
   * @return
   */
  private boolean isThereSelectedMovies()
  {
    return (this.movieListSelected != null && this.movieListSelected.size() > 0);
  }

  /**
   * @return the regions
   */
  public List<CatalogTO> getRegions()
  {
    return regions;
  }

  /**
   * @param regions the regions to set
   */
  public void setRegions( List<CatalogTO> regions )
  {
    this.regions = regions;
  }

  /**
   * @return the regionIdSelected
   */
  public Long getRegionIdSelected()
  {
    return regionIdSelected;
  }

  /**
   * @param regionIdSelected the regionIdSelected to set
   */
  public void setRegionIdSelected( Long regionIdSelected )
  {
    this.regionIdSelected = regionIdSelected;
  }

  /**
   * @return the template
   */
  public String getTemplate()
  {
    return template;
  }

  /**
   * @param template the template to set
   */
  public void setTemplate( String template )
  {
    this.template = template;
  }

  /**
   * @return the weekIdSelected
   */
  public Long getWeekIdSelected()
  {
    return weekIdSelected;
  }

  /**
   * @param weekIdSelected the weekIdSelected to set
   */
  public void setWeekIdSelected( Long weekIdSelected )
  {
    this.weekIdSelected = weekIdSelected;
    validateWeekEditable();
  }

  private void validateWeekEditable()
  {
    WeekTO foundWeekTO = null;
    for( WeekTO weekTO : weeks )
    {
      if( weekIdSelected != null && weekTO.getIdWeek().intValue() == weekIdSelected.intValue() )
      {
        foundWeekTO = weekTO;
      }
    }
    this.isWeekEditable = this.isEditableWeek( foundWeekTO );
  }

  /**
   * @return the weeks
   */
  public List<WeekTO> getWeeks()
  {
    return weeks;
  }

  /**
   * @param weeks the weeks to set
   */
  public void setWeeks( List<WeekTO> weeks )
  {
    this.weeks = weeks;
  }

  /**
   * @return the movieName
   */
  public String getMovieName()
  {
    return movieName;
  }

  /**
   * @param movieName the movieName to set
   */
  public void setMovieName( String movieName )
  {
    this.movieName = movieName;
  }

  /**
   * @return the subject
   */
  public String getSubject()
  {
    return subject;
  }

  /**
   * @param subject the subject to set
   */
  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  /**
   * @return the movieList
   */
  public BookingsLazyDataModel getMovieList()
  {
    return movieList;
  }

  /**
   * @param movieList the movieList to set
   */
  public void setMovieList( BookingsLazyDataModel movieList )
  {
    this.movieList = movieList;
  }

  /**
   * @return the movieListSelected
   */
  public List<EventTO> getMovieListSelected()
  {
    return movieListSelected;
  }

  /**
   * @param movieListSelected the movieListSelected to set
   */
  public void setMovieListSelected( List<EventTO> movieListSelected )
  {
    this.movieListSelected = movieListSelected;
  }

  /**
   * @return the isWeekEditable
   */
  public Boolean getIsWeekEditable()
  {
    return isWeekEditable;
  }

  /**
   * @param isWeekEditable the isWeekEditable to set
   */
  public void setIsWeekEditable( Boolean isWeekEditable )
  {
    this.isWeekEditable = isWeekEditable;
  }

  /**
   * @return the enablePreviewButton
   */
  public boolean isEnablePreviewButton()
  {
    return enablePreviewButton;
  }

  /**
   * @return the enableSendButton
   */
  public boolean isEnableSendButton()
  {
    return enableSendButton;
  }

  private boolean isEditableWeek( WeekTO weekTO )
  {
    boolean isEditableWeek = false;
    if( weekTO != null )
    {
      Date today = DateUtils.truncate( weekTO.getTimestamp(), Calendar.DATE );
      isEditableWeek = today.before( weekTO.getStartingDayWeek() );
    }
    return isEditableWeek;
  }

  /**
   * @return the file
   */
  public StreamedContent getFile()
  {
    FileTO fileTO = new FileTO();
    StreamedContent file = null;
    if( this.validateSelection() )
    {

      Long weekId = this.weekIdSelected;
      Long regionId = this.regionIdSelected;

      fileTO = this.bookingSpecialEventServiceIntegratorEJB.getWeeklyBookingReportPresaleByRegion(
        this.movieListSelected, weekId, regionId );

      byte[] bytes = fileTO.getFile();

      File excelFile;
      try
      {
        String name = getName( weekId, regionId );
        excelFile = File.createTempFile( name, ".xlsx" );
        FileOutputStream fileOutputStream = new FileOutputStream( excelFile );
        fileOutputStream.write( bytes );
        fileOutputStream.flush();
        fileOutputStream.close();

        file = new DefaultStreamedContent( new FileInputStream( excelFile ),
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", name + ".xlsx", "UTF-8" );
      }
      catch( Exception e )
      {
        LOG.error( e.getMessage(), e );
      }
    }
    else
    {
      validationFail();
    }

    return file;
  }

  private String getName( Long weekId, Long regionId )
  {
    StringBuilder sb = new StringBuilder();

    WeekTO week = this.bookingServiceIntegratorEJB.findWeek( weekId.intValue() );
    RegionTO<CatalogTO, CatalogTO> region = this.bookingServiceIntegratorEJB.findRegion( regionId.intValue() );
    sb.append( "Programación Semana " ).append( week.getNuWeek() ).append( " Zona " )
        .append( region.getCatalogRegion().getName() );

    return sb.toString();
  }

  /**
   * Clase que realiza la carga inicial de los datos.
   * 
   * @author jreyesv
   */
  static class BookingsLazyDataModel extends LazyDataModel<EventTO>
  {

    private static final long serialVersionUID = -7055241023514754121L;
    private BookingSpecialEventServiceIntegratorEJB bookingSpecialEventServiceIntegratorEJB;
    private Long userId;
    private List<EventTO> result;
    // Filters
    private String filterMovieName;
    private Long filterIdWeek;
    private Long filterIdRegion;
    // Constants
    private static final int MIN_NUMBER_FILTERS_TO_SEARCH = 5;
    private static final int NO_RECORDS_FOUND = 0;

    /**
     * Constructor de la clase.
     * 
     * @param serviceAdminWeekIntegratorEJB
     */
    public BookingsLazyDataModel( BookingSpecialEventServiceIntegratorEJB bookingSpecialEventServiceIntegratorEJB, Long userId )
    {
      this.bookingSpecialEventServiceIntegratorEJB = bookingSpecialEventServiceIntegratorEJB;
      this.userId = userId;
    }

    /**
     * Método que realiza una consulta paginada de semanas.
     */
    @Override
    public List<EventTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      int page = first / pageSize;
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( userId );
      // Paging
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      // Sorting
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( BookingQuery.BOOKING_ID );
      pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
      // Filtering
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, true );
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_BOOKED, true );
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_PRESALE_ACTIVE, true );

      if( StringUtils.isNotBlank( this.filterMovieName ) )
      {
        pagingRequestTO.getFilters().put( BookingQuery.BOOKING_EVENT_NAME, this.filterMovieName );
      }
      if( this.filterIdWeek != null && this.filterIdWeek > 0 )
      {
        pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, this.filterIdWeek );
      }
      if( this.filterIdRegion != null && this.filterIdRegion > 0 )
      {
        pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, this.filterIdRegion );
      }
      if( pagingRequestTO.getFilters().size() >= MIN_NUMBER_FILTERS_TO_SEARCH
          && this.isSelectedWeekAndRegion( pagingRequestTO.getFilters() ) )
      {
        // Responsing
        PagingResponseTO<EventTO> response = bookingSpecialEventServiceIntegratorEJB
            .getEventsBookedForReport( pagingRequestTO );
        this.result = response.getElements();
        this.setRowCount( response.getTotalCount() );
      }
      else
      {
        this.result = null;
        this.setRowCount( NO_RECORDS_FOUND );
      }
      return this.result;
    }

    /**
     * Method that validates that filters contains a week and a region selected.
     * @param filters
     * @return isValid
     */
    private boolean isSelectedWeekAndRegion( Map<ModelQuery, Object> filters )
    {
      return (filters.containsKey( BookingQuery.BOOKING_WEEK_ID ) && filters
          .containsKey( BookingQuery.BOOKING_REGION_ID ));
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
     * @return the filterMovieName
     */
    public String getFilterMovieName()
    {
      return filterMovieName;
    }

    /**
     * @param filterMovieName the filterMovieName to set
     */
    public void setFilterMovieName( String filterMovieName )
    {
      this.filterMovieName = filterMovieName;
    }

    /**
     * @return the filterIdWeek
     */
    public Long getFilterIdWeek()
    {
      return filterIdWeek;
    }

    /**
     * @param filterIdWeek the filterIdWeek to set
     */
    public void setFilterIdWeek( Long filterIdWeek )
    {
      this.filterIdWeek = filterIdWeek;
    }

    /**
     * @return the filterIdRegion
     */
    public Long getFilterIdRegion()
    {
      return filterIdRegion;
    }

    /**
     * @param filterIdRegion the filterIdRegion to set
     */
    public void setFilterIdRegion( Long filterIdRegion )
    {
      this.filterIdRegion = filterIdRegion;
    }

    public EventTO getRowData( String rowKey )
    {
      EventTO result = null;
      if( CollectionUtils.isNotEmpty( this.result ) )
      {
        for( EventTO eventTO : this.result )
        {
          if( eventTO.getIdEvent().equals( Long.valueOf( rowKey ) ) )
          {
            result = eventTO;
            break;
          }
        }
      }
      return result;
    }

    public Object getRowKey( EventTO eventTO )
    {
      return eventTO.getIdEvent();
    }

  }

}
