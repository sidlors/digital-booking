package mx.com.cinepolis.digital.booking.integration.income;

import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;

/**
 * Interface for income services
 * 
 * @author gsegura
 */
public interface IncomeIntegratorEJB
{
  /**
   * Finds the registers associated to the incomes from a given theater Id and week Id, and groups by the Movie
   * <ul>
   * <li>mx.com.cinepolis.digital.booking.commons.to.IncomeTO.theater.id
   * <li>mx.com.cinepolis.digital.booking.commons.to.WeekTO.idWeek
   * </ul>
   * 
   * @param income
   * @return
   */
  IncomeTreeTO searchIncomesByMovie( IncomeTO income );

  /**
   * Finds the registers associated to the incomes from a given theater Id and week Id, and groups by the Screen
   * <ul>
   * <li>mx.com.cinepolis.digital.booking.commons.to.IncomeTO.theater.id
   * <li>mx.com.cinepolis.digital.booking.commons.to.WeekTO.idWeek
   * </ul>
   * 
   * @param income
   * @return
   */
  IncomeTreeTO searchIncomesByScreen( IncomeTO income );

  /**
   * Synchronizes the incomes for a given theater
   * 
   * @param theater
   */
  void synchronizeIncomes( TheaterTO theater );

  /**
   * Searchs the lastWeek
   * 
   * @param weekTO
   * @return
   */
  WeekTO getLastWeek( WeekTO weekTO );

}
