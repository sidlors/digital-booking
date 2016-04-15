package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.IncomeSettingsTypeTO;
import mx.com.cinepolis.digital.booking.model.IncomeSettingsTypeDO;
import mx.com.cinepolis.digital.booking.model.IncomeSettingsTypeLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.IncomeSettingsTypeDO} to a
 * {@link mx.com.cinepolis.digital.booking.to.IncomeSettingsTypeTO }
 * 
 * @author jreyesv
 */
public class IncomeSettingsTypeDOToIncomeSettingsTypeTOTransformer implements Transformer
{
  private Language language;

  /** Constructor default */
  public IncomeSettingsTypeDOToIncomeSettingsTypeTOTransformer()
  {
  }

  /** Constructor by language */
  public IncomeSettingsTypeDOToIncomeSettingsTypeTOTransformer( Language language )
  {
    this.language = language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    IncomeSettingsTypeTO to = null;
    if( object instanceof IncomeSettingsTypeDO )
    {
      IncomeSettingsTypeDO incomeSettingsTypeDO = (IncomeSettingsTypeDO) object;
      to = new IncomeSettingsTypeTO();
      to.setId( incomeSettingsTypeDO.getIdIncomeSettingsType().longValue() );
      to.setIndicatorType( incomeSettingsTypeDO.getDsIndicatorType() );

      for( IncomeSettingsTypeLanguageDO incomeSettingsTypeLanguageDO : incomeSettingsTypeDO
          .getIncomeSettingsTypeLanguageDOList() )
      {
        if( incomeSettingsTypeLanguageDO.getIdLanguage().getIdLanguage().equals( this.language.getId() ) )
        {
          to.setName( incomeSettingsTypeLanguageDO.getDsName() );
          break;
        }
      }
    }

    return to;
  }

}
