package mx.com.cinepolis.digital.booking.commons.query;


/**
 * Enumeration for criteria query for teh entity {@link mx.com.cinepolis.digital.booking.model.EventDO}
 * 
 * @author gsegura
 */
public enum EventQuery implements ModelQuery
{
  EVENT_ID("idEvent"), EVENT_NAME("dsName"), EVENT_TYPE_ID("idEventType"), EVENT_ACTIVE("fgActive"), EVENT_PREMIERE(
      "fgPremiere"), EVENT_LANGUAGE_ID("idLanguage"), EVENT_MOVIE_DISTRIBUTOR_ID("idDistributor"), EVENT_CODE_DBS(
      "dsCodeDbs"), EVENT_MOVIE_ID("idEventMovie"), EVENT_PRERELEASE("fgPrerelease"), EVENT_CURRENT_MOVIE(
      "currentMovie"), EVENT_FESTIVAL("fgFestival"),EVENT_ID_VISTA("idVista"), EVENT_ALTERNATE_CONTENT("fgAlternateContent");

  private String query;

  private EventQuery( String query )
  {
    this.query = query;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getQuery()
  {
    return query;
  }

}
