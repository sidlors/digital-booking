package mx.com.cinepolis.digital.booking.dao.util;

import java.util.ArrayList;

import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO;
import mx.com.cinepolis.digital.booking.commons.to.PersonTO;
import mx.com.cinepolis.digital.booking.commons.to.UserTO;
import mx.com.cinepolis.digital.booking.model.NewsFeedDO;
import mx.com.cinepolis.digital.booking.model.RegionDO;
import mx.com.cinepolis.digital.booking.model.RegionLanguageDO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.NewsFeedDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.NewsFeedObservationTO}
 * 
 * @author gsegura
 */
public class NewsFeedDOToNewsFeedObservationTOTransformer implements Transformer
{

  private Language language;

  /**
   * Constructor default
   */
  public NewsFeedDOToNewsFeedObservationTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Cosntructor by language
   * 
   * @param language
   */
  public NewsFeedDOToNewsFeedObservationTOTransformer( Language language )
  {
    this.language = language;
  }

  @Override
  public Object transform( Object object )
  {
    NewsFeedObservationTO to = null;
    if( object instanceof NewsFeedDO )
    {
      NewsFeedDO newsFeedDO = (NewsFeedDO) object;
      to = new NewsFeedObservationTO();
      to.setId( newsFeedDO.getIdObservation().getIdObservation() );
      to.setObservation( newsFeedDO.getIdObservation().getDsObservation() );
      to.setIdNewsFeed( newsFeedDO.getIdNewsFeed() );
      to.setStart( newsFeedDO.getDtStart() );
      to.setEnd( newsFeedDO.getDtEnd() );
      to.setFgActive( newsFeedDO.isFgActive() );
      to.setTimestamp( newsFeedDO.getDtLastModification() );
      to.setUserId( Long.valueOf( newsFeedDO.getIdLastUserModifier() ) );
      to.setUsername( newsFeedDO.getIdObservation().getIdUser().getDsUsername() );
      extractRegions( to, newsFeedDO );
      to.setPersonTO( new PersonTO() );
      to.getPersonTO().setId( newsFeedDO.getIdObservation().getIdUser().getIdPerson().getIdPerson().longValue() );
      to.getPersonTO().setName( newsFeedDO.getIdObservation().getIdUser().getIdPerson().getDsName() );
      to.getPersonTO().setDsLastname( newsFeedDO.getIdObservation().getIdUser().getIdPerson().getDsLastname() );
      to.getPersonTO().setDsMotherLastname(
        newsFeedDO.getIdObservation().getIdUser().getIdPerson().getDsMotherLastname() );
      to.getPersonTO()
          .setFullName( PersonDOUtils.buildFullName( newsFeedDO.getIdObservation().getIdUser().getIdPerson() ) );
      UserTO user = new UserTO();
      user.setId( newsFeedDO.getIdObservation().getIdUser().getIdUser().longValue() );
      user.setUsername( newsFeedDO.getIdObservation().getIdUser().getDsUsername() );
      to.setUser( user );

    }
    return to;
  }

  private void extractRegions( NewsFeedObservationTO to, NewsFeedDO newsFeedDO )
  {
    if( CollectionUtils.isNotEmpty( newsFeedDO.getRegionDOList() ) )
    {
      to.setRegions( new ArrayList<CatalogTO>() );
      for( RegionDO regionDO : newsFeedDO.getRegionDOList() )
      {
        CatalogTO region = new CatalogTO( regionDO.getIdRegion().longValue() );
        for( RegionLanguageDO regionLanguageDO : regionDO.getRegionLanguageDOList() )
        {
          if( regionLanguageDO.getIdLanguage().getIdLanguage().equals( this.language.getId() ) )
          {
            region.setName( regionLanguageDO.getDsName() );
            break;
          }
        }
        to.getRegions().add( region );
      }
    }
  }
}
