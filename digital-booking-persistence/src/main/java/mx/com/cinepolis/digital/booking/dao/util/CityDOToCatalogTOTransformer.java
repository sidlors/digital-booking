package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.CityDO;
import mx.com.cinepolis.digital.booking.model.CityLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.CityDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
public class CityDOToCatalogTOTransformer implements Transformer{
	/**
	 * Languaje
	 */
	private Language language;

	  /**
	   * Constructor default
	   */
	  public CityDOToCatalogTOTransformer()
	  {
	    this.language = Language.ENGLISH;
	  }

	  /**
	   * Constructor with {@link mx.com.cinepolis.digital.booking.commons.constants.Language}
	   * 
	   * @param language
	   */
	  public CityDOToCatalogTOTransformer( Language language )
	  {
	    this.language = language;
	  }

	  /*
	   * (non-Javadoc)
	   * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
	   */
	@Override
	public Object transform(Object object) {
		CatalogTO to = null;
	    if( object instanceof CityDO )
	    {
	      to = new CatalogTO();
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
	    }
	    return to;
	}

}
