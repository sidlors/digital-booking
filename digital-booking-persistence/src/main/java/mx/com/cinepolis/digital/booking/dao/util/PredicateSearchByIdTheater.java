package mx.com.cinepolis.digital.booking.dao.util;

import mx.com.cinepolis.digital.booking.commons.to.BookingTO;

import org.apache.commons.collections.Predicate;

/**
 * Predicate class thata evaluates if a the theater id of a given booking equals a
 * {@link mx.com.cinepolis.digital.booking.dao.util.PredicateSearchByIdTheater.idTheater}
 * 
 * @author gsegura
 */
public class PredicateSearchByIdTheater implements Predicate
{
  private Long idTheater;

  /**
   * Constructor by idTheater
   * 
   * @param idTheater
   */
  public PredicateSearchByIdTheater( Long idTheater )
  {
    this.idTheater = idTheater;
  }

  @Override
  public boolean evaluate( Object arg0 )
  {
    boolean isEvaluated = false;
    Long idTheaterTmp = null;
    if( arg0 instanceof BookingTO )
    {
      idTheaterTmp = ((BookingTO) arg0).getTheater().getId();
      if( idTheater.equals( idTheaterTmp ) )
      {
        isEvaluated = true;
      }
    }
    return isEvaluated;
  }
}
