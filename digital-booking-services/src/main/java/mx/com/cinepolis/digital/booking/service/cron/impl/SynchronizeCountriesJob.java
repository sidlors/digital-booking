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
 * Job que realiza la sincronización de los paises.
 * 
 * @author shernandezl
 */
public class SynchronizeCountriesJob extends EJB3InvokerJob implements Job
{
  private static final Logger logger = LoggerFactory.getLogger( SynchronizeCountriesJob.class );

  /**
   * Ejecución del Job synchronizeCountries
   */
  @Override
  public void execute( JobExecutionContext jobExecutionContext ) throws JobExecutionException
  {
    try
    {
      InitialContext ctx = new InitialContext();
      Object obj = ctx
          .lookup( "ejb.ServiceDataSynchronizerEJB#mx.com.cinepolis.digital.booking.service.synchronize.ServiceDataSynchronizerEJB" );
      ((ServiceDataSynchronizerEJB) obj).synchronizeCountries( Language.SPANISH );
      ((ServiceDataSynchronizerEJB) obj).synchronizeCountries( Language.ENGLISH );
    }
    catch( Exception e )
    {
      logger.error( e.getMessage(), e.getStackTrace() );
    }
  }

}
