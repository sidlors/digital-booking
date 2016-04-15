package mx.com.cinepolis.digital.booking.dao.util;

import java.util.List;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.RegionLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.RegionDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.CatalogTO}.
 * 
 * @author afuentes
 */
public class RegionDOToCatalogTOTransformer implements Transformer
{

  private Language language;

  /**
   * Default constructor.
   */
  public RegionDOToCatalogTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor with {@link Language}
   * 
   * @param language The {@link Language}.
   */
  public RegionDOToCatalogTOTransformer( Language language )
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
    if( obj instanceof RegionDO )
    {
      RegionDO regionDO = (RegionDO) obj;
      catalogTO.setId( Long.valueOf( regionDO.getIdRegion() ) );
      catalogTO.setName( getRegionName( regionDO.getRegionLanguageDOList() ) );
    }
    return catalogTO;
  }

  private String getRegionName( List<RegionLanguageDO> regionLanguageDOList )
  {
    String name = null;
    for( RegionLanguageDO regionLanguageDO : regionLanguageDOList )
    {
      if( regionLanguageDO.getIdLanguage().getIdLanguage().intValue() == language.getId() )
      {
        name = regionLanguageDO.getDsName();
      }
    }
    return name;
  }

}
