package mx.com.cinepolis.digital.booking.dao.util;

import java.util.ArrayList;

import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PresaleTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenShowTO;
import mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO;
import mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenShowDO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.SpecialEventScreenTO} to a
 * {@link mx.com.cinepolis.digital.booking.model.BookingSpecialEventScreenDO}
 * 
 * @author jreyesv
 */
public class BookingSpecialEventScreenDOToSpecialEventScreenTOTransformer implements Transformer
{

  /**
   * Default constructor
   */
  public BookingSpecialEventScreenDOToSpecialEventScreenTOTransformer()
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    SpecialEventScreenTO specialEventScreenTO = null;
    if( object instanceof BookingSpecialEventScreenDO )
    {
      BookingSpecialEventScreenDO specialEventScreenDO = (BookingSpecialEventScreenDO) object;
      specialEventScreenTO = new SpecialEventScreenTO();
      specialEventScreenTO.setId( specialEventScreenDO.getIdBookingSpecialEventScreen() );
      specialEventScreenTO.setIdSpecialEvent( specialEventScreenDO.getIdBookingSpecialEvent()
          .getIdBookingSpecialEvent() );
      specialEventScreenTO.setIdScreen( specialEventScreenDO.getIdScreen().getIdScreen().longValue() );
      specialEventScreenTO.setIdObservation( specialEventScreenDO.getIdObservation() != null ? specialEventScreenDO
          .getIdObservation().getIdObservation() : null );
      CatalogTO status = new CatalogTO();
      status.setId( specialEventScreenDO.getIdBookingStatus().getIdBookingStatus().longValue() );
      specialEventScreenTO.setStatus( status );
      specialEventScreenTO.setSpecialEventScreenShows( new ArrayList<SpecialEventScreenShowTO>() );
      if( CollectionUtils.isNotEmpty( specialEventScreenDO.getBookingSpecialEventScreenShowDOList() ) )
      {
        for( BookingSpecialEventScreenShowDO specialEventScreenShowDO : specialEventScreenDO
            .getBookingSpecialEventScreenShowDOList() )
        {
          specialEventScreenTO.getSpecialEventScreenShows().add(
            (SpecialEventScreenShowTO) new BookingSpecialEventScreenShowDOToSpecialEventScreenShowTOTransformer()
                .transform( specialEventScreenShowDO ) );
        }
      }
      if( CollectionUtils.isNotEmpty( specialEventScreenDO.getPresaleDOList() ) )
      {
        specialEventScreenTO.setPresaleTO( (PresaleTO) new PresaleDOToPresaleTOTransformer()
            .transform( specialEventScreenDO.getPresaleDOList().get( 0 ) ) );
      }
      else
      {
        specialEventScreenTO.setPresaleTO( new PresaleTO( null, false ));
      }
    }
    return specialEventScreenTO;
  }

}
