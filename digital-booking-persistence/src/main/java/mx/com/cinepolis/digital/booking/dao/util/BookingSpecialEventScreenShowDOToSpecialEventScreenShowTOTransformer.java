package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenShowTO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenShowDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenShowTO} to a
 * {@link mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenShowDO}
 * 
 * @author jreyesv
 */
public class BookingSpecialEventScreenShowDOToSpecialEventScreenShowTOTransformer implements Transformer
{

  /**
   * Default constructor
   */
  public BookingSpecialEventScreenShowDOToSpecialEventScreenShowTOTransformer()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    SpecialEventScreenShowTO specialEventScreenShowTO = null;
    if( object instanceof BookingSpecialEventScreenShowDO )
    {
      BookingSpecialEventScreenShowDO specialEventScreenShowDO = (BookingSpecialEventScreenShowDO) object;
      specialEventScreenShowTO = new SpecialEventScreenShowTO();
      specialEventScreenShowTO.setId( specialEventScreenShowDO.getIdBookingSpecialEventScreenShow() );
      specialEventScreenShowTO.setIdSpecialEventScreen( specialEventScreenShowDO.getIdBookingSpecialEventScreen().getIdBookingSpecialEventScreen() );
      specialEventScreenShowTO.setNuShow( specialEventScreenShowDO.getNuShow() );
    }
    return specialEventScreenShowTO;
  }

}
