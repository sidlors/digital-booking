package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;

import org.apache.commons.collections.Transformer;

public class TheaterTOToCatalogTOTransformer implements Transformer
{

  /**
   * Default constructor.
   */
  public TheaterTOToCatalogTOTransformer()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object obj )
  {
    CatalogTO catalogTO = new CatalogTO();
    if( obj instanceof TheaterTO )
    {
      TheaterTO theaterTO = (TheaterTO) obj;
      catalogTO.setId( Long.valueOf( theaterTO.getId() ) );
      catalogTO.setName( theaterTO.getName() );
    }
    return catalogTO;
  }
}
