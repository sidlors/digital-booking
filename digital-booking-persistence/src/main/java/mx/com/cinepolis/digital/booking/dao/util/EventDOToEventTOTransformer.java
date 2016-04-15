package mx.com.cinepolis.digital.booking.dao.util;

import java.util.ArrayList;

import mx.com.cinepolis.digital.booking.commons.constants.CategoryType;
import mx.com.cinepolis.digital.booking.commons.constants.Language;
import mx.com.cinepolis.digital.booking.commons.to.CatalogTO;
import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.model.CategoryDO;
import mx.com.cinepolis.digital.booking.model.CategoryLanguageDO;
import mx.com.cinepolis.digital.booking.model.EventDO;
import mx.com.cinepolis.digital.booking.model.EventMovieDO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.PredicateUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.BooleanUtils;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.EventDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.EventTO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public class EventDOToEventTOTransformer implements Transformer
{

  private Language language;

  /**
   * Constructor default
   */
  public EventDOToEventTOTransformer()
  {
    this.language = Language.ENGLISH;
  }

  /**
   * Constructor by language
   * 
   * @param language
   */
  public EventDOToEventTOTransformer( Language language )
  {
    this.language = language;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    EventTO to = null;

    if( object instanceof EventDO )
    {
      EventDO eventDO = (EventDO) object;
      if( CollectionUtils.isNotEmpty( eventDO.getEventMovieDOList() ) )
      {
        to = extractMovieData( eventDO );
      }
      else
      {
        to = new EventTO();
      }

      to.setIdEvent( eventDO.getIdEvent() );
      to.setIdVista( eventDO.getIdVista() );
      to.setQtCopy( eventDO.getQtCopy() );
      to.setCodeDBS( eventDO.getDsCodeDbs() );
      to.setDsEventName( eventDO.getDsName() );
      to.setDuration( eventDO.getQtDuration() );
      to.setFgActive( eventDO.isFgActive() );
      to.setTimestamp( eventDO.getDtLastModification() );
      to.setUserId( Long.valueOf( eventDO.getIdLastUserModifier() ) );
      to.setPremiere( BooleanUtils.isTrue( eventDO.getFgPremiere() ) );
      to.setCurrentMovie( eventDO.isCurrentMovie() );
      to.setPrerelease( eventDO.isFgPrerelease() );
      to.setFestival( eventDO.isFgFestival() );
      to.setFgActiveIa( eventDO.isFgActiveIa());
      to.setFgAlternateContent( eventDO.isFgAlternateContent() );
      extractCategories( eventDO, to );
    }
    return to;
  }

  private void extractCategories( EventDO eventDO, EventTO eventTO )
  {
    eventTO.setSoundFormats( new ArrayList<CatalogTO>() );
    eventTO.setMovieFormats( new ArrayList<CatalogTO>() );
    for( CategoryDO categoryDO : eventDO.getCategoryDOList() )
    {
      CatalogTO category = new CatalogTO( categoryDO.getIdCategory().longValue() );
      for( CategoryLanguageDO categoryLanguageDO : categoryDO.getCategoryLanguageDOList() )
      {
        if( categoryLanguageDO.getIdLanguage().getIdLanguage().equals( language.getId() ) )
        {
          category.setName( categoryLanguageDO.getDsName() );
        }
      }
      if( categoryDO.getIdCategoryType().getIdCategoryType().equals( CategoryType.SOUND_FORMAT.getId() ) )
      {
        eventTO.getSoundFormats().add( category );
      }
      else
      {
        eventTO.getMovieFormats().add( category );
      }
    }
  }

  private EventTO extractMovieData( EventDO eventDO )
  {
    EventTO to = new EventMovieTO();
    EventMovieDO eventMovieDO = (EventMovieDO) CollectionUtils.find( eventDO.getEventMovieDOList(),
      PredicateUtils.notNullPredicate() );
    ((EventMovieTO) to).setIdEventMovie( eventMovieDO.getIdEventMovie() );
    ((EventMovieTO) to).setDsCountry( eventMovieDO.getDsCountry() );
    ((EventMovieTO) to).setDistributor( (DistributorTO) new DistributorDOToDistributorTOTransformer()
        .transform( eventMovieDO.getIdDistributor() ) );
    ((EventMovieTO) to).getDistributor().setName( eventMovieDO.getIdDistributor().getDsName() );
    
    ((EventMovieTO) to).setDsActor( eventMovieDO.getDsActor() );
    ((EventMovieTO) to).setDsDirector( eventMovieDO.getDsDirector() );
    ((EventMovieTO) to).setDsGenre( eventMovieDO.getDsGenre() );
    ((EventMovieTO) to).setDsRating( eventMovieDO.getDsRating() );
    ((EventMovieTO) to).setDsSynopsis( eventMovieDO.getDsSynopsis() );
    ((EventMovieTO) to).setDsUrl( eventMovieDO.getDsUrl() );
    ((EventMovieTO) to).setDtRelease( eventMovieDO.getDtRelease() );
    ((EventMovieTO) to).setDsOriginalName( eventMovieDO.getDsOriginalName() );

    if( eventMovieDO.getIdMovieImage() != null )
    {
      ((EventMovieTO) to).setIdMovieImage( eventMovieDO.getIdMovieImage() );
    }

    return to;
  }

}
