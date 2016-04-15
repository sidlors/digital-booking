package mx.com.cinepolis.digital.booking.service.util;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.commons.to.TheaterTO;
import mx.com.cinepolis.digital.booking.model.CityLanguageDO;
import mx.com.cinepolis.digital.booking.model.RegionLanguageDO;
import mx.com.cinepolis.digital.booking.model.TerritoryLanguageDO;
import mx.com.cinepolis.digital.booking.model.TheaterDO;
import mx.com.cinepolis.digital.booking.model.TheaterLanguageDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.TheaterDO} into a
 * {@link mx.com.cinepolis.digital.booking.commons.to.TheaterTO} with only the following attributes:
 * <ul>
 * <li>id
 * <li>nuTheater
 * <li>region
 * <li>name
 * </ul>
 * 
 * @author gsegura
 */
public class TheaterDOToTheaterTOSimpleTransformer implements Transformer
{

  private Language language;

  public TheaterDOToTheaterTOSimpleTransformer()
  {
    language = Language.ENGLISH;
  }

  public TheaterDOToTheaterTOSimpleTransformer( Language language )
  {
    this.language = language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    TheaterTO theaterTO = null;
    if( object instanceof TheaterDO )
    {
      TheaterDO theaterDO = (TheaterDO) object;
      theaterTO = new TheaterTO();
      theaterTO.setId( theaterDO.getIdTheater().longValue() );
      CatalogTO catalogRegion = extractRegion( theaterDO );
      CatalogTO idTerritory = extractTerritory( theaterDO );
      CatalogTO city = extractCity( theaterDO );
      theaterTO.setRegion( new RegionTO<CatalogTO, CatalogTO>( catalogRegion, idTerritory ) );
      theaterTO.setCity( city );
      theaterTO.setNuTheater( theaterDO.getNuTheater() );

      for( TheaterLanguageDO theaterLanguageDO : theaterDO.getTheaterLanguageDOList() )
      {
        if( theaterLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
        {
          theaterTO.setName( theaterLanguageDO.getDsName() );
          break;
        }
      }
    }
    return theaterTO;
  }

  private CatalogTO extractCity( TheaterDO theaterDO )
  {
    CatalogTO city = new CatalogTO( theaterDO.getIdCity().getIdCity().longValue() );
    for( CityLanguageDO c : theaterDO.getIdCity().getCityLanguageDOList() )
    {
      if( c.getIdLanguage().getIdLanguage().equals( this.language.getId() ) )
      {
        city.setName( c.getDsName() );
        break;
      }
    }
    return city;
  }

  private CatalogTO extractTerritory( TheaterDO theaterDO )
  {
    CatalogTO idTerritory = new CatalogTO( theaterDO.getIdRegion().getIdTerritory().getIdTerritory().longValue() );
    for( TerritoryLanguageDO territoryLanguageDO : theaterDO.getIdRegion().getIdTerritory()
        .getTerritoryLanguageDOList() )
    {
      if( territoryLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
      {
        idTerritory.setName( territoryLanguageDO.getDsName() );
        break;
      }
    }
    return idTerritory;
  }

  private CatalogTO extractRegion( TheaterDO theaterDO )
  {
    CatalogTO catalogRegion = new CatalogTO( theaterDO.getIdRegion().getIdRegion().longValue() );
    for( RegionLanguageDO regionLanguageDO : theaterDO.getIdRegion().getRegionLanguageDOList() )
    {
      if( regionLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
      {
        catalogRegion.setName( regionLanguageDO.getDsName() );
        break;
      }
    }
    return catalogRegion;
  }

}
