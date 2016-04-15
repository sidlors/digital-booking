package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTO;
import mx.com.cinepolis.digital.booking.model.IncomeSettingsDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.IncomeSettingsDO}
 * 
 * @author jreyesv
 * @since 0.2.0
 */
public interface IncomeSettingsDAO extends GenericDAO<IncomeSettingsDO>
{

  /**
   * Creates a {link mx.com.cinepolis.digital.booking.model.IncomeSettingsDO}
   * 
   * @param incomeSettingTO The income setting to create.
   */
  void save( IncomeSettingsTO incomeSettingTO );

  /**
   * Updates a {link mx.com.cinepolis.digital.booking.model.IncomeSettingsDO}
   * 
   * @param incomeSettingTO The income setting to update.
   */
  void update( IncomeSettingsTO incomeSettingTO );

  /**
   * Logically deletes a {link mx.com.cinepolis.digital.booking.model.IncomeSettingsDO}
   * 
   * @param incomeSettingTO The income setting to logically delete.
   */
  void delete( IncomeSettingsTO incomeSettingTO );

  /**
   * Finds the records of incomeSettings by theater
   * 
   * @param idTheater
   * @return
   */
  List<IncomeSettingsTO> findIncomeSettingsByTheater( Long idTheater );
}
