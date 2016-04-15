package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.CategoryLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.CategoryDO} to a {@link
 * mx.com.cinepolis.digital.booking.to.CatalogTO.CatalogTO()}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public class CategoryDOToCatalogTOTransformer implements Transformer
{

  private Language language;

  /**
   * Constructor default
   */
  public CategoryDOToCatalogTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor with {@link mx.com.cinepolis.digital.booking.commons.constants.Language}
   * 
   * @param language
   */
  public CategoryDOToCatalogTOTransformer( Language language )
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
    if( object instanceof CategoryDO )
    {
      to = new CatalogTO();
      to.setFgActive( ((CategoryDO) object).isFgActive() );
      to.setId( ((CategoryDO) object).getIdCategory().longValue() );

      for( CategoryLanguageDO categoryLanguageDO : ((CategoryDO) object).getCategoryLanguageDOList() )
      {
        if( categoryLanguageDO.getIdLanguage().getIdLanguage().equals( this.language.getId() ) )
        {
          to.setName( categoryLanguageDO.getDsName() );
          break;
        }
      }
      to.setTimestamp( ((CategoryDO) object).getDtLastModification() );
      to.setUserId( Long.valueOf( ((CategoryDO) object).getIdLastUserModifier() ) );
    }
    return to;
  }

}
