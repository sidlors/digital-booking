package mx.com.cinepolis.digital.booking.service.cron.impl;

import javax.naming.InitialContext;

import mx.com.cinepolis.digital.booking.service.localtasks.ServiceLocalTasksEJB;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.jobs.ee.ejb.EJB3InvokerJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Job class that executes the "deactivate presales" task.
 * 
 * @author jreyesv
 */
public class DeactivatePresalesJob extends EJB3InvokerJob implements Job
{
  private static final Logger logger = LoggerFactory.getLogger( DeactivatePresalesJob.class );

  /**
   * Executes the "deactivate presales" task.
   */
  @Override
  public void execute( JobExecutionContext jobExecutionContext ) throws JobExecutionException
  {
    try
    {
      InitialContext ctx = new InitialContext();
      Object obj = ctx
          .lookup( "ejb.ServiceLocalTasksEJB#mx.com.cinepolis.digital.booking.service.localtasks.ServiceLocalTasksEJB" );
      ((ServiceLocalTasksEJB) obj).deactivatePresales();
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e.getStackTrace() );
    }
  }

}
