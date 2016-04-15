package mx.com.cinepolis.digital.booking.service.cron;

import javax.ejb.Local;

/**
 * Interface for launching cron jobs
 * 
 * @author gsegura
 */
@Local
public interface ServiceCronEJB
{
  void start();
  
  void stop();
}
