package mx.com.cinepolis.digital.booking.commons.query;


/**
 * Enumeration for criteria query for the entity {@link mx.com.cinepolis.digital.booking.model.TheaterDO}
 * 
 * @author agustin.ramirez
 * @since 0.0.1
 */
public enum TheaterQuery implements ModelQuery
{
  ID_CITY("idCity"), THEATER_ID("idTheater"), THEATER_NAME("dsName"), THEATER_ID_LANGUAGE("idLanguage"), ID_REGION(
      "idRegion"), REGION_NAME("dsName"), ID_STATE("idState"), THEATER_ACTIVE("fgActive"),  ID_USER("idUser"), THEATER_ID_VISTA("idVista");
  /**
   * Query Attribute
   */
  private String query;

  /**
   * Constructor
   * 
   * @param query
   */
  private TheaterQuery( String query )
  {
    this.query = query;
  }

  /*
   * (non-Javadoc)
   * @see mx.com.cinepolis.digital.booking.model.query.ModelQuery#getQuery()
   */
  @Override
  public String getQuery()
  {

    return query;
  }

}
