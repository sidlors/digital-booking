package mx.com.cinepolis.digital.booking.web.beans.systemconfiguration;

import java.io.Serializable;
import java.lang.reflect.Method;
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

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.query.ModelQuery;
import mx.com.cinepolis.digital.booking.commons.query.SystemLogQuery;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.SystemLogTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.integration.systemlog.ServiceSystemLogIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data trailers screen controller
 * 
 * @author jreyesv
 */
@ManagedBean(name = "systemLogBean")
@ViewScoped
public class SystemLogBean extends BaseManagedBean implements Serializable
{
  /**
   * Serial version
   */
  private static final long serialVersionUID = 3427553524978395751L;

  /**
   * Constants
   */
  private static final Logger LOG = LoggerFactory.getLogger( SystemLogBean.class );

  /**
   * Services
   */
  @EJB
  private ServiceSystemLogIntegratorEJB serviceSystemLogIntegratorEJB;

  /***
   * Variables
   */
  private SystemLogLazyDatamodel systemLogTOList;
  private List<CatalogTO> operationsList;
  private List<CatalogTO> processList;
  // Filters
  private String firstNameFilter;
  private String lastNameFilter;
  private String userNameFilter;
  private Long operationFilter;
  private Long processFilter;
  private Date startDateFilter;
  private Date finalDateFilter;
  private Date startTimeFilter;
  private Date finalTimeFilter;

  /**
   * Constructor, initializes the values for System Log screen.
   */
  @PostConstruct
  public void init()
  {
    this.resetFilters();
    this.operationsList = this.serviceSystemLogIntegratorEJB.getAllOperations();
    this.processList = this.serviceSystemLogIntegratorEJB.getAllProcess();
    this.systemLogTOList = new SystemLogLazyDatamodel( serviceSystemLogIntegratorEJB, getUserId() );
  }

  /**
   * Method that initializes the date range for searching.
   */
  private void initDateRange()
  {
    int daysToSubtract = -30;
    Calendar startDateTime = Calendar.getInstance();
    startDateTime.setTime( DateUtils.addDays( startDateTime.getTime(), daysToSubtract ) );
    Calendar finalDateTime = Calendar.getInstance();
    this.setTimeByDefault( startDateTime, finalDateTime );
    this.startDateFilter = startDateTime.getTime();
    this.startTimeFilter = startDateTime.getTime();
    this.finalDateFilter = finalDateTime.getTime();
    this.finalTimeFilter = finalDateTime.getTime();
  }

  /**
   * Method that sets the time configuration by default.
   * 
   * @param startDate, with the start date.
   * @param finalDate, with the final date.
   */
  private void setTimeByDefault( Calendar startDate, Calendar finalDate )
  {
    int defaultStartHour = 00;
    int defaultStartMinute = 00;
    int defaultStartSecond = 00;
    int defaultFinalHour = 23;
    int defaultFinalMinute = 59;
    int defaultFinalSecond = 59;
    startDate.set( Calendar.HOUR_OF_DAY, defaultStartHour );
    startDate.set( Calendar.MINUTE, defaultStartMinute );
    startDate.set( Calendar.SECOND, defaultStartSecond );
    finalDate.set( Calendar.HOUR_OF_DAY, defaultFinalHour );
    finalDate.set( Calendar.MINUTE, defaultFinalMinute );
    finalDate.set( Calendar.SECOND, defaultFinalSecond );
  }

  /**
   * Method that joins the date and time selected.
   * 
   * @param dateSource, with date information.
   * @param timeSource, with time information.
   * @return {@link Method that joins date and time selected.}, with result date.
   */
  private Date convertDateTime( Date dateSource, Date timeSource )
  {
    Date response = null;
    if( this.bothDatesSelectedOrNot( this.startDateFilter, this.finalDateFilter ) && this.startDateFilter != null )
    {
      if( this.bothDatesSelectedOrNot( this.startTimeFilter, this.finalTimeFilter ) && this.startTimeFilter == null )
      {
        Calendar startDateTime = Calendar.getInstance();
        Calendar finalDateTime = Calendar.getInstance();
        this.setTimeByDefault( startDateTime, finalDateTime );
        this.startTimeFilter = startDateTime.getTime();
        this.finalTimeFilter = finalDateTime.getTime();
        
      }
      if(timeSource==null)
      {
        Calendar calTime=Calendar.getInstance();
        this.setTimeByDefault( Calendar.getInstance(), calTime );
        timeSource=calTime.getTime();
      }
      Calendar calendarDate = DateUtils.toCalendar( dateSource );
      Calendar calendarTime = DateUtils.toCalendar( timeSource );
      calendarDate.set( Calendar.HOUR_OF_DAY, calendarTime.get( Calendar.HOUR_OF_DAY ) );
      calendarDate.set( Calendar.MINUTE, calendarTime.get( Calendar.MINUTE ) );
      calendarDate.set( Calendar.SECOND, calendarTime.get( Calendar.SECOND ) );
      response = calendarDate.getTime();
      LOG.info( "********************************* Converted date : " + response.toString() );
    }
    return response;
  }

  /**
   * Method that sets the search criteria.
   */
  public void setFilters()
  {
    if( validateDatesAndTimes() )
    {
      this.startDateFilter = this.convertDateTime( this.startDateFilter, this.startTimeFilter );
      this.finalDateFilter = this.convertDateTime( this.finalDateFilter, this.finalTimeFilter );
      if(this.startDateFilter.equals( this.finalDateFilter ))
      {
        this.compareTimes();
      }
      if( this.systemLogTOList != null && isValidDateAndTimeRange() )
      {
        this.systemLogTOList.setFirstNameFilter( this.firstNameFilter );
        this.systemLogTOList.setLastNameFilter( this.lastNameFilter );
        this.systemLogTOList.setUserNameFilter( this.userNameFilter );
        this.systemLogTOList.setOperationFilter( this.operationFilter );
        this.systemLogTOList.setProcessFilter( this.processFilter );
        this.systemLogTOList.setStartDateFilter( this.startDateFilter );
        this.systemLogTOList.setFinalDateFilter( this.finalDateFilter );
      }
    }
  }

  /**
   * method for compare the start time and final time.
   * 
   */
  private void compareTimes()
  {
    Calendar startTime=Calendar.getInstance();
    startTime.setTime( this.startDateFilter);
    Calendar finalTime=Calendar.getInstance();
    finalTime.setTime( this.startDateFilter);
    
    Calendar time=Calendar.getInstance();
    time.set( Calendar.HOUR, 00 );
    time.set( Calendar.MINUTE, 00 );
    time.set( Calendar.SECOND, 00 );
    
    if(startTime.getTime().equals( finalTime.getTime() ) )
    {
      if(startTime.get( Calendar.HOUR )==00&&startTime.get( Calendar.MINUTE)==00&&startTime.get( Calendar.SECOND )==00)
      {
        this.setTimeByDefault( startTime, finalTime );
      }
    }
  }
  /**
   * Method that resets the search criteria.
   */
  public void resetFilters()
  {
    this.firstNameFilter = null;
    this.lastNameFilter = null;
    this.userNameFilter = null;
    this.operationFilter = null;
    this.processFilter = null;
    this.initDateRange();
    this.setFilters();
  }

  /**
   * Method that validates whether final date and time is before than start date and time.
   * 
   * @return response, with the validation result.
   */
  private boolean isValidDateAndTimeRange()
  {
    boolean response = true;
    if( this.finalDateFilter != null && this.finalDateFilter.before( this.startDateFilter ) )
    {
      response = false;
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.LOG_FINAL_DATE_BEFORE_START_DATE );
    }
    return response;
  }

  /**
   * Method that validates whether date and time ranges are valid.
   * 
   * @return isValid, with the result of validation.
   */
  private boolean validateDatesAndTimes()
  {
    boolean isValid = true;
    if( !this.bothDatesSelectedOrNot( this.startDateFilter, this.finalDateFilter ) )
    {
      isValid = false;
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.LOG_INVALID_DATE_RANGE );
    }
    else if( !this.bothDatesSelectedOrNot( this.startTimeFilter, this.finalTimeFilter ) )
    {
      isValid = false;
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.LOG_INVALID_TIME_RANGE );
    }
    return isValid;
  }

  /**
   * Method that evaluates whether date and time ranges was correctly selected.
   * 
   * @param startDate, with the start date selected.
   * @param finalDate, with the final date selected.
   * @return the result of evaluation.
   */
  private boolean bothDatesSelectedOrNot( Date startDate, Date finalDate )
  {
    return (startDate != null && finalDate != null) || (startDate == null && finalDate == null);
  }

  /**
   * @return the firstNameFilter
   */
  public String getFirstNameFilter()
  {
    return firstNameFilter;
  }

  /**
   * @param firstNameFilter the firstNameFilter to set
   */
  public void setFirstNameFilter( String firstNameFilter )
  {
    this.firstNameFilter = firstNameFilter;
  }

  /**
   * @return the lastNameFilter
   */
  public String getLastNameFilter()
  {
    return lastNameFilter;
  }

  /**
   * @param lastNameFilter the lastNameFilter to set
   */
  public void setLastNameFilter( String lastNameFilter )
  {
    this.lastNameFilter = lastNameFilter;
  }

  /**
   * @return the userNameFilter
   */
  public String getUserNameFilter()
  {
    return userNameFilter;
  }

  /**
   * @param userNameFilter the userNameFilter to set
   */
  public void setUserNameFilter( String userNameFilter )
  {
    this.userNameFilter = userNameFilter;
  }

  /**
   * @return the operationFilter
   */
  public Long getOperationFilter()
  {
    return operationFilter;
  }

  /**
   * @param operationFilter the operationFilter to set
   */
  public void setOperationFilter( Long operationFilter )
  {
    this.operationFilter = operationFilter;
  }

  /**
   * @return the processFilter
   */
  public Long getProcessFilter()
  {
    return processFilter;
  }

  /**
   * @param processFilter the processFilter to set
   */
  public void setProcessFilter( Long processFilter )
  {
    this.processFilter = processFilter;
  }

  /**
   * @return the startDateFilter
   */
  public Date getStartDateFilter()
  {
    return startDateFilter;
  }

  /**
   * @param startDateFilter the startDateFilter to set
   */
  public void setStartDateFilter( Date startDateFilter )
  {
    this.startDateFilter = startDateFilter;
  }

  /**
   * @return the finalDateFilter
   */
  public Date getFinalDateFilter()
  {
    return finalDateFilter;
  }

  /**
   * @param finalDateFilter the finalDateFilter to set
   */
  public void setFinalDateFilter( Date finalDateFilter )
  {
    this.finalDateFilter = finalDateFilter;
  }

  /**
   * @return the startTimeFilter
   */
  public Date getStartTimeFilter()
  {
    return startTimeFilter;
  }

  /**
   * @param startTimeFilter the startTimeFilter to set
   */
  public void setStartTimeFilter( Date startTimeFilter )
  {
    this.startTimeFilter = startTimeFilter;
  }

  /**
   * @return the finalTimeFilter
   */
  public Date getFinalTimeFilter()
  {
    return finalTimeFilter;
  }

  /**
   * @param finalTimeFilter the finalTimeFilter to set
   */
  public void setFinalTimeFilter( Date finalTimeFilter )
  {
    this.finalTimeFilter = finalTimeFilter;
  }

  /**
   * @return the systemLogTOList
   */
  public SystemLogLazyDatamodel getSystemLogTOList()
  {
    return systemLogTOList;
  }

  /**
   * @return the operationsList
   */
  public List<CatalogTO> getOperationsList()
  {
    return operationsList;
  }

  /**
   * @param operationsList the operationsList to set
   */
  public void setOperationsList( List<CatalogTO> operationsList )
  {
    this.operationsList = operationsList;
  }

  /**
   * @return the processList
   */
  public List<CatalogTO> getProcessList()
  {
    return processList;
  }

  /**
   * @param processList the processList to set
   */
  public void setProcessList( List<CatalogTO> processList )
  {
    this.processList = processList;
  }

  /**
   * Lazy data model class for System Log records list.
   * 
   * @author jreyesv
   */
  static class SystemLogLazyDatamodel extends LazyDataModel<SystemLogTO>
  {

    /**
     * Serial version
     */
    private static final long serialVersionUID = 6808087254436758725L;

    // Filters
    private String firstNameFilter;
    private String lastNameFilter;
    private String userNameFilter;
    private Long operationFilter;
    private Long processFilter;
    private Date startDateFilter;
    private Date finalDateFilter;
    private Long userId;
    private ServiceSystemLogIntegratorEJB serviceSystemLogIntegratorEJB;

    public SystemLogLazyDatamodel( ServiceSystemLogIntegratorEJB serviceSystemLogIntegratorEJB, Long userId )
    {
      this.serviceSystemLogIntegratorEJB = serviceSystemLogIntegratorEJB;
      this.userId = userId;
    }

    @Override
    public List<SystemLogTO> load( int first, int pageSize, String sortField, SortOrder sortOrder,
        Map<String, String> filters )
    {
      int page = first / pageSize;
      PagingRequestTO pagingRequestTO = new PagingRequestTO();
      pagingRequestTO.setUserId( userId );
      pagingRequestTO.setPage( page );
      pagingRequestTO.setPageSize( pageSize );
      pagingRequestTO.setSort( new ArrayList<ModelQuery>() );
      pagingRequestTO.getSort().add( SystemLogQuery.DT_OPERATION );
      pagingRequestTO.setSortOrder( mx.com.cinepolis.digital.booking.commons.query.SortOrder.ASCENDING );
      pagingRequestTO.setFilters( new HashMap<ModelQuery, Object>() );

      this.setFiltersToRequest( pagingRequestTO );
      PagingResponseTO<SystemLogTO> response = this.serviceSystemLogIntegratorEJB.getSystemLogSummary( pagingRequestTO );
      this.setRowCount( response.getTotalCount() );

      return response.getElements();
    }

    private void setFiltersToRequest( PagingRequestTO pagingRequestTO )
    {
      if( StringUtils.isNotBlank( this.firstNameFilter ) )
      {
        pagingRequestTO.getFilters().put( SystemLogQuery.PERSON_NAME, this.firstNameFilter );
      }
      if( StringUtils.isNotBlank( this.lastNameFilter ) )
      {
        pagingRequestTO.getFilters().put( SystemLogQuery.PERSON_LAST_NAME, this.lastNameFilter );
      }
      if( StringUtils.isNotBlank( this.userNameFilter ) )
      {
        pagingRequestTO.getFilters().put( SystemLogQuery.USER_NAME, this.userNameFilter );
      }
      if( this.operationFilter != null && this.operationFilter.longValue() > 0 )
      {
        pagingRequestTO.getFilters().put( SystemLogQuery.ID_OPERATION, this.operationFilter );
      }
      if( this.processFilter != null && this.processFilter.longValue() > 0 )
      {
        pagingRequestTO.getFilters().put( SystemLogQuery.ID_PROCESS, this.processFilter );
      }
      if( this.startDateFilter != null )
      {
        pagingRequestTO.getFilters().put( SystemLogQuery.START_DATE, this.startDateFilter );
      }
      if( this.finalDateFilter != null )
      {
        pagingRequestTO.getFilters().put( SystemLogQuery.FINAL_DATE, this.finalDateFilter );
      }
    }

    /**
     * @return the firstNameFilter
     */
    public String getFirstNameFilter()
    {
      return firstNameFilter;
    }

    /**
     * @param firstNameFilter the firstNameFilter to set
     */
    public void setFirstNameFilter( String firstNameFilter )
    {
      this.firstNameFilter = firstNameFilter;
    }

    /**
     * @return the lastNameFilter
     */
    public String getLastNameFilter()
    {
      return lastNameFilter;
    }

    /**
     * @param lastNameFilter the lastNameFilter to set
     */
    public void setLastNameFilter( String lastNameFilter )
    {
      this.lastNameFilter = lastNameFilter;
    }

    /**
     * @return the userNameFilter
     */
    public String getUserNameFilter()
    {
      return userNameFilter;
    }

    /**
     * @param userNameFilter the userNameFilter to set
     */
    public void setUserNameFilter( String userNameFilter )
    {
      this.userNameFilter = userNameFilter;
    }

    /**
     * @return the operationFilter
     */
    public Long getOperationFilter()
    {
      return operationFilter;
    }

    /**
     * @param operationFilter the operationFilter to set
     */
    public void setOperationFilter( Long operationFilter )
    {
      this.operationFilter = operationFilter;
    }

    /**
     * @return the processFilter
     */
    public Long getProcessFilter()
    {
      return processFilter;
    }

    /**
     * @param processFilter the processFilter to set
     */
    public void setProcessFilter( Long processFilter )
    {
      this.processFilter = processFilter;
    }

    /**
     * @return the startDateFilter
     */
    public Date getStartDateFilter()
    {
      return startDateFilter;
    }

    /**
     * @param startDateFilter the startDateFilter to set
     */
    public void setStartDateFilter( Date startDateFilter )
    {
      this.startDateFilter = startDateFilter;
    }

    /**
     * @return the finalDateFilter
     */
    public Date getFinalDateFilter()
    {
      return finalDateFilter;
    }

    /**
     * @param finalDateFilter the finalDateFilter to set
     */
    public void setFinalDateFilter( Date finalDateFilter )
    {
      this.finalDateFilter = finalDateFilter;
    }

  }

}