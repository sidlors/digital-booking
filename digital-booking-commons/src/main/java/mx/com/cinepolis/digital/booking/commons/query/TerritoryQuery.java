package mx.com.cinepolis.digital.booking.commons.query;


/**
 * Enumeration for criteria query for teh entity {@link mx.com.cinepolis.digital.booking.model.TerritoryDO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public enum TerritoryQuery implements ModelQuery
{
  TERRITORY_ID("idTerritory"), TERRITORY_LANGUAGE_ID("idLanguage"), TERRITORY_NAME("dsName");
  private String query;

  private TerritoryQuery( String query )
  {
    this.query = query;
  }

  /**
   * @return the query
   */
  @Override
  public String getQuery()
  {
    return query;
  }
}
