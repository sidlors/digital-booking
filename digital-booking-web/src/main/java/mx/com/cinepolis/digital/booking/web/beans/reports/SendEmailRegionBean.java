package mx.com.cinepolis.digital.booking.web.beans.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.AbstractTO;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.EmailTemplateTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionEmailTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.integration.booking.BookingServiceIntegratorEJB;
import mx.com.cinepolis.digital.booking.web.beans.BaseManagedBean;
import mx.com.cinepolis.digital.booking.web.util.ResourceBundleUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ManagedBean(name = "sendEmailRegionBean")
@ViewScoped
public class SendEmailRegionBean extends BaseManagedBean
{
  private static final Logger LOG = LoggerFactory.getLogger( SendEmailRegionBean.class );
  private static final String NO_SELECTED_WEEK_REGION_ERROR_TEXT = "reports.sendemailregion.mesgerror.noSelectedWeekRegionText";

  private static final long serialVersionUID = 6775849662361044272L;

  private List<CatalogTO> regions;
  private List<WeekTO> weeks;
  private String subject;
  private String template;
  private Long weekIdSelected;
  private Long regionIdSelected;
  private List<FileTO> files;
  private FileTO selectedFile;
  private int idFile;
  private Long selectedFileId;
  private Boolean isWeekEditable = false;

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
    idFile = 0;

    weeks = bookingServiceIntegratorEJB.findWeeksActive( abstractTO );
    regions = bookingServiceIntegratorEJB.findAllActiveRegions( abstractTO );
    files = new ArrayList<FileTO>();

    for( WeekTO weekTO : weeks )
    {
      if( weekTO.isActiveWeek() )
      {
        setWeekIdSelected( weekTO.getIdWeek().longValue() );
        EmailTemplateTO emailTemplateTO = this.bookingServiceIntegratorEJB.getEmailTemplateRegion( weekTO,
          regionIdSelected );
        if( emailTemplateTO != null )
        {
          subject = emailTemplateTO.getSubject();
          template = emailTemplateTO.getBody();
        }
      }
    }
  }

  /**
   * Método que valida que el objeto seleccionado no sea nulo
   * 
   * @return true si no se selecciono registro
   */
  public Boolean validateSelection()
  {
    Boolean isValid = true;
    isValid &= !(weekIdSelected == null || weekIdSelected.equals( 0L ));
    isValid &= !(regionIdSelected == null || regionIdSelected.equals( 0L ));
    isValid &= StringUtils.isNotEmpty( template );
    if( !isValid )
    {
      createMessageError( NO_SELECTED_WEEK_REGION_ERROR_TEXT );
      isValid = false;
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
      regionEmailTO.setSubject( subject );
      regionEmailTO.setMessage( this.template );
      regionEmailTO.setFiles( this.files );
      this.bookingServiceIntegratorEJB.sendRegionEmail( regionEmailTO );
    }
    else
    {
      validationFail();
    }
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
    isWeekEditable = this.isEditableWeek( foundWeekTO );
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

  public void loadTemplate( AjaxBehaviorEvent event )
  {
    this.resetFiles();
    if( weekIdSelected != null && !weekIdSelected.equals( 0L ) )
    {
      EmailTemplateTO emailTemplateTO = this.bookingServiceIntegratorEJB.getEmailTemplateRegion( new WeekTO(
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

  public void resetFiles()
  {
    this.files = new ArrayList<FileTO>();
    this.idFile = 0;
  }

  /**
   * Metodo para agregar un attachment
   * 
   * @return
   * @throws IOException
   */
  public void handleFileUpload( FileUploadEvent event ) throws IOException
  {
    if( validateSelection() )
    {
      if( this.files.size() == 2 )
      {
        throw DigitalBookingExceptionBuilder
            .build( DigitalBookingExceptionCode.SEND_EMAIL_REGION_ERROR_CAN_ONLY_UPLOAD_UP_TO_TWO_FILES );
      }
      FileTO fileSaved = new FileTO();
      fileSaved.setName( event.getFile().getFileName() );
      fileSaved.setFile( event.getFile().getContents() );
      fileSaved.setId( Long.valueOf( this.idFile++ ) );

      this.files.add( fileSaved );
      String summary = ResourceBundleUtil.getMessageFormBundle( "reports.sendemailregion.success" );
      FacesMessage msg = new FacesMessage( summary );
      FacesContext.getCurrentInstance().addMessage( null, msg );
    }
  }

  /**
   * @return the files
   */
  public List<FileTO> getFiles()
  {
    return files;
  }

  /**
   * @param files the files to set
   */
  public void setFiles( List<FileTO> files )
  {
    this.files = files;
  }

  /**
   * @return the selectedFile
   */
  public FileTO getSelectedFile()
  {
    return selectedFile;
  }

  /**
   * @param selectedFile the selectedFile to set
   */
  public void setSelectedFile( FileTO selectedFile )
  {
    this.selectedFile = selectedFile;
  }

  /**
   * @return the selectedFileId
   */
  public Long getSelectedFileId()
  {
    return selectedFileId;
  }

  /**
   * @param selectedFileId the selectedFileId to set
   */
  public void setSelectedFileId( Long selectedFileId )
  {
    this.selectedFileId = selectedFileId;
  }

  public void removeAttachment()
  {
    if( this.selectedFile != null )
    {
      this.files.remove( this.selectedFile );
    }
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
    StreamedContent file = null;

    Long weekId = this.weekIdSelected;
    Long regionId = this.regionIdSelected;

    FileTO fileTO = bookingServiceIntegratorEJB.getWeeklyBookingReportByRegion( weekId, regionId );

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
