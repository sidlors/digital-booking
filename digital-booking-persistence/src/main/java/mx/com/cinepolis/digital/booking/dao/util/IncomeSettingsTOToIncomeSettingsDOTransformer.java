package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTO;
import mx.com.cinepolis.digital.booking.model.IncomeSettingsDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.to.IncomeSettingsTO } to a
 * {@link mx.com.cinepolis.digital.booking.model.IncomeSettingsDO}
 * 
 * @author jreyesv
 */
public class IncomeSettingsTOToIncomeSettingsDOTransformer implements Transformer
{

  /** Constructor default */
  public IncomeSettingsTOToIncomeSettingsDOTransformer()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    IncomeSettingsDO incomeSettingsDO = null;
    if( object instanceof IncomeSettingsTO )
    {
      IncomeSettingsTO incomeSettingsTO = (IncomeSettingsTO) object;
      incomeSettingsDO = new IncomeSettingsDO();
      incomeSettingsDO.setIdIncomeSettings( incomeSettingsTO.getId() != null ? incomeSettingsTO.getId().intValue() : null );
      incomeSettingsDO.setIdTheater( null );
      incomeSettingsDO.setIdIncomeSettingsType( null );
      incomeSettingsDO.setQtGreenSemaphore( incomeSettingsTO.getGreenSemaphore() );
      incomeSettingsDO.setQtYellowSemaphore( incomeSettingsTO.getYellowSemaphore() );
      incomeSettingsDO.setQtRedSemaphore( incomeSettingsTO.getRedSemaphore() );
    }
    return incomeSettingsDO;
  }

}
