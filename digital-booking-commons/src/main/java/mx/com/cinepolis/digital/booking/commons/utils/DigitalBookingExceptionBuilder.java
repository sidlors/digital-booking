package mx.com.cinepolis.digital.booking.commons.utils;

import java.util.Date;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;

/**
 * Clase que crea las excepciones de tipo EWalletException
 * 
 * @author gsegura
 */
public final class DigitalBookingExceptionBuilder
{

  private static final int ERROR_DESCONOCIDO = 0;

  /* Se ofusca el constructor */
  private DigitalBookingExceptionBuilder()
  {
  }

  /**
   * Genera una excepción DigitalBookingException con el código de error indicado
   * 
   * @param bookingExceptionCode Código de Error
   * @return Excepción DigitalBookingException
   */
  public static DigitalBookingException build( DigitalBookingExceptionCode code )
  {
    return build( code.getId(), null, null );
  }

  /**
   * Genera una excepción DigitalBookingException con el código de error indicado
   * 
   * @param code Código de Error
   * @return Excepción DigitalBookingException
   */
  public static DigitalBookingException build( int code )
  {
    return build( code, null, null );
  }

  /**
   * Genera una excepción DigitalBookingException con el código de error indicado, anexa los parámetros
   * 
   * @param code Código de Error
   * @param args Parámetros
   * @return Excepción DigitalBookingException
   */
  public static DigitalBookingException build( int code, Object[] args )
  {
    return build( code, null, args );
  }

  /**
   * Genera una excepción DigitalBookingException con el código de error indicado, anexa los parámetros
   * 
   * @param code Código de Error
   * @param args Parámetros
   * @return Excepción DigitalBookingException
   */
  public static DigitalBookingException build( DigitalBookingExceptionCode code, Object[] args )
  {
    return build( code.getId(), null, args );
  }

  /**
   * Genera una excepción DigitalBookingException con el código de error indicado, agrega la causa de error
   * 
   * @param code Código de Error
   * @param cause Causa de error
   * @return Excepción DigitalBookingException
   */
  public static DigitalBookingException build( int code, Throwable cause )
  {
    return build( code, cause, null );
  }

  /**
   * Genera una excepción DigitalBookingException con el código de error indicado, agrega la causa de error
   * 
   * @param code Código de Error
   * @param cause Causa de error
   * @return Excepción DigitalBookingException
   */
  public static DigitalBookingException build( DigitalBookingExceptionCode code, Throwable cause )
  {
    return build( code.getId(), cause, null );
  }

  /**
   * Genera una excepción DigitalBookingException con el código de error indicado, agrega la causa de error, agrega los
   * parámetros de
   * 
   * @param code Código de Error
   * @param cause Causa de error
   * @param args Parámetros
   * @return Excepción DigitalBookingException
   */
  public static DigitalBookingException build( DigitalBookingExceptionCode code, Throwable cause, Object[] args )
  {
    return build( code.getId(), cause, args );
  }

  /**
   * Genera una excepción DigitalBookingException con el código de error indicado, agrega la causa de error, agrega los
   * parámetros de
   * 
   * @param code Código de Error
   * @param cause Causa de error
   * @param args Parámetros
   * @return Excepción DigitalBookingException
   */
  public static DigitalBookingException build( int code, Throwable cause, Object[] args )
  {
    Date date = new Date();
    DigitalBookingException digitalBookingException = null;
    if( cause != null )
    {
      digitalBookingException = new DigitalBookingException( cause.getMessage(), cause );
    }
    else
    {
      digitalBookingException = new DigitalBookingException();
    }

    digitalBookingException.setCode( code );
    digitalBookingException.setArgs( args );
    digitalBookingException.setDate( date );
    digitalBookingException.setId( date.getTime() );

    return digitalBookingException;
  }

  /**
   * Genera una excepción DigitalBookingException con un código de error 0 (desconocido)
   * 
   * @return Excepción DigitalBookingException
   */
  public static DigitalBookingException build()
  {
    return build( ERROR_DESCONOCIDO );
  }

  /**
   * Genera una excepción EWalletException con un código de error 0 (desconocido) y agrega la causa de error
   * 
   * @param cause Causa de error
   * @return Excepción DigitalBookingException
   */
  public static DigitalBookingException build( Throwable cause )
  {
    return build( ERROR_DESCONOCIDO, cause );
  }

}
