package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.DistributorTO;
import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;
import mx.com.cinepolis.digital.booking.commons.to.EventTO;
import mx.com.cinepolis.digital.booking.model.EventMovieDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.EventMovieDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.EventMovieTO}
 * 
 * @author shernandezl
 *
 */
public class EventMovieDOToEventMovieTOTransformer implements Transformer
{
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    EventMovieTO eventMovieTO = null;
    
    if(object instanceof EventMovieDO)
    {
      EventMovieDO eventMovieDO = (EventMovieDO) object;
      eventMovieTO = new EventMovieTO();
      
      EventTO eventTO = (EventTO) new EventDOToEventTOTransformer().transform( eventMovieDO.getIdEvent() );
      eventMovieTO.setIdEvent( eventTO.getIdEvent() );
      eventMovieTO.setIdVista( eventTO.getIdVista() );
      eventMovieTO.setQtCopy( eventTO.getQtCopy() );
      eventMovieTO.setCodeDBS( eventTO.getCodeDBS() );
      eventMovieTO.setDsEventName( eventTO.getDsEventName() );
      eventMovieTO.setDuration( eventTO.getDuration() );
      eventMovieTO.setFgActive( eventTO.isFgActive() );
      eventMovieTO.setTimestamp( eventTO.getTimestamp() );
      eventMovieTO.setUserId( eventTO.getUserId() );
      eventMovieTO.setPremiere( eventTO.isPremiere() );
      eventMovieTO.setSoundFormats( eventTO.getSoundFormats() );
      eventMovieTO.setMovieFormats( eventTO.getMovieFormats() );
      
      eventMovieTO.setIdEventMovie( eventMovieDO.getIdEventMovie() );
      eventMovieTO.setDsCountry( eventMovieDO.getDsCountry() );
      eventMovieTO.setDistributor( (DistributorTO) new DistributorDOToDistributorTOTransformer()
          .transform( eventMovieDO.getIdDistributor() ) );
      eventMovieTO.getDistributor().setName( eventMovieDO.getIdDistributor().getDsName() );

      eventMovieTO.setDsActor( eventMovieDO.getDsActor() );
      eventMovieTO.setDsDirector( eventMovieDO.getDsDirector() );
      eventMovieTO.setDsGenre( eventMovieDO.getDsGenre() );
      eventMovieTO.setDsRating( eventMovieDO.getDsRating() );
      eventMovieTO.setDsSynopsis( eventMovieDO.getDsSynopsis() );
      eventMovieTO.setDsUrl( eventMovieDO.getDsUrl() );
      eventMovieTO.setDtRelease( eventMovieDO.getDtRelease() );
      eventMovieTO.setDsOriginalName( eventMovieDO.getDsOriginalName() );

      if( eventMovieDO.getIdMovieImage() != null )
      {
        eventMovieTO.setIdMovieImage( eventMovieDO.getIdMovieImage() );
      }
    }
    return eventMovieTO;
  }

}
