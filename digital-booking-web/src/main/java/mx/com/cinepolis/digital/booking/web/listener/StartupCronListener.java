package mx.com.cinepolis.digital.booking.web.listener;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mx.com.cinepolis.digital.booking.integration.cron.ServiceCronIntegrationEJB;

/**
 * @author gsegura
 */
public class StartupCronListener implements ServletContextListener
{
  private static final Logger LOG = LoggerFactory.getLogger( StartupCronListener.class );
  
  @EJB
  private ServiceCronIntegrationEJB serviceCronIntegrationEJB;

  @Override
  public void contextInitialized( ServletContextEvent sce )
  {
    LOG.info( "Iniciando los crones" );
    serviceCronIntegrationEJB.start();

  }

  @Override
  public void contextDestroyed( ServletContextEvent sce )
  {
    LOG.info( "Fin de los crones" );
    serviceCronIntegrationEJB.stop();
  }

}
