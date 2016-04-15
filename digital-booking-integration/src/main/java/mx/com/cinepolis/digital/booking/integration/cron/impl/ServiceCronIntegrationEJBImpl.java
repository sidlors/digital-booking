package mx.com.cinepolis.digital.booking.integration.cron.impl;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.integration.cron.ServiceCronIntegrationEJB;
import mx.com.cinepolis.digital.booking.service.cron.ServiceCronEJB;

@Local(value = ServiceCronIntegrationEJB.class)
@Stateless()
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceCronIntegrationEJBImpl implements ServiceCronIntegrationEJB
{

  @EJB
  private ServiceCronEJB serviceCronEJB;

  @Override
  public void start()
  {
    serviceCronEJB.start();
  }

  @Override
  public void stop()
  {
    serviceCronEJB.stop();

  }

}
