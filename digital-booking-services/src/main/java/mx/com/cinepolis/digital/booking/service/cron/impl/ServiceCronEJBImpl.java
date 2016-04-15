package mx.com.cinepolis.digital.booking.service.cron.impl;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import mx.com.cinepolis.digital.booking.commons.constants.Configuration;
import mx.com.cinepolis.digital.booking.persistence.dao.ConfigurationDAO;
import mx.com.cinepolis.digital.booking.service.cron.ServiceCronEJB;
import mx.com.cinepolis.digital.booking.service.util.ExceptionHandlerServiceInterceptor;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Interceptors({ ExceptionHandlerServiceInterceptor.class })
public class ServiceCronEJBImpl implements ServiceCronEJB
{
  
  private static final String SYNCHRONIZATION_GROUP = "synchronizationGroup";
  private static final String LOCAL_TASKS_GROUP = "localTasksGroup";
  private static final String TRIGGER_EVENT_MOVIES = "triggerEventMovie";
  private static final String TRIGGER_DISTRIBUTORS = "triggerDistributors";
  private static final String TRIGGER_COUNTRIES = "triggerCountries";
  private static final String TRIGGER_CITIES = "triggerCities";
  private static final String TRIGGER_STATES = "triggerEstates";
  private static final String TRIGGER_THEATERS = "triggerTheaters";
  private static final String TRIGGER_DEACTIVATE_PRESALES = "triggerDeactivatePresales";
  private static final String MOVIES_JOB = "SynchronizeEventMoviesJob";
  private static final String DISTRIBUTORS_JOB = "SynchronizeDistributorsJob";
  private static final String CONTRIES_JOB = "SynchronizeCountriesJob";
  private static final String CITIES_JOB = "SynchronizeCitiesJob";
  private static final String STATES_JOB = "SynchronizeStatesJob";
  private static final String THEATERS_JOB = "SynchronizeTheatersJob";
  private static final String DEACTIVATE_PRESALES_JOB = "DeactivatePresalesJob";
  private static final Logger LOG = LoggerFactory.getLogger( ServiceCronEJBImpl.class );

  @EJB
  private ConfigurationDAO configurationDAO;

  private Scheduler sched;

  /**
   * Start of cron.
   */
  @Override
  public void start()
  {

    try
    {
      SchedulerFactory schedFact = new StdSchedulerFactory();

      sched = schedFact.getScheduler();
      sched.start();

      JobDetail jobMovies = newJob( SynchronizeEventMoviesJob.class ).withIdentity( MOVIES_JOB, SYNCHRONIZATION_GROUP )
          .build();
      JobDetail jobDistributors = newJob( SynchronizeDistributorsJob.class ).withIdentity( DISTRIBUTORS_JOB,
        SYNCHRONIZATION_GROUP ).build();
      JobDetail jobCountries = newJob( SynchronizeCountriesJob.class ).withIdentity( CONTRIES_JOB,
        SYNCHRONIZATION_GROUP ).build();
      JobDetail jobCities = newJob( SynchronizeCitiesJob.class ).withIdentity( CITIES_JOB, SYNCHRONIZATION_GROUP )
          .build();
      JobDetail jobStates = newJob( SynchronizeStatesJob.class ).withIdentity( STATES_JOB, SYNCHRONIZATION_GROUP )
          .build();
      JobDetail jobTheaters = newJob( SynchronizeTheatersJob.class ).withIdentity( THEATERS_JOB, SYNCHRONIZATION_GROUP )
          .build();
      JobDetail jobDeactivatePresales = newJob( DeactivatePresalesJob.class ).withIdentity( DEACTIVATE_PRESALES_JOB, LOCAL_TASKS_GROUP )
          .build();

      // Trigger the job to run now, and then every 40 seconds
      Trigger triggerMovies = newTrigger().withIdentity( TRIGGER_EVENT_MOVIES, SYNCHRONIZATION_GROUP )
          .withSchedule( cronSchedule( getSchedule( Configuration.SCHEDULE_EVENT_MOVIES.getParameter() ) ) )
          .forJob( MOVIES_JOB, SYNCHRONIZATION_GROUP ).build();
      Trigger triggerDistributors = newTrigger().withIdentity( TRIGGER_DISTRIBUTORS, SYNCHRONIZATION_GROUP )
          .withSchedule( cronSchedule( getSchedule( Configuration.SCHEDULE_DISTRIBUTORS.getParameter() ) ) )
          .forJob( DISTRIBUTORS_JOB, SYNCHRONIZATION_GROUP ).build();
      Trigger triggerCountries = newTrigger().withIdentity( TRIGGER_COUNTRIES, SYNCHRONIZATION_GROUP )
          .withSchedule( cronSchedule( getSchedule( Configuration.SCHEDULE_COUNTRYS.getParameter() ) ) )
          .forJob( CONTRIES_JOB, SYNCHRONIZATION_GROUP ).build();
      Trigger triggerCities = newTrigger().withIdentity( TRIGGER_CITIES, SYNCHRONIZATION_GROUP )
          .withSchedule( cronSchedule( getSchedule( Configuration.SCHEDULE_CITIES.getParameter() ) ) )
          .forJob( CITIES_JOB, SYNCHRONIZATION_GROUP ).build();
      Trigger triggerStates = newTrigger().withIdentity( TRIGGER_STATES, SYNCHRONIZATION_GROUP )
          .withSchedule( cronSchedule( getSchedule( Configuration.SCHEDULE_ESTATES.getParameter() ) ) )
          .forJob( STATES_JOB, SYNCHRONIZATION_GROUP ).build();
      Trigger triggerTheaters = newTrigger().withIdentity( TRIGGER_THEATERS, SYNCHRONIZATION_GROUP )
          .withSchedule( cronSchedule( getSchedule( Configuration.SCHEDULE_THEATERS.getParameter() ) ) )
          .forJob( THEATERS_JOB, SYNCHRONIZATION_GROUP ).build();
      Trigger triggerDeactivatePresales = newTrigger().withIdentity( TRIGGER_DEACTIVATE_PRESALES, LOCAL_TASKS_GROUP )
          .withSchedule( cronSchedule( getSchedule( Configuration.SCHEDULE_DEACTIVATE_PRESALES.getParameter() ) ) )
          .forJob( DEACTIVATE_PRESALES_JOB, LOCAL_TASKS_GROUP ).build();

      // Tell quartz to schedule the job using our trigger
      sched.scheduleJob( jobMovies, triggerMovies );
      sched.scheduleJob( jobDeactivatePresales, triggerDeactivatePresales );
      sched.scheduleJob( jobDistributors, triggerDistributors );
      sched.scheduleJob( jobCountries, triggerCountries );
      sched.scheduleJob( jobCities, triggerCities );
      sched.scheduleJob( jobStates, triggerStates );
      sched.scheduleJob( jobTheaters, triggerTheaters );
    }
    catch( SchedulerException e )
    {
      LOG.error( e.getMessage(), e.getStackTrace() );
    }

  }

  /**
   * @param parameter
   * @return
   */
  private String getSchedule( String parameter )
  {
    return this.configurationDAO.findByParameterName( parameter ).getDsValue();
  }

  /**
   * 
   */
  @Override
  public void stop()
  {
    try
    {
      LOG.debug( "Fin del scheduler" );
      if( this.sched != null )
      {
        this.sched.shutdown();
      }
    }
    catch( SchedulerException e )
    {
      LOG.error( e.getMessage(), e.getStackTrace() );
    }

  }

}
