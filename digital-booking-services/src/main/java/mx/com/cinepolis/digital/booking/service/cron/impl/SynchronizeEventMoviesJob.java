package mx.com.cinepolis.digital.booking.service.cron.impl;

import javax.naming.InitialContext;

import mx.com.cinepolis.digital.booking.service.synchronize.ServiceDataSynchronizerEJB;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.jobs.ee.ejb.EJB3InvokerJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Job que realiza la sincronización de las películas.
 * 
 * @author shernandezl
 */
public class SynchronizeEventMoviesJob extends EJB3InvokerJob implements Job
{
  private static final Logger logger = LoggerFactory.getLogger( SynchronizeEventMoviesJob.class );

  /**
   * Ejecución del Job synchronizeEventMovies
   */
  @Override
  public void execute( JobExecutionContext jobExecutionContext ) throws JobExecutionException
  {
    try
    {
      InitialContext ctx = new InitialContext();
      Object obj = ctx
          .lookup( "ejb.ServiceDataSynchronizerEJB#mx.com.cinepolis.digital.booking.service.synchronize.ServiceDataSynchronizerEJB" );
      ((ServiceDataSynchronizerEJB) obj).synchronizeEventMovies();
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e.getStackTrace() );
    }
  }

}
