package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.TerritoryDO;
import mx.com.cinepolis.digital.booking.model.TerritoryLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.TerritoryDO} to a
 * {@link mx.com.cinepolis.digital.booking.to.CatalogTO.CatalogTO }
 * 
 * @author gsegura
 * @since 0.0.1
 */
public class TerritoryDOToCatalogTOTransformer implements Transformer
{
  private Language language;

  /**
   * Constructor default
   */
  public TerritoryDOToCatalogTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor with {@link mx.com.cinepolis.digital.booking.commons.constants.Language}
   * 
   * @param language
   */
  public TerritoryDOToCatalogTOTransformer( Language laguage )
  {
    this.language = laguage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    CatalogTO to = null;
    if( object instanceof TerritoryDO )
    {
      to = new CatalogTO();
      to.setId( ((TerritoryDO) object).getIdTerritory().longValue() );
      to.setTimestamp( ((TerritoryDO) object).getDtLastModification() );
      to.setUserId( Long.valueOf( ((TerritoryDO) object).getIdLastUserModifier() ) );

      for( TerritoryLanguageDO territoryLanguageDO : ((TerritoryDO) object).getTerritoryLanguageDOList() )
      {
        if( territoryLanguageDO.getIdLanguage().getIdLanguage().equals( this.language.getId() ) )
        {
          to.setName( territoryLanguageDO.getDsName() );
        }
      }
    }
    return to;
  }

}
