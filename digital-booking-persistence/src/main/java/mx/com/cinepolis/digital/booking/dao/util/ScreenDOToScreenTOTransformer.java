package mx.com.cinepolis.digital.booking.dao.util;

import java.util.ArrayList;

import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.ScreenTO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.ScreenDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.ScreenDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.ScreenTO}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
public class ScreenDOToScreenTOTransformer implements Transformer
{
  /**
   * Language
   */
  private Language language;

  /**
   * Constructor default
   */
  public ScreenDOToScreenTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor with {@link mx.com.cinepolis.digital.booking.commons.constants.Language}
   * 
   * @param language
   */
  public ScreenDOToScreenTOTransformer( Language language )
  {
    this.language = language;
  }

  /*
   * (non-Javadoc)
   * @see org.apache.commons.collections.Transformer#transform(java.lang.Object)
   */
  @Override
  public Object transform( Object object )
  {
    ScreenTO screenTO = null;
    if( object instanceof ScreenDO )
    {
      screenTO = new ScreenTO();
      ScreenDO entity = (ScreenDO) object;
      screenTO.setFgActive( entity.isFgActive() );
      screenTO.setTimestamp( entity.getDtLastModification() );
      screenTO.setUserId( Long.valueOf( entity.getIdLastUserModifier() ) );
      screenTO.setId( entity.getIdScreen().longValue() );
      screenTO.setIdVista( entity.getIdVista() );
      screenTO.setNuCapacity( entity.getNuCapacity() );
      screenTO.setNuScreen( entity.getNuScreen() );
      screenTO.setOriginalNuScreen( entity.getNuScreen() );
      screenTO.setIdTheater( entity.getIdTheater().getIdTheater() );
      screenTO.setMovieFormats( new ArrayList<CatalogTO>() );
      screenTO.setSoundFormats( new ArrayList<CatalogTO>() );
      CategoryDOToCatalogTOTransformer categoryTransformer = new CategoryDOToCatalogTOTransformer( language );
      for( CategoryDO categoryDO : entity.getCategoryDOList() )
      {
        if( categoryDO.getIdCategoryType().getIdCategoryType().equals( CategoryType.MOVIE_FORMAT.getId() )
            && categoryDO.isFgActive() )
        {
          screenTO.getMovieFormats().add( (CatalogTO) categoryTransformer.transform( categoryDO ) );
        }
        else if( categoryDO.getIdCategoryType().getIdCategoryType().equals( CategoryType.SOUND_FORMAT.getId() )
            && categoryDO.isFgActive() )
        {
          screenTO.getSoundFormats().add( (CatalogTO) categoryTransformer.transform( categoryDO ) );
        }
        else if( categoryDO.getIdCategoryType().getIdCategoryType().equals( CategoryType.SCREEN_FORMAT.getId() )
            && categoryDO.isFgActive() )
        {
          screenTO.setScreenFormat( (CatalogTO) categoryTransformer.transform( categoryDO ) );
        }
      }

    }

    return screenTO;
  }

}
