package mx.com.cinepolis.digital.booking.service.cron.impl;

import javax.naming.InitialContext;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.service.synchronize.ServiceDataSynchronizerEJB;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.jobs.ee.ejb.EJB3InvokerJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Job que realiza la sincronización de los estados.
 * 
 * @author shernandezl
 */
public class SynchronizeStatesJob extends EJB3InvokerJob implements Job
{
  private static final Logger logger = LoggerFactory.getLogger( SynchronizeStatesJob.class );

  /**
   * Ejecución del Job synchronizeStates
   */
  @Override
  public void execute( JobExecutionContext jobExecutionContext ) throws JobExecutionException
  {
    try
    {
      InitialContext ctx = new InitialContext();
      Object obj = ctx
          .lookup( "ejb.ServiceDataSynchronizerEJB#mx.com.cinepolis.digital.booking.service.synchronize.ServiceDataSynchronizerEJB" );
      ((ServiceDataSynchronizerEJB) obj).synchronizeStates( 1L, Language.SPANISH );
      ((ServiceDataSynchronizerEJB) obj).synchronizeStates( 1L, Language.ENGLISH );
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e.getStackTrace() );
    }
  }

}
