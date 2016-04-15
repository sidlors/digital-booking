package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.SystemMenuTO;
import mx.com.cinepolis.digital.booking.model.SystemMenuDO;
import mx.com.cinepolis.digital.booking.model.SystemMenuLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.SystemMenuDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.SystemMenuTO}
 * 
 * @author afuentes
 */
public class SystemMenuDOToSystemMenuTOTransformer implements Transformer
{

  private Language language;

  /**
   * Default constructor.
   */
  public SystemMenuDOToSystemMenuTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor with {@link mx.com.cinepolis.digital.booking.commons.constants.Language}
   * 
   * @param language
   */
  public SystemMenuDOToSystemMenuTOTransformer( Language language )
  {
    this.language = language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object obj )
  {
    SystemMenuTO menuTO = new SystemMenuTO();
    if( obj instanceof SystemMenuDO )
    {
      SystemMenuDO menuDO = (SystemMenuDO) obj;
      menuTO.setId( Long.valueOf( menuDO.getIdSystemMenu() ) );
      menuTO.setUrl( menuDO.getDsSystemMenuUrl() );
      menuTO.setIcon( menuDO.getDsSystemMenuIcon() );
      menuTO.setIsFunction( menuDO.isFgFunction() );
      menuTO.setOrder( menuDO.getOrder() );
      for( SystemMenuLanguageDO systemMenuLanguageDO : menuDO.getSystemMenuLanguageDOList() )
      {
        if( systemMenuLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
        {
          menuTO.setName( systemMenuLanguageDO.getDsName() );
        }
      }
    }
    return menuTO;
  }

}
