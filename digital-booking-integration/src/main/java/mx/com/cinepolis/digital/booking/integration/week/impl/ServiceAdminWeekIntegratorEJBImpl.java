package mx.com.cinepolis.digital.booking.integration.week.impl;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.integration.week.ServiceAdminWeekIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.week.ServiceAdminWeekEJB;

/**
 * Clase que implementa los servicios de integración de la administración de semanas.
 * 
 * @author shernandezl
 */
@Stateless
@Local(value = ServiceAdminWeekIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ServiceAdminWeekIntegratorEJBImpl implements ServiceAdminWeekIntegratorEJB
{
  @EJB
  private ServiceAdminWeekEJB serviceAdminWeekEJB;

  /*
   * {@inheritDoc}
   * @see mx.com.cinepolis.digital.booking.integration.week.ServiceAdminWeekIntegratorEJB#
   * getCatalogWeekSummary(mx.com.cinepolis.digital.booking.model.to.PagingRequestTO)
   */
  @Override
  public PagingResponseTO<WeekTO> getCatalogWeekSummary( PagingRequestTO pagingRequestTO )
  {
    return serviceAdminWeekEJB.getCatalogWeekSummary( pagingRequestTO );
  }

  /*
   * {@inheritDoc}
   * @see mx.com.cinepolis.digital.booking.integration.week.ServiceAdminWeekIntegratorEJB#getNextWeek()
   */
  @Override
  public WeekTO getNextWeek()
  {
    return serviceAdminWeekEJB.getNextWeek();
  }

  /*
   * {@inheritDoc}
   * @see mx.com.cinepolis.digital.booking.integration.week.ServiceAdminWeekIntegratorEJB#saveWeek(
   * mx.com.cinepolis.digital.booking.model.to.WeekTO)
   */
  @Override
  public void saveWeek( WeekTO weekTO )
  {
    serviceAdminWeekEJB.saveWeek( weekTO );
  }

  /*
   * {@inheritDoc}
   * @see mx.com.cinepolis.digital.booking.integration.week.ServiceAdminWeekIntegratorEJB#updateWeeK(
   * mx.com.cinepolis.digital.booking.model.to.WeekTO)
   */
  @Override
  public void updateWeeK( WeekTO weekTO )
  {
    serviceAdminWeekEJB.updateWeek( weekTO );
  }

  /*
   * {@inheritDoc}
   * @see mx.com.cinepolis.digital.booking.integration.week.ServiceAdminWeekIntegratorEJB#deleteWeek(
   * mx.com.cinepolis.digital.booking.model.to.WeekTO)
   */
  @Override
  public void deleteWeek( WeekTO weekTO )
  {
    serviceAdminWeekEJB.deleteWeek( weekTO );
  }

}
