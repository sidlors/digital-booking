package mx.com.cinepolis.digital.booking.service.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;

import mx.com.cinepolis.digital.booking.commons.constants.Configuration;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.EmailTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.model.EmailDO;
import mx.com.cinepolis.digital.booking.model.PersonDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.RegionLanguageDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;
import mx.com.cinepolis.digital.booking.service.reports.ServiceReportsEJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class sends the booking week for theater
 * 
 * @author gsegura
 * @since 0.2.0
 */
public class RegionEmailSenderWorker implements Runnable
{
  private static final Logger LOG = LoggerFactory.getLogger( RegionEmailSenderWorker.class );
  private EmailSenderEJB emailSenderEJB;

  private UserDO user;
  private Integer idWeek;
  private Long idRegion;
  private String message;
  private String subject;
  private List<FileTO> attachments;
  private WeekDAO weekDAO;
  private ConfigurationDAO configurationDAO;
  private RegionDAO regionDAO;
  private ServiceReportsEJB serviceReportsEJB;

  /**
   * @param emailSenderEJB the emailSenderEJB to set
   */
  public void setEmailSenderEJB( EmailSenderEJB emailSenderEJB )
  {
    this.emailSenderEJB = emailSenderEJB;
  }

  /**
   * @param idWeek the idWeek to set
   */
  public void setIdWeek( Integer idWeek )
  {
    this.idWeek = idWeek;
  }

  /**
   * @param idRegion the idRegion to set
   */
  public void setIdRegion( Long idRegion )
  {
    this.idRegion = idRegion;
  }

  /**
   * @param message the message to set
   */
  public void setMessage( String message )
  {
    this.message = message;
  }

  /**
   * @param weekDAO the weekDAO to set
   */
  public void setWeekDAO( WeekDAO weekDAO )
  {
    this.weekDAO = weekDAO;
  }

  /**
   * @param configurationDAO the configurationDAO to set
   */
  public void setConfigurationDAO( ConfigurationDAO configurationDAO )
  {
    this.configurationDAO = configurationDAO;
  }

  /**
   * @param regionDAO the regionDAO to set
   */
  public void setRegionDAO( RegionDAO regionDAO )
  {
    this.regionDAO = regionDAO;
  }

  /**
   * @param serviceReportsEJB the serviceReportsEJB to set
   */
  public void setServiceReportsEJB( ServiceReportsEJB serviceReportsEJB )
  {
    this.serviceReportsEJB = serviceReportsEJB;
  }

  /**
   * @param user the user to set
   */
  public void setUser( UserDO user )
  {
    this.user = user;
  }

  @Override
  public void run()
  {
    try
    {
      LOG.info( "Sending email..." );
      emailSenderEJB.sendEmail( createEmail() );
      LOG.info( "Email sent." );
    }
    catch( MessagingException e )
    {
      LOG.error( e.getMessage(), e );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.ERROR_SENDING_EMAIL_MESSAGE, e );
    }
    catch( IOException e )
    {
      LOG.error( e.getMessage(), e );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.ERROR_SENDING_EMAIL_MESSAGE, e );
    }

  }

  private EmailTO createEmail()
  {
    RegionDO region = this.regionDAO.find( this.idRegion.intValue() );
    List<String> emails = new ArrayList<String>();
    extractRegionEmailList( region, emails );
    extractRegionTheaterEmailList( region, emails );

    WeekTO weekTO = this.weekDAO.getWeek( idWeek.intValue() );

    EmailTO emailTO = new EmailTO();
    String subj = subject;
    if( subj == null )
    {
      subj = configurationDAO.findByParameterName( Configuration.EMAIL_BOOKING_REGION_SUBJECT.getParameter() )
          .getDsValue();
    }
    subj = subj.replace( "$weekNumber", String.valueOf( weekTO.getNuWeek() ) );
    subj = subj.replace( "$regionName", String.valueOf( getRegionName( region.getRegionLanguageDOList() ) ) );
    String emailBCC = configurationDAO.findByParameterName( Configuration.EMAIL_BOOKING_REGION_BCC.getParameter() )
        .getDsValue();
    emailTO.setSubject( subj );
    emailTO.setMessage( this.message );

    emailTO.setFiles( new ArrayList<FileTO>() );
    emailTO.getFiles().add( this.serviceReportsEJB.getWeeklyBookingReportByRegion( idWeek.longValue(), this.idRegion ) );
    if( CollectionUtils.isNotEmpty( this.attachments ) )
    {
      for( FileTO fileTO : attachments )
      {
        emailTO.getFiles().add( fileTO );
      }
    }

    emailTO.setRecipients( emails );
    extractEmailLoggedUser( emailTO );
    emailTO.setRecipientsBCC( Arrays.asList( emailBCC ) );
    return emailTO;
  }

  private void extractRegionTheaterEmailList( RegionDO region, List<String> emails )
  {
    if( CollectionUtils.isNotEmpty( region.getTheaterDOList() ) )
    {
      for( TheaterDO theater : region.getTheaterDOList() )
      {
        if( theater.getIdEmail() != null && StringUtils.isNotBlank( theater.getIdEmail().getDsEmail() ) )
        {
          emails.add( theater.getIdEmail().getDsEmail() );
        }
      }
    }
  }

  private void extractRegionEmailList( RegionDO region, List<String> emails )
  {
    if( CollectionUtils.isNotEmpty( region.getPersonDOList() ) )
    {
      for( PersonDO person : region.getPersonDOList() )
      {
        if( CollectionUtils.isNotEmpty( person.getEmailDOList() ) )
        {
          for( EmailDO email : person.getEmailDOList() )
          {
            emails.add( email.getDsEmail() );
          }
        }
      }
    }
  }

  private void extractEmailLoggedUser( EmailTO emailTO )
  {
    if( this.user != null && this.user.getIdPerson() != null
        && CollectionUtils.isNotEmpty( this.user.getIdPerson().getEmailDOList() ) )
    {
      emailTO.setRecipientsCC( new ArrayList<String>() );
      for( EmailDO email : this.user.getIdPerson().getEmailDOList() )
      {
        emailTO.getRecipientsCC().add( email.getDsEmail() );
      }
    }
  }

  /**
   * @param attachments the attachments to set
   */
  public void setAttachments( List<FileTO> attachments )
  {
    this.attachments = attachments;
  }

  private String getRegionName( List<RegionLanguageDO> regionLanguageDOs )
  {
    String regionName = "";
    for( RegionLanguageDO regionLanguageDO : regionLanguageDOs )
    {
      if( regionLanguageDO.getIdLanguage().getIdLanguage().intValue() == Language.SPANISH.getId() )
      {
        regionName = regionLanguageDO.getDsName();
      }
    }
    return regionName;
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
