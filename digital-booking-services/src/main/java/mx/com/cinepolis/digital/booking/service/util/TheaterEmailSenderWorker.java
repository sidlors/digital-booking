package mx.com.cinepolis.digital.booking.service.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.mail.MessagingException;

import mx.com.cinepolis.digital.booking.commons.constants.Configuration;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.EmailTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.to.reports.WeeklyBookingReportTheaterTO;
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
 * This class sends the booking week for theater
 * 
 * @author gsegura
 * @since 0.2.0
 */
public class TheaterEmailSenderWorker implements Runnable
{
  private static final Logger LOG = LoggerFactory.getLogger( TheaterEmailSenderWorker.class );
  private EmailSenderEJB emailSenderEJB;
  private UserDO user;
  private Integer idWeek;
  private Long idTheater;
  private WeekDAO weekDAO;
  private ConfigurationDAO configurationDAO;
  private TheaterDAO theaterDAO;
  private ServiceReportsEJB serviceReportsEJB;
  private ReportDAO reportDAO;
  private String template;
  private String subject;

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
   * @param idTheater the idTheater to set
   */
  public void setIdTheater( Long idTheater )
  {
    this.idTheater = idTheater;
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
   * @param theaterDAO the theaterDAO to set
   */
  public void setTheaterDAO( TheaterDAO theaterDAO )
  {
    this.theaterDAO = theaterDAO;
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
      emailSenderEJB.sendEmail( createEmail( idWeek, idTheater, template, subject ) );
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

  private EmailTO createEmail( Integer idWeek, Long idTheater, String template, String subject )
  {
    TheaterDO theaterDO = this.theaterDAO.find( idTheater.intValue() );
    validateEmail( theaterDO );

    WeekTO weekTO = this.weekDAO.getWeek( idWeek.intValue() );

    EmailTO emailTO = new EmailTO();
    emailTO.setRecipients( new ArrayList<String>() );
    emailTO.setRecipientsCC( new ArrayList<String>() );
    emailTO.setRecipientsBCC( new ArrayList<String>() );
    if( StringUtils.isNotEmpty( theaterDO.getIdEmail().getDsEmail() ) )
    {
      emailTO.getRecipients().addAll( Arrays.asList( theaterDO.getIdEmail().getDsEmail().split( "," ) ) );
    }

    if( StringUtils.isNotEmpty( theaterDO.getDsCCEmail() ) )
    {
      emailTO.getRecipientsCC().addAll( Arrays.asList( theaterDO.getDsCCEmail().split( "," ) ) );
    }

    String subj = subject;
    if( subj == null )
    {
      subj = configurationDAO.findByParameterName( Configuration.EMAIL_BOOKING_THEATER_SUBJECT.getParameter() )
          .getDsValue();
    }
    WeeklyBookingReportTheaterTO theater = reportDAO.findWeeklyBookingReportTheaterTO( idWeek.longValue(), idTheater,
      Language.SPANISH );
    subj = subj.replace( "$weekNumber",
      String.valueOf( weekTO.getNuWeek() ).concat( "-" ).concat( theater.getTheaterName() ) );
    String message = template;
    String emailBCC = configurationDAO.findByParameterName( Configuration.EMAIL_BOOKING_THEATER_BCC.getParameter() )
        .getDsValue();
    emailTO.setSubject( subj );
    emailTO.setMessage( message );
    emailTO.setFiles( Arrays.asList( this.serviceReportsEJB.getWeeklyBookingReportByTheater( idWeek.longValue(),
      idTheater ) ) );

    extractEmailLoggedUser( emailTO );
    emailTO.getRecipientsCC().addAll( Arrays.asList( emailBCC ) );
    return emailTO;
  }

  /**
   * method for test extraction of the mail
   * 
   * @param emailTO
   */
  private void extractEmailLoggedUser( EmailTO emailTO )
  {
    if( this.user != null && this.user.getIdPerson() != null
        && CollectionUtils.isNotEmpty( this.user.getIdPerson().getEmailDOList() ) )
    {
      for( EmailDO email : this.user.getIdPerson().getEmailDOList() )
      {
        emailTO.getRecipientsCC().add( email.getDsEmail() );
      }
    }
  }

  /**
   * Method for test validation of the email
   * 
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

  /**
   * @return the reportDAO
   */
  public ReportDAO getReportDAO()
  {
    return reportDAO;
  }

  /**
   * @param reportDAO the reportDAO to set
   */
  public void setReportDAO( ReportDAO reportDAO )
  {
    this.reportDAO = reportDAO;
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
