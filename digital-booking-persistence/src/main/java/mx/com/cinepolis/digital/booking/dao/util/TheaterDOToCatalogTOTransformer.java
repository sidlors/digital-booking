package mx.com.cinepolis.digital.booking.dao.util;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.TheaterLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.TheaterDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}.
 * 
 * @author afuentes
 */
public class TheaterDOToCatalogTOTransformer implements Transformer
{
  private Language language;

  /**
   * Default constructor.
   */
  public TheaterDOToCatalogTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor with {@link Language}
   * 
   * @param language The {@link Language}.
   */
  public TheaterDOToCatalogTOTransformer( Language language )
  {
    this.language = language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object obj )
  {
    CatalogTO catalogTO = new CatalogTO();
    if( obj instanceof TheaterDO )
    {
      TheaterDO theaterDO = (TheaterDO) obj;
      catalogTO.setId( Long.valueOf( theaterDO.getIdTheater() ) );
      catalogTO.setName( getTheaterName( theaterDO.getTheaterLanguageDOList() ) );
    }
    return catalogTO;
  }

  private String getTheaterName( List<TheaterLanguageDO> theaterLanguageDOList )
  {
    String name = null;
    for( TheaterLanguageDO theaterLanguageDO : theaterLanguageDOList )
    {
      if( theaterLanguageDO.getIdLanguage().getIdLanguage().intValue() == language.getId() )
      {
        name = theaterLanguageDO.getDsName();
      }
    }
    return name;
  }

}
