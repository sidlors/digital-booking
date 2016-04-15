package mx.com.cinepolis.digital.booking.service.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;

import mx.com.cinepolis.digital.booking.commons.constants.Configuration;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.EmailTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.model.EmailDO;
import mx.com.cinepolis.digital.booking.model.PersonDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.UserDO;
import mx.com.cinepolis.digital.booking.persistence.dao.BookingDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;
import mx.com.cinepolis.digital.booking.persistence.dao.RegionDAO;
import mx.com.cinepolis.digital.booking.service.reports.ServiceReportsEJB;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.TransformerUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class sends the booking presale for region
 * 
 * @author jreyesv
 */
public class RegionEmailSenderPresaleWorker implements Runnable
{

  private static final Logger LOG = LoggerFactory.getLogger( RegionEmailSenderWorker.class );
  private EmailSenderEJB emailSenderEJB;
  private ServiceReportsEJB serviceReportsEJB;
  private UserDO user;
  private String message;
  private String subject;
  private Integer idWeek;
  private Long idRegion;
  private List<EventTO> eventSelected;
  private RegionDAO regionDAO;
  private BookingDAO bookingDAO;
  private ConfigurationDAO configurationDAO;

  private static final String ID_THEATER = "getIdTheater";
  private static final String ID_EVENT = "getIdEvent";

  /**
   * Method that executes the send email
   */
  @Override
  public void run()
  {
    try
    {
      LOG.info( "Sending email of presales by region..." );
      emailSenderEJB.sendEmail( this.createEmail() );
      LOG.info( "Email of presales by region sent." );
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

  /**
   * Method that creates a EmailTO to be sent
   * 
   * @return emailTO
   */
  private EmailTO createEmail()
  {
    EmailTO emailTO = new EmailTO();
    RegionDO region = this.regionDAO.find( this.idRegion.intValue() );
    List<String> emails = new ArrayList<String>();
    this.extractRegionEmailList( region, emails );
    this.extractRegionTheaterEmailList( region, emails );
    /*-- Add subject --*/
    emailTO.setSubject( this.subject );
    /*-- Add email BCC --*/
    String emailBCC = configurationDAO.findByParameterName( Configuration.EMAIL_BOOKING_REGION_BCC.getParameter() )
        .getDsValue();
    emailTO.setRecipientsBCC( Arrays.asList( emailBCC ) );
    /*-- Add recipients emails --*/
    emailTO.setRecipients( emails );
    /*-- Add recipients CC emails --*/
    this.extractEmailLoggedUser( emailTO );
    /*-- Add message --*/
    emailTO.setMessage( this.message );
    /*-- Add report file --*/
    emailTO.setFiles( new ArrayList<FileTO>() );
    emailTO.getFiles().add( this.serviceReportsEJB.getWeeklyBookingReportPresaleByRegion( this.eventSelected, this.idWeek.longValue(), this.idRegion ) );
    return emailTO;
  }

  /**
   * Method that extracts the email list of region's staff
   * 
   * @param region
   * @param emails
   */
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

  /**
   * Method that extracts the email list of theater's staff
   * 
   * @param region
   * @param emails
   */
  private void extractRegionTheaterEmailList( RegionDO region, List<String> emails )
  {
    if( CollectionUtils.isNotEmpty( region.getTheaterDOList() ) && CollectionUtils.isNotEmpty( this.eventSelected ) )
    {
      List<Object[]> dataList = this.bookingDAO.findTheaterBookedPresalesByWeekAndRegion( this.idWeek.longValue(),
        this.idRegion );
      for( Object[] data : dataList )
      {
        Integer idTheater = ((Number) data[1]).intValue();
        Long idEvent = ((Number) data[2]).longValue();
        Predicate predicate = PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( ID_THEATER ),
          PredicateUtils.equalPredicate( idTheater ) );
        Predicate predicateToFindEvent = PredicateUtils.transformedPredicate( TransformerUtils.invokerTransformer( ID_EVENT ),
          PredicateUtils.equalPredicate( idEvent ) );
        if( CollectionUtils.exists( region.getTheaterDOList(), predicate ) && CollectionUtils.exists( this.eventSelected, predicateToFindEvent ) )
        {
          TheaterDO theater = (TheaterDO) CollectionUtils.find( region.getTheaterDOList(), predicate );
          if( theater.getIdEmail() != null && StringUtils.isNotBlank( theater.getIdEmail().getDsEmail() ) && !this.emailAlreadyExist( emails, theater.getIdEmail().getDsEmail() ) )
          {
            emails.add( theater.getIdEmail().getDsEmail() );
          }
        }
      }
    }
  }

  private boolean emailAlreadyExist(List<String> emails, String emailToAdd)
  {
    boolean exist = false;
    for(String email : emails)
    {
      if(email.trim().equals( emailToAdd.trim() ))
      {
        exist = true;
        break;
      }
    }
    return exist;
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

  /* Setters */
  /**
   * @param emailSenderEJB the emailSenderEJB to set
   */
  public void setEmailSenderEJB( EmailSenderEJB emailSenderEJB )
  {
    this.emailSenderEJB = emailSenderEJB;
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

  /**
   * @param message the message to set
   */
  public void setMessage( String message )
  {
    this.message = message;
  }

  /**
   * @param subject the subject to set
   */
  public void setSubject( String subject )
  {
    this.subject = subject;
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
   * @param eventSelected the eventSelected to set
   */
  public void setEventSelected( List<EventTO> eventSelected )
  {
    this.eventSelected = eventSelected;
  }

  /**
   * @param regionDAO the regionDAO to set
   */
  public void setRegionDAO( RegionDAO regionDAO )
  {
    this.regionDAO = regionDAO;
  }

  /**
   * @param bookingDAO the bookingDAO to set
   */
  public void setBookingDAO( BookingDAO bookingDAO )
  {
    this.bookingDAO = bookingDAO;
  }

  /**
   * @param configurationDAO the configurationDAO to set
   */
  public void setConfigurationDAO( ConfigurationDAO configurationDAO )
  {
    this.configurationDAO = configurationDAO;
  }

}
