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
import javax.faces.event.AjaxBehaviorEvent;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.query.BookingQuery;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.TheaterQuery;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EmailTemplateTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.collections.CollectionUtils;
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
 * @author kperez
 */
@ManagedBean(name = "sendEmailTheatersBean")
@ViewScoped
public class SendEmailTheatersBean extends BaseManagedBean
{
  private static final Logger LOG = LoggerFactory.getLogger( SendEmailTheatersBean.class );
  private static final String NO_SELECTED_REGION_WEEK_ERROR_TEXT = "reports.sendemailtheaters.mesgerror.noSelectedRegionWeekText";

  private static final String NO_SELECTED_THEATERS_ERROR_TEXT = "reports.sendemailtheaters.mesgerror.noSelectedTheatersText";

  private static final long serialVersionUID = 514949729643277094L;

  private List<CatalogTO> regions;
  private Long regionIdSelected;
  private List<TheaterTO> theaters;
  private List<TheaterTO> theatersSelected;
  private Long theaterIdSelected;
  private Long weekIdSelected;
  private List<WeekTO> weekList;
  private Boolean isWeekEditable = false;
  private String template;
  private String subject;

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
    setRegions( bookingServiceIntegratorEJB.findAllActiveRegions( abstractTO ) );
    setWeekList( bookingServiceIntegratorEJB.findWeeksActive( abstractTO ) );

    for( WeekTO weekTO : weekList )
    {
      if( weekTO.isActiveWeek() )
      {
        setWeekIdSelected( weekTO.getIdWeek().longValue() );
      }
    }

    this.theaters = new ArrayList<TheaterTO>();
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
    if( this.validateSendDocument() )
    {
      List<TheaterTO> theaterTOs = new ArrayList<TheaterTO>();
      for( TheaterTO theaterTO : theatersSelected )
      {
        if( theaterTO.getImageSemaphore().equals( "sem_vd" ) || theaterTO.getImageSemaphore().equals( "sem_am" ) )
        {
          super.fillSessionData( theaterTO );
          theaterTOs.add( theaterTO );
        }
      }
      bookingServiceIntegratorEJB.sendTheatersEmail( weekIdSelected, theaterTOs, template, subject );
      setTheatersSelected( null );
    }

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
          if( theaterTO.getImageSemaphore().equals( "sem_rj" ) )
          {
            reds++;
          }
        }
        if( reds > 0 )
        {
          isValid = false;
          validationFail();
          createMessageError( "reports.sendemailtheaters.mesgerror.lightErrorText" );
        }
      }
      else
      {
        for( TheaterTO theaterTO : theatersSelected )
        {
          if( theaterTO.getImageSemaphore().equals( "sem_rj" ) )
          {
            isValid = false;
            validationFail();
            createMessageError( "reports.sendemailtheaters.mesgerror.lightErrorText" );
          }
        }
      }
    }
    return isValid;
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
          .findTheatersByBookingWeekAndRegion( pagingRequestTO );
      theaters = response.getElements();

      validateWeekEditable();

    }
  }

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
    isWeekEditable = isEditableWeek( foundWeekTO );
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

  static class TheaterLazyDataModel extends LazyDataModel<TheaterTO>
  {

    private static final long serialVersionUID = -1356371545363848113L;
    private Long regionId;
    private Long weekId;
    private BookingServiceIntegratorEJB bookingServiceIntegratorEJB;

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
     * @return the weekId
     */
    public Long getWeekId()
    {
      return weekId;
    }

    /**
     * @param weekId the weekId to set
     */
    public void setWeekId( Long weekId )
    {
      this.weekId = weekId;
    }

    /**
     * @return the bookingServiceIntegratorEJB
     */
    public BookingServiceIntegratorEJB getBookingServiceIntegratorEJB()
    {
      return bookingServiceIntegratorEJB;
    }

    /**
     * @param bookingServiceIntegratorEJB the bookingServiceIntegratorEJB to set
     */
    public void setBookingServiceIntegratorEJB( BookingServiceIntegratorEJB bookingServiceIntegratorEJB )
    {
      this.bookingServiceIntegratorEJB = bookingServiceIntegratorEJB;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<TheaterTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      List<TheaterTO> theaters = new ArrayList<TheaterTO>();
      if( this.weekId != null && this.regionId != null )
      {
        PagingRequestTO pagingRequestTO = new PagingRequestTO();
        pagingRequestTO.setNeedsPaging( Boolean.FALSE );
        pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
        pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );
        pagingRequestTO.getFilters().put( BookingQuery.BOOKING_ACTIVE, Boolean.TRUE );
        pagingRequestTO.getFilters().put( BookingQuery.BOOKING_REGION_ID, this.regionId );
        pagingRequestTO.getFilters().put( BookingQuery.BOOKING_WEEK_ID, this.weekId );

        PagingResponseTO<TheaterTO> response = this.bookingServiceIntegratorEJB
            .findTheatersByBookingWeekAndRegion( pagingRequestTO );
        this.setRowCount( response.getTotalCount() );
        theaters = response.getElements();
      }

      return theaters;
    }
  }

  public void validateRows()
  {
    if( CollectionUtils.isNotEmpty( theatersSelected ) && theatersSelected.size() >= 1 )
    {
      int emaphoreRed = 0;
      for( TheaterTO theaterTO : theatersSelected )
      {
        if( theaterTO.getImageSemaphore().equals( "sem_rj" ) )
        {
          emaphoreRed++;
        }
      }
      if( emaphoreRed > 0 )
      {
        validationFail();
      }
      else
      {
        this.theaterIdSelected = theatersSelected.get( 0 ).getId();
      }
    }
    else
    {
      this.theaterIdSelected = null;
    }
  }

  public void loadTemplate( AjaxBehaviorEvent event )
  {
    clearTheaters();
    if( weekIdSelected != null && !weekIdSelected.equals( 0L ) )
    {
      EmailTemplateTO emailTemplateTO = this.bookingServiceIntegratorEJB.getEmailTemplateTheater( new WeekTO(
          weekIdSelected.intValue() ), regionIdSelected );
      if( emailTemplateTO != null )
      {
        subject = emailTemplateTO.getSubject();
        template = emailTemplateTO.getBody();
      }
    }
    else
    {
      setWeekIdSelected( null );
      subject = StringUtils.EMPTY;
      template = StringUtils.EMPTY;
    }
  }

  public void clearTheaters()
  {
    this.theaters.clear();
  }

  private boolean isEditableWeek( WeekTO weekTO )
  {
    boolean isEditableWeek = true;
    Date today = DateUtils.truncate( Calendar.getInstance().getTime(), Calendar.DATE );
    if( weekTO == null || !today.before( weekTO.getStartingDayWeek() ) )
    {
      isEditableWeek = false;
    }
    return isEditableWeek;
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

    try
    {
      fileTO = bookingServiceIntegratorEJB.getWeeklyBookingReportByTheater( weekId, theaterId );
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

}
