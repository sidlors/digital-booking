package mx.com.cinepolis.digital.booking.dao.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import mx.com.cinepolis.digital.booking.commons.to.WeekTO;
import mx.com.cinepolis.digital.booking.commons.utils.CinepolisUtils;
import mx.com.cinepolis.digital.booking.model.WeekDO;

import org.apache.commons.collections.Transformer;

/**
 * Transforms a {@link mx.com.cinepolis.digital.booking.model.WeekDO} to a
 * {@link mx.com.cinepolis.digital.booking.commons.to.WeekTO }
 * 
 * @author gsegura
 * @since 0.2.0
 */
public class WeekDOToWeekTOTransformer implements Transformer
{
  private static final String PREFIX_SPECIAL_WEEK = "(*) ";

  private static final String DATE_FORMAT = "dd/MM/yyyy";

  private boolean activeWeek;
  private DateFormat formatter;

  /**
   * Constructor default
   */
  public WeekDOToWeekTOTransformer()
  {
    this.activeWeek = false;
    formatter = new SimpleDateFormat( DATE_FORMAT );
  }

  /**
   * Constructor by activeWeek
   * 
   * @param activeWeek
   */
  public WeekDOToWeekTOTransformer( boolean activeWeek )
  {
    this.activeWeek = activeWeek;
    formatter = new SimpleDateFormat( DATE_FORMAT );
  }

  @Override
  public Object transform( Object object )
  {
    WeekTO to = null;
    if( object instanceof WeekDO )
    {
      to = new WeekTO();
      to.setNuWeek( ((WeekDO) object).getNuWeek() );
      to.setNuYear( ((WeekDO) object).getNuYear() );
      to.setFgActive( ((WeekDO) object).isFgActive() );
      to.setFinalDayWeek( ((WeekDO) object).getDtFinalDayWeek() );
      to.setIdWeek( ((WeekDO) object).getIdWeek() );
      to.setSpecialWeek( ((WeekDO) object).isFgSpecialWeek() );
      to.setStartingDayWeek( ((WeekDO) object).getDtStartingDayWeek() );
      to.setTimestamp( ((WeekDO) object).getDtLastModification() );
      to.setUserId( Long.valueOf( ((WeekDO) object).getIdLastUserModifier() ) );
      to.setActiveWeek( this.activeWeek );
      if( ((WeekDO) object).isFgSpecialWeek() )
      {
        to.setWeekDescription( to.getStartingDayWeek() != null ? CinepolisUtils.buildStringUsingMutable(
          PREFIX_SPECIAL_WEEK, formatter.format( to.getStartingDayWeek() ) ) : null );
      }
      else
      {
        to.setWeekDescription( to.getStartingDayWeek() != null ? formatter.format( to.getStartingDayWeek() ) : null );
      }

    }
    return to;
  }

}
