package mx.com.cinepolis.digital.booking.service.util;

import java.io.Serializable;
import java.util.Comparator;

import mx.com.cinepolis.digital.booking.commons.to.EventMovieTO;

import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * Compares the exhibition weeks of two {@link mx.com.cinepolis.digital.booking.commons.to.EventMovieTO}
 * 
 * @author gsegura
 */
public class EventMovieComparatorByExhibitionWeeks implements Comparator<EventMovieTO>, Serializable
{

  private static final long serialVersionUID = -4944960498756757863L;

  /**
   * {@inheritDoc}
   */
  @Override
  public int compare( EventMovieTO movie1, EventMovieTO movie2 )
  {
    return new CompareToBuilder().append( movie2.getExhibitionWeeks(), movie1.getExhibitionWeeks() )
        .append( movie1.getDsEventName(), movie2.getDsEventName() ).toComparison();
  }

}
