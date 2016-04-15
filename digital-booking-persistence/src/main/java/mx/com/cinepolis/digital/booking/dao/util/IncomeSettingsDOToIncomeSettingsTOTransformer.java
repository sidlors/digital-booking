package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTO;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO;
import mx.com.cinepolis.digital.booking.model.IncomeSettingsDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.IncomeSettingsDO} to a
 * {@link mx.com.cinepolis.digital.booking.to.IncomeSettingsTO }
 * 
 * @author jreyesv
 */
public class IncomeSettingsDOToIncomeSettingsTOTransformer implements Transformer
{
  private Language language;

  /** Constructor default */
  public IncomeSettingsDOToIncomeSettingsTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /** Constructor by language */
  public IncomeSettingsDOToIncomeSettingsTOTransformer( Language language )
  {
    this.language = language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    IncomeSettingsTO to = null;
    if( object instanceof IncomeSettingsDO )
    {
      IncomeSettingsDO incomeSettingsDO = (IncomeSettingsDO) object;
      to = new IncomeSettingsTO();
      to.setId( incomeSettingsDO.getIdIncomeSettings().longValue() );
      to.setIdTheater( incomeSettingsDO.getIdTheater().getIdTheater().longValue() );
      to.setIncomeSettingsType( (IncomeSettingsTypeTO) new IncomeSettingsTypeDOToIncomeSettingsTypeTOTransformer(
          this.language ).transform( incomeSettingsDO.getIdIncomeSettingsType() ) );
      to.setGreenSemaphore( incomeSettingsDO.getQtGreenSemaphore() );
      to.setYellowSemaphore( incomeSettingsDO.getQtYellowSemaphore() );
      to.setRedSemaphore( incomeSettingsDO.getQtRedSemaphore() );
    }
    return to;
  }

}
