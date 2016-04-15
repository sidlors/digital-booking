package mx.com.cinepolis.digital.booking.dao.util;

import java.util.ArrayList;

import mx.com.cinepolis.digital.booking.commons.to.CityTO;
import mx.com.cinepolis.digital.booking.model.CityDO;
import mx.com.cinepolis.digital.booking.model.CityLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.CityTO} to a
 * {@link mx.com.cinepolis.digital.booking.model.CityDO}
 * 
 * @author jreyesv
 * @since 0.0.1
 */
public class CityTOToCityDOTransformer implements Transformer
{

  /**
   * Constructor default
   */
  public CityTOToCityDOTransformer()
  {
  }

  /*
   * (non-Javadoc)
   * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
   */
  @Override
  public Object transform( Object object )
  {
    CityDO cityDO = null;
    if( object instanceof CityTO )
    {
      CityTO cityTO = (CityTO) object;
      cityDO = new CityDO();
      cityDO.setFgActive( cityTO.isFgActive() );
      if( cityTO.getId() != null )
      {
        cityDO.setIdCity( cityTO.getId().intValue() );
      }
      else
      {
        cityDO.setIdCity( null );
      }
      cityDO.setIdVista( cityTO.getIdVista() );
      cityDO.setCityLanguageDOList( new ArrayList<CityLanguageDO>() );
      // Sets the country information.
      cityDO.setIdCountry( null );
      // Sets the state information.
      cityDO.setIdState( null );
    }
    return cityDO;
  }

}
