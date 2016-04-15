package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.Date;
import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.PagingRequestTO;
import mx.com.cinepolis.digital.booking.commons.to.PagingResponseTO;
import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.model.WeekDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.WeekDO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
public interface WeekDAO extends GenericDAO<WeekDO>
{

  /**
   * @param weekTO
   */
  void save( WeekTO weekTO );

  /**
   * Updates a register
   * 
   * @param weekTO
   */
  void update( WeekTO weekTO );

  /**
   * Deletes a register
   * 
   * @param weekTO
   */
  void delete( WeekTO weekTO );

  /**
   * Finds all records that satisfies the query
   * 
   * @param pagingRequestTO a paging request
   * @return A {@link mx.com.cinepolis.digital.booking.model.to.PagingResponseTO<WeekTO>} with the results
   */
  PagingResponseTO<WeekTO> findAllByPaging( PagingRequestTO pagingRequestTO );

  /**
   * Finds all active Weeks, list first the active week, then the last week, then two weeks later and later the special
   * weeks if any during those periods
   * 
   * @param date
   * @param getFIveWeeks
   * @return
   */
  List<WeekTO> getCurrentWeeks( Date date, boolean getFiveWeeks );

  /**
   * Finds the special weeks active during a date range
   * 
   * @param start
   * @param end
   * @return
   */
  List<WeekTO> getSpecialWeeks( Date start, Date end );

  /**
   * Finds a week by its ids *
   * 
   * @param id
   * @return
   */
  WeekTO getWeek( int id );

  /**
   * Finds the current week according the date sent
   * 
   * @param date
   * @return
   */
  WeekTO getCurrentWeek( Date date );

  /**
   * Count the number of weeks.
   * 
   * @param firstDate1
   * @param firstDate2
   * @return
   */
  int countWeeks( Date firstDate1, Date firstDate2 );

  /**
   * Find the next number of week
   * 
   * @return
   */
  WeekTO getNextWeek( int currentYear );

  /**
   * Find Week by start date and final date.
   * 
   * @param start
   * @param end
   * @return
   */
  WeekTO getWeekByDates( WeekTO weekTO );

  /**
   * FInds the current week and las week
   * 
   * @param date
   * @return
   */
  List<WeekTO> getCurrentAndLasWeek( Date date );

  /**
   * Find Weeks between startDay and FinalDay
   */
  List<WeekDO> findWeeksByStartDayAndFinalDay( Date startDay, Date finalDay );

}
