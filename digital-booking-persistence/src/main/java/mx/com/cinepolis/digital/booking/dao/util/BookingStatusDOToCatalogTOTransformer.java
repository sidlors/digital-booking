package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.BookingStatusDO;
import mx.com.cinepolis.digital.booking.model.BookingStatusLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.BookingStatusDO} to a {@link
 * mx.com.cinepolis.digital.booking.to.CatalogTO }
 * 
 * @author gsegura
 * @since 0.2.0
 */
public class BookingStatusDOToCatalogTOTransformer implements Transformer
{
  private Language language;

  /** Constructor default */
  public BookingStatusDOToCatalogTOTransformer()
  {
  }

  /** Constructor by language */
  public BookingStatusDOToCatalogTOTransformer( Language language )
  {
    this.language = language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    CatalogTO to = null;
    if( object instanceof BookingStatusDO )
    {
      to = new CatalogTO();
      to.setId( ((BookingStatusDO) object).getIdBookingStatus().longValue() );

      for( BookingStatusLanguageDO bookingStatusLanguageDO : ((BookingStatusDO) object)
          .getBookingStatusLanguageDOList() )
      {
        if( bookingStatusLanguageDO.getIdLanguage().getIdLanguage().equals( this.language.getId() ) )
        {
          to.setName( bookingStatusLanguageDO.getDsName() );
          break;
        }
      }
    }

    return to;
  }

}
