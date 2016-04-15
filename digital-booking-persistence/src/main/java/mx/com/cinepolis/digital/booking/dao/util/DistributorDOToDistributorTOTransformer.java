package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.model.DistributorDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.DistributorDO} to a {@link
 * mx.com.cinepolis.digital.booking.commons.to.DistributorTO}
 * 
 * @author kperez
 * @since 0.0.2
 */
public class DistributorDOToDistributorTOTransformer implements Transformer
{

  
  @Override
  public Object transform( Object object )
  {
    DistributorTO to = null;
    if ( object instanceof DistributorDO )
    {
     to = new DistributorTO();
     to.setFgActive( ((DistributorDO) object).isFgActive() );
     to.setId( ((DistributorDO) object).getIdDistributor().longValue() );
     to.setName( ((DistributorDO) object).getDsName() );
     to.setTimestamp( ((DistributorDO) object).getDtLastModification() );
     to.setUserId( Long.valueOf( ((DistributorDO) object).getIdLastUserModifier() ) );
     to.setIdVista( ((DistributorDO) object).getIdVista() );
     to.setShortName( ((DistributorDO) object).getDsShortName() );
    }
    return to;
  }
}
