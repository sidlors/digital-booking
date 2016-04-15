package mx.com.cinepolis.digital.booking.service.util;

import java.io.IOException;

import javax.ejb.Local;
import javax.mail.MessagingException;

import mx.com.cinepolis.digital.booking.commons.to.EmailTO;

/**
 * Interface que define los metodos asociados al envio de correos
 * 
 * @author rgarcia
 */
@Local
public interface EmailSenderEJB
{
  /**
   * Sends and email given its {@link mx.com.cinepolis.digital.booking.commons.to.EmailTO}
   * 
   * @param emailTO
   * @throws MessagingException
   * @throws IOException
   */
  void sendEmail( EmailTO emailTO ) throws MessagingException, IOException;
}
