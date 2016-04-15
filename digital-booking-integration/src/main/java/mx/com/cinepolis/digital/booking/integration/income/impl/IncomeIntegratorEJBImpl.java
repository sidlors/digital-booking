package mx.com.cinepolis.digital.booking.integration.income.impl;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.integration.income.IncomeIntegratorEJB;
import mx.com.cinepolis.digital.booking.service.income.IncomeServiceEJB;

/**
 * Class that implements the integration services for incomes
 * 
 * @author gsegura
 */
@Stateless
@Local(value = IncomeIntegratorEJB.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class IncomeIntegratorEJBImpl implements IncomeIntegratorEJB
{

  @EJB
  private IncomeServiceEJB incomeServiceEJB;

  /**
   * {@inheritDoc}
   */
  @Override
  public IncomeTreeTO searchIncomesByMovie( IncomeTO income )
  {
    return incomeServiceEJB.searchIncomesByMovie( income );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IncomeTreeTO searchIncomesByScreen( IncomeTO income )
  {
    return incomeServiceEJB.searchIncomesByScreen( income );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void synchronizeIncomes( TheaterTO theater )
  {
    incomeServiceEJB.synchronizeIncomes( theater );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WeekTO getLastWeek( WeekTO weekTO )
  {
    return incomeServiceEJB.getLastWeek(weekTO);
  }

}
