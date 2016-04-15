package mx.com.cinepolis.digital.booking.service.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.to.EmailTO;
import mx.com.cinepolis.digital.booking.commons.to.FileTO;
import mx.com.cinepolis.digital.booking.commons.utils.ReflectionHelper;
import mx.com.cinepolis.digital.booking.service.util.impl.EmailSenderEJBImpl;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Clase de prueba de EmailSender
 * 
 * @author agustin.ramirez
 */
public class EmailSenderTest
{
  /**
   * Email Sender
   */
  private EmailSenderEJBImpl emailSender;

  /***
   * TODO para que la primer prueba funcione se tiene eliminar la dependencia de java-ee-api del pom , y agregar la de
   * wlfullclient , ya que si existen las dos se da el error de Absent Code attribute in method that is not native or
   * abstract in class file javax/ws/rs/ext/RuntimeDelegate
   * http://www.andrejkoelewijn.com/blog/2010/03/04/absent-code-attribute-in-method-that-is-not-native-or-abstract/
   */

  /**
	 * 
	 */
  @Before
  public void setUp()
  {
    emailSender = new EmailSenderEJBImpl();
    Properties props = new Properties();
    props.put( "mail.smtp.auth", "true" );
    props.put( "mail.smtp.starttls.enable", "false" );
    props.put( "mail.smtp.host", "sofac.com.mx" );
    props.put( "mail.smtp.port", "587" );
    Session session = Session.getInstance( props, new javax.mail.Authenticator()
    {
      protected PasswordAuthentication getPasswordAuthentication()
      {
        return new PasswordAuthentication( "cinepolis", "c1nepol1s" );
      }
    } );
    ReflectionHelper.set( session, "session", emailSender );
    ReflectionHelper.set( "pruebas@intellego.com.mx", "from", emailSender );

  }

  /**
   * Test sendEmail
   * 
   * @throws MessagingException
   * @throws IOException
   */
  @Ignore
  @Test
  public void testSendEmail() throws MessagingException, IOException
  {

    EmailTO emailTO = new EmailTO();
    emailTO.setSubject( "Mensaje de prueba Digital Booking" );
    emailTO.setRecipients( new ArrayList<String>() );
    emailTO.getRecipients().add( "guillermo.segura@intellego.com.mx" );
    emailTO.getRecipients().add( "kaos_eco@hotmail.com" );
    emailTO.setRecipientsCC( new ArrayList<String>() );
    emailTO.getRecipientsCC().add( "shamanbros@yahoo.com" );
    emailTO.getRecipientsCC().add( "shamanbros@gmail.com" );
    emailTO.setFiles( new ArrayList<FileTO>() );
    FileTO fileTO = new FileTO();
    File f = new File( "C:/Desert.jpg" );
    fileTO.setName( "imagen.jpg" );
    fileTO.setFile( FileUtils.readFileToByteArray( f ) );
    emailTO.getFiles().add( fileTO );
    String messageHTML = "<html><head><title> Prueba unitaria</title></head><body><h1> Esta es una prueba de envio de correo"
        + "</h1><p>Enviando Email desde Booking" + " through Java.</body></html>";
    emailTO.setMessage( messageHTML );

    emailSender.sendEmail( emailTO );
  }

  /**
   * Email
   * 
   * @throws IOException
   * @throws MessagingException
   */
  @Test
  @Ignore
  public void testSendEmail_emailTONull() throws MessagingException, IOException
  {
    try
    {
      emailSender.sendEmail( null );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( DigitalBookingException e )
    {
      e.printStackTrace();
    }

  }

  /**
   * Email
   * 
   * @throws MessagingException
   * @throws IOException
   */
  @Test
  @Ignore
  public void testSendEmail_notRecipients() throws MessagingException, IOException
  {

    try
    {
      EmailTO emailTO = new EmailTO();
      emailTO.setSubject( "Mensaje de prueba Digital Booking" );
      emailTO.setRecipients( null );

      emailSender.sendEmail( emailTO );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( DigitalBookingException e )
    {
      e.printStackTrace();
    }
  }

  /**
   * Email
   * 
   * @throws MessagingException
   * @throws IOException
   */
  @Test
  @Ignore
  public void testSendEmail_notSubject() throws MessagingException, IOException
  {

    try
    {
      EmailTO emailTO = new EmailTO();
      emailTO.setSubject( "Subject de Ejemplo" );
      emailTO.setRecipients( new ArrayList<String>() );
      emailTO.getRecipients().add( "agustin.ramirez@intellego.com.mx" );
      emailTO.getRecipients().add( "kaos_eco@hotmail.com" );
      emailTO.setRecipientsCC( new ArrayList<String>() );
      emailTO.getRecipientsCC().add( "agustin.ramirez@ciencias.unam.mx" );
      emailTO.setFiles( new ArrayList<FileTO>() );
      emailTO.setMessage( null );
      emailSender.sendEmail( emailTO );
      Assert.fail( "Debio lanzar excepcion" );
    }
    catch( DigitalBookingException e )
    {
      e.printStackTrace();
    }
  }

  @Test
  @Ignore
  public void testSendEmail_multiplesRecipients() throws MessagingException, IOException
  {

    try
    {
      EmailTO emailTO = new EmailTO();
      emailTO.setSubject( "Subject de Ejemplo" );
      emailTO.setRecipients( new ArrayList<String>() );
      emailTO.getRecipients().add( "guillermo.segura@intellego.com.mx" );
      for( int i = 0; i < 80; i++ )
      {
        emailTO.getRecipients().add( "testCinemaBooker" + i + "@yopmail.com" );
      }
      emailTO.setRecipientsCC( new ArrayList<String>() );
      emailTO.getRecipientsCC().add( "guillermo.segura@intellego.com.mx" );
      for( int i = 0; i < 80; i++ )
      {
        emailTO.getRecipientsCC().add( "testCinemaBooker" + i + "@yopmail.com" );
      }
      emailTO.setFiles( new ArrayList<FileTO>() );
      String messageHTML = "<html><head><title> Prueba unitaria</title></head><body><h1> Esta es una prueba de envio de correo"
          + "</h1><p>Enviando Email desde Booking" + " through Java.</body></html>";
      emailTO.setMessage( messageHTML );
      emailSender.sendEmail( emailTO );
    }
    catch( DigitalBookingException e )
    {
      e.printStackTrace();
    }
  }

}
