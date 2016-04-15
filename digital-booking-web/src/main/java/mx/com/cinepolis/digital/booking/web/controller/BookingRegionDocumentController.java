package mx.com.cinepolis.digital.booking.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "bookingRegionDocumentController")
@ApplicationScoped
public class BookingRegionDocumentController
{

  private static final Logger LOG = LoggerFactory.getLogger( BookingRegionDocumentController.class );

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
    Long regionId = Long.valueOf( getRegionFromParams( params ) );

    FileTO fileTO = bookingServiceIntegratorEJB.getWeeklyBookingReportByRegion( weekId, regionId );

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

  private String getRegionFromParams( Map<String, String> params )
  {
    String regionStr = params.get( "regionId" );
    if( regionStr == null || regionStr.equals( "null" ) )
    {
      regionStr = params.get( "filterPanelForm:regionMenu_input" );
    }
    return regionStr;
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

  private String getName( Long weekId )
  {
    StringBuilder sb = new StringBuilder();
    WeekTO week = this.bookingServiceIntegratorEJB.findWeek( weekId.intValue() );
    sb.append( "Programación Semana " ).append( week.getNuWeek() );
    return sb.toString();
  }
}
