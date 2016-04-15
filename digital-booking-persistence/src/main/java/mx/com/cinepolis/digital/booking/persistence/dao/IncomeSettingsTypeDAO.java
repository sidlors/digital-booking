package mx.com.cinepolis.digital.booking.persistence.dao;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO;
import mx.com.cinepolis.digital.booking.model.IncomeSettingsTypeDO;
import mx.com.cinepolis.digital.booking.persistence.base.dao.GenericDAO;

/**
 * DAO interface for manipulating entity {@link mx.com.cinepolis.digital.booking.model.IncomeSettingsTypeDO}
 * 
 * @author jreyesv
 * @since 0.2.0
 */
public interface IncomeSettingsTypeDAO extends GenericDAO<IncomeSettingsTypeDO>
{

  /**
   * Finds the income setting type
   * 
   * @param id of the income setting type
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO}
   */
  IncomeSettingsTypeTO get( int id );

  /**
   * Finds the income setting type
   * 
   * @param id of the income setting type
   * @param language the language
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO}
   */
  IncomeSettingsTypeTO get( int id, Language language );

  /**
   * Finds all the income settings type
   * 
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO}
   */
  List<IncomeSettingsTypeTO> getAll();

  /**
   * Finds all the income settings type
   * 
   * @param language the language
   * @return a {@link mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO}
   */
  List<IncomeSettingsTypeTO> getAll( Language language );

}
