package mx.com.cinepolis.digital.booking.commons.query;


/**
 * Enumeration for criteria query for teh entity {@link mx.com.cinepolis.digital.booking.model.RegionDO}
 * 
 * @author gsegura
 * @since 0.0.1
 */
public enum RegionQuery implements ModelQuery
{
  REGION_ID("idRegion"), REGION_NAME("dsName"), REGION_LANGUAGE_ID("idLanguage"), TERRITORY_ID("idTerritory"), TERRITORY_NAME(
      "dsName"), REGION_ACTIVE("fgActive"), REGION_ID_USER("idUser");

  private String query;

  private RegionQuery( String query )
  {
    this.query = query;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getQuery()
  {
    // TODO Auto-generated method stub
    return query;
  }

}
