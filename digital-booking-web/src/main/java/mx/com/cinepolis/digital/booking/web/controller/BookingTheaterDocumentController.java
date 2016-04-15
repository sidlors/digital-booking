package mx.com.cinepolis.digital.booking.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;

import org.apache.commons.lang.StringEscapeUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "bookingTheaterDocumentController")
@ApplicationScoped
public class BookingTheaterDocumentController
{

  private static final Logger LOG = LoggerFactory.getLogger( BookingTheaterDocumentController.class );

  @EJB
  private BookingServiceIntegratorEJB bookingServiceIntegratorEJB;

  @PostConstruct
  public void init()
  {

  }

  /**
   * @return the file
   */
  public StreamedContent getFile()
  {
    StreamedContent file = null;
    FacesContext ctx = FacesContext.getCurrentInstance();
    Map<String, String> params = ctx.getExternalContext().getRequestParameterMap();

    Long weekId = Long.valueOf( getWeekFromParams( params ) );
    Long theaterId = Long.valueOf( getTheaterFromParams( params ) );

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
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", name + ".xlsx" );
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
  
  private String getWeekFromParams( Map<String, String> params )
  {
    String weekStr = params.get( "weekId" );
    if( weekStr == null || weekStr.equals( "null" ) )
    {
      weekStr = params.get( "filterPanelForm:weekMenu_input" );
    }
    return weekStr;
  }
  
  private String getTheaterFromParams( Map<String, String> params )
  {
    String weekStr = params.get( "theaterId" );
    if( weekStr == null || weekStr.equals( "null" ) )
    {
      weekStr = params.get( "filterPanelForm:theaterMenu_input" );
    }
    return weekStr;
  }
}
