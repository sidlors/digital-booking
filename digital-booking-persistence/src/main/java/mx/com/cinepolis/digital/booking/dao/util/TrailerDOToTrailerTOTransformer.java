package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.TrailerTO;
import mx.com.cinepolis.digital.booking.model.TrailerDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.TrailerDO} to a {@link
 * mx.com.cinepolis.digital.booking.to.CatalogTO.CatalogTO()}
 * 
 * @author jcarbajal
 */
public class TrailerDOToTrailerTOTransformer implements Transformer
{

  @Override
  public Object transform( Object object )
  {
    TrailerTO trailerTO=null;
    if(object instanceof TrailerDO)
    {
      
      trailerTO = new TrailerTO();
      trailerTO.setIdTrailer( ((TrailerDO) object).getIdTrailer() );
      trailerTO.setDsGenre( ((TrailerDO) object).getDsGenre() );
    }
    return null;
  }
  

}
