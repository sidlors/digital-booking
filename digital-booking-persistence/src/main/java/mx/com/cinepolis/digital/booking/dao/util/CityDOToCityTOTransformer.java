package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.CityTO;
import mx.com.cinepolis.digital.booking.commons.to.StateTO;
import mx.com.cinepolis.digital.booking.model.CityDO;
import mx.com.cinepolis.digital.booking.model.CityLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.CityDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.CityTO}
 * 
 * @author jreyesv
 * @since 0.0.1
 */
public class CityDOToCityTOTransformer implements Transformer
{
  /**
   * Languaje
   */
  private Language language;

  /**
   * Constructor default
   */
  public CityDOToCityTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor with {@link mx.com.cinepolis.digital.booking.commons.constants.Language}
   * 
   * @param language
   */
  public CityDOToCityTOTransformer( Language language )
  {
    this.language = language;
  }

  /*
   * (non-Javadoc)
   * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
   */
  @SuppressWarnings("unchecked")
  @Override
  public Object transform( Object object )
  {
    CityTO to = null;
    if( object instanceof CityDO )
    {
      to = new CityTO();
      to.setFgActive( ((CityDO) object).isFgActive() );
      to.setId( ((CityDO) object).getIdCity().longValue() );
      to.setIdVista( ((CityDO) object).getIdVista() );

      for( CityLanguageDO cityLanguageDO : ((CityDO) object).getCityLanguageDOList() )
      {
        if( cityLanguageDO.getIdLanguage().getIdLanguage().equals( this.language.getId() ) )
        {
          to.setName( cityLanguageDO.getDsName() );
          break;
        }
      }
      to.setTimestamp( ((CityDO) object).getDtLastModification() );
      to.setUserId( Long.valueOf( ((CityDO) object).getIdLastUserModifier() ) );
      // Sets the coutry information.
      to.setCountry( (CatalogTO) new CountryDOToCatalogTOTransformer( language ).transform( ((CityDO) object)
          .getIdCountry() ) );
      // Sets the state information.
      to.setState( (StateTO<CatalogTO, Number>) new StateDOToStateTOTransformer( language )
          .transform( ((CityDO) object).getIdState() ) );
    }
    return to;
  }

}
