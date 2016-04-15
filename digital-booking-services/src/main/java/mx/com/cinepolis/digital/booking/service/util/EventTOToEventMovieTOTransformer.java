package mx.com.cinepolis.digital.booking.service.util;

import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.commons.to.EventTO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.EventMovieTO}
 * 
 * @author afuentes
 */
public class EventTOToEventMovieTOTransformer implements Transformer
{

  /**
   * {@inheritDoc}
   */
  @Override
  public Object transform( Object object )
  {
    EventMovieTO eventMovieTO = null;
    if( object instanceof EventMovieTO )
    {
      eventMovieTO = (EventMovieTO) object;
    }
    return eventMovieTO;
  }

}
