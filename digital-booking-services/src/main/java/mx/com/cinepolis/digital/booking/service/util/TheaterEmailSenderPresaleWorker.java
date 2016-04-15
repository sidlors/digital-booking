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
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.model.EmailDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ReportDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.TheaterDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.WeekDAO;
import mx.com.cinepolis.digital.booking.service.reports.ServiceReportsEJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class sends the booking presale for region
 * 
 * @author jcarbajal
 */
public class TheaterEmailSenderPresaleWorker implements Runnable
{

  private static final Logger LOG = LoggerFactory.getLogger( TheaterEmailSenderPresaleWorker.class );
  private EmailSenderEJB emailSenderEJB;
  private ServiceReportsEJB serviceReportsEJB;
  private UserDO user;
  private Long idWeek;
  private Long idTheater;
  private List<EventTO> eventSelected;
  private TheaterDAO theaterDAO;
  private WeekDAO weekDAO;
  private ConfigurationDAO configurationDAO;
  private ReportDAO reportDAO;
  private String subject;
  private String theaterName;
  
  
  @Override
  public void run()
  {
    try
    {
      LOG.info( "Sending email of presales by Theater..." );
      emailSenderEJB.sendEmail( this.createEmail() );
      LOG.info( "Email of presales by Theater sent." );
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
    
    TheaterDO theaterDO = this.theaterDAO.find( idTheater.intValue() );
    validateEmail( theaterDO );

    WeekTO weekTO = this.weekDAO.getWeek( idWeek.intValue() );

    EmailTO emailTO = new EmailTO();
    emailTO.setRecipients( new ArrayList<String>() );
    emailTO.setRecipientsCC( new ArrayList<String>() );
    emailTO.setRecipientsBCC( new ArrayList<String>() );
    if ( StringUtils.isNotEmpty( theaterDO.getIdEmail().getDsEmail() ) )
    {
      emailTO.getRecipients().addAll( Arrays.asList( theaterDO.getIdEmail().getDsEmail().split( "," ) ) );
    }

    if ( StringUtils.isNotEmpty( theaterDO.getDsCCEmail() ) )
    {
      emailTO.getRecipientsCC().addAll( Arrays.asList( theaterDO.getDsCCEmail().split( "," ) ) );
    }
    
    String subject = this.subject;
    subject = subject.replace( "$weekNumber", String.valueOf( weekTO.getNuWeek() ).concat( "-" ).concat( theaterName ));
    String message = configurationDAO.findByParameterName( Configuration.EMAIL_BOOKING_THEATER_BODY.getParameter() )
        .getDsValue();
    String emailBCC = configurationDAO.findByParameterName( Configuration.EMAIL_BOOKING_THEATER_BCC.getParameter() )
        .getDsValue();
    emailTO.setSubject( subject );
    emailTO.setMessage( message );
    emailTO.setFiles( Arrays.asList( this.serviceReportsEJB.getWeeklyBookingReportPresaleByTheater( eventSelected, idWeek, idTheater ) ));
    
    extractEmailLoggedUser( emailTO );
    emailTO.getRecipientsCC().addAll( Arrays.asList( emailBCC ) );
    return emailTO;
  }
  /**
   * Method that sets the email recipients CC
   * 
   * @param emailTO
   */
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
   * Validate if the theater has email 
   * @param theaterDO
   */
  private void validateEmail( TheaterDO theaterDO )
  {
    if( theaterDO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.INEXISTENT_THEATER );
    }
    if( theaterDO.getIdEmail() == null || StringUtils.isBlank( theaterDO.getIdEmail().getDsEmail() ) )
    {
      TheaterTO t = (TheaterTO) new TheaterDOToTheaterTOSimpleTransformer( Language.ENGLISH ).transform( theaterDO );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.ERROR_THEATER_HAS_NO_EMAIL,
        new Object[] { t.getName() } );
    }
  }
  public EmailSenderEJB getEmailSenderEJB()
  {
    return emailSenderEJB;
  }
/**
 * 
 * @param emailSenderEJB the emailSenderEJB to set 
 */
  public void setEmailSenderEJB( EmailSenderEJB emailSenderEJB )
  {
    this.emailSenderEJB = emailSenderEJB;
  }

  public ServiceReportsEJB getServiceReportsEJB()
  {
    return serviceReportsEJB;
  }
/**
 * 
 * @param serviceReportsEJB the serviceReportsEJB to set 
 */
  public void setServiceReportsEJB( ServiceReportsEJB serviceReportsEJB )
  {
    this.serviceReportsEJB = serviceReportsEJB;
  }
/**
 * 
 * @return user
 */
  public UserDO getUser()
  {
    return user;
  }

  public void setUser( UserDO user )
  {
    this.user = user;
  }

  public Long getIdWeek()
  {
    return idWeek;
  }

  public void setIdWeek( Long idWeek )
  {
    this.idWeek = idWeek;
  }

 
  public List<EventTO> getEventSelected()
  {
    return eventSelected;
  }
/**
 * 
 * @param eventSelected the eventSelected to set 
 */
  public void setEventSelected( List<EventTO> eventSelected )
  {
    this.eventSelected = eventSelected;
  }
  public ConfigurationDAO getConfigurationDAO()
  {
    return configurationDAO;
  }

  /**
   * 
   * @param configurationDAO the configurationDAO to set 
   */
  public void setConfigurationDAO( ConfigurationDAO configurationDAO )
  {
    this.configurationDAO = configurationDAO;
  }

  public Long getIdTheater()
  {
    return idTheater;
  }
/**
 * 
 * @param idTheater the idTheater to set 
 */
  public void setIdTheater( Long idTheater )
  {
    this.idTheater = idTheater;
  }

  /**
   * 
   * @return theaterDAO
   */
  public TheaterDAO getTheaterDAO()
  {
    return theaterDAO;
  }
/**
 * 
 * @param theaterDAO the theaterDAO to set 
 */
  public void setTheaterDAO( TheaterDAO theaterDAO )
  {
    this.theaterDAO = theaterDAO;
  }

  public WeekDAO getWeekDAO()
  {
    return weekDAO;
  }

  public void setWeekDAO( WeekDAO weekDAO )
  {
    this.weekDAO = weekDAO;
  }

  public ReportDAO getReportDAO()
  {
    return reportDAO;
  }

  public void setReportDAO( ReportDAO reportDAO )
  {
    this.reportDAO = reportDAO;
  }

  public String getSubject()
  {
    return subject;
  }

  public void setSubject( String subject )
  {
    this.subject = subject;
  }

  public String getTheaterName()
  {
    return theaterName;
  }

  /**
   * 
   * @param theaterName the theaterName to set 
   */
  public void setTheaterName( String theaterName )
  {
    this.theaterName = theaterName;
  }

 
}
