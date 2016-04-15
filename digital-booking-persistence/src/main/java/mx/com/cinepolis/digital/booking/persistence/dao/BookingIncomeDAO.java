package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.IncomeDetailTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeTreeTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.model.BookingIncomeDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.BookingIncomeDO}
 * 
 * @author jcarbajal
 * @author gsegura
 */
public interface BookingIncomeDAO extends GenericDAO<BookingIncomeDO>
{

  /**
   * Relaunches the process of synchronization of incomes for a given theater
   * 
   * @param theater
   */
  void synchronizeIncomes( TheaterTO theater );

  /**
   * Finds the registers associated to the incomes from a given theater Id and week Id:
   * <ul>
   * <li>mx.com.cinepolis.digital.booking.commons.to.IncomeTO.theater.id
   * <li>mx.com.cinepolis.digital.booking.commons.to.WeekTO.idWeek
   * </ul>
   * 
   * @param income
   * @return
   */
  List<IncomeTO> findAllByIdTheaterIdWeek( IncomeTO income );

  /**
   * Finds the registers associated to the incomes from a given theater Id and week Id:
   * <ul>
   * <li>mx.com.cinepolis.digital.booking.commons.to.IncomeTO.theater.id
   * <li>mx.com.cinepolis.digital.booking.commons.to.WeekTO.idWeek
   * <li>mx.com.cinepolis.digital.booking.commons.to.IncomeTO.date
   * </ul>
   * 
   * @param income
   * @return
   */
  int countByIdTheaterIdWeekDate( IncomeTO income );

  /**
   * Finds the details
   * 
   * @param request
   * @return
   */
  IncomeDetailTO findIncomeDetail( IncomeDetailTO request );

  /**
   * Finds the top Incomes/Attendance per week
   * 
   * @param week
   * @return
   */
  List<IncomeTO> findTopEvents( WeekTO week );

  /**
   * Finds the weekend income from a movie for a given theater
   * 
   * @param movie
   * @param week
   * @return
   */
  double getWeekendIncomeByMovie( IncomeTreeTO movie, WeekTO week );

  /**
   * Finds the week income from a movie for a given theater
   * 
   * @param movie
   * @param week
   * @return
   */
  double getWeekIncomeByMovie( IncomeTreeTO movie, WeekTO week );

  /**
   * Finds the weekend income from a screen for a given theater
   * 
   * @param screen
   * @param week
   * @return
   */
  double getWeekendIncomeByScreen( IncomeTreeTO screen, WeekTO week );

  /**
   * Finds the week income from a screen for a given theater
   * 
   * @param screen
   * @param week
   * @return
   */
  double getWeekIncomeByScreen( IncomeTreeTO screen, WeekTO week );
  /**
   * finds the total admission by theater and week
   * @param idTheater
   * @param idWeek
   * @return
   */
  double getTotalWeekendAdmissionByTheaterAndWeek(Long idTheater, Long idWeek);
  
  /**
   * finds the total admission by theater and week
   * @param idTheater
   * @param idWeek
   * @return
   */
  double getTotalWeekAdmissionByTheaterAndWeek(Long idTheater, Long idWeek);

}
