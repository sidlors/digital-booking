package mx.com.cinepolis.digital.booking.dao.util;

import java.sql.SQLException;

import javax.ejb.EJBTransactionRolledbackException;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.persistence.PersistenceException;

import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingException;
import mx.com.cinepolis.digital.booking.commons.exception.DigitalBookingExceptionCode;
import mx.com.cinepolis.digital.booking.commons.utils.DigitalBookingExceptionBuilder;

import org.eclipse.persistence.exceptions.DatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This method intercepts all calls from DAO classes, if exception occurs while
 * processing the method converts it to a system exception of type
 * {@link mx.com.cinepolis.digital.booking.model.exception.DigitalBookingException}
 * 
 * @author gsegura
 */
public class ExceptionHandlerDAOInterceptor
{
  private static final Logger LOG = LoggerFactory.getLogger( ExceptionHandlerDAOInterceptor.class );

  /**
   * Intercepts the exceptions
   * 
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
      LOG.debug( "throwing DigitalBookingException, code:" + e.getCode() );
      throw e;
    }
    catch( SQLException e )
    {
      result = processSQLException( invocationContext, result, e );
    }
    catch( PersistenceException e )
    {
      result = processPersistenceException( invocationContext, result, e );
    }
    catch( DatabaseException e )
    {
      result = processDatabaseException( invocationContext, result, e );
    }
    catch( EJBTransactionRolledbackException e )
    {
      result = processEJBTransactionRolledbackException( invocationContext, result, e );
    }
    catch( Throwable t )
    {
      LOG.error( "Error de persistencia:" + t.getClass()
          .getCanonicalName() );
      LOG.error( t.getMessage(), t );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.GENERIC_UNKNOWN_ERROR, t );
    }
    return result;

  }

  private Object processEJBTransactionRolledbackException( final InvocationContext invocationContext, Object r,
      EJBTransactionRolledbackException e ) throws InterruptedException, Exception
  {
    Object result = r;
    if( e.getCausedByException() instanceof PersistenceException )
    {
      result = processPersistenceException( invocationContext, result, (PersistenceException) e.getCausedByException() );
    }
    else if( e.getCause() instanceof DatabaseException )
    {
      result = processDatabaseException( invocationContext, result, (DatabaseException) e.getCausedByException() );
    }
    else if( e.getCause() instanceof SQLException )
    {
      result = processSQLException( invocationContext, result, (SQLException) e.getCausedByException() );
    }
    else
    {
      LOG.error( e.getMessage(), e );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_GENERIC, e );
    }
    return result;
  }

  private Object processSQLException( final InvocationContext invocationContext, Object r, SQLException e )
      throws InterruptedException, Exception
  {
    
    Object result = r;
    LOG.error( "Error de persistencia:" + e.getClass()
        .getCanonicalName() );
    LOG.error( "Error de persistencia:" + e.getMessage() );
    int errorCode = this.getErrorCode( e, 5 );
    if( errorCode == 1205 )
    {
      long wait = (long) (Math.random() * 500) + 1;
      LOG.error( "Se relanza la peticion por interbloqueo, esperando " + wait + " ms" );
      Thread.sleep( wait );
      result = handleExceptions( invocationContext );
    }
    else
    {
      LOG.error( e.getMessage(), e );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_GENERIC, e );
    }
    return result;
  }

  private Object processDatabaseException( final InvocationContext invocationContext, Object r, DatabaseException e )
      throws InterruptedException, Exception
  {
    Object result = r;
    LOG.error( "Error de persistencia:" + e.getClass()
        .getCanonicalName() );
    LOG.error( "Error de base de datos:" + e.getMessage() );
    int errorCode = e.getErrorCode();

    if( e.getMessage()
        .contains( "Transaction timed out" ) )
    {
      LOG.error( e.getMessage(), e );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.TRANSACTION_TIMEOUT, e );
    }
    else if( errorCode == 1205 )
    {
      long wait = (long) (Math.random() * 500) + 1;
      LOG.error( "Se relanza la peticion por interbloqueo, esperando " + wait + " ms" );
      Thread.sleep( wait );
      result = handleExceptions( invocationContext );
    }
    else if( this.getErrorCode( e, 5 ) == 1205 )
    {
      long wait = (long) (Math.random() * 500) + 1;
      LOG.error( "Se relanza la peticion por interbloqueo, esperando " + wait + " ms" );
      Thread.sleep( wait );
      result = handleExceptions( invocationContext );
    }
    else
    {
      LOG.error( e.getMessage(), e );
      throw DigitalBookingExceptionBuilder.build(
          DigitalBookingExceptionCode.PERSISTENCE_ERROR_NEW_OBJECT_FOUND_DURING_COMMIT, e );
    }
    return result;
  }

  private Object processPersistenceException( final InvocationContext invocationContext, Object r,
      PersistenceException e ) throws InterruptedException, Exception
  {
    Object result = r;
    LOG.error( "Error de persistencia:" + e.getClass()
        .getCanonicalName() );
    LOG.error( "Error de persistencia:" + e.getMessage() );
    int errorCode = this.getErrorCode( e, 5 );
    if( errorCode == 1205 )
    {
      long wait = (long) (Math.random() * 500) + 1;
      LOG.error( "Se relanza la peticion por interbloqueo, esperando " + wait + " ms" );
      Thread.sleep( wait );
      result = handleExceptions( invocationContext );
    }
    else
    {
      LOG.error( e.getMessage(), e );
      throw DigitalBookingExceptionBuilder.build( DigitalBookingExceptionCode.PERSISTENCE_ERROR_GENERIC, e );
    }
    return result;
  }

  private int getErrorCode( Throwable e, int n )
  {
    int errorCode = 0;
    if( e instanceof SQLException )
    {
      errorCode = ((SQLException) e).getErrorCode();
    }
    else if( n > 0 )
    {
      errorCode = getErrorCode( e.getCause(), n - 1 );
    }
    return errorCode;
  }

}
