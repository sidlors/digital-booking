package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.DistributorDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.DistributorDO} to a {@link
 * mx.com.cinepolis.digital.booking.to.CatalogTO.CatalogTO()}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public class DistributorDOToCatalogTOTransformer implements Transformer
{

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    CatalogTO to = null;
    if( object instanceof DistributorDO )
    {
      to = new CatalogTO();
      to.setFgActive( ((DistributorDO) object).isFgActive() );
      to.setId( ((DistributorDO) object).getIdDistributor().longValue() );
      to.setName( ((DistributorDO) object).getDsName() );
      to.setTimestamp( ((DistributorDO) object).getDtLastModification() );
      to.setUserId( Long.valueOf( ((DistributorDO) object).getIdLastUserModifier() ) );
      to.setIdVista( ((DistributorDO) object).getIdVista() );
    }
    return to;
  }
}
