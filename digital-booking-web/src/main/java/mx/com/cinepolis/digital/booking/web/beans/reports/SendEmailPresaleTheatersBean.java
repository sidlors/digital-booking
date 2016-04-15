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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.TheaterQuery;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.BookingTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.integration.bookingspecialevent.BookingSpecialEventServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controlador del envio de correo a cines
 * 
 * @author jreyesv
 */
@ManagedBean(name = "sendEmailPresaleTheatersBean")
@ViewScoped
public class SendEmailPresaleTheatersBean extends BaseManagedBean
{

  private static final long serialVersionUID = -2545004063498476468L;
  private static final Logger LOG = LoggerFactory.getLogger( SendEmailPresaleTheatersBean.class );
  private static final String NO_SELECTED_REGION_WEEK_ERROR_TEXT = "reports.sendemailtheaters.mesgerror.noSelectedRegionWeekText";
  private static final String NO_SPECIFIED_SUBJECT_ERROR_TEXT = "reports.sendemailpresaletheaters.mesgerror.noSpecifiedSubjectText";
  private static final String NO_SELECTED_THEATERS_ERROR_TEXT = "reports.sendemailtheaters.mesgerror.noSelectedTheatersText";
  private static final String SELECTED_RED_LIGHT_ERROR_TEXT = "reports.sendemailtheaters.mesgerror.lightErrorText";
  private static final String GREEN_LIGHT_TEXT = "sem_vd";
  private static final String YELLOW_LIGHT_TEXT = "sem_am";
  private static final String RED_LIGHT_TEXT = "sem_rj";
  private static final String ID_EVENT = "getIdEvent";

  private List<CatalogTO> regions;
  private Long regionIdSelected;
  private List<TheaterTO> theaters;
  private List<TheaterTO> theatersSelected;
  private Long theaterIdSelected;
  private Long weekIdSelected;
  private String movieName;
  private String subject;
  private List<WeekTO> weekList;
  private Boolean isWeekEditable = false;
  private BookingsLazyDataModel movieList;
  private List<EventTO> movieListSelected;
  private List<BookingTO> bookingTOs;
  private boolean enablePreviewButton = false;
  private boolean enableSendButton = false;
  /**
   * Booking special event service
   */
  @EJB
  private BookingSpecialEventServiceIntegratorEJB bookingSpecialEventServiceIntegratorEJB;
  /**
   * Bookign service
   */
  @EJB
  private BookingServiceIntegratorEJB bookingServiceIntegratorEJB;

  /**
   * Postonstructor default
   */
  @PostConstruct
  public void init()
  {
    AbstractTO abstractTO = new AbstractTO();
    super.fillSessionData( abstractTO );
    abstractTO.setFgActive( true );
    this.setWeekList( bookingSpecialEventServiceIntegratorEJB.findWeeksActive( abstractTO ) );
    this.setRegions( bookingSpecialEventServiceIntegratorEJB.findAllActiveRegions( abstractTO ) );
    this.movieList = new BookingsLazyDataModel( bookingSpecialEventServiceIntegratorEJB, this.getUserId() );

    for( WeekTO weekTO : weekList )
    {
      if( weekTO.isActiveWeek() )
      {
        setWeekIdSelected( weekTO.getIdWeek().longValue() );
        this.movieList.filterIdWeek = this.getWeekIdSelected();
      }
    }

    this.theaters = new ArrayList<TheaterTO>();
    this.bookingTOs = null;
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   * 
   * @return true si la lista es nula
   */
  public Boolean validateSelection()
  {
    Boolean isValid = false;
    if( CollectionUtils.isEmpty( theatersSelected ) || this.weekIdSelected == null )
    {
      validationFail();
      isValid = true;
    }
    return isValid;
  }

  /**
   * Método que envia un correo a los cines seleccionados.
   */
  public void sendDocument()
  {
    if( this.validateSendDocument() && validateSubject() )
    {
      List<TheaterTO> theaterTOs = new ArrayList<TheaterTO>();
      for( TheaterTO theaterTO : theatersSelected )
      {
        if( theaterTO.getImageSemaphore().equals( GREEN_LIGHT_TEXT )
            || theaterTO.getImageSemaphore().equals( YELLOW_LIGHT_TEXT ) )
        {
          super.fillSessionData( theaterTO );
          theaterTOs.add( theaterTO );
        }
      }
      TheaterEmailTO theaterEmailTO = new TheaterEmailTO();
      theaterEmailTO.setIdWeek( weekIdSelected );
      theaterEmailTO.setTheatersTO( theatersSelected );
      theaterEmailTO.setSubject( subject );

      bookingSpecialEventServiceIntegratorEJB.sendPresalesBookedByTheaterEmail( theaterEmailTO, this.movieListSelected );
      setTheatersSelected( null );
    }

  }

  /**
   * Method to valid the subject
   * 
   * @author jcarbajal
   * @return boolean
   */
  public boolean validateSubject()
  {
    boolean isValid = false;
    isValid = StringUtils.isNotEmpty( this.subject );
    if( !isValid )
    {
      validationFail();
      createMessageError( NO_SPECIFIED_SUBJECT_ERROR_TEXT );
    }
    return isValid;
  }

  public boolean validateSendDocument()
  {
    boolean isValid = true;
    if( this.validateSelection() )
    {
      isValid = false;
      validationFail();
      createMessageError( NO_SELECTED_THEATERS_ERROR_TEXT );
    }
    else
    {
      if( theatersSelected.size() > 1 )
      {
        int reds = 0;
        for( TheaterTO theaterTO : theatersSelected )
        {
          if( theaterTO.getImageSemaphore().equals( RED_LIGHT_TEXT ) )
          {
            reds++;
          }
        }
        if( reds > 0 )
        {
          isValid = false;
          validationFail();
          createMessageError( SELECTED_RED_LIGHT_ERROR_TEXT );
        }
      }
      else
      {
        for( TheaterTO theaterTO : theatersSelected )
        {
          if( theaterTO.getImageSemaphore().equals( RED_LIGHT_TEXT ) )
          {
            isValid = false;
            validationFail();
            createMessageError( SELECTED_RED_LIGHT_ERROR_TEXT );
          }
        }
      }
    }
    return isValid;
  }

  /**
   * Method set the filter parameters for lazy search.
   */
  private void setFilterValues()
  {
    this.movieList.setFilterMovieName( this.movieName );
    this.movieList.setFilterIdWeek( this.weekIdSelected );
    this.movieList.setFilterIdRegion( this.regionIdSelected );
  }

  /**
   * Method reset the values shows in view.
   */
  public void reset()
  {
    this.movieName = null;
    this.weekIdSelected = null;
    for( WeekTO weekTO : this.weekList )
    {
      if( weekTO.isActiveWeek() )
      {
        setWeekIdSelected( weekTO.getIdWeek().longValue() );
      }
    }
    this.regionIdSelected = null;
    this.subject = null;
    this.movieList.setFilterIdRegion( null );
    this.movieList.setFilterIdWeek( this.weekIdSelected );
    this.movieList.setFilterMovieName( null );
    this.movieListSelected = null;
    this.theaters = null;
    this.theatersSelected = null;
    this.bookingTOs = null;
    this.enableButtons();
  }

  /**
   * Método que consulta cines por semana y region
   */
  public void searchTheaters()
  {
    if( this.regionIdSelected == null || this.regionIdSelected.equals( Long.valueOf( 0 ) )
        || this.weekIdSelected == null || this.weekIdSelected.equals( Long.valueOf( 0 ) ) )
    {
      validationFail();
      createMessageError( NO_SELECTED_REGION_WEEK_ERROR_TEXT );
    }
    else
    {
      // Setea el valor de los parámetros de consulta para los eventos
      this.setFilterValues();
      this.movieList.load( 0, 0, null, null, null );
      this.updateBookingsTOsValue();
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      super.fillSessionData( pagingRequestTO );
      pagingRequestTO.setNeedsPaging( Boolean.FALSE );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
      pagingRequestTO.getFilters().put( TheaterQuery.THEATER_ACTIVE, Boolean.TRUE );
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, Boolean.TRUE );
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, this.regionIdSelected );
      pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, this.weekIdSelected );
      PagingResponseTO<TheaterTO> response = this.bookingServiceIntegratorEJB
          .findTheatersByBookingWeekAndRegionForPresaleReport( pagingRequestTO );
      this.theaters = response.getElements();
      this.theatersSelected.clear();
      this.movieListSelected.clear();
    }
  }

  /**
   * Method that updates the value of bookingTOs list.
   */
  public void updateBookingsTOsValue()
  {
    // Consulta para traer toda la información de los bookings
    this.bookingTOs = this.bookingSpecialEventServiceIntegratorEJB.findBookingsInPresaleByEevntZoneAndWeek(
      this.movieList.result, this.weekIdSelected, this.regionIdSelected );
  }

  /**
   * Method that validates whether weekIdSelected is editable.
   */
  private void validateWeekEditable()
  {
    WeekTO foundWeekTO = null;
    for( WeekTO weekTO : weekList )
    {
      if( weekIdSelected != null && weekTO.getIdWeek().intValue() == weekIdSelected.intValue() )
      {
        foundWeekTO = weekTO;
      }
    }
    this.isWeekEditable = this.isEditableWeek( foundWeekTO );
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
   * @return the theaters
   */
  public List<TheaterTO> getTheaters()
  {
    return theaters;
  }

  /**
   * @param theaters the theaters to set
   */
  public void setTheaters( List<TheaterTO> theaters )
  {
    this.theaters = theaters;
  }

  /**
   * @return the theatersSelected
   */
  public List<TheaterTO> getTheatersSelected()
  {
    return theatersSelected;
  }

  /**
   * @param theatersSelected the theatersSelected to set
   */
  public void setTheatersSelected( List<TheaterTO> theatersSelected )
  {
    this.theatersSelected = theatersSelected;
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
    this.validateWeekEditable();
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
   * @return the weekList
   */
  public List<WeekTO> getWeekList()
  {
    return weekList;
  }

  /**
   * @param weekList the weekList to set
   */
  public void setWeekList( List<WeekTO> weekList )
  {
    this.weekList = weekList;
  }

  /**
   * @return the theaterIdSelected
   */
  public Long getTheaterIdSelected()
  {
    return theaterIdSelected;
  }

  /**
   * @param theaterIdSelected the theaterIdSelected to set
   */
  public void setTheaterIdSelected( Long theaterIdSelected )
  {
    this.theaterIdSelected = theaterIdSelected;
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
   * @return the bookingTOs
   */
  public List<BookingTO> getBookingTOs()
  {
    return bookingTOs;
  }

  /**
   * @return the enablePreviewButton
   */
  public boolean isEnablePreviewButton()
  {
    return enablePreviewButton;
  }

  /**
   * @param enablePreviewButton the enablePreviewButton to set
   */
  public void setEnablePreviewButton( boolean enablePreviewButton )
  {
    this.enablePreviewButton = enablePreviewButton;
  }

  /**
   * @return the enableSendButton
   */
  public boolean isEnableSendButton()
  {
    return enableSendButton;
  }

  /**
   * @param enableSendButton the enableSendButton to set
   */
  public void setEnableSendButton( boolean enableSendButton )
  {
    this.enableSendButton = enableSendButton;
  }

  /**
   * @param bookingTOs the bookingTOs to set
   */
  public void setBookingTOs( List<BookingTO> bookingTOs )
  {
    this.bookingTOs = bookingTOs;
  }

  /**
   * Method that validates whether exist red semaphores in the theaters selected.
   */
  public void validateRows()
  {
    if( CollectionUtils.isNotEmpty( this.theatersSelected ) && this.theatersSelected.size() >= 1 )
    {
      int semaphoreRed = this.countSemaphoresInRed();
      if( semaphoreRed > 0 )
      {
        validationFail();
      }
      else
      {
        this.theaterIdSelected = this.theatersSelected.get( 0 ).getId();
      }
    }
    else
    {
      this.theaterIdSelected = null;
    }
    this.enableButtons();
  }

  /**
   * Method that counts the semaphores painted in red color in the theaters list.
   * 
   * @return semaphoreRed, with the number of semaphores painted in red color.
   */
  private int countSemaphoresInRed()
  {
    int semaphoreRed = 0;
    for( TheaterTO theaterTO : this.theatersSelected )
    {
      if( theaterTO.getImageSemaphore().equals( RED_LIGHT_TEXT ) )
      {
        semaphoreRed++;
        break;
      }
    }
    return semaphoreRed;
  }

  /**
   * Method that clears the theaters lists and movies lists.
   */
  public void clearTheaters()
  {
    if( CollectionUtils.isNotEmpty( this.theaters ) )
    {
      this.theaters.clear();
      this.movieList.setFilterIdRegion( null );
      this.movieList.setFilterIdWeek( this.weekIdSelected );
      this.movieList.setFilterMovieName( null );
      this.movieListSelected = null;
      this.theatersSelected = null;
      this.bookingTOs = null;
    }
    this.enableButtons();
  }

  private boolean isEditableWeek( WeekTO weekTO )
  {
    boolean isEditableWeek = true;
    Date today = DateUtils.truncate( Calendar.getInstance().getTime(), Calendar.DATE );
    if( weekTO != null )
    {
      today = DateUtils.truncate( weekTO.getTimestamp(), Calendar.DATE );
    }
    if( weekTO == null || !today.before( weekTO.getStartingDayWeek() ) )
    {
      isEditableWeek = false;
    }
    return isEditableWeek;
  }

  /**
   * Method validate whether the preview and send buttons be enable or disable.
   * 
   * @param event
   */
  public void enableButtons()
  {
    this.enablePreviewButton = !(this.isValidWeekMenu() && this.isThereSelectedTheaters() && this
        .isThereSelectedMovies());
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
   * Method validate whether exist movies selected.
   * 
   * @return
   */
  private boolean isThereSelectedMovies()
  {
    return (this.movieListSelected != null && this.movieListSelected.size() > 0);
  }

  /**
   * Method validate whether exist theaters selected.
   * 
   * @return
   */
  private boolean isThereSelectedTheaters()
  {
    return (this.theatersSelected != null && this.theatersSelected.size() > 0 && this.countSemaphoresInRed() == 0);
  }

  /**
   * @return the file
   */
  public StreamedContent getFile()
  {
    StreamedContent file = null;

    Long weekId = this.weekIdSelected;
    Long theaterId = this.theaterIdSelected;

    FileTO fileTO = null;
    if( this.validateSendDocument() )
    {
      try
      {
        fileTO = bookingSpecialEventServiceIntegratorEJB.getWeeklyBookingReportPresaleByTheater(
          this.movieListSelected, weekId, theaterId );
      }
      catch( DigitalBookingException e )
      {
        RequestContext.getCurrentInstance().execute(
          "DigitalBookingUtil.showErrorDialog({severity:'" + FacesMessage.SEVERITY_ERROR + "',summary:'"
              + "Message Error" + "',detail:'" + StringEscapeUtils.escapeJavaScript( e.getDescription() ) + "'});" );
        throw e;
      }

      byte[] bytes = fileTO.getFile();

      File excelFile;
      try
      {
        String name = getName( weekId );
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

  private String getName( Long weekId )
  {
    StringBuilder sb = new StringBuilder();
    WeekTO week = this.bookingServiceIntegratorEJB.findWeek( weekId.intValue() );
    sb.append( "Programación Semana " ).append( week.getNuWeek() );
    return sb.toString();
  }

  /**
   * Clase que realiza la carga inicial de los datos.
   * 
   * @author jreyesv
   */
  static class BookingsLazyDataModel extends LazyDataModel<EventTO>
  {

    private static final long serialVersionUID = -8121251689533706211L;
    private BookingSpecialEventServiceIntegratorEJB bookingSpecialEventServiceIntegratorEJB;
    private Long userId;
    List<EventTO> result;
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
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( userId );
      pagingRequestTO.setNeedsPaging( Boolean.FALSE );
      // Paging
      if( pageSize > 0 )
      {
        int page = first / pageSize;
        pagingRequestTO.setPage( page );
        pagingRequestTO.setPageSize( pageSize );
        pagingRequestTO.setNeedsPaging( Boolean.TRUE );
      }
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
     * 
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

    /**
     * Method that gets an EventTO by rowKey.
     * 
     * @param rowKey
     * @return result The EventTO resulting.
     */
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

    /**
     * Method that gets the rowKey from an EventTO.
     * 
     * @param eventTO
     * @return idEvent
     */
    public Object getRowKey( EventTO eventTO )
    {
      return eventTO.getIdEvent();
    }

  }

  /**
   * Method that updates the semaphores in the theater list.
   */
  public void updateSemaphoresValue()
  {
    for( TheaterTO theater : this.theaters )
    {
      theater.setImageSemaphore( this.getSemaphoreValue( theater.getId() ) );
    }
    this.enableButtons();
  }

  /**
   * Method that gets the semaphore value for a theater.
   * 
   * @param idTheater
   * @return semaphoreValue
   */
  private String getSemaphoreValue( Long idTheater )
  {
    String semaphoreValue = "";
    switch( this.evalEventsSelcted( idTheater ) )
    {
      case 1:
        semaphoreValue = YELLOW_LIGHT_TEXT;
        break;
      case 2:
        semaphoreValue = GREEN_LIGHT_TEXT;
        break;
      default:
        semaphoreValue = RED_LIGHT_TEXT;
        break;
    }
    return semaphoreValue;
  }

  /**
   * Method that returns the evaluation, indicating whether the idEvents exist in the events selected.
   * 
   * @param idTheater
   * @return eval
   */
  private int evalEventsSelcted( Long idTheater )
  {
    int eval = 0;
    Predicate predicate;
    for( BookingTO bookingTO : this.bookingTOs )
    {
      if( bookingTO.getTheater().getId().equals( idTheater ) )
      {
        Long idEvent = bookingTO.getEvent().getIdEvent();
        predicate = PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( ID_EVENT ),
          PredicateUtils.equalPredicate( idEvent ) );
        if( CollectionUtils.exists( this.movieListSelected, predicate ) )
        {
          eval++;
        }
      }
    }
    eval = (eval > 0 ? (eval == this.movieListSelected.size() ? 2 : 1) : 0);
    return eval;
  }
}
