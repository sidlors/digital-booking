package mx.com.cinepolis.digital.booking.service.util.impl;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import mx.com.cinepolis.digital.booking.commons.constants.Configuration;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.to.EmailTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;
import mx.com.cinepolis.digital.booking.model.ConfigurationDO;
import mx.com.cinepolis.digital.booking.service.configuration.ConfigurationServiceEJB;
import mx.com.cinepolis.digital.booking.service.util.EmailSenderEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EJB utileria que se encarga del envio de correos
 * 
 * @author agustin.ramirez
 */
@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class EmailSenderEJBImpl implements EmailSenderEJB
{
  private static final Logger LOG = LoggerFactory.getLogger( EmailSenderEJBImpl.class );
  /**
   * ConfigurationServiceEJB
   */
  @EJB
  private ConfigurationServiceEJB configurationServiceEJB;

  /**
   * Session de correo
   */
  private Session session;

  /**
   * Direccion que envia el correo
   */
  private String from;

  /**
   * Inicializa la sesion de correo
   */
  @PostConstruct
  public void initialize()
  {
    ConfigurationDO emailHost = configurationServiceEJB.findParameterByName( Configuration.EMAIL_HOST.getParameter() );
    ConfigurationDO emailPort = configurationServiceEJB.findParameterByName( Configuration.EMAIL_PORT.getParameter() );
    final ConfigurationDO emailUser = configurationServiceEJB.findParameterByName( Configuration.EMAIL_USER
        .getParameter() );
    final ConfigurationDO emailPass = configurationServiceEJB.findParameterByName( Configuration.EMAIL_PASSWORD
        .getParameter() );
    ConfigurationDO emailFrom = configurationServiceEJB.findParameterByName( Configuration.EMAIL_FROM.getParameter() );
    this.from = emailFrom.getDsValue();
    Properties props = new Properties();
    props.put( "mail.smtp.auth", "true" );
    props.put( "mail.smtp.starttls.enable", "false" );
    props.put( "mail.smtp.host", emailHost.getDsValue() );
    props.put( "mail.smtp.port", emailPort.getDsValue() );
    session = Session.getInstance( props, new javax.mail.Authenticator()
    {
      protected PasswordAuthentication getPasswordAuthentication()
      {
        return new PasswordAuthentication( emailUser.getDsValue(), emailPass.getDsValue() );
      }
    } );
  }

  /**
   * Envia un correo
   * 
   * @param emailTO
   * @throws MessagingException
   * @throws IOException
   */
  public void sendEmail( EmailTO emailTO ) throws MessagingException, IOException
  {

    validateEmailTO( emailTO );

    List<EmailTO> recipientList = extractRecipients( emailTO );
    int n = 1;
    for( EmailTO recipients : recipientList )
    {
      loggingRecipients( recipients.getRecipients(), "recipients" );
      loggingRecipients( recipients.getRecipientsCC(), "recipientsCC" );
      loggingRecipients( recipients.getRecipientsBCC(), "recipientsBCC" );

      int maxRetries = 3;
      int retry = 1;
      while( maxRetries > 0 )
      {
        try
        {
          sendMessage( emailTO, recipients );
          break;
        }
        catch( NoSuchProviderException e )
        {
          maxRetries--;
          retry++;
          if( maxRetries > 0 )
          {
            LOG.info( "Error sending mail: " + e.getMessage() );
            LOG.info( "Retrying..." + retry );
          }
          else
          {
            throw e;
          }
        }
        catch( MessagingException e )
        {
          maxRetries--;
          retry++;
          if( maxRetries > 0 )
          {
            LOG.info( "Error sending mail: " + e.getMessage() );
            LOG.info( "Retrying..." + retry );
          }
          else
          {
            throw e;
          }
        }
      }
      LOG.info( "Email sent batch " + n + " of " + recipientList.size() );
      n++;
    }

  }

  private void sendMessage( EmailTO emailTO, EmailTO recipients ) throws NoSuchProviderException, MessagingException,
      AddressException, IOException
  {
    Transport bus = session.getTransport( "smtp" );
    bus.connect();

    MimeMessage mimeMessage = new MimeMessage( session );
    mimeMessage.setRecipients( RecipientType.TO, extractAddresses( recipients.getRecipients() ) );
    mimeMessage.setRecipients( RecipientType.CC, extractAddresses( recipients.getRecipientsCC() ) );
    mimeMessage.setRecipients( RecipientType.BCC, extractAddresses( recipients.getRecipientsBCC() ) );
    mimeMessage.setFrom( new InternetAddress( this.from ) );
    mimeMessage.setSubject( emailTO.getSubject() );
    mimeMessage.setSentDate( new Date() );
    MimeMultipart multipart = new MimeMultipart( "related" );
    BodyPart messageBodyPart = new MimeBodyPart();
    messageBodyPart.setContent( emailTO.getMessage(), "text/html" );

    multipart.addBodyPart( messageBodyPart );
    mimeMessage.setContent( multipart );
    if( emailTO.getFiles() != null )
    {
      for( FileTO fileTO : emailTO.getFiles() )
      {
        File f = new File( fileTO.getName() );
        FileUtils.writeByteArrayToFile( f, fileTO.getFile() );
        setFileAsAttachment( mimeMessage, multipart, f );
        mimeMessage.saveChanges();
      }
    }
    bus.sendMessage( mimeMessage, mimeMessage.getAllRecipients() );

    bus.close();
  }

  private void loggingRecipients( List<String> recipients, String name )
  {
    if( CollectionUtils.isNotEmpty( recipients ) )
    {
      LOG.info( "Sending email to " + name + ": " + Arrays.toString( recipients.toArray() ) );
    }
  }

  private List<EmailTO> extractRecipients( EmailTO emailTO )
  {
    int limit = 10;
    List<EmailTO> recipientsList = new ArrayList<EmailTO>();
    List<String> recipients = emailTO.getRecipients();
    List<String> recipientsCC = emailTO.getRecipientsCC();
    List<String> recipientsBCC = emailTO.getRecipientsBCC();
    while( true )
    {
      EmailTO to = new EmailTO();
      to.setRecipients( new ArrayList<String>() );
      to.setRecipientsCC( new ArrayList<String>() );
      to.setRecipientsBCC( new ArrayList<String>() );
      recipients = extractRecipients( recipients, to.getRecipients(), limit );

      recipientsCC = extractRecipients( recipientsCC, to.getRecipientsCC(), limit );
      recipientsBCC = extractRecipients( recipientsBCC, to.getRecipientsBCC(), limit );

      if( addRecipientsList( to ) )
      {
        recipientsList.add( to );
      }

      if( CollectionUtils.isEmpty( recipients ) && CollectionUtils.isEmpty( recipientsCC )
          && CollectionUtils.isEmpty( recipientsBCC ) )
      {
        break;
      }
    }
    return recipientsList;
  }

  private boolean addRecipientsList( EmailTO to )
  {
    return CollectionUtils.isNotEmpty( to.getRecipients() ) || CollectionUtils.isNotEmpty( to.getRecipientsCC() )
        || CollectionUtils.isNotEmpty( to.getRecipientsBCC() );
  }

  private List<String> extractRecipients( List<String> source, List<String> destination, int limit )
  {
    List<String> data = new ArrayList<String>();
    if( CollectionUtils.isNotEmpty( source ) )
    {
      if( source.size() > limit )
      {
        destination.addAll( source.subList( 0, limit ) );
        data = source.subList( limit, source.size() );
      }
      else
      {
        destination.addAll( source );
      }
    }
    return data;
  }

  /**
   * Añade un archivo , el cual se adjuntara en el correo
   * 
   * @param msg
   * @param multipart
   * @param routeFile
   * @throws MessagingException
   */
  private void setFileAsAttachment( Message msg, MimeMultipart multipart, File file ) throws MessagingException
  {

    MimeBodyPart p2 = new MimeBodyPart();
    FileDataSource fds = new FileDataSource( file );
    p2.setDataHandler( new DataHandler( fds ) );

    // MimeUtility.encodeText( fds.getName() )
    p2.setFileName( safeEncode( fds.getName() ) );
    multipart.addBodyPart( p2 );
    msg.setContent( multipart );
  }

  private String safeEncode( String name )
  {
    String encoded = null;
    try
    {
      encoded = MimeUtility.encodeText( name );
    }
    catch( UnsupportedEncodingException e )
    {
      encoded = name;
    }
    return encoded;
  }

  /**
   * Extrae las direcciones de la lista de recipientes.
   * 
   * @param recipients the recipients
   * @return the address[]
   * @throws AddressException the address exception
   */
  private Address[] extractAddresses( List<String> recipients ) throws AddressException
  {
    Address[] addresses = new Address[0];
    if( CollectionUtils.isNotEmpty( recipients ) )
    {
      addresses = new Address[recipients.size()];
      int i = 0;
      for( String recipient : recipients )
      {
        addresses[i++] = new InternetAddress( recipient );
      }
    }
    return addresses;
  }

  /**
   * Validaciones generales del TO.
   * 
   * @param emailTO the email to
   */
  private void validateEmailTO( EmailTO emailTO )
  {
    if( emailTO == null )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.ERROR_SENDING_EMAIL_NO_DATA.getId() );
    }
    if( CollectionUtils.isEmpty( emailTO.getRecipients() ) && CollectionUtils.isEmpty( emailTO.getRecipientsCC() )
        && CollectionUtils.isEmpty( emailTO.getRecipientsBCC() ) )
    {
      throw DigitalBookingExceptionBuilder
          .build( DigitalBookingExceptionCode.ERROR_SENDING_EMAIL_NO_RECIPIENTS.getId() );
    }
    if( StringUtils.isEmpty( emailTO.getSubject() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.ERROR_SENDING_EMAIL_SUBJECT.getId() );
    }

    if( StringUtils.isEmpty( emailTO.getMessage() ) )
    {
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.ERROR_SENDING_EMAIL_MESSAGE.getId() );
    }

  }

  /**
   * @return the from
   */
  public String getFrom()
  {
    return from;
  }

  /**
   * @param from the from to set
   */
  public void setFrom( String from )
  {
    this.from = from;
  }

}
