package mx.com.cinepolis.digital.booking.commons.query;


/**
 * Enumeration for criteria query for teh entity {@link mx.com.cinepolis.digital.booking.model.WeekDO}
 * 
 * @author gsegura
 * @since 0.2.0
 */
public enum WeekQuery implements ModelQuery
{
  WEEK_ID("idWeek"), WEEK_NUM("nuWeek"), WEEK_YEAR("nuYear"), WEEK_DAY("weekDay"), 
  WEEK_START("dtStartingDayWeek"), WEEK_END("dtFinalDayWeek"), SPECIAL_WEEK("fgSpecialWeek"), WEEK_ACTIVE("fgActive");

  private String query;

  private WeekQuery( String query )
  {
    this.query = query;
  }

  /**
   * 
   */
  @Override
  public String getQuery()
  {
    return query;
  }

}
