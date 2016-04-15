package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.RoleDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.RoleDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}.
 * 
 * @author afuentes
 */
public class RoleDOToCatalogTOTransformer implements Transformer
{

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object obj )
  {
    CatalogTO catalogTO = new CatalogTO();
    if( obj instanceof RoleDO )
    {
      RoleDO roleDO = (RoleDO) obj;
      catalogTO.setId( Long.valueOf( roleDO.getIdRole() ) );
      catalogTO.setName( roleDO.getDsRole() );
    }
    return catalogTO;
  }

}
