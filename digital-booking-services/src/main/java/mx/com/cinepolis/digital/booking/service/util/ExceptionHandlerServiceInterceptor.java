package mx.com.cinepolis.digital.booking.service.util;

import javax.ejb.EJBTransactionRolledbackException;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This method intercepts all calls from Service classes, if exception occurs while processing the method converts it to
 * a system exception of type {@link mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException}
 * 
 * @author gsegura
 */
public class ExceptionHandlerServiceInterceptor
{
  private static final Logger LOG = LoggerFactory.getLogger( ExceptionHandlerServiceInterceptor.class );

  /**
   * Intercepts the exceptions
   * @param invocationContext
   * @return
   * @throws Exception
   */
  @AroundInvoke
  public Object handleExceptions( final InvocationContext invocationContext ) throws Exception
  {

    Object result = null;

    try
    {
      result = invocationContext.proceed();
    }
    catch( DigitalBookingException e )
    {
      LOG.error( "throwing DigitalBookingException, code:" + e.getCode() );
      throw e;
    }
    catch( EJBTransactionRolledbackException e )
    {
      LOG.error("Error de transaccion "+ e.getMessage());
      LOG.error("-----------");
      LOG.error(e.getClass().getCanonicalName());
      if( e.getCause() instanceof DigitalBookingException )
      {
        throw (DigitalBookingException) e.getCause();
      }
      else
      {
        LOG.error( "Relanzando excepcion:" + e.getMessage() );
        throw e;
      }
    }
    catch( Throwable t )
    {
      if( t.getCause() instanceof DigitalBookingException )
      {
        throw (DigitalBookingException) t.getCause();
      }
      else
      {
        LOG.error( t.getMessage(), t );
        throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.GENERIC_UNKNOWN_ERROR, t );
      }
    }
    return result;

  }
}
