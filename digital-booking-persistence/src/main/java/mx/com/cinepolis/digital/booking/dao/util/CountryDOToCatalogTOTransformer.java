package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.CountryDO;
import mx.com.cinepolis.digital.booking.model.CountryLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.CountryDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}
 * 
 * @author afuentes
 */
public class CountryDOToCatalogTOTransformer implements Transformer
{
  private Language language = Language.ENGLISH;

  /**
   * Default constructor
   */
  public CountryDOToCatalogTOTransformer()
  {
  }

  /**
   * Constructor by language
   * 
   * @param language The language
   */
  public CountryDOToCatalogTOTransformer( Language language )
  {
    this.language = language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    CatalogTO to = null;
    if( object instanceof CountryDO )
    {
      CountryDO countryDO = (CountryDO) object;
      to = new CatalogTO();
      to.setId( countryDO.getIdCountry().longValue() );
      to.setIdVista( ((CountryDO) object).getIdVista() );
      to.setTimestamp( ((CountryDO) object).getDtLastModification() );
      to.setUserId( Long.valueOf( ((CountryDO) object).getIdLastUserModifier() ) );
      for( CountryLanguageDO m : countryDO.getCountryLanguageDOList() )
      {
        if( m.getIdLanguage().getIdLanguage().equals( language.getId() ) )
        {
          to.setName( m.getDsName() );
          break;
        }
      }
    }
    return to;
  }

}
