package mx.com.cinepolis.digital.booking.dao.util;

import java.util.ArrayList;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.to.RegionTO;
import mx.com.cinepolis.digital.booking.model.EmailDO;
import mx.com.cinepolis.digital.booking.model.PersonDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.RegionLanguageDO;
import mx.com.cinepolis.digital.booking.model.TerritoryLanguageDO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.RegionDO} to a
 * {@link mx.com.cinepolis.digital.booking.model.to.RegionTO<CatalogTO, Integer>}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public class RegionDOToRegionTOTransformer implements Transformer
{
  private Language language;

  /**
   * Constructor default
   */
  public RegionDOToRegionTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor with {@link mx.com.cinepolis.digital.booking.commons.constants.Language}
   * 
   * @param language
   */
  public RegionDOToRegionTOTransformer( Language laguage )
  {
    this.language = laguage;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    RegionTO<CatalogTO, CatalogTO> to = null;
    if( object instanceof RegionDO )
    {
      RegionDO regionDO = (RegionDO) object;
      String territoryName = "";
      for( TerritoryLanguageDO territoryLanguageDO : regionDO.getIdTerritory().getTerritoryLanguageDOList() )
      {
        if( territoryLanguageDO.getIdLanguage().getIdLanguage().equals( this.language.getId() ) )
        {
          territoryName = territoryLanguageDO.getDsName();
        }
      }

      to = new RegionTO<CatalogTO, CatalogTO>( new CatalogTO(), new CatalogTO( regionDO.getIdTerritory()
          .getIdTerritory().longValue(), territoryName ) );
      to.getCatalogRegion().setId( Long.valueOf( regionDO.getIdRegion().intValue() ) );
      to.getCatalogRegion().setTimestamp( regionDO.getDtLastModification() );
      to.getCatalogRegion().setUserId( Long.valueOf( regionDO.getIdLastUserModifier() ) );

      for( RegionLanguageDO regionLanguageDO : regionDO.getRegionLanguageDOList() )
      {
        if( regionLanguageDO.getIdLanguage().getIdLanguage().equals( this.language.getId() ) )
        {
          to.getCatalogRegion().setName( regionLanguageDO.getDsName() );
        }
      }

      this.extractPersonAndEmail( to, regionDO );

    }
    return to;
  }

  private void extractPersonAndEmail( RegionTO<CatalogTO, CatalogTO> to, RegionDO regionDO )
  {
    to.setPersons( new ArrayList<PersonTO>() );
    if( CollectionUtils.isNotEmpty( regionDO.getPersonDOList() ) )
    {

      for( PersonDO personDO : regionDO.getPersonDOList() )
      {
        PersonTO personTO = new PersonTO();
        personTO.setId( personDO.getIdPerson().longValue() );
        personTO.setName( personDO.getDsName() );
        personTO.setDsLastname( personDO.getDsLastname() );
        personTO.setDsMotherLastname( personDO.getDsMotherLastname() );
        if( CollectionUtils.isNotEmpty( personDO.getEmailDOList() ) )
        {
          personTO.setEmails( new ArrayList<CatalogTO>() );
          for( EmailDO emailDO : personDO.getEmailDOList() )
          {
            personTO.getEmails().add( new CatalogTO( emailDO.getIdEmail().longValue(), emailDO.getDsEmail() ) );
          }
        }
        to.getPersons().add( personTO );
      }
    }
  }

}
