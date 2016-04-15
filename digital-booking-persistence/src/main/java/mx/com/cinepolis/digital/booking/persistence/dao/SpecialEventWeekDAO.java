package mx.com.cinepolis.digital.booking.persistence.dao;

import mx.com.cinepolis.digital.booking.commons.to.SpecialEventTO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventDO;
import mx.com.cinepolis.digital.booking.model.SpecialEventWeekDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.SpecialEventWeekDO}
 * 
 * @author jcarbajal
 */
public interface SpecialEventWeekDAO extends GenericDAO<SpecialEventWeekDO>
{

  /**
   * Create record
   */
  void createSpecialEventWeek( BookingSpecialEventDO specialEventDO, SpecialEventTO specialEventTO );
  /**
   * Edit Weeks by SpecialEvent
   */

}
